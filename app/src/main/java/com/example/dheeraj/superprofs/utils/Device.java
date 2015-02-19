package com.example.dheeraj.superprofs.utils;

import android.app.Activity;
import android.util.DisplayMetrics;

/**
 * Created by dheeraj on 19/2/15.
 */
public class Device {
    private static DisplayMetrics dimension = new DisplayMetrics();


    private Device() {

    }

    public static int getWidth(Activity activity) {
        activity.getWindowManager().getDefaultDisplay().getMetrics(dimension);
        return dimension.widthPixels;
    }
}
