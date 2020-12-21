package com.ly.baselibrary.mvp2.demo;


import com.ly.baselibrary.mvp2.demo.model.DynamicBean;
import com.ly.baselibrary.mvp2.demo.model.UserBean;

import io.reactivex.Observable;

/**
 * 该类用来管理RetrofitService中对应的各种API接口
 * 当做Retrofit和presenter中的桥梁，Activity就不用直接和retrofit打交道了
 */

public class DataManager {

    private RetrofitService mRetrofitService;

    private DataManager(){
        this.mRetrofitService = RetrofitHelper.getInstance().getServer();
    }

    //由于该对象会被频繁调用，采用单例模式，下面是一种线程安全模式的单例写法，构造方法已被private修饰
    private volatile static DataManager instance;
    public static DataManager getInstance() {
        if (instance == null) {
            synchronized (DataManager.class) {
                if (instance == null) {
                    instance = new DataManager();
                }
            }
        }
        return instance;
    }


    // 将retrofit的业务方法映射到DataManager中，以后统一用该类来调用业务方法
    // 以后在RetrofitService中增加业务方法的时候，相应的这里也要添加一个方法，建议方法名字相一致
    public Observable<UserBean> getLogin(String data){
        return mRetrofitService.getLogin(data);
    }

    public Observable<DynamicBean> getDynamic(String data) {
        return mRetrofitService.getDynamic(data);
    }
}
