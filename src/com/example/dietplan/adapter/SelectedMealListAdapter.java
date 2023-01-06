package com.example.dietplan.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.dietplan.R;
import com.example.dietplan.database.Food;

public class SelectedMealListAdapter extends BaseAdapter {

	private Context context;
	private List<Food> data;

	public SelectedMealListAdapter(Context context, List<Food> data) {
		this.context = context;
		this.data = data;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return data.get(position).getId();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.list_meal_selected, parent, false);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		} else
			holder = (ViewHolder) convertView.getTag();

		final Food f = (Food) getItem(position);
		holder.name.setText(f.getName());
		holder.cal.setText(String.valueOf(f.getCalories()));
		holder.size.setText("[" + f.getSize() + "]");
		holder.qty.setText("x" + f.getQuantity());
		return convertView;
	}

	class ViewHolder {
		protected TextView name, cal, size, qty;

		ViewHolder(View v) {
			name = (TextView) v.findViewById(R.id.selname);
			cal = (TextView) v.findViewById(R.id.selcal);
			size = (TextView) v.findViewById(R.id.selsize);
			qty = (TextView) v.findViewById(R.id.selqty);
		}
	}
}
