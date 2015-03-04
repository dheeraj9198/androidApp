package com.example.dheeraj.superprofs.services;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.example.dheeraj.superprofs.CourseActivity;
import com.example.dheeraj.superprofs.DownloadActivity;
import com.example.dheeraj.superprofs.R;
import com.example.dheeraj.superprofs.downloader.LectureDownloader;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;

public class DownloaderService extends Service {
    private static final String TAG = DownloaderService.class.getSimpleName();

    private final IBinder mBinder = new LocalBinder();
    public static boolean isRunning = false;



    public class LocalBinder extends Binder {
        public DownloaderService getDownloaderService() {
            // Return this instance of LocalService so clients can call public methods
            return DownloaderService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        isRunning = true;
        lectureId = (int) intent.getExtras().get(DownloadActivity.LECTURE_ID);
        //TODO get it from intent, make it a queue
        dashUrl = "http://54.86.202.143:1935/vod_android/mp4:100k.mp4/manifest.mpd";
        // Let it continue running until it is stopped.
        Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
        final NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.sp_logo)
                        .setContentTitle("Downloading Video")
                        .setContentText("0 % completed");
        final NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(CourseActivity.appId, mBuilder.build());

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    LectureDownloader.downloadLecture(new URL(dashUrl), lectureId);
                } catch (MalformedURLException e) {
                    Log.e(TAG, "caught exception while setting up url", e);
                } catch (IOException e) {
                    Log.e(TAG, "caught exception while setting up url", e);
                }
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (!LectureDownloader.completed) {
                    try {
                        /**
                         * to avoid device crash
                         */
                        Thread.sleep(1000);
                    }catch (Exception e){

                    }
                    int percent = LectureDownloader.getDownloadedPercent();
                    mBuilder.setContentText(percent+"% completed");
                    mNotificationManager.notify(CourseActivity.appId, mBuilder.build());
                }
            }
        }).start();

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        isRunning = false;
        super.onDestroy();
    }

    public int gerRandom(){
        return new Random().nextInt();
    }
}
