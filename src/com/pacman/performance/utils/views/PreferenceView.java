package com.pacman.performance.utils.views;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;

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

    public static class CustomSpinner extends Preference {

        private List<String> list = new ArrayList<String>();

        public CustomSpinner(Context context, String title, String summary,
                List<String> list) {
            super(context);
            this.list = list;
            if (title != null) setTitle(title);
            if (summary != null) setSummary(summary);
        }

        public CustomSpinner(Context context, String title, String summary,
                String[] list) {
            super(context);
            for (String value : list)
                if (value != null) this.list.add(value);
            if (title != null) setTitle(title);
            if (summary != null) setSummary(summary);
        }

        @Override
        public View getView(View convertView, ViewGroup parent) {
            View view = super.getView(convertView, parent);

            final LinearLayout layout = (LinearLayout) view
                    .findViewById(android.R.id.widget_frame);
            layout.setVisibility(View.VISIBLE);

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                    getContext(), android.R.layout.simple_spinner_item, list);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            Spinner spinner = new Spinner(getContext());
            spinner.setAdapter(adapter);

            layout.addView(spinner);

            return view;
        }

    }

}
