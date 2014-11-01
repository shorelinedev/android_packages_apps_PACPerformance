package com.pacman.performance.utils.views;

import com.pacman.performance.R;
import com.pacman.performance.utils.views.GenericView.Item;

import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class ListItem implements Item {

    private final String text;
    private final Fragment fragment;

    public ListItem(String text, Fragment fragment) {
        this.text = text;
        this.fragment = fragment;
    }

    @Override
    public View getView(LayoutInflater inflater, View convertView) {
        View view = (View) inflater.inflate(R.layout.list_item, null);

        if (view != null)
            ((TextView) view.findViewById(R.id.list_item_title)).setText(text);

        return view;
    }

    public Fragment getFragment() {
        return fragment;
    }

    @Override
    public String getTitle() {
        return text;
    }

    @Override
    public String getSummary() {
        return null;
    }

    @Override
    public boolean isHeader() {
        return false;
    }

}
