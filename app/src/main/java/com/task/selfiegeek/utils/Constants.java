package com.task.selfiegeek.utils;

import android.os.Environment;

import java.io.File;

/*
 * Created by Nimit Agg on 12-12-2016.
 */
public class Constants {

    public static final String TAG_SHARED_PREF = "sharedPreferences";
    public static final String key = "kid_rJtGRFUDg";
    public static final String secretKey = "2a34a3ce4efb4e348715e345a5d41849";
    public static final String TAG = "Selfie Geek";
    public static int imgNumber = 1;
    public static final int CAMERA_RQ = 6969;
    public static String imgLoc = Environment.getExternalStorageDirectory().toString() + File.separator + "SelfieGeek";
}