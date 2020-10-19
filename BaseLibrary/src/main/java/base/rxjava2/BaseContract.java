package base.rxjava2;

import android.graphics.Bitmap;

import java.util.Map;

/**
 * @description:baseContract
 */

public interface BaseContract {
    interface View extends BaseMVPView<Presenter> {
        void setContent(String content);  //设置内容
        void setCode(Bitmap value);  //设置验证码
    }

    interface Presenter extends BaseMVPPresenter {
        void getCode(); //获取验证码
        void userLogin(Map map); //登录
    }
}
