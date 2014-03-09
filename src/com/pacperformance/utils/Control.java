package com.pacperformance.utils;

import com.pacperformance.fragments.CPUFragment;

public class Control {

	public static String MIN_CPU_FREQ = null;
	public static String MAX_CPU_FREQ = null;

	public static void setCPU() {
		setValue(MIN_CPU_FREQ, CPUHelper.MIN_FREQ);
		setValue(MAX_CPU_FREQ, CPUHelper.MAX_FREQ);

		CPUFragment.setLayout();
	}

	private static void setValue(String value, String path) {
		if (value != null)
			RootHelper.run("echo " + value + " > " + path);
	}
}
