package com.example.dheeraj.superprofs.services;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.example.dheeraj.superprofs.R;

public class DownloaderService extends Service {
    private boolean threadRun;
    
    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Let it continue running until it is stopped.
        Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
        threadRun = true;
        final NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.play_button)
                        .setContentTitle("My notification")
                        .setContentText("Hello World!");
        final NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        new Thread(new Runnable() {
            @Override
            public void run() {
                int k = 1;
                while(threadRun) {
                    try{
                        Thread.sleep(100);
                    }catch (Exception e){

                    }
                    if(k > 50000){
                        threadRun = false;
                        stopSelf();
                    }
                    mBuilder.setContentText(++k + "");
                    mNotificationManager.notify(1, mBuilder.build());
                }

            }
        }).start();
        return START_STICKY;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        threadRun = false;
        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
    }
}
