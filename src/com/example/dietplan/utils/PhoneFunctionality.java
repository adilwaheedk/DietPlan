package com.example.dietplan.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Vibrator;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.dietplan.R;

public class PhoneFunctionality {

	public final static long VIBRATE_TIME = (long) 500;
	public final static int ALERT_NOTIF_ID = IdGenerator.generateViewId();
	public final static int MEAL_NOTIF_ID = IdGenerator.generateViewId();

	public static void playSound(Context context) {
		Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
		Ringtone r = RingtoneManager.getRingtone(context.getApplicationContext(), notification);
		r.play();
	}

	public static void hideKeyboard(Activity parentAct) {
		InputMethodManager inputManager = (InputMethodManager) parentAct
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		inputManager.hideSoftInputFromWindow(parentAct.getCurrentFocus().getWindowToken(),
				InputMethodManager.HIDE_NOT_ALWAYS);
	}

	public static void vibrateMobile(Context context) {
		Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
		vibrator.vibrate(VIBRATE_TIME);
	}

	public static void showNotification(Class<?> cls, Context context, int id, int icon,
			String title, String text, boolean auto_cancel) {
		Intent intent = new Intent(context, cls);
		PendingIntent pi = PendingIntent.getActivity(context, 0, intent, 0);
		Notification notif = new Notification.Builder(context).setContentTitle(title)
				.setContentText(text).setSmallIcon(icon).setContentIntent(pi).build();
		NotificationManager notifManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		notif.flags |= auto_cancel ? Notification.FLAG_AUTO_CANCEL : Notification.FLAG_NO_CLEAR;
		notifManager.notify(id, notif);
	}

	public static void hideNotification(Context context, int id) {
		NotificationManager notifManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		notifManager.cancel(id);
	}

	public void showAlertDialog(Context context, String title, String message, int icon) {
		AlertDialog alertDialog = new AlertDialog.Builder(context).create();
		alertDialog.setTitle(title);
		alertDialog.setMessage(message);
		alertDialog.setIcon(icon);
		alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
					}
				});
		alertDialog.show();
	}

	public static void startAnim(Context context, View view) {
		view.startAnimation(AnimationUtils.loadAnimation(context, R.anim.image_click));
	}

	public static void sendMessage(Context context, String receiver, String message) {
		SmsManager sms = SmsManager.getDefault();
		sms.sendTextMessage(receiver, null, message, null, null);
	}

	public static String getSimNumber(Context context) {
		TelephonyManager teleManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		return teleManager.getLine1Number();
	}

	public static void makeToast(Context context, String msg) {
		Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
	}

	public static boolean setListViewHeightBasedOnItems(ListView listView, int limit) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter != null & limit > 0) {
			int numberOfItems = listAdapter.getCount();
			if (numberOfItems > limit)
				numberOfItems = limit;
			int totalItemsHeight = 0;
			for (int itemPos = 0; itemPos < numberOfItems; itemPos++) {
				View item = listAdapter.getView(itemPos, null, listView);
				item.measure(0, 0);
				totalItemsHeight += item.getMeasuredHeight();
			}
			int totalDividersHeight = listView.getDividerHeight() * (numberOfItems - 1);
			ViewGroup.LayoutParams params = listView.getLayoutParams();
			params.height = totalItemsHeight + totalDividersHeight;
			listView.setLayoutParams(params);
			listView.requestLayout();
			return true;
		} else
			return false;
	}

	public static boolean setListViewHeight(ListView listView, int value) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter != null) {
			ViewGroup.LayoutParams params = listView.getLayoutParams();
			params.height = value;
			listView.setLayoutParams(params);
			listView.requestLayout();
			return true;
		} else
			return false;
	}
}
