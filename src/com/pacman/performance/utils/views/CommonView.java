package com.pacman.performance.utils.views;

import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pacman.performance.R;
import com.pacman.performance.utils.views.GenericView.Item;

public class CommonView implements Item {

    private String mTitle;
    private String mSummary;

    private RelativeLayout layoutView;
    private TextView titleView;
    private TextView summaryView;

    private OnClickListener onClickListener;

    public interface OnClickListener {
        public void onClick(CommonView commonView);
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

    public void setTitle(String title) {
        mTitle = title;
        refresh();
    }

    public void setSummary(String summary) {
        mSummary = summary;
        refresh();
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    @Override
    public boolean isHeader() {
        return false;
    }

    @Override
    public View getView(LayoutInflater inflater, View convertView) {
        View view = (View) inflater.inflate(R.layout.list_common, null);

        if (view != null) {
            layoutView = (RelativeLayout) view.findViewById(R.id.list_layout);
            titleView = (TextView) view.findViewById(R.id.list_title);
            summaryView = (TextView) view.findViewById(R.id.list_summary);
        }

        if (layoutView != null)
            layoutView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (onClickListener != null)
                        onClickListener.onClick(CommonView.this);
                }
            });

        refresh();

        return view;
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
    }

}
