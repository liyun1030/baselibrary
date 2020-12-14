package com.common.base.network.config;

import android.os.Build;
import android.text.TextUtils;

import com.common.base.BuildConfig;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.ArrayList;
import java.util.List;

import okhttp3.CookieJar;
import okhttp3.Interceptor;
import okhttp3.JavaNetCookieJar;

/**
 * 网络配置
 * Created by Yan Kai on 2016/3/18.
 */
public class NetworkManagerConfiguration {

    String userAgent;
    long connectTimeout;
    long writeTimeout;
    long readTimeout;
    CookieJar cookieJar;
    List<Interceptor> interceptorList;

    public NetworkManagerConfiguration(Builder builder) {
        userAgent = builder.userAgent;
        connectTimeout = builder.connectTimeout;
        writeTimeout = builder.writeTimeout;
        readTimeout = builder.readTimeout;
        cookieJar = builder.cookieJar;
        interceptorList = builder.interceptorList;
    }


    public static class Builder {
        private String userAgent;
        private long connectTimeout = 10;
        private long writeTimeout = 60;
        private long readTimeout = 20;
        private CookieJar cookieJar;
        private List<Interceptor> interceptorList = new ArrayList<Interceptor>();

        public void setUserAgent(String appName, String appVersionName) {
            userAgent = encodeHeadInfo(appName + "/" + appVersionName + " (Android; " + Build.VERSION.RELEASE + "; " + Build.MANUFACTURER + "/" + Build.MODEL + " )");
        }

        public void connectTimeout(long connectTimeout) {
            this.connectTimeout = connectTimeout;
        }

        public void writeTimeout(long writeTimeout) {
            this.writeTimeout = writeTimeout;
        }

        public void readTimeout(long readTimeout) {
            this.readTimeout = readTimeout;
        }

        public void cookieJar(CookieJar cookieJar) {
            this.cookieJar = cookieJar;
        }

        public void addInterceptor(Interceptor interceptor) {
            if(interceptor != null) {
                this.interceptorList.add(interceptor);
            }
        }


        public NetworkManagerConfiguration build() {
            initEmptyFieldsWithDefaultValues();
            return new NetworkManagerConfiguration(this);
        }

        private void initEmptyFieldsWithDefaultValues() {
            if (null == cookieJar) {
                CookieHandler cookieHandler = new CookieManager(null, CookiePolicy.ACCEPT_ALL);
                cookieJar = new JavaNetCookieJar(cookieHandler);
            }
            if (TextUtils.isEmpty(userAgent)) {
                setUserAgent("Curefun", BuildConfig.VERSION_NAME);
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

}
