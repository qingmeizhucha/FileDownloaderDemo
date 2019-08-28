package com.example.mryan.filedownloaderdemo;

import android.app.Application;
import android.os.Build;
import android.os.StrictMode;

import com.liulishuo.filedownloader.FileDownloader;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
     //   FileDownloader.init(getApplicationContext());
        initOkHttpUtils();//初始化OKHTTP
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
        }

    }
    //初始化网络请求配置
    private void initOkHttpUtils() {
        //设置网络请求参数
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10000,TimeUnit.MILLISECONDS)
                .readTimeout(10000,TimeUnit.MILLISECONDS)
                .build();
        //okhttp跟库网络参数发起请求，get/post
        OkHttpUtils.initClient(okHttpClient);
    }
}
