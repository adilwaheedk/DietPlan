package com.example.dietplan.database;

public class Diet {
	private String Day;
	private String MealType;
	private String Name;
	private int Calories;

	public Diet() {
		Calories = 0;
	}

	public void setDay(String Day) {
		this.Day = Day;
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

	public String getDay() {
		return Day;
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
