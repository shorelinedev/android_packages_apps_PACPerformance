package com.pacman.performance.utils.views;

import java.util.List;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.pacman.performance.R;

public class GenericListView extends ArrayAdapter<String> {

    private final Activity activity;
    private final List<String> filesString;
    private final List<String> values;

    public GenericListView(Activity activity, List<String> filesString,
            List<String> values) {
        super(activity, R.layout.list_generic, filesString);
        this.activity = activity;
        this.filesString = filesString;
        this.values = values;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (activity != null) {
            LayoutInflater inflater = activity.getLayoutInflater();
            View rowView = inflater.inflate(R.layout.list_generic, null, true);
            TextView title = (TextView) rowView.findViewById(R.id.list_title);
            TextView value = (TextView) rowView.findViewById(R.id.list_value);
            if (filesString.size() > 0)
                title.setText(filesString.get(position));
            if (values.size() > 0) value.setText(values.get(position));

            return rowView;
        }
        return null;
    }
}
