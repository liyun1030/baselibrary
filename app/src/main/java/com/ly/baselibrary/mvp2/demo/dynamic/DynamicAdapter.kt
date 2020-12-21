package com.ly.baselibrary.mvp2.demo.dynamic

import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.common.base.adapter.BaseRecyclerAdapter
import com.common.base.adapter.RecyclerHolder
import com.ly.baselibrary.R
import com.ly.baselibrary.mvp2.demo.model.DynamicBean
import java.util.*

class DynamicAdapter(v: RecyclerView?,datas: MutableCollection<DynamicBean.RowsBean>?,itemLayoutId: Int) : BaseRecyclerAdapter<DynamicBean.RowsBean>(v,datas,itemLayoutId) {
    override fun convert(holder: RecyclerHolder?, item: DynamicBean.RowsBean?, position: Int) {
        val dynamicAdTvName = holder?.getView<TextView>(R.id.dynamicAdTvName)
        val dynamicAdIvSet = holder?.getView<TextView>(R.id.dynamicAdIvSet)
        val dynamicAdTvDate = holder?.getView<TextView>(R.id.dynamicAdTvDate)
        val dynamicAdTvContent = holder?.getView<TextView>(R.id.dynamicAdTvContent)
        val dynamicAdIvImage = holder?.getView<MultiImageView>(R.id.dynamicAdIvImage)
        val dynamicAdLlItem = holder?.getView<LinearLayout>(R.id.dynamicAdLlItem)
        dynamicAdIvSet?.setOnClickListener {
            //deleteDynamic(t?.did,viewType)
        }
        if (item?.urlList!!.size > 0) {
            dynamicAdIvImage?.setList(item.urlList)
            val arrayList = ArrayList(item.urlList)
        }
        dynamicAdTvName?.text = item.name
        dynamicAdTvDate?.text = "123"
        dynamicAdTvContent?.text = item.content
    }

}
