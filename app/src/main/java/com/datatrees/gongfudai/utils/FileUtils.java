package com.datatrees.gongfudai.utils;

import java.io.File;

import android.net.Uri;
import android.os.Environment;

import com.zp.baseapp.App;

/**
 * Environment.getDataDirectory() = /data
 * Environment.getDownloadCacheDirectory() = /cache
 * Environment.getExternalStorageDirectory() = /mnt/sdcard
 * Environment.getExternalStoragePublicDirectory(¡°test¡±) = /mnt/sdcard/test
 * Environment.getRootDirectory() = /system getPackageCodePath() =
 * /data/app/com.my.app-1.apk getPackageResourcePath() =
 * /data/app/com.my.app-1.apk getCacheDir() = /data/data/com.my.app/cache
 * getDatabasePath(¡°test¡±) = /data/data/com.my.app/databases/test
 * getDir(¡°test¡±, Context.MODE_PRIVATE) = /data/data/com.my.app/app_test
 * getExternalCacheDir() = /mnt/sdcard/Android/data/com.my.app/cache
 * getExternalFilesDir(¡°test¡±) =
 * /mnt/sdcard/Android/data/com.my.app/files/test getExternalFilesDir(null) =
 * /mnt/sdcard/Android/data/com.my.app/files getFilesDir() =
 * /data/data/com.my.app/files
 */

public class FileUtils {
	public static final String CROP_CACHE_FILE_NAME = "crop_cache_file.jpg";

	public static Uri buildUri() {
		return Uri.fromFile(getCacheDir())
				.buildUpon().appendPath(CROP_CACHE_FILE_NAME).build();
	}

	/**
	 * /mnt/sdcard
	 * 
	 * @return
	 */
	public static String getExternalDir() {
		String filePath = null;
		if (hasExtStorState()) {
			filePath = Environment.getExternalStorageDirectory().getPath();
		}
		return filePath;
	}

	/**
	 * 
	 * @return
	 */
	public static File getExtImageFilesDir() {
		File file = null;
		if (hasExtStorState()) {
			file = App.getContext().getExternalFilesDir("images");
		} else {
			file = App.getContext().getFilesDir();
		}
		return file;
	}

	/**
	 * 
	 * @return
	 */
	public static File getCacheDir() {
		File file = null;
		if (hasExtStorState()) {
			file = App.getContext().getExternalCacheDir();
		} else {
			file = App.getContext().getCacheDir();
		}
		return file;
	}

	public static boolean hasExtStorState() {
		return Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState());
	}
}
