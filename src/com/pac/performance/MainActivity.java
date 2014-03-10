package com.pac.performance;

import com.pac.performance.fragments.CPUFragment;
import com.pac.performance.fragments.MiscFragment;
import com.pac.performance.fragments.VoltageFragment;
import com.pac.performance.utils.CPUHelper;
import com.pac.performance.utils.Control;
import com.pac.performance.utils.RootHelper;
import com.pac.performance.utils.Utils;
import com.pac.performance.utils.VoltageHelper;
import com.stericson.roottools.RootTools;

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
	private static MenuItem setonboot;

	public static boolean CPUChange = false;
	public static boolean VoltageChange = false;
	public static boolean MiscChange = false;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (!RootTools.isRootAvailable(getApplicationContext())) {
			Utils.toast(
					getString(getString(R.string.app_name).contains("PAC") ? R.string.noroot
							: R.string.nicetrykanger), getApplicationContext());
			finish();
		} else if (!RootTools.isAccessGiven()) {
			Utils.toast(getString(R.string.norootaccess),
					getApplicationContext());
			RootTools.offerSuperUser(this);
			finish();
		} else if (!RootTools.isBusyboxAvailable()) {
			Utils.toast(getString(R.string.nobusybox), getApplicationContext());
			RootTools.offerBusyBox(this);
			finish();
		} else {
			Utils.saveString("kernelversion",
					Utils.getFormattedKernelVersion(), getApplicationContext());
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
			case 1:
				if (Utils.exist(VoltageHelper.CPU_VOLTAGE)
						|| Utils.exist(VoltageHelper.FAUX_VOLTAGE))
					return new VoltageFragment();
				else
					return new MiscFragment();
			case 2:
				return new MiscFragment();
			default:
				return new CPUFragment();
			}
		}

		@Override
		public int getCount() {
			return Utils.exist(VoltageHelper.CPU_VOLTAGE)
					|| Utils.exist(VoltageHelper.FAUX_VOLTAGE) ? 3 : 2;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			switch (position) {
			case 0:
				return getString(R.string.cpu);
			case 1:
				if (Utils.exist(VoltageHelper.CPU_VOLTAGE)
						|| Utils.exist(VoltageHelper.FAUX_VOLTAGE))
					return getString(R.string.voltage);
				else
					return getString(R.string.misc);
			case 2:
				return getString(R.string.misc);
			}
			return null;
		}
	}

	public void setPerm() {
		RootHelper.run("chmod 777 " + CPUHelper.MAX_FREQ);
		RootHelper.run("chmod 777 " + CPUHelper.MIN_FREQ);
		RootHelper.run("chmod 777 " + CPUHelper.MAX_SCREEN_OFF);
		RootHelper.run("chmod 777 " + CPUHelper.MIN_SCREEN_ON);
		RootHelper.run("chmod 777 " + CPUHelper.CUR_GOVERNOR);
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
		setonboot = menu.findItem(R.id.action_setonboot).setChecked(
				Utils.getBoolean("setonboot", false, getApplicationContext()));
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
			if (VoltageChange) {
				Control.setVoltage(getApplicationContext());
				VoltageChange = false;
			}
			if (MiscChange) {
				Control.setMisc(getApplicationContext());
				MiscChange = false;
			}
			showButtons(false);
			Utils.toast(getString(R.string.applysuccessfully),
					getApplicationContext());
			break;
		case R.id.action_cancel:
			if (CPUChange) {
				CPUFragment.setLayout();
				CPUChange = false;
			}
			if (VoltageChange) {
				VoltageFragment.setLayout();
				VoltageChange = false;
			}
			if (MiscChange) {
				MiscFragment.setLayout();
				MiscChange = false;
			}
			Control.reset();
			showButtons(false);
			break;
		case R.id.action_setonboot:
			Utils.saveBoolean("setonboot", !setonboot.isChecked(),
					getApplicationContext());
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
