package com.example.dheeraj.superprofs.models.MPDModels;

/**
 * Created by dheeraj on 26/2/15.
 */
public class SegmentTemplateBase {
    private SegmentTimelineBase SegmentTimeline;
    private String initialization;
    private String media;
    private String timescale;


    public SegmentTimelineBase getSegmentTimeline() {
        return SegmentTimeline;
    }

    public void setSegmentTimeline(SegmentTimelineBase segmentTimeline) {
        SegmentTimeline = segmentTimeline;
    }

    public String getInitialization() {
        return initialization;
    }

    public void setInitialization(String initialization) {
        this.initialization = initialization;
    }

    public String getMedia() {
        return media;
    }

    public void setMedia(String media) {
        this.media = media;
    }

    public String getTimescale() {
        return timescale;
    }

    public void setTimescale(String timescale) {
        this.timescale = timescale;
    }
}
