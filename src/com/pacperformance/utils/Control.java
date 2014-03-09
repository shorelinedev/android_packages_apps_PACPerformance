package com.pacperformance.utils;

public class Control {

	public static String MAX_CPU_FREQ = null;

	public static void setCPU() {
		setValue(MAX_CPU_FREQ, CPUHelper.MAX_FREQ);
	}

	private static void setValue(String value, String path) {
		if (value != null)
			RootHelper.run("echo " + value + " > " + path);
	}
}
