package com.ly.test;

import com.common.base.activity.BaseActivity;
import com.common.base.tools.CommUtils;

public class MainActivity extends BaseActivity{

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void init() {
        CommUtils.showToast(this,"test");
    }
}
