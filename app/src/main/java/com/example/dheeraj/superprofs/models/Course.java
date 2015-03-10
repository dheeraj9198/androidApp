package com.example.dheeraj.superprofs.models;

import android.graphics.Bitmap;
import com.example.dheeraj.superprofs.utils.ImageUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by dheeraj on 18/2/15.
 */
@JsonIgnoreProperties({"bitMap"})
public final class Course {
    private static final String TAG = Course.class.getSimpleName();

    private int id;
    private String name;
    private String description;
    private String imageUrl;
    private int status;
    private int priority;
    private int subjectId;
    private int broadcastType;
    private int ownedByProfessor;
    private String publishedAt;
    private int approvalStatus;
    private int isDiscussionEnabled;
    private String createdAt;
    private String updatedAt;
    private int createdBy;
    private int updatedBy;

    /**
     * relational data
     */
    private Professor professor;
    private ArrayList<Language> languages;
    private ArrayList<Section> sections;
    private ArrayList<Attachment> attachments;
    private ArrayList<CourseMeta> courseMetas;
    private ArrayList<Course> similarCourses;
    private ArrayList<CourseReview> courseReviews;

    public ArrayList<CourseReview> getCourseReviews() {
        return courseReviews;
    }

    public void setCourseReviews(ArrayList<CourseReview> courseReviews) {
        this.courseReviews = courseReviews;
    }

    /**
     * from access-log/mongo-db
     */
    private ArrayList<User> students;

    /**
     * image bitmap
     */
    @JsonIgnore
    private Bitmap bitmap;

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void downloadBitmap() {
        this.bitmap = ImageUtils.getBitmapFromUrl(imageUrl);
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
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

    public int getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }

    public int getBroadcastType() {
        return broadcastType;
    }

    public void setBroadcastType(int broadcastType) {
        this.broadcastType = broadcastType;
    }

    public int getOwnedByProfessor() {
        return ownedByProfessor;
    }

    public void setOwnedByProfessor(int ownedByProfessor) {
        this.ownedByProfessor = ownedByProfessor;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    public int getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(int approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public int getIsDiscussionEnabled() {
        return isDiscussionEnabled;
    }

    public void setIsDiscussionEnabled(int isDiscussionEnabled) {
        this.isDiscussionEnabled = isDiscussionEnabled;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public int getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }

    public int getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(int updatedBy) {
        this.updatedBy = updatedBy;
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

    public void setLanguages(ArrayList<Language> languages) {
        this.languages = languages;
    }

    public ArrayList<Section> getSections() {
        return sections;
    }

    public void setSections(ArrayList<Section> sections) {
        this.sections = sections;
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

    public ArrayList<Course> getSimilarCourses() {
        return similarCourses;
    }

    public void setSimilarCourses(ArrayList<Course> similarCourses) {
        this.similarCourses = similarCourses;
    }

    public ArrayList<User> getStudents() {
        return students;
    }

    public void setStudents(ArrayList<User> students) {
        this.students = students;
    }

    @Override
    public String toString() {
        return "Course{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", status=" + status +
                ", priority=" + priority +
                ", subjectId=" + subjectId +
                ", broadcastType=" + broadcastType +
                ", ownedByProfessor=" + ownedByProfessor +
                ", publishedAt='" + publishedAt + '\'' +
                ", approvalStatus=" + approvalStatus +
                ", isDiscussionEnabled=" + isDiscussionEnabled +
                ", createdAt='" + createdAt + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                ", createdBy=" + createdBy +
                ", updatedBy=" + updatedBy +
                ", professor=" + professor +
                ", languages=" + languages +
                ", sections=" + sections +
                ", attachments=" + attachments +
                ", courseMetas=" + courseMetas +
                ", similarCourses=" + similarCourses +
                ", students=" + students +
                '}';
    }
}
