package com.pac.performance.helpers;

import com.pac.performance.utils.Constants;

public class IOHelper implements Constants {

    public enum StorageType {
        INTERNAL, EXTERNAL
    }

    public int getReadahead(StorageType type) {
        String file = type == StorageType.INTERNAL ? IO_INTERNAL_READ_AHEAD
                : IO_EXTERNAL_READ_AHEAD;
        if (mUtils.existFile(file)) {
            String values = mUtils.readFile(file);
            if (values != null) return Integer.parseInt(values);
        }
        return 0;
    }

    public String[] getSchedulers(StorageType type) {
        String file = type == StorageType.INTERNAL ? IO_INTERNAL_SCHEDULER
                : IO_EXTERNAL_SCHEDULER;
        if (mUtils.existFile(file)) {
            String values = mUtils.readFile(file);
            if (values != null) {
                String[] valueArray = values.split(" ");
                String[] out = new String[valueArray.length];

                for (int i = 0; i < valueArray.length; i++)
                    out[i] = valueArray[i].replace("[", "").replace("]", "");

                return out;
            }
        }
        return null;
    }

    public String getScheduler(StorageType type) {
        String file = type == StorageType.INTERNAL ? IO_INTERNAL_SCHEDULER
                : IO_EXTERNAL_SCHEDULER;
        if (mUtils.existFile(file)) {
            String values = mUtils.readFile(file);
            if (values != null) {
                String[] valueArray = values.split(" ");

                for (String value : valueArray)
                    if (value.contains("[")) return value.replace("[", "")
                            .replace("]", "");
            }
        }
        return "";
    }

    public boolean hasExternalStorage() {
        return mUtils.existFile(IO_EXTERNAL_READ_AHEAD)
                || mUtils.existFile(IO_EXTERNAL_SCHEDULER);
    }

}
