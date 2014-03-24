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

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pac.performance.MainActivity;
import com.pac.performance.R;

public class ViewPagerFragment extends Fragment implements
        ViewPager.OnPageChangeListener {

    public static ViewPager mViewPager;
    public static PagerTabStrip mPagerTabStrip;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container,
                false);

        assert rootView != null;
        mViewPager = (ViewPager) rootView.findViewById(R.id.viewPager);
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

        return rootView;
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return MainActivity.mFragments.get(position);
        }

        @Override
        public int getCount() {
            return MainActivity.mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return MainActivity.mFragmentNames.get(position);
        }
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {}

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {}

    @Override
    public void onPageSelected(int arg0) {
        MainActivity.actionBar.setSelectedNavigationItem(arg0);
    }

}
