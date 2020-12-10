package com.common.base.adapter;



import java.util.List;

import me.drakeet.multitype.Item;
import me.drakeet.multitype.MultiTypeAdapter;
import me.drakeet.multitype.TypePool;

/**
 * adapter基类，返回position
 */
public class BaseMultiTypeAdapter extends MultiTypeAdapter {

    public BaseMultiTypeAdapter( List<? extends Item> items) {
        super(items);
    }

    public BaseMultiTypeAdapter( List<? extends Item> items, TypePool pool) {
        super(items, pool);
    }

    public void setItems( List items) {
        this.items.clear();
        this.items.addAll(items);
    }

    public void addItems( List items) {
        this.items.addAll(items);
    }

    public List<? extends Item> getItems() {
        return items;
    }

    public void removeAll() {
        this.items.clear();
    }

}
