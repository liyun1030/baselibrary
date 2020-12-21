package com.ly.baselibrary.mvp2.demo.login;


import com.common.base.rxjava2.view.BaseMvpView;
import com.ly.baselibrary.mvp2.demo.model.UserBean;

/**
 * @author jiang zhu on 2019/11/23
 */
public interface LoginView extends BaseMvpView {
    // 当前页面比较简单仅仅是获取接口数据进行展示，
    // 业务比较复杂的时候，可能一个页面需要不同的接口得到不同的数据类型
    void onSuccess(UserBean mUser);
}
