package com.example.dietplan.activity;

import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;

import com.example.dietplan.R;
import com.example.dietplan.adapter.FoodListAdapter;
import com.example.dietplan.database.Food;
import com.example.dietplan.database.FoodCRUD;
import com.example.dietplan.utils.PhoneFunctionality;

public class ActivityFood extends Activity {

	private ExpandableListView listView;
	private List<String> parentData;
	private HashMap<String, List<Food>> childData;
	private Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_food);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		context = getApplicationContext();
		prepareListData();
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

	private void prepareListData() {
		childData = new HashMap<String, List<Food>>();
		FoodCRUD crud = new FoodCRUD(this);
		crud.openDB();
		parentData = crud.getFoodTypes();
		for (int i = 0; i < parentData.size(); i++)
			childData.put(parentData.get(i), crud.getFoods(parentData.get(i)));
		crud.closeDB();
	}

	private void setListAdapter() {
		if (parentData != null && childData != null) {
			listView = (ExpandableListView) findViewById(R.id.expandableListViewCalories);
			listView.setAdapter(new FoodListAdapter(this, parentData, childData));

			listView.setOnChildClickListener(new OnChildClickListener() {
				@Override
				public boolean onChildClick(ExpandableListView parent, View v, int groupPosition,
						int childPosition, long id) {
					Food f = childData.get(parentData.get(groupPosition)).get(childPosition);
					PhoneFunctionality.makeToast(
							context,
							f.getName() + " Contains " + f.getCalories() + " Calories In "
									+ f.getSize());
					return false;
				}
			});
		} else
			PhoneFunctionality.makeToast(context, "Empty List");
	}
}
