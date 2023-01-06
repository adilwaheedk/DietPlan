package com.example.dietplan.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.dietplan.R;
import com.example.dietplan.utils.Calculate;
import com.example.dietplan.utils.PhoneFunctionality;

public class ActivityProfile extends Activity {

	// Shared Preferences Keys
	public static final String Name = "name";
	public static final String Gender = "gender";
	public static final String Height = "height";
	public static final String Weight = "weight";
	public static final String Age = "age";
	public static final String Active = "active";
	public static final String Goal = "goal";
	public static final String Auto = "auto";
	public static final String BMI = "bmi";
	public static final String BMR = "bmr";
	public static final String Intake = "intake";

	private Context context;
	private TextView name, age, weight, height, inches, calvalue;
	private RadioButton male, female, cms, kg, mgoal, lgoal, ggoal, autocal, manualcal;
	private RadioGroup heightgrp, weightgrp, calgrp;
	private View callayout;
	private Spinner active;

	private float height_cm, weight_kg;
	private int age_yrs;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		context = getApplicationContext();
		initResources();
		getSavedProfile();
		setListeners();
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

	private void initResources() {
		name = (TextView) findViewById(R.id.fullname);
		age = (TextView) findViewById(R.id.age);
		weight = (TextView) findViewById(R.id.weight);
		height = (TextView) findViewById(R.id.height);
		inches = (TextView) findViewById(R.id.heighti);
		calvalue = (TextView) findViewById(R.id.calvalue);
		autocal = (RadioButton) findViewById(R.id.autocal);
		manualcal = (RadioButton) findViewById(R.id.manualcal);
		male = (RadioButton) findViewById(R.id.male);
		female = (RadioButton) findViewById(R.id.female);
		cms = (RadioButton) findViewById(R.id.centimeters);
		kg = (RadioButton) findViewById(R.id.weightkg);
		mgoal = (RadioButton) findViewById(R.id.maintaingoal);
		lgoal = (RadioButton) findViewById(R.id.loosegoal);
		ggoal = (RadioButton) findViewById(R.id.gaingoal);
		active = (Spinner) findViewById(R.id.activitylevelspinner);
		heightgrp = (RadioGroup) findViewById(R.id.heightgrp);
		weightgrp = (RadioGroup) findViewById(R.id.weightgrp);
		calgrp = (RadioGroup) findViewById(R.id.choosecalgrp);
		callayout = (View) findViewById(R.id.callayout);
	}

	private void getSavedProfile() {
		SharedPreferences sharedprefs = PreferenceManager.getDefaultSharedPreferences(this);
		if (sharedprefs.contains(Name)) {
			name.setText(sharedprefs.getString(Name, ""));
			height.setText(String.valueOf((sharedprefs.getFloat(Height, 0))));
			weight.setText(String.valueOf((sharedprefs.getFloat(Weight, 0))));
			age.setText(String.valueOf(sharedprefs.getInt(Age, 0)));
			active.setSelection(sharedprefs.getInt(Active, 0));
			if (sharedprefs.getBoolean(Gender, false))
				male.setChecked(true);
			else
				female.setChecked(true);
			String goal = sharedprefs.getString(Goal, "");
			if (goal.contentEquals("G"))
				ggoal.setChecked(true);
			else if (goal.contentEquals("M"))
				mgoal.setChecked(true);
			else
				lgoal.setChecked(true);
			if (sharedprefs.getBoolean(Auto, false)) {
				autocal.setChecked(true);
				callayout.setVisibility(View.GONE);
			} else {
				manualcal.setChecked(true);
				callayout.setVisibility(View.VISIBLE);
				calvalue.setText(String.valueOf(sharedprefs.getFloat(Intake, 0)));
			}
		}
	}

	private void setListeners() {
		heightgrp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup arg0, int id) {
				switch (id) {
					case -1 :
						break;
					case R.id.feetinches :
						inches.setVisibility(View.VISIBLE);
						height.setHint("Height (feet)");
						inches.setHint("Height (inches)");
						if (height.getText().length() > 0) {
							float inch = Calculate.convertCmToInch(Float.parseFloat(height
									.getText().toString()));
							float[] ft_inch = Calculate.convertInchToFtInch(inch);
							height.setText(String.valueOf(ft_inch[0]));
							inches.setText(String.valueOf(ft_inch[1]));
						} else
							inches.setText("");
						break;
					case R.id.centimeters :
						inches.setVisibility(View.GONE);
						height.setHint("Height (cms)");
						float ft = 0,
						inch = 0;
						if (height.getText().length() > 0)
							ft = Float.parseFloat(height.getText().toString());
						else
							height.setText("0");
						if (inches.getText().length() > 0)
							inch = Float.parseFloat(inches.getText().toString());
						height.setText(String.valueOf(Calculate.convertFtInchToCm(ft, inch)));
						break;
					default :
						break;
				}
			}
		});

		weightgrp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup arg0, int id) {
				switch (id) {
					case -1 :
						break;
					case R.id.weightpounds :
						weight.setHint("Weight (lbs)");
						if (weight.getText().length() > 0)
							weight.setText(String.valueOf(Calculate.convertKgToLbs(Float
									.parseFloat(weight.getText().toString()))));
						else
							weight.setText("");
						break;
					case R.id.weightkg :
						weight.setHint("Weight (kg)");
						if (weight.getText().length() > 0)
							weight.setText(String.valueOf(Calculate.convertLbsToKg(Float
									.parseFloat(weight.getText().toString()))));
						else
							weight.setText("");
						break;
					default :
						break;
				}
			}
		});

		calgrp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup arg0, int id) {
				switch (id) {
					case R.id.autocal :
						callayout.setVisibility(View.GONE);
						break;
					case R.id.manualcal :
						callayout.setVisibility(View.VISIBLE);
						break;
					default :
						break;
				}
			}
		});
	}
	// Auto assign goal Button
	public void assignGoal(View v) {
		PhoneFunctionality.hideKeyboard(this);
		if (!isTextViewEmpty())
			if (isValueCorrect()) {
				float BMI_val = Calculate.getBMIValue(weight_kg, height_cm);
				if (BMI_val < 18.5)
					ggoal.setChecked(true);
				else if (BMI_val < 25)
					mgoal.setChecked(true);
				else
					lgoal.setChecked(true);
			}
	}

	// Save Profile Button
	public void saveProfile(View v) {
		PhoneFunctionality.hideKeyboard(this);
		if (!isTextViewEmpty())
			if (isValueCorrect()) {
				SharedPreferences sharedprefs = PreferenceManager.getDefaultSharedPreferences(this);
				Editor editor = sharedprefs.edit();
				editor.putString(Name, name.getText().toString().trim());
				editor.putFloat(Height, height_cm);
				editor.putFloat(Weight, weight_kg);
				editor.putInt(Age, age_yrs);
				editor.putInt(Active, active.getSelectedItemPosition());

				Boolean isMale = false;
				if (male.isChecked())
					isMale = true;
				editor.putBoolean(Gender, isMale);

				String goal;
				if (ggoal.isChecked())
					goal = "G";
				else if (mgoal.isChecked())
					goal = "M";
				else
					goal = "L";
				editor.putString(Goal, goal);

				editor.putFloat(BMI, Calculate.getBMIValue(weight_kg, height_cm));
				float bmr_val = Calculate.getBMRValue(weight_kg, height_cm, age_yrs,
						male.isChecked());
				editor.putFloat(BMR, bmr_val);

				if (autocal.isChecked()) {
					editor.putBoolean(Auto, true);
					editor.putFloat(Intake,
							Calculate.getDailyIntake(bmr_val, active.getSelectedItemPosition()));
				} else {
					editor.putBoolean(Auto, false);
					editor.putFloat(Intake, Float.valueOf(calvalue.getText().toString()));
				}

				editor.commit();
				startActivity(new Intent(this, ActivityHome.class));
			}
	}

	public void defaultValues(View v) {
		name.setText("");
		height.setText("");
		inches.setText("");
		age.setText("");
		weight.setText("");
		cms.setChecked(true);
		kg.setChecked(true);
		male.setChecked(true);
		mgoal.setChecked(true);
		active.setSelection(0);
	}

	private boolean isTextViewEmpty() {
		if (name.getText().toString().isEmpty() || age.getText().toString().isEmpty()
				|| height.getText().toString().isEmpty()
				|| (inches.getText().toString().isEmpty() && !cms.isChecked())
				|| weight.getText().toString().isEmpty()) {
			PhoneFunctionality.makeToast(context, "Check fields");
			return true;
		} else
			return false;
	}

	private boolean isValueCorrect() {
		if (!isHeightValid()) {
			PhoneFunctionality.makeToast(context, "Incorrect Height");
			return false;
		}
		if (!isWeightValid()) {
			PhoneFunctionality.makeToast(context, "Incorrect Weight");
			return false;
		}
		if (!isAgeValid()) {
			PhoneFunctionality.makeToast(context, "Incorrect Age");
			return false;
		}
		return true;
	}

	private boolean isHeightValid() {
		if (cms.isChecked())
			height_cm = Float.parseFloat(height.getText().toString());
		else
			height_cm = Calculate.convertFtInchToCm(Float.parseFloat(height.getText().toString()),
					Float.parseFloat(inches.getText().toString()));

		if (height_cm >= 134.62 && height_cm <= 218.44)
			return true;
		else
			return false;
	}

	private boolean isWeightValid() {
		if (kg.isChecked())
			weight_kg = Float.parseFloat(weight.getText().toString());
		else
			weight_kg = Calculate.convertLbsToKg(Float.parseFloat(weight.getText().toString()));
		if (weight_kg >= 10 && weight_kg <= 200)
			return true;
		else
			return false;
	}

	private boolean isAgeValid() {
		age_yrs = Integer.parseInt(age.getText().toString());
		if (age_yrs >= 4 && age_yrs <= 90)
			return true;
		else
			return false;
	}
}
