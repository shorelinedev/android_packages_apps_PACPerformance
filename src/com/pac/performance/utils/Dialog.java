package com.pac.performance.utils;

import com.pac.performance.utils.CommandControl.CommandType;
import com.pac.performance.utils.interfaces.DialogReturn;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.LinearLayout;

public class Dialog implements Constants {

    public void showDialogList(final String[] modifiedItems,
            final String[] items, final String file,
            final DialogReturn dialogreturn, final CommandType command,
            final Activity activity) {

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setItems(modifiedItems, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, final int item) {
                mCommandControl.runCommand(items == null ? modifiedItems[item]
                        : items[item], file, command, activity);
                dialogreturn.dialogReturn(modifiedItems[item]);
            }
        }).show();
    }

    public void showDialogGeneric(final String file, String value,
            final DialogReturn dialogreturn, final CommandType command,
            final Activity activity) {

        LinearLayout layout = new LinearLayout(activity);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(30, 20, 30, 20);

        final EditText editor = new EditText(activity);
        editor.setGravity(Gravity.CENTER);
        editor.setText(value);

        layout.addView(editor);

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setView(layout)
                .setNegativeButton(android.R.string.cancel,
                        new OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                    int which) {}
                        })
                .setPositiveButton(android.R.string.ok, new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mCommandControl.runCommand(editor.getText().toString(),
                                file, command, activity);
                        dialogreturn.dialogReturn(editor.getText().toString());
                    }
                }).show();
    }

}
