package com.example.dheeraj.superprofs.db;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.TrafficStats;
import android.util.Log;

import com.example.dheeraj.superprofs.db.dao.current.LectureDownloadStatus;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.stmt.QueryBuilder;
import com.squareup.otto.Subscribe;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: dheeraj
 * Date: 18/10/13
 * Time: 3:48 PM
 */

//make it a singleton
public class DbHandler {

    private static final String TAG = DbHandler.class.getSimpleName();

    private static DbHandler dbHandler;

    private DatabaseHelper databaseHelper = null;
    private Context context;

    private DbHandler(DatabaseHelper databaseHelper, Context context) {
        this.databaseHelper = databaseHelper;
        this.context = context;
    }


    //TODO attach to first activity's start
    public static void start(Context context) {
        if (dbHandler == null) {
            synchronized (DbHandler.class) {
                if (dbHandler == null) {
                    synchronized (DbHandler.class) {
                        dbHandler = new DbHandler(OpenHelperManager.getHelper(context, DatabaseHelper.class), context);
                    }
                }
            }
        }
    }

    //TODO attach to first activity's stop
    public static void stop() {
        dbHandler.context = null;
        if (dbHandler.databaseHelper != null) {
            OpenHelperManager.releaseHelper();
            dbHandler.databaseHelper = null;
        }
    }

    public boolean isStarted() {
        return context != null;
    }

    public static DatabaseHelper getDatabaseHelper() {
        if (dbHandler != null) {
            return dbHandler.databaseHelper;
        }
        throw new RuntimeException("null db handler, start method not called yet");
    }


/*    public List<LectureDownloadStatus> getLectureDownloadStatusList() {
        try {
            QueryBuilder<LectureDownloadStatus, Long> queryBuilder = getHelper().getLectureDownloadStatusDao().queryBuilder();
            queryBuilder.where().not().eq("status", TranscodeStatus.PENDING).and().eq("postedOnApi", false);
            return queryBuilder.query();

        } catch (SQLException e) {
            return null;
        }
    }

    public LectureDownloadStatus getLectureDownloadStatus(String inputFile) {
        try {
            return getHelper().getTranscodeStatusDao().queryForEq("inputFile", inputFile).get(0);
        } catch (Exception e) {
            //  Log.d(TAG, "Failed to load transcodeStatus from db for inputFile=" + inputFile);
            logger.debug("Failed to load transcodeStatus from db for inputFile=" + inputFile);
            return null;
        }
    }

    public TranscodeStatus getTranscodeStatusForSession(String sessionId) {
        try {
            return getHelper().getTranscodeStatusDao().queryForEq("sessionId", sessionId).get(0);
        } catch (Exception e) {
            //Log.d(TAG, "Failed to load transcodeStatus from db for sessionId=" + sessionId);
            ;
            logger.error("Failed to load transcodeStatus from db for sessionId=" + sessionId);
            return null;
        }
    }

    public boolean saveTranscodeStatus(TranscodeStatus transcodeStatus) {
        try {
            return getHelper().getTranscodeStatusDao().createOrUpdate(transcodeStatus).getNumLinesChanged() == 1;
        } catch (SQLException e) {
            // Log.e(TAG, "Failed to save transcodeStatus data to db", e);
            logger.error("Failed to save transcodeStatus data to db" + e);
            return false;
        }
    }

    public void deleteSchedule() {
        try {
            DeleteBuilder<ScheduleTrigger, String> queryBuilder = getHelper().getScheduleTriggerDao().deleteBuilder();
            queryBuilder.delete();

        } catch (SQLException e) {
            //Log.w(TAG, "Failed to delete schedule from db");
            logger.error("Failed to delete schedule from db");
        }
    }*/
}
