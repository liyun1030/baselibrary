package com.ly.baselibrary.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;
import com.basely.permission.util.LocationUtil;
import com.common.base.tool.CommUtils;
import com.ly.baselibrary.R;
import com.zdf.activitylauncher.ActivityLauncher;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TrackActivity extends Activity implements LocationSource, AMapLocationListener {
    MapView mMapView = null;
    AMap aMap = null;
    private AMapLocation oldLocation;
    List<LatLng> points = new ArrayList<LatLng>();
    Polyline polyline;

    public int count = 0;

    private OnLocationChangedListener mListener;
    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track);

        mMapView = (MapView) findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState);// 此方法必须重写
        init();
    }

    private  void init(){
        if (aMap == null) {
            aMap = mMapView.getMap();
        }
        //设置amap的一些属性
        UiSettings uiSettings = aMap.getUiSettings();
        uiSettings.setCompassEnabled(true);// 设置指南针是否显示
        uiSettings.setZoomControlsEnabled(true);// 设置缩放按钮是否显示
        uiSettings.setScaleControlsEnabled(true);// 设置比例尺是否显示
        uiSettings.setRotateGesturesEnabled(true);// 设置地图旋转是否可用
        uiSettings.setTiltGesturesEnabled(true);// 设置地图倾斜是否可用
        uiSettings.setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示

        MyLocationStyle myLocationStyle = new MyLocationStyle();
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER);//连续定位、蓝点不会移动到地图中心点，定位点依照设备方向旋转，并且蓝点会跟随设备移动。
        aMap.setMyLocationStyle(myLocationStyle);
        aMap.setLocationSource(this);// 设置定位监听
        // 设置定位的类型为定位模式 ，可以由定位 LOCATION_TYPE_LOCATE、跟随 LOCATION_TYPE_MAP_FOLLOW 或地图根据面向方向旋转 LOCATION_TYPE_MAP_ROTATE
        aMap.setMyLocationType(AMap.LOCATION_TYPE_MAP_ROTATE);

        // 缩放级别（zoom）：值越大地图越详细(4-20)
        aMap.moveCamera(CameraUpdateFactory.zoomTo(14));
        //使用 aMap.setMapTextZIndex(2) 可以将地图底图文字设置在添加的覆盖物之上
        aMap.setMapTextZIndex(2);
        if(LocationUtil.getInstance(this).isOpenGps()){
            aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        }else{
            start2Location();
        }
    }
    public void start2Location() {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        ActivityLauncher.init(this).startActivityForResult(intent, new ActivityLauncher.Callback() {
            @Override
            public void onActivityResult(int resultCode, Intent data) {
                //授权了就去安装
                if (resultCode == Activity.RESULT_OK) {
                    aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
                } else {
                    if(LocationUtil.getInstance(TrackActivity.this).isOpenGps()){
                        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
                    }else{
                        CommUtils.showToast(TrackActivity.this, "手机系统的定位服务未开启，无法定位!");
                    }
                }
            }
        });
    }

    /**
     * 定位成功后回调函数
     */
    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (mListener != null && amapLocation != null) {
            if (amapLocation.getErrorCode() == 0) {
                mListener.onLocationChanged(amapLocation);// 显示系统小蓝点
                Log.e("Amap", amapLocation.getLatitude() + "," + amapLocation.getLongitude());
                LatLng newLatLng =  new LatLng(amapLocation.getLatitude(),amapLocation.getLongitude());
                points.add(count, newLatLng);
                count++;
                drawLines();
                write(amapLocation.getLatitude() + "," + amapLocation.getLongitude());
            } else {
                String errText = "定位失败," + amapLocation.getErrorCode()+ ": " + amapLocation.getErrorInfo();
                Log.e("AmapErr", errText);
                Toast.makeText(this, errText, Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * 绘制路线
     */
    public void drawLines() {
        PolylineOptions options = new PolylineOptions();
        options.setCustomTexture(BitmapDescriptorFactory.fromResource(R.drawable.common_loading3));
        options.geodesic(true).setDottedLine(false).color(Color.GREEN).addAll(points).useGradient(true).width(10).visible(true);
        polyline = aMap.addPolyline(options);

        // 获取轨迹坐标点
        LatLngBounds.Builder b= LatLngBounds.builder();
        for (int i = 0; i< points.size(); i++) {
            b.include(points.get(i));
        }
        LatLngBounds bounds= b.build();

        CameraUpdate update = CameraUpdateFactory.newLatLngBounds(bounds, 100);//
        aMap.animateCamera(update);

    }
    /**
     * 对地图进行截屏
     */
    Bitmap aMapBitmap=null;
    String filePath=null;
    private void aMapScreenShot() {
        aMap.getMapScreenShot(new AMap.OnMapScreenShotListener() {
            @Override
            public void onMapScreenShot(Bitmap bitmap) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
                aMapBitmap = bitmap;
                try {
                    filePath = Environment.getExternalStorageDirectory() + "/DCIM/Camera/test_" + sdf.format(new Date()) + ".png";
                    // 保存在SD卡根目录下，图片为png格式。
                    FileOutputStream fos = new FileOutputStream(filePath);
                    boolean ifSuccess = bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                    try {
                        fos.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onMapScreenShot(Bitmap bitmap, int i) {

            }
        });
    }
    /**
     * 写入文件方法
     * @param content
     */
    public static void write(String content) {
        try {
            //判断实际是否有SD卡，且应用程序是否有读写SD卡的能力，有则返回true
            if (Environment.getExternalStorageState().equals(
                    Environment.MEDIA_MOUNTED)) {
                // 获取SD卡的目录
                File sdCardDir = Environment.getExternalStorageDirectory();
                String path = "/AAA/";
                File dir = new File(path);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                File targetFile = new File(sdCardDir.getCanonicalPath() + path+"aaa.txt");
                if(!targetFile.exists()){
                    targetFile.createNewFile();
                }
                //使用RandomAccessFile是在原有的文件基础之上追加内容，
                //而使用outputstream则是要先清空内容再写入
                RandomAccessFile raf = new RandomAccessFile(targetFile, "rw");
                //光标移到原始文件最后，再执行写入
                raf.seek(targetFile.length());
                Date date = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss.SSS");
                content+="["+sdf.format(date)+"]";
                content+="\n";
                raf.write(content.getBytes());
                raf.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 激活定位
     */
    @Override
    public void activate(OnLocationChangedListener listener) {
        mListener = listener;
        if (mlocationClient == null) {
            mlocationClient = new AMapLocationClient(this);
            mLocationOption = new AMapLocationClientOption();
            //设置定位监听
            mlocationClient.setLocationListener(this);
            //设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //设置定位参数
            mlocationClient.setLocationOption(mLocationOption);
            mLocationOption.setOnceLocation(false);
            mLocationOption.setGpsFirst(true);
            // 设置发送定位请求的时间间隔,最小值为1000ms,1秒更新一次定位信息
            mLocationOption.setInterval(10000);
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
            mLocationOption.setMockEnable(true);
            //获取最近3s内精度最高的一次定位结果：
            //设置setOnceLocationLatest(boolean b)接口为true，启动定位时SDK会返回最近3s内精度最高的一次定位结果。如果设置其为true，
            // setOnceLocation(boolean b)接口也会被设置为true，反之不会，默认为false。
            mLocationOption.setOnceLocationLatest(true);
            mlocationClient.startLocation();
        }
    }

    @Override
    public void deactivate() {

    }
}