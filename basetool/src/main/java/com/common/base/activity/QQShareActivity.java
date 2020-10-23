package com.common.base.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.common.base.R;
import com.common.base.share.ShareConstants;
import com.common.base.tool.CommUtils;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * qq分享
 */
public class QQShareActivity extends Activity implements IUiListener {

    public static final String TYPE = "type";
    public static final String TO = "to";

    public static final int TYPE_CASE = 1;
    public static final int TYPE_EXAM = 2;
    public static final int TYPE_ESTIMATE_IMAGE = 3;
    public static final int TYPE_INVITE_FRIEND = 4;
    public static final int TYPE_EXAM_GRADE_RANK = 5;
    public static final int TYPE_INFO_ARTICLE = 6;
    public static final int TYPE_CASE_CHALLENGE = 7;
    public static final int TYPE_CASE_REPORT = 8;
    public static final int TYPE_CURE_FUN_COMPETE = 9;
    public static final int TYPE_MEDICINE_KNOWLEDGE = 10;

    public static final int TO_FRIEND = 1;
    public static final int TO_QZONE = 2;
    public static final int PUBLISH_QZONE = 3;
    public static final String ACTION_SHARE = "com.curefun.share.shareSuccess";

    private int mType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Tencent tencent = Tencent.createInstance(ShareConstants.QQ_APP_ID, getApplicationContext());
        Bundle bundle = getIntent().getExtras();
        if (null == bundle) {
            CommUtils.showToast(getApplicationContext(), R.string.error_common);
            finish();
            return;
        }
        mType = bundle.getInt(TYPE, -1);

        int to = bundle.getInt(TO, -1);
        if (TO_FRIEND == to) {
            tencent.shareToQQ(this, bundle, this);
        } else if (TO_QZONE == to) {
            tencent.shareToQzone(this, bundle, this);
        } else if (PUBLISH_QZONE == to) {
            tencent.publishToQzone(this, bundle, this);
        } else {
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Tencent.onActivityResultData(requestCode, resultCode, data, this);
    }

    @Override
    public void onComplete(Object o) {
        JSONObject jsonObject = (JSONObject) o;
        if (null == jsonObject) {
            finish();
            return;
        }
        int ret = -1;
        try {
            ret = jsonObject.getInt("ret");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (ret != 0) {
            CommUtils.showToast(getApplicationContext(), R.string.errcode_fail);
            finish();
            return;
        }
        CommUtils.showToast(getApplicationContext(), R.string.errcode_success);
        finish();
    }

    private void sendArticleBroadCast() {
        Intent intent = new Intent();
        intent.putExtra("share", true);
        intent.setAction(ACTION_SHARE);
        sendBroadcast(intent);
    }

    @Override
    public void onError(UiError uiError) {
        finish();
    }

    @Override
    public void onCancel() {
        finish();
    }

}
