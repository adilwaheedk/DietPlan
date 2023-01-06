package com.example.dietplan.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.dietplan.R;
import com.example.dietplan.utils.PhoneFunctionality;

public class ActivityManageMeal extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_managemeal);
		getActionBar().setDisplayHomeAsUpEnabled(true);
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

	public void foodDetails(View v) {
		PhoneFunctionality.startAnim(this, v);
		startActivity(new Intent(this, ActivityFood.class));
	}

	public void addFood(View v) {
		PhoneFunctionality.startAnim(this, v);
		startActivity(new Intent(this, ActivityAddFood.class));
	}

	public void deleteFood(View v) {
		PhoneFunctionality.startAnim(this, v);
		startActivity(new Intent(this, ActivityDeleteFood.class));
	}

	public void createMeal(View v) {
		PhoneFunctionality.startAnim(this, v);
		startActivity(new Intent(this, ActivityCreateMeal.class));
	}

	public void deleteMeal(View v) {
		PhoneFunctionality.startAnim(this, v);
		startActivity(new Intent(this, ActivityDeleteMeal.class));
	}

	public void editMeal(View v) {
		startActivity(new Intent(this, ActivityEditMeal.class));
	}
}
