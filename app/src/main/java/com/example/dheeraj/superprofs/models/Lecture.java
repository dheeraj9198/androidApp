package com.example.dheeraj.superprofs.models;

/**
 * Created by dheeraj on 18/2/15.
 */
public final class Lecture {

    private int id;
    private String name;
    private String description;
    private int is_public;
    private int duration;
    private String image_url;
    private int broadcast_state;
    private int status;
    private int is_quality_verified;
    private int quality_verified_by;
    private String comment;

    @Override
    public String toString() {
        return "Lecture{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", is_public=" + is_public +
                ", duration=" + duration +
                ", image_url='" + image_url + '\'' +
                ", broadcast_state=" + broadcast_state +
                ", status=" + status +
                ", is_quality_verified=" + is_quality_verified +
                ", quality_verified_by=" + quality_verified_by +
                ", comment='" + comment + '\'' +
                '}';
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

    public int getIs_public() {
        return is_public;
    }

    public void setIs_public(int is_public) {
        this.is_public = is_public;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public int getBroadcast_state() {
        return broadcast_state;
    }

    public void setBroadcast_state(int broadcast_state) {
        this.broadcast_state = broadcast_state;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getIs_quality_verified() {
        return is_quality_verified;
    }

    public void setIs_quality_verified(int is_quality_verified) {
        this.is_quality_verified = is_quality_verified;
    }

    public int getQuality_verified_by() {
        return quality_verified_by;
    }

    public void setQuality_verified_by(int quality_verified_by) {
        this.quality_verified_by = quality_verified_by;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
