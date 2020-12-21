package com.common.base.rxjava2.activity;

import com.common.base.rxjava2.presenter.BaseMvpPresenter;
import com.common.base.rxjava2.presenter.BasePresenter;
import com.common.base.rxjava2.view.BaseMvpView;

public abstract class BaseMvpActivity<V extends BaseMvpView, P extends BaseMvpPresenter<V>> extends SuperMvpActivity {

    private P presenter;

    @Override
    protected void initPresenter() {
        //实例化Presenter
        presenter = createPresenter();
        //绑定
        if (presenter != null){
            presenter.attachView((V) this);
        }
    }

    // 初始化Presenter
    protected abstract P createPresenter();

    protected P getPresenter(){
        return presenter;
    }


    @Override
    protected void onDestroy() {
        //解绑
        if (presenter != null){
            presenter.detachView();
        }
        super.onDestroy();
    }

}
