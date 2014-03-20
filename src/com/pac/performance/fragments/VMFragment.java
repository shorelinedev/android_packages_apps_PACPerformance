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
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pac.performance.MainActivity;
import com.pac.performance.R;
import com.pac.performance.helpers.LayoutHelper;
import com.pac.performance.helpers.VMHelper;
import com.pac.performance.utils.Control;
import com.pac.performance.utils.InformationDialog;
import com.pac.performance.utils.Utils;

import java.util.List;

public class VMFragment extends Fragment implements OnClickListener,
        TextWatcher {

    private static Context context;

    public static LinearLayout layout = null;

    private static List<String> mVMFiles;

    private static TextView mVMTitle;
    private static Button[] mMinusButtons;
    private static EditText[] mVMEdits;
    private static Button[] mPlusButtons;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        context = getActivity();
        View rootView = inflater.inflate(R.layout.generic, container, false);
        layout = (LinearLayout) rootView.findViewById(R.id.layout);

        setLayout();

        mVMTitle.setOnClickListener(this);

        for (int i = 0; i < mVMFiles.size(); i++) {
            mMinusButtons[i].setOnClickListener(this);
            mVMEdits[i].addTextChangedListener(this);
            mPlusButtons[i].setOnClickListener(this);
        }

        return rootView;
    }

    private void setLayout() {
        mVMFiles = VMHelper.getVMFiles();

        // Create VM Tuning Title
        mVMTitle = new TextView(context);
        LayoutHelper.setTextTitle(mVMTitle, getString(R.string.vmtuning),
                context);
        mVMTitle.setPadding(0, Math.round(MainActivity.mHeight / 25), 0, 0);
        if (Utils.exist(VMHelper.VM_PATH))
            layout.addView(mVMTitle);

        mMinusButtons = new Button[mVMFiles.size()];
        mVMEdits = new EditText[mVMFiles.size()];
        mPlusButtons = new Button[mVMFiles.size()];

        for (int i = 0; i < mVMFiles.size(); i++) {
            TextView mVMText = new TextView(context);
            LayoutHelper.setSubTitle(
                    mVMText,
                    Utils.setAllLetterUpperCase(mVMFiles.get(i).replace("_",
                            " ")));
            if (Utils.exist(VMHelper.VM_PATH))
                layout.addView(mVMText);

            LinearLayout mVMLayout = new LinearLayout(context);
            mVMLayout.setGravity(Gravity.CENTER);
            if (Utils.exist(VMHelper.VM_PATH))
                layout.addView(mVMLayout);

            Button mMinusButton = new Button(context);
            mMinusButton.setText(getString(R.string.minus));
            mMinusButtons[i] = mMinusButton;
            mVMLayout.addView(mMinusButton);

            EditText mVMEdit = new EditText(context);
            mVMEdit.setInputType(InputType.TYPE_NUMBER_FLAG_SIGNED
                    | InputType.TYPE_CLASS_NUMBER);
            mVMEdits[i] = mVMEdit;
            mVMLayout.addView(mVMEdit);

            Button mPlusButton = new Button(context);
            mPlusButton.setText(getString(R.string.plus));
            mPlusButtons[i] = mPlusButton;
            mVMLayout.addView(mPlusButton);
        }
        setValues();
    }

    public static void setValues() {
        for (int i = 0; i < mVMFiles.size(); i++)
            LayoutHelper.setEditText(mVMEdits[i],
                    String.valueOf(VMHelper.getVMValues().get(i)));
    }

    @Override
    public void onClick(View v) {
        if (!v.equals(mVMTitle)) {
            MainActivity.VMChange = true;
            MainActivity.showButtons(true);
        }

        if (v.equals(mVMTitle))
            InformationDialog.showInfo(mVMTitle.getText().toString(),
                    getString(R.string.vmtunig_summary), context);

        for (int i = 0; i < mVMFiles.size(); i++) {
            if (v.equals(mMinusButtons[i])) {
                mVMEdits[i].setText(String.valueOf(Integer.parseInt(mVMEdits[i]
                        .getText().toString()) - 1));
                Control.runVMGeneric(mVMEdits[i].getText().toString(),
                        mVMFiles.get(i));
            }
            if (v.equals(mPlusButtons[i])) {
                mVMEdits[i].setText(String.valueOf(Integer.parseInt(mVMEdits[i]
                        .getText().toString()) + 1));
                Control.runVMGeneric(mVMEdits[i].getText().toString(),
                        mVMFiles.get(i));
            }
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
        MainActivity.VMChange = true;
        MainActivity.showButtons(true);

        for (int i = 0; i < mVMFiles.size(); i++)
            Control.runVMGeneric(mVMEdits[i].getText().toString(),
                    mVMFiles.get(i));
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
            int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }
}
