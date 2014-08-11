package com.pac.performance.utils;

import java.io.IOException;

import com.stericson.roottools.RootTools;

import android.app.Activity;
import android.util.Log;

public class CommandControl implements Constants {

    public final String fileSplit = "qrfdvbfdgsdfasd";

    public enum CommandType {
        GENERIC, CPU
    }

    public void commandSaver(String file, String value, Activity activity) {
        mUtils.saveString(file, value, activity);

        String name = mUtils.getString(COMMAND_NAME, "nothing_found", activity);
        if (!name.contains(file)) mUtils.saveString(COMMAND_NAME,
                name.equals("nothing_found") ? file : name + fileSplit + file,
                activity);
    }

    public void runGeneric(String file, String value, Activity activity) {
        rootHelper.runCommand("echo " + value + " > " + file);

        commandSaver(file, "echo " + value + " > " + file, activity);
    }

    public boolean moduleActive(String module) {
        String output = null;
        try {
            output = RootTools.getOutput("echo `ps | grep " + module
                    + " | grep -v \"grep " + module + "\" | awk '{print $1}'`");
        } catch (IOException e) {
            Log.e(TAG, "failed to get status of " + module);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return output != null && output.length() > 0 && !output.equals("error");
    }

    public void startModule(String module, boolean save, Activity activity) {
        rootHelper.runCommand("start " + module);

        if (save) commandSaver(module, "start " + module, activity);
    }

    public void stopModule(String module, boolean save, Activity activity) {
        rootHelper.runCommand("stop " + module);
        if (module.equals(CPU_MPDEC)) bringCoresOnline();

        if (save) commandSaver(module, "stop " + module, activity);
    }

    public void bringCoresOnline() {
        new Thread() {
            public void run() {
                try {
                    for (int i = 0; i < cpuHelper.getCoreCount(); i++)
                        rootHelper.runCommand("echo 1 > "
                                + String.format(CPU_CORE_ONLINE, i));

                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public void runCommand(final String value, final String file,
            CommandType command, final Activity activity) {
        if (command == CommandType.CPU) {
            if (cpuHelper.getCoreCount() > 1) {
                new Thread() {
                    public void run() {
                        boolean stoppedMpdec = false;
                        if (moduleActive(CPU_MPDEC)) {
                            stopModule(CPU_MPDEC, false, activity);
                            stoppedMpdec = true;
                        }

                        for (int i = 0; i < cpuHelper.getCoreCount(); i++)
                            runGeneric(String.format(file, i), value, activity);

                        if (stoppedMpdec) startModule(CPU_MPDEC, false,
                                activity);
                    }
                }.start();
            }
        } else if (command == CommandType.GENERIC) {
            runGeneric(file, value, activity);
        }
    }

}
