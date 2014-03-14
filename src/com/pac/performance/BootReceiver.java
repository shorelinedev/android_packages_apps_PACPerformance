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

package com.pac.performance;

import com.pac.performance.helpers.CPUHelper;
import com.pac.performance.helpers.RootHelper;
import com.pac.performance.helpers.VMHelper;
import com.pac.performance.utils.Control;
import com.pac.performance.utils.Utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootReceiver extends BroadcastReceiver {

	private static Context context;

	@Override
	public void onReceive(Context c, Intent intent) {
		context = c;
		if (Utils.getBoolean("setonboot", false, context))
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

		for (int i = 0; i < CPUHelper.getCoreCount(); i++)
			RootHelper.run(Utils.getString(
					CPUHelper.CORE_STAT.replace("present", String.valueOf(i)),
					"", context));

		for (String name : VMHelper.getVMFiles())
			RootHelper.run(Utils.getString(name, "", context));

		Utils.toast(context.getString(R.string.ppbootedup), context);
	}
}