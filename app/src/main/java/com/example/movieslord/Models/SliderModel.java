package com.example.movieslord.Models;

public class SliderModel {
    private String Stitle;
    private String Simgurl;
    private String Svdurl;
    public SliderModel() {
    }

    public SliderModel(String stitle, String simgurl, String svdurl) {
        Stitle = stitle;
        Simgurl = simgurl;
        Svdurl = svdurl;
    }




    public String getStitle() {
        return Stitle;
    }

    public void setStitle(String stitle) {
        Stitle = stitle;
    }

    public String getSimgurl() {
        return Simgurl;
    }

    public void setSimgurl(String simgurl) {
        Simgurl = simgurl;
    }

    public String getSvdurl() {
        return Svdurl;
    }

    public void setSvdurl(String svdurl) {
        Svdurl = svdurl;
    }
}
