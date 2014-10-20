package com.pacman.performance.fragments;

import com.pacman.performance.R;
import com.pacman.performance.utils.Constants;
import com.pacman.performance.utils.Dialog.DialogReturn;

import android.os.Bundle;
import android.os.Handler;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;

public class KernelSamepageMerging extends PreferenceFragment implements
		Constants {

	private final Handler hand = new Handler();

	private PreferenceScreen root;

	private Preference[] mInfos;

	private CheckBoxPreference mEnableKsm;
	private Preference mPagesToScan, mSleepMilliseconds;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		root = getPreferenceManager().createPreferenceScreen(getActivity());

		setPreferenceScreen(root);

		getActivity().runOnUiThread(run);
		hand.post(run2);
	}

	private final Runnable run = new Runnable() {

		@Override
		public void run() {
			root.addPreference(prefHelper.setPreferenceCategory(
					getString(R.string.ksm_stats), getActivity()));

			mInfos = new Preference[KSM_INFOS.length];
			for (int i = 0; i < KSM_INFOS.length; i++) {
				mInfos[i] = prefHelper.setPreference(getResources()
						.getStringArray(R.array.ksm_infos)[i], String
						.valueOf(kernelsamepagemergingHelper.getInfos(i)),
						getActivity());
				root.addPreference(mInfos[i]);
			}

			root.addPreference(prefHelper.setPreferenceCategory(
					getString(R.string.parameters), getActivity()));

			mEnableKsm = prefHelper.setCheckBoxPreference(
					kernelsamepagemergingHelper.isKsmActive(),
					getString(R.string.ksm_enable),
					getString(R.string.ksm_enable_summary), getActivity());
			root.addPreference(mEnableKsm);

			mPagesToScan = prefHelper.setPreference(
					getString(R.string.ksm_pages_to_scan), String
							.valueOf(kernelsamepagemergingHelper
									.getPagesToScan()), getActivity());
			root.addPreference(mPagesToScan);

			mSleepMilliseconds = prefHelper.setPreference(
					getString(R.string.ksm_sleep_milliseconds),
					kernelsamepagemergingHelper.getSleepMilliseconds()
							+ getString(R.string.ms), getActivity());
			root.addPreference(mSleepMilliseconds);
		}
	};

	private final Runnable run2 = new Runnable() {
		@Override
		public void run() {
			for (int i = 0; i < KSM_INFOS.length; i++)
				mInfos[i].setSummary(String.valueOf(kernelsamepagemergingHelper
						.getInfos(i)));

			hand.postDelayed(run2, 1000);
		}
	};

	@Override
	public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen,
			final Preference preference) {

		if (preference == mEnableKsm)
			mCommandControl.runGeneric(KSM_RUN, mEnableKsm.isChecked() ? "1"
					: "0", -1, getActivity());

		if (preference == mPagesToScan) {
			String[] values = new String[1025];
			for (int i = 0; i < values.length; i++)
				values[i] = String.valueOf(i);

			mDialog.showSeekBarDialog(values, values, preference.getSummary()
					.toString(), new DialogReturn() {
				@Override
				public void dialogReturn(String value) {
					mCommandControl.runGeneric(KSM_PAGES_TO_SCAN, value, -1,
							getActivity());
					preference.setSummary(value + getString(R.string.value));
				}
			}, getActivity());
		}

		if (preference == mSleepMilliseconds) {
			String[] modifiedvalues = new String[5001];
			for (int i = 0; i < modifiedvalues.length; i++)
				modifiedvalues[i] = i + getString(R.string.ms);

			String[] values = new String[5001];
			for (int i = 0; i < values.length; i++)
				values[i] = String.valueOf(i);

			mDialog.showSeekBarDialog(modifiedvalues, values, preference
					.getSummary().toString(), new DialogReturn() {
				@Override
				public void dialogReturn(String value) {
					mCommandControl.runGeneric(KSM_SLEEP_MILLISECONDS, value,
							-1, getActivity());
					preference.setSummary(value);
				}
			}, getActivity());
		}

		return true;
	}

	@Override
	public void onDestroy() {
		hand.removeCallbacks(run2);
		super.onDestroy();
	};
}
