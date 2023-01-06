package com.example.dietplan.adapter;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.dietplan.R;
import com.example.dietplan.database.Diet;
import com.example.dietplan.utils.Formatter;

public class ScheduleListAdapter extends BaseExpandableListAdapter {

	private Context context;
	private List<String> parentData;
	private List<Integer> grandtotal;
	private HashMap<String, List<Diet>> childData;

	public ScheduleListAdapter(Context context, List<String> parentData,
			HashMap<String, List<Diet>> childData, List<Integer> grandtotal) {
		this.context = context;
		this.parentData = parentData;
		this.childData = childData;
		this.grandtotal = grandtotal;
	}

	@Override
	public Object getChild(int groupPosition, int childPosititon) {
		return this.childData.get(this.parentData.get(groupPosition)).get(childPosititon);
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
			convertView = inflater.inflate(R.layout.list_schedule_child, null);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		} else
			holder = (ViewHolder) convertView.getTag();
		convertView.setLongClickable(true);
		final Diet obj = (Diet) getChild(groupPosition, childPosition);

		holder.mealtype.setText(obj.getMealType());
		holder.diet.setText(Formatter.FormatDiet(obj.getName()));
		holder.cal.setText(String.valueOf(obj.getCalories()));
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
			convertView = inflater.inflate(R.layout.list_schedule_parent, null);
		}
		convertView.setLongClickable(false);
		TextView day = (TextView) convertView.findViewById(R.id.scheduleday);
		TextView cals = (TextView) convertView.findViewById(R.id.scheduletotal);

		day.setText(groupTitle);
		cals.setText(grandtotal.get(groupPosition).toString());

		if (isExpanded)
			convertView.setBackgroundResource(R.color.green_2);
		else
			convertView.setBackgroundResource(R.color.mahroon);
		return convertView;
	}

	public View getViewByPosition(int pos, ListView listView) {
		final int firstListItemPosition = listView.getFirstVisiblePosition();
		final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

		if (pos < firstListItemPosition || pos > lastListItemPosition) {
			return listView.getAdapter().getView(pos, null, listView);
		} else {
			final int childIndex = pos - firstListItemPosition;
			return listView.getChildAt(childIndex);
		}
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

	static class ViewHolder {
		protected TextView mealtype, diet, cal;

		ViewHolder(View v) {
			mealtype = (TextView) v.findViewById(R.id.schedulemealtype);
			diet = (TextView) v.findViewById(R.id.schedulediet);
			cal = (TextView) v.findViewById(R.id.schedulecal);
		}
	}
}
