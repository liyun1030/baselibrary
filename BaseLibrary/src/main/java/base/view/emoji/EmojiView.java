package base.view.emoji;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.common.base.R;
import com.common.base.adapter.EmojiAdapter;
import com.common.base.tools.BaseConstant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * created by 李云 on 2019/5/6
 * 本类的作用:emoji自定义view
 */
public class EmojiView extends LinearLayout {
    public interface EmojiItemClick {
        public void insertEmoji(String txt);
        public void delEmoji();
    }
    private LayoutInflater inflater;
    private Context ctx;
    List<List<Integer>> emojiList = new ArrayList<>();//表情2的数据源
    List<List<String>> emojiTxtList = new ArrayList<>();//表情2的符号数据源
    Map<Integer, List<List<Integer>>> expressionTypeList = new TreeMap<>();//表情总数据源,以图标为key
    private List<GridView> gridList = new ArrayList<>();
    private EmojiAdapter mExpressionAdapter;
    private ViewPager vViewPager;
    private LinearLayout vLl_dots;
    private EmojiItemClick emojiItemClickListener;
    // 正则表达式
    private Pattern mPattern;
    // String 图片字符串 Integer表情
    private HashMap<String, Integer> mSmileyToRes;

    public EmojiView(Context context) {
        super(context);
        this.ctx = context;
        initView();
    }

    public EmojiView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.ctx = context;
        initView();
    }

    public EmojiView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.ctx = context;
        initView();
    }

    public void setListener(EmojiItemClick listener) {
        this.emojiItemClickListener = listener;
    }

    public void initView() {
        inflater = LayoutInflater.from(ctx);
        View emojiView = inflater.inflate(R.layout.emoji_main, this,true);
        vViewPager = (ViewPager) emojiView.findViewById(R.id.viwepager_expression);//viewPager
        vLl_dots = (LinearLayout) emojiView.findViewById(R.id.ll_dot_container);//圆点容器

        // 获取表情ID与表情图标的Map
        mSmileyToRes = buildSmileyToRes();
        // 获取构建的正则表达式
        mPattern = buildPattern();
        initData();
    }

    //初始化数据
    private void initData() {
        List<Integer> idData = new ArrayList<>();
        List<String> strData = new ArrayList<>();
        for (int i = 0; i < BaseConstant.emoBeans.size(); i++) {
            EmoBean bean = BaseConstant.emoBeans.get(i);
            String fileName = "emoji_" + bean.getFileName();
            idData.add(getImageResourceId(fileName));
            strData.add(bean.getCh());
        }
        //把图片数据分页，每页20个表情一个删除键，三行
        emojiList = splitList(idData, 20);
        expressionTypeList.put(R.mipmap.emoji_001, emojiList);
        //把符号数据分页，每页最多20个表情，加上一个删除键
        // emojiTxtList = splitStringList(Arrays.asList(mParser.arrTextEMoji), 20);
        emojiTxtList = splitStringList(strData, 20);
        initEmojiAdapter(emojiList.size());
    }

    public int getImageResourceId(String name) {
        ApplicationInfo appInfo =ctx.getApplicationInfo();
//        第一个参数是你的资源文件的名字，不带后缀的，，第二个参数是你资源文件所在的目录，比如layout,drawable或者是values，，，第三个是你的包名
        int resId = ctx.getResources().getIdentifier(name, "drawable", appInfo.packageName);
//        R.drawable drawables = new R.drawable();
        //默认的id
//        int resId = 0;
//        try {
//            //根据字符串字段名，取字段//根据资源的ID的变量名获得Field的对象,使用反射机制来实现的
//            java.lang.reflect.Field field = R.mipmap.class.getField(name);
//            //取值
//            resId = (Integer) field.get(drawables);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        return resId;
    }

    /**
     * 使用HashMap的key-value的形式来影射表情的ID和图片资源
     */
    private HashMap<String, Integer> buildSmileyToRes() {
        HashMap<String, Integer> smileyToRes = new HashMap<String, Integer>();
        for (int i = 0; i < BaseConstant.emoBeans.size(); i++) {
            // 图片名称作为key值，图片资源ID作为value值
            EmoBean bean = BaseConstant.emoBeans.get(i);
            String fileName = "emoji_" + bean.getFileName();
            smileyToRes.put(bean.getCh(), getImageResourceId(fileName));
        }
        return smileyToRes;
    }

    /**
     * 构建正则表达式,用来找到我们所要使用的图片
     * @return
     */
    private Pattern buildPattern() {
        StringBuilder patternString = new StringBuilder();
        patternString.append('(');
        for (EmoBean bean : BaseConstant.emoBeans) {
            patternString.append(Pattern.quote(bean.getCh()));
            patternString.append('|');
        }

        for (EmoBean bean : BaseConstant.emoBeans) {
            patternString.append(Pattern.quote(bean.getFileName()));
            patternString.append('|');
        }
        patternString.replace(patternString.length() - 1,
                patternString.length(), ")");
        // 把String字符串编译成正则表达式()
        // ([调皮]|[调皮]|[调皮])
        return Pattern.compile(patternString.toString());
    }

    /**
     * 初始化表情资源
     */
    private void initEmojiAdapter(int emojiPage) {
        gridList.clear();
        for (int i = 0; i < emojiPage; i++) {
            GridView gridView = (GridView) inflater.inflate(R.layout.gridview_emoji, null);
            final List<Integer> emojiResource = emojiList.get(i);
            final List<String> emojiResourceName = emojiTxtList.get(i);
            mExpressionAdapter = new EmojiAdapter(inflater, emojiResource);
            gridView.setAdapter(mExpressionAdapter);
            //点击表情，将表情添加到输入框中。
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                    if (emojiItemClickListener != null) {
                        if(emojiResource.size()!=position){
                            String txt=emojiResourceName.get(position);
                            emojiItemClickListener.insertEmoji(txt);
                        }else{
                            emojiItemClickListener.delEmoji();
                        }
                    }
                }
            });
            gridList.add(gridView);
        }
        vViewPager.setAdapter(new ExpressionAdapter(gridList));
        gotoInitData(gridList);
    }

    /**
     * 表情适配器
     */
    public class ExpressionAdapter extends PagerAdapter {
        private List<GridView> list;

        public ExpressionAdapter(List<GridView> list) {
            this.list = list;
        }

        @Override
        public int getCount() {
            if (list != null && list.size() > 0) {
                return list.size();
            } else {
                return 0;
            }
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((GridView) object);
        }

        @Override
        public GridView instantiateItem(ViewGroup container, int position) {
            GridView GridView = list.get(position);
            container.addView(GridView);
            return GridView;
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
    }

    /**
     * 初始表情布局下底部圆点
     *
     * @param list
     */
    private void gotoInitData(List<GridView> list) {
        vLl_dots.removeAllViews();
        for (int i = 0; i < list.size(); i++) {
            ImageView imageView = new ImageView(ctx);
            if (i == 0) {
                imageView.setImageResource(R.drawable.shape_dot_select);
            } else {
                imageView.setImageResource(R.drawable.shape_dot_nomal);
            }
            LayoutParams layoutParams = new LayoutParams(dp2px(ctx, 8),
                    dp2px(ctx, 8));
            layoutParams.setMargins(20, 0, 0, 0);
            vLl_dots.addView(imageView, layoutParams);

        }
        if (vLl_dots.getChildCount() <= 1) {
            vLl_dots.setVisibility(View.GONE);
        } else {
            vLl_dots.setVisibility(VISIBLE);
        }
        vViewPager.setOffscreenPageLimit(6);
        vViewPager.setCurrentItem(0);
        vViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < vLl_dots.getChildCount(); i++) {
                    if (i != position) {
                        ((ImageView) vLl_dots.getChildAt(i)).setImageResource(R.drawable.shape_dot_nomal);
                    }
                }
                ((ImageView) vLl_dots.getChildAt(position)).setImageResource(R.drawable.shape_dot_select);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /**
     * 把lista按固定长度分割成若干list
     *
     * @param length 每个集合长度
     * @return
     */
    public static List<List<Integer>> splitList(List<Integer> dataList, int length) {
        List<List<Integer>> lists = new ArrayList<>();
        for (int i = 0; i < dataList.size(); i = i + length) {
            int j = i + length;
            if (j > dataList.size()) {
                j = dataList.size();
            }

            List<Integer> insertList = dataList.subList(i, j);
            if (insertList.size() == 0) {
                break;
            }
            lists.add(insertList);
        }
        return lists;
    }

    /**
     * 把lista按固定长度分割成若干list
     *
     * @param length 每个集合长度
     * @return
     */
    public static List<List<String>> splitStringList(List<String> dataList, int length) {
        List<List<String>> lists = new ArrayList<>();
        for (int i = 0; i < dataList.size(); i = i + length) {
            int j = i + length;
            if (j > dataList.size()) {
                j = dataList.size();
            }
            List<String> insertList = dataList.subList(i, j);
            if (insertList.size() == 0) {
                break;
            }
            lists.add(insertList);
        }
        return lists;
    }

    /**
     * dp转px
     */
    public static int dp2px(Context context, int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                context.getResources().getDisplayMetrics());
    }

    /**
     * 根据文本替换成图片，int width, int height 可控制表情大小
     * @param text 对应表情
     * @return 一个表示图片的序列
     */
    public CharSequence addSmileySpansReSize(CharSequence text, int width,
                                             int height) {
        if(TextUtils.isEmpty(text)){
            return "";
        }
        // 把文字替换为对应图片
        SpannableStringBuilder builder = new SpannableStringBuilder(text);
        // 判断提取工具类（按照正则表达式）
        Matcher matcher = mPattern.matcher(text);
        while (matcher.find()) {
            // 获取对应表情的图片id
            Object resObj=mSmileyToRes.get(matcher.group());
            if(resObj==null){
                return builder;
            }
            int resId = ((Integer) resObj).intValue();
            // 替换制定字符
            builder.setSpan(
                    new ImageSpan(ctx, decodeSampledBitmapFromResource(
                            ctx.getResources(), resId,
                            dp2px(ctx, width),
                            dp2px(ctx, height))), matcher
                            .start(), matcher.end(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return builder;
    }

    public static Bitmap decodeSampledBitmapFromResource(Resources res,
                                                         int resId, int reqWidth, int reqHeight) {
        // 第一次解析将inJustDecodeBounds设置为true，来获取图片大小
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.inDensity = res.getDisplayMetrics().densityDpi;
        BitmapFactory.decodeResource(res, resId, options);
        // 调用上面定义的方法计算inSampleSize值
        options.inSampleSize = calculateInSampleSize(options, reqWidth,
                reqHeight);
        // 使用获取到的inSampleSize值再次解析图片
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        // 源图片的高度和宽度
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            // 计算出实际宽高和目标宽高的比率
            final int heightRatio = Math.round((float) height
                    / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            // 选择宽和高中最小的比率作为inSampleSize的值，这样可以保证最终图片的宽和高
            // 一定都会大于等于目标的宽和高。
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }
}
