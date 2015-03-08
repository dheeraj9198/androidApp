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
import com.example.dheeraj.superprofs.R;
import com.example.dheeraj.superprofs.db.DbHandler;
import com.example.dheeraj.superprofs.db.tables.LectureDownloadStatus;
import com.example.dheeraj.superprofs.downloader.LectureDownloader;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

    public static LectureDownloader currentLectureDownloader = null;


    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, final int startId) {
        isRunning = true;

        //directly read pending data
        ExecutorService downloderExecutorService = Executors.newSingleThreadExecutor();
        downloderExecutorService.execute(new Runnable() {
            @Override
            public void run() {
                while (isRunning) {
                    try {
                        currentLectureDownloader = null;
                        LectureDownloadStatus lectureDownloadStatus = DbHandler.getDbHandler().getOnePendingLectureDownloadStatus();
                        if (lectureDownloadStatus == null) {
                            isRunning = false;
                            stopSelf();
                            break;
                        }
                        lectureDownloadStatus.setStatus(LectureDownloadStatus.STATUS_RUNNING);
                        if (!DbHandler.getDbHandler().saveLectureDownloadStatus(lectureDownloadStatus)) {
                            Log.e(TAG, "unable to save lecture download status");
                        }
                        LectureDownloader lectureDownloader = new LectureDownloader(/* TODO lectureDownloadStatus.getDashUrl()*/
                                "http://frontend.test.superprofs.com:1935/vod_android/mp4:sp_high_4.mp4/manifest.mpd",
                                lectureDownloadStatus.getLectureId());
                        currentLectureDownloader = lectureDownloader;
                        int status = lectureDownloader.downloadLecture();
                        switch (status) {
                            case LectureDownloader.ERROR:
                                lectureDownloadStatus.setStatus(LectureDownloadStatus.STATUS_ERROR);
                                break;
                            case LectureDownloader.INTERRUPTED:
                                lectureDownloadStatus.setStatus(LectureDownloadStatus.STATUS_PENDING);
                                lectureDownloadStatus.setPercentCompleted(lectureDownloader.getDownloadedPercent());
                                break;
                            case LectureDownloader.PAUSED:
                                lectureDownloadStatus.setStatus(LectureDownloadStatus.STATUS_PAUSED);
                                lectureDownloadStatus.setPercentCompleted(lectureDownloader.getDownloadedPercent());
                                break;
                            case LectureDownloader.COMPLETED:
                                lectureDownloadStatus.setStatus(LectureDownloadStatus.STATUS_FINISHED);
                                lectureDownloadStatus.setCompleted(true);
                                lectureDownloadStatus.setPercentCompleted(100);
                                break;
                        }
                        if (!DbHandler.getDbHandler().saveLectureDownloadStatus(lectureDownloadStatus)) {
                            Log.e(TAG, "unable to save lecture download status");
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "caught exception in downloaderExecutorService", e);
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (Exception e) {

                    }
                }
            }
        });
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
                while (true) {
                    if (!DownloaderService.isRunning) {
                        break;
                    }
                    try {
                        /**
                         * to avoid device crash
                         */
                        Thread.sleep(1000);
                    } catch (Exception e) {

                    }
                    if (currentLectureDownloader != null) {
                        mBuilder.setContentText(currentLectureDownloader.getDownloadedPercent() + "% completed");
                        mNotificationManager.notify(CourseActivity.appId, mBuilder.build());
                    }
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

    public DownloadStats getDownloadStats() {
        return new DownloadStats(currentLectureDownloader.getDownloadedPercent(), currentLectureDownloader.getLectureId());
    }

    public static class DownloadStats {
        int percent;
        int lectureId;

        public DownloadStats(int percent, int lectureId) {
            this.percent = percent;
            this.lectureId = lectureId;
        }

        public int getPercent() {
            return percent;
        }

        public int getLectureId() {
            return lectureId;
        }
    }
}
