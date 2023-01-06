package com.example.dietplan.adapter;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.example.dietplan.R;
import com.example.dietplan.database.Food;

public class FoodListAdapter extends BaseExpandableListAdapter {

	private Context context;
	private List<String> parentData;
	private HashMap<String, List<Food>> childData;

	public FoodListAdapter(Context context, List<String> parentData,
			HashMap<String, List<Food>> childData) {
		this.context = context;
		this.parentData = parentData;
		this.childData = childData;
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return this.childData.get(this.parentData.get(groupPosition)).get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, final int childPosition, boolean isLastChild,
			View convertView, ViewGroup parent) {

		final ViewHolder holder;
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) this.context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.list_food_child, null);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		} else
			holder = (ViewHolder) convertView.getTag();

		final Food f = (Food) getChild(groupPosition, childPosition);

		holder.name.setText(f.getName());
		holder.cal.setText(String.valueOf(f.getCalories()));
		holder.size.setText("[" + f.getSize() + "]");
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
			convertView = inflater.inflate(R.layout.list_food_parent, null);
		}

		TextView foodtype = (TextView) convertView.findViewById(R.id.foodtype);
		foodtype.setText(groupTitle);
		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

	class ViewHolder {
		protected TextView name, cal, size;

		ViewHolder(View v) {
			name = (TextView) v.findViewById(R.id.foodname);
			cal = (TextView) v.findViewById(R.id.foodcal);
			size = (TextView) v.findViewById(R.id.foodsize);
		}
	}
}
