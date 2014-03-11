package com.pac.performance.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
				if (!(file.getName().equals("lowmem_reserve_ratio")
						|| file.getName().equals("compact_memory")
						|| file.getName().equals("overcommit_memory")
						|| file.getName().equals("nr_pdflush_threads") || file
						.getName().equals("scan_unevictable_pages")))
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
				if (!(path.getName().equals("lowmem_reserve_ratio")
						|| path.getName().equals("compact_memory")
						|| path.getName().equals("overcommit_memory")
						|| path.getName().equals("nr_pdflush_threads") || path
						.getName().equals("scan_unevictable_pages")))
					dummy.add(path.getPath());
		} else
			dummy.add("0");
		return dummy;
	}
}
