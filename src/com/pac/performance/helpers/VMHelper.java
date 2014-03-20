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

package com.pac.performance.helpers;

import com.pac.performance.utils.Utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class VMHelper {

    private static final String[] supportedvm = { "dirty_ratio",
            "dirty_background_ratio", "dirty_expire_centisecs",
            "dirty_writeback_centisecs", "min_free_kbytes", "overcommit_ratio",
            "swappiness", "vfs_cache_pressure" };

    public static final String VM_PATH = "/proc/sys/vm";

    public static List<String> getVMValues() {
        List<String> dummy = new ArrayList<String>();
        if (Utils.exist(VM_PATH))
            try {
                for (String file : getVMPaths())
                    dummy.add(Utils.readLine(file));
            } catch (IOException ignored) {

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
                for (String supported : supportedvm)
                    if (file.getName().equals(supported))
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
                for (String supported : supportedvm)
                    if (path.getName().equals(supported))
                        dummy.add(path.getPath());
        } else
            dummy.add("0");
        return dummy;
    }
}
