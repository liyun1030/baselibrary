package com.basely.permission.util;

import java.util.List;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;

import com.zdf.activitylauncher.ActivityLauncher;

import me.weyye.hipermission.HiPermission;

/**
 * 获取用户的地理位置
 */
public class LocationUtil {

    private static LocationUtil instance;
    private Context mContext;
    private LocationManager locationManager;

    private LocationUtil(Context context) {
        this.mContext = context;
    }

    public static LocationUtil getInstance(Context context) {
        if (instance == null) {
            instance = new LocationUtil(context);
        }
        return instance;
    }

    /**
     * 获取经纬度
     *
     * @return
     */
    @SuppressLint("MissingPermission")
    public String getLngAndLat(OnLocationResultListener onLocationResultListener) {
        double latitude = 0.0;
        double longitude = 0.0;

        mOnLocationListener = onLocationResultListener;

        String locationProvider = null;
        locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        //获取所有可用的位置提供器
        List<String> providers = locationManager.getProviders(true);

        if (providers.contains(LocationManager.GPS_PROVIDER)) {
            //如果是GPS
            locationProvider = LocationManager.GPS_PROVIDER;
        } else if (providers.contains(LocationManager.NETWORK_PROVIDER)) {
            //如果是Network
            locationProvider = LocationManager.NETWORK_PROVIDER;
        } else {
            Intent i = new Intent();
            i.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            mContext.startActivity(i);
            return null;
        }

        //获取Location
        Location location = locationManager.getLastKnownLocation(locationProvider);
        if (location != null) {
            //不为空,显示地理位置经纬度
            if (mOnLocationListener != null) {
                mOnLocationListener.onLocationResult(location);
            }

        }
        //监视地理位置变化
        locationManager.requestLocationUpdates(locationProvider, 3000, 1, locationListener);
        return null;
    }
    /**
     * 定位权限检查-精确位置
     */
    public boolean checkPermission() {
        if (HiPermission.checkPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION)) {
            return true;
        }
        return false;
    }
    public boolean isOpenGps(){
        LocationManager lm = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        boolean ok = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (ok) {//开启了定位服务
            if (Build.VERSION.SDK_INT >= 23) {
                if (checkPermission()) {
                    //开启了定位权限
                   return true;
                } else {
                   return false;
                }
            } else {
                //6.0以下不需要
                return true;
            }
        } else {//未开启定位服务
           return false;
        }
    }

    public LocationListener locationListener = new LocationListener() {

        // Provider的状态在可用、暂时不可用和无服务三个状态直接切换时触发此函数
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        // Provider被enable时触发此函数，比如GPS被打开
        @Override
        public void onProviderEnabled(String provider) {

        }

        // Provider被disable时触发此函数，比如GPS被关闭
        @Override
        public void onProviderDisabled(String provider) {

        }

        //当坐标改变时触发此函数，如果Provider传进相同的坐标，它就不会被触发
        @Override
        public void onLocationChanged(Location location) {
            if (mOnLocationListener != null) {
                mOnLocationListener.OnLocationChange(location);
            }
        }
    };

    @SuppressLint("MissingPermission")
    public void removeListener() {
        locationManager.removeUpdates(locationListener);
    }

    private OnLocationResultListener mOnLocationListener;

    public interface OnLocationResultListener {
        void onLocationResult(Location location);

        void OnLocationChange(Location location);
    }
}
