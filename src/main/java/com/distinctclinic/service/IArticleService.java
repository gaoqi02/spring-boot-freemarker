package com.distinctclinic.service;

import com.distinctclinic.model.Article;
import com.distinctclinic.model.TagArticle;

import java.util.List;

/**
 * Created by gaoqi on 2015/11/30.
 */
public interface IArticleService {

    public int createNewArticle(Article article, List<String> topic);

    public int updateArticle(Article article);

    public int deleteTag(int tagId);

    public int updateTag(TagArticle tagArticle);

    public int newTag(TagArticle tagArticle);
}
