package com.alivenet.dmv.driverapplication;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import util.HttpClient;

/**
 * Created by narendra on 6/17/2016.
 */
public class LogOutDialog extends Dialog {

    SharedPreferences mPref;
    public static final String MYPREF = "user_info";
    Activity activity;
    String noofpeople;
    Button btnlogout_Yes , btnlogout_No;

    public LogOutDialog(Activity a, String msg) {
        super(a, R.style.custom_dialog_theme);
        // TODO Auto-generated constructor stub
        activity = a;
        noofpeople = msg;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // hide notification bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.logout_dialog);
        mPref = getContext().getSharedPreferences(MYPREF, Context.MODE_PRIVATE);

        btnlogout_Yes = (Button) findViewById(R.id.btnlogout_yes);
        btnlogout_No = (Button) findViewById(R.id.btnlogout_no);
        // people_size.setText(noofpeople);
        btnlogout_No.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                // dismiss dialog box

                dismiss();
            }
        });

        btnlogout_Yes.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                // dismiss dialog box
                LogOutAsync logout =new LogOutAsync();
                logout.execute();


            }
        });

    }


    /// Log.....out AsynTask
    public class LogOutAsync extends AsyncTask<String, Void, String> {
        private Dialog mDialog;
        private String response;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            mDialog = ProgressDialog.show(activity, "", "Loading...", true);
            mDialog.setCancelable(false);
        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            response = logoutService();
            return response;
        }

        @Override
        protected void onPostExecute(String response) {
            // TODO Auto-generated method stub
            super.onPostExecute(response);



            if (mDialog != null) {
                mDialog.dismiss();
            }
            Log.d("##########Response", "" + response);

            //responseCode
            JSONObject object;

            if (response != null) {
                try {

                    object = new JSONObject(response);
                    String responseCode = object.getString("responseCode");

                    // String responseMessage = object.getString("responseMessage");
                    Log.d("object", "" + object);
                    if (responseCode.equals("200")) {

                        try {
                            activity.stopService(new Intent(activity,
                                    AndroidLocationServices.class));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                        CommonMethod.setclientname(activity, Constant.CLIENT_NAME,"");
                        CommonMethod.setclientmobile(activity, Constant.CLIENT_MOBILE,"");
                        CommonMethod.setclientpickupLat(activity, Constant.CLIENT_PICKLAT,"");
                        CommonMethod.setclientpickupLong(activity, Constant.CLIENT_PICLOT,"");
                        CommonMethod.setclientestimatedistance(activity, Constant.CLIENT_ESTIMATED_DISTANCE,"");
                        CommonMethod.setclientestimatedTime(activity, Constant.CLIENT_ESTIMATED_TIME,"");
                        CommonMethod.setclientestimatedTime(activity, Constant.CLIENT_ESTIMATED_TIME,"");
                        CommonMethod.setconfirmstatus(activity, Constant.CLIENT_TRIP_CONFIRM,"no");
                        CommonMethod.setUserId(activity,"USER_ID","");
                        CommonMethod.setBookingID(activity, Constant.BOOKING_ID,"");
                        MyApplication.message="";
                        MyApplication. bookingId="";
                        MyApplication. driverId="";
                        MyApplication.userId="";
                        MyApplication.pickupLat="";
                        MyApplication.pickupLong="";
                        MyApplication.destinationLat="";
                        MyApplication.destinationLong="";
                        MyApplication.distance="";
                        MyApplication.username="";
                        MyApplication.paymentMethod="";
                        CommonMethodUtil.savelogin(activity,"loginType",false);
                        Intent homeIntent = new Intent(getContext(), LoginActivity.class);
                        ComponentName cn = homeIntent.getComponent();
                        Intent mainIntent = Intent
                                .makeRestartActivityTask(cn);
                        getContext().startActivity(mainIntent);
                        dismiss();
                        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                        Toast toast = Toast.makeText(activity, "you are logged out successfully", Toast.LENGTH_LONG);
                        toast.show();
                        toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 35, 250);

                    }
                    else {

                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } else {

            }
        }

        private String logoutService() {
            String url = ConstantUtil.Log_out;

            HttpClient client = new HttpClient(url);
            Log.e("before connection", "" + url);
            try {
                client.connectForMultipart();

                Log.e("after connection", "" + url);
                client.addFormPart("userId",CommonMethod.getdriverid(activity));
                Log.d("client", client.toString());
                client.finishMultipart();

                response = client.getResponse().toString();
                Log.d("response", response);

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return response;
        }


    }

}