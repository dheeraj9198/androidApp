package com.example.dheeraj.superprofs.models.MPDModels;

/**
 * Created by dheeraj on 26/2/15.
 */
public class AdaptationSetBase {
    private RepresentationBase Representation;
    private SegmentTemplateBase SegmentTemplate;

    public SegmentTemplateBase getSegmentTemplate() {
        return SegmentTemplate;
    }

    public void setSegmentTemplate(SegmentTemplateBase segmentTemplate) {
        SegmentTemplate = segmentTemplate;
    }

    public RepresentationBase getRepresentation() {

        return Representation;
    }

    public void setRepresentation(RepresentationBase representation) {
        Representation = representation;
    }
}
