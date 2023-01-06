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
import com.example.dietplan.database.FoodCRUD;
import com.example.dietplan.utils.PhoneFunctionality;

public class ActivityDeleteFood extends Activity {

	private Spinner foodtype, foods;
	private Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_deletefood);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		foodtype = (Spinner) findViewById(R.id.spinnerDelFoodType);
		foods = (Spinner) findViewById(R.id.spinnerDelFood);
		context = getApplicationContext();
		setFoodTypeAdapter();
		setFoodsAdapter();
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

	public void deleteFood(View v) {
		if (foods.getCount() > 0) {
			FoodCRUD crud = new FoodCRUD(this);
			crud.openDB();
			int deleted = crud.deleteFood(foodtype.getSelectedItem().toString(), foods
					.getSelectedItem().toString());
			crud.closeDB();
			if (deleted != 0)
				PhoneFunctionality.makeToast(context, "Food Deleted");
			else
				PhoneFunctionality.makeToast(context, "Food Failed To Delete");
			refreshFood(v);
		} else
			PhoneFunctionality.makeToast(context, "Add Food First");
	}

	public void refreshFood(View v) {
		foodtype.setSelection(0);
	}

	private void setFoodsAdapter() {
		FoodCRUD crud = new FoodCRUD(this);
		crud.openDB();
		ArrayAdapter<String> foodsAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, crud.getFoodNames(foodtype.getSelectedItem()
						.toString()));
		foods.setAdapter(foodsAdapter);
		crud.closeDB();
	}

	private void setFoodTypeAdapter() {
		FoodCRUD crud = new FoodCRUD(this);
		crud.openDB();
		ArrayAdapter<String> foodTypeAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, crud.getFoodTypes());
		foodtype.setAdapter(foodTypeAdapter);
		crud.closeDB();
	}

	private void setSpinnerListener() {
		foodtype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
				setFoodsAdapter();
			}

			public void onNothingSelected(AdapterView<?> parent) {
				parent.setSelection(0);
			}
		});
	}
}
