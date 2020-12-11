package com.ly.baselibrary

import android.util.Base64
import com.common.base.activity.BaseActivity
import com.common.base.bean.BaseModel
import com.common.base.bean.StudentHomeRoleModel
import com.common.base.bean.ThirdLoginByResModel
import com.common.base.bean.UserLoginReModel
import com.common.base.network.CommonBusiness
import com.common.base.tool.CommUtils
import kotlinx.android.synthetic.main.activity_network.*

/**
 * 网络请求示例
 */
class NetworkActivity : BaseActivity(), CommonBusiness.UserListener {
    val STUDENT_HOME_MODEL_INDEX = "http://192.168.1.222/szhiqu/app/index/icon"
    val LOGIN_LOGIN_BY_ACCOUNT = "http://192.168.1.222/szhiqu/uapi/login/loginByPsw"
    var commonBusiness: CommonBusiness? = null
    override fun getLayoutId(): Int {
        return R.layout.activity_network
    }

    override fun init() {
        commonBusiness = CommonBusiness.getInstance()
        postBtn.setOnClickListener {
            val model = UserLoginReModel()
            model.setAccount("19000000001")
            model.setPassword(
                CommUtils.replaceBlankEnter(
                    Base64.encodeToString(
                        "000001".toByteArray(),
                        Base64.DEFAULT
                    )
                )
            )
            commonBusiness?.userOperation(this@NetworkActivity,model,LOGIN_LOGIN_BY_ACCOUNT,2,this@NetworkActivity)
        }
        getBtn.setOnClickListener {
            commonBusiness?.userOperation(this@NetworkActivity,null,STUDENT_HOME_MODEL_INDEX,1,this@NetworkActivity)
        }
    }

    override fun loginSucc(model: ThirdLoginByResModel?) {
        CommUtils.showToast(this,model?.data?.username)
    }

    override fun getHomeIndex(model: StudentHomeRoleModel?) {
        CommUtils.showToast(this,model?.data?.get(0)?.page)
    }

    override fun fail(msg: String?) {
        TODO("Not yet implemented")
    }

}