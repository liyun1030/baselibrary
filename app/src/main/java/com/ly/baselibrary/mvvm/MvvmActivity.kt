package com.ly.baselibrary.mvvm

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.common.base.tool.AndroidUtil
import com.common.base.tool.CommUtils
import com.google.gson.Gson
import com.ly.baselibrary.R
import kotlinx.android.synthetic.main.activity_main_mvvm.*

class MvvmActivity : AppCompatActivity() {
    private lateinit var netViewModel: NetViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_mvvm)
        /*创建viewmodel*/
        netViewModel = ViewModelProviders.of(this).get(NetViewModel::class.java)

        btn.setOnClickListener {
            /*请求数据*/
            netViewModel.getFictions()
        }

        /*数据发生变化时更新ui*/
        netViewModel.fictions.observe(this, Observer {
            if(AndroidUtil.isListEmpty(it)){
                CommUtils.showToast(this,"请求失败")
            }else {
                tv.text = Gson().toJson(it)
            }
        })
    }
}
