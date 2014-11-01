package com.pacman.performance.fragments;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.pacman.performance.GenericPathReaderActivity;
import com.pacman.performance.R;
import com.pacman.performance.helpers.IOHelper.StorageType;
import com.pacman.performance.utils.Constants;
import com.pacman.performance.utils.CommandControl.CommandType;
import com.pacman.performance.utils.views.CommonView;
import com.pacman.performance.utils.views.CommonView.OnClickListener;
import com.pacman.performance.utils.views.GenericView.GenericAdapter;
import com.pacman.performance.utils.views.GenericView.Item;
import com.pacman.performance.utils.views.HeaderItem;
import com.pacman.performance.utils.views.PopupView;
import com.pacman.performance.utils.views.PopupView.OnItemClickListener;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class IOSchedulerFragment extends Fragment implements Constants,
        OnItemClickListener, OnClickListener {

    private ListView list;
    private List<Item> views = new ArrayList<Item>();

    private final List<String> readaheadValues = new ArrayList<String>();

    private PopupView mInternalScheduler, mExternalScheduler;
    private CommonView mInternalSchedulerTunable, mExternalSchedulerTunable;
    private PopupView mInternalReadahead, mExternalReadahead;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        list = new ListView(getActivity());
        list.setPadding(20, 0, 20, 0);

        getActivity().runOnUiThread(run);
        return list;
    }

    private final Runnable run = new Runnable() {

        @Override
        public void run() {
            views.clear();

            readaheadValues.clear();
            for (int i = 0; i < 32; i++)
                readaheadValues.add((i * 128 + 128) + getString(R.string.kb));

            views.add(new HeaderItem(getString(R.string.scheduler)));

            CommonView mSchedulerSummary = new CommonView();
            mSchedulerSummary.setSummary(getString(R.string.scheduler_summary));
            views.add(mSchedulerSummary);

            mInternalScheduler = new PopupView(getActivity(),
                    ioHelper.getSchedulers(StorageType.INTERNAL));
            mInternalScheduler.setTitle(getString(R.string.internal_scheduler));
            mInternalScheduler.setItem(ioHelper
                    .getScheduler(StorageType.INTERNAL));
            mInternalScheduler.setOnItemClickListener(IOSchedulerFragment.this);
            views.add(mInternalScheduler);

            if (ioHelper.hasExternalStorage()) {
                mExternalScheduler = new PopupView(getActivity(),
                        ioHelper.getSchedulers(StorageType.EXTERNAL));
                mExternalScheduler
                        .setTitle(getString(R.string.external_scheduler));
                mExternalScheduler.setItem(ioHelper
                        .getScheduler(StorageType.EXTERNAL));
                mExternalScheduler
                        .setOnItemClickListener(IOSchedulerFragment.this);
                views.add(mExternalScheduler);
            }

            views.add(new HeaderItem(getString(R.string.advanced)));

            CommonView mSchedulerTunableSummary = new CommonView();
            mSchedulerTunableSummary
                    .setSummary(getString(R.string.scheduler_tunable_summary));
            views.add(mSchedulerTunableSummary);

            mInternalSchedulerTunable = new CommonView();
            mInternalSchedulerTunable
                    .setTitle(getString(R.string.internal_scheduler_tunable));
            mInternalSchedulerTunable
                    .setOnClickListener(IOSchedulerFragment.this);
            views.add(mInternalSchedulerTunable);

            if (ioHelper.hasExternalStorage()) {
                mExternalSchedulerTunable = new CommonView();
                mExternalSchedulerTunable
                        .setTitle(getString(R.string.external_scheduler_tunable));
                mExternalSchedulerTunable
                        .setOnClickListener(IOSchedulerFragment.this);
                views.add(mExternalSchedulerTunable);
            }

            views.add(new HeaderItem(getString(R.string.read_ahead)));

            CommonView mReadAheadSummary = new CommonView();
            mReadAheadSummary
                    .setSummary(getString(R.string.read_ahead_summary));
            views.add(mReadAheadSummary);

            mInternalReadahead = new PopupView(getActivity(), readaheadValues);
            mInternalReadahead
                    .setTitle(getString(R.string.internal_read_ahead));
            mInternalReadahead.setItem(ioHelper
                    .getReadahead(StorageType.INTERNAL)
                    + getString(R.string.kb));
            mInternalReadahead.setOnItemClickListener(IOSchedulerFragment.this);
            views.add(mInternalReadahead);

            if (ioHelper.hasExternalStorage()) {
                mExternalReadahead = new PopupView(getActivity(),
                        readaheadValues);
                mExternalReadahead
                        .setTitle(getString(R.string.external_read_ahead));
                mExternalReadahead.setItem(ioHelper
                        .getReadahead(StorageType.EXTERNAL)
                        + getString(R.string.kb));
                mExternalReadahead
                        .setOnItemClickListener(IOSchedulerFragment.this);
                views.add(mExternalReadahead);
            }

            list.setAdapter(new GenericAdapter(getActivity(), views));
        }
    };

    @Override
    public void onItemClick(PopupView popupView, int item) {
        if (popupView == mInternalScheduler)
            mCommandControl.runCommand(
                    ioHelper.getSchedulers(StorageType.INTERNAL)[item],
                    IO_INTERNAL_SCHEDULER, CommandType.GENERIC, -1,
                    getActivity());

        if (popupView == mExternalScheduler)
            mCommandControl.runCommand(
                    ioHelper.getSchedulers(StorageType.EXTERNAL)[item],
                    IO_EXTERNAL_SCHEDULER, CommandType.GENERIC, -1,
                    getActivity());

        if (popupView == mInternalReadahead)
            mCommandControl.runCommand(
                    readaheadValues.get(item).replace(getString(R.string.kb),
                            ""), IO_INTERNAL_READ_AHEAD, CommandType.GENERIC,
                    -1, getActivity());

        if (popupView == mExternalReadahead)
            mCommandControl.runCommand(
                    readaheadValues.get(item).replace(getString(R.string.kb),
                            ""), IO_EXTERNAL_READ_AHEAD, CommandType.GENERIC,
                    -1, getActivity());

    }

    @Override
    public void onClick(CommonView commonView) {
        if (commonView == mInternalSchedulerTunable) {
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

        if (commonView == mExternalSchedulerTunable) {
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
    }

}
