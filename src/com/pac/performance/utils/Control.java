package com.pac.performance.utils;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import com.pac.performance.R;
import com.pac.performance.fragments.CPUFragment;

public class Control {

	private static List<String> Commands = new ArrayList<String>();
	private static List<String> listfiles = new ArrayList<String>();
	private static Context context;

	public static String[][] stringfiles = { { CPUHelper.MAX_FREQ,
			CPUHelper.MIN_FREQ, CPUHelper.MAX_SCREEN_OFF,
			CPUHelper.MIN_SCREEN_ON, CPUHelper.CUR_GOVERNOR } };

	public static void setCPU(Context c) {
		context = c;
		setValueCPU();
		CPUFragment.setLayout();
		reset();
	}

	private static void reset() {
		Commands.clear();
		listfiles.clear();
		Utils.toast(context.getString(R.string.applysuccessfully), context);
	}

	private static void setValueCPU() {
		for (int i = 0; i < Commands.size(); i++) {
			RootHelper.run(Commands.get(i));
			Utils.saveString(listfiles.get(i), Commands.get(i), context);
		}
	}

	public static void saveCommand(String command) {
		Commands.add(command);
	}

	public static void runGeneric(String value, String file) {
		saveCommand("echo " + value + " > " + file);
		listfiles.add(file);
	}
}
