package com.example.mryan.filedownloaderdemo.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.mryan.filedownloaderdemo.Bean.MusicList;
import com.example.mryan.filedownloaderdemo.R;
import com.example.mryan.filedownloaderdemo.Utils.DownLoaderUtils;

import java.util.ArrayList;

public abstract class BaseRecycleAdapter<T> extends RecyclerView.Adapter<BaseViewHolder> {
    private Context mContext;
    private int mLayoutId;
    private ArrayList<T> mData;
    private int position;
    //第一步 定义接口
    public interface OnItemClickListener {
        void onClick(int position);
    }

    private OnItemClickListener listener;
    //第二步， 写一个公共的方法
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    /**
     * 构造方法
     * @param context
     * @param mLayoutId
     * @param mData
     */
    public BaseRecycleAdapter(Context context,int mLayoutId,ArrayList<T> mData){
        this.mContext = context;
        this.mLayoutId = mLayoutId;
        this.mData = mData;
        if(mData == null){
            this.mData = new ArrayList<T>();
        }
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        BaseViewHolder viewHolder = BaseViewHolder.getRecyclerHolder(mContext, parent, mLayoutId);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final BaseViewHolder holder, final int position) {
        this.position = position;
        convert(holder, mData.get(position),position);
        //下载按钮添加监听器
        /*holder.getView(R.id.imageView4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DownLoaderUtils.downloader(mContext, (MusicList.SongListBean) mData.get(position));
            }
        });*/

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClick(position);
                }
            }
        });

    }
    @Override
    public int getItemCount() {
        return mData.size();
    }
    /**
     * 对外提供的方法
     */

    protected abstract void convert(BaseViewHolder holder, T t,int positon);


    /**
     * 针对多种类型的itemView
     *
     * @param <T>
     */
    public interface ConmonItemType<T> {
        int getLayoutId(int itemType); //不同的Item的布局

        int getItemViewType(int position, T t); //type
    }


    public ArrayList<T> getmData() {
        return mData;
    }
    public void setmData(ArrayList<T> mData) {
        this.mData = mData;
    }
    public void add2mData(ArrayList<T> mData) {
        this.mData.addAll(mData);
        notifyDataSetChanged();
    }
    public void delData(int position){
        this.mData.remove(position);
        this.notifyDataSetChanged();
    }
}
