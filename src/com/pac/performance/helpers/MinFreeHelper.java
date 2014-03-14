package com.pac.performance.helpers;

import java.io.IOException;

import com.pac.performance.utils.Utils;

public class MinFreeHelper {

	public static final String MINFREE = "/sys/module/lowmemorykiller/parameters/minfree";

	public static Integer[] getMinFreeValues() {
		if (Utils.exist(MINFREE))
			try {
				String[] dummy = Utils.readLine(MINFREE).split(",");
				Integer[] values = new Integer[dummy.length];
				for (int i = 0; i < dummy.length; i++)
					values[i] = Integer.parseInt(dummy[i]);
				return values;
			} catch (IOException e) {
			}
		return new Integer[] { 0 };
	}
}
