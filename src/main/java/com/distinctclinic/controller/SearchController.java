package com.distinctclinic.controller;

import com.distinctclinic.service.ISearchService;
import com.weejinfu.common.message.JsonData;
import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/**
 * Created by gaoqi on 2015/11/28.
 */
@RestController
@RequestMapping(value = "/distinctclinic")
public class SearchController {

    private Logger logger = Logger.getLogger(this.getClass());

    @Autowired
    private ISearchService searchService;

    /**
     * 查询文章
     * 1.从数据库拿，看是否有相关的keyword,如果没有则2，有就直接返回
     * 2.从es中拿，内容进行匹配
     * @param tag
     * @return
     */
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public JsonData index(@RequestParam("tag") String tag){

        JsonData jsonData = new JsonData(true);

        logger.info("***********************************");
        logger.info("开始执行查询，输入的查询条件是:" + tag);

        try {
            searchService.searchForTag(jsonData, tag);

        } catch (Exception e) {
            //如果es查询出现了错误，那么从数据库中用like 和title进行匹配
            e.printStackTrace();
            searchService.searchForTagWrong(jsonData, tag);
        }

        logger.info("***********************************");
        return jsonData;
    }


}
