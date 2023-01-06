package com.example.dietplan.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class MealCRUD {

	private DatabaseHelper dbh;

	public MealCRUD(Context context) {
		dbh = new DatabaseHelper(context);
	}

	public void openDB() throws NullPointerException {
		dbh.sqldb = dbh.getWritableDatabase();
	}

	public void closeDB() {
		dbh.sqldb = dbh.getReadableDatabase();
		if (dbh.sqldb != null && dbh.sqldb.isOpen())
			dbh.sqldb.close();
	}

	public void addMeal(Meal obj) {
		ContentValues cv = getContentFromObject(obj);
		dbh.sqldb.insert(DatabaseHelper.TABLE_MEAL, null, cv);
	}

	public int updateMeal(Meal obj, String old_meal_name) {
		ContentValues cv = getContentFromObject(obj);
		int noOfRows = dbh.sqldb.update(DatabaseHelper.TABLE_MEAL, cv, DatabaseHelper.NAME
				+ " =? AND " + DatabaseHelper.TYPE + "=?",
				new String[]{old_meal_name, obj.getMealType()});
		return noOfRows;
	}
	public boolean isMealExist(Meal obj) {
		Cursor c = dbh.sqldb.query(DatabaseHelper.TABLE_MEAL, new String[]{DatabaseHelper.NAME},
				DatabaseHelper.NAME + " =? AND " + DatabaseHelper.TYPE + "=?",
				new String[]{obj.getName(), obj.getMealType()}, null, null, null, null);
		if (c.getCount() > 0)
			return true;
		else
			return false;
	}

	public List<String> getMealsName(String meal_type) {
		List<String> list = new ArrayList<String>();
		String selectQuery = "SELECT " + DatabaseHelper.NAME + " FROM " + DatabaseHelper.TABLE_MEAL
				+ " WHERE " + DatabaseHelper.TYPE + " = '" + meal_type + "'";
		Cursor c = dbh.sqldb.rawQuery(selectQuery, null);
		while (c.moveToNext()) {
			list.add(c.getString(c.getColumnIndexOrThrow(DatabaseHelper.NAME)));
		}
		c.close();
		return list;
	}

	public List<Meal> getMeals(String meal_type) {
		List<Meal> list = new ArrayList<Meal>();
		String selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_MEAL + " WHERE "
				+ DatabaseHelper.TYPE + " = '" + meal_type + "'";
		Cursor c = dbh.sqldb.rawQuery(selectQuery, null);
		while (c.moveToNext()) {
			list.add(getObjectFromCursor(c));
		}
		c.close();
		return list;
	}

	public Meal getMeal(String meal_type, String meal_name) {
		String selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_MEAL + " WHERE "
				+ DatabaseHelper.TYPE + " = '" + meal_type + "' AND " + DatabaseHelper.NAME
				+ " = '" + meal_name + "'";
		Cursor c = dbh.sqldb.rawQuery(selectQuery, null);
		return getObjectFromCursor(c);
	}

	public int deleteMeal(String type, String name) {
		int noOfRows = dbh.sqldb.delete(DatabaseHelper.TABLE_MEAL, DatabaseHelper.TYPE + " =? AND "
				+ DatabaseHelper.NAME + " =? ", new String[]{type, name});
		return noOfRows;
	}

	private ContentValues getContentFromObject(Meal obj) {
		ContentValues cv = new ContentValues();
		cv.put(DatabaseHelper.TYPE, obj.getMealType());
		cv.put(DatabaseHelper.NAME, obj.getName());
		cv.put(DatabaseHelper.CALORIES, obj.getCalories());
		return cv;
	}

	private Meal getObjectFromCursor(Cursor c) {
		Meal obj = new Meal();
		obj.setMealType(c.getString(c.getColumnIndexOrThrow(DatabaseHelper.TYPE)));
		obj.setName(c.getString(c.getColumnIndexOrThrow(DatabaseHelper.NAME)));
		obj.setCalories(c.getInt(c.getColumnIndexOrThrow(DatabaseHelper.CALORIES)));
		return obj;
	}
}
