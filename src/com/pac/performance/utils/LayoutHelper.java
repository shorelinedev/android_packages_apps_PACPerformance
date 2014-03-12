package com.pac.performance.utils;

import com.pac.performance.MainActivity;
import com.pac.performance.R;

import android.content.Context;
import android.graphics.Typeface;
import android.view.Gravity;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

public class LayoutHelper {

	public static void setEditText(EditText i, String text) {
		i.setGravity(Gravity.CENTER);
		i.setText(text);
	}

	public static void setSubTitle(TextView i, String text) {
		i.setGravity(Gravity.CENTER);
		i.setTextSize(18);
		i.setText(text);
	}

	public static void setCheckBox(CheckBox i, boolean checked, String text) {
		i.setText(text);
		i.setChecked(checked);
	}

	public static void setSpinner(Spinner i, ArrayAdapter<String> adapter,
			int selection) {
		i.setAdapter(adapter);
		i.setSelection(selection);
		i.setPadding(MainActivity.mWidth / 13, 0, MainActivity.mWidth / 13, 0);
	}

	public static void setSeekBarText(TextView i, String text) {
		i.setGravity(Gravity.CENTER);
		i.setText(text);
		i.setPadding(0, 15, 0, 0);
	}

	public static void setNormalSeekBar(SeekBar i, int max, int progress,
			Context context) {
		i.setMax(max);
		i.setProgress(progress);
		i.setProgressDrawable(context.getResources().getDrawable(
				R.drawable.seekbar_progress_bg));
		i.setThumb(context.getResources().getDrawable(
				R.drawable.seekbar_control));
	}

	public static void setSeekBar(SeekBar i, int max, int progress,
			Context context) {
		i.setMax(max);
		i.setProgress(progress);
		i.setPadding(MainActivity.mWidth / 13, 0, MainActivity.mWidth / 13, 0);
		i.setProgressDrawable(context.getResources().getDrawable(
				R.drawable.seekbar_progress_bg));
		i.setThumb(context.getResources().getDrawable(
				R.drawable.seekbar_control));
	}

	public static void setTextTitle(TextView i, String text, Context context) {
		i.setTextSize(16);
		i.setGravity(Gravity.CENTER);
		i.setTextColor(context.getResources().getColor(android.R.color.white));
		i.setTypeface(null, Typeface.BOLD);
		i.setText(text);
	}

	public static void setCurFreqText(TextView i, Context context) {
		i.setGravity(Gravity.CENTER);
		i.setTextSize(18);
	}
}
