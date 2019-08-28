package com.example.mryan.filedownloaderdemo.Service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;

import com.example.mryan.filedownloaderdemo.Bean.Local_Music;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MusicService extends Service {
    private MediaPlayer mediaPlayer;//播放器
    private int currentPosition;//当前播放歌曲的位置
    private int mDuration;//当前音乐时长
    ArrayList<Local_Music> mp3Info;//本地音乐库
    private Timer timer;
    private boolean timeStart = false;
    private TimerTask task;
    private Intent intentPos;
    private NotificationManager notificationManager;
    NotificationCompat.Builder nb;
    //内部类PlayBinder实现Binder,
    public class PlayBinder extends Binder {
        public MusicService getPlayService() {
            return MusicService.this;
        }
    }

    public MusicService(){

    }
    @Override
    public IBinder onBind(Intent intent) {
        return new PlayBinder();
    }

    public void setMp3Info(ArrayList<Local_Music> local_musics){
        this.mp3Info = local_musics;
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnCompletionListener(mp -> next());
        notificationManager = getSystemService(NotificationManager.class);
        nb = new NotificationCompat.Builder(this,"media");
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel chan1 = new NotificationChannel(
                    "media",
                    "会话类型", NotificationManager.IMPORTANCE_DEFAULT);
            //chan1.setLightColor(Color.GREEN);
            //chan1.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            notificationManager.createNotificationChannel(chan1);
            nb.setChannelId("media");
        }

        timer = new Timer();
        intentPos = new Intent("Change_Position");
        task = new TimerTask() {
            @Override
            public void run() {
                intentPos.putExtra("pos", mediaPlayer.getCurrentPosition());
                intentPos.putExtra("duration", mDuration);
                sendBroadcast(intentPos);
            }
        };
    }


    public void play(int position){
        if(position >=0 && position <mp3Info.size()){
            Local_Music mp3= mp3Info.get(position);
            try{
                mediaPlayer.reset();//复位
                mediaPlayer.setDataSource(mp3.getPath());
                mediaPlayer.prepare();
                mediaPlayer.setOnPreparedListener(MediaPlayer::start);
                currentPosition = position;
                mDuration = mp3.getDuration();

            }catch (Exception e){

            }
        }
    }

    /**
     * 播放下一首
     */
    public void next(){

    }
}
