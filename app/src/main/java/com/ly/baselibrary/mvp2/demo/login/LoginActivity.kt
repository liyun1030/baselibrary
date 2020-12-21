package com.ly.baselibrary.mvp2.demo.dynamic;
import android.content.Intent
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import com.common.base.rxjava2.activity.BaseMvpActivity
import com.common.base.tool.CommUtils
import com.google.gson.Gson
import com.ly.baselibrary.MainActivity
import com.ly.baselibrary.R
import com.ly.baselibrary.mvp2.demo.login.LoginPresenter
import com.ly.baselibrary.mvp2.demo.login.LoginView
import com.ly.baselibrary.mvp2.demo.model.UserBean
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : BaseMvpActivity<LoginView, LoginPresenter>(),
    LoginView, View.OnClickListener {

    override fun createPresenter(): LoginPresenter {
        return LoginPresenter()
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_login
    }

    override fun initViews() {
        loginBtnLogin.setOnClickListener(this)
    }

    override fun onSuccess(mUser: UserBean?) {
        if (mUser != null) {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    override fun showProgressDialog() {
        runOnUiThread {
            loginBtnLoading.visibility = View.VISIBLE
        }
    }

    override fun hideProgressDialog() {
        loginBtnLoading.visibility = View.GONE
    }

    override fun onError(result: String?) {
        CommUtils.showToast(this,result)
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
                R.id.loginBtnLogin -> {
                    submit()
                }
            }
        }
    }

    private fun submit() {
        // validate
        val username = loginEtUsername.text.toString().trim { it <= ' ' }
        if (TextUtils.isEmpty(username)) {
            Toast.makeText(this, "账号不能为空", Toast.LENGTH_SHORT).show()
            return
        }

        val password = loginEtPassword.text.toString().trim { it <= ' ' }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show()
            return
        }

        // TODO validate success, do something
        val hashMap = java.util.LinkedHashMap<String, String>()
        hashMap["account"] = username
        hashMap["password"] = password
        val gao = Gson()
        val data = gao.toJson(hashMap)
        presenter.getLogin(data)

        Toast.makeText(this, "账号：${username}   密码：${password}", Toast.LENGTH_SHORT).show()

    }


}
