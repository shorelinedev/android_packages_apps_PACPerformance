package com.pac.performance.fragments;

import java.util.ArrayList;
import java.util.List;

import com.pac.performance.R;
import com.pac.performance.utils.Constants;
import com.pac.performance.utils.views.GenericListView;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

public class CustomCommanderFragment extends Fragment implements Constants {

    public Switch actionBarSwitch;
    private ListView list;
    private ArrayAdapter<String> adapter;

    public final String prefName = "customcommands";
    public final String splitName = "qwertyuioasdfghjk";
    public final String splitCommand = "poiuytrescvbnmkhg";

    private List<String> names = new ArrayList<String>();
    private List<String> commands = new ArrayList<String>();;

    @SuppressWarnings("deprecation")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        actionBarSwitch = new Switch(getActivity());
        actionBarSwitch.setChecked(mUtils.getBoolean("customcommander", false,
                getActivity()));

        getActivity().getActionBar().setDisplayOptions(
                ActionBar.DISPLAY_SHOW_CUSTOM, ActionBar.DISPLAY_SHOW_CUSTOM);
        getActivity().getActionBar().setCustomView(
                actionBarSwitch,
                new ActionBar.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT,
                        ActionBar.LayoutParams.WRAP_CONTENT,
                        Gravity.CENTER_VERTICAL | Gravity.RIGHT));
        actionBarSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUtils.saveBoolean("customcommander",
                        actionBarSwitch.isChecked(), getActivity());
            }
        });

        LinearLayout layout = new LinearLayout(getActivity());
        layout.setPadding(10, 0, 10, 0);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));

        TextView summaryText = new TextView(getActivity());
        summaryText.setText(getString(R.string.custom_commander_summary));
        layout.addView(summaryText);

        list = new ListView(getActivity());
        list.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
                showMenuDialog(position);
            }
        });
        layout.addView(list);

        getActivity().runOnUiThread(create);
        return layout;
    }

    private final Runnable refresh = new Runnable() {

        @Override
        public void run() {
            String saved = mUtils.getString(prefName, "", getActivity());

            names.clear();
            commands.clear();

            for (String name : getSavedNames(saved))
                names.add(name);

            for (String command : getSavedCommands(saved))
                commands.add(command);

            list.setVisibility(names.size() < 1 || commands.size() < 1 ? View.GONE
                    : View.VISIBLE);

            adapter.notifyDataSetChanged();
            list.invalidateViews();
            list.refreshDrawableState();
        }
    };

    private final Runnable create = new Runnable() {

        @Override
        public void run() {
            String saved = mUtils.getString(prefName, "", getActivity());

            names = getSavedNames(saved);
            commands = getSavedCommands(saved);

            list.setVisibility(names.size() < 1 || commands.size() < 1 ? View.GONE
                    : View.VISIBLE);

            adapter = new GenericListView(getActivity(), names, commands);
            list.setAdapter(adapter);
        }
    };

    private List<String> getSavedNames(String saved) {
        List<String> List = new ArrayList<String>();
        String[] nameCommands = saved.split(splitCommand);
        for (String nameCommand : nameCommands)
            List.add(nameCommand.split(splitName)[0]);
        return List;
    }

    public List<String> getSavedCommands(String saved) {
        List<String> List = new ArrayList<String>();
        if (!saved.isEmpty()) {
            String[] nameCommands = saved.split(splitCommand);
            for (String nameCommand : nameCommands)
                List.add(nameCommand.split(splitName)[1]);
        }
        return List;
    }

    private void showAddDialog(final String name, String command,
            final boolean overwrite) {
        LinearLayout dialogLayout = new LinearLayout(getActivity());
        dialogLayout.setOrientation(LinearLayout.VERTICAL);
        dialogLayout.setGravity(Gravity.CENTER);
        dialogLayout.setPadding(30, 20, 30, 20);

        final EditText nameText = new EditText(getActivity());
        if (overwrite) nameText.setText(name);
        else nameText.setHint(getString(R.string.name));
        final EditText commandText = new EditText(getActivity());
        if (overwrite) commandText.setText(command);
        else commandText.setHint(getString(R.string.command));

        dialogLayout.addView(nameText);
        dialogLayout.addView(commandText);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(dialogLayout)
                .setNegativeButton(getString(android.R.string.cancel),
                        new OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                    int which) {}
                        })
                .setPositiveButton(getString(android.R.string.ok),
                        new OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                    int which) {

                                if (nameText.getText().toString().isEmpty()) {
                                    mUtils.toast(
                                            getString(R.string.empty,
                                                    getString(R.string.name)),
                                            getActivity());
                                    return;
                                }

                                if (commandText.getText().toString().isEmpty()) {
                                    mUtils.toast(
                                            getString(R.string.empty,
                                                    getString(R.string.command)),
                                            getActivity());
                                    return;
                                }

                                if (overwrite) overwriteCommand(name, nameText
                                        .getText().toString(), commandText
                                        .getText().toString());
                                else saveCommand(nameText.getText().toString(),
                                        commandText.getText().toString());
                            }
                        }).show();
    }

    private void showMenuDialog(final int position) {
        String saved = mUtils.getString(prefName, "", getActivity());
        if (saved.isEmpty()) return;

        final List<String> names = getSavedNames(saved);
        final List<String> commands = getSavedCommands(saved);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setItems(
                getResources().getStringArray(R.array.custom_commander_menu),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, final int item) {
                        switch (item) {
                            case 0:
                                rootHelper.runCommand(commands.get(position));
                                mUtils.toast(commands.get(position),
                                        getActivity());
                                break;
                            case 1:
                                showAddDialog(names.get(position),
                                        commands.get(position), true);
                                break;
                            case 2:
                                deleteCommand(names.get(position));
                                break;
                        }
                    }
                }).show();
    }

    private void saveCommand(String name, String command) {
        String saved = mUtils.getString(prefName, "", getActivity());

        if (alreadyExists(name, saved)) {
            mUtils.toast(getString(R.string.custom_commander_already_exist),
                    getActivity());
            return;
        }

        if (saved.isEmpty()) mUtils.saveString(prefName, name + splitName
                + command + splitCommand, getActivity());
        else mUtils.saveString(prefName, saved + name + splitName + command
                + splitCommand, getActivity());

        getActivity().runOnUiThread(refresh);
    }

    private void overwriteCommand(String oldName, String newName, String command) {
        String saved = mUtils.getString(prefName, "", getActivity());
        if (saved.isEmpty()) return;

        if (alreadyExists(newName, saved)) {
            mUtils.toast(getString(R.string.custom_commander_already_exist),
                    getActivity());
            return;
        }

        String[] commands = saved.split(splitCommand);
        String oldCommand = null;
        for (String command2 : commands)
            if (command2.split(splitName)[0].equals(oldName)) oldCommand = command2;

        mUtils.saveString(prefName,
                saved.replace(oldCommand, newName + splitName + command),
                getActivity());

        getActivity().runOnUiThread(refresh);
    }

    private void deleteCommand(String name) {
        String saved = mUtils.getString(prefName, "", getActivity());
        if (saved.isEmpty()) return;

        String[] commands = saved.split(splitCommand);
        String oldCommand = null;
        for (String command2 : commands)
            if (command2.split(splitName)[0].equals(name)) oldCommand = command2;

        mUtils.saveString(prefName,
                saved.replace(oldCommand + splitCommand, ""), getActivity());

        getActivity().runOnUiThread(refresh);;
    }

    private boolean alreadyExists(String name, String saved) {
        List<String> names = getSavedNames(saved);
        for (String name2 : names)
            if (name2.equals(name)) return true;
        return false;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.add, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_add) showAddDialog(null, null, false);
        return true;
    }
}
