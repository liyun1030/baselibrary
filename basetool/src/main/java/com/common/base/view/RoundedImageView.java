package com.common.base.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

import com.common.base.R;

/**
 * 自定义圆角ImageView
 * Created by xt on 2016/8/26.
 */
public class RoundedImageView extends AppCompatImageView {
    private float[] rids;

//    private float[] rids = {10.0f,10.0f,10.0f,10.0f,0.0f,0.0f,0.0f,0.0f,};

    private Path path;

    public RoundedImageView(Context context) {
        this(context, null);
    }

    public RoundedImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundedImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        handleTypedArray(context, attrs);
    }

    private void handleTypedArray(Context context, AttributeSet attrs) {
        if (attrs == null)
            return;
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RadiusImageView);
        float left = typedArray.getDimension(R.styleable.RadiusImageView_left_radio, 0);
        float top = typedArray.getDimension(R.styleable.RadiusImageView_top_radio, 0);
        float right = typedArray.getDimension(R.styleable.RadiusImageView_right_radio, 0);
        float bottom = typedArray.getDimension(R.styleable.RadiusImageView_bottom_radio, 0);
        rids = new float[]{left, left, top, top, right, right, bottom, bottom};
        typedArray.recycle();
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
