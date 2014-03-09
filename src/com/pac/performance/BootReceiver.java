package com.pac.performance;

import com.pac.performance.utils.CPUHelper;
import com.pac.performance.utils.RootHelper;
import com.pac.performance.utils.Utils;
import com.stericson.roottools.RootTools;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootReceiver extends BroadcastReceiver {

	private static Context context;

	@Override
	public void onReceive(Context c, Intent intent) {
		context = c;
		if (Utils.getBoolean("setonboot", false, context)
				&& RootTools.isAccessGiven() && RootTools.isBusyboxAvailable())
			if (Utils.getFormattedKernelVersion().equals(
					Utils.getString("kernelversion", "nothing", context))) {
				setOnBoot();
				Utils.toast(context.getString(R.string.applysuccessfully),
						context);
			} else
				Utils.toast(context.getString(R.string.newkernel), context);
	}

	private static void setOnBoot() {
		setValue(CPUHelper.MAX_FREQ);
		setValue(CPUHelper.MIN_FREQ);
		setValue(CPUHelper.MAX_SCREEN_OFF);
		setValue(CPUHelper.MIN_SCREEN_ON);
		setValue(CPUHelper.CUR_GOVERNOR);
	}

	private static void setValue(String file) {
		if (Utils.exist(file)
				&& !Utils.getString(file, "nothing", context).equals("nothing"))
			RootHelper.run("echo " + Utils.getString(file, "nothing", context)
					+ " > " + file);
	}
}