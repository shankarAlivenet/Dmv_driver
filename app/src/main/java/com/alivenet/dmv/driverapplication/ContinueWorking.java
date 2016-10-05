package com.alivenet.dmv.driverapplication;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

/**
 * Created by narendra on 6/17/2016.
 */
public class ContinueWorking extends Dialog {
    Activity activity;
    String noofpeople;

    Button btnSaveReservation,btnfoenextfair, btn_logout;
    public ContinueWorking(Activity a, String msg) {
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
        setContentView(R.layout.continueworking);
            btnfoenextfair = (Button) findViewById(R.id.btn_redyfare);
            btn_logout = (Button) findViewById(R.id.btn_logout);
            btnSaveReservation = (Button)findViewById(R.id.btnsave_reservation);
            btnfoenextfair.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    CommonMethod.setclientname(activity, com.alivenet.dmv.driverapplication.Constant.CLIENT_NAME,"");
                    CommonMethod.setclientmobile(activity, com.alivenet.dmv.driverapplication.Constant.CLIENT_MOBILE,"");
                    CommonMethod.setclientpickupLat(activity, com.alivenet.dmv.driverapplication.Constant.CLIENT_PICKLAT,"");
                    CommonMethod.setclientpickupLong(activity, com.alivenet.dmv.driverapplication.Constant.CLIENT_PICLOT,"");
                    CommonMethod.setclientestimatedistance(activity, com.alivenet.dmv.driverapplication.Constant.CLIENT_ESTIMATED_DISTANCE,"");
                    CommonMethod.setclientestimatedTime(activity, com.alivenet.dmv.driverapplication.Constant.CLIENT_ESTIMATED_TIME,"");
                    CommonMethod.setclientestimatedTime(activity, com.alivenet.dmv.driverapplication.Constant.CLIENT_ESTIMATED_TIME,"");
                    CommonMethod.setconfirmstatus(activity,com.alivenet.dmv.driverapplication.Constant.CLIENT_TRIP_CONFIRM,"no");
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
                    Intent homeIntent = new Intent(getContext(), MainActivity.class);
                    ComponentName cn = homeIntent.getComponent();
                    Intent mainIntent = Intent
                            .makeRestartActivityTask(cn);
                    getContext().startActivity(mainIntent);
                    dismiss();
                    homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);




                }
            });
            btn_logout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    CommonMethod.setclientname(activity, com.alivenet.dmv.driverapplication.Constant.CLIENT_NAME,"");
                    CommonMethod.setclientmobile(activity, com.alivenet.dmv.driverapplication.Constant.CLIENT_MOBILE,"");
                    CommonMethod.setclientpickupLat(activity, com.alivenet.dmv.driverapplication.Constant.CLIENT_PICKLAT,"");
                    CommonMethod.setclientpickupLong(activity, com.alivenet.dmv.driverapplication.Constant.CLIENT_PICLOT,"");
                    CommonMethod.setclientestimatedistance(activity, com.alivenet.dmv.driverapplication.Constant.CLIENT_ESTIMATED_DISTANCE,"");
                    CommonMethod.setclientestimatedTime(activity, com.alivenet.dmv.driverapplication.Constant.CLIENT_ESTIMATED_TIME,"");
                    CommonMethod.setclientestimatedTime(activity, com.alivenet.dmv.driverapplication.Constant.CLIENT_ESTIMATED_TIME,"");
                    CommonMethod.setconfirmstatus(activity,com.alivenet.dmv.driverapplication.Constant.CLIENT_TRIP_CONFIRM,"no");
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

                    Intent broadcastIntent = new Intent();
                    broadcastIntent.setAction("ACTION_LOGOUT");
                    activity.sendBroadcast(broadcastIntent);

                    Intent homeIntent = new Intent(getContext(), LoginActivity.class);
                    ComponentName cn = homeIntent.getComponent();
                    Intent mainIntent = Intent
                            .makeRestartActivityTask(cn);
                    getContext().startActivity(mainIntent);
                    dismiss();
                    homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);



                }
            });

            btnSaveReservation.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                // dismiss dialog box
                dismiss();
            }
        });


        }

    }

