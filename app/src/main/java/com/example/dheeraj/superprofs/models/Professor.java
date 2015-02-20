package com.example.dheeraj.superprofs.models;

import java.util.ArrayList;

/**
 * Created by dheeraj on 18/2/15.
 */
public final class Professor {
    private int id;
    private int user_id;
    private String tagline;
    private String summary;
    private float revenue_share_percentage;
    private int total_experience;
    private int status;
    private int profile_status;
    private int created_by;
    private int updated_by;
    private String created_at;
    private String updated_at;
    private int numCourses;

    /**
     * relational data
     */
    private User user;
    private ArrayList<ProfessorEducation> professorEducations;
    private ArrayList<ProfessorExperience> professorExperiences;

    public ArrayList<ProfessorExperience> getProfessorExperiences() {
        return professorExperiences;
    }

    public void setProfessorExperiences(ArrayList<ProfessorExperience> professorExperiences) {
        this.professorExperiences = professorExperiences;
    }

    public int getNumCourses() {
        return numCourses;
    }

    public void setNumCourses(int numCourses) {
        this.numCourses = numCourses;
    }



    public ArrayList<ProfessorEducation> getProfessorEducations() {
        return professorEducations;
    }

    public void setProfessorEducations(ArrayList<ProfessorEducation> professorEducations) {
        this.professorEducations = professorEducations;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
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

    public float getRevenue_share_percentage() {
        return revenue_share_percentage;
    }

    public void setRevenue_share_percentage(float revenue_share_percentage) {
        this.revenue_share_percentage = revenue_share_percentage;
    }

    public int getTotal_experience() {
        return total_experience;
    }

    public void setTotal_experience(int total_experience) {
        this.total_experience = total_experience;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getProfile_status() {
        return profile_status;
    }

    public void setProfile_status(int profile_status) {
        this.profile_status = profile_status;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    @Override
    public String toString() {
        return "Professor{" +
                "id=" + id +
                ", user_id=" + user_id +
                ", tagline='" + tagline + '\'' +
                ", summary='" + summary + '\'' +
                ", revenue_share_percentage=" + revenue_share_percentage +
                ", total_experience=" + total_experience +
                ", status=" + status +
                ", profile_status=" + profile_status +
                ", created_by=" + created_by +
                ", updated_by=" + updated_by +
                ", created_at='" + created_at + '\'' +
                ", updated_at='" + updated_at + '\'' +
                ", numCourses=" + numCourses +
                ", user=" + user +
                ", professorEducations=" + professorEducations +
                ", professorExperiences=" + professorExperiences +
                '}';
    }


}
