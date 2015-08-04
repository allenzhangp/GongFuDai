package com.datatrees.gongfudai.model;

/**
 * Created by ucmed on 2015/8/1.
 */
public class ContactData {
    private String id;
    private String name;
    private String number;

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
        return "contact_id:" + id + ",contact_name:" + name + ",contact_number:" + number;
    }
}
