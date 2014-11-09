package com.pacman.performance;

import java.util.ArrayList;
import java.util.List;

import com.pacman.performance.R;
import com.pacman.performance.helpers.RootHelper.PartitionType;
import com.pacman.performance.services.NotificationStatsService;
import com.pacman.performance.utils.Constants;
import com.pacman.performance.utils.views.HeaderItem;
import com.pacman.performance.utils.views.GenericView.GenericAdapter;
import com.pacman.performance.utils.views.GenericView.Item;
import com.pacman.performance.utils.views.ListItem;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
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

@SuppressWarnings("deprecation")
public class MainActivity extends Activity implements Constants {

    public static int displayHeight;
    public static int displayWidth;

    private ProgressDialog progress;

    private DrawerLayout mDrawerLayout;
    private ListView mLeftDrawer;
    private LinearLayout mRightDrawer;
    private ActionBarDrawerToggle mDrawerToggle;
    private Switch mSetonbootSwitch;
    private Spinner mDelaySpinner;
    private Switch mNotiSwitch;

    private CharSequence mTitle;

    private final List<Item> items = new ArrayList<Item>();
    private int curposition = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progress = new ProgressDialog(this);
        progress.setMessage(getString(R.string.loading));
        progress.show();

        new Handler().postDelayed(run, 200);
    }

    private final Runnable run = new Runnable() {

        @Override
        public void run() {

            // Check root access and busybox installation
            boolean hasRoot = rootHelper.rootAccess();
            boolean hasBusybox = rootHelper.busyboxInstalled();

            if (!hasRoot || !hasBusybox) {
                Intent i = new Intent(MainActivity.this, TextActivity.class);
                Bundle args = new Bundle();
                args.putString(TextActivity.ARG_TEXT,
                        !hasRoot ? getString(R.string.no_root)
                                : getString(R.string.no_busybox));
                Log.d(TAG, !hasRoot ? getString(R.string.no_root)
                        : getString(R.string.no_busybox));
                i.putExtras(args);
                startActivity(i);

                finish();
                return;
            }

            /*
             * Save kernel version to make sure the user still using the current
             * kernel on boot
             */
            mUtils.saveString("kernel_version", mUtils.readFile(PROC_VERSION),
                    MainActivity.this);

            String[] files = { String.format(CPU_MAX_FREQ, 0),
                    String.format(CPU_MIN_FREQ, 0),
                    String.format(CPU_SCALING_GOVERNOR, 0) };

            for (String file : files)
                rootHelper.runCommand("chmod 644 " + file);

            setFragments();

            setDrawer();
            selectItem(curposition);

            mDrawerToggle.syncState();

            progress.dismiss();
        }
    };

    // Initialize all views
    private void setDrawer() {
        mTitle = getTitle();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mRightDrawer = (LinearLayout) findViewById(R.id.right_drawer);
        mLeftDrawer = (ListView) findViewById(R.id.left_drawer);
        mSetonbootSwitch = (Switch) findViewById(R.id.setonboot_switch);
        mDelaySpinner = (Spinner) findViewById(R.id.delay_spinner);
        mNotiSwitch = (Switch) findViewById(R.id.noti_switch);

        GenericAdapter adapter = new GenericAdapter(this, items);

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

        mSetonbootSwitch
                .setChecked(mUtils.getBoolean("setonboot", false, this));
        mSetonbootSwitch.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mUtils.saveBoolean("setonboot", mSetonbootSwitch.isChecked(),
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

        mDelaySpinner.setAdapter(delayAdapter);
        mDelaySpinner
                .setSelection(mUtils.getInteger("setonbootdelay", 0, this));
        mDelaySpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                    int position, long id) {
                mUtils.saveInteger("setonbootdelay", position,
                        MainActivity.this);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        mNotiSwitch.setChecked(mUtils.getBoolean("notistats", false, this));
        mNotiSwitch.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mUtils.saveBoolean("notistats", mNotiSwitch.isChecked(),
                        MainActivity.this);

                Intent i = new Intent(MainActivity.this,
                        NotificationStatsService.class);

                if (mNotiSwitch.isChecked()) startService(i);
                else stopService(i);
            }
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
        if (mMemoryStatsFragment.hasSupport() || cpuHelper.getCoreCount() < 9)
            items.add(new HeaderItem(getString(R.string.fancy_stats)));
        // Cpu stats only supports max. 8 cores
        if (cpuHelper.getCoreCount() < 9)
            items.add(new ListItem(getString(R.string.cpu_stats),
                    mCPUStatsFragment));
        if (mMemoryStatsFragment.hasSupport())
            items.add(new ListItem(getString(R.string.memory_stats),
                    mMemoryStatsFragment));

        // Kernel
        items.add(new HeaderItem(getString(R.string.kernel)));
        items.add(new ListItem(getString(R.string.cpu), mCPUFragment));
        if (cpuVoltageHelper.hasCpuVoltage())
            items.add(new ListItem(getString(R.string.cpu_voltage),
                    mCPUVoltageFragment));
        if (gpuHelper.hasGpu())
            items.add(new ListItem(getString(R.string.gpu), mGPUFragment));
        if (screenHelper.hasScreen())
            items.add(new ListItem(getString(R.string.screen), mScreenFragment));
        items.add(new ListItem(getString(R.string.io_scheduler),
                mIOSchedulerFragment));
        if (kernelsamepagemergingHelper.hasKsm())
            items.add(new ListItem(getString(R.string.ksm),
                    mKernelSamepageMerging));
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
                || rootHelper.getPartitionName(PartitionType.FOTA) != null)
            items.add(new ListItem(getString(R.string.backup), mBackupFragment));
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
        if (items.get(position).isHeader()) {
            mLeftDrawer.setItemChecked(curposition, true);
            return;
        }

        if (items.get(position).getTitle().equals(getString(R.string.settings))) {
            mDrawerLayout.openDrawer(mRightDrawer);
            mDrawerLayout.closeDrawer(mLeftDrawer);
            return;
        }

        if (mCustomCommanderFragment.actionBarSwitch != null)
            mCustomCommanderFragment.actionBarSwitch
                    .setVisibility(items.get(position).getFragment() == mCustomCommanderFragment ? View.VISIBLE
                            : View.GONE);

        if (items.get(position).getTitle()
                .equals(getString(R.string.per_app_mode))) {
            startActivity(new Intent(MainActivity.this,
                    PerAppModeActivity.class));
        } else {
            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.content_frame,
                            items.get(position).getFragment()).commit();
            mDrawerLayout.closeDrawer(mLeftDrawer);
        }

        curposition = position;

        setTitle(items.get(position).getTitle());
        mLeftDrawer.setItemChecked(curposition, true);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(mLeftDrawer)) {
            super.onBackPressed();
        } else {
            mDrawerLayout.openDrawer(mLeftDrawer);
        }
    }
}
