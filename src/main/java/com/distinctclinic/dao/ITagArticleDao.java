package com.distinctclinic.dao;


import com.distinctclinic.model.TagArticle;

import java.util.List;

public interface ITagArticleDao {

    public TagArticle getById(int id);

    public List<TagArticle> getByArticleId(int id);

    public List<TagArticle> getByIds(List<Integer> ids);

    public int insert(TagArticle tagArticle);

    public int update(TagArticle tagArticle);

    public int deleteById(int id);

    public int deleteByArticleId(int id);

    public List<TagArticle> getByTag(String tag);

}
