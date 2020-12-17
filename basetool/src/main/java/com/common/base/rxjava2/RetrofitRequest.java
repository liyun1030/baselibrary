package com.common.base.rxjava2;

import com.common.base.bean.UserLoginReModel;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RetrofitRequest {
    // 填上需要访问的服务器地址
    public static String HOST = "http://192.168.1.222/";

    @POST("szhiqu/uapi/login/loginByPsw")
    Observable<RetrofitResponse<JavaBean>> login(@Body UserLoginReModel model);
}
