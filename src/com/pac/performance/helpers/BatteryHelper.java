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
