package com.example.mryan.filedownloaderdemo.Fragment;

import android.arch.core.executor.TaskExecutor;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.mryan.filedownloaderdemo.Adapter.BaseRecycleAdapter;
import com.example.mryan.filedownloaderdemo.Adapter.BaseViewHolder;
import com.example.mryan.filedownloaderdemo.Bean.DownLoaderBean;
import com.example.mryan.filedownloaderdemo.Bean.MusicList;
import com.example.mryan.filedownloaderdemo.R;
import com.example.mryan.filedownloaderdemo.Utils.DownLoaderUtils;

import java.util.ArrayList;

public class ProgressFragment extends Fragment {
    View view;
    TextView percent;
    RecyclerView recyclerView;
    BaseRecycleAdapter adapter;
    Progress_Recevier recevier;
    LocalBroadcastManager broadcastManager;
    ProgressBar progressBar;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.progress_fragment,null);
        recyclerView = view.findViewById(R.id.progress_recycle);
        progressBar = view.findViewById(R.id.progressBar);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(manager);
        initAdapter();
        registerReceiver();
        return view;
    }
    private void registerReceiver(){
        broadcastManager = LocalBroadcastManager.getInstance(getActivity());
        recevier = new Progress_Recevier();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.music.downloader.progressBar");
        intentFilter.addAction("com.music.downloader.start");
        intentFilter.addAction("com.music.downloader.finish");
        broadcastManager.registerReceiver(recevier,intentFilter);
    }
    private void initAdapter() {
        adapter = new BaseRecycleAdapter<DownLoaderBean>(getContext(),R.layout.progress_item,null) {
            @Override
            protected void convert(BaseViewHolder holder, DownLoaderBean bean, int positon) {
                holder.setText(R.id.title,bean.getTitle());
                holder.setText(R.id.name,bean.getName());
                holder.setText(R.id.time,bean.getTime());
                holder.setImageUrl(R.id.img,bean.getImageUrl());
                holder.setText(R.id.percent,"0%");
            }
        };
        recyclerView.setAdapter(adapter);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        broadcastManager.unregisterReceiver(recevier);
    }


    /**
     * 更新进度条
     * intent中携带进度条信息
     * @param intent
     */
    public void updateProgress(Intent intent){
        DownLoaderBean bean = (DownLoaderBean) intent.getSerializableExtra("progress");
        long x = bean.getTotalSize();
        long y = bean.getSoFarsize();
        float progress = (float) y / x * 100;
        while(progress<0){
            progress*=10;
        }
        final float pp = progress;
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar = view.findViewById(R.id.progressBar);
                percent = view.findViewById(R.id.percent);
                progressBar.setProgress((int)pp);
                percent.setText((int)pp+"%");
            }
        });
    }

    /**
     * 添加一个新的任务到下载界面
     * intent中包含下载信息
     * @param intent
     */
    public void addNewStart(Intent intent){

        DownLoaderBean bean = (DownLoaderBean) intent.getSerializableExtra("start");
        final ArrayList<DownLoaderBean> newdata = new ArrayList<DownLoaderBean>();
        newdata.add(bean);
        adapter.add2mData(newdata);
        adapter.notifyDataSetChanged();
    }
    public void downLoaderFinish(Intent intent){
        //下载完成将进度条设置为100%
        progressBar.setProgress(100);
        //休眠0.5秒缓冲一下
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //根据任务ID获取在数组中的位置并删除
        adapter.delData(getPositionByTaskId(intent.getIntExtra("DownloaderId",0)));
        //刷新adapter
        adapter.notifyDataSetChanged();
    }


    private int getPositionByTaskId(int taskid){
       ArrayList<DownLoaderBean> beans = adapter.getmData();
       for(int i=0; i<beans.size(); i++){
           if(beans.get(i).getDownloaderId() == taskid){
               return i;
           }
       }
       return 0;
    }
    class Progress_Recevier extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {

            switch (intent.getAction()){
                case "com.music.downloader.progressBar":
                    updateProgress(intent);
                    break;
                case "com.music.downloader.start":
                    addNewStart(intent);
                    break;
                case "com.music.downloader.finish":
                    downLoaderFinish(intent);
                    break;
            }
        }
    }
}
