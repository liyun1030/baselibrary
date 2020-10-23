package com.telecomyt.permission.util;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;

import java.util.UUID;

/**
 * 蓝牙管理工具类
 */
public class BluetoothUtil {
    /**
     * 说明
     * <uses-permission android:name="android.permission.BLUETOOTH"/>  //使用蓝牙所需要的权限
     * <uses-permission android:name="android.permission.BLUETOOTH_ADMIN"/> //使用扫描和设置蓝牙的权限（申明这一个权限必须申明上面一个权限）
     */
    private static volatile BluetoothUtil instance;
    private BluetoothAdapter bluetoothAdapter;

    private BluetoothUtil() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter(); //获取Bluetooth适配器
    }

    public static BluetoothUtil getInstance() {
        synchronized(BluetoothUtil.class) {
            if (instance == null) {
                instance = new BluetoothUtil();
            }
        }
        return instance;
    }

    /**
     * 当前 Android 设备是否支持 Bluetooth
     *
     * @return true：支持 Bluetooth false：不支持 Bluetooth
     */
    public boolean isBluetoothSupported() {
        return bluetoothAdapter != null ? true : false;
    }

    /**
     * 当前 Android 设备的 bluetooth 是否已经开启
     *
     * @return true：Bluetooth 已经开启 false：Bluetooth 未开启
     */
    @SuppressLint("MissingPermission")
    public boolean isBluetoothEnabled() {
        if (bluetoothAdapter != null) {
            return bluetoothAdapter.isEnabled();
        }

        return false;
    }

    /**
     * 强制开启当前 Android 设备的 Bluetooth
     *
     * @return true：强制打开 Bluetooth　成功　false：强制打开 Bluetooth 失败
     */
    @SuppressLint("MissingPermission")
    public boolean turnOnBluetooth() {
        if (bluetoothAdapter != null) {
            return bluetoothAdapter.enable();
        }

        return false;
    }

    /**
     * 强制开启当前 Android 设备的 Bluetooth
     *
     * @return true：强制打开 Bluetooth　成功　false：强制打开 Bluetooth 失败
     */
    @SuppressLint("MissingPermission")
    public boolean closeBluetooth() {
        if (bluetoothAdapter != null) {
            //            bluetoothAdapter.cancelDiscovery();
            return bluetoothAdapter.disable();
        }
        return false;
    }

}
