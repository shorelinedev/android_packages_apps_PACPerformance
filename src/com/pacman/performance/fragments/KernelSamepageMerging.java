package com.pacman.performance.fragments;

import java.util.ArrayList;
import java.util.List;

import com.pacman.performance.R;
import com.pacman.performance.utils.Constants;
import com.pacman.performance.utils.CommandControl.CommandType;
import com.pacman.performance.utils.views.CheckBoxView;
import com.pacman.performance.utils.views.CheckBoxView.OnCheckBoxListener;
import com.pacman.performance.utils.views.CommonView;
import com.pacman.performance.utils.views.GenericView.GenericAdapter;
import com.pacman.performance.utils.views.GenericView.Item;
import com.pacman.performance.utils.views.HeaderItem;
import com.pacman.performance.utils.views.SeekBarView;
import com.pacman.performance.utils.views.SeekBarView.OnSeekBarListener;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class KernelSamepageMerging extends Fragment implements Constants,
        OnCheckBoxListener, OnSeekBarListener {

    private final Handler hand = new Handler();

    private ListView list;
    private List<Item> views = new ArrayList<Item>();

    private CommonView[] mInfos;

    private String[] mPagesToScanValues;
    private String[] mSleepMillisecondsValues;

    private CheckBoxView mEnableKsm;
    private SeekBarView mPagesToScan, mSleepMilliseconds;

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

            views.add(new HeaderItem(getString(R.string.ksm_stats)));

            mInfos = new CommonView[KSM_INFOS.length];
            for (int i = 0; i < KSM_INFOS.length; i++) {
                mInfos[i] = new CommonView();
                mInfos[i].setTitle(getResources().getStringArray(
                        R.array.ksm_infos)[i]);
                mInfos[i].setSummary(String.valueOf(kernelsamepagemergingHelper
                        .getInfos(i)));
                views.add(mInfos[i]);
            }

            views.add(new HeaderItem(getString(R.string.parameters)));

            mEnableKsm = new CheckBoxView();
            mEnableKsm.setChecked(kernelsamepagemergingHelper.isKsmActive());
            mEnableKsm.setTitle(getString(R.string.ksm_enable));
            mEnableKsm.setSummary(getString(R.string.ksm_enable_summary));
            mEnableKsm.setOnCheckBoxListener(KernelSamepageMerging.this);
            views.add(mEnableKsm);

            mPagesToScanValues = new String[1025];
            for (int i = 0; i < mPagesToScanValues.length; i++)
                mPagesToScanValues[i] = String.valueOf(i);
            mPagesToScan = new SeekBarView(mPagesToScanValues);
            mPagesToScan.setTitle(getString(R.string.ksm_pages_to_scan));
            mPagesToScan.setItem(String.valueOf(kernelsamepagemergingHelper
                    .getPagesToScan()));
            mPagesToScan.setOnSeekBarListener(KernelSamepageMerging.this);
            views.add(mPagesToScan);

            mSleepMillisecondsValues = new String[5001];
            for (int i = 0; i < mSleepMillisecondsValues.length; i++)
                mSleepMillisecondsValues[i] = i + getString(R.string.ms);
            mSleepMilliseconds = new SeekBarView(mSleepMillisecondsValues);
            mSleepMilliseconds
                    .setTitle(getString(R.string.ksm_sleep_milliseconds));
            mSleepMilliseconds.setItem(kernelsamepagemergingHelper
                    .getSleepMilliseconds() + getString(R.string.ms));
            mSleepMilliseconds.setOnSeekBarListener(KernelSamepageMerging.this);
            views.add(mSleepMilliseconds);

            list.setAdapter(new GenericAdapter(getActivity(), views));
        }
    };

    @Override
    public void onClick(CheckBoxView checkBoxView, boolean checked) {
        if (checkBoxView == mEnableKsm)
            mCommandControl.runCommand(checked ? "1" : "0", KSM_RUN,
                    CommandType.GENERIC, -1, getActivity());
    };

    @Override
    public void onStop(SeekBarView seekBarView, int item) {
        if (seekBarView == mPagesToScan)
            mCommandControl.runCommand(mPagesToScanValues[item],
                    KSM_PAGES_TO_SCAN, CommandType.GENERIC, -1, getActivity());

        if (seekBarView == mSleepMilliseconds)
            mCommandControl.runCommand(mSleepMillisecondsValues[item].replace(
                    getString(R.string.ms), ""), KSM_SLEEP_MILLISECONDS,
                    CommandType.GENERIC, -1, getActivity());
    }

    private final Runnable run2 = new Runnable() {
        @Override
        public void run() {
            for (int i = 0; i < KSM_INFOS.length; i++)
                mInfos[i].setSummary(String.valueOf(kernelsamepagemergingHelper
                        .getInfos(i)));

            hand.postDelayed(run2, 1000);
        }
    };

    @Override
    public void onDestroy() {
        hand.removeCallbacks(run2);
        super.onDestroy();
    }

}
