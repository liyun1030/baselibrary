package com.common.base.rxjava2;


import com.common.base.bean.UserLoginReModel;
import com.common.base.rxjava2.Exception.ApiException;
import com.common.base.rxjava2.model.JavaBean;

import io.reactivex.Observable;


public class BaseContract {
    public interface Persenter {
        public void getCarList(String userId);
    }

    public interface View {
        void getDataSuccess();
        void getDataFail(ApiException apiException);
    }

    public interface Model {
        public Observable<RetrofitResponse<JavaBean>> login(UserLoginReModel model);
    }

}
