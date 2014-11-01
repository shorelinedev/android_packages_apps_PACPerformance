package com.pacman.performance.utils.views;

import java.util.Arrays;
import java.util.List;

import android.app.Fragment;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.TextView;

import com.pacman.performance.R;
import com.pacman.performance.utils.views.GenericView.Item;

public class PopupView implements Item {

    private final Context context;
    private final List<String> list;

    private String mTitle;
    private String mSummary;
    private int position;

    private LinearLayout layoutView;
    private TextView titleView;
    private TextView summaryView;
    private TextView valueView;

    private OnItemClickListener onItemClickListener;

    public PopupView(Context context, List<String> list) {
        this.context = context;
        this.list = list;
    }

    public PopupView(Context context, String[] list) {
        this(context, Arrays.asList(list));
    }

    @Override
    public Fragment getFragment() {
        return null;
    }

    public interface OnItemClickListener {
        public void onItemClick(PopupView popupView, int item);
    }

    public void setTitle(String title) {
        mTitle = title;
        refresh();
    }

    public void setSummary(String summary) {
        mSummary = summary;
        refresh();
    }

    public void setItem(int position) {
        this.position = position;
        refresh();
    }

    public void setItem(String item) {
        this.position = list.indexOf(item);
        refresh();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
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

    @Override
    public View getView(LayoutInflater inflater, View convertView) {
        View view = (View) inflater.inflate(R.layout.list_popup, null);

        if (view != null) {
            layoutView = (LinearLayout) view.findViewById(R.id.list_layout);
            titleView = (TextView) view.findViewById(R.id.list_title);
            summaryView = (TextView) view.findViewById(R.id.list_summary);
            valueView = (TextView) view.findViewById(R.id.list_value);
        }

        final PopupMenu popup = new PopupMenu(context, valueView);
        for (int i = 0; i < list.size(); i++)
            popup.getMenu().add(Menu.NONE, i, Menu.NONE, list.get(i));
        popup.setOnMenuItemClickListener(new OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (valueView != null)
                    valueView.setText(list.get(item.getItemId()));
                if (onItemClickListener != null)
                    onItemClickListener.onItemClick(PopupView.this,
                            item.getItemId());
                return false;
            }
        });

        if (layoutView != null) {
            layoutView.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    popup.show();
                }
            });
        }

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

        if (valueView != null) valueView.setText(list.get(position));
    }
}
