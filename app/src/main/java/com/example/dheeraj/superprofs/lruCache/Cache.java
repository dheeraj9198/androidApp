package com.example.dheeraj.superprofs.lruCache;

import android.graphics.Bitmap;
import android.util.LruCache;

import org.boon.Str;

/**
 * Created by dheeraj on 20/2/15.
 */
public final class Cache {
    private Cache(){

    }
    private static final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
    // Use 1/8th of the available memory for this memory cache.
    private static final int cacheSize = maxMemory / 8;

    private static final LruCache<String,Bitmap> STRING_BITMAP_LRU_CACHE = new  LruCache<String, Bitmap>(cacheSize){
        @Override
        protected int sizeOf(String s, Bitmap bitmap) {
            return bitmap.getByteCount() / 1024;
        }
    };

    public static void putStringBitmapInCache(String s,Bitmap bitmap){
            if(STRING_BITMAP_LRU_CACHE.get(s) == null){
                STRING_BITMAP_LRU_CACHE.put(s,bitmap);
            }
    }

    public static Bitmap getStringBitmapFromCache(String s){
        return STRING_BITMAP_LRU_CACHE.get(s);
    }

}
