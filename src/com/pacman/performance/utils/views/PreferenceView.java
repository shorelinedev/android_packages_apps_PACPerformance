package com.pacman.performance.utils.views;

import android.content.Context;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceCategory;

public class PreferenceView {

    public static class CustomCategory extends PreferenceCategory {

        public CustomCategory(Context context, String title) {
            super(context);
            if (title != null) setTitle(title);
        }

    }

    public static class CustomPreference extends Preference {

        public CustomPreference(Context context, String title, String summary) {
            super(context);
            if (title != null) setTitle(title);
            if (summary != null) setSummary(summary);
        }

    }

    public static class CustomCheckBox extends CheckBoxPreference {

        public CustomCheckBox(Context context, boolean checked, String title,
                String summary) {
            super(context);
            setChecked(checked);
            if (title != null) setTitle(title);
            if (summary != null) setSummary(summary);
        }

    }

}
