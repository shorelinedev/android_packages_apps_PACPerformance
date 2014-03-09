package com.pacperformance.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.TextView;

public class LayoutHelper {

	public static int titlecolor = android.R.color.holo_red_dark;

	public static void setTextTitle(TextView i, String text, Context context) {
		i.setBackgroundColor(context.getResources().getColor(titlecolor));
		i.setTextColor(context.getResources().getColor(android.R.color.white));
		i.setTypeface(null, Typeface.BOLD);
		i.setText(text);
	}

}
