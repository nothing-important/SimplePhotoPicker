package com.example.photopickerlirary;

import android.support.annotation.NonNull;

public class PhotoBean implements Comparable<PhotoBean>{

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

    @Override
    public int compareTo(@NonNull PhotoBean photoBean) {

        return 0;
    }
}
