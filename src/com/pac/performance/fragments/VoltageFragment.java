package com.pac.performance.fragments;

import java.util.ArrayList;
import java.util.List;

import com.pac.performance.MainActivity;
import com.pac.performance.R;
import com.pac.performance.helpers.LayoutHelper;
import com.pac.performance.helpers.VoltageHelper;
import com.pac.performance.utils.Control;
import com.pac.performance.utils.InformationDialog;
import com.pac.performance.utils.Utils;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class VoltageFragment extends Fragment implements OnClickListener,
		OnSeekBarChangeListener {

	private static Context context;

	public static LinearLayout layout = null;

	private static OnClickListener OnClickListener;
	private static OnSeekBarChangeListener OnSeekBarChangeListener;

	private static Integer[] mVoltagesMV;

	private static TextView mVoltageText;

	private static SeekBar[] mVoltageBars;
	private static TextView[] mVoltageTexts;
	private static Button[] mVoltMinusbuttons;
	private static Button[] mVoltPlusButtons;

	private static Integer[] mFauxVoltagesMV;

	private static TextView mFauxVoltageText;

	private static SeekBar[] mFauxVoltageBars;
	private static TextView[] mFauxVoltageTexts;
	private static Button[] mFauxVoltMinusbuttons;
	private static Button[] mFauxVoltPlusButtons;

	private static List<String> mVoltageList = new ArrayList<String>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		context = getActivity();
		View rootView = inflater.inflate(R.layout.generic, container, false);
		layout = (LinearLayout) rootView.findViewById(R.id.layout);

		OnClickListener = this;
		OnSeekBarChangeListener = this;

		setLayout();
		return rootView;
	}

	public static void setLayout() {
		layout.removeAllViews();

		// Generic Voltage Control
		mVoltagesMV = VoltageHelper.getVoltages();

		mVoltageText = new TextView(context);
		LayoutHelper.setTextTitle(mVoltageText,
				context.getString(R.string.voltagecontrol), context);
		mVoltageText.setPadding(0, (int) (MainActivity.mHeight / 25), 0, 15);
		mVoltageText.setOnClickListener(OnClickListener);
		if (Utils.exist(VoltageHelper.CPU_VOLTAGE))
			layout.addView(mVoltageText);

		mVoltageBars = new SeekBar[mVoltagesMV.length];
		mVoltageTexts = new TextView[mVoltagesMV.length];
		mVoltMinusbuttons = new Button[mVoltagesMV.length];
		mVoltPlusButtons = new Button[mVoltagesMV.length];
		for (int i = 0; i < mVoltagesMV.length; i++) {

			LinearLayout mVoltageLayout = new LinearLayout(context);
			mVoltageLayout.setOrientation(LinearLayout.VERTICAL);
			if (Utils.exist(VoltageHelper.CPU_VOLTAGE))
				layout.addView(mVoltageLayout);

			TextView mVoltageFreq = new TextView(context);
			LayoutHelper.setSubTitle(mVoltageFreq,
					String.valueOf(VoltageHelper.getFreqVoltages()[i])
							+ context.getString(R.string.mhz));
			mVoltageLayout.addView(mVoltageFreq);

			TextView mVoltageText = new TextView(context);
			LayoutHelper.setSeekBarText(
					mVoltageText,
					String.valueOf(mVoltagesMV[i])
							+ context.getString(R.string.mv));
			mVoltageTexts[i] = mVoltageText;
			mVoltageLayout.addView(mVoltageText);

			LinearLayout mVoltageBarLayout = new LinearLayout(context);
			mVoltageBarLayout.setGravity(Gravity.CENTER);
			mVoltageLayout.addView(mVoltageBarLayout);

			LayoutParams lp = new LinearLayout.LayoutParams(0,
					LinearLayout.LayoutParams.WRAP_CONTENT, 1f);

			Button mVoltMinusbutton = new Button(context);
			mVoltMinusbutton.setText(context.getString(R.string.minus));
			mVoltMinusbuttons[i] = mVoltMinusbutton;
			mVoltMinusbutton.setOnClickListener(OnClickListener);
			mVoltageBarLayout.addView(mVoltMinusbutton);

			SeekBar mVoltageBar = new SeekBar(context);
			LayoutHelper.setNormalSeekBar(mVoltageBar, 180,
					(mVoltagesMV[i] - 600) / 5, context);
			mVoltageBar.setLayoutParams(lp);
			mVoltageBar.setOnSeekBarChangeListener(OnSeekBarChangeListener);
			mVoltageBars[i] = mVoltageBar;
			mVoltageBarLayout.addView(mVoltageBar);

			Button mVoltPlusButton = new Button(context);
			mVoltPlusButton.setText(context.getString(R.string.plus));
			mVoltPlusButtons[i] = mVoltPlusButton;
			mVoltPlusButton.setOnClickListener(OnClickListener);
			mVoltageBarLayout.addView(mVoltPlusButton);
		}

		// Faux Voltage Control
		mFauxVoltagesMV = VoltageHelper.getFauxVoltages();

		mFauxVoltageText = new TextView(context);
		LayoutHelper.setTextTitle(mFauxVoltageText,
				context.getString(R.string.fauxvoltagecontrol), context);
		mFauxVoltageText
				.setPadding(0, (int) (MainActivity.mHeight / 25), 0, 15);
		mFauxVoltageText.setOnClickListener(OnClickListener);
		if (Utils.exist(VoltageHelper.FAUX_VOLTAGE))
			layout.addView(mFauxVoltageText);

		mFauxVoltageBars = new SeekBar[mFauxVoltagesMV.length];
		mFauxVoltageTexts = new TextView[mFauxVoltagesMV.length];
		mFauxVoltMinusbuttons = new Button[mFauxVoltagesMV.length];
		mFauxVoltPlusButtons = new Button[mFauxVoltagesMV.length];

		for (int i = 0; i < mFauxVoltagesMV.length; i++) {

			LinearLayout mFauxVoltageLayout = new LinearLayout(context);
			mFauxVoltageLayout.setOrientation(LinearLayout.VERTICAL);
			if (Utils.exist(VoltageHelper.FAUX_VOLTAGE))
				layout.addView(mFauxVoltageLayout);

			TextView mFauxVoltageFreq = new TextView(context);
			LayoutHelper.setSubTitle(mFauxVoltageFreq,
					String.valueOf(VoltageHelper.getFauxFreqVoltages()[i])
							+ context.getString(R.string.mhz));
			mFauxVoltageLayout.addView(mFauxVoltageFreq);

			TextView mFauxVoltageText = new TextView(context);
			LayoutHelper.setSeekBarText(
					mFauxVoltageText,
					String.valueOf(mFauxVoltagesMV[i])
							+ context.getString(R.string.mv));
			mFauxVoltageTexts[i] = mFauxVoltageText;
			mFauxVoltageLayout.addView(mFauxVoltageText);

			LinearLayout mFauxVoltageBarLayout = new LinearLayout(context);
			mFauxVoltageLayout.addView(mFauxVoltageBarLayout);

			LayoutParams lp = new LinearLayout.LayoutParams(0,
					LinearLayout.LayoutParams.WRAP_CONTENT, 1f);

			Button mFauxVoltMinusbutton = new Button(context);
			mFauxVoltMinusbutton.setText(context.getString(R.string.minus));
			mFauxVoltMinusbuttons[i] = mFauxVoltMinusbutton;
			mFauxVoltMinusbutton.setOnClickListener(OnClickListener);
			mFauxVoltageBarLayout.addView(mFauxVoltMinusbutton);

			SeekBar mFauxVoltageBar = new SeekBar(context);
			LayoutHelper.setNormalSeekBar(mFauxVoltageBar, 180,
					(mFauxVoltagesMV[i] - 600) / 5, context);
			mFauxVoltageBar.setLayoutParams(lp);
			mFauxVoltageBar.setOnSeekBarChangeListener(OnSeekBarChangeListener);
			mFauxVoltageBars[i] = mFauxVoltageBar;
			mFauxVoltageBarLayout.addView(mFauxVoltageBar);

			Button mFauxVoltPlusButton = new Button(context);
			mFauxVoltPlusButton.setText(context.getString(R.string.plus));
			mFauxVoltPlusButtons[i] = mFauxVoltPlusButton;
			mFauxVoltPlusButton.setOnClickListener(OnClickListener);
			mFauxVoltageBarLayout.addView(mFauxVoltPlusButton);
		}
	}

	@Override
	public void onClick(View v) {
		if (v.equals(mVoltageText) || v.equals(mFauxVoltageText))
			InformationDialog.showInfo(v.equals(mVoltageText) ? mVoltageText
					.getText().toString() : mFauxVoltageText.getText()
					.toString(), context
					.getString(R.string.voltagecontrol_summary), context);
		for (int i = 0; i < mVoltagesMV.length; i++) {
			if (v.equals(mVoltMinusbuttons[i])) {
				mVoltageBars[i].setProgress(mVoltageBars[i].getProgress() - 1);
				saveVoltages();
			}
			if (v.equals(mVoltPlusButtons[i])) {
				mVoltageBars[i].setProgress(mVoltageBars[i].getProgress() + 1);
				saveVoltages();
			}
		}
		for (int i = 0; i < mFauxVoltagesMV.length; i++) {
			if (v.equals(mFauxVoltMinusbuttons[i])) {
				mFauxVoltageBars[i].setProgress(mFauxVoltageBars[i]
						.getProgress() - 1);
				saveFauxVoltage(String.valueOf(VoltageHelper
						.getFauxFreqVoltages()[i])
						+ "000 "
						+ mFauxVoltageTexts[i].getText().toString()
								.replace(context.getString(R.string.mv), "000"));
			}
			if (v.equals(mFauxVoltPlusButtons[i])) {
				mFauxVoltageBars[i].setProgress(mFauxVoltageBars[i]
						.getProgress() + 1);
				saveFauxVoltage(String.valueOf(VoltageHelper
						.getFauxFreqVoltages()[i])
						+ "000 "
						+ mFauxVoltageTexts[i].getText().toString()
								.replace(context.getString(R.string.mv), "000"));
			}
		}
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		MainFragment.showButtons(true);
		MainFragment.VoltageChange = true;

		mVoltageList.clear();
		for (int i = 0; i < mVoltagesMV.length; i++) {
			if (seekBar.equals(mVoltageBars[i]))
				mVoltageTexts[i].setText(String.valueOf(progress * 5 + 600)
						+ context.getString(R.string.mv));
			mVoltageList.add(mVoltageTexts[i].getText().toString()
					.replace(context.getString(R.string.mv), ""));
		}

		for (int i = 0; i < mFauxVoltagesMV.length; i++)
			if (seekBar.equals(mFauxVoltageBars[i]))
				mFauxVoltageTexts[i].setText(String.valueOf(progress * 5 + 600)
						+ context.getString(R.string.mv));
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		for (int i = 0; i < mFauxVoltagesMV.length; i++)
			if (seekBar.equals(mFauxVoltageBars[i]))
				saveFauxVoltage(String.valueOf(VoltageHelper
						.getFauxFreqVoltages()[i])
						+ "000 "
						+ mFauxVoltageTexts[i].getText().toString()
								.replace(context.getString(R.string.mv), "000"));

		if (Utils.exist(VoltageHelper.CPU_VOLTAGE))
			saveVoltages();
	}

	private static void saveVoltages() {
		Control.runVoltageGeneric(Utils.listSplitline(mVoltageList),
				VoltageHelper.CPU_VOLTAGE);
	}

	private static void saveFauxVoltage(String value) {
		Control.runVoltageGeneric(value, VoltageHelper.FAUX_VOLTAGE);
	}

}
