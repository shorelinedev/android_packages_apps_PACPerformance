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

package com.pac.performance.fragments;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.pac.performance.R;
import com.pac.performance.fragments.AudioFragment;
import com.pac.performance.fragments.BatteryFragment;
import com.pac.performance.fragments.CPUFragment;
import com.pac.performance.fragments.InformationFragment;
import com.pac.performance.fragments.IOFragment;
import com.pac.performance.fragments.MinFreeFragment;
import com.pac.performance.fragments.VMFragment;
import com.pac.performance.fragments.VoltageFragment;
import com.pac.performance.helpers.RootHelper;
import com.pac.performance.helpers.VMHelper;
import com.pac.performance.helpers.VoltageHelper;
import com.pac.performance.utils.Constants;
import com.pac.performance.utils.Control;
import com.pac.performance.utils.Utils;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

// Remove this if you build with Eclipse etc.
import android.preference.PreferenceFrameLayout;

public class MainFragment extends Fragment implements Constants,
        ActionBar.OnNavigationListener, ViewPager.OnPageChangeListener {

    private Context context;
    private View rootView;

    private static ViewPager mViewPager;
    private static PagerTabStrip mPagerTabStrip;

    private static ActionBar actionBar = null;

    private static int currentPage = 0;

    private static Random rnd = new Random();

    public static int mWidth = 1;
    public static int mHeight = 1;

    private static List<Fragment> mFragments = new ArrayList<Fragment>();
    private static List<String> mFragmentNames = new ArrayList<String>();

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

    private final Handler hand = new Handler();
    private ProgressDialog dialog;
    private Bundle savedInstanceState;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_main, container, false);

        context = getActivity();
        this.savedInstanceState = savedInstanceState;
        getActionBar().setDisplayHomeAsUpEnabled(true);

        new Initialize().execute();

        // Remove this if you build with Eclipse etc.
        if (container instanceof PreferenceFrameLayout) ((PreferenceFrameLayout.LayoutParams) rootView
                .getLayoutParams()).removeBorders = true;

        return rootView;
    }

    private void setFragments() {
        mFragments.clear();
        mFragmentNames.clear();

        // add CPU Fragment
        mFragments.add(new CPUFragment());
        mFragmentNames.add(getString(R.string.cpu));

        // add Battery Fragment
        if (Utils.exist(FAST_CHARGE) || Utils.exist(BLX)) {
            mFragments.add(new BatteryFragment());
            mFragmentNames.add(getString(R.string.battery));
        }

        // add Audio Fragment
        if (Utils.exist(FAUX_SOUND_CONTROL)) {
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
    }

    private void setViewPager() {
        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(
                getFragmentManager());
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setCurrentItem(0);
        mViewPager.setOnPageChangeListener(this);

        mPagerTabStrip = (PagerTabStrip) rootView
                .findViewById(R.id.pagerTabStrip);
        mPagerTabStrip.setTabIndicatorColor(getResources().getColor(
                android.R.color.white));
        mPagerTabStrip.setDrawFullUnderline(false);

        mViewPager.setOffscreenPageLimit(mFragments.size());

        actionBar = getActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

        actionBar.setListNavigationCallbacks(
                new ArrayAdapter<String>(actionBar.getThemedContext(),
                        android.R.layout.simple_list_item_1,
                        android.R.id.text1, mFragmentNames), this);
    }

    private class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentNames.get(position);
        }
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {}

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {}

    @Override
    public void onPageSelected(int arg0) {
        MainFragment.actionBar.setSelectedNavigationItem(arg0);
    }

    private ActionBar getActionBar() {
        return getActivity().getActionBar();
    }

    private void selectItem(int position) {
        if (currentPage != position && mViewPager != null) mViewPager
                .setCurrentItem(position);

        currentPage = position;

        int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256),
                rnd.nextInt(256));

        if (mPagerTabStrip != null) mPagerTabStrip.setBackgroundColor(color);
        getActionBar().setBackgroundDrawable(new ColorDrawable(color));

        if (savedInstanceState == null) {
            mViewPager.setVisibility(View.VISIBLE);
            dialog.dismiss();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.pacperformancemenu, menu);
        applyButton = menu.findItem(R.id.action_apply);
        cancelButton = menu.findItem(R.id.action_cancel);
        setonboot = menu.findItem(R.id.action_setonboot).setChecked(
                Utils.getBoolean("setonboot", false, getActivity()));

        showButtons(false);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_apply:
                if (CPUChange) Control.setCPU(getActivity());
                if (BatteryChange) Control.setBattery(getActivity());
                if (AudioChange) Control.setAudio(getActivity());
                if (VoltageChange) Control.setVoltage(getActivity());
                if (IOChange) Control.setIO(getActivity());
                if (MinFreeChange) Control.setMinFree(getActivity());
                if (VMChange) Control.setVM(getActivity());

                Control.reset();
                Utils.toast(getString(R.string.applysuccessfully),
                        getActivity());
                break;
            case R.id.action_cancel:
                Control.reset();
                break;
            case R.id.action_setonboot:
                Utils.saveBoolean("setonboot", !setonboot.isChecked(),
                        getActivity());
                setonboot.setChecked(!setonboot.isChecked());
                break;
            case R.id.action_refresh_info:
                InformationFragment.refreshData();
                break;
            case android.R.id.home:
                getActivity().finish();
                break;
        }
        return true;
    }

    private void setPerm() {
        String[] files = { MAX_FREQ, MIN_FREQ, MAX_SCREEN_OFF, MIN_SCREEN_ON,
                CUR_GOVERNOR };
        for (String file : files)
            RootHelper.run("chmod 777 " + file);
    }

    public static void showButtons(boolean show) {
        applyButton.setVisible(show);
        cancelButton.setVisible(show);
    }

    @Override
    public boolean onNavigationItemSelected(int itemPosition, long itemId) {
        selectItem(itemPosition);
        return false;
    }

    @Override
    public void onResume() {
        hand.postDelayed(run, 0);
        super.onResume();
    }

    Runnable run = new Runnable() {
        @Override
        public void run() {
            CPUFragment.setCurFreq();
            BatteryFragment.setBatteryVoltage();
            hand.postDelayed(run, 1000);
        }
    };

    @Override
    public void onDestroy() {
        hand.removeCallbacks(run);
        super.onDestroy();
    }

    private class Initialize extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            setHasOptionsMenu(true);

            getActionBar().setIcon(
                    getResources().getDrawable(R.drawable.pacperformance_logo));

            if (mWidth == 1 || mHeight == 1) {
                Display display = getActivity().getWindowManager()
                        .getDefaultDisplay();
                mWidth = display.getWidth();
                mHeight = display.getHeight();
            }

            CPUChange = BatteryChange = AudioChange = VoltageChange = IOChange = MinFreeChange = VMChange = false;

            Utils.saveString("kernelversion",
                    Utils.getFormattedKernelVersion(), getActivity());

            mViewPager = (ViewPager) rootView.findViewById(R.id.viewPager);
            if (savedInstanceState == null) mViewPager.setVisibility(View.GONE);

            if (savedInstanceState == null) {
                dialog = new ProgressDialog(context);
                dialog.setMessage(getString(R.string.loading));
                dialog.show();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            setViewPager();
        }

        @Override
        protected String doInBackground(String... params) {
            setPerm();
            setFragments();
            return null;
        }
    }
}
