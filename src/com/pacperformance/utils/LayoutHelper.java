package com.pacperformance.utils;

import com.pacperformance.MainActivity;

import android.content.Context;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.Surface;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

public class LayoutHelper {

	public static void setSpinner(Spinner i, ArrayAdapter<String> adapter,
			int selection) {
		i.setAdapter(adapter);
		i.setSelection(selection);
		i.setPadding(MainActivity.mWidth / 13, 0, MainActivity.mWidth / 13, 0);
	}

	public static void setSeekBarText(TextView i, String text) {
		i.setGravity(Gravity.CENTER);
		i.setText(text);
	}

	public static void setSeekBar(SeekBar i, int max, int progress) {
		i.setMax(max);
		i.setProgress(progress);
		i.setPadding(MainActivity.mWidth / 13, 0, MainActivity.mWidth / 13, 0);
	}

	@SuppressWarnings("deprecation")
	public static void setTextTitle(TextView i, String text, Context context) {
		final int rotation = ((WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay()
				.getOrientation();
		switch (rotation) {
		case Surface.ROTATION_0:
		case Surface.ROTATION_180:
			i.setTextSize((int) (MainActivity.mWidth / 42.35));
			break;
		case Surface.ROTATION_90:
		case Surface.ROTATION_270:
			i.setTextSize((int) (MainActivity.mHeight / 42.35));
			break;
		}
		i.setGravity(Gravity.CENTER);
		i.setTextColor(context.getResources().getColor(android.R.color.white));
		i.setTypeface(null, Typeface.BOLD);
		i.setText(text);
	}

	@SuppressWarnings("deprecation")
	public static void setCurFreqText(TextView i, Context context) {
		i.setGravity(Gravity.CENTER);
		final int rotation = ((WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay()
				.getOrientation();
		switch (rotation) {
		case Surface.ROTATION_0:
		case Surface.ROTATION_180:
			i.setTextSize((int) (MainActivity.mWidth / 36));
			break;
		case Surface.ROTATION_90:
		case Surface.ROTATION_270:
			i.setTextSize((int) (MainActivity.mHeight / 36));
			break;
		}
	}
}
