package com.pac.performance.fragments;

import com.pac.performance.utils.CommandControl.CommandType;
import com.pac.performance.utils.Constants;
import com.pac.performance.utils.Dialog.DialogReturn;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;

public class VirtualMachineFragment extends PreferenceFragment implements
        Constants {

    private PreferenceScreen root;
    private Preference[] mVMs;

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
            mVMs = new Preference[virtualmachineHelper.getVMfiles().size()];
            for (int i = 0; i < virtualmachineHelper.getVMfiles().size(); i++) {
                mVMs[i] = prefHelper.setPreference(virtualmachineHelper
                        .getVMfiles().get(i).replace("_", " "),
                        virtualmachineHelper.getVMValue(virtualmachineHelper
                                .getVMfiles().get(i)), getActivity());

                root.addPreference(mVMs[i]);
            }
        }
    };

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen,
            final Preference preference) {

        for (int i = 0; i < virtualmachineHelper.getVMfiles().size(); i++)
            if (preference == mVMs[i]) mDialog.showDialogGeneric(VM_PATH + "/"
                    + virtualmachineHelper.getVMfiles().get(i), preference
                    .getSummary().toString(), new DialogReturn() {
                @Override
                public void dialogReturn(String value) {
                    preference.setSummary(value);
                }
            }, 0, CommandType.GENERIC, -1, getActivity());

        return true;
    }
}
