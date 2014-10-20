package com.pacman.performance.fragments;

import com.pacman.performance.R;
import com.pacman.performance.utils.Constants;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class KernelInformationFragment extends Fragment implements Constants {

	@SuppressWarnings("deprecation")
	@Override
	public View onCreateView(final LayoutInflater inflater,
			final ViewGroup container, Bundle savedInstanceState) {

		final ScrollView scroll = new ScrollView(getActivity());

		final LinearLayout layout = new LinearLayout(getActivity());
		layout.setLayoutParams(new ViewGroup.LayoutParams(
				ViewGroup.LayoutParams.FILL_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT));
		layout.setOrientation(LinearLayout.VERTICAL);
		layout.setPadding(10, 0, 10, 0);
		scroll.addView(layout);

		final TextView header = (TextView) inflater.inflate(
				R.layout.list_header, container, false);
		final TextView kernelVersion = new TextView(getActivity());
		final TextView header1 = (TextView) inflater.inflate(
				R.layout.list_header, container, false);
		final TextView cpuInfo = new TextView(getActivity());
		final TextView header2 = (TextView) inflater.inflate(
				R.layout.list_header, container, false);
		final TextView memInfo = new TextView(getActivity());

		layout.addView(header);
		layout.addView(kernelVersion);
		layout.addView(header1);
		layout.addView(cpuInfo);
		layout.addView(header2);
		layout.addView(memInfo);

		getActivity().runOnUiThread(new Runnable() {
			@Override
			public void run() {
				header.setText(getString(R.string.kernel_version));
				kernelVersion.setText(mUtils.getString("kernel_version",
						"unknown", getActivity()));
				header1.setText(getString(R.string.cpu_info));
				cpuInfo.setText(mUtils.readFile(PROC_CPUINFO));
				header2.setText(getString(R.string.memory_info));
				memInfo.setText(mUtils.readFile(PROC_MEMINFO));
			}
		});

		return scroll;
	}
}
