package com.pac.performance;

import com.pac.performance.services.PerAppModesService;
import com.pac.performance.utils.Constants;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BootReceiver extends BroadcastReceiver implements Constants {

    @Override
    public void onReceive(final Context context, Intent intent) {

        String kernelVersion = mUtils.getString("kernel_version", "", context);
        if (kernelVersion.isEmpty()
                || !mUtils.readFile(PROC_VERSION).equals(kernelVersion)) {
            Log.d(TAG, "new kernel!");
            mUtils.toast(
                    context.getString(R.string.new_kernel,
                            context.getString(R.string.app_name)), context);

            context.getSharedPreferences(PREF_NAME, 0).edit().clear().commit();
            return;
        }

        // Start Per App Mode if activated
        if (mUtils.getBoolean("perappmode", false, context)) context
                .startService(new Intent(context, PerAppModesService.class));

        // Run set on boot
        if (mUtils.getBoolean("setonboot", false, context)) {
            String savedCommands = mUtils
                    .getString(COMMAND_NAME, null, context);

            if (savedCommands != null) {
                // Check first if root is accessable and busyox is
                // installed
                if (!rootHelper.rootAccess() || !rootHelper.busyboxInstalled()) return;

                try {
                    Thread.sleep(mUtils
                            .getInteger("setonbootdelay", 0, context) * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                String[] files = savedCommands.split(mCommandControl.fileSplit);
                for (String file : files)
                    rootHelper
                            .runCommand(mUtils.getString(file, "ls", context));
            }

            mUtils.toast(
                    context.getString(R.string.booted_up,
                            context.getString(R.string.app_name)), context);
        }

        // Run custom commander
        if (mUtils.getBoolean("customcommander", false, context)) {
            String savedCommands = mUtils.getString(
                    mCustomCommanderFragment.prefName, null, context);

            if (savedCommands != null) {
                // Check first if root is accessable and busyox is
                // installed
                if (!rootHelper.rootAccess() || !rootHelper.busyboxInstalled()) return;

                for (String command : mCustomCommanderFragment
                        .getSavedCommands(savedCommands))
                    rootHelper.runCommand(command);
            }

            mUtils.toast(
                    context.getString(R.string.booted_up,
                            context.getString(R.string.custom_commander)),
                    context);
        }

        return;
    }
}
