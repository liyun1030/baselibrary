package com.ly.baselibrary.mvvm

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.common.base.bean.UserLoginReModel
import com.common.base.mvvm.MvvmRetrofitFactory
import com.common.base.mvvm.dataConvert
import com.common.base.rxjava2.RetrofitRequest
import com.common.base.tool.CommUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NetViewModel : ViewModel() {
    var fictions = MutableLiveData<List<Fiction>>()
    fun getFictions() {
        /*viewModelScope是一个绑定到当前viewModel的作用域  当ViewModel被清除时会自动取消该作用域，所以不用担心内存泄漏为问题*/
        viewModelScope.launch {
            try {
                /*withContext表示挂起块  配合Retrofit声明的suspend函数执行 该块会挂起直到里面的网络请求完成 最一行就是返回值*/
                val data = withContext(Dispatchers.IO) {
                    /*dataConvert扩展函数可以很方便的解析出我们想要的数据  接口很多的情况下下可以节省不少无用代码*/
                    val userLoginReModel = UserLoginReModel()
                    userLoginReModel.account = "19000000001"
                    userLoginReModel.password = "000001"
                    MvvmRetrofitFactory.instance.getService(ApiService::class.java)
                        .login(userLoginReModel).dataConvert()
                }
                /*给LiveData赋值  ui会自动更新*/
                fictions.value = listOf(data)

            } catch (e: Exception) {
                /*请求异常的话在这里处理*/
                e.printStackTrace()
                fictions.value = listOf()
                Log.i("请求失败", "${e.message}")
            }
        }
    }
}