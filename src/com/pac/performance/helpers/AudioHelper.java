package com.pac.performance.helpers;

import java.io.IOException;

import com.pac.performance.utils.Utils;

public class AudioHelper {

	public static final String FAUX_HEADPHONE_PA_GAIN = "/sys/kernel/sound_control/gpl_headphone_pa_gain";
	public static final String FAUX_SPEAKER_GAIN = "/sys/kernel/sound_control/gpl_speaker_gain";
	public static final String FAUX_CAM_MIC_GAIN = "/sys/kernel/sound_control/gpl_cam_mic_gain";
	public static final String FAUX_HANDSET_MIC_GAIN = "/sys/kernel/sound_control/gpl_mic_gain";
	public static final String FAUX_HEADPHONE_GAIN = "/sys/kernel/sound_control/gpl_headphone_gain";
	public static final String FAUX_SOUND_CONTROL = "/sys/kernel/sound_control";

	public static final String[] FAUX_SOUND = { FAUX_SOUND_CONTROL,
			FAUX_HEADPHONE_GAIN, FAUX_HANDSET_MIC_GAIN, FAUX_CAM_MIC_GAIN,
			FAUX_SPEAKER_GAIN, FAUX_HEADPHONE_PA_GAIN };

	public static int getFauxSoundControlValues(int count) {
		try {
			if (FAUX_SOUND[count].equals(FAUX_HEADPHONE_GAIN))
				return Integer.parseInt(Utils.readLine(FAUX_HEADPHONE_GAIN)
						.split(" ")[0]) - 40;
			else if (FAUX_SOUND[count].equals(FAUX_HEADPHONE_PA_GAIN))
				return Integer.parseInt(Utils.readLine(FAUX_HEADPHONE_PA_GAIN)
						.split(" ")[0]) - 12;
			else
				return Integer.parseInt(Utils.readLine(FAUX_SOUND[count])) - 40;
		} catch (NumberFormatException e) {
		} catch (IOException e) {
		}
		return 0;
	}
}
