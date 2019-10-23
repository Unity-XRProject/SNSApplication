package com.sds.xr.utils;

import android.content.Context;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {
    private static final String LOG_TAG = "XR_";
    private static final String MSG_FORMAT = "%s: %s";
    private static final String FILE_NAME_FORMAT = "yyyy-MM-dd";
    private static final String TIME_FORMAT = "HH:mm:ss.SSS";
    private static final String PATH_LOGS = "logs";
    private static final String FILE_EXTENSION = ".log";

    private static final Logger instance = new Logger();
    private static Context context;
    public static void init(Context ctxt) {
        context = ctxt;
    }

    private static void write(String msg) {
        File logDirectory = new File(context.getExternalFilesDir(null), PATH_LOGS);
        if(!logDirectory.exists()) {
            logDirectory.mkdir();
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat(FILE_NAME_FORMAT);
        String fileName = dateFormat.format(new Date()) + FILE_EXTENSION;
        String path = context.getExternalFilesDir(null) + "/" + PATH_LOGS + "/" + fileName;
        File logFile = new File(path);
        try {
            if(!logFile.exists()) {
                logFile.createNewFile();
            }
            BufferedWriter bw = new BufferedWriter(new FileWriter(logFile, true));

            bw.write(msg);
            bw.newLine();
            bw.flush();
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void v(String msg) {
//        if (!BuildConfig.DEBUG) return;
        String fMsg = String.format(MSG_FORMAT, getCallerInfo(), msg);
        Log.v(LOG_TAG, fMsg);
        write(fMsg);
    }

    public static void d(String msg) {
//        if (!BuildConfig.DEBUG) return;
        String fMsg = String.format(MSG_FORMAT, getCallerInfo(), msg);
        Log.d(LOG_TAG, fMsg);
        write(fMsg);
    }

    public static void i(String msg) {
//        if (!BuildConfig.DEBUG) return;
        String fMsg = String.format(MSG_FORMAT, getCallerInfo(), msg);
        Log.i(LOG_TAG, fMsg);
        write(fMsg);
    }

    public static void w(String msg) {
//        if (!BuildConfig.DEBUG) return;
        String fMsg = String.format(MSG_FORMAT, getCallerInfo(), msg);
        Log.d(LOG_TAG, fMsg);
        write(fMsg);
    }

    public static void e(String msg) {
//        if (!BuildConfig.DEBUG) return;
        String fMsg = String.format(MSG_FORMAT, getCallerInfo(), msg);
        Log.e(LOG_TAG, fMsg);
        write(fMsg);
    }

    private static String getCallerInfo() {
        StackTraceElement[] elements = new Exception().getStackTrace();
        String className = elements[2].getClassName();
        int length = className.length();

        SimpleDateFormat dateFormat = new SimpleDateFormat(TIME_FORMAT);

        return dateFormat.format(new Date()) + " " + className.substring(className.lastIndexOf(".") + 1, length) + " [" + elements[2].getLineNumber() + "]";
    }
}
