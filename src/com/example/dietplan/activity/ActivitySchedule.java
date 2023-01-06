package com.example.dietplan.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.TextView;

import com.example.dietplan.R;
import com.example.dietplan.adapter.ScheduleListAdapter;
import com.example.dietplan.database.Diet;
import com.example.dietplan.database.DietCRUD;
import com.example.dietplan.utils.IntentHelper;
import com.example.dietplan.utils.PhoneFunctionality;

public class ActivitySchedule extends Activity {

	private ScheduleListAdapter listAdapter;
	private ExpandableListView listView;
	private List<String> parentData;
	private List<Integer> grandtotal;
	private HashMap<String, List<Diet>> childData;
	private Context context;
	private static String meal_type, day;
	private Diet obj;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_schedule);
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

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.setHeaderTitle("Choose an option");
		if (v.getId() == R.id.expandableListViewSchedule) {
			MenuInflater inflater = getMenuInflater();
			inflater.inflate(R.menu.menu_list, menu);
		}
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// AdapterContextMenuInfo info = (AdapterContextMenuInfo)
		// item.getMenuInfo();
		switch (item.getItemId()) {
			case R.id.edit :
				IntentHelper.addObjectForKey(meal_type, "meal_type");
				IntentHelper.addObjectForKey(day, "day");
				startActivity(new Intent(this, ActivityDialogEditDiet.class));
				return true;
			case R.id.delete :
				DeleteDiet();
				return true;
			default :
				return super.onContextItemSelected(item);
		}
	}

	private void prepareListData() {
		parentData = new ArrayList<String>();
		childData = new HashMap<String, List<Diet>>();
		grandtotal = new ArrayList<Integer>();
		DietCRUD crud = new DietCRUD(this);
		crud.openDB();
		parentData = crud.getDays();
		String day;
		for (int i = 0; i < parentData.size(); i++) {
			day = parentData.get(i);
			List<Diet> diets = crud.getDailyDiet(day);
			int total = 0;
			for (int j = 0; j < diets.size(); j++)
				total += diets.get(j).getCalories();
			grandtotal.add(total);
			childData.put(day, diets);
		}
		crud.closeDB();
	}

	private void setListAdapter() {
		if (childData != null) {
			listView = (ExpandableListView) findViewById(R.id.expandableListViewSchedule);
			listAdapter = new ScheduleListAdapter(this, parentData, childData, grandtotal);
			listView.setAdapter(listAdapter);
			registerForContextMenu(listView);
			listView.setOnItemLongClickListener(new OnItemLongClickListener() {
				@Override
				public boolean onItemLongClick(AdapterView<?> parent, View v, int childPosition,
						long id) {
					listView.performItemClick(v, childPosition, id);
					TextView tv = (TextView) v.findViewById(R.id.schedulemealtype);
					meal_type = tv.getText().toString();
					return false;
				}
			});

			listView.setOnChildClickListener(new OnChildClickListener() {
				@Override
				public boolean onChildClick(ExpandableListView parent, View v, int groupPosition,
						int childPosition, long id) {
					day = "";
					switch (groupPosition) {
						case 0 :
							day = "Monday";
						case 1 :
							day = "Tuesday";
						case 2 :
							day = "Wednesday";
						case 3 :
							day = "Thursday";
						case 4 :
							day = "Friday";
						case 5 :
							day = "Saturday";
						case 6 :
							day = "Sunday";
					}
					obj = childData.get(parentData.get(groupPosition)).get(childPosition);
					// PhoneFunctionality.makeToast(context,
					// Formatter.FormatDiet(obj.getName())
					// + "\n" + "Contains " + obj.getCalories() + " calories");
					return false;
				}
			});

			final Object obj = IntentHelper.getObjectForKey("key_today");
			if (obj != null) {
				int pos = parentData.indexOf(obj);
				listView.expandGroup(pos, true);
			} else {
				for (int i = 0; i < parentData.size(); i++)
					listView.expandGroup(i);
			}
			listAdapter.notifyDataSetChanged();

		} else
			PhoneFunctionality.makeToast(context, "Empty list");
	}

	private void DeleteDiet() {
		DietCRUD crud = new DietCRUD(this);
		crud.openDB();
		if (crud.deleteDiet(obj) > 0)
			PhoneFunctionality.makeToast(context, "Diet Deleted");
		else
			PhoneFunctionality.makeToast(context, "Diet Not Deleted");
		crud.closeDB();
	}
}
