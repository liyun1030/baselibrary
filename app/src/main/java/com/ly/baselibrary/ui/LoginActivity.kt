package com.ly.baselibrary.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import com.common.base.activity.BaseActivity
import com.common.base.application.BaseApplication
import com.common.base.network.CommonBusiness
import com.common.base.network.Constant
import com.common.base.tool.AndroidUtil
import com.common.base.tool.CommUtils
import com.ly.baselibrary.R
import com.ly.baselibrary.application.MyApplication
import com.ly.baselibrary.model.UserInfoModel
import kotlinx.android.synthetic.main.activity_org_login.*

/**
 *本类的作用:登录
 */
class LoginActivity : BaseActivity(), TextWatcher{
    var isVisible: Boolean? = false
    var commonBusiness: CommonBusiness? = null
    val HIDE_LOADING = 1
    val LOGIN_FAILURE = 2
    val MODEL_NULL = 3
    val KEY_FAILURE = "failureInfo"
    override fun getLayoutId(): Int {
        return R.layout.activity_org_login
    }

    override fun init() {
        commonBusiness = CommonBusiness.getInstance()
        loginTxt.isEnabled = true
        accountTxt.setOnEditorActionListener { textView: TextView, actionId: Int?, keyEvent: KeyEvent? ->
            if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_UNSPECIFIED) {
                login()
                true
            } else {
                false
            }
        }
        accountTxt.addTextChangedListener(this)
        pwdTxt.addTextChangedListener(this)
        clearImg.setOnClickListener {
            accountTxt.setText(null)
            pwdTxt.setText(null)
        }
        if (!isVisible!!) {
            visibleImg.setImageResource(R.mipmap.ic_pwd_hide)
            pwdTxt.transformationMethod = HideReturnsTransformationMethod.getInstance()
        } else {
            visibleImg.setImageResource(R.mipmap.ic_pwd_show)
            pwdTxt.transformationMethod = PasswordTransformationMethod.getInstance()
        }
        pwdTxt.transformationMethod = PasswordTransformationMethod.getInstance()
        visibleImg.setOnClickListener {
            if (!isVisible!!) {
                //显示密码
                visibleImg.setImageResource(R.mipmap.ic_pwd_show)
                //如果被选中则显示密码
                pwdTxt.transformationMethod = HideReturnsTransformationMethod.getInstance()
                //TextView默认光标在最左端，这里控制光标在最右端
                if (!TextUtils.isEmpty(pwdTxt.text)) {
                    pwdTxt.setSelection(pwdTxt.getText().length)
                }
            } else {
                //隐藏密码
                visibleImg.setImageResource(R.mipmap.ic_pwd_hide)
                //如果没选中CheckBox则隐藏密码
                pwdTxt.transformationMethod = PasswordTransformationMethod.getInstance()
                if (!TextUtils.isEmpty(pwdTxt.text)) {
                    pwdTxt.setSelection(pwdTxt.getText().length)
                }
            }
            isVisible = !isVisible!!
        }
        forgetTxt.setOnClickListener {
            //忘记密码
        }
        loginTxt.setOnClickListener {
            login()
        }
        protocolLayout.setOnClickListener {
            //用户协议
        }
        privateLayout.setOnClickListener {
            //隐私政策
        }
    }

    @Synchronized
    private fun login() {
        var phoneTxt = accountTxt.text.toString().trim()
        var pwd = pwdTxt.text.toString().trim()
        if (TextUtils.isEmpty(phoneTxt)) {
            CommUtils.showToast(this, "手机号不能为空!")
            return
        }
        if (TextUtils.isEmpty(pwd)) {
            CommUtils.showToast(this, "密码不能为空!")
            return
        }
        if (pwd.length < 6) {
            CommUtils.showToast(this, R.string.please_input_at_least_six_byte_psw)
            return
        }
        if (pwd.length >16) {
            CommUtils.showToast(this, "密码长度不能超过16位,请重新输入!")
            return
        }
        CommUtils.hideKeyboard(accountTxt)
        val hasNetwork = (this.application as BaseApplication).isHasNetwork
        if (!hasNetwork) {
            val msg = handler!!.obtainMessage()
            msg.what = LOGIN_FAILURE
            val bundle = Bundle()
            bundle.putString(KEY_FAILURE, this.resources.getString(R.string.error_network))
            msg.data = bundle
            handler!!.sendMessageDelayed(msg, 500)
        } else {
            showLoading()
            accountTxt.postDelayed({
                handler.sendEmptyMessageDelayed(HIDE_LOADING, 500)
            },1000)
        }
    }

    val handler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            if (HIDE_LOADING == msg.what) {
                loginSuccess()
            } else if (LOGIN_FAILURE == msg.what) {
                loginFailure(msg.data.getString(KEY_FAILURE))
            } else if (MODEL_NULL == msg.what) {
                modelNull()
            }
        }
    }
    private fun loginFailure(msg: String) {
        CommUtils.showToast(this, msg)
        hideLoading()
    }
    private fun loginSuccess() {
        hideLoading()
        var userInfoModel=UserInfoModel()
        userInfoModel.phone=accountTxt.text.toString().trim()
        userInfoModel.pwd=pwdTxt.text.toString().trim()
        MyApplication.getInstance().spInstance.setObject(Constant.SP_USER_KEY, userInfoModel)
        val intent = Intent(this, HomeActivity::class.java)
        AndroidUtil.startActivity(this, intent, true)
    }



    /**
     * 登录失败
     */
    private fun modelNull() {
        hideLoading()
        CommUtils.showToast(applicationContext, R.string.login_failed)
    }

    override fun afterTextChanged(s: Editable?) {
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        val account = accountTxt.text.toString().trim()
        val pwd = pwdTxt.text.toString().trim()
        if (TextUtils.isEmpty(account)) {
            clearImg.setVisibility(View.INVISIBLE)
        } else {
            clearImg.setVisibility(View.VISIBLE)
        }
    }
}