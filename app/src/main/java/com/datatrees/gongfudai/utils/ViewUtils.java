package com.datatrees.gongfudai.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static android.view.View.GONE;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

/**
 * Utilities for working with the {@link View} class
 */
public class ViewUtils {

    /**
     * bitmap转为base64
     *
     * @param bitmap
     * @return
     */
    public static String bitmapToBase64(Bitmap bitmap) {
        String result = null;
        ByteArrayOutputStream baos = null;
        try {
            if (bitmap != null) {
                baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                baos.flush();
                baos.close();

                byte[] bitmapBytes = baos.toByteArray();
                result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.flush();
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * base64转为bitmap
     *
     * @param base64Data
     * @return
     */
    public static Bitmap base64ToBitmap(String base64Data) {
        byte[] bytes = Base64.decode(base64Data, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    /**
     * Set visibility of given view to be gone or visible
     * <p/>
     * This method has no effect if the view visibility is currently invisible
     *
     * @param view
     * @param gone
     * @return view
     */
    public static <V extends View> V setGone(final V view, final boolean gone) {
        if (view != null)
            if (gone) {
                if (GONE != view.getVisibility())
                    view.setVisibility(GONE);
            } else {
                if (VISIBLE != view.getVisibility())
                    view.setVisibility(VISIBLE);
            }
        return view;
    }

    /**
     * Set visibility of given view to be invisible or visible
     * <p/>
     * This method has no effect if the view visibility is currently gone
     *
     * @param view
     * @param invisible
     * @return view
     */
    public static <V extends View> V setInvisible(final V view, final boolean invisible) {
        if (view != null)
            if (invisible) {
                if (INVISIBLE != view.getVisibility())
                    view.setVisibility(INVISIBLE);
            } else {
                if (VISIBLE != view.getVisibility())
                    view.setVisibility(VISIBLE);
            }
        return view;
    }

    private ViewUtils() {

    }

    /**
     * @param edit
     * @return
     */
    public static String getString(EditText edit) {
        if (edit == null) {
            throw new NullPointerException("EditText is null");
        }
        final String text = edit.getText().toString();
        if (TextUtils.isEmpty(text)) {
            return null;
        }
        return text;
    }

    /**
     * @param activity
     */
    public static void hideInputPanel(Activity activity) {
        try {
            // hide the input method panel.
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            View focus = activity.getCurrentFocus();
            if (focus != null)
                imm.hideSoftInputFromWindow(focus.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showInputPanel(Activity activity) {
        try {
            // hide the input method panel.
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            View focus = activity.getCurrentFocus();
            if (focus != null)
                imm.showSoftInput(focus, InputMethodManager.SHOW_FORCED);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
