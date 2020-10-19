package com.common.base.model;

import android.view.View;

import java.io.Serializable;

public class DevelopModeVo implements Serializable{
    public String modeName;
    public int mode;
    public String textAfter;
    public View.OnClickListener onClickListener;

    public DevelopModeVo(String modeName, int mode,String textAfter, View.OnClickListener onClickListener) {
        this.modeName = modeName;
        this.mode = mode;
        this.textAfter = textAfter;
        this.onClickListener = onClickListener;
    }

    @Override
    public boolean equals(Object obj) {
        DevelopModeVo developModeVo = (DevelopModeVo) obj;
        if (developModeVo.mode == this.mode){
            return true;
        }
        return super.equals(obj);
    }
}
