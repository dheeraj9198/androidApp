package com.example.dheeraj.superprofs.models;

import java.util.ArrayList;

/**
 * Created by dheeraj on 18/2/15.
 */
public final class Section {
    private int id;
    private String name;
    private int courseId;
    private String description;
    private int sectionNumber;
    private int lectureCount;
    private int duration;

    /**
     * realational data
     */

    private ArrayList<Lecture> lectures;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getSectionNumber() {
        return sectionNumber;
    }

    public void setSectionNumber(int sectionNumber) {
        this.sectionNumber = sectionNumber;
    }

    public int getLectureCount() {
        return lectureCount;
    }


    public void setLectureCount(int lectureCount) {
        this.lectureCount = lectureCount;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public ArrayList<Lecture> getLectures() {
        return lectures;
    }

    public void setLectures(ArrayList<Lecture> lectures) {
        this.lectures = lectures;
    }

    @Override
    public String toString() {
        return "Section{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", courseId=" + courseId +
                ", description='" + description + '\'' +
                ", sectionNumber=" + sectionNumber +
                ", lectureCount=" + lectureCount +
                ", duration=" + duration +
                ", lectures=" + lectures +
                '}';
    }
}
