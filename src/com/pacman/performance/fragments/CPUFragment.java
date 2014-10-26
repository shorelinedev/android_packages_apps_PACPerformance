package com.pacman.performance.fragments;

import java.util.Locale;

import com.pacman.performance.R;
import com.pacman.performance.GenericPathReaderActivity;
import com.pacman.performance.utils.Constants;
import com.pacman.performance.utils.CommandControl.CommandType;
import com.pacman.performance.utils.Dialog.DialogReturn;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;

public class CPUFragment extends PreferenceFragment implements Constants {

	private final Handler hand = new Handler();

	private PreferenceScreen root;
	private CheckBoxPreference[] mCoreBoxes;
	private Preference mCpuMaxScaling, mCpuMinScaling;
	private Preference mGovernorScaling, mGovernorTunables;
	private Preference mMcPowerSaving;
	private CheckBoxPreference mMpdecision, mIntelliPlug, mIntelliPlugEco;

	private String[] mAvailableFreqs;

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
			mAvailableFreqs = new String[cpuHelper.getCpuFreqs().length];
			for (int i = 0; i < cpuHelper.getCpuFreqs().length; i++)
				mAvailableFreqs[i] = (Integer
						.parseInt(cpuHelper.getCpuFreqs()[i]) / 1000)
						+ getString(R.string.mhz);

			root.addPreference(prefHelper.setPreferenceCategory(
					getString(R.string.cpu_stats), getActivity()));

			mCoreBoxes = new CheckBoxPreference[cpuHelper.getCoreCount()];
			for (int i = 0; i < cpuHelper.getCoreCount(); i++) {
				final int mCoreFreq = cpuHelper.getCurFreq(i);
				mCoreBoxes[i] = prefHelper.setCheckBoxPreference(
						mCoreFreq != 0,
						getString(R.string.core, i),
						mCoreFreq == 0 ? getString(R.string.offline) : String
								.valueOf(mCoreFreq / 1000)
								+ getString(R.string.mhz), getActivity());
				root.addPreference(mCoreBoxes[i]);
			}

			root.addPreference(prefHelper.setPreferenceCategory(
					getString(R.string.parameters), getActivity()));

			mCpuMaxScaling = prefHelper.setPreference(
					getString(R.string.cpu_max_freq),
					(cpuHelper.getMaxFreq(0) / 1000) + getString(R.string.mhz),
					getActivity());

			root.addPreference(mCpuMaxScaling);

			mCpuMinScaling = prefHelper.setPreference(
					getString(R.string.cpu_min_freq),
					(cpuHelper.getMinFreq(0) / 1000) + getString(R.string.mhz),
					getActivity());

			root.addPreference(mCpuMinScaling);

			mGovernorScaling = prefHelper.setPreference(
					getString(R.string.cpu_governor), cpuHelper.getGovernor(0),
					getActivity());

			root.addPreference(mGovernorScaling);

			root.addPreference(prefHelper.setPreferenceCategory(
					getString(R.string.advanced), getActivity()));

			mGovernorTunables = prefHelper.setPreference(
					getString(R.string.cpu_governor_tunables),
					getString(R.string.cpu_governor_tunables_summary),
					getActivity());

			root.addPreference(mGovernorTunables);

			if (cpuHelper.hasMcPowerSaving()) {
				mMcPowerSaving = prefHelper
						.setPreference(
								getString(R.string.mc_power_saving),
								getString(R.string.mc_power_saving_summary)
										+ ": "
										+ getResources().getStringArray(
												R.array.mc_power_saving_items)[cpuHelper
												.getMcPowerSaving()],
								getActivity());

				root.addPreference(mMcPowerSaving);
			}

			if (cpuHelper.hasMpdecision())
				root.addPreference(prefHelper.setPreferenceCategory(
						getString(R.string.hotplug), getActivity()));

			if (cpuHelper.hasMpdecision()) {
				mMpdecision = prefHelper.setCheckBoxPreference(
						cpuHelper.isMpdecisionActive(),
						getString(R.string.mpdecision),
						getString(R.string.mpdecision_summary), getActivity());

				root.addPreference(mMpdecision);
			}

			if (cpuHelper.hasIntelliPlug()) {
				mIntelliPlug = prefHelper.setCheckBoxPreference(
						cpuHelper.isIntelliPlugActive(),
						getString(R.string.intelliplug),
						getString(R.string.intelliplug_summary), getActivity());

				root.addPreference(mIntelliPlug);
			}

			if (cpuHelper.hasIntelliPlugEco()) {
				mIntelliPlugEco = prefHelper.setCheckBoxPreference(
						cpuHelper.isIntelliPlugEcoActive(),
						getString(R.string.intelliplug_eco_mode), null,
						getActivity());

				root.addPreference(mIntelliPlugEco);
			}
		}
	};

	private final Runnable run2 = new Runnable() {
		@Override
		public void run() {
			for (int i = 0; i < cpuHelper.getCoreCount(); i++) {
				final int mCoreFreq = cpuHelper.getCurFreq(i);
				if (mCoreBoxes != null) {
					mCoreBoxes[i]
							.setSummary(mCoreFreq == 0 ? getString(R.string.offline)
									: String.valueOf(mCoreFreq / 1000)
											+ getString(R.string.mhz));
					mCoreBoxes[i].setChecked(mCoreFreq != 0);
				}
			}

			if (mCpuMaxScaling != null && mCpuMinScaling != null) {
				mCpuMaxScaling.setSummary((cpuHelper.getMaxFreq(0) / 1000)
						+ getString(R.string.mhz));
				mCpuMinScaling.setSummary((cpuHelper.getMinFreq(0) / 1000)
						+ getString(R.string.mhz));
			}

			hand.postDelayed(run2, 500);
		}
	};

	@Override
	public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen,
			final Preference preference) {

		for (int i = 0; i < cpuHelper.getCoreCount(); i++)
			if (preference == mCoreBoxes[i]) {
				if (i != 0)
					mCommandControl.runCommand(mCoreBoxes[i].isChecked() ? "1"
							: "0", String.format(CPU_CORE_ONLINE, i),
							CommandType.GENERIC, -1, getActivity());
				else {
					mCoreBoxes[i].setChecked(true);
					mUtils.toast(getString(R.string.turn_off_not_possible, i),
							getActivity());
				}
			}

		if (preference == mCpuMaxScaling)
			mDialog.showDialogList(mAvailableFreqs, cpuHelper.getCpuFreqs(),
					CPU_MAX_FREQ, new DialogReturn() {
						@Override
						public void dialogReturn(String value) {
							preference.setSummary(value);
						}
					}, CommandType.CPU, -1, getActivity());

		if (preference == mCpuMinScaling)
			mDialog.showDialogList(mAvailableFreqs, cpuHelper.getCpuFreqs(),
					CPU_MIN_FREQ, new DialogReturn() {
						@Override
						public void dialogReturn(String value) {
							preference.setSummary(value);
						}
					}, CommandType.CPU, -1, getActivity());

		if (preference == mGovernorScaling)
			mDialog.showDialogList(cpuHelper.getCpuGovernors(), null,
					CPU_SCALING_GOVERNOR, new DialogReturn() {
						@Override
						public void dialogReturn(String value) {
							preference.setSummary(value);
						}
					}, CommandType.CPU, -1, getActivity());

		if (preference == mGovernorTunables) {
			String governor = cpuHelper.getGovernor(0);
			Intent i = new Intent(getActivity(),
					GenericPathReaderActivity.class);
			Bundle args = new Bundle();
			args.putString(
					GenericPathReaderActivity.ARG_TITLE,
					getString(R.string.cpu_governor_tunables) + ": "
							+ governor.toUpperCase(Locale.getDefault()));
			args.putString(GenericPathReaderActivity.ARG_PATH,
					String.format(CPU_GOVERNOR_TUNABLES, governor));
			args.putString(
					GenericPathReaderActivity.ARG_ERROR,
					getString(R.string.not_tunable,
							governor.toUpperCase(Locale.getDefault())));
			i.putExtras(args);

			startActivity(i);
		}

		if (preference == mMcPowerSaving)
			mDialog.showDialogList(
					getResources()
							.getStringArray(R.array.mc_power_saving_items),
					new String[] { "0", "1", "2" }, CPU_MC_POWER_SAVING,
					new DialogReturn() {
						@Override
						public void dialogReturn(String value) {
							preference
									.setSummary(getString(R.string.mc_power_saving_summary)
											+ ": " + value);
						}
					}, CommandType.GENERIC, -1, getActivity());

		if (preference == mMpdecision) {
			if (mMpdecision.isChecked())
				mCommandControl.startModule(CPU_MPDEC, true, getActivity());
			else
				mCommandControl.stopModule(CPU_MPDEC, true, getActivity());
		}

		if (preference == mIntelliPlug) {
			if (mIntelliPlug.isChecked())
				mCommandControl.runGeneric(CPU_INTELLI_PLUG, "1", -1,
						getActivity());
			else {
				mCommandControl.runGeneric(CPU_INTELLI_PLUG, "0", -1,
						getActivity());
				mCommandControl.bringCoresOnline();
			}
		}

		if (preference == mIntelliPlugEco)
			mCommandControl.runGeneric(CPU_INTELLI_PLUG_ECO,
					mIntelliPlugEco.isChecked() ? "1" : "0", -1, getActivity());

		return true;
	}

	@Override
	public void onDestroy() {
		hand.removeCallbacks(run2);
		super.onDestroy();
	};

}
