package com.example.forhealth;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.forhealth.Fragment.TendingFragment;
import com.example.forhealth.weight.ViewPagerAdapter;

import java.lang.reflect.Field;

import static android.support.v4.view.PagerAdapter.POSITION_NONE;

public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {

    private ViewPager mViewPager;
    private TextView mTextMessage;
    private MenuItem menuItem;
    private BottomNavigationView bottomNavigationView;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mViewPager.setCurrentItem(0);
                    return true;
                case R.id.navigation_dashboard:
                    mViewPager.setCurrentItem(1);
                    return true;
                case R.id.navigation_tending:
                    mViewPager.setCurrentItem(2);
                    return true;
                case R.id.navigation_notifications:
                    mViewPager.setCurrentItem(3);
                    return true;
            }
            return false;
        }
    };
    private ViewPagerAdapter mViewPagerAdapter;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTextMessage = (TextView) findViewById(R.id.message);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mViewPager.addOnPageChangeListener(this);
        mViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position == 2) {
                     TendingFragment fragment = (TendingFragment) mViewPagerAdapter.getItem(2);
                    // call the fragmentVisible method here
                     fragment.fragmentVisible();
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }

        });

        mViewPager.setAdapter(mViewPagerAdapter);
        mViewPagerAdapter.notifyDataSetChanged();
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        bottomNavigationView.setSelectedItemId(R.id.navigation_home);
    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int i) {
        menuItem = bottomNavigationView.getMenu().getItem(i);
        menuItem.setChecked(true);
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }



    public static void actionStart(Context context) {
        Intent intent = new Intent(context,MainActivity.class);
        context.startActivity(intent);
    }
}
