package com.example.mryan.filedownloaderdemo.Bean;

import java.util.ArrayList;

public class Constants {
    public static ArrayList<DownLoaderBean> downLoaderBeans;
    public static final  String music_list_json= "http://tingapi.ting.baidu.com/v1/restserver/ting?format=json&calback=&method=baidu.ting.billboard.billList&type=2&size=";
    public static final  String music_info_json = "http://tingapi.ting.baidu.com/v1/restserver/ting?format=json&calback=&from=webapp_music&method=baidu.ting.song.play&songid=";
}
