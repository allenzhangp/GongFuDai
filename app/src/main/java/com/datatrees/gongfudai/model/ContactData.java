package com.datatrees.gongfudai.model;

import org.json.JSONArray;

/**
 * Created by ucmed on 2015/8/1.
 */
public class ContactData {
    private String id;
    private String name;
    private String number;
    private String fn;
    private String mn;
    private String ln;
    private String type;
    private String ext;
    private String insDt;
    private String updDt;
    private JSONArray phoneArray;

    public ContactData() {
        id = "";
        name = "";
        number = "";
        fn = "";
        mn = "";
        ln = "";
        type = "";
        ext = "";
        insDt = "";
        updDt = "";
        phoneArray = new JSONArray();
    }

    public JSONArray getPhoneArray() {
        return phoneArray;
    }

    public void setPhoneArray(JSONArray phoneArray) {
        this.phoneArray = phoneArray;
    }

    public String getInsDt() {
        return insDt;
    }

    public void setInsDt(String insDt) {
        this.insDt = insDt;
    }

    public String getUpdDt() {
        return updDt;
    }

    public void setUpdDt(String updDt) {
        this.updDt = updDt;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    public String getFn() {
        return fn;
    }

    public void setFn(String fn) {
        this.fn = fn;
    }

    public String getLn() {
        return ln;
    }

    public void setLn(String ln) {
        this.ln = ln;
    }

    public String getMn() {
        return mn;
    }

    public void setMn(String mn) {
        this.mn = mn;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setId(String idValue) {
        id = idValue;
    }

    public void setContactName(String contactName) {
        name = contactName;
    }

    public void setNumber(String phoneNumber) {
        number = phoneNumber;
    }

    public String getId() {
        return id;
    }

    public String getContactName() {
        return name;
    }

    public String getNumber() {
        return number;
    }

    @Override
    public String toString() {
        return "contact_id:" + id + ",contact_name:" + name + ",contact_number:" + number + ",insDt:" + insDt + ",updDt:" + updDt + ",type:" + type;
    }
}
