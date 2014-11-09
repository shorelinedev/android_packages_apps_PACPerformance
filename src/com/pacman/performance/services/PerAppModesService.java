package com.pacman.performance.services;

import java.util.ArrayList;
import java.util.List;

import com.pacman.performance.R;
import com.pacman.performance.PerAppModeActivity;
import com.pacman.performance.utils.Constants;

import android.app.ActivityManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

public class PerAppModesService extends Service implements Constants {

    private String currentRunning = "";
    private final Handler hand = new Handler();
    private NotificationCompat.Builder mBuilder;
    private NotificationManager mNotificationManager;

    private final String prefName = "perappmodes";
    private final String nameSplit = "wegerhasdasfa";
    private final String cpuMaxSplit = "ksdnfweknwef";
    private final String cpuMinSplit = "geargergerger";
    private final String governorSplit = "sdgerhrehresdf";
    private final String gpuMax2dSplit = "wefewgerhrths";
    private final String gpuMax3dSplit = "fwefewfwegrehh";

    private String profile;
    private String originalMaxCpu;
    private String originalMinCpu;
    private String originalGovernor;
    private String originalMaxGpu2d;
    private String originalMaxGpu3d;

    Runnable run = new Runnable() {
        @Override
        public void run() {
            final ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
            @SuppressWarnings("deprecation")
            List<ActivityManager.RunningTaskInfo> runningAppProcessInfo = am
                    .getRunningTasks(1);
            String running = runningAppProcessInfo.get(0).topActivity
                    .getPackageName();
            if (!currentRunning.equals(running)
                    && !running.equals(getPackageName())) {
                currentRunning = running;

                String savedApps = mUtils.getString(currentRunning, "",
                        PerAppModesService.this);

                if (!savedApps.isEmpty()) {
                    mBuilder.setTicker(getString(R.string.apply_profile,
                            savedApps));
                    mNotificationManager.notify(PER_APP_MODE_ID,
                            mBuilder.build());

                    profile = savedApps;
                    originalMaxCpu = String.valueOf(cpuHelper.getMaxFreq(0));
                    originalMinCpu = String.valueOf(cpuHelper.getMinFreq(0));
                    originalGovernor = cpuHelper.getGovernor(0);
                    originalMaxGpu2d = gpuHelper.hasGpu2dFreqs() ? String
                            .valueOf(gpuHelper.getGpu2dCurFreq()) : "0";
                    originalMaxGpu3d = gpuHelper.hasGpu3dFreqs() ? String
                            .valueOf(gpuHelper.getGpu3dCurFreq()) : "0";

                    String savedProfiles = mUtils.getString(prefName, "",
                            PerAppModesService.this);
                    if (!savedProfiles.isEmpty()) {
                        List<String> contents = new ArrayList<String>();
                        for (String content : savedProfiles
                                .split(gpuMax3dSplit))
                            contents.add(content + gpuMax3dSplit);

                        for (String content : contents)
                            if (savedApps.equals(content.split(nameSplit)[0])) {
                                applyMaxCpu(content.split(nameSplit)[1]
                                        .split(cpuMaxSplit)[0]);
                                applyMinCpu(content.split(cpuMaxSplit)[1]
                                        .split(cpuMinSplit)[0]);
                                applyGovernor(content.split(cpuMinSplit)[1]
                                        .split(governorSplit)[0]);
                                applyMaxGpu2d(content.split(governorSplit)[1]
                                        .split(gpuMax2dSplit)[0]);
                                applyMaxGpu3d(content.split(gpuMax2dSplit)[1]
                                        .split(gpuMax3dSplit)[0]);
                            }
                    }
                } else if (originalMaxCpu != null) {
                    mBuilder.setTicker(getString(R.string.reset_profile_change,
                            profile));
                    mNotificationManager.notify(PER_APP_MODE_ID,
                            mBuilder.build());

                    applyMaxCpu(originalMaxCpu);
                    applyMinCpu(originalMinCpu);
                    applyGovernor(originalGovernor);
                    applyMaxGpu2d(originalMaxGpu2d);
                    applyMaxGpu3d(originalMaxGpu3d);
                    profile = null;
                    originalMaxCpu = null;
                    originalMinCpu = null;
                    originalGovernor = null;
                    originalMaxGpu2d = null;
                    originalMaxGpu3d = null;
                }
            }

            hand.postDelayed(run, 1000);
        }
    };

    private void applyMaxCpu(String freq) {
        apply(CPU_MAX_FREQ, freq, true);
    }

    private void applyMinCpu(String freq) {
        apply(CPU_MIN_FREQ, freq, true);
    }

    private void applyGovernor(String gov) {
        apply(CPU_SCALING_GOVERNOR, gov, true);
    }

    private void applyMaxGpu2d(String freq) {
        apply(gpuHelper.getGpu2dFreqFile(), freq, false);
    }

    private void applyMaxGpu3d(String freq) {
        apply(gpuHelper.getGpu3dFreqFile(), freq, false);
    }

    private void apply(String file, String value, boolean isCpu) {
        if (file == null || value == null) return;

        if (isCpu) {
            if (cpuHelper.getCoreCount() > 1) {
                boolean stoppedMpdec = false;
                if (rootHelper.moduleActive(CPU_MPDEC)) {
                    mCommandControl.stopModule(CPU_MPDEC, false, this);
                    stoppedMpdec = true;
                }

                for (int i = 0; i < cpuHelper.getCoreCount(); i++)
                    // Check if core is online with a while loop
                    while (true)
                        if (mUtils.existFile(String.format(file, i))) {
                            rootHelper.runCommand("echo " + value + " > "
                                    + String.format(file, i));
                            break;
                        }

                if (stoppedMpdec)
                    mCommandControl.startModule(CPU_MPDEC, false, this);
            }
        } else rootHelper.runCommand("echo " + value + " > " + file);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        mUtils.toast(getString(R.string.start_per_app_mode), this);
        hand.post(run);
        buildNotification();
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        mUtils.toast(getString(R.string.stop_per_app_mode), this);
        if (hand != null) hand.removeCallbacks(run);
        if (mNotificationManager != null)
            mNotificationManager.cancel(PER_APP_MODE_ID);
        super.onDestroy();
    }

    private void buildNotification() {
        Intent resultIntent = new Intent(this, PerAppModeActivity.class);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0,
                resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle(getString(R.string.app_name)).setOngoing(true)
                .setContentText(getString(R.string.active_per_app_mode))
                .setContentIntent(resultPendingIntent);
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(PER_APP_MODE_ID, mBuilder.build());
    }

}
