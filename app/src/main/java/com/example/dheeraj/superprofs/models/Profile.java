package com.example.dheeraj.superprofs.models;

import android.graphics.Bitmap;

import com.example.dheeraj.superprofs.utils.ImageUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by dheeraj on 19/2/15.
 */
@JsonIgnoreProperties({"bitMap"})
public final class Profile {
    private  int id;
    private int userId;
    private String imageUrl;
    private String imageCaption;
    private int gender;
    private String dateOfBirth;
    private String facebookUrl;


    private String linkedUrl;
    private String twitterUrl;
    private String createdAt;
    private String updatedAt;

    @JsonIgnore
    private Bitmap bitmap;

    public void eraseBitmap(){
        bitmap = null;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void downloadBitmap() {
        this.bitmap = ImageUtils.getBitmapFromUrl(imageUrl);
    }

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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageCaption() {
        return imageCaption;
    }

    public void setImageCaption(String imageCaption) {
        this.imageCaption = imageCaption;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getFacebookUrl() {
        return facebookUrl;
    }

    public void setFacebookUrl(String facebookUrl) {
        this.facebookUrl = facebookUrl;
    }

    public String getLinkedUrl() {
        return linkedUrl;
    }

    public void setLinkedUrl(String linkedUrl) {
        this.linkedUrl = linkedUrl;
    }

    public String getTwitterUrl() {
        return twitterUrl;
    }

    public void setTwitterUrl(String twitterUrl) {
        this.twitterUrl = twitterUrl;
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

    @Override
    public String toString() {
        return "Profile{" +
                "id=" + id +
                ", userId=" + userId +
                ", imageUrl='" + imageUrl + '\'' +
                ", imageCaption='" + imageCaption + '\'' +
                ", gender=" + gender +
                ", dateOfBirth='" + dateOfBirth + '\'' +
                ", facebookUrl='" + facebookUrl + '\'' +
                ", linkedUrl='" + linkedUrl + '\'' +
                ", twitterUrl='" + twitterUrl + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                '}';
    }
}
