package com.pacperformance.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import android.content.Context;
import android.widget.Toast;

public class Utils {

	public static void toast(String text, Context context) {
		Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
	}

	public static boolean exist(String file) {
		return new File(file).exists();
	}

	public static String readBlock(String filename) throws IOException {
		BufferedReader buffreader = new BufferedReader(
				new FileReader(filename), 256);
		String line;
		StringBuilder text = new StringBuilder();
		while ((line = buffreader.readLine()) != null) {
			text.append(line);
			text.append("\n");
		}
		buffreader.close();
		return text.toString();
	}

	public static String readLine(String filename) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(filename),
				256);
		try {
			return reader.readLine();
		} finally {
			reader.close();
		}
	}

}
