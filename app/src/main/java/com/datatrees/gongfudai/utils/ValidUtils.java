package com.datatrees.gongfudai.utils;

import android.text.TextUtils;
import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * valid
 * Created by zhangping on 15/8/15.
 */
public class ValidUtils {
    private static final String NUM_REG = "^[0-9]*$";
    private static final String PHONE_NUMBER_REG = "^((\\+{0,1}86){0,1})1[0-9]{10}";
    private static final String EMAIL_NUMBER_REG = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";

    public static boolean isValidNumber(String str) {
        if (TextUtils.isEmpty(str))
            return false;
        Pattern regex = Pattern.compile(NUM_REG);
        Matcher matcher = regex.matcher(str);
        return matcher.matches();
    }

    /**
     *
     * @param phoneNumber
     * @return
     */
    public static boolean isValidPhoneNumber(String phoneNumber) {
        if (TextUtils.isEmpty(phoneNumber))
            return false;
        Pattern regex = Pattern.compile(PHONE_NUMBER_REG);
        Matcher matcher = regex.matcher(phoneNumber);
        return matcher.matches();
    }

    /**
     *
     * @return
     */
    public static boolean isValidEmail(String email) {
        if (TextUtils.isEmpty(email))
            return false;
        Pattern regex = Pattern.compile(EMAIL_NUMBER_REG);
        Matcher matcher = regex.matcher(email);
        return matcher.matches();
    }

    /**
     *
     * @return
     */
    public static boolean isValidPass(String pass) {
        if (pass == null)
            return false;
        final int length = pass.length();
        if (length < 6 || length > 16)
            return false;
        return true;
    }

    /**
     *
     * @param frist
     * @param last
     * @return
     */
    public static boolean isSame(final String frist, final String last) {
        if (frist == null || last == null)
            return false;
        if (last.equals(frist))
            return true;
        return false;
    }

    /**
     *
     * @param edit
     * @return
     */
    public static boolean isValidIdCard(EditText edit) {
        if (edit == null)
            return false;
        final String text = edit.getText().toString();
        if (text.length() == 15 || text.length() == 18)
            return true;
        return false;
    }
}
