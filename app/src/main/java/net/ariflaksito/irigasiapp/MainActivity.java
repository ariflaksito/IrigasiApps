package net.ariflaksito.irigasiapp;

import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;

import net.ariflaksito.adapter.TabsPagerAdapter;

public class MainActivity extends ActionBarActivity implements ActionBar.TabListener {

    private ViewPager viewPager;
    private TabsPagerAdapter adapter;
    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);

        viewPager = (ViewPager) findViewById(R.id.pager);
        actionBar = getSupportActionBar();
        actionBar.setStackedBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorPrimaryDark)));

        adapter = new TabsPagerAdapter(getSupportFragmentManager(), MainActivity.this);

        viewPager.setAdapter(adapter);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.addTab(actionBar.newTab().setText("TITIK IRIGASI")
                .setTabListener(this));
        actionBar.addTab(actionBar.newTab().setText("MAP")
                .setTabListener(this));
        actionBar.addTab(actionBar.newTab().setText("HISTORY")
                .setTabListener(this));

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }
}
