package com.example.mryan.filedownloaderdemo.Fragment;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mryan.filedownloaderdemo.Adapter.BaseRecycleAdapter;
import com.example.mryan.filedownloaderdemo.Adapter.BaseViewHolder;
import com.example.mryan.filedownloaderdemo.Bean.Local_Music;
import com.example.mryan.filedownloaderdemo.R;
import com.example.mryan.filedownloaderdemo.Utils.MusicUtils;

import java.util.ArrayList;

public class Finish_fragment extends Fragment {
    View view;
    ArrayList<Local_Music> data;
    private BaseRecycleAdapter adapter;
    RecyclerView recyclerView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.finish_fragment,null);
        data = MusicUtils.getLocalMusic(getContext());

        initView();
        return view;
    }

    private void initView() {
        recyclerView = view.findViewById(R.id.finish_recycleView);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(manager);
        adapter = new BaseRecycleAdapter<Local_Music>(getContext(),R.layout.finish_item,data) {
            @Override
            protected void convert(BaseViewHolder holder, Local_Music local_music, int positon) {
                holder.setText(R.id.finish_name,local_music.getSong());
                holder.setText(R.id.finish_auther,local_music.getSinger());
                holder.setImageBybm(R.id.finish_bm,local_music.getBm());
                holder.setText(R.id.finish_time,local_music.getTime());
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseRecycleAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                System.out.println("点击的" + position);
            }
        });
    }
    public void refush(){
        data = MusicUtils.getLocalMusic(getContext());
        adapter.setmData(data);
        adapter.notifyDataSetChanged();
    }
    class Finish_receiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()){
                case "com.music.downloader.finish":
                    refush();
                    break;
            }
        }
    }
}
