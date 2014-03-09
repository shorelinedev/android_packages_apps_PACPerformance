package com.pacperformance;

import com.pacperformance.fragments.CPUFragment;
import com.pacperformance.utils.CPUHelper;
import com.pacperformance.utils.Control;
import com.pacperformance.utils.RootHelper;
import com.pacperformance.utils.Utils;
import com.roottools.RootTools;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends FragmentActivity {

	private SectionsPagerAdapter mSectionsPagerAdapter;
	private ViewPager mViewPager;

	public static int mWidth = 0;
	public static int mHeight = 0;

	private static MenuItem applyButton;
	private static MenuItem cancelButton;

	public static boolean CPUChange = false;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (!RootTools.isRootAvailable()) {
			Utils.toast(getString(R.string.noroot), getApplicationContext());
			finish();
		} else if (!RootTools.isAccessGiven()) {
			Utils.toast(getString(R.string.norootaccess),
					getApplicationContext());
			RootTools.offerSuperUser(getParent());
			finish();
		} else if (!RootTools.isBusyboxAvailable()) {
			Utils.toast(getString(R.string.nobusybox), getApplicationContext());
			RootTools.offerBusyBox(getParent());
			finish();
		} else {
			RootTools.debugMode = true;
			setContentView(R.layout.activity_main);
			setPerm();
			Display display = this.getWindowManager().getDefaultDisplay();
			mWidth = display.getWidth();
			mHeight = display.getHeight();

			mSectionsPagerAdapter = new SectionsPagerAdapter(
					getSupportFragmentManager());

			mViewPager = (ViewPager) findViewById(R.id.viewPager);
			mViewPager.setAdapter(mSectionsPagerAdapter);
		}

	}

	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			switch (position) {
			case 0:
				return new CPUFragment();
			default:
				return new CPUFragment();
			}
		}

		@Override
		public int getCount() {
			return 1;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			switch (position) {
			case 0:
				return getString(R.string.cpu);
			}
			return null;
		}
	}

	public void setPerm() {
		if (CPUHelper.getMaxFreq() != 0)
			RootHelper.run("chmod 777 " + CPUHelper.MAX_FREQ);
		if (CPUHelper.getMinFreq() != 0)
			RootHelper.run("chmod 777 " + CPUHelper.MIN_FREQ);
		if (CPUHelper.getMaxScreenOffFreq() != 0)
			RootHelper.run("chmod 777 " + CPUHelper.MAX_SCREEN_OFF);
		if (CPUHelper.getMinScreenOnFreq() != 0)
			RootHelper.run("chmod 777 " + CPUHelper.MIN_SCREEN_ON);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		applyButton = menu.findItem(R.id.action_apply);
		cancelButton = menu.findItem(R.id.action_cancel);
		applyButton.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_WITH_TEXT
				| MenuItem.SHOW_AS_ACTION_ALWAYS);
		cancelButton.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_WITH_TEXT
				| MenuItem.SHOW_AS_ACTION_ALWAYS);
		showButtons(false);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_apply:
			if (CPUChange) {
				Control.setCPU(getApplicationContext());
				CPUChange = false;
			}
			showButtons(false);
			break;
		case R.id.action_cancel:
			if (CPUChange)
				CPUFragment.setLayout();
			showButtons(false);
			break;
		}
		return true;
	}

	public static void showButtons(boolean show) {
		applyButton.setVisible(show);
		cancelButton.setVisible(show);
	}

}
