package com.ly.baselibrary.mvp2.demo.dynamic;

import com.common.base.rxjava2.presenter.BaseMvpPresenter;
import com.ly.baselibrary.mvp2.demo.DataManager;
import com.ly.baselibrary.mvp2.demo.model.DynamicBean;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


public class DynamicPresenter extends BaseMvpPresenter<DynamicView> {

    private DataManager dataManager;
    private DynamicBean mDynamic;

    public DynamicPresenter() {
        dataManager = DataManager.getInstance();
    }

    public void getDynamic(String data){
        if(getMvpView() != null){
            // 进行网络请求
            dataManager.getDynamic(data)
                    // doOnSubscribe用于在call之前执行一些初始化操作
                    .doOnSubscribe(new Consumer<Disposable>() {
                        @Override
                        public void accept(@NonNull Disposable disposable) {
                            //请求加入管理,统一管理订阅,防止内存泄露
                            addDisposable(disposable);
                            // 显示进度提示
                            getMvpView().showProgressDialog();
                        }
                    })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<DynamicBean>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(DynamicBean dynamic) {
                            mDynamic = dynamic;
                        }

                        @Override
                        public void onError(Throwable e) {
                            // 在事件处理过程中出异常时，onError() 会被触发，同时队列自动终止，不允许再有事件发出
                            e.printStackTrace();
                            getMvpView().onError("请求失败！！");
                            getMvpView().hideProgressDialog();
                        }

                        @Override
                        public void onComplete() {
                            // onComplete方法和onError方法是互斥的，
                            // RxJava 规定，当不会再有新的 onNext() 发出时，需要触发 onCompleted() 方法作为标志。
                            if (mDynamic != null){
                                getMvpView().onSuccess(mDynamic);
                            }
                            // 隐藏进度
                            getMvpView().hideProgressDialog();
                        }
                    });
        }
    }


}
