package com.ly.baselibrary.mvp2.demo;


import com.ly.baselibrary.mvp2.demo.model.DynamicBean;
import com.ly.baselibrary.mvp2.demo.model.UserBean;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * @author jiang zhu on 2019/11/23
 */
public interface RetrofitService {
    @POST("user/getLogin")
    Observable<UserBean> getLogin(@Query("data") String data);

    @GET("dynamic/getDynamics")
    Observable<DynamicBean> getDynamic(@Query("data") String data);

}
