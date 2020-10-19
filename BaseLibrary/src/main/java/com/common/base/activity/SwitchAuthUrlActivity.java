package com.common.base.activity;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.common.base.R;
import com.common.base.adapter.AuthUrlListAdapter;
import com.common.base.listener.OnAuthUrlSwitchListener;
import com.common.base.model.AuthUrlVo;
import com.common.base.model.DevelopModeEnum;
import com.common.base.model.DevelopModel;

import java.util.ArrayList;
import java.util.List;

public class SwitchAuthUrlActivity extends BaseActivity implements AuthUrlListAdapter.OnCheckChangedListener, View.OnClickListener {
    //https://ua.yijiupi.com:8433/himalaya-ApiService-UA/
    public static String MARKET = "https://ua2.yijiupi.com/himalaya-ApiService-UA2/";
    //https://ua.test.yijiupidev.com/himalaya-ApiService-UA/
    public static String TEST = "http://ua2.test.yijiupidev.com/himalaya-ApiService-UA2/";
    //https://ua.release.yijiupidev.com/himalaya-ApiService-UA/
    public static String TEST_PRE = "http://ua2.release.yijiupidev.com/himalaya-ApiService-UA2/";
    //https://ua.yijiupi.com:8433/himalaya-ApiService-UA/
    public static String ONLINE = "https://ua2.yijiupi.com/himalaya-ApiService-UA2/";
    //http://ua.pre.yijiupi.com/himalaya-ApiService-UA/
    public static String ONLINE_PRE = "http://ua2.pre.yijiupi.com/himalaya-ApiService-UA2/";
    public static String SELF_CHOOSE = "https://ua.yijiupi.com:8433/himalaya-ApiService-UA/";
    //http://197.255.20.34:9080/himalaya-ApiService-UA2/
    public static String DEVELOP_URL = "http://ua2.dev.yijiupidev.com/himalaya-ApiService-UA2/";
    public static final int REQUEST_CODE_SWITCH_AUTH = 100;
    private RecyclerView authUrlList;
    private AuthUrlListAdapter adapter;
    private List<AuthUrlVo> urlVos = new ArrayList<>();
    private EditText etUrl;
    private EditText etVersion;
    private Button btnOK;
    private AuthUrlVo mAuthVo;
    private OnAuthUrlSwitchListener listener;


    @Override
    public int getLayoutId() {
        return R.layout.activity_switch_authurl;
    }

    @Override
    public void init() {
        authUrlList = (RecyclerView) findViewById(R.id.recyclerViewAuthUrl);
        etUrl = (EditText) findViewById(R.id.etUrl);
        etVersion = (EditText) findViewById(R.id.etVersion);
        btnOK = (Button) findViewById(R.id.btnOK);
        btnOK.setOnClickListener(this);
        listener = DevelopModel.mListener;
        authUrlList.setLayoutManager(new LinearLayoutManager(this));
        int version = getVersionName();
        urlVos.add(new AuthUrlVo(MARKET, DevelopModeEnum.AuthUrlMode.market.mode, version));
        urlVos.add(new AuthUrlVo(TEST, DevelopModeEnum.AuthUrlMode.测试.mode, version));
        urlVos.add(new AuthUrlVo(TEST_PRE, DevelopModeEnum.AuthUrlMode.测试预发布.mode, version));
        urlVos.add(new AuthUrlVo(ONLINE, DevelopModeEnum.AuthUrlMode.生产.mode, version));
        urlVos.add(new AuthUrlVo(ONLINE_PRE, DevelopModeEnum.AuthUrlMode.生产预发布.mode, version));
        urlVos.add(new AuthUrlVo(SELF_CHOOSE, DevelopModeEnum.AuthUrlMode.自定义.mode, version));
        urlVos.add(new AuthUrlVo(DEVELOP_URL, DevelopModeEnum.AuthUrlMode.开发.mode, version));
        adapter = new AuthUrlListAdapter(this, urlVos);
        authUrlList.setAdapter(adapter);
        adapter.setOnCheckChangedListener(this);
        etUrl.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (mAuthVo==null){
                    return;
                }
                mAuthVo.authUrl = charSequence.toString();
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        etVersion.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (mAuthVo==null){
                    return;
                }
                try{
                    if (!TextUtils.isEmpty(charSequence)&&charSequence.toString().trim().length()>0){
                        mAuthVo.version = Integer.parseInt(charSequence.toString());
                    }else{
                        mAuthVo.version = 0;
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    mAuthVo.version = -1;
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    @Override
    public void onChecked(AuthUrlVo authUrlVo) {
        updateCheckedUrl(authUrlVo);
        mAuthVo = new AuthUrlVo(authUrlVo.authUrl,authUrlVo.mode,authUrlVo.version);
    }

    private void updateCheckedUrl(AuthUrlVo authUrlVo) {
        etUrl.setText(authUrlVo.authUrl);
        etVersion.setText(authUrlVo.version + "");
    }

    private int getVersionName(){
        int version = 56;
        try {
            // 获取packagemanager的实例
            PackageManager packageManager = getPackageManager();
            // getPackageName()是你当前类的包名，0代表是获取版本信息
            PackageInfo packInfo = packageManager.getPackageInfo(getPackageName(), 0);
            version = packInfo.versionCode;
        }catch (Exception e){
            e.printStackTrace();
        }
        return version;
    }

    @Override
    public void onClick(View view) {
        if (view == btnOK){
            if (mAuthVo == null){
                Toast.makeText(this,"还没有选择环境",Toast.LENGTH_SHORT).show();
                return;
            }
            if (mAuthVo.authUrl==null||TextUtils.isEmpty(mAuthVo.authUrl)){
                Toast.makeText(this,"URL为空",Toast.LENGTH_SHORT).show();
                return;
            }
            if (mAuthVo.version==0){
                Toast.makeText(this,"版本号为空",Toast.LENGTH_SHORT).show();
                return;
            }
            if (mAuthVo.version==-1){
                Toast.makeText(this,"版本号错误",Toast.LENGTH_SHORT).show();
                return;
            }
            if (listener!=null){
                listener.onAuthUrlSwitch(mAuthVo);
            }
            setResult(RESULT_OK);
            finish();
        }
    }
}
