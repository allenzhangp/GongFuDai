package com.datatrees.gongfudai.net;

import android.app.ActivityManager;
import android.content.Context;
import android.widget.ImageView;

import com.datatrees.gongfudai.net.cache.BitmapLruCache;
import com.datatrees.gongfudai.utils.LogUtil;
import com.datatrees.gongfudai.volley.Request;
import com.datatrees.gongfudai.volley.RequestQueue;
import com.datatrees.gongfudai.volley.toolbox.ImageLoader;
import com.datatrees.gongfudai.volley.toolbox.Volley;


/**
 * VolleyUtil
 * @author Serena Lee
 *
 */
public class VolleyUtil {

	private static final String TAG = "Volley";

	private static VolleyUtil instance;
	private static RequestQueue mRequestQueue;
	private static ImageLoader mImageLoader;
	private final static int RATE = 8; // 默认分配最大空间的几分之一

	private VolleyUtil(Context context) {
		mRequestQueue = Volley.newRequestQueue(context);

		// 确定在LruCache中，分配缓存空间大小,默认程序分配最大空间的 1/8
		ActivityManager manager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		int maxSize = manager.getMemoryClass() / RATE; // 比如 64M/8,单位为M

		// BitmapLruCache自定义缓存class，android本身支持二级缓存，在BitmapLruCache封装一个软引用缓存
		mImageLoader = new ImageLoader(mRequestQueue, new BitmapLruCache(
				1024 * 1024 * maxSize));

		LogUtil.i(TAG, "Volley初始化完成");
	}

	/**
	 * 初始化Volley相关对象，在使用Volley前应该完成初始化
	 * 
	 * @param context
	 */
	public static void init(Context context) {
		if (instance == null) {
			instance = new VolleyUtil(context);
		}
	}

	/**
	 * 得到请求队列对象
	 * 
	 * @return
	 */
	public static RequestQueue getRequestQueue() {
		throwIfNotInit();
		return mRequestQueue;
	}

	/**
	 * 得到ImageLoader对象
	 * 
	 * @return
	 */
	public static ImageLoader getImageLoader() {
		throwIfNotInit();
		return mImageLoader;
	}

	public static void addRequest(Request<?> request) {
		getRequestQueue().add(request);
	}

	public static void getImage(String requestUrl, ImageView imageView) {
		getImage(requestUrl, imageView, 0, 0);
	}

	public static void getImage(String requestUrl, ImageView imageView,
			int defaultImageResId, int errorImageResId) {
		getImage(requestUrl, imageView, defaultImageResId, errorImageResId, 0,
				0);
	}

	public static void getImage(String requestUrl, ImageView imageView,
			int defaultImageResId, int errorImageResId, int maxWidth,
			int maxHeight) {
		imageView.setTag(requestUrl);
		mImageLoader.get(requestUrl, ImageListenerFactory.getImageListener(
				imageView, defaultImageResId, errorImageResId), maxWidth,
				maxHeight);
	}

	/**
	 * 检查是否完成初始化
	 */
	private static void throwIfNotInit() {
		if (instance == null) {// 尚未初始化
			throw new IllegalStateException("Volley尚未初始化，在使用前应该执行init()");
		}
	}
}
