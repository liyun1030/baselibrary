package com.common.base.rxjava2;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * : 读取cookie
 */

public class CookieReadInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder builder = chain.request().newBuilder();
//        builder.addHeader("Cookie", SharePreferencesUtils.getString(MyApp.myApp, "cookiess", ""));
        return chain.proceed(builder.build());
    }
}
