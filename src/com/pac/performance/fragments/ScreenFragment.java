package com.pac.performance.fragments;

import com.pac.performance.R;
import com.pac.performance.utils.Constants;
import com.pac.performance.utils.CommandControl.CommandType;
import com.pac.performance.utils.Dialog.DialogReturn;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;

public class ScreenFragment extends PreferenceFragment implements Constants {

    private Preference[] mScreenCalibration;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final PreferenceScreen root = getPreferenceManager()
                .createPreferenceScreen(getActivity());

        new Thread() {
            public void run() {

                if (screenHelper.hasColorCalibration()) {
                    root.addPreference(prefHelper.setPreferenceCategory(
                            getString(R.string.color_calibration),
                            getActivity()));

                    mScreenCalibration = new Preference[screenHelper
                            .getColorCalibration().length];
                    for (int i = 0; i < mScreenCalibration.length; i++) {
                        String title = "";
                        switch (i) {
                            case 0:
                                title = getString(R.string.red);
                                break;
                            case 1:
                                title = getString(R.string.green);
                                break;
                            case 2:
                                title = getString(R.string.blue);
                                break;
                        }
                        mScreenCalibration[i] = prefHelper.setPreference(title,
                                screenHelper.getColorCalibration()[i],
                                getActivity());

                        root.addPreference(mScreenCalibration[i]);
                    }
                }
            }
        }.start();

        setPreferenceScreen(root);
    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen,
            final Preference preference) {

        for (int i = 0; i < mScreenCalibration.length; i++)
            if (preference == mScreenCalibration[i]) {
                final int position = i;
                String[] value = new String[226];
                for (int x = 0; x < value.length; x++)
                    value[x] = String.valueOf(x + 30);

                mDialog.showSeekBarDialog(value, value, preference.getSummary()
                        .toString(), new DialogReturn() {
                    @Override
                    public void dialogReturn(String value) {
                        String commandvalue = "";
                        String[] currentvalue = screenHelper
                                .getColorCalibration();
                        for (int i = 0; i < currentvalue.length; i++) {
                            String command = i == position ? value
                                    : currentvalue[i];

                            commandvalue = !commandvalue.isEmpty() ? commandvalue
                                    + " " + command
                                    : command;
                        }
                        mCommandControl.runCommand(commandvalue,
                                screenHelper.getColorCalibrationFile(),
                                CommandType.GENERIC, position, getActivity());
                        if (screenHelper.hasColorCalibrationCtrl()) mCommandControl
                                .runCommand("1", screenHelper
                                        .getColorCalibrationCtrlFile(),
                                        CommandType.GENERIC, position,
                                        getActivity());
                        preference.setSummary(value);
                    }
                }, getActivity());
            }

        return true;
    }
}
