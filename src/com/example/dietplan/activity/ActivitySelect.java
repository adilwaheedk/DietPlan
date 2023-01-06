package com.example.dietplan.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.dietplan.R;
import com.example.dietplan.database.DatabaseHelper;
import com.example.dietplan.database.Diet;
import com.example.dietplan.database.DietCRUD;
import com.example.dietplan.database.MealCRUD;
import com.example.dietplan.utils.PhoneFunctionality;

public class ActivitySelect extends Activity {

	private Spinner breakfastspinner, lunchspinner, snackspinner, dinnerspinner;
	private TextView daytextview, calselected, calsperday, goalval, lbslbl, lbsval, calsrcmnd;
	private String intake, goal;
	private ArrayList<String> days;
	private int counter, cals_slctd, cals_perday, cals_req, cals_rcmnd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		daytextview = (TextView) findViewById(R.id.daytext);
		calselected = (TextView) findViewById(R.id.calselectedval);
		calsperday = (TextView) findViewById(R.id.calsperdayval);
		calsrcmnd = (TextView) findViewById(R.id.calsrcmndval);
		goalval = (TextView) findViewById(R.id.goalval);
		lbslbl = (TextView) findViewById(R.id.nooflbslbl);
		lbsval = (TextView) findViewById(R.id.nooflbsval);
		breakfastspinner = (Spinner) findViewById(R.id.breakfastspinner);
		lunchspinner = (Spinner) findViewById(R.id.lunchspinner);
		snackspinner = (Spinner) findViewById(R.id.snackspinner);
		dinnerspinner = (Spinner) findViewById(R.id.dinnerspinner);
		getSharedPrefs();
		setDays();
		setMeals();

		calsperday.setText(intake);
		calselected.setText(String.valueOf(getTotalCalories()));
		setRecommendCalories();
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

	// Next Button
	public void nextBtn(View v) {
		PhoneFunctionality.startAnim(this, v);
		if (counter < days.size() - 1)
			counter++;
		else
			counter = 0;
		daytextview.setText(days.get(counter));
		setMeals();
	}

	// Previous Button
	public void previousBtn(View v) {
		PhoneFunctionality.startAnim(this, v);
		if (counter > 0)
			counter--;
		else
			counter = days.size() - 1;
		daytextview.setText(days.get(counter));
		setMeals();
	}

	// Save Button
	public void savedietBtn(View v) {
		if (breakfastspinner.getSelectedItem() != null && lunchspinner.getSelectedItem() != null
				&& snackspinner.getSelectedItem() != null
				&& dinnerspinner.getSelectedItem() != null) {
			if (checkCalories()) {
				String day = daytextview.getText().toString();
				Diet obj;
				DietCRUD crud = new DietCRUD(this);
				crud.openDB();
				String meal;

				meal = breakfastspinner.getSelectedItem().toString();
				obj = new Diet();
				obj.setCalories(crud.getCalories(meal));
				obj.setName(meal);
				obj.setDay(day);
				obj.setMealType(DatabaseHelper.BREAKFAST);
				if (crud.isDietExist(obj))
					crud.updateDiet(obj);
				else
					crud.addDiet(obj);

				meal = lunchspinner.getSelectedItem().toString();
				obj = new Diet();
				obj.setCalories(crud.getCalories(meal));
				obj.setName(meal);
				obj.setDay(day);
				obj.setMealType(DatabaseHelper.LUNCH);
				if (crud.isDietExist(obj))
					crud.updateDiet(obj);
				else
					crud.addDiet(obj);

				meal = snackspinner.getSelectedItem().toString();
				obj = new Diet();
				obj.setCalories(crud.getCalories(meal));
				obj.setName(meal);
				obj.setDay(day);
				obj.setMealType(DatabaseHelper.SNACK);
				if (crud.isDietExist(obj))
					crud.updateDiet(obj);
				else
					crud.addDiet(obj);

				meal = dinnerspinner.getSelectedItem().toString();
				obj = new Diet();
				obj.setCalories(crud.getCalories(meal));
				obj.setName(meal);
				obj.setDay(day);
				obj.setMealType(DatabaseHelper.DINNER);
				if (crud.isDietExist(obj))
					crud.updateDiet(obj);
				else
					crud.addDiet(obj);

				crud.closeDB();
				PhoneFunctionality.makeToast(this, day + " Diet Plan Saved!");
			} else
				PhoneFunctionality.makeToast(this, "Check Selected Calories");
		} else
			PhoneFunctionality.makeToast(this, "Check Meals");
	}
	private void getSharedPrefs() {
		SharedPreferences sharedprefs = PreferenceManager.getDefaultSharedPreferences(this);
		intake = String.valueOf(sharedprefs.getFloat(ActivityProfile.Intake, 0)).split("\\.")[0];
		goal = sharedprefs.getString(ActivityProfile.Goal, null);
		if (goal != null)
			setGoalText(goal);
	}

	private void setRecommendCalories() {
		if (lbsval.getText().length() > 0) {
			if (goal.equals("M")) {
				cals_perday = Integer.valueOf(intake);
				calsrcmnd.setText(String.valueOf(cals_perday - 500) + " to "
						+ String.valueOf(cals_perday + 500));
			} else {
				calculateCalories();
				calsrcmnd.setText(String.valueOf(cals_rcmnd));
			}
		}
	}

	private void calculateCalories() {
		cals_perday = Integer.valueOf(intake);
		cals_req = Integer.valueOf(lbsval.getText().toString()) * 500;
		if (goal.equals("G"))
			cals_rcmnd = cals_perday + cals_req;
		else if (goal.equals("L"))
			cals_rcmnd = cals_perday - cals_req;
		else
			cals_rcmnd = cals_perday;
	}

	private boolean checkCalories() {
		cals_slctd = Integer.valueOf(calselected.getText().toString());
		if (goal.equals("M")) {
			cals_perday = Integer.valueOf(intake);
			if (cals_slctd >= cals_perday - 500 && cals_slctd <= cals_perday + 500)
				return true;
		} else if (lbsval.getText().length() > 0) {
			calculateCalories();
			if (goal.equals("G")) {
				if (cals_slctd >= cals_rcmnd)
					return true;
			} else if (cals_slctd <= cals_rcmnd)
				return true;
		}
		return false;
	}

	private int getTotalCalories() {
		int total_cal = 0;
		DietCRUD crud = new DietCRUD(this);
		crud.openDB();
		if (breakfastspinner.getCount() > 0)
			total_cal += crud.getCalories(breakfastspinner.getSelectedItem().toString());
		else
			total_cal += 0;
		if (lunchspinner.getCount() > 0)
			total_cal += crud.getCalories(lunchspinner.getSelectedItem().toString());
		else
			total_cal += 0;
		if (snackspinner.getCount() > 0)
			total_cal += crud.getCalories(snackspinner.getSelectedItem().toString());
		else
			total_cal += 0;
		if (dinnerspinner.getCount() > 0)
			total_cal += crud.getCalories(dinnerspinner.getSelectedItem().toString());
		else
			total_cal += 0;

		crud.closeDB();
		return total_cal;
	}

	private void setGoalText(String goal) {
		String str;
		if (goal.equals("G"))
			str = "Gain";
		else if (goal.equals("L"))
			str = "Lose";
		else {
			str = "Maintain";
			lbslbl.setVisibility(View.INVISIBLE);
			lbsval.setVisibility(View.INVISIBLE);
		}
		goalval.setText(str);
	}

	private void setDays() {
		DietCRUD crud = new DietCRUD(this);
		crud.openDB();
		counter = 0;
		days = crud.getDays();
		daytextview.setText(days.get(counter));
		crud.closeDB();
	}

	private void setMeals() {
		MealCRUD crud = new MealCRUD(this);
		crud.openDB();
		ArrayAdapter<String> adapter;
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
				crud.getMealsName(DatabaseHelper.BREAKFAST));
		breakfastspinner.setAdapter(adapter);

		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
				crud.getMealsName(DatabaseHelper.LUNCH));
		lunchspinner.setAdapter(adapter);

		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
				crud.getMealsName(DatabaseHelper.SNACK));
		snackspinner.setAdapter(adapter);

		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
				crud.getMealsName(DatabaseHelper.DINNER));
		dinnerspinner.setAdapter(adapter);
		crud.closeDB();
	}

	private void setListeners() {

		breakfastspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				calselected.setText(String.valueOf(getTotalCalories()));
			}
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});

		lunchspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				calselected.setText(String.valueOf(getTotalCalories()));
			}
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});

		snackspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				calselected.setText(String.valueOf(getTotalCalories()));
			}
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});

		dinnerspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				calselected.setText(String.valueOf(getTotalCalories()));
			}
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});

		lbsval.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				setRecommendCalories();
				return false;
			}
		});
	}
}
