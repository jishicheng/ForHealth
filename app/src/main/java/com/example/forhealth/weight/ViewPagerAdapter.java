package com.example.forhealth.weight;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.view.ViewGroup;

import com.example.forhealth.Fragment.DairyFragment;
import com.example.forhealth.Fragment.HomeFragment;
import com.example.forhealth.Fragment.MoreFragment;
import com.example.forhealth.Fragment.TendingFragment;

public class ViewPagerAdapter extends FragmentPagerAdapter {
    private FragmentManager mFragmentManager;
    private FragmentTransaction mCurTransaction = null;
    private Fragment mfragment[] = new Fragment[]{new HomeFragment(),new DairyFragment(),new TendingFragment(),new MoreFragment()};

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
        this.mFragmentManager = fm;
    }

    @Override
    public Fragment getItem(int i) {
        return mfragment[i];
    }

    @Override
    public int getCount() {
        return mfragment.length;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }


}
