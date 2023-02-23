package com.example.movieslord.Models;

import java.io.Serializable;

public class MoviesModel implements Serializable {
    private String title;
    private String imgurl;
    private String vdurl;
    private String detail;
    private String id;
    private long lastPosition;

    public MoviesModel() {
    }




    public MoviesModel(String title, String imgurl, String vdurl, String detail) {
        this.title = title;
        this.imgurl = imgurl;
        this.vdurl = vdurl;
        this.detail = detail;
    }

    public MoviesModel(String title, String imgurl, String vdurl, String detail, String id) {
        this.title = title;
        this.imgurl = imgurl;
        this.vdurl = vdurl;
        this.detail = detail;
        this.id = id;
    }

    public MoviesModel(String title, String imgurl, String vdurl, String detail, String id, long lastPosition) {
        this.title = title;
        this.imgurl = imgurl;
        this.vdurl = vdurl;
        this.detail = detail;
        this.id = id;
        this.lastPosition = lastPosition;
    }

    public long getLastPosition() {
        return lastPosition;
    }

    public void setLastPosition(long lastPosition) {
        this.lastPosition = lastPosition;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public String getVdurl() {
        return vdurl;
    }

    public void setVdurl(String vdurl) {
        this.vdurl = vdurl;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}
