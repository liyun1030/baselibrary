package base.tools;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.text.TextUtils;
import android.util.Base64;
import android.widget.ImageView;

import com.bumptech.glide.BitmapTypeRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.common.base.R;
import com.common.base.network.Constant;
import com.common.base.view.RoundedImageView;

/**
 * 图片加载工具类-glide
 */
public class GlideUtil {
    // 加载网络图片
    public static void load(Context context, String imageUrl, ImageView imageVie) {
        if (context == null) return;
        Glide.with(context)
                .load(imageUrl)
//                .placeholder(R.drawable.ic_qr_default)
//                .crossFade()
                .dontAnimate()
                .into(imageVie);
    }

    // 加载本地图片
    public static void load(Context context, int holderResId, ImageView imageVie) {
        if (context == null) return;
        Glide.with(context)
                .load(holderResId)
//                .placeholder(R.drawable.ic_qr_default)
//                .error(R.color.abc_tab_text_normal)
                .crossFade()
                .into(imageVie);
    }


    /**
     * 加载圆形图片
     */
    public static void loadRoundImage(final Context context, String url, final ImageView imageView) {
        String address = "";
        if (!TextUtils.isEmpty(url)) {
            address = url.trim();
        }
        if (!TextUtils.isEmpty(url) && !url.startsWith("http:") && !url.startsWith("file:") && !url.startsWith("https:")) {
            address = url;
        }
        if (context == null) return;
        Glide.with(context)
                .load(address)
                .centerCrop()
                .dontAnimate()
//                .placeholder(R.mipmap.ic_mime_default)
//                .error(R.mipmap.ic_mime_default)
                .transform(new GlideCircleTransform(context))
                .into(imageView);//只能展示在线圆形图片

    }
    public static void loadCourseImage(final Context context, String url, final RoundedImageView imageView) {
        String address="";
        if (!TextUtils.isEmpty(url)){
            address = url.trim();
        }
        if (!TextUtils.isEmpty(url) && !url.startsWith("http:") && !url.startsWith("file:") && !url.startsWith("https:")) {
            address = Constant.getCDNPrefix() + url;
        }
        if (context==null) return;
        Glide.with(context)
                .load(address)
                .centerCrop()
                .dontAnimate()
                .into(imageView);//只能展示在线圆形图片

    }
    public static void loadCourseImages(final Context context, String url, final ImageView imageView) {
        String address = "";
        if (!TextUtils.isEmpty(url)) {
            address = url.trim();
        }
        if (!TextUtils.isEmpty(url) && !url.startsWith("http:") && !url.startsWith("file:") && !url.startsWith("https:")) {
            address = Constant.PIC_HOST + address;
        }
        if (context == null) return;
        if (context instanceof Activity) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                if (((Activity) context).isDestroyed()) {
                    return;
                }
            }
            if (((Activity) context).isFinishing()) return;
        }
        Glide.with(context)
                .load(address)
                .centerCrop()
                .dontAnimate()
                .placeholder(R.drawable.ic_delault)
                .error(R.drawable.ic_delault)
                .into(imageView);//只能展示在线圆形图片

    }

    public static boolean isBase64Img(String imgurl) {
        if (!TextUtils.isEmpty(imgurl) && (imgurl.startsWith("data:image/png;base64,")
                || imgurl.startsWith("data:image/*;base64,") || imgurl.startsWith("data:image/jpg;base64,")
        )) {
            return true;
        }
        return false;
    }

    /**
     * glide加载base64图片
     */
    public static void loadImage(Context ctx, String url, ImageView imageView) {
        byte[] decode = null;
        if (isBase64Img(url)) {
            url = url.split(",")[1];
            decode = Base64.decode(url, Base64.DEFAULT);
        }

        BitmapTypeRequest bitmapTypeRequest = Glide.with(ctx).load(decode == null ? url : decode).asBitmap();
//        bitmapTypeRequest.placeholder(R.drawable.ic_qr_default);
        bitmapTypeRequest.diskCacheStrategy(DiskCacheStrategy.RESULT);
        bitmapTypeRequest.dontAnimate();
        bitmapTypeRequest.into(imageView);
    }
    public static Bitmap stringToBitmap(String string) {
        Bitmap bitmap = null;
        try {
            byte[] bitmapArray = Base64.decode(string.split(",")[1], Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }
}
























