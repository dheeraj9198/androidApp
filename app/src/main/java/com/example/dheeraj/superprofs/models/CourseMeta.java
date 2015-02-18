package com.example.dheeraj.superprofs.models;

/**
 * Created by dheeraj on 18/2/15.
 */
public final class CourseMeta {

    private static final String minuteTag = " Minutes";
    private static final String hoursTag = " Hours";

    private int id;
    private int course_id;
    private String new_till;
    private int total_duration;
    private int available_content_duration;
    private float cumulative_rating;
    private int lecture_count;
    private int review_count;
    private int likes_count;
    private int dislikes_count;

    public int getId() {
        return id;
    }

    public String getDurationString() {
        int x = total_duration;
        x = x / 60;
        if (x < 60) {
            return x + minuteTag;
        }
        x = x / 60;
        return x + hoursTag;
    }


    @Override
    public String toString() {
        return "CourseMeta{" +
                "id=" + id +
                ", course_id=" + course_id +
                ", new_till='" + new_till + '\'' +
                ", total_duration=" + total_duration +
                ", available_content_duration=" + available_content_duration +
                ", cumulative_rating=" + cumulative_rating +
                ", lecture_count=" + lecture_count +
                ", review_count=" + review_count +
                ", likes_count=" + likes_count +
                ", dislikes_count=" + dislikes_count +
                '}';
    }

    public int getCourse_id() {
        return course_id;
    }

    public void setCourse_id(int course_id) {
        this.course_id = course_id;
    }

    public String getNew_till() {
        return new_till;
    }

    public void setNew_till(String new_till) {
        this.new_till = new_till;
    }

    public int getTotal_duration() {
        return total_duration;
    }

    public void setTotal_duration(int total_duration) {
        this.total_duration = total_duration;
    }

    public int getAvailable_content_duration() {
        return available_content_duration;
    }

    public void setAvailable_content_duration(int available_content_duration) {
        this.available_content_duration = available_content_duration;
    }

    public float getCumulative_rating() {

        return cumulative_rating;
    }

    public String getCumulativeRatingString(){
        return String.format("%.1f", cumulative_rating);
    }

    public void setCumulative_rating(float cumulative_rating) {
        this.cumulative_rating = cumulative_rating;
    }

    public int getLecture_count() {
        return lecture_count;
    }

    public void setLecture_count(int lecture_count) {
        this.lecture_count = lecture_count;
    }

    public int getReview_count() {
        return review_count;
    }

    public void setReview_count(int review_count) {
        this.review_count = review_count;
    }

    public int getLikes_count() {
        return likes_count;
    }

    public void setLikes_count(int likes_count) {
        this.likes_count = likes_count;
    }

    public int getDislikes_count() {
        return dislikes_count;
    }

    public void setDislikes_count(int dislikes_count) {
        this.dislikes_count = dislikes_count;
    }

    public void setId(int id) {
        this.id = id;
    }
}
