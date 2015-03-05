package com.example.dheeraj.superprofs.db.tables;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by dheeraj on 3/4/2015.
 */
@DatabaseTable(tableName = "LectureDownloadStatus")
public class LectureDownloadStatus {

    /**
     * fields
     */
    public static final String FIELD_LECTURE_Id = "lectureId";
    public static final String FIELD_STATE = "status";

    public static final int STATUS_PENDING =0;
    public static final int STATUS_RUNNING =1;
    public static final int STATUS_FINISHED =2;
    public static final int STATUS_ERROR =3;


    /**
     * primary key
     */
    @DatabaseField(id = true,index = true)
    int lectureId;
    
    @DatabaseField(canBeNull = false)
    int courseId;

    @DatabaseField(canBeNull =  false)
    String dashUrl;

    @DatabaseField(index = true)
    int status = STATUS_PENDING;

    @DatabaseField()
    int percentCompleted = 0;

    @DatabaseField()
    boolean completed = false;

    
    public LectureDownloadStatus(){
    }
    
    public LectureDownloadStatus(int lectureId, int courseId, String dashUrl, int status, int percentCompleted, boolean completed) {
        this.lectureId = lectureId;
        this.courseId = courseId;
        this.dashUrl = dashUrl;
        this.status = status;
        this.percentCompleted = percentCompleted;
        this.completed = completed;
    }

    public long getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

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

    public int getLectureId() {
        return lectureId;
    }

    public void setLectureId(int lectureId) {
        this.lectureId = lectureId;
    }

}
