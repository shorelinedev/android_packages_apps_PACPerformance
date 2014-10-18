package com.pac.performance.fragments;

import com.pac.performance.R;
import com.pac.performance.utils.Constants;
import com.pac.performance.utils.CommandControl.CommandType;
import com.pac.performance.utils.Dialog.DialogReturn;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.text.InputType;

public class CPUVoltageFragment extends PreferenceFragment implements Constants {

    private PreferenceScreen root;
    private Preference[] mVoltage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        root = getPreferenceManager().createPreferenceScreen(getActivity());

        setPreferenceScreen(root);

        getActivity().runOnUiThread(run);
    }

    private final Runnable run = new Runnable() {

        @Override
        public void run() {
            mVoltage = new Preference[cpuVoltageHelper.getFreqs().length];
            for (int i = 0; i < cpuVoltageHelper.getFreqs().length; i++) {
                mVoltage[i] = prefHelper.setPreference(
                        cpuVoltageHelper.getFreqs()[i]
                                + getString(R.string.mhz),
                        cpuVoltageHelper.getVoltages()[i]
                                + getString(R.string.mv), getActivity());

                root.addPreference(mVoltage[i]);
            }
        }
    };

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen,
            Preference preference) {

        for (int i = 0; i < cpuVoltageHelper.getFreqs().length; i++)
            if (preference == mVoltage[i]) {
                final int position = i;
                mDialog.showDialogGeneric(CPU_VOLTAGE,
                        cpuVoltageHelper.getVoltages()[i], new DialogReturn() {
                            @Override
                            public void dialogReturn(String value) {
                                String commandvalue = "";
                                String[] currentvalue = cpuVoltageHelper
                                        .getVoltages();
                                for (int i = 0; i < currentvalue.length; i++) {
                                    String command = i == position ? value
                                            : currentvalue[i];

                                    commandvalue = !commandvalue.isEmpty() ? commandvalue
                                            + " " + command
                                            : command;
                                }
                                mCommandControl.runCommand(commandvalue,
                                        CPU_VOLTAGE, CommandType.CPU, position,
                                        getActivity());

                                new Thread() {
                                    public void run() {
                                        try {
                                            Thread.sleep(100);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }

                                        getActivity().runOnUiThread(
                                                new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        for (int i = 0; i < cpuVoltageHelper
                                                                .getFreqs().length; i++)
                                                            mVoltage[i]
                                                                    .setSummary(cpuVoltageHelper
                                                                            .getVoltages()[i]
                                                                            + getString(R.string.mv));
                                                    }
                                                });
                                    }
                                }.start();
                            }
                        }, InputType.TYPE_CLASS_NUMBER, null, -1, getActivity());
            }

        return true;
    }
}
