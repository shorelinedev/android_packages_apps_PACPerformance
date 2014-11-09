package com.pacman.performance.helpers;

import com.pacman.performance.utils.Constants;

public class ScreenHelper implements Constants {

    private String SCREEN_CALIBRATION;
    private String SCREEN_CALIBRATION_CTRL;

    public String[] getLimits() {
        String[] values = new String[226];
        for (int i = 0; i < values.length; i++)
            values[i] = String.valueOf(i + 30);
        return values;
    }

    public String getColorCalibrationCtrlFile() {
        return SCREEN_CALIBRATION_CTRL == null ? "" : SCREEN_CALIBRATION_CTRL;
    }

    public String getColorCalibrationFile() {
        return SCREEN_CALIBRATION == null ? "" : SCREEN_CALIBRATION;
    }

    public String[] getColorCalibration() {
        if (SCREEN_CALIBRATION != null)
            if (mUtils.existFile(SCREEN_CALIBRATION)) {
                String value = mUtils.readFile(SCREEN_CALIBRATION);
                if (value != null)
                    return new String[] { value.split(" ")[0],
                            value.split(" ")[1], value.split(" ")[2] };
            }
        return null;
    }

    public boolean hasColorCalibrationCtrl() {
        if (SCREEN_CALIBRATION_CTRL == null)
            for (String file : SCREEN_KCAL_CTRL_ARRAY)
                if (mUtils.existFile(file)) SCREEN_CALIBRATION_CTRL = file;
        return SCREEN_CALIBRATION_CTRL != null;
    }

    public boolean hasColorCalibration() {
        if (SCREEN_CALIBRATION == null) for (String file : SCREEN_KCAL_ARRAY)
            if (mUtils.existFile(file)) SCREEN_CALIBRATION = file;
        return SCREEN_CALIBRATION != null;
    }

    public boolean hasScreen() {
        for (String[] array : SCREEN_ARRAY)
            for (String file : array)
                return mUtils.existFile(file);
        return false;
    }

}
