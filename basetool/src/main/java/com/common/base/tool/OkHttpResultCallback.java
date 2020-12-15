package com.common.base.tool;

import android.text.TextUtils;

import com.common.base.bean.BaseModel;
import com.common.base.network.BaseApi;
import com.common.base.network.OkHttpUtil;
import com.common.base.network.ResponseCodeShow;
import com.common.base.network.config.NetworkManager;

/**
 * 治趣app网络回调 这个callback主要是针对curefun工程的网络请求
 * Created by Yan Kai on 2016/2/23.
 */
public class OkHttpResultCallback extends NetworkManager.ResultStringCallback {

    private OkHttpUtil.ResponseListener listener;

    public OkHttpResultCallback(OkHttpUtil.ResponseListener listener) {
        this.listener = listener;
    }

    @Override
    public void onSuccess(int flag, String response) {
        if (TextUtils.isEmpty(response)) {
            failure(flag);
        } else {
            /**
             * 主要因为上面的判断格式是之前治趣接口返回的规范
             * 后面因为新来的一波后台人员开发襄阳项目所返回的规范(之前提过新一波人说兼容web端)
             */
            String message = OkHttpUtil.getRequestCode(response);
            String responseType = BaseApi.getResponseType(response);
            if (responseType != null && !responseType.equals("")) {
                if (responseType.equals("N")) {//成功
                    listener.success(flag, response);
                }
                if (responseType.equals("E")) {//失败
                    listener.failure(flag, BaseApi.getResponseMsg(response));
                }
                if (responseType.equals("S")) {//授权

                }

            } else {
                if (!TextUtils.isEmpty(message)) {
                    if (message.equals("0000") ) {
                        listener.success(flag, response);
                    }
                } else {
                    listener.failure(flag, "数据异常");
                }
            }
        }
    }

    private void failure(int flag) {
        BaseModel baseModel = new BaseModel();
        baseModel.setMessage(ResponseCodeShow.TIME_OUT);
        baseModel.setStatus("fail");
        listener.failure(flag, JsonUtils.writeEntity2Json(baseModel));
    }

    @Override
    public void onFailure(int flag, Exception e) {
//        listener.failure(flag, ResponseCodeShow.TIME_OUT);
        listener.failure(flag, "网络请求异常");
        if (flag == 95) {
            e.printStackTrace();
        }
    }
}
