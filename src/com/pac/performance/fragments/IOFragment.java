package com.pac.performance.fragments;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.pac.performance.MainActivity;
import com.pac.performance.R;
import com.pac.performance.helpers.IOHelper;
import com.pac.performance.helpers.LayoutHelper;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.LinearLayout.LayoutParams;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;
import android.widget.TextView;

public class IOFragment extends Fragment implements OnClickListener,
		OnItemSelectedListener, OnSeekBarChangeListener {

	private static Context context;

	public static LinearLayout layout = null;

	private static OnClickListener OnClickListener;
	private static OnItemSelectedListener OnItemSelectedListener;
	private static OnSeekBarChangeListener OnSeekBarChangeListener;

	private static TextView mInternalSchedulerTitle;
	private static String[] mAvailableInternalSchedulers;
	private static List<String> mAvailableInternalSchedulersList = new ArrayList<String>();
	private static Spinner mInternalSchedulerSpinner;

	private static TextView mExternalSchedulerTitle;
	private static String[] mAvailableExternalSchedulers;
	private static List<String> mAvailableExternalSchedulersList = new ArrayList<String>();
	private static Spinner mExternalSchedulerSpinner;

	private static TextView mInternalReadTitle;
	private static Button mInternalReadMinus;
	private static SeekBar mInternalReadBar;
	private static Button mInternalReadPlus;
	private static TextView mInternalReadText;

	private static TextView mExternalReadTitle;
	private static Button mExternalReadMinus;
	private static SeekBar mExternalReadBar;
	private static Button mExternalReadPlus;
	private static TextView mExternalReadText;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		context = getActivity();
		View rootView = inflater.inflate(R.layout.generic, container, false);
		layout = (LinearLayout) rootView.findViewById(R.id.layout);

		OnClickListener = this;
		OnItemSelectedListener = this;
		OnSeekBarChangeListener = this;

		setLayout();
		return rootView;
	}

	public static void setLayout() {
		layout.removeAllViews();

		// Internal storage scheduler
		LinearLayout mInternalSchedulerLayout = new LinearLayout(context);
		mInternalSchedulerLayout.setPadding(0,
				(int) (MainActivity.mHeight / 25), 0, 0);
		mInternalSchedulerLayout.setGravity(Gravity.CENTER);
		if (Utils.exist(IOHelper.INTERNAL_SCHEDULER))
			layout.addView(mInternalSchedulerLayout);

		mInternalSchedulerTitle = new TextView(context);
		LayoutHelper.setTextTitle(mInternalSchedulerTitle,
				context.getString(R.string.internalstoragescheduler), context);
		mInternalSchedulerTitle.setOnClickListener(OnClickListener);
		mInternalSchedulerLayout.addView(mInternalSchedulerTitle);

		mAvailableInternalSchedulers = IOHelper.getInternalSchedulers();
		mAvailableInternalSchedulersList = Arrays
				.asList(mAvailableInternalSchedulers);

		ArrayAdapter<String> adapterInternalScheduler = new ArrayAdapter<String>(
				context, R.layout.spinner, mAvailableInternalSchedulers);
		adapterInternalScheduler
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		mInternalSchedulerSpinner = new Spinner(context);
		LayoutHelper.setSpinner(mInternalSchedulerSpinner,
				adapterInternalScheduler, mAvailableInternalSchedulersList
						.indexOf(IOHelper.getCurInternalScheduler()));
		mInternalSchedulerSpinner
				.setOnItemSelectedListener(OnItemSelectedListener);
		mInternalSchedulerLayout.addView(mInternalSchedulerSpinner);

		// External storage scheduler
		LinearLayout mExternalSchedulerLayout = new LinearLayout(context);
		mExternalSchedulerLayout.setPadding(0,
				(int) (MainActivity.mHeight / 25), 0, 0);
		mExternalSchedulerLayout.setGravity(Gravity.CENTER);
		if (Utils.exist(IOHelper.EXTERNAL_SCHEDULER))
			layout.addView(mExternalSchedulerLayout);

		mExternalSchedulerTitle = new TextView(context);
		LayoutHelper.setTextTitle(mExternalSchedulerTitle,
				context.getString(R.string.externalstoragescheduler), context);
		mExternalSchedulerTitle.setOnClickListener(OnClickListener);
		mExternalSchedulerLayout.addView(mExternalSchedulerTitle);

		mAvailableExternalSchedulers = IOHelper.getExternalSchedulers();
		mAvailableExternalSchedulersList = Arrays
				.asList(mAvailableExternalSchedulers);

		ArrayAdapter<String> adapterExternalScheduler = new ArrayAdapter<String>(
				context, R.layout.spinner, mAvailableExternalSchedulers);
		adapterExternalScheduler
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		mExternalSchedulerSpinner = new Spinner(context);
		LayoutHelper.setSpinner(mExternalSchedulerSpinner,
				adapterExternalScheduler, mAvailableExternalSchedulersList
						.indexOf(IOHelper.getCurExternalScheduler()));
		mExternalSchedulerSpinner
				.setOnItemSelectedListener(OnItemSelectedListener);
		mExternalSchedulerLayout.addView(mExternalSchedulerSpinner);

		// Internal storage read-ahead
		mInternalReadTitle = new TextView(context);
		LayoutHelper.setTextTitle(mInternalReadTitle,
				context.getString(R.string.internalstorageread), context);
		mInternalReadTitle.setPadding(0, (int) (MainActivity.mHeight / 25), 0,
				0);
		mInternalReadTitle.setOnClickListener(OnClickListener);
		if (Utils.exist(IOHelper.INTERNAL_READ))
			layout.addView(mInternalReadTitle);

		mInternalReadText = new TextView(context);
		LayoutHelper.setSeekBarText(
				mInternalReadText,
				String.valueOf(IOHelper.getInternalRead())
						+ context.getString(R.string.kb));
		if (Utils.exist(IOHelper.INTERNAL_READ))
			layout.addView(mInternalReadText);

		LinearLayout mInternalReadLayout = new LinearLayout(context);
		mInternalReadLayout.setGravity(Gravity.CENTER);
		if (Utils.exist(IOHelper.INTERNAL_READ))
			layout.addView(mInternalReadLayout);

		LayoutParams lp = new LinearLayout.LayoutParams(0,
				LinearLayout.LayoutParams.WRAP_CONTENT, 1f);

		mInternalReadMinus = new Button(context);
		mInternalReadMinus.setText(context.getString(R.string.minus));
		mInternalReadMinus.setOnClickListener(OnClickListener);
		mInternalReadLayout.addView(mInternalReadMinus);

		mInternalReadBar = new SeekBar(context);
		LayoutHelper.setNormalSeekBar(mInternalReadBar, 31,
				(IOHelper.getInternalRead() - 128) / 128, context);
		mInternalReadBar.setLayoutParams(lp);
		mInternalReadBar.setOnSeekBarChangeListener(OnSeekBarChangeListener);
		mInternalReadLayout.addView(mInternalReadBar);

		mInternalReadPlus = new Button(context);
		mInternalReadPlus.setText(context.getString(R.string.plus));
		mInternalReadPlus.setOnClickListener(OnClickListener);
		mInternalReadLayout.addView(mInternalReadPlus);

		// External storage read-ahead
		mExternalReadTitle = new TextView(context);
		LayoutHelper.setTextTitle(mExternalReadTitle,
				context.getString(R.string.externalstorageread), context);
		mExternalReadTitle.setPadding(0, (int) (MainActivity.mHeight / 25), 0,
				0);
		mExternalReadTitle.setOnClickListener(OnClickListener);
		if (Utils.exist(IOHelper.EXTERNAL_READ))
			layout.addView(mExternalReadTitle);

		mExternalReadText = new TextView(context);
		LayoutHelper.setSeekBarText(
				mExternalReadText,
				String.valueOf(IOHelper.getExternalRead())
						+ context.getString(R.string.kb));
		if (Utils.exist(IOHelper.EXTERNAL_READ))
			layout.addView(mExternalReadText);

		LinearLayout mExternalReadLayout = new LinearLayout(context);
		mExternalReadLayout.setGravity(Gravity.CENTER);
		if (Utils.exist(IOHelper.EXTERNAL_READ))
			layout.addView(mExternalReadLayout);

		mExternalReadMinus = new Button(context);
		mExternalReadMinus.setText(context.getString(R.string.minus));
		mExternalReadMinus.setOnClickListener(OnClickListener);
		mExternalReadLayout.addView(mExternalReadMinus);

		mExternalReadBar = new SeekBar(context);
		LayoutHelper.setNormalSeekBar(mExternalReadBar, 31,
				(IOHelper.getExternalRead() - 128) / 128, context);
		mExternalReadBar.setLayoutParams(lp);
		mExternalReadBar.setOnSeekBarChangeListener(OnSeekBarChangeListener);
		mExternalReadLayout.addView(mExternalReadBar);

		mExternalReadPlus = new Button(context);
		mExternalReadPlus.setText(context.getString(R.string.plus));
		mExternalReadPlus.setOnClickListener(OnClickListener);
		mExternalReadLayout.addView(mExternalReadPlus);
	}

	@Override
	public void onClick(View v) {
		if (v.equals(mInternalSchedulerTitle))
			InformationDialog.showInfo(mInternalSchedulerTitle.getText()
					.toString(), context
					.getString(R.string.storagescheduler_summary), context);
		if (v.equals(mExternalSchedulerTitle))
			InformationDialog.showInfo(mExternalSchedulerTitle.getText()
					.toString(), context
					.getString(R.string.storagescheduler_summary), context);
		if (v.equals(mInternalReadTitle))
			InformationDialog.showInfo(mInternalReadTitle.getText().toString(),
					context.getString(R.string.internalstorageread_summary),
					context);
		if (v.equals(mInternalReadMinus))
			mInternalReadBar.setProgress(mInternalReadBar.getProgress() - 1);
		if (v.equals(mInternalReadPlus))
			mInternalReadBar.setProgress(mInternalReadBar.getProgress() + 1);
		if (v.equals(mExternalReadTitle))
			InformationDialog.showInfo(mExternalReadTitle.getText().toString(),
					context.getString(R.string.externalstorageread_summary),
					context);
		if (v.equals(mExternalReadMinus))
			mExternalReadBar.setProgress(mExternalReadBar.getProgress() - 1);
		if (v.equals(mExternalReadPlus))
			mExternalReadBar.setProgress(mExternalReadBar.getProgress() + 1);
		saveReadahead(v.equals(mExternalReadMinus)
				|| v.equals(mExternalReadPlus) ? mExternalReadBar
				: mInternalReadBar);
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		if (arg0.equals(mInternalSchedulerSpinner)) {
			if (arg2 != mAvailableInternalSchedulersList.indexOf(IOHelper
					.getCurInternalScheduler())) {
				MainFragment.showButtons(true);
				MainFragment.IOChange = true;
				Control.runIOGeneric(
						mAvailableInternalSchedulersList.get(arg2),
						IOHelper.INTERNAL_SCHEDULER);
			}
		}
		if (arg0.equals(mExternalSchedulerSpinner)) {
			if (arg2 != mAvailableExternalSchedulersList.indexOf(IOHelper
					.getCurExternalScheduler())) {
				MainFragment.showButtons(true);
				MainFragment.IOChange = true;
				Control.runIOGeneric(
						mAvailableExternalSchedulersList.get(arg2),
						IOHelper.EXTERNAL_SCHEDULER);
			}
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		MainFragment.IOChange = true;
		MainFragment.showButtons(true);
		if (seekBar.equals(mInternalReadBar))
			mInternalReadText.setText(String.valueOf(progress * 128 + 128)
					+ context.getString(R.string.kb));
		if (seekBar.equals(mExternalReadBar))
			mExternalReadText.setText(String.valueOf(progress * 128 + 128)
					+ context.getString(R.string.kb));
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		saveReadahead(seekBar);
	}

	private static void saveReadahead(SeekBar seekBar) {
		if (seekBar.equals(mInternalReadBar))
			Control.runIOGeneric(mInternalReadText.getText().toString()
					.replace(context.getString(R.string.kb), ""),
					IOHelper.INTERNAL_READ);
		if (seekBar.equals(mExternalReadBar))
			Control.runIOGeneric(mExternalReadText.getText().toString()
					.replace(context.getString(R.string.kb), ""),
					IOHelper.EXTERNAL_READ);
	}
}
