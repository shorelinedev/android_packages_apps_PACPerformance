package com.pacman.performance.utils.views;

import com.pacman.performance.R;
import com.pacman.performance.utils.views.HeaderListView.RowType;

import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class ListItems {

	public interface Item {
		public int getViewType();

		public Fragment getFragment();

		public String getTitle();

		public boolean isHeader();

		public View getView(LayoutInflater inflater, View convertView);
	}

	public static class ListItem implements Item {

		private final String text;
		private final Fragment fragment;

		public ListItem(String text, Fragment fragment) {
			this.text = text;
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
		public boolean isHeader() {
			return false;
		}
	}
}
