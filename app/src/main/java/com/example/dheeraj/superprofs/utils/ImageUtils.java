package com.example.dheeraj.superprofs.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.InputStream;
import java.net.URL;

/**
 * Created by dheeraj on 18/2/15.
 */
public class ImageUtils {
    private static final String TAG = ImageUtils.class.getSimpleName();

    private ImageUtils() {

    }

    public static Bitmap getBitmapFromUrl(String url) {
        try {
            InputStream in = new URL(url).openStream();
            return BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e(TAG, "unable to generate bitmap from url");
            return null;
        }
    }

}
