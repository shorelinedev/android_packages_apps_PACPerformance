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

import java.io.IOException;

import com.pac.performance.utils.Utils;

public class VoltageHelper {

	public static final String FAUX_VOLTAGE = "/sys/devices/system/cpu/cpufreq/vdd_table/vdd_levels";
	public static final String CPU_VOLTAGE = "/sys/devices/system/cpu/cpu0/cpufreq/UV_mV_table";

	public static Integer[] getFauxFreqVoltages() {
		if (Utils.exist(FAUX_VOLTAGE))
			try {
				String[] value = Utils.readBlock(FAUX_VOLTAGE).replace(" ", "")
						.split("\\r?\\n");
				Integer[] freq = new Integer[value.length];
				for (int i = 0; i < value.length; i++)
					freq[i] = Integer.parseInt(value[i].split(":")[0]) / 1000;
				return freq;
			} catch (IOException e) {
			}
		return new Integer[] { 0 };
	}

	public static Integer[] getFauxVoltages() {
		if (Utils.exist(FAUX_VOLTAGE))
			try {
				String[] value = Utils.readBlock(FAUX_VOLTAGE).replace(" ", "")
						.split("\\r?\\n");
				Integer[] voltage = new Integer[value.length];
				for (int i = 0; i < value.length; i++)
					voltage[i] = Integer.parseInt(value[i].split(":")[1]) / 1000;
				return voltage;
			} catch (IOException e) {
			}
		return new Integer[] { 0 };
	}

	public static Integer[] getFreqVoltages() {
		if (Utils.exist(CPU_VOLTAGE))
			try {
				String[] value = Utils.readBlock(CPU_VOLTAGE)
						.replace(" mV", "").replace("mhz:", "")
						.split("\\r?\\n");
				Integer[] freq = new Integer[value.length];
				for (int i = 0; i < value.length; i++)
					freq[i] = Integer.parseInt(value[i].split(" ")[0]);
				return freq;
			} catch (IOException e) {
			}
		return new Integer[] { 0 };
	}

	public static Integer[] getVoltages() {
		if (Utils.exist(CPU_VOLTAGE))
			try {
				String[] value = Utils.readBlock(CPU_VOLTAGE)
						.replace(" mV", "").replace("mhz:", "")
						.split("\\r?\\n");
				Integer[] voltages = new Integer[value.length];
				for (int i = 0; i < value.length; i++)
					voltages[i] = Integer.parseInt(value[i].split(" ")[1]);
				return voltages;
			} catch (IOException e) {
			}
		return new Integer[] { 0 };
	}

}
