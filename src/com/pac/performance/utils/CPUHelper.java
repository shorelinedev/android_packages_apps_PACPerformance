package com.pac.performance.utils;

import java.io.IOException;

public class CPUHelper {

	public static String CPU_VOLTAGE = "/sys/devices/system/cpu/cpu0/cpufreq/scaling_governor";
	public static final String CUR_GOVERNOR = "/sys/devices/system/cpu/cpu0/cpufreq/scaling_governor";
	public static final String AVAILABLE_GOVERNOR = "/sys/devices/system/cpu/cpu0/cpufreq/scaling_available_governors";
	public static final String MIN_SCREEN_ON = "/sys/devices/system/cpu/cpu0/cpufreq/screen_on_min_freq";
	public static final String MAX_SCREEN_OFF = "/sys/devices/system/cpu/cpu0/cpufreq/screen_off_max_freq";
	public static final String MIN_FREQ = "/sys/devices/system/cpu/cpu0/cpufreq/scaling_min_freq";
	public static final String MAX_FREQ = "/sys/devices/system/cpu/cpu0/cpufreq/scaling_max_freq";
	public static final String AVAILABLE_FREQ = "/sys/devices/system/cpu/cpu0/cpufreq/stats/time_in_state";
	public static final String FREQUENCY_SCALING = "/sys/devices/system/cpu/cpupresent/cpufreq/scaling_cur_freq";
	public static final String CORE_VALUE = "/sys/devices/system/cpu/present";

	public static String getCurGovernor() {
		if (Utils.exist(CUR_GOVERNOR))
			try {
				return Utils.readLine(CUR_GOVERNOR);
			} catch (NumberFormatException e) {
			} catch (IOException e) {
			}
		return "";
	}

	public static String[] getAvailableGovernor() {
		if (Utils.exist(AVAILABLE_GOVERNOR))
			try {
				return Utils.readLine(AVAILABLE_GOVERNOR).split(" ");
			} catch (IOException e) {
			}
		return new String[] { "" };
	}

	public static int getMinScreenOnFreq() {
		if (Utils.exist(MIN_SCREEN_ON))
			try {
				return Integer.valueOf(Utils.readLine(MIN_SCREEN_ON));
			} catch (NumberFormatException e) {
			} catch (IOException e) {
			}
		return 0;
	}

	public static int getMaxScreenOffFreq() {
		if (Utils.exist(MAX_SCREEN_OFF))
			try {
				return Integer.valueOf(Utils.readLine(MAX_SCREEN_OFF));
			} catch (NumberFormatException e) {
			} catch (IOException e) {
			}
		return 0;
	}

	public static int getMinFreq() {
		if (Utils.exist(MIN_FREQ))
			try {
				return Integer.valueOf(Utils.readLine(MIN_FREQ));
			} catch (NumberFormatException e) {
			} catch (IOException e) {
			}
		return 0;
	}

	public static int getMaxFreq() {
		if (Utils.exist(MAX_FREQ))
			try {
				return Integer.parseInt(Utils.readLine(MAX_FREQ));
			} catch (NumberFormatException e) {
			} catch (IOException e) {
			}
		return 0;
	}

	public static Integer[] getAvailableFreq() {
		if (Utils.exist(AVAILABLE_FREQ))
			try {
				String[] value = Utils.readBlock(AVAILABLE_FREQ).split(
						"\\r?\\n");
				Integer[] freqs = new Integer[value.length];
				for (int i = 0; i < value.length; i++)
					freqs[i] = Integer.valueOf(value[i].split(" ")[0]);
				return freqs;
			} catch (IOException e) {
			}
		return new Integer[] { 0 };
	}

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
		if (Utils.exist(CORE_VALUE))
			try {
				return Integer
						.parseInt(Utils.readLine(CORE_VALUE).split("-")[1]) + 1;
			} catch (IOException e) {
			}
		return 0;
	}
}
