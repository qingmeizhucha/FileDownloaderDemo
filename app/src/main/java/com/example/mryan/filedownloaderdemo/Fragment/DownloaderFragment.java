package com.example.mryan.filedownloaderdemo.Fragment;

import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.alibaba.fastjson.JSON;
import com.example.mryan.filedownloaderdemo.Adapter.BaseRecycleAdapter;
import com.example.mryan.filedownloaderdemo.Adapter.BaseViewHolder;
import com.example.mryan.filedownloaderdemo.Bean.Constants;
import com.example.mryan.filedownloaderdemo.Bean.MusicList;
import com.example.mryan.filedownloaderdemo.R;
import com.example.mryan.filedownloaderdemo.Utils.DownLoaderUtils;
import com.example.mryan.filedownloaderdemo.Utils.EndLessOnScrollListener;
import com.example.mryan.filedownloaderdemo.Utils.TimeUtils;
import com.githang.hiloadmore.LoadMoreHandler;
import com.githang.hiloadmore.recyclerview.LoadMoreRecyclerViewContainer;
import com.githang.hiloadmore.recyclerview.RecyclerFooterView;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadLargeFileListener;
import com.liulishuo.filedownloader.FileDownloader;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;

public class DownloaderFragment extends Fragment {
    View view;
    private RecyclerView recyclerView;
    private BaseRecycleAdapter mAdapter;

    private int nowData;
    private int lastData;
    private int pos;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.download_fragment,null);
        nowData = 10;
        lastData = 0;
        initView();
        getData(nowData);
        return view;
    }

    private void initView() {
        recyclerView = view.findViewById(R.id.recycleView);
        mAdapter = new BaseRecycleAdapter<MusicList.SongListBean>(getContext(),R.layout.item,null) {

            @Override
            protected void convert(BaseViewHolder holder, MusicList.SongListBean songListBean,int position) {
                holder.setImageUrl(R.id.music_item_bmg,songListBean.getPic_small());
                holder.setText(R.id.music_item_artist,songListBean.getArtist_name());
                holder.setText(R.id.music_item_title,songListBean.getTitle());
                holder.setText(R.id.music_time,TimeUtils.timeFormat(songListBean.getFile_duration()));

            }
        };
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(manager);
        // mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(mAdapter);
        recyclerView.addOnScrollListener(new EndLessOnScrollListener(manager) {
            @Override
            public void onLoadMore(int currentPage) {
                lastData = nowData;
                getData(nowData+=10);
            }
        });
        mAdapter.setOnItemClickListener(new BaseRecycleAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                DownLoaderUtils.downloader(getContext(), (MusicList.SongListBean) mAdapter.getmData().get(position));
            }
        });
    }

    /**
     * pager表示需要加载的歌曲数量
     * @param pager
     */
    private void getData(final int pager) {
        OkHttpUtils.get()
                .url(Constants.music_list_json+Integer.toString(pager))
                .id(100)
                .addHeader("User-Agent","Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:0.9.4)")
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Toast.makeText(getContext(),"网络加载失败" + id,Toast.LENGTH_SHORT).show();
                Log.e("InternetError",e.toString());
            }

            @Override
            public void onResponse(String response, int id) {
                final MusicList musicList = JSON.parseObject(response,MusicList.class);
                for (MusicList.SongListBean bean:musicList.getSong_list()) {
                    setMusicUrl(bean);
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ArrayList<MusicList.SongListBean> newMu = new ArrayList<MusicList.SongListBean>();
                        for(int i=lastData; i<pager; i++){
                            newMu.add(musicList.getSong_list().get(i));
                        }
                        mAdapter.add2mData(newMu);
                        mAdapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }

    private void setMusicUrl(final MusicList.SongListBean bean){
        OkHttpUtils.get()
                .url(Constants.music_info_json+bean.getSong_id())
                .id(100)
                .addHeader("User-Agent","Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:0.9.4)")
                .build().execute(new StringCallback() {
            @Override//网络请求错误回调
            public void onError(Call call, Exception e, int id) {
                //  Log.e(TAG,"onError"+e.getMessage());
                System.out.println("网络错误+");
            }

            @Override//网络请求成功回调
            public void onResponse(String response, int id) {
                try {
                    JSONObject object = new JSONObject(response);
                    bean.setUrl(object.optJSONObject("bitrate").optString("file_link"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
