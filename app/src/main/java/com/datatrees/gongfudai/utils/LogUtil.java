package com.datatrees.gongfudai.utils;

import android.annotation.SuppressLint;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * LogUtils
 * Created by zhangping on 15/8/11.
 */
public class LogUtil {
    private static final boolean DEBUG = true;

    public static void d(String TAG, String msg) {
        if (DEBUG) {
            Log.d(TAG, "[" + getFileLineMethod() + "]" + msg);
        }
    }

    public static void d(String msg) {
        if (DEBUG) {
            Log.d(getClassName(), "[" + getLineMethod() + "]" + msg);
        }
    }

    public static void e(String msg) {
        if (DEBUG) {
            Log.e(getClassName(), getLineMethod() + msg);
        }
    }

    public static void e(String TAG, String msg) {
        if (DEBUG) {
            Log.e(TAG, getLineMethod() + msg);
        }
    }

    public static void i(String msg) {
        if (DEBUG) {
            Log.i(getClassName(), getLineMethod() + msg);
        }
    }

    public static void i(String TAG, String msg) {
        if (DEBUG) {
            Log.i(TAG, getLineMethod() + msg);
        }
    }

    public static String getFileLineMethod() {
        StackTraceElement traceElement = ((new Exception()).getStackTrace())[2];
        StringBuffer toStringBuffer = new StringBuffer("[")
                .append(traceElement.getFileName()).append(" | ")
                .append(traceElement.getLineNumber()).append(" | ")
                .append(traceElement.getMethodName()).append("]");
        return toStringBuffer.toString();
    }

    public static String getLineMethod() {
        StackTraceElement traceElement = ((new Exception()).getStackTrace())[2];
        StringBuffer toStringBuffer = new StringBuffer("[")
                .append(traceElement.getLineNumber()).append(" | ")
                .append(traceElement.getMethodName()).append("]");
        return toStringBuffer.toString();
    }

    public static String getFileName() {
        StackTraceElement traceElement = ((new Exception()).getStackTrace())[2];
        return traceElement.getFileName();
    }

    public static String getClassName() {
        StackTraceElement traceElement = ((new Exception()).getStackTrace())[2];
        String className = traceElement.getClassName();
        return className.substring(className.lastIndexOf(".") + 1);
    }

    public static String getMethondName() {
        StackTraceElement traceElement = ((new Exception()).getStackTrace())[1];
        return traceElement.getMethodName();
    }

    public static int getLineNumber() {
        StackTraceElement traceElement = ((new Exception()).getStackTrace())[1];
        return traceElement.getLineNumber();
    }

    @SuppressLint("SimpleDateFormat")
    public static String getTime() {
        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        return sdf.format(now);
    }
}
