package com.pacman.performance.fragments;

import java.util.ArrayList;
import java.util.List;

import com.pacman.performance.R;
import com.pacman.performance.utils.CommandControl.CommandType;
import com.pacman.performance.utils.Constants;
import com.pacman.performance.utils.views.CommonView;
import com.pacman.performance.utils.views.CommonView.OnClickListener;
import com.pacman.performance.utils.views.GenericView.GenericAdapter;
import com.pacman.performance.utils.views.GenericView.Item;
import com.pacman.performance.utils.views.HeaderItem;
import com.pacman.performance.utils.views.SeekBarView;
import com.pacman.performance.utils.views.SeekBarView.OnSeekBarListener;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class LowMemoryKillerFragment extends Fragment implements Constants,
        OnSeekBarListener, OnClickListener {

    private ListView list;
    private List<Item> views = new ArrayList<Item>();

    private String[] values;
    private String[] modifiedvalues;

    private SeekBarView[] mMinFree;
    private CommonView[] mProfile;

    private final String[] mProfileValues = new String[] {
            "512,1024,1280,2048,3072,4096", "1024,2048,2560,4096,6144,8192",
            "1024,2048,4096,8192,12288,16384",
            "2048,4096,8192,16384,24576,32768",
            "4096,8192,16384,32768,49152,65536" };

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

            values = new String[257];
            modifiedvalues = new String[257];
            for (int x = 0; x < values.length; x++) {
                modifiedvalues[x] = x + getString(R.string.mb);
                values[x] = String.valueOf(x * 256);
            }

            String[] minfrees = lowmemorykillerHelper.getMinFrees();
            mMinFree = new SeekBarView[minfrees.length];
            for (int i = 0; i < minfrees.length; i++) {
                mMinFree[i] = new SeekBarView(modifiedvalues);
                mMinFree[i].setTitle(getResources().getStringArray(
                        R.array.lmk_names)[i]);
                mMinFree[i].setItem(lowmemorykillerHelper.getMinFree(i) / 256
                        + getString(R.string.mb));
                mMinFree[i].setOnSeekBarListener(LowMemoryKillerFragment.this);
                views.add(mMinFree[i]);
            }

            views.add(new HeaderItem(getString(R.string.profiles)));

            mProfile = new CommonView[mProfileValues.length];
            for (int i = 0; i < mProfileValues.length; i++) {
                mProfile[i] = new CommonView();
                mProfile[i].setTitle(getResources().getStringArray(
                        R.array.lmk_profiles)[i]);
                mProfile[i].setSummary(mProfileValues[i]);
                mProfile[i].setOnClickListener(LowMemoryKillerFragment.this);
                views.add(mProfile[i]);
            }

            list.setAdapter(new GenericAdapter(getActivity(), views));
        }
    };

    @Override
    public void onStop(SeekBarView seekBarView, int item) {
        for (int i = 0; i < lowmemorykillerHelper.getMinFrees().length; i++)
            if (seekBarView == mMinFree[i]) {
                String commandvalue = "";
                String[] currentvalue = lowmemorykillerHelper.getMinFrees();
                for (int x = 0; x < currentvalue.length; x++) {
                    String command = x == i ? values[item] : currentvalue[x];

                    commandvalue = !commandvalue.isEmpty() ? commandvalue + ","
                            + command : command;
                }
                mCommandControl.runCommand(commandvalue, LMK_MINFREE,
                        CommandType.GENERIC, i, getActivity());
                refresh();
            }
    }

    @Override
    public void onClick(CommonView commonView) {
        for (int i = 0; i < mProfileValues.length; i++)
            if (commonView == mProfile[i]) {
                /*
                 * We do not use our common way to apply a change, because those
                 * profiles could break the minfree settings by the user.
                 */
                rootHelper.runCommand("echo "
                        + commonView.getSummary().toString() + " > "
                        + LMK_MINFREE);
                refresh();
            }
    }

    private void refresh() {
        new Thread() {
            public void run() {
                try {
                    Thread.sleep(10);
                    getActivity().runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            String[] minfrees = lowmemorykillerHelper
                                    .getMinFrees();
                            for (int i = 0; i < minfrees.length; i++) {
                                mMinFree[i].setItem(lowmemorykillerHelper
                                        .getMinFree(i)
                                        / 256
                                        + getString(R.string.mb));
                            }
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }.start();
    }

}
