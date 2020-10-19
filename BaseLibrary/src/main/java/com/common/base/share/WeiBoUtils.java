package com.common.base.share;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.common.base.R;
import com.common.base.tools.CommUtils;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;


/**
 * 新浪微博分享
 */
public class WeiBoUtils {

    public AuthInfo mAuthInfo;

    private static Context context;

    private static OnWbLoginListener listener;

    private static SsoHandler mSsoHandler;

    public WeiBoUtils(Context context, OnWbLoginListener listener) {
        this.context = context;
        this.listener = listener;
        mAuthInfo = new AuthInfo(context, ShareConstants.WB_APP_ID, ShareConstants.WB_REDIRECT_URL, "");
    }

    public static void wbLogin(Context context, OnWbLoginListener listener) {
        WeiBoUtils wxUtils = new WeiBoUtils(context, listener);
        mSsoHandler = new SsoHandler((Activity) context, wxUtils.mAuthInfo);
        mSsoHandler.authorize(new AuthListener());
    }

    /**
     * 登入按钮的监听器，接收授权结果。
     */
    private static class AuthListener implements WeiboAuthListener {
        @Override
        public void onComplete(Bundle values) {
            Oauth2AccessToken accessToken = Oauth2AccessToken.parseAccessToken(values);
            if (accessToken != null && accessToken.isSessionValid()) {
                String accessTokenToken = accessToken.getToken();
                listener.onWbLoginSuccess(accessTokenToken);
//                UsersAPI mUsersAPI = new UsersAPI(context, ShareConstants.WB_APP_ID, accessToken);
//                long uid = Long.parseLong(accessToken.getUid());
//                mUsersAPI.show(uid, mListener);
            }
        }

        @Override
        public void onWeiboException(WeiboException e) {
            CommUtils.showToast(context, R.string.access_fail);
        }

        @Override
        public void onCancel() {
            CommUtils.showToast(context, R.string.access_fail);
        }
    }


    public static void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (mSsoHandler != null) {
            mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }

    /**
     * 微博 OpenAPI 回调接口。
     */
    private static RequestListener mListener = new RequestListener() {
        @Override
        public void onComplete(String response) {
            if (!TextUtils.isEmpty(response)) {
                listener.onWbLoginSuccess(response);
            }
        }

        @Override
        public void onWeiboException(WeiboException e) {
            CommUtils.showToast(context, R.string.access_fail);
        }
    };

    public interface OnWbLoginListener{
        void onWbLoginSuccess(String response);
    }
}
