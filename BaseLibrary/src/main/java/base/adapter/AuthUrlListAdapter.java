package base.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.common.base.R;
import com.common.base.model.AuthUrlVo;

import java.util.List;


public class AuthUrlListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<AuthUrlVo> dataList;

    public AuthUrlListAdapter(Context context, List<AuthUrlVo> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new AuthUrlListItem(LayoutInflater.from(context).inflate(R.layout.item_authurl_list, parent, false)) {
        };
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final AuthUrlListItem item = (AuthUrlListItem) holder;
        final AuthUrlVo vo = dataList.get(position);
        item.checkBox.setChecked(vo.checked);
        item.tvName.setText(vo.name);
        item.itemView.setContentDescription(vo.name);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (vo.checked) {
                    return;
                }
                for (AuthUrlVo authUrlVo : dataList) {
                    authUrlVo.checked = false;
                }
                vo.checked = true;
                if (listener != null){
                    listener.onChecked(vo);
                }
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    private OnCheckChangedListener listener;
    public void setOnCheckChangedListener(OnCheckChangedListener listener){
        this.listener = listener;
    }
    public interface OnCheckChangedListener{
        void onChecked(AuthUrlVo authUrlVo);
    }
    class AuthUrlListItem extends RecyclerView.ViewHolder {
        public CheckBox checkBox;
        public TextView tvName;
        public AuthUrlListItem(View view) {
            super(view);
            checkBox = (CheckBox) view.findViewById(R.id.checkBox);
            tvName = (TextView) view.findViewById(R.id.tvName);
        }
    }
}
