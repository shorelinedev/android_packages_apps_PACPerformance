/*
 * Copyright (C) 2014 PAC-man ROM
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.pac.performance;

import java.util.ArrayList;
import java.util.List;

import com.pac.performance.fragments.AudioFragment;
import com.pac.performance.fragments.BatteryFragment;
import com.pac.performance.fragments.CPUFragment;
import com.pac.performance.fragments.InformationFragment;
import com.pac.performance.fragments.IOFragment;
import com.pac.performance.fragments.MinFreeFragment;
import com.pac.performance.fragments.VMFragment;
import com.pac.performance.fragments.ViewPagerFragment;
import com.pac.performance.fragments.VoltageFragment;
import com.pac.performance.helpers.AudioHelper;
import com.pac.performance.helpers.BatteryHelper;
import com.pac.performance.helpers.CPUHelper;
import com.pac.performance.helpers.RootHelper;
import com.pac.performance.helpers.VMHelper;
import com.pac.performance.helpers.VoltageHelper;
import com.pac.performance.utils.Control;
import com.pac.performance.utils.Utils;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends Activity {

    private static DrawerLayout mDrawerLayout;
    private static ListView mDrawerList;
    private static ActionBarDrawerToggle mDrawerToggle;

    public static int mWidth = 1;
    public static int mHeight = 1;

    public static List<Fragment> mFragments = new ArrayList<Fragment>();
    public static List<String> mFragmentNames = new ArrayList<String>();

    private static MenuItem applyButton;
    private static MenuItem cancelButton;
    private static MenuItem setonboot;

    public static boolean CPUChange = false;
    public static boolean BatteryChange = false;
    public static boolean AudioChange = false;
    public static boolean VoltageChange = false;
    public static boolean IOChange = false;
    public static boolean MinFreeChange = false;
    public static boolean VMChange = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setPerm();

        Display display = getWindowManager().getDefaultDisplay();
        mWidth = display.getWidth();
        mHeight = display.getHeight();

        mFragments.clear();
        mFragmentNames.clear();

        // add CPU Fragment
        mFragments.add(new CPUFragment());
        mFragmentNames.add(getString(R.string.cpu));

        // add Battery Fragment
        if (Utils.exist(BatteryHelper.FAST_CHARGE)
                || Utils.exist(BatteryHelper.BLX)) {
            mFragments.add(new BatteryFragment());
            mFragmentNames.add(getString(R.string.battery));
        }

        // add Audio Fragment
        if (Utils.exist(AudioHelper.FAUX_SOUND_CONTROL)) {
            mFragments.add(new AudioFragment());
            mFragmentNames.add(getString(R.string.audio));
        }

        // add Voltage Fragment
        if (Utils.exist(VoltageHelper.CPU_VOLTAGE)
                || Utils.exist(VoltageHelper.FAUX_VOLTAGE)) {
            mFragments.add(new VoltageFragment());
            mFragmentNames.add(getString(R.string.voltage));
        }

        // add IO Fragment
        mFragments.add(new IOFragment());
        mFragmentNames.add(getString(R.string.io));

        // add MinFree Fragment
        mFragments.add(new MinFreeFragment());
        mFragmentNames.add(getString(R.string.minfree));

        // add VM Fragment
        if (VMHelper.getVMValues().size() == VMHelper.getVMFiles().size()) {
            mFragments.add(new VMFragment());
            mFragmentNames.add(getString(R.string.vm));
        }

        // add Information Fragment
        mFragments.add(new InformationFragment());
        mFragmentNames.add(getString(R.string.information));

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
                GravityCompat.START);

        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.fragment_list, mFragmentNames));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.ic_drawer, R.string.drawer_open,
                R.string.drawer_close) {
            public void onDrawerClosed(View view) {
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                invalidateOptionsMenu();
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceState == null) {
            Fragment fragment = new ViewPagerFragment();

            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, fragment).commit();
            selectItem(0);
        }

    }

    private class DrawerItemClickListener implements
            ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                long id) {
            selectItem(position);
        }
    }

    private void selectItem(int position) {
        if (ViewPagerFragment.mViewPager != null)
            ViewPagerFragment.mViewPager.setCurrentItem(position);
        setDrawerSelected(position);
    }

    public void setDrawerSelected(int position) {
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        applyButton = menu.findItem(R.id.action_apply);
        cancelButton = menu.findItem(R.id.action_cancel);
        setonboot = menu.findItem(R.id.action_setonboot).setChecked(
                Utils.getBoolean("setonboot", false, getApplicationContext()));

        showButtons(false);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.action_apply:
            if (CPUChange)
                Control.setCPU(getApplicationContext());
            if (BatteryChange)
                Control.setBattery(getApplicationContext());
            if (AudioChange)
                Control.setAudio(getApplicationContext());
            if (VoltageChange)
                Control.setVoltage(getApplicationContext());
            if (IOChange)
                Control.setMisc(getApplicationContext());
            if (MinFreeChange)
                Control.setMinFree(getApplicationContext());
            if (VMChange)
                Control.setVM(getApplicationContext());

            Control.reset();
            Utils.toast(getString(R.string.applysuccessfully),
                    getApplicationContext());
            break;
        case R.id.action_cancel:
            Control.reset();
            break;
        case R.id.action_setonboot:
            Utils.saveBoolean("setonboot", !setonboot.isChecked(),
                    getApplicationContext());
            setonboot.setChecked(!setonboot.isChecked());
            break;
        }
        return mDrawerToggle.onOptionsItemSelected(item);
    }

    private void setPerm() {
        String[] files = { CPUHelper.MAX_FREQ, CPUHelper.MIN_FREQ,
                CPUHelper.MAX_SCREEN_OFF, CPUHelper.MIN_SCREEN_ON,
                CPUHelper.CUR_GOVERNOR };
        for (String file : files)
            RootHelper.run("chmod 777 " + file);
    }

    public static void showButtons(boolean show) {
        applyButton.setVisible(show);
        cancelButton.setVisible(show);
    }
}