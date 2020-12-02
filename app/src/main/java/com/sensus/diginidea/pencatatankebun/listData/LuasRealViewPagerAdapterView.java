package com.sensus.diginidea.pencatatankebun.listData;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by Fanglin Chen on 12/18/14.
 */

public class LuasRealViewPagerAdapterView extends FragmentPagerAdapter {
    private ArrayList<Fragment> fragments;

//    public static final int id = 0;
    public static final int luasan = 0;
    public static final int realisasi = 1;

    public static final String UI_LUASAN = "LUAS AREA";
    public static final String UI_REALISASI = "REALISASI";

    public LuasRealViewPagerAdapterView(FragmentManager fm, ArrayList<Fragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    public Fragment getItem(int pos) {
        return fragments.get(pos);
    }

    public int getCount() {
        return fragments.size();
    }

    public CharSequence getPageTitle(int position) {
        switch (position) {
            case luasan:
                return UI_LUASAN;
            case realisasi:
                return UI_REALISASI;
            default:
                break;
        }
        return null;
    }
}
