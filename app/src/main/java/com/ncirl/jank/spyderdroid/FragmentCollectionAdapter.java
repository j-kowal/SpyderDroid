package com.ncirl.jank.spyderdroid;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
/**
 * Author: Jan Kowal
 * FragmentCollectionAdapter extending FragmentPagerAdapter
 * This adapter will be having an array list of fragments and handle switching between them
 */
public class FragmentCollectionAdapter extends FragmentPagerAdapter {

    // private arraylist which will be containing all the fragments
    private ArrayList<Fragment> fragments;

    public FragmentCollectionAdapter(FragmentManager fm) {
        super(fm); // passing fragment manager object in super
        fragments = new ArrayList<>();
        // adding all the fragments
        fragments.add(new TempFragment());
        fragments.add(new RangerFragment());
        fragments.add(new LightFragment());
        fragments.add(new SoundFragment());
        fragments.add(new RGBFragment());
        fragments.add(new MessageFragment());
    }

    @Override
    //returning fragment of given index
    public Fragment getItem(int i) {
        return fragments.get(i);
    }

    @Override
    // returning the number of all the fragments
    public int getCount() {
        return fragments.size();
    }
}
