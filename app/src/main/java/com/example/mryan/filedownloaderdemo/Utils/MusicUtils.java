package com.example.mryan.filedownloaderdemo.Utils;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.MediaScannerConnection;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import com.example.mryan.filedownloaderdemo.Bean.Local_Music;

import java.io.File;
import java.util.ArrayList;

public class MusicUtils {
    public static ArrayList<Local_Music> getLocalMusic(Context context){
        ArrayList<Local_Music> list = new ArrayList<Local_Music>();
        getAllFiles(context);
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null,
                MediaStore.Audio.Media.DEFAULT_SORT_ORDER);


        if (cursor != null) {
            while (cursor.moveToNext()) {
                Local_Music song = new Local_Music();
                song.setSong( cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)));
                song.setSinger( cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)));
                song.setPath(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)));
                song.setDuration( cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)));
                song.setSize( cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE)));
                song.setTime(formatTime(song.getDuration()));
                song.setBm(getBit(song.getPath()));
                if (song.getSize() > 100 * 800) {//过滤掉短音频
                    // 分离出歌曲名和歌手
                    if (song != null && song.getSong()!=null && song.getSong().contains("-")) {
                        String[] str = song.getSong().split("-");
                        song.setSinger( str[0]);
                        song.setSong( str[1]);
                    }
                    list.add(song);
                }
            }
            // 释放资源
            cursor.close();
        }
        System.out.println("查找到的歌曲" + list.size());
        return list;
    }



    private static void getAllFiles(Context context){
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Music/");
//2.获取所有文件
        String[] files = file.list();
        Log.e("本地歌曲" , files.toString());
        for (int i = 0; i < files.length; i++) {
            files[i] = file.getAbsolutePath() + File.separator + files[i];
        }
//3.扫描文件，并添加到MediaStore
        MediaScannerConnection.scanFile(context, files, null, null);

    }
    //格式化时间
    private  static String formatTime(int time) {
        if (time / 1000 % 60 < 10) {
            return time / 1000 / 60 + ":0" + time / 1000 % 60;
        } else {
            return time / 1000 / 60 + ":" + time / 1000 % 60;
        }
    }

    private static Bitmap getBit(String uri){
        MediaMetadataRetriever mediaMetadataRetriever=new MediaMetadataRetriever();
        mediaMetadataRetriever.setDataSource(uri);
        Bitmap bitmap;
        byte[] picture = mediaMetadataRetriever.getEmbeddedPicture();
        if(picture == null || picture.length == 0){
            bitmap = null;
        }
        else{
            bitmap= BitmapFactory.decodeByteArray(picture,0,picture.length);
        }
        return bitmap;
    }

}
