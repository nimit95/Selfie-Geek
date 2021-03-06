package com.task.selfiegeek.fragments;


import android.Manifest;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;


import com.task.selfiegeek.Network.UploadFile;
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
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;
import static com.task.selfiegeek.utils.Constants.TAG;
import static com.task.selfiegeek.utils.Constants.imgLoc;


public class CameraFragment extends Fragment {

    private Camera mCamera;
    private CameraPreview mPreview;
    private FloatingActionButton click, switchCamera, videoMode;
    private boolean isCamera = true;
    private MediaRecorder recorder;
    private UploadFile uploadFile;
    private int i =0;
    private String videoPath;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private FrameLayout preview;
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

        uploadFile = new UploadFile(getActivity());

        preview = (FrameLayout) view.findViewById(R.id.camera_preview);

        recorder = new MediaRecorder();
        click = (FloatingActionButton) view.findViewById(R.id.floatingActionButton);
        switchCamera = (FloatingActionButton) view.findViewById(R.id.switch_camera);
        videoMode = (FloatingActionButton) view.findViewById(R.id.video);
        click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isCamera) {
                    mCamera.takePicture(null, null, mPicture);
                    preview.setVisibility(View.INVISIBLE);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            preview.setVisibility(View.VISIBLE);
                        }
                    }, 200);

                } else {
                    if (isRecording) {
                        Log.e("chal", "chal");
                        recorder.stop();
                        preview.setVisibility(View.INVISIBLE);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                preview.setVisibility(View.VISIBLE);
                            }
                        }, 200);
                        SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmss");
                        String currentDateandTime = sdf.format(new Date());
                        Toast.makeText(getActivity(), "Video saved at " + imgLoc + File.separator + "video" + currentDateandTime, Toast.LENGTH_SHORT).show();
                        isRecording = false;
                        releaseMediaRecorder();
                        click.setImageResource(android.R.color.transparent);
                        try {
                            uploadFile.uploadFile(new File(getVideoPath()));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        prepareRecorder2();
                        recorder.start();
                        click.setImageResource(R.drawable.ic_stop_black_24dp);
                        isRecording = true;

                    }
                }
            }
        });
        switchCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isRecording) {
                    Toast.makeText(getContext(), getString(R.string.warning), Toast.LENGTH_SHORT).show();
                } else if (isBack) {
                    isBack = false;
                    switchCamera.setImageResource(R.drawable.ic_camera_rear_black_24dp);
                    mCamera.startPreview();
                    mCamera.release();
                    mPreview.getHolder().removeCallback(mPreview);
                    mCamera = getCameraInstance(1);
                    i=1;
                    mCamera.setDisplayOrientation(90);
                    try {
                        mCamera.setPreviewDisplay(mPreview.getHolder());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    mCamera.startPreview();
                } else {
                    isBack = true;
                    switchCamera.setImageResource(R.drawable.ic_camera_front_black_24dp);
                    mCamera.startPreview();
                    mCamera.release();
                    mPreview.getHolder().removeCallback(mPreview);
                    mCamera = getCameraInstance(0);
                    mCamera.setDisplayOrientation(90);
                    i=0;
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
                if (isRecording) {
                    Toast.makeText(getContext(), getString(R.string.warning), Toast.LENGTH_SHORT).show();
                } else if (isCamera) {
                    isCamera = false;
                    click.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.red)));
                    videoMode.setImageResource(R.drawable.ic_camera_alt_black_24dp);

                } else {
                    isCamera = true;
                    click.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
                    videoMode.setImageResource(R.drawable.ic_videocam_black_24dp);
                }
            }
        });
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        mPreview.getHolder().removeCallback(mPreview);
        //releaseMediaRecorder();       // if you are using MediaRecorder, release it first
        //releaseCamera();              // release the camera immediately on pause event
    }

    @Override
    public void onResume() {
        Log.e("yah","yaj");

        mCamera = getCameraInstance(i);
        mPreview = new CameraPreview(getActivity(), mCamera, getActivity().getWindowManager().getDefaultDisplay().getWidth(), 0);
        preview.addView(mPreview);
        super.onResume();
    }

    private void releaseMediaRecorder() {
        if (recorder != null) {
            recorder.reset();   // clear recorder configuration
            recorder.release(); // release the recorder object
            recorder = null;
            mCamera.lock();           // lock camera for later use
        }
    }

    private void releaseCamera() {
        if (mCamera != null) {
            mCamera.release();        // release the camera for other applications
            mCamera = null;
        }
    }

    public Camera getCameraInstance(int i) {
        Camera c = null;
        try {
            c = Camera.open(i); // attempt to get a Camera instance
        } catch (Exception e) {
            // Camera is not available (in use or does not exist)
        }
        //set camera to continually auto-focus
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(Camera.CameraInfo.CAMERA_FACING_BACK, info);
        int rotation = getActivity().getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break; //Natural orientation
            case Surface.ROTATION_90:
                degrees = 90;
                break; //Landscape left
            case Surface.ROTATION_180:
                degrees = 180;
                break;//Upside down
            case Surface.ROTATION_270:
                degrees = 270;
                break;//Landscape right
        }
        int rotate = (info.orientation - degrees + 360) % 360;

        //STEP #2: Set the 'rotation' parameter
        Camera.Parameters params = c.getParameters();
        List<Camera.Size> supportedSizes = params.getSupportedPictureSizes();
        Camera.Size bestSize = supportedSizes.get(0);
        for (int j = 1; j < supportedSizes.size(); j++) {
            if ((supportedSizes.get(i).width * supportedSizes.get(i).height) > (bestSize.width * bestSize.height)) {
                bestSize = supportedSizes.get(i);
            }
        }
        params.setPictureSize(bestSize.width, bestSize.height);
        params.setJpegQuality(80);
        params.setJpegThumbnailQuality(80);
        if (i == 1)
            params.setRotation(rotate + 180);
        else
            params.setRotation(rotate);
        params.setPictureFormat(ImageFormat.JPEG);
        params.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        c.setParameters(params);
        c.setDisplayOrientation(90);
        return c; // returns null if camera is unavailable
    }

    private Camera.PictureCallback mPicture = new Camera.PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {

            File pictureFile = new File(imgLoc);
            pictureFile.mkdirs();
            SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmss");
            String currentDateandTime = sdf.format(new Date());
            pictureFile = new File(imgLoc + File.separator + "image" + currentDateandTime + ".jpg");
            // if()
            if (pictureFile == null) {
                Log.e(TAG, "Error creating media file, check storage permissions: ");
                return;
            }


            try {
                FileOutputStream fos = new FileOutputStream(pictureFile);
                fos.write(data);
                fos.close();
                Toast.makeText(getActivity(), "Image saved at" + pictureFile.getPath() + "", Toast.LENGTH_SHORT).show();
                uploadFile.uploadFile(pictureFile);
            } catch (FileNotFoundException e) {
                Log.d(TAG, "File not found: " + e.getMessage());
            } catch (IOException e) {
                Log.d(TAG, "Error accessing file: " + e.getMessage());
            }
        }
    };

    private void prepareRecorder2() {
        recorder = new MediaRecorder();
        recorder.setPreviewDisplay(mPreview.getHolder().getSurface());
        CamcorderProfile camcorderProfile = CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH);
        if (true) {
            mCamera.unlock();
            recorder.setCamera(mCamera);
        }

        recorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
        recorder.setVideoSource(MediaRecorder.VideoSource.DEFAULT);

        recorder.setProfile(camcorderProfile);
        SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmss");
        String currentDateandTime = sdf.format(new Date());
        if (camcorderProfile.fileFormat == MediaRecorder.OutputFormat.THREE_GPP) {
            try {
                File newFile = File.createTempFile("video" + currentDateandTime, ".3gp", new File(imgLoc));
                recorder.setOutputFile(newFile.getAbsolutePath());
                Log.e("km",newFile.getAbsolutePath());
                setVideoPath(newFile.getAbsolutePath());
            } catch (IOException e) {
                ////  Log.v(LOGTAG,"Couldn't create file");
                e.printStackTrace();
                //finish();
            }
        } else if (camcorderProfile.fileFormat == MediaRecorder.OutputFormat.MPEG_4) {
            try {
                File newFile = File.createTempFile("video" + currentDateandTime, ".mp4", new File(imgLoc));
                recorder.setOutputFile(newFile.getAbsolutePath());
                Log.e("km",newFile.getAbsolutePath());
                setVideoPath(newFile.getAbsolutePath());
            } catch (IOException e) {
                // Log.v(LOGTAG,"Couldn't create file");
                e.printStackTrace();
                // finish();
            }
        } else {
            try {
                File newFile = File.createTempFile("video" + currentDateandTime, ".3gp", new File(imgLoc));
                recorder.setOutputFile(newFile.getAbsolutePath());
                setVideoPath(newFile.getAbsolutePath());
            } catch (IOException e) {
                // Log.v(LOGTAG,"Couldn't create file");
                e.printStackTrace();
                // finish();
            }

        }
        //recorder.setMaxDuration(50000); // 50 seconds
        //recorder.setMaxFileSize(5000000); // Approximately 5 megabytes

        try {
            recorder.prepare();
        } catch (IllegalStateException e) {
            e.printStackTrace();
            // finish();
        } catch (IOException e) {
            e.printStackTrace();
            // finish();
        }

    }

    private boolean isBack = true, isRecording = false;

    public String getVideoPath() {
        return videoPath;
    }

    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }
}
