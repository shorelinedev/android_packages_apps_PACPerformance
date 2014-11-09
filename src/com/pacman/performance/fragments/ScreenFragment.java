package com.pacman.performance.fragments;

import java.util.ArrayList;
import java.util.List;

import com.pacman.performance.R;
import com.pacman.performance.utils.CommandControl.CommandType;
import com.pacman.performance.utils.Constants;
import com.pacman.performance.utils.views.GenericView.GenericAdapter;
import com.pacman.performance.utils.views.GenericView.Item;
import com.pacman.performance.utils.views.HeaderItem;
import com.pacman.performance.utils.views.SeekBarView;
import com.pacman.performance.utils.views.SeekBarView.OnSeekBarListener;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class ScreenFragment extends Fragment implements Constants,
        OnSeekBarListener {

    private ListView list;
    private List<Item> views = new ArrayList<Item>();

    private SeekBarView[] mScreenCalibration;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        list = new ListView(getActivity());
        list.setPadding(20, 0, 20, 0);

        getActivity().runOnUiThread(run);
        return list;
    }

    private final Runnable run = new Runnable() {

        @Override
        public void run() {
            views.clear();

            if (screenHelper.hasColorCalibration()) {
                views.add(new HeaderItem(getString(R.string.color_calibration)));

                mScreenCalibration = new SeekBarView[screenHelper
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
                    mScreenCalibration[i] = new SeekBarView(
                            screenHelper.getLimits());
                    mScreenCalibration[i].setTitle(title);
                    mScreenCalibration[i]
                            .setOnSeekBarListener(ScreenFragment.this);
                    mScreenCalibration[i].setItem(screenHelper
                            .getColorCalibration()[i]);

                    views.add(mScreenCalibration[i]);
                }

                list.setAdapter(new GenericAdapter(getActivity(), views));
            }
        }
    };

    @Override
    public void onStop(SeekBarView seekBarView, int item) {
        if (screenHelper.hasColorCalibration())
            for (int i = 0; i < mScreenCalibration.length; i++)
                if (seekBarView == mScreenCalibration[i]) {
                    String commandvalue = "";
                    String[] currentvalue = screenHelper.getColorCalibration();
                    for (int x = 0; x < currentvalue.length; x++) {
                        String command = x == i ? screenHelper.getLimits()[item]
                                : currentvalue[x];

                        commandvalue = !commandvalue.isEmpty() ? commandvalue
                                + " " + command : command;
                    }
                    mCommandControl.runCommand(commandvalue,
                            screenHelper.getColorCalibrationFile(),
                            CommandType.GENERIC, i, getActivity());
                    if (screenHelper.hasColorCalibrationCtrl())
                        mCommandControl.runCommand("1",
                                screenHelper.getColorCalibrationCtrlFile(),
                                CommandType.GENERIC, i, getActivity());
                }
    }

}
