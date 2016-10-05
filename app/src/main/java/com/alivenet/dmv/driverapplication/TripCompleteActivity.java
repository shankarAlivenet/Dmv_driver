package com.alivenet.dmv.driverapplication;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.Date;

import util.*;

/**
 * Created by sunil on 6/21/2016.
 */
public class TripCompleteActivity extends AppCompatActivity implements RatingBar.OnRatingBarChangeListener {
  TextView trip_complete,trip_charge,pick_location,location_pick,drop_location,location_drop,name,passanger_name,
    gross_fare,location2,currentdate_time,taximodelno,trip_rate,how_yourtrip;
    RatingBar triprating;
    Button btn_tip,btn_submit;
    EditText define_trip;
    private Typeface mTypeface;


    private GPSTracker gps;
    double latitude,longitude;
    String currentDateTimeString;
    private BroadcastReceiver broadcastReceiver=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trip_completed);
        Toolbar toolbar = (Toolbar) findViewById(R.id.maintoolbar);
        //setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setDisplayShowHomeEnabled(true);

        CommonMethod.setTripcomplete(TripCompleteActivity.this,Constant.TRIPCOMPLETE,false);


        trip_complete=(TextView)findViewById(R.id.txt_tripcomp);
        trip_charge=(TextView)findViewById(R.id.txt_chargecredit);
        pick_location=(TextView)findViewById(R.id.txt_pickuplocation);
        location_pick=(TextView)findViewById(R.id.txt_locationpik);
        drop_location=(TextView)findViewById(R.id.txt_droploc);
        location_drop=(TextView)findViewById(R.id.txt_locationdrop);
        name=(TextView)findViewById(R.id.txt_name);
        passanger_name=(TextView)findViewById(R.id.passenger_name);
        gross_fare=(TextView)findViewById(R.id.grossfare);
        location2=(TextView)findViewById(R.id.grossfares);
        currentdate_time=(TextView)findViewById(R.id.currentdate_time);
        taximodelno=(TextView)findViewById(R.id.currentdate_times);
        trip_rate=(TextView)findViewById(R.id.trip_rate);
        how_yourtrip=(TextView)findViewById(R.id.howyour_trip);
        btn_tip=(Button)findViewById(R.id.btn_tip);
        btn_submit=(Button)findViewById(R.id.btn_submit);
        define_trip=(EditText)findViewById(R.id.definetrip);
        triprating=(RatingBar) findViewById(R.id.trip_rating);


        currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
        gps = new GPSTracker(TripCompleteActivity.this);
        getLatLong();
//        logout();
        ((RatingBar) findViewById(R.id.trip_rating))
                .setOnRatingBarChangeListener(TripCompleteActivity.this);



        if(CommonMethod.getpickupadress(TripCompleteActivity.this)!=null||CommonMethod.getpickupadress(TripCompleteActivity.this)!=null){
            // location_pick.setText(CommonMethod.getCompleteAddressString(Double.parseDouble(CommonMethod.getclientpickupLat(TripCompleteActivity.this)),Double.parseDouble(CommonMethod.getclientpickupLong(TripCompleteActivity.this)),TripCompleteActivity.this));
            location_pick.setText(CommonMethod.getpickupadress(TripCompleteActivity.this));
        }else {
            location_pick.setText("");
        }


        if (CommonMethod.getDropaddress(TripCompleteActivity.this)!=null){
            location_drop.setText(CommonMethod.getDropaddress(TripCompleteActivity.this));
        }else{
            location_drop.setText("");
        }



        if (CommonMethod.getclientname(TripCompleteActivity.this)!=null){
            passanger_name.setText(CommonMethod.getclientname(TripCompleteActivity.this));
        }else {
            passanger_name.setText("");
        }
        if(MyApplication.totalfair!=null){
            gross_fare.setText("$ "+MyApplication.totalfair);
        }else {
            gross_fare.setText("");
        }

        currentdate_time.setText(currentDateTimeString);

        trip_charge.setText(MyApplication.trip_chrg);






       /* triprating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Float  bar=triprating.getRating();
                System.out.println("gfhsgdjhdhd"+bar);

            }
        });*/




        //mTypeface=Typeface.createFromAsset(getAssets(),"assets/System San Francisco Display Regular.ttf");
        //getFontFamily();
        //toolbar = (Toolbar) findViewById(R.id.maintoolbar);
        //toolbar.setTitle("Login");
        // toolbar.setNavigationIcon(getResources().getDrawable(R.mipmap.nav_icon_1));

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    new ContinueWorking(TripCompleteActivity.this ,"hello").show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });



    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private  void getFontFamily(){

        trip_complete.setTypeface(mTypeface);
        trip_charge.setTypeface(mTypeface);
        pick_location.setTypeface(mTypeface);
        location_pick.setTypeface(mTypeface);
        drop_location.setTypeface(mTypeface);
        location_drop.setTypeface(mTypeface);
        name.setTypeface(mTypeface);
        passanger_name.setTypeface(mTypeface);
        gross_fare.setTypeface(mTypeface);
        location2.setTypeface(mTypeface);
        currentdate_time.setTypeface(mTypeface);
        taximodelno.setTypeface(mTypeface);
        trip_rate.setTypeface(mTypeface);
        how_yourtrip.setTypeface(mTypeface);
        btn_tip.setTypeface(mTypeface);
        btn_submit.setTypeface(mTypeface);
        define_trip.setTypeface(mTypeface);

    }
//13 aug
    public void logout() {

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("ACTION_LOGOUT");
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //TODO  map updations work here

                Log.d("onReceive","Logout in progress");
                //At this point you should start the login activity and finish this one
                Intent in = new Intent(TripCompleteActivity.this,LoginActivity.class);
                in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(in);
                finish();
                    if(broadcastReceiver==null)
                    {

                    }else {
                        unregisterReceiver(broadcastReceiver);
                        broadcastReceiver=null;
                    }
                   }
        };
        registerReceiver(broadcastReceiver, intentFilter);


    }

    //*//***for getting lattitude and longitude****//*///
    private void getLatLong() {

        if(gps.canGetLocation()){
            latitude = gps.getLatitude();
            longitude = gps.getLongitude();
            Log.d("latitude....=",""+latitude);
            Log.d("longitude....=",""+longitude);


            // \n is for new line
            // Toast.makeText(LoginActivity.this, "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
        }else{
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }
    }
    @Override
    public void onRatingChanged(RatingBar ratingBar, float rating,
                                boolean fromTouch) {
        final int numStars = ratingBar.getNumStars();
       // ratingText.setText(rating + "/" + numStars);
    }

    @Override
    public void onBackPressed()
    {

        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);

        // code here to show dialog
       // super.onBackPressed();



    }

}


