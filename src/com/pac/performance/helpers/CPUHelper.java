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

public class CPUHelper implements Constants {

    public static boolean getIntelliPlugEcoMode() {
        if (Utils.exist(INTELLIPLUG_ECO_MODE)) try {
            return Utils.readLine(INTELLIPLUG_ECO_MODE).equals("1");
        } catch (IOException ignored) {}
        return false;
    }

    public static boolean getIntelliPlug() {
        if (Utils.exist(INTELLIPLUG)) try {
            return Utils.readLine(INTELLIPLUG).equals("1");
        } catch (IOException ignored) {}
        return false;
    }

    public static String getCurGovernor() {
        if (Utils.exist(CUR_GOVERNOR)) try {
            return Utils.readLine(CUR_GOVERNOR);
        } catch (NumberFormatException ignored) {} catch (IOException ignored) {}
        return "";
    }

    public static String[] getAvailableGovernor() {
        if (Utils.exist(AVAILABLE_GOVERNOR)) try {
            return Utils.readLine(AVAILABLE_GOVERNOR).split(" ");
        } catch (IOException ignored) {}
        return new String[] { "" };
    }

    public static int getMinScreenOnFreq() {
        if (Utils.exist(MIN_SCREEN_ON)) try {
            return Integer.valueOf(Utils.readLine(MIN_SCREEN_ON));
        } catch (NumberFormatException ignored) {} catch (IOException ignored) {}
        return 0;
    }

    public static int getMaxScreenOffFreq() {
        if (Utils.exist(MAX_SCREEN_OFF)) try {
            return Integer.valueOf(Utils.readLine(MAX_SCREEN_OFF));
        } catch (NumberFormatException ignored) {} catch (IOException ignored) {}
        return 0;
    }

    public static int getMinFreq() {
        if (Utils.exist(MIN_FREQ)) try {
            return Integer.valueOf(Utils.readLine(MIN_FREQ));
        } catch (NumberFormatException ignored) {} catch (IOException ignored) {}
        return 0;
    }

    public static int getMaxFreq() {
        if (Utils.exist(MAX_FREQ)) try {
            return Integer.parseInt(Utils.readLine(MAX_FREQ));
        } catch (NumberFormatException ignored) {} catch (IOException ignored) {}
        return 0;
    }

    public static Integer[] getAvailableFreq() {
        if (Utils.exist(AVAILABLE_FREQ)) try {
            String[] value = Utils.readBlock(AVAILABLE_FREQ).split("\\r?\\n");
            Integer[] freqs = new Integer[value.length];
            for (int i = 0; i < value.length; i++)
                freqs[i] = Integer.valueOf(value[i].split(" ")[0]);
            return freqs;
        } catch (IOException ignored) {}
        return new Integer[] { 0 };
    }

    public static boolean getCoreOnline(int core) {
        if (Utils.exist(CORE_STAT.replace("present", String.valueOf(core)))) try {
            return Utils.readLine(
                    CORE_STAT.replace("present", String.valueOf(core))).equals(
                    "1");

        } catch (IOException ignored) {}
        return true;
    }

    public static int getFreqScaling(int core) {
        try {
            return Utils.exist(FREQUENCY_SCALING.replace("present",
                    String.valueOf(core))) ? Integer.valueOf(Utils
                    .readLine(FREQUENCY_SCALING.replace("present",
                            String.valueOf(core)))) : 0;

        } catch (IOException ignored) {}
        return 0;
    }

    public static int getCoreCount() {
        if (Utils.exist(CORE_VALUE)) try {
            String output = Utils.readLine(CORE_VALUE);
            if (!output.equals("0")) return Integer
                    .parseInt(output.split("-")[1]) + 1;
            else return 1;

        } catch (IOException ignored) {}
        return 0;
    }
}
