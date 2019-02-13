package com.simarro.joshu.clientsqr.Resources;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class MyFragmentPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragments;

    public MyFragmentPagerAdapter(FragmentManager fm) {

        super(fm);

        this.fragments = new ArrayList<Fragment>();

    }

    public void addFragment(Fragment fragment) {

        this.fragments.add(fragment);

    }



    @Override

    public Fragment getItem(int arg0) {

        return this.fragments.get(arg0);

    }



    @Override

    public int getCount() {

        return this.fragments.size();

    }
}
