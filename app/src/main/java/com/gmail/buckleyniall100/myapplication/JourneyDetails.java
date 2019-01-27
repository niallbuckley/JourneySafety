package com.gmail.buckleyniall100.myapplication;

import java.util.Date;

public class JourneyDetails {

    public int numErrors;
    public String startDate;

    public JourneyDetails(){
    }

    public JourneyDetails(int numErrors, String startDate) {
        this.numErrors = numErrors;
        this.startDate = startDate;
    }

    public int getNumErrors() {
        return numErrors;
    }

    public String getStartDate() {
        return startDate;
    }
}
