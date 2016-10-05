package com.alivenet.dmv.driverapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommonMethodUtil  {


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

    public static void showAlert(String message, Activity context) {

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


    public static boolean isEmailValid(String email) {
        boolean isValid = false;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }


    /** Called For Saving UserId Of Users */
    public static void saveUserId(Context context, String key,
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
        String userId = sharedPreferences.getString(ConstantUtil.USER_ID, "");

        return userId;
    }

    /** Called For Saving bookingId Of Users */
    public static void saveBookingId(Context context, String key,
                                  String value) {

        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();

    }

    ///////////sunil
    public static void savelogin(Context context, String key,
                                     boolean value) {

        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.commit();

    }
    public static boolean getlogin(Activity conteActivity) {

        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(conteActivity);
        Boolean logintype = sharedPreferences.getBoolean(ConstantUtil.LoginType, false);

        return logintype;
    }
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
    public static String Timeformate(String timestring)
    {
        String CurrentString = timestring;
        String[] separated = CurrentString.split("-");
        String[] splitdaytime = separated[2].split(" ");
        int selectedYear = Integer.parseInt(separated[0]);
        int selectedDay = Integer.parseInt(splitdaytime[0]);
        int selectedMonth = Integer.parseInt(separated[1]);
        splitdaytime[1] = splitdaytime[1].substring(0, splitdaytime[1].length() - 3);
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, selectedYear);
        cal.set(Calendar.DAY_OF_MONTH, selectedDay);
        cal.set(Calendar.MONTH, selectedMonth-1);
        String format = new SimpleDateFormat("EEEE, MMM d, yyyy").format(cal.getTime());
        String finaldateformate=format+"  "+splitdaytime[1];
        System.out.println("formated day time == " + finaldateformate);
        return finaldateformate;
    }

    public static String DateFormatApp(String findate) {

        DateFormat fromFormat = new SimpleDateFormat("dd-MM-yyyy");

        DateFormat toFormat = new SimpleDateFormat(" MMM d, yyyy");
        toFormat.setLenient(false);
        String dateStr = findate;
        Date date = null;
        try {
            date = fromFormat.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String dateNew = toFormat.format(date);

            return dateNew;

    }

}
