package com.example.dheeraj.superprofs.models;

/**
 * Created by dheeraj on 18/2/15.
 */
public final class CourseReview {
    private int id;
    private int userId;
    private int courseId;
    private String tile;
    private String review;
    private float rating;
    private int isApproved;
    private String createdAt;
    private String updatedAt;
    /**
     * realational data
     */
    private User user;

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

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public String getTile() {
        return tile;
    }

    public void setTile(String tile) {
        this.tile = tile;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public int getIsApproved() {
        return isApproved;
    }

    public void setIsApproved(int isApproved) {
        this.isApproved = isApproved;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "CourseReview{" +
                "id=" + id +
                ", userId=" + userId +
                ", courseId=" + courseId +
                ", tile='" + tile + '\'' +
                ", review='" + review + '\'' +
                ", rating=" + rating +
                ", isApproved=" + isApproved +
                ", createdAt='" + createdAt + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                ", user=" + user +
                '}';
    }
}
