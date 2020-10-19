package com.common.base.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @Description: RecyclerView Adapter基类封装
 * @Author: LiuXiaoLong
 * @Time: 2018/1/17:17:18
 */
public abstract class BaseRecyclerAdapter<T>  extends RecyclerView.Adapter<RecyclerHolder> {
    protected List<T> dataSources;//总数据源
    protected Context mContext;
    protected final int mItemLayoutId;
    private OnItemClickListener listener;
    private OnItemLongClickListener longlistener;

    public BaseRecyclerAdapter(RecyclerView v, Collection<T> datas, int itemLayoutId) {
        if (datas == null) {
            dataSources = new ArrayList<>();
        } else if (datas instanceof List) {
            dataSources = (List<T>) datas;
        } else {
            dataSources = new ArrayList<>(datas);
        }
        this.mContext=v.getContext();
        this.mItemLayoutId=itemLayoutId;
    }
    public List<T> getDataSources() {
        return dataSources;
    }
    @Override
    public RecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        Log.e("测试","onCreateViewHolder");
        View root = LayoutInflater.from(mContext).inflate(mItemLayoutId, parent, false);
        return new RecyclerHolder(root);
    }

    @Override
    public void onBindViewHolder(RecyclerHolder holder, int position) {
//        Log.e("测试","onBindViewHolder"+position);
        convert(holder, dataSources.get(position), position);
        holder.itemView.setOnClickListener(getitemviewOnClickListener(position));
        holder.itemView.setOnLongClickListener(getitemviewOnLongClickListener(position));
    }

    /**
     * 让子类实现的抽象方法
     * @param holder  holder
     * @param item    一般是实体bean对象
     * @param position  item的位置
     */
    public abstract void convert(RecyclerHolder holder, T item, int position);

    @Override
    public int getItemCount() {
        return dataSources.size();
    }

    public View.OnClickListener getitemviewOnClickListener(final int position) {
        return new View.OnClickListener() {
            @Override
            public void onClick(@Nullable View v) {
                if (listener != null && v != null) {
                    listener.onItemClick(v, dataSources.get(position), position);//回调
                }
            }
        };
    }
    public View.OnLongClickListener getitemviewOnLongClickListener(final int position) {
        return new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View v) {
                if (longlistener != null && v != null) {
                    longlistener.onItemLongClick(v, dataSources.get(position), position);//回调
                }
                return true;
            }
        };
    }

    /**
     * 长按/短按监听
     */
    //------------------------------------------------------------------------------------
    public interface OnItemClickListener {
        void onItemClick(View view, Object data, int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
    public interface OnItemLongClickListener {
        void onItemLongClick(View view, Object data, int position);
    }
    public void setOnItemLongClickListener(OnItemLongClickListener longlistener) {
        this.longlistener = longlistener;
    }
    //------------------------------------------------------------------------------------


//    /**
//     * Adapter封装
//     * @param list  每次重新获取的数据源（不是总的数据源）
//     * @param requestCode  请求码
//     * @param xv
//     */
//    public void notifyDataSetChanged(List<T> list, RequestCode requestCode, XRecyclerView xv) {
//        List<T> datas=new ArrayList<>();
//        datas.addAll(list);
//        if (xv.getAdapter() == null)
//            xv.setAdapter(this);
//
//        boolean b = datas.size() == Constant.HAS_MORE;
//        switch (requestCode) {
//            case first:
//                dataSources.clear();
//                dataSources.addAll(datas);
//                notifyDataSetChanged();
//                if (b){
//                    xv.refreshComplete();
//                }else{
//                    xv.setNoMore(true);
//                }
//                break;
//            case refresh:
//                dataSources.clear();
//                dataSources.addAll(datas);
//                notifyDataSetChanged();
//                if (b){
//                    xv.refreshComplete();
//                }else{
//                    xv.setNoMore(true);
//                }
//                break;
//            case more:
//                dataSources.addAll(datas);
//                notifyDataSetChanged();
//                if (b){
//                    xv.loadMoreComplete();
//                }else{
//                    xv.setNoMore(true);
//                }
//                break;
//            default:
//                throw new IllegalArgumentException("请填写正确的RequestCode----------------");
//        }
//    }
}
