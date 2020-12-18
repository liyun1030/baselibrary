package com.basely.permission.util;

import java.util.ArrayList;
import java.util.List;
import android.Manifest;
import android.content.Context;

import com.basely.permission.R;

import me.weyye.hipermission.HiPermission;
import me.weyye.hipermission.PermissionCallback;
import me.weyye.hipermission.PermissionItem;

/**
 * 权限工具类封装
 * 2020.5.20-liyun
 */
public class PermissionUtil {
    private static PermissionUtil instance = null;
    private Context context;
    public static PermissionUtil getInstance(Context ctx) {
        if (instance == null) {
            instance = new PermissionUtil(ctx);
        }
        return instance;
    }

    private PermissionUtil(Context ctx) {
        this.context=ctx;
    }

    /**
     * 权限检查
     */
    public void checkPermissions(PermissionCallback callback) {
        if(context==null){
            return;
        }
        List<PermissionItem> permissonItems = new ArrayList<PermissionItem>();
        permissonItems.add(new PermissionItem(Manifest.permission.CAMERA,"相机", R.drawable.permission_ic_camera));
        permissonItems.add(new PermissionItem(Manifest.permission.WRITE_EXTERNAL_STORAGE,"存储",R.drawable.permission_ic_storage));
//        permissonItems.add(new PermissionItem(Manifest.permission.SEND_SMS,"短信",R.drawable.permission_ic_sms));
//        permissonItems.add(new PermissionItem(Manifest.permission.READ_SMS,"短信",R.drawable.permission_ic_sms));
//        permissonItems.add(new PermissionItem(Manifest.permission.RECEIVE_SMS,"短信",R.drawable.permission_ic_sms));
//        permissonItems.add(new PermissionItem(Manifest.permission.CALL_PHONE,"打电话",R.drawable.permission_ic_phone));
        permissonItems.add(new PermissionItem(Manifest.permission.ACCESS_FINE_LOCATION,"定位",R.drawable.permission_ic_location));
//        permissonItems.add(new PermissionItem(Manifest.permission.READ_CONTACTS,"联系人",R.drawable.permission_ic_contacts));
//        permissonItems.add(new PermissionItem(Manifest.permission.RECORD_AUDIO,"语音",R.drawable.permission_ic_micro_phone));
//        permissonItems.add(new PermissionItem(Manifest.permission.READ_CALL_LOG,"通话记录",R.drawable.permission_ic_micro_phone));
//        permissonItems.add(new PermissionItem(Manifest.permission.BLUETOOTH,"蓝牙",R.drawable.permission_ic_micro_phone));
//        permissonItems.add(new PermissionItem(Manifest.permission.NFC,"NFC",R.drawable.permission_ic_micro_phone));
        HiPermission.create(context)
                .title("亲爱的用户")
                .permissions(permissonItems)
                .animStyle(R.style.PermissionAnimScale)
                .msg("此应用需要获取以下权限")
                .checkMutiPermission(callback);
    }
    /**
     * 权限检查
     */
    public boolean checkPermission(String permission){
        if(HiPermission.checkPermission(context,permission)){
            return true;
        }
        return false;
    }
}
