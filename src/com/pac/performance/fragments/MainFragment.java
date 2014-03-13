package com.pac.performance.fragments;

import com.pac.performance.MainActivity;
import com.pac.performance.R;
import com.pac.performance.utils.Control;
import com.pac.performance.utils.Utils;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

public class MainFragment extends Fragment {

	private SectionsPagerAdapter mSectionsPagerAdapter;
	public static ViewPager mViewPager = null;

	private static MenuItem applyButton;
	private static MenuItem cancelButton;
	private static MenuItem setonboot;

	public static boolean CPUChange = false;
	public static boolean BatteryChange = false;
	public static boolean AudioChange = false;
	public static boolean VoltageChange = false;
	public static boolean IOChange = false;
	public static boolean VMChange = false;

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

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.main, menu);
		applyButton = menu.findItem(R.id.action_apply);
		cancelButton = menu.findItem(R.id.action_cancel);
		applyButton.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_WITH_TEXT
				| MenuItem.SHOW_AS_ACTION_ALWAYS);
		cancelButton.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_WITH_TEXT
				| MenuItem.SHOW_AS_ACTION_ALWAYS);
		showButtons(false);
		setonboot = menu.findItem(R.id.action_setonboot).setChecked(
				Utils.getBoolean("setonboot", false, getActivity()));
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_apply:
			if (CPUChange)
				Control.setCPU(getActivity());
			if (BatteryChange)
				Control.setBattery(getActivity());
			if (AudioChange)
				Control.setAudio(getActivity());
			if (VoltageChange)
				Control.setVoltage(getActivity());
			if (IOChange)
				Control.setMisc(getActivity());
			if (VMChange)
				Control.setVM(getActivity());

			Control.reset();
			showButtons(false);
			Utils.toast(getString(R.string.applysuccessfully), getActivity());
			break;
		case R.id.action_cancel:
			Control.reset();
			showButtons(false);
			break;
		case R.id.action_setonboot:
			Utils.saveBoolean("setonboot", !setonboot.isChecked(),
					getActivity());
			setonboot.setChecked(!setonboot.isChecked());
			break;
		}
		return true;
	}

	public static void showButtons(boolean show) {
		applyButton.setVisible(show);
		cancelButton.setVisible(show);
	}
}
