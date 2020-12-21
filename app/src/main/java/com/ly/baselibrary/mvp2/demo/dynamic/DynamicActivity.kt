package com.ly.baselibrary.mvp2.demo.dynamic;
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.common.base.rxjava2.activity.BaseMvpActivity
import com.common.base.tool.CommUtils
import com.google.gson.Gson
import com.ly.baselibrary.R
import com.ly.baselibrary.mvp2.demo.model.DynamicBean
import kotlinx.android.synthetic.main.activity_dynamic.*
import java.util.*

@Suppress("NAME_SHADOWING")
class DynamicActivity : BaseMvpActivity<DynamicView, DynamicPresenter>(), DynamicView {

    private lateinit var dynamicAdapter: DynamicAdapter

    override fun getLayoutId(): Int {
        return R.layout.activity_dynamic
    }

    override fun initViews() {
        getDynamic(1)
        //为RecyclerView添加配置
        val llm = LinearLayoutManager(this)
        dynamicRv.layoutManager = llm
    }

    private fun getDynamic(page: Int, keyword: String) {
        val hashMap = LinkedHashMap<String, String>()
        hashMap["page"] = page.toString()
        hashMap["keyword"] = keyword
        val gao = Gson()
        val data = gao.toJson(hashMap)
        presenter.getDynamic(data)
    }

    private fun getDynamic(page: Int) {
        getDynamic(page, "")
    }

    override fun onSuccess(mDynamic: DynamicBean?) {
        if (mDynamic != null) {
            dynamicAdapter = DynamicAdapter(dynamicRv, mDynamic.rows, R.layout.adapter_dynamic_list)
            dynamicRv.adapter = dynamicAdapter
        }
    }

    override fun showProgressDialog() {
        runOnUiThread { dynamicPb.visibility = View.VISIBLE }
    }

    override fun hideProgressDialog() {
        dynamicPb.visibility = View.GONE
    }

    override fun onError(result: String?) {
        CommUtils.showToast(this, result)
    }

    override fun createPresenter(): DynamicPresenter {
        return DynamicPresenter()
    }

}
