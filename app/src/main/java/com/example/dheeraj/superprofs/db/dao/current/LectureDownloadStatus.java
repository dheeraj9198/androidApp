package com.example.dheeraj.superprofs.db.dao.current;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by windows 7 on 3/4/2015.
 */
@DatabaseTable(tableName = "LectureDownloadStatus")
public class LectureDownloadStatus {

    /**
     * fields
     */
    public static final String FIELD_LECTURE_Id = "lectureId";
    public static final String FIELD_STATE = "status";

    public static final int STATUS_PENDING =0;
    public static final int STATUS_RUNNING =0;
    public static final int STATUS_FINISHED =0;
    public static final int STATUS_ERROR =0;



    @DatabaseField(generatedId = true)
    long id;

    @DatabaseField(canBeNull = false,index = true,unique = true)
    long lectureId;

    @DatabaseField(canBeNull =  false)
    String dashUrl;

    @DatabaseField()
    int status = STATUS_PENDING;

    @DatabaseField()
    int percentCompleted = 0;

    @DatabaseField()
    boolean completed = false;

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public int getPercentCompleted() {
        return percentCompleted;
    }

    public void setPercentCompleted(int percentCompleted) {
        this.percentCompleted = percentCompleted;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDashUrl() {
        return dashUrl;
    }

    public void setDashUrl(String dashUrl) {
        this.dashUrl = dashUrl;
    }

    public long getLectureId() {
        return lectureId;
    }

    public void setLectureId(int lectureId) {
        this.lectureId = lectureId;
    }

    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
