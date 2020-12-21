package com.common.base.rxjava2;


import com.common.base.bean.UserLoginReModel;
import com.common.base.rxjava2.model.JavaBean;

import io.reactivex.Observable;

public class BaseMvpModel implements BaseContract.Model {
    @Override
    public Observable<RetrofitResponse<JavaBean>> login(UserLoginReModel model) {
        return RetrofitUtil.getInstance().getRequest().login(model);
    }
}
