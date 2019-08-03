package com.emon.traveller.model;

public class Blog {
    private String title, discrip, uid, url,prourl,proname,date,selectedkey;

    public Blog() {
    }

    public Blog(String title, String discrip, String uid, String url,String prourl,String proname,String date,String selectedkey) {
        this.title = title;
        this.discrip = discrip;
        this.uid = uid;
        this.url = url;
        this.prourl = prourl;
        this.proname = proname;
        this.date=date;
        this.selectedkey=selectedkey;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDiscrip() {
        return discrip;
    }

    public void setDiscrip(String discrip) {
        this.discrip = discrip;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getProurl() {
        return prourl;
    }

    public void setProurl(String prourl) {
        this.prourl = prourl;
    }

    public String getProname() {
        return proname;
    }

    public void setProname(String proname) {
        this.proname = proname;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSelectedkey() {
        return selectedkey;
    }

    public void setSelectedkey(String selectedkey) {
        this.selectedkey = selectedkey;
    }
}
