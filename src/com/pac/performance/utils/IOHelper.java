package com.pac.performance.utils;

import java.io.IOException;

public class IOHelper {

	public static final String EXTERNAL_READ = "/sys/block/mmcblk1/queue/read_ahead_kb";
	public static final String INTERNAL_READ = "/sys/block/mmcblk0/queue/read_ahead_kb";
	public static final String EXTERNAL_SCHEDULER = "/sys/block/mmcblk1/queue/scheduler";
	public static final String INTERNAL_SCHEDULER = "/sys/block/mmcblk0/queue/scheduler";

	public static int getExternalRead() {
		if (Utils.exist(EXTERNAL_READ))
			try {
				return Integer.parseInt(Utils.readLine(EXTERNAL_READ));
			} catch (IOException e) {
			}
		return 0;
	}

	public static int getInternalRead() {
		if (Utils.exist(INTERNAL_READ))
			try {
				return Integer.parseInt(Utils.readLine(INTERNAL_READ));
			} catch (IOException e) {
			}
		return 0;
	}

	public static String getCurExternalScheduler() {
		if (Utils.exist(EXTERNAL_SCHEDULER))
			try {
				String[] values = Utils.readLine(EXTERNAL_SCHEDULER).split(" ");
				for (String scheduler : values)
					if (scheduler.contains("["))
						return scheduler.replace("[", "").replace("]", "");
			} catch (IOException e) {
			}
		return "0";
	}

	public static String[] getExternalSchedulers() {
		if (Utils.exist(EXTERNAL_SCHEDULER))
			try {
				String[] values = Utils.readLine(EXTERNAL_SCHEDULER).split(" ");
				String[] schedulers = new String[values.length];
				for (int i = 0; i < values.length; i++)
					schedulers[i] = values[i].replace("[", "").replace("]", "");
				return schedulers;
			} catch (IOException e) {
			}
		return new String[] { "0" };
	}

	public static String getCurInternalScheduler() {
		if (Utils.exist(INTERNAL_SCHEDULER))
			try {
				String[] values = Utils.readLine(INTERNAL_SCHEDULER).split(" ");
				for (String scheduler : values)
					if (scheduler.contains("["))
						return scheduler.replace("[", "").replace("]", "");
			} catch (IOException e) {
			}
		return "0";
	}

	public static String[] getInternalSchedulers() {
		if (Utils.exist(INTERNAL_SCHEDULER))
			try {
				String[] values = Utils.readLine(INTERNAL_SCHEDULER).split(" ");
				String[] schedulers = new String[values.length];
				for (int i = 0; i < values.length; i++)
					schedulers[i] = values[i].replace("[", "").replace("]", "");
				return schedulers;
			} catch (IOException e) {
			}
		return new String[] { "0" };
	}

}
