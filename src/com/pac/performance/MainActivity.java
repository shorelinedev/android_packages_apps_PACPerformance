package com.pac.performance;

import java.util.ArrayList;
import java.util.List;

import com.pac.performance.helpers.RootHelper.PartitionType;
import com.pac.performance.utils.Constants;
import com.pac.performance.utils.interfaces.Item;
import com.pac.performance.utils.views.HeaderItem;
import com.pac.performance.utils.views.HeaderListView;
import com.pac.performance.utils.views.ListItem;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Switch;

public class MainActivity extends Activity implements Constants {

    public static int displayHeight;
    public static int displayWidth;

    private ProgressDialog progress;

    private DrawerLayout mDrawerLayout;
    private ListView mLeftDrawer;
    private LinearLayout mRightDrawer;
    private Switch mDrawerSwitch;
    private ActionBarDrawerToggle mDrawerToggle;
    private Spinner mDrawerSpinner;

    private CharSequence mTitle;

    private final List<Item> items = new ArrayList<Item>();
    private int curposition = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Check root access
        if (!rootHelper.rootAccess()) {
            mUtils.toast(getString(R.string.no_root), this);
            finish();
            return;
        }

        // Check busybox installation
        if (!rootHelper.busyboxInstalled()) {
            mUtils.toast(getString(R.string.no_busybox), this);
            finish();
            return;
        }

        setContentView(R.layout.activity_main);

        // Use Asynctask to speed up launching
        new Initialize().execute(savedInstanceState);
    }

    // Initialize all views
    private void setDrawer() {
        mTitle = getTitle();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mRightDrawer = (LinearLayout) findViewById(R.id.right_drawer);
        mLeftDrawer = (ListView) findViewById(R.id.left_drawer);
        mDrawerSwitch = (Switch) findViewById(R.id.drawer_switch);
        mDrawerSpinner = (Spinner) findViewById(R.id.drawer_spinner);

        HeaderListView adapter = new HeaderListView(this, items);

        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
                GravityCompat.START);
        mLeftDrawer.setAdapter(adapter);
        mLeftDrawer.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
                selectItem(position);
            }
        });

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.ic_drawer, 0, 0) {
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
            }

            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(getString(R.string.app_name));
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        mDrawerSwitch.setChecked(mUtils.getBoolean("setonboot", false, this));
        mDrawerSwitch.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mUtils.saveBoolean("setonboot", mDrawerSwitch.isEnabled(),
                        MainActivity.this);
            }
        });

        List<String> delays = new ArrayList<String>();

        ArrayAdapter<String> delayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, delays);
        delayAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        for (int i = 0; i < 11; i++)
            delays.add(getString(R.string.seconds, i));

        mDrawerSpinner.setAdapter(delayAdapter);
        mDrawerSpinner.setSelection(mUtils
                .getInteger("setonbootdelay", 0, this));
        mDrawerSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                    int position, long id) {
                mUtils.saveInteger("setonbootdelay", position,
                        MainActivity.this);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    // Use list to collect all fragments so we can easily organize them
    private void setFragments() {
        items.clear();

        // Information
        items.add(new HeaderItem(getString(R.string.info)));
        items.add(new ListItem(getString(R.string.time_in_state),
                mTimeInStateFragment));
        items.add(new ListItem(getString(R.string.kernel_info),
                mKernelInformationFragment));

        // Fancy Stats
        if (mMemoryStatsFragment.hasSupport() || cpuHelper.getCoreCount() < 9) items
                .add(new HeaderItem(getString(R.string.fancy_stats)));
        // Cpu stats only supports max. 8 cores
        if (cpuHelper.getCoreCount() < 9) items.add(new ListItem(
                getString(R.string.cpu_stats), mCPUStatsFragment));
        if (mMemoryStatsFragment.hasSupport()) items.add(new ListItem(
                getString(R.string.memory_stats), mMemoryStatsFragment));

        // Kernel
        items.add(new HeaderItem(getString(R.string.kernel)));
        items.add(new ListItem(getString(R.string.cpu), mCPUFragment));
        if (cpuVoltageHelper.hasCpuVoltage()) items.add(new ListItem(
                getString(R.string.cpu_voltage), mCPUVoltageFragment));
        if (gpuHelper.hasGpu()) items.add(new ListItem(getString(R.string.gpu),
                mGPUFragment));
        if (screenHelper.hasScreen()) items.add(new ListItem(
                getString(R.string.screen), mScreenFragment));
        items.add(new ListItem(getString(R.string.io_scheduler),
                mIOSchedulerFragment));
        items.add(new ListItem(getString(R.string.low_memory_killer),
                mLowMemoryKillerFragment));
        items.add(new ListItem(getString(R.string.virtual_machine),
                mVirtualMachineFragment));

        // Utilities
        items.add(new HeaderItem(getString(R.string.utilities)));
        items.add(new ListItem(getString(R.string.per_app_mode), null));
        items.add(new ListItem(getString(R.string.custom_commander),
                mCustomCommanderFragment));
        if (rootHelper.getPartitionName(PartitionType.BOOT) != null
                || rootHelper.getPartitionName(PartitionType.RECOVERY) != null
                || rootHelper.getPartitionName(PartitionType.FOTA) != null) items
                .add(new ListItem(getString(R.string.backup), mBackupFragment));
        items.add(new ListItem(getString(R.string.build_prop),
                mBuildpropFragment));

        items.add(new ListItem(getString(R.string.settings), null));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        mDrawerLayout.closeDrawer(mRightDrawer);
        return mDrawerToggle.onOptionsItemSelected(item);
    }

    private void selectItem(final int position) {
        if (items.get(position).isHeader()) return;

        if (items.get(position).getTitle()
                .equals(getString(R.string.per_app_mode))) {
            startActivity(new Intent(this, PerAppModeActivity.class));
            return;
        }

        if (items.get(position).getTitle().equals(getString(R.string.settings))) {
            mDrawerLayout.openDrawer(mRightDrawer);
            mDrawerLayout.closeDrawer(mLeftDrawer);
            return;
        }

        if (curposition != position || position == 1) {

            if (mCustomCommanderFragment.actionBarSwitch != null) mCustomCommanderFragment.actionBarSwitch
                    .setVisibility(items.get(position).getFragment() == mCustomCommanderFragment ? View.VISIBLE
                            : View.GONE);

            new Thread() {
                public void run() {
                    new Thread() {
                        public void run() {}
                    }.start();

                    getFragmentManager()
                            .beginTransaction()
                            .replace(R.id.content_frame,
                                    items.get(position).getFragment()).commit();

                    curposition = position;

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            setTitle(items.get(position).getTitle());
                            mLeftDrawer.setItemChecked(curposition, true);
                        }
                    });
                }
            }.start();
        }

        mDrawerLayout.closeDrawer(mLeftDrawer);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }

    private class Initialize extends AsyncTask<Bundle, Void, Bundle> {

        @Override
        protected void onPreExecute() {
            getActionBar().hide();

            progress = new ProgressDialog(MainActivity.this);
            progress.setMessage(getString(R.string.loading));
            progress.show();

            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(final Bundle result) {
            new Thread() {
                public void run() {

                    /*
                     * Save kernel version to make sure the user still using the
                     * current kernel on boot
                     */
                    mUtils.saveString("kernel_version",
                            mUtils.readFile(PROC_VERSION), MainActivity.this);

                    String[] files = { String.format(CPU_MAX_FREQ, 0),
                            String.format(CPU_MIN_FREQ, 0),
                            String.format(CPU_SCALING_GOVERNOR, 0) };

                    for (String file : files)
                        rootHelper.runCommand("chmod 644 " + file);

                    setFragments();

                    runOnUiThread(new Runnable() {
                        public void run() {
                            setDrawer();

                            if (result == null) selectItem(curposition);

                            getActionBar().show();

                            mDrawerToggle.syncState();
                            mDrawerLayout.openDrawer(mLeftDrawer);

                            progress.hide();
                        }
                    });
                }
            }.start();

            super.onPostExecute(result);
        }

        @Override
        protected Bundle doInBackground(Bundle... params) {
            return params[0];
        }
    }
}
