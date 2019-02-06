package com.gmail.buckleyniall100.myapplication;

import java.util.Date;

public class User {

    public int score;
    public Date startDate;
    public Date endDate;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(int score, Date startDate, Date endDate) {
        this.score = score;
        this.startDate = startDate;
        this.endDate = endDate;
    }

}
