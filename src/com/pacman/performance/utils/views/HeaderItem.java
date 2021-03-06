package com.pacman.performance.utils.views;

import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.pacman.performance.R;
import com.pacman.performance.utils.views.GenericView.Item;

public class HeaderItem implements Item {
    private final String name;

    public HeaderItem(String name) {
        this.name = name;
    }

    @Override
    public View getView(LayoutInflater inflater, View convertView) {
        View view = (View) inflater.inflate(R.layout.list_header, null);

        if (view != null)
            ((TextView) view.findViewById(R.id.list_header_title))
                    .setText(name);

        return view;
    }

    @Override
    public Fragment getFragment() {
        return null;
    }

    @Override
    public String getTitle() {
        return name;
    }

    @Override
    public String getSummary() {
        return null;
    }

    @Override
    public boolean isHeader() {
        return true;
    }

}
