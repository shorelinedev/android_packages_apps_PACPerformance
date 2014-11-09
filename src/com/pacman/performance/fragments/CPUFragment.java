package com.pacman.performance.fragments;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.pacman.performance.GenericPathReaderActivity;
import com.pacman.performance.R;
import com.pacman.performance.utils.CommandControl.CommandType;
import com.pacman.performance.utils.Constants;
import com.pacman.performance.utils.views.CheckBoxView;
import com.pacman.performance.utils.views.CheckBoxView.OnCheckBoxListener;
import com.pacman.performance.utils.views.CommonView;
import com.pacman.performance.utils.views.CommonView.OnClickListener;
import com.pacman.performance.utils.views.GenericView.GenericAdapter;
import com.pacman.performance.utils.views.GenericView.Item;
import com.pacman.performance.utils.views.HeaderItem;
import com.pacman.performance.utils.views.PopupView;
import com.pacman.performance.utils.views.PopupView.OnItemClickListener;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class CPUFragment extends Fragment implements Constants,
        OnCheckBoxListener, OnItemClickListener, OnClickListener {

    private final Handler hand = new Handler();

    private ListView list;
    private List<Item> views = new ArrayList<Item>();

    private List<String> mAvailableFreqs = new ArrayList<String>();

    private CheckBoxView[] mCoreBoxes;
    private PopupView mCpuMaxScaling, mCpuMinScaling;
    private PopupView mGovernorScaling;
    private CommonView mGovernorTunables;
    private PopupView mMcPowerSaving;
    private CheckBoxView mMpdecision, mIntelliPlug, mIntelliPlugEco;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        list = new ListView(getActivity());
        list.setPadding(20, 0, 20, 0);

        getActivity().runOnUiThread(run);
        hand.post(run2);
        return list;
    }

    private final Runnable run = new Runnable() {

        @Override
        public void run() {
            views.clear();
            mAvailableFreqs.clear();

            for (String freq : cpuHelper.getCpuFreqs())
                mAvailableFreqs.add((Integer.parseInt(freq) / 1000)
                        + getString(R.string.mhz));

            views.add(new HeaderItem(getString(R.string.cpu_stats)));

            mCoreBoxes = new CheckBoxView[cpuHelper.getCoreCount()];
            for (int i = 0; i < cpuHelper.getCoreCount(); i++) {
                final int mCoreFreq = cpuHelper.getCurFreq(i);
                mCoreBoxes[i] = new CheckBoxView();
                mCoreBoxes[i].setTitle(getString(R.string.core, i));
                mCoreBoxes[i]
                        .setSummary(mCoreFreq == 0 ? getString(R.string.offline)
                                : String.valueOf(mCoreFreq / 1000)
                                        + getString(R.string.mhz));
                mCoreBoxes[i].setChecked(mCoreFreq != 0);
                mCoreBoxes[i].setOnCheckBoxListener(CPUFragment.this);

                views.add(mCoreBoxes[i]);
            }

            views.add(new HeaderItem(getString(R.string.parameters)));

            mCpuMaxScaling = new PopupView(getActivity(), mAvailableFreqs);
            mCpuMaxScaling.setTitle(getString(R.string.cpu_max_freq));
            mCpuMaxScaling.setSummary(getString(R.string.cpu_max_freq_summary));
            mCpuMaxScaling.setItem(mAvailableFreqs.indexOf((cpuHelper
                    .getMaxFreq(0) / 1000) + getString(R.string.mhz)));
            mCpuMaxScaling.setOnItemClickListener(CPUFragment.this);
            views.add(mCpuMaxScaling);

            mCpuMinScaling = new PopupView(getActivity(), mAvailableFreqs);
            mCpuMinScaling.setTitle(getString(R.string.cpu_min_freq));
            mCpuMinScaling.setSummary(getString(R.string.cpu_min_freq_summary));
            mCpuMinScaling.setItem(mAvailableFreqs.indexOf((cpuHelper
                    .getMinFreq(0) / 1000) + getString(R.string.mhz)));
            mCpuMinScaling.setOnItemClickListener(CPUFragment.this);
            views.add(mCpuMinScaling);

            mGovernorScaling = new PopupView(getActivity(),
                    cpuHelper.getCpuGovernors());
            mGovernorScaling.setTitle(getString(R.string.cpu_governor));
            mGovernorScaling
                    .setSummary(getString(R.string.cpu_governor_summary));
            mGovernorScaling.setItem(cpuHelper.getGovernor(0));
            mGovernorScaling.setOnItemClickListener(CPUFragment.this);
            views.add(mGovernorScaling);

            mGovernorTunables = new CommonView();
            mGovernorTunables
                    .setTitle(getString(R.string.cpu_governor_tunables));
            mGovernorTunables
                    .setSummary(getString(R.string.cpu_governor_tunables_summary));
            mGovernorTunables.setOnClickListener(CPUFragment.this);
            views.add(mGovernorTunables);

            if (cpuHelper.hasMcPowerSaving()) {
                mMcPowerSaving = new PopupView(getActivity(), getResources()
                        .getStringArray(R.array.mc_power_saving_items));
                mMcPowerSaving.setTitle(getString(R.string.mc_power_saving));
                mMcPowerSaving
                        .setSummary(getString(R.string.mc_power_saving_summary));
                mMcPowerSaving.setItem(getResources().getStringArray(
                        R.array.mc_power_saving_items)[cpuHelper
                        .getMcPowerSaving()]);
                mMcPowerSaving.setOnItemClickListener(CPUFragment.this);
                views.add(mMcPowerSaving);
            }

            if (cpuHelper.hasMpdecision() || cpuHelper.hasIntelliPlug())
                views.add(new HeaderItem(getString(R.string.hotplug)));

            if (cpuHelper.hasMpdecision()) {
                mMpdecision = new CheckBoxView();
                mMpdecision.setChecked(cpuHelper.isMpdecisionActive());
                mMpdecision.setTitle(getString(R.string.mpdecision));
                mMpdecision.setSummary(getString(R.string.mpdecision_summary));
                mMpdecision.setOnCheckBoxListener(CPUFragment.this);
                views.add(mMpdecision);
            }

            if (cpuHelper.hasIntelliPlug()) {
                mIntelliPlug = new CheckBoxView();
                mIntelliPlug.setChecked(cpuHelper.isIntelliPlugActive());
                mIntelliPlug.setTitle(getString(R.string.intelliplug));
                mIntelliPlug
                        .setSummary(getString(R.string.intelliplug_summary));
                mIntelliPlug.setOnCheckBoxListener(CPUFragment.this);
                views.add(mIntelliPlug);
            }

            if (cpuHelper.hasIntelliPlugEco()) {
                mIntelliPlugEco = new CheckBoxView();
                mIntelliPlugEco.setChecked(cpuHelper.isIntelliPlugEcoActive());
                mIntelliPlugEco
                        .setTitle(getString(R.string.intelliplug_eco_mode));
                mIntelliPlugEco.setOnCheckBoxListener(CPUFragment.this);
                views.add(mIntelliPlugEco);
            }

            list.setAdapter(new GenericAdapter(getActivity(), views));
        }
    };

    @Override
    public void onClick(CheckBoxView checkBoxView, boolean checked) {
        for (int i = 0; i < cpuHelper.getCoreCount(); i++)
            if (checkBoxView == mCoreBoxes[i]) {
                if (i != 0) mCommandControl.runCommand(checked ? "1" : "0",
                        String.format(CPU_CORE_ONLINE, i), CommandType.GENERIC,
                        -1, getActivity());
                else {
                    mCoreBoxes[i].setChecked(true);
                    mUtils.toast(getString(R.string.turn_off_not_possible, i),
                            getActivity());
                }
            }

        if (checkBoxView == mMpdecision)
            if (checked) mCommandControl.startModule(CPU_MPDEC, true,
                    getActivity());
            else mCommandControl.stopModule(CPU_MPDEC, true, getActivity());

        if (checkBoxView == mIntelliPlug) {
            if (checked) mCommandControl.runCommand("1", CPU_INTELLI_PLUG,
                    CommandType.GENERIC, -1, getActivity());
            else {
                mCommandControl.runCommand("0", CPU_INTELLI_PLUG,
                        CommandType.GENERIC, -1, getActivity());
                mCommandControl.bringCoresOnline();
            }
        }

        if (checkBoxView == mIntelliPlugEco)
            mCommandControl.runCommand(checked ? "1" : "0",
                    CPU_INTELLI_PLUG_ECO, CommandType.GENERIC, -1,
                    getActivity());
    };

    @Override
    public void onItemClick(PopupView popupView, int item) {
        if (popupView == mCpuMaxScaling)
            mCommandControl.runCommand(cpuHelper.getCpuFreqs()[item],
                    CPU_MAX_FREQ, CommandType.CPU, -1, getActivity());

        if (popupView == mCpuMinScaling)
            mCommandControl.runCommand(cpuHelper.getCpuFreqs()[item],
                    CPU_MIN_FREQ, CommandType.CPU, -1, getActivity());

        if (popupView == mGovernorScaling)
            mCommandControl.runCommand(cpuHelper.getCpuGovernors()[item],
                    CPU_SCALING_GOVERNOR, CommandType.CPU, -1, getActivity());

        if (popupView == mMcPowerSaving)
            mCommandControl
                    .runCommand(String.valueOf(item), CPU_MC_POWER_SAVING,
                            CommandType.GENERIC, -1, getActivity());
    }

    @Override
    public void onClick(CommonView commonView) {
        if (commonView == mGovernorTunables) {
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
    }

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
                mCpuMaxScaling.setItem(mAvailableFreqs.indexOf((cpuHelper
                        .getMaxFreq(0) / 1000) + getString(R.string.mhz)));
                mCpuMinScaling.setItem(mAvailableFreqs.indexOf((cpuHelper
                        .getMinFreq(0) / 1000) + getString(R.string.mhz)));
            }

            hand.postDelayed(run2, 500);
        }
    };

    @Override
    public void onDestroy() {
        hand.removeCallbacks(run2);
        super.onDestroy();
    }

}
