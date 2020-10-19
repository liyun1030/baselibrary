package com.common.base.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import com.common.base.R;
import com.common.base.model.DataModel;
import com.common.base.model.OrderReqModel;
import com.common.base.network.ResponseCode;
import com.common.base.pay.PayManager;
import com.common.base.tools.CommUtils;
import com.common.base.tools.RXTags;
import com.hwangjr.rxbus.RxBus;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hwangjr.rxbus.thread.EventThread;

import java.util.List;

/**
 * 支付订单页面
 * Created by huangyong on 2017/11/14.
 */

public class PayOrderActivity extends BaseActivity implements View.OnClickListener {
    TextView mAlertTv;
    View mAliLayout;
    View mWxLayout;
    CheckBox mAliRb;
    CheckBox mWxRb;
    Button mPayBtn;

    OrderReqModel mOrderReqModel;
    boolean obtain_goodid;
    private String mOrderId;   //内部订单
    private int retryTimes = 0; //重查订单状态次数

    /**
     * 支付
     *
     * @param contenxt
     * @param order    必填：goods_type   : 商品类型 1-病例
     *                 goods_id     : 商品ID，购买病例时送病例ID
     *                 goods_remark : 商品描述，病例购买时送病例名称
     *                 pay_amount   : 支付金额 单位分
     */
    public static void startActivity(Context contenxt, OrderReqModel order) {
        Intent i = new Intent(contenxt, PayOrderActivity.class);
        i.putExtra("pay_order", order);
        contenxt.startActivity(i);
    }

    /**
     * 支付 （讲堂课程支付2018/8/8添加）
     *
     * @param contenxt
     * @param order    必填：goods_type   : 商品类型  2-课程 3-课程组合
     *                 goods_id     : 商品ID，这里直接传递过来 不用重新网络请求
     *                 goods_remark : 商品描述，
     *                 pay_amount   : 支付金额 单位分
     */
    public static void startActivityHasGoodID(Context contenxt, OrderReqModel order) {
        Intent i = new Intent(contenxt, PayOrderActivity.class);
        i.putExtra("pay_order", order);
        i.putExtra("obtain_goodid", true);
        contenxt.startActivity(i);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_pay;
    }

    @Override
    public void init() {
        obtain_goodid = getIntent().getBooleanExtra("obtain_goodid", false);
        View ibBack = findViewById(R.id.ib_back);
        ibBack.setOnClickListener(this);
        TextView titleTv = (TextView) findViewById(R.id.tv_title);
        titleTv.setText(getString(R.string.pay_title));
        mAlertTv = (TextView) findViewById(R.id.pay_alert_tv);
        if (obtain_goodid) {
            mAlertTv.setText(Html.fromHtml(getString(R.string.pay_alert_course)));
        } else {
            mAlertTv.setText(Html.fromHtml(getString(R.string.pay_alert)));
        }
        mAliLayout = findViewById(R.id.ali_pay_layout);
        mWxLayout = findViewById(R.id.wx_pay_layout);
        mAliRb = (CheckBox) findViewById(R.id.ali_pay_rb);
        mWxRb = (CheckBox) findViewById(R.id.wx_pay_rb);
        mPayBtn = (Button) findViewById(R.id.pay_btn);
        initData();
    }

    protected void initData() {
        RxBus.get().register(this);
        mOrderReqModel = (OrderReqModel) getIntent().getSerializableExtra("pay_order");
        mPayBtn.setText("购买 " + CommUtils.parseRMB(mOrderReqModel.getPay_amount()));
        mAliLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAliRb.setChecked(true);
                mWxRb.setChecked(false);
            }
        });

        mWxLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAliRb.setChecked(false);
                mWxRb.setChecked(true);
            }
        });

        mPayBtn.setOnClickListener(mPayClicker);
    }

    View.OnClickListener mPayClicker = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mPayBtn.setEnabled(false);
            if (mWxRb.isChecked() && !isWeixinAvilible(PayOrderActivity.this)) {
                CommUtils.showToast(PayOrderActivity.this, "未安装微信客户端，请先安装！");
                mPayBtn.setEnabled(true);
                return;
            } else if (mAliRb.isChecked() && !isAliAvilible(PayOrderActivity.this)) {
                CommUtils.showToast(PayOrderActivity.this, "未安装支付宝客户端，请先安装！");
                mPayBtn.setEnabled(true);
                return;
            }
            showLoading();

            v.getHandler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mPayBtn.setEnabled(true);
                }
            }, 2000);

            retryTimes = 0;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    if (obtain_goodid) {
                        sendpayhasGoodID();
                    } else {
                        sendPay();
                    }
                }
            }).start();
        }
    };

    public void sendPay() {
        DataModel result = new DataModel();   //内部订单
        if (result == null) {
            RxBus.get().post(RXTags.ORDER_AFTER_PAY_FAIL, "创建订单失败");
        } else if (TextUtils.equals(result.getMessage(), "1219011")) {
            RxBus.get().post(RXTags.ORDER_AFTER_PAY_FAIL, "病例已经购买");
        } else if (TextUtils.equals(result.getMessage(), ResponseCode.REQUEST_SUCCESS+"")) {
            mOrderReqModel.goods_id = result.data;
            mOrderId = result.data;
            if (mAliRb.isChecked()) {
                PayManager.getInstance().sendPayWithAli(mOrderReqModel, PayOrderActivity.this);
            } else {
                PayManager.getInstance().sendPayWithWeixin(mOrderReqModel, PayOrderActivity.this);
            }
        } else {
            RxBus.get().post(RXTags.ORDER_AFTER_PAY_FAIL, "创建订单失败");
        }
    }

    public void sendpayhasGoodID() {
        mOrderReqModel.goods_id = mOrderReqModel.getGoods_id();
        mOrderId = mOrderReqModel.getGoods_id();
        if (mAliRb.isChecked()) {
            PayManager.getInstance().sendPayWithAli(mOrderReqModel, PayOrderActivity.this);
        } else {
            PayManager.getInstance().sendPayWithWeixin(mOrderReqModel, PayOrderActivity.this);
        }
    }

    @Subscribe(thread = EventThread.MAIN_THREAD, tags = {@Tag(RXTags.SHOW_LOADING)})
    public void show(String args) {
        showLoading();
    }

    @Subscribe(thread = EventThread.MAIN_THREAD, tags = {@Tag(RXTags.HIDE_LOADING)})
    public void hide(String args) {
        hideLoading();
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.ib_back) {
            finish();
        }
    }

    public static boolean isWeixinAvilible(Context context) {
        final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mm")) {
                    return true;
                }
            }
        }

        return false;
    }

    public static boolean isAliAvilible(Context context) {
        final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.eg.android.AlipayGphone")) {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxBus.get().unregister(this);
    }


}
