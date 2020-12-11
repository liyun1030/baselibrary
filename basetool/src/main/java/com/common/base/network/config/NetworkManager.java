package com.common.base.network.config;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.common.base.callback.DialogCallback;
import com.common.base.tool.CommUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.HttpHeaders;
import com.lzy.okgo.request.GetRequest;
import com.lzy.okgo.request.PostRequest;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * 网络管理类 关联网络框架  网络请求核心类子工程都是使用这个
 */
public class NetworkManager {
    private static final String TAG = "NetworkManager";


    private static NetworkManager mInstance;

    private String mUserAgent;

    private OkHttpClient mOkHttpClient;
    private Handler mDelivery;

    private NetworkManagerConfiguration networkManagerConfiguration;

    public static NetworkManager getInstance() {
        if (null == mInstance) {
            synchronized (NetworkManager.class) {
                if (null == mInstance) {
                    mInstance = new NetworkManager();
                }
            }
        }
        return mInstance;
    }

    /**
     * get请求
     *
     * @param url      请求url
     * @param callback 请求回调
     */
    public static void get(String url, int flag, Headers.Builder headerBuilder, Header[] headers, ResultCallback callback, Context context) {
//        getInstance().getRequest(url, flag, callback, headerBuilder,context);
        // TODO: 2020/3/12
        getInstance().okgoGet(url, flag, callback, headerBuilder,headers,context);
        // TODO: 2020/3/12
    }

    private  void okgoGet(String url, int flag, ResultCallback callback, Headers.Builder headerBuilder, Header[] headers, Context context) {
        initOkGo();
        GetRequest<String> getRequest = OkGo.get(url);
        getRequest.tag(flag);
        HttpHeaders okgoheaders=new HttpHeaders();
        if (headers != null&&headers.length>0) {
            for (Header header : headers) {
                okgoheaders.put(header.getName(), header.getValue());
            }
        }
        okgoheaders.put("User-Agent", mUserAgent);
        getRequest.headers(okgoheaders);
        requetNetData(callback,getRequest,null, context);

    }

    /**
     * post请求
     *
     * @param url      请求url
     * @param callback 请求回调
     */
    public static void post(String url, int flag, Headers.Builder headerBuilder, Header[] headers, String content, final ResultCallback callback, Context context) {
//        getInstance().postRequest(url, flag, callback, content, headerBuilder,context);
        // TODO: 2020/3/13
        getInstance().okgoPost(url, flag, callback, content, headerBuilder,headers, context);

    }

    private void okgoPost(String url, int flag, ResultCallback callback, String content, Headers.Builder headerBuilder, Header[] headers, Context context) {
        //-----------
        initOkGo();
        PostRequest<String> postRequest = OkGo.<String>post(url);
        postRequest.tag(flag);
        HttpHeaders okgoheaders=new HttpHeaders();
        if (headers != null&&headers.length>0) {
            for (Header header : headers) {
                okgoheaders.put(header.getName(), header.getValue());
            }
        }
        okgoheaders.put("User-Agent", mUserAgent);
        postRequest.headers(okgoheaders);
        postRequest.upRequestBody(RequestBody.create(MediaType.parse("application/json"), content==null?"":content));
        requetNetData(callback,null,postRequest, context);
    }

    private void requetNetData(final ResultCallback callback, final GetRequest<String> getRequest, PostRequest<String> postRequest, final Context ctx) {
        DialogCallback clallback=new DialogCallback<String>(ctx) {
            @Override
            public void onSuccess(com.lzy.okgo.model.Response<String> response) {
                if (ctx!=null){
                    Activity activity = CommUtils.getActivityFromView(ctx);
                    if ((activity != null && activity.isFinishing())) return;
                }
                if (!response.isSuccessful()) {
                    sendFailCallback((int) response.getRawCall().request().tag(), callback, new TimeoutException());
                    return;
                }
                String contentType = response.headers().get("Content-Type");
                if (null == contentType) {
                    return;
                }
                if (contentType.contains("image")) {
                    try {
                        sendSuccessCallBack((int) response.getRawCall().request().tag(), callback, response.getRawResponse().body().bytes());
                    } catch (Exception e) {
                        e.printStackTrace();
                        sendFailCallback((int) response.getRawCall().request().tag(), callback, new TimeoutException());
                    }
                } else if (contentType.contains("application/json")) {
                    try {
                        String str = response.body();
                        //LogUtils.i("System.out","responsebody="+str);
                        if (callback.clazz == String.class) {
                            sendSuccessCallBack((int) response.getRawCall().request().tag(), callback, str);
                        } else {
                            Object object = JSON.parseObject(str, callback.clazz);
                            sendSuccessCallBack((int) response.getRawCall().request().tag(), callback, object);
                        }
                    } catch (final Exception e) {
                        Log.e(TAG, "convert json failure", e);
                        sendFailCallback((int) response.getRawCall().request().tag(), callback, e);
                    }
                }


            }

            @Override
            public void onError(com.lzy.okgo.model.Response<String> response) {
                if (ctx!=null){
                    Activity activity = CommUtils.getActivityFromView(ctx);
                    if ((activity != null && activity.isFinishing())) return;
                }
                sendFailCallback((int) response.getRawCall().request().tag(), callback, (Exception)response.getException());
            }
        };
        if (getRequest!=null)
            getRequest.execute(clallback);
        if (postRequest!=null)
            postRequest.execute(clallback);
    }

    private void initOkGo() {
        OkHttpClient httpClient = getHttpClient();
        OkGo.getInstance().setOkHttpClient(httpClient);
    }



    public static void form(String url, int flag, Headers.Builder headerBuilder, MultipartBody.Builder multipartBuilder, final ResultCallback callback, Context context) {
        getInstance().formRequest(url, flag, callback, multipartBuilder, headerBuilder,context);
    }

    public static String form(String url, int flag, Headers.Builder headerBuilder, MultipartBody.Builder multipartBuilder, final ResultCallback callback, Context context, boolean issync) {
        return getInstance().formRequest(url, flag, callback, multipartBuilder, headerBuilder, context, issync);
    }

    private NetworkManager() {
        mDelivery = new Handler(Looper.getMainLooper());
    }

    /**
     * okhttp主要使用配置项 Token相关校验等
     */
    public void config(NetworkManagerConfiguration networkManagerConfiguration) {
        if (null == networkManagerConfiguration) {
            throw new NullPointerException("NetworkManagerConfiguration can't be null");
        }
        this.networkManagerConfiguration = networkManagerConfiguration;
    }

    /**
     *okhttp初始化使用
     */
    private OkHttpClient getHttpClient() {
        if (null == mOkHttpClient) {
            mUserAgent = networkManagerConfiguration.userAgent;
            mOkHttpClient = new OkHttpClient();
            HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor();
            logInterceptor.setLevel(HttpLoggingInterceptor.Level.NONE);
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.connectTimeout(networkManagerConfiguration.connectTimeout, TimeUnit.SECONDS);
            builder.writeTimeout(networkManagerConfiguration.writeTimeout, TimeUnit.SECONDS);
            builder.readTimeout(networkManagerConfiguration.readTimeout, TimeUnit.SECONDS);
            builder.cookieJar(networkManagerConfiguration.cookieJar);
            if (networkManagerConfiguration.interceptorList != null && networkManagerConfiguration.interceptorList.size() > 0) {
                for (Interceptor i : networkManagerConfiguration.interceptorList) {
                    builder.addNetworkInterceptor(i);
                }
            }
            builder.addNetworkInterceptor(logInterceptor);
            mOkHttpClient = builder.build();
        }
        return mOkHttpClient;
    }

    private void getRequest(String url, int flag, final ResultCallback callback, Headers.Builder headerBuilder, Context context) {
        getHttpClient();
        if (null == headerBuilder) {
            headerBuilder = new Headers.Builder();
        }
        headerBuilder.add("User-Agent", mUserAgent);
        final Request request = new Request.Builder().url(url).tag(flag).headers(headerBuilder.build()).build();
        deliveryResult(callback, request,context);
    }

    private void postRequest(String url, int flag, final ResultCallback callback, String json, Headers.Builder headerBuilder, Context context) {
        getHttpClient();
        if (null == headerBuilder) {
            headerBuilder = new Headers.Builder();
        }
        headerBuilder.add("User-Agent", mUserAgent);
        if (null == json) {
            json = "";
        }
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), json);
        final Request request = new Request.Builder().url(url).tag(flag).post(requestBody).headers(headerBuilder.build()).build();
        deliveryResult(callback, request, context);
    }

    private void formRequest(String url, int flag, final ResultCallback callback, MultipartBody.Builder multipartBuilder, Headers.Builder headerBuilder, Context context) {
        getHttpClient();
        if (null == headerBuilder) {
            headerBuilder = new Headers.Builder();
        }
        headerBuilder.add("User-Agent", mUserAgent);
        final Request request = new Request.Builder().url(url).tag(flag).post(multipartBuilder.build()).headers(headerBuilder.build()).build();
        deliveryResult(callback, request, context);
    }

    private String formRequest(String url, int flag, final ResultCallback callback, MultipartBody.Builder multipartBuilder, Headers.Builder headerBuilder, Context context, boolean issync) {
        getHttpClient();
        if (null == headerBuilder) {
            headerBuilder = new Headers.Builder();
        }
        headerBuilder.add("User-Agent", mUserAgent);
        final Request request = new Request.Builder().url(url).tag(flag).post(multipartBuilder.build()).headers(headerBuilder.build()).build();
//        deliveryResult(callback, request, context);
        if (issync){
            Call call = getHttpClient().newCall(request);
            try {
                Response response = call.execute();
                return response.body().string();
//                System.out.println(response.body().string());
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }

    private void deliveryResult(final ResultCallback callback, final Request request, final Context context) {
//        LogUtils.i("System.out","url="+request.url());
        getHttpClient().newCall(request).enqueue(new Callback() {
                                                     @Override
                                                     public void onFailure(Call call, IOException e) {
                                                         if (context!=null){
                                                             Activity activity = CommUtils.getActivityFromView(context);
                                                             if ((activity != null && activity.isFinishing())) return;
                                                         }
                                                         sendFailCallback((int) call.request().tag(), callback, e);
                                                     }

                                                     @Override
                                                     public void onResponse(Call call, Response response) throws IOException {
                                                         if (context!=null){
                                                             Activity activity = CommUtils.getActivityFromView(context);
                                                             if ((activity != null && activity.isFinishing())) return;
                                                         }
                                                         if (!response.isSuccessful()) {
                                                             sendFailCallback((int) response.request().tag(), callback, new TimeoutException());
                                                             return;
                                                         }

                                                         String contentType = response.headers().get("Content-Type");
                                                         if (null == contentType) {
                                                             return;
                                                         }
                                                         if (contentType.contains("image")) {
                                                             try {
                                                                 sendSuccessCallBack((int) response.request().tag(), callback, response.body().bytes());
                                                             } catch (Exception e) {
                                                                 e.printStackTrace();
                                                                 sendFailCallback((int) response.request().tag(), callback, new TimeoutException());
                                                             }
                                                         } else if (contentType.contains("application/json")) {
                                                             try {
                                                                 String str = response.body().string();
//                                                                 LogUtils.i("System.out","responsebody="+str);
                                                                 if (callback.clazz == String.class) {
                                                                     sendSuccessCallBack((int) response.request().tag(), callback, str);
                                                                 } else {
                                                                     Object object = JSON.parseObject(str, callback.clazz);
                                                                     sendSuccessCallBack((int) response.request().tag(), callback, object);
                                                                 }
                                                             } catch (final Exception e) {
                                                                 Log.e(TAG, "convert json failure", e);
                                                                 sendFailCallback((int) response.request().tag(), callback, e);
                                                             }
                                                         }
                                                     }
                                                 }

        );
    }


    private void sendFailCallback(final int tag, final ResultCallback callback, final Exception e) {
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                if (null != callback) {
                    callback.onFailure(tag, e);
                }
            }
        });
    }

    private <T> void sendSuccessCallBack(final int tag, final ResultCallback<T> callback, final T obj) {
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                if (null != callback) {
                    callback.onSuccess(tag, obj);
                }
            }
        });
    }

    public static void cancelTag(Object tag) {
        List<Call> queuedCalls = getInstance().getHttpClient().dispatcher().queuedCalls();
        for (Call call : queuedCalls) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
        List<Call> runningCalls = getInstance().getHttpClient().dispatcher().runningCalls();
        for (Call call : runningCalls) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
    }

    public static void cancelAll() {
        getInstance().getHttpClient().dispatcher().cancelAll();
    }

    /**
     * 自定义网络请求回调泛型基类
     * @param <T>
     */
    public static abstract class ResultCallback<T> {

        protected Class<T> clazz;

        public ResultCallback() {
            Class clazz = getClass();
            //通过反射获取clazz的具体类型
            while (clazz != Object.class) {
                Type t = clazz.getGenericSuperclass();
                if (t instanceof ParameterizedType) {
                    Type[] args = ((ParameterizedType) t).getActualTypeArguments();
                    if (args[0] instanceof Class) {
                        this.clazz = (Class<T>) args[0];
                        break;
                    }
                }
                clazz = clazz.getSuperclass();
            }
        }

        /**
         * 请求成功回调
         *
         * @param flag
         * @param response
         */
        public abstract void onSuccess(int flag, T response);

        /**
         * 请求失败回调
         *
         * @param flag
         * @param e
         */
        public abstract void onFailure(int flag, Exception e);
    }

    /**
     * 项目子工程的callback都是实现这个类(约定返回格式一般都是string类型)
     */
    public static abstract class ResultStringCallback extends ResultCallback<String> {

        public ResultStringCallback() {
            super();
        }
    }

    /**
     * 返回格式是byte[]的处理
     */
    public static abstract class ResultByteCallback extends ResultCallback<byte[]> {

        public ResultByteCallback() {
            super();
        }
    }


    //===================同步执行方法==================

    public static <T> T syncGet(Class<T> clazz, String url, int flag, Headers.Builder headerBuilder) {
        return getInstance().getSyncRequest(clazz, url, flag, headerBuilder);
    }

    public static <T> T syncPost(Class<T> clazz, String url, int flag, String content, Headers.Builder headerBuilder) {
        return getInstance().postSyncRequest(clazz, url, flag, content, headerBuilder);
    }

    private <T> T getSyncRequest(Class<T> clazz, String url, int flag, Headers.Builder headerBuilder) {
        getHttpClient();
        if (null == headerBuilder) {
            headerBuilder = new Headers.Builder();
        }
        headerBuilder.add("User-Agent", mUserAgent);
        final Request request = new Request.Builder().url(url).tag(flag).headers(headerBuilder.build()).build();
        return syncExecute(clazz, request);
    }

    private <T> T postSyncRequest(Class<T> clazz, String url, int flag, String json, Headers.Builder headerBuilder) {
        getHttpClient();
        if (null == headerBuilder) {
            headerBuilder = new Headers.Builder();
        }
        headerBuilder.add("User-Agent", mUserAgent);
        if (null == json) {
            json = "";
        }
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), json);
        final Request request = new Request.Builder().url(url).tag(flag).post(requestBody).headers(headerBuilder.build()).build();
        return syncExecute(clazz, request);
    }

    private <T> T syncExecute(Class<T> clazz, Request request) {
        try {
            Response response = getHttpClient().newCall(request).execute();
            if (!response.isSuccessful()) {
                return null;
            }

            String contentType = response.headers().get("Content-Type");
            if (null == contentType) {
                return null;
            }
            if (contentType.contains("application/json")) {
                try {
                    String str = response.body().string();
                    if (clazz == String.class) {
                        return (T) str;
                    } else {
                        return JSON.parseObject(str, clazz);
                    }
                } catch (final Exception e) {
                    Log.e(TAG, "convert json failure", e);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
