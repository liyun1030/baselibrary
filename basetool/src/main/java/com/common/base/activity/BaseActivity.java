package com.common.base.activity;

import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.core.content.ContextCompat;

import com.common.base.R;
import com.common.base.tool.AtyContainer;
import com.common.base.tool.BaseConstant;
import com.common.base.view.LoadingDialog;
import com.gyf.barlibrary.ImmersionBar;
import com.lzy.okgo.OkGo;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * created by 李云 on 2018/12/21
 * 本类的作用:基于okgo-mvp的基类
 * https://github.com/jeasonlzy
 */
public abstract class BaseActivity extends SwipeBackActivity implements EasyPermissions.PermissionCallbacks {
    private LoadingDialog mLoadingDialog;
    private ImmersionBar mImmersionBar;//状态栏
    int mColor;
    int mTemplColor;

    public <T extends View> T findView(int id) {
        return (T) findViewById(id);
    }

    private Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        super.onCreate(savedInstanceState);
        AtyContainer.getInstance().addActivity(this);
        Log.e("onCreate", this.getClass().getSimpleName().toString());
        setContentView(getLayoutId());
        AppManager.getAppManager().addActivity(this);
        unbinder = ButterKnife.bind(this);
        init();
        initSystemBarTint();
    }

    public abstract int getLayoutId();

    public abstract void init();


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    /**
     * 检查重复点击
     *
     * @return false为重复点击
     */
    private long lastClickTime;

    protected boolean checkClickQuick() {
        if (lastClickTime - System.currentTimeMillis() < -BaseConstant.CLICK_CHECK) {
            lastClickTime = System.currentTimeMillis();
            return false;
        }
        lastClickTime = System.currentTimeMillis();
        return true;
    }

    /**
     * 设置状态栏颜色
     */
    protected void initSystemBarTint() {
        initWindowStatusBarColor();
        if (mColor == R.color.white && Build.VERSION_CODES.M > Build.VERSION.SDK_INT) return;
        if (mImmersionBar == null)
            mImmersionBar = ImmersionBar.with(this);
        mImmersionBar
                .statusBarColor(mColor)
                .statusBarDarkFont(true)
//                .flymeOSStatusBarFontColor(R.color.black)
                .fitsSystemWindows(true)
                .init();
    }

    private void initWindowStatusBarColor() {
        mColor = R.color.white;
        if (Build.VERSION_CODES.M > Build.VERSION.SDK_INT) {
            mColor = R.color.white;
        }
        if (mTemplColor != 0) {
            mColor = mTemplColor;
        }
    }

    public void setWindowStatusBarColor(int color) {
        this.mTemplColor = color;
    }

    /**
     * 初始化 Toolbar
     */
    public void initToolBar(Toolbar toolbar, boolean homeAsUpEnabled, String title) {
        toolbar.setTitle(title);
    }

    public void initToolBar(Toolbar toolbar, boolean homeAsUpEnabled, int resTitle) {
        initToolBar(toolbar, homeAsUpEnabled, getString(resTitle));
    }

    public void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }


    /**
     * 显示dialog
     */
    public void showLoading() {
        mLoadingDialog = new LoadingDialog(this, "数据加载中...", R.mipmap.ic_dialog_loading);
        if (this == null || this.isFinishing()) return;
        if (mLoadingDialog != null && !mLoadingDialog.isShowing()) {
            mLoadingDialog.show();
        }
    }

    protected void setStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.dark_blue));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //Activity销毁时，取消网络请求
        OkGo.getInstance().cancelTag(this);
        if (unbinder != null) {
            unbinder.unbind();
        }
        if (mImmersionBar != null) {
            mImmersionBar.destroy();
        }
        AtyContainer.getInstance().removeActivity(this);
        Log.e("onDestroy", this.getClass().getSimpleName().toString());
    }

    public void hideLoading() {
        if (this == null || this.isFinishing()) return;
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
        }

    }

    @Override
    public void finish() {
        super.finish();
        hideLoading();
        overridePendingTransition(R.anim.left, R.anim.right_to_left_open);
    }

    /**
     * 当权限被成功申请的时候执行回调
     * @param requestCode 权限请求的识别码
     * @param perms       申请的权限的名字
     */
    @Override
    public void onPermissionsGranted(int requestCode,  List<String> perms) {
        Log.i("EasyPermissions", "获取成功的权限$perms");
    }

    /**
     * 当权限申请失败的时候执行的回调
     * @param requestCode 权限请求的识别码
     * @param perms       申请的权限的名字
     */
    @Override
    public void onPermissionsDenied(int requestCode,  List<String> perms) {
        //处理权限名字字符串
        StringBuffer sb = new StringBuffer();
        for (String str : perms) {
            sb.append(str);
            sb.append("\n");
        }
        sb.replace(sb.length() - 2, sb.length(), "");
        //用户点击拒绝并不在询问时候调用
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            Toast.makeText(this, "已拒绝权限" + sb + "并不再询问", Toast.LENGTH_SHORT).show();
            new AppSettingsDialog.Builder(this)
                    .setRationale("此功能需要" + sb + "权限，否则无法正常使用，是否打开设置")
                    .setPositiveButton("好")
                    .setNegativeButton("不行")
                    .build()
                    .show();
        }
    }

    /**
     * 重写要申请权限的Activity或者Fragment的onRequestPermissionsResult()方法，
     * 在里面调用EasyPermissions.onRequestPermissionsResult()，实现回调。
     * @param requestCode  权限请求的识别码
     * @param permissions  申请的权限
     * @param grantResults 授权结果
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,  String[] permissions,  int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }
}

