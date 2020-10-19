package com.common.base.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * 自动适配GridView
 * Created by Yan Kai on 2016/2/25.
 */
public class GridView4ScrollView extends GridView {
    public GridView4ScrollView(Context context) {
        super(context);
    }

    public GridView4ScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GridView4ScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}