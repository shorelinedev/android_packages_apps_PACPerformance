/*
 * Copyright (C) 2014 PAC-man ROM
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.pac.performance.helpers;

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

	public static void setCheckBox(CheckBox i, boolean checked, String text,
			Context context) {
		i.setText(text);
		i.setChecked(checked);
		i.setButtonDrawable(context.getResources().getDrawable(
				R.drawable.checkbox));
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
