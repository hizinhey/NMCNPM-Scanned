package com.johnnghia.scanned.models.objects;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TextFile {
    private String title;
    private String text;
    private Date date;

    public TextFile(String text, Date date, String title) {
        this.text = text;
        this.date = date;
        this.title = title;
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

    public static List<TextFile> dummyFiles(int size) {
        List<TextFile> textFiles = new ArrayList<>();
        for (int i = 0; i < size; i++){
            textFiles.add(new TextFile("abc", new Date(),"File " + i));
        }
        return textFiles;
    }
}
