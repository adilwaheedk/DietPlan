package com.example.dietplan.database;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class FoodCRUD {

	private DatabaseHelper dbh;

	public FoodCRUD(Context context) {
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

	public void addFood(String table_name, Food f) {
		ContentValues cv = putValuesIn(f);
		dbh.sqldb.insert(table_name, null, cv);
	}

	public int updateFood(String table_name, Food f) {
		ContentValues cv = putValuesIn(f);
		int noOfRows = dbh.sqldb.update(table_name, cv, DatabaseHelper.NAME + " =? ",
				new String[]{f.getName()});
		return noOfRows;
	}

	public int deleteFood(String table_name, String food_name) {
		return dbh.sqldb.delete(table_name, DatabaseHelper.NAME + " =? ", new String[]{food_name});
	}

	public void addFoodType(String food_type) {
		ContentValues cv = new ContentValues();
		cv.put(DatabaseHelper.NAME, food_type);
		dbh.sqldb.insert(DatabaseHelper.TABLE_FOODTYPES, null, cv);
	}

	public int deleteFoodType(String food_type) {
		return dbh.sqldb.delete(DatabaseHelper.TABLE_FOODTYPES, DatabaseHelper.NAME + " =? ",
				new String[]{food_type});
	}

	public boolean isFoodExist(String table_name) {
		Cursor c = dbh.sqldb.query(table_name, new String[]{DatabaseHelper.ID}, null, null, null,
				null, null, null);
		if (c.getCount() > 0)
			return true;
		else
			return false;
	}

	public int getCalories(String table_name, String food_name) {
		Cursor c = dbh.sqldb.query(table_name, new String[]{DatabaseHelper.CALORIES},
				DatabaseHelper.NAME + " =? ", new String[]{food_name}, null, null, null, null);
		if (c.moveToFirst()) {
			return c.getInt(0);
		} else
			return 0;
	}

	public ArrayList<String> getFoodTypes() {
		ArrayList<String> list = new ArrayList<String>();
		String selectQuery = "SELECT name FROM " + DatabaseHelper.TABLE_FOODTYPES;
		Cursor c = dbh.sqldb.rawQuery(selectQuery, null);
		while (c.moveToNext()) {
			list.add(c.getString(0));
		}
		c.close();
		return list;
	}

	public ArrayList<Food> getFoods(String table_name) {
		ArrayList<Food> list = new ArrayList<Food>();
		String selectQuery = "SELECT * FROM " + table_name;
		Cursor c = dbh.sqldb.rawQuery(selectQuery, null);
		while (c.moveToNext()) {
			list.add(getDataFromCursor(c));
		}
		c.close();
		return list;
	}

	public ArrayList<String> getFoodNames(String table_name) {
		ArrayList<String> list = new ArrayList<String>();
		String selectQuery = "SELECT " + DatabaseHelper.NAME + " FROM " + table_name;
		Cursor c = dbh.sqldb.rawQuery(selectQuery, null);
		while (c.moveToNext()) {
			list.add(c.getString(c.getColumnIndexOrThrow(DatabaseHelper.NAME)));
		}
		c.close();
		return list;
	}

	private ContentValues putValuesIn(Food f) {
		ContentValues cv = new ContentValues();
		cv.put(DatabaseHelper.NAME, f.getName());
		cv.put(DatabaseHelper.SIZE, f.getSize());
		cv.put(DatabaseHelper.CALORIES, f.getCalories());
		return cv;
	}

	private Food getDataFromCursor(Cursor c) {
		Food obj = new Food();
		obj.setName(c.getString(c.getColumnIndexOrThrow(DatabaseHelper.NAME)));
		obj.setSize(c.getString(c.getColumnIndexOrThrow(DatabaseHelper.SIZE)));
		obj.setCalories(c.getInt(c.getColumnIndexOrThrow(DatabaseHelper.CALORIES)));
		return obj;
	}
}
