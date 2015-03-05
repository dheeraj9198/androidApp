package com.example.dheeraj.superprofs;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.PorterDuff;
import android.os.IBinder;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dheeraj.superprofs.db.DbHandler;
import com.example.dheeraj.superprofs.db.tables.LectureDownloadStatus;
import com.example.dheeraj.superprofs.models.Lecture;
import com.example.dheeraj.superprofs.models.Section;
import com.example.dheeraj.superprofs.services.DownloaderService;
import com.example.dheeraj.superprofs.utils.JsonHandler;

public class DownloadActivity extends ActionBarActivity {
    public static final String TAG = DownloadActivity.class.getSimpleName();

    private boolean keepRunning = false;

    public static final String LECTURE_ID = "lectureId";
    private DownloaderService downloaderService;
    private boolean bound = false;

    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            DownloaderService.LocalBinder binder = (DownloaderService.LocalBinder) service;
            downloaderService = binder.getDownloaderService();
            bound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            bound = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (DownloaderService.isRunning) {
            Intent intent = new Intent(DownloadActivity.this, DownloaderService.class);
            bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        }

        keepRunning = !keepRunning;
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (keepRunning) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (bound && DownloaderService.isRunning && DownloaderService.currentLectureDownloader != null) {
                                DownloaderService.DownloadStats downloadStats = downloaderService.getDownloadStats();
                                LinearLayout linearLayout = (LinearLayout) findViewById(R.id.section_lectures);
                                View view = linearLayout.findViewById( downloadStats.getLectureId());
                                ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.download_progress_bar);
                                progressBar.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.orange), PorterDuff.Mode.SRC_IN);
                                progressBar.getProgressDrawable().setColorFilter(getResources().getColor(R.color.orange), PorterDuff.Mode.SRC_IN);
                                progressBar.setProgress(downloadStats.getPercent());
                            }
                        }
                    });
                    try {
                        Thread.sleep(1000);
                    } catch (Exception e) {

                    }
                }
            }
        }).start();

        setContentView(R.layout.activity_download);
        View.OnClickListener lectureOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Lecture lecture = (Lecture) v.getTag();
                if (lecture != null) {
                    Toast.makeText(DownloadActivity.this,"Added lecture in download queue "+lecture.getId(),Toast.LENGTH_SHORT).show();
                    //make db entry
                    DbHandler.getDbHandler().saveLectureDownloadStatus(new LectureDownloadStatus(lecture.getId(),
                            CourseActivity.course.getId(),
                    /*TODO dash url*/"http://frontend.test.superprofs.com:1935/vod_android/mp4:sp_high_4.mp4/manifest.mpd",
                            LectureDownloadStatus.STATUS_PENDING,
                            0, false
                    ));

                    if (!bound) {
                        Intent intent = new Intent(DownloadActivity.this, DownloaderService.class);
                        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
                        if (!DownloaderService.isRunning) {
                            startService(intent);
                        }
                    }
                }else{
                    Toast.makeText(DownloadActivity.this,"no lecture found",Toast.LENGTH_SHORT).show();
                    Log.e(TAG,"no lecture found int the tag");
                }
            }
        };

        String sectionString = (String) getIntent().getExtras().get("lectures");
        Section[] sections = JsonHandler.parse(sectionString, Section[].class);

        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.section_lectures);
        for (Section section : sections) {
            View sectionView = getLayoutInflater().inflate(R.layout.list_item_section_head, null);
            TextView textView = (TextView) sectionView.findViewById(R.id.section_title);
            textView.setText(section.getDescription());
            linearLayout.addView(sectionView);
            for (Lecture lecture : section.getLectures()) {
                View lectureView = getLayoutInflater().inflate(R.layout.list_item_download_lecture, null);
                lectureView.setId(lecture.getId());
                TextView textViewLecture = (TextView) lectureView.findViewById(R.id.title);
                textViewLecture.setText(lecture.getName());
                if (lecture.getId() % 2 == 0) {
                    ImageView imageView = (ImageView) lectureView.findViewById(R.id.icon);
                    imageView.setImageDrawable(getResources().getDrawable(R.drawable.download_complete));
                }
                lectureView.setTag(lecture);
                lectureView.setOnClickListener(lectureOnClickListener);
                linearLayout.addView(lectureView);
            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_download, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop() {
        keepRunning =
                !keepRunning;
        super.onStop();
        if (bound) {
            unbindService(mConnection);
            bound = false;
        }
    }

    @Override
    protected void onStart() {
        super.onStop();
    }

}
