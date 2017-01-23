package com.task.selfiegeek.activity;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.task.selfiegeek.R;
import com.task.selfiegeek.adapter.MyPageAdapter;
import com.task.selfiegeek.fragments.CameraFragment;
import com.task.selfiegeek.fragments.ViewUpload;

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
        viewPager.setAdapter(myPageAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position==1){
                    Log.e("e","E");
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
