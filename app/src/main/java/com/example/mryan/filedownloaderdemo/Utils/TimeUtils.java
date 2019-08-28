package com.example.mryan.filedownloaderdemo.Utils;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class TimeUtils {
    public static String timeFormat(int file_duration) {
        long  ms = file_duration * 1000 ;
        //毫秒数
        SimpleDateFormat formatter = new SimpleDateFormat("mm:ss",Locale.CHINA);
        //初始化Formatter的转换格式。

        String hms = formatter.format(ms);
        return hms;
    }
}
