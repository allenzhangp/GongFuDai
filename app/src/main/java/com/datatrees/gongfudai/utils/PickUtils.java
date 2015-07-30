package com.datatrees.gongfudai.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.net.Uri;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.CursorLoader;
import android.widget.Toast;

import com.zp.baseapp.R;

@SuppressLint("NewApi")
public class PickUtils {

	public static String getPath(final Context context, final Uri uri) {

		final boolean isKitKat = VersionUtils.hasKitkat();

		if (isKitKat && !DocumentsContract.isDocumentUri(context, uri)) {
			return getRealPathFromURI(context, uri);
		}

		// DocumentProvider
		if (isKitKat) {
			// ExternalStorageProvider
			if (isExternalStorageDocument(uri)) {
				final String docId = DocumentsContract.getDocumentId(uri);
				final String[] split = docId.split(":");
				final String type = split[0];

				if ("primary".equalsIgnoreCase(type)) {
					return Environment.getExternalStorageDirectory() + "/"
							+ split[1];
				}
			}
			// DownloadsProvider
			else if (isDownloadsDocument(uri)) {

				final String id = DocumentsContract.getDocumentId(uri);
				final Uri contentUri = ContentUris.withAppendedId(
						Uri.parse("content://downloads/public_downloads"),
						Long.valueOf(id));

				return getDataColumn(context, contentUri, null, null);
			}
			// MediaProvider
			else if (isMediaDocument(uri)) {
				final String docId = DocumentsContract.getDocumentId(uri);
				final String[] split = docId.split(":");
				final String type = split[0];

				Uri contentUri = null;
				if ("image".equals(type)) {
					contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
				} else if ("video".equals(type)) {
					contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
				} else if ("audio".equals(type)) {
					contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
				}

				final String selection = "_id=?";
				final String[] selectionArgs = new String[] { split[1] };

				return getDataColumn(context, contentUri, selection,
						selectionArgs);
			}
		}
		// MediaStore (and general)
		// or file
		else {
			return getRealPathFromURI(context, uri);
		}

		return null;
	}

	/**
	 * Get the value of the data column for this Uri. This is useful for
	 * MediaStore Uris, and other file-based ContentProviders.
	 * 
	 * @param context
	 *            The context.
	 * @param uri
	 *            The Uri to query.
	 * @param selection
	 *            (Optional) Filter used in the query.
	 * @param selectionArgs
	 *            (Optional) Selection arguments used in the query.
	 * @return The value of the _data column, which is typically a file path.
	 */
	public static String getDataColumn(Context context, Uri uri,
			String selection, String[] selectionArgs) {

		Cursor cursor = null;
		final String column = "_data";
		final String[] projection = { column };

		try {
			cursor = context.getContentResolver().query(uri, projection,
					selection, selectionArgs, null);
			if (cursor != null && cursor.moveToFirst()) {
				final int index = cursor.getColumnIndexOrThrow(column);
				return cursor.getString(index);
			}
		} finally {
			if (cursor != null)
				cursor.close();
		}
		return null;
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is ExternalStorageProvider.
	 */
	public static boolean isExternalStorageDocument(Uri uri) {
		return "com.android.externalstorage.documents".equals(uri
				.getAuthority());
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is DownloadsProvider.
	 */
	public static boolean isDownloadsDocument(Uri uri) {
		return "com.android.providers.downloads.documents".equals(uri
				.getAuthority());
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is MediaProvider.
	 */
	public static boolean isMediaDocument(Uri uri) {
		return "com.android.providers.media.documents".equals(uri
				.getAuthority());
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is Google Photos.
	 */
	public static boolean isGooglePhotosUri(Uri uri) {
		return "com.google.android.apps.photos.content".equals(uri
				.getAuthority());
	}

	/**
	 * 
	 * @param context
	 * @return
	 */
	public static Uri getLastRecordedVideoUri(Context context) {
		String[] proj = { MediaStore.Video.Media._ID };
		Uri contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
		String sortOrder = MediaStore.Video.VideoColumns.DATE_TAKEN + " DESC";
		CursorLoader loader = new android.support.v4.content.CursorLoader(
				context, contentUri, proj, null, null, sortOrder);
		Cursor cursor = loader.loadInBackground();
		cursor.moveToFirst();

		Uri uri = Uri.parse(contentUri.toString() + "/" + cursor.getLong(0));

		return uri;
	}

	/**
	 * 
	 * @param context
	 * @param contentUri
	 * @return
	 */
	public static String getRealPathFromContentURI(Context context,
			Uri contentUri) {
		if (contentUri == null)
			return null;

		String[] proj = { MediaStore.Images.Media.DATA };
		CursorLoader loader = new CursorLoader(context, contentUri, proj, null,
				null, null);
		Cursor cursor = loader.loadInBackground();

		if (cursor == null)
			return null;

		int column_index = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
		if (column_index == -1) {
			cursor.close();
			return null;
		}

		cursor.moveToFirst();
		String path = cursor.getString(column_index);
		cursor.close();
		return path;
	}

	/**
	 * 
	 * @param context
	 * @param uri
	 * @return
	 */
	public static String getRealPathFromURI(Context context, Uri uri) {
		String path;
		if ("content".equals(uri.getScheme())) {
			path = getRealPathFromContentURI(context, uri);
		} else if ("file".equals(uri.getScheme())) {
			path = uri.getPath();
		} else {
			path = uri.toString();
		}
		return path;
	}
	/**
	 * 
	 * @param a
	 * @param requestCode
	 */
	public static void pickImage(Activity a, int requestCode) {
		Intent intent = new Intent(Intent.ACTION_PICK);
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		a.startActivityForResult(
				Intent.createChooser(intent, a.getString(R.string.pick_photo)),
				requestCode);
	}
	/**
	 * 
	 * @param a
	 * @param requestCode
	 */
	public static void pickImage(Fragment a, int requestCode) {
		Intent intent = new Intent(Intent.ACTION_PICK);
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		a.startActivityForResult(
				Intent.createChooser(intent, a.getString(R.string.pick_photo)),
				requestCode);
	}
	/**
	 * 
	 * @param a
	 * @param requestCode
	 */
	public static void launchCamera(Activity a, int requestCode, PiclCallBackListenter l) {
        String state = android.os.Environment.getExternalStorageState();
        if (!state.equals(android.os.Environment.MEDIA_MOUNTED)) {
            Toast.makeText(a, R.string.pick_no_sdcard, Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = prepareLaunchCameraIntent(l);
            a.startActivityForResult(intent, requestCode);
        }
    }
	
	private static boolean valid(Context a){
		String state = android.os.Environment.getExternalStorageState();
        if (!state.equals(android.os.Environment.MEDIA_MOUNTED)) {
            Toast.makeText(a, R.string.pick_no_sdcard, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
	}
	
	
	public static String createNewFile(Context context){
		if(!valid(context)){
			return null;
		}
		File path = Environment
				.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);

		String mediaCapturePath = path + File.separator + "Camera"
				+ File.separator + "IMG-" + System.currentTimeMillis() + ".jpg";
		return mediaCapturePath;
	}
	
	/**
	 * 
	 * @return
	 */
	private static Intent prepareLaunchCameraIntent(PiclCallBackListenter l) {

		File path = Environment
				.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);

		String mediaCapturePath = path + File.separator + "Camera"
				+ File.separator + "IMG-" + System.currentTimeMillis() + ".jpg";
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT,
				Uri.fromFile(new File(mediaCapturePath)));
		if(l != null){
			l.setPath(mediaCapturePath);
		}

		// make sure the directory we plan to store the recording in exists
		File directory = new File(mediaCapturePath).getParentFile();
		if (!directory.exists() && !directory.mkdirs()) {
			try {
				throw new IOException("Path to file could not be created.");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return intent;
	}
	/**
	 * 
	 * @param a
	 * @param requestCode
	 */
	public static void  lauchVideo(Activity a, int requestCode){
		Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
		a.startActivityForResult(intent,
				requestCode);
	}
	/**
	 * 
	 * @param a
	 * @param requestCode
	 */
	public static void pickVideo(Activity a, int requestCode){
		Intent intent = new Intent(Intent.ACTION_PICK);
		intent.setType("video/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);

		a.startActivityForResult(
				Intent.createChooser(intent, a.getString(R.string.pick_video)),
				requestCode);
	}
	/**
	 * 
	 * @param url
	 * @return
	 */
    public static boolean isValidImage(String url) {
        if (url == null) 
            return false;
        
        if (url.endsWith(".png") || url.endsWith(".jpg") || url.endsWith(".jpeg"))
            return true;
        return false;
    }
    /**
     * 
     * @param url
     * @return
     */
    public static boolean isVideo(String url) {
        if (url == null)
            return false;
        if (url.endsWith(".mp4") || url.endsWith(".3gp"))
            return true;
        return false;
    }
    /**
     * 
     * @param url
     * @return
     */
    public static boolean isValidAudio(String url) {
        if (url == null) 
            return false;
        
        if (url.endsWith(".amr") || url.endsWith(".mp3") )
            return true;
        return false;
    }
    /**
     * 
     * @param activity
     * @return
     */
    public static Uri getLastRecordedVideoUri(Activity activity) {
        String[] proj = { MediaStore.Video.Media._ID };
        Uri contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        String sortOrder = MediaStore.Video.VideoColumns.DATE_TAKEN + " DESC";
        CursorLoader loader = new CursorLoader(activity, contentUri, proj, null, null, sortOrder);
        Cursor cursor = loader.loadInBackground();
        cursor.moveToFirst();
        
        Uri uri = Uri.parse(contentUri.toString() + "/" + cursor.getLong(0));
        
        return uri;
    }
    
    /**
	 * 
	 * @param a
	 * @param requestCode
	 */
	public static void pickAudio(Activity a, int requestCode) {
		Intent intent = new Intent(Intent.ACTION_PICK);
		intent.setType("audio/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		a.startActivityForResult(
				Intent.createChooser(intent, a.getString(R.string.pick_photo)),requestCode);
	}
	/**
	 * 
	 * @param reqWidth
	 * @param reqHeight
	 * @param options
	 * @throws IOException 
	 */
	public static Bitmap  calculateInSampleSize(String path, int reqWidth, int reqHeight) throws IOException {
		BitmapFactory.Options options = new Options();
		options.inJustDecodeBounds = true;
		FileInputStream is = new FileInputStream(path);  
		BitmapFactory.decodeFileDescriptor(is.getFD(), null, options);  
		final int height = options.outHeight;
		final int width = options.outWidth;
		int sampleSize = 1;
		if (height > reqHeight || width > reqWidth) {
			final int heightRatio = Math.round((float) height
					/ (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);
			sampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}

		options.inSampleSize = sampleSize;
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFileDescriptor(is.getFD(), null, options); 
	}
	
	public static interface PiclCallBackListenter{
		public void setPath(String path);
	}
}
