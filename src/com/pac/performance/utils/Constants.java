package com.pac.performance.utils;

public interface Constants {
    
    public static final String TAG = "PACPerformance";

    // CPU
    public static final String INTELLIPLUG_ECO_MODE = "/sys/module/intelli_plug/parameters/eco_mode_active";
    public static final String INTELLIPLUG = "/sys/module/intelli_plug/parameters/intelli_plug_active";
    public static final String CUR_GOVERNOR = "/sys/devices/system/cpu/cpu0/cpufreq/scaling_governor";
    public static final String AVAILABLE_GOVERNOR = "/sys/devices/system/cpu/cpu0/cpufreq/scaling_available_governors";
    public static final String MIN_SCREEN_ON = "/sys/devices/system/cpu/cpu0/cpufreq/screen_on_min_freq";
    public static final String MAX_SCREEN_OFF = "/sys/devices/system/cpu/cpu0/cpufreq/screen_off_max_freq";
    public static final String MIN_FREQ = "/sys/devices/system/cpu/cpu0/cpufreq/scaling_min_freq";
    public static final String MAX_FREQ = "/sys/devices/system/cpu/cpu0/cpufreq/scaling_max_freq";
    public static final String AVAILABLE_FREQ = "/sys/devices/system/cpu/cpu0/cpufreq/stats/time_in_state";
    public static final String CORE_STAT = "/sys/devices/system/cpu/cpupresent/online";
    public static final String FREQUENCY_SCALING = "/sys/devices/system/cpu/cpupresent/cpufreq/scaling_cur_freq";
    public static final String CORE_VALUE = "/sys/devices/system/cpu/present";

    // Battery
    public static final String BLX = "/sys/devices/virtual/misc/batterylifeextender/charging_limit";
    public static final String FAST_CHARGE = "/sys/kernel/fast_charge/force_fast_charge";
    public static final String BATTERY_VOLTAGE = "/sys/class/power_supply/battery/voltage_now";

    // Audio
    public static final String FAUX_HEADPHONE_PA_GAIN = "/sys/kernel/sound_control/gpl_headphone_pa_gain";
    public static final String FAUX_SPEAKER_GAIN = "/sys/kernel/sound_control/gpl_speaker_gain";
    public static final String FAUX_CAM_MIC_GAIN = "/sys/kernel/sound_control/gpl_cam_mic_gain";
    public static final String FAUX_HANDSET_MIC_GAIN = "/sys/kernel/sound_control/gpl_mic_gain";
    public static final String FAUX_HEADPHONE_GAIN = "/sys/kernel/sound_control/gpl_headphone_gain";
    public static final String FAUX_SOUND_CONTROL = "/sys/kernel/sound_control";

    // Voltage
    public static final String FAUX_VOLTAGE = "/sys/devices/system/cpu/cpufreq/vdd_table/vdd_levels";
    public static final String CPU_VOLTAGE = "/sys/devices/system/cpu/cpu0/cpufreq/UV_mV_table";

    // IO
    public static final String EXTERNAL_READ = "/sys/block/mmcblk1/queue/read_ahead_kb";
    public static final String INTERNAL_READ = "/sys/block/mmcblk0/queue/read_ahead_kb";
    public static final String EXTERNAL_SCHEDULER = "/sys/block/mmcblk1/queue/scheduler";
    public static final String INTERNAL_SCHEDULER = "/sys/block/mmcblk0/queue/scheduler";

    // MinFree
    public static final String MINFREE = "/sys/module/lowmemorykiller/parameters/minfree";

    // VM
    public static final String VM_PATH = "/proc/sys/vm";

}
