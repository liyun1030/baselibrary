package com.common.base.tool;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.common.base.R;
import com.sina.weibo.sdk.utils.MD5;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Calendar;
import java.util.List;


/**
 * created by 李云 on 2019/6/17
 * 本类的作用:
 */
public class AndroidUtil {
    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dp2px(Context context, int dp) {
        return Math.round(TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics()));
    }

    public static void openActivity(Activity activity, Class<?> pClass) {
        openActivity(activity, pClass, null);
    }

    public static void openActivity(Activity activity, Class<?> pClass, Bundle bundle) {
        openActivity(activity, pClass, bundle, R.anim.left_to_right_close, R.anim.right_to_left_open);
    }

    public static void openActivity(Activity activity, Intent intent, boolean isFinish) {
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.left_to_right_close, R.anim.right_to_left_open);
        if (isFinish) {
            activity.finish();
        }
    }

    public static void openActivity(Activity activity, Class<?> pClass, Bundle bundle, int enterAnim, int exitAnim) {
        Intent intent = new Intent(activity, pClass);
        if (null != bundle) {
            intent.putExtras(bundle);
        }
        activity.startActivity(intent);
//        activity.overridePendingTransition(enterAnim, exitAnim);
    }

    public static void openActivity(Activity activity, String action) {
        openActivity(activity, action, null);
    }

    public static void finishActivity(Context ctx, boolean isFinish) {
        if (isFinish) {
            ((Activity) ctx).finish();
        }
    }

    public static void openActivity(Activity activity, String action, Bundle bundle) {
        Intent intent = new Intent(action);
        if (null != bundle) {
            intent.putExtras(bundle);
        }
        activity.startActivity(intent);
    }

    public static void openActivityForResult(Activity activity, Intent intent, int requestCode) {
        activity.startActivityForResult(intent, requestCode);
        activity.overridePendingTransition(R.anim.left_to_right_close, R.anim.right_to_left_open);
    }

    /**
     * 隐藏键盘
     */
    public static void hideInputMethod(View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void openActivityForResult(Activity activity, String action, int requestCode) {
        openActivityForResult(activity, action, null, requestCode);
    }

    public static void openActivityForResult(Activity activity, String action, Bundle bundle, int requestCode) {
        Intent intent = new Intent(action);
        if (null != bundle) {
            intent.putExtras(bundle);
        }
        activity.startActivityForResult(intent, requestCode);
    }

    public static void FragmentOpenActivity(Fragment fragment, Class<?> pClass) {
        Intent intent = new Intent(fragment.getContext(), pClass);
        fragment.startActivity(intent);
        fragment.getActivity().overridePendingTransition(R.anim.left_to_right_close, R.anim.right_to_left_open);
    }

    public static void FragmentOpenActivityForResult(Fragment fragment, Class<?> pClass, int requestCode) {
        Intent intent = new Intent(fragment.getContext(), pClass);
        fragment.startActivityForResult(intent, requestCode);
        fragment.getActivity().overridePendingTransition(R.anim.left_to_right_close, R.anim.right_to_left_open);
    }

    /**
     * @param bm        传入的bitmap
     * @param newWidth  指定的图片宽
     * @param newHeight 指定的图片高
     * @return newbm   返回 bitmap类型
     */
    public static Bitmap setImgSize(Bitmap bm, int newWidth, int newHeight) {
        // 获得图片的宽高.
        int width = bm.getWidth();
        int height = bm.getHeight();
        // 计算缩放比例.
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 取得想要缩放的matrix参数.
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片.
        Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
        return newbm;
    }

    /**
     * 判断list是否为空
     */
    public static boolean isListEmpty(List list) {
        if (list == null || list.size() == 0) {
            return true;
        }
        return false;
    }

    /**
     * 隐藏键盘
     * @param act
     */
    public static void hideInputMethod(Activity act) {
        WindowManager.LayoutParams params = act.getWindow().getAttributes();
        // 隐藏软键盘
        act.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        params.softInputMode = WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN;
    }
    /**
     * 获取屏幕密度
     *
     * @param context
     * @return
     */
    public static float density(Context context) {
        return context.getResources().getDisplayMetrics().density;
    }

    /**
     * 将px值转换为dip或dp值，保证尺寸大小不变
     *
     * @param pxValue （DisplayMetrics类中属性density）
     * @return
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 将dip或dp值转换为px值，保证尺寸大小不变
     *
     * @param dipValue （DisplayMetrics类中属性density）
     * @return
     */
    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param pxValue （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /***
     * 获取屏幕宽度的像素值
     *
     * @param activity
     * @return
     */
    public static int getWidthPixels(Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay()
                .getMetrics(dm);
        return dm.widthPixels;
    }

    /***
     * 获取屏幕宽度的像素值
     *
     * @param context
     * @return
     */
    public static int getWidthPixels(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.widthPixels;
    }

    /***
     * 获取屏幕高度的像素值
     *
     * @param activity
     * @return
     */
    public static int getHeightPixels(Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay()
                .getMetrics(dm);
        return dm.heightPixels;
    }

    /***
     * 获取屏幕高度的像素值
     *
     * @param context
     * @return
     */
    public static int getHeightPixels(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.heightPixels;
    }

    public static void startActivity(Context ctx, Intent intent, boolean isFinish) {
        ctx.startActivity(intent);
        if (isFinish) {
            ((Activity) ctx).finish();
        }
    }

    /**
     * 图片上传拼串
     *
     * @return
     */
    public static String listToString(List list, char separator) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            if (i == list.size() - 1) {
                sb.append(list.get(i));
            } else {
                sb.append(list.get(i));
                sb.append(separator);
            }
        }
        return sb.toString();
    }

    /**
     * 获取屏幕长宽比
     *
     * @param context
     * @return
     */
    public static float getScreenRate(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        float H = dm.heightPixels;
        float W = dm.widthPixels;
        return (H / W);
    }

    public static float dp2px(float value) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, Resources.getSystem().getDisplayMetrics());
    }

    public static float sp2px(float value) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, value, Resources.getSystem().getDisplayMetrics());
    }

    /**
     * 检查是否有sd卡
     */
    public static boolean checkSDCardAvailable() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 把view保存为图片
     */
    public static void viewSaveToImage(Context ctx, View view) {
        view.setDrawingCacheEnabled(true);
        view.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        view.setDrawingCacheBackgroundColor(Color.WHITE);

        // 把一个View转换成图片
        Bitmap cachebmp = loadBitmapFromView(view);
        FileOutputStream fos = null;
        File file = null;
        String fileName = Calendar.getInstance().getTimeInMillis() + ".png";
        try {
            // 判断手机设备是否有SD卡
            boolean isHasSDCard = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
            if (isHasSDCard) {
                // SD卡根目录
                //系统相册目录
                String galleryPath = Environment.getExternalStorageDirectory()
                        + File.separator + Environment.DIRECTORY_DCIM
                        + File.separator + "Camera" + File.separator;
                file = new File(galleryPath, fileName);
                fos = new FileOutputStream(file);
            } else {
                CommUtils.showToast(ctx, "保存失败!");
            }
            cachebmp.compress(Bitmap.CompressFormat.PNG, 90, fos);
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        CommUtils.showToast(ctx, "保存成功!");
        //通知相册更新
        MediaStore.Images.Media.insertImage(ctx.getContentResolver(),
                cachebmp, fileName, null);
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uri = Uri.fromFile(file);
        intent.setData(uri);
        ctx.sendBroadcast(intent);
        view.destroyDrawingCache();
    }

    public static void saveTmpPic(View view, String filePath) {
        String dir=FileUtils.getAppDir() + "images";
        File dirFile=new File(dir);
        if(!dirFile.exists()){
            dirFile.mkdirs();
        }
        view.setDrawingCacheEnabled(true);
        view.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        view.setDrawingCacheBackgroundColor(Color.WHITE);

        // 把一个View转换成图片
        Bitmap cachebmp = loadBitmapFromView(view);
        FileOutputStream fos = null;
        File file = null;
        try {
            // 判断手机设备是否有SD卡
            boolean isHasSDCard = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
            if (isHasSDCard) {
                // SD卡根目录
                file = new File(filePath);
                fos = new FileOutputStream(file);
            } else {
                return;
            }
            cachebmp.compress(Bitmap.CompressFormat.PNG, 90, fos);
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        view.destroyDrawingCache();
    }

    public static Bitmap loadBitmapFromView(View v) {
        int w = v.getWidth();
        int h = v.getHeight();

        Bitmap bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bmp);
        c.drawColor(Color.WHITE);
        /** 如果不设置canvas画布为白色，则生成透明 */
        v.layout(0, 0, w, h);
        v.draw(c);
        return bmp;
    }
    public static void deleteTmpPic(String filePath){
        try{
            File file=new File(filePath);
            if(file.exists()){
                file.delete();
            }
        }catch (Exception e){}
    }

    /**
     * 方法描述：判断某一应用是否正在运行
     * Created by cafeting on 2017/2/4.
     * @param context   上下文
     * @param packageName 应用的包名
     * @return true 表示正在运行，false 表示没有运行
     */
    public static boolean isAppRunning(Context context, String packageName) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(100);
        if (list.size() <= 0) {
            return false;
        }
        for (ActivityManager.RunningTaskInfo info : list) {
            if (info.baseActivity.getPackageName().equals(packageName)) {
                return true;
            }
        }
        return false;
    }
    //获取已安装应用的 uid，-1 表示未安装此应用或程序异常
    public static int getPackageUid(Context context, String packageName) {
        try {
            ApplicationInfo applicationInfo = context.getPackageManager().getApplicationInfo(packageName, 0);
            if (applicationInfo != null) {
                return applicationInfo.uid;
            }
        } catch (Exception e) {
            return -1;
        }
        return -1;
    }
    /**
     * 判断某一 uid 的程序是否有正在运行的进程，即是否存活
     * Created by cafeting on 2017/2/4.
     *
     * @param context   上下文
     * @param uid 已安装应用的 uid
     * @return true 表示正在运行，false 表示没有运行
     */
    public static boolean isProcessRunning(Context context, int uid) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> runningServiceInfos = am.getRunningServices(200);
        if (runningServiceInfos.size() > 0) {
            for (ActivityManager.RunningServiceInfo appProcess : runningServiceInfos){
                if (uid == appProcess.uid) {
                    return true;
                }
            }
        }
        return false;
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
        return MD5.hexdigest(result);
    }
    public static boolean isAppActive(Context context,String packageName){
        int uid = getPackageUid(context, packageName);
        if(uid > 0){
            boolean rstA = isAppRunning(context, packageName);
            boolean rstB = isProcessRunning(context, uid);
            if(rstA||rstB){
                //指定包名的程序正在运行中
                return true;
            }else{
                //指定包名的程序未在运行中
                return false;
            }
        }else{
            //应用未安装
            return false;
        }
    }
}
