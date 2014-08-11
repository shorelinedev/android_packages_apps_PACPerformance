package com.pac.performance.helpers;

import android.app.Activity;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceCategory;

public class PreferenceHelper {

    public PreferenceCategory setPreferenceCategory(String title,
            Activity activity) {
        PreferenceCategory mCategory = new PreferenceCategory(activity);
        if (title != null) mCategory.setTitle(title);
        return mCategory;
    }

    public Preference setPreference(String title, String summary,
            Activity activity) {
        Preference mPref = new Preference(activity);
        if (title != null) mPref.setTitle(title);
        if (summary != null) mPref.setSummary(summary);
        return mPref;
    }

    public CheckBoxPreference setCheckBoxPreference(boolean checked,
            String title, String summary, Activity activity) {
        CheckBoxPreference mBox = new CheckBoxPreference(activity);
        mBox.setChecked(checked);
        if (title != null) mBox.setTitle(title);
        if (summary != null) mBox.setSummary(summary);
        return mBox;
    }
}
