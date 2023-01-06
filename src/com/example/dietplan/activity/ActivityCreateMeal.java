package com.example.dietplan.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.dietplan.R;
import com.example.dietplan.adapter.MealListAdapter;
import com.example.dietplan.adapter.SelectedMealListAdapter;
import com.example.dietplan.database.Food;
import com.example.dietplan.database.FoodCRUD;
import com.example.dietplan.database.Meal;
import com.example.dietplan.database.MealCRUD;
import com.example.dietplan.utils.Formatter;
import com.example.dietplan.utils.PhoneFunctionality;

public class ActivityCreateMeal extends Activity {

	private static ArrayList<Food> foods;
	private static int meal_calories;
	private static TextView textviewcals;
	private static Context context;
	private static ListView mylistview;
	private static SelectedMealListAdapter adapter;

	private Spinner mealtype;
	private List<String> parentData;
	private HashMap<String, List<Food>> childData;
	private ExpandableListView listView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_createmeal);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		textviewcals = (TextView) findViewById(R.id.caloriesValue);
		mealtype = (Spinner) findViewById(R.id.spinnerMealType);
		listView = (ExpandableListView) findViewById(R.id.expandableListViewMeal);
		mylistview = (ListView) findViewById(R.id.listViewSelectedMeal);
		context = getApplicationContext();

		initValues();
		prepareFoodList();
		setListAdapter();
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

	// Save Meal Button
	public void saveMeal(View v) {
		if (!foods.isEmpty()) {
			String meal_name = Formatter.ConvertFoodsToMeal(foods);
			Meal meal = new Meal();
			meal.setCalories(meal_calories);
			meal.setName(meal_name);
			meal.setMealType(mealtype.getSelectedItem().toString());
			MealCRUD crud = new MealCRUD(this);
			crud.openDB();
			if (!crud.isMealExist(meal)) {
				crud.addMeal(meal);
				PhoneFunctionality.makeToast(context, "New Meal Created");
			} else {
				crud.updateMeal(meal, meal.getMealType());
				PhoneFunctionality.makeToast(context, "Meal Updated");
			}
			crud.closeDB();
			clearMeal(v);
		} else
			PhoneFunctionality.makeToast(context, "No Item Selected");
	}

	// Clear Button
	public void clearMeal(View v) {
		initValues();
		setListAdapter();
	}

	public static void addFoodInMeal(Food f) {
		updateCalories(meal_calories += f.getCalories() * f.getQuantity());
		foods.add(f);
		updateSelectedMeal();
	}

	public static void removeFoodInMeal(Food f) {
		updateCalories(meal_calories -= f.getCalories() * f.getQuantity());
		foods.remove(f);
		if (foods.size() > 0)
			updateSelectedMeal();
		else
			PhoneFunctionality.setListViewHeight(mylistview, 0);
	}

	private static void updateSelectedMeal() {
		adapter = new SelectedMealListAdapter(context, foods);
		mylistview.setAdapter(adapter);
		PhoneFunctionality.setListViewHeightBasedOnItems(mylistview, 5);
	}

	private static void updateCalories(int cals) {
		textviewcals.setText(String.valueOf(cals));
	}

	private void initValues() {
		meal_calories = 0;
		updateCalories(meal_calories);
		mealtype.setSelection(0);
	}

	private void prepareFoodList() {
		childData = new HashMap<String, List<Food>>();
		FoodCRUD crud = new FoodCRUD(this);
		crud.openDB();
		parentData = crud.getFoodTypes();
		for (int i = 0; i < parentData.size(); i++)
			childData.put(parentData.get(i), crud.getFoods(parentData.get(i)));
		crud.closeDB();
	}

	private void setListAdapter() {
		if (parentData != null & childData != null) {
			listView.setAdapter(new MealListAdapter(this, parentData, childData));
			// for (int i = 0; i < parentData.size(); i++)
			// listView.expandGroup(i);

			// Selected Items list
			foods = new ArrayList<Food>();
			adapter = new SelectedMealListAdapter(context, foods);
			mylistview.setAdapter(adapter);
			PhoneFunctionality.setListViewHeight(mylistview, 0);

		} else
			PhoneFunctionality.makeToast(context, "Empty Food List");
	}
}
