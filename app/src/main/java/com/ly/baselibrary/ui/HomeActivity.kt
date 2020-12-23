package com.ly.baselibrary.ui

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.KeyEvent
import androidx.fragment.app.FragmentTransaction
import com.basely.permission.util.PermissionUtil
import com.common.base.activity.BaseActivity
import com.common.base.tool.AndroidUtil
import com.common.base.tool.CommUtils
import com.curefun.pbl.fragment.CreateOrderFragment
import com.curefun.pbl.fragment.OfflineFragment
import com.curefun.pbl.fragment.RobOrderFragment
import com.flyco.tablayout.listener.CustomTabEntity
import com.flyco.tablayout.listener.OnTabSelectListener
import com.hazz.kotlinmvp.mvp.model.bean.TabEntity
import com.ly.baselibrary.R
import kotlinx.android.synthetic.main.activity_home.*
import me.weyye.hipermission.PermissionCallback
import java.util.*

/**
 *created by 李云 on 2019/6/18
 *本类的作用:首页
 */

class HomeActivity : BaseActivity() {
    override fun getLayoutId(): Int {
        return R.layout.activity_home
    }

    override fun init() {
        setSwipeBackEnable(false)
    }

    override fun allowCaptureScreen(): Boolean {
        return false
    }

    override fun allowRecordScreen(): Boolean {
        return false
    }

    private val titles = arrayOf("创单", "下线", "抢单")

    // 未被选中的图标
    private val unSelectIds_teacher = intArrayOf(
        R.mipmap.ic_class_unselected,
        R.mipmap.ic_course_unselected,
        R.mipmap.ic_my_unselected
    )

    // 被选中的图标
    private val selectIds_teacher =
        intArrayOf(R.mipmap.ic_class_selected, R.mipmap.ic_course_selected, R.mipmap.ic_my_selected)
    private val mTabEntities = ArrayList<CustomTabEntity>()
    private var mCreateOrderFragment: CreateOrderFragment? = null
    private var mOfflineFragment: OfflineFragment? = null
    private var robOrderFragment: RobOrderFragment? = null

    //默认为0
    private var mIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            mIndex = savedInstanceState.getInt("currTabIndex")
        }
        super.onCreate(savedInstanceState)
        initTab()
        tab_layout.currentTab = mIndex
        switchFragment(mIndex)
        initPermission()
    }

    //初始化底部菜单
    private fun initTab() {
        //为Tab赋值
        (0 until titles.size).mapTo(mTabEntities) {
            TabEntity(titles[it], selectIds_teacher[it], unSelectIds_teacher[it])
        }
        tab_layout?.setTabData(mTabEntities)
        tab_layout?.setOnTabSelectListener(object : OnTabSelectListener {
            override fun onTabSelect(position: Int) {
                //切换Fragment
                switchFragment(position)
            }

            override fun onTabReselect(position: Int) {
            }
        })
        line.background.alpha = (0.46 * 255).toInt()
    }

    /**
     * 切换Fragment
     * @param position 下标
     */
    private fun switchFragment(position: Int) {
        val transaction = supportFragmentManager.beginTransaction()
        hideFragments(transaction)
        when (position) {
            0 // 创单
            -> {
                mCreateOrderFragment?.let {
                    transaction.show(it)
                } ?: CreateOrderFragment().let {
                    mCreateOrderFragment = it
                    transaction.add(R.id.fl_container, it, "order")
                }
                if (Build.VERSION_CODES.M > Build.VERSION.SDK_INT) {
                    setWindowStatusBarColor(R.color.color_15b38a)
                } else {
                    setWindowStatusBarColor(R.color.white)
                }
                initSystemBarTint()
            }
            1  //下线
            -> {
                mOfflineFragment?.let {
                    transaction.show(it)
                } ?: OfflineFragment.getInstance(titles[position]).let {
                    mOfflineFragment = it
                    transaction.add(R.id.fl_container, it, "offline")
                }
                if (Build.VERSION_CODES.M > Build.VERSION.SDK_INT) {
                    setWindowStatusBarColor(R.color.color_15b38a)
                } else {
                    setWindowStatusBarColor(R.color.white)
                }
                initSystemBarTint()
            }
            2 //抢单
            -> {
                robOrderFragment?.let {
                    transaction.show(it)

                } ?: RobOrderFragment().let {
                    robOrderFragment = it
                    transaction.add(R.id.fl_container, it, "rob")
                }
                setWindowStatusBarColor(R.color.color_15b38a)
                initSystemBarTint()
            }
        }
        mIndex = position
        tab_layout.currentTab = mIndex
        transaction.commitAllowingStateLoss()
    }


    /**
     * 隐藏所有的Fragment
     * @param transaction transaction
     */
    private fun hideFragments(transaction: FragmentTransaction) {
        mCreateOrderFragment?.let { transaction.hide(it) }
        mOfflineFragment?.let { transaction.hide(it) }
        robOrderFragment?.let { transaction.hide(it) }
    }


    private fun initPermission() {
        PermissionUtil.getInstance(this).checkPermissions(object : PermissionCallback {
            override fun onClose() {}
            override fun onFinish() {
                CommUtils.showToast(this@HomeActivity, "全部权限已开启")
            }

            override fun onDeny(permission: String, position: Int) {
                CommUtils.showToast(this@HomeActivity, permission + "拒绝")
            }

            override fun onGuarantee(permission: String, position: Int) {}
        })
    }

    @SuppressLint("MissingSuperCall")
    override fun onSaveInstanceState(outState: Bundle) {
        //记录fragment的位置,防止崩溃 activity被系统回收时，fragment错乱
        if (tab_layout != null) {
            outState.putInt("currTabIndex", mIndex)
        }
    }

    private var mExitTime: Long = 0
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (System.currentTimeMillis().minus(mExitTime) <= 2000) {
                AndroidUtil.finishActivity(this, true)
            } else {
                mExitTime = System.currentTimeMillis()
                showToast("再按一次退出程序")
            }
            return true
        }
        return super.onKeyDown(keyCode, event)
    }
}

