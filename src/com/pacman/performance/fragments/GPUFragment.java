package com.pacman.performance.fragments;

import java.util.ArrayList;
import java.util.List;

import com.pacman.performance.R;
import com.pacman.performance.utils.Constants;
import com.pacman.performance.utils.CommandControl.CommandType;
import com.pacman.performance.utils.views.CommonView;
import com.pacman.performance.utils.views.GenericView.GenericAdapter;
import com.pacman.performance.utils.views.GenericView.Item;
import com.pacman.performance.utils.views.HeaderItem;
import com.pacman.performance.utils.views.PopupView;
import com.pacman.performance.utils.views.PopupView.OnItemClickListener;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class GPUFragment extends Fragment implements Constants,
        OnItemClickListener {

    private final Handler hand = new Handler();

    private ListView list;
    private List<Item> views = new ArrayList<Item>();

    private CommonView mGpu2dCurFreq;
    private PopupView mGpu2dMaxFreq, mGpu2dGovernor;
    private String[] mAvailable2dFreqs;

    private CommonView mGpu3dCurFreq;
    private PopupView mGpu3dMaxFreq, mGpu3dGovernor;
    private String[] mAvailable3dFreqs;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        list = new ListView(getActivity());
        list.setPadding(20, 0, 20, 0);

        getActivity().runOnUiThread(run);
        hand.post(run2);
        return list;
    }

    private final Runnable run = new Runnable() {

        @Override
        public void run() {
            views.clear();

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

            if (gpuHelper.hasGpu2dCurFreq() || gpuHelper.hasGpu3dCurFreq())
                views.add(new HeaderItem(getString(R.string.gpu_stats)));

            if (gpuHelper.hasGpu2dCurFreq()) {
                mGpu2dCurFreq = new CommonView();
                mGpu2dCurFreq.setTitle(getString(R.string.gpu_2d_cur_freq));
                mGpu2dCurFreq
                        .setSummary((gpuHelper.getGpu2dCurFreq() / 1000000)
                                + getString(R.string.mhz));
                views.add(mGpu2dCurFreq);
            }

            if (gpuHelper.hasGpu3dCurFreq()) {
                mGpu3dCurFreq = new CommonView();
                mGpu3dCurFreq.setTitle(getString(R.string.gpu_3d_cur_freq));
                mGpu3dCurFreq
                        .setSummary((gpuHelper.getGpu3dCurFreq() / 1000000)
                                + getString(R.string.mhz));
                views.add(mGpu3dCurFreq);
            }

            if (gpuHelper.hasGpu2dMaxFreq() || gpuHelper.hasGpu2dGovernor()
                    || gpuHelper.hasGpu3dMaxFreq()
                    || gpuHelper.hasGpu3dGovernor())
                views.add(new HeaderItem(getString(R.string.parameters)));

            if (gpuHelper.hasGpu2dMaxFreq()) {
                mGpu2dMaxFreq = new PopupView(getActivity(), mAvailable2dFreqs);
                mGpu2dMaxFreq.setTitle(getString(R.string.gpu_2d_max_freq));
                mGpu2dMaxFreq.setItem((gpuHelper.getGpu2dMaxFreq() / 1000000)
                        + getString(R.string.mhz));
                mGpu2dMaxFreq.setOnItemClickListener(GPUFragment.this);
                views.add(mGpu2dMaxFreq);
            }

            if (gpuHelper.hasGpu3dMaxFreq()) {
                mGpu3dMaxFreq = new PopupView(getActivity(), mAvailable3dFreqs);
                mGpu3dMaxFreq.setTitle(getString(R.string.gpu_3d_max_freq));
                mGpu3dMaxFreq.setItem((gpuHelper.getGpu3dMaxFreq() / 1000000)
                        + getString(R.string.mhz));
                mGpu3dMaxFreq.setOnItemClickListener(GPUFragment.this);
                views.add(mGpu3dMaxFreq);
            }

            if (gpuHelper.hasGpu2dGovernor()) {
                mGpu2dGovernor = new PopupView(getActivity(),
                        gpuHelper.getGpu2dGovernors());
                mGpu2dGovernor.setTitle(getString(R.string.gpu_2d_governor));
                mGpu2dGovernor.setItem(gpuHelper.getGpu2dGovernor());
                mGpu2dGovernor.setOnItemClickListener(GPUFragment.this);
                views.add(mGpu2dGovernor);
            }

            if (gpuHelper.hasGpu3dGovernor()) {
                mGpu3dGovernor = new PopupView(getActivity(),
                        gpuHelper.getGpu3dGovernors());
                mGpu3dGovernor.setTitle(getString(R.string.gpu_3d_governor));
                mGpu3dGovernor.setItem(gpuHelper.getGpu3dGovernor());
                mGpu3dGovernor.setOnItemClickListener(GPUFragment.this);
                views.add(mGpu3dGovernor);
            }

            list.setAdapter(new GenericAdapter(getActivity(), views));
        }
    };

    @Override
    public void onItemClick(PopupView popupView, int item) {
        if (popupView == mGpu2dMaxFreq)
            mCommandControl.runCommand(gpuHelper.getGpu2dFreqs()[item],
                    gpuHelper.getGpu2dFreqFile(), CommandType.GENERIC, -1,
                    getActivity());

        if (popupView == mGpu2dGovernor)
            mCommandControl.runCommand(gpuHelper.getGpu2dGovernors()[item],
                    gpuHelper.getGpu2dGovernorFile(), CommandType.GENERIC, -1,
                    getActivity());

        if (popupView == mGpu3dMaxFreq)
            mCommandControl.runCommand(gpuHelper.getGpu3dFreqs()[item],
                    gpuHelper.getGpu3dFreqFile(), CommandType.GENERIC, -1,
                    getActivity());

        if (popupView == mGpu3dGovernor)
            mCommandControl.runCommand(gpuHelper.getGpu3dGovernors()[item],
                    gpuHelper.getGpu3dGovernorFile(), CommandType.GENERIC, -1,
                    getActivity());
    }

    private final Runnable run2 = new Runnable() {
        @Override
        public void run() {

            if (mGpu2dCurFreq != null)
                mGpu2dCurFreq
                        .setSummary((gpuHelper.getGpu2dCurFreq() / 1000000)
                                + getString(R.string.mhz));
            if (mGpu2dMaxFreq != null)
                mGpu2dMaxFreq.setItem((gpuHelper.getGpu2dMaxFreq() / 1000000)
                        + getString(R.string.mhz));

            if (mGpu3dCurFreq != null)
                mGpu3dCurFreq
                        .setSummary((gpuHelper.getGpu3dCurFreq() / 1000000)
                                + getString(R.string.mhz));
            if (mGpu3dMaxFreq != null)
                mGpu3dMaxFreq.setItem((gpuHelper.getGpu3dMaxFreq() / 1000000)
                        + getString(R.string.mhz));

            hand.postDelayed(run2, 1000);
        }
    };

    @Override
    public void onDestroy() {
        hand.removeCallbacks(run2);
        super.onDestroy();
    };

}
