package com.pac.performance.utils;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Handler;

import com.pac.performance.R;
import com.pac.performance.fragments.CPUFragment;

public class Control {

	private static List<String> CPUCommands = new ArrayList<String>();
	private static List<String> CPUlistfiles = new ArrayList<String>();

	private static List<String> VoltageCommands = new ArrayList<String>();
	private static List<String> Voltagelistfiles = new ArrayList<String>();

	private static Context context;

	public static String[][] stringfiles = {
			{ CPUHelper.MAX_FREQ, CPUHelper.MIN_FREQ, CPUHelper.MAX_SCREEN_OFF,
					CPUHelper.MIN_SCREEN_ON, CPUHelper.CUR_GOVERNOR },
			{ VoltageHelper.CPU_VOLTAGE } };

	public static void setCPU(Context c) {
		context = c;
		setValueCPU();
		reset();
	}

	private static void setValueCPU() {
		for (int i = 0; i < CPUCommands.size(); i++) {
			RootHelper.run(CPUCommands.get(i));
		}
	}

	public static void setVoltage(Context c) {
		context = c;
		setValueVoltage();
		reset();
	}

	private static void setValueVoltage() {
		for (int i = 0; i < VoltageCommands.size(); i++) {
			RootHelper.run(VoltageCommands.get(i));
			Utils.saveString(Voltagelistfiles.get(i), VoltageCommands.get(i),
					context);
		}
	}

	public static void reset() {

		Runnable r = new Runnable() {
			public void run() {
				CPUFragment.setLayout();

				CPUCommands.clear();
				CPUlistfiles.clear();

				VoltageCommands.clear();
				Voltagelistfiles.clear();
			}
		};
		Handler handler = new Handler();
		handler.post(r);
		handler.postDelayed(r, 300);

		Utils.toast(context.getString(R.string.applysuccessfully), context);
	}

	private static void saveCPUCommand(String command) {
		CPUCommands.add(command);
	}

	public static void runCPUGeneric(String value, String file) {
		saveCPUCommand("echo " + value + " > " + file);
		CPUlistfiles.add(file);
	}

	private static void saveVoltageCommand(String command) {
		VoltageCommands.add(command);
	}

	public static void runVoltageGeneric(String value, String file) {
		saveVoltageCommand("echo " + value + " > " + file);
		Voltagelistfiles.add(file);
	}
}
