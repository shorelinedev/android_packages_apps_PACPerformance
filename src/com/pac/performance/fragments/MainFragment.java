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

import com.pac.performance.MainActivity;
import com.pac.performance.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MainFragment extends Fragment {

	private SectionsPagerAdapter mSectionsPagerAdapter;
	public static ViewPager mViewPager = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		setHasOptionsMenu(true);
		View rootView = inflater.inflate(R.layout.fragment_main, container,
				false);

		mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());

		mViewPager = (ViewPager) rootView.findViewById(R.id.viewPager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

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
}
