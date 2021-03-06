package com.common.base.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.common.base.R;
import java.util.List;

/**
 * 表情库
 */
public class EmojiAdapter extends BaseAdapter {

    private List<Integer> mExpression;

    private LayoutInflater mInflater;

    public EmojiAdapter(LayoutInflater mInflater, List<Integer> mExpression) {
        this.mInflater = mInflater;
        this.mExpression=mExpression;
    }

    @Override
    public int getCount() {
        return mExpression.size()+1;
    }

    @Override
    public Object getItem(int position) {
        return mExpression.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {

        ViewHolder viewHolder = null;
        if(convertView == null){
            convertView = mInflater.inflate(R.layout.emoji_item, null);
            viewHolder = new ViewHolder();
            viewHolder.imageview = (ImageView) convertView.findViewById(R.id.rc_emoji_item);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();

        }
        if (position==mExpression.size()){
            viewHolder.imageview.setImageResource(R.mipmap.emoji_delete);
        }else {
            viewHolder.imageview.setImageResource(mExpression.get(position));
        }
        return convertView;
    }
    class ViewHolder{
        ImageView imageview;
    }
}
