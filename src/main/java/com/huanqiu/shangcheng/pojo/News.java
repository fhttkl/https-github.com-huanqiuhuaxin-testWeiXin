package com.huanqiu.shangcheng.pojo;

public class News {
    private String Title;//消息的标题
    private String Description;//消息的描述
    private String PicUrl;//引用的图片的url
    private String Url;//跳转地址

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getPicUrl() {
        return PicUrl;
    }

    public void setPicUrl(String picUrl) {
        PicUrl = picUrl;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }
}
