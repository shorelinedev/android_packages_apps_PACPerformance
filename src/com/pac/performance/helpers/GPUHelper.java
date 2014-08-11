package com.pac.performance.helpers;

import com.pac.performance.utils.Constants;

public class GPUHelper implements Constants {

    private String GPU_CUR_FREQ;
    public String GPU_MAX_FREQ;
    private String GPU_AVAILABLE_FREQS;
    public String GPU_SCALING_GOVERNOR;
    private String[] GPU_AVAILABLE_GOVERNORS;

    public String[] mGpuFreqs;

    public String[] getGpuGovernors() {
        if (GPU_AVAILABLE_GOVERNORS == null) for (String file : GPU_AVAILABLE_GOVERNORS_ARRAY)
            if (GPU_AVAILABLE_GOVERNORS == null) if (mUtils.existFile(file)) {
                String value = mUtils.readFile(file);
                if (value != null) GPU_AVAILABLE_GOVERNORS = value.split(" ");
            }
        return GPU_AVAILABLE_GOVERNORS == null ? GPU_GENERIC_GOVERNORS
                .split(" ") : GPU_AVAILABLE_GOVERNORS;
    }

    public String getGpuGovernor() {
        if (GPU_SCALING_GOVERNOR != null) if (mUtils
                .existFile(GPU_SCALING_GOVERNOR)) {
            String value = mUtils.readFile(GPU_SCALING_GOVERNOR);
            if (value != null) return value;
        }
        return "";
    }

    public boolean hasGpuGovernor() {
        if (GPU_SCALING_GOVERNOR == null) for (String file : GPU_SCALING_GOVERNOR_ARRAY)
            if (mUtils.existFile(file)) GPU_SCALING_GOVERNOR = file;
        return GPU_SCALING_GOVERNOR != null;
    }

    public String[] getGpuFreqs() {
        if (GPU_AVAILABLE_FREQS != null) if (mGpuFreqs == null) if (mUtils
                .existFile(GPU_AVAILABLE_FREQS)) {
            String value = mUtils.readFile(GPU_AVAILABLE_FREQS);
            if (value != null) return value.split(" ");
        }
        return mGpuFreqs;
    }

    public boolean hasGpuFreqs() {
        if (GPU_AVAILABLE_FREQS == null) {
            for (String file : GPU_AVAILABLE_FREQS_ARRAY)
                if (mUtils.existFile(file)) GPU_AVAILABLE_FREQS = file;
        }
        return GPU_AVAILABLE_FREQS != null;
    }

    public int getGpuMaxFreq() {
        if (GPU_MAX_FREQ != null) if (mUtils.existFile(GPU_MAX_FREQ)) {
            String value = mUtils.readFile(GPU_MAX_FREQ);
            if (value != null) return Integer.parseInt(value);
        }
        return 0;
    }

    public boolean hasGpuMaxFreq() {
        if (GPU_MAX_FREQ == null) {
            for (String file : GPU_MAX_FREQ_ARRAY)
                if (mUtils.existFile(file)) GPU_MAX_FREQ = file;
        }
        return GPU_MAX_FREQ != null;
    }

    public int getGpuCurFreq() {
        if (GPU_CUR_FREQ != null) if (mUtils.existFile(GPU_CUR_FREQ)) {
            String value = mUtils.readFile(GPU_CUR_FREQ);
            if (value != null) return Integer.parseInt(value);
        }
        return 0;
    }

    public boolean hasGpuCurFreq() {
        if (GPU_CUR_FREQ == null) {
            for (String file : GPU_CUR_FREQ_ARRAY)
                if (mUtils.existFile(file)) GPU_CUR_FREQ = file;
        }
        return GPU_CUR_FREQ != null;
    }

}
