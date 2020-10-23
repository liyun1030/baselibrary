package com.basely.permission.util;

import android.content.Context;
import android.os.Environment;

import com.basely.permission.util.nfc.NfcUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.math.BigDecimal;

/**
 * 缓存管理
 */
public class CacheManageUtils {
    private static volatile CacheManageUtils instance;
    private Context context;

    private CacheManageUtils(Context ctx) {
        this.context = ctx;
    }

    public static CacheManageUtils getInstance(Context ctx) {
        if (instance == null) {
            synchronized (NfcUtil.class) {
                if (instance == null) {
                    instance = new CacheManageUtils(ctx);
                }
            }
        }
        return instance;
    }

    /**
     * Context.getExternalFilesDir() --> SDCard/Android/data/你的应用的包名/files/ 目录，一般放一些长时间保存的数据
     * Context.getExternalCacheDir() --> SDCard/Android/data/你的应用包名/cache/目录，一般存放临时缓存数据
     */

    /**
     * 获取缓存值大小
     */
    public String getTotalCacheSize() {
        long cacheSize = getFolderSize(context.getCacheDir());
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            cacheSize += getFolderSize(context.getExternalCacheDir());
        }
        return getFormatSize(cacheSize);
    }

    /**
     * 创建缓存文件
     */
    public boolean createCache() {
        File tmpFile = new File(context.getCacheDir() + "/temp.txt");
        if (!tmpFile.exists()) {
            try {
                tmpFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        RandomAccessFile raf = null;
        String textContent = "这是一个测试字符串，写进text文本的,这是一个测试字符串，写进text文本的这是一个测试字符串，写进text文本的这是一个测试字符串，写进text文本的这是一个测试字符串，写进text文本的这是一个测试字符串，写进text文本的这是一个测试字符串，写进text文本的,这是一个测试字符串，写进text文本的这是一个测试字符串，写进text文本的这是一个测试字符串，写进text文本的这是一个测试字符串，写进text文本的,这是一个测试字符串，写进text文本的这是一个测试字符串，写进text文本的这是一个测试字符串，写进text文本的这是一个测试字符串，写进text文本的这是一个测试字符串，写进text文本的这是一个测试字符串，写进text文本的,这是一个测试字符串，写进text文本的这是一个测试字符串，写进text文本的这是一个测试字符串，写进text文本的这是一个测试字符串，写进text文本的,这是一个测试字符串，写进text文本的这是一个测试字符串，写进text文本的这是一个测试字符串，写进text文本的这是一个测试字符串，写进text文本的这是一个测试字符串，写进text文本的这是一个测试字符串，写进text文本的,这是一个测试字符串，写进text文本的这是一个测试字符串，写进text文本的这是一个测试字符串，写进text文本的这是一个测试字符串，写进text文本的,这是一个测试字符串，写进text文本的这是一个测试字符串，写进text文本的这是一个测试字符串，写进text文本的这是一个测试字符串，写进text文本的这是一个测试字符串，写进text文本的这是一个测试字符串，写进text文本的,这是一个测试字符串，写进text文本的这是一个测试字符串，写进text文本的这是一个测试字符串，写进text文本的这是一个测试字符串，写进text文本的,这是一个测试字符串，写进text文本的这是一个测试字符串，写进text文本的这是一个测试字符串，写进text文本的这是一个测试字符串，写进text文本的这是一个测试字符串，写进text文本的这是一个测试字符串，写进text文本的,这是一个测试字符串，写进text文本的这是一个测试字符串，写进text文本的这是一个测试字符串，写进text文本的这是一个测试字符串，写进text文本的,这是一个测试字符串，写进text文本的这是一个测试字符串，写进text文本的这是一个测试字符串，写进text文本的这是一个测试字符串，写进text文本的这是一个测试字符串，写进text文本的这是一个测试字符串，写进text文本的,这是一个测试字符串，写进text文本的这是一个测试字符串，写进text文本的这是一个测试字符串，写进text文本的这是一个测试字符串，写进text文本的,这是一个测试字符串，写进text文本的这是一个测试字符串，写进text文本的这是一个测试字符串，写进text文本的这是一个测试字符串，写进text文本的这是一个测试字符串，写进text文本的这是一个测试字符串，写进text文本的,这是一个测试字符串，写进text文本的这是一个测试字符串，写进text文本的这是一个测试字符串，写进text文本的这是一个测试字符串，写进text文本的,这是一个测试字符串，写进text文本的这是一个测试字符串，写进text文本的这是一个测试字符串，写进text文本的这是一个测试字符串，写进text文本的这是一个测试字符串，写进text文本的这是一个测试字符串，写进text文本的,这是一个测试字符串，写进text文本的这是一个测试字符串，写进text文本的这是一个测试字符串，写进text文本的这是一个测试字符串，写进text文本的,这是一个测试字符串，写进text文本的这是一个测试字符串，写进text文本的这是一个测试字符串，写进text文本的这是一个测试字符串，写进text文本的这是一个测试字符串，写进text文本的这是一个测试字符串，写进text文本的,这是一个测试字符串，写进text文本的这是一个测试字符串，写进text文本的这是一个测试字符串，写进text文本的这是一个测试字符串，写进text文本的,这是一个测试字符串，写进text文本的这是一个测试字符串，写进text文本的这是一个测试字符串，写进text文本的这是一个测试字符串，写进text文本的这是一个测试字符串，写进text文本的这是一个测试字符串，写进text文本的,这是一个测试字符串，写进text文本的这是一个测试字符串，写进text文本的这是一个测试字符串，写进text文本的这是一个测试字符串，写进text文本的,这是一个测试字符串，写进text文本的这是一个测试字符串，写进text文本的这是一个测试字符串，写进text文本的这是一个测试字符串，写进text文本的这是一个测试字符串，写进text文本的这是一个测试字符串，写进text文本的,这是一个测试字符串，写进text文本的这是一个测试字符串，写进text文本的这是一个测试字符串，写进text文本的这是一个测试字符串，写进text文本的,这是一个测试字符串，写进text文本的这是一个测试字符串，写进text文本的这是一个测试字符串，写进text文本的这是一个测试字符串，写进text文本的这是一个测试字符串，写进text文本的这是一个测试字符串，写进text文本的,这是一个测试字符串，写进text文本的这是一个测试字符串，写进text文本的这是一个测试字符串，写进text文本的";
        try {
            raf = new RandomAccessFile(tmpFile, "rwd");
            raf.seek(tmpFile.length());
            raf.write(textContent.getBytes());
            raf.close();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 清除所有缓存
     */
    public void clearAllCache() {
        deleteDir(context.getCacheDir());
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            deleteDir(context.getExternalCacheDir());
            //TODO 有网页清理时注意排错，是否存在/data/data/应用package目录下找不到database文件夹的问题
            context.deleteDatabase("webview.db");
            context.deleteDatabase("webviewCache.db");
        }
    }

    /**
     * 删除某个文件
     */
    private boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        }
        if (dir != null) {
            return dir.delete();
        } else {
            return false;
        }
    }

    /**
     * 获取文件
     */
    private long getFolderSize(File file) {
        long size = 0;
        if (file != null) {
            File[] fileList = file.listFiles();
            if (fileList != null && fileList.length > 0) {
                for (int i = 0; i < fileList.length; i++) {
                    // 如果下面还有文件
                    if (fileList[i].isDirectory()) {
                        size = size + getFolderSize(fileList[i]);
                    } else {
                        size = size + fileList[i].length();
                    }
                }
            }
        }
        return size;
    }

    /**
     * 格式化单位
     */
    private String getFormatSize(double size) {
        double kiloByte = size / 1024;
        double megaByte = kiloByte / 1024;
        double gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "MB";
        }
        double teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "GB";
        }
        BigDecimal result4 = BigDecimal.valueOf(teraBytes);
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString()
                + "TB";
    }
}
