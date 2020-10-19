package com.common.base.pay;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.common.base.application.BaseApplication;
import com.common.base.model.OrderResModel;
import com.common.base.pay.alipay.PayResult;
import com.common.base.tools.RXTags;
import com.hwangjr.rxbus.RxBus;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


/**
 * 支付宝支付请求2
 * 安全的的支付宝支付流程，用法
 */
public class AliPayReq {
    public static final String TAG = "AliPayReq";
    /**
     * ali pay sdk flag
     */
    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_CHECK_FLAG = 2;

    private Activity mActivity;

    //服务器签名成功的订单信息
    private String signedAliPayOrderInfo;

    private Handler mHandler;

    public AliPayReq() {
        super();
        mHandler = new Handler(BaseApplication.getInstance().getMainLooper()) {

            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case SDK_PAY_FLAG: {
                        PayResult payResult = new PayResult((String) msg.obj);
                        Log.d(TAG,"-----------------------ALI PAY RESULT = \n" + payResult) ;
                        // 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
                        String resultInfo = payResult.getResult();
                        Log.d(TAG, "支付:" + resultInfo);
                        String resultStatus = payResult.getResultStatus();

                        // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                        // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "9000")
                                || TextUtils.equals(resultStatus,"8000")) {
                            RxBus.get().post(RXTags.ORDER_AFTER_PAY,"");
                           // Toast.makeText(mActivity, "支付成功", Toast.LENGTH_SHORT).show();

                        } else if(TextUtils.equals(resultStatus,"6001")){
                            //用户取消
                          //  RxBus.get().post(RXTags.ORDER_AFTER_PAY,"");
                        } else {
                            RxBus.get().post(RXTags.ORDER_AFTER_PAY_FAIL,"");
                            // 判断resultStatus 为非“9000”则代表可能支付失败
                            // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
//                            if (TextUtils.equals(resultStatus, "8000")) {
//                                Toast.makeText(mActivity, "支付结果确认中", Toast.LENGTH_SHORT).show();
//                            } else {
//                                // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
//                                Toast.makeText(mActivity, "支付失败", Toast.LENGTH_SHORT).show();
//                            }
                        }
                        break;
                    }
                    case SDK_CHECK_FLAG: {
                        Toast.makeText(mActivity, "检查结果为：" + msg.obj, Toast.LENGTH_SHORT).show();
                        break;
                    }
                    default:
                        break;
                }
            }

        };

    }


    /**
     * 发送支付宝支付请求
     */
    public void send() {
        // 做RSA签名之后的订单信息
        final String sign = signedAliPayOrderInfo;
        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask alipay = new PayTask(mActivity);
                // 调用支付接口，获取支付结果
                String result = alipay.pay(sign, true);

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    /**
     * 查询终端设备是否存在支付宝认证账户
     */
    public void check() {
        Runnable checkRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask payTask = new PayTask(mActivity);
                // 调用查询接口，获取查询结果
                //		boolean isExist = payTask.checkAccountIfExist();

                Message msg = new Message();
                msg.what = SDK_CHECK_FLAG;
                //		msg.obj = isExist;
                mHandler.sendMessage(msg);
            }
        };

        Thread checkThread = new Thread(checkRunnable);
        checkThread.start();
    }


    /**
     * 创建订单信息
     */
    public static String getOrderInfo(OrderResModel.Data meta) {
        String bizContent = "{\"timeout_express\":\"%timeout_express\",\"product_code\":\"QUICK_MSECURITY_PAY\",\"total_amount\":\"%total_amount\",\"subject\":\"%subject\",\"body\":\"%body\",\"out_trade_no\":\"%out_trade_no\"}";
        String orderInfo = "app_id=%app_id&biz_content=%biz_content&charset=%charset&format=%format&method=%method&notify_url=%notify_url&sign_type=%sign_type&timestamp=%timestamp&version=%version&sign=%sign";
//        bizContent = bizContent.replace("%timeout_express", meta.timeout_express == null ? "": meta.timeout_express);
//        bizContent = bizContent.replace("%total_amount", meta.total_amount == null ? "" : meta.total_amount);
//        bizContent = bizContent.replace("%subject", meta.subject == null ? "" : meta.subject);
//        bizContent = bizContent.replace("%body", meta.body == null ? "" : meta.body);
//        bizContent = bizContent.replace("%out_trade_no", meta.order_id == null ? "" : meta.order_id);


        orderInfo = orderInfo.replace("%app_id", PayManager.AL_APPID);
        orderInfo = orderInfo.replace("%biz_content", meta.biz_content == null ? "" : encode(meta.biz_content));

        orderInfo = orderInfo.replace("%charset", encode("UTF-8"));
        orderInfo = orderInfo.replace("%format", encode("json"));
        orderInfo = orderInfo.replace("%method", encode("alipay.trade.app.pay"));
        orderInfo = orderInfo.replace("%sign_type",encode("RSA2"));
        orderInfo = orderInfo.replace("%version",encode("1.0"));

        orderInfo = orderInfo.replace("%notify_url", encode(meta.notify_url == null ? "" : meta.notify_url));
        orderInfo = orderInfo.replace("%timestamp", encode(meta.timestamp == null ? "" : meta.timestamp));
        orderInfo = orderInfo.replace("%sign", encode(meta.sign == null ? "" : meta.sign));
        return orderInfo;
    }

    public static String encode(String info){
        try {
            // 仅需对sign 做URL编码
            info = URLEncoder.encode(info, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return info;
    }


    public static class Builder {
        //上下文
        private Activity activity;

        //服务器签名成功的订单信息
        private String signedAliPayOrderInfo;

        public Builder() {
            super();
        }

        public Builder with(Activity activity) {
            this.activity = activity;
            return this;
        }

        /**
         * 设置服务器签名成功的订单信息
         *
         * @param signedAliPayOrderInfo
         * @return
         */
        public Builder setSignedAliPayOrderInfo(String signedAliPayOrderInfo) {
            this.signedAliPayOrderInfo = signedAliPayOrderInfo;
            return this;
        }

        public AliPayReq create() {
            AliPayReq aliPayReq = new AliPayReq();
            aliPayReq.mActivity = this.activity;
            aliPayReq.signedAliPayOrderInfo = this.signedAliPayOrderInfo;

            return aliPayReq;
        }

    }
}
