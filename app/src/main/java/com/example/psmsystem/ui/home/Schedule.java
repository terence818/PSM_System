package com.example.psmsystem.ui.home;

public class Schedule {



    public Schedule(String name, String date, String day, String serve, String calories) {
        this.name = name;
        this.date = date;
        this.day = day;
        this.serve = serve;
        this.calories = calories;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getServe() {
        return serve;
    }

    public void setServe(String serve) {
        this.serve = serve;
    }

    private String name;
    private String date;
    private String day;
    private String serve;

    public String getCalories() {
        return calories;
    }

    public void setCalories(String calories) {
        this.calories = calories;
    }

    private String calories;


    public Schedule() {
        this.name="";
        this.date="";
        this.day="";
        this.serve="";
        this.calories="";
    }



}


