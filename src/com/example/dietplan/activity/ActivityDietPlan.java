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

public class ActivityDietPlan extends Activity {

	private TextView bmi, bmr, active;
	private SharedPreferences sharedprefs;
	private String name;
	private static float BMI, BMR, Intake;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dietplan);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		sharedprefs = PreferenceManager.getDefaultSharedPreferences(this);
		bmi = (TextView) findViewById(R.id.bmivalue);
		bmr = (TextView) findViewById(R.id.bmrvalue);
		active = (TextView) findViewById(R.id.activevalue);
		getPreferences();
		setValues();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu, menu);
		return true;
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

	private void getPreferences() {
		name = sharedprefs.getString(ActivityProfile.Name, "");
		BMI = sharedprefs.getFloat(ActivityProfile.BMI, 0);
		BMR = sharedprefs.getFloat(ActivityProfile.BMR, 0);
		Intake = sharedprefs.getFloat(ActivityProfile.Intake, 0);
	}

	private void setValues() {
		String bmiCategory = name;
		if (BMI < 18.5)
			bmiCategory += " is Underweight";
		else if (BMI < 25)
			bmiCategory += " has Normal Weight";
		else if (BMI < 30)
			bmiCategory += " is Overweight";
		else
			bmiCategory += " has Obesity";

		bmi.setText(bmiCategory);
		bmr.setText(String.valueOf(BMR).split("\\.")[0]);
		active.setText(String.valueOf(Intake).split("\\.")[0] + " Calories");
	}

	public void scheduleBtn(View v) {
		PhoneFunctionality.startAnim(this, v);
		startActivity(new Intent(this, ActivitySchedule.class));
	}

	public void selectdietBtn(View v) {
		PhoneFunctionality.startAnim(this, v);
		startActivity(new Intent(this, ActivitySelect.class));
	}
}
