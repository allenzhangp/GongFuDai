package com.datatrees.gongfudai.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.Photo;
import android.provider.ContactsContract.PhoneLookup;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by ucmed on 2015/8/1.
 */
public class ContactsAccessPublic {
    /**
     * 获取库Phon表字段*
     */
    private static final String[] PHONES_PROJECTION = new String[]{
            Phone.DISPLAY_NAME, Phone.NUMBER, Photo.PHOTO_ID, Phone.CONTACT_ID};

    /**
     * 联系人显示名称*
     */
    private static final int PHONES_DISPLAY_NAME_INDEX = 0;

    /**
     * 电话号码*
     */
    private static final int PHONES_NUMBER_INDEX = 1;

    public static void getPhoneContacts(Context context) {
        String str = "";
        // 得到contentresolver对象
        ContentResolver cr = context.getContentResolver();
        // 取得电话本中开始一项的光标，必须先moveToNext()
        Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null,
                null, null, null);
        while (cursor.moveToNext()) {
            //取得联系人的名字索引
            int nameIndex = cursor.getColumnIndex(PhoneLookup.DISPLAY_NAME);
            String contact = cursor.getString(nameIndex);
            str += (contact + ":" + "/n");
            //取得联系人的ID索引值
            String contactId = cursor.getString(cursor
                    .getColumnIndex(ContactsContract.Contacts._ID));
            Cursor phone = cr.query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "="
                            + contactId, null, null);

            //一个人可能有几个号码
            while (phone.moveToNext()) {
                String strPhoneNumber = phone
                        .getString(phone
                                .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                str += (strPhoneNumber + "/n");
                Log.i("TAG", "phoneNumber:" + strPhoneNumber + "contact" + contact);
            }
            phone.close();
        }
        cursor.close();
    }

    /**
     * 得到手机通讯录联系人信息*
     */
    public static void getPhoneContacts2(Context mContext) {
        ContentResolver resolver = mContext.getContentResolver();
        /**联系人名称**/
        ArrayList<String> mContactsName = new ArrayList<String>();
        // 获取手机联系人
        Cursor phoneCursor = resolver.query(Phone.CONTENT_URI, PHONES_PROJECTION, null, null, null);
        if (phoneCursor != null) {
            while (phoneCursor.moveToNext()) {
                //得到手机号码
                String phoneNumber = phoneCursor.getString(PHONES_NUMBER_INDEX);
                //当手机号码为空的或者为空字段 跳过当前循环
                if (TextUtils.isEmpty(phoneNumber))
                    continue;
                //得到联系人名称
                String contactName = phoneCursor.getString(PHONES_DISPLAY_NAME_INDEX);


                mContactsName.add(contactName + phoneNumber);
            }

            phoneCursor.close();
        }
    }

    /**
     * 得到手机SIM卡联系人人信息*
     */
    public static void getSIMContacts(Context mContext) {
        ContentResolver resolver = mContext.getContentResolver();
        ArrayList<String> mContactsName = new ArrayList<String>();
        // 获取Sims卡联系人
        Uri uri = Uri.parse("content://icc/adn");
        Cursor phoneCursor = resolver.query(uri, PHONES_PROJECTION, null, null,
                null);

        if (phoneCursor != null) {
            while (phoneCursor.moveToNext()) {
                // 得到手机号码
                String phoneNumber = phoneCursor.getString(PHONES_NUMBER_INDEX);
                // 当手机号码为空的或者为空字段 跳过当前循环
                if (TextUtils.isEmpty(phoneNumber))
                    continue;
                // 得到联系人名称
                String contactName = phoneCursor
                        .getString(PHONES_DISPLAY_NAME_INDEX);

                //Sim卡中没有联系人头像
                mContactsName.add(contactName + phoneNumber);
            }
            phoneCursor.close();
        }
    }
}
