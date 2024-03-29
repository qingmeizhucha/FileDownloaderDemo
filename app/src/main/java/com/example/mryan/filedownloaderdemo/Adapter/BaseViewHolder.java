package com.example.mryan.filedownloaderdemo.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class BaseViewHolder extends RecyclerView.ViewHolder {
    private SparseArray<View> mViews;//存储控件
    private View mConvertView;
    private Context mContext;

    public BaseViewHolder(Context context, View itemView) {
        super(itemView);
        this.mContext = context;
        mConvertView = itemView;
        mViews = new SparseArray<View>();
    }
    /**
     * 获取一个viewholder的方法
     */
    public static BaseViewHolder getRecyclerHolder(Context context, ViewGroup parent, int layoutId){
        View itemView = LayoutInflater.from(context).inflate(layoutId, parent, false);
        BaseViewHolder viewHolder = new BaseViewHolder(context, itemView);
        return viewHolder;
    }


    /**
     * 获取控件
     */
    public <T extends View> T getView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = mConvertView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    /**
     * 给TextView设置setText方法
     */
    public BaseViewHolder setText(int viewId, String text) {
        TextView tv = getView(viewId);
        tv.setText(text);
        return this;
    }


    /**
     * 根据bitmap加载图片
     */

    public BaseViewHolder setImageBybm(int viewId, Bitmap bm){
        ImageView imageView = getView(viewId);
        imageView.setImageBitmap(bm);
        return this;
    }
    /**
     * 给ImageView设置setImageResource方法
     */
    public BaseViewHolder setImageResource(int viewId, int resId) {
        ImageView view = getView(viewId);
        view.setImageResource(resId);
        return this;
    }

    /**
     * 给ImageView设置setImageResource方法
     */
    public BaseViewHolder setImageUrl(int viewId, String url) {
        ImageView view = getView(viewId);
        Glide.with(view).load(url).into(view);
        return this;
    }

    /**
     * 添加点击事件
     */
    public BaseViewHolder setOnClickListener(int viewId, View.OnClickListener listener) {
        View view = getView(viewId);
        view.setOnClickListener(listener);
        return this;
    }

}
