package com.pacman.performance.utils;

import android.os.Environment;

import com.pacman.performance.fragments.BackupFragment;
import com.pacman.performance.fragments.BuildpropFragment;
import com.pacman.performance.fragments.CPUFragment;
import com.pacman.performance.fragments.CPUStatsFragment;
import com.pacman.performance.fragments.CPUVoltageFragment;
import com.pacman.performance.fragments.CustomCommanderFragment;
import com.pacman.performance.fragments.GPUFragment;
import com.pacman.performance.fragments.IOSchedulerFragment;
import com.pacman.performance.fragments.KernelInformationFragment;
import com.pacman.performance.fragments.KernelSamepageMerging;
import com.pacman.performance.fragments.LowMemoryKillerFragment;
import com.pacman.performance.fragments.MemoryStatsFragment;
import com.pacman.performance.fragments.ScreenFragment;
import com.pacman.performance.fragments.TimeInStateFragment;
import com.pacman.performance.fragments.VirtualMachineFragment;
import com.pacman.performance.helpers.CPUHelper;
import com.pacman.performance.helpers.CPUVoltageHelper;
import com.pacman.performance.helpers.GPUHelper;
import com.pacman.performance.helpers.IOHelper;
import com.pacman.performance.helpers.KernelSamepageMergingHelper;
import com.pacman.performance.helpers.LowMemoryKillerHelper;
import com.pacman.performance.helpers.PreferenceHelper;
import com.pacman.performance.helpers.RootHelper;
import com.pacman.performance.helpers.ScreenHelper;
import com.pacman.performance.helpers.VirtualMachineHelper;

public interface Constants {

    public final String TAG = "PACPerformance";
    public final String PREF_NAME = "commands";
    public final String COMMAND_NAME = "names";

    public final String PROC_VERSION = "/proc/version";
    public final String PROC_CPUINFO = "/proc/cpuinfo";
    public final String PROC_MEMINFO = "/proc/meminfo";
    public final String PROC_STAT = "/proc/stat";

    public final String EXTERNAL_STORAGE_DIRECTORY = Environment
            .getExternalStorageDirectory().toString();
    public final String PAC_BACKUP = EXTERNAL_STORAGE_DIRECTORY + "/PACBackup";

    public final int enter_anim = android.R.anim.slide_in_left;
    public final int exit_anim = android.R.anim.slide_out_right;

    public final BackupFragment mBackupFragment = new BackupFragment();
    public final BuildpropFragment mBuildpropFragment = new BuildpropFragment();
    public final CPUFragment mCPUFragment = new CPUFragment();
    public final CPUStatsFragment mCPUStatsFragment = new CPUStatsFragment();
    public final CPUVoltageFragment mCPUVoltageFragment = new CPUVoltageFragment();
    public final CustomCommanderFragment mCustomCommanderFragment = new CustomCommanderFragment();
    public final GPUFragment mGPUFragment = new GPUFragment();
    public final KernelInformationFragment mKernelInformationFragment = new KernelInformationFragment();
    public final KernelSamepageMerging mKernelSamepageMerging = new KernelSamepageMerging();
    public final IOSchedulerFragment mIOSchedulerFragment = new IOSchedulerFragment();
    public final LowMemoryKillerFragment mLowMemoryKillerFragment = new LowMemoryKillerFragment();
    public final MemoryStatsFragment mMemoryStatsFragment = new MemoryStatsFragment();
    public final ScreenFragment mScreenFragment = new ScreenFragment();
    public final TimeInStateFragment mTimeInStateFragment = new TimeInStateFragment();
    public final VirtualMachineFragment mVirtualMachineFragment = new VirtualMachineFragment();

    public final CommandControl mCommandControl = new CommandControl();
    public final Dialog mDialog = new Dialog();
    public final Utils mUtils = new Utils();

    public final CPUHelper cpuHelper = new CPUHelper();
    public final CPUVoltageHelper cpuVoltageHelper = new CPUVoltageHelper();
    public final GPUHelper gpuHelper = new GPUHelper();
    public final ScreenHelper screenHelper = new ScreenHelper();
    public final IOHelper ioHelper = new IOHelper();
    public final KernelSamepageMergingHelper kernelsamepagemergingHelper = new KernelSamepageMergingHelper();
    public final LowMemoryKillerHelper lowmemorykillerHelper = new LowMemoryKillerHelper();
    public final PreferenceHelper prefHelper = new PreferenceHelper();
    public final RootHelper rootHelper = new RootHelper();
    public final VirtualMachineHelper virtualmachineHelper = new VirtualMachineHelper();

    // Services
    public final int NOTI_STATS_ID = 0;
    public final int PER_APP_MODE_ID = 1;

    public final String PARTITON_PATH = "/dev/block/platform";
    public final String BUILD_PROP = "/system/build.prop";

    // CPU
    public final String CPU_CUR_FREQ = "/sys/devices/system/cpu/cpu%d/cpufreq/scaling_cur_freq";
    public final String CPU_CORE_ONLINE = "/sys/devices/system/cpu/cpu%d/online";
    public final String CPU_MAX_FREQ = "/sys/devices/system/cpu/cpu%d/cpufreq/scaling_max_freq";
    public final String CPU_MIN_FREQ = "/sys/devices/system/cpu/cpu%d/cpufreq/scaling_min_freq";
    public final String CPU_TIME_STATE = "/sys/devices/system/cpu/cpu0/cpufreq/stats/time_in_state";
    public final String CPU_SCALING_GOVERNOR = "/sys/devices/system/cpu/cpu%d/cpufreq/scaling_governor";
    public final String CPU_AVAILABLE_GOVERNORS = "/sys/devices/system/cpu/cpu0/cpufreq/scaling_available_governors";
    public final String CPU_GOVERNOR_TUNABLES = "/sys/devices/system/cpu/cpufreq/%s";
    public final String CPU_MC_POWER_SAVING = "/sys/devices/system/cpu/sched_mc_power_savings";
    public final String CPU_MPDECISION_BINARY = "/system/bin/mpdecision";
    public final String CPU_MPDEC = "mpdecision";
    public final String CPU_INTELLI_PLUG = "/sys/module/intelli_plug/parameters/intelli_plug_active";
    public final String CPU_INTELLI_PLUG_ECO = "/sys/module/intelli_plug/parameters/eco_mode_active";

    // CPU Voltage
    public final String CPU_VOLTAGE = "/sys/devices/system/cpu/cpu%d/cpufreq/UV_mV_table";

    // GPU
    public final String GPU_GENERIC_GOVERNORS = "performance powersave ondemand simple";

    public final String GPU_CUR_KGSL2D0_QCOM_FREQ = "/sys/devices/platform/kgsl-2d0.0/kgsl/kgsl-2d0/gpuclk";
    public final String GPU_MAX_KGSL2D0_QCOM_FREQ = "/sys/devices/platform/kgsl-2d0.0/kgsl/kgsl-2d0/max_gpuclk";
    public final String GPU_AVAILABLE_KGSL2D0_QCOM_FREQS = "/sys/devices/platform/kgsl-2d0.0/kgsl/kgsl-2d0/gpu_available_frequencies";
    public final String GPU_SCALING_KGSL2D0_QCOM_GOVERNOR = "/sys/devices/platform/kgsl-2d0.0/kgsl/kgsl-2d0/pwrscale/trustzone/governor";

    public final String GPU_CUR_KGSL3D0_QCOM_FREQ = "/sys/devices/platform/kgsl-3d0.0/kgsl/kgsl-3d0/gpuclk";
    public final String GPU_MAX_KGSL3D0_QCOM_FREQ = "/sys/devices/platform/kgsl-3d0.0/kgsl/kgsl-3d0/max_gpuclk";
    public final String GPU_AVAILABLE_KGSL3D0_QCOM_FREQS = "/sys/devices/platform/kgsl-3d0.0/kgsl/kgsl-3d0/gpu_available_frequencies";
    public final String GPU_SCALING_KGSL3D0_QCOM_GOVERNOR = "/sys/devices/platform/kgsl-3d0.0/kgsl/kgsl-3d0/pwrscale/trustzone/governor";

    public final String GPU_CUR_FDB00000_QCOM_FREQ = "/sys/devices/fdb00000.qcom,kgsl-3d0/kgsl/kgsl-3d0/devfreq/cur_freq";
    public final String GPU_MAX_FDB00000_QCOM_FREQ = "/sys/devices/fdb00000.qcom,kgsl-3d0/kgsl/kgsl-3d0/max_gpuclk";
    public final String GPU_AVAILABLE_FDB00000_QCOM_FREQS = "/sys/devices/fdb00000.qcom,kgsl-3d0/kgsl/kgsl-3d0/gpu_available_frequencies";
    public final String GPU_SCALING_FDB00000_QCOM_GOVERNOR = "/sys/devices/fdb00000.qcom,kgsl-3d0/kgsl/kgsl-3d0/devfreq/governor";
    public final String GPU_AVAILABLE_FDB00000_QCOM_GOVERNORS = "/sys/devices/fdb00000.qcom,kgsl-3d0/kgsl/kgsl-3d0/devfreq/available_governors";

    public final String[] GPU_2D_CUR_FREQ_ARRAY = new String[] { GPU_CUR_KGSL2D0_QCOM_FREQ };

    public final String[] GPU_2D_MAX_FREQ_ARRAY = new String[] { GPU_MAX_KGSL2D0_QCOM_FREQ };

    public final String[] GPU_2D_AVAILABLE_FREQS_ARRAY = new String[] { GPU_AVAILABLE_KGSL2D0_QCOM_FREQS };

    public final String[] GPU_2D_SCALING_GOVERNOR_ARRAY = new String[] { GPU_SCALING_KGSL2D0_QCOM_GOVERNOR };

    public final String[] GPU_3D_CUR_FREQ_ARRAY = new String[] {
            GPU_CUR_KGSL3D0_QCOM_FREQ, GPU_CUR_FDB00000_QCOM_FREQ };

    public final String[] GPU_3D_MAX_FREQ_ARRAY = new String[] {
            GPU_MAX_KGSL3D0_QCOM_FREQ, GPU_MAX_FDB00000_QCOM_FREQ };

    public final String[] GPU_3D_AVAILABLE_FREQS_ARRAY = new String[] {
            GPU_AVAILABLE_KGSL3D0_QCOM_FREQS, GPU_AVAILABLE_FDB00000_QCOM_FREQS };

    public final String[] GPU_3D_SCALING_GOVERNOR_ARRAY = new String[] {
            GPU_SCALING_KGSL3D0_QCOM_GOVERNOR,
            GPU_SCALING_FDB00000_QCOM_GOVERNOR };

    public final String[] GPU_3D_AVAILABLE_GOVERNORS_ARRAY = new String[] { GPU_AVAILABLE_FDB00000_QCOM_GOVERNORS };

    public final String[][] GPU_ARRAY = new String[][] { GPU_2D_CUR_FREQ_ARRAY,
            GPU_2D_MAX_FREQ_ARRAY, GPU_2D_AVAILABLE_FREQS_ARRAY,
            GPU_2D_SCALING_GOVERNOR_ARRAY, GPU_3D_CUR_FREQ_ARRAY,
            GPU_3D_MAX_FREQ_ARRAY, GPU_3D_AVAILABLE_FREQS_ARRAY,
            GPU_3D_SCALING_GOVERNOR_ARRAY };

    // Screen
    public final String SCREEN_KCAL_CTRL = "/sys/devices/platform/kcal_ctrl.0/kcal";
    public final String SCREEN_KCAL_CTRL_CTRL = "/sys/devices/platform/kcal_ctrl.0/kcal_ctrl";

    public final String SCREEN_DIAG0_POWER = "/sys/devices/platform/DIAG0.0/power_rail";
    public final String SCREEN_DIAG0_POWER_CTRL = "/sys/devices/platform/DIAG0.0/power_rail_ctrl";

    public final String[] SCREEN_KCAL_ARRAY = { SCREEN_KCAL_CTRL,
            SCREEN_DIAG0_POWER };

    public final String[] SCREEN_KCAL_CTRL_ARRAY = { SCREEN_KCAL_CTRL_CTRL,
            SCREEN_DIAG0_POWER_CTRL };

    public final String[][] SCREEN_ARRAY = { SCREEN_KCAL_ARRAY,
            SCREEN_KCAL_CTRL_ARRAY };

    // I/O
    public final String IO_INTERNAL_SCHEDULER = "/sys/block/mmcblk0/queue/scheduler";
    public final String IO_EXTERNAL_SCHEDULER = "/sys/block/mmcblk1/queue/scheduler";
    public final String IO_INTERNAL_SCHEDULER_TUNABLE = "/sys/block/mmcblk0/queue/iosched";
    public final String IO_EXTERNAL_SCHEDULER_TUNABLE = "/sys/block/mmcblk1/queue/iosched";
    public final String IO_INTERNAL_READ_AHEAD = "/sys/block/mmcblk0/queue/read_ahead_kb";
    public final String IO_EXTERNAL_READ_AHEAD = "/sys/block/mmcblk1/queue/read_ahead_kb";

    // Kernel Samepage Merging
    public final String KSM_FOLDER = "/sys/kernel/mm/ksm";
    public final String KSM_FULL_SCANS = KSM_FOLDER + "/full_scans";
    public final String KSM_PAGES_SHARED = KSM_FOLDER + "/pages_shared";
    public final String KSM_PAGES_SHARING = KSM_FOLDER + "/pages_sharing";
    public final String KSM_PAGES_UNSHARED = KSM_FOLDER + "/pages_unshared";
    public final String KSM_PAGES_VOLATILE = KSM_FOLDER + "/pages_volatile";
    public final String KSM_RUN = KSM_FOLDER + "/run";
    public final String KSM_PAGES_TO_SCAN = KSM_FOLDER + "/pages_to_scan";
    public final String KSM_SLEEP_MILLISECONDS = KSM_FOLDER
            + "/sleep_millisecs";

    public final String[] KSM_INFOS = new String[] { KSM_FULL_SCANS,
            KSM_PAGES_SHARED, KSM_PAGES_SHARING, KSM_PAGES_UNSHARED,
            KSM_PAGES_VOLATILE, };

    // Low Memory Killer
    public final String LMK_MINFREE = "/sys/module/lowmemorykiller/parameters/minfree";

    // Virtual Machine
    public final String VM_PATH = "/proc/sys/vm";

    public final String TMP_FILE = "/data/local/pacperformance.tmp";
}
