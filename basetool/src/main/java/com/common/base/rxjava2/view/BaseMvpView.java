package com.common.base.rxjava2.view;

/**
 * 定义通用的接口方法
 */

public interface BaseMvpView{

    // 出错信息的回调
    void onError(String result);
    // 显示进度框
    void showProgressDialog();
    // 关闭进度框
    void hideProgressDialog();
}
