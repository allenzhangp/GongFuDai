package com.datatrees.gongfudai.net;

import android.widget.ImageView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader.ImageContainer;
import com.android.volley.toolbox.ImageLoader.ImageListener;
/**
 * ImageListenerFactory
 * @author Serena Lee
 *
 */
public class ImageListenerFactory{
	
	public static ImageListener getImageListener(final ImageView view,
            final int defaultImageResId, final int errorImageResId) {
        return new ImageListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (errorImageResId != 0) {
                    view.setImageResource(errorImageResId);
                }
            }

            @Override
            public void onResponse(ImageContainer response, boolean isImmediate) {
                if (response.getBitmap() != null) {
                	
                    if(view.getTag().toString() == response.getRequestUrl()){
                    	view.setImageBitmap(response.getBitmap());
                    }
                } else if (defaultImageResId != 0) {
                    view.setImageResource(defaultImageResId);
                }
            }
        };
    }
}