package com.example.dietplan.alarm;

import java.util.Calendar;
import java.util.GregorianCalendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.dietplan.activity.ActivityAppSettings;
import com.example.dietplan.utils.IdGenerator;

public class AlertManager {

	private static final int RC_BREAKFAST = IdGenerator.generateViewId();
	private static final int RC_LUNCH = IdGenerator.generateViewId();
	private static final int RC_SNACK = IdGenerator.generateViewId();
	private static final int RC_DINNER = IdGenerator.generateViewId();
	private static String TIME_BREAKFAST, TIME_LUNCH, TIME_SNACK, TIME_DINNER;
	private static boolean WAKEUP, BREAKFAST, LUNCH, SNACK, DINNER;
	private static int alarmType;
	private static AlarmManager alarmManager;
	private static Intent intent;

	public static void scheduleAlarms(Context context) {
		getSharedPreferences(context);
		intent = new Intent(context, AlarmReceiver.class);
		alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

		if (WAKEUP)
			alarmType = AlarmManager.RTC_WAKEUP;
		else
			alarmType = AlarmManager.RTC;
		if (BREAKFAST)
			setAlarm(context, RC_BREAKFAST, TIME_BREAKFAST);
		if (LUNCH)
			setAlarm(context, RC_LUNCH, TIME_LUNCH);
		if (SNACK)
			setAlarm(context, RC_SNACK, TIME_SNACK);
		if (DINNER)
			setAlarm(context, RC_DINNER, TIME_DINNER);
	}

	public static void cancelAlarms(Context context) {
		if (BREAKFAST)
			cancelAlarm(context, RC_BREAKFAST);
		if (LUNCH)
			cancelAlarm(context, RC_LUNCH);
		if (SNACK)
			cancelAlarm(context, RC_SNACK);
		if (DINNER)
			cancelAlarm(context, RC_DINNER);
	}

	private static void getSharedPreferences(Context context) {
		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
		WAKEUP = sharedPrefs.getBoolean(ActivityAppSettings.WAKEUP, false);
		BREAKFAST = sharedPrefs.getBoolean(ActivityAppSettings.CHECK_BREAKFAST, false);
		LUNCH = sharedPrefs.getBoolean(ActivityAppSettings.CHECK_LUNCH, false);
		SNACK = sharedPrefs.getBoolean(ActivityAppSettings.CHECK_SNACK, false);
		DINNER = sharedPrefs.getBoolean(ActivityAppSettings.CHECK_DINNER, false);
		TIME_BREAKFAST = sharedPrefs.getString(ActivityAppSettings.TIME_BREAKFAST, null);
		TIME_LUNCH = sharedPrefs.getString(ActivityAppSettings.TIME_LUNCH, null);
		TIME_SNACK = sharedPrefs.getString(ActivityAppSettings.TIME_SNACK, null);
		TIME_DINNER = sharedPrefs.getString(ActivityAppSettings.TIME_DINNER, null);
	}

	private static void setAlarm(Context context, int req_code, String time) {
		if (time != null) {
			String[] splitter = time.split(":");
			Calendar cal = new GregorianCalendar();
			cal.setTimeInMillis(System.currentTimeMillis());
			cal.set(Calendar.HOUR_OF_DAY, Integer.valueOf(splitter[0]));
			cal.set(Calendar.MINUTE, Integer.valueOf(splitter[1]));
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			if (System.currentTimeMillis() > cal.getTimeInMillis())
				cal.setTimeInMillis(cal.getTimeInMillis() + 24 * 60 * 60 * 1000);
			PendingIntent pi = PendingIntent.getBroadcast(context, req_code, intent,
					PendingIntent.FLAG_UPDATE_CURRENT);
			alarmManager.set(alarmType, cal.getTimeInMillis(), pi);
		}
	}

	private static void cancelAlarm(Context context, int req_code) {
		PendingIntent pi = PendingIntent.getBroadcast(context, req_code, intent,
				PendingIntent.FLAG_UPDATE_CURRENT);
		alarmManager.cancel(pi);
	}
}
