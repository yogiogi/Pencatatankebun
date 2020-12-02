package com.sensus.diginidea.pencatatankebun.listData;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by Yogi Prasetyo 19/10/2017.
 */

public class ActionTabsViewPagerAdapterView extends FragmentPagerAdapter {
    private ArrayList<Fragment> fragments;

    public static final int produksi = 0;
    public static final int estimasiprod = 1;
    public static final int estimasiluas = 2;

    public static final String UI_PRODUKSI = "PRODUKSI";
    public static final String UI_EST_PROD = "ESTIMASI PRODUKSI";
    public static final String UI_EST_LUAS_AREA = "ESTIMASI LUAS AREA";

    public ActionTabsViewPagerAdapterView(FragmentManager fm, ArrayList<Fragment> fragments) {
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
            case produksi:
                return UI_PRODUKSI;
            case estimasiprod:
                return UI_EST_PROD;
            case estimasiluas:
                return UI_EST_LUAS_AREA;
            default:
                break;
        }
        return null;
    }
}
