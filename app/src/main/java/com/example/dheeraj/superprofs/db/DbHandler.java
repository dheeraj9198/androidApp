package com.example.dheeraj.superprofs.db;

import android.content.Context;
import android.util.Log;

import com.example.dheeraj.superprofs.db.tables.CourseJson;
import com.example.dheeraj.superprofs.db.tables.LectureDownloadStatus;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;

/**
 * Created with IntelliJ IDEA.
 * User: dheeraj
 * Date: 18/10/13
 * Time: 3:48 PM
 */

//make it a singleton
public class DbHandler {

    private static final String TAG = DbHandler.class.getSimpleName();

    private static final Long LIMIT_ONE = 1L;

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

    public static DbHandler getDbHandler() {
        if (dbHandler != null) {
            return dbHandler;
        }
        throw new RuntimeException("null db handler, start method not called yet");
    }

    public LectureDownloadStatus getOnePendingLectureDownloadStatus() {
        try {
            return databaseHelper.getLectureDownloadStatusDao().queryForFirst(
                    databaseHelper.getLectureDownloadStatusDao().queryBuilder().where()
                            .eq(LectureDownloadStatus.FIELD_STATE, LectureDownloadStatus.STATUS_PENDING)
                            .prepare());
        } catch (SQLException e) {
            return null;
        }

    }

    public boolean saveLectureDownloadStatus(LectureDownloadStatus lectureDownloadStatus) {
        try {
            return databaseHelper.getLectureDownloadStatusDao().createOrUpdate(lectureDownloadStatus).getNumLinesChanged() == 1;
        } catch (SQLException e) {
            return false;
        }

    }
    
    public boolean saveCourseJson(CourseJson courseJson) {
        try {
            return databaseHelper.getCourseJsonDao().createOrUpdate(courseJson).getNumLinesChanged() == 1;
        } catch (SQLException e) {
            return false;
        }

    }

    public CourseJson getCourseJson(long courseId) {
        try {
            return databaseHelper.getCourseJsonDao().queryForId(courseId);
        } catch (Exception e) {
            Log.e(TAG, "Failed to get course json for course id : " + courseId, e);
            return null;
        }
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
