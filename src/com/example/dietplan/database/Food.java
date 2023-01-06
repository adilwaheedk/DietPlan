package com.example.dietplan.database;

public class Food {
	private long _id;
	private String Name;
	private String Size;
	private int Calories;
	private int Quantity;

	public Food() {
		Calories = 0;
		Quantity = 1;
	}

	public void setId(long _id) {
		this._id = _id;
	}

	public void setName(String Name) {
		this.Name = Name;
	}

	public void setSize(String Size) {
		this.Size = Size;
	}

	public void setCalories(int Calories) {
		this.Calories = Calories;
	}

	public void setQuantity(int Quantity) {
		this.Quantity = Quantity;
	}

	public long getId() {
		return _id;
	}

	public String getName() {
		return Name;
	}

	public String getSize() {
		return Size;
	}

	public int getCalories() {
		return Calories;
	}

	public int getQuantity() {
		return Quantity;
	}
}
