package com.common.base.tools;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.view.View;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Date;

/**
 * Created by Yan Kai on 2015/11/16.
 */
public class ImageUtils {

    private static int index = 0;

    public final static Bitmap compressImageFromFile(String srcPath) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        newOpts.inJustDecodeBounds = true;//只读边,不读内容
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);

        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        float hh = 800f;//
        float ww = 480f;//
        int be = 1;
        if (w > h && w > ww) {
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;//设置采样率

        newOpts.inPreferredConfig = Bitmap.Config.ARGB_8888;//该模式是默认的,可不设
        newOpts.inPurgeable = true;// 同时设置才会有效
        newOpts.inInputShareable = true;//。当系统内存不够时候图片自动被回收

        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
//      return compressBmpFromBmp(bitmap);//原来的方法调用了这个方法企图进行二次压缩
        //其实是无效的,大家尽管尝试
        return bitmap;
    }

    public static String saveFile(Bitmap bitmap, String path) throws Exception {
        File dirFile = new File(path);
        if (!dirFile.exists()) {
            dirFile.mkdir();
        }
        File myCaptureFile = new File(path + File.separator + (index++) + new Date().getTime() + ".jpg");
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        bos.flush();
        bos.close();
        return myCaptureFile.getAbsolutePath();
    }
    public static String saveCertFile(Bitmap bitmap, String dir, String fileName) throws Exception {
        File dirFile = new File(dir);
        if (!dirFile.exists()) {
            dirFile.mkdir();
        }
        File myCaptureFile = new File(dir + File.separator + fileName);
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        bos.flush();
        bos.close();
        return myCaptureFile.getAbsolutePath();
    }
    /**
     * 以最省内存的方式读取本地资源的图片
     *
     * @param context
     * @param resId
     * @return
     */
    public static Bitmap readBitMap(Context context, int resId) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        // 获取资源图片
        InputStream is = context.getResources().openRawResource(resId);
        return BitmapFactory.decodeStream(is, null, opt);
    }

    /**
     * 以最省内存的方式读取本地资源的图片 或者SDCard中的图片
     *
     * @param imagePath 图片在SDCard中的路径
     * @return
     */
    public static Bitmap getSDCardImg(String imagePath) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        // 获取资源图片
        return BitmapFactory.decodeFile(imagePath, opt);
    }

    /**
     * 编辑图片大小，保持图片不变形。
     *
     * @param sourceBitmap
     * @param resetWidth
     * @param resetHeight
     * @return
     */
    public static Bitmap resetImage(Bitmap sourceBitmap, int resetWidth, int resetHeight) {
        int width = sourceBitmap.getWidth();
        int height = sourceBitmap.getHeight();
        int tmpWidth;
        int tmpHeight;
        float scaleWidth = (float) resetWidth / (float) width;
        float scaleHeight = (float) resetHeight / (float) height;
        float maxTmpScale = scaleWidth >= scaleHeight ? scaleWidth : scaleHeight;
        // 保持不变形
        tmpWidth = (int) (maxTmpScale * width);
        tmpHeight = (int) (maxTmpScale * height);
        Matrix m = new Matrix();
        m.setScale(maxTmpScale, maxTmpScale, tmpWidth, tmpHeight);
        sourceBitmap = Bitmap.createBitmap(sourceBitmap, 0, 0, width, height, m, false);
        // 切图
        int x = (tmpWidth - resetWidth) / 2;
        int y = (tmpHeight - resetHeight) / 2;
        return Bitmap.createBitmap(sourceBitmap, x, y, resetWidth, resetHeight);
    }

    /**
     * 获取本地图片并指定高度和宽度
     *
     * @param imagePath
     * @return
     */
    public static Bitmap getNativeImage(String imagePath) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        // 获取这个图片的宽和高
        Bitmap myBitmap = BitmapFactory.decodeFile(imagePath, options); // 此时返回myBitmap为空
        // 计算缩放比
        int be = (int) (options.outHeight / (float) 200);
        int ys = options.outHeight % 200;// 求余数
        float fe = ys / (float) 200;
        if (fe >= 0.5)
            be = be + 1;
        if (be <= 0)
            be = 1;
        options.inSampleSize = be;
        // 重新读入图片，注意这次要把options.inJustDecodeBounds 设为 false
        options.inJustDecodeBounds = false;
        myBitmap = BitmapFactory.decodeFile(imagePath, options);
        return myBitmap;
    }

    /**
     * 指定压缩尺寸
     *
     * @param bmp
     * @param width
     * @param height
     * @return
     */
    public static Bitmap getScaleBitmap2(Bitmap bmp, int width, int height) {
        Bitmap newBmp;
        int imageHeight = bmp.getHeight();
        int imageWidth = bmp.getWidth();
        float scaleWidth = (float) width / imageWidth;
        float scaleHeight = (float) height / imageHeight;
        Matrix matrix = new Matrix();
        if (scaleWidth >= scaleHeight) {
            matrix.postScale(scaleHeight, scaleHeight);
        } else {
            matrix.postScale(scaleWidth, scaleWidth);
        }
        newBmp = Bitmap.createBitmap(bmp, 0, 0, imageWidth, imageHeight,
                matrix, true);
        return newBmp;
    }

    /**
     * 代码创建一个selector 代码生成会清除padding
     */
    public static Drawable createStateDrawable(Context context, int bulr, int focus) {
        Drawable bulrDrawable = context.getResources().getDrawable(bulr);
        Drawable focusDrawable = context.getResources().getDrawable(focus);
        StateListDrawable drawable = new StateListDrawable();
        drawable.addState(new int[]{android.R.attr.state_pressed},
                focusDrawable);
        drawable.addState(new int[]{}, bulrDrawable);
        return drawable;
    }

    /**
     * 图片资源回收
     *
     * @param bitmap
     */
    public void distoryBitmap(Bitmap bitmap) {
        if (null != bitmap && bitmap.isRecycled()) {
            bitmap.recycle();
        }
    }

    public static Bitmap rotateImage(Context context, int resId, int rotateDegree) {
        Bitmap bitmapOrg = BitmapFactory.decodeResource(context.getResources(), resId);
        Matrix matrix = new Matrix();
        matrix.postRotate(rotateDegree);
        return Bitmap.createBitmap(bitmapOrg, 0, 0, bitmapOrg.getWidth(), bitmapOrg.getHeight(), matrix, true);
    }

    public static Bitmap horizontalFlipImage(Context context, int resId) {
        Bitmap bitmapOrg = BitmapFactory.decodeResource(context.getResources(), resId);
        Matrix matrix = new Matrix();
        matrix.setScale(-1, 1);
        matrix.postTranslate(bitmapOrg.getWidth(), 0);
        return Bitmap.createBitmap(bitmapOrg, 0, 0, bitmapOrg.getWidth(), bitmapOrg.getHeight(), matrix, true);
    }

    //缩放系数
    public final static int SCALE = 8;

    public static Bitmap fastBlur(Bitmap sbitmap, float radiusf) {
        Bitmap bitmap = Bitmap.createScaledBitmap(sbitmap, sbitmap.getWidth() / SCALE, sbitmap.getHeight() / SCALE, false);//先缩放图片，增加模糊速度
        int radius = (int) radiusf;
        if (radius < 1) {
            return (null);
        }

        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        int[] pix = new int[w * h];
        bitmap.getPixels(pix, 0, w, 0, 0, w, h);

        int wm = w - 1;
        int hm = h - 1;
        int wh = w * h;
        int div = radius + radius + 1;

        int r[] = new int[wh];
        int g[] = new int[wh];
        int b[] = new int[wh];
        int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
        int vmin[] = new int[Math.max(w, h)];

        int divsum = (div + 1) >> 1;
        divsum *= divsum;
        int dv[] = new int[256 * divsum];
        for (i = 0; i < 256 * divsum; i++) {
            dv[i] = (i / divsum);
        }

        yw = yi = 0;

        int[][] stack = new int[div][3];
        int stackpointer;
        int stackstart;
        int[] sir;
        int rbs;
        int r1 = radius + 1;
        int routsum, goutsum, boutsum;
        int rinsum, ginsum, binsum;

        for (y = 0; y < h; y++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            for (i = -radius; i <= radius; i++) {
                p = pix[yi + Math.min(wm, Math.max(i, 0))];
                sir = stack[i + radius];
                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);
                rbs = r1 - Math.abs(i);
                rsum += sir[0] * rbs;
                gsum += sir[1] * rbs;
                bsum += sir[2] * rbs;
                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }
            }
            stackpointer = radius;

            for (x = 0; x < w; x++) {

//                r[yi] = dv[rsum];
//                g[yi] = dv[gsum];
//                b[yi] = dv[bsum];
                r[yi] = rsum > dv.length || rsum == dv.length ? dv[dv.length - 1] : dv[rsum];
                g[yi] = gsum > dv.length || gsum == dv.length ? dv[dv.length - 1] : dv[gsum];
                b[yi] = bsum > dv.length || bsum == dv.length ? dv[dv.length - 1] : dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (y == 0) {
                    vmin[x] = Math.min(x + radius + 1, wm);
                }
                p = pix[yw + vmin[x]];

                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[(stackpointer) % div];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi++;
            }
            yw += w;
        }
        for (x = 0; x < w; x++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            yp = -radius * w;
            for (i = -radius; i <= radius; i++) {
                yi = Math.max(0, yp) + x;

                sir = stack[i + radius];

                sir[0] = r[yi];
                sir[1] = g[yi];
                sir[2] = b[yi];

                rbs = r1 - Math.abs(i);

                rsum += r[yi] * rbs;
                gsum += g[yi] * rbs;
                bsum += b[yi] * rbs;

                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }

                if (i < hm) {
                    yp += w;
                }
            }
            yi = x;
            stackpointer = radius;
            for (y = 0; y < h; y++) {
                // Preserve alpha channel: ( 0xff000000 & pix[yi] )
                pix[yi] = (0xff000000 & pix[yi]) | (dv[rsum] << 16) | (dv[gsum] << 8) | dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (x == 0) {
                    vmin[y] = Math.min(y + r1, hm) * w;
                }
                p = x + vmin[y];

                sir[0] = r[p];
                sir[1] = g[p];
                sir[2] = b[p];

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[stackpointer];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi += w;
            }
        }
        bitmap.setPixels(pix, 0, w, 0, 0, w, h);
        return (bitmap);
    }

    public static Drawable changeColor(Drawable originDrawable, int color) {
        return changeColor(originDrawable, ColorStateList.valueOf(color));
    }

    public static Drawable changeColor(Drawable originDrawable, int color, PorterDuff.Mode tintMode) {
        return changeColor(originDrawable, ColorStateList.valueOf(color), tintMode);
    }

    public static Drawable changeColor(Drawable originDrawable, ColorStateList colorStateList) {
        return changeColor(originDrawable, colorStateList, null);
    }

    public static Drawable changeColor(Drawable originDrawable, ColorStateList colorStateList, PorterDuff.Mode tintMode) {
        Drawable tintDrawable = DrawableCompat.wrap(originDrawable.mutate());
        if (tintMode != null) {
            DrawableCompat.setTintMode(tintDrawable, tintMode);
        }
        DrawableCompat.setTintList(tintDrawable, colorStateList);
        return tintDrawable;
    }

    /**
     * 设置背景图片.
     *
     * @param view
     * @param background
     */
    public static void setBackground(View view, Drawable background) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            view.setBackgroundDrawable(background);
        } else {
            view.setBackground(background);
        }
    }

    /**
     * 设置背景图片.
     *
     * @param view
     * @param context
     * @param drawable
     * @param color
     */
    public static void setBackground(View view, Context context, int drawable, int color) {
        setBackground(view, ImageUtils.changeColor(ContextCompat.getDrawable(context, drawable), ContextCompat.getColor(context, color)));
    }

    /**
     * 图片重复转换
     *
     * @param width
     * @param src
     */
    public static Bitmap createRepeater(int width, Bitmap src) {
        int count = (width + src.getWidth() - 1) / src.getWidth(); //计算出平铺填满所给width（宽度）最少需要的重复次数
        Bitmap bitmap = Bitmap.createBitmap(src.getWidth() * count, src.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        for (int idx = 0; idx < count; ++idx) {
            canvas.drawBitmap(src, idx * src.getWidth(), 0, null);
        }
        return bitmap;
    }

    /**
     * 截取显示部分图片
     */
    public static Bitmap showPartImg(Resources resources, int imgId, double pWidth, double pHeight) {
        Bitmap bitmap = BitmapFactory.decodeResource(resources, imgId);
        int h = (int) (bitmap.getHeight() * pHeight);
        int w = (int) (bitmap.getWidth() * pWidth);
        return Bitmap.createBitmap(bitmap, 0, 0, w, h);
    }

    /** */
    /**
     * 图片去色,返回灰度图片
     *
     * @param bmpOriginal 传入的图片
     * @return 去色后的图片
     */
    public static Bitmap toGrayscale(Bitmap bmpOriginal) {
        int width, height;
        height = bmpOriginal.getHeight();
        width = bmpOriginal.getWidth();
        Bitmap bmpGrayscale = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_4444);
        Canvas c = new Canvas(bmpGrayscale);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(f);
        c.drawBitmap(bmpOriginal, 0, 0, paint);
        return bmpGrayscale;
    }

}
