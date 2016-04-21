package com.distinctclinic.service.impl;

import com.distinctclinic.constant.DistinctConfig;
import com.distinctclinic.dao.IArticleDao;
import com.distinctclinic.dao.ITagArticleDao;
import com.distinctclinic.model.Article;
import com.distinctclinic.model.TagArticle;
import com.distinctclinic.service.IArticleService;
import com.weejinfu.common.exception.BaseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by gaoqi on 2015/11/30.
 */
@Service
public class ArticleServiceImpl implements IArticleService {

    @Autowired
    private IArticleDao articleDao;

    @Autowired
    private ITagArticleDao tagArticleDao;

    /**
     * 新建文章，1看文章是否存在于数据库，是否有同名的。。
     * 2.若有则直接抛出异常结束 无则3
     * 3.插入文章，插入标签
     * @param article
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int createNewArticle(Article article, List<String> topics) {

        //1.判断数据库中是否存在文章
        Article tmpArticle = articleDao.getByTitle(article.getTitle());
        if (tmpArticle != null) {
            throw new BaseException(500, "数据库中已有同名文章");
        }
        int articleId = articleDao.insert(article);

        StringBuffer tmpTopic = new StringBuffer();
        if (topics != null) {
            for (String s : topics) {
                tmpTopic.append(s);
                tmpTopic.append(DistinctConfig.topicPattern);
            }
        }

        for (String tag: article.getTags()) {
            TagArticle tagArticle = new TagArticle();
            tagArticle.setArticleId(articleId);
            tagArticle.setTag(tag);
            tagArticle.setOtherTopic(tmpTopic.toString());
            tagArticleDao.insert(tagArticle);
        }

        return articleId;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateArticle(Article article) {

        if (articleDao.getByTitle(article.getTitle()) != null) {
            throw new BaseException(500, "数据库中已有重复项目");
        }
        /**
         * 更新article的相关信息
         */
        articleDao.update(article);

        /**
         * 更新tagArticle
         */
        tagArticleDao.deleteByArticleId(article.getId());

        StringBuffer tmpTopic = new StringBuffer();
        for (String s : article.getOtherTopics()) {
            tmpTopic.append(s);
            tmpTopic.append(DistinctConfig.topicPattern);
        }
        for (String tag: article.getTags()) {
            TagArticle tagArticle = new TagArticle();
            tagArticle.setArticleId(article.getId());
            tagArticle.setTag(tag);
            tagArticle.setOtherTopic(tmpTopic.toString());
            tagArticleDao.insert(tagArticle);
        }

        return 1;
    }

    @Override
    public int deleteTag(int tagId) {
        return tagArticleDao.deleteById(tagId);
    }

    @Override
    public int updateTag(TagArticle tagArticle) {
        //判断是否有重名标签
        if (tagArticleDao.getByTag(tagArticle.getTag()) != null) {
            throw new BaseException(500, "已有重名标签");
        }
        return tagArticleDao.update(tagArticle);
    }

    @Override
    public int newTag(TagArticle tagArticle) {
        return tagArticleDao.insert(tagArticle);
    }
}
