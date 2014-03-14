package com.pac.performance.fragments;

import com.pac.performance.MainActivity;
import com.pac.performance.R;
import com.pac.performance.helpers.AudioHelper;
import com.pac.performance.helpers.LayoutHelper;
import com.pac.performance.utils.Control;
import com.pac.performance.utils.Utils;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.LinearLayout.LayoutParams;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class AudioFragment extends Fragment implements OnClickListener,
		OnSeekBarChangeListener {

	private static Context context;

	public static LinearLayout layout = null;

	private static OnClickListener OnClickListener;
	private static OnSeekBarChangeListener OnSeekBarChangeListener;

	private static TextView[] mFauxSoundTexts;
	private static Button[] mFauxSoundMinuses;
	private static SeekBar[] mFauxSoundBars;
	private static Button[] mFauxSoundPluses;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		context = getActivity();
		View rootView = inflater.inflate(R.layout.generic, container, false);
		layout = (LinearLayout) rootView.findViewById(R.id.layout);

		OnClickListener = this;
		OnSeekBarChangeListener = this;

		setLayout();
		return rootView;
	}

	public static void setLayout() {
		layout.removeAllViews();

		TextView mFauxSoundTitle = new TextView(context);
		LayoutHelper.setTextTitle(mFauxSoundTitle,
				context.getString(R.string.fauxsoundcontrol), context);
		mFauxSoundTitle.setPadding(0, (int) (MainActivity.mHeight / 25), 0, 15);
		if (Utils.exist(AudioHelper.FAUX_SOUND_CONTROL))
			layout.addView(mFauxSoundTitle);

		if (Utils.exist(AudioHelper.FAUX_SOUND_CONTROL)) {

			mFauxSoundTexts = new TextView[AudioHelper.FAUX_SOUND.length];
			mFauxSoundMinuses = new Button[AudioHelper.FAUX_SOUND.length];
			mFauxSoundBars = new SeekBar[AudioHelper.FAUX_SOUND.length];
			mFauxSoundPluses = new Button[AudioHelper.FAUX_SOUND.length];

			for (int i = 1; i < AudioHelper.FAUX_SOUND.length; i++) {

				TextView mFauxTitle = new TextView(context);
				if (AudioHelper.FAUX_SOUND[i]
						.equals(AudioHelper.FAUX_HEADPHONE_GAIN))
					LayoutHelper.setSubTitle(mFauxTitle,
							context.getString(R.string.headphonegain));
				else if (AudioHelper.FAUX_SOUND[i]
						.equals(AudioHelper.FAUX_HANDSET_MIC_GAIN))
					LayoutHelper.setSubTitle(mFauxTitle,
							context.getString(R.string.handsetmicgain));
				else if (AudioHelper.FAUX_SOUND[i]
						.equals(AudioHelper.FAUX_CAM_MIC_GAIN))
					LayoutHelper.setSubTitle(mFauxTitle,
							context.getString(R.string.cammicgain));
				else if (AudioHelper.FAUX_SOUND[i]
						.equals(AudioHelper.FAUX_SPEAKER_GAIN))
					LayoutHelper.setSubTitle(mFauxTitle,
							context.getString(R.string.speakergain));
				else if (AudioHelper.FAUX_SOUND[i]
						.equals(AudioHelper.FAUX_HEADPHONE_PA_GAIN))
					LayoutHelper.setSubTitle(mFauxTitle,
							context.getString(R.string.headphonepagain));
				layout.addView(mFauxTitle);

				TextView mFauxSoundText = new TextView(context);
				LayoutHelper.setSeekBarText(
						mFauxSoundText,
						String.valueOf(AudioHelper.getFauxSoundControlValues(i)
								+ context.getString(R.string.db)));
				mFauxSoundTexts[i] = mFauxSoundText;
				layout.addView(mFauxSoundText);

				LinearLayout mFauxSoundLayout = new LinearLayout(context);
				mFauxSoundLayout.setGravity(Gravity.CENTER);
				layout.addView(mFauxSoundLayout);

				LayoutParams lp = new LinearLayout.LayoutParams(0,
						LinearLayout.LayoutParams.WRAP_CONTENT, 1f);

				Button mFauxSoundMinus = new Button(context);
				mFauxSoundMinus.setText(context.getString(R.string.minus));
				mFauxSoundMinus.setOnClickListener(OnClickListener);
				mFauxSoundMinuses[i] = mFauxSoundMinus;
				mFauxSoundLayout.addView(mFauxSoundMinus);

				SeekBar mFauxSoundBar = new SeekBar(context);
				LayoutHelper
						.setNormalSeekBar(
								mFauxSoundBar,
								AudioHelper.FAUX_SOUND[i]
										.equals(AudioHelper.FAUX_HEADPHONE_PA_GAIN) ? 17
										: 30,
								AudioHelper.FAUX_SOUND[i]
										.equals(AudioHelper.FAUX_HEADPHONE_PA_GAIN) ? AudioHelper
										.getFauxSoundControlValues(i) + 12
										: AudioHelper
												.getFauxSoundControlValues(i) + 20,
								context);

				mFauxSoundBar.setLayoutParams(lp);
				mFauxSoundBar
						.setOnSeekBarChangeListener(OnSeekBarChangeListener);
				mFauxSoundBars[i] = mFauxSoundBar;
				mFauxSoundLayout.addView(mFauxSoundBar);

				Button mFauxSoundPlus = new Button(context);
				mFauxSoundPlus.setText(context.getString(R.string.plus));
				mFauxSoundPlus.setOnClickListener(OnClickListener);
				mFauxSoundPluses[i] = mFauxSoundPlus;
				mFauxSoundLayout.addView(mFauxSoundPlus);
			}
		}

	}

	@Override
	public void onClick(View v) {
		for (int i = 0; i < AudioHelper.FAUX_SOUND.length; i++)
			if (v.equals(mFauxSoundMinuses[i]) || v.equals(mFauxSoundPluses[i])) {
				mFauxSoundBars[i]
						.setProgress(v.equals(mFauxSoundMinuses[i]) ? mFauxSoundBars[i]
								.getProgress() - 1 : mFauxSoundBars[i]
								.getProgress() + 1);
				saveFauxValue(i);
			}
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		MainFragment.AudioChange = true;
		MainFragment.showButtons(true);
		for (int i = 0; i < AudioHelper.FAUX_SOUND.length; i++)
			if (seekBar.equals(mFauxSoundBars[i]))
				mFauxSoundTexts[i]
						.setText(String.valueOf(AudioHelper.FAUX_SOUND[i]
								.equals(AudioHelper.FAUX_HEADPHONE_PA_GAIN) ? progress - 12
								: progress - 20)
								+ context.getString(R.string.db));
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		for (int i = 0; i < AudioHelper.FAUX_SOUND.length; i++)
			if (seekBar.equals(mFauxSoundBars[i]))
				saveFauxValue(i);
	}

	private static void saveFauxValue(int i) {
		Control.runAudioFaux(
				mFauxSoundTexts[i].getText().toString()
						.replace(context.getString(R.string.db), ""),
				AudioHelper.FAUX_SOUND[i]);
	}

}
