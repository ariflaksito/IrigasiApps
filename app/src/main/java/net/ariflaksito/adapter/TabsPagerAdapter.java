package net.ariflaksito.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import net.ariflaksito.irigasiapp.Tab1;
import net.ariflaksito.irigasiapp.Tab2;
import net.ariflaksito.irigasiapp.Tab3;

/**
 * Created by ariflaksito on 8/5/17.
 */

public class TabsPagerAdapter extends FragmentPagerAdapter {

    private static final int NO_OF_TABS = 3;
    private Context cx;

    public TabsPagerAdapter(FragmentManager fm, Context cx) {
        super(fm);
        this.cx = cx;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new Tab1();
            case 1:
                return new Tab3(cx);
            case 2:
                return new Tab2();
        }

        return null;
    }

    @Override
    public int getCount() {
        return NO_OF_TABS;
    }
}
