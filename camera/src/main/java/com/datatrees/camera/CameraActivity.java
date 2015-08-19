package com.datatrees.camera;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Rect;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.ErrorCallback;
import android.hardware.Camera.PictureCallback;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * CameraActivity
 * Created by zhangping on 15/8/17.
 */
public class CameraActivity extends Activity implements OnClickListener {
    Activity context;
    Preview preview;
    Camera camera;
    ImageButton exitButton;
    ImageView fotoButton;
    LinearLayout progressLayout;
    ImageView iv_camera_bg;

    /**
     * 触摸屏幕时显示的聚焦图案
     */
    private FocusImageView mFocusImageView;

    private int cameraPosition = 1;//0代表前置摄像头，1代表后置摄像头

    String folder;

    public static final String DEFULT_FOLDER = "/sdcard/com.datatrees.gongfudai/temp";

    public static final String IDCARD_TYPE = "idcard_type";

    public static final int PHOTO_PRE = 12;

    int type = 0;//0正面,1反面,2手持身份证

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        folder = getIntent().getStringExtra("file_folder");
        type = getIntent().getIntExtra(IDCARD_TYPE, 0);
        if (folder == null || folder.trim().equals("")) {
            folder = DEFULT_FOLDER;
        }

        context = this;
        mFocusImageView = (FocusImageView) findViewById(R.id.focusImageView);
        fotoButton = (ImageView) findViewById(R.id.ibtn_camera_take);
        exitButton = (ImageButton) findViewById(R.id.ibtn_cancle);
        exitButton.setOnClickListener(this);
        progressLayout = (LinearLayout) findViewById(R.id.progress_layout);
        iv_camera_bg = (ImageView) findViewById(R.id.iv_camera_bg);
        findViewById(R.id.ibtn_open_light).setOnClickListener(this);

        preview = new Preview(this,
                (SurfaceView) findViewById(R.id.camera_fragment));
        FrameLayout frame = (FrameLayout) findViewById(R.id.preview);
        frame.addView(preview);
        preview.setKeepScreenOn(true);

        if (type == 0) {
            iv_camera_bg.setBackgroundResource(R.drawable.bg_idcard_front);
        } else if (type == 1) {
            iv_camera_bg.setBackgroundResource(R.drawable.bg_idcard_back);
        } else if (type == 2) {
            iv_camera_bg.setBackgroundResource(R.drawable.bg_idcard_withcard);
        }

        fotoButton.setOnClickListener(this);

        findViewById(R.id.btn_swicamera).setOnClickListener(this);

        preview.mSurfaceView.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_UP:
                        Point point = new Point((int) event.getX(), (int) event.getY());
                        onFocus(point, autoFocusCallback);
                        mFocusImageView.startFocus(point);
                        break;
                }
                return true;
            }
        });
    }

    /**
     * 手动聚焦
     *
     * @param point 触屏坐标
     */
    protected void onFocus(Point point, Camera.AutoFocusCallback callback) {
        Camera.Parameters parameters = camera.getParameters();
        //不支持设置自定义聚焦，则使用自动聚焦，返回
        if (parameters.getMaxNumFocusAreas() <= 0) {
            camera.autoFocus(callback);
            return;
        }
        List<Camera.Area> areas = new ArrayList<Camera.Area>();
        int left = point.x - 300;
        int top = point.y - 300;
        int right = point.x + 300;
        int bottom = point.y + 300;
        left = left < -1000 ? -1000 : left;
        top = top < -1000 ? -1000 : top;
        right = right > 1000 ? 1000 : right;
        bottom = bottom > 1000 ? 1000 : bottom;
        areas.add(new Camera.Area(new Rect(left, top, right, bottom), 100));
        parameters.setFocusAreas(areas);
        try {
            //本人使用的小米手机在设置聚焦区域的时候经常会出异常，看日志发现是框架层的字符串转int的时候出错了，
            //目测是小米修改了框架层代码导致，在此try掉，对实际聚焦效果没影响
            camera.setParameters(parameters);
        } catch (Exception e) {
        }
        camera.autoFocus(callback);
    }


    @Override
    protected void onResume() {
        super.onResume();
        try {
            if (camera == null) {
                camera = Camera.open();
                camera.startPreview();
                camera.setErrorCallback(new ErrorCallback() {
                    public void onError(int error, Camera mcamera) {
                        camera.release();
                        camera = Camera.open();
                    }
                });
            }
            if (camera != null) {
                if (Build.VERSION.SDK_INT >= 14)
                    setCameraDisplayOrientation(context,
                            CameraInfo.CAMERA_FACING_BACK, camera);
                preview.setCamera(camera);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setCameraDisplayOrientation(Activity activity, int cameraId,
                                             android.hardware.Camera camera) {
        android.hardware.Camera.CameraInfo info = new android.hardware.Camera.CameraInfo();
        android.hardware.Camera.getCameraInfo(cameraId, info);
        int rotation = activity.getWindowManager().getDefaultDisplay()
                .getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }

        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360; // compensate the mirror
        } else { // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        camera.setDisplayOrientation(result);
    }

    private final Camera.AutoFocusCallback autoFocusCallback = new Camera.AutoFocusCallback() {

        @Override
        public void onAutoFocus(boolean success, Camera camera) {
            //聚焦之后根据结果修改图片
            if (success) {
                mFocusImageView.onFocusSuccess();
            } else {
                //聚焦失败显示的图片，由于未找到合适的资源，这里仍显示同一张图片
                mFocusImageView.onFocusFailed();

            }
        }
    };

    public void takeFocusedPicture() {
        camera.takePicture(null, null, jpegCallback);

    }

    PictureCallback jpegCallback = new PictureCallback() {
        @SuppressWarnings("deprecation")
        public void onPictureTaken(byte[] data, Camera camera) {

            FileOutputStream outStream = null;
            File videoDirectory = new File(folder);
            if (!videoDirectory.exists()) {
                videoDirectory.mkdirs();
            }
            String filePath = folder + File.separator + System.currentTimeMillis() + ".jpg";
            try {
                // Write to SD Card
                outStream = new FileOutputStream(filePath);
                outStream.write(data);
                outStream.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
            }
            cameraRelease();
            fotoButton.setClickable(true);
            progressLayout.setVisibility(View.GONE);
            exitButton.setClickable(true);

            startActivityForResult(new Intent(CameraActivity.this, PhotoPreActivity.class).putExtra(IDCARD_TYPE, type).putExtra(PhotoPreActivity.IMAGE_PATH, filePath), PHOTO_PRE);

        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String filePath = null;
        if (data != null)
            filePath = data.getStringExtra(PhotoPreActivity.IMAGE_PATH);
        if (resultCode == RESULT_OK && requestCode == PHOTO_PRE) {
            Intent extras = new Intent();
            extras.putExtra(PhotoPreActivity.IMAGE_PATH, filePath);
            extras.putExtra(IDCARD_TYPE, type);
            setResult(RESULT_OK, extras);
            cameraRelease();
            this.finish();
        }
    }


    private void cameraRelease() {
        if (camera != null) {
            camera.stopPreview();//停掉原来摄像头的预览
            camera.release();//释放资源
            camera = null;//取消原来摄像头
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) { //按下的如果是BACK，同时没有重复
            cameraRelease();
            setResult(RESULT_CANCELED);
        }
        return super.onKeyDown(keyCode, event);
    }

    int openLight = 1;//0关闭1开闪光灯

    @Override
    public void onClick(View v) {
        if (camera == null)
            return;
        int id = v.getId();
        if (id == R.id.ibtn_cancle) {
            cameraRelease();
            setResult(RESULT_CANCELED);
            this.finish();
        } else if (id == R.id.ibtn_open_light) {
            if (openLight == 0) {
                openLight = 1;
                Camera.Parameters parameters = camera.getParameters();
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                camera.setParameters(parameters);
            } else {
                openLight = 0;
                Camera.Parameters parameters = camera.getParameters();
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                camera.setParameters(parameters);
            }

        } else if (id == R.id.btn_swicamera) {
            //切换前后摄像头
            int cameraCount = 0;
            try {
                CameraInfo cameraInfo = new CameraInfo();
                cameraCount = Camera.getNumberOfCameras();//得到摄像头的个数

                for (int i = 0; i < cameraCount; i++) {
                    Camera.getCameraInfo(i, cameraInfo);//得到每一个摄像头的信息
                    if (cameraPosition == 1) {
                        //现在是后置，变更为前置
                        if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {//代表摄像头的方位，CAMERA_FACING_FRONT前置      CAMERA_FACING_BACK后置
                            camera.stopPreview();//停掉原来摄像头的预览
                            camera.release();//释放资源
                            camera = null;//取消原来摄像头
                            camera = Camera.open(i);//打开当前选中的摄像头
                            camera.startPreview();//开始预览
                            preview.setCamera(camera);
                            cameraPosition = 0;
                            break;
                        }
                    } else {
                        //现在是前置， 变更为后置
                        if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {//代表摄像头的方位，CAMERA_FACING_FRONT前置      CAMERA_FACING_BACK后置
                            camera.stopPreview();//停掉原来摄像头的预览
                            camera.release();//释放资源
                            camera = null;//取消原来摄像头
                            camera = Camera.open(i);//打开当前选中的摄像头

                            camera.startPreview();//开始预览
                            cameraPosition = 1;
                            break;
                        }
                    }

                }
                if (camera != null) {
                    camera.setErrorCallback(new ErrorCallback() {
                        public void onError(int error, Camera mcamera) {
                            camera.release();
                            camera = Camera.open();
                        }
                    });
                    if (Build.VERSION.SDK_INT >= 14)
                        setCameraDisplayOrientation(context,
                                CameraInfo.CAMERA_FACING_BACK, camera);
                    camera.setPreviewDisplay(preview.mHolder);//通过surfaceview;
                    preview.setCamera(camera);
                }
            } catch (Exception e) {
            }
        } else if (id == R.id.ibtn_camera_take) {
            try {
                takeFocusedPicture();
            } catch (Exception e) {
            }
            exitButton.setClickable(false);
            fotoButton.setClickable(false);
            progressLayout.setVisibility(View.VISIBLE);
        }
    }
}
