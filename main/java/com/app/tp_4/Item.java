package com.app.tp_4;

import java.util.Date;

public class Item {
    private String note;
    private Date date;

    public Item() {
    }

    public Item(String note, Date date) {
        this.note = note;
        this.date = date;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
