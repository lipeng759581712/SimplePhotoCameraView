package com.google.android.cameraview.demo;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.cameraview.CameraView;


/**
 * Created by maxpengli on 2017/6/27.
 */

public class CameraActivity extends AppCompatActivity {
    private static final String TAG = "xxp";

    private Handler mBackgroundHandler;

    private CameraView mCameraView;

    private RelativeLayout preview_Layout;

    private ImageView preview_imageview;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.camera_layout);

        initView();



    }

    private void initView() {
        mCameraView = (CameraView) findViewById(R.id.camera);
        mCameraView.addCallback(mCallback);

        findViewById(R.id.take_picture).setOnClickListener(mOnClickListener);

        findViewById(R.id.switch_button).setOnClickListener(mOnClickListener);

        preview_Layout = (RelativeLayout) findViewById(R.id.preview_container);

        preview_imageview = (ImageView) findViewById(R.id.preview_image);

        findViewById(R.id.preview_close_icon).setOnClickListener(mOnClickListener);
    }

    @Override
    protected void onResume() {
        mCameraView.start();
        super.onResume();
    }

    @Override
    protected void onPause() {
        mCameraView.stop();
        super.onPause();
    }



    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.take_picture:
                    if (mCameraView != null) {
                        mCameraView.takePicture();
                    }
                    break;
                case R.id.switch_button:
                    if (mCameraView != null) {
                        int facing = mCameraView.getFacing();
                        mCameraView.setFacing(facing == CameraView.FACING_FRONT ?
                                CameraView.FACING_BACK : CameraView.FACING_FRONT);
                    }
                    break;
                case R.id.preview_close_icon:
                    preview_Layout.setVisibility(View.GONE);
                    break;
            }
        }
    };


    private CameraView.Callback mCallback = new CameraView.Callback() {

        @Override
        public void onCameraOpened(CameraView cameraView) {
            Log.d(TAG, "onCameraOpened");
        }

        @Override
        public void onCameraClosed(CameraView cameraView) {
            Log.d(TAG, "onCameraClosed");
        }

        @Override
        public void onPictureTaken(CameraView cameraView, final byte[] data) {
            Bitmap bitmap = BitmapUtils.createSampledBitmapFromBytes(data, 1024);
            Log.d(TAG, "onPictureTaken " + data.length);
            Toast.makeText(cameraView.getContext(), "take picture return "+data.length, Toast.LENGTH_SHORT).show();

            Matrix matrix = new Matrix();

            matrix.setRotate(90);

            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);


            preview_Layout.setVisibility(View.VISIBLE);
            preview_imageview.setImageBitmap(bitmap);

        }

    };



}
