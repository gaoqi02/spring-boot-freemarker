package com.distinctclinic.domain;

/**
 * Created by gaoqi on 2015/12/2.
 */
public class CrawlRequestVo {


    private Request request;
    private String spider_name;

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public String getSpider_name() {
        return spider_name;
    }

    public void setSpider_name(String spider_name) {
        this.spider_name = spider_name;
    }
}
