package com.pacman.performance.helpers;

import com.pacman.performance.utils.Constants;

public class KernelSamepageMergingHelper implements Constants {

    public int getSleepMilliseconds() {
        if (mUtils.existFile(KSM_SLEEP_MILLISECONDS)) {
            String value = mUtils.readFile(KSM_SLEEP_MILLISECONDS);
            if (value != null) return Integer.parseInt(value);
        }
        return 0;
    }

    public int getPagesToScan() {
        if (mUtils.existFile(KSM_PAGES_TO_SCAN)) {
            String value = mUtils.readFile(KSM_PAGES_TO_SCAN);
            if (value != null) return Integer.parseInt(value);
        }
        return 0;
    }

    public boolean isKsmActive() {
        if (mUtils.existFile(KSM_RUN)) {
            String value = mUtils.readFile(KSM_RUN);
            if (value != null) return value.equals("1");
        }
        return false;
    }

    public int getInfos(int position) {
        if (mUtils.existFile(KSM_INFOS[position])) {
            String value = mUtils.readFile(KSM_INFOS[position]);
            if (value != null) return Integer.parseInt(value);
        }
        return 0;
    }

    public boolean hasKsm() {
        return mUtils.existFile(KSM_FOLDER);
    }

}
