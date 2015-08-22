package com.datatrees.camera;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * PhotoPreActivity
 * Created by zhangping on 15/8/18.
 */
public class PhotoPreActivity extends Activity implements View.OnClickListener {

    ImageView image_photo;

    String imagePath;

    public static final String IMAGE_PATH = "image_path";
    public static final String CAMER_APOSITION = "camera_position";

    int type;
    int cameraPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photopre);
        image_photo = (ImageView) findViewById(R.id.image_photo);
        findViewById(R.id.btn_camera_cancle).setOnClickListener(this);
        findViewById(R.id.btn_camera_ok).setOnClickListener(this);
        imagePath = getIntent().getStringExtra(IMAGE_PATH);
        cameraPosition = getIntent().getIntExtra(CAMER_APOSITION, 0);
        type = getIntent().getIntExtra(CameraActivity.IDCARD_TYPE, 0);
        if (imagePath == null || imagePath.trim().equals(""))
            this.finish();
        setImage();
    }

    private void setImage() {
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(imagePath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Bitmap realImage;
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 5;
        options.inPurgeable = true;                   //Tell to gc that whether it needs free memory, the Bitmap can be cleared
        options.inInputShareable = true;              //Which kind of reference will be used to recover the Bitmap data after being clear, when it will be used in the future
        realImage = BitmapFactory.decodeFile(imagePath, options);
        try {
            if (exif.getAttribute(ExifInterface.TAG_ORIENTATION)
                    .equalsIgnoreCase("1")) {
                if (cameraPosition == 1) {
                    realImage = rotate(realImage, 90);
                } else {
                    realImage = rotate(realImage, -90);
                }
            } else if (exif.getAttribute(ExifInterface.TAG_ORIENTATION)
                    .equalsIgnoreCase("8")) {
                realImage = rotate(realImage, 90);
            } else if (exif.getAttribute(ExifInterface.TAG_ORIENTATION)
                    .equalsIgnoreCase("3")) {
                realImage = rotate(realImage, 90);
            } else if (exif.getAttribute(ExifInterface.TAG_ORIENTATION)
                    .equalsIgnoreCase("0")) {
                realImage = rotate(realImage, 90);
            }

            image_photo.setImageBitmap(realImage);
            imagePath = bitmap2file(realImage).getAbsolutePath();
        } catch (Exception e) {
            imagePath = "";
        }
    }


    private File bitmap2file(Bitmap bitmap) {
        String fileName = null;
        if (type == 1)
            fileName = "back";
        else if (type == 2) {
            fileName = "withCard";
        } else {
            fileName = "front";
        }
        String folderPath = CameraActivity.DEFULT_FOLDER;
        File fileDir = new File(folderPath);
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }
        File file = new File(folderPath + File.separator + fileName);
        if (file.exists()) {
            file.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(file);
            if (bitmap.compress(Bitmap.CompressFormat.JPEG, 70, out)) {
                out.flush();
                out.close();
            }
            File del = new File(imagePath);
            if (del.exists())
                del.delete();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return file;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_camera_cancle) {
            setResult(RESULT_CANCELED);
        } else if (id == R.id.btn_camera_ok) {
            Intent data = new Intent();
            data.putExtra(IMAGE_PATH, imagePath);
            setResult(RESULT_OK, data);
        }
        finish();
    }

    public static Bitmap rotate(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(),
                source.getHeight(), matrix, false);
    }
}
