package com.distinctclinic.dao.impl;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Collections;
import java.math.BigDecimal;

import javax.sql.DataSource;

import com.distinctclinic.dao.IArticleDao;
import com.distinctclinic.model.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;


@Repository
public class ArticleDaoImpl extends NamedParameterJdbcDaoSupport implements
        IArticleDao, RowMapper<Article> {

    public static final String QUERY_BY_ID = "select * from article where id = :id";

    public static final String QUERY_BY_IDS = "select * from article where id in (:ids)";

    public static final String INSERT = "insert into article (title,url,sequence,content) values (:title,:url,:sequence,:content)";

    public static final String UPDATE = "update article set title=:title,url=:url,sequence=:sequence,content=:content,post_date=:post_date where id=:id";

    public static final String DELETE_BY_DID = "delete from article where id = :id";
    @Override
    public Article mapRow(ResultSet rs, int rowNum) throws SQLException {
        Article article = new Article();
        article.setId(rs.getInt("id"));
        article.setTitle(rs.getString("title"));
        article.setUrl(rs.getString("url"));
        article.setSequence(rs.getInt("sequence"));
        article.setContent(rs.getString("content"));
        article.setContent(rs.getString("post_date"));
        return article;

    }

    @Autowired
    public void initDataSource(DataSource dataSource) {
        setDataSource(dataSource);
    }

    @Override
    public Article getById(int id) {

        MapSqlParameterSource paramSource = new MapSqlParameterSource();
        paramSource.addValue("id", id);
        List<Article> list = this.getNamedParameterJdbcTemplate().query(
                QUERY_BY_ID, paramSource, this);
        if(list!= null && list.size() >0){
            return list.get(0);
        }
        return null;

    }

    @Override
    public List<Article> getByIds(List<Integer> ids) {
        if(ids==null || ids.size()==0){
            return Collections.emptyList();
        }

        MapSqlParameterSource paramSource = new MapSqlParameterSource();
        paramSource.addValue("ids", ids);
        List<Article> list = this.getNamedParameterJdbcTemplate().query(
                QUERY_BY_IDS, paramSource, this);
        return list;

    }

    @Override
    public int insert(Article article) {

        KeyHolder keyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource paramSource = new MapSqlParameterSource();
        paramSource.addValue("title", article.getTitle());
        paramSource.addValue("url", article.getUrl());
        paramSource.addValue("sequence", article.getSequence());
        paramSource.addValue("content", article.getContent());
        int rows = this.getNamedParameterJdbcTemplate().update(INSERT,
                paramSource,keyHolder);
        if (rows > 0) {
            return keyHolder.getKey().intValue();  //返回主键id
        } else {
            return 0;
        }

    }

    @Override
    public int update(Article article) {
        MapSqlParameterSource paramSource = new MapSqlParameterSource();
        paramSource.addValue("id", article.getId());
        paramSource.addValue("title", article.getTitle());
        paramSource.addValue("url", article.getUrl());
        paramSource.addValue("sequence", article.getSequence());
        paramSource.addValue("content", article.getContent());
        paramSource.addValue("post_date", article.getPostDate());
        return this.getNamedParameterJdbcTemplate().update(
                UPDATE, paramSource);

    }

    @Override
    public int deleteById(int id) {
        MapSqlParameterSource paramSource = new MapSqlParameterSource();
        paramSource.addValue("id", id);
        return this.getNamedParameterJdbcTemplate().update(
                DELETE_BY_DID, paramSource);
    }

    @Override
    public List<Article> getByTag(String tag) {
        MapSqlParameterSource paramSource = new MapSqlParameterSource();
        paramSource.addValue("tag", tag);

        List<Article> list = this.getNamedParameterJdbcTemplate().query(
                "select * from article where tag=:tag", paramSource, this);
        return list;
    }

    @Override
    public List<Article> getSimilarityByTitle(String title) {
        MapSqlParameterSource paramSource = new MapSqlParameterSource();
        paramSource.addValue("title", title);

        StringBuffer stringBuffer = new StringBuffer("SELECT * from article where title LIKE");
        stringBuffer.append(" '%" + title + "%' ");

        List<Article> list = this.getNamedParameterJdbcTemplate().query(
                stringBuffer.toString(), paramSource, this);
        return list;
    }

    @Override
    public List<Article> getAllArticles() {
        MapSqlParameterSource paramSource = new MapSqlParameterSource();

        StringBuffer stringBuffer = new StringBuffer();

        List<Article> list = this.getNamedParameterJdbcTemplate().query(
                "SELECT * from article", paramSource, this);
        return list;
    }

    @Override
    public Article getByTitle(String title) {
        MapSqlParameterSource paramSource = new MapSqlParameterSource();
        paramSource.addValue("title", title);

        List<Article> list = this.getNamedParameterJdbcTemplate().query(
                "SELECT * from article where title =:title", paramSource, this);
        if (list != null && list.size() >0) {
            return list.get(0);
        }

        return null;
    }
}
