package com.example.dheeraj.superprofs.events;

/**
 * Created by windows 7 on 3/3/2015.
 */
public class DownloadProgressEvent {
    private int lectureId;
    private int percent;

    public DownloadProgressEvent(int lectureId, int percent) {
        this.lectureId = lectureId;
        this.percent = percent;
    }

    public int getLectureId() {
        return lectureId;
    }

    public void setLectureId(int lectureId) {
        this.lectureId = lectureId;
    }

    public int getPercent() {
        return percent;
    }

    public void setPercent(int percent) {
        this.percent = percent;
    }
}
