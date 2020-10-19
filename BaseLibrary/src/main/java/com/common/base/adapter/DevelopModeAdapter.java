package com.common.base.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.common.base.R;
import com.common.base.activity.SwitchAuthUrlActivity;
import com.common.base.model.DevelopModeEnum;
import com.common.base.model.DevelopModeVo;

import java.util.List;


public class DevelopModeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<DevelopModeVo> modeList;

    public DevelopModeAdapter(Context context, List<DevelopModeVo> modeVoList) {
        this.context = context;
        this.modeList = modeVoList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new DevelopModeItem(LayoutInflater.from(context).inflate(R.layout.item_developmode, parent, false)) {
        };
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof DevelopModeItem) {
            DevelopModeItem item = (DevelopModeItem) holder;
            final DevelopModeVo vo = modeList.get(position);
            item.tvName.setText(modeList.get(position).modeName);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (vo.mode == DevelopModeEnum.DevelopMode.切换认证中心URL.mode) {
                        switchAuthUrl();
                    } else {
                        String temp;
                        temp = vo.modeName;
                        vo.modeName = vo.textAfter;
                        vo.textAfter = temp;
                        vo.onClickListener.onClick(view);
                        notifyDataSetChanged();
                    }
                }
            });
        }
    }

    /**
     * 切换认证中心URL
     */
    private void switchAuthUrl() {
        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            Intent intent = new Intent(activity, SwitchAuthUrlActivity.class);
            activity.startActivityForResult(intent, SwitchAuthUrlActivity.REQUEST_CODE_SWITCH_AUTH);
        }
    }

    @Override
    public int getItemCount() {
        return modeList.size();
    }

    class DevelopModeItem extends RecyclerView.ViewHolder {
        public TextView tvName;

        public DevelopModeItem(View view) {
            super(view);
            tvName = (TextView) view.findViewById(R.id.tvName);
        }
    }
}
