package com.gmail.buckleyniall100.myapplication;

import java.util.Date;

public class User {

    public int score;
    public Date startDate;
    public Date endDate;
    public int ticked;
    public int hoursSinceSlept;
    public int hoursSlept;


    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(int score, Date startDate, Date endDate, int ticked, int hoursSlept, int hoursSinceSlept) {
        this.score = score;
        this.startDate = startDate;
        this.endDate = endDate;
        this.ticked = ticked;
        this.hoursSlept = hoursSlept;
        this.hoursSinceSlept = hoursSinceSlept;
    }

}
