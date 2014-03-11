package com.pac.performance;

import java.util.ArrayList;
import java.util.List;

import com.pac.performance.fragments.CPUFragment;
import com.pac.performance.fragments.InformationFragment;
import com.pac.performance.fragments.MainFragment;
import com.pac.performance.fragments.MiscFragment;
import com.pac.performance.fragments.VMFragment;
import com.pac.performance.fragments.VoltageFragment;
import com.pac.performance.utils.CPUHelper;
import com.pac.performance.utils.RootHelper;
import com.pac.performance.utils.Utils;
import com.pac.performance.utils.VMHelper;
import com.pac.performance.utils.VoltageHelper;
import com.stericson.RootTools.RootTools;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
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

public class MainActivity extends FragmentActivity {

	private static DrawerLayout mDrawerLayout;
	private static ListView mDrawerList;
	private static ActionBarDrawerToggle mDrawerToggle;

	private static CharSequence mDrawerTitle;
	private static CharSequence mTitle;

	public static int mWidth = 0;
	public static int mHeight = 0;

	public static List<Fragment> mFragments = new ArrayList<Fragment>();
	public static List<String> mFragmentNames = new ArrayList<String>();

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		if (!RootTools.isRootAvailable()) {
			Utils.toast(getString(R.string.noroot), getApplicationContext());
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

			setPerm();

			Display display = this.getWindowManager().getDefaultDisplay();
			mWidth = display.getWidth();
			mHeight = display.getHeight();

			mFragments.add(new CPUFragment());
			mFragmentNames.add(getString(R.string.cpu));
			if (Utils.exist(VoltageHelper.CPU_VOLTAGE)
					|| Utils.exist(VoltageHelper.FAUX_VOLTAGE)) {
				mFragments.add(new VoltageFragment());
				mFragmentNames.add(getString(R.string.voltage));
			}
			mFragments.add(new MiscFragment());
			mFragmentNames.add(getString(R.string.misc));
			if (VMHelper.getVMValues().size() == VMHelper.getVMFiles().size()) {
				mFragments.add(new VMFragment());
				mFragmentNames.add(getString(R.string.vm));
			}
			mFragments.add(new InformationFragment());
			mFragmentNames.add(getString(R.string.information));

			mTitle = mDrawerTitle = getTitle();
			mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
			mDrawerList = (ListView) findViewById(R.id.left_drawer);

			mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
					GravityCompat.START);

			mDrawerList.setAdapter(new ArrayAdapter<String>(this,
					R.layout.drawer_list_item, mFragmentNames));
			mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

			getActionBar().setDisplayHomeAsUpEnabled(true);
			getActionBar().setHomeButtonEnabled(true);

			mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
					R.drawable.ic_drawer, R.string.drawer_open,
					R.string.drawer_close) {
				public void onDrawerClosed(View view) {
					getActionBar().setTitle(mTitle);
					invalidateOptionsMenu();
				}

				public void onDrawerOpened(View drawerView) {
					getActionBar().setTitle(mDrawerTitle);
					invalidateOptionsMenu();
				}
			};
			mDrawerLayout.setDrawerListener(mDrawerToggle);

			if (savedInstanceState == null) {
				Fragment fragment = new MainFragment();

				FragmentManager fragmentManager = getSupportFragmentManager();
				fragmentManager.beginTransaction()
						.replace(R.id.content_frame, fragment).commit();
				selectItem(0);
			}
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return mDrawerToggle.onOptionsItemSelected(item);
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
		if (MainFragment.mViewPager != null)
			MainFragment.mViewPager.setCurrentItem(position);
		mDrawerList.setItemChecked(position, true);
		setTitle(mFragmentNames.get(position));
		mDrawerLayout.closeDrawer(mDrawerList);
	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getActionBar().setTitle(mTitle);
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

	public void setPerm() {
		RootHelper.run("chmod 777 " + CPUHelper.MAX_FREQ);
		RootHelper.run("chmod 777 " + CPUHelper.MIN_FREQ);
		RootHelper.run("chmod 777 " + CPUHelper.MAX_SCREEN_OFF);
		RootHelper.run("chmod 777 " + CPUHelper.MIN_SCREEN_ON);
		RootHelper.run("chmod 777 " + CPUHelper.CUR_GOVERNOR);
		RootHelper.run("chmod 777 " + VMHelper.VM_PATH + "/*");
	}
}
