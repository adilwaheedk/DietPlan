package com.example.dietplan.utils;

import java.text.DecimalFormat;

public class Calculate {

	// Convert feet, inch to centimeter
	public static float convertFtInchToCm(float ft, float inch) {
		return doubleToFloat((ft * 30.48) + (inch * 2.54));
	}

	// Convert centimeter to inch
	public static float convertCmToInch(float cm) {
		return doubleToFloat(cm * 0.39370);
	}

	// Convert inch to feet and inch
	public static float[] convertInchToFtInch(float inch) {
		float[] arr = new float[2];
		int int_part = Integer.valueOf(String.valueOf(inch / 12).split("\\.")[0]);
		arr[0] = int_part;
		arr[1] = inch - (int_part * 12);
		return arr;
	}

	// Convert pounds to kilogram
	public static float convertLbsToKg(float lbs) {
		return doubleToFloat(lbs / 2.2046);
	}

	// Convert kilogram to pounds
	public static float convertKgToLbs(float kg) {
		return doubleToFloat(kg * 2.2046);
	}

	// Body Mass Index
	public static float getBMIValue(float weight_kg, float height_cm) {
		return doubleToFloat(weight_kg / Math.pow(height_cm * 0.01, 2));
	}

	// Basal Metabolic Rate (BMR) (Calories Per Day)
	public static float getBMRValue(float weight_kg, float height_cm, int age_yrs, boolean isMale) {
		if (isMale)
			return doubleToFloat(88.362 + (13.397 * weight_kg) + (4.799 * height_cm)
					- (5.677 * age_yrs));
		else
			return doubleToFloat(447.593 + (9.247 * weight_kg) + (3.098 * height_cm)
					- (4.330 * age_yrs));
	}

	// Daily Calories Intake on the basis of Activity Level
	public static float getDailyIntake(float bmr_value, int active_level) {
		double intake;
		if (active_level == 0)
			intake = bmr_value * 1.2;
		else if (active_level == 1)
			intake = bmr_value * 1.375;
		else if (active_level == 2)
			intake = bmr_value * 1.55;
		else if (active_level == 3)
			intake = bmr_value * 1.725;
		else
			intake = bmr_value * 1.9;
		return doubleToFloat(intake);
	}

	// Convert double to float
	private static float doubleToFloat(double value) {
		String s = new DecimalFormat("##.###").format(value);
		return Float.parseFloat(s);
	}
}
