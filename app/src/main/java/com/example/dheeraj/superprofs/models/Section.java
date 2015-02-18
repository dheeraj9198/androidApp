package com.example.dheeraj.superprofs.models;

import java.util.ArrayList;

/**
 * Created by dheeraj on 18/2/15.
 */
public final class Section {
    private int id;
    private String name;
    private int course_id;
    private String description;
    private int section_number;
    private int lecture_count;
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

    public int getCourse_id() {
        return course_id;
    }

    public void setCourse_id(int course_id) {
        this.course_id = course_id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getSection_number() {
        return section_number;
    }

    public void setSection_number(int section_number) {
        this.section_number = section_number;
    }

    public int getLecture_count() {
        return lecture_count;
    }

    public void setLecture_count(int lecture_count) {
        this.lecture_count = lecture_count;
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
                ", course_id=" + course_id +
                ", description='" + description + '\'' +
                ", section_number=" + section_number +
                ", lecture_count=" + lecture_count +
                ", duration=" + duration +
                ", lectures=" + lectures +
                '}';
    }
}
