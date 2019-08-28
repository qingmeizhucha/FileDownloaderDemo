package com.example.mryan.filedownloaderdemo.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class Fragment_Adapter extends FragmentPagerAdapter {
    private FragmentManager mfragmentManager;
    private ArrayList<Fragment> mlist;
    private String[] tabNames;//tab选项名字
    public Fragment_Adapter(FragmentManager fm, ArrayList<Fragment> list, String[] title) {
        super(fm);
        this.mlist = list;
        this.tabNames = title;
    }
    @Override
    public Fragment getItem(int arg0) {
        return mlist.get(arg0);//显示第几个页面
    }

    @Override
    public int getCount() {
        return mlist.size();//有几个页面
    }
    public CharSequence getPageTitle(int position) {
        //返回tab选项的名字
        return tabNames[position];
    }
}
