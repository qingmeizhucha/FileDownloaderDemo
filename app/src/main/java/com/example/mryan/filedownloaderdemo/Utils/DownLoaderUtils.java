package com.example.mryan.filedownloaderdemo.Utils;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

import com.example.mryan.filedownloaderdemo.Bean.Constants;
import com.example.mryan.filedownloaderdemo.Bean.DownLoaderBean;
import com.example.mryan.filedownloaderdemo.Bean.MusicList;
import com.example.mryan.filedownloaderdemo.Fragment.ProgressFragment;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadLargeFileListener;
import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.notification.FileDownloadNotificationHelper;
import com.qw.soul.permission.SoulPermission;
import com.qw.soul.permission.bean.Permission;
import com.qw.soul.permission.callbcak.CheckRequestPermissionListener;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Vector;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

import okhttp3.Call;

import static android.net.wifi.SupplicantState.INVALID;

public class DownLoaderUtils {
    public static void downloader(final Context context, final MusicList.SongListBean bean) {

        //检查是否有存储权限
        SoulPermission.getInstance().checkAndRequestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, new CheckRequestPermissionListener() {
            @Override
            public void onPermissionOk(Permission permission) {
                //有权限去下载
                downGoon(context,bean);
            }
            @Override
            public void onPermissionDenied(Permission permission) {
                Toast.makeText(context,"你需要允许存储权限才能下载",Toast.LENGTH_SHORT).show();
            }
        });

    }
    public static void downGoon(final Context context, final MusicList.SongListBean bean) {
        String url;
        url = bean.getUrl();
        String state;
        String path = null;
        state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)){
            path = Environment.getExternalStorageDirectory().getAbsolutePath();
        }
        File file = new File(path + "/Music/");
        //不存在创建
        if (!file.exists()) {
            file.mkdir();
            System.out.println("没有");
        }
        FileDownloader.setup(context);
        FileDownloader.getImpl()
                .create(url)
                .setPath(file.getAbsolutePath() + "/" + bean.getTitle() + ".mp3")
                .setCallbackProgressTimes(30)
                .setCallbackProgressMinInterval(500)
                .setListener(new FileDownloadLargeFileListener() {
                    @Override
                    protected void started(BaseDownloadTask task) {
                        super.started(task);
                        Toast.makeText(context,"开始下载",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent("com.music.downloader.start");
                        DownLoaderBean db = new DownLoaderBean();
                        db.setDownloaderId(task.getId());
                        db.setName(bean.getAuthor());
                        db.setTime(TimeUtils.timeFormat(bean.getFile_duration()));
                        db.setTitle(bean.getTitle());
                        db.setImageUrl(bean.getPic_small());
                        db.setStart(false);
                        intent.putExtra("start",db);
                        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                        System.out.println("发送了广播");
                    }

                    @Override
                    protected void pending(BaseDownloadTask task, long soFarBytes, long totalBytes) {

                    }
                    @Override
                    protected void progress(BaseDownloadTask task, long soFarBytes, long totalBytes) {
                        Intent intent = new Intent("com.music.downloader.progressBar");
                        DownLoaderBean db = new DownLoaderBean();
                        db.setName(bean.getAuthor());
                        db.setTime(TimeUtils.timeFormat(bean.getFile_duration()));
                        db.setSoFarsize(soFarBytes);
                        db.setTotalSize(totalBytes);
                        db.setTitle(bean.getTitle());
                        db.setImageUrl(bean.getPic_small());
                        db.setStart(true);
                        System.out.println("下载进度" + soFarBytes/totalBytes*100);
                        intent.putExtra("progress",db);
                        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                        //FileDownloadNotificationHelper
                    }

                    @Override
                    protected void paused(BaseDownloadTask task, long soFarBytes, long totalBytes) {
                        System.out.println("下载暂停");
                    }

                    @Override
                    protected void completed(BaseDownloadTask task) {
                        System.out.println("下载完成");
                        Intent intent = new Intent("com.music.downloader.finish");
                        intent.putExtra("DownloaderId",task.getId());
                        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                    }

                    @Override
                    protected void error(BaseDownloadTask task, Throwable e) {
                        e.printStackTrace();
                        System.out.println("下载出错");
                    }

                    @Override
                    protected void warn(BaseDownloadTask task) {
                        Toast.makeText(context,"已经在下载队列中",Toast.LENGTH_SHORT).show();
                    }
                }).start();
    }


}
