package com.example.firsttry;

public class TournamentDataClass {

    private String title;
    private String location;

    private String date;

    private String hour;

    private String description;

    public TournamentDataClass(String title, String location, String date, String hour, String description) {
        this.title = title;
        this.location = location;
        this.date = date;
        this.hour = hour;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
