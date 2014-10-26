package com.pacman.performance;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.TextView;

public class TextActivity extends Activity {

    public static final String ARG_TEXT = "text";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TextView text = new TextView(this);
        setContentView(text);

        text.setTextSize(20);
        text.setGravity(Gravity.CENTER);
        text.setText(getIntent().getExtras().getString(ARG_TEXT));
    }

}
