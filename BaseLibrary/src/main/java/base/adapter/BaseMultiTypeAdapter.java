package base.adapter;

import android.support.annotation.NonNull;

import java.util.List;

import me.drakeet.multitype.Item;
import me.drakeet.multitype.MultiTypeAdapter;
import me.drakeet.multitype.TypePool;

/**
 * adapter基类，返回position
 * Created by Yan Kai on 2016/12/6 0006.
 */
public class BaseMultiTypeAdapter extends MultiTypeAdapter {

    public BaseMultiTypeAdapter(@NonNull List<? extends Item> items) {
        super(items);
    }

    public BaseMultiTypeAdapter(@NonNull List<? extends Item> items, TypePool pool) {
        super(items, pool);
    }

    public void setItems(@NonNull List items) {
        this.items.clear();
        this.items.addAll(items);
    }

    public void addItems(@NonNull List items) {
        this.items.addAll(items);
    }

    public List<? extends Item> getItems() {
        return items;
    }

    public void removeAll() {
        this.items.clear();
    }

}
