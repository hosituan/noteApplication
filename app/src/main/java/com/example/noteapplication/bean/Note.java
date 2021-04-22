package com.example.noteapplication.bean;

import java.io.Serializable;
import java.util.*;
public class Note implements Serializable {

    private String noteTitle;
    private String category;
    private String priority;
    private String status;
    private Date date;

    public Note()  {

    }

    public Note(String noteTitle, String category, String priority, String status, Date date) {
        this.noteTitle= noteTitle;
        this.category = category;
        this.priority = priority;
        this.status = status;
        this.date = date;
    }

    public String getNoteTitle() {
        return noteTitle;
    }

    public void setNoteTitle(String noteTitle) {
        this.noteTitle = noteTitle;
    }

    public  String getCategory() {
        return  this.category;
    }

    public  String getPriority() {
        return  this.priority;
    }
    public  String getStatus() {
        return  this.status;
    }
    public  void setCategory(String category) {
        this.category = category;
    }

    public  void setPriority(String priority) {
        this.priority = priority;
    }

    public  void setStatus(String status) {
        this.status = status;
    }

    public Date getDate() {
        return  this.date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

}