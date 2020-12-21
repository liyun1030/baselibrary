package com.ly.baselibrary.mvp2.demo;

import com.common.base.rxjava2.RetrofitRequest;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitHelper {

    // 声明Retrofit对象
    private Retrofit mRetrofit = null;
    OkHttpClient client = new OkHttpClient();

    // 由于该对象会被频繁调用，采用单例模式，下面是一种线程安全模式的单例写法，构造方法已被private修饰
    private volatile static RetrofitHelper instance = null;
    public static RetrofitHelper getInstance(){
        if (instance == null){
            synchronized (RetrofitHelper.class) {
                if(instance == null){
                    instance = new RetrofitHelper();
                }
            }
        }
        return instance;
    }

    private RetrofitHelper(){
        initRetrofit();
    }

    /**
     * 初始化 retrofit
     */
    private void initRetrofit() {
        mRetrofit = new Retrofit.Builder()
                .baseUrl(RetrofitRequest.HOST)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    public RetrofitService getServer(){
        return mRetrofit.create(RetrofitService.class);
    }


}
