package com.example.mryan.filedownloaderdemo;

import android.Manifest;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.TableLayout;
import android.support.design.widget.TabLayout;
import android.widget.Toast;

import com.example.mryan.filedownloaderdemo.Adapter.Fragment_Adapter;
import com.example.mryan.filedownloaderdemo.Fragment.DownloaderFragment;
import com.example.mryan.filedownloaderdemo.Fragment.Finish_fragment;
import com.example.mryan.filedownloaderdemo.Fragment.ProgressFragment;
import com.qw.soul.permission.SoulPermission;
import com.qw.soul.permission.bean.Permission;
import com.qw.soul.permission.callbcak.CheckRequestPermissionListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private String[] titles = new String[]{"网络音乐","正在下载","本地音乐"};
    LayoutInflater layoutInflater;
    private ViewPager viewPager;
    private ArrayList<Fragment> list;
    private TabLayout tabLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        layoutInflater = getLayoutInflater();
        //检查是否有存储权限
        SoulPermission.getInstance().checkAndRequestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, new CheckRequestPermissionListener() {
            @Override
            public void onPermissionOk(Permission permission) {
                //有权限才能继续运行
                initTable();
                initFragment();
            }
            @Override
            public void onPermissionDenied(Permission permission) {
                finish();
            }
        });


    }
    private void initTable() {
        tabLayout = findViewById(R.id.tabMode);
        tabLayout.addTab(tabLayout.newTab().setText(titles[0]));
        tabLayout.addTab(tabLayout.newTab().setText(titles[1]));
        tabLayout.addTab(tabLayout.newTab().setText(titles[1]));
    }
    private void initFragment() {
        viewPager = findViewById(R.id.view_pager);
        list = new ArrayList<Fragment>();
        list.add(new DownloaderFragment());
        list.add(new ProgressFragment());
        list.add(new Finish_fragment());
        Fragment_Adapter adapter = new Fragment_Adapter(getSupportFragmentManager(),list,titles);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }


}
