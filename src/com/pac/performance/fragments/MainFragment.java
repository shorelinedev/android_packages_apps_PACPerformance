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
