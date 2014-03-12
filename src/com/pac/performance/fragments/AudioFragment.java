package com.pac.performance.fragments;

import com.pac.performance.R;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class AudioFragment extends Fragment {

	private static Context context;

	public static LinearLayout layout = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		context = getActivity();
		View rootView = inflater.inflate(R.layout.generic, container, false);
		layout = (LinearLayout) rootView.findViewById(R.id.layout);

		setLayout();
		return rootView;
	}

	public static void setLayout() {
		layout.removeAllViews();
	}
}
