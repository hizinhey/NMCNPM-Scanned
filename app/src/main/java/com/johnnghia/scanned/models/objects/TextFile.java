package com.johnnghia.scanned.models.objects;

import com.google.firebase.firestore.IgnoreExtraProperties;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@IgnoreExtraProperties
public class TextFile implements Serializable {
    private String id;
    private String title;
    private String text;
    private Date date;

    public TextFile(){}
    public TextFile(String id, String text, Date date, String title) {
        this.text = text;
        this.date = date;
        this.title = title;
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
