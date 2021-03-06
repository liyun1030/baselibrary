package com.common.base.rxjava2;

import com.common.base.network.TokenInterceptor;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @description: retrofit请求工具类
 */

public class RetrofitUtil {
    private static OkHttpClient mOkHttpClient;
    public static final int TIMEOUT = 60;//超时时间
    private static volatile RetrofitRequest request = null;
    Retrofit mRetrofit = null;
    /**
     * 超时时间
     */
    private static volatile RetrofitUtil mInstance;

    /**
     * 单例封装
     *
     * @return
     */
    public static RetrofitUtil getInstance() {
        if (mInstance == null) {
            synchronized (RetrofitUtil.class) {
                if (mInstance == null) {
                    mInstance = new RetrofitUtil();
                }
            }
        }
        return mInstance;
    }

    /**
     * 初始化Retrofit
     */
    public void init() {
        mRetrofit = new Retrofit.Builder()
                .client(initOKHttp())
                // 设置请求的域名
                .baseUrl(RetrofitRequest.HOST)
                // 设置解析转换工厂，用自己定义的
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    /**
     * 全局httpclient
     *
     * @return
     */
    public static OkHttpClient initOKHttp() {
        if (mOkHttpClient == null) {
            mOkHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(TIMEOUT, TimeUnit.SECONDS)//设置连接超时时间
                    .readTimeout(TIMEOUT, TimeUnit.SECONDS)//设置读取超时时间
                    .writeTimeout(TIMEOUT, TimeUnit.SECONDS)//设置写入超时时间
                    .addInterceptor(InterceptorUtil.LogInterceptor())//添加日志拦截器
                    .addInterceptor(TokenInterceptor.getInstance(null))
                    //cookie
                    .addInterceptor(new CookieReadInterceptor())
                    .addInterceptor(new CookiesSaveInterceptor())
                    .build();
        }
        return mOkHttpClient;
    }

    public RetrofitRequest getRequest() {
        if (request == null) {
            synchronized (RetrofitRequest.class) {
                request = mRetrofit.create(RetrofitRequest.class);
            }
        }
        return request;
    }
}
