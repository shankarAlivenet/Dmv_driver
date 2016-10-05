package com.alivenet.dmv.driverapplication;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.IntDef;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommonMethod {


	@Retention(RetentionPolicy.SOURCE)
	@IntDef({GRANTED, DENIED, BLOCKED})
	public @interface PermissionStatus {}

	public static final int GRANTED = 0;
	public static final int DENIED = 1;
	public static final int BLOCKED = 2;
	public static List<Address> addresses;
	public static Geocoder geocoder;
	public static String strAdd = "";

	@PermissionStatus
	public static int getPermissionStatus(Activity activity, String androidPermissionName) {
		if(ContextCompat.checkSelfPermission(activity, androidPermissionName) != PackageManager.PERMISSION_GRANTED) {
			if(!ActivityCompat.shouldShowRequestPermissionRationale(activity, androidPermissionName)){
				return BLOCKED;
			}
			return DENIED;
		}
		return GRANTED;
	}


	/** Called for Showing Alert in Application */
	public static void showAlert(String message, Context context) {

		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage(message).setCancelable(false)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {

					}
				});
		try {
			builder.show();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	public static void showAlert(String message,final  Activity source,final Activity destinations) {

		AlertDialog.Builder builder = new AlertDialog.Builder(source);
		builder.setMessage(message).setCancelable(false)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {

					}
				});
		   builder.setMessage(message).setCancelable(false).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {

				Intent in = new Intent(source,destinations.getClass());
				in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				source.startActivity(in);

			}
		});
		try {
			builder.show();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	
	/** Called for checking Internet connection */
	public static boolean isOnline(Context context) {

		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo;
		try {
			netInfo = cm.getActiveNetworkInfo();
			if (netInfo != null && netInfo.isConnectedOrConnecting()) {
				return true;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return false;
	}

	/** Called For Saving UserId Of Users */
	public static void setUserId(Context context, String key,
									String value) {

		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putString(key, value);
		editor.commit();

	}

	public static String getUserId(Activity conteActivity) {

		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(conteActivity);
		String USER_ID = sharedPreferences.getString("USER_ID", "");

		return USER_ID;
	}



	public static void setBookingID(Context context, String key,
								 String value) {

		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putString(key, value);
		editor.commit();

	}

	public static String getBookingID(Activity conteActivity) {

		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(conteActivity);
		String BOOKING_ID = sharedPreferences.getString(Constant.BOOKING_ID, "");

		return BOOKING_ID;
	}

	public static void setpickupadress(Context context, String key,
									String value) {

		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putString(key, value);
		editor.commit();

	}

	public static String getpickupadress(Activity conteActivity) {

		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(conteActivity);
		String BOOKING_ID = sharedPreferences.getString(Constant.PICKUPADDRESS, "");

		return BOOKING_ID;
	}
	public static void setDropaddress(Context context, String key,
									   String value) {

		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putString(key, value);
		editor.commit();

	}

	public static String getDropaddress(Activity conteActivity) {

		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(conteActivity);
		String BOOKING_ID = sharedPreferences.getString(Constant.DROPADRESS, "");

		return BOOKING_ID;
	}



	public static void setDriverId(Context context, String key,
									String value) {

		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putString(key, value);
		editor.commit();

	}



	public static String getdriverid(Context conteActivity) {

		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(conteActivity);
		String driver_id = sharedPreferences.getString("DRIVER_ID", "");

		return driver_id;
	}

// here code for save driver image value


	public static void setImage(Context context, String key,
								   String value) {

		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putString(key, value);
		editor.commit();

	}



	public static void setclientname(Context context, String key,
								  String value) {

		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		Editor editor = sharedPreferences.edit();
		editor.putString(key, value);
		editor.commit();

	}

	public static String getclientname(Activity conteActivity) {

		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(conteActivity);
		String name = sharedPreferences.getString(Constant.CLIENT_NAME, "");

		return name;
	}

	public static void setclientmobile(Context context, String key,
									 String value) {

		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		Editor editor = sharedPreferences.edit();
		editor.putString(key, value);
		editor.commit();

	}

	public static String getclientmobile(Activity conteActivity) {

		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(conteActivity);
		String mobile = sharedPreferences.getString(Constant.CLIENT_MOBILE, "");

		return mobile;
	}

	public static void setclientpickupLat(Context context, String key, String value) {

		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		Editor editor = sharedPreferences.edit();
		editor.putString(key, value);
		editor.commit();

	}

	public static String getclientpickupLat(Activity conteActivity) {

		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(conteActivity);
		String setclientpickupLat = sharedPreferences.getString(Constant.CLIENT_PICKLAT, "");

		return setclientpickupLat;
	}
	public static void setclientpickupLong(Context context, String key,
									   String value) {

		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		Editor editor = sharedPreferences.edit();
		editor.putString(key, value);
		editor.commit();

	}

	public static String getclientpickupLong(Activity conteActivity) {

		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(conteActivity);
		String setclientpickupLong = sharedPreferences.getString(Constant.CLIENT_PICLOT, "");

		return setclientpickupLong;
	}


	public static void setclientestimatedistance(Context context, String key,
										  String value) {

		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		Editor editor = sharedPreferences.edit();
		editor.putString(key, value);
		editor.commit();

	}

	public static String getclientestimatedistance(Activity conteActivity) {

		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(conteActivity);
		String setclientestimatedistance = sharedPreferences.getString(Constant.CLIENT_ESTIMATED_DISTANCE, "");

		return setclientestimatedistance;
	}
	public static void setclientestimatedTime(Context context, String key,
										   String value) {

		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		Editor editor = sharedPreferences.edit();
		editor.putString(key, value);
		editor.commit();

	}


	public static void setconfirmstatus(Context context, String key,
											  String value) {

		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		Editor editor = sharedPreferences.edit();
		editor.putString(key, value);
		editor.commit();

	}

	public static String getconfirmstatus(Activity conteActivity) {

		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(conteActivity);
		String confirmstatus = sharedPreferences.getString(Constant.CLIENT_TRIP_CONFIRM, "");

		return confirmstatus;
	}
	//
	public static void setcabType(Context context, String key,
										String value) {

		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		Editor editor = sharedPreferences.edit();
		editor.putString(key, value);
		editor.commit();

	}

	public static String getCabType(Activity conteActivity) {

		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(conteActivity);
		String cabtype = sharedPreferences.getString(Constant.CABTYPE, "");

		return cabtype;
	}
	//

	public static void setTripcomplete(Context context, String key,
										boolean value) {

		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		Editor editor = sharedPreferences.edit();
		editor.putBoolean(key, value);
		editor.commit();

	}

	public static boolean getTripcomplete(Context conteActivity) {

		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(conteActivity);
		boolean confirmstatus = sharedPreferences.getBoolean(Constant.TRIPCOMPLETE,false);

		return confirmstatus;
	}


	/** Called For Saving UserId Of Users */
	public static void setPaymentMode(Context context, String key,
								 String value) {

		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putString(key, value);
		editor.commit();

	}

	public static String getPaymentMode(Activity conteActivity) {

		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(conteActivity);
		String paymentmode = sharedPreferences.getString(Constant.CLIENT_PAYMENT_MODE, "");

		return paymentmode;
	}

	public static void setDriverId_sevice(Context context, String key,
								   String value) {

		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putString(key, value);
		editor.commit();

	}



	public static String getdriverid_service(Context conteActivity) {

		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(conteActivity);
		String driver_id = sharedPreferences.getString("DRIVER_ID_SERVICE", "");

		return driver_id;
	}

}
