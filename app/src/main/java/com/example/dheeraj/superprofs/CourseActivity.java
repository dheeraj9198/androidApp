package com.example.dheeraj.superprofs;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.dheeraj.superprofs.db.DatabaseHelper;
import com.example.dheeraj.superprofs.db.DbHandler;
import com.example.dheeraj.superprofs.fragments.courseActivity.SpinnerFragment;
import com.example.dheeraj.superprofs.models.Course;
import com.example.dheeraj.superprofs.services.DownloaderService;
import com.example.dheeraj.superprofs.utils.JsonHandler;

public class CourseActivity extends ActionBarActivity {
    private static final String TAG = CourseActivity.class.getSimpleName();

    public static final int appId = 32123;

    private boolean backPressedOnce;

    public static Course course = null;
    /**
     * to save it to db for offline content
     */
    public static String courseJsonStringOF = null;


    // service methods

    private boolean keepRunning = false;

    public static final String LECTURE_ID = "lectureId";
    public DownloaderService downloaderService;
    public boolean bound = false;

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
    protected void onResume() {
        backPressedOnce = false;
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //final NotificationManager mNotificationManager =
        //    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        //mNotificationManager.cancel(1);
        //stopService(new Intent(getBaseContext(), DownloaderService.class));
    }

    @Override
    public void onBackPressed() {
        if (!backPressedOnce) {
            Toast.makeText(this, "Press back button once more to exit", Toast.LENGTH_SHORT).show();
            backPressedOnce = true;
        } else {
            DbHandler.stop();
            super.onBackPressed();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        backPressedOnce = false;

        //TODO shift this to starting activity
        DbHandler.stop();
        if (!DbHandler.isStarted()) {
            DbHandler.start(getApplicationContext());
        }


        //TODO shift this to starting activity also
        // start downloader service if there is any pending download
        Intent intent = new Intent(CourseActivity.this, DownloaderService.class);
        if (DbHandler.getDbHandler().getAllPendingLectureDownloadStatuses() != null && DbHandler.getDbHandler().getAllPendingLectureDownloadStatuses().size() > 0 && !DownloaderService.isRunning) {
            bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
            startService(intent);
        }
        // bind if download service is already running
        else if (DownloaderService.isRunning) {
            bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        }

        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new SpinnerFragment())
                    .commit();
        }
    }

    public void startAndBindToDownloadService() {
        Intent intent = new Intent(CourseActivity.this, DownloaderService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        if (!DownloaderService.isRunning) {
            startService(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
        if (id == R.id.action_download && course != null) {
            Intent intent = new Intent(getApplicationContext(), DownloadActivity.class);
            intent.putExtra("lectures", JsonHandler.stringify(course.getSections()));
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

 /*   @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Check which request we're responding to
        if (requestCode == 1) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, new SpinnerFragment())
                        .commit();
            }
        }
    }*/

}
