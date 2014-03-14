package com.pac.performance.helpers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.pac.performance.utils.Utils;

public class VMHelper {

	public static final String VM_PATH = "/proc/sys/vm";

	public static List<String> getVMValues() {
		List<String> dummy = new ArrayList<String>();
		if (Utils.exist(VM_PATH))
			try {
				for (String file : getVMPaths())
					dummy.add(Utils.readLine(file));
			} catch (IOException e) {

			}
		else
			dummy.add("0");
		return dummy;
	}

	public static List<String> getVMFiles() {
		List<String> dummy = new ArrayList<String>();
		if (Utils.exist(VM_PATH)) {
			File[] filepaths = new File(VM_PATH).listFiles();
			for (File file : filepaths)
				if ((file.getName().equals("dirty_ratio")
						|| file.getName().equals("dirty_background_ratio")
						|| file.getName().equals("dirty_expire_centisecs")
						|| file.getName().equals("dirty_writeback_centisecs")
						|| file.getName().equals("min_free_kbytes")
						|| file.getName().equals("overcommit_ratio")
						|| file.getName().equals("swappiness") || file
						.getName().equals("vfs_cache_pressure")))
					dummy.add(file.getName());
		} else
			dummy.add("0");
		return dummy;
	}

	public static List<String> getVMPaths() {
		List<String> dummy = new ArrayList<String>();
		if (Utils.exist(VM_PATH)) {
			File[] filepaths = new File(VM_PATH).listFiles();
			for (File path : filepaths)
				if ((path.getName().equals("dirty_ratio")
						|| path.getName().equals("dirty_background_ratio")
						|| path.getName().equals("dirty_expire_centisecs")
						|| path.getName().equals("dirty_writeback_centisecs")
						|| path.getName().equals("min_free_kbytes")
						|| path.getName().equals("overcommit_ratio")
						|| path.getName().equals("swappiness") || path
						.getName().equals("vfs_cache_pressure")))
					dummy.add(path.getPath());
		} else
			dummy.add("0");
		return dummy;
	}
}
