package com.common.base.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.common.base.R;
import com.common.base.tools.CommUtils;


/**
 * Created by liuxiaolong on 2019/12/16 0016
 */
public class CourseButtonDialog extends Dialog {
    Context mContext;
    EditText etNotes;
    TextView tv_title;
    ImageView mDown;
    Button mSave;
    RelativeLayout Rl;
    String mTitle;
    public CourseButtonDialog(@NonNull Context context,String title) {
        super(context, R.style.ActionSheetDialogStyle);
        mContext=context;
        mTitle=title;
        init();
    }

    public CourseButtonDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        init();
    }

    protected CourseButtonDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init();
    }


    private void init() {
        setContentView(R.layout.layout_button_dialog);
        tv_title=(TextView)findViewById(R.id.tv_title);
        etNotes=(EditText)findViewById(R.id.et_notes);
        mDown=(ImageView)findViewById(R.id.iv_down);
        mSave=(Button)findViewById(R.id.bt_save);
        Rl=(RelativeLayout)findViewById(R.id.rl);
        tv_title.setText(mTitle+"");
//        setCancelable(false);//点击外部不可dismiss
        setCanceledOnTouchOutside(true);
        Window window = this.getWindow();

        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(params);
        window.setGravity(Gravity.BOTTOM);


        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE |
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);


        etNotes.setFocusable(true);
        etNotes.setFocusableInTouchMode(true);
        etNotes.requestFocus();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                CommUtils.showKeyboard(etNotes);
            }
        },50);


        mDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CommUtils.isSoftShowing((Activity)mContext)){
                    CommUtils.hideKeyboard(etNotes);
                }else{
                    CourseButtonDialog.this.dismiss();
                }
            }
        });

        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(etNotes.getText().toString().trim())){
                    CommUtils.showToast(mContext, "内容不能为空");
                    return;
                }
                if (mOnItemClick!=null){
                    mOnItemClick.itemSaveClick(etNotes.getText().toString());
                    CourseButtonDialog.this.dismiss();
                }
            }
        });

        Rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CourseButtonDialog.this.dismiss();
            }
        });

        etNotes.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().replaceAll(" ", "").length() > 0) {
                    mSave.setBackground(mContext.getResources().getDrawable(R.drawable.ic_test_content));
                    mSave.setTextColor(mContext.getResources().getColor(R.color.white));
                } else {
                    mSave.setBackground(mContext.getResources().getDrawable(R.drawable.bg_save_shape));
                    mSave.setTextColor(mContext.getResources().getColor(R.color.gray3));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
    public interface onSaveClick{
        public void itemSaveClick(String note);
    }
    onSaveClick mOnItemClick;
    public void setonSaveClick(onSaveClick onsaveclick){
        mOnItemClick=onsaveclick;
    }

    public void setEtNotes(String etNotes) {
        if (!TextUtils.isEmpty(etNotes)){
            if (etNotes!=null){
                this.etNotes.setText(etNotes);
                this.etNotes.setSelection(this.etNotes.getText().toString().length());
            }
        }
    }

}