package com.common.base.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.common.base.application.BaseApplication;

/**
 * 网络监听
 * Created by Yan Kai on 2015/11/11.
 */
public class NetworkReceiver extends BroadcastReceiver {

//    private CureFunApi cureFunApi;
//    private Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
//        if (cureFunApi == null) {
//            cureFunApi = new CureFunApi(context, this);
//            this.context = context;
//        }
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeInfo = manager.getActiveNetworkInfo();
        if (null != activeInfo) {
            ((BaseApplication) context.getApplicationContext()).setHasNetwork(activeInfo.isAvailable());
//            String account = SharedPreferencesUtils.getAccount(context);
//            String password = SharedPreferencesUtils.getPassword(context);
//            if (TextUtils.isEmpty(account) || TextUtils.isEmpty(password) || CommUtils.getNetworkStatus(context) == -1) {
//                return;
//            }
//            if (CommUtils.verifyPhoneNum(account) || CommUtils.verifyEmail(account)) {
//                cureFunApi.userLogin(context.getApplicationContext(), account, password);
//            }
        } else {
            ((BaseApplication) context.getApplicationContext()).setHasNetwork(false);
        }

    }
//
//    @Override
//    public void success(int flag, String response) {
//        if (null == response)
//            return;
//        String resCode = CureFunApi.getRequestCode(response);
//        switch (flag) {
//            case Constants.LOGIN_FLAG:
//                if (resCode.equals(ResponseCode.REQUEST_SUCCESS)) {
//                    LoginResModel loginModel = JsonUtils.readJson2Entity(response, LoginResModel.class);
////                    CommUtils.showToast(context, R.string.login_success);
////                    CureFunUtils.saveAccountPwd(this, account, password);
//                    MyApplication.getInstance().setLoginInfo(loginModel);
////                    Intent intent = new Intent(context, RedpacketService.class);
////                    context.startService(intent);
//                } else {
//                    MyApplication.getInstance().setLoginInfo(null);
//                    CommUtils.showToast(context, ResponseCode.getMsg(resCode));
//                }
//        }
//    }
//
//    @Override
//    public void failure(int flag, String errorResponse) {
//
//    }
}
