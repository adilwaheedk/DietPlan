package com.example.dietplan.adapter;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.dietplan.R;
import com.example.dietplan.activity.ActivityCreateMeal;
import com.example.dietplan.database.Food;

public class MealListAdapter extends BaseExpandableListAdapter {

	private Context context;
	private List<String> parentData;
	private HashMap<String, List<Food>> childData;
	private SparseBooleanArray itemChecked;
	private SparseIntArray itemQuantity;

	public MealListAdapter(Context context, List<String> parentData,
			HashMap<String, List<Food>> childData) {
		this.context = context;
		this.parentData = parentData;
		this.childData = childData;
		itemChecked = new SparseBooleanArray();
		itemQuantity = new SparseIntArray();
		for (int i = 0; i < parentData.size(); i++)
			for (int j = 0; j < childData.size(); j++) {
				int key = i * 100 + j;
				itemChecked.put(key, false);
				itemQuantity.put(key, 1);
			}
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return this.childData.get(this.parentData.get(groupPosition)).get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return groupPosition * 100 + childPosition;
	}

	@Override
	public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild,
			View convertView, ViewGroup parent) {

		final ViewHolder holder;
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) this.context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.list_meal_child, null);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		} else
			holder = (ViewHolder) convertView.getTag();

		final Food f = (Food) getChild(groupPosition, childPosition);

		holder.name.setText(f.getName());
		holder.cal.setText(String.valueOf(f.getCalories()));
		holder.size.setText("[" + f.getSize() + "]");
		holder.qty.setText(String.valueOf(f.getQuantity()));

		final int childId = Integer.valueOf(String
				.valueOf(getChildId(groupPosition, childPosition)));
		holder.check.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				int qty = Integer.valueOf(holder.qty.getText().toString());
				f.setQuantity(qty);
				itemQuantity.put(childId, qty);
				if (holder.check.isChecked()) {
					holder.qty.setEnabled(false);
					itemChecked.put(childId, true);
					ActivityCreateMeal.addFoodInMeal(f);
				} else {
					holder.qty.setEnabled(true);
					itemChecked.put(childId, false);
					ActivityCreateMeal.removeFoodInMeal(f);
				}
			}
		});

		Log.e("array", String.valueOf(childId));
		holder.qty.setText(String.valueOf(itemQuantity.get(childId, 1)));
		holder.check.setChecked(itemChecked.get(childId, false));
		holder.qty.setEnabled(!itemChecked.get(childId, false));
		return convertView;
	}
	@Override
	public int getChildrenCount(int groupPosition) {
		return this.childData.get(this.parentData.get(groupPosition)).size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return this.parentData.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return this.parentData.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView,
			ViewGroup parent) {

		String groupTitle = (String) getGroup(groupPosition);
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) this.context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.list_meal_parent, null);
		}

		TextView foodtype = (TextView) convertView.findViewById(R.id.hdrfoodtype);
		foodtype.setText(groupTitle);
		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

	class ViewHolder {
		protected TextView name, cal, size, qty;
		protected CheckBox check;

		ViewHolder(View v) {
			name = (TextView) v.findViewById(R.id.foodname);
			cal = (TextView) v.findViewById(R.id.foodcal);
			size = (TextView) v.findViewById(R.id.foodsize);
			qty = (TextView) v.findViewById(R.id.foodqty);
			check = (CheckBox) v.findViewById(R.id.foodcheck);
		}
	}
}
