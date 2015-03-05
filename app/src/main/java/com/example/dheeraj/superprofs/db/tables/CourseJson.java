package com.example.dheeraj.superprofs.db.tables;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by dheeraj on 5/3/15.
 */
@DatabaseTable(tableName = "CourseJson")
public class CourseJson {
    
    public static final String FIELD_COURSE_ID = "courseId";
    
    @DatabaseField(id = true,index = true)
    long courseId;
    
    @DatabaseField(canBeNull = false)
    String jsonData;

    public CourseJson(){
    }
    
    public CourseJson(long courseId, String jsonData) {
        this.courseId = courseId;
        this.jsonData = jsonData;
    }

    public long getCourseId() {
        return courseId;
    }

    public void setCourseId(long courseId) {
        this.courseId = courseId;
    }

    public String getJsonData() {
        return jsonData;
    }

    public void setJsonData(String jsonData) {
        this.jsonData = jsonData;
    }
}
