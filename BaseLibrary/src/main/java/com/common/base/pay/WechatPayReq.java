package com.common.base.pay;

import android.app.Activity;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;


/**
 * 微信支付请求
 */
public class WechatPayReq  {
    private static final String TAG = WechatPayReq.class.getSimpleName();
    private Activity mActivity;
    //预支付码（重要）
    private String prepayId;
    //"Sign=WXPay"
    private String packageValue = "Sign=WXPay";
    private String nonceStr;
    //时间戳
    private String timeStamp;
    //签名
    private String sign;
    //内部订单号
    private String orderId;
    //微信支付核心api
    IWXAPI mWXApi;

    public WechatPayReq() {
        super();
    }


    /**
     * 发送微信支付请求
     */
    public void send() {
        mWXApi = WXAPIFactory.createWXAPI(mActivity, null);
        mWXApi.registerApp(PayManager.WX_APPID);
        PayReq request = new PayReq();
        request.appId = PayManager.WX_APPID;
        request.partnerId = PayManager.WX_MATCHID;
        request.prepayId = this.prepayId;
        request.packageValue = this.packageValue != null ? this.packageValue : "Sign=WXPay";
        request.nonceStr = this.nonceStr;
        request.timeStamp = this.timeStamp;
        request.sign = this.sign;

        mWXApi.sendReq(request);
    }

    public static class Builder {
        //上下文
        private Activity activity;
        //预支付码（重要）
        private String prepayId;
        //"Sign=WXPay"
        private String packageValue = "Sign=WXPay";
        private String nonceStr;
        //时间戳
        private String timeStamp;
        //签名
        private String sign;
        //内部订单号
        private String orderId;

        public Builder() {
            super();
        }

        public Builder with(Activity activity) {
            this.activity = activity;
            return this;
        }



        /**
         * 设置预支付码（重要）
         *
         * @param prepayId
         * @return
         */
        public Builder setPrepayId(String prepayId) {
            this.prepayId = prepayId;
            return this;
        }


        /**
         * 设置
         *
         * @param packageValue
         * @return
         */
        public Builder setPackageValue(String packageValue) {
            this.packageValue = packageValue;
            return this;
        }


        /**
         * 设置
         *
         * @param nonceStr
         * @return
         */
        public Builder setNonceStr(String nonceStr) {
            this.nonceStr = nonceStr;
            return this;
        }

        /**
         * 设置时间戳
         *
         * @param timeStamp
         * @return
         */
        public Builder setTimeStamp(String timeStamp) {
            this.timeStamp = timeStamp;
            return this;
        }

        /**
         * 设置签名
         *
         * @param sign
         * @return
         */
        public Builder setSign(String sign) {
            this.sign = sign;
            return this;
        }


        public WechatPayReq create() {
            WechatPayReq wechatPayReq = new WechatPayReq();

            wechatPayReq.mActivity = this.activity;

            //预支付码（重要）
            wechatPayReq.prepayId = this.prepayId;
            //"Sign=WXPay"
            wechatPayReq.packageValue = this.packageValue;
            wechatPayReq.nonceStr = this.nonceStr;
            //时间戳
            wechatPayReq.timeStamp = this.timeStamp;
            //签名
            wechatPayReq.sign = this.sign;
            wechatPayReq.orderId =  this.orderId;
            return wechatPayReq;
        }
    }


}