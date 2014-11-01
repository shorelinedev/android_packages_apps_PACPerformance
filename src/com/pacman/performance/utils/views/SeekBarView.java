package com.pacman.performance.utils.views;

import java.util.ArrayList;
import java.util.List;

import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.pacman.performance.R;
import com.pacman.performance.utils.views.GenericView.Item;

public class SeekBarView implements Item {

    private String mTitle;
    private final List<String> values;
    private String item;

    private TextView titleView;
    private TextView seekBarValueView;
    private SeekBar seekBarView;
    private Button minusView;
    private Button plusView;

    private OnSeekBarListener onSeekBarListener;

    public SeekBarView(List<String> values) {
        this.values = values;
    }

    public SeekBarView(String[] values) {
        List<String> list = new ArrayList<String>();
        for (String value : values)
            list.add(value);
        this.values = list;
    }

    public interface OnSeekBarListener {
        public void onStop(SeekBarView seekBarView, int item);
    }

    public void setItem(String item) {
        this.item = item;
        refresh();
    }

    public void setTitle(String title) {
        mTitle = title;
        refresh();
    }

    @Override
    public Fragment getFragment() {
        return null;
    }

    @Override
    public String getTitle() {
        return mTitle;
    }

    @Override
    public String getSummary() {
        return null;
    }

    @Override
    public boolean isHeader() {
        return false;
    }

    public void setOnSeekBarListener(OnSeekBarListener onSeekBarListener) {
        this.onSeekBarListener = onSeekBarListener;
    }

    @Override
    public View getView(LayoutInflater inflater, View convertView) {
        View view = (View) inflater.inflate(R.layout.list_seekbar, null);

        if (view != null) {
            titleView = (TextView) view.findViewById(R.id.list_title);
            seekBarValueView = (TextView) view
                    .findViewById(R.id.list_seekbar_value);
            seekBarView = (SeekBar) view.findViewById(R.id.list_seekbar);
            minusView = (Button) view.findViewById(R.id.list_button_minus);
            plusView = (Button) view.findViewById(R.id.list_button_plus);
        }

        if (seekBarView != null) {
            seekBarView
                    .setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

                        @Override
                        public void onStopTrackingTouch(SeekBar seekBar) {
                            if (onSeekBarListener != null)
                                onSeekBarListener.onStop(SeekBarView.this,
                                        seekBar.getProgress());
                        }

                        @Override
                        public void onStartTrackingTouch(SeekBar seekBar) {}

                        @Override
                        public void onProgressChanged(SeekBar seekBar,
                                int progress, boolean fromUser) {
                            if (seekBarValueView != null)
                                seekBarValueView.setText(values.get(progress));
                        }
                    });

            if (minusView != null && plusView != null) {
                minusView.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        seekBarView.setProgress(seekBarView.getProgress() - 1);
                        if (onSeekBarListener != null)
                            onSeekBarListener.onStop(SeekBarView.this,
                                    seekBarView.getProgress());
                    }
                });
                plusView.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        seekBarView.setProgress(seekBarView.getProgress() + 1);
                        if (onSeekBarListener != null)
                            onSeekBarListener.onStop(SeekBarView.this,
                                    seekBarView.getProgress());
                    }
                });
            }
        }

        refresh();
        return view;
    }

    private void refresh() {
        if (titleView != null && mTitle != null) titleView.setText(mTitle);
        if (seekBarView != null) {
            seekBarView.setMax(values.size() - 1);
            if (item != null)
                seekBarView.setProgress(values.indexOf(item) < 0 ? 0 : values
                        .indexOf(item));
        }
    }

}
