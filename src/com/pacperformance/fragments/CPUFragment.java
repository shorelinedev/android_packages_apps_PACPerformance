package com.pacperformance.fragments;

import java.util.ArrayList;
import java.util.List;

import com.pacperformance.MainActivity;
import com.pacperformance.R;
import com.pacperformance.utils.CPUHelper;
import com.pacperformance.utils.Control;
import com.pacperformance.utils.LayoutHelper;
import com.pacperformance.utils.Utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class CPUFragment extends Fragment implements OnSeekBarChangeListener {

	private static Context context;
	private static LinearLayout layout;
	private static OnSeekBarChangeListener OnSeekBarChangeListener;

	private static LinearLayout mCurFreqLayout;
	private static CurCpuThread mCurCpuThread;
	private static TextView[] mCurFreqTexts;

	private static LinearLayout mFreqScalingLayout;

	private static List<String> mAvailableFreqList = new ArrayList<String>();
	private static SeekBar mMaxFreqScalingBar;
	private static TextView mMaxFreqScalingText;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		context = getActivity();
		View rootView = inflater.inflate(R.layout.generic, container, false);
		layout = (LinearLayout) rootView.findViewById(R.id.layout);

		OnSeekBarChangeListener = this;

		setLayout();
		return rootView;
	}

	public static void setLayout() {
		layout.removeAllViews();

		mCurFreqLayout = new LinearLayout(context);
		mCurFreqLayout.setPadding(0, 100, 0, 0);
		mCurFreqLayout.setOrientation(LinearLayout.VERTICAL);
		if (Utils.exist(CPUHelper.FREQUENCY_SCALING.replace("present", "0")))
			layout.addView(mCurFreqLayout);

		TextView mCurFreqTitle = new TextView(context);
		LayoutHelper.setTextTitle(mCurFreqTitle,
				context.getString(R.string.currentfreq), context);
		mCurFreqLayout.addView(mCurFreqTitle);

		mCurFreqTexts = new TextView[CPUHelper.getCoreValue()];
		for (int i = 0; i < CPUHelper.getCoreValue(); i += 2) {
			TextView mCurFreqText = new TextView(context);
			mCurFreqText.setGravity(Gravity.CENTER);
			mCurFreqText.setTextSize(20);
			mCurFreqTexts[i] = mCurFreqText;
			mCurFreqLayout.addView(mCurFreqText);
		}

		mFreqScalingLayout = new LinearLayout(context);
		mFreqScalingLayout.setPaddingRelative(0, 120, 0, 0);
		mFreqScalingLayout.setOrientation(LinearLayout.VERTICAL);
		if (Utils.exist(CPUHelper.MIN_SCREEN_ON)
				|| (Utils.exist(CPUHelper.MAX_SCREEN_OFF)
						|| Utils.exist(CPUHelper.MIN_FREQ) || Utils
							.exist(CPUHelper.MAX_FREQ)))
			layout.addView(mFreqScalingLayout);

		TextView mMaxScalingText = new TextView(context);
		LayoutHelper.setTextTitle(mMaxScalingText,
				context.getString(R.string.maxfreq), context);
		mFreqScalingLayout.addView(mMaxScalingText);

		mAvailableFreqList.clear();
		for (int text : CPUHelper.getAvailableFreq())
			mAvailableFreqList.add(String.valueOf(text));

		if (Integer.parseInt(mAvailableFreqList.get(0)) > Integer
				.parseInt(mAvailableFreqList.get(mAvailableFreqList.size() - 1))) {
			mAvailableFreqList.clear();
			for (int i = CPUHelper.getAvailableFreq().length; i > 0; i--)
				mAvailableFreqList.add(String.valueOf(CPUHelper
						.getAvailableFreq()[i]));
		}

		int mMax = mAvailableFreqList.indexOf(String.valueOf(CPUHelper
				.getMaxFreq()));

		mMaxFreqScalingBar = new SeekBar(context);
		LayoutHelper.setSeekBar(mMaxFreqScalingBar,
				mAvailableFreqList.size() - 1, mMax);
		mMaxFreqScalingBar.setOnSeekBarChangeListener(OnSeekBarChangeListener);
		mFreqScalingLayout.addView(mMaxFreqScalingBar);

		mMaxFreqScalingText = new TextView(context);
		LayoutHelper.setSeekBarText(mMaxFreqScalingText,
				String.valueOf(CPUHelper.getMaxFreq() / 1000) + "MHz");
		mFreqScalingLayout.addView(mMaxFreqScalingText);
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

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		MainActivity.showButtons(true);
		MainActivity.CPUChange = true;
		if (seekBar.equals(mMaxFreqScalingBar))
			mMaxFreqScalingText
					.setText(String.valueOf(Integer.parseInt(mAvailableFreqList
							.get(progress)) / 1000) + "MHz");
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {

	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		if (seekBar.equals(mMaxFreqScalingBar))
			Control.MAX_CPU_FREQ = mMaxFreqScalingText.getText().toString()
					.replace(getString(R.string.mhz), "000");
	}
}
