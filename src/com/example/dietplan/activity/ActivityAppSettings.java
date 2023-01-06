package com.example.dietplan.activity;

import android.content.Context;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;
import android.view.Menu;
import android.view.MenuItem;

import com.example.dietplan.R;
import com.example.dietplan.alarm.AlertManager;
import com.example.dietplan.database.DietCRUD;
import com.example.dietplan.utils.PhoneFunctionality;

public class ActivityAppSettings extends PreferenceActivity {

	public static String SWITCH_ALERT = "prefALert";
	public static String CHECK_BREAKFAST = "prefBreakfastALert";
	public static String CHECK_LUNCH = "prefLunchALert";
	public static String CHECK_SNACK = "prefSnacksALert";
	public static String CHECK_DINNER = "prefDinnerALert";
	public static String TIME_BREAKFAST = "prefBreakfast";
	public static String TIME_LUNCH = "prefLunch";
	public static String TIME_SNACK = "prefSnacks";
	public static String TIME_DINNER = "prefDinner";
	public static String RESET_SCHEDULE = "prefResetSchedule";
	public static String SOUND = "prefSound";
	public static String VIBRATE = "prefVibrate";
	public static String WAKEUP = "prefWakeup";
	public static int PREFS_FILE = R.xml.app_settings;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getFragmentManager().beginTransaction()
				.replace(android.R.id.content, new MyPreferenceFragment()).commit();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home :
				finish();
				return true;
			default :
				return super.onOptionsItemSelected(item);
		}
	}

	public static class MyPreferenceFragment extends PreferenceFragment {
		@Override
		public void onCreate(final Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			addPreferencesFromResource(PREFS_FILE);
			final Context context = MyPreferenceFragment.this.getActivity().getApplicationContext();
			final SwitchPreference switchAlerts = (SwitchPreference) this.getPreferenceManager()
					.findPreference(SWITCH_ALERT);
			switchAlerts.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
				@Override
				public boolean onPreferenceChange(Preference preference, Object newValue) {
					if (newValue instanceof Boolean) {
						Boolean checked = (Boolean) newValue;
						if (checked)
							enableAlert(context);
						else
							disableAlert(context);
					}
					return true;
				}
			});

			Preference myPref = (Preference) findPreference(RESET_SCHEDULE);
			myPref.setOnPreferenceClickListener(new OnPreferenceClickListener() {
				public boolean onPreferenceClick(Preference preference) {
					DietCRUD crud = new DietCRUD(context);
					crud.openDB();
					crud.clearScheduleTable();
					crud.closeDB();
					PhoneFunctionality.makeToast(context, "Schedule Cleared");
					return true;
				}
			});
		}
	}

	private static void enableAlert(Context context) {
		PhoneFunctionality.showNotification(ActivityHome.class, context,
				PhoneFunctionality.ALERT_NOTIF_ID, R.drawable.meal_time, "Meal alerts enabled",
				"Touch to show application", false);
		AlertManager.scheduleAlarms(context);
	}

	private static void disableAlert(Context context) {
		PhoneFunctionality.hideNotification(context, PhoneFunctionality.ALERT_NOTIF_ID);
		AlertManager.cancelAlarms(context);
	}

}