package com.example.dietplan.database;

public class Meal {
	private String MealType;
	private String Name;
	private int Calories;

	public Meal() {
		Calories = 0;
	}

	public void setMealType(String MealType) {
		this.MealType = MealType;
	}

	public void setName(String Name) {
		this.Name = Name;
	}

	public void setCalories(int Calories) {
		this.Calories = Calories;
	}

	public String getMealType() {
		return MealType;
	}

	public String getName() {
		return Name;
	}

	public int getCalories() {
		return Calories;
	}
}
