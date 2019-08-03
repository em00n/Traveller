package com.emon.traveller.model;

public class Model {
    int id;
           String reason,amount,date,time;

    public Model(int id,String reason, String amount,String date,String time) {
        this.id = id;
        this.reason = reason;
        this.amount = amount;
        this.date=date;
        this.time=time;
    }

    public Model() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
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
