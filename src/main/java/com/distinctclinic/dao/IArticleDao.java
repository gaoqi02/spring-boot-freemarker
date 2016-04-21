package com.distinctclinic.dao;


import com.distinctclinic.model.Article;

import java.util.List;

public interface IArticleDao {

    public Article getById(int id);

    public List<Article> getByIds(List<Integer> ids);

    public int insert(Article article);

    public int update(Article article);

    public int deleteById(int id);

    public List<Article> getByTag(String tag);

    public List<Article> getSimilarityByTitle(String title);

    public List<Article> getAllArticles();

    public Article getByTitle(String title);


}
