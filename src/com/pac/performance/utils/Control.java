package com.pac.performance.utils;

import android.content.Context;

import com.pac.performance.R;
import com.pac.performance.fragments.CPUFragment;

public class Control {

	public static String GOVERNOR = "";
	public static String MIN_CPU_SCREEN_ON_FREQ = "";
	public static String MAX_CPU_SCREEN_OFF_FREQ = "";
	public static String MIN_CPU_FREQ = "";
	public static String MAX_CPU_FREQ = "";

	private static Context context;

	public static void setCPU(Context c) {
		context = c;
		setValue(MAX_CPU_FREQ, CPUHelper.MAX_FREQ);
		setValue(MIN_CPU_FREQ, CPUHelper.MIN_FREQ);
		setValue(MAX_CPU_SCREEN_OFF_FREQ, CPUHelper.MAX_SCREEN_OFF);
		setValue(MIN_CPU_SCREEN_ON_FREQ, CPUHelper.MIN_SCREEN_ON);
		setValue(GOVERNOR, CPUHelper.CUR_GOVERNOR);

		CPUFragment.setLayout();
		reset();
	}

	private static void reset() {
		MAX_CPU_FREQ = "";
		MIN_CPU_FREQ = "";
		MAX_CPU_SCREEN_OFF_FREQ = "";
		MIN_CPU_SCREEN_ON_FREQ = "";

		Utils.toast(context.getString(R.string.applysuccessfully), context);
	}

	private static void setValue(String value, String path) {
		if (!value.isEmpty()) {
			RootHelper.run("echo " + value + " > " + path);
			Utils.saveString(path, value, context);
		}
	}
}
