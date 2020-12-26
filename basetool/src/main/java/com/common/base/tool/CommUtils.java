package  com.common.base.tool;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Looper;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.NotificationManagerCompat;

import com.common.base.R;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 工具类
 * Created by Administrator on 2015/9/28.
 */
public class CommUtils {
    /**
     * 获取屏幕宽度
     *
     * @return
     */
    public static int getWindowWidth(Context context) {
        WindowManager wm = (WindowManager) (context.getSystemService(Context.WINDOW_SERVICE));
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    /**
     * 获取屏幕高度
     *
     * @param context
     * @return
     */
    public static int getWindowHeight(Context context) {
        WindowManager wm = (WindowManager) (context.getSystemService(Context.WINDOW_SERVICE));
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
    }

    /**
     * 获取控件在屏幕中的坐标
     *
     * @param view
     * @return
     */
    public static int[] getLocOnScreen(View view) {
        int[] pos = new int[2];
        view.getLocationOnScreen(pos);
        return pos;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     *
     * @param context
     * @param dpValue
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     *
     * @param context
     * @param dp
     */
    public static int dp2px(Context context, int dp) {
        return Math.round(TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics()));
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     *
     * @param context
     * @param pxValue
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param pxValue
     * @return
     */
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue
     * @return
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * 显示Toast
     *
     * @param ctx
     * @param resId
     */
    public static void showToast(Context ctx, int resId) {
        if (ctx == null) return;
        Toast.makeText(ctx, resId, Toast.LENGTH_SHORT).show();
    }
    /**
     * 显示Toast
     *
     * @param ctx
     * @param string
     */
    public static Toast getCustomToast(Context ctx, String string, int duration) {
        if (ctx == null) return null;
        if (TextUtils.isEmpty(string)) return null;
        LayoutInflater inflater = LayoutInflater.from(ctx);
        View view = inflater.inflate(R.layout.activity_custom_toast, null);
        TextView msgTxt = view.findViewById(R.id.msgTxt);
        msgTxt.setText(string);
        Toast toast = new Toast(ctx);
        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 200);
        toast.setDuration(duration);
        toast.setView(view);
        return toast;
    }
    /**
     * 显示Toast
     *
     * @param ctx
     * @param string
     */
    public static void showToast(Context ctx, String string) {
        if (ctx == null) return;
        if (TextUtils.isEmpty(string)) return;
        show(ctx, string, Toast.LENGTH_SHORT);
    }

    public static void show(Context context, String message, int duration) {
        if (TextUtils.isEmpty(message)) {
            return;
        }
        //后setText 兼容小米默认会显示app名称的问题
        //后setText 兼容小米默认会显示app名称的问题
        Toast toast = getCustomToast(context, message, duration);
        if (toast == null) return;
        if (isNotificationEnabled(context)) {
            toast.show();
        } else {
            showSystemToast(toast);
        }
    }

    /**
     * 显示系统Toast
     */
    private static void showSystemToast(Toast toast) {
        try {
            Method getServiceMethod = Toast.class.getDeclaredMethod("getService");
            getServiceMethod.setAccessible(true);
            final Object iNotificationManagerObj = getServiceMethod.invoke(null);

            Class iNotificationManagerCls = Class.forName("android.app.INotificationManager");
            Object iNotificationManagerProxy = Proxy.newProxyInstance(toast.getClass().getClassLoader(), new Class[]{iNotificationManagerCls}, new InvocationHandler() {
                @Override
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                    //强制使用系统Toast
                    if ("enqueueToast".equals(method.getName())
                            || "enqueueToastEx".equals(method.getName())) {  //华为p20 pro上为enqueueToastEx
                        args[0] = "android";
                    }
                    return method.invoke(iNotificationManagerObj, args);
                }
            });
            Field sServiceFiled = Toast.class.getDeclaredField("sService");
            sServiceFiled.setAccessible(true);
            sServiceFiled.set(null, iNotificationManagerProxy);
            toast.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 消息通知是否开启
     *
     * @return
     */
    private static boolean isNotificationEnabled(Context context) {
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        boolean areNotificationsEnabled = notificationManagerCompat.areNotificationsEnabled();
        return areNotificationsEnabled;
    }

    public static boolean isMainThread() {
        return Looper.getMainLooper() == Looper.myLooper();
    }

    /**
     * 获取指定长度的随机字条串
     *
     * @param length
     * @return
     */
    public static String getRandomString(int length) {
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            sb.append(base.charAt(random.nextInt(base.length())));
        }
        return sb.toString();
    }

    /**
     * 根据wifi获取ip地址
     *
     * @param ctx
     * @return
     */
    public static String getIp4Wifi(Context ctx) {
        //获取wifi服务
        WifiManager wifiManager = (WifiManager) ctx.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        //判断wifi是否开启
        if (!wifiManager.isWifiEnabled()) {
            return "";
        }
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int ipAddress = wifiInfo.getIpAddress();
        return (ipAddress & 0xFF) + "." +
                ((ipAddress >> 8) & 0xFF) + "." +
                ((ipAddress >> 16) & 0xFF) + "." +
                (ipAddress >> 24 & 0xFF);
    }

    /**
     * 根据Gprs获取ip
     *
     * @return
     */
    public static String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException ex) {
            Log.e("WifiPreferenceIpAddress", ex.toString());
        }
        return null;
    }

    /**
     * 时间格式化
     *
     * @param millis
     * @return
     */
    public static String formatTime(long millis) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss", Locale.CHINA);    //"yyyy-MM-dd HH:mm:ss"
        format.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        Date date = new Date(millis);
        return format.format(date);
    }

    /**
     * 时间格式化
     *
     * @param millis
     * @param formatStr 格式化样式："yyyy-MM-dd HH:mm:ss"
     * @return
     */
    public static String formatTime2(long millis, String formatStr) {
        SimpleDateFormat format = new SimpleDateFormat(formatStr, Locale.CHINA);    //"yyyy-MM-dd HH:mm:ss"
        format.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        Date date = new Date(millis);
        return format.format(date);
    }

    /**
     * 时间格式化
     *
     * @param millis
     * @param timeZone 时区。如：GMT+8
     * @return
     */
    public static String formatTime(long millis, String timeZone) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);    //"yyyy-MM-dd HH:mm:ss"
        if (timeZone != null) {
            String[] zoneIDs = TimeZone.getAvailableIDs();
            for (String zone : zoneIDs) {
                if (timeZone.equals(zone)) {
                    format.setTimeZone(TimeZone.getTimeZone(timeZone));
                    break;
                }
            }
        }
        Date date = new Date(millis);
        return format.format(date);
    }

    /**
     * 时间格式化
     *
     * @param millis
     * @return
     */
    public static String formatTimeWithYMD(long millis, String style) {
        SimpleDateFormat format = new SimpleDateFormat(style, Locale.CHINA);    //"yyyy-MM-dd HH:mm:ss"
        Date date = new Date(millis);
        return format.format(date);
    }

    /**
     * 时间--date
     *
     * @param dateString
     * @return
     */
    public static Date stringToDate(String dateString) {
        ParsePosition position = new ParsePosition(0);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        return simpleDateFormat.parse(dateString, position);
    }

    public static String getBase64(String text) {
        if (TextUtils.isEmpty(text)) {
            return "";
        } else {
            return new String(Base64.encode(text.getBytes(), Base64.DEFAULT));
        }
    }

    /**
     * 获取列表长度
     *
     * @param list
     * @return
     */
    public static int getSize(List list) {
        return list == null ? 0 : list.size();
    }


    /**
     * 邮箱验证
     *
     * @param email
     * @return
     */
    public static boolean verifyEmail(String email) {
        if (!TextUtils.isEmpty(email.trim())) {
            Pattern pattern = Pattern.compile("^(\\w-*\\.*)+@(\\w-?)+(\\.\\w{2,})+$");  //^\w+[-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$
            Matcher matcher = pattern.matcher(email);
            return matcher.matches();
        }
        return false;
    }

    /**
     * 昵称验证
     *
     * @param nickname
     * @return
     */
    public static boolean verifyNickname(String nickname) {
        if (!TextUtils.isEmpty(nickname)) {
            Pattern pattern = Pattern.compile("^[a-zA-Z0-9\\u4e00-\\u9fa5]+$");
            Matcher matcher = pattern.matcher(nickname);
            return matcher.matches();
        }
        return false;
    }

    /**
     * 手机号验证
     *
     * @param phone_num
     * @return
     */
    public static boolean verifyPhoneNum(String phone_num) {
        if (!TextUtils.isEmpty(phone_num.trim())) {
            Pattern pattern = Pattern.compile("^1\\d{10}$");
            Matcher matcher = pattern.matcher(phone_num);
            return matcher.matches();
        }
        return false;
    }

    /**
     * 获取网络状态
     *
     * @param ctx
     * @return -1:无网络;0:移动网络;1:wifi网络;2:以太网
     */
    public static int getNetworkStatus(Context ctx) {
        int status = -1;
        ConnectivityManager manager = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        // NetworkInfo mobileInfo =
        // manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        // NetworkInfo wifiInfo =
        // manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo netInfo = manager.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isAvailable()) {
            if (netInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                ///// WiFi网络
                status = 1;
            } else if (netInfo.getType() == ConnectivityManager.TYPE_ETHERNET) {
                ///// 有线网络
                status = 2;
            } else if (netInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                ///////// 3g网络
                status = 0;
            }
        } else {
            status = -1;
        }
        return status;
    }

    /**
     * 获取wifi状态
     *
     * @param ctx
     * @return
     */
    public static boolean getWifiStatus(Context ctx) {
        WifiManager wifiManager = (WifiManager) ctx.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int state = wifiInfo.getNetworkId();
        return state != -1;
    }

    /**
     * 获取wifi名称（SSID）
     *
     * @param ctx
     * @return
     */
    public static String getWifiName(Context ctx) {
        WifiManager wifiManager = (WifiManager) ctx.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        Log.d("wifiInfo", wifiInfo.toString());
        Log.d("SSID", wifiInfo.getSSID());
        return wifiInfo.getSSID();
    }

    /**
     * 获得版本号
     *
     * @param context
     * @return
     */
    public static String getVersion(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            return info.versionName;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 获取包名
     *
     * @param context
     * @return
     */
    public static String getPkgName(Context context) {
        return context.getPackageName();
    }

    public static boolean isEmpty(String str) {
        return str == null || TextUtils.isEmpty(str.trim()) || "null".equals(str.trim());
    }

    /**
     * 将字符串格式化为不同大小
     *
     * @param txt
     * @param size
     * @param start
     * @param end
     * @return
     */
    public static SpannableString formatTextSize(String txt, int size, int start, int end) {
        SpannableString spanString = new SpannableString(txt);
        AbsoluteSizeSpan span = new AbsoluteSizeSpan(size, true);
        spanString.setSpan(span, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spanString;
    }

    /**
     * 将字符串格式化为不同大小
     *
     * @param txt
     * @param color
     * @param start
     * @param end
     * @return
     */
    public static SpannableString formatTextColor(String txt, int color, int start, int end) {
        SpannableString spanString = new SpannableString(txt);
        ForegroundColorSpan span = new ForegroundColorSpan(color);
        spanString.setSpan(span, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spanString;
    }

    /**
     * base64加密
     *
     * @param msg
     * @param extra 加密冗余字段
     * @return
     */
    public static String encodeBase64(String msg, String extra) {
        if (TextUtils.isEmpty(msg)) {
            return null;
        }
        String txt = Base64.encodeToString(msg.getBytes(), Base64.DEFAULT) + extra;
        return Base64.encodeToString(txt.getBytes(), Base64.DEFAULT);
    }

    /**
     * base64解密
     *
     * @param base64
     * @param extra  加密冗余字段
     * @return
     */
    public static String decodeBase64(String base64, String extra) {
        if (TextUtils.isEmpty(base64)) {
            return null;
        }
        String txt = new String(Base64.decode(base64, Base64.DEFAULT));
        return new String(Base64.decode(txt.substring(0, txt.length() - extra.length()), Base64.DEFAULT));
    }

    /**
     * 分数格式化显示
     *
     * @param score
     * @return
     */
    public static String formatScore(float score) {
        String scoreStr = score + "";
        if (scoreStr.endsWith(".0")) {
            return scoreStr.substring(0, scoreStr.length() - 2);
        } else {
            return scoreStr;
        }
    }

    /**
     * 治疗时间格式化
     *
     * @param millis 毫秒
     * @return
     */
    public static String formatCureTime(long millis) {
        long time = millis / 1000;
        long hour = time / 3600;
        long minute = time % 3600 / 60;
        long second = time - 3600 * hour - minute * 60;
        return formatTimeZero(hour) + ":" + formatTimeZero(minute) + ":" + formatTimeZero(second);
    }

    /**
     * 治疗时间格式化
     *
     * @param time 秒
     * @return
     */
    public static String formatCureTimeForSecond(long time) {
        long hour = time / 3600;
        long minute = time % 3600 / 60;
        long second = time - 3600 * hour - minute * 60;
        return formatTimeZero(hour) + ":" + formatTimeZero(minute) + ":" + formatTimeZero(second);
    }

    /**
     * 治疗时间格式化
     * 返回类型 100'100"
     *
     * @param time 秒
     * @return
     */
    public static String formatCureTimeToMinuteAndSecond(long time) {
        long minute = time / 60;
        long second = time - minute * 60;
        return minute + "'" + second + "\"";
    }

    /**
     * 治疗时间格式化
     * 返回类型 100分20秒
     *
     * @param time 秒
     * @return
     */
    public static String formatCureTimeToMinuteAndSecondChinese(long time) {
        long minute = time / 60;
        long second = time - minute * 60;
        return minute + "分" + second + "秒";
    }

    public static String formatTimeChinese(long millis) {
        long time = millis / 1000;
        long hour = time / 3600;
        long minute = time % 3600 / 60;
        long second = time - 3600 * hour - minute * 60;
        String hourStr = "";
        if (hour != 0) {
            hourStr = formatTimeZero(hour) + "时";
        }
        return hourStr + formatTimeZero(minute) + "分" + formatTimeZero(second);
    }

    public static String formatTimeChineseDay(long time) {
        long day = time / 86400;
        long hour = time % 86400 / 3600;
        long minute = time % 3600 / 60;
        long second = time - 3600 * hour - minute * 60;
        String hourStr = "";
        if (day != 0) {
            return day + "天" + formatTimeZero(hour) + "时";
        }
        if (hour != 0) {
            return formatTimeZero(hour) + "时" + hourStr + formatTimeZero(minute) + "分";
        }
        if (second == 0 && minute == 0) {

        }
        return hourStr + formatTimeZero(minute) + "分" + formatTimeZero(second) + "秒";
    }

    public static String formatTimeZero(long time) {
        if (time < 10) {
            return "0" + time;
        } else {
            return time + "";
        }
    }

    /**
     * 考试时间格式化
     *
     * @param millis 毫秒
     * @return
     */
    public static String formatExamTime(long millis) {
        long time = millis / 1000;
        long hour = time / 3600;
        long minute = time % 3600 / 60;
        long second = time - 3600 * hour - minute * 60;
        String hourStr = "";
        if (hour != 0) {
            hourStr = formatTimeZero(hour) + "时";
        }
        return hourStr + formatTimeZero(minute) + "分" + formatTimeZero(second) + "秒";
    }

    /**
     * 音频时间格式化
     */
    public static String formatAudioTime(int mills) {
        int time = mills / 1000;
        int minute = time / 60;
        int second = time - minute * 60;
        String m;
        if (minute < 1) {
            m = "0";
        } else {
            m = String.valueOf(minute);
        }
        String s;
        if (second < 10) {
            s = "0" + second;
        } else {
            s = String.valueOf(second);
        }
        return m + ":" + s;
    }


    public static void showKeyboard(View v) {
        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(v, 0);
    }

    public static void hideKeyboard(View v) {
        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
        }
    }

    public static boolean isKeyboardVisible(View v) {
        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        return imm.isActive();
    }

    public static boolean isSoftShowing(Activity context) {
        //获取当前屏幕内容的高度
        int screenHeight = context.getWindow().getDecorView().getHeight();
        //获取View可见区域的bottom
        Rect rect = new Rect();
        //DecorView即为activity的顶级view
        context.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        //考虑到虚拟导航栏的情况（虚拟导航栏情况下：screenHeight = rect.bottom + 虚拟导航栏高度）
        //选取screenHeight*2/3进行判断
        return screenHeight * 2 / 3 > rect.bottom;
    }


    public static String formatString(String string) {
        return TextUtils.isEmpty(string) ? "" : string;
    }

    /**
     * 小数格式化 若有小数则保留一位小数
     *
     * @param number
     * @return
     */
    public static String formatDecimal(double number) {
        double score = number * 10;
        NumberFormat df = new DecimalFormat("0");
        df.setMaximumFractionDigits(1);
        return df.format(score);
    }

    /**
     * 格式化字符串，去空格回车
     *
     * @param str
     * @return
     */
    public static String replaceBlankEnter(String str) {
        String dest = "";
        if (str != null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }

    /**
     * 结束时间格式化
     */
    public static String formatFinishTime(long time) {
        long minute = time / 60;
        long second = time - minute * 60;
        return formatTimeZero(minute) + ":" + formatTimeZero(second);
    }

    private static long lastClickTime;

    public static synchronized boolean isFastClick() {
        long time = System.currentTimeMillis();
        if (time - lastClickTime < 1000) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    public static int getStatusBarHeight(Context context) {
        Class<?> c;

        Object obj;

        Field field;

        int x, sbar = 0;

        try {

            c = Class.forName("com.android.internal.R$dimen");

            obj = c.newInstance();

            field = c.getField("status_bar_height");

            x = Integer.parseInt(field.get(obj).toString());

            sbar = context.getResources().getDimensionPixelSize(x);

        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return sbar;
    }


    public static String getMessageDate(long l) {
        Calendar cal = Calendar.getInstance();//使用日历类
        int nowYear = cal.get(Calendar.YEAR);//得到年
        int nowMonth = cal.get(Calendar.MONTH) + 1;//得到月，因为从0开始的，所以要加1
        int nowday = cal.get(Calendar.DAY_OF_MONTH);//得到天


        Calendar c = Calendar.getInstance();//使用日历类
        c.setTimeInMillis(l);
        int year = c.get(Calendar.YEAR);//得到年
        int month = c.get(Calendar.MONTH) + 1;//得到月，因为从0开始的，所以要加1
        int day = c.get(Calendar.DAY_OF_MONTH);//得到天

        SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        String t = dateFormater.format(l);

        if (nowYear != year) {
            return t;
        } else if (nowMonth != month) {
            return t.substring(2, 10);
        } else if (nowday != day) {
            return t.substring(2, 10);
        } else {
            return t.substring(11);
        }
    }

    /**
     * 获取ip地址
     *
     * @return
     */
    public static String getHostIP() {

        String hostIp = null;
        try {
            Enumeration nis = NetworkInterface.getNetworkInterfaces();
            InetAddress ia = null;
            while (nis.hasMoreElements()) {
                NetworkInterface ni = (NetworkInterface) nis.nextElement();
                Enumeration<InetAddress> ias = ni.getInetAddresses();
                while (ias.hasMoreElements()) {
                    ia = ias.nextElement();
                    if (ia instanceof Inet6Address) {
                        continue;// skip ipv6
                    }
                    String ip = ia.getHostAddress();
                    if (!"127.0.0.1".equals(ip)) {
                        hostIp = ia.getHostAddress();
                        break;
                    }
                }
            }
        } catch (SocketException e) {
            Log.i("yao", "SocketException");
            e.printStackTrace();
        }
        return hostIp;

    }

    /**
     * 以最省内存的方式读取本地资源的图片
     *
     * @param context
     * @param resId
     * @return
     */
    public static Bitmap readBitMap(Context context, int resId) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        // 获取资源图片
        InputStream is = context.getResources().openRawResource(resId);
        return BitmapFactory.decodeStream(is, null, opt);
    }

    /**
     * 时间--date
     *
     * @param dateString
     * @return
     */
    public static String stringToTimeStamp(String dateString) {
        ParsePosition position = new ParsePosition(0);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        Date parse = simpleDateFormat.parse(dateString, position);
        long time = parse.getTime();
        return String.valueOf(time);
    }

    public static String parseRMB(int fen) {
        return "¥" + String.format("%.2f", (float) (fen / 100f));
    }

    public static String parseDiscount(int discount) {
        String result = String.format("%.2f", (float) (discount / 10f)) + "";
        if (result.endsWith("0")) {
            result = result.substring(0, result.length() - 1);
        }
        if (result.endsWith("0")) {
            result = result.substring(0, result.length() - 1);
        }
        if (result.endsWith(".")) {
            result = result.substring(0, result.length() - 1);
        }
        return result + "折";
    }

    /**
     * View 中获取这个 Activity 对象
     */
    public static Activity getActivityFromView(Context context) {
        while (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                return (Activity) context;
            }
            context = ((ContextWrapper) context).getBaseContext();
        }
        return null;
    }


    /**
     * 解决popupWindow弹窗异常 Android 7.0以上
     * popupWindow 在某个控件底部
     *
     * @param pw     popupWindow
     * @param anchor v
     * @param xoff   x轴偏移
     * @param yoff   y轴偏移
     */
    public static void showAsDropDown(final PopupWindow pw, final View anchor, final int xoff, final int yoff) {
        if (Build.VERSION.SDK_INT >= 24) {
            Rect visibleFrame = new Rect();
            anchor.getGlobalVisibleRect(visibleFrame);
            int height = anchor.getResources().getDisplayMetrics().heightPixels - visibleFrame.bottom;
            pw.setHeight(height);
            pw.showAsDropDown(anchor, xoff, yoff);
        } else {
            pw.showAsDropDown(anchor, xoff, yoff);
        }
    }

    /**
     * 版本号比较
     *
     * @param v1
     * @param v2
     * @return 0代表相等，1代表左边大，-1代表右边大
     * Utils.compareVersion("1.0.358_20180820090554","1.0.358_20180820090553")=1
     */
    public static int compareVersion(String v1, String v2) {
        if (v1 == null || v1.equals("")) {
            v1 = "0";
        }
        if (v2 == null || v2.equals("")) {
            v2 = "0";
        }
        if (v1.equals(v2)) {
            return 0;
        }
        String[] version1Array = v1.split("[._]");
        String[] version2Array = v2.split("[._]");
        int index = 0;
        int minLen = Math.min(version1Array.length, version2Array.length);
        long diff = 0;

        while (index < minLen
                && (diff = Long.parseLong(version1Array[index])
                - Long.parseLong(version2Array[index])) == 0) {
            index++;
        }
        if (diff == 0) {
            for (int i = index; i < version1Array.length; i++) {
                if (Long.parseLong(version1Array[i]) > 0) {
                    return 1;
                }
            }

            for (int i = index; i < version2Array.length; i++) {
                if (Long.parseLong(version2Array[i]) > 0) {
                    return -1;
                }
            }
            return 0;
        } else {
            return diff > 0 ? 1 : -1;
        }
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


    public static String getHMS(long timetemp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timetemp);
        SimpleDateFormat fmat = new SimpleDateFormat("HH:mm:ss");
        String time = fmat.format(calendar.getTime());
        return time;
    }


    public static String getNYRHMS(long timetemp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timetemp);
        SimpleDateFormat fmat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String time = fmat.format(calendar.getTime());
        return time;
    }


    /**
     * 取出网络请求url中非法字符
     *
     * @param input
     * @param charset
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String NetUrlEncoding(String input, String charset) throws UnsupportedEncodingException {
        byte[] bytes = new byte[0];
        try {
            bytes = input.getBytes(charset);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        StringBuilder sb = new StringBuilder(bytes.length);
        for (int i = 0; i < bytes.length; ++i) {
            int cp = bytes[i] < 0 ? bytes[i] + 256 : bytes[i];
            if (cp <= 0x20 || cp >= 0x7F || (
                    cp == 0x22 || cp == 0x25 || cp == 0x3C ||
                            cp == 0x3E || cp == 0x20 || cp == 0x5B ||
                            cp == 0x5C || cp == 0x5D || cp == 0x5E ||
                            cp == 0x60 || cp == 0x7b || cp == 0x7c ||
                            cp == 0x7d
            )) {
                sb.append(String.format("%%%02X", cp));
            } else {
                sb.append((char) cp);
            }
        }
        return sb.toString();
    }


    /**
     * 返回日时分秒
     *
     * @param second
     * @return
     */
    public static String secondToTime(long second) {
        long days = second / 86400;//转换天数
        second = second % 86400;//剩余秒数
        long hours = second / 3600;//转换小时数
        second = second % 3600;//剩余秒数
        long minutes = second / 60;//转换分钟
        second = second % 60;//剩余秒数
//        if (0 < days){
//            return days + "天，"+hours+"小时，"+minutes+"分，"+second+"秒";
//        }else {
//            return hours+"小时，"+minutes+"分，"+second+"秒";
//        }
        if (days * 24 + hours == 0 && minutes > 0) return minutes + ":" + second;
        if (days * 24 + hours > 0) return (days * 24 + hours) + ":" + minutes + ":" + second;
        return second + "";
    }

    public static void HideSoftKeyBoard(View v) {
        InputMethodManager imm = ((InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE));
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 显示Toast
     *
     * @param ctx
     * @param string
     */
    public static void showMutiWindowToast(Context ctx, String string) {
        if (ctx == null) return;
        if (TextUtils.isEmpty(string)) return;
        LayoutInflater inflater = LayoutInflater.from(ctx);
        View view = inflater.inflate(R.layout.activity_multi_window, null);
        TextView msgTxt = view.findViewById(R.id.msgTxt);
        msgTxt.setText(string);
        Toast toast = new Toast(ctx);
        toast.setGravity(Gravity.CENTER, 0, -100);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(view);
        toast.show();
    }

    //获取近半年
    public static ArrayList<String> getCurHalfYear() {
        ArrayList<String> halfMonth = new ArrayList<>();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM", Locale.CHINA);
        String ym = format.format(new Date());
        halfMonth.add(getLast12Months(5));
        halfMonth.add(ym);
        return halfMonth;
    }

    private static String getLast12Months(int i) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM", Locale.CHINA);
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.MONTH, -i);
        Date m = c.getTime();
        return sdf.format(m);
    }

    public static int getMonthDiff(Calendar c1, Calendar c2) {
        int year1 = c1.get(Calendar.YEAR);//获取年
        int year2 = c2.get(Calendar.YEAR);
        int month1 = c1.get(Calendar.MONTH);//月份
        int month2 = c2.get(Calendar.MONTH);
        int day1 = c1.get(Calendar.DAY_OF_MONTH);//日
        int day2 = c2.get(Calendar.DAY_OF_MONTH);
        // 获取年的差值
        int yearInterval = year1 - year2;
        // 如果 d1的 月-日 小于 d2的 月-日 那么 yearInterval-- 这样就得到了相差的年数
        if (month1 < month2 || month1 == month2 && day1 < day2)
            yearInterval--;
        // 获取月数差值
        int monthInterval = (month1 + 12) - month2;
        if (day1 < day2)
            monthInterval--;
        monthInterval %= 12;
        int monthsDiff = Math.abs(yearInterval * 12 + monthInterval);
        return monthsDiff;
    }

    public static SpannableString matcherSearchTitle(int color, String text, String keyword) {
        SpannableString s = new SpannableString(text);
        if (TextUtils.isEmpty(text) && TextUtils.isEmpty(keyword) && text.contains(keyword)) {
            Pattern p = Pattern.compile(keyword);
            Matcher m = p.matcher(s);
            while (m.find()) {
                int start = m.start();
                int end = m.end();
                s.setSpan(new ForegroundColorSpan(color), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        return s;
    }
}
