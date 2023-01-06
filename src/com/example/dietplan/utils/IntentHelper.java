package com.example.dietplan.utils;

import java.util.Hashtable;

public class IntentHelper {

	private static IntentHelper instance;
	private Hashtable<String, Object> hash;

	private IntentHelper() {
		hash = new Hashtable<String, Object>();
	}

	private static IntentHelper getInstance() {
		if (instance == null) {
			instance = new IntentHelper();
		}
		return instance;
	}

	public static void addObjectForKey(Object object, String key) {
		getInstance().hash.put(key, object);
	}

	public static Object getObjectForKey(String key) {
		IntentHelper helper = getInstance();
		Object data = helper.hash.get(key);
		helper.hash.remove(key);
		helper = null;
		return data;
	}
}