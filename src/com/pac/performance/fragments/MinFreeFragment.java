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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.pac.performance.MainActivity;
import com.pac.performance.R;
import com.pac.performance.helpers.LayoutHelper;
import com.pac.performance.helpers.MinFreeHelper;
import com.pac.performance.utils.Control;
import com.pac.performance.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class MinFreeFragment extends Fragment implements OnClickListener,
        OnSeekBarChangeListener {

    private static Context context;

    public static LinearLayout layout = null;

    private static int minfreelength = 0;
    private static TextView[] mMinFreeTexts;
    private static Button[] mMinFreeMinuses;
    private static SeekBar[] mMinFreeBars;
    private static Button[] mMinFreePluses;
    private static List<String> mMinFreeList = new ArrayList<String>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        context = getActivity();
        View rootView = inflater.inflate(R.layout.generic, container, false);
        layout = (LinearLayout) rootView.findViewById(R.id.layout);

        setLayout();

        for (int i = 0; i < minfreelength; i++) {
            mMinFreeMinuses[i].setOnClickListener(this);
            mMinFreeBars[i].setOnSeekBarChangeListener(this);
            mMinFreePluses[i].setOnClickListener(this);
        }

        return rootView;
    }

    private void setLayout() {

        TextView mMinFreeTitle = new TextView(context);
        LayoutHelper.setTextTitle(mMinFreeTitle,
                getString(R.string.minfreesettings), context);
        mMinFreeTitle.setPadding(0, Math.round(MainActivity.mHeight / 25), 0,
                15);
        layout.addView(mMinFreeTitle);

        minfreelength = MinFreeHelper.getMinFreeValues().length;

        mMinFreeTexts = new TextView[minfreelength];
        mMinFreeMinuses = new Button[minfreelength];
        mMinFreeBars = new SeekBar[minfreelength];
        mMinFreePluses = new Button[minfreelength];

        for (int i = 0; i < minfreelength; i++) {
            TextView mMinFreeSubTitle = new TextView(context);
            switch (i) {
            case 0:
                LayoutHelper.setSubTitle(mMinFreeSubTitle,
                        getString(R.string.forgroundapplications));
                break;
            case 1:
                LayoutHelper.setSubTitle(mMinFreeSubTitle,
                        getString(R.string.visbileapplications));
                break;
            case 2:
                LayoutHelper.setSubTitle(mMinFreeSubTitle,
                        getString(R.string.secondaryserver));
                break;
            case 3:
                LayoutHelper.setSubTitle(mMinFreeSubTitle,
                        getString(R.string.hiddenapplications));
                break;
            case 4:
                LayoutHelper.setSubTitle(mMinFreeSubTitle,
                        getString(R.string.contentproviders));
                break;
            case 5:
                LayoutHelper.setSubTitle(mMinFreeSubTitle,
                        getString(R.string.emptyapplications));
                break;
            }
            layout.addView(mMinFreeSubTitle);

            TextView mMinFreeText = new TextView(context);
            LayoutHelper
                    .setSeekBarText(mMinFreeText,
                            String.valueOf(Math.round(MinFreeHelper
                                    .getMinFreeValues()[i] / 1024 * 4)
                                    + context.getString(R.string.mb)
                                    + "["
                                    + String.valueOf(MinFreeHelper
                                            .getMinFreeValues()[i]) + "]"));
            mMinFreeTexts[i] = mMinFreeText;
            layout.addView(mMinFreeText);

            LayoutParams lp = new LinearLayout.LayoutParams(0,
                    LinearLayout.LayoutParams.WRAP_CONTENT, 1f);

            LinearLayout mMinFreeLayout = new LinearLayout(context);
            mMinFreeLayout.setGravity(Gravity.CENTER);
            layout.addView(mMinFreeLayout);

            Button mMinFreeMinus = new Button(context);
            mMinFreeMinus.setText(getString(R.string.minus));
            mMinFreeMinuses[i] = mMinFreeMinus;
            mMinFreeLayout.addView(mMinFreeMinus);

            SeekBar mMinFreeBar = new SeekBar(context);
            mMinFreeBar.setLayoutParams(lp);
            mMinFreeBars[i] = mMinFreeBar;
            mMinFreeLayout.addView(mMinFreeBar);

            Button mMinFreePlus = new Button(context);
            mMinFreePlus.setText(getString(R.string.plus));
            mMinFreePluses[i] = mMinFreePlus;
            mMinFreeLayout.addView(mMinFreePlus);
        }
        setValues();
    }

    public static void setValues() {

        for (int i = 0; i < minfreelength; i++)
            LayoutHelper.setNormalSeekBar(mMinFreeBars[i], 256,
                    MinFreeHelper.getMinFreeValues()[i] / 256, context);

    }

    @Override
    public void onClick(View v) {
        for (int i = 0; i < MinFreeHelper.getMinFreeValues().length; i++) {
            if (v.equals(mMinFreeMinuses[i])) {
                mMinFreeBars[i].setProgress(mMinFreeBars[i].getProgress() - 1);
                saveValues();
            }
            if (v.equals(mMinFreePluses[i])) {
                mMinFreeBars[i].setProgress(mMinFreeBars[i].getProgress() + 1);
                saveValues();
            }
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress,
            boolean fromUser) {
        MainActivity.MinFreeChange = true;
        MainActivity.showButtons(true);

        mMinFreeList.clear();
        for (int i = 0; i < MinFreeHelper.getMinFreeValues().length; i++) {
            if (seekBar.equals(mMinFreeBars[i]))
                mMinFreeTexts[i].setText(String.valueOf(progress
                        + getString(R.string.mb) + "[" + progress * 256 + "]"));
            mMinFreeList
                    .add(String.valueOf(Integer
                            .parseInt(mMinFreeTexts[i].getText().toString()
                                    .split(getString(R.string.mb))[0]) * 256)
                            + ",");
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        for (int i = 0; i < MinFreeHelper.getMinFreeValues().length; i++)
            if (seekBar.equals(mMinFreeBars[i]))
                saveValues();
    }

    private static void saveValues() {
        String values = "";
        for (String value : mMinFreeList)
            values = values + value;
        Control.runMinFreeGeneric(Utils.replaceLastChar(values, 1),
                MinFreeHelper.MINFREE);
    }

}
