package com.common.base.tool;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.provider.Settings;
import android.support.design.widget.TabLayout;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sina.weibo.sdk.utils.MD5;

import java.lang.reflect.Field;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Yan Kai on 2015/12/18.
 */
public class CureFunUtils {
    public static final String TAG = "CureFunUtils";


    public static void changeTextColor(String begin, String end, int indexOfBegin, int indexOfEnd, TextView tv, int color) {
        String text = tv.getText().toString();
        int tb = text.indexOf(begin) + indexOfBegin;
        int te = text.indexOf(end) + indexOfEnd;
        SpannableStringBuilder style = new SpannableStringBuilder(text);
        style.setSpan(new ForegroundColorSpan(color), tb, te, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv.setText(style);
    }

    public static void searchChangeKeyWordColor(TextView tv, String keyWord, String matchingWord) {
        if (TextUtils.isEmpty(keyWord)) {
            tv.setText("");
            return;
        }
        int length = keyWord.length();
        SpannableStringBuilder styledText = new SpannableStringBuilder(matchingWord);
        for (int i = 0; i < length; i++) {
            if (matchingWord.contains(keyWord.charAt(i) + "")) {
                int index = matchingWord.indexOf(keyWord.charAt(i) + "");
                styledText.setSpan(new ForegroundColorSpan(Color.RED), index, index + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        tv.setText(styledText);
    }

    //判断是否包含特殊字符
    public static boolean compileExChar(String str, Context context) {
        String limitEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Pattern pattern = Pattern.compile(limitEx);
        Matcher m = pattern.matcher(str);
        return m.find();
    }

    /**
     * 隐藏手机号中间四位
     * @param phone 手机号
     * @return
     */
    public static String formatPhone(long phone) {
        String phoneStr = String.valueOf(phone);
        if (TextUtils.isEmpty(phoneStr) || phoneStr.length() != 11) {
            return phoneStr;
        } else {
            return phoneStr.substring(0, 3) + "****" + phoneStr.substring(7, 11);
        }
    }


    public static String getDeviceId(Context c) {
        String result = "";
        String m_szDevIDShort = "35" + //we make this look like a valid IMEI
                Build.BOARD.length() % 10 +
                Build.BRAND.length() % 10 +
                Build.CPU_ABI.length() % 10 +
                Build.DEVICE.length() % 10 +
                Build.DISPLAY.length() % 10 +
                Build.HOST.length() % 10 +
                Build.ID.length() % 10 +
                Build.MANUFACTURER.length() % 10 +
                Build.MODEL.length() % 10 +
                Build.PRODUCT.length() % 10 +
                Build.TAGS.length() % 10 +
                Build.TYPE.length() % 10 +
                Build.USER.length() % 10; //13 digits

        result += m_szDevIDShort;

        try {
            //        TelephonyManager TelephonyMgr = (TelephonyManager)c.getSystemService(c.TELEPHONY_SERVICE);
            //        String szImei = TelephonyMgr.getDeviceId();
            //        szImei = TextUtils.isEmpty(szImei) ? "" : szImei;
            String szImei = "";
            result += szImei;

            String m_szAndroidID = Settings.Secure.getString(c.getContentResolver(), Settings.Secure.ANDROID_ID);
            m_szAndroidID = TextUtils.isEmpty(m_szAndroidID) ? "" : m_szAndroidID;
            result += m_szAndroidID;

        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d(TAG, "---------------------- DeviceId = " + result);
        return MD5.hexdigest(result);
    }

    /**
     * tabLayout底部线宽设置
     *
     * @param tabLayout
     * @param margin
     */
    public static void setIndicator(TabLayout tabLayout, Context context, int margin) {
        try {
            //拿到tabLayout的mTabStrip属性
            Field mTabStripField = tabLayout.getClass().getDeclaredField("mTabStrip");
            if (mTabStripField==null) return;
            mTabStripField.setAccessible(true);
            LinearLayout mTabStrip = (LinearLayout) mTabStripField.get(tabLayout);
            int dp10 = CommUtils.dip2px(context, margin);
            for (int i = 0; i < mTabStrip.getChildCount(); i++) {
                View tabView = mTabStrip.getChildAt(i);
                //拿到tabView的mTextView属性
                Field mTextViewField = tabView.getClass().getDeclaredField("mTextView");
                mTextViewField.setAccessible(true);
                TextView mTextView = (TextView) mTextViewField.get(tabView);
                tabView.setPadding(0, 0, 0, 0);

                //字多宽线就多宽，所以测量mTextView的宽度
                int width;
                width = mTextView.getWidth();
                if (width == 0) {
                    mTextView.measure(0, 0);
                    width = mTextView.getMeasuredWidth();
                }

                //设置tab左右间距为10dp  注意这里不能使用Padding 因为源码中线的宽度是根据 tabView的宽度来设置的
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) tabView.getLayoutParams();
                params.width = width;
                params.leftMargin = dp10;
                params.rightMargin = dp10;
                tabView.setLayoutParams(params);
                tabView.invalidate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void setTablayoutIndicator(TabLayout tabs, Context context) {
        Class<?> tabLayout = tabs.getClass();
        Field tabStrip = null;
        try {
            tabStrip = tabLayout.getDeclaredField("mTabStrip");
        } catch (NoSuchFieldException e) {
            try {
                tabStrip = tabLayout.getDeclaredField("slidingTabIndicator");
            } catch (NoSuchFieldException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        }
        if (tabStrip==null) return;
        tabStrip.setAccessible(true);
        LinearLayout llTab = null;
        try {
            llTab = (LinearLayout) tabStrip.get(tabs);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < llTab.getChildCount(); i++) {
            View child = llTab.getChildAt(i);
            child.setPadding(0, 0, 0, 0);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                params.setMarginStart(CommUtils.dip2px(context,50f));
                params.setMarginEnd(CommUtils.dip2px(context,50f));
            }
            child.setLayoutParams(params);
            child.invalidate();
        }
    }
}
