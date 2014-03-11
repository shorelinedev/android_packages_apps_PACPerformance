package com.pac.performance.utils;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Handler;

import com.pac.performance.MainActivity;
import com.pac.performance.fragments.CPUFragment;
import com.pac.performance.fragments.MiscFragment;
import com.pac.performance.fragments.VMFragment;
import com.pac.performance.fragments.VoltageFragment;

public class Control {

	private static List<String> CPUCommands = new ArrayList<String>();
	private static List<String> CPUlistfiles = new ArrayList<String>();

	private static List<String> VoltageCommands = new ArrayList<String>();
	private static List<String> Voltagelistfiles = new ArrayList<String>();

	private static List<String> MiscCommands = new ArrayList<String>();
	private static List<String> Misclistfiles = new ArrayList<String>();

	private static List<String> VMCommands = new ArrayList<String>();
	private static List<String> VMlistfiles = new ArrayList<String>();

	private static Context context;

	public static String[][] stringfiles = {
			{ CPUHelper.MAX_FREQ, CPUHelper.MIN_FREQ, CPUHelper.MAX_SCREEN_OFF,
					CPUHelper.MIN_SCREEN_ON, CPUHelper.CUR_GOVERNOR,
					CPUHelper.INTELLIPLUG, CPUHelper.INTELLIPLUG_ECO_MODE },
			{ VoltageHelper.CPU_VOLTAGE, VoltageHelper.FAUX_VOLTAGE },
			{ MiscHelper.INTERNAL_SCHEDULER, MiscHelper.EXTERNAL_SCHEDULER,
					MiscHelper.INTERNAL_READ, MiscHelper.EXTERNAL_READ } };

	public static void setCPU(Context c) {
		context = c;
		setValueCPU();
	}

	private static void setValueCPU() {
		for (int i = 0; i < CPUCommands.size(); i++) {
			RootHelper.run(CPUCommands.get(i));
			Utils.saveString(CPUlistfiles.get(i), CPUCommands.get(i), context);
		}
	}

	public static void setVoltage(Context c) {
		context = c;
		setValueVoltage();
	}

	private static void setValueVoltage() {
		for (int i = 0; i < VoltageCommands.size(); i++) {
			RootHelper.run(VoltageCommands.get(i));
			Utils.saveString(Voltagelistfiles.get(i), VoltageCommands.get(i),
					context);
		}
	}

	public static void setMisc(Context c) {
		context = c;
		setValueMisc();
	}

	private static void setValueMisc() {
		for (int i = 0; i < MiscCommands.size(); i++) {
			RootHelper.run(MiscCommands.get(i));
			Utils.saveString(Misclistfiles.get(i), MiscCommands.get(i), context);
		}
	}

	public static void setVM(Context c) {
		context = c;
		setValueVM();
	}

	private static void setValueVM() {
		for (int i = 0; i < VMCommands.size(); i++) {
			RootHelper.run(VMCommands.get(i));
			Utils.saveString(VMlistfiles.get(i), VMCommands.get(i), context);
		}
	}

	public static void reset() {

		Runnable r = new Runnable() {
			public void run() {
				if (CPUFragment.layout != null && MainActivity.CPUChange)
					CPUFragment.setLayout();
				if (VoltageFragment.layout != null
						&& MainActivity.VoltageChange)
					VoltageFragment.setLayout();
				if (MiscFragment.layout != null && MainActivity.MiscChange)
					MiscFragment.setLayout();
				if (VMFragment.layout != null && MainActivity.VMChange)
					VMFragment.setLayout();

				CPUCommands.clear();
				CPUlistfiles.clear();

				VoltageCommands.clear();
				Voltagelistfiles.clear();

				MiscCommands.clear();
				Misclistfiles.clear();

				VMCommands.clear();
				VMlistfiles.clear();
			}
		};
		Handler handler = new Handler();
		handler.post(r);
		handler.postDelayed(r, 100);
	}

	public static void runCPUGeneric(String value, String file) {
		CPUCommands.add("echo " + value + " > " + file);
		CPUlistfiles.add(file);
	}

	public static void runVoltageGeneric(String value, String file) {
		VoltageCommands.add("echo " + value + " > " + file);
		Voltagelistfiles.add(file);
	}

	public static void runMiscGeneric(String value, String file) {
		MiscCommands.add("echo " + value + " > " + file);
		Misclistfiles.add(file);
	}

	public static void runVMGeneric(String value, String file) {
		VMCommands.add("sysctl -w vm." + file + "=" + value);
		VMlistfiles.add(file);
	}
}
