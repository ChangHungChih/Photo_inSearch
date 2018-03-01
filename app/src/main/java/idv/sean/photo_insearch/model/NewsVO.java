package idv.sean.photo_insearch.model;

import java.io.Serializable;
import java.sql.Date;

public class NewsVO implements Serializable {
    private String news_id;     /*消息編號*/
    private String news_type;   /*消息類別*/
    private String title;       /*標題*/
    private String article;     /*內文*/
    private Date news_date;     /*發佈日期*/
    private byte[] news_pic;    /*消息圖片*/
    private String emp_id;      /*處理員工編號*/
    private String picBase64;   /*圖片傳輸用*/

    public NewsVO() {
    }

    public String getNews_id() {
        return news_id;
    }

    public void setNews_id(String news_id) {
        this.news_id = news_id;
    }

    public String getNews_type() {
        return news_type;
    }

    public void setNews_type(String news_type) {
        this.news_type = news_type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArticle() {
        return article;
    }

    public void setArticle(String article) {
        this.article = article;
    }

    public Date getNews_date() {
        return news_date;
    }

    public void setNews_date(Date news_date) {
        this.news_date = news_date;
    }

    public byte[] getNews_pic() {
        return news_pic;
    }

    public void setNews_pic(byte[] news_pic) {
        this.news_pic = news_pic;
    }

    public String getEmp_id() {
        return emp_id;
    }

    public void setEmp_id(String emp_id) {
        this.emp_id = emp_id;
    }

    public String getPicBase64() {
        return picBase64;
    }

    public void setPicBase64(String picBase64) {
        this.picBase64 = picBase64;
    }
}
