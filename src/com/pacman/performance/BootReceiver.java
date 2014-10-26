package com.pacman.performance;

import com.pacman.performance.R;
import com.pacman.performance.services.NotificationStatsService;
import com.pacman.performance.services.PerAppModesService;
import com.pacman.performance.utils.Constants;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BootReceiver extends BroadcastReceiver implements Constants {

	@Override
	public void onReceive(final Context context, Intent intent) {

		Log.i(TAG, "Bootreceiver: starting");

		// Start Notification Stats if activated
		if (mUtils.getBoolean("notistats", false, context)) {
			Log.i(TAG, "Bootreceiver: starting Notification Stats");
			context.startService(new Intent(context,
					NotificationStatsService.class));
		}

		// Start Per App Mode if activated
		if (mUtils.getBoolean("perappmode", false, context)) {
			Log.i(TAG, "Bootreceiver: starting Per app mode");
			context.startService(new Intent(context, PerAppModesService.class));
		}

		// Run set on boot
		if (mUtils.getBoolean("setonboot", false, context)) {
			String savedCommands = mUtils
					.getString(COMMAND_NAME, null, context);

			if (savedCommands != null) {

				Log.i(TAG, "Bootreceiver: set on boot");

				// Check first if root is accessable and busybox is installed
				if (!rootHelper.rootAccess() || !rootHelper.busyboxInstalled()) {
					Log.i(TAG, "Bootreceiver: root or busybox failed");
					return;
				}

				// Check if kernel is still the same
				String kernelVersion = mUtils.getString("kernel_version", "",
						context);
				if (kernelVersion.isEmpty()
						|| !mUtils.readFile(PROC_VERSION).equals(kernelVersion)) {
					Log.i(TAG, "Bootreceiver: new kernel detected");
					mUtils.toast(
							context.getString(R.string.new_kernel,
									context.getString(R.string.app_name)),
							context);

					context.getSharedPreferences(PREF_NAME, 0).edit().clear()
							.commit();
					return;
				}

				try {
					int delay = mUtils.getInteger("setonbootdelay", 0, context);
					Log.i(TAG, "Bootreceiver: set on boot delay " + delay
							+ "sec");
					Thread.sleep(delay * 1000);

					String[] files = savedCommands
							.split(mCommandControl.fileSplit);
					for (String file : files)
						rootHelper.runCommand(mUtils.getString(file, "ls",
								context));

				} catch (InterruptedException e) {
					e.printStackTrace();
				}

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

				Log.i(TAG, "Bootreceiver: custom commander");

				// Check first if root is accessable and busybox is installed
				if (!rootHelper.rootAccess() || !rootHelper.busyboxInstalled()) {
					Log.i(TAG, "Bootreceiver: root or busybox failed");
					return;
				}

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
