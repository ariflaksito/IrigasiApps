package net.ariflaksito.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import net.ariflaksito.irigasiapp.Tab1;
import net.ariflaksito.irigasiapp.Tab2;

/**
 * Created by ariflaksito on 8/5/17.
 */

public class TabsPagerAdapter extends FragmentPagerAdapter {

    private static final int NO_OF_TABS = 2;

    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new Tab1();
            case 1:
                return new Tab2();
        }

        return null;
    }

    @Override
    public int getCount() {
        return NO_OF_TABS;
    }
}
