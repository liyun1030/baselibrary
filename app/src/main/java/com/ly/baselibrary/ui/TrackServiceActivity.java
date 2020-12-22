package com.ly.baselibrary.ui;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Build;
import android.provider.Settings;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.TextureMapView;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.track.AMapTrackClient;
import com.amap.api.track.ErrorCode;
import com.amap.api.track.OnTrackLifecycleListener;
import com.amap.api.track.TrackParam;
import com.amap.api.track.query.model.AddTerminalRequest;
import com.amap.api.track.query.model.AddTerminalResponse;
import com.amap.api.track.query.model.AddTrackRequest;
import com.amap.api.track.query.model.AddTrackResponse;
import com.amap.api.track.query.model.QueryTerminalRequest;
import com.amap.api.track.query.model.QueryTerminalResponse;
import com.basely.permission.util.LocationUtil;
import com.common.base.network.Constant;
import com.common.base.tool.CommUtils;
import com.ly.baselibrary.R;
import com.ly.baselibrary.application.MyApplication;
import com.ly.baselibrary.model.UserInfoModel;
import com.zdf.activitylauncher.ActivityLauncher;

/**
 * 轨迹上报示例
 */
public class TrackServiceActivity extends Activity implements LocationSource, AMapLocationListener {

    private static final String TAG = "TrackServiceActivity";
    private static final String CHANNEL_ID_SERVICE_RUNNING = "CHANNEL_ID_SERVICE_RUNNING";

    private TextureMapView mapView;
    private AMapTrackClient aMapTrackClient;
    private android.widget.TextView startTrackView;
    private android.widget.TextView startGatherView;

    private long terminalId;
    private long trackId;
    private boolean isServiceRunning;
    private boolean isGatherRunning;

    private UserInfoModel model;
    private boolean uploadToTrack = false;
    /**
     * 服务id，请修改成您创建的服务的id
     * <p>
     * 猎鹰轨迹服务，同一个开发者账号下的key可以直接使用同账号下的sid，不再需要人工绑定
     */
    public static final long SERVICE_ID = 238678;
    private String TERMINAL_NAME = "";

    //定位需要的数据
    LocationSource.OnLocationChangedListener mListener;
    AMapLocationClient mlocationClient;
    AMapLocationClientOption mLocationOption;

    //定位蓝点
    MyLocationStyle myLocationStyle;


    private OnTrackLifecycleListener onTrackListener = new SimpleOnTrackLifecycleListener() {
        @Override
        public void onBindServiceCallback(int status, String msg) {
            android.util.Log.w(TAG, "onBindServiceCallback, status: " + status + ", msg: " + msg);
        }

        @Override
        public void onStartTrackCallback(int status, String msg) {
            if (status == ErrorCode.TrackListen.START_TRACK_SUCEE || status == ErrorCode.TrackListen.START_TRACK_SUCEE_NO_NETWORK) {
                // 成功启动
                Toast.makeText(TrackServiceActivity.this, "启动服务成功", Toast.LENGTH_SHORT).show();
                isServiceRunning = true;
                updateBtnStatus();
            } else if (status == ErrorCode.TrackListen.START_TRACK_ALREADY_STARTED) {
                // 已经启动
                Toast.makeText(TrackServiceActivity.this, "服务已经启动", Toast.LENGTH_SHORT).show();
                isServiceRunning = true;
                updateBtnStatus();
            } else {
                android.util.Log.w(TAG, "error onStartTrackCallback, status: " + status + ", msg: " + msg);
                Toast.makeText(TrackServiceActivity.this,
                        "error onStartTrackCallback, status: " + status + ", msg: " + msg,
                        Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onStopTrackCallback(int status, String msg) {
            if (status == ErrorCode.TrackListen.STOP_TRACK_SUCCE) {
                // 成功停止
                Toast.makeText(TrackServiceActivity.this, "停止服务成功", Toast.LENGTH_SHORT).show();
                isServiceRunning = false;
                isGatherRunning = false;
                updateBtnStatus();
            } else {
                android.util.Log.w(TAG, "error onStopTrackCallback, status: " + status + ", msg: " + msg);
                Toast.makeText(TrackServiceActivity.this,
                        "error onStopTrackCallback, status: " + status + ", msg: " + msg,
                        Toast.LENGTH_LONG).show();

            }
        }

        @Override
        public void onStartGatherCallback(int status, String msg) {
            if (status == ErrorCode.TrackListen.START_GATHER_SUCEE) {
                Toast.makeText(TrackServiceActivity.this, "定位采集开启成功", Toast.LENGTH_SHORT).show();
                isGatherRunning = true;
                updateBtnStatus();
            } else if (status == ErrorCode.TrackListen.START_GATHER_ALREADY_STARTED) {
                Toast.makeText(TrackServiceActivity.this, "定位采集已经开启", Toast.LENGTH_SHORT).show();
                isGatherRunning = true;
                updateBtnStatus();
            } else {
                android.util.Log.w(TAG, "error onStartGatherCallback, status: " + status + ", msg: " + msg);
                Toast.makeText(TrackServiceActivity.this,
                        "error onStartGatherCallback, status: " + status + ", msg: " + msg,
                        Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onStopGatherCallback(int status, String msg) {
            if (status == ErrorCode.TrackListen.STOP_GATHER_SUCCE) {
                Toast.makeText(TrackServiceActivity.this, "定位采集停止成功", Toast.LENGTH_SHORT).show();
                isGatherRunning = false;
                updateBtnStatus();
            } else {
                android.util.Log.w(TAG, "error onStopGatherCallback, status: " + status + ", msg: " + msg);
                Toast.makeText(TrackServiceActivity.this,
                        "error onStopGatherCallback, status: " + status + ", msg: " + msg,
                        Toast.LENGTH_LONG).show();
            }
        }
    };

    @Override
    protected void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_service);
        model = MyApplication.getInstance().getSpInstance().getObject(
                Constant.SP_USER_KEY,
                UserInfoModel.class);
        TERMINAL_NAME = model.getPhone();
        // 不要使用Activity作为Context传入
        aMapTrackClient = new AMapTrackClient(getApplicationContext());
        aMapTrackClient.setInterval(5, 30);

        mapView = findViewById(R.id.activity_track_service_map);
        mapView.getMap().moveCamera(CameraUpdateFactory.zoomTo(14));
        //114.379751,30.464747--梅花坞
        mapView.getMap().moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(30.464747, 114.379751),14));
        mapView.onCreate(savedInstanceState);
        // 启用地图内置定位
        mapView.getMap().setLocationSource(this);
        // 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        mapView.getMap().setMyLocationEnabled(true);
        // 设置定位的类型为定位模式，有定位、跟随或地图根据面向方向旋转几种
        myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle.interval(2000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        mapView.getMap().setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
        mapView.getMap().getUiSettings().setMyLocationButtonEnabled(true);//设置默认定位按钮是否显示，非必需设置。
        mapView.getMap().getUiSettings().setScaleControlsEnabled(true);//控制比例尺控件是否显示
        mapView.getMap().setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）默认执行此种模式。


        myLocationStyle.showMyLocation(true);
        mapView.getMap().setOnMyLocationChangeListener(new AMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                //从location对象中获取经纬度信息，地址描述信息，建议拿到位置之后调用逆地理编码接口获取
            }
        });

        startTrackView = findViewById(R.id.activity_track_service_start_track);
        startGatherView = findViewById(R.id.activity_track_service_start_gather);
        updateBtnStatus();
        // 服务启停
        startTrackView.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                if (isServiceRunning) {
                    aMapTrackClient.stopTrack(new TrackParam(SERVICE_ID, terminalId), onTrackListener);
                } else {
                    startTrack();
                }
            }
        });
        // 采集启停
        startGatherView.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                if (isGatherRunning) {
                    aMapTrackClient.stopGather(onTrackListener);
                } else {
                    aMapTrackClient.setTrackId(trackId);
                    aMapTrackClient.startGather(onTrackListener);
                }
            }
        });

        ((CheckBox) findViewById(R.id.activity_track_service_upload_to_track)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                uploadToTrack = isChecked;
            }
        });
        if(LocationUtil.getInstance(this).isOpenGps()){
            startLocation();
        }else{
            start2Location();
        }
    }

    private void updateBtnStatus() {
        startTrackView.setText(isServiceRunning ? "停止服务" : "启动服务");
        startTrackView.setTextColor(isServiceRunning ? 0xFFFFFFFF : 0xFF000000);
        startTrackView.setBackgroundResource(isServiceRunning ? R.drawable.round_corner_btn_bg_active : R.drawable.round_corner_btn_bg);
        startGatherView.setText(isGatherRunning ? "停止采集" : "开始采集");
        startGatherView.setTextColor(isGatherRunning ? 0xFFFFFFFF : 0xFF000000);
        startGatherView.setBackgroundResource(isGatherRunning ? R.drawable.round_corner_btn_bg_active : R.drawable.round_corner_btn_bg);
    }

    private void startTrack() {
        // 先根据Terminal名称查询Terminal ID，如果Terminal还不存在，就尝试创建，拿到Terminal ID后，
        // 用Terminal ID开启轨迹服务
        aMapTrackClient.queryTerminal(new QueryTerminalRequest(SERVICE_ID, TERMINAL_NAME), new SimpleOnTrackListener() {
            @Override
            public void onQueryTerminalCallback(QueryTerminalResponse queryTerminalResponse) {
                if (queryTerminalResponse.isSuccess()) {
                    if (queryTerminalResponse.isTerminalExist()) {
                        // 当前终端已经创建过，直接使用查询到的terminal id
                        terminalId = queryTerminalResponse.getTid();
                        if (uploadToTrack) {
                            aMapTrackClient.addTrack(new AddTrackRequest(SERVICE_ID, terminalId), new SimpleOnTrackListener() {
                                @Override
                                public void onAddTrackCallback(AddTrackResponse addTrackResponse) {
                                    if (addTrackResponse.isSuccess()) {
                                        // trackId需要在启动服务后设置才能生效，因此这里不设置，而是在startGather之前设置了track id
                                        trackId = addTrackResponse.getTrid();
                                        TrackParam trackParam = new TrackParam(SERVICE_ID, terminalId);
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                            trackParam.setNotification(createNotification());
                                        }
                                        aMapTrackClient.startTrack(trackParam, onTrackListener);
                                    } else {
                                        Toast.makeText(TrackServiceActivity.this, "网络请求失败，" + addTrackResponse.getErrorMsg(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } else {
                            // 不指定track id，上报的轨迹点是该终端的散点轨迹
                            TrackParam trackParam = new TrackParam(SERVICE_ID, terminalId);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                trackParam.setNotification(createNotification());
                            }
                            aMapTrackClient.startTrack(trackParam, onTrackListener);
                        }
                    } else {
                        // 当前终端是新终端，还未创建过，创建该终端并使用新生成的terminal id
                        aMapTrackClient.addTerminal(new AddTerminalRequest(TERMINAL_NAME, SERVICE_ID), new SimpleOnTrackListener() {
                            @Override
                            public void onCreateTerminalCallback(AddTerminalResponse addTerminalResponse) {
                                if (addTerminalResponse.isSuccess()) {
                                    terminalId = addTerminalResponse.getTid();
                                    TrackParam trackParam = new TrackParam(SERVICE_ID, terminalId);
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                        trackParam.setNotification(createNotification());
                                    }
                                    aMapTrackClient.startTrack(trackParam, onTrackListener);
                                } else {
                                    Toast.makeText(TrackServiceActivity.this, "网络请求失败，" + addTerminalResponse.getErrorMsg(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                } else {
                    Toast.makeText(TrackServiceActivity.this, "网络请求失败，" + queryTerminalResponse.getErrorMsg(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * 在8.0以上手机，如果app切到后台，系统会限制定位相关接口调用频率
     * 可以在启动轨迹上报服务时提供一个通知，这样Service启动时会使用该通知成为前台Service，可以避免此限制
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private Notification createNotification() {
        Notification.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID_SERVICE_RUNNING, "app service", NotificationManager.IMPORTANCE_LOW);
            nm.createNotificationChannel(channel);
            builder = new Notification.Builder(getApplicationContext(), CHANNEL_ID_SERVICE_RUNNING);
        } else {
            builder = new Notification.Builder(getApplicationContext());
        }
        Intent nfIntent = new Intent(TrackServiceActivity.this, TrackServiceActivity.class);
        nfIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        builder.setContentIntent(PendingIntent.getActivity(TrackServiceActivity.this, 0, nfIntent, 0))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("猎鹰sdk运行中")
                .setContentText("猎鹰sdk运行中");
        Notification notification = builder.build();
        return notification;
    }


    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        if (isServiceRunning) {
            aMapTrackClient.stopTrack(new TrackParam(SERVICE_ID, terminalId), new SimpleOnTrackLifecycleListener());
        }
        if (null != mlocationClient) {
            mlocationClient.onDestroy();
        }
    }

    @Override
    protected void onSaveInstanceState(android.os.Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (mListener != null && aMapLocation != null) {
            if (aMapLocation != null && aMapLocation.getErrorCode() == 0) {
                mListener.onLocationChanged(aMapLocation);// 显示系统小蓝点

            } else {
                String errText = "定位失败," + aMapLocation.getErrorCode() + ": " + aMapLocation.getErrorInfo();
            }
        }

    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
        startLocation();
    }

    public void startLocation() {
        if (mlocationClient == null) {
            //初始化定位
            mlocationClient = new AMapLocationClient(this);
            //初始化定位参数
            mLocationOption = new AMapLocationClientOption();
            //设置定位回调监听
            mlocationClient.setLocationListener(this);

            //设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //设置定位参数
            mlocationClient.setLocationOption(mLocationOption);
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
            mlocationClient.startLocation();//启动定位
        }else{
            mlocationClient.startLocation();//启动定位
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
                    startLocation();
                    mapView.onResume();
                } else {
                    CommUtils.showToast(TrackServiceActivity.this, "手机系统的定位服务未开启，无法定位!");
                }
            }
        });
    }

    @Override
    public void deactivate() {
        mListener = null;
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
        }
        mlocationClient = null;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
