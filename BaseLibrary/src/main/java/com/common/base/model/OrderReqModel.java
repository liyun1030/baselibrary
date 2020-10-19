package com.common.base.model;

import java.io.Serializable;

public class OrderReqModel implements Serializable{
    public static int RMB_RATIO = 1;    //1分钱RMB 换算1积分

    //----- 必选项  start------
    public String case_id;                                                            //商品ID，购买病例时送病例ID
    public int goods_type;                                                           //商品类型 1-病例
    public String goods_id;                                                            //内部订单号
    public String goods_remark;                                                      //商品描述，病例购买时送病例名称
    public int pay_amount;                                                         //订单需要的rmb 以分为单位
    //----- 必选项  end------



    public int pay_type;   //支付类型 1-微信 2-支付宝
    public String user_id;  //用户ID
    public String device_id; //设备ID 必输
    public String device_ip;//设备IP  必输


    public String getCase_id() {
        return case_id;
    }

    public void setCase_id(String case_id) {
        this.case_id = case_id;
    }

    public String getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(String goods_id) {
        this.goods_id = goods_id;
    }

    public int getPay_amount() {
        return pay_amount;
    }

    public void setPay_amount(int pay_amount) {
        this.pay_amount = pay_amount;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public int getGoods_type() {
        return goods_type;
    }

    public void setGoods_type(int goods_type) {
        this.goods_type = goods_type;
    }

    public static int getRmbRatio() {
        return RMB_RATIO;
    }

    public static void setRmbRatio(int rmbRatio) {
        RMB_RATIO = rmbRatio;
    }

    public String getGoods_remark() {
        return goods_remark;
    }

    public void setGoods_remark(String goods_remark) {
        this.goods_remark = goods_remark;
    }

    public int getPay_type() {
        return pay_type;
    }

    public void setPay_type(int pay_type) {
        this.pay_type = pay_type;
    }


    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public String getDevice_ip() {
        return device_ip;
    }

    public void setDevice_ip(String device_ip) {
        this.device_ip = device_ip;
    }
}
