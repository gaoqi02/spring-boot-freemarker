package com.distinctclinic.service;

import com.distinctclinic.domain.Request;
import com.distinctclinic.domain.CrawlRequestVo;
import com.weejinfu.common.message.JsonData;

/**
 * Created by gaoqi on 2015/11/28.
 */
public interface ISearchService {

    public void searchForTag(JsonData jsonData, String tag);

    public void searchForTagWrong(JsonData jsonData, String tag);

    public CrawlRequestVo build(Request request, String spiderName);
}
