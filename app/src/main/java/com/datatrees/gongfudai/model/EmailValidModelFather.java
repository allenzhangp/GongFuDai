package com.datatrees.gongfudai.model;

import java.util.ArrayList;

/**
 * Created by zhangping on 15/8/13.
 */
public class EmailValidModelFather {
    public String key;
    public String title;
    public String image;
    public String startUrl;
    public String endUrl;
    public String css;
    public String website;
    public boolean usePCUA;
    //运营商专用
    public ArrayList<EmailValidModel> operatorlist = new ArrayList<>();

}
