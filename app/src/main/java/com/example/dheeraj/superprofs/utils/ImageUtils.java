package com.example.dheeraj.superprofs.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.example.dheeraj.superprofs.lruCache.Cache;

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
            if(Cache.getStringBitmapFromCache(url) != null){
                return Cache.getStringBitmapFromCache(url);
            }
            InputStream in = new URL(url).openStream();
            Bitmap bitmap = BitmapFactory.decodeStream(in);
            if(bitmap != null) {
                Cache.putStringBitmapInCache(url, bitmap);
            }
            return bitmap;
        } catch (Exception e) {
            Log.e(TAG, "unable to generate bitmap from url",e);
            return null;
        }
    }

}
