package com.common.base.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;

/**
 * 自定义圆角ImageView
 * Created by liuxiaolong on 2019/9/4 0004
 */
public class RoundImageView extends AppCompatImageView {
    private float[] rids;

//    private float[] rids = {10.0f,10.0f,10.0f,10.0f,0.0f,0.0f,0.0f,0.0f,};

    private Path path;

    //圆角大小，默认为10
    private int mBorderRadius = 20;

    public RoundImageView(Context context) {
        this(context, null);
    }
    public RoundImageView(Context context,int radiussize) {
        this(context, null);
        mBorderRadius=radiussize;
    }
    public RoundImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        handleTypedArray(context, attrs);
    }

    private void handleTypedArray(Context context, AttributeSet attrs) {
        float left = mBorderRadius;
        float top = mBorderRadius;
        float right = mBorderRadius;
        float bottom = mBorderRadius;
        rids = new float[]{left, left, top, top, right, right, bottom, bottom};
        path = new Path();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int w = this.getWidth();
        int h = this.getHeight();
        path.addRoundRect(new RectF(0, 0, w, h), rids, Path.Direction.CW);
        canvas.clipPath(path);
        super.onDraw(canvas);
    }
}
