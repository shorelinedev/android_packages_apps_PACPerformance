package com.pacman.performance.utils.views;

import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pacman.performance.R;
import com.pacman.performance.utils.views.GenericView.Item;

public class CheckBoxView implements Item {

    private String mTitle;
    private String mSummary;
    private boolean mChecked;

    private LinearLayout layoutView;
    private TextView titleView;
    private TextView summaryView;
    private CheckBox checkboxView;

    private OnCheckBoxListener onCheckBoxListener;

    public void setChecked(boolean checked) {
        mChecked = checked;
        refresh();
    }

    public interface OnCheckBoxListener {
        public void onClick(CheckBoxView checkBoxView, boolean checked);
    }

    public void setSummary(String summary) {
        mSummary = summary;
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
        return mSummary;
    }

    @Override
    public boolean isHeader() {
        return false;
    }

    public void setOnCheckBoxListener(OnCheckBoxListener onCheckBoxListener) {
        this.onCheckBoxListener = onCheckBoxListener;
    }

    @Override
    public View getView(LayoutInflater inflater, View convertView) {
        View view = (View) inflater.inflate(R.layout.list_checkbox, null);

        if (view != null) {
            layoutView = (LinearLayout) view.findViewById(R.id.list_layout);
            titleView = (TextView) view.findViewById(R.id.list_title);
            summaryView = (TextView) view.findViewById(R.id.list_summary);
            checkboxView = (CheckBox) view.findViewById(R.id.list_checkbox);
        }

        if (checkboxView != null)
            checkboxView.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    click();
                }
            });

        if (layoutView != null) {
            layoutView.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    checkboxView.setChecked(!checkboxView.isChecked());
                    click();
                }
            });
        }

        refresh();
        return view;
    }

    private void click() {
        mChecked = checkboxView.isChecked();
        if (onCheckBoxListener != null)
            onCheckBoxListener.onClick(CheckBoxView.this,
                    checkboxView.isChecked());
    }

    private void refresh() {
        if (titleView != null) if (mTitle != null && !mTitle.isEmpty()) {
            titleView.setText(mTitle);
            titleView.setVisibility(View.VISIBLE);
        } else titleView.setVisibility(View.GONE);

        if (summaryView != null) if (mSummary != null && !mSummary.isEmpty()) {
            summaryView.setText(mSummary);
            summaryView.setVisibility(View.VISIBLE);
        } else summaryView.setVisibility(View.GONE);

        if (checkboxView != null) checkboxView.setChecked(mChecked);
    }

}
