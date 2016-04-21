package com.distinctclinic.model;

public class TagArticle{
     int id;//
     String tag;//标签名
     int articleId;//
     String otherTopic;//

     public void setId(int id){
     this.id=id;
   }

     public int getId(){
     return this.id;
   }

     public void setTag(String tag){
     this.tag=tag;
   }

     public String getTag(){
     return this.tag;
   }

     public void setArticleId(int articleId){
     this.articleId=articleId;
   }

     public int getArticleId(){
     return this.articleId;
   }

     public void setOtherTopic(String otherTopic){
     this.otherTopic=otherTopic;
   }

     public String getOtherTopic(){
     return this.otherTopic;
   }

}