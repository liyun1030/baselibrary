package com.basely.permission.util;

import android.content.Context;
import android.os.BatteryManager;
import android.os.Build;
import android.os.PowerManager;

import androidx.annotation.RequiresApi;

import java.lang.reflect.Method;

import static android.content.Context.BATTERY_SERVICE;

/**
 * 电源管理
 */
public class PowerManagerUtil {
    private static volatile PowerManagerUtil instance;
    private Context context;

    private PowerManagerUtil(Context ctx) {
        this.context = ctx;
    }

    public static PowerManagerUtil getInstance(Context ctx) {
        if (instance == null) {
            synchronized (PowerManagerUtil.class) {
                if (instance == null) {
                    instance = new PowerManagerUtil(ctx);
                }
            }
        }
        return instance;
    }

    /**
     * 获取电量百分比
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public int getBatteryInfo() {
        BatteryManager manager = (BatteryManager) context.getSystemService(BATTERY_SERVICE);
        return manager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);///当前电量百分比
    }

    /**
     * 当前屏幕是否点亮,true为亮，false为灭
     */
    public boolean isScreenLight() {
        PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        //true为打开，false为关闭
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            return powerManager.isInteractive();
        } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT_WATCH) {
            return powerManager.isScreenOn();
        }
        return false;
    }

    /**
     * 关机
     */
    public void shutDowm() {
        try {
            //获得ServiceManager类
            Class ServiceManager = Class
                    .forName("android.os.ServiceManager");
            //获得ServiceManager的getService方法
            Method getService = ServiceManager.getMethod("getService", String.class);
            //调用getService获取RemoteService
            Object oRemoteService = getService.invoke(null, Context.POWER_SERVICE);
            //获得IPowerManager.Stub类
            Class cStub = Class
                    .forName("android.os.IPowerManager$Stub");
            //获得asInterface方法
            Method asInterface = cStub.getMethod("asInterface", android.os.IBinder.class);
            //调用asInterface方法获取IPowerManager对象
            Object oIPowerManager = asInterface.invoke(null, oRemoteService);
            //获得shutdown()方法
            Method shutdown = oIPowerManager.getClass().getMethod("shutdown", boolean.class, boolean.class);
            //调用shutdown()方法
            shutdown.invoke(oIPowerManager, false, true);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
