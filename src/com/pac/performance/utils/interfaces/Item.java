package com.pac.performance.utils.interfaces;

import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;

public interface Item {
    public int getViewType();

    public Fragment getFragment();

    public String getTitle();

    public boolean isHeader();

    public View getView(LayoutInflater inflater, View convertView);
}
