/*
 * Copyright (C) 2012 Lorenzo Villani.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.camera;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.widget.Toast;

/**
 * By default the following features are enabled, unless you override them by calling setters in the
 * builder:
 * 
 * <ul>
 * <li>Scale;</li>
 * <li>Scale up (if needed);</li>
 * <li>Face detection;</li>
 * </ul>
 * 
 * @since 1.0.1
 */
// TODO: Circle Crop
// TODO: Set Wallpaper
public class Crop {
	public static final int REQUEST_CROP = 10;
    public static final int REQUEST_PICK = 11;
	
    private static final String EXTRA_ASPECT_X = "aspectX";
    private static final String EXTRA_ASPECT_Y = "aspectY";
    private static final String EXTRA_OUTPUT_X = "outputX";
    private static final String EXTRA_OUTPUT_Y = "outputY";
    private static final String EXTRA_BITMAP_DATA = "data";
    private static final String EXTRA_SCALE = "scale";
    private static final String EXTRA_SCALE_UP_IF_NEEDED = "scaleUpIfNeeded";
    @SuppressWarnings("unused")
	private static final String EXTRA_NO_FACE_DETECTION = "noFaceDetection";
   
    
    private Intent cropIntent;
    
    
    /**
     * Create a crop Intent builder with source image
     *
     * @param source Source image URI
     */
    public Crop(Uri source) {
        cropIntent = new Intent();
        cropIntent.setData(source);
    }

    /**
     * Set output URI where the cropped image will be saved
     *
     * @param output Output image URI
     */
    public Crop output(Uri output) {
        cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, output);
        return this;
    }

    /**
     * Request that the Bitmap data is sent in the return Intent
     *
     * Note: This is only useful for debugging or if you have a small max crop size. Trying to
     * return large Bitmaps will fail. The image will not be written to the output URI in this case.
     */
    public Crop returnBitmap() {
        cropIntent.putExtra(EXTRA_BITMAP_DATA, true);
        return this;
    }

    /**
     * Set fixed aspect ratio for crop area
     *
     * @param x Aspect X
     * @param y Aspect Y
     */
    public Crop withAspect(int x, int y) {
        cropIntent.putExtra(EXTRA_ASPECT_X, x);
        cropIntent.putExtra(EXTRA_ASPECT_Y, y);
        return this;
    }

    /**
     * Crop area with fixed 1:1 aspect ratio
     */
    public Crop asSquare() {
        cropIntent.putExtra(EXTRA_ASPECT_X, 1);
        cropIntent.putExtra(EXTRA_ASPECT_Y, 1);
        return this;
    }
    
    /**
     * 
     * @param scale
     * @return
     */
    public Crop asScale(boolean scale){
    	cropIntent.putExtra(EXTRA_SCALE, scale);
    	return this;
    }
    
    /**
     * @param scale
     * @return
     */
    public Crop asScaleUp(boolean scale){
    	cropIntent.putExtra(EXTRA_SCALE_UP_IF_NEEDED, scale);
    	return this;
    }

    /**
     * Set maximum crop size
     *
     * @param width Max width
     * @param height Max height
     */
    public Crop withMazSize(int width, int height) {
        cropIntent.putExtra(EXTRA_OUTPUT_X, width);
        cropIntent.putExtra(EXTRA_OUTPUT_Y, height);
        return this;
    }
    
    /**
     * Send the crop Intent!
     *
     * @param activity Activity that will receive result
     */
    public void start(Activity activity) {
        cropIntent.setClass(activity, CropImage.class);
        activity.startActivityForResult(cropIntent, REQUEST_CROP);
    }
    
    public void start(Activity activity,Fragment f) {
        cropIntent.setClass(activity, CropImage.class);
        f.startActivityForResult(cropIntent, REQUEST_CROP);
    }

    /**
     *  Utility method that starts an image picker. Often preceded a crop.
     *
     * @param activity Activity that will receive result
     */
    public static void pickImage(Activity activity) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT).setType("image/*");
        try {
            activity.startActivityForResult(intent, REQUEST_PICK);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(activity, R.string.error_pick_image, Toast.LENGTH_SHORT).show();
        }
    }
}
