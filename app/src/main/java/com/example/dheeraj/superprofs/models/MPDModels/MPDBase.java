package com.example.dheeraj.superprofs.models.MPDModels;

/**
 * Created by dheeraj on 26/2/15.
 */
public class MPDBase {
    private String Location;
    private PeriodBase Period;

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public PeriodBase getPeriod() {
        return Period;
    }

    public void setPeriod(PeriodBase period) {
        Period = period;
    }
}
