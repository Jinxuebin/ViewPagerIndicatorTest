package com.jin.viewpagerindicator.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.format.Formatter;
import android.util.Log;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 简化你的打印日志
 * Created by Jin on 2017/8/18.
 */
 public class LogUtils{

    /**
     * 日志开关
     * 你可在配置文件中设置是否开启开关
     * 当你代码基本编辑完成可关闭此开关
     * S_G:switch_global
     * S_d:switch_log_d
     * S_e:switch_log_e
     * S_i:switch_log_i
     * S_v:switch_log_v
     * S_w:switch_log_w
     */
    private static boolean S_G = true;
    private static boolean S_d = true;
    private static boolean S_e = true;
    private static boolean S_i = true;
    private static boolean S_v = true;
    private static boolean S_w = true;


    public static void setsG(boolean sG) {
        S_G = sG;
        S_d = sG;
        S_e = sG;
        S_i = sG;
        S_v = sG;
        S_w = sG;
    }


    public static void setSd(boolean sd) {
        S_d = sd;
    }

    public static void setSe(boolean se) {
        S_e = se;
    }

    public static void setSi(boolean si) {
        S_i = si;
    }

    public static void setSv(boolean sv) {
        S_v = sv;
    }

    public static void setSw(boolean sw) {
        S_w = sw;
    }





    public static void d(String s, Object... args){
        if(!S_d) return;
        Log.d("LogUtils-d",print(s,args));
    }

    public static void d(Context c, String s, Object... args){
        String name = matchClassName(c);
        d("-" + name + s,args);
    }

    public static void e(String s, Object... args){
        if(!S_e) return;
        Log.e("LogUtils-e",print(s,args));
    }

    public static void e(Context c, String s, Object... args) {
        String name = matchClassName(c);
        e("-" + name + s,args);
    }

    public static void i(String s, Object... args){
        if(!S_i) return;
        Log.i("LogUtils-i",print(s,args));
    }

    public static void i(Context c, String s, Object... args){
        String name = matchClassName(c);
        i("-" + name + s,args);
    }

    public static void v(String s, Object... args){
        if(!S_v) return;
        Log.v("LogUtils-v",print(s,args));
    }

    public static void v(Context c, String s, Object... args){
        String name = matchClassName(c);
        v("-" + name + s,args);
    }

    @SuppressWarnings("all")
    public static void w(String s, Object... args){
        if(!S_w) return;
        Log.w("LogUtils-w",print(s,args));
    }

    public static void w(Context c, String s, Object... args){
        String name = matchClassName(c);
        w("-" + name + s,args);
    }




    /**
     * 用于输出文件的大小,将字节数换算成MB单位或者KB单位
     * @param c context
     * @param str str
     * @param args args
     */
    public static void l(Context c, String str, long... args){
        if(args.length > 0){
            Object[] size = new String[args.length];
            int j = 0;
            for(long i : args){
                size[j++] = Formatter.formatFileSize(c,i);
            }
            i(c,str,size);
        }else{
            i(str);
        }
    }

    private static String matchClassName(Context c) {
        Matcher matcher = Pattern.compile("[a-zA-Z]+$").matcher(c.getClass().getName());
        return matcher.find() ? matcher.group(0) : c.getClass().getName();
    }


    @NonNull
    private static String getNameFormTrace(StackTraceElement[] traceElements, int place) {
        StringBuilder taskName = new StringBuilder();
        if (traceElements != null && traceElements.length > place) {
            StackTraceElement traceElement = traceElements[place];
            taskName.append("at ")
                    .append(traceElement.getFileName())
                    .append(".")
                    .append(traceElement.getMethodName())
                    .append("(")
                    .append(traceElement.getFileName())
                    .append(":")
                    .append(traceElement.getLineNumber())
                    .append("):\n");
        }
        return taskName.toString();
    }

    private static String getContent(String msg, int place, Object... args) {
        try {
            String sourceLinks = getNameFormTrace(Thread.currentThread().getStackTrace(), place);
            return sourceLinks + String.format(Locale.ENGLISH,msg,args);
        }catch (Throwable throwable) {
            return msg;
        }
    }

    /**
     * 找到调用LogUtils的位置.
     * @param content content
     * @param args args
     * @return msg
     */
    private static String print(String content,Object... args) {
           return getContent(content,5,args);
    }
}
