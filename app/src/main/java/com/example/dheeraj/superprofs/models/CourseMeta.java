package com.example.dheeraj.superprofs.models;

/**
 * Created by dheeraj on 18/2/15.
 */
public final class CourseMeta {

    private static final String minuteTag = " Minutes";
    private static final String hoursTag = " Hours";

    private int id;
    private int courseId;
    private String newTill;
    private int totalDuration;
    private int availableContentDuration;
    private float cumulativeRating;
    private int lectureCount;
    private int reviewCount;
    private int likesCount;
    private int dislikesCount;

    public int getId() {
        return id;
    }

    public String getDurationString() {
        int x = totalDuration;
        x = x / 60;
        if (x < 60) {
            return x + minuteTag;
        }
        x = x / 60;
        return x + hoursTag;
    }

    public static String getMinuteTag() {
        return minuteTag;
    }

    public static String getHoursTag() {
        return hoursTag;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public String getNewTill() {
        return newTill;
    }

    public void setNewTill(String newTill) {
        this.newTill = newTill;
    }

    public int getTotalDuration() {
        return totalDuration;
    }

    public void setTotalDuration(int totalDuration) {
        this.totalDuration = totalDuration;
    }

    public int getAvailableContentDuration() {
        return availableContentDuration;
    }

    public void setAvailableContentDuration(int availableContentDuration) {
        this.availableContentDuration = availableContentDuration;
    }

    public float getCumulativeRating() {
        return cumulativeRating;
    }

    public void setCumulativeRating(float cumulativeRating) {
        this.cumulativeRating = cumulativeRating;
    }

    public int getLectureCount() {
        return lectureCount;
    }

    public void setLectureCount(int lectureCount) {
        this.lectureCount = lectureCount;
    }

    public int getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(int reviewCount) {
        this.reviewCount = reviewCount;
    }

    public int getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(int likesCount) {
        this.likesCount = likesCount;
    }

    public int getDislikesCount() {
        return dislikesCount;
    }

    public void setDislikesCount(int dislikesCount) {
        this.dislikesCount = dislikesCount;
    }

    public String getCumulativeRatingString(){
        return String.format("%.1f", cumulativeRating);
    }


    @Override
    public String toString() {
        return "CourseMeta{" +
                "id=" + id +
                ", courseId=" + courseId +
                ", newTill='" + newTill + '\'' +
                ", totalDuration=" + totalDuration +
                ", availableContentDuration=" + availableContentDuration +
                ", cumulativeRating=" + cumulativeRating +
                ", lectureCount=" + lectureCount +
                ", reviewCount=" + reviewCount +
                ", likesCount=" + likesCount +
                ", dislikesCount=" + dislikesCount +
                '}';
    }
}
