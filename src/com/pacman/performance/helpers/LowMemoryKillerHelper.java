package com.pacman.performance.helpers;

import com.pacman.performance.utils.Constants;

public class LowMemoryKillerHelper implements Constants {

    public int getMinFree(int position) {
        if (mUtils.existFile(LMK_MINFREE)) {
            String value = mUtils.readFile(LMK_MINFREE);
            if (value != null) return Integer
                    .parseInt(value.split(",")[position]);
        }
        return 0;
    }

    public String[] getMinFrees() {
        if (mUtils.existFile(LMK_MINFREE)) {
            String value = mUtils.readFile(LMK_MINFREE);
            if (value != null) return value.split(",");
        }
        return null;
    }

}
