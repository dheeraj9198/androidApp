package com.example.dheeraj.superprofs.db;

import android.content.Context;
import android.util.Log;

import com.example.dheeraj.superprofs.db.tables.CourseJson;
import com.example.dheeraj.superprofs.db.tables.LectureDownloadStatus;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.stmt.QueryBuilder;

import java.security.cert.LDAPCertStoreParameters;
import java.sql.SQLException;
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
                        DatabaseHelper databaseHelper1 = OpenHelperManager.getHelper(context, DatabaseHelper.class);
                        dbHandler = new DbHandler(databaseHelper1, context);

                        /**
                         * do one time tasks here
                         */
                        List<LectureDownloadStatus> lectureDownloadStatuses = dbHandler.getAllRunningLectureDownloadStatuses();
                        for (LectureDownloadStatus lectureDownloadStatus : lectureDownloadStatuses) {
                            lectureDownloadStatus.setStatus(LectureDownloadStatus.STATUS_PENDING);
                            dbHandler.saveLectureDownloadStatus(lectureDownloadStatus);
                        }
                    }
                }
            }
        }
    }

    //TODO attach to first activity's stop
    public static void stop() {
        if (dbHandler != null) {
            dbHandler.context = null;
            if (dbHandler.databaseHelper != null) {
                OpenHelperManager.releaseHelper();
                dbHandler.databaseHelper = null;
            }
        }
    }

    public static boolean isStarted() {
        return dbHandler != null;
    }

    public static DbHandler getDbHandler() {
        if (dbHandler != null) {
            return dbHandler;
        }
        throw new RuntimeException("null db handler, start method not called yet");
    }

    public void deleteLectureDownloadStatus(int id) {
        try {
            databaseHelper.getLectureDownloadStatusDao().deleteById((long) id);
        } catch (Exception e) {
            Log.e(TAG, "Failed to delete lecture download status by id : " + id, e);
        }
    }

    public LectureDownloadStatus getLectureDownloadStatusById(int id) {
        try {
            return databaseHelper.getLectureDownloadStatusDao().queryForId((long) id);
        } catch (Exception e) {
            Log.e(TAG, "Failed to get lecture download status id : " + id, e);
            return null;
        }
    }

    public void saveStatusById(int id,int status){
        try {
            LectureDownloadStatus lectureDownloadStatus =  databaseHelper.getLectureDownloadStatusDao().queryForId((long) id);
            if(lectureDownloadStatus != null){
                lectureDownloadStatus.setStatus(status);
                saveLectureDownloadStatus(lectureDownloadStatus);
            }
        } catch (Exception e) {
            Log.e(TAG, "Failed to save lecture download status id : " + id, e);
        }
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


    public List<LectureDownloadStatus> getAllPendingLectureDownloadStatuses() {
        try {
            return databaseHelper.getLectureDownloadStatusDao().queryForEq(LectureDownloadStatus.FIELD_STATE, LectureDownloadStatus.STATUS_PENDING);
        } catch (SQLException e) {
            return null;
        }
    }

    public List<LectureDownloadStatus> getAllRunningLectureDownloadStatuses() {
        try {
            return databaseHelper.getLectureDownloadStatusDao().queryForEq(LectureDownloadStatus.FIELD_STATE, LectureDownloadStatus.STATUS_RUNNING);
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
