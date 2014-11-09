package com.pacman.performance.fragments;

import java.util.ArrayList;
import java.util.List;

import com.pacman.performance.utils.Constants;
import com.pacman.performance.utils.CommandControl.CommandType;
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

public class VirtualMachineFragment extends Fragment implements Constants,
        OnApplyListener {

    private ListView list;
    private List<Item> views = new ArrayList<Item>();

    private EditTextView[] mVMs;

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

            mVMs = new EditTextView[virtualmachineHelper.getVMfiles().size()];
            for (int i = 0; i < virtualmachineHelper.getVMfiles().size(); i++) {
                mVMs[i] = new EditTextView();
                mVMs[i].setTitle(virtualmachineHelper.getVMfiles().get(i)
                        .replace("_", " "));
                mVMs[i].setValue(virtualmachineHelper
                        .getVMValue(virtualmachineHelper.getVMfiles().get(i)));
                mVMs[i].setInputType(InputType.TYPE_CLASS_NUMBER);
                mVMs[i].setOnApplyListener(VirtualMachineFragment.this);
                views.add(mVMs[i]);
            }

            list.setAdapter(new GenericAdapter(getActivity(), views));
        }
    };

    @Override
    public void onApply(EditTextView edittextView, String value) {
        for (int i = 0; i < virtualmachineHelper.getVMfiles().size(); i++)
            if (edittextView == mVMs[i])
                mCommandControl.runCommand(value, VM_PATH + "/"
                        + virtualmachineHelper.getVMfiles().get(i),
                        CommandType.GENERIC, -1, getActivity());
    }

}
