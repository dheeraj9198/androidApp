package com.example.dheeraj.superprofs.models;

import java.util.ArrayList;

/**
 * Created by dheeraj on 18/2/15.
 */
public final class Professor {
    private int id;
    private int userId;
    private String tagline;
    private String summary;
    private float revenueSharePercentage;
    private int totalExperience;
    private int status;
    private int profileStatus;
    private int createdBy;
    private int updatedBy;
    private String createdAt;
    private String updatedAt;
    private int numCourses;

    /**
     * relational data
     */
    private User user;
    private ArrayList<ProfessorEducation> professorEducations;
    private ArrayList<ProfessorExperience> professorExperiences;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getTagline() {
        return tagline;
    }

    public void setTagline(String tagline) {
        this.tagline = tagline;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public float getRevenueSharePercentage() {
        return revenueSharePercentage;
    }

    public void setRevenueSharePercentage(float revenueSharePercentage) {
        this.revenueSharePercentage = revenueSharePercentage;
    }

    public int getTotalExperience() {
        return totalExperience;
    }

    public void setTotalExperience(int totalExperience) {
        this.totalExperience = totalExperience;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getProfileStatus() {
        return profileStatus;
    }

    public void setProfileStatus(int profileStatus) {
        this.profileStatus = profileStatus;
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

    public int getNumCourses() {
        return numCourses;
    }

    public void setNumCourses(int numCourses) {
        this.numCourses = numCourses;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ArrayList<ProfessorEducation> getProfessorEducations() {
        return professorEducations;
    }

    public void setProfessorEducations(ArrayList<ProfessorEducation> professorEducations) {
        this.professorEducations = professorEducations;
    }

    public ArrayList<ProfessorExperience> getProfessorExperiences() {
        return professorExperiences;
    }

    public void setProfessorExperiences(ArrayList<ProfessorExperience> professorExperiences) {
        this.professorExperiences = professorExperiences;
    }

    @Override
    public String toString() {
        return "Professor{" +
                "id=" + id +
                ", userId=" + userId +
                ", tagline='" + tagline + '\'' +
                ", summary='" + summary + '\'' +
                ", revenueSharePercentage=" + revenueSharePercentage +
                ", totalExperience=" + totalExperience +
                ", status=" + status +
                ", profileStatus=" + profileStatus +
                ", createdBy=" + createdBy +
                ", updatedBy=" + updatedBy +
                ", createdAt='" + createdAt + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                ", numCourses=" + numCourses +
                ", user=" + user +
                ", professorEducations=" + professorEducations +
                ", professorExperiences=" + professorExperiences +
                '}';
    }
}
