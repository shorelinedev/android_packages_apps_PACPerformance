package com.pac.performance.fragments;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphView.GraphViewData;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.LineGraphView;
import com.pac.performance.R;
import com.pac.performance.utils.Constants;

import android.app.Fragment;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MemoryStatsFragment extends Fragment implements Constants {

    private boolean support = false;
    private float total = 0;

    private final Handler hand = new Handler();

    private View rootView;
    private LinearLayout layout;
    private LinearLayout memorylayout;
    private TextView freeMemory;
    private TextView inuseMemory;
    private TextView totalMemory;
    private TextView usageMemory;

    private GraphViewSeries series;

    private Double[] y = new Double[10];

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fancy_stats, container, false);
        layout = (LinearLayout) rootView.findViewById(R.id.layout);
        memorylayout = (LinearLayout) rootView.findViewById(R.id.info_layout);

        recreate();

        return rootView;
    }

    @SuppressWarnings("deprecation")
    private void recreate() {
        layout.removeAllViews();
        memorylayout.removeAllViews();

        for (int i = 0; i < y.length; i++)
            y[i] = (double) 0;

        GraphView graphView = new LineGraphView(getActivity(),
                getString(R.string.memory_stats));
        ((LineGraphView) graphView).setDrawBackground(true);
        graphView.setHorizontalLabels(new String[] {});
        graphView.setVerticalLabels(new String[] { "100%", "75%", "50%", "25%",
                "0%" });

        series = new GraphViewSeries(new GraphViewData[] {
                new GraphViewData(1, 2.0d), new GraphViewData(2, 1.5d),
                new GraphViewData(3, 2.5d), new GraphViewData(4, 1.0d) });

        graphView.setManualYAxisBounds(99, 0);

        Display display = ((WindowManager) getActivity().getSystemService(
                Context.WINDOW_SERVICE)).getDefaultDisplay();

        int displaywidth = display.getWidth();
        int displayheight = display.getHeight();

        int width = displaywidth;
        int height = ((int) Math.round(displayheight / 1.3));

        int rotation = display.getRotation();
        switch (rotation) {
            case Surface.ROTATION_90:
            case Surface.ROTATION_270:
                width = displaywidth;
                height = ((int) Math.round(displayheight / 1.5));
                break;
        }

        graphView.setLayoutParams(new ViewGroup.LayoutParams(width, height));

        graphView.addSeries(series);

        LinearLayout l = new LinearLayout(getActivity());
        freeMemory = new TextView(getActivity());
        inuseMemory = new TextView(getActivity());

        TextView space = new TextView(getActivity());
        space.setText(" ");

        l.addView(freeMemory);
        l.addView(space);
        l.addView(inuseMemory);
        memorylayout.addView(l);

        LinearLayout l2 = new LinearLayout(getActivity());
        totalMemory = new TextView(getActivity());
        totalMemory.setText(getString(R.string.total) + ": "
                + (long) getTotalMemory() + getString(R.string.mb));
        usageMemory = new TextView(getActivity());
        TextView space2 = new TextView(getActivity());
        space2.setText(" ");

        l2.addView(totalMemory);
        l2.addView(space2);
        l2.addView(usageMemory);
        memorylayout.addView(l2);

        layout.addView(graphView);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        recreate();
        super.onConfigurationChanged(newConfig);
    }

    private float getTotalMemory() {
        if (total == 0) {
            String value = mUtils.readFile(PROC_MEMINFO);
            if (value != null) {
                String[] mems = value.split("MemTotal:")[1].split(" ");
                for (String mem : mems) {
                    if (mem != null && !mem.isEmpty() && total == 0) total = Float
                            .parseFloat(mem) / 1024;
                }
            }
        }
        return total;
    }

    private float getActiveMemory() {
        String value = mUtils.readFile(PROC_MEMINFO);
        if (value != null) {
            String[] mems = value.split("Active:")[1].split(" ");
            for (String mem : mems)
                if (mem != null && !mem.isEmpty()) return Float.parseFloat(mem) / 1024;
        }
        return 0;
    }

    public boolean hasSupport() {
        if (!support) {
            String value = mUtils.readFile(PROC_MEMINFO);
            if (value != null) support = value.contains("MemTotal:")
                    && value.contains("Active");
        }
        return support;
    }

    @Override
    public void onResume() {
        hand.post(run);
        super.onResume();
    }

    Runnable run = new Runnable() {

        @Override
        public void run() {
            freeMemory.setText(getString(R.string.available) + ": "
                    + (long) (getTotalMemory() - getActiveMemory())
                    + getString(R.string.mb));
            inuseMemory.setText(getString(R.string.in_use) + ": "
                    + (long) getActiveMemory() + getString(R.string.mb));
            usageMemory.setText(getString(R.string.usage)
                    + ": "
                    + (int) (Math.round(getActiveMemory() / getTotalMemory()
                            * 100)) + "%");

            for (int i = 0; i < y.length - 1; i++)
                y[i] = y[i + 1];

            y[y.length - 1] = (double) Math.round(getActiveMemory()
                    / getTotalMemory() * 100);

            GraphViewData[] data = new GraphViewData[y.length];
            for (int i = 0; i < y.length; i++)
                data[i] = new GraphViewData(i, y[i]);

            series.resetData(data);

            hand.postDelayed(run, 1000);
        }
    };

    public void onDestroy() {
        hand.removeCallbacks(run);
        super.onDestroy();
    };
}
