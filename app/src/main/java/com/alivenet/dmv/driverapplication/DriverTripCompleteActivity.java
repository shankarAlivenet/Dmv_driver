package com.alivenet.dmv.driverapplication;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.support.multidex.MultiDex;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;
import java.util.Locale;

public class DriverTripCompleteActivity extends AppCompatActivity {
    private GoogleMap googleMap;
    Button btntripcomplete;
    TextView wayfromclient,clientname,location,paymethod;
    EditText nameofclient,editlocation,selectpaymethod;
    private Typeface mTypeface;
    double latitude,longitude;
    String strAdd = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_trip_complete);
        wayfromclient = (TextView) findViewById(R.id.text_way);
        clientname = (TextView) findViewById(R.id.txt_clientname);
        nameofclient = (EditText) findViewById(R.id.edit_nameofclient);
        location = (TextView) findViewById(R.id.txt_location);
        editlocation = (EditText) findViewById(R.id.edit_location);
        paymethod = (TextView) findViewById(R.id.txt_paymethod);
        selectpaymethod = (EditText) findViewById(R.id.select_paymethod);
        btntripcomplete = (Button) findViewById(R.id.btncomplete);
/*
        mTypeface= Typeface.createFromAsset(getAssets(), "assets/System San Francisco Display Regular.ttf");
        getFontFamily();*/
        btntripcomplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(DriverTripCompleteActivity.this, TripCompleteActivity.class);
                startActivity(in);
            }
        });
//for google map



        GoogleMap googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.setMyLocationEnabled(true);
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.getUiSettings().setCompassEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        googleMap.getUiSettings().setZoomGesturesEnabled(true);
        googleMap.getUiSettings().setRotateGesturesEnabled(true);
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        List<String> providers = lm.getProviders(true);
        Location l = null;
        for (int i = 0; i < providers.size(); i++) {
            l = lm.getLastKnownLocation(providers.get(i));
            if (l != null) {
                latitude = l.getLatitude();
                longitude = l.getLongitude();
                strAdd = getCompleteAddressString(latitude, longitude);
                // tvAddress.setText("Complete Address : " + strAdd);
                break;
            }
        }
        if (googleMap != null) {

            MarkerOptions marker = new MarkerOptions().position(
                    new LatLng(latitude, longitude)).title(strAdd);

            marker.icon(BitmapDescriptorFactory
                    .defaultMarker(BitmapDescriptorFactory.HUE_GREEN));

// Moving Camera to a Location with animation
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(latitude, longitude)).zoom(12).build();

            googleMap.animateCamera(CameraUpdateFactory
                    .newCameraPosition(cameraPosition));

            googleMap.addMarker(marker);
            googleMap.setMyLocationEnabled(true);
        }


    }

    private void getFontFamily(){
        wayfromclient.setTypeface(mTypeface);
        clientname.setTypeface(mTypeface);
        nameofclient.setTypeface(mTypeface);
        location.setTypeface(mTypeface);
        editlocation.setTypeface(mTypeface);
        paymethod.setTypeface(mTypeface);
        selectpaymethod.setTypeface(mTypeface);
        btntripcomplete.setTypeface(mTypeface);

    }

    ///google map get address
    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        Geocoder geocoder = new Geocoder(DriverTripCompleteActivity.this, Locale.getDefault());
        try {
            List<android.location.Address> addresses = geocoder
                    .getFromLocation(LATITUDE, LONGITUDE,1);
            if (addresses != null) {
                android.location.Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress
                            .append(returnedAddress.getAddressLine(i)).append(
                            "\n");
                }
                strAdd = strReturnedAddress.toString();
                Log.w("My Current ",
                        "" + strReturnedAddress.toString());
            } else {
                Log.w("My Current loc", "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.w("My Current loction", "Canont get Address!");
        }
        return strAdd;
    }
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}

