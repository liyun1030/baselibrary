/*
 * Copyright 2016 jeasonlzy(廖子尧)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.common.base.callback;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.Window;

import com.common.base.R;
import com.common.base.view.LoadingDialog;
import com.lzy.okgo.request.base.Request;

/**
 * ================================================
 * 作    者：jeasonlzy（廖子尧）Github地址：https://github.com/jeasonlzy
 * 版    本：1.0
 * 创建日期：2016/1/14
 * 描    述：对于网络请求是否需要弹出进度对话框
 * 修订历史：
 * ================================================
 */
public abstract class DialogCallback<T> extends JsonCallback<T> {

    private LoadingDialog loadingDialog;
    Activity activity;

    private void initDialog(Activity activity) {
        if (activity == null || activity.isFinishing()) return;
        this.activity = activity;
        loadingDialog = new LoadingDialog(activity, "数据加载中...", R.mipmap.ic_dialog_loading);
    }

    public DialogCallback(Context ctx) {
        super();
        initDialog((Activity) ctx);
    }

    @Override
    public void onStart(Request<T, ? extends Request> request) {
        if (activity != null && activity.isFinishing()) {
            if (loadingDialog != null && !loadingDialog.isShowing()) {
                try {
                    loadingDialog.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onFinish() {
        //网络请求结束后关闭对话框
        if (activity != null && activity.isFinishing()) {
            if (loadingDialog != null && loadingDialog.isShowing()) {
                try {
                    loadingDialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
