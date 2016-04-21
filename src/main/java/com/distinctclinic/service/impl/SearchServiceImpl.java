package com.distinctclinic.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.distinctclinic.dao.IArticleDao;
import com.distinctclinic.dao.ITagArticleDao;
import com.distinctclinic.domain.Request;
import com.distinctclinic.domain.CrawlRequestVo;
import com.distinctclinic.model.Article;
import com.distinctclinic.model.TagArticle;
import com.distinctclinic.service.ISearchService;
import com.distinctclinic.util.ESUtils;
import com.weejinfu.common.message.JsonData;
import org.apache.log4j.Logger;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by gaoqi on 2015/11/28.
 */
@Service
public class SearchServiceImpl implements ISearchService {

    private Logger logger = Logger.getLogger(this.getClass());

    @Autowired
    private IArticleDao articleDao;

    @Autowired
    private ITagArticleDao tagArticleDao;

    public static Client client = ESUtils.getInstance();

    @Override
    public void searchForTag(JsonData jsonData, String tag) {
        List<TagArticle> tagArticles = tagArticleDao.getByTag(tag);

        if (tagArticles != null && tagArticles.size() > 0) {
//        if (false) {
            logger.info("数据库查询的结果：" + JSONObject.toJSONString(tagArticles));

            HashSet<String> set = new HashSet<>();
            List<Article> articles = new ArrayList<>();

            for (TagArticle tagArticle: tagArticles) {
                Article article = articleDao.getById(tagArticle.getArticleId());
                articles.add(article);

                String[] otherTopics = tagArticle.getOtherTopic().split("、");
                for (String otherTopic: otherTopics) {
                    if (!set.contains(otherTopic)) {
                        set.add(otherTopic);
                    }
                }

            }

            Collections.sort(articles, new Comparator<Article>() {
                @Override
                public int compare(Article o1, Article o2) {
                    if (o1.getSequence() > o2.getSequence()) {
                        return 1;
                    } else {
                        return -1;
                    }
                }
            });
            logger.info("数据库查询排序的结果：" + JSONObject.toJSONString(tagArticles));
            jsonData.put("articles", articles);
            jsonData.put("other_topic", set);
        } else {
            logger.info("数据库查询结果为空，执行ES进行查询");
            /**
             * 如果articles的大小为0..从es中拿
             * ES中拿文章内容做索引。。
             * 下面的相关tag则再发一次请求
             * //TODO  这里的查询未来需要优化一下
             */
            //按照文章内容进行匹配
            SearchResponse response = client.prepareSearch("distinctclinic")
                    .setTypes("search").setQuery(QueryBuilders.matchQuery("content", tag)).setFrom(0).setSize(20)
                    .execute().actionGet();

            SearchHits shs = response.getHits();
            List<Map<String, Object>> contentList = new ArrayList<Map<String, Object>>();
            for (SearchHit hit : shs) {
                contentList.add(hit.getSource());
            }
            logger.info("ES进行查询，结果如下" + JSONObject.toJSONString(contentList));
            jsonData.put("articles", contentList);

            //----------------相关tag-------------------
            SearchResponse responseTag = client.prepareSearch("tags")
                    .setTypes("search").setQuery(QueryBuilders.matchQuery("tag", tag)).setFrom(0).setSize(7)
                    .execute().actionGet();

            SearchHits hits = responseTag.getHits();
            List<String> tags = new ArrayList<String>();
            for (SearchHit hit : hits) {
                tags.add(hit.getSource().get("tag").toString());
            }
            jsonData.put("other_topic", tags);
        }

    }

    @Override
    public void searchForTagWrong(JsonData jsonData, String tag) {
        List<TagArticle> tagArticles = tagArticleDao.getByTag(tag);
        List<Article> articles = articleDao.getSimilarityByTitle(tag);
        HashSet set = new HashSet();
        Collections.sort(articles, new Comparator<Article>() {
            @Override
            public int compare(Article o1, Article o2) {
                if (o1.getSequence() > o2.getSequence()){
                    return 1;
                } else {
                    return -1;
                }
            }
        });

        jsonData.put("articles", articles);
        for (TagArticle tagArticle: tagArticles) {
            Article article = articleDao.getById(tagArticle.getArticleId());
            articles.add(article);

            String[] otherTopics = tagArticle.getOtherTopic().split("、");
            for (String otherTopic: otherTopics) {
                if (!set.contains(otherTopic)) {
                    set.add(otherTopic);
                }
            }

        }
        jsonData.put("other_topic", set);
    }

    @Override
    public CrawlRequestVo build(Request request, String spiderName) {
        CrawlRequestVo crawlRequestVo = new CrawlRequestVo();

        crawlRequestVo.setRequest(request);
        crawlRequestVo.setSpider_name(spiderName);
        return crawlRequestVo;
    }


}
