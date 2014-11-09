package com.pacman.performance.utils.views;

import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.pacman.performance.R;
import com.pacman.performance.utils.views.GenericView.Item;

public class EditTextView implements Item {

    private String mTitle;
    private String mSummary;
    private String mValue;
    private int mInputType;

    private TextView titleView;
    private TextView summaryView;
    private EditText edittextView;
    private Button applyView;

    private OnApplyListener onApplyListener;

    @Override
    public Fragment getFragment() {
        return null;
    }

    public interface OnApplyListener {
        public void onApply(EditTextView edittextView, String value);
    }

    public void setTitle(String title) {
        mTitle = title;
        refresh();
    }

    public void setSummary(String summary) {
        mSummary = summary;
        refresh();
    }

    public void setValue(String value) {
        mValue = value;
        refresh();
    }

    public void setInputType(int type) {
        mInputType = type;
        refresh();
    }

    public void setOnApplyListener(OnApplyListener onApplyListener) {
        this.onApplyListener = onApplyListener;
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
        View view = (View) inflater.inflate(R.layout.list_edittext, null);

        if (view != null) {
            titleView = (TextView) view.findViewById(R.id.list_title);
            summaryView = (TextView) view.findViewById(R.id.list_summary);
            edittextView = (EditText) view.findViewById(R.id.list_edittext);
            applyView = (Button) view.findViewById(R.id.list_apply);
        }

        applyView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (onApplyListener != null)
                    onApplyListener.onApply(EditTextView.this, edittextView
                            .getText().toString());
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

        if (edittextView != null && mValue != null) {
            edittextView.setText(mValue);
            if (mInputType != 0) edittextView.setInputType(mInputType);
        }
    }
}
