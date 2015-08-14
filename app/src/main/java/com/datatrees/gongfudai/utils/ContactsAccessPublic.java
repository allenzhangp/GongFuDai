package com.datatrees.gongfudai.utils;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.text.TextUtils;

import com.datatrees.gongfudai.model.ContactData;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ucmed on 2015/8/1.
 */
public class ContactsAccessPublic {

    public static List<ContactData> getContactsAll(Context mContext, List<ContactData> content) {
        if (content == null)
            content = new ArrayList<>();
        content.addAll(getContacts(mContext));
        content.addAll(getSIMContacts(mContext));
        return content;
    }

    /**
     * 得到手机通讯录联系人信息*
     */
    public static List<ContactData> getPhoneContacts(Context mContext) {
        ArrayList<ContactData> contactDatas = new ArrayList<>();
        ContentResolver resolver = mContext.getContentResolver();
        // 获取手机联系人
        Cursor phoneCursor = resolver.query(Phone.CONTENT_URI, ContactsQuery.PHONES_PROJECTION, null, null, null);
        if (phoneCursor != null) {
            ContactData contactData;
            while (phoneCursor.moveToNext()) {
                //得到手机号码
                String phoneNumber = phoneCursor.getString(ContactsQuery.PHONES_NUMBER_INDEX);
                //当手机号码为空的或者为空字段 跳过当前循环
                if (TextUtils.isEmpty(phoneNumber))
                    continue;
                //得到联系人名称
                String contactName = phoneCursor.getString(ContactsQuery.PHONES_DISPLAY_NAME_INDEX);
                String contactId = phoneCursor.getString(ContactsQuery.PHONES_CONTACT_ID);
                String phoneTypeName = mContext.getResources().getString(ContactsContract.CommonDataKinds.Phone.getTypeLabelResource(phoneCursor.getInt(ContactsQuery.PHONES_TYPE)));

                contactData = new ContactData();
                contactData.setFn(contactName);
                contactData.setNumber(phoneNumber);
                contactData.setContactName(contactName);
                contactData.setId(contactId);
                contactData.setType(phoneTypeName);
                LogUtil.i("TAG", contactData.toString());
                contactDatas.add(contactData);
            }
            phoneCursor.close();
        }

        return contactDatas;
    }

    /**
     * 得到手机SIM卡联系人人信息*
     */
    public static List<ContactData> getSIMContacts(Context mContext) {
        ArrayList<ContactData> contactDatas = new ArrayList<>();
        ContentResolver resolver = mContext.getContentResolver();
        // 获取Sims卡联系人
        Uri uri = Uri.parse("content://icc/adn");
        Cursor phoneCursor = resolver.query(uri, ContactsQuery.PHONES_PROJECTION, null, null,
                null);
        if (phoneCursor != null) {
            ContactData contactData;
            while (phoneCursor.moveToNext()) {

                // 得到手机号码
                String phoneNumber = phoneCursor.getString(ContactsQuery.PHONES_NUMBER_INDEX);
                // 当手机号码为空的或者为空字段 跳过当前循环
                if (TextUtils.isEmpty(phoneNumber))
                    continue;
                // 得到联系人名称
                String contactName = phoneCursor
                        .getString(ContactsQuery.PHONES_DISPLAY_NAME_INDEX);
                String contactId = phoneCursor.getString(ContactsQuery.PHONES_CONTACT_ID);


                JSONObject phontObj = new JSONObject();

                contactData = new ContactData();
                try {
                    contactData.setNumber(phoneNumber);
                    contactData.setContactName(contactName);
                    contactData.setId(contactId);
                    phontObj.put("type", "");
                    phontObj.put("phone", phoneNumber);
                    contactData.getPhoneArray().put(phontObj);
                } catch (JSONException e) {
                }

                LogUtil.i("TAG", contactData.toString());
                contactDatas.add(contactData);
            }
            phoneCursor.close();
        }
        return contactDatas;
    }

    /**
     * 返回特定联系人的手机
     *
     * @param context
     * @param contactData
     * @return
     */
    public static List<ContactData> getPersonPhone(Context context, Uri contactData) {
        List<ContactData> data = new ArrayList<>();

        ContentResolver resolver = context.getContentResolver();
        Cursor c = resolver.query(contactData, ContactsQuery.PROJECTION, null, null, null);
        c.moveToFirst();

        int phoneColumn = c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER);
        int phoneNum = c.getInt(phoneColumn);
        if (phoneNum > 0) {
            //取得联系人的名字索引
            String contact = c.getString(ContactsQuery.DISPLAY_NAME);
            //取得联系人的ID索引值
            String contactId = c.getString(ContactsQuery.ID);
            Cursor phone = resolver.query(Phone.CONTENT_URI, ContactsQuery.PHONES_NUMBER_PROJECTION, ContactsQuery.PHONES_NUMBER_SELECTION, new String[]{contactId}, null);
            ContactData item = null;
            //一个人可能有几个号码
            while (phone.moveToNext()) {
                item = new ContactData();
                String strPhoneNumber = phone
                        .getString(ContactsQuery.PHONE_NUMBER);
                item.setContactName(contact);
                item.setNumber(strPhoneNumber);
                data.add(item);
            }
            phone.close();
        }
        return data;
    }

    public static List<ContactData> getContacts(Context context) {
        List<ContactData> datas = new ArrayList<>();
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(ContactsQuery.CONTENT_URI, ContactsQuery.PROJECTION,
                ContactsQuery.SELECTION,
                null,
                ContactsQuery.SORT_ORDER);
        while (cursor.moveToNext()) {
            ContactData contactData = new ContactData();

            //取得联系人的名字索引
            String contact = cursor.getString(ContactsQuery.DISPLAY_NAME);
            //取得联系人的ID索引值
            String contactId = cursor.getString(ContactsQuery.ID);
            Cursor phone = resolver.query(Phone.CONTENT_URI, ContactsQuery.PHONES_NUMBER_PROJECTION, ContactsQuery.PHONES_NUMBER_SELECTION, new String[]{contactId}, null);

            contactData.setId(contactId);
            contactData.setContactName(contact);
            contactData.setFn(contact);
            //一个人可能有几个号码
            while (phone.moveToNext()) {
                try {
                    JSONObject jsonObject = new JSONObject();
                    String strPhoneNumber = phone
                            .getString(ContactsQuery.PHONE_NUMBER);
                    int type = phone.getInt(2);
                    String phoneLabel = context.getResources().getString(ContactsContract.CommonDataKinds.Phone.getTypeLabelResource(type));
                    jsonObject.put("phone", strPhoneNumber);
                    jsonObject.put("type", phoneLabel);
                    contactData.getPhoneArray().put(jsonObject);
                } catch (JSONException e) {
                }
            }
            phone.close();
            datas.add(contactData);
        }
        cursor.close();
        return datas;
    }


    public interface ContactsQuery {

        String[] PHONES_NUMBER_PROJECTION = new String[]{Phone.NUMBER, VersionUtils.hasHoneycomb() ? Phone.DISPLAY_NAME_PRIMARY : Phone.DISPLAY_NAME, Phone.TYPE};
        String PHONES_NUMBER_SELECTION = Phone.CONTACT_ID + "= ?";

        /**
         * 获取库Phon表字段*
         */
        String[] PHONES_PROJECTION = new String[]{
                VersionUtils.hasHoneycomb() ? Phone.DISPLAY_NAME_PRIMARY : Phone.DISPLAY_NAME, Phone.NUMBER, Phone.PHOTO_ID, Phone.CONTACT_ID, Phone.TYPE, Phone.TIMES_CONTACTED, Phone.LAST_TIME_CONTACTED};

        /**
         * 联系人显示名称*
         */
        int PHONES_DISPLAY_NAME_INDEX = 0;

        /**
         * 电话号码*
         */
        int PHONES_NUMBER_INDEX = 1;

        /**
         * contact id
         */
        int PHONES_CONTACT_ID = 3;


        int PHONES_TYPE = 4;

        int PHONES_TIMES_CONTACTED = 5;

        int PHOENS_LAST_TIME_CONTACTED = 6;

        // A content URI for the Contacts table
        Uri CONTENT_URI = ContactsContract.Contacts.CONTENT_URI;

        // The selection clause for the CursorLoader query. The search criteria defined here
        // restrict results to contacts that have a display name and are linked to visible groups.
        // Notice that the search on the string provided by the user is implemented by appending
        // the search string to CONTENT_FILTER_URI.
        @SuppressLint("InlinedApi")
        String SELECTION =
                (VersionUtils.hasHoneycomb() ? ContactsContract.Contacts.DISPLAY_NAME_PRIMARY : ContactsContract.Contacts.DISPLAY_NAME) +
                        "<>''" + " AND " + ContactsContract.Contacts.IN_VISIBLE_GROUP + "=1";

        // The desired sort order for the returned Cursor. In Android 3.0 and later, the primary
        // sort key allows for localization. In earlier versions. use the display name as the sort
        // key.
        @SuppressLint("InlinedApi")
        String SORT_ORDER =
                VersionUtils.hasHoneycomb() ? ContactsContract.Contacts.SORT_KEY_PRIMARY : ContactsContract.Contacts.DISPLAY_NAME;

        // The projection for the CursorLoader query. This is a list of columns that the Contacts
        // Provider should return in the Cursor.
        @SuppressLint("InlinedApi")
        String[] PROJECTION = {

                // The contact's row id
                ContactsContract.Contacts._ID,

                // A pointer to the contact that is guaranteed to be more permanent than _ID. Given
                // a contact's current _ID value and LOOKUP_KEY, the Contacts Provider can generate
                // a "permanent" contact URI.
                ContactsContract.Contacts.LOOKUP_KEY,

                // In platform version 3.0 and later, the Contacts table contains
                // DISPLAY_NAME_PRIMARY, which either contains the contact's displayable name or
                // some other useful identifier such as an email address. This column isn't
                // available in earlier versions of Android, so you must use Contacts.DISPLAY_NAME
                // instead.
                VersionUtils.hasHoneycomb() ? ContactsContract.Contacts.DISPLAY_NAME_PRIMARY : ContactsContract.Contacts.DISPLAY_NAME,

                // In Android 3.0 and later, the thumbnail image is pointed to by
                // PHOTO_THUMBNAIL_URI. In earlier versions, there is no direct pointer; instead,
                // you generate the pointer from the contact's ID value and constants defined in
                // android.provider.ContactsContract.Contacts.
                VersionUtils.hasHoneycomb() ? ContactsContract.Contacts.PHOTO_THUMBNAIL_URI : ContactsContract.Contacts._ID,

                // The sort order column for the returned Cursor, used by the AlphabetIndexer
                SORT_ORDER,
                ContactsContract.Contacts.HAS_PHONE_NUMBER,
                ContactsContract.Contacts.TIMES_CONTACTED,
                ContactsContract.Contacts.LAST_TIME_CONTACTED,
        };

        // The query column numbers which map to each value in the projection
        int ID = 0;
        int DISPLAY_NAME = 2;
        int PHONE_NUMBER = 0;
        int PHONE_NAME = 1;
    }
}
