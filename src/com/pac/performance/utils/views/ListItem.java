package com.pac.performance.utils.views;

import com.pac.performance.R;
import com.pac.performance.utils.interfaces.Item;
import com.pac.performance.utils.views.HeaderListView.RowType;

import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class ListItem implements Item {
    private final String str1;
    private final Fragment fragment;

    public ListItem(String text1, Fragment fragment) {
        this.str1 = text1;
        this.fragment = fragment;
    }

    @Override
    public int getViewType() {
        return RowType.LIST_ITEM.ordinal();
    }

    @Override
    public View getView(LayoutInflater inflater, View convertView) {
        View view = convertView == null ? (View) inflater.inflate(
                R.layout.list_item, null) : convertView;

        ((TextView) view.findViewById(R.id.list_item_title)).setText(str1);

        return view;
    }

    public Fragment getFragment() {
        return fragment;
    }

    @Override
    public String getTitle() {
        return str1;
    }

    @Override
    public boolean isHeader() {
        return false;
    }
}
