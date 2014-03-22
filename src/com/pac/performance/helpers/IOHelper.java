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

import com.pac.performance.utils.Constants;
import com.pac.performance.utils.Utils;

import java.io.IOException;

public class IOHelper implements Constants {

    public static int getExternalRead() {
        if (Utils.exist(EXTERNAL_READ)) try {
            return Integer.parseInt(Utils.readLine(EXTERNAL_READ));
        } catch (IOException ignored) {}
        return 0;
    }

    public static int getInternalRead() {
        if (Utils.exist(INTERNAL_READ)) try {
            return Integer.parseInt(Utils.readLine(INTERNAL_READ));
        } catch (IOException ignored) {}
        return 0;
    }

    public static String getCurExternalScheduler() {
        if (Utils.exist(EXTERNAL_SCHEDULER)) try {
            String[] values = Utils.readLine(EXTERNAL_SCHEDULER).split(" ");
            for (String scheduler : values)
                if (scheduler.contains("[")) return scheduler.replace("[", "")
                        .replace("]", "");
        } catch (IOException ignored) {}
        return "0";
    }

    public static String[] getExternalSchedulers() {
        if (Utils.exist(EXTERNAL_SCHEDULER)) try {
            String[] values = Utils.readLine(EXTERNAL_SCHEDULER).split(" ");
            String[] schedulers = new String[values.length];
            for (int i = 0; i < values.length; i++)
                schedulers[i] = values[i].replace("[", "").replace("]", "");
            return schedulers;
        } catch (IOException ignored) {}
        return new String[] { "0" };
    }

    public static String getCurInternalScheduler() {
        if (Utils.exist(INTERNAL_SCHEDULER)) try {
            String[] values = Utils.readLine(INTERNAL_SCHEDULER).split(" ");
            for (String scheduler : values)
                if (scheduler.contains("[")) return scheduler.replace("[", "")
                        .replace("]", "");
        } catch (IOException ignored) {}
        return "0";
    }

    public static String[] getInternalSchedulers() {
        if (Utils.exist(INTERNAL_SCHEDULER)) try {
            String[] values = Utils.readLine(INTERNAL_SCHEDULER).split(" ");
            String[] schedulers = new String[values.length];
            for (int i = 0; i < values.length; i++)
                schedulers[i] = values[i].replace("[", "").replace("]", "");
            return schedulers;
        } catch (IOException ignored) {}
        return new String[] { "0" };
    }

}
