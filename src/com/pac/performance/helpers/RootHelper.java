package com.pac.performance.helpers;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

import android.util.Log;

import com.pac.performance.utils.Constants;
import com.stericson.roottools.CommandCapture;
import com.stericson.roottools.RootDeniedException;
import com.stericson.roottools.RootTools;

public class RootHelper implements Constants {

    public enum PartitionType {
        BOOT, RECOVERY, FOTA
    }

    private String bootPartition;
    private String recoveryPartition;
    private String fotaPartition;

    private String[] bootPartitionNames = new String[] { "boot", "kernel", "LX" };
    private String[] recoveryPartitionNames = new String[] { "recovery", "SS" };
    private String[] fotaPartitionNames = new String[] { "FOTAKernel" };

    public void runCommand(String command) {
        try {
            RootTools.getShell().add(new CommandCapture(0, command))
                    .commandCompleted(0, 0);
            if (command.contains("/cache/tmp")) RootTools.getShell()
                    .add(new CommandCapture(0, "chmod 777 /cache"))
                    .commandCompleted(0, 0);
            Log.d(TAG, command);
        } catch (IOException e) {
            Log.e(TAG, "failed to run " + command);
        } catch (TimeoutException ignored) {
            Log.e(TAG, "Timeout: Cannot gain root access");
        } catch (RootDeniedException e) {
            Log.e(TAG, "Root access denied");
        }
    }

    public void writePartition(String path, String partition) {
        runCommand("dd if=" + path + " of=" + partition);
    }

    public void getPartition(String path, String partition) {
        runCommand("dd if=" + partition + " of=" + path);
    }

    public void mount(boolean writeable, String mountpoint) {
        runCommand(writeable ? "mount -o rw,remount " + mountpoint
                : "mount -o ro,remount " + mountpoint);
    }

    public String getPartitionName(PartitionType partition) {
        if ((partition == PartitionType.BOOT && bootPartition == null)
                || (partition == PartitionType.RECOVERY && recoveryPartition == null)
                || (partition == PartitionType.FOTA && fotaPartition == null)) {

            String[] partitionNames = null;
            switch (partition) {
                case BOOT:
                    partitionNames = bootPartitionNames;
                    break;
                case RECOVERY:
                    partitionNames = recoveryPartitionNames;
                    break;
                case FOTA:
                    partitionNames = fotaPartitionNames;
                    break;
            }

            if (partitionNames != null) {
                File[] platforms = new File(mUtils.existFile(PARTITON_PATH
                        + "/omap") ? PARTITON_PATH + "/omap" : PARTITON_PATH)
                        .listFiles();
                for (File platform : platforms)
                    if (platform.isDirectory()) for (File emmc : platform
                            .listFiles())
                        if (emmc.getName().equals("by-name")) for (File part : emmc
                                .listFiles())
                            for (String names : partitionNames)
                                if (names.equals(part.getName())
                                        || names.toUpperCase().equals(
                                                part.getName())) {
                                    if (partition == PartitionType.BOOT) bootPartition = part
                                            .getAbsolutePath();
                                    if (partition == PartitionType.RECOVERY) recoveryPartition = part
                                            .getAbsolutePath();
                                    if (partition == PartitionType.FOTA) fotaPartition = part
                                            .getAbsolutePath();
                                }
            }
        }

        if (partition == PartitionType.BOOT) return bootPartition;
        if (partition == PartitionType.RECOVERY) return recoveryPartition;
        if (partition == PartitionType.FOTA) return fotaPartition;
        return null;
    }
}
