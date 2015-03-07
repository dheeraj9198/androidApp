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
    int courseId;
    
    @DatabaseField(canBeNull = false)
    String jsonData;

    public CourseJson(){
    }
    
    public CourseJson(int courseId, String jsonData) {
        this.courseId = courseId;
        this.jsonData = jsonData;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public String getJsonData() {
        return jsonData;
    }

    public void setJsonData(String jsonData) {
        this.jsonData = jsonData;
    }
}
