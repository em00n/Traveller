package com.emon.traveller.model;

public class Note {
    int noteid;
    String subject,details,date,time;

    public Note() {
    }

    public Note(int noteid, String subject, String details, String date, String time) {
        this.noteid = noteid;
        this.subject = subject;
        this.details = details;
        this.date = date;
        this.time = time;
    }

    public int getNoteid() {
        return noteid;
    }

    public void setNoteid(int noteid) {
        this.noteid = noteid;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
