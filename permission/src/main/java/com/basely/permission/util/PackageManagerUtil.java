package com.basely.permission.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.content.FileProvider;
import android.widget.Toast;

import com.basely.permission.bean.AppInfo;
import com.zdf.activitylauncher.ActivityLauncher;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 包管理器
 */
public class PackageManagerUtil {
    private static volatile PackageManagerUtil instance;
    private Context context;
    private String authority;
    private static final String intentType = "application/vnd.android.package-archive";

    private PackageManagerUtil(Context ctx) {
        this.context = ctx;
        String applicationID = context.getPackageName();
        authority = applicationID + ".fileProvider";
    }

    public static PackageManagerUtil getInstance(Context ctx) {
        if (instance == null) {
            synchronized (PackageManagerUtil.class) {
                if (instance == null) {
                    instance = new PackageManagerUtil(ctx);
                }
            }
        }
        return instance;
    }

    /**
     * 获取应用程序名称
     */

    public synchronized String getAppName() {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            int labelRes = packageInfo.applicationInfo.labelRes;
            return context.getResources().getString(labelRes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean checkApplication(String packageName) {
        if (packageName == null || "".equals(packageName)) {
            return false;
        }
        try {
            ApplicationInfo info = context.getPackageManager().getApplicationInfo(packageName, PackageManager.GET_UNINSTALLED_PACKAGES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }

    }

    /**
     * [获取应用程序版本名称信息]
     *
     * @return 当前应用的版本名称
     */

    private synchronized String getVersionName() {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            return packageInfo.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * [获取应用程序版本名称信息]
     *
     * @return 当前应用的版本名称
     */
    private synchronized int getVersionCode(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }


    /**
     * [获取应用程序版本名称信息]
     *
     * @return 当前应用的版本名称
     */
    private synchronized String getPackageName() {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            return packageInfo.packageName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 获取图标 bitmap
     */

    private synchronized Bitmap getBitmap() {
        PackageManager packageManager = null;
        ApplicationInfo applicationInfo = null;
        try {
            packageManager = context.getApplicationContext()
                    .getPackageManager();
            applicationInfo = packageManager.getApplicationInfo(
                    context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            applicationInfo = null;
        }
        Drawable d = packageManager.getApplicationIcon(applicationInfo); //xxx根据自己的情况获取drawable
        BitmapDrawable bd = (BitmapDrawable) d;
        Bitmap bm = bd.getBitmap();
        return bm;
    }

    /**
     * 获取系统中所有应用信息，
     **/
    public List<AppInfo> getAllApps() {
        List<AppInfo> list = new ArrayList<AppInfo>();
        AppInfo myAppInfo;
        //获取到所有安装了的应用程序的信息，包括那些卸载了的，但没有清除数据的应用程序
        PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> packageInfos = packageManager.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES);
        for (PackageInfo info : packageInfos) {
            myAppInfo = new AppInfo();
            //拿到包名
            String packageName = info.packageName;
            //拿到应用程序的信息
            ApplicationInfo appInfo = info.applicationInfo;
            //拿到应用程序的图标
            Drawable icon = appInfo.loadIcon(packageManager);
            //拿到应用程序的大小
            //long codesize = packageStats.codeSize;
            //Log.i("info", "-->"+codesize);
            //拿到应用程序的程序名
            String appName = appInfo.loadLabel(packageManager).toString();
            myAppInfo.setPackageName(packageName);
            myAppInfo.setAppName(appName);
            myAppInfo.setIcon(icon);

            if (filterApp(appInfo)) {
                myAppInfo.setSystemApp(false);
            } else {
                myAppInfo.setSystemApp(true);
            }
            list.add(myAppInfo);
        }
        return list;
    }

    /**
     * 判断某一个应用程序是不是系统的应用程序，
     * 如果是返回true，否则返回false。
     */
    private boolean filterApp(ApplicationInfo info) {
        //有些系统应用是可以更新的，如果用户自己下载了一个系统的应用来更新了原来的，它还是系统应用，这个就是判断这种情况的
        if ((info.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0) {
            return true;
        } else if ((info.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {//判断是不是系统应用
            return true;
        }
        return false;
    }

    /**
     * 安装apk
     *
     * @return
     */
    public void installNormal(String filePath) {
        File file = new File(filePath);
        if (file == null || !file.exists() || !file.isFile() || file.length() <= 0) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            boolean canInstallPackage = context.getPackageManager().canRequestPackageInstalls();
            if (canInstallPackage) {
                installApk(filePath);
            } else {
                Uri packageURI = Uri.parse("package:" + getPackageName());
                Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, packageURI);

                //检查是否可以安装未知来源的应用，没有权限就一直去尝试，我感觉这样子是很流氓的...
                //在这里拦截OnActivityResult,不要代码割裂
                ActivityLauncher.init((Activity) context).startActivityForResult(intent, new ActivityLauncher.Callback() {
                    @Override
                    public void onActivityResult(int resultCode, Intent data) {
                        //授权了就去安装
                        if (resultCode == Activity.RESULT_OK) {
                            installApk(filePath);
                        } else {
                            Toast.makeText(context, "你没有授权安装App", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        } else {
            installApk(filePath);
        }
    }

    /**
     * 跳转到安装apk的页面
     */
    private void installApk(String storageApkPath) {
        File apkFile = new File(storageApkPath);
        if (!apkFile.exists()) {
            return;
        }

        Intent intent = new Intent(Intent.ACTION_VIEW);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Uri contentUri = FileProvider.getUriForFile(context, authority, apkFile);
            intent.setDataAndType(contentUri, intentType);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            intent.setDataAndType(Uri.parse("file://" + apkFile.toString()), intentType);
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 卸载app
     */
    public void uninstallNormal(String packageName) {
        if (packageName == null || packageName.length() == 0) {
            return;
        }

        Intent i = new Intent(Intent.ACTION_DELETE, Uri.parse(new StringBuilder().append("package:")
                .append(packageName).toString()));
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }

    /**
     * 复制文件到SD卡
     *
     * @param fileName 复制的文件名
     * @param path     保存的目录路径
     * @return
     */
    public boolean copyApkFromAssets(String fileName, String path) {
        boolean copyIsFinish = false;
        try {
            InputStream is = context.getAssets().open(fileName);
            File file = new File(path);
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            byte[] temp = new byte[1024];
            int i = 0;
            while ((i = is.read(temp)) > 0) {
                fos.write(temp, 0, i);
            }
            fos.close();
            is.close();
            copyIsFinish = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return copyIsFinish;
    }
}
