package com.zp.baseapp.net.cache;

import java.lang.ref.SoftReference;
import java.util.LinkedHashMap;

import android.graphics.Bitmap;
import com.android.volley.toolbox.ImageLoader.ImageCache;
/**
 * BitmapSoftRefCache
 * @author Serena Lee
 *
 */
public class BitmapSoftRefCache implements ImageCache{
	
	private LinkedHashMap<String, SoftReference<Bitmap>> map;
	public BitmapSoftRefCache() {
		map = new LinkedHashMap<String, SoftReference<Bitmap>>();
	}

	@Override
	public Bitmap getBitmap(String url) {
		Bitmap bitmap = null;
		SoftReference<Bitmap> softRef = map.get(url);
		if(softRef != null){
			bitmap = softRef.get();
			if(bitmap == null){
				map.remove(url);
			}
		}
		return bitmap;
	}

	@Override
	public void putBitmap(String url, Bitmap bitmap) {
		SoftReference<Bitmap> softRef = new SoftReference<Bitmap>(bitmap);
		map.put(url, softRef);
	}

}