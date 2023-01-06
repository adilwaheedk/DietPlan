package com.example.dietplan.utils;

import java.util.ArrayList;
import java.util.List;

import com.example.dietplan.database.Food;

public class Formatter {
	public static String ConvertFoodsToMeal(List<Food> foods) {
		String meal = new String();
		for (int i = 0; i < foods.size(); i++) {
			Food f = foods.get(i);
			meal = meal.concat(f.getName() + "+" + f.getSize() + "+" + f.getQuantity() + "+"
					+ f.getCalories());
			if (i < foods.size() - 1)
				meal = meal.concat("\n");
		}
		return meal;
	}

	public static List<Food> ConvertMealToFoods(String meal) {
		List<Food> list = new ArrayList<Food>();
		String[] foods = meal.split("\n");
		for (int i = 0; i < foods.length; i++) {
			Food f = new Food();
			String[] splitter = foods[i].split("\\+");
			f.setName(splitter[0]);
			f.setSize(splitter[1]);
			f.setQuantity(Integer.parseInt(splitter[2]));
			f.setCalories(Integer.parseInt(splitter[3]));
			list.add(f);
		}
		return list;
	}

	public static String FormatDiet(String diet) {
		String string = "";
		String[] foods = diet.split("\n");
		for (int i = 0; i < foods.length; i++) {
			String[] splitter = foods[i].split("\\+");
			string += splitter[0];
			string += " " + splitter[1];
			string += " x" + splitter[2];
			string += " (" + splitter[3] + ")";
			if (i < foods.length - 1)
				string += "\n";
		}
		return string;
	}
}
