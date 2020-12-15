package com.common.base.network;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.common.base.activity.AppManager;
import com.common.base.bean.BaseBean;
import com.common.base.tool.AtyContainer;
import com.common.base.tool.SharedPreferencesUtils;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.security.MessageDigest;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.internal.http.HttpHeaders;
import okio.Buffer;
import okio.BufferedSource;
import okio.Okio;

/**
 * token安全策略
 */

public class TokenInterceptor implements Interceptor {
    public static final boolean STOKENINTERCEPTOR = true;
    private static final Charset UTF8 = Charset.forName("UTF-8");
    private static final String TAG = "TokenInterceptor";
    public static final String SHARE_KEY_TOKEN = "share_key_token";
    public static final String SP_UID = "uid";
    public static final String SP_NAME = "sp_token";
    private static TokenInterceptor sInstance;
    private static Context context;
    public static final String ORG_ID = "url_o_id";
    public static final String H_ID = "url_h_id";
    public static final String R_ID = "url_r_id";
    private TokenInterceptor(Context context) {
        this.context = context;
    }

    public static TokenInterceptor getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new TokenInterceptor(context);
        }
        return sInstance;
    }

    /**
     * 登录token
     */
    private static String sRequestToken;
    private static String uid;
    private static final String[] UNCHECK_URLS =
            {
//                    "/resource/" ,                                  //资源文件
//                    "articleDetail.html" ,                         //资源文件
//                    "zhiqu_front/www/student.html",               //资源文件
//                    "/user/register/pcEmail",                      //邮箱注册
//                    "/userinfo/send/email",                        //邮箱找回密码
//                    "/send/authcode",                               //手机或邮箱找回密码
//                    "/userinfo/send/email",                        //邮箱找回密码
//                    "/userinfo/check/password",                    //检查手机号和验证码
//                    "/userinfo/submit/password",                   //重置密码(找回密码)
//                    "/user/register/phone/vercode",                //获取注册短信验证码
//                    "/userinfo/findpassword",                       //获取找回密码短信验证码
//                    "/user/register/lphone/userinfo",               //手机注册
//                    "/user/register/easyRegister",                 //手机快速注册
//                    "/user/register/phone/newuserinfo",            //注册新账户
//                    "/user/register/phone/send/authcode",           //注册时发送手机验证码信息
//                    "/usersafety/validate/account"                 //验证验证码
            };

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        String srcUrl = request.url().toString();
        int spliteIndex = srcUrl.indexOf("/", 9);
        int lastSpliteIndex = srcUrl.lastIndexOf("?") == -1 ? srcUrl.length() : srcUrl.lastIndexOf("?");

        String ipportStr = srcUrl.substring(0, spliteIndex);
        String intefaceStr = srcUrl.substring(spliteIndex, lastSpliteIndex);
        String paramStr = srcUrl.substring(lastSpliteIndex);


        String tokenUrl = withToken(ipportStr, intefaceStr, paramStr);
        if (isUnCheckUrl(srcUrl)) {
            return chain.proceed(request);
        }
        String userAgent = encodeHeadInfo("yjt" + "/ (Android; " + Build.VERSION.RELEASE + "; " + Build.MANUFACTURER + "/" + Build.MODEL + " )");
        Request tokenRequest = request.newBuilder().removeHeader("Accept-Encoding").url(tokenUrl).removeHeader("User-Agent").addHeader("User-Agent",
                userAgent).
                build();

        Response response = chain.proceed(tokenRequest).newBuilder().build();

        ResponseBody responseBody = response.body();
        long contentLength = responseBody.contentLength();

        if (!HttpHeaders.hasBody(response)) {
        }
//        else if (bodyEncoded(response.headers())) {
//            //HTTP (encoded body omitted)
//        }
        else {
            BufferedSource source = responseBody.source();
            source.request(Long.MAX_VALUE); // Buffer the entire body.
            Buffer buffer = source.buffer();

            Charset charset = UTF8;
            MediaType contentType = responseBody.contentType();
            if (contentType != null) {
                try {
                    charset = contentType.charset(UTF8);
                } catch (UnsupportedCharsetException e) {
                    //Couldn't decode the response body; charset is likely malformed.
                    return response;
                }
            }

            if (!isPlaintext(buffer)) {
                return response;
            }
            if (contentLength != 0) {
                String result = buffer.clone().readString(charset);
                handlerTokenResponse(result);
            }

        }
        return response;
    }

    static boolean isPlaintext(Buffer buffer) throws EOFException {
        try {
            Buffer prefix = new Buffer();
            long byteCount = buffer.size() < 64 ? buffer.size() : 64;
            buffer.copyTo(prefix, 0, byteCount);
            for (int i = 0; i < 16; i++) {
                if (prefix.exhausted()) {
                    break;
                }
                int codePoint = prefix.readUtf8CodePoint();
                if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                    return false;
                }
            }
            return true;
        } catch (EOFException e) {
            return false; // Truncated UTF-8 sequence.
        }
    }


    private boolean bodyEncoded(Headers headers) {
        String contentEncoding = headers.get("Content-Encoding");
        return contentEncoding != null && !contentEncoding.equalsIgnoreCase("identity");
    }

    public boolean handlerTokenResponse(String bodyStr) {
        try {
            BaseBean b = JSON.parseObject(bodyStr, BaseBean.class);
            if (TextUtils.equals(b.getCode(), ResponseCode.RESPONSE_STATUS_TOKENARGSERROR)
                    || TextUtils.equals(b.getCode(), ResponseCode.RESPONSE_STATUS_SIGNERROR)
                    || TextUtils.equals(b.getCode(), ResponseCode.RESPONSE_STATUS_TOKENFORMATERROR)
                    || TextUtils.equals(b.getCode(), ResponseCode.RESPONSE_STATUS_TOKENWRONG)) {
                toast(ResponseCode.RESPONSE_STATUS_TOKENWRONG_MSG);
                loginOut();
                return true;
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void loginOut() {
        ACache mCache = ACache.get(context);
        mCache.clear();
        TokenInterceptor.setsRequestToken(null);
        SharedPreferencesUtils.getInstance(context, TokenInterceptor.SP_NAME).remove(TokenInterceptor.SHARE_KEY_TOKEN);
        SharedPreferencesUtils.getInstance(context, Constant.SP_NAME).remove(Constant.SP_USER_LOGIN_KEY);
        TokenInterceptor.setsRequestToken("");
        SharedPreferencesUtils.getInstance(context, Constant.SP_NAME).remove(Constant.SP_USER_DETAIL_KEY);
        SharedPreferencesUtils.getInstance(context, Constant.SP_NAME).clear();
        AtyContainer.getInstance().finishAllActivity();
        AppManager.getAppManager().finishAllActivity();
        Intent intent=new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction("com.doctor.user.login");
        context.startActivity(intent);

    }


    private boolean isUnCheckUrl(String url) {
        for (String u : UNCHECK_URLS) {
            if (url.indexOf(u) > 0) {
                return true;
            }
        }
        return false;
    }

    private void toast(final String msg) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                // 此处执行UI操作
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 设置请求token
     */
    public static void setsRequestToken(String token) {
        SharedPreferencesUtils.getInstance(context, SP_NAME).putString(SHARE_KEY_TOKEN, token == null ? "" : token);
        sRequestToken = TextUtils.isEmpty(token) ? "" : token;
    }
    public static void setsUid(String uid) {
        SharedPreferencesUtils.getInstance(context, SP_NAME).putString(SP_UID, uid == null ? "" : uid);
        uid = TextUtils.isEmpty(uid) ? "" : uid;
    }
    public static String getUid() {
        uid = SharedPreferencesUtils.getInstance(context, SP_NAME).getString(SP_UID, "");
        return uid;
    }
    public static String getsRequestToken() {
        if (TextUtils.isEmpty(sRequestToken)) {
            sRequestToken = SharedPreferencesUtils.getInstance(context, SP_NAME).getString(SHARE_KEY_TOKEN, "");
        }
        return sRequestToken;
    }
    public static String getsRequestOrgId() {
        return SharedPreferencesUtils.getInstance(context, SP_NAME).getString(ORG_ID, "");
    }

    public static String getsRequestHid() {
        return SharedPreferencesUtils.getInstance(context, SP_NAME).getString(H_ID, "");
    }
    public static String withToken(String srcUrl) {
        int spliteIndex = srcUrl.indexOf("/", 9);
        int lastSpliteIndex = srcUrl.lastIndexOf("?") == -1 ? srcUrl.length() : srcUrl.lastIndexOf("?");

        String ipportStr = srcUrl.substring(0, spliteIndex);
        String intefaceStr = srcUrl.substring(spliteIndex, lastSpliteIndex);
        String paramStr = srcUrl.substring(lastSpliteIndex);
        return withToken(ipportStr, intefaceStr, paramStr);
    }

    public static String withToken(String ipPortStr, String intefaceStr, String paramStr) {
        if (intefaceStr.endsWith("/")) {
            intefaceStr = intefaceStr.substring(0, intefaceStr.length() - 1);
        }
        long t = System.currentTimeMillis();
        String tHex = Long.toHexString(t);
        String signStr = "yjt"+ tHex  +intefaceStr;
        String sign = MD5(signStr);
        String resultUrl = "";
        resultUrl += ipPortStr;
        resultUrl += intefaceStr;
        resultUrl += String.format("?sign=%s", sign);
        resultUrl += String.format("&device_type=%d", 1);    //1代表终端
        resultUrl += String.format("&t=%d", t);
        resultUrl += String.format("&token=%s", getsRequestToken());
        resultUrl += String.format("&url_o_id=%s", getsRequestOrgId());
        resultUrl += String.format("&url_h_id=%s", getsRequestHid());
        if (!TextUtils.isEmpty(paramStr)) {
            resultUrl += paramStr.replace("?", "&");
        }
        return resultUrl;
    }
    public final static String MD5(String s) {
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            byte[] btInput = s.getBytes();

            MessageDigest mdInst = MessageDigest.getInstance("MD5");

            mdInst.update(btInput);

            byte[] md = mdInst.digest();

            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    private static class ForwardingResponseBody extends ResponseBody {
        private final ResponseBody mBody;
        private final BufferedSource mInterceptedSource;

        public ForwardingResponseBody(ResponseBody body, InputStream interceptedStream) {
            this.mBody = body;
            this.mInterceptedSource = Okio.buffer(Okio.source(interceptedStream));
        }

        public MediaType contentType() {
            return this.mBody.contentType();
        }

        public long contentLength() {
            return this.mBody.contentLength();
        }

        public BufferedSource source() {
            return this.mInterceptedSource;
        }
    }

    /**
     * okhttp验证head编码，需要剔除异常字符，避免报错
     *
     * @param headInfo
     * @return
     */
    private static String encodeHeadInfo(String headInfo) {
        StringBuilder stringBuffer = new StringBuilder();
        for (int i = 0, length = headInfo.length(); i < length; i++) {
            char c = headInfo.charAt(i);
            if (c <= '\u001f' || c >= '\u007f') {
                stringBuffer.append(String.format("\\u%04x", (int) c));
            } else {
                stringBuffer.append(c);
            }
        }
        return stringBuffer.toString();
    }

}
