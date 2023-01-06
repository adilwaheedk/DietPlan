package com.example.dietplan.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.dietplan.R;
import com.example.dietplan.adapter.EditMealListAdapter;
import com.example.dietplan.database.Food;
import com.example.dietplan.database.Meal;
import com.example.dietplan.database.MealCRUD;
import com.example.dietplan.utils.Formatter;
import com.example.dietplan.utils.PhoneFunctionality;

public class ActivityEditMeal extends Activity {

	private static List<Food> foods;
	private static TextView textviewcals;
	private static int meal_calories;

	private Spinner mealtype, meals;
	private ListView listview;
	private EditMealListAdapter adapter;
	private Meal meal;
	private Context context;
	private MealCRUD crud;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_editmeal);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		mealtype = (Spinner) findViewById(R.id.spinnerEditMealType);
		meals = (Spinner) findViewById(R.id.spinnerEditMeal);
		listview = (ListView) findViewById(R.id.listviewEditMeal);
		textviewcals = (TextView) findViewById(R.id.editmealtotalcal);
		context = getApplicationContext();
		crud = new MealCRUD(context);
		crud.openDB();
		foods = new ArrayList<Food>();
		setSpinnerListeners();
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
				if (crud != null)
					crud.closeDB();
				finish();
				return true;
			default :
				return super.onOptionsItemSelected(item);
		}
	}

	public void editMeal(View v) {
		if (meals.getCount() > 0) {
			if (foods.size() > 0) {
				try {
					meal = new Meal();
					meal.setName(Formatter.ConvertFoodsToMeal(foods));
					meal.setCalories(Integer.parseInt(textviewcals.getText().toString()));
					meal.setMealType(mealtype.getSelectedItem().toString());
					if (crud.updateMeal(meal, meals.getSelectedItem().toString()) > 0)
						PhoneFunctionality.makeToast(context, "Meal Updated");
					else
						PhoneFunctionality.makeToast(context, "Meal Failed To Update");
					refreshMeal(v);
				} catch (Exception ex) {
					PhoneFunctionality.makeToast(context, ex.getMessage());
				}
			} else
				PhoneFunctionality.makeToast(context, "Select Food To Add In Meal");
		} else
			PhoneFunctionality.makeToast(context, "Create Meal First");
	}
	
	public void refreshMeal(View v) {
		foods.clear();
		List<String> meal_names = crud.getMealsName(mealtype.getSelectedItem().toString());
		if (meal_names.size() > 0)
			setMealsAdapter(meal_names);
		else
			PhoneFunctionality.makeToast(context, "No Meal Found");
	}

	public static void addFoodInMeal(Food f) {
		updateCalories(meal_calories += f.getCalories() * f.getQuantity());
		foods.add(f);
	}

	public static void removeFoodInMeal(Food f) {
		updateCalories(meal_calories -= f.getCalories() * f.getQuantity());
		foods.remove(f);
	}

	private static void updateCalories(int cals) {
		textviewcals.setText(String.valueOf(cals));
	}

	private void setSpinnerListeners() {
		mealtype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
				List<String> meal_names = crud.getMealsName(mealtype.getSelectedItem().toString());
				if (meal_names.size() > 0)
					setMealsAdapter(meal_names);
				else
					PhoneFunctionality.makeToast(context, "No Meal Found");
			}

			public void onNothingSelected(AdapterView<?> parent) {
				parent.setSelection(0);
			}
		});
		meals.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
				adapter = new EditMealListAdapter(context, Formatter.ConvertMealToFoods(meals
						.getSelectedItem().toString()));
				listview.setAdapter(adapter);
			}

			public void onNothingSelected(AdapterView<?> parent) {
				parent.setSelection(0);
			}
		});
	}
	
	private void setMealsAdapter(List<String> meal_names) {
		meals.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
				meal_names));
	}
}
