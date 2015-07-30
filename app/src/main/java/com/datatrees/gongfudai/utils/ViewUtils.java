package com.datatrees.gongfudai.utils;

import static android.view.View.GONE;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * Utilities for working with the {@link View} class
 */
public class ViewUtils {

	/**
	 * Set visibility of given view to be gone or visible
	 * <p>
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
	 * <p>
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
	 * 
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
	 * 
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
				imm.showSoftInput(focus,InputMethodManager.SHOW_FORCED); 
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
