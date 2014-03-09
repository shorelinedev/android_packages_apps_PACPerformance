package com.pac.performance.utils;

import android.app.AlertDialog;
import android.content.Context;

public class InformationDialog {

	public static void showInfo(String title, String text, Context context) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(title).setMessage(text).show();
	}
}
