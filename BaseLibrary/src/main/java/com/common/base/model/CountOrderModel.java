package com.common.base.model;

public class CountOrderModel {

    String user_id;
    int goods_type;
    String resource_id;
    int price_rmb;
    int price_point;
    String remark;
    String sign;


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

    public String getResource_id() {
        return resource_id;
    }

    public void setResource_id(String resource_id) {
        this.resource_id = resource_id;
    }

    public int getPrice_rmb() {
        return price_rmb;
    }

    public void setPrice_rmb(int price_rmb) {
        this.price_rmb = price_rmb;
    }

    public int getPrice_point() {
        return price_point;
    }

    public void setPrice_point(int price_point) {
        this.price_point = price_point;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
