package com.pac.performance.helpers;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.pac.performance.utils.Constants;

public class VirtualMachineHelper implements Constants {

    private List<String> vmFiles = new ArrayList<String>();

    private static final String[] supportedVM = { "dirty_ratio",
            "dirty_background_ratio", "dirty_expire_centisecs",
            "dirty_writeback_centisecs", "min_free_kbytes", "overcommit_ratio",
            "swappiness", "vfs_cache_pressure" };

    public String getVMValue(String file) {
        if (mUtils.existFile(VM_PATH + "/" + file)) {
            String value = mUtils.readFile(VM_PATH + "/" + file);
            if (value != null) return value;
        }
        return null;
    }

    public List<String> getVMfiles() {
        if (vmFiles.size() < 1) {
            File[] files = new File(VM_PATH).listFiles();
            if (files.length > 0) {
                for (String supported : supportedVM)
                    for (int i = 0; i < files.length; i++)
                        if (files[i].getName().equals(supported)) vmFiles
                                .add(files[i].getName());
            }
        }
        return vmFiles;
    }
}
