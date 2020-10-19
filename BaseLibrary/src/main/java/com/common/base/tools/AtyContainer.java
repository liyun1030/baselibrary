package com.common.base.tools;

import android.app.Activity;

import com.common.base.activity.BaseActivity;

import java.util.ArrayList;
import java.util.List;

public class AtyContainer {

    private AtyContainer() {
    }

    private static AtyContainer instance = new AtyContainer();
    private static List<Activity> activityStack = new ArrayList<Activity>();

    public static AtyContainer getInstance() {
        return instance;
    }

    public void addActivity(Activity aty) {
        activityStack.add(aty);
    }

    public void removeActivity(Activity aty) {
        activityStack.remove(aty);
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        for (int i = 0, size = activityStack.size(); i < size; i++) {
            if (null != activityStack.get(i)) {
                activityStack.get(i).finish();
            }
        }
        activityStack.clear();
    }

    /**
     * 结束所有Activity
     */
    public void finishOtherActivity(BaseActivity act) {
        activityStack.remove(act);
        finishAllActivity();
        activityStack.add(act);
    }

    public Activity getFrontActivity() {
       if (activityStack.size()>0){
           Activity activity = activityStack.get(activityStack.size()-1);
           return activity;
       }else{
           return null;
       }
    }


}