package com.common.base.adapter;



import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

/**
 * 带标题的PagerAdapter
 */
public class FragmentWithTitlePagerAdapter extends FragmentPagerAdapter {

    protected List<? extends Fragment> fragments;
    protected List<String> titles;

    public FragmentWithTitlePagerAdapter(FragmentManager fm, List<? extends Fragment> list, List<String> titles) {
        super(fm);
        this.fragments = list;
        this.titles = titles;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (null == titles || position >= titles.size()) {
            return "";
        } else {
            return titles.get(position);
        }
    }


    // 动态设置我们标题的方法
    public void setPageTitle(int position, String title) {
        if(position >= 0 && position < titles.size()) {
            titles.set(position, title);
            notifyDataSetChanged();
        }
    }

}
