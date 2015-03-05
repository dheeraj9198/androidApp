package com.example.dheeraj.superprofs.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.dheeraj.superprofs.db.tables.CourseJson;
import com.example.dheeraj.superprofs.db.tables.LectureDownloadStatus;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

/**
 * Database helper class used to manage the creation and upgrading of our database.
 * This class also provides the DAOs used by the other classes.
 * <p/>
 * Created with IntelliJ IDEA.
 * User: lokesh
 * Date: 10/11/13
 * Time: 3:58 PM
 */

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    private static final String TAG = DatabaseHelper.class.getSimpleName();
    // name of the database file for the application
    private static final String DATABASE_NAME = "com.superprofs.db";
    // any time we make changes to your database objects, we may have to increase the database version
    private static final int DATABASE_VERSION = 1;


    private Dao<LectureDownloadStatus,Long> lectureDownloadStatusDao;
    private Dao<CourseJson,Long> courseJsonDao;
    
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * This is called when the database is first created.
     * Usually you should call createTable statements here to create
     * the tables that will store your data.
     */
    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            Log.i(TAG,"onCreate");
            TableUtils.createTableIfNotExists(connectionSource, LectureDownloadStatus.class);
            TableUtils.createTableIfNotExists(connectionSource, CourseJson.class);
        } catch (SQLException e) {
            Log.e(TAG,"Can't create database",e);
            throw new RuntimeException(e);
        }
    }

    /**
     * This is called when your application is upgraded and it has a higher version number.
     * This allows you to adjust the various data to match the new version number.
     */
    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            Log.i(TAG,"onUpgrade");
            TableUtils.dropTable(connectionSource, LectureDownloadStatus.class, true);
            // after we drop the old databases, we create the new ones
            onCreate(database, connectionSource);
        } catch (SQLException e) {
            //   Log.e(DatabaseHelper.class.getName(), "Can't drop databases", e);
            logger.error("Can't drop databases" + e);
            throw new RuntimeException(e);
        }
    }

    public Dao<LectureDownloadStatus, Long> getLectureDownloadStatusDao() throws SQLException {
        if (lectureDownloadStatusDao == null) {
            lectureDownloadStatusDao = getDao(LectureDownloadStatus.class);
        }
        return lectureDownloadStatusDao;
    }

    public Dao<CourseJson, Long> getCourseJsonDao() throws SQLException {
        if (courseJsonDao == null) {
            courseJsonDao = getDao(CourseJson.class);
        }
        return courseJsonDao;
    }

    @Override
    public void close() {
        super.close();
        lectureDownloadStatusDao = null;
        courseJsonDao = null;
    }
}
