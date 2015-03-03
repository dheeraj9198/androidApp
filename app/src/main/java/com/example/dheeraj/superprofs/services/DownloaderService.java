package com.example.dheeraj.superprofs.services;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
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

public class DownloaderService extends Service {
    private static final String TAG = DownloaderService.class.getSimpleName();
    private int lectureId;
    private String dashUrl;

    private Messenger messageHandler;

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Bundle extras = intent.getExtras();
        messageHandler = (Messenger) extras.get("MESSENGER");

        lectureId = (int) intent.getExtras().get(DownloadActivity.LECTURE_ID);
        //TODO get it from intent
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
                    sendMessage(percent,lectureId);
                    mBuilder.setContentText(percent+"% completed");
                    mNotificationManager.notify(CourseActivity.appId, mBuilder.build());
                }
            }
        }).start();

        return START_STICKY;
    }

    public void sendMessage(int state,int lid) {
        Message message = Message.obtain();
        message.arg1 = state;
        message.arg2 = lid;

        try {
            messageHandler.send(message);
        }catch (RemoteException e){
            Log.e(TAG,"caught remote exception : ",e);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
