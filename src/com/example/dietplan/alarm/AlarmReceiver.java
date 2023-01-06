package com.example.dietplan.alarm;

import java.util.Calendar;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.dietplan.R;
import com.example.dietplan.activity.ActivityAppSettings;
import com.example.dietplan.activity.ActivitySchedule;
import com.example.dietplan.database.DatabaseHelper;
import com.example.dietplan.utils.IdGenerator;
import com.example.dietplan.utils.IntentHelper;
import com.example.dietplan.utils.PhoneFunctionality;

public class AlarmReceiver extends BroadcastReceiver {

	private static String TIME_BREAKFAST, TIME_LUNCH, TIME_SNACK, TIME_DINNER;
	private static boolean SOUND, VIBRATION;
	private Calendar calendar;

	@Override
	public void onReceive(Context context, Intent intent) {
		getSharedPreferences(context);
		calendar = Calendar.getInstance();
		String today = returnDay();
		String meal_type = null;
		if (checkTime(TIME_BREAKFAST))
			meal_type = DatabaseHelper.BREAKFAST;
		else if (checkTime(TIME_LUNCH))
			meal_type = DatabaseHelper.LUNCH;
		else if (checkTime(TIME_SNACK))
			meal_type = DatabaseHelper.SNACK;
		else if (checkTime(TIME_DINNER))
			meal_type = DatabaseHelper.DINNER;

		IntentHelper.addObjectForKey(today, "key_today");
		PhoneFunctionality.showNotification(ActivitySchedule.class, context,
				IdGenerator.generateViewId(), R.drawable.plate_small, meal_type + " time",
				"Touch for meal details", true);
		if (VIBRATION)
			PhoneFunctionality.vibrateMobile(context);
		if (SOUND)
			PhoneFunctionality.playSound(context);
	}
	private String returnDay() {
		switch (calendar.get(Calendar.DAY_OF_WEEK)) {
			case Calendar.SUNDAY :
				return DatabaseHelper.SUNDAY;
			case Calendar.MONDAY :
				return DatabaseHelper.MONDAY;
			case Calendar.TUESDAY :
				return DatabaseHelper.TUESDAY;
			case Calendar.WEDNESDAY :
				return DatabaseHelper.WEDNESDAY;
			case Calendar.THURSDAY :
				return DatabaseHelper.THURSDAY;
			case Calendar.FRIDAY :
				return DatabaseHelper.FRIDAY;
			case Calendar.SATURDAY :
				return DatabaseHelper.SATURDAY;
			default :
				return null;
		}
	}

	private boolean checkTime(String meal_type) {
		if (meal_type != null) {
			String[] splitter = meal_type.split(":");
			if (calendar.get(Calendar.HOUR_OF_DAY) == Integer.valueOf(splitter[0])
					& calendar.get(Calendar.MINUTE) == Integer.valueOf(splitter[1]))
				return true;
		}
		return false;
	}

	private void getSharedPreferences(Context context) {
		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
		SOUND = sharedPrefs.getBoolean(ActivityAppSettings.SOUND, false);
		VIBRATION = sharedPrefs.getBoolean(ActivityAppSettings.VIBRATE, false);
		TIME_BREAKFAST = sharedPrefs.getString(ActivityAppSettings.TIME_BREAKFAST, null);
		TIME_LUNCH = sharedPrefs.getString(ActivityAppSettings.TIME_LUNCH, null);
		TIME_SNACK = sharedPrefs.getString(ActivityAppSettings.TIME_SNACK, null);
		TIME_DINNER = sharedPrefs.getString(ActivityAppSettings.TIME_DINNER, null);
	}
}
