package com.task.selfiegeek.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.task.selfiegeek.fragments.CameraFragment;
import com.task.selfiegeek.fragments.ViewUpload;

/**
 * Created by Nimit Agg on 21-01-2017.
 */

public class MyPageAdapter extends FragmentPagerAdapter {
    private static final int NUM_COUNT = 2;

    public MyPageAdapter(FragmentManager fm) {
        super(fm);
    }

    private ViewUpload viewUpload = new ViewUpload();

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Camera";
            case 1:
                return "Uploaded";
            default:
                return null;
        }
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new CameraFragment();
            case 1:
                return viewUpload;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return NUM_COUNT;
    }

    public ViewUpload getFragment() {
        return viewUpload;
    }
}
