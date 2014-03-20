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

public class BatteryHelper implements Constants {

    public static int getBLX() {
        if (Utils.exist(BLX))
            try {
                return Integer.parseInt(Utils.readLine(BLX));
            } catch (NumberFormatException ignored) {
            } catch (IOException ignored) {
            }
        return 0;
    }

    public static boolean getFastCharge() {
        if (Utils.exist(FAST_CHARGE))
            try {
                return Utils.readLine(FAST_CHARGE).equals("1");
            } catch (IOException ignored) {
            }
        return false;
    }

    public static int getCurBatteryVoltage() {
        if (Utils.exist(BATTERY_VOLTAGE))
            try {
                return Integer.parseInt(Utils.readLine(BATTERY_VOLTAGE));
            } catch (NumberFormatException ignored) {
            } catch (IOException ignored) {
            }
        return 0;
    }
}
