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

public class BatteryHelper {

	public static final String BLX = "/sys/devices/virtual/misc/batterylifeextender/charging_limit";
	public static final String FAST_CHARGE = "/sys/kernel/fast_charge/force_fast_charge";
	public static final String BATTERY_VOLTAGE = "/sys/class/power_supply/battery/voltage_now";

	public static int getBLX() {
		if (Utils.exist(BLX))
			try {
				return Integer.parseInt(Utils.readLine(BLX));
			} catch (NumberFormatException e) {
			} catch (IOException e) {
			}
		return 0;
	}

	public static boolean getFastCharge() {
		if (Utils.exist(FAST_CHARGE))
			try {
				return Utils.readLine(FAST_CHARGE).equals("1");
			} catch (IOException e) {
			}
		return false;
	}

	public static int getCurBatteryVoltage() {
		if (Utils.exist(BATTERY_VOLTAGE))
			try {
				return Integer.parseInt(Utils.readLine(BATTERY_VOLTAGE));
			} catch (NumberFormatException e) {
			} catch (IOException e) {
			}
		return 0;
	}
}
