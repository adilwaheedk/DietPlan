package com.example.dietplan.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.dietplan.R;
import com.example.dietplan.activity.ActivityEditMeal;
import com.example.dietplan.database.Food;

public class EditMealListAdapter extends BaseAdapter {

	private Context context;
	private List<Food> data;
	public EditMealListAdapter(Context context, List<Food> data) {
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.list_meal_edit, null);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		} else
			holder = (ViewHolder) convertView.getTag();

		final Food f = (Food) getItem(position);
		holder.name.setText(f.getName());
		holder.cal.setText(String.valueOf(f.getCalories()));
		holder.size.setText(f.getSize());
		holder.qty.setText(String.valueOf(f.getQuantity()));
		holder.check.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				f.setName(holder.name.getText().toString());
				f.setCalories(Integer.parseInt(holder.cal.getText().toString()));
				f.setSize(holder.size.getText().toString());
				f.setQuantity(Integer.parseInt(holder.qty.getText().toString()));
				if (holder.check.isChecked()) {
					setViewEnable(holder, false);
					ActivityEditMeal.addFoodInMeal(f);
				} else {
					setViewEnable(holder, true);
					ActivityEditMeal.removeFoodInMeal(f);
				}
			}
		});

		return convertView;
	}

	private void setViewEnable(ViewHolder holder, Boolean istrue) {
		holder.name.setEnabled(istrue);
		holder.cal.setEnabled(istrue);
		holder.size.setEnabled(istrue);
		holder.qty.setEnabled(istrue);
	}

	class ViewHolder {
		protected TextView name, size, qty, cal;
		protected CheckBox check;

		ViewHolder(View v) {
			name = (TextView) v.findViewById(R.id.editmealname);
			cal = (TextView) v.findViewById(R.id.editmealcal);
			size = (TextView) v.findViewById(R.id.editmealsize);
			qty = (TextView) v.findViewById(R.id.editmealqty);
			check = (CheckBox) v.findViewById(R.id.editmealcheckBox);
		}
	}
}
