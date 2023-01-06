package com.example.dietplan.database;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class DietCRUD {

	private DatabaseHelper dbh;

	public DietCRUD(Context context) {
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

	public void addDiet(Diet obj) {
		ContentValues cv = getContentFromObject(obj);
		dbh.sqldb.insert(DatabaseHelper.TABLE_DIET, null, cv);
	}

	public int updateDiet(Diet obj) {
		ContentValues cv = getContentFromObject(obj);
		int noOfRows = dbh.sqldb.update(DatabaseHelper.TABLE_DIET, cv, DatabaseHelper.NAME
				+ " =? AND " + DatabaseHelper.TYPE + " =? AND " + DatabaseHelper.DAY + " =?",
				new String[]{obj.getName(), obj.getMealType(), obj.getDay()});
		return noOfRows;
	}

	public boolean isDietExist(Diet obj) {
		Cursor c = dbh.sqldb.query(DatabaseHelper.TABLE_DIET, new String[]{DatabaseHelper.ID},
				DatabaseHelper.NAME + " =? AND " + DatabaseHelper.TYPE + " =? AND "
						+ DatabaseHelper.DAY + " =?", new String[]{obj.getName(),
						obj.getMealType(), obj.getDay()}, null, null, null, null);
		if (c.getCount() > 0)
			return true;
		else
			return false;
	}

	public int deleteDiet(Diet obj) {
		return dbh.sqldb.delete(DatabaseHelper.TABLE_DIET, DatabaseHelper.NAME + " =? AND "
				+ DatabaseHelper.TYPE + " =? AND " + DatabaseHelper.DAY + " =?",
				new String[]{obj.getName(), obj.getMealType(), obj.getDay()});
	}

	public void clearScheduleTable() {
		dbh.sqldb.delete(DatabaseHelper.TABLE_DIET, null, new String[]{});
	}

	public ArrayList<Diet> getDailyDiet(String day) {
		ArrayList<Diet> list = new ArrayList<Diet>();
		String selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_DIET + " WHERE "
				+ DatabaseHelper.DAY + " = '" + day + "'";
		Cursor c = dbh.sqldb.rawQuery(selectQuery, null);
		if (c.moveToFirst()) {
			do {
				list.add(getObjectFromCursor(c));
			} while (c.moveToNext());
		}
		c.close();
		return list;
	}

	public ArrayList<Diet> getMeal(String day, String type) {
		ArrayList<Diet> list = new ArrayList<Diet>();
		String selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_DIET + " WHERE "
				+ DatabaseHelper.DAY + " = '" + day + "' AND " + DatabaseHelper.TYPE + " = '"
				+ type + "'";
		Cursor c = dbh.sqldb.rawQuery(selectQuery, null);
		if (c.moveToFirst()) {
			do {
				list.add(getObjectFromCursor(c));
			} while (c.moveToNext());
		}
		c.close();
		return list;
	}

	public ArrayList<String> getDays() {
		ArrayList<String> list = new ArrayList<String>();
		list.add(DatabaseHelper.MONDAY);
		list.add(DatabaseHelper.TUESDAY);
		list.add(DatabaseHelper.WEDNESDAY);
		list.add(DatabaseHelper.THURSDAY);
		list.add(DatabaseHelper.FRIDAY);
		list.add(DatabaseHelper.SATURDAY);
		list.add(DatabaseHelper.SUNDAY);
		return list;
	}

	public int getCalories(String name) {
		int calories = 0;
		String selectQuery = "SELECT " + DatabaseHelper.CALORIES + " FROM "
				+ DatabaseHelper.TABLE_MEAL + " WHERE " + DatabaseHelper.NAME + " = '" + name + "'";
		Cursor c = dbh.sqldb.rawQuery(selectQuery, null);
		while (c.moveToNext()) {
			calories = c.getInt(c.getColumnIndexOrThrow(DatabaseHelper.CALORIES));
		}
		c.close();
		return calories;
	}

	private ContentValues getContentFromObject(Diet obj) {
		ContentValues cv = new ContentValues();
		cv.put(DatabaseHelper.DAY, obj.getDay());
		cv.put(DatabaseHelper.TYPE, obj.getMealType());
		cv.put(DatabaseHelper.NAME, obj.getName());
		cv.put(DatabaseHelper.CALORIES, obj.getCalories());
		return cv;
	}

	private Diet getObjectFromCursor(Cursor c) {
		Diet obj = new Diet();
		obj.setDay(c.getString(c.getColumnIndexOrThrow(DatabaseHelper.DAY)));
		obj.setMealType(c.getString(c.getColumnIndexOrThrow(DatabaseHelper.TYPE)));
		obj.setName(c.getString(c.getColumnIndexOrThrow(DatabaseHelper.NAME)));
		obj.setCalories(c.getInt(c.getColumnIndexOrThrow(DatabaseHelper.CALORIES)));
		return obj;
	}
}
