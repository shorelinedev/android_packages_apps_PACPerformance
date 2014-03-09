package com.pacperformance.fragments;

import com.pacperformance.R;
import com.pacperformance.utils.CPUHelper;
import com.pacperformance.utils.LayoutHelper;
import com.pacperformance.utils.Utils;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CPUFragment extends Fragment {

	private LinearLayout layout;

	private CurCpuThread mCurCpuThread;
	private TextView[] mCurFreqTexts;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.generic, container, false);
		layout = (LinearLayout) rootView.findViewById(R.id.layout);

		setLayout();
		return rootView;
	}

	public void setLayout() {
		layout.removeAllViews();

		TextView mCurFreqTitle = new TextView(getActivity());
		LayoutHelper.setTextTitle(mCurFreqTitle,
				getString(R.string.currentfreq), getActivity());
		if (Utils.exist(CPUHelper.FREQUENCY_SCALING.replace("present", "0")))
			layout.addView(mCurFreqTitle);

		mCurFreqTexts = new TextView[CPUHelper.getCoreValue()];
		for (int i = 0; i < CPUHelper.getCoreValue(); i += 2) {
			TextView mCurFreqText = new TextView(getActivity());
			mCurFreqText.setGravity(Gravity.CENTER);
			mCurFreqText.setTextSize(20);
			mCurFreqTexts[i] = mCurFreqText;
			if (Utils
					.exist(CPUHelper.FREQUENCY_SCALING.replace("present", "0")))
				layout.addView(mCurFreqText);
		}

	}

	@Override
	public void onResume() {
		mCurCpuThread = new CurCpuThread();
		mCurCpuThread.start();
		super.onResume();
	}

	protected class CurCpuThread extends Thread {

		@Override
		public void run() {
			try {
				while (true) {
					mCurCPUHandler.sendMessage(mCurCPUHandler.obtainMessage(0,
							""));
					sleep(500);
				}
			} catch (InterruptedException e) {
			}
		}
	}

	@SuppressLint("HandlerLeak")
	protected Handler mCurCPUHandler = new Handler() {
		public void handleMessage(Message msg) {
			for (int i = 0; i < CPUHelper.getCoreValue(); i += 2) {
				String freq1 = CPUHelper.getFreqScaling(i) == 0 ? getString(R.string.offline)
						: String.valueOf(CPUHelper.getFreqScaling(i) / 1000)
								+ "MHz";
				String freq2 = CPUHelper.getFreqScaling(i + 1) == 0 ? getString(R.string.offline)
						: String.valueOf(CPUHelper.getFreqScaling(i + 1) / 1000)
								+ "MHz";
				mCurFreqTexts[i].setText("Core " + String.valueOf(i + 1) + ": "
						+ freq1 + " Core " + String.valueOf(i + 2) + ": "
						+ freq2);
			}
		}
	};
}
