package com.ly.baselibrary.ui


import android.content.Intent
import com.common.base.activity.BaseActivity
import com.common.base.network.Constant
import com.common.base.tool.AndroidUtil
import com.common.base.tool.CommUtils
import com.ly.baselibrary.MainActivity
import com.ly.baselibrary.R
import com.ly.baselibrary.application.MyApplication
import com.ly.baselibrary.model.UserInfoModel
import kotlinx.android.synthetic.main.activity_welcome.*

class WelcomActivity : BaseActivity() {
    var model: UserInfoModel? = null
    override fun getLayoutId(): Int {
        return R.layout.activity_welcome
    }
    override fun init() {
        model = MyApplication.getInstance().spInstance.getObject(Constant.SP_USER_KEY, UserInfoModel::class.java)
        versionTxt!!.text = "V " + CommUtils.getMetaInfo(this, "VERSION_NAME")
        versionTxt.postDelayed({
            AndroidUtil.openActivity(this@WelcomActivity, Intent(this@WelcomActivity, MainActivity::class.java), true)
//            if (model != null) {
//                AndroidUtil.openActivity(this@WelcomActivity, Intent(this@WelcomActivity, HomeActivity::class.java), true)
//            } else {
//                AndroidUtil.openActivity(this@WelcomActivity, Intent(this@WelcomActivity, LoginActivity::class.java), true)
//            }
        }, 1000)
    }
}
