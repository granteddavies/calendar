package com.calendar;

import java.io.Serializable;
import java.util.Date;

public class Event implements Serializable{
    private int year;
    private int month;
    private int dayOfMonth;
    private String description;

    public Event(int year, int month, int dayOfMonth, String description) {
        this.year = year;
        this.month = month;
        this.dayOfMonth = dayOfMonth;
        this.description = description;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDayOfMonth() {
        return dayOfMonth;
    }

    public String getDescription() {
        return description;
    }
}
