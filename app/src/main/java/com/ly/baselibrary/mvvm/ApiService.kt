package com.ly.baselibrary.mvvm

import com.common.base.bean.BaseBean
import com.common.base.bean.UserLoginReModel
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("szhiqu/uapi/login/loginByPsw")
    suspend fun login(@Body model: UserLoginReModel?): BaseBean<Fiction>
}