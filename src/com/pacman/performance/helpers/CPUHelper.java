package com.pacman.performance.helpers;

import java.util.ArrayList;
import java.util.List;

import com.pacman.performance.utils.Constants;

public class CPUHelper implements Constants {

    private String[] mCpuFreqs = null;
    private String[] mCpuGovernors = null;

    public boolean isIntelliPlugEcoActive() {
        return mUtils.readFile(CPU_INTELLI_PLUG_ECO).equals("1");
    }

    public boolean hasIntelliPlugEco() {
        return mUtils.existFile(CPU_INTELLI_PLUG_ECO);
    }

    public boolean isIntelliPlugActive() {
        return mUtils.readFile(CPU_INTELLI_PLUG).equals("1");
    }

    public boolean hasIntelliPlug() {
        return mUtils.existFile(CPU_INTELLI_PLUG);
    }

    public boolean isMpdecisionActive() {
        return rootHelper.moduleActive(CPU_MPDEC);
    }

    public boolean hasMpdecision() {
        return mUtils.existFile(CPU_MPDECISION_BINARY);
    }

    public boolean hasMcPowerSaving() {
        return mUtils.existFile(CPU_MC_POWER_SAVING);
    }

    public int getMcPowerSaving() {
        if (mUtils.existFile(CPU_MC_POWER_SAVING)) {
            String value = mUtils.readFile(CPU_MC_POWER_SAVING);
            if (value != null) return Integer.parseInt(value);
        }
        return 0;
    }

    public String[] getCpuGovernors() {
        if (mCpuGovernors == null) if (mUtils
                .existFile(CPU_AVAILABLE_GOVERNORS)) mCpuGovernors = mUtils
                .readFile(CPU_AVAILABLE_GOVERNORS).split(" ");
        return mCpuGovernors;
    }

    public String getGovernor(int core) {
        if (mUtils.existFile(String.format(CPU_SCALING_GOVERNOR, core))) {
            String value = mUtils.readFile(String.format(CPU_SCALING_GOVERNOR,
                    core));
            if (value != null) return value;
        }
        return "";
    }

    public String[] getCpuFreqs() {
        if (mCpuFreqs == null) if (mUtils.existFile(CPU_TIME_STATE)) {
            String values = mUtils.readFile(CPU_TIME_STATE);
            if (values != null) {
                String[] valueArray = values.split("\\r?\\n");
                mCpuFreqs = new String[valueArray.length];
                for (int i = 0; i < valueArray.length; i++)
                    mCpuFreqs[i] = valueArray[i].split(" ")[0];

                if (Integer.parseInt(mCpuFreqs[0]) > Integer
                        .parseInt(mCpuFreqs[mCpuFreqs.length - 1])) {
                    List<String> freqs = new ArrayList<String>();
                    for (int x = mCpuFreqs.length - 1; x >= 0; x--)
                        freqs.add(mCpuFreqs[x]);
                    for (int i = 0; i < mCpuFreqs.length; i++)
                        mCpuFreqs[i] = freqs.get(i);
                }
            }
        }
        return mCpuFreqs;
    }

    public int getMinFreq(int core) {
        if (mUtils.existFile(String.format(CPU_MIN_FREQ, core))) {
            String value = mUtils.readFile(String.format(CPU_MIN_FREQ, core));
            if (value != null) return Integer.parseInt(value);
        }
        return 0;
    }

    public int getMaxFreq(int core) {
        if (mUtils.existFile(String.format(CPU_MAX_FREQ, core))) {
            String value = mUtils.readFile(String.format(CPU_MAX_FREQ, core));
            if (value != null) return Integer.parseInt(value);
        }
        return 0;
    }

    public int getCurFreq(int core) {
        if (mUtils.existFile(String.format(CPU_CUR_FREQ, core))) {
            String value = mUtils.readFile(String.format(CPU_CUR_FREQ, core));
            if (value != null) return Integer.parseInt(value);
        }
        return 0;
    }

    public int getCoreCount() {
        return Runtime.getRuntime().availableProcessors();
    }
}
