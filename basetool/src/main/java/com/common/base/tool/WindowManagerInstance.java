package com.common.base.tool;

import android.view.WindowManager;

public class WindowManagerInstance {

    public static WindowManagerInstance instance;
    public WindowManager.LayoutParams wmParams=new WindowManager.LayoutParams();
    public boolean hasView = false;

    public WindowManager.LayoutParams getMywmParams(){
        return wmParams;
    }

    public static synchronized WindowManagerInstance getInstance(){
        if (instance == null){
            instance = new WindowManagerInstance();
        }
        return instance;
    }
}
