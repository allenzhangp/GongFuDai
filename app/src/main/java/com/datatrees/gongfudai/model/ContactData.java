package com.datatrees.gongfudai.model;

/**
 * Created by ucmed on 2015/8/1.
 */
public class ContactData {
    public String id;
    public String name;
    public String number;

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
}
