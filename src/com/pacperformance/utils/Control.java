package com.pacperformance.utils;

import android.content.Context;

import com.pacperformance.R;
import com.pacperformance.fragments.CPUFragment;

public class Control {

	public static String MIN_CPU_SCREEN_ON_FREQ = "";
	public static String MAX_CPU_SCREEN_OFF_FREQ = "";
	public static String MIN_CPU_FREQ = "";
	public static String MAX_CPU_FREQ = "";

	public static void setCPU(Context c) {
		setValue(MAX_CPU_FREQ, CPUHelper.MAX_FREQ);
		setValue(MIN_CPU_FREQ, CPUHelper.MIN_FREQ);
		setValue(MAX_CPU_SCREEN_OFF_FREQ, CPUHelper.MAX_SCREEN_OFF);
		setValue(MIN_CPU_SCREEN_ON_FREQ, CPUHelper.MIN_SCREEN_ON);

		CPUFragment.setLayout();
		reset(c);
	}

	private static void reset(Context context) {
		MAX_CPU_FREQ = "";
		MIN_CPU_FREQ = "";
		MAX_CPU_SCREEN_OFF_FREQ = "";
		MIN_CPU_SCREEN_ON_FREQ = "";

		Utils.toast(context.getString(R.string.applysuccessfully), context);
	}

	private static void setValue(String value, String path) {
		if (!value.isEmpty()) {
			RootHelper.run("echo " + value + " > " + path);
		}
	}
}
