package com.example.dheeraj.superprofs.models;

/**
 * Created by dheeraj on 18/2/15.
 */
public final class Lecture {

    private int id;
    private String name;
    private String description;
    private int isPublic;
    private int duration;
    private String imageUrl;
    private int broadcastState;
    private int status;
    private int isQualityVerified;
    private int qualityVerifiedBy;
    private String comment;



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

    public boolean getIsPublic() {
        return isPublic == 1;
    }

    public void setIsPublic(int isPublic) {
        this.isPublic = isPublic;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getBroadcastState() {
        return broadcastState;
    }

    public void setBroadcastState(int broadcastState) {
        this.broadcastState = broadcastState;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getIsQualityVerified() {
        return isQualityVerified;
    }

    public void setIsQualityVerified(int isQualityVerified) {
        this.isQualityVerified = isQualityVerified;
    }

    public int getQualityVerifiedBy() {
        return qualityVerifiedBy;
    }

    public void setQualityVerifiedBy(int qualityVerifiedBy) {
        this.qualityVerifiedBy = qualityVerifiedBy;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public String toString() {
        return "Lecture{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", isPublic=" + isPublic +
                ", duration=" + duration +
                ", imageUrl='" + imageUrl + '\'' +
                ", broadcastState=" + broadcastState +
                ", status=" + status +
                ", isQualityVerified=" + isQualityVerified +
                ", qualityVerifiedBy=" + qualityVerifiedBy +
                ", comment='" + comment + '\'' +
                '}';
    }
}
