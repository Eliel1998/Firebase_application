package com.example.firebase_application;

import androidx.annotation.NonNull;

public class CustomMovie {
    private String title;
    private String note;

    public CustomMovie(String title, String note) {
        this.title = title;
        this.note = note;
    }

    public String getTitle() {
        return title;
    }

    public String getNote() {
        return note;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public String toString() {
        return "Filme: " + title + "\n" + "Nota: " + note;
    }
}
