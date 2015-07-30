package com.zp.baseapp.net.cache;

import com.android.volley.toolbox.ImageLoader.ImageCache;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.util.Log;
/**
 * BitmapLruCache
 * @author Serena Lee
 *
 */
public class BitmapLruCache extends LruCache<String, Bitmap> implements ImageCache {
	private static final String TAG = "BitmapLruCache";

	private BitmapSoftRefCache softRefCache;

	public BitmapLruCache(int maxSize) {
		super(maxSize);
		softRefCache = new BitmapSoftRefCache();
	}

	@Override
	protected int sizeOf(String key, Bitmap value) {
		return value.getRowBytes() * value.getHeight();
	}

	@Override
	protected void entryRemoved(boolean evicted, String key, Bitmap oldValue, Bitmap newValue) {
		if (evicted) {
			softRefCache.putBitmap(key, oldValue);
		}
	}

	@Override
	public Bitmap getBitmap(String url) {
		Bitmap bitmap = get(url);
		if (bitmap == null) {
			bitmap = softRefCache.getBitmap(url);
		} else {
			Log.i(TAG, "LruCache has url:" + url);
		}
		return bitmap;
	}

	@Override
	public void putBitmap(String url, Bitmap bitmap) {
		put(url, bitmap);
	}

}