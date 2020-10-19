package com.common.base.business;

import android.content.Context;
import com.common.base.application.BaseApplication;
import com.common.base.bean.BaseBean;
import com.common.base.listener.OrderListener;
import com.common.base.model.*;
import com.common.base.network.BaseApiUrl;
import com.common.base.network.ResponseCode;
import com.common.base.tools.*;

import java.util.Map;

/**
 * 订单 支付
 */

public class OrderBusiness {
    private static OrderBusiness okGoUtil = new OrderBusiness();
    private Context ctx;
    private OrderBusiness() {
    }
    public static OrderBusiness getInstance(Context context) {
        okGoUtil.ctx = context;
        return okGoUtil;
    }

    /**
     * 外部订单
     * 创建支付宝订单2-创建微信支付订单1
     * @param orderReq
     * @return
     */
    public void createPayOrder(OrderReqModel orderReq,String uid,final int type,final OrderListener listener){
        orderReq.user_id = uid;
        orderReq.device_id = CureFunUtils.getDeviceId(BaseApplication.getInstance());
        orderReq.device_ip = CommUtils.getHostIP();
        Map maps = null;
        if (orderReq != null) {
            maps = MapUtils.objectToMap(orderReq);
        }
        String url=null;
        switch (type){
            case 1:
                //微信
                url=BaseApiUrl.CREATE_WX_PAY_ORDER;
                break;
            case 2:
                //支付宝
                url=BaseApiUrl.CREATE_ALI_PAY_ORDER;
                break;
        }
        OkgoUtil.post(ctx, url, maps, new OkgoUtil.ResponseListener() {
            @Override
            public void success(String response) {
                OrderResModel bean = JsonUtils.readJson2Entity(response, OrderResModel.class);
                if (listener != null) {
                    listener.createPayOrder(type,bean);
                }
            }

            @Override
            public void failure(String errorResponse) {
                if (listener != null) {
                    listener.fail(errorResponse);
                }
            }
        });
    }
}
