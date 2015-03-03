package com.example.dheeraj.superprofs.models.MPDModels;

import java.util.ArrayList;

/**
 * Created by dheeraj on 26/2/15.
 */
public class PeriodBase {
    private ArrayList<AdaptationSetBase> AdaptationSet;

    public ArrayList<AdaptationSetBase> getAdaptationSet() {
        return AdaptationSet;
    }

    public void setAdaptationSet(ArrayList<AdaptationSetBase> adaptationSet) {
        AdaptationSet = adaptationSet;
    }
}
