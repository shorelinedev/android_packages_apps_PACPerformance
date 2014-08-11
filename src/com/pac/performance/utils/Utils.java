package com.pac.performance.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class Utils implements Constants {

    public void toast(String message, Context context) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public boolean existFile(String path) {
        return new File(path).exists();
    }

    public int getInteger(String name, int defaults, Context context) {
        return context.getSharedPreferences(PREF_NAME, 0)
                .getInt(name, defaults);
    }

    public void saveInteger(String name, int value, Context context) {
        context.getSharedPreferences(PREF_NAME, 0).edit().putInt(name, value)
                .commit();
    }

    public String getString(String name, String defaults, Context context) {
        return context.getSharedPreferences(PREF_NAME, 0).getString(name,
                defaults);
    }

    public void saveString(String name, String value, Activity activity) {
        activity.getSharedPreferences(PREF_NAME, 0).edit()
                .putString(name, value).commit();
    }

    public boolean getBoolean(String name, boolean defaults, Context context) {
        return context.getSharedPreferences(PREF_NAME, 0).getBoolean(name,
                defaults);
    }

    public void saveBoolean(String name, boolean value, Context context) {
        context.getSharedPreferences(PREF_NAME, 0).edit()
                .putBoolean(name, value).commit();
    }

    public String readFile(String filepath) {
        try {
            BufferedReader buffreader = new BufferedReader(new FileReader(
                    filepath), 256);
            String line;
            StringBuilder text = new StringBuilder();
            while ((line = buffreader.readLine()) != null) {
                text.append(line);
                text.append("\n");
            }
            buffreader.close();
            return text.toString().trim();
        } catch (FileNotFoundException e) {
            Log.e(TAG, filepath + "does not exist");
        } catch (IOException e) {
            Log.e(TAG, "I/O read error: " + filepath);
        }
        return null;
    }
}
