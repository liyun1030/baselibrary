package com.curefun.pbl.fragment

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Handler
import android.os.Message
import android.text.TextUtils
import android.view.View
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.common.base.fragment.BaseFragment
import com.common.base.network.BaseApiUrl
import com.common.base.network.TokenInterceptor
import com.common.base.okgo.OkgoUtil
import com.hwangjr.rxbus.RxBus
import com.hwangjr.rxbus.annotation.Subscribe
import com.hwangjr.rxbus.annotation.Tag
import com.ly.baselibrary.R
import kotlinx.android.synthetic.main.fragment_rob_order.*
import java.util.*

/**
 * created by 李云 on 2019/6/18
 * 本类的作用:抢单
 */
class RobOrderFragment : BaseFragment(){

    var handler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_rob_order
    }

    override fun initView() {
        RxBus.get().register(this)
        swiperereshlayout!!.setSize(SwipeRefreshLayout.DEFAULT)
        swiperereshlayout!!.setOnRefreshListener {
            // 启动刷新的控件
            swiperereshlayout!!.post {
                // 设置是否开始刷新,true为刷新，false为停止刷新
                swiperereshlayout!!.isRefreshing = true
                onResume()
            }
        }
        //        if (Build.VERSION.SDK_INT >= 23) {
        //            swiperereshlayout.setProgressBackgroundColorSchemeColor(
        //                    getResources().getColor(android.R.color.holo_orange_light));
        //            swiperereshlayout.setColorSchemeColors(//刷新控件动画中的颜色
        //                    getResources().getColor(android.R.color.holo_blue_dark),
        //                    getResources().getColor(android.R.color.holo_red_dark),
        //                    getResources().getColor(android.R.color.holo_green_dark)
        //            );
        //        }
    }

    override fun initData() {

    }

    override fun onResume() {
        super.onResume()
    }


    private fun unRegistJpush() {
        val maps = HashMap<String, Any>()
        OkgoUtil.get(null, BaseApiUrl.HOST_URL, maps, object : OkgoUtil.ResponseListener {
            override fun success(response: String) {}
            override fun failure(errorResponse: String) {}
        })
    }
    companion object {
        private val REQUEST_CODE_SCAN = 0x0000
        private val DECODED_CONTENT_KEY = "codedContent"
        private val DECODED_BITMAP_KEY = "codedBitmap"
    }
}
