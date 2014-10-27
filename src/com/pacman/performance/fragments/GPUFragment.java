package com.pacman.performance.fragments;

import com.pacman.performance.R;
import com.pacman.performance.utils.Constants;
import com.pacman.performance.utils.CommandControl.CommandType;
import com.pacman.performance.utils.Dialog.DialogReturn;
import com.pacman.performance.utils.views.PreferenceView.CustomCategory;
import com.pacman.performance.utils.views.PreferenceView.CustomPreference;

import android.os.Bundle;
import android.os.Handler;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;

public class GPUFragment extends PreferenceFragment implements Constants {

    private final Handler hand = new Handler();

    private PreferenceScreen root;

    private CustomPreference mGpu2dCurFreq, mGpu2dMaxFreq, mGpu2dGovernor;
    private String[] mAvailable2dFreqs;

    private CustomPreference mGpu3dCurFreq, mGpu3dMaxFreq, mGpu3dGovernor;
    private String[] mAvailable3dFreqs;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        root = getPreferenceManager().createPreferenceScreen(getActivity());

        setPreferenceScreen(root);

        getActivity().runOnUiThread(run);
        hand.post(run2);
    }

    private final Runnable run = new Runnable() {

        @Override
        public void run() {
            if (gpuHelper.hasGpu2dFreqs()) {
                mAvailable2dFreqs = new String[gpuHelper.getGpu2dFreqs().length];
                for (int i = 0; i < gpuHelper.getGpu2dFreqs().length; i++)
                    mAvailable2dFreqs[i] = (Integer.parseInt(gpuHelper
                            .getGpu2dFreqs()[i]) / 1000000)
                            + getString(R.string.mhz);
            }

            if (gpuHelper.hasGpu3dFreqs()) {
                mAvailable3dFreqs = new String[gpuHelper.getGpu3dFreqs().length];
                for (int i = 0; i < gpuHelper.getGpu3dFreqs().length; i++)
                    mAvailable3dFreqs[i] = (Integer.parseInt(gpuHelper
                            .getGpu3dFreqs()[i]) / 1000000)
                            + getString(R.string.mhz);
            }

            if (gpuHelper.hasGpu2dCurFreq()) {
                root.addPreference(new CustomCategory(getActivity(),
                        getString(R.string.gpu_stats)));

                mGpu2dCurFreq = new CustomPreference(getActivity(),
                        getString(R.string.gpu_2d_cur_freq),
                        (gpuHelper.getGpu2dCurFreq() / 1000000)
                                + getString(R.string.mhz));

                root.addPreference(mGpu2dCurFreq);
            }

            if (gpuHelper.hasGpu3dCurFreq()) {
                root.addPreference(new CustomCategory(getActivity(),
                        getString(R.string.gpu_stats)));

                mGpu3dCurFreq = new CustomPreference(getActivity(),
                        getString(R.string.gpu_3d_cur_freq),
                        (gpuHelper.getGpu3dCurFreq() / 1000000)
                                + getString(R.string.mhz));

                root.addPreference(mGpu3dCurFreq);
            }

            if (gpuHelper.hasGpu2dMaxFreq() || gpuHelper.hasGpu2dGovernor()
                    || gpuHelper.hasGpu3dMaxFreq()
                    || gpuHelper.hasGpu3dGovernor())
                root.addPreference(new CustomCategory(getActivity(),
                        getString(R.string.parameters)));

            if (gpuHelper.hasGpu2dMaxFreq()) {
                mGpu2dMaxFreq = new CustomPreference(getActivity(),
                        getString(R.string.gpu_2d_max_freq),
                        (gpuHelper.getGpu2dMaxFreq() / 1000000)
                                + getString(R.string.mhz));

                root.addPreference(mGpu2dMaxFreq);
            }

            if (gpuHelper.hasGpu3dMaxFreq()) {
                mGpu3dMaxFreq = new CustomPreference(getActivity(),
                        getString(R.string.gpu_3d_max_freq),
                        (gpuHelper.getGpu3dMaxFreq() / 1000000)
                                + getString(R.string.mhz));

                root.addPreference(mGpu3dMaxFreq);
            }

            if (gpuHelper.hasGpu2dGovernor()) {
                mGpu2dGovernor = new CustomPreference(getActivity(),
                        getString(R.string.gpu_2d_governor),
                        gpuHelper.getGpu2dGovernor());

                root.addPreference(mGpu2dGovernor);
            }

            if (gpuHelper.hasGpu3dGovernor()) {
                mGpu3dGovernor = new CustomPreference(getActivity(),
                        getString(R.string.gpu_3d_governor),
                        gpuHelper.getGpu3dGovernor());

                root.addPreference(mGpu3dGovernor);
            }
        }
    };

    private final Runnable run2 = new Runnable() {
        @Override
        public void run() {

            if (mGpu2dCurFreq != null)
                mGpu2dCurFreq
                        .setSummary((gpuHelper.getGpu2dCurFreq() / 1000000)
                                + getString(R.string.mhz));
            if (mGpu2dMaxFreq != null)
                mGpu2dMaxFreq
                        .setSummary((gpuHelper.getGpu2dMaxFreq() / 1000000)
                                + getString(R.string.mhz));

            if (mGpu3dCurFreq != null)
                mGpu3dCurFreq
                        .setSummary((gpuHelper.getGpu3dCurFreq() / 1000000)
                                + getString(R.string.mhz));
            if (mGpu3dMaxFreq != null)
                mGpu3dMaxFreq
                        .setSummary((gpuHelper.getGpu3dMaxFreq() / 1000000)
                                + getString(R.string.mhz));

            hand.postDelayed(run2, 1000);
        }
    };

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen,
            final Preference preference) {

        if (preference == mGpu2dMaxFreq && gpuHelper.hasGpu2dFreqs())
            mDialog.showDialogList(mAvailable2dFreqs,
                    gpuHelper.getGpu2dFreqs(), gpuHelper.getGpu2dFreqFile(),
                    new DialogReturn() {
                        @Override
                        public void dialogReturn(String value) {
                            preference.setSummary(value);
                        }
                    }, CommandType.GENERIC, -1, getActivity());

        if (preference == mGpu2dGovernor)
            mDialog.showDialogList(gpuHelper.getGpu2dGovernors(), null,
                    gpuHelper.getGpu2dGovernorFile(), new DialogReturn() {
                        @Override
                        public void dialogReturn(String value) {
                            try {
                                Thread.sleep(10);
                                preference.setSummary(gpuHelper
                                        .getGpu2dGovernor());
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }, CommandType.GENERIC, -1, getActivity());

        if (preference == mGpu3dMaxFreq && gpuHelper.hasGpu3dFreqs())
            mDialog.showDialogList(mAvailable3dFreqs,
                    gpuHelper.getGpu3dFreqs(), gpuHelper.getGpu3dFreqFile(),
                    new DialogReturn() {
                        @Override
                        public void dialogReturn(String value) {
                            preference.setSummary(value);
                        }
                    }, CommandType.GENERIC, -1, getActivity());

        if (preference == mGpu3dGovernor)
            mDialog.showDialogList(gpuHelper.getGpu3dGovernors(), null,
                    gpuHelper.getGpu3dGovernorFile(), new DialogReturn() {
                        @Override
                        public void dialogReturn(String value) {
                            try {
                                Thread.sleep(10);
                                preference.setSummary(gpuHelper
                                        .getGpu3dGovernor());
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }, CommandType.GENERIC, -1, getActivity());

        return true;
    }

    @Override
    public void onDestroy() {
        hand.removeCallbacks(run2);
        super.onDestroy();
    };
}
