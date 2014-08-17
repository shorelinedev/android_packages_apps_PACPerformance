package com.pac.performance.utils;

import android.app.Activity;

public class CommandControl implements Constants {

    public final String fileSplit = "qrfdvbfdgsdfasd";
    public final String idSplit = "sdfwefwefwe";

    public enum CommandType {
        GENERIC, CPU
    }

    public void commandSaver(String file, String value, int customID,
            Activity activity) {
        mUtils.saveString(file, value, activity);

        String saved = mUtils
                .getString(COMMAND_NAME, "nothing_found", activity);
        if (customID > -1) {
            mUtils.saveString(file + idSplit + customID, value, activity);

            if (!saved.contains(file + idSplit + customID)) mUtils.saveString(
                    COMMAND_NAME, saved.equals("nothing_found") ? file
                            + idSplit + customID : saved + fileSplit + file
                            + idSplit + customID, activity);
        } else {
            mUtils.saveString(file, value, activity);

            String name = mUtils.getString(COMMAND_NAME, "nothing_found",
                    activity);
            if (!name.contains(file)) mUtils.saveString(COMMAND_NAME,
                    name.equals("nothing_found") ? file : name + fileSplit
                            + file, activity);
        }
    }

    public void runGeneric(String file, String value, int customID,
            Activity activity) {
        rootHelper.runCommand("echo " + value + " > " + file);

        commandSaver(file, "echo " + value + " > " + file, customID, activity);
    }

    public void startModule(String module, boolean save, Activity activity) {
        rootHelper.runCommand("start " + module);

        if (save) commandSaver(module, "start " + module, -1, activity);
    }

    public void stopModule(String module, boolean save, Activity activity) {
        rootHelper.runCommand("stop " + module);
        if (module.equals(CPU_MPDEC)) bringCoresOnline();

        if (save) commandSaver(module, "stop " + module, -1, activity);
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
            CommandType command, final int customID, final Activity activity) {
        if (command == CommandType.CPU) {
            if (cpuHelper.getCoreCount() > 1) {
                new Thread() {
                    public void run() {
                        boolean stoppedMpdec = false;
                        if (rootHelper.moduleActive(CPU_MPDEC)) {
                            stopModule(CPU_MPDEC, false, activity);
                            stoppedMpdec = true;
                        }

                        for (int i = 0; i < cpuHelper.getCoreCount(); i++)
                            // Check if core is online with a while loop
                            while (true)
                                if (mUtils.existFile(String.format(file, i))) {
                                    runGeneric(String.format(file, i), value,
                                            customID, activity);
                                    break;
                                }

                        if (stoppedMpdec) startModule(CPU_MPDEC, false,
                                activity);
                    }
                }.start();
            }
        } else if (command == CommandType.GENERIC) {
            runGeneric(file, value, customID, activity);
        }
    }
}
