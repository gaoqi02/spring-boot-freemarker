package com.distinctclinic.domain;

/**
 * Created by gaoqi on 2015/12/2.
 */
public class Item {

    private String content;
    private int date;
    private String id;
    private String title;
    private String post_data;

    public String getPost_data() {
        return post_data;
    }

    public void setPost_data(String post_data) {
        this.post_data = post_data;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
