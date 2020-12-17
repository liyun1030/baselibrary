package com.common.base.rxjava2.model;

import com.common.base.rxjava2.presenter.BasePresenter;

/**
 * 拿到P层，把结果给P层
 * 将由子类传 CONTRACT 到父类中
 */

public abstract class SuperBaseModel<P extends BasePresenter, CONTRACT> extends SuperBase<CONTRACT> {

    public P presenter;

    public SuperBaseModel(P mPresenter) {
        this.presenter = mPresenter;    //拿到P层
    }

}
