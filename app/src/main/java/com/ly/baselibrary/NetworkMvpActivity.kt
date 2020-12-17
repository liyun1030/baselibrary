package com.ly.baselibrary

import com.common.base.activity.BaseActivity
import com.common.base.bean.StudentHomeRoleModel
import com.common.base.bean.ThirdLoginByResModel
import com.common.base.network.CommonBusiness
import com.common.base.rxjava2.BaseContract
import com.common.base.rxjava2.BaseMvpModel
import com.common.base.rxjava2.Exception.ApiException
import com.common.base.rxjava2.presenter.BasePresenter
import com.common.base.rxjava2.schedulers.SchedulerProvider
import com.common.base.tool.CommUtils
import kotlinx.android.synthetic.main.activity_network.*

/**
 * 网络请求示例
 */
class NetworkMvpActivity : BaseActivity(), CommonBusiness.UserListener, BaseContract.View {
    val STUDENT_HOME_MODEL_INDEX = "http://192.168.1.222/szhiqu/app/index/icon"
    val LOGIN_LOGIN_BY_ACCOUNT = "http://192.168.1.222/szhiqu/uapi/login/loginByPsw"
    var commonBusiness: CommonBusiness? = null
    private var presenter: BasePresenter? = null
    override fun getLayoutId(): Int {
        return R.layout.activity_network
    }

    override fun init() {
        commonBusiness = CommonBusiness.getInstance()
        presenter = BasePresenter(BaseMvpModel(), this, SchedulerProvider.getInstance())
        postBtn.setOnClickListener {
            presenter?.login()
        }
        getBtn.setOnClickListener {
            commonBusiness?.userOperation(
                this@NetworkMvpActivity,
                null,
                STUDENT_HOME_MODEL_INDEX,
                1,
                this@NetworkMvpActivity
            )
        }
    }

    override fun loginSucc(model: ThirdLoginByResModel?) {
        CommUtils.showToast(this, model?.data?.username)
    }

    override fun getHomeIndex(model: StudentHomeRoleModel?) {
        CommUtils.showToast(this, model?.data?.get(0)?.page)
    }

    override fun fail(msg: String?) {
    }

    override fun getDataSuccess() {
        CommUtils.showToast(this, "succ")
    }

    override fun getDataFail(api: ApiException) {
        CommUtils.showToast(this, api.code.toString()+","+api.message)
    }

}