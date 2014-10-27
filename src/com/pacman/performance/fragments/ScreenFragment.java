package com.pacman.performance.fragments;

import com.pacman.performance.R;
import com.pacman.performance.utils.Constants;
import com.pacman.performance.utils.CommandControl.CommandType;
import com.pacman.performance.utils.Dialog.DialogReturn;
import com.pacman.performance.utils.views.PreferenceView.CustomCategory;
import com.pacman.performance.utils.views.PreferenceView.CustomPreference;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;

public class ScreenFragment extends PreferenceFragment implements Constants {

    private PreferenceScreen root;
    private CustomPreference[] mScreenCalibration;

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
            if (screenHelper.hasColorCalibration()) {
                root.addPreference(new CustomCategory(getActivity(),
                        getString(R.string.color_calibration)));

                mScreenCalibration = new CustomPreference[screenHelper
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
                    mScreenCalibration[i] = new CustomPreference(getActivity(),
                            title, screenHelper.getColorCalibration()[i]);

                    root.addPreference(mScreenCalibration[i]);
                }
            }
        }
    };

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
                        if (screenHelper.hasColorCalibrationCtrl())
                            mCommandControl.runCommand("1",
                                    screenHelper.getColorCalibrationCtrlFile(),
                                    CommandType.GENERIC, position,
                                    getActivity());
                        preference.setSummary(value);
                    }
                }, getActivity());
            }

        return true;
    }
}
