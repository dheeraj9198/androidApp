package com.example.dheeraj.superprofs.utils;

import android.graphics.Bitmap;
import android.util.Log;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by dheeraj on 5/3/15.
 */
public class AppUtils {
    private static final String TAG = AppUtils.class.getSimpleName();

    public static final String manifestFileNameUnencrypted = "manifest.mpd";
    public static final String manifestFileNameEncrypted = "manifestE.mpd";
    public static final String lectureFolderName = "lectures";
    public static final String imageFolderName = "images";

    private AppUtils() {
    }

    public static void deleteAllFilesAndFolderInNewThread(final int lectureId) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                String name = Device.getDir() + File.separator + lectureFolderName + File.separator + lectureId;
                try {
                    FileUtils.deleteDirectory(new File(name));
                } catch (Exception e) {
                    Log.e(TAG, "unable to delete file recursively", e);
                }
            }
        });
        executorService.shutdown();
    }

    public static String getLectureFolderName(String lectureID) {
        String name = Device.getDir() + File.separator + lectureFolderName + File.separator + lectureID;
        makeDirIfNotExists(name);
        return name;
    }

    private static void makeDirIfNotExists(String file) {
        File file1 = new File(file);
        if (!file1.exists()) {
            file1.mkdirs();
        }
    }

    public static File getImageFileNameFromURL(String url) {
        String file = Device.getDir() + File.separator + imageFolderName;
        makeDirIfNotExists(file);
        return new File(file + File.separator + MD5Checksum.getMd5OfString(url) + ".PNG");
    }

    public static void saveBitmapToStorage(Bitmap bitmap, File file) {
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, fos);
            fos.close();
        } catch (FileNotFoundException e) {
            Log.e(TAG, "File not found: " + e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, "Error accessing file: " + e.getMessage());
        }
    }


}
