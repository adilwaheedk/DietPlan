package com.example.dietplan.activity;

import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.Window;

import com.example.dietplan.R;
import com.example.dietplan.database.DatabaseHelper;

public class ActivitySplash extends Activity {

	private static int secs_delay = 2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_splash);
		SharedPreferences sharedprefs = PreferenceManager.getDefaultSharedPreferences(this);

		final Object activity;
		final Context context = this;

		if (sharedprefs.contains(ActivityProfile.Name))
			activity = ActivityHome.class;
		else
			activity = ActivityProfile.class;
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {

				DatabaseHelper dbh = new DatabaseHelper(context);
				try {
					dbh.createDatabase();
				} catch (IOException e) {
					e.printStackTrace();
				}
				startActivity(new Intent(ActivitySplash.this, (Class<?>) activity));
				finish();
			}
		}, secs_delay * 1000);
	}
}
