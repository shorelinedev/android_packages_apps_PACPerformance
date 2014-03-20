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

import java.io.IOException;

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
            } catch (IOException ignored) {
            }
        return new Integer[] { 0 };
    }
}
