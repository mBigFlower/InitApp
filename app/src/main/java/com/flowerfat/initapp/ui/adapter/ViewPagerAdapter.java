package com.flowerfat.initapp.ui.adapter;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.flowerfat.initapp.ui.tour.TourDayFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 明明大美女 on 2016/9/7.
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {
    private final List<TourDayFragment> mFragments = new ArrayList<>();
    private final List<String> mFragmentTitles = new ArrayList<>();

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public void addFragment(TourDayFragment fragment, String title) {
        mFragments.add(fragment);
        mFragmentTitles.add(title);
    }

    @Override
    public TourDayFragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitles.get(position);
    }
}
