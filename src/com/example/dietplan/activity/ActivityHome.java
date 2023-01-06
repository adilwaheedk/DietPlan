package com.example.dietplan.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.dietplan.R;
import com.example.dietplan.utils.PhoneFunctionality;

public class ActivityHome extends Activity {
	private TextView profilename;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);

		SharedPreferences sharedprefs = PreferenceManager.getDefaultSharedPreferences(this);
		profilename = (TextView) findViewById(R.id.profilenametext);
		profilename.setText("Welcome " + sharedprefs.getString(ActivityProfile.Name, ""));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_home, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.home_action_settings :
				settingsBtn();
				return true;
			default :
				return super.onOptionsItemSelected(item);
		}
	}

	public void profileBtn(View v) {
		PhoneFunctionality.startAnim(this, v);
		startActivity(new Intent(this, ActivityProfile.class));
	}

	public void dietPlanBtn(View v) {
		PhoneFunctionality.startAnim(this, v);
		startActivity(new Intent(this, ActivityDietPlan.class));
	}

	public void mealBtn(View v) {
		PhoneFunctionality.startAnim(this, v);
		startActivity(new Intent(this, ActivityManageMeal.class));
	}

	public void settingsBtn() {
		startActivity(new Intent(this, ActivityAppSettings.class));
	}
}
