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
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.pac.performance.R;
import com.pac.performance.helpers.BatteryHelper;
import com.pac.performance.helpers.LayoutHelper;
import com.pac.performance.utils.Constants;
import com.pac.performance.utils.Control;
import com.pac.performance.utils.InformationDialog;
import com.pac.performance.utils.Utils;

public class BatteryFragment extends Fragment implements Constants,
        OnClickListener, OnCheckedChangeListener, OnSeekBarChangeListener {

    private static Context context;

    public static LinearLayout layout = null;

    private static TextView mBatteryVoltageTitle;
    private static TextView mBatteryVoltageText;

    private static CheckBox mFastChargeBox;

    private static TextView mBLXTitle;
    private static TextView mBLXText;
    private static Button mBLXMinus;
    private static SeekBar mBLXBar;
    private static Button mBLXPlus;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        context = getActivity();
        View rootView = inflater.inflate(R.layout.generic, container, false);
        layout = (LinearLayout) rootView.findViewById(R.id.layout);

        setLayout();

        mBatteryVoltageTitle.setOnClickListener(this);
        mFastChargeBox.setOnCheckedChangeListener(this);
        mBLXMinus.setOnClickListener(this);
        mBLXBar.setOnSeekBarChangeListener(this);
        mBLXPlus.setOnClickListener(this);

        return rootView;
    }

    private void setLayout() {
        mBatteryVoltageTitle = new TextView(context);
        mBatteryVoltageTitle.setPadding(0, MainFragment.mHeight / 25, 0, 15);
        if (Utils.exist(BATTERY_VOLTAGE)) layout.addView(mBatteryVoltageTitle);

        mBatteryVoltageText = new TextView(context);
        if (Utils.exist(BATTERY_VOLTAGE)) layout.addView(mBatteryVoltageText);

        LinearLayout mFastChargeLayout = new LinearLayout(context);
        mFastChargeLayout.setGravity(Gravity.CENTER);
        mFastChargeLayout.setPadding(0, (int) (MainFragment.mHeight / 21.6), 0,
                0);
        if (Utils.exist(FAST_CHARGE)) layout.addView(mFastChargeLayout);

        mFastChargeBox = new CheckBox(context);
        mFastChargeLayout.addView(mFastChargeBox);

        mBLXTitle = new TextView(context);
        LayoutHelper.setTextTitle(mBLXTitle, getString(R.string.blx), context);
        mBLXTitle.setPadding(0, (int) (MainFragment.mHeight / 21.6), 0, 0);
        if (Utils.exist(BLX)) layout.addView(mBLXTitle);

        mBLXText = new TextView(context);
        LayoutHelper.setSeekBarText(mBLXText,
                String.valueOf(BatteryHelper.getBLX()));
        if (Utils.exist(BLX)) layout.addView(mBLXText);

        LinearLayout mBLXLayout = new LinearLayout(context);
        mBLXLayout.setGravity(Gravity.CENTER);
        if (Utils.exist(BLX)) layout.addView(mBLXLayout);

        mBLXMinus = (Button) LayoutHelper.createSeekBar(getActivity())[0];
        mBLXLayout.addView(mBLXMinus);

        mBLXBar = (SeekBar) LayoutHelper.createSeekBar(getActivity())[1];
        mBLXLayout.addView(mBLXBar);

        mBLXPlus = (Button) LayoutHelper.createSeekBar(getActivity())[2];
        mBLXLayout.addView(mBLXPlus);

        setValues();
    }

    public static void setValues() {
        LayoutHelper.setTextTitle(mBatteryVoltageTitle,
                context.getString(R.string.curbatteryvoltage), context);

        LayoutHelper.setSubTitle(mBatteryVoltageText,
                String.valueOf(BatteryHelper.getCurBatteryVoltage() / 1000)
                        + context.getString(R.string.mv));

        LayoutHelper.setCheckBox(mFastChargeBox, BatteryHelper.getFastCharge(),
                context.getString(R.string.fastcharge), context);

        LayoutHelper.setNormalSeekBar(mBLXBar, 100, BatteryHelper.getBLX(),
                context);
    }

    public static void setBatteryVoltage() {
        if (context != null && mBatteryVoltageText != null) mBatteryVoltageText
                .setText(String.valueOf(BatteryHelper.getCurBatteryVoltage() / 1000)
                        + context.getString(R.string.mv));
    }

    @Override
    public void onClick(View v) {
        if (v.equals(mBatteryVoltageTitle)) try {
            startActivity(new Intent(Intent.ACTION_POWER_USAGE_SUMMARY));
        } catch (Exception ignored) {}
        if (v.equals(mBLXTitle)) InformationDialog.showInfo(mBLXTitle.getText()
                .toString(), getString(R.string.blx_summary), context);
        if (v.equals(mBLXMinus)) {
            mBLXBar.setProgress(mBLXBar.getProgress() - 1);
            saveBLX();
        }
        if (v.equals(mBLXPlus)) {
            mBLXBar.setProgress(mBLXBar.getProgress() + 1);
            saveBLX();
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        MainFragment.BatteryChange = true;
        MainFragment.showButtons(true);
        if (buttonView.equals(mFastChargeBox)) Control.runBatteryGeneric(
                isChecked ? "1" : "0", FAST_CHARGE);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress,
            boolean fromUser) {
        MainFragment.BatteryChange = true;
        MainFragment.showButtons(true);

        if (seekBar.equals(mBLXBar)) {
            mBLXText.setText(String.valueOf(progress));
            saveBLX();
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {}

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {}

    private static void saveBLX() {
        Control.runBatteryGeneric(mBLXText.getText().toString(), BLX);
    }
}
