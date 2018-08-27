package com.example.photopickerlirary;

import java.util.List;

public class PhotoParentBean {

    private String name;
    private String count;
    private List<PhotoBean> childs;

    public List<PhotoBean> getChilds() {
        return childs;
    }

    public void setChilds(List<PhotoBean> childs) {
        this.childs = childs;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }
}
