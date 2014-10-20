package com.pacman.performance.services;

import java.util.ArrayList;
import java.util.List;

import com.pacman.performance.R;
import com.pacman.performance.utils.Constants;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

public class NotificationStatsService extends Service implements Constants {

	final Handler hand = new Handler();
	private NotificationCompat.Builder mBuilder;
	private NotificationManager mNotificationManager;

	Runnable run = new Runnable() {
		@Override
		public void run() {
			if (mBuilder != null && mNotificationManager != null) {

				List<String> freqs = new ArrayList<String>();
				for (int i = 0; i < cpuHelper.getCoreCount(); i++)
					freqs.add(String.valueOf(cpuHelper.getCurFreq(i) / 1000));

				String freqsList = "";
				for (String freq : freqs)
					freqsList = freqsList + "|" + freq;
				mBuilder.setContentText(freqsList.substring(1));

				mNotificationManager.notify(NOTI_STATS_ID, mBuilder.build());
			}
			hand.postDelayed(run, 1000);
		}
	};

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		hand.post(run);
		buildNotification();
		super.onCreate();
	}

	@Override
	public void onDestroy() {
		if (hand != null)
			hand.removeCallbacks(run);
		if (mNotificationManager != null)
			mNotificationManager.cancel(NOTI_STATS_ID);
		super.onDestroy();
	}

	private void buildNotification() {
		mBuilder = new NotificationCompat.Builder(this)
				.setSmallIcon(R.drawable.ic_launcher)
				.setContentTitle(getString(R.string.app_name)).setOngoing(true);
		mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		mNotificationManager.notify(NOTI_STATS_ID, mBuilder.build());
	}
}
