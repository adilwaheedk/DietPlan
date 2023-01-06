package com.example.dietplan.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.dietplan.R;
import com.example.dietplan.database.MealCRUD;
import com.example.dietplan.utils.PhoneFunctionality;

public class ActivityDeleteMeal extends Activity {

	private Spinner mealtype, meals;
	private Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_deletemeal);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		mealtype = (Spinner) findViewById(R.id.spinnerDelMealType);
		meals = (Spinner) findViewById(R.id.spinnerDelMeal);
		context = getApplicationContext();
		setSpinnerListener();
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

	public void deleteMeal(View v) {
		if (meals.getCount() > 0) {
			MealCRUD crud = new MealCRUD(this);
			crud.openDB();
			int deleted = crud.deleteMeal(mealtype.getSelectedItem().toString(), meals
					.getSelectedItem().toString());
			crud.closeDB();
			if (deleted != 0)
				PhoneFunctionality.makeToast(context, "Meal Deleted");
			else
				PhoneFunctionality.makeToast(context, "Meal Failed To Delete");
			refreshMeal(v);
		} else
			PhoneFunctionality.makeToast(context, "Create Meal First");
	}

	public void refreshMeal(View v) {
		setAdapter();
	}

	private void setSpinnerListener() {
		mealtype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
				setAdapter();
			}

			public void onNothingSelected(AdapterView<?> parent) {
				parent.setSelection(0);
			}
		});
	}

	private void setAdapter() {
		MealCRUD crud = new MealCRUD(this);
		crud.openDB();
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, crud.getMealsName(mealtype.getSelectedItem()
						.toString()));
		meals.setAdapter(adapter);
		crud.closeDB();
	}
}
