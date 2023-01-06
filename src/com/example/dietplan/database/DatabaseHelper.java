package com.example.dietplan.database;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

	private final Context context;
	private static String DATABASE_FOOD = "DietChartDatabase";
	private static String DATABASE_FOOD_PATH;
	private static String DATABASE_FOOD_FULL_PATH;
	private static final int DATABASE_VERSION = 1;

	public static final String TABLE_MEAL = "Meal";
	public static final String TABLE_FOODTYPES = "FoodTypes";
	public static final String TABLE_DIET = "Diet";

	public static final String ID = "_id";
	public static final String NAME = "name";
	public static final String SIZE = "serving_size";
	public static final String CALORIES = "calories";
	public static final String GOAL = "goal";
	public static final String TYPE = "type";
	public static final String DAY = "day";

	public static final String MONDAY = "Monday";
	public static final String TUESDAY = "Tuesday";
	public static final String WEDNESDAY = "Wednesday";
	public static final String THURSDAY = "Thurdsay";
	public static final String FRIDAY = "Friday";
	public static final String SATURDAY = "Saturday";
	public static final String SUNDAY = "Sunday";

	public static final String BREAKFAST = "Breakfast";
	public static final String LUNCH = "Lunch";
	public static final String SNACK = "Snack";
	public static final String DINNER = "Dinner";

	public SQLiteDatabase sqldb;

	/**
	 * Constructor Takes and keeps a reference of the passed context in order to
	 * access to the application assets and resources.
	 * 
	 * @param context
	 */
	public DatabaseHelper(Context context) {
		super(context, DATABASE_FOOD, null, DATABASE_VERSION);
		this.context = context;

		// DATABASE_FOOD_PATH = "/data/data/com.example.dietchart/databases/"
		DATABASE_FOOD_PATH = context.getApplicationInfo().dataDir + "/databases/";
		// if (android.os.Build.VERSION.SDK_INT >= 17) {
		// DATABASE_FOOD_PATH = context.getApplicationInfo().dataDir +
		// "/databases/";
		// } else {
		// DATABASE_FOOD_PATH = context.getFilesDir().getPath() + "/databases/"
		// }

		DATABASE_FOOD_FULL_PATH = DATABASE_FOOD_PATH + DATABASE_FOOD;
	}

	/**
	 * Creates an empty database on the system and rewrites it with your own
	 * database.
	 * */
	public void createDatabase() throws IOException {
		if (!isDatabaseExist()) {
			this.getReadableDatabase();
			try {
				copyDatabase();
			} catch (IOException e) {
				throw new Error("Error copying database");
			}
		}
	}

	/**
	 * Check if the database already exist to avoid re-copying the file each
	 * time you open the application.
	 * 
	 * @return true if it exists, false if it doesn't
	 */
	private boolean isDatabaseExist() {
		SQLiteDatabase checkDB = null;
		try {
			checkDB = SQLiteDatabase.openDatabase(DATABASE_FOOD_FULL_PATH, null,
					SQLiteDatabase.OPEN_READONLY);
		} catch (SQLiteException e) {
		}

		if (checkDB != null) {
			checkDB.close();
			return true;
		}
		return false;
	}

	/**
	 * Copy database from local assets-folder to the just created empty database
	 * in the system folder, from where it can be accessed and handled. This is
	 * done by transferring byte stream.
	 * */
	private void copyDatabase() throws IOException {
		// Open your local db as the input stream
		InputStream iStr = context.getAssets().open(DATABASE_FOOD);
		// Open the empty db as the output stream
		OutputStream oStr = new FileOutputStream(DATABASE_FOOD_FULL_PATH);
		// transfer bytes from the input file to the output file
		byte[] buffer = new byte[1024];
		int length;
		while ((length = iStr.read(buffer)) > 0) {
			oStr.write(buffer, 0, length);
		}
		// Close the streams
		oStr.flush();
		oStr.close();
		iStr.close();
		this.close();
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}
}
