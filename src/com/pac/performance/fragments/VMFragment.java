package com.pac.performance.fragments;

import java.util.List;

import com.pac.performance.MainActivity;
import com.pac.performance.R;
import com.pac.performance.utils.Control;
import com.pac.performance.utils.InformationDialog;
import com.pac.performance.utils.LayoutHelper;
import com.pac.performance.utils.Utils;
import com.pac.performance.utils.VMHelper;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class VMFragment extends Fragment implements OnClickListener,
		TextWatcher {

	private static Context context;

	public static LinearLayout layout = null;

	private static OnClickListener OnClickListener;
	private static TextWatcher TextWatcher;

	private static List<String> mVMFiles;

	private static TextView mVMTitle;
	private static Button[] mMinusButtons;
	private static EditText[] mVMEdits;
	private static Button[] mPlusButtons;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		context = getActivity();
		View rootView = inflater.inflate(R.layout.generic, container, false);
		layout = (LinearLayout) rootView.findViewById(R.id.layout);

		OnClickListener = this;
		TextWatcher = this;

		mVMFiles = VMHelper.getVMFiles();
		setLayout();
		return rootView;
	}

	public static void setLayout() {
		layout.removeAllViews();

		List<String> VMValues = VMHelper.getVMValues();

		// Create VM Tuning Title
		mVMTitle = new TextView(context);
		LayoutHelper.setTextTitle(mVMTitle,
				context.getString(R.string.vmtuning), context);
		mVMTitle.setOnClickListener(OnClickListener);
		mVMTitle.setPadding(0, (int) (MainActivity.mHeight / 25), 0, 0);
		if (Utils.exist(VMHelper.VM_PATH))
			layout.addView(mVMTitle);

		mMinusButtons = new Button[mVMFiles.size()];
		mVMEdits = new EditText[mVMFiles.size()];
		mPlusButtons = new Button[mVMFiles.size()];

		for (int i = 0; i < mVMFiles.size(); i++) {

			TextView mVMText = new TextView(context);
			LayoutHelper.setSubTitle(
					mVMText,
					Utils.setAllLetterUpperCase(mVMFiles.get(i).replace("_",
							" ")));
			if (Utils.exist(VMHelper.VM_PATH))
				layout.addView(mVMText);

			LinearLayout mVMLayout = new LinearLayout(context);
			mVMLayout.setGravity(Gravity.CENTER);
			if (Utils.exist(VMHelper.VM_PATH))
				layout.addView(mVMLayout);

			Button mMinusButton = new Button(context);
			mMinusButton.setText(context.getString(R.string.minus));
			mMinusButtons[i] = mMinusButton;
			mMinusButton.setOnClickListener(OnClickListener);
			mVMLayout.addView(mMinusButton);

			EditText mVMEdit = new EditText(context);
			LayoutHelper.setEditText(mVMEdit, String.valueOf(VMValues.get(i)));
			mVMEdit.setInputType(InputType.TYPE_NUMBER_FLAG_SIGNED
					| InputType.TYPE_CLASS_NUMBER);
			mVMEdit.addTextChangedListener(TextWatcher);
			mVMEdits[i] = mVMEdit;
			mVMLayout.addView(mVMEdit);

			Button mPlusButton = new Button(context);
			mPlusButton.setText(context.getString(R.string.plus));
			mPlusButtons[i] = mPlusButton;
			mPlusButton.setOnClickListener(OnClickListener);
			mVMLayout.addView(mPlusButton);
		}
	}

	@Override
	public void onClick(View v) {
		if (v.equals(mVMTitle))
			InformationDialog.showInfo(mVMTitle.getText().toString(),
					context.getString(R.string.vmtunig_summary), context);

		for (int i = 0; i < mVMFiles.size(); i++) {
			MainFragment.VMChange = true;
			MainFragment.showButtons(true);
			if (v.equals(mMinusButtons[i])) {
				mVMEdits[i].setText(String.valueOf(Integer.parseInt(mVMEdits[i]
						.getText().toString()) - 1));
				Control.runVMGeneric(mVMEdits[i].getText().toString(),
						mVMFiles.get(i));
			}
			if (v.equals(mPlusButtons[i])) {
				mVMEdits[i].setText(String.valueOf(Integer.parseInt(mVMEdits[i]
						.getText().toString()) + 1));
				Control.runVMGeneric(mVMEdits[i].getText().toString(),
						mVMFiles.get(i));
			}
		}
	}

	@Override
	public void afterTextChanged(Editable s) {
		MainFragment.VMChange = true;
		MainFragment.showButtons(true);

		for (int i = 0; i < mVMFiles.size(); i++)
			if (s.equals(mVMEdits[i]))
				Control.runVMGeneric(mVMEdits[i].getText().toString(),
						mVMFiles.get(i));
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
	}
}
