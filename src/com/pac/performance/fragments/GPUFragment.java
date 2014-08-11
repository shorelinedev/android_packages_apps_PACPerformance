package com.pac.performance.fragments;

import com.pac.performance.R;
import com.pac.performance.utils.CommandControl.CommandType;
import com.pac.performance.utils.Constants;
import com.pac.performance.utils.interfaces.DialogReturn;

import android.os.Bundle;
import android.os.Handler;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;

public class GPUFragment extends PreferenceFragment implements Constants {

    private final Handler hand = new Handler();

    private Preference mGpuCurFreq;
    private Preference mGpuMaxFreq;
    private Preference mGpuGovernor;

    private String[] mAvailableFreqs;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (gpuHelper.hasGpuFreqs()) {
            mAvailableFreqs = new String[gpuHelper.getGpuFreqs().length];
            for (int i = 0; i < gpuHelper.getGpuFreqs().length; i++)
                mAvailableFreqs[i] = (Integer
                        .parseInt(gpuHelper.getGpuFreqs()[i]) / 1000000)
                        + getString(R.string.mhz);
        }

        PreferenceScreen root = getPreferenceManager().createPreferenceScreen(
                getActivity());

        if (gpuHelper.hasGpuCurFreq()) {
            root.addPreference(prefHelper.setPreferenceCategory(
                    getString(R.string.gpu_stats), getActivity()));

            mGpuCurFreq = prefHelper.setPreference(
                    getString(R.string.cur_freq),
                    (gpuHelper.getGpuCurFreq() / 1000000)
                            + getString(R.string.mhz), getActivity());

            root.addPreference(mGpuCurFreq);
        }

        if (gpuHelper.hasGpuMaxFreq() || gpuHelper.hasGpuGovernor()) root
                .addPreference(prefHelper.setPreferenceCategory(
                        getString(R.string.parameters), getActivity()));

        if (gpuHelper.hasGpuMaxFreq()) {
            mGpuMaxFreq = prefHelper.setPreference(
                    getString(R.string.gpu_max_freq),
                    (gpuHelper.getGpuMaxFreq() / 1000000)
                            + getString(R.string.mhz), getActivity());

            root.addPreference(mGpuMaxFreq);
        }

        if (gpuHelper.hasGpuGovernor()) {
            mGpuGovernor = prefHelper.setPreference(
                    getString(R.string.gpu_governor),
                    gpuHelper.getGpuGovernor(), getActivity());

            root.addPreference(mGpuGovernor);
        }

        setPreferenceScreen(root);
    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen,
            final Preference preference) {

        if (preference == mGpuMaxFreq && gpuHelper.hasGpuFreqs()) mDialog
                .showDialogList(mAvailableFreqs, gpuHelper.getGpuFreqs(),
                        gpuHelper.GPU_MAX_FREQ, new DialogReturn() {
                            @Override
                            public void dialogReturn(String value) {
                                preference.setSummary(value);
                            }
                        }, CommandType.GENERIC, getActivity());

        if (preference == mGpuGovernor) mDialog.showDialogList(
                gpuHelper.getGpuGovernors(), null,
                gpuHelper.GPU_SCALING_GOVERNOR, new DialogReturn() {
                    @Override
                    public void dialogReturn(String value) {
                        try {
                            Thread.sleep(10);
                            preference.setSummary(gpuHelper.getGpuGovernor());
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }, CommandType.GENERIC, getActivity());

        return true;
    }

    @Override
    public void onResume() {
        hand.post(run);
        super.onResume();
    }

    Runnable run = new Runnable() {
        @Override
        public void run() {

            if (mGpuCurFreq != null) mGpuCurFreq.setSummary((gpuHelper
                    .getGpuCurFreq() / 1000000) + getString(R.string.mhz));
            if (mGpuMaxFreq != null) mGpuMaxFreq.setSummary((gpuHelper
                    .getGpuMaxFreq() / 1000000) + getString(R.string.mhz));

            hand.postDelayed(run, 1000);
        }
    };

    public void onDestroy() {
        hand.removeCallbacks(run);
        super.onDestroy();
    };
}
