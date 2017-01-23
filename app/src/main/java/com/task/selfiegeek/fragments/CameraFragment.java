package com.task.selfiegeek.fragments;


import android.Manifest;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.task.selfiegeek.R;
import com.task.selfiegeek.activity.CameraPreview;
import com.task.selfiegeek.activity.MainActivity;
import com.task.selfiegeek.utils.Constants;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;
import static com.task.selfiegeek.utils.Constants.TAG;
import static com.task.selfiegeek.utils.Constants.imgLoc;


public class CameraFragment extends Fragment {

    private Camera mCamera;
    private CameraPreview mPreview;
    private FloatingActionButton click, switchCamera, videoMode;
    private boolean isCamera = true;
    private boolean isBack = true;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public CameraFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_camera, container, false);
        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                Toast.makeText(getActivity(), "Permission Granted", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                Toast.makeText(getActivity(), "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }


        };

        new TedPermission(getActivity())
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE )
                .check();
        mCamera = getCameraInstance();
        mCamera.setDisplayOrientation(90);
        mPreview = new CameraPreview(getActivity(), mCamera,getActivity().getWindowManager().getDefaultDisplay().getWidth(),0);
        final FrameLayout preview = (FrameLayout) view.findViewById(R.id.camera_preview);
        preview.addView(mPreview);
        click = (FloatingActionButton) view.findViewById(R.id.floatingActionButton);
        switchCamera = (FloatingActionButton) view.findViewById(R.id.switch_camera);
        videoMode = (FloatingActionButton) view.findViewById(R.id.video);
        click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isCamera){
                    mCamera.takePicture(null, null, mPicture);
                    preview.setVisibility(View.INVISIBLE);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            preview.setVisibility(View.VISIBLE);
                        }
                    },200);

                }
            }
        });
        switchCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isBack){
                    isBack = false;
                    switchCamera.setImageResource(R.drawable.ic_camera_rear_black_24dp);
                    mCamera.startPreview();
                    mCamera.release();
                    mCamera = getCamera2Instance();
                    mCamera.setDisplayOrientation(90);
                    try {
                        mCamera.setPreviewDisplay(mPreview.getHolder());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    mCamera.startPreview();
                }
                else{
                    isBack = true;
                    switchCamera.setImageResource(R.drawable.ic_camera_front_black_24dp);
                    mCamera.startPreview();
                    mCamera.release();
                    mCamera = getCameraInstance();
                    mCamera.setDisplayOrientation(90);
                    try {
                        mCamera.setPreviewDisplay(mPreview.getHolder());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    mCamera.startPreview();
                }
            }
        });
        videoMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isCamera){
                    isCamera = false;
                    videoMode.setImageResource(R.drawable.ic_camera_alt_black_24dp);
                }
                else{
                    isCamera = true;
                    videoMode.setImageResource(R.drawable.ic_videocam_black_24dp);
                }
            }
        });
        return view ;
    }
    @Override
    public void onPause() {
        super.onPause();
        //releaseMediaRecorder();       // if you are using MediaRecorder, release it first
        //releaseCamera();              // release the camera immediately on pause event
    }
 /*   private void releaseMediaRecorder(){
        if (mMediaRecorder != null) {
            mMediaRecorder.reset();   // clear recorder configuration
            mMediaRecorder.release(); // release the recorder object
            mMediaRecorder = null;
            mCamera.lock();           // lock camera for later use
        }
    }*/

    private void releaseCamera(){
        if (mCamera != null){
            mCamera.release();        // release the camera for other applications
            mCamera = null;
        }
    }

    public static Camera getCameraInstance(){
        Camera c = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance
        }
        catch (Exception e){
            // Camera is not available (in use or does not exist)
        }
        Camera.Parameters params = c.getParameters();
        params.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        c.setParameters(params);
        return c; // returns null if camera is unavailable
    }
    public static Camera getCamera2Instance(){
        Camera c = null;
        try {
            c = Camera.open(1); // attempt to get a Camera instance
        }
        catch (Exception e){
            // Camera is not available (in use or does not exist)
        }
        //set camera to continually auto-focus
        Camera.Parameters params = c.getParameters();
        params.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        c.setParameters(params);
        return c; // returns null if camera is unavailable
    }
    private Camera.PictureCallback mPicture = new Camera.PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {

            File pictureFile = new File(imgLoc);
            pictureFile.mkdirs();
            SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmss");
            String currentDateandTime = sdf.format(new Date());
            pictureFile = new File(imgLoc +File.separator+"image"+currentDateandTime+".jpg");
           // if()
            if (pictureFile == null){
                Log.e(TAG, "Error creating media file, check storage permissions: ");
                return;
            }


            try {
                FileOutputStream fos = new FileOutputStream(pictureFile);
                fos.write(data);
                fos.close();
                Toast.makeText(getActivity(),"Image saved at"+pictureFile.getPath()+"",Toast.LENGTH_SHORT).show();
            } catch (FileNotFoundException e) {
                Log.d(TAG, "File not found: " + e.getMessage());
            } catch (IOException e) {
                Log.d(TAG, "Error accessing file: " + e.getMessage());
            }
        }
    };
}
