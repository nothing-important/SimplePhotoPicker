package com.example.photopickerlirary.entity;

import android.support.annotation.NonNull;

import java.io.Serializable;

public class PhotoBean implements Serializable{

    private String path;
    private boolean isSelected;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
