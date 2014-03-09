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

	private static SeekBar mMinFreqScalingBar;
	private static TextView mMinFreqScalingText;

	private static SeekBar mMaxScreenOffFreqScalingBar;
	private static TextView mMaxScreenOffFreqScalingText;

	private static SeekBar mMinScreenOnFreqScalingBar;
	private static TextView mMinScreenOnFreqScalingText;

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

		// Current Freq
		mCurFreqLayout = new LinearLayout(context);
		mCurFreqLayout.setPadding(0, (int) (MainActivity.mHeight / 15), 0, 0);
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
		setCurFreq();

		// Create a layout for scaling
		mFreqScalingLayout = new LinearLayout(context);
		mFreqScalingLayout.setPaddingRelative(0,
				(int) (MainActivity.mHeight / 10), 0, 0);
		mFreqScalingLayout.setOrientation(LinearLayout.VERTICAL);
		if (Utils.exist(CPUHelper.MIN_SCREEN_ON)
				|| (Utils.exist(CPUHelper.MAX_SCREEN_OFF)
						|| Utils.exist(CPUHelper.MIN_FREQ) || Utils
							.exist(CPUHelper.MAX_FREQ)))
			layout.addView(mFreqScalingLayout);

		// Create a list to store all freqs
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

		// Max Cpu Scaling
		TextView mMaxScalingText = new TextView(context);
		LayoutHelper.setTextTitle(mMaxScalingText,
				context.getString(R.string.maxfreq), context);
		if (Utils.exist(CPUHelper.MAX_FREQ))
			mFreqScalingLayout.addView(mMaxScalingText);

		int mMax = mAvailableFreqList.indexOf(String.valueOf(CPUHelper
				.getMaxFreq()));

		mMaxFreqScalingBar = new SeekBar(context);
		LayoutHelper.setSeekBar(mMaxFreqScalingBar,
				mAvailableFreqList.size() - 1, mMax);
		mMaxFreqScalingBar.setOnSeekBarChangeListener(OnSeekBarChangeListener);
		if (Utils.exist(CPUHelper.MAX_FREQ))
			mFreqScalingLayout.addView(mMaxFreqScalingBar);

		mMaxFreqScalingText = new TextView(context);
		LayoutHelper.setSeekBarText(
				mMaxFreqScalingText,
				String.valueOf(CPUHelper.getMaxFreq() / 1000)
						+ context.getString(R.string.mhz));
		if (Utils.exist(CPUHelper.MAX_FREQ))
			mFreqScalingLayout.addView(mMaxFreqScalingText);

		// Min Cpu Scaling
		TextView mMinScalingText = new TextView(context);
		LayoutHelper.setTextTitle(mMinScalingText,
				context.getString(R.string.minfreq), context);
		if (Utils.exist(CPUHelper.MIN_FREQ))
			mFreqScalingLayout.addView(mMinScalingText);

		int mMin = mAvailableFreqList.indexOf(String.valueOf(CPUHelper
				.getMinFreq()));

		mMinFreqScalingBar = new SeekBar(context);
		LayoutHelper.setSeekBar(mMinFreqScalingBar,
				mAvailableFreqList.size() - 1, mMin);
		mMinFreqScalingBar.setOnSeekBarChangeListener(OnSeekBarChangeListener);
		if (Utils.exist(CPUHelper.MIN_FREQ))
			mFreqScalingLayout.addView(mMinFreqScalingBar);

		mMinFreqScalingText = new TextView(context);
		LayoutHelper.setSeekBarText(
				mMinFreqScalingText,
				String.valueOf(CPUHelper.getMinFreq() / 1000)
						+ context.getString(R.string.mhz));
		if (Utils.exist(CPUHelper.MIN_FREQ))
			mFreqScalingLayout.addView(mMinFreqScalingText);

		// Max Screen Off Cpu Scaling
		TextView mMaxScreenOffFreqText = new TextView(context);
		LayoutHelper.setTextTitle(mMaxScreenOffFreqText,
				context.getString(R.string.maxscreenofffreq), context);
		if (Utils.exist(CPUHelper.MAX_SCREEN_OFF))
			mFreqScalingLayout.addView(mMaxScreenOffFreqText);

		int mMaxScreenOff = mAvailableFreqList.indexOf(String.valueOf(CPUHelper
				.getMaxScreenOffFreq()));

		mMaxScreenOffFreqScalingBar = new SeekBar(context);
		LayoutHelper.setSeekBar(mMaxScreenOffFreqScalingBar,
				mAvailableFreqList.size() - 1, mMaxScreenOff);
		mMaxScreenOffFreqScalingBar
				.setOnSeekBarChangeListener(OnSeekBarChangeListener);
		if (Utils.exist(CPUHelper.MAX_SCREEN_OFF))
			mFreqScalingLayout.addView(mMaxScreenOffFreqScalingBar);

		mMaxScreenOffFreqScalingText = new TextView(context);
		LayoutHelper.setSeekBarText(mMaxScreenOffFreqScalingText,
				String.valueOf(CPUHelper.getMaxScreenOffFreq() / 1000)
						+ context.getString(R.string.mhz));
		if (Utils.exist(CPUHelper.MAX_SCREEN_OFF))
			mFreqScalingLayout.addView(mMaxScreenOffFreqScalingText);

		// Min Screen On Cpu Scaling
		TextView mMinScreenOnFreqText = new TextView(context);
		LayoutHelper.setTextTitle(mMinScreenOnFreqText,
				context.getString(R.string.minscreenonfreq), context);
		if (Utils.exist(CPUHelper.MIN_SCREEN_ON))
			mFreqScalingLayout.addView(mMinScreenOnFreqText);

		int mMinScreenOn = mAvailableFreqList.indexOf(String.valueOf(CPUHelper
				.getMinScreenOnFreq()));

		mMinScreenOnFreqScalingBar = new SeekBar(context);
		LayoutHelper.setSeekBar(mMinScreenOnFreqScalingBar,
				mAvailableFreqList.size() - 1, mMinScreenOn);
		mMinScreenOnFreqScalingBar
				.setOnSeekBarChangeListener(OnSeekBarChangeListener);
		if (Utils.exist(CPUHelper.MIN_SCREEN_ON))
			mFreqScalingLayout.addView(mMinScreenOnFreqScalingBar);

		mMinScreenOnFreqScalingText = new TextView(context);
		LayoutHelper.setSeekBarText(
				mMinScreenOnFreqScalingText,
				String.valueOf(CPUHelper.getMinScreenOnFreq() / 1000)
						+ context.getString(R.string.mhz));
		if (Utils.exist(CPUHelper.MIN_SCREEN_ON))
			mFreqScalingLayout.addView(mMinScreenOnFreqScalingText);
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
			setCurFreq();
		}
	};

	private static void setCurFreq() {
		for (int i = 0; i < CPUHelper.getCoreValue(); i += 2) {
			String freq1 = CPUHelper.getFreqScaling(i) == 0 ? context
					.getString(R.string.offline) : String.valueOf(CPUHelper
					.getFreqScaling(i) / 1000) + "MHz";
			String freq2 = CPUHelper.getFreqScaling(i + 1) == 0 ? context
					.getString(R.string.offline) : String.valueOf(CPUHelper
					.getFreqScaling(i + 1) / 1000) + "MHz";
			mCurFreqTexts[i].setText("Core " + String.valueOf(i + 1) + ": "
					+ freq1 + " Core " + String.valueOf(i + 2) + ": " + freq2);
		}
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		MainActivity.showButtons(true);
		MainActivity.CPUChange = true;
		if (seekBar.equals(mMaxFreqScalingBar)) {
			mMaxFreqScalingText
					.setText(String.valueOf(Integer.parseInt(mAvailableFreqList
							.get(progress)) / 1000) + "MHz");
			if (progress < mMinFreqScalingBar.getProgress())
				mMinFreqScalingBar.setProgress(progress);
		}
		if (seekBar.equals(mMinFreqScalingBar)) {
			mMinFreqScalingText
					.setText(String.valueOf(Integer.parseInt(mAvailableFreqList
							.get(progress)) / 1000) + "MHz");
			if (progress > mMaxFreqScalingBar.getProgress())
				mMaxFreqScalingBar.setProgress(progress);
		}
		if (seekBar.equals(mMaxScreenOffFreqScalingBar))
			mMaxScreenOffFreqScalingText
					.setText(String.valueOf(Integer.parseInt(mAvailableFreqList
							.get(progress)) / 1000) + "MHz");
		if (seekBar.equals(mMinScreenOnFreqScalingBar))
			mMinScreenOnFreqScalingText
					.setText(String.valueOf(Integer.parseInt(mAvailableFreqList
							.get(progress)) / 1000) + "MHz");
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		if (seekBar.equals(mMaxFreqScalingBar)
				|| (seekBar.equals(mMinFreqScalingBar))) {
			Control.MAX_CPU_FREQ = mMaxFreqScalingText.getText().toString()
					.replace(getString(R.string.mhz), "000");
			Control.MIN_CPU_FREQ = mMinFreqScalingText.getText().toString()
					.replace(getString(R.string.mhz), "000");
		} else if (seekBar.equals(mMaxScreenOffFreqScalingBar)) {
			Control.MAX_CPU_SCREEN_OFF_FREQ = mMaxScreenOffFreqScalingText
					.getText().toString()
					.replace(getString(R.string.mhz), "000");
		} else if (seekBar.equals(mMinScreenOnFreqScalingBar))
			Control.MIN_CPU_SCREEN_ON_FREQ = mMinScreenOnFreqScalingText
					.getText().toString()
					.replace(getString(R.string.mhz), "000");
	}
}
