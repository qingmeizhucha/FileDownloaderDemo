package com.example.mryan.filedownloaderdemo.Bean;

import android.graphics.Bitmap;

public class Local_Music {
    private String song;//歌曲名
    private String singer;//歌手
    private String path;//地址
    private int duration;//长度

    private String time;//时间
    private Bitmap bm;//专辑图片
    private long size;//歌曲大小
    public String getTime() {
        return time;
    }
    public void setTime(String time) {
        this.time = time;
    }
    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }


    public String getSong() {
        return song;
    }

    public void setSong(String song) {
        this.song = song;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public Bitmap getBm() {
        return bm;
    }

    public void setBm(Bitmap bm) {
        this.bm = bm;
    }
}
