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

import android.content.Context;
import android.os.Handler;

import com.pac.performance.fragments.AudioFragment;
import com.pac.performance.fragments.BatteryFragment;
import com.pac.performance.fragments.CPUFragment;
import com.pac.performance.fragments.IOFragment;
import com.pac.performance.fragments.MainFragment;
import com.pac.performance.fragments.MinFreeFragment;
import com.pac.performance.fragments.VMFragment;
import com.pac.performance.fragments.VoltageFragment;
import com.pac.performance.helpers.RootHelper;

import java.util.ArrayList;
import java.util.List;

public class Control implements Constants {

    private static List<String> CPUCommands = new ArrayList<String>();
    private static List<String> BatteryCommands = new ArrayList<String>();
    private static List<String> AudioCommands = new ArrayList<String>();
    private static List<String> VoltageCommands = new ArrayList<String>();
    private static List<String> IOCommands = new ArrayList<String>();
    private static List<String> MinFreeCommands = new ArrayList<String>();
    private static List<String> VMCommands = new ArrayList<String>();

    public static void setCPU(Context context) {
        for (String cpuvalue : CPUCommands) {
            String file = cpuvalue.split("::")[0];
            String command = cpuvalue.split("::")[1];

            RootHelper.run(command);
            Utils.saveString(file, command, context);
        }
    }

    public static void setBattery(Context context) {
        for (String batteryvalue : BatteryCommands) {
            String file = batteryvalue.split("::")[0];
            String command = batteryvalue.split("::")[1];

            RootHelper.run(command);
            Utils.saveString(file, command, context);
        }
    }

    public static void setAudio(Context context) {
        for (String audiovalue : AudioCommands) {
            String file = audiovalue.split("::")[0];
            String command = audiovalue.split("::")[1];

            RootHelper.run(command);
            Utils.saveString(file, command, context);
        }
    }

    public static void setVoltage(Context context) {
        for (String voltagevalue : VoltageCommands) {
            String file = voltagevalue.split("::")[0];
            String command = voltagevalue.split("::")[1];

            RootHelper.run(command);
            Utils.saveString(file, command, context);
        }
    }

    public static void setIO(Context context) {
        for (String iovalue : IOCommands) {
            String file = iovalue.split("::")[0];
            String command = iovalue.split("::")[1];

            RootHelper.run(command);
            Utils.saveString(file, command, context);
        }
    }

    public static void setMinFree(Context context) {
        for (String minfreevalue : MinFreeCommands) {
            String file = minfreevalue.split("::")[0];
            String command = minfreevalue.split("::")[1];

            RootHelper.run(command);
            Utils.saveString(file, command, context);
        }
    }

    public static void setVM(Context context) {
        for (String vmvalue : VMCommands) {
            String file = vmvalue.split("::")[0];
            String command = vmvalue.split("::")[1];

            RootHelper.run(command);
            Utils.saveString(file, command, context);
        }
    }

    public static void reset() {

        Runnable r = new Runnable() {
            public void run() {

                MainFragment.showButtons(false);

                if (CPUFragment.layout != null && MainFragment.CPUChange) CPUFragment
                        .setValues();

                if (BatteryFragment.layout != null
                        && MainFragment.BatteryChange) BatteryFragment
                        .setValues();

                if (AudioFragment.layout != null && MainFragment.AudioChange) AudioFragment
                        .setValues();

                if (VoltageFragment.layout != null
                        && MainFragment.VoltageChange) VoltageFragment
                        .setValues();

                if (IOFragment.layout != null && MainFragment.IOChange) IOFragment
                        .setValues();

                if (MinFreeFragment.layout != null
                        && MainFragment.MinFreeChange) MinFreeFragment
                        .setValues();

                if (VMFragment.layout != null && MainFragment.VMChange) VMFragment
                        .setValues();

                MainFragment.CPUChange = MainFragment.BatteryChange = MainFragment.AudioChange = false;
                MainFragment.VoltageChange = MainFragment.IOChange = MainFragment.MinFreeChange = false;
                MainFragment.VMChange = false;

                CPUCommands.clear();
                BatteryCommands.clear();
                AudioCommands.clear();
                VoltageCommands.clear();
                IOCommands.clear();
                MinFreeCommands.clear();
                VMCommands.clear();
            }
        };
        Handler handler = new Handler();
        handler.post(r);
        handler.postDelayed(r, 600);
    }

    public static void runCPUGeneric(String value, String file) {
        if (CPUCommands.indexOf(file) != -1) CPUCommands.remove(CPUCommands
                .indexOf(file));

        CPUCommands.add(file + "::echo " + value + " > " + file);
    }

    public static void runBatteryGeneric(String value, String file) {
        if (BatteryCommands.indexOf(file) != -1) BatteryCommands
                .remove(BatteryCommands.indexOf(file));

        BatteryCommands.add(file + "::echo " + value + " > " + file);
    }

    public static void runAudioFaux(String value, String file) {
        if (AudioCommands.indexOf(file) != -1) AudioCommands
                .remove(AudioCommands.indexOf(file));

        if (file.equals(FAUX_HEADPHONE_GAIN)) AudioCommands.add(String
                .valueOf(file + "::echo " + (Integer.parseInt(value) + 40)
                        + " " + (Integer.parseInt(value) + 40) + " > " + file));
        else if (file.equals(FAUX_HEADPHONE_PA_GAIN)) AudioCommands.add(String
                .valueOf(file + "::echo " + (Integer.parseInt(value) + 12)
                        + " " + (Integer.parseInt(value) + 12) + " > " + file));
        else AudioCommands.add(String.valueOf(file + "::echo "
                + (Integer.parseInt(value) + 40) + " > " + file));
    }

    public static void runVoltageGeneric(String value, String file) {
        if (VoltageCommands.indexOf(file) != -1) VoltageCommands
                .remove(VoltageCommands.indexOf(file));

        VoltageCommands.add(file + "::echo " + value + " > " + file);
    }

    public static void runIOGeneric(String value, String file) {
        if (IOCommands.indexOf(file) != -1) IOCommands.remove(IOCommands
                .indexOf(file));

        IOCommands.add(file + "::echo " + value + " > " + file);
    }

    public static void runMinFreeGeneric(String value, String file) {
        if (MinFreeCommands.indexOf(file) != -1) MinFreeCommands
                .remove(MinFreeCommands.indexOf(file));

        MinFreeCommands.add(file + "::echo " + value + " > " + file);
    }

    public static void runVMGeneric(String value, String file) {
        if (VMCommands.indexOf(file) != -1) VMCommands.remove(VMCommands
                .indexOf(file));

        VMCommands.add(file + "::sysctl -w vm." + file + "=" + value);
    }
}
