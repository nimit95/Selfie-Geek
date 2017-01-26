package com.task.selfiegeek.activity;

import android.Manifest;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.task.selfiegeek.R;
import com.task.selfiegeek.adapter.MyPageAdapter;
import com.task.selfiegeek.fragments.CameraFragment;
import com.task.selfiegeek.fragments.ViewUpload;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private MyPageAdapter myPageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        viewPager.setOffscreenPageLimit(2);
        myPageAdapter = new MyPageAdapter(getSupportFragmentManager());
     /*   PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                Toast.makeText(getApplicationContext(), "Permission Granted", Toast.LENGTH_SHORT).show();
                viewPager.setOffscreenPageLimit(2);
                myPageAdapter = new MyPageAdapter(getSupportFragmentManager());
                viewPager.setAdapter(myPageAdapter);
            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                Toast.makeText(getApplicationContext(), "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }


        };*/
        Dexter.withActivity(this)

                .withPermissions(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        Toast.makeText(getApplicationContext(), "Permission Granted", Toast.LENGTH_SHORT).show();

                        viewPager.setAdapter(myPageAdapter);
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        Toast.makeText(getApplicationContext(), "Permission Denied\n" + permissions.toString(), Toast.LENGTH_SHORT).show();
                    }
                }).check();
        // .wit(permissionlistener)
        //       .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 1) {
                    Log.e("e", "E");
                    ViewUpload fragment = myPageAdapter.getFragment();
                    fragment.update();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
}
