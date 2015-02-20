package com.example.dheeraj.superprofs.models;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.example.dheeraj.superprofs.utils.ImageUtils;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by dheeraj on 18/2/15.
 */
public final class Course {
    private static final String TAG = Course.class.getSimpleName();

    private int id;
    private String name;
    private String description;
    private String image_url;
    private int status;
    private int priority;
    private int subject_id;
    private int broadcast_type;
    private int owned_by_professor;
    private String published_at;
    private int approval_status;
    private int is_discussion_enabled;
    private String created_at;
    private String updated_at;
    private int created_by;
    private int updated_by;

    /**
     * relational data
     */
    private Professor professor;
    private ArrayList<Language> languages;
    private ArrayList<Section> sections;
    private ArrayList<Attachment> attachments;
    private ArrayList<CourseMeta> courseMetas;

    /**
     * from access-log/mongo-db
     */
    private ArrayList<User> students;

    /**
     * image bitmap
     */
    private Bitmap bitmap;

    public Bitmap getImageBitmap()
    {
        return bitmap;
    }

    @Override
    public String toString() {
        return "Course{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", image_url='" + image_url + '\'' +
                ", status=" + status +
                ", priority=" + priority +
                ", subject_id=" + subject_id +
                ", broadcast_type=" + broadcast_type +
                ", owned_by_professor=" + owned_by_professor +
                ", published_at='" + published_at + '\'' +
                ", approval_status=" + approval_status +
                ", is_discussion_enabled=" + is_discussion_enabled +
                ", created_at='" + created_at + '\'' +
                ", updated_at='" + updated_at + '\'' +
                ", created_by=" + created_by +
                ", updated_by=" + updated_by +
                ", professor=" + professor +
                ", languages=" + languages +
                ", sections=" + sections +
                ", attachments=" + attachments +
                ", courseMetas=" + courseMetas +
                ", students=" + students +
                ", bitmap=" + bitmap +
                ", courseReviews=" + courseReviews +
                '}';
    }

    public ArrayList<User> getStudents() {
        return students;
    }

    public void setStudents(ArrayList<User> students) {
        this.students = students;
    }

    public ArrayList<Attachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(ArrayList<Attachment> attachments) {
        this.attachments = attachments;
    }

    public ArrayList<CourseMeta> getCourseMetas() {
        return courseMetas;
    }

    public void setCourseMetas(ArrayList<CourseMeta> courseMetas) {
        this.courseMetas = courseMetas;
    }

    private ArrayList<CourseReview> courseReviews;


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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = ImageUtils.getBitmapFromUrl(image_url);
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getSubject_id() {
        return subject_id;
    }

    public void setSubject_id(int subject_id) {
        this.subject_id = subject_id;
    }

    public int getBroadcast_type() {
        return broadcast_type;
    }

    public void setBroadcast_type(int broadcast_type) {
        this.broadcast_type = broadcast_type;
    }

    public int getOwned_by_professor() {
        return owned_by_professor;
    }

    public void setOwned_by_professor(int owned_by_professor) {
        this.owned_by_professor = owned_by_professor;
    }

    public String getPublished_at() {
        return published_at;
    }

    public void setPublished_at(String published_at) {
        this.published_at = published_at;
    }

    public int getApproval_status() {
        return approval_status;
    }

    public void setApproval_status(int approval_status) {
        this.approval_status = approval_status;
    }

    public int getIs_discussion_enabled() {
        return is_discussion_enabled;
    }

    public void setIs_discussion_enabled(int is_discussion_enabled) {
        this.is_discussion_enabled = is_discussion_enabled;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public int getCreated_by() {
        return created_by;
    }

    public void setCreated_by(int created_by) {
        this.created_by = created_by;
    }

    public int getUpdated_by() {
        return updated_by;
    }

    public void setUpdated_by(int updated_by) {
        this.updated_by = updated_by;
    }

    public Professor getProfessor() {
        return professor;
    }

    public void setProfessor(Professor professor) {
        this.professor = professor;
    }

    public ArrayList<Language> getLanguages() {
        return languages;
    }

    public String getAllLanguages(){
        StringBuilder stringBuilder = new StringBuilder();
        Iterator<Language> languageIterator = getLanguages().iterator();
        while(languageIterator.hasNext()){
            stringBuilder.append(languageIterator.next().getName());
            if(languageIterator.hasNext()) {
                stringBuilder.append(", ");
            }
        }
        return stringBuilder.toString();
    }

    public void setLanguages(ArrayList<Language> languages) {
        this.languages = languages;
    }

    public ArrayList<Section> getSections() {
        return sections;
    }

    public void setSections(ArrayList<Section> sections) {
        this.sections = sections;
    }

    public ArrayList<CourseReview> getCourseReviews() {
        return courseReviews;
    }

    public void setCourseReviews(ArrayList<CourseReview> courseReviews) {
        this.courseReviews = courseReviews;
    }

}
