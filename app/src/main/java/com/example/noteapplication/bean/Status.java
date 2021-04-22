package com.example.noteapplication.bean;

import java.io.Serializable;
import java.util.Date;

public class Status implements Serializable {
    private String title;
    private Date date;

    public Status(String title, Date date) {
        this.date = date;
        this.title = title;
    }

    public Date getDate() {
        return this.date;
    }

    public String getTitle() {
        return this.title;
    }
}