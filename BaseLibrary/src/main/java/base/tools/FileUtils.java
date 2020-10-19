package base.tools;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;

import com.common.base.network.Constant;

import java.io.File;
import java.text.DecimalFormat;

/**
 * 文件工具类
 */
public class FileUtils {
    public static boolean hasSDCard() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    // "/storage/sdcard/"
    public static String getSDCardDir() {
        return Environment.getExternalStorageDirectory().getAbsolutePath() + "/";
    }

    // "/data/data/包名/files/"
    public static String getAppFilesDir(Context context) {
        return context.getFilesDir().getAbsolutePath() + "/";
    }

    public static String getAppDir() {
        return getRootFilePath() + Constant.PBL_APP_DIR + "/";
    }

    public static String getReadableFileSize(long size) {
        if (size <= 0) return "0";
        final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#.##").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }

    public static boolean fileIsExist(String filePath) {
        if (null == filePath || filePath.length() < 1) {
            return false;
        }
        File f = new File(filePath);
        return f.exists();
    }

    public static String getSizeStr(long size) {
        String sizeStr;
        if (size > 1024L * 1024L * 1024L * 1024L) {
            sizeStr = (float) Math.round(size * 100L / (1024L * 1024L * 1024L * 1024L)) / 100L + " TB";
        } else if (size > 1024L * 1024L * 1024L) {
            sizeStr = (float) Math.round(size * 100L / (1024L * 1024L * 1024L)) / 100L + " GB";
        } else if (size > 1024L * 1024L) {
            sizeStr = (float) Math.round(size * 100L / (1024L * 1024L)) / 100L + " MB";
        } else {
            sizeStr = (float) Math.round(size * 100L / 1024L) / 100L + " KB";
        }
        return sizeStr;
    }

    public static String getRootFilePath() {
        if (hasSDCard()) {
            // filePath:/sdcard/
            return Environment.getExternalStorageDirectory().getAbsolutePath() + "/";
        } else {
            // filePath: /data/data/
            return Environment.getDataDirectory().getAbsolutePath() + "/data/";
        }
    }

    public static boolean createDirectory(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return false;
        }
        File file = new File(filePath);
        return file.exists() || file.mkdirs();

    }

    /**
     * 根据文件name(带文件格式)得到文件的title(不带文件格式) 例：123.mp4——>123
     *
     * @param name
     * @return
     */
    public static String getTitleFromName(String name) {
        if (!TextUtils.isEmpty(name)) {
            if (name.contains(".")) {
                name = name.substring(0, name.lastIndexOf("."));
            }
        }
        return name;
    }

    public static String getFileName(String pathAndName) {
        int start = pathAndName.lastIndexOf("/");
        int end = pathAndName.lastIndexOf(".");
        if (start != -1 && end != -1) {
            return pathAndName.substring(start + 1, end);
        } else {
            return null;
        }

    }

    public static String getFileNameWithFormat(String pathAndName) {
        int start = pathAndName.lastIndexOf("/");
        int end = pathAndName.length();
        if (start != -1 && end != -1) {
            return pathAndName.substring(start + 1, end);
        } else {
            return null;
        }

    }

    public static boolean deleteFile(String path) {
        return deleteFile(new File(path));
    }

    public static boolean deleteFile(File file) {
        if (file.isFile()) {
            return file.delete();
        }
        if (file.isDirectory()) {
            File[] childFile = file.listFiles();
            if (childFile == null || childFile.length == 0) {

                return file.delete();
            }
            for (File f : childFile) {
                deleteFile(f);
            }
            return file.delete();
        }
        return false;
    }

    //创建存储头像的根目录
    public static String getPhotoRootPath() {
        String path = getAppDir() + "avatar" + "/";
        createDirectory(path);
        return path;
    }

    /**
     * 分享截图文件夹
     *
     * @return
     */
    public static String getShareImagePath() {
        String path = getAppDir() + "share" + "/";
        createDirectory(path);
        return path;
    }

    /**
     * 根据URI获取文件真实路径（兼容多张机型）
     *
     * @param context
     * @param uri
     * @return
     */
    public static String getFilePathByUri(Context context, Uri uri) {
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            int sdkVersion = Build.VERSION.SDK_INT;
            if (sdkVersion >= 19) { // api >= 19
                return getRealPathFromUriAboveApi19(context, uri);
            } else { // api < 19
                return getRealPathFromUriBelowAPI19(context, uri);
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    /**
     * 适配api19及以上,根据uri获取图片的绝对路径
     *
     * @param context 上下文对象
     * @param uri     图片的Uri
     * @return 如果Uri对应的图片存在, 那么返回该图片的绝对路径, 否则返回null
     */
    @SuppressLint("NewApi")
    private static String getRealPathFromUriAboveApi19(Context context, Uri uri) {
        String filePath = null;
        if (DocumentsContract.isDocumentUri(context, uri)) {
            // 如果是document类型的 uri, 则通过document id来进行处理
            String documentId = DocumentsContract.getDocumentId(uri);
            if (isMediaDocument(uri)) { // MediaProvider
                // 使用':'分割
                String type = documentId.split(":")[0];
                String id = documentId.split(":")[1];

                String selection = MediaStore.Images.Media._ID + "=?";
                String[] selectionArgs = {id};

                //
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                filePath = getDataColumn(context, contentUri, selection, selectionArgs);
            } else if (isDownloadsDocument(uri)) { // DownloadsProvider
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(documentId));
                filePath = getDataColumn(context, contentUri, null, null);
            } else if (isExternalStorageDocument(uri)) {
                // ExternalStorageProvider
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    filePath = Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            } else {
                //Log.e("路径错误");
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // 如果是 content 类型的 Uri
            filePath = getDataColumn(context, uri, null, null);
        } else if ("file".equals(uri.getScheme())) {
            // 如果是 file 类型的 Uri,直接获取图片对应的路径
            filePath = uri.getPath();
        }
        return filePath;
    }

    /**
     * 适配api19以下(不包括api19),根据uri获取图片的绝对路径
     *
     * @param context 上下文对象
     * @param uri     图片的Uri
     * @return 如果Uri对应的图片存在, 那么返回该图片的绝对路径, 否则返回null
     */
    private static String getRealPathFromUriBelowAPI19(Context context, Uri uri) {
        return getDataColumn(context, uri, null, null);
    }

    /**
     * 获取数据库表中的 _data 列，即返回Uri对应的文件路径
     *
     * @return
     */
    private static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        String path = null;

        String[] projection = new String[]{MediaStore.Images.Media.DATA};
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndexOrThrow(projection[0]);
                path = cursor.getString(columnIndex);
            }
        } catch (Exception e) {
            if (cursor != null) {
                cursor.close();
            }
        }
        return path;
    }

    /**
     * @param uri the Uri to check
     * @return Whether the Uri authority is MediaProvider
     */
    private static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    private static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri the Uri to check
     * @return Whether the Uri authority is DownloadsProvider
     */
    private static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static String getCacheSize() {
        String str = getSizeStr(getDirSize(new File(BaseConstant.CACHE_DIR)));
        return str;
    }

    public static boolean clearCache(String path) {
        boolean flag = true;
        if (path != null) {
            File file = new File(path);
            if (file.exists()) {
                File[] filePaths = file.listFiles();
                for (File f : filePaths) {
                    if (f.isFile()) {
                        f.delete();
                    }
                    if (f.isDirectory()) {
                        String fpath = f.getPath();
                        clearCache(fpath);
                        f.delete();
                    }
                }
            }
        } else {
            flag = false;
        }
        return flag;
    }

    public static long getDirSize(File file) {
        //判断文件是否存在
        if (file.exists()) {
            //如果是目录则递归计算其内容的总大小
            if (file.isDirectory()) {
                File[] children = file.listFiles();
                long size = 0;
                for (File f : children)
                    size += getDirSize(f);
                return size;
            } else {//如果是文件则直接返回其大小,以“兆”为单位
                long size = file.length() / 1024 / 1024;
                return size;
            }
        } else {
            return 0;
        }
    }
}
