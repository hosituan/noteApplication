package com.example.noteapplication.bean;
import java.util.Date;

import java.io.Serializable;

public class Category implements Serializable {
    private String title;
    private Date date;

    public Category(String title, Date date) {
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