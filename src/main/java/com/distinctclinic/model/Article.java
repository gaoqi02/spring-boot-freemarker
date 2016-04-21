package com.distinctclinic.model;

import java.util.List;

/**
 * Created by gaoqi on 2015/11/28.
 */
public class Article {

    private int id; //
    private String title;
    private String url;
    private int sequence;
    private String content;
    private List<String> tags;
    private List<String> otherTopics;
    private String postDate;

    public String getPostDate() {
        return postDate;
    }

    public void setPostDate(String postDate) {
        this.postDate = postDate;
    }

    public List<String> getOtherTopics() {
        return otherTopics;
    }

    public void setOtherTopics(List<String> otherTopics) {
        this.otherTopics = otherTopics;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
