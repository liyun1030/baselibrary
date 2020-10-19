package com.common.base.pay;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;
import com.common.base.R;
import com.common.base.application.BaseApplication;
import com.common.base.business.OrderBusiness;
import com.common.base.listener.OrderListener;
import com.common.base.model.OrderReqModel;
import com.common.base.model.OrderResModel;
import com.common.base.network.ResponseCode;
import com.common.base.tools.RXTags;
import com.hwangjr.rxbus.RxBus;

/**
 * 支付管理
 *
 * @author huangyong
 */
public class PayManager {
    public static final String TAG = "PayManager";
    private static final Object mLock = new Object();
    public static final String WX_APPID = "wxdd44c96a85693969";   //微信appid
    public static final String WX_MATCHID = "1490801672";          //微信商户id
    public static final String AL_APPID = "2017103109637393";       //支付宝pid
    private static PayManager mInstance;
    private OrderBusiness mBusiness;
    public static PayManager getInstance() {
        if (mInstance == null) {
            synchronized (mLock) {
                if (mInstance == null) {
                    mInstance = new PayManager();
                    mInstance.mBusiness = OrderBusiness.getInstance(BaseApplication.getInstance());
                }
            }
        }
        return mInstance;
    }

    /**
     * 微信支付
     *
     * @param req
     * @param act
     */
    public void sendPayWithWeixin(final OrderReqModel req, final Activity act) {
        req.pay_type = 1;
        sendPayRequest(req, act);
    }

    /**
     * 支付宝支付
     *
     * @param req
     * @param act
     */
    public void sendPayWithAli(final OrderReqModel req, final Activity act) {
        req.pay_type = 2;
        sendPayRequest(req, act);
    }

    /**
     * 支付请求
     */
    private void sendPayRequest(final OrderReqModel req, final Activity act) {
        OrderListener listener=new OrderListener() {
            @Override
            public void createPayOrder(int type,OrderResModel model) {
                if(type==1) {
                    payWX(act, model);
                }else if(type==2){
                    payAL(act,model);
                }
                RxBus.get().post(RXTags.HIDE_LOADING, "");
            }

            @Override
            public void fail(String str) {

            }
        };
        if (req.pay_type == 1) {
            mBusiness.createPayOrder(req,"",1,listener);
        } else {
            mBusiness.createPayOrder(req,"",2,listener);
        }
    }

    private void payWX(Activity act, OrderResModel res) {
        if (res != null && TextUtils.equals(res.getMessage(), ResponseCode.REQUEST_SUCCESS + "")) {
            OrderResModel.Data resutl = res.data;
            WechatPayReq wechatPayReq = new WechatPayReq.Builder()
                    .with(act) //activity实例
                    .setPrepayId(resutl.pre_pay_id)//预支付码
                    .setNonceStr(resutl.noncestr)
                    .setTimeStamp(resutl.timestamp)//时间戳
                    .setSign(resutl.sign)//签名
                    //  .setPackageValue("orderid|" + res.getOrder_id())
                    .create();
            wechatPayReq.send();
        } else {
            RxBus.get().post(RXTags.ORDER_AFTER_PAY_FAIL, act.getString(R.string.pay_exception));
        }
    }

    private void payAL(Activity act, OrderResModel res) {
        if (res != null && TextUtils.equals(res.getMessage(), ResponseCode.REQUEST_SUCCESS + "")) {
            OrderResModel.Data result = res.data;

            final String rawAliOrderInfo = AliPayReq.getOrderInfo(result);
            Log.d(TAG, "Alipay  rawAliOrderInfo = " + rawAliOrderInfo);
            AliPayReq aliPayReq = new AliPayReq.Builder()
                    .with(act) //activity实例
                    .setSignedAliPayOrderInfo(rawAliOrderInfo) //set the signed ali pay order info
                    .create();
            aliPayReq.send();
        } else {
            RxBus.get().post(RXTags.ORDER_AFTER_PAY_FAIL, act.getString(R.string.pay_exception));
        }
    }

}
