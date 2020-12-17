package com.common.base.rxjava2.presenter;

import com.common.base.bean.UserLoginReModel;
import com.common.base.rxjava2.BaseContract;
import com.common.base.rxjava2.BaseMvpModel;
import com.common.base.rxjava2.Exception.ApiException;
import com.common.base.rxjava2.ResponseTransformer;
import com.common.base.rxjava2.model.SuperBase;
import com.common.base.rxjava2.model.SuperBaseModel;
import com.common.base.rxjava2.schedulers.BaseSchedulerProvider;


import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * 拿到V层和M层，View必须是继承BaseActivity
 * Presenter关联M和V，M层和V层不能有交互，通过中间层P来进行交互
 */

public class BasePresenter {

    private BaseMvpModel model;

    private BaseContract.View view;

    private BaseSchedulerProvider schedulerProvider;

    private CompositeDisposable mDisposable;

    public BasePresenter(BaseMvpModel model,
                         BaseContract.View view,
                     BaseSchedulerProvider schedulerProvider) {
        this.model = model;
        this.view = view;
        this.schedulerProvider = schedulerProvider;
        mDisposable = new CompositeDisposable();

    }

    public void despose(){
        mDisposable.dispose();
    }

    public void login() {
        UserLoginReModel userLoginReModel=new UserLoginReModel();
        userLoginReModel.setAccount("19000000001");
        userLoginReModel.setPassword("000001");
//        Disposable disposable = model.login(userLoginReModel)
//                .compose(ResponseTransformer.handleResult())
//                .compose(schedulerProvider.applySchedulers())
//                .subscribe(carBeans -> {
//                    // 处理数据 直接获取到List<JavaBean> carBeans
//                    view.getDataSuccess();
//                }, throwable -> {
//                    // 处理异常
//                    view.getDataFail();
//                });
        Disposable disposable = model.login(userLoginReModel)
                .compose(ResponseTransformer.handleResult())
                .compose(schedulerProvider.applySchedulers())
                .subscribe(carBeans -> {
                    // 处理数据 直接获取到List<JavaBean> carBeans
                    view.getDataSuccess();
                }, throwable -> {
                    // 处理异常
                    view.getDataFail((ApiException)throwable);
                });
        mDisposable.add(disposable);
    }

}

