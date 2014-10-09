package com.pac.performance.utils.views;

import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.pac.performance.R;
import com.pac.performance.utils.views.HeaderListView.RowType;
import com.pac.performance.utils.views.ListItems.Item;

public class HeaderItem implements Item {
    private final String name;

    public HeaderItem(String name) {
        this.name = name;
    }

    @Override
    public int getViewType() {
        return RowType.HEADER_ITEM.ordinal();
    }

    @Override
    public View getView(LayoutInflater inflater, View convertView) {
        View view = convertView == null ? (View) inflater.inflate(
                R.layout.list_header, null) : convertView;

        ((TextView) view.findViewById(R.id.list_header_title)).setText(name);

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
    public boolean isHeader() {
        return true;
    }

}
