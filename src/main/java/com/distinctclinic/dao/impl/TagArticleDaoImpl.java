package com.distinctclinic.dao.impl;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Collections;
import java.math.BigDecimal;

import javax.sql.DataSource;

import com.distinctclinic.dao.ITagArticleDao;
import com.distinctclinic.model.TagArticle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;


@Repository
public class TagArticleDaoImpl extends NamedParameterJdbcDaoSupport implements
        ITagArticleDao, RowMapper<TagArticle> {

    public static final String QUERY_BY_ARTICLE_ID = "select * from tag_article where article_id = :articleId";

    public static final String QUERY_BY_IDS = "select * from tag_article where id in (:ids)";

    public static final String INSERT = "insert into tag_article (tag,article_id,other_topic) values (:tag,:article_id,:other_topic)";

    public static final String UPDATE = "update tag_article set tag=:tag,article_id=:article_id,other_topic=:other_topic where id=:id";

    public static final String DELETE_BY_DID = "delete from tag_article where article_id = :id";
    @Override
    public TagArticle mapRow(ResultSet rs, int rowNum) throws SQLException {
        TagArticle tagArticle = new TagArticle();
        tagArticle.setId(rs.getInt("id"));
        tagArticle.setTag(rs.getString("tag"));
        tagArticle.setArticleId(rs.getInt("article_id"));
        tagArticle.setOtherTopic(rs.getString("other_topic"));
        return tagArticle;

    }

    @Autowired
    public void initDataSource(DataSource dataSource) {
        setDataSource(dataSource);
    }

    @Override
    public List<TagArticle> getByArticleId(int articleId) {

        MapSqlParameterSource paramSource = new MapSqlParameterSource();
        paramSource.addValue("articleId", articleId);
        List<TagArticle> list = this.getNamedParameterJdbcTemplate().query(
                QUERY_BY_ARTICLE_ID, paramSource, this);

        return list;

    }

    @Override
    public TagArticle getById(int id) {

        MapSqlParameterSource paramSource = new MapSqlParameterSource();
        paramSource.addValue("id", id);
        List<TagArticle> list = this.getNamedParameterJdbcTemplate().query(
                "select * from tag_article where id=:id", paramSource, this);
        if(list!= null && list.size() >0){
            return list.get(0);
        }
        return null;

    }

    @Override
    public List<TagArticle> getByIds(List<Integer> ids) {
        if(ids==null || ids.size()==0){
            return Collections.emptyList();
        }

        MapSqlParameterSource paramSource = new MapSqlParameterSource();
        paramSource.addValue("ids", ids);
        List<TagArticle> list = this.getNamedParameterJdbcTemplate().query(
                QUERY_BY_IDS, paramSource, this);
        return list;

    }

    @Override
    public int insert(TagArticle tagArticle) {

        KeyHolder keyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource paramSource = new MapSqlParameterSource();
        paramSource.addValue("tag", tagArticle.getTag());
        paramSource.addValue("article_id", tagArticle.getArticleId());
        paramSource.addValue("other_topic", tagArticle.getOtherTopic());
        int rows = this.getNamedParameterJdbcTemplate().update(INSERT,
                paramSource,keyHolder);
        if (rows > 0) {
            return keyHolder.getKey().intValue();  //返回主键id
        } else {
            return 0;
        }

    }

    @Override
    public int update(TagArticle tagArticle) {
        MapSqlParameterSource paramSource = new MapSqlParameterSource();
        paramSource.addValue("id", tagArticle.getId());
        paramSource.addValue("tag", tagArticle.getTag());
        paramSource.addValue("article_id", tagArticle.getArticleId());
        paramSource.addValue("other_topic", tagArticle.getOtherTopic());
        return this.getNamedParameterJdbcTemplate().update(
                UPDATE, paramSource);

    }

    @Override
    public int deleteByArticleId(int id) {
        MapSqlParameterSource paramSource = new MapSqlParameterSource();
        paramSource.addValue("id", id);
        return this.getNamedParameterJdbcTemplate().update(
                DELETE_BY_DID, paramSource);
    }

    @Override
    public int deleteById(int id) {
        MapSqlParameterSource paramSource = new MapSqlParameterSource();
        paramSource.addValue("id", id);
        return this.getNamedParameterJdbcTemplate().update(
                "delete from tag_article where id=:id", paramSource);
    }

    @Override
    public List<TagArticle> getByTag(String tag) {
        MapSqlParameterSource paramSource = new MapSqlParameterSource();
        paramSource.addValue("tag", tag);
        return this.getNamedParameterJdbcTemplate().query(
                "select * from tag_article where tag=:tag", paramSource, this);
    }
}
