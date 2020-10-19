package base.pay;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.common.base.R;
import com.common.base.model.CaseBaseInfoModel;
import com.common.base.model.CountOrderModel;

/**
 * 人民币购买-对话框
 */

public class BuyCaseRmbDialog extends DialogFragment implements View.OnClickListener {
    Activity mAct;
    TextView mShenyuJifenTv;
    TextView mPayJifenTv;
    TextView mPayRmbTv;
    Button mCancelBtn;
    Button mBuyBtn;
    ImageView mClose;
    CaseBaseInfoModel info;
    CountOrderModel model;
//    ObtainGoodsIDBusiness mBusiness;
    //   @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        LayoutInflater i = getActivity().getLayoutInflater();
        View view = i.inflate(R.layout.activity_pay_confirm, container, true);
        mShenyuJifenTv = (TextView) view.findViewById(R.id.shyu_jifen_tv);
        mPayJifenTv = (TextView) view.findViewById(R.id.pay_jifen_tv);
        mPayRmbTv = (TextView)view.findViewById(R.id.pay_rmb_tv);
        mCancelBtn = (Button) view.findViewById(R.id.cancel_btn);
        mBuyBtn = (Button) view.findViewById(R.id.buy_btn);
        mClose=(ImageView)view.findViewById(R.id.tv_close);
        mClose.setOnClickListener(this);
        mCancelBtn.setOnClickListener(this);
        mBuyBtn.setOnClickListener(this);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mAct = getActivity();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        mBusiness=new ObtainGoodsIDBusiness(mAct,this);
        mShenyuJifenTv.setText(Html.fromHtml(getString(R.string.pay_confirm1,info.getMy_point() + "")));
        mPayJifenTv.setText(Html.fromHtml(getString(R.string.pay_confirm6,(info.getBuy_discount_point() == 0 ? info.getBuy_point() : info.getBuy_discount_point()) + "")));
        float rmb = (info.getCase_price() / 100f);
        mPayRmbTv.setText(Html.fromHtml(getString(R.string.pay_confirm5,rmb+"")));//需要支付价格
    }

    public synchronized static void showDialog(FragmentManager fm, CaseBaseInfoModel info, CountOrderModel countOrderModel) {
        BuyCaseRmbDialog f = (BuyCaseRmbDialog) fm.findFragmentByTag("BuyCaseRmbDialog");
        if(f == null) {
            f = new BuyCaseRmbDialog();
        }
        f.info = info;
        f.model=countOrderModel;//获取购买的商品ID，用于后面创建订单 所需要的body
        if(!f.isAdded()) {
            f.show(fm,"BuyCaseRmbDialog");
        }

    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.buy_btn) {
//            ((CourseDetailsActivity)mAct).getGoodsID();
//            OrderReqModel orderReq = new OrderReqModel();
//            orderReq.goods_type = 1;
//            orderReq.case_id = info.getCase_id();
//            orderReq.goods_remark = "治趣虚拟病人";
//            orderReq.pay_amount = info.getBuy_discount_point() == 0 ? info.getBuy_point() : info.getBuy_discount_point();
//            PayOrderActivity.startActivity(getActivity(),orderReq);
            this.dismiss();
        } else if(v.getId() == R.id.cancel_btn) {
            this.dismiss();
//            startActivity(new Intent(mAct, UserPointActivity.class));
        }else if (v.getId() ==R.id.tv_close){
            this.dismiss();
        }
    }

    public void setData(CaseBaseInfoModel model) {
        this.info = model;
    }


}
