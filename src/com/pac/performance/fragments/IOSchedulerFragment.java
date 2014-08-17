package com.pac.performance.fragments;

import java.util.Locale;

import com.pac.performance.GenericPathReaderActivity;
import com.pac.performance.R;
import com.pac.performance.helpers.IOHelper.StorageType;
import com.pac.performance.utils.CommandControl.CommandType;
import com.pac.performance.utils.Constants;
import com.pac.performance.utils.interfaces.DialogReturn;

import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;

public class IOSchedulerFragment extends PreferenceFragment implements
        Constants {

    private String[] readaheadValues;
    private String[] readaheadValuesOriginal;

    private Preference mInternalScheduler, mExternalScheduler;
    private Preference mInternalSchedulerTunable, mExternalSchedulerTunable;
    private Preference mInternalReadahead, mExternalReadahead;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        readaheadValues = new String[32];
        readaheadValuesOriginal = new String[32];
        for (int i = 0; i < 32; i++) {
            readaheadValues[i] = (i * 128 + 128) + getString(R.string.kb);
            readaheadValuesOriginal[i] = String.valueOf(i * 128 + 128);
        }

        PreferenceScreen root = getPreferenceManager().createPreferenceScreen(
                getActivity());

        root.addPreference(prefHelper.setPreferenceCategory(
                getString(R.string.scheduler), getActivity()));

        root.addPreference(prefHelper.setPreference(null,
                getString(R.string.scheduler_summary), getActivity()));

        mInternalScheduler = prefHelper.setPreference(
                getString(R.string.internal_scheduler),
                ioHelper.getScheduler(StorageType.INTERNAL), getActivity());

        root.addPreference(mInternalScheduler);

        if (ioHelper.hasExternalStorage()) {
            mExternalScheduler = prefHelper.setPreference(
                    getString(R.string.external_scheduler),
                    ioHelper.getScheduler(StorageType.EXTERNAL), getActivity());

            root.addPreference(mExternalScheduler);
        }

        root.addPreference(prefHelper.setPreferenceCategory(
                getString(R.string.advanced), getActivity()));

        root.addItemFromInflater(prefHelper.setPreference(null,
                getString(R.string.scheduler_tunable_summary), getActivity()));

        mInternalSchedulerTunable = prefHelper.setPreference(
                getString(R.string.internal_scheduler_tunable), null,
                getActivity());

        root.addPreference(mInternalSchedulerTunable);

        if (ioHelper.hasExternalStorage()) {
            mExternalSchedulerTunable = prefHelper.setPreference(
                    getString(R.string.external_scheduler_tunable), null,
                    getActivity());

            root.addPreference(mExternalSchedulerTunable);
        }

        root.addPreference(prefHelper.setPreferenceCategory(
                getString(R.string.read_ahead), getActivity()));

        root.addPreference(prefHelper.setPreference(null,
                getString(R.string.read_ahead_summary), getActivity()));

        mInternalReadahead = prefHelper.setPreference(
                getString(R.string.internal_scheduler),
                ioHelper.getReadahead(StorageType.INTERNAL)
                        + getString(R.string.kb), getActivity());

        root.addPreference(mInternalReadahead);

        if (ioHelper.hasExternalStorage()) {
            mExternalReadahead = prefHelper.setPreference(
                    getString(R.string.external_scheduler),
                    ioHelper.getReadahead(StorageType.EXTERNAL)
                            + getString(R.string.kb), getActivity());

            root.addPreference(mExternalReadahead);
        }

        setPreferenceScreen(root);
    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen,
            final Preference preference) {

        if (preference == mInternalScheduler) mDialog.showDialogList(
                ioHelper.getSchedulers(StorageType.INTERNAL), null,
                IO_INTERNAL_SCHEDULER, new DialogReturn() {
                    @Override
                    public void dialogReturn(String value) {
                        try {
                            Thread.sleep(10);
                            preference.setSummary(ioHelper
                                    .getScheduler(StorageType.INTERNAL));
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }, CommandType.GENERIC, -1, getActivity());

        if (preference == mExternalScheduler) mDialog.showDialogList(
                ioHelper.getSchedulers(StorageType.EXTERNAL), null,
                IO_EXTERNAL_SCHEDULER, new DialogReturn() {
                    @Override
                    public void dialogReturn(String value) {
                        try {
                            Thread.sleep(10);
                            preference.setSummary(ioHelper
                                    .getScheduler(StorageType.EXTERNAL));
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }, CommandType.GENERIC, -1, getActivity());

        if (preference == mInternalSchedulerTunable) {
            String scheduler = ioHelper.getScheduler(StorageType.INTERNAL);
            Intent i = new Intent(getActivity(),
                    GenericPathReaderActivity.class);
            Bundle args = new Bundle();
            args.putString(GenericPathReaderActivity.ARG_TITLE,
                    getString(R.string.internal_scheduler_tunable) + ": "
                            + scheduler.toUpperCase(Locale.getDefault()));
            args.putString(GenericPathReaderActivity.ARG_PATH,
                    IO_INTERNAL_SCHEDULER_TUNABLE);
            args.putString(
                    GenericPathReaderActivity.ARG_ERROR,
                    getString(R.string.not_tunable,
                            scheduler.toUpperCase(Locale.getDefault())));
            i.putExtras(args);

            startActivity(i);
            getActivity().overridePendingTransition(enter_anim, exit_anim);
        }

        if (preference == mExternalSchedulerTunable) {
            String scheduler = ioHelper.getScheduler(StorageType.EXTERNAL);
            Intent i = new Intent(getActivity(),
                    GenericPathReaderActivity.class);
            Bundle args = new Bundle();
            args.putString(GenericPathReaderActivity.ARG_TITLE,
                    getString(R.string.external_scheduler_tunable) + ": "
                            + scheduler.toUpperCase(Locale.getDefault()));
            args.putString(GenericPathReaderActivity.ARG_PATH,
                    IO_EXTERNAL_SCHEDULER_TUNABLE);
            args.putString(
                    GenericPathReaderActivity.ARG_ERROR,
                    getString(R.string.not_tunable,
                            scheduler.toUpperCase(Locale.getDefault())));
            i.putExtras(args);

            startActivity(i);
            getActivity().overridePendingTransition(enter_anim, exit_anim);
        }

        if (preference == mInternalReadahead) mDialog.showDialogList(
                readaheadValues, readaheadValuesOriginal,
                IO_INTERNAL_READ_AHEAD, new DialogReturn() {

                    @Override
                    public void dialogReturn(String value) {
                        try {
                            Thread.sleep(10);
                            preference.setSummary(ioHelper
                                    .getReadahead(StorageType.INTERNAL)
                                    + getString(R.string.kb));
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }, CommandType.GENERIC, -1, getActivity());

        if (preference == mExternalReadahead) mDialog.showDialogList(
                readaheadValues, readaheadValuesOriginal,
                IO_EXTERNAL_READ_AHEAD, new DialogReturn() {

                    @Override
                    public void dialogReturn(String value) {
                        try {
                            Thread.sleep(10);
                            preference.setSummary(ioHelper
                                    .getReadahead(StorageType.EXTERNAL)
                                    + getString(R.string.kb));
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }, CommandType.GENERIC, -1, getActivity());

        return true;
    }
}
