package com.pac.performance;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.pac.performance.R;
import com.pac.performance.services.PerAppModesService;
import com.pac.performance.utils.Constants;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

public class PerAppModeActivity extends Activity implements Constants {

    private enum ProfileType {
        NAME, MAXCPU, MINCPU, GOVERNOR
    }

    public Switch actionBarSwitch;
    public ActionBar actionBar;

    private Fragment[] fragments;
    private String[] tabs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(enter_anim, exit_anim);
        setContentView(R.layout.per_app_mode_main);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setTitle(getString(R.string.per_app_mode));

        actionBarSwitch = new Switch(this);
        actionBarSwitch
                .setChecked(mUtils.getBoolean("perappmode", false, this));

        getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM,
                ActionBar.DISPLAY_SHOW_CUSTOM);
        getActionBar().setCustomView(
                actionBarSwitch,
                new ActionBar.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT,
                        ActionBar.LayoutParams.WRAP_CONTENT,
                        Gravity.CENTER_VERTICAL | Gravity.RIGHT));
        actionBarSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUtils.saveBoolean("perappmode", actionBarSwitch.isChecked(),
                        PerAppModeActivity.this);
                if (actionBarSwitch.isChecked()) startService(new Intent(
                        PerAppModeActivity.this, PerAppModesService.class));
                else stopService(new Intent(PerAppModeActivity.this,
                        PerAppModesService.class));
            }
        });

        fragments = new Fragment[] { new Apps(), new Profiles() };
        tabs = getResources().getStringArray(R.array.per_app_mode_tab);

        final SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(
                getFragmentManager());

        actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        final ViewPager mViewPager = (ViewPager) findViewById(R.id.pager);
        if (mViewPager == null || mSectionsPagerAdapter == null) return;
        mViewPager.setAdapter(mSectionsPagerAdapter);

        mViewPager
                .setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
                    @Override
                    public void onPageSelected(int position) {
                        actionBar.setSelectedNavigationItem(position);
                    }
                });

        actionBar.removeAllTabs();
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++)
            actionBar.addTab(actionBar.newTab()
                    .setText(mSectionsPagerAdapter.getPageTitle(i))
                    .setTabListener(new TabListener() {
                        @Override
                        public void onTabUnselected(Tab tab,
                                FragmentTransaction ft) {}

                        @Override
                        public void onTabSelected(Tab tab,
                                FragmentTransaction ft) {
                            mViewPager.setCurrentItem(tab.getPosition());
                        }

                        @Override
                        public void onTabReselected(Tab tab,
                                FragmentTransaction ft) {}
                    }));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(enter_anim, exit_anim);
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments[position];
        }

        @Override
        public int getCount() {
            return fragments.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabs[position];
        }
    }

    private class Apps extends PreferenceFragment implements Constants {

        private Preference[] mApps;

        public final List<String> packages = new ArrayList<String>();

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setHasOptionsMenu(true);

            final ProgressDialog progress = new ProgressDialog(getActivity());
            progress.setMessage(getString(R.string.loading));
            progress.show();

            final PreferenceScreen root = getPreferenceManager()
                    .createPreferenceScreen(getActivity());
            setPreferenceScreen(root);

            new Thread() {
                public void run() {
                    PackageManager packageManager = getActivity()
                            .getPackageManager();

                    ArrayList<ApplicationInfo> appsUnsorted = new ArrayList<ApplicationInfo>();
                    for (ApplicationInfo info : getActivity()
                            .getPackageManager().getInstalledApplications(
                                    PackageManager.GET_META_DATA))
                        try {
                            if (null != getActivity()
                                    .getPackageManager()
                                    .getLaunchIntentForPackage(info.packageName)) appsUnsorted
                                    .add(info);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    List<String> sorting = new ArrayList<String>();
                    for (ApplicationInfo app : appsUnsorted)
                        sorting.add(app.loadLabel(packageManager).toString());

                    Collections.sort(sorting);

                    List<ApplicationInfo> apps = new ArrayList<ApplicationInfo>();
                    for (String appName : sorting)
                        for (ApplicationInfo app : appsUnsorted)
                            if (appName.equals(app.loadLabel(packageManager)
                                    .toString())) apps.add(app);

                    final List<String> titles = new ArrayList<String>();
                    final List<String> summaries = new ArrayList<String>();
                    final List<Drawable> icons = new ArrayList<Drawable>();

                    String pack = "";
                    for (ApplicationInfo app : apps)
                        if (!app.packageName.equals(getActivity()
                                .getPackageName())
                                && !pack.contains(app.packageName)) {
                            pack = pack + app.packageName;
                            titles.add(app.loadLabel(packageManager).toString());
                            summaries.add(mUtils.getString(app.packageName, "",
                                    getActivity()));
                            packages.add(app.packageName);
                            icons.add(app.loadIcon(packageManager));
                        }

                    mApps = new Preference[titles.size()];
                    for (int i = 0; i < titles.size(); i++) {
                        mApps[i] = prefHelper.setPreference(titles.get(i),
                                summaries.get(i), getActivity());
                        mApps[i].setIcon(icons.get(i));
                        root.addPreference(mApps[i]);
                    }
                    progress.dismiss();
                }
            }.start();

        }

        @Override
        public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen,
                Preference preference) {

            for (int i = 0; i < mApps.length; i++)
                if (preference == mApps[i]) menuDialog(preference, preference
                        .getSummary().toString(), i);

            return true;
        }

        private void menuDialog(final Preference preference,
                final String profile, final int position) {
            final Profiles pro = new Profiles();
            List<String> profilesList = pro.getContent(getActivity(),
                    ProfileType.NAME);
            final String[] profiles = new String[profilesList.size()];
            for (int i = 0; i < profilesList.size(); i++)
                profiles[i] = profilesList.get(i);

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            if (!profiles[0].isEmpty()) builder.setItems(profiles,
                    new OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            preference.setSummary(profiles[which]);
                            mUtils.saveString(packages.get(position),
                                    profiles[which], getActivity());
                        }
                    });
            if (!profile.isEmpty()) builder.setNeutralButton(
                    getString(R.string.delete_profile), new OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mUtils.saveString(packages.get(position), "",
                                    getActivity());
                            preference.setSummary("");
                        }
                    });

            if (profiles[0].isEmpty() && profile.isEmpty()) mUtils.toast(
                    getString(R.string.no_profiles), getActivity());
            else builder.show();
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            if (item.getItemId() == android.R.id.home) {
                finish();
                overridePendingTransition(enter_anim, exit_anim);
            }
            return super.onOptionsItemSelected(item);
        }

    }

    private class Profiles extends PreferenceFragment implements Constants {

        private PreferenceScreen root;
        private Preference[] mProfiles;

        private final String prefName = "perappmodes";
        private final String nameSplit = "qwdwqfwefew";
        private final String cpuMaxSplit = "gsgerherer";
        private final String cpuMinSplit = "sdfafwefwe";
        private final String governorSplit = "rherhtrjtr";

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setHasOptionsMenu(true);

            root = getPreferenceManager().createPreferenceScreen(getActivity());
            refresh();
            setPreferenceScreen(root);
        }

        private void refresh() {
            root.removeAll();
            List<String> content = getContent(getActivity(), ProfileType.NAME);
            if (!content.get(0).isEmpty()) {
                mProfiles = new Preference[content.size()];
                for (int i = 0; i < content.size(); i++) {
                    mProfiles[i] = prefHelper.setPreference(content.get(i),
                            null, getActivity());

                    root.addPreference(mProfiles[i]);
                }
            } else root.addPreference(prefHelper.setPreference(null,
                    getString(R.string.no_profiles), getActivity()));
        }

        @Override
        public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen,
                Preference preference) {

            if (mProfiles != null) for (int i = 0; i < mProfiles.length; i++)
                if (preference == mProfiles[i]) menuDialog(
                        getContent(getActivity(), ProfileType.NAME).get(i),
                        getContent(getActivity(), ProfileType.MAXCPU).get(i),
                        getContent(getActivity(), ProfileType.MINCPU).get(i),
                        getContent(getActivity(), ProfileType.GOVERNOR).get(i),
                        preference);

            return true;
        }

        private void addDialog(final boolean modify, final String name,
                String maxCpu, String minCpu, String governor) {
            LinearLayout layout = new LinearLayout(getActivity());
            layout.setOrientation(LinearLayout.VERTICAL);
            layout.setPadding(10, 10, 10, 10);

            final EditText nameEditor = new EditText(getActivity());
            layout.addView(nameEditor);
            if (modify) nameEditor.setText(name);
            else nameEditor.setHint(getString(R.string.name));

            TextView maxCpuText = new TextView(getActivity());
            maxCpuText.setText(getString(R.string.cpu_max_freq));
            layout.addView(maxCpuText);

            List<String> cpuFreqs = new ArrayList<String>();

            for (String freq : cpuHelper.getCpuFreqs())
                cpuFreqs.add((Integer.parseInt(freq) / 1000)
                        + getString(R.string.mhz));

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                    getActivity(), android.R.layout.simple_spinner_item,
                    cpuFreqs);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            final Spinner maxCpuSpinner = new Spinner(getActivity());
            maxCpuSpinner.setAdapter(adapter);
            if (modify) maxCpuSpinner.setSelection(cpuFreqs.indexOf((Integer
                    .parseInt(maxCpu) / 1000) + getString(R.string.mhz)));
            layout.addView(maxCpuSpinner);

            TextView minCpuText = new TextView(getActivity());
            minCpuText.setText(getString(R.string.cpu_min_freq));
            layout.addView(minCpuText);

            final Spinner minCpuSpinner = new Spinner(getActivity());
            minCpuSpinner.setAdapter(adapter);
            if (modify) minCpuSpinner.setSelection(cpuFreqs.indexOf((Integer
                    .parseInt(minCpu) / 1000) + getString(R.string.mhz)));
            layout.addView(minCpuSpinner);

            TextView governorText = new TextView(getActivity());
            governorText.setText(getString(R.string.cpu_governor));
            layout.addView(governorText);

            List<String> governors = new ArrayList<String>();

            for (String gov : cpuHelper.getCpuGovernors())
                governors.add(gov);

            adapter = new ArrayAdapter<String>(getActivity(),
                    android.R.layout.simple_spinner_item, governors);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            final Spinner governorSpinner = new Spinner(getActivity());
            governorSpinner.setAdapter(adapter);
            if (modify) governorSpinner.setSelection(governors
                    .indexOf(governor));
            layout.addView(governorSpinner);

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setView(layout)
                    .setNegativeButton(getString(android.R.string.cancel),
                            new OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                        int which) {}
                            })
                    .setPositiveButton(getString(android.R.string.ok),
                            new OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                        int which) {
                                    if (nameEditor.getText().toString()
                                            .isEmpty()) {
                                        mUtils.toast(
                                                getString(R.string.empty,
                                                        nameEditor.getHint()),
                                                getActivity());
                                        return;
                                    }

                                    if (modify) overwrite(
                                            name,
                                            nameEditor.getText().toString(),
                                            cpuHelper.getCpuFreqs()[maxCpuSpinner
                                                    .getSelectedItemPosition()],
                                            cpuHelper.getCpuFreqs()[minCpuSpinner
                                                    .getSelectedItemPosition()],
                                            governorSpinner.getSelectedItem()
                                                    .toString());
                                    else saveProfile(
                                            nameEditor.getText().toString(),
                                            cpuHelper.getCpuFreqs()[maxCpuSpinner
                                                    .getSelectedItemPosition()],
                                            cpuHelper.getCpuFreqs()[minCpuSpinner
                                                    .getSelectedItemPosition()],
                                            governorSpinner.getSelectedItem()
                                                    .toString());
                                }
                            }).show();
        }

        private void menuDialog(final String name, final String maxCpu,
                final String minCpu, final String governor,
                final Preference preference) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setItems(
                    getResources().getStringArray(R.array.per_app_mode_profile),
                    new OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case 0:
                                    addDialog(true, name, maxCpu, minCpu,
                                            governor);
                                    break;
                                case 1:
                                    deleteProfile(getActivity(), name);
                                    break;
                            }
                        }
                    }).show();
        }

        private void saveProfile(String name, String maxCpu, String minCpu,
                String governor) {
            String saved = mUtils.getString(prefName, "", getActivity());

            if (saved.contains(name + nameSplit)) {
                mUtils.toast(
                        getString(R.string.per_app_mode_already_exist, name),
                        getActivity());
                return;
            }

            mUtils.saveString(prefName, saved.isEmpty() ? name + nameSplit
                    + maxCpu + cpuMaxSplit + minCpu + cpuMinSplit + governor
                    + governorSplit : saved + name + nameSplit + maxCpu
                    + cpuMaxSplit + minCpu + cpuMinSplit + governor
                    + governorSplit, getActivity());

            refresh();
        }

        private void overwrite(String oldname, String name, String maxCpu,
                String minCpu, String governor) {
            String saved = mUtils.getString(prefName, "", getActivity());

            List<String> contents = new ArrayList<String>();

            for (String content : saved.split(governorSplit))
                if (content.split(nameSplit)[0].equals(oldname)) contents
                        .add(name + nameSplit + maxCpu + cpuMaxSplit + minCpu
                                + cpuMinSplit + governor + governorSplit);
                else contents.add(content + governorSplit);

            String finalContent = "";
            for (String content : contents)
                finalContent = finalContent + content;

            mUtils.saveString(prefName, finalContent, getActivity());

            refresh();
        }

        private void deleteProfile(Context context, String name) {
            String saved = mUtils.getString(prefName, "", context);

            List<String> contents = new ArrayList<String>();

            for (String content : saved.split(governorSplit))
                if (!content.split(nameSplit)[0].equals(name)) contents
                        .add(content + governorSplit);

            String finalContent = "";
            for (String content : contents)
                finalContent = finalContent + content;

            mUtils.saveString(prefName, finalContent, getActivity());

            refresh();
        }

        public List<String> getContent(Context context, ProfileType profile) {
            List<String> content = new ArrayList<String>();

            String saved = mUtils.getString(prefName, "", context);

            for (String config : saved.split(governorSplit))
                if (saved.split(governorSplit).length > 0) switch (profile) {
                    case NAME:
                        content.add(config.split(nameSplit)[0]);
                        break;
                    case MAXCPU:
                        content.add(config.split(nameSplit)[1]
                                .split(cpuMaxSplit)[0]);
                        break;
                    case MINCPU:
                        content.add(config.split(cpuMaxSplit)[1]
                                .split(cpuMinSplit)[0]);
                        break;
                    case GOVERNOR:
                        content.add(config.split(cpuMinSplit)[1]
                                .split(governorSplit)[0]);
                        break;
                }

            return content;
        }

        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
            inflater.inflate(R.menu.add, menu);
            super.onCreateOptionsMenu(menu, inflater);
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            if (item.getItemId() == R.id.menu_add) addDialog(false, null, null,
                    null, null);
            if (item.getItemId() == android.R.id.home) {
                finish();
                overridePendingTransition(enter_anim, exit_anim);
            }
            return super.onOptionsItemSelected(item);
        }

    }

}
