package com.basely.permission.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;

/**
 * 屏幕操作类
 * 2020.5.21-liyun
 */
public class ScreenCaptureUtil {
    private static ScreenCaptureUtil instance = null;
    private Context context;

    public static ScreenCaptureUtil getInstance(Context ctx) {
        if (instance == null) {
            instance = new ScreenCaptureUtil(ctx);
        }
        return instance;
    }

    private ScreenCaptureUtil(Context ctx) {
        this.context = ctx;
    }

    /**
     * 截屏
     */
    public Bitmap takeScreenShot(View view) {
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        return view.getDrawingCache();
    }

    /**
     * 保存图片到SD卡中,strName = FileUtils.getShareImagePath() + System.currentTimeMillis() + ".png";
     */
    public boolean savePic(Bitmap mBitmap, String strName) {
//        String str=FileUtil.getShareImagePath() + System.currentTimeMillis() + ".png";
        if (null == mBitmap) {
            return false;
        }
        FileOutputStream fos = null;
        try {
            File file = new File(strName);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            fos = new FileOutputStream(strName);
            mBitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();


            // 其次把文件插入到系统图库
            try {
                MediaStore.Images.Media.insertImage(context.getContentResolver(),
                        file.getAbsolutePath(), strName, null);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            // 最后通知图库更新
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) { // 判断SDK版本是不是4.4或者高于4.4
                String[] paths = new String[]{file.getAbsolutePath()};
                MediaScannerConnection.scanFile(context, paths, null, null);
            } else {
                final Intent intent;
                if (file.isDirectory()) {
                    intent = new Intent(Intent.ACTION_MEDIA_MOUNTED);
                    intent.setClassName("com.android.providers.media", "com.android.providers.media.MediaScannerReceiver");
                    intent.setData(Uri.fromFile(Environment.getExternalStorageDirectory()));
                } else {
                    intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    intent.setData(Uri.fromFile(file));
                }
                context.sendBroadcast(intent);
            }
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
