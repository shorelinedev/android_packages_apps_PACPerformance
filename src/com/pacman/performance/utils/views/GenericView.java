package com.pacman.performance.utils.views;

import java.util.List;

import android.app.Fragment;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public class GenericView {

    public interface Item {

        public Fragment getFragment();

        public String getTitle();

        public String getSummary();

        public boolean isHeader();

        public View getView(LayoutInflater inflater, View convertView);
    }

    public static class GenericAdapter extends ArrayAdapter<Item> {

        private LayoutInflater mInflater;

        public GenericAdapter(Context context, List<Item> items) {
            super(context, 0, items);
            mInflater = LayoutInflater.from(context);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return getItem(position).getView(mInflater, convertView);
        }
    }
}
