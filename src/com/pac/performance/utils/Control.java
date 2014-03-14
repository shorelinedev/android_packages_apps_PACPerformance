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

package com.pac.performance.utils;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Handler;

import com.pac.performance.MainActivity;
import com.pac.performance.fragments.AudioFragment;
import com.pac.performance.fragments.BatteryFragment;
import com.pac.performance.fragments.CPUFragment;
import com.pac.performance.fragments.IOFragment;
import com.pac.performance.fragments.MinFreeFragment;
import com.pac.performance.fragments.VMFragment;
import com.pac.performance.fragments.VoltageFragment;
import com.pac.performance.helpers.AudioHelper;
import com.pac.performance.helpers.BatteryHelper;
import com.pac.performance.helpers.CPUHelper;
import com.pac.performance.helpers.IOHelper;
import com.pac.performance.helpers.MinFreeHelper;
import com.pac.performance.helpers.RootHelper;
import com.pac.performance.helpers.VoltageHelper;

public class Control {

	private static List<String> CPUCommands = new ArrayList<String>();
	private static List<String> CPUlistfiles = new ArrayList<String>();

	private static List<String> BatteryCommands = new ArrayList<String>();
	private static List<String> Batterylistfiles = new ArrayList<String>();

	private static List<String> AudioCommands = new ArrayList<String>();
	private static List<String> Audiolistfiles = new ArrayList<String>();

	private static List<String> VoltageCommands = new ArrayList<String>();
	private static List<String> Voltagelistfiles = new ArrayList<String>();

	private static List<String> IOCommands = new ArrayList<String>();
	private static List<String> IOlistfiles = new ArrayList<String>();

	private static List<String> MinFreeCommands = new ArrayList<String>();
	private static List<String> MinFreelistfiles = new ArrayList<String>();

	private static List<String> VMCommands = new ArrayList<String>();
	private static List<String> VMlistfiles = new ArrayList<String>();

	public static String[][] stringfiles = {
			{ CPUHelper.MAX_FREQ, CPUHelper.MIN_FREQ, CPUHelper.MAX_SCREEN_OFF,
					CPUHelper.MIN_SCREEN_ON, CPUHelper.CUR_GOVERNOR,
					CPUHelper.INTELLIPLUG, CPUHelper.INTELLIPLUG_ECO_MODE },
			{ BatteryHelper.FAST_CHARGE, BatteryHelper.BLX },
			AudioHelper.FAUX_SOUND,
			{ VoltageHelper.CPU_VOLTAGE, VoltageHelper.FAUX_VOLTAGE },
			{ IOHelper.INTERNAL_SCHEDULER, IOHelper.EXTERNAL_SCHEDULER,
					IOHelper.INTERNAL_READ, IOHelper.EXTERNAL_READ },
			{ MinFreeHelper.MINFREE } };

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

	public static void setAudio(Context context) {
		for (int i = 0; i < AudioCommands.size(); i++) {
			RootHelper.run(AudioCommands.get(i));
			Utils.saveString(Audiolistfiles.get(i), AudioCommands.get(i),
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

	public static void setMinFree(Context context) {
		for (int i = 0; i < MinFreeCommands.size(); i++) {
			RootHelper.run(MinFreeCommands.get(i));
			Utils.saveString(MinFreelistfiles.get(i), MinFreeCommands.get(i),
					context);
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
				if (CPUFragment.layout != null && MainActivity.CPUChange)
					CPUFragment.setLayout();

				if (BatteryFragment.layout != null
						&& MainActivity.BatteryChange)
					BatteryFragment.setLayout();

				if (AudioFragment.layout != null && MainActivity.AudioChange)
					AudioFragment.setLayout();

				if (VoltageFragment.layout != null
						&& MainActivity.VoltageChange)
					VoltageFragment.setLayout();

				if (IOFragment.layout != null && MainActivity.IOChange)
					IOFragment.setLayout();

				if (MinFreeFragment.layout != null
						&& MainActivity.MinFreeChange)
					MinFreeFragment.setLayout();

				if (VMFragment.layout != null && MainActivity.VMChange)
					VMFragment.setLayout();

				MainActivity.CPUChange = MainActivity.BatteryChange = MainActivity.AudioChange = false;
				MainActivity.VoltageChange = MainActivity.IOChange = MainActivity.MinFreeChange = false;
				MainActivity.VMChange = false;

				CPUCommands.clear();
				CPUlistfiles.clear();

				BatteryCommands.clear();
				Batterylistfiles.clear();

				AudioCommands.clear();
				Audiolistfiles.clear();

				VoltageCommands.clear();
				Voltagelistfiles.clear();

				IOCommands.clear();
				IOlistfiles.clear();

				MinFreeCommands.clear();
				MinFreelistfiles.clear();

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

	public static void runAudioFaux(String value, String file) {
		if (file.equals(AudioHelper.FAUX_HEADPHONE_GAIN))
			AudioCommands.add("echo "
					+ String.valueOf(Integer.parseInt(value) + 40) + " "
					+ String.valueOf(Integer.parseInt(value) + 40) + " > "
					+ file);
		else if (file.equals(AudioHelper.FAUX_HEADPHONE_PA_GAIN))
			AudioCommands.add("echo "
					+ String.valueOf(Integer.parseInt(value) + 12) + " "
					+ String.valueOf(Integer.parseInt(value) + 12) + " > "
					+ file);
		else
			AudioCommands.add("echo "
					+ String.valueOf(Integer.parseInt(value) + 40) + " > "
					+ file);
		Audiolistfiles.add(file);
	}

	public static void runVoltageGeneric(String value, String file) {
		VoltageCommands.add("echo " + value + " > " + file);
		Voltagelistfiles.add(file);
	}

	public static void runIOGeneric(String value, String file) {
		IOCommands.add("echo " + value + " > " + file);
		IOlistfiles.add(file);
	}

	public static void runMinFreeGeneric(String value, String file) {
		MinFreeCommands.add("echo " + value + " > " + file);
		MinFreelistfiles.add(file);
	}

	public static void runVMGeneric(String value, String file) {
		VMCommands.add("sysctl -w vm." + file + "=" + value);
		VMlistfiles.add(file);
	}
}
