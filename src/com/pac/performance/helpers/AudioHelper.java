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

public class AudioHelper implements Constants {

    public static final String[] FAUX_SOUND = { FAUX_SOUND_CONTROL,
            FAUX_HEADPHONE_GAIN, FAUX_HANDSET_MIC_GAIN, FAUX_CAM_MIC_GAIN,
            FAUX_SPEAKER_GAIN, FAUX_HEADPHONE_PA_GAIN };

    public static int getFauxSoundControlValues(int count) {
        try {
            if (FAUX_SOUND[count].equals(FAUX_HEADPHONE_GAIN)) return Integer
                    .parseInt(Utils.readLine(FAUX_HEADPHONE_GAIN).split(" ")[0]) - 40;
            else if (FAUX_SOUND[count].equals(FAUX_HEADPHONE_PA_GAIN)) return Integer
                    .parseInt(Utils.readLine(FAUX_HEADPHONE_PA_GAIN).split(" ")[0]) - 12;
            else return Integer.parseInt(Utils.readLine(FAUX_SOUND[count])) - 40;
        } catch (NumberFormatException ignored) {} catch (IOException ignored) {}
        return 0;
    }
}
