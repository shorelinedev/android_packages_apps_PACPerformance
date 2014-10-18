package com.pac.performance;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.pac.performance.utils.CommandControl.CommandType;
import com.pac.performance.utils.Constants;
import com.pac.performance.utils.Dialog.DialogReturn;
import com.pac.performance.utils.views.GenericListView;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class GenericPathReaderActivity extends Activity implements Constants {

    private Handler hand = new Handler();

    public static final String ARG_TITLE = "title";
    public static final String ARG_PATH = "path";
    public static final String ARG_ERROR = "error";
    private String path;

    private final List<File> files = new ArrayList<File>();
    private final List<String> values = new ArrayList<String>();
    private final List<String> filesString = new ArrayList<String>();

    private ListView list;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(enter_anim, exit_anim);

        list = new ListView(this);
        list.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
                mDialog.showDialogGeneric(
                        files.get(position).getAbsolutePath(),
                        values.get(position), new DialogReturn() {
                            @Override
                            public void dialogReturn(String value) {
                                hand.postDelayed(refresh, 50);
                            }
                        }, 0, CommandType.GENERIC, -1,
                        GenericPathReaderActivity.this);
            }
        });
        setContentView(list);

        getActionBar().setDisplayHomeAsUpEnabled(true);

        // Get args
        getActionBar().setTitle(getIntent().getExtras().getString(ARG_TITLE));
        path = getIntent().getExtras().getString(ARG_PATH);

        hand.postDelayed(create, 50);
    }

    private final Runnable create = new Runnable() {

        @Override
        public void run() {
            // Collecting all files and add them to Lists
            File[] fileArray = new File(path).listFiles();
            if (fileArray != null) {
                for (File file : fileArray) {
                    String value = mUtils.readFile(file.getAbsolutePath());
                    if (value != null) {
                        files.add(file);
                        values.add(value);
                        filesString.add(file.getName());
                    }
                }
                // Setup adapter
                if (files.size() > 0) {
                    adapter = new GenericListView(
                            GenericPathReaderActivity.this, filesString, values);
                    list.setAdapter(adapter);
                }
            }
        }
    };

    private final Runnable refresh = new Runnable() {

        @Override
        public void run() {
            // Remove all items first otherwise we will get duplicated items
            files.clear();
            values.clear();
            filesString.clear();

            File[] fileArray = new File(path).listFiles();
            if (fileArray != null) {
                for (File file : fileArray) {
                    String value = mUtils.readFile(file.getAbsolutePath());
                    if (value != null) {
                        files.add(file);
                        values.add(value);
                        filesString.add(file.getName());
                    }
                }

                adapter.notifyDataSetChanged();
                list.invalidateViews();
                list.refreshDrawableState();
            }
        }
    };

    // Overwrite onBackPressed for animation
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(enter_anim, exit_anim);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            overridePendingTransition(enter_anim, exit_anim);
        }
        return true;
    }

}
