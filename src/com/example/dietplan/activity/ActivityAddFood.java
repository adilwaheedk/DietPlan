package com.example.dietplan.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.dietplan.R;
import com.example.dietplan.database.Food;
import com.example.dietplan.database.FoodCRUD;
import com.example.dietplan.utils.PhoneFunctionality;

public class ActivityAddFood extends Activity {

	private TextView foodname, foodsize, foodcal;
	private Spinner spinnerFoodtype;
	private Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_addfood);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		foodname = (TextView) findViewById(R.id.foodnameval);
		foodsize = (TextView) findViewById(R.id.foodsizeval);
		foodcal = (TextView) findViewById(R.id.foodcalval);
		spinnerFoodtype = (Spinner) findViewById(R.id.spinnerFoodType);
		context = getApplicationContext();
		initValues();
		setAdapter();
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

	// Save Food Button
	public void saveFood(View v) {
		if (foodname.getText().length() > 0 & foodsize.getText().length() > 0
				& foodcal.getText().length() > 0) {
			Food food = new Food();
			food.setName(foodname.getText().toString());
			food.setSize(foodsize.getText().toString());
			food.setCalories(Integer.valueOf(foodcal.getText().toString()));

			FoodCRUD crud = new FoodCRUD(this);
			crud.openDB();
			crud.addFood(spinnerFoodtype.getSelectedItem().toString(), food);
			PhoneFunctionality.makeToast(context, "Food Saved");
			crud.closeDB();
			clearFood(v);
		} else
			PhoneFunctionality.makeToast(context, "Check fields");
	}

	// Clear Button
	public void clearFood(View v) {
		initValues();
	}

	private void initValues() {
		foodname.setText("");
		foodsize.setText("");
		foodcal.setText("");
		spinnerFoodtype.setSelection(0);
	}

	private void setAdapter() {
		FoodCRUD crud = new FoodCRUD(this);
		crud.openDB();
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, crud.getFoodTypes());
		spinnerFoodtype.setAdapter(adapter);
		crud.closeDB();
	}
}
