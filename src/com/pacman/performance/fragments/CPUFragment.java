package com.pacman.performance.fragments;

import java.util.Locale;

import com.pacman.performance.R;
import com.pacman.performance.GenericPathReaderActivity;
import com.pacman.performance.utils.Constants;
import com.pacman.performance.utils.CommandControl.CommandType;
import com.pacman.performance.utils.Dialog.DialogReturn;
import com.pacman.performance.utils.views.PreferenceView.CustomCategory;
import com.pacman.performance.utils.views.PreferenceView.CustomCheckBox;
import com.pacman.performance.utils.views.PreferenceView.CustomPreference;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;

public class CPUFragment extends PreferenceFragment implements Constants {

    private final Handler hand = new Handler();

    private PreferenceScreen root;
    private CheckBoxPreference[] mCoreBoxes;
    private CustomPreference mCpuMaxScaling, mCpuMinScaling;
    private CustomPreference mGovernorScaling, mGovernorTunables;
    private CustomPreference mMcPowerSaving;
    private CustomCheckBox mMpdecision, mIntelliPlug, mIntelliPlugEco;

    private String[] mAvailableFreqs;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        root = getPreferenceManager().createPreferenceScreen(getActivity());

        setPreferenceScreen(root);

        getActivity().runOnUiThread(run);
        hand.post(run2);
    }

    private final Runnable run = new Runnable() {

        @Override
        public void run() {
            mAvailableFreqs = new String[cpuHelper.getCpuFreqs().length];
            for (int i = 0; i < cpuHelper.getCpuFreqs().length; i++)
                mAvailableFreqs[i] = (Integer
                        .parseInt(cpuHelper.getCpuFreqs()[i]) / 1000)
                        + getString(R.string.mhz);

            root.addPreference(new CustomCategory(getActivity(),
                    getString(R.string.cpu_stats)));

            mCoreBoxes = new CheckBoxPreference[cpuHelper.getCoreCount()];
            for (int i = 0; i < cpuHelper.getCoreCount(); i++) {
                final int mCoreFreq = cpuHelper.getCurFreq(i);
                mCoreBoxes[i] = new CustomCheckBox(getActivity(),
                        mCoreFreq != 0, getString(R.string.core, i),
                        mCoreFreq == 0 ? getString(R.string.offline)
                                : String.valueOf(mCoreFreq / 1000)
                                        + getString(R.string.mhz));

                root.addPreference(mCoreBoxes[i]);
            }

            root.addPreference(new CustomCategory(getActivity(),
                    getString(R.string.parameters)));

            mCpuMaxScaling = new CustomPreference(getActivity(),
                    getString(R.string.cpu_max_freq),
                    (cpuHelper.getMaxFreq(0) / 1000) + getString(R.string.mhz));

            root.addPreference(mCpuMaxScaling);

            mCpuMinScaling = new CustomPreference(getActivity(),
                    getString(R.string.cpu_min_freq),
                    (cpuHelper.getMinFreq(0) / 1000) + getString(R.string.mhz));

            root.addPreference(mCpuMinScaling);

            mGovernorScaling = new CustomPreference(getActivity(),
                    getString(R.string.cpu_governor), cpuHelper.getGovernor(0));

            root.addPreference(mGovernorScaling);

            root.addPreference(new CustomCategory(getActivity(),
                    getString(R.string.advanced)));

            mGovernorTunables = new CustomPreference(getActivity(),
                    getString(R.string.cpu_governor_tunables),
                    getString(R.string.cpu_governor_tunables_summary));

            root.addPreference(mGovernorTunables);

            if (cpuHelper.hasMcPowerSaving()) {
                mMcPowerSaving = new CustomPreference(
                        getActivity(),
                        getString(R.string.mc_power_saving),
                        getString(R.string.mc_power_saving_summary)
                                + ": "
                                + getResources().getStringArray(
                                        R.array.mc_power_saving_items)[cpuHelper
                                        .getMcPowerSaving()]);

                root.addPreference(mMcPowerSaving);
            }

            if (cpuHelper.hasMpdecision())
                root.addPreference(new CustomCategory(getActivity(),
                        getString(R.string.hotplug)));

            if (cpuHelper.hasMpdecision()) {
                mMpdecision = new CustomCheckBox(getActivity(),
                        cpuHelper.isMpdecisionActive(),
                        getString(R.string.mpdecision),
                        getString(R.string.mpdecision_summary));

                root.addPreference(mMpdecision);
            }

            if (cpuHelper.hasIntelliPlug()) {
                mIntelliPlug = new CustomCheckBox(getActivity(),
                        cpuHelper.isIntelliPlugActive(),
                        getString(R.string.intelliplug),
                        getString(R.string.intelliplug_summary));

                root.addPreference(mIntelliPlug);
            }

            if (cpuHelper.hasIntelliPlugEco()) {
                mIntelliPlugEco = new CustomCheckBox(getActivity(),
                        cpuHelper.isIntelliPlugEcoActive(),
                        getString(R.string.intelliplug_eco_mode), null);

                root.addPreference(mIntelliPlugEco);
            }
        }
    };

    private final Runnable run2 = new Runnable() {
        @Override
        public void run() {
            for (int i = 0; i < cpuHelper.getCoreCount(); i++) {
                final int mCoreFreq = cpuHelper.getCurFreq(i);
                if (mCoreBoxes != null) {
                    mCoreBoxes[i]
                            .setSummary(mCoreFreq == 0 ? getString(R.string.offline)
                                    : String.valueOf(mCoreFreq / 1000)
                                            + getString(R.string.mhz));
                    mCoreBoxes[i].setChecked(mCoreFreq != 0);
                }
            }

            if (mCpuMaxScaling != null && mCpuMinScaling != null) {
                mCpuMaxScaling.setSummary((cpuHelper.getMaxFreq(0) / 1000)
                        + getString(R.string.mhz));
                mCpuMinScaling.setSummary((cpuHelper.getMinFreq(0) / 1000)
                        + getString(R.string.mhz));
            }

            hand.postDelayed(run2, 500);
        }
    };

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen,
            final Preference preference) {

        for (int i = 0; i < cpuHelper.getCoreCount(); i++)
            if (preference == mCoreBoxes[i]) {
                if (i != 0) mCommandControl.runCommand(
                        mCoreBoxes[i].isChecked() ? "1" : "0",
                        String.format(CPU_CORE_ONLINE, i), CommandType.GENERIC,
                        -1, getActivity());
                else {
                    mCoreBoxes[i].setChecked(true);
                    mUtils.toast(getString(R.string.turn_off_not_possible, i),
                            getActivity());
                }
            }

        if (preference == mCpuMaxScaling)
            mDialog.showDialogList(mAvailableFreqs, cpuHelper.getCpuFreqs(),
                    CPU_MAX_FREQ, new DialogReturn() {
                        @Override
                        public void dialogReturn(String value) {
                            preference.setSummary(value);
                        }
                    }, CommandType.CPU, -1, getActivity());

        if (preference == mCpuMinScaling)
            mDialog.showDialogList(mAvailableFreqs, cpuHelper.getCpuFreqs(),
                    CPU_MIN_FREQ, new DialogReturn() {
                        @Override
                        public void dialogReturn(String value) {
                            preference.setSummary(value);
                        }
                    }, CommandType.CPU, -1, getActivity());

        if (preference == mGovernorScaling)
            mDialog.showDialogList(cpuHelper.getCpuGovernors(), null,
                    CPU_SCALING_GOVERNOR, new DialogReturn() {
                        @Override
                        public void dialogReturn(String value) {
                            preference.setSummary(value);
                        }
                    }, CommandType.CPU, -1, getActivity());

        if (preference == mGovernorTunables) {
            String governor = cpuHelper.getGovernor(0);
            Intent i = new Intent(getActivity(),
                    GenericPathReaderActivity.class);
            Bundle args = new Bundle();
            args.putString(
                    GenericPathReaderActivity.ARG_TITLE,
                    getString(R.string.cpu_governor_tunables) + ": "
                            + governor.toUpperCase(Locale.getDefault()));
            args.putString(GenericPathReaderActivity.ARG_PATH,
                    String.format(CPU_GOVERNOR_TUNABLES, governor));
            args.putString(
                    GenericPathReaderActivity.ARG_ERROR,
                    getString(R.string.not_tunable,
                            governor.toUpperCase(Locale.getDefault())));
            i.putExtras(args);

            startActivity(i);
        }

        if (preference == mMcPowerSaving)
            mDialog.showDialogList(
                    getResources()
                            .getStringArray(R.array.mc_power_saving_items),
                    new String[] { "0", "1", "2" }, CPU_MC_POWER_SAVING,
                    new DialogReturn() {
                        @Override
                        public void dialogReturn(String value) {
                            preference
                                    .setSummary(getString(R.string.mc_power_saving_summary)
                                            + ": " + value);
                        }
                    }, CommandType.GENERIC, -1, getActivity());

        if (preference == mMpdecision) {
            if (mMpdecision.isChecked()) mCommandControl.startModule(CPU_MPDEC,
                    true, getActivity());
            else mCommandControl.stopModule(CPU_MPDEC, true, getActivity());
        }

        if (preference == mIntelliPlug) {
            if (mIntelliPlug.isChecked()) mCommandControl.runGeneric(
                    CPU_INTELLI_PLUG, "1", -1, getActivity());
            else {
                mCommandControl.runGeneric(CPU_INTELLI_PLUG, "0", -1,
                        getActivity());
                mCommandControl.bringCoresOnline();
            }
        }

        if (preference == mIntelliPlugEco)
            mCommandControl.runGeneric(CPU_INTELLI_PLUG_ECO,
                    mIntelliPlugEco.isChecked() ? "1" : "0", -1, getActivity());

        return true;
    }

    @Override
    public void onDestroy() {
        hand.removeCallbacks(run2);
        super.onDestroy();
    };

}
