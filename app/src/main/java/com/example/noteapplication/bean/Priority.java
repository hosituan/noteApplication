package com.example.noteapplication.bean;

import java.util.Date;

import java.io.Serializable;

public class Priority implements Serializable {
    private String title = "";
    private Date date = new Date();


    public  Priority() {

    }

    public Priority(String title, Date date) {
        this.date = date;
        this.title = title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getDate() {
        return this.date;
    }

    public String getTitle() {
        return this.title;
    }
}