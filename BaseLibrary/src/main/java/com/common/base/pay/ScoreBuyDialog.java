package com.common.base.pay;

import android.app.Dialog;
import android.content.Context;
import android.text.SpannableString;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.common.base.R;

/**
 * 积分购买对话框
 */
public class ScoreBuyDialog extends Dialog implements View.OnClickListener {

    private TextView mTitleTV;
    private TextView mLastScoreTV;
    private TextView mPayScoreTV;
    private TextView mPromptMsgTV;
    private Button mBuyBtn;
    private Button mCancelBtn;
    private LinearLayout llCancelConfirm;

    public ScoreBuyDialog(Context context) {
        super(context, R.style.Dialog);
        init();
    }

    public ScoreBuyDialog(Context context, int themeResId) {
        super(context, themeResId);
        init();
    }

    public ScoreBuyDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init();
    }

    private void init() {
        setContentView(R.layout.layout_pre_buy_dialog);
        mTitleTV = (TextView) findViewById(R.id.tv_title);
        mLastScoreTV = (TextView) findViewById(R.id.tv_last_score);
        mPayScoreTV = (TextView) findViewById(R.id.tv_pay_score);
        mPromptMsgTV = (TextView) findViewById(R.id.tv_info);
        mBuyBtn = (Button) findViewById(R.id.btn_buy_now);
        mCancelBtn = (Button) findViewById(R.id.btn_cancel);
        llCancelConfirm = (LinearLayout) findViewById(R.id.ll_cancel_confirm);
        mBuyBtn.setOnClickListener(this);
        mCancelBtn.setOnClickListener(this);
    }

    public void setTitle(String title) {
        mTitleTV.setText(title);
    }

    public void setContextColor(int color) {
        mLastScoreTV.setTextColor(getContext().getResources().getColor(color));
        mPayScoreTV.setTextColor(getContext().getResources().getColor(color));
        mPromptMsgTV.setTextColor(getContext().getResources().getColor(color));
    }

    public void setLastScore(String last) {
        mLastScoreTV.setText(last);
    }

    public void setPayScore(String pay) {
        mPayScoreTV.setText(pay);
    }

    public void setPromptMessage(String msg) {
        mPromptMsgTV.setText(msg);
    }

    public void setPromptMessage(SpannableString builder) {
        mPromptMsgTV.setText(builder);
    }

    public void setConfirmListener(View.OnClickListener listener) {
        mBuyBtn.setOnClickListener(listener);
    }

    public void setCancelListener(View.OnClickListener listener) {
        mCancelBtn.setOnClickListener(listener);
    }

    public void setButtonShow(boolean show) {
        if (show) {
            llCancelConfirm.setVisibility(View.VISIBLE);
        } else {
            llCancelConfirm.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_buy_now) {
            dismiss();
        } else if (v.getId() == R.id.btn_cancel) {
            dismiss();
        }
    }
}
