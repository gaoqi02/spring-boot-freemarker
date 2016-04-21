package com.distinctclinic.controller;

import com.alibaba.fastjson.JSONObject;
import com.distinctclinic.controller.helper.BaseController;
import com.distinctclinic.controller.helper.ControllerHelper;
import com.distinctclinic.dao.IArticleDao;
import com.distinctclinic.dao.ITagArticleDao;
import com.distinctclinic.domain.Item;
import com.distinctclinic.domain.Request;
import com.distinctclinic.domain.CrawlRequestVo;
import com.distinctclinic.model.Article;
import com.distinctclinic.model.TagArticle;
import com.distinctclinic.service.IArticleService;
import com.distinctclinic.service.ISearchService;
import com.weejinfu.common.exception.BaseException;
import com.weejinfu.common.message.JsonData;
import com.weejinfu.common.rpc.IRemoteService;
import com.weejinfu.common.util.HttpClientUtils;
import com.weejinfu.common.util.JsonParser;
import org.apache.commons.httpclient.HttpClient;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by gaoqi on 2015/11/30.
 */
@Controller
@RequestMapping(value = "/distinctclinic/article")
public class ArticleTagController extends BaseController {

    private Logger logger = Logger.getLogger(this.getClass());

    @Autowired
    private IArticleService articleService;

    @Autowired
    private ITagArticleDao tagArticleDao;

    @Autowired
    private IArticleDao articleDao;

    @Autowired
    private ISearchService searchService;

    protected IRemoteService scrawlService = getScrawlService();

    /**
     * 获得指定文章
     * @param id
     * @param model
     * @return
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String detail(@PathVariable("id") int id,Map<String, Object> model){

        Article article = articleDao.getById(id);
        List<TagArticle> tagArticles = tagArticleDao.getByArticleId(id);

        model.put("article", article);
        model.put("tagArticles", tagArticles);

        return "/index";
    }


    /**
     * 获取所有文章
     * @param model
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String allArticlesList(Map<String, Object> model) throws IOException{

        List<Article> articles = articleDao.getAllArticles();

        model.put("articles", articles);

        return "/art_list";
    }

    /**
     * 新建文章
     * @param article
     * @return
     */
    @RequestMapping(value = "", method = RequestMethod.POST, consumes = {"application/json"})
    @ResponseBody
    public JsonData newArticle(@RequestBody Article article){

        JsonData jsonData = new JsonData(true);

        logger.info("***********************************");
        logger.info("新建文章，文章内容如下:" + JSONObject.toJSONString(article));

        try {
            int id = articleService.createNewArticle(article, article.getOtherTopics());
            if (id < 1) {
                throw new BaseException(500, "新建文章失败，可能是数据库错误出现回滚");
            }

        } catch (BaseException e) {
            logger.info("新建文章出错,错误如下:" + e.getMessage());
            jsonData.setErrResult(e);
        }

        logger.info("***********************************");
        return jsonData;
    }

    /**
     * 更新文章
     * @param article
     * @return
     */
    @RequestMapping(value = "/", method = RequestMethod.PUT)
    @ResponseBody
    public JsonData updateArticle(@RequestBody Article article){

        JsonData jsonData = new JsonData(true);

        logger.info("***********************************");
        logger.info("更新文章，文章内容如下:" + JSONObject.toJSONString(article));

        try {
            int id = articleService.updateArticle(article);
            if (id < 1) {
                throw new BaseException(500, "更新文章失败，可能是数据库错误出现回滚");
            }

        } catch (BaseException e) {
            logger.info("更新文章出错,错误如下:" + e.getMessage());
            jsonData.setErrResult(e);
        }

        logger.info("***********************************");
        return jsonData;
    }

//    public static void main(String[] args) {

//        Article article = new Article();
//        article.setUrl("www.baidu.com");
//        article.setContent("asd");
//        article.setTitle("asd1111");
//        article.setSequence(1);
//        TagArticle tagArticle = new TagArticle();
//        tagArticle.setArticleId(1);
//        tagArticle.setOtherTopic("1、3、/4");
//        tagArticle.setTag("sss");
//        System.out.print(JSONObject.toJSONString(tagArticle));
//        List<String> strings = new ArrayList<>();
//        strings.add("1");
//        strings.add("2");
//        strings.add("3");
//        article.setOtherTopics(strings);
//
//        List<String> strings1 = new ArrayList<>();
//        strings1.add("as");
//        strings1.add("bv");
//        strings1.add("qw");
//        article.setTags(strings1);
//
//        System.out.print(JSONObject.toJSONString(article));
//
//    }

    /**
     * 操作标签的接口
     * 如果传输过来的标签有tagId就表明是更新或者删除.删除：如果标签名字和数据库相同证明没有更新就是删除  如果名字不同则是更新
     * 如果没有tagId表示新建标签
     * @param tagArticle
     * @return
     */
    @RequestMapping(value = "/tag", method = RequestMethod.POST)
    @ResponseBody
    public JsonData dealTag(@RequestBody TagArticle tagArticle){

        JsonData jsonData = new JsonData(true);

        logger.info("***********************************");
        logger.info("操作文章，标签内容如下:" + JSONObject.toJSONString(tagArticle));

        try {
            if (tagArticle.getId() > 0) {
                if (tagArticleDao.getById(tagArticle.getId()).getTag().equals(tagArticle.getTag())) {
                    logger.info("进行删除标签的操作，删除标签");
                    articleService.deleteTag(tagArticle.getId());

                } else {
                    logger.info("更新标签操作:");
                    articleService.updateTag(tagArticle);

                }
            } else {
                int tagId = articleService.newTag(tagArticle);
                logger.info("新建标签操作,新建成功id:" + tagId);

            }

        } catch (BaseException e) {
            logger.info("操作标签出错,错误如下:" + e.getMessage());
            jsonData.setErrResult(e);
        }

        logger.info("***********************************");
        return jsonData;
    }

    /**
     * 请求爬虫接口
     * @return
     */
    @RequestMapping(value = "/crawl", method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public JsonData getWechatArticle(@RequestParam("url")  String url, ModelMap modelMap){

        JsonData jsonData = new JsonData(true);

        logger.info("***********************************");
//        logger.info("获得文章，文章链接:" + request.getUrl());

        try {

            Request request = new Request();
            request.setUrl(url);
            CrawlRequestVo requestVo = searchService.build(request, "weixin_article_spider");
            String checkResult = scrawlService.post("/crawl.json", requestVo);

            if (checkResult == null) {
                throw new BaseException(500, "查找不到任何信息");
            }
            if (!JsonParser.read(checkResult, String.class, "status").equals("ok")) {
                throw new BaseException(500, "未搜索到您输入的文章,请检查输入的连接是否有效");
            }
            List<Item> item = JsonParser.read(checkResult, List.class, "items");

            if (item != null && item.size() > 0) {
                jsonData.put("item", item.get(0));

            } else {
                throw new BaseException(500, "未搜索到您输入的文章,请检查输入的连接是否有效");
            }

        } catch (BaseException e) {
            logger.info("获取出错,错误如下:" + e.getMessage());
            jsonData.setErrResult(e);
        }
        logger.info("***********************************");
        return jsonData;
    }

    /**
     * 、、、、、、、、、、、、、、
     * @return
     */
    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public JsonData parse(){

        JsonData jsonData = new JsonData(true);

        logger.info("***********************************");
//        logger.info("获得文章，文章链接:" + request.getUrl());

        try {
            List<Article> articles = articleDao.getAllArticles();
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            for (Article article : articles) {
                Request request = new Request();
                //去除 url中的换行符等符号
                String url = article.getUrl();
                Matcher m = p.matcher(url);
                url = m.replaceAll("");

                request.setUrl(url);
                article.setUrl(url);
                CrawlRequestVo requestVo = searchService.build(request, "weixin_article_spider");
                String checkResult = scrawlService.post("/crawl.json", requestVo);

                if (checkResult == null) {
                    throw new BaseException(500, "查找不到任何信息");
                }
                if (!JsonParser.read(checkResult, String.class, "status").equals("ok")) {
                    throw new BaseException(500, "未搜索到您输入的文章,请检查输入的连接是否有效");
                }
                List<LinkedHashMap> item = JsonParser.read(checkResult, List.class, "items");

                if (item != null && item.size() > 0) {
                    article.setPostDate(item.get(0).get("post_date").toString());
                    String content = item.get(0).get("content").toString();
                    String txtcontent = content.replaceAll("</?[^>]+>", ""); //剔出<html>的标签
                    content = txtcontent.replaceAll("<a>\\s*|\t|\r|\n</a>", "");//去除字符串中
                    Matcher m1 = p.matcher(content);
                    content = m1.replaceAll("");
                    article.setContent(content);
                } else {
                    throw new BaseException(500, "未搜索到您输入的文章,请检查输入的连接是否有效");
                }
                articleDao.update(article);
            }

        } catch (BaseException e) {
            logger.info("获取出错,错误如下:" + e.getMessage());
            jsonData.setErrResult(e);
        }
        logger.info("***********************************");
        return jsonData;
    }


}
