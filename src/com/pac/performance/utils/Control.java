package com.pac.performance.utils;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Handler;

import com.pac.performance.fragments.CPUFragment;
import com.pac.performance.fragments.MainFragment;
import com.pac.performance.fragments.IOFragment;
import com.pac.performance.fragments.VMFragment;
import com.pac.performance.fragments.VoltageFragment;

public class Control {

	private static List<String> CPUCommands = new ArrayList<String>();
	private static List<String> CPUlistfiles = new ArrayList<String>();

	private static List<String> BatteryCommands = new ArrayList<String>();
	private static List<String> Batterylistfiles = new ArrayList<String>();

	private static List<String> VoltageCommands = new ArrayList<String>();
	private static List<String> Voltagelistfiles = new ArrayList<String>();

	private static List<String> IOCommands = new ArrayList<String>();
	private static List<String> IOlistfiles = new ArrayList<String>();

	private static List<String> VMCommands = new ArrayList<String>();
	private static List<String> VMlistfiles = new ArrayList<String>();

	public static String[][] stringfiles = {
			{ CPUHelper.MAX_FREQ, CPUHelper.MIN_FREQ, CPUHelper.MAX_SCREEN_OFF,
					CPUHelper.MIN_SCREEN_ON, CPUHelper.CUR_GOVERNOR,
					CPUHelper.INTELLIPLUG, CPUHelper.INTELLIPLUG_ECO_MODE },
			{ BatteryHelper.FAST_CHARGE, BatteryHelper.BLX },
			{ VoltageHelper.CPU_VOLTAGE, VoltageHelper.FAUX_VOLTAGE },
			{ IOHelper.INTERNAL_SCHEDULER, IOHelper.EXTERNAL_SCHEDULER,
					IOHelper.INTERNAL_READ, IOHelper.EXTERNAL_READ } };

	public static void setCPU(Context context) {
		for (int i = 0; i < CPUCommands.size(); i++) {
			RootHelper.run(CPUCommands.get(i));
			Utils.saveString(CPUlistfiles.get(i), CPUCommands.get(i), context);
		}
	}

	public static void setBattery(Context context) {
		for (int i = 0; i < BatteryCommands.size(); i++) {
			RootHelper.run(BatteryCommands.get(i));
			Utils.saveString(Batterylistfiles.get(i), BatteryCommands.get(i),
					context);
		}
	}

	public static void setVoltage(Context context) {
		for (int i = 0; i < VoltageCommands.size(); i++) {
			RootHelper.run(VoltageCommands.get(i));
			Utils.saveString(Voltagelistfiles.get(i), VoltageCommands.get(i),
					context);
		}
	}

	public static void setMisc(Context context) {
		for (int i = 0; i < IOCommands.size(); i++) {
			RootHelper.run(IOCommands.get(i));
			Utils.saveString(IOlistfiles.get(i), IOCommands.get(i), context);
		}
	}

	public static void setVM(Context context) {
		for (int i = 0; i < VMCommands.size(); i++) {
			RootHelper.run(VMCommands.get(i));
			Utils.saveString(VMlistfiles.get(i), VMCommands.get(i), context);
		}
	}

	public static void reset() {

		Runnable r = new Runnable() {
			public void run() {
				if (CPUFragment.layout != null && MainFragment.CPUChange)
					CPUFragment.setLayout();
				if (VoltageFragment.layout != null
						&& MainFragment.VoltageChange)
					VoltageFragment.setLayout();
				if (IOFragment.layout != null && MainFragment.IOChange)
					IOFragment.setLayout();
				if (VMFragment.layout != null && MainFragment.VMChange)
					VMFragment.setLayout();

				MainFragment.CPUChange = MainFragment.VoltageChange = MainFragment.IOChange = MainFragment.VMChange = false;

				CPUCommands.clear();
				CPUlistfiles.clear();

				BatteryCommands.clear();
				Batterylistfiles.clear();

				VoltageCommands.clear();
				Voltagelistfiles.clear();

				IOCommands.clear();
				IOlistfiles.clear();

				VMCommands.clear();
				VMlistfiles.clear();
			}
		};
		Handler handler = new Handler();
		handler.post(r);
		handler.postDelayed(r, 500);
	}

	public static void runCPUGeneric(String value, String file) {
		CPUCommands.add("echo " + value + " > " + file);
		CPUlistfiles.add(file);
	}

	public static void runBatteryGeneric(String value, String file) {
		BatteryCommands.add("echo " + value + " > " + file);
		Batterylistfiles.add(file);
	}

	public static void runVoltageGeneric(String value, String file) {
		VoltageCommands.add("echo " + value + " > " + file);
		Voltagelistfiles.add(file);
	}

	public static void runIOGeneric(String value, String file) {
		IOCommands.add("echo " + value + " > " + file);
		IOlistfiles.add(file);
	}

	public static void runVMGeneric(String value, String file) {
		VMCommands.add("sysctl -w vm." + file + "=" + value);
		VMlistfiles.add(file);
	}
}
