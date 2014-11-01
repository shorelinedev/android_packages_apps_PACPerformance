package com.pacman.performance.fragments;

import java.util.ArrayList;
import java.util.List;

import com.pacman.performance.R;
import com.pacman.performance.utils.CommandControl.CommandType;
import com.pacman.performance.utils.Constants;
import com.pacman.performance.utils.views.EditTextView;
import com.pacman.performance.utils.views.EditTextView.OnApplyListener;
import com.pacman.performance.utils.views.GenericView.GenericAdapter;
import com.pacman.performance.utils.views.GenericView.Item;

import android.app.Fragment;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class CPUVoltageFragment extends Fragment implements Constants,
        OnApplyListener {

    private ListView list;
    private List<Item> views = new ArrayList<Item>();

    private EditTextView[] mVoltage;

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

            if (cpuVoltageHelper.hasCpuVoltage()) {
                mVoltage = new EditTextView[cpuVoltageHelper.getFreqs().length];
                for (int i = 0; i < cpuVoltageHelper.getFreqs().length; i++) {
                    mVoltage[i] = new EditTextView();
                    mVoltage[i].setTitle(cpuVoltageHelper.getFreqs()[i]
                            + getString(R.string.mhz));
                    mVoltage[i].setValue(cpuVoltageHelper.getVoltages()[i]);
                    mVoltage[i].setInputType(InputType.TYPE_CLASS_NUMBER);
                    mVoltage[i].setOnApplyListener(CPUVoltageFragment.this);

                    views.add(mVoltage[i]);
                }
            }

            list.setAdapter(new GenericAdapter(getActivity(), views));
        }
    };

    @Override
    public void onApply(EditTextView edittextView, String value) {
        for (int i = 0; i < cpuVoltageHelper.getFreqs().length; i++)
            if (edittextView == mVoltage[i]) {
                String commandvalue = "";
                String[] currentvalue = cpuVoltageHelper.getVoltages();
                for (int x = 0; x < currentvalue.length; x++) {
                    String command = x == i ? value : currentvalue[x];

                    commandvalue = !commandvalue.isEmpty() ? commandvalue + " "
                            + command : command;
                }
                mCommandControl.runCommand(commandvalue, CPU_VOLTAGE,
                        CommandType.CPU, i, getActivity());

                new Thread() {
                    public void run() {
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                for (int i = 0; i < cpuVoltageHelper.getFreqs().length; i++)
                                    mVoltage[i].setValue(cpuVoltageHelper
                                            .getVoltages()[i]);
                            }
                        });
                    }
                }.start();
            }
    }

}
