package com.curefun.pbl.fragment

import android.os.Bundle
import com.common.base.fragment.BaseFragment
import com.curefun.pbl.model.bean.TabInfoBean
import com.ly.baselibrary.R


/**
 * created by 李云 on 2019/6/18
 * 本类的作用:下线
 */
class OfflineFragment : BaseFragment() {
    override fun getLayoutId(): Int {
        return R.layout.fragment_offline
    }

    private var mTitle: String? = null
    private var titles: ArrayList<TabInfoBean.Tab> = ArrayList()
    private var tabBeans: ArrayList<TabInfoBean> = ArrayList()

    /**
     * 存放 tab 标题
     */
    companion object {
        fun getInstance(title: String): OfflineFragment {
            val fragment = OfflineFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            fragment.mTitle = title
            return fragment
        }
    }


    override fun initData() {
        init()
    }

    override fun initView() {

    }

    fun init() {

    }

}

