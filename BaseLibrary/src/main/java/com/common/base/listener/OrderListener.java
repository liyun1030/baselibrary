package com.common.base.listener;


import com.common.base.model.OrderResModel;

/**
 * 订单
 * Created by huangyong on 2017/9/11.
 */
public interface OrderListener {
    //订单列表
    public void createPayOrder(int type,OrderResModel model);

    public void fail(String str);
}
