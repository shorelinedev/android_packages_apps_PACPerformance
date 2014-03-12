package com.pac.performance.fragments;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.pac.performance.MainActivity;
import com.pac.performance.R;
import com.pac.performance.helpers.CPUHelper;
import com.pac.performance.helpers.LayoutHelper;
import com.pac.performance.utils.Control;
import com.pac.performance.utils.InformationDialog;
import com.pac.performance.utils.Utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class CPUFragment extends Fragment implements OnSeekBarChangeListener,
		OnItemSelectedListener, OnClickListener, OnCheckedChangeListener {

	private static Context context;

	public static LinearLayout layout = null;

	private static OnSeekBarChangeListener OnSeekBarChangeListener;
	private static OnItemSelectedListener OnItemSelectedListener;
	private static OnClickListener OnClickListener;
	private static OnCheckedChangeListener OnCheckedChangeListener;

	private static LinearLayout mCurFreqLayout;
	private static CurCpuThread mCurCpuThread;
	private static TextView[] mCurFreqTexts;

	private static CheckBox[] mCoreControlBoxes;

	private static LinearLayout mFreqScalingLayout;

	private static List<String> mAvailableFreqList = new ArrayList<String>();

	private static SeekBar mMaxFreqScalingBar;
	private static TextView mMaxFreqScalingText;
	private static TextView mMaxScalingText;

	private static SeekBar mMinFreqScalingBar;
	private static TextView mMinFreqScalingText;
	private static TextView mMinScalingText;

	private static SeekBar mMaxScreenOffFreqScalingBar;
	private static TextView mMaxScreenOffFreqScalingText;
	private static TextView mMaxScreenOffFreqText;

	private static SeekBar mMinScreenOnFreqScalingBar;
	private static TextView mMinScreenOnFreqScalingText;
	private static TextView mMinScreenOnFreqText;

	private static Spinner mGovernorSpinner;
	private static String[] mAvailableGovernor;
	private static List<String> mAvailableGovernorList = new ArrayList<String>();
	private static TextView mGovernorTitle;

	private static CheckBox mIntelliPlugBox;
	private static CheckBox mIntelliPlugEcoModeBox;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		context = getActivity();
		View rootView = inflater.inflate(R.layout.generic, container, false);
		layout = (LinearLayout) rootView.findViewById(R.id.layout);

		OnSeekBarChangeListener = this;
		OnItemSelectedListener = this;
		OnClickListener = this;
		OnCheckedChangeListener = this;

		setLayout();
		return rootView;
	}

	public static void setLayout() {
		layout.removeAllViews();

		// Current Freq
		mCurFreqLayout = new LinearLayout(context);
		mCurFreqLayout.setPadding(0, (int) (MainActivity.mHeight / 25), 0, 0);
		mCurFreqLayout.setOrientation(LinearLayout.VERTICAL);
		if (Utils.exist(CPUHelper.FREQUENCY_SCALING.replace("present", "0"))
				&& Utils.exist(CPUHelper.CORE_VALUE))
			layout.addView(mCurFreqLayout);

		TextView mCurFreqTitle = new TextView(context);
		LayoutHelper.setTextTitle(mCurFreqTitle,
				context.getString(R.string.currentfreq), context);
		mCurFreqLayout.addView(mCurFreqTitle);

		mCurFreqTexts = new TextView[CPUHelper.getCoreCount()];
		for (int i = 0; i < CPUHelper.getCoreCount(); i += 2) {
			TextView mCurFreqText = new TextView(context);
			LayoutHelper.setCurFreqText(mCurFreqText, context);
			mCurFreqTexts[i] = mCurFreqText;
			mCurFreqLayout.addView(mCurFreqText);
		}
		setCurFreq();

		// Core Control
		LinearLayout mCoreControlLayout = new LinearLayout(context);
		mCoreControlLayout.setGravity(Gravity.CENTER);
		if (Utils.exist(CPUHelper.CORE_STAT.replace("present", "0")))
			layout.addView(mCoreControlLayout);

		mCoreControlBoxes = new CheckBox[CPUHelper.getCoreCount()];
		for (int i = 1; i < CPUHelper.getCoreCount(); i++) {
			CheckBox mCoreBox = new CheckBox(context);
			LayoutHelper.setCheckBox(
					mCoreBox,
					CPUHelper.getCoreOnline(i),
					context.getString(R.string.core) + " "
							+ String.valueOf(i + 1));
			mCoreBox.setOnCheckedChangeListener(OnCheckedChangeListener);
			mCoreControlBoxes[i] = mCoreBox;
			mCoreControlLayout.addView(mCoreBox);
		}

		// Create a layout for scaling
		mFreqScalingLayout = new LinearLayout(context);
		mFreqScalingLayout.setPaddingRelative(0,
				(int) (MainActivity.mHeight / 20), 0, 0);
		mFreqScalingLayout.setOrientation(LinearLayout.VERTICAL);
		if (Utils.exist(CPUHelper.AVAILABLE_GOVERNOR)
				|| Utils.exist(CPUHelper.MIN_SCREEN_ON)
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
			for (int i = CPUHelper.getAvailableFreq().length - 1; i >= 0; i--)
				mAvailableFreqList.add(String.valueOf(CPUHelper
						.getAvailableFreq()[i]));
		}

		// Max Cpu Scaling
		mMaxScalingText = new TextView(context);
		LayoutHelper.setTextTitle(mMaxScalingText,
				context.getString(R.string.maxfreq), context);
		if (Utils.exist(CPUHelper.MAX_FREQ))
			mFreqScalingLayout.addView(mMaxScalingText);
		mMaxScalingText.setOnClickListener(OnClickListener);

		int mMax = mAvailableFreqList.indexOf(String.valueOf(CPUHelper
				.getMaxFreq()));

		mMaxFreqScalingText = new TextView(context);
		LayoutHelper.setSeekBarText(
				mMaxFreqScalingText,
				String.valueOf(CPUHelper.getMaxFreq() / 1000)
						+ context.getString(R.string.mhz));
		if (Utils.exist(CPUHelper.MAX_FREQ))
			mFreqScalingLayout.addView(mMaxFreqScalingText);

		mMaxFreqScalingBar = new SeekBar(context);
		LayoutHelper.setSeekBar(mMaxFreqScalingBar,
				mAvailableFreqList.size() - 1, mMax, context);
		mMaxFreqScalingBar.setOnSeekBarChangeListener(OnSeekBarChangeListener);
		if (Utils.exist(CPUHelper.MAX_FREQ))
			mFreqScalingLayout.addView(mMaxFreqScalingBar);

		// Min Cpu Scaling
		mMinScalingText = new TextView(context);
		LayoutHelper.setTextTitle(mMinScalingText,
				context.getString(R.string.minfreq), context);
		if (Utils.exist(CPUHelper.MIN_FREQ))
			mFreqScalingLayout.addView(mMinScalingText);
		mMinScalingText.setOnClickListener(OnClickListener);

		int mMin = mAvailableFreqList.indexOf(String.valueOf(CPUHelper
				.getMinFreq()));

		mMinFreqScalingText = new TextView(context);
		LayoutHelper.setSeekBarText(
				mMinFreqScalingText,
				String.valueOf(CPUHelper.getMinFreq() / 1000)
						+ context.getString(R.string.mhz));
		if (Utils.exist(CPUHelper.MIN_FREQ))
			mFreqScalingLayout.addView(mMinFreqScalingText);

		mMinFreqScalingBar = new SeekBar(context);
		LayoutHelper.setSeekBar(mMinFreqScalingBar,
				mAvailableFreqList.size() - 1, mMin, context);
		mMinFreqScalingBar.setOnSeekBarChangeListener(OnSeekBarChangeListener);
		if (Utils.exist(CPUHelper.MIN_FREQ))
			mFreqScalingLayout.addView(mMinFreqScalingBar);

		// Max Screen Off Cpu Scaling
		mMaxScreenOffFreqText = new TextView(context);
		LayoutHelper.setTextTitle(mMaxScreenOffFreqText,
				context.getString(R.string.maxscreenofffreq), context);
		if (Utils.exist(CPUHelper.MAX_SCREEN_OFF))
			mFreqScalingLayout.addView(mMaxScreenOffFreqText);
		mMaxScreenOffFreqText.setOnClickListener(OnClickListener);

		int mMaxScreenOff = mAvailableFreqList.indexOf(String.valueOf(CPUHelper
				.getMaxScreenOffFreq()));

		mMaxScreenOffFreqScalingText = new TextView(context);
		LayoutHelper.setSeekBarText(mMaxScreenOffFreqScalingText,
				String.valueOf(CPUHelper.getMaxScreenOffFreq() / 1000)
						+ context.getString(R.string.mhz));
		if (Utils.exist(CPUHelper.MAX_SCREEN_OFF))
			mFreqScalingLayout.addView(mMaxScreenOffFreqScalingText);

		mMaxScreenOffFreqScalingBar = new SeekBar(context);
		LayoutHelper.setSeekBar(mMaxScreenOffFreqScalingBar,
				mAvailableFreqList.size() - 1, mMaxScreenOff, context);
		mMaxScreenOffFreqScalingBar
				.setOnSeekBarChangeListener(OnSeekBarChangeListener);
		if (Utils.exist(CPUHelper.MAX_SCREEN_OFF))
			mFreqScalingLayout.addView(mMaxScreenOffFreqScalingBar);

		// Min Screen On Cpu Scaling
		mMinScreenOnFreqText = new TextView(context);
		LayoutHelper.setTextTitle(mMinScreenOnFreqText,
				context.getString(R.string.minscreenonfreq), context);
		if (Utils.exist(CPUHelper.MIN_SCREEN_ON))
			mFreqScalingLayout.addView(mMinScreenOnFreqText);
		mMinScreenOnFreqText.setOnClickListener(OnClickListener);

		int mMinScreenOn = mAvailableFreqList.indexOf(String.valueOf(CPUHelper
				.getMinScreenOnFreq()));

		mMinScreenOnFreqScalingText = new TextView(context);
		LayoutHelper.setSeekBarText(
				mMinScreenOnFreqScalingText,
				String.valueOf(CPUHelper.getMinScreenOnFreq() / 1000)
						+ context.getString(R.string.mhz));
		if (Utils.exist(CPUHelper.MIN_SCREEN_ON))
			mFreqScalingLayout.addView(mMinScreenOnFreqScalingText);

		mMinScreenOnFreqScalingBar = new SeekBar(context);
		LayoutHelper.setSeekBar(mMinScreenOnFreqScalingBar,
				mAvailableFreqList.size() - 1, mMinScreenOn, context);
		mMinScreenOnFreqScalingBar
				.setOnSeekBarChangeListener(OnSeekBarChangeListener);
		if (Utils.exist(CPUHelper.MIN_SCREEN_ON))
			mFreqScalingLayout.addView(mMinScreenOnFreqScalingBar);

		// CPU Governor Layout
		LinearLayout mGovernorLayout = new LinearLayout(context);
		mGovernorLayout.setGravity(Gravity.CENTER);
		mGovernorLayout
				.setPadding(0, (int) (MainActivity.mHeight / 21.6), 0, 0);
		if (Utils.exist(CPUHelper.AVAILABLE_GOVERNOR))
			layout.addView(mGovernorLayout);

		// Governor
		mGovernorTitle = new TextView(context);
		LayoutHelper.setTextTitle(mGovernorTitle,
				context.getString(R.string.cpugovernor), context);
		mGovernorLayout.addView(mGovernorTitle);
		mGovernorTitle.setOnClickListener(OnClickListener);

		mAvailableGovernor = CPUHelper.getAvailableGovernor();
		mAvailableGovernorList = Arrays.asList(mAvailableGovernor);

		ArrayAdapter<String> adapterGovernor = new ArrayAdapter<String>(
				context, R.layout.spinner, mAvailableGovernor);
		adapterGovernor
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		mGovernorSpinner = new Spinner(context);
		LayoutHelper.setSpinner(mGovernorSpinner, adapterGovernor,
				mAvailableGovernorList.indexOf(CPUHelper.getCurGovernor()));
		mGovernorSpinner.setOnItemSelectedListener(OnItemSelectedListener);
		mGovernorLayout.addView(mGovernorSpinner);

		// IntelliPlug
		LinearLayout mIntelliPlugLayout = new LinearLayout(context);
		mIntelliPlugLayout.setGravity(Gravity.CENTER);
		mIntelliPlugLayout.setPadding(0, (int) (MainActivity.mHeight / 21.6),
				0, 0);
		if (Utils.exist(CPUHelper.INTELLIPLUG))
			layout.addView(mIntelliPlugLayout);

		mIntelliPlugBox = new CheckBox(context);
		LayoutHelper.setCheckBox(mIntelliPlugBox, CPUHelper.getIntelliPlug(),
				context.getString(R.string.intelliplug));
		mIntelliPlugBox.setOnCheckedChangeListener(OnCheckedChangeListener);
		mIntelliPlugLayout.addView(mIntelliPlugBox);

		mIntelliPlugEcoModeBox = new CheckBox(context);
		LayoutHelper.setCheckBox(mIntelliPlugEcoModeBox,
				CPUHelper.getIntelliPlugEcoMode(),
				context.getString(R.string.intelliplugecomode));
		mIntelliPlugEcoModeBox
				.setOnCheckedChangeListener(OnCheckedChangeListener);
		if (Utils.exist(CPUHelper.INTELLIPLUG))
			mIntelliPlugLayout.addView(mIntelliPlugEcoModeBox);
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
		for (int i = 0; i < CPUHelper.getCoreCount(); i += 2) {
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
		MainFragment.showButtons(true);
		MainFragment.CPUChange = true;
		if (seekBar.equals(mMaxFreqScalingBar)) {
			mMaxFreqScalingText
					.setText(String.valueOf(Integer.parseInt(mAvailableFreqList
							.get(progress)) / 1000) + "MHz");
			if (progress < mMinFreqScalingBar.getProgress())
				mMinFreqScalingBar.setProgress(progress);
			if (progress < mMinScreenOnFreqScalingBar.getProgress())
				mMinScreenOnFreqScalingBar.setProgress(progress);
		}
		if (seekBar.equals(mMinFreqScalingBar)) {
			mMinFreqScalingText
					.setText(String.valueOf(Integer.parseInt(mAvailableFreqList
							.get(progress)) / 1000) + "MHz");
			if (progress > mMaxFreqScalingBar.getProgress())
				mMaxFreqScalingBar.setProgress(progress);
			if (progress > mMaxScreenOffFreqScalingBar.getProgress())
				mMaxScreenOffFreqScalingBar.setProgress(progress);
		}
		if (seekBar.equals(mMaxScreenOffFreqScalingBar)) {
			mMaxScreenOffFreqScalingText
					.setText(String.valueOf(Integer.parseInt(mAvailableFreqList
							.get(progress)) / 1000) + "MHz");
			if (progress < mMinFreqScalingBar.getProgress())
				mMinFreqScalingBar.setProgress(progress);
		}
		if (seekBar.equals(mMinScreenOnFreqScalingBar)) {
			mMinScreenOnFreqScalingText
					.setText(String.valueOf(Integer.parseInt(mAvailableFreqList
							.get(progress)) / 1000) + "MHz");
			if (progress > mMaxFreqScalingBar.getProgress())
				mMaxFreqScalingBar.setProgress(progress);
		}
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		if (seekBar.equals(mMaxFreqScalingBar)
				|| (seekBar.equals(mMinFreqScalingBar))
				|| seekBar.equals(mMaxScreenOffFreqScalingBar)
				|| seekBar.equals(mMinScreenOnFreqScalingBar)) {
			Control.runCPUGeneric(mMaxFreqScalingText.getText().toString()
					.replace(getString(R.string.mhz), "000"),
					CPUHelper.MAX_FREQ);
			Control.runCPUGeneric(mMinFreqScalingText.getText().toString()
					.replace(getString(R.string.mhz), "000"),
					CPUHelper.MIN_FREQ);
			Control.runCPUGeneric(mMaxScreenOffFreqScalingText.getText()
					.toString().replace(getString(R.string.mhz), "000"),
					CPUHelper.MAX_SCREEN_OFF);
			Control.runCPUGeneric(mMinScreenOnFreqScalingText.getText()
					.toString().replace(getString(R.string.mhz), "000"),
					CPUHelper.MIN_SCREEN_ON);
		}
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		if (arg0.equals(mGovernorSpinner)) {
			if (arg2 != mAvailableGovernorList.indexOf(CPUHelper
					.getCurGovernor())) {
				MainFragment.showButtons(true);
				MainFragment.CPUChange = true;
				Control.runCPUGeneric(mAvailableGovernorList.get(arg2),
						CPUHelper.CUR_GOVERNOR);
			}
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
	}

	@Override
	public void onClick(View v) {
		if (v.equals(mMaxScalingText))
			InformationDialog.showInfo(mMaxScalingText.getText().toString(),
					context.getString(R.string.maxfreq_summary), context);
		else if (v.equals(mMinScalingText))
			InformationDialog.showInfo(mMinScalingText.getText().toString(),
					context.getString(R.string.minfreq_summary), context);
		else if (v.equals(mMaxScreenOffFreqText))
			InformationDialog.showInfo(mMaxScreenOffFreqText.getText()
					.toString(), context
					.getString(R.string.maxscreenofffreq_summary), context);
		else if (v.equals(mMinScreenOnFreqText))
			InformationDialog.showInfo(mMinScalingText.getText().toString(),
					context.getString(R.string.minscreenonfreq_summary),
					context);
		else if (v.equals(mGovernorTitle))
			InformationDialog.showInfo(mGovernorTitle.getText().toString(),
					context.getString(R.string.cpugovernor_summary), context);
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		MainFragment.showButtons(true);
		MainFragment.CPUChange = true;
		for (int i = 1; i < CPUHelper.getCoreCount(); i++)
			if (buttonView.equals(mCoreControlBoxes[i]))
				Control.runCPUGeneric(
						isChecked ? "1" : "0",
						CPUHelper.CORE_STAT.replace("present",
								String.valueOf(i)));

		if (buttonView.equals(mIntelliPlugBox))
			Control.runCPUGeneric(isChecked ? "1" : "0", CPUHelper.INTELLIPLUG);
		if (buttonView.equals(mIntelliPlugEcoModeBox))
			Control.runCPUGeneric(isChecked ? "1" : "0",
					CPUHelper.INTELLIPLUG_ECO_MODE);
	}
}
