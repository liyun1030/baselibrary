package com.common.base.model;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.common.base.activity.SwitchAuthUrlActivity;
import com.common.base.listener.OnAuthUrlSwitchListener;
import com.common.base.widgets.FloatViewPresenter;

import java.util.ArrayList;
import java.util.List;

public class DevelopModel {
    private final static String TAG = "DevelopModel";
    private static String mAppName;//使用本组件的APP名称
    public static OnAuthUrlSwitchListener mListener;
    private static int clickCount = 0;
    private final static int DEVELOP_MODE_STEP_COUNT = 8;
    private static long lastTime = 0;
    public static List<DevelopModeVo> customModes = new ArrayList<>();//开发者自定义的mode
    public static void init(String appName){
        Log.d(TAG,"DevelopModel init");
        Log.i(TAG,appName+"使用了开发者模式业务组件");
        mAppName = appName;
    }

    /**
     * 进入开发者模式
     * 需要传入一个view。
     * 通过这个view的长按事件来触发开发者模式
     * 模仿android系统进入开发者模式的方法进行
     */
    public static void stepIntoDevelopMode(View view, final OnAuthUrlSwitchListener listener){
        if (mAppName == null|| TextUtils.isEmpty(mAppName)){
            Log.e(TAG,"使用本组件需要初始化，请确认您是否调用了DevelopModel.init");
            return;
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickCount++;
                if (clickCount == 1){
                    lastTime = System.currentTimeMillis();
                }
                if (clickCount >= DEVELOP_MODE_STEP_COUNT){
                    clickCount = 0;
                    if (System.currentTimeMillis()-lastTime<=5000){
                        stepIntoDevelopMode(view.getContext(),listener);
                    }
                    lastTime = 0;
                }
            }
        });

    }

    private static FloatViewPresenter presenter;
    /**
     * 同时开放一个无参数的api给业务使用，
     * 可能前端会使用不同的方式来进行调用而进入开发者模式
     */
    public static void stepIntoDevelopMode(Context context,OnAuthUrlSwitchListener listener){
        Log.i(TAG,"用户尝试进入开发者模式");
        mListener = listener;
        if (presenter == null){
            presenter = new FloatViewPresenter();
        }
        presenter.initFloatView(context);
    }

    /**
     * 新增一个自定义模块
     * @param mode 模块标识符
     * @param modelText 模块文案
     * @param onClickListener 点击事件
     * @param textAfter 点击后的文案
     */
    public static void addModel(int mode, String modelText,String textAfter, View.OnClickListener onClickListener){
        DevelopModeVo developModeVo = new DevelopModeVo(modelText,mode,textAfter,onClickListener);
        if (!customModes.contains(developModeVo)){
            customModes.add(developModeVo);
        }
    }


    public static void onActivityResult(int requestCode, int resultCode, Intent data,Context context) {
        presenter.onActivityResult(requestCode,resultCode,data,context);
    }

    /**
     * 提供一个函数，自己设置各个认证中心URL
     * @param marketUrl
     * @param testUrl
     * @param testPreUrl
     * @param onLineUrl
     * @param onlinePreUrl
     * @param selfSetUrl
     */
    public static void setAuthUrls(String marketUrl,String testUrl,String testPreUrl,String onLineUrl,String onlinePreUrl,String selfSetUrl,String developUrl){
        if (!TextUtils.isEmpty(marketUrl)) {
            SwitchAuthUrlActivity.MARKET = marketUrl;
        }
        if (!TextUtils.isEmpty(testUrl)) {
            SwitchAuthUrlActivity.TEST = testUrl;
        }
        if (!TextUtils.isEmpty(testPreUrl)) {
            SwitchAuthUrlActivity.TEST_PRE = testPreUrl;
        }
        if (!TextUtils.isEmpty(onLineUrl)) {
            SwitchAuthUrlActivity.ONLINE = onLineUrl;
        }
        if (!TextUtils.isEmpty(onlinePreUrl)) {
            SwitchAuthUrlActivity.ONLINE_PRE = onlinePreUrl;
        }
        if (!TextUtils.isEmpty(selfSetUrl)) {
            SwitchAuthUrlActivity.SELF_CHOOSE = selfSetUrl;
        }
        if (!TextUtils.isEmpty(developUrl)) {
            SwitchAuthUrlActivity.DEVELOP_URL = developUrl;
        }

    }
    /**
     * 隐藏开发者模式
     */
    public static void hide(){
        if (presenter!=null){
            presenter.hide();
        }
    }

    /**
     * 恢复开发者模式
     */
    public static void show(){
        if (presenter!=null){
            presenter.show();
        }
    }

}
