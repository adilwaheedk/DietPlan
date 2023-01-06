package com.example.dietplan.activity;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.dietplan.R;
import com.example.dietplan.database.Diet;
import com.example.dietplan.database.DietCRUD;
import com.example.dietplan.database.Meal;
import com.example.dietplan.database.MealCRUD;
import com.example.dietplan.utils.IntentHelper;
import com.example.dietplan.utils.PhoneFunctionality;

public class ActivityDialogEditDiet extends Activity {

	private Spinner spinnerEditDiet;
	private MealCRUD crud;
	private Diet diet;
	private List<Meal> meals;
	private String meal_type, day;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialogbox_edit_diet);
		TextView tv = (TextView) findViewById(R.id.mealtypeTV);
		TextView tv1 = (TextView) findViewById(R.id.mealdayTV);
		day = tv1.getText().toString();
		tv.setText((String) IntentHelper.getObjectForKey("meal_type"));
		tv1.setText((String) IntentHelper.getObjectForKey("day"));
		meal_type = tv.getText().toString();
		day = tv1.getText().toString();
		spinnerEditDiet = (Spinner) findViewById(R.id.spinnerEditDiet);
		crud = new MealCRUD(this);
		crud.openDB();
		meals = crud.getMeals(meal_type);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, crud.getMealsName(meal_type));
		spinnerEditDiet.setAdapter(adapter);
		crud.closeDB();
	}
	public void editDiet(View v) {
		if (meals != null && meals.size() > 0) {
			int pos = spinnerEditDiet.getSelectedItemPosition();
			DietCRUD crud = new DietCRUD(this);
			crud.openDB();
			Meal meal = meals.get(pos);
			diet = new Diet();
			diet.setName(meal.getName());
			diet.setCalories(meal.getCalories());
			diet.setMealType(meal_type);
			diet.setDay(day);
			if (crud.updateDiet(diet) > 0)
				PhoneFunctionality.makeToast(this, "Diet Updated");
			else
				PhoneFunctionality.makeToast(this, "Diet Update Fail");
			crud.closeDB();
			finish();
		} else
			PhoneFunctionality.makeToast(this, "No Diet Found");
	}

	public void cancelDiet(View v) {
		finish();
	}
}
