package com.example.mryan.filedownloaderdemo.Bean;

import java.io.Serializable;

public class DownLoaderBean implements Serializable {
    String title;
    String name;
    String time;
    String imageUrl;
    long soFarsize;
    long totalSize;
    boolean isStart;
    int downloaderId;

    public int getDownloaderId() {
        return downloaderId;
    }

    public void setDownloaderId(int downloaderId) {
        this.downloaderId = downloaderId;
    }

    public boolean isStart() {
        return isStart;
    }

    public void setStart(boolean start) {
        isStart = start;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public long getSoFarsize() {
        return soFarsize;
    }

    public void setSoFarsize(long soFarsize) {
        this.soFarsize = soFarsize;
    }

    public long getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(long totalSize) {
        this.totalSize = totalSize;
    }


}
