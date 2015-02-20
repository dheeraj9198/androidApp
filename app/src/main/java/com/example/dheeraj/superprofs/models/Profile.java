package com.example.dheeraj.superprofs.models;

import android.graphics.Bitmap;

import com.example.dheeraj.superprofs.utils.ImageUtils;

/**
 * Created by dheeraj on 19/2/15.
 */
public final class Profile {
    private  int id;
    private int user_id;
    private String image_url;
    private String image_caption;
    private int gender;
    private String date_of_birth;
    private String facebook_url;


    private String linked_url;
    private String twitter_url;
    private String created_at;
    private String updated_at;

    private Bitmap bitmap;

    public Bitmap getBitmap() {
        return bitmap;
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

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public void setBitmap() {
        this.bitmap = ImageUtils.getBitmapFromUrl(image_url);
    }

    public String getImage_caption() {
        return image_caption;
    }

    public void setImage_caption(String image_caption) {
        this.image_caption = image_caption;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getDate_of_birth() {
        return date_of_birth;
    }

    public void setDate_of_birth(String date_of_birth) {
        this.date_of_birth = date_of_birth;
    }

    public String getFacebook_url() {
        return facebook_url;
    }

    public void setFacebook_url(String facebook_url) {
        this.facebook_url = facebook_url;
    }

    public String getLinked_url() {
        return linked_url;
    }

    public void setLinked_url(String linked_url) {
        this.linked_url = linked_url;
    }

    public String getTwitter_url() {
        return twitter_url;
    }

    public void setTwitter_url(String twitter_url) {
        this.twitter_url = twitter_url;
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

    @Override
    public String toString() {
        return "Profile{" +
                "id=" + id +
                ", user_id=" + user_id +
                ", image_url='" + image_url + '\'' +
                ", image_caption='" + image_caption + '\'' +
                ", gender=" + gender +
                ", date_of_birth='" + date_of_birth + '\'' +
                ", facebook_url='" + facebook_url + '\'' +
                ", linked_url='" + linked_url + '\'' +
                ", twitter_url='" + twitter_url + '\'' +
                ", created_at='" + created_at + '\'' +
                ", updated_at='" + updated_at + '\'' +
                '}';
    }
}
