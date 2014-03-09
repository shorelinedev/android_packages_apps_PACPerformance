package com.pacperformance.utils;

import java.io.IOException;

public class CPUHelper {

	public static String FREQUENCY_SCALING = "/sys/devices/system/cpu/cpupresent/cpufreq/scaling_cur_freq";
	public static String CORE_VALUE = "/sys/devices/system/cpu/present";

	public static int getFreqScaling(int core) {
		try {
			return Utils.exist(FREQUENCY_SCALING.replace("present",
					String.valueOf(core))) ? Integer.valueOf(Utils
					.readLine(FREQUENCY_SCALING.replace("present",
							String.valueOf(core)))) : 0;

		} catch (IOException e) {
		}
		return 0;
	}

	public static int getCoreValue() {
		try {
			return Integer.parseInt(Utils.readLine(CORE_VALUE).split("-")[1]) + 1;
		} catch (IOException e) {
		}
		return 0;
	}
}
