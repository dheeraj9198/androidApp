package com.example.dheeraj.superprofs.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.util.DisplayMetrics;

import java.io.File;
import java.io.IOException;

/**
 * Created by dheeraj on 19/2/15.
 */
public class Device {
    private static final DisplayMetrics dimension = new DisplayMetrics();


    private Device() {

    }

    public static int getWidth(Activity activity) {
        activity.getWindowManager().getDefaultDisplay().getMetrics(dimension);
        return dimension.widthPixels;
    }

    public static String getDir() {
        String dir = Environment.getExternalStorageDirectory().getAbsolutePath() +
                File.separator + "superprofs";


        File file = new File(dir);
        if (!file.exists()) {
            if (!file.mkdir()) {
                throw new RuntimeException("Unable to create SP dir on SDCard");
            }
        }
        return dir;

    }
}
