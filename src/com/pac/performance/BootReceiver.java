package com.pac.performance;

import com.pac.performance.utils.Control;
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
					Utils.getString("kernelversion", "nothing", context)))
				setValue();
			else
				Utils.toast(context.getString(R.string.newkernel), context);
	}

	private static void setValue() {
		for (int i = 0; i < Control.stringfiles.length; i++)
			for (String name : Control.stringfiles[i])
				RootHelper.run(Utils.getString(name, "", context));

		Utils.toast(context.getString(R.string.ppbootedup), context);
	}
}