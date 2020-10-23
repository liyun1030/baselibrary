package com.common.base.callback;

import android.app.Activity;

import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.request.base.Request;

public abstract class StringDialogCallback extends StringCallback {
//    private SweetAlertDialog dialog;
    public StringDialogCallback(Activity activity) {
//        dialog = new SweetAlertDialog(activity, SweetAlertDialog.PROGRESS_TYPE);
//        dialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
//        dialog.setTitleText("加载中...");
//        dialog.setCancelable(true);
    }

    @Override
    public void onStart(Request<String, ? extends Request> request) {
//        if (dialog != null && !dialog.isShowing()) {
//            dialog.show();
//        }
    }

    @Override
    public void onFinish() {
//        if (dialog != null && dialog.isShowing()) {
//            dialog.dismiss();
//        }
    }
}
