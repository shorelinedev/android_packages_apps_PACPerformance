/*
 * Copyright (C) 2014 PAC-man ROM
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.pac.performance.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;
import android.widget.TextView;

import com.pac.performance.R;
import com.pac.performance.helpers.CPUHelper;
import com.pac.performance.helpers.LayoutHelper;
import com.pac.performance.utils.Constants;
import com.pac.performance.utils.Control;
import com.pac.performance.utils.InformationDialog;
import com.pac.performance.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CPUFragment extends Fragment implements Constants,
        OnSeekBarChangeListener, OnItemSelectedListener, OnClickListener,
        OnCheckedChangeListener {

    private static Context context;

    public static LinearLayout layout = null;

    private static Handler hand = new Handler();
    private static TextView[] mCurFreqTexts;

    private static CheckBox[] mCoreControlBoxes;

    private static List<String> mAvailableFreqList = new ArrayList<String>();

    private static SeekBar mMaxFreqScalingBar;
    private static TextView mMaxFreqScalingText;
    private static TextView mMaxScalingText;

    private static SeekBar mMinFreqScalingBar;
    private static TextView mMinFreqScalingText;
    private static TextView mMinScalingText;

    private static SeekBar mMaxScreenOffFreqScalingBar;
    private static TextView mMaxScreenOffFreqScalingText;
    private static TextView mMaxScreenOffFreqText;

    private static SeekBar mMinScreenOnFreqScalingBar;
    private static TextView mMinScreenOnFreqScalingText;
    private static TextView mMinScreenOnFreqText;

    private static Spinner mGovernorSpinner;
    private static List<String> mAvailableGovernorList = new ArrayList<String>();
    private static TextView mGovernorTitle;

    private static CheckBox mIntelliPlugBox;
    private static CheckBox mIntelliPlugEcoModeBox;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        context = getActivity();
        View rootView = inflater.inflate(R.layout.generic, container, false);
        layout = (LinearLayout) rootView.findViewById(R.id.layout);

        mAvailableFreqList.clear();

        setLayout();

        for (int i = 1; i < CPUHelper.getCoreCount(); i++)
            mCoreControlBoxes[i].setOnCheckedChangeListener(this);
        mMaxScalingText.setOnClickListener(this);
        mMaxFreqScalingBar.setOnSeekBarChangeListener(this);
        mMinScalingText.setOnClickListener(this);
        mMinFreqScalingBar.setOnSeekBarChangeListener(this);
        mMaxScreenOffFreqText.setOnClickListener(this);
        mMaxScreenOffFreqScalingBar.setOnSeekBarChangeListener(this);
        mMinScreenOnFreqText.setOnClickListener(this);
        mMinScreenOnFreqScalingBar.setOnSeekBarChangeListener(this);
        mGovernorTitle.setOnClickListener(this);
        mGovernorSpinner.setOnItemSelectedListener(this);
        mIntelliPlugBox.setOnCheckedChangeListener(this);
        mIntelliPlugEcoModeBox.setOnCheckedChangeListener(this);

        return rootView;
    }

    private void setLayout() {

        // Current Freq
        LinearLayout mCurFreqLayout = new LinearLayout(context);
        mCurFreqLayout.setPadding(0, Math.round(MainFragment.mHeight / 25), 0,
                15);
        mCurFreqLayout.setOrientation(LinearLayout.VERTICAL);
        if (Utils.exist(FREQUENCY_SCALING.replace("present", "0"))
                && Utils.exist(CORE_VALUE)) layout.addView(mCurFreqLayout);

        TextView mCurFreqTitle = new TextView(context);
        LayoutHelper.setTextTitle(mCurFreqTitle,
                getString(R.string.currentfreq), context);
        mCurFreqTitle.setPadding(0, 0, 0, 15);
        mCurFreqLayout.addView(mCurFreqTitle);

        mCurFreqTexts = new TextView[CPUHelper.getCoreCount()];
        for (int i = 0; i < CPUHelper.getCoreCount(); i += 2) {
            TextView mCurFreqText = new TextView(context);
            LayoutHelper.setCurFreqText(mCurFreqText);
            mCurFreqTexts[i] = mCurFreqText;
            mCurFreqLayout.addView(mCurFreqText);
        }
        setCurFreq();

        // Core Control
        LinearLayout mCoreControlLayout = new LinearLayout(context);
        mCoreControlLayout.setGravity(Gravity.CENTER);
        if (Utils.exist(CORE_STAT.replace("present", "0"))) layout
                .addView(mCoreControlLayout);

        mCoreControlBoxes = new CheckBox[CPUHelper.getCoreCount()];
        for (int i = 1; i < CPUHelper.getCoreCount(); i++) {
            CheckBox mCoreBox = new CheckBox(context);
            mCoreControlBoxes[i] = mCoreBox;
            mCoreControlLayout.addView(mCoreBox);
        }

        // Create a layout for scaling
        LinearLayout mFreqScalingLayout = new LinearLayout(context);
        mFreqScalingLayout.setPaddingRelative(0,
                Math.round(MainFragment.mHeight / 20), 0, 0);
        mFreqScalingLayout.setOrientation(LinearLayout.VERTICAL);
        if (Utils.exist(AVAILABLE_GOVERNOR)
                || Utils.exist(MIN_SCREEN_ON)
                || (Utils.exist(MAX_SCREEN_OFF) || Utils.exist(MIN_FREQ) || Utils
                        .exist(MAX_FREQ))) layout.addView(mFreqScalingLayout);

        // Create a list to store all freqs
        for (int text : CPUHelper.getAvailableFreq())
            mAvailableFreqList.add(String.valueOf(text));

        if (Integer.parseInt(mAvailableFreqList.get(0)) > Integer
                .parseInt(mAvailableFreqList.get(mAvailableFreqList.size() - 1))) {
            mAvailableFreqList.clear();
            for (int i = CPUHelper.getAvailableFreq().length - 1; i >= 0; i--)
                mAvailableFreqList.add(String.valueOf(CPUHelper
                        .getAvailableFreq()[i]));
        }

        // Max Cpu Scaling
        mMaxScalingText = new TextView(context);
        LayoutHelper.setTextTitle(mMaxScalingText, getString(R.string.maxfreq),
                context);
        if (Utils.exist(MAX_FREQ)) mFreqScalingLayout.addView(mMaxScalingText);

        mMaxFreqScalingText = new TextView(context);
        LayoutHelper.setSeekBarText(
                mMaxFreqScalingText,
                String.valueOf(CPUHelper.getMaxFreq() / 1000)
                        + context.getString(R.string.mhz));
        if (Utils.exist(MAX_FREQ)) mFreqScalingLayout
                .addView(mMaxFreqScalingText);

        mMaxFreqScalingBar = new SeekBar(context);
        if (Utils.exist(MAX_FREQ)) mFreqScalingLayout
                .addView(mMaxFreqScalingBar);

        // Min Cpu Scaling
        mMinScalingText = new TextView(context);
        LayoutHelper.setTextTitle(mMinScalingText, getString(R.string.minfreq),
                context);
        if (Utils.exist(MIN_FREQ)) mFreqScalingLayout.addView(mMinScalingText);

        mMinFreqScalingText = new TextView(context);
        LayoutHelper.setSeekBarText(
                mMinFreqScalingText,
                String.valueOf(CPUHelper.getMinFreq() / 1000)
                        + context.getString(R.string.mhz));
        if (Utils.exist(MIN_FREQ)) mFreqScalingLayout
                .addView(mMinFreqScalingText);

        mMinFreqScalingBar = new SeekBar(context);
        if (Utils.exist(MIN_FREQ)) mFreqScalingLayout
                .addView(mMinFreqScalingBar);

        // Max Screen Off Cpu Scaling
        mMaxScreenOffFreqText = new TextView(context);
        LayoutHelper.setTextTitle(mMaxScreenOffFreqText,
                getString(R.string.maxscreenofffreq), context);
        if (Utils.exist(MAX_SCREEN_OFF)) mFreqScalingLayout
                .addView(mMaxScreenOffFreqText);

        mMaxScreenOffFreqScalingText = new TextView(context);
        LayoutHelper.setSeekBarText(mMaxScreenOffFreqScalingText,
                String.valueOf(CPUHelper.getMaxScreenOffFreq() / 1000)
                        + context.getString(R.string.mhz));
        if (Utils.exist(MAX_SCREEN_OFF)) mFreqScalingLayout
                .addView(mMaxScreenOffFreqScalingText);

        mMaxScreenOffFreqScalingBar = new SeekBar(context);
        if (Utils.exist(MAX_SCREEN_OFF)) mFreqScalingLayout
                .addView(mMaxScreenOffFreqScalingBar);

        // Min Screen On Cpu Scaling
        mMinScreenOnFreqText = new TextView(context);
        LayoutHelper.setTextTitle(mMinScreenOnFreqText,
                getString(R.string.minscreenonfreq), context);
        if (Utils.exist(MIN_SCREEN_ON)) mFreqScalingLayout
                .addView(mMinScreenOnFreqText);

        mMinScreenOnFreqScalingText = new TextView(context);
        LayoutHelper.setSeekBarText(
                mMinScreenOnFreqScalingText,
                String.valueOf(CPUHelper.getMinScreenOnFreq() / 1000)
                        + context.getString(R.string.mhz));
        if (Utils.exist(MIN_SCREEN_ON)) mFreqScalingLayout
                .addView(mMinScreenOnFreqScalingText);

        mMinScreenOnFreqScalingBar = new SeekBar(context);
        if (Utils.exist(MIN_SCREEN_ON)) mFreqScalingLayout
                .addView(mMinScreenOnFreqScalingBar);

        // CPU Governor Layout
        LinearLayout mGovernorLayout = new LinearLayout(context);
        mGovernorLayout.setGravity(Gravity.CENTER);
        mGovernorLayout
                .setPadding(0, (int) (MainFragment.mHeight / 21.6), 0, 0);
        if (Utils.exist(AVAILABLE_GOVERNOR)) layout.addView(mGovernorLayout);

        // Governor
        mGovernorTitle = new TextView(context);
        LayoutHelper.setTextTitle(mGovernorTitle,
                getString(R.string.cpugovernor), context);
        mGovernorLayout.addView(mGovernorTitle);

        String[] mAvailableGovernor = CPUHelper.getAvailableGovernor();
        mAvailableGovernorList = Arrays.asList(mAvailableGovernor);

        mGovernorSpinner = new Spinner(context);
        mGovernorLayout.addView(mGovernorSpinner);

        // IntelliPlug
        LinearLayout mIntelliPlugLayout = new LinearLayout(context);
        mIntelliPlugLayout.setGravity(Gravity.CENTER);
        mIntelliPlugLayout.setPadding(0, (int) (MainFragment.mHeight / 21.6),
                0, 0);
        if (Utils.exist(INTELLIPLUG)) layout.addView(mIntelliPlugLayout);

        mIntelliPlugBox = new CheckBox(context);
        mIntelliPlugLayout.addView(mIntelliPlugBox);

        mIntelliPlugEcoModeBox = new CheckBox(context);
        if (Utils.exist(INTELLIPLUG)) mIntelliPlugLayout
                .addView(mIntelliPlugEcoModeBox);

        setValues();
    }

    public static void setValues() {
        for (int i = 1; i < CPUHelper.getCoreCount(); i++)
            LayoutHelper.setCheckBox(
                    mCoreControlBoxes[i],
                    CPUHelper.getCoreOnline(i),
                    context.getString(R.string.core) + " "
                            + String.valueOf(i + 1), context);

        LayoutHelper.setSeekBar(mMaxFreqScalingBar,
                mAvailableFreqList.size() - 1, mAvailableFreqList
                        .indexOf(String.valueOf(CPUHelper.getMaxFreq())),
                context);

        LayoutHelper.setSeekBar(mMinFreqScalingBar,
                mAvailableFreqList.size() - 1, mAvailableFreqList
                        .indexOf(String.valueOf(CPUHelper.getMinFreq())),
                context);

        LayoutHelper.setSeekBar(mMaxScreenOffFreqScalingBar, mAvailableFreqList
                .size() - 1, mAvailableFreqList.indexOf(String
                .valueOf(CPUHelper.getMaxScreenOffFreq())), context);

        LayoutHelper.setSeekBar(mMinScreenOnFreqScalingBar, mAvailableFreqList
                .size() - 1, mAvailableFreqList.indexOf(String
                .valueOf(CPUHelper.getMinScreenOnFreq())), context);

        ArrayAdapter<String> adapterGovernor = new ArrayAdapter<String>(
                context, R.layout.spinner, mAvailableGovernorList);
        adapterGovernor
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        LayoutHelper.setSpinner(mGovernorSpinner, adapterGovernor,
                mAvailableGovernorList.indexOf(CPUHelper.getCurGovernor()));

        LayoutHelper.setCheckBox(mIntelliPlugBox, CPUHelper.getIntelliPlug(),
                context.getString(R.string.intelliplug), context);

        LayoutHelper.setCheckBox(mIntelliPlugEcoModeBox,
                CPUHelper.getIntelliPlugEcoMode(),
                context.getString(R.string.intelliplugecomode), context);
    }

    @Override
    public void onResume() {
        hand.postDelayed(run, 0);
        super.onResume();
    }

    Runnable run = new Runnable() {
        @Override
        public void run() {
            setCurFreq();
            hand.postDelayed(run, 1000);
        }
    };

    @Override
    public void onDestroy() {
        hand.removeCallbacks(run);
        super.onDestroy();
    }

    private static void setCurFreq() {
        for (int i = 0; i < CPUHelper.getCoreCount(); i += 2) {
            String freq1 = CPUHelper.getFreqScaling(i) == 0 ? context
                    .getString(R.string.offline) : String.valueOf(CPUHelper
                    .getFreqScaling(i) / 1000) + "MHz";
            String freq2 = CPUHelper.getFreqScaling(i + 1) == 0 ? context
                    .getString(R.string.offline) : String.valueOf(CPUHelper
                    .getFreqScaling(i + 1) / 1000) + "MHz";
            mCurFreqTexts[i].setText(CPUHelper.getCoreCount() == 1 ? "Core "
                    + String.valueOf(i + 1) + ": " + freq1 : "Core "
                    + String.valueOf(i + 1) + ": " + freq1 + " Core "
                    + String.valueOf(i + 2) + ": " + freq2);
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress,
            boolean fromUser) {
        MainFragment.showButtons(true);
        MainFragment.CPUChange = true;

        if (seekBar.equals(mMaxFreqScalingBar)) {
            mMaxFreqScalingText
                    .setText(String.valueOf(Integer.parseInt(mAvailableFreqList
                            .get(progress)) / 1000) + "MHz");
            if (progress < mMinFreqScalingBar.getProgress()) mMinFreqScalingBar
                    .setProgress(progress);
            if (progress < mMinScreenOnFreqScalingBar.getProgress()) mMinScreenOnFreqScalingBar
                    .setProgress(progress);
            if (progress < mMaxScreenOffFreqScalingBar.getProgress()) mMaxScreenOffFreqScalingBar
                    .setProgress(progress);
        }
        if (seekBar.equals(mMinFreqScalingBar)) {
            mMinFreqScalingText
                    .setText(String.valueOf(Integer.parseInt(mAvailableFreqList
                            .get(progress)) / 1000) + "MHz");
            if (progress > mMaxFreqScalingBar.getProgress()) mMaxFreqScalingBar
                    .setProgress(progress);
            if (progress > mMaxScreenOffFreqScalingBar.getProgress()) mMaxScreenOffFreqScalingBar
                    .setProgress(progress);
            if (progress > mMinScreenOnFreqScalingBar.getProgress()) mMinScreenOnFreqScalingBar
                    .setProgress(progress);
        }
        if (seekBar.equals(mMaxScreenOffFreqScalingBar)) {
            mMaxScreenOffFreqScalingText
                    .setText(String.valueOf(Integer.parseInt(mAvailableFreqList
                            .get(progress)) / 1000) + "MHz");
            if (progress < mMinFreqScalingBar.getProgress()) mMinFreqScalingBar
                    .setProgress(progress);
            if (progress > mMaxFreqScalingBar.getProgress()) mMaxFreqScalingBar
                    .setProgress(progress);
        }
        if (seekBar.equals(mMinScreenOnFreqScalingBar)) {
            mMinScreenOnFreqScalingText
                    .setText(String.valueOf(Integer.parseInt(mAvailableFreqList
                            .get(progress)) / 1000) + "MHz");
            if (progress > mMaxFreqScalingBar.getProgress()) mMaxFreqScalingBar
                    .setProgress(progress);
            if (progress > mMinFreqScalingBar.getProgress()) mMinFreqScalingBar
                    .setProgress(progress);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {}

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        if (seekBar.equals(mMaxFreqScalingBar)
                || (seekBar.equals(mMinFreqScalingBar))
                || seekBar.equals(mMaxScreenOffFreqScalingBar)
                || seekBar.equals(mMinScreenOnFreqScalingBar)) {
            Control.runCPUGeneric(
                    mAvailableFreqList.get(mMaxFreqScalingBar.getProgress()),
                    MAX_FREQ);
            Control.runCPUGeneric(
                    mAvailableFreqList.get(mMinFreqScalingBar.getProgress()),
                    MIN_FREQ);
            Control.runCPUGeneric(mAvailableFreqList
                    .get(mMaxScreenOffFreqScalingBar.getProgress()),
                    MAX_SCREEN_OFF);
            Control.runCPUGeneric(mAvailableFreqList
                    .get(mMinScreenOnFreqScalingBar.getProgress()),
                    MIN_SCREEN_ON);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
            long arg3) {
        if (arg0.equals(mGovernorSpinner)) {
            if (arg2 != mAvailableGovernorList.indexOf(CPUHelper
                    .getCurGovernor())) {
                MainFragment.showButtons(true);
                MainFragment.CPUChange = true;

                Control.runCPUGeneric(mAvailableGovernorList.get(arg2),
                        CUR_GOVERNOR);
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {}

    @Override
    public void onClick(View v) {
        if (v.equals(mMaxScalingText)) InformationDialog.showInfo(
                mMaxScalingText.getText().toString(),
                getString(R.string.maxfreq_summary), context);
        else if (v.equals(mMinScalingText)) InformationDialog.showInfo(
                mMinScalingText.getText().toString(),
                getString(R.string.minfreq_summary), context);
        else if (v.equals(mMaxScreenOffFreqText)) InformationDialog.showInfo(
                mMaxScreenOffFreqText.getText().toString(),
                getString(R.string.maxscreenofffreq_summary), context);
        else if (v.equals(mMinScreenOnFreqText)) InformationDialog.showInfo(
                mMinScalingText.getText().toString(),
                getString(R.string.minscreenonfreq_summary), context);
        else if (v.equals(mGovernorTitle)) InformationDialog.showInfo(
                mGovernorTitle.getText().toString(),
                getString(R.string.cpugovernor_summary), context);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        MainFragment.showButtons(true);
        MainFragment.CPUChange = true;

        for (int i = 1; i < CPUHelper.getCoreCount(); i++)
            if (buttonView.equals(mCoreControlBoxes[i])) Control.runCPUGeneric(
                    isChecked ? "1" : "0",
                    CORE_STAT.replace("present", String.valueOf(i)));

        if (buttonView.equals(mIntelliPlugBox)) Control.runCPUGeneric(
                isChecked ? "1" : "0", INTELLIPLUG);
        if (buttonView.equals(mIntelliPlugEcoModeBox)) Control.runCPUGeneric(
                isChecked ? "1" : "0", INTELLIPLUG_ECO_MODE);
    }
}
