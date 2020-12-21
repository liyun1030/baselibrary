package com.common.base.rxjava2.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

public abstract class SuperMvpActivity extends SwipeBackActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());

        initPresenter();
        //初始化控件，一般在BaseActivity中通过ButterKnife来绑定，所以该方法内部一般我们初始化界面相关的操作
        initViews();
        //获取数据
        getDataFromServer();
    }


    // 设置布局
    protected abstract int getLayoutId();

    // 初始化界面
    protected abstract void initViews();
    // 获取数据
    protected void getDataFromServer(){}
    // 实例化presenter
    protected void initPresenter(){}

}
