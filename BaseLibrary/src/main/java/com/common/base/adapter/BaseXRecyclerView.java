package com.common.base.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;

import com.common.base.R;
import com.common.base.widgets.MarginDecoration;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

public class BaseXRecyclerView extends XRecyclerView {
    public BaseXRecyclerView(Context context) {
        this(context,null);

    }

    public BaseXRecyclerView(Context context, AttributeSet attrs) {
        this(context,attrs,0);
    }

    public BaseXRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initBaseConfigure(context);
    }

    /**
     * 默认配置（可选）
     * @param context
     */
    private void initBaseConfigure(Context context) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        setLayoutManager(layoutManager);
        setArrowImageView(R.drawable.ic_downgrey);
        getDefaultFootView().setNoMoreHint("没有更多了");
    }

    /**
     *添加分割线（可选）
     */
    private void setItemDecoration(int dp) {
        addItemDecoration(new MarginDecoration(getContext(),dp));
    }

    /**
     * 设置自定义样式（可选）
     */
    private void setRefreshStyle() {
        setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        getDefaultRefreshHeaderView().setRefreshTimeVisible(true);
    }



}
