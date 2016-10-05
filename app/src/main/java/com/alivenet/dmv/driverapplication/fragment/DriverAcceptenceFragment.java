package com.alivenet.dmv.driverapplication.fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alivenet.dmv.driverapplication.CommonMethod;
import com.alivenet.dmv.driverapplication.CommonMethodUtil;
import com.alivenet.dmv.driverapplication.Constant;
import com.alivenet.dmv.driverapplication.ConstantUtil;
import com.alivenet.dmv.driverapplication.GPSTracker;
import com.alivenet.dmv.driverapplication.MyApplication;
import com.alivenet.dmv.driverapplication.R;
import com.alivenet.dmv.driverapplication.TripCompleteActivity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.sinch.android.rtc.PushPair;
import com.sinch.android.rtc.Sinch;
import com.sinch.android.rtc.SinchClient;
import com.sinch.android.rtc.calling.Call;
import com.sinch.android.rtc.calling.CallListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import util.HttpClient;


public class DriverAcceptenceFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,LocationListener {

    Call call;
    Button callButton;
    int firsttime;
    CameraUpdate update = null;
    TextView callState;
    // for trip complete
    private String driverId,userId,bookingId;
    private TripCompleteAsync mTripCompleteAsync;

    TextView wayfromclient, clientname, location, paymethod;
    TextView nameofclient, editlocation, selectpaymethod, phoneno, showphonenumbe;

    LinearLayout ln_nameofclient,ln_arrived, ln_editlocation, ln_selectpaymethod;
    LinearLayout ln_confirm_cancle, ln_ride, ln_startride, ln_phone, ln_tripcomplete;

    Button btnconfrm, btncancel,btnarrived, btn_ridestart, btntripcomplete;
    public View view;
    public GoogleMap googleMap;
    String pickLat, pickLong, delLan, delLong, status;
    List<LatLng> polyz;
    Geocoder geocoder;
    List<Address> addresses;
    Marker marker;
    private Typeface mTypeface;
    double latitude, longitude;
    double previousLocationLatitude;
    double previousLocationLongitude;
    double currentLocationLatitude;
    double currentLocationLongitude;
    public static String strAdd = "";
    private GPSTracker gps;
    static LatLng picuplatlong;
    static LatLng destilatlong;
    public String confirmtripstatus, Accepttype;
    Confirm_cancle_trip confirm_cancle_trip;
    Start_ride startride;
    Arrieved_at_user_locations arrieved_at_user_locations;
    float angle;
    public static Bitmap car_icon;
    public static boolean isMarkerRotating;
    public static Context  context;
    protected String mLastUpdateTime;
    public boolean flagmarkerremove;

    Bitmap mBitmap;
    MarkerOptions   markerOptions=new MarkerOptions();
    LocationManager locationManager;
    PolylineOptions polylineOptions = new PolylineOptions();

    private static int mCustomDurationBar=3000;

    /**
     * The fastest rate for active location updates. Exact. Updates will never be more frequent
     * than this value.
     */
    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 2000;
    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;

    // Keys for storing activity state in the Bundle.
    protected final static String REQUESTING_LOCATION_UPDATES_KEY = "requesting-location-updates-key";
    protected final static String LOCATION_KEY = "location-key";
    protected final static String LAST_UPDATED_TIME_STRING_KEY = "last-updated-time-string-key";
    protected Boolean mRequestingLocationUpdates=true;
    /**
     * Provides the entry point to Google Play services.
     */
   public GoogleApiClient mGoogleApiClient;

    /**
     * Stores parameters for requests to the FusedLocationProviderApi.
     */
    protected LocationRequest mLocationRequest;

    /**
     * Represents a geographical location.
     */
    protected Location mCurrentLocation;
    private static DecimalFormat df2 = new DecimalFormat(".##");
    private static final long MIN_TIME = 400;
    private static final float MIN_DISTANCE = 1000;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view != null) {

            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);

        }
        try {
            view = inflater.inflate(R.layout.activity_driver_acceptence, container, false);
            //googleMap = ((MapFragment) getActivity().getFragmentManager().findFragmentById(R.id.mapdirection)).getMap();
        } catch (InflateException e) {
        }

        geocoder = new Geocoder(getActivity(), Locale.getDefault());
        context=getActivity();
        wayfromclient = (TextView) view.findViewById(R.id.text_way);
        clientname = (TextView) view.findViewById(R.id.txt_clientname);
        nameofclient = (TextView) view.findViewById(R.id.edit_nameofclient);
        location = (TextView) view.findViewById(R.id.txt_location);
        editlocation = (TextView) view.findViewById(R.id.edit_location);
        paymethod = (TextView) view.findViewById(R.id.txt_paymethod);
        selectpaymethod = (TextView) view.findViewById(R.id.select_paymethod);
        phoneno = (TextView) view.findViewById(R.id.txt_phn);
        showphonenumbe = (TextView) view.findViewById(R.id.mobile);

        btncancel = (Button) view.findViewById(R.id.btn_cancel);
        btnarrived = (Button) view.findViewById(R.id.btn_arrived);
        btncancel = (Button) view.findViewById(R.id.btn_cancel);
        btnconfrm = (Button) view.findViewById(R.id.btn_confm);
        btn_ridestart = (Button) view.findViewById(R.id.btn_ridestart);
        btntripcomplete = (Button) view.findViewById(R.id.btn_tripcomplet);

        ln_nameofclient = (LinearLayout) view.findViewById(R.id.ln_clintnme);
        ln_arrived = (LinearLayout) view.findViewById(R.id.arrived);
        ln_editlocation = (LinearLayout) view.findViewById(R.id.ln_locations);
        ln_selectpaymethod = (LinearLayout) view.findViewById(R.id.ln_payment);
        ln_confirm_cancle = (LinearLayout) view.findViewById(R.id.ln_confirm_cancle);
        ln_ride = (LinearLayout) view.findViewById(R.id.ln_ride);
        ln_tripcomplete = (LinearLayout) view.findViewById(R.id.ln_tripcomplet);
        ln_startride = (LinearLayout) view.findViewById(R.id.ln_startride);
        ln_phone = (LinearLayout) view.findViewById(R.id.ln_phone);
        googleMap = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map)).getMap();
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.setMyLocationEnabled(true);
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.getUiSettings().setCompassEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        googleMap.getUiSettings().setZoomGesturesEnabled(true);
        googleMap.getUiSettings().setRotateGesturesEnabled(true);

        Bitmap sprite = BitmapFactory.decodeResource(getActivity().getResources(),R.drawable.car_icon);
        Matrix mat = new Matrix();
        mBitmap = Bitmap.createBitmap(sprite, 0, 0, sprite.getWidth(), sprite.getHeight(), mat, true);


        // for trip complete
        driverId = CommonMethod.getdriverid(getActivity());
        userId = CommonMethod.getUserId(getActivity());
        bookingId = CommonMethod.getBookingID(getActivity());
        Log.d("All Ids","DriverId=="+driverId+ "UserId=="+userId+ "BookingId=="+bookingId);




        gps = new GPSTracker(getActivity());
        getLatLong();
      if(MyApplication.bookingId!=null&&MyApplication.userdeclinebookingId!=null) {
       if (MyApplication.userdeclinebookingId.equals(MyApplication.bookingId)) {
           CommonMethod.setTripcomplete(getActivity(),Constant.TRIPCOMPLETE,false);
        MyApplication.userdeclinebookingId = " ";
        CommonMethod.setclientname(getActivity(), Constant.CLIENT_NAME, "");
        CommonMethod.setclientmobile(getActivity(), Constant.CLIENT_MOBILE, "");
        CommonMethod.setclientpickupLat(getActivity(), Constant.CLIENT_PICKLAT, "");
        CommonMethod.setclientpickupLong(getActivity(), Constant.CLIENT_PICLOT, "");
        CommonMethod.setclientestimatedistance(getActivity(), Constant.CLIENT_ESTIMATED_DISTANCE, "");
        CommonMethod.setclientestimatedTime(getActivity(), Constant.CLIENT_ESTIMATED_TIME, "");
        CommonMethod.setclientestimatedTime(getActivity(), Constant.CLIENT_ESTIMATED_TIME, "");
        CommonMethod.setconfirmstatus(getActivity(), Constant.CLIENT_TRIP_CONFIRM, "no");
        MyApplication.message = "";
        MyApplication.bookingId = "";
        MyApplication.driverId = "";
        MyApplication.userId = "";
        MyApplication.pickupLat = "";
        MyApplication.pickupLong = "";
        MyApplication.destinationLat = "";
        MyApplication.destinationLong = "";
        MyApplication.distance = "";
        MyApplication.username = "";
        MyApplication.paymentMethod = "";

        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("upadteride");
        getActivity().sendBroadcast(broadcastIntent);

    }
}


        if (MyApplication.message != "") {
            if (MyApplication.bookingId != "") {
                CommonMethod.setUserId(getActivity(), "USER_ID", MyApplication.userId);

                CommonMethod.setBookingID(getActivity(), Constant.BOOKING_ID, MyApplication.bookingId);

                CommonMethod.setclientname(getActivity(), Constant.CLIENT_NAME, MyApplication.username);




               CommonMethod.setclientmobile(getActivity(), Constant.CLIENT_MOBILE, MyApplication.usermobile);

                CommonMethod.setclientpickupLat(getActivity(), Constant.CLIENT_PICKLAT, MyApplication.pickupLat);
                CommonMethod.setclientpickupLong(getActivity(), Constant.CLIENT_PICLOT, MyApplication.pickupLong);
                CommonMethod.setclientestimatedistance(getActivity(), Constant.CLIENT_ESTIMATED_DISTANCE, MyApplication.distance);



                if(MyApplication.pickupLat!=null&&!MyApplication.pickupLat.equals("")&&MyApplication.pickupLong!=null&&!MyApplication.pickupLong.equals(""))
                {
                    latitude = Double.parseDouble(MyApplication.pickupLat);
                    longitude = Double.parseDouble(MyApplication.pickupLong);
                   if(MyApplication.pickupadress!=null) {
                       editlocation.setText(MyApplication.pickupadress.toString().trim());
                       CommonMethod.setpickupadress(getActivity(), Constant.PICKUPADDRESS, MyApplication.pickupadress);
                       CommonMethod.setDropaddress(getActivity(), Constant.DROPADRESS, MyApplication.destinationsaddress);
                   }
                }
                ln_nameofclient.setVisibility(View.VISIBLE);
                ln_editlocation.setVisibility(View.VISIBLE);
                ln_selectpaymethod.setVisibility(View.VISIBLE);
                ln_arrived.setVisibility(View.VISIBLE);
                ln_confirm_cancle.setVisibility(View.VISIBLE);
                ln_phone.setVisibility(View.VISIBLE);

                ln_tripcomplete.setVisibility(View.GONE);
                wayfromclient.setVisibility(View.VISIBLE);
                ln_ride.setVisibility(View.GONE);
                if(MyApplication.username!=null&&!MyApplication.username.equals("")&&MyApplication.paymentMethod!=null&&!MyApplication.paymentMethod.equals("")&&MyApplication.distance!=null&&!MyApplication.distance.equals(""))
                {
                    nameofclient.setText(MyApplication.username.toString());



                    if(MyApplication.paymentMethod.toString().equals("1"))
                    {
                        selectpaymethod.setText("Credit Card ");
                    }else if(MyApplication.paymentMethod.toString().equals("2")){
                        selectpaymethod.setText("PayPal");
                    }else if(MyApplication.paymentMethod.toString().equals("3"))
                    {
                        selectpaymethod.setText("VIP");
                    }else if(MyApplication.paymentMethod.toString().equals("PayPal"))
                    {
                        selectpaymethod.setText("PayPal");

                    }else if(MyApplication.paymentMethod.toString().equals("Credit Card"))
                    {
                        selectpaymethod.setText("Credit Card ");
                    }else if(MyApplication.paymentMethod.toString().equals("VIP")){
                        selectpaymethod.setText("VIP");
                    }




                    df2.setRoundingMode(RoundingMode.UP);
                    wayfromclient.setText("YOU ARE " +df2.format(Double.valueOf( MyApplication.distance)) + " MILES AWAY FROM THE CLIENT");

                    CommonMethod.setPaymentMode(getActivity(), Constant.CLIENT_PAYMENT_MODE, MyApplication.paymentMethod.toString());
                   // showphonenumbe.setText(CommonMethod.getclientmobile(getActivity()).toString());
                    showphonenumbe.setText("call to " + CommonMethod.getclientname(getActivity()));
                }


            }
        }

        confirmtripstatus = CommonMethod.getconfirmstatus(getActivity());

        if (confirmtripstatus.equals("yes")) {

            if(CommonMethod.getTripcomplete(getActivity()))
            {
                ln_confirm_cancle.setVisibility(View.GONE);
                ln_tripcomplete.setVisibility(View.VISIBLE);
                ln_startride.setVisibility(View.GONE);
                ln_nameofclient.setVisibility(View.VISIBLE);
                ln_editlocation.setVisibility(View.VISIBLE);
                ln_selectpaymethod.setVisibility(View.VISIBLE);
                ln_arrived.setVisibility(View.VISIBLE);
                ln_ride.setVisibility(View.GONE);
                phoneno.setVisibility(View.INVISIBLE);

             //  ln_phone.setVisibility(View.INVISIBLE);
                wayfromclient.setVisibility(View.VISIBLE);

                editlocation.setText(CommonMethod.getpickupadress(getActivity()));

                if(CommonMethod.getPaymentMode(getActivity()).toString().equals("1"))
                {
                    selectpaymethod.setText("Credit Card ");
                }else if(CommonMethod.getPaymentMode(getActivity()).toString().equals("2")){
                    selectpaymethod.setText("PayPal");
                }else if(CommonMethod.getPaymentMode(getActivity()).toString().equals("3"))
                {
                    selectpaymethod.setText("VIP");
                }

               // showphonenumbe.setText(CommonMethod.getclientmobile(getActivity()).toString());
                showphonenumbe.setText("call to " + CommonMethod.getclientname(getActivity()));
                nameofclient.setText(CommonMethod.getclientname(getActivity()).toString());
                df2.setRoundingMode(RoundingMode.UP);
                wayfromclient.setText("YOU ARE " +df2.format(Double.valueOf(CommonMethod.getclientestimatedistance(getActivity()).toString())) + " MILES AWAY FROM THE CLIENT");
                flagmarkerremove=true;
            }else {
                ln_confirm_cancle.setVisibility(View.GONE);
                ln_startride.setVisibility(View.VISIBLE);
                ln_tripcomplete.setVisibility(View.GONE);
                ln_nameofclient.setVisibility(View.VISIBLE);
                ln_editlocation.setVisibility(View.VISIBLE);
                ln_selectpaymethod.setVisibility(View.VISIBLE);
                ln_arrived.setVisibility(View.VISIBLE);
                ln_ride.setVisibility(View.GONE);
                ln_phone.setVisibility(View.VISIBLE);

                wayfromclient.setVisibility(View.VISIBLE);

                editlocation.setText(CommonMethod.getpickupadress(getActivity()));

                if(CommonMethod.getPaymentMode(getActivity()).toString().equals("1"))
                {
                    selectpaymethod.setText("Credit Card ");
                }else if(CommonMethod.getPaymentMode(getActivity()).toString().equals("2")){
                    selectpaymethod.setText("PayPal");
                }else if(CommonMethod.getPaymentMode(getActivity()).toString().equals("3"))
                {
                    selectpaymethod.setText("VIP");
                }

                showphonenumbe.setText("call to " + CommonMethod.getclientname(getActivity()));
                nameofclient.setText(CommonMethod.getclientname(getActivity()).toString());
                df2.setRoundingMode(RoundingMode.UP);
                wayfromclient.setText("YOU ARE " + df2.format(Double.valueOf(CommonMethod.getclientestimatedistance(getActivity()).toString())) + " MILES AWAY FROM THE CLIENT");
                MyApplication.message = "";
                MyApplication.bookingId = "";
                MyApplication.driverId = "";
                MyApplication.userId = "";
                MyApplication.pickupLat = "";
                MyApplication.pickupLong = "";
                MyApplication.destinationLat = "";
                MyApplication.destinationLong = "";
                MyApplication.distance = "";
                MyApplication.username = "";
                MyApplication.paymentMethod = "";

            }
        }


        /*mTypeface = Typeface.createFromAsset(getActivity().getAssets(), "System San Francisco Display Regular.ttf");
        getFontFamily();*/
        btnconfrm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*Intent in = new Intent(getActivity(), DriverTripCompleteActivity.class);
                startActivity(in);*/
                if (CommonMethod.isOnline(getActivity())) {

                    if (CommonMethod.isOnline(getActivity())) {
                        startride = new Start_ride();
                        startride.execute();

                    } else {
                        CommonMethodUtil.showAlert("Please connect internet", getActivity());
                    }

                   // confirm_cancle_trip = new Confirm_cancle_trip();
                   // confirm_cancle_trip.execute();
                   // Accepttype = "yes";


                } else {
                    CommonMethodUtil.showAlert("Please connect internet", getActivity());
                }


            }
        });
        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (CommonMethod.isOnline(getActivity())) {
                    BitmapDrawable bg = (BitmapDrawable)getResources().getDrawable(R.mipmap.smallbutton);
                    btncancel.setBackground(bg);

                    confirm_cancle_trip = new Confirm_cancle_trip();
                    confirm_cancle_trip.execute();
                    Accepttype = "no";

                } else {
                    CommonMethodUtil.showAlert("Please connect internet", getActivity());
                }




            }
        });
        btntripcomplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(CommonMethod.isOnline(getActivity())) {

                    mTripCompleteAsync = new TripCompleteAsync();
                    mTripCompleteAsync.execute("");
                }



            }
        });
        btn_ridestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (CommonMethod.isOnline(getActivity())) {
                    startride = new Start_ride();
                    startride.execute();

                } else {
                    CommonMethodUtil.showAlert("Please connect internet", getActivity());
                }




            }
        });
        btnarrived.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (CommonMethod.isOnline(getActivity())) {
                    arrieved_at_user_locations = new Arrieved_at_user_locations();
                    arrieved_at_user_locations.execute();

                } else {
                    CommonMethodUtil.showAlert("Please connect internet", getActivity());
                }




            }
        });

        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        List<String> providers = locationManager.getProviders(true);
        Location l = null;

        for (int i = 0; i < providers.size(); i++) {
            l = locationManager.getLastKnownLocation(providers.get(i));
            if (l != null) {
                latitude = l.getLatitude();
                longitude = l.getLongitude();
                strAdd = getCompleteAddressString(latitude, longitude);
                // tvAddress.setText("Complete Address : " + strAdd);
                break;
            }
        }
        if (googleMap != null) {

            if (MyApplication.bookingId != "") {

                if( MyApplication.pickupLat!=null&&MyApplication.pickupLong!=null)
                {
                    pickLat = MyApplication.pickupLat;
                    pickLong = MyApplication.pickupLong;
                }

                delLan = String.valueOf(latitude);
                delLong = String.valueOf(longitude);
                    if(MyApplication.pickupLat!=null&&MyApplication.pickupLong!=null&&delLan!=null&&delLong!=null){
                        drawMarkerforuserlocations(pickLat, pickLong, delLan, delLong);
                    }else {}



            } else if (confirmtripstatus.equals("yes")) {

                pickLat = CommonMethod.getclientpickupLat(getActivity());
                pickLong = CommonMethod.getclientpickupLong(getActivity());
                delLan = String.valueOf(latitude);
                delLong = String.valueOf(longitude);
                    if(pickLat!=null&&pickLong!=null&&delLan!=null&&delLong!=null)
                    {
                        drawMarkerforuserlocations(pickLat, pickLong, delLan, delLong);
                    }




            }



        }


        updateValuesFromBundle(savedInstanceState);

        // Kick off the process of building a GoogleApiClient and requesting the LocationServices
        // API.
        buildGoogleApiClient();
        callState = (TextView) view.findViewById(R.id.callState);
        final SinchClient sinchClient = Sinch.getSinchClientBuilder()
                .context(getActivity())
                .userId(CommonMethod.getdriverid(getActivity()))
                .applicationKey("e6cd36ab-43b7-4af1-8ba7-9c6b04400043")
                .applicationSecret("Hm8fLyXxPEOSbQ0XVNihnw==")
                .environmentHost("sandbox.sinch.com")
                .build();

        sinchClient.setSupportCalling(true);
        sinchClient.start();

        showphonenumbe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (call == null) {
                    if(CommonMethod.getclientmobile(getActivity()).toString()!=""&&CommonMethod.getclientmobile(getActivity()).toString()!=null) {
                        call = sinchClient.getCallClient().callPhoneNumber("+"+CommonMethod.getclientmobile(getActivity()).toString());
                        System.out.println("mobile_number"+CommonMethod.getclientmobile(getActivity()).toString());
                        //call = sinchClient.getCallClient().callPhoneNumber("+918510834641");
                        try {
                            call.addCallListener(new SinchCallListener());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        showphonenumbe.setText("Hang Up");
                    }
                } else {
                    call.hangup();
                }
            }
        });




        return view;

    }



    private class SinchCallListener implements CallListener {
        //the call is ended for any reason
        @Override
        public void onCallEnded(Call endedCall) {
            call = null; //no longer a current call
            showphonenumbe.setText("Call"); //change text on button
            callState.setText(""); //empty call state
            //hardware volume buttons should revert to their normal function
            // setVolumeControlStream(AudioManager.USE_DEFAULT_STREAM_TYPE);
        }
        //call is connected
        @Override
        public void onCallEstablished(Call establishedCall) {
            //change the call state in the view
            callState.setText("connected");
            //the hardware volume buttons should control the voice stream volume
            //setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);
        }
        //call is trying to connect
        @Override
        public void onCallProgressing(Call progressingCall) {
            //set call state to "ringing" in the view
            callState.setText("ringing");
        }
        @Override
        public void onShouldSendPushNotification(Call call, List<PushPair> pushPairs) {
            //intentionally left empty
        }
    }

    private void updateValuesFromBundle(Bundle savedInstanceState) {
        //  Log.i(TAG, "Updating values from bundle");
        if (savedInstanceState != null) {
            // Update the value of mRequestingLocationUpdates from the Bundle, and make sure that
            // the Start Updates and Stop Updates buttons are correctly enabled or disabled.
            if (savedInstanceState.keySet().contains(REQUESTING_LOCATION_UPDATES_KEY)) {
                mRequestingLocationUpdates = savedInstanceState.getBoolean(
                        REQUESTING_LOCATION_UPDATES_KEY);
                // setButtonsEnabledState();
            }

            // Update the value of mCurrentLocation from the Bundle and update the UI to show the
            // correct latitude and longitude.
            if (savedInstanceState.keySet().contains(LOCATION_KEY)) {
                // Since LOCATION_KEY was found in the Bundle, we can be sure that mCurrentLocation
                // is not null.
                mCurrentLocation = savedInstanceState.getParcelable(LOCATION_KEY);
            }

            // Update the value of mLastUpdateTime from the Bundle and update the UI.
            if (savedInstanceState.keySet().contains(LAST_UPDATED_TIME_STRING_KEY)) {
                //  mLastUpdateTime = savedInstanceState.getString(LAST_UPDATED_TIME_STRING_KEY);
            }
            //  updateUI();
        }
    }
    protected synchronized void buildGoogleApiClient() {
        //  Log.i(TAG, "Building GoogleApiClient");
       mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        createLocationRequest();
    }
    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }
    protected void startLocationUpdates() {

        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

    }
    protected void stopLocationUpdates() {

       LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }

    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onResume() {
        super.onResume();


        if (mGoogleApiClient.isConnected() && mRequestingLocationUpdates) {
            startLocationUpdates();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        // Stop location updates to save battery, but don't disconnect the GoogleApiClient object.
        if (mGoogleApiClient.isConnected()) {
            stopLocationUpdates();
        }
    }

    @Override
    public void onStop() {
        mGoogleApiClient.disconnect();

        super.onStop();
    }
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putBoolean(REQUESTING_LOCATION_UPDATES_KEY, mRequestingLocationUpdates);
        savedInstanceState.putParcelable(LOCATION_KEY, mCurrentLocation);
        savedInstanceState.putString(LAST_UPDATED_TIME_STRING_KEY, mLastUpdateTime);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        if (mCurrentLocation == null) {
            mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        }


        if (mRequestingLocationUpdates) {
            startLocationUpdates();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    // method for getlocations from latlong
    public String getlocationfromlatlang(String lat, String lot) {
        String Address = "";
        try {
            addresses = geocoder.getFromLocation(Double.parseDouble(lat), Double.parseDouble(lot), 1);
        } catch (Exception e) {
            // TODO Auto-generated catch block

            e.printStackTrace();
            return Address;
        }
        String address = addresses.get(0).getAddressLine(0);
        String city = addresses.get(0).getLocality();
        String state = addresses.get(0).getAdminArea();
        String country = addresses.get(0).getCountryName();
        String postalCode = addresses.get(0).getPostalCode();
        String knownName = addresses.get(0).getFeatureName();
        return Address = address + " " + city;
    }

    static public double initialBearing (double lat1, double long1, double lat2, double long2)
    {
        return (_bearing(lat1, long1, lat2, long2) + 360.0) % 360 ;
    }

    static public double finalBearing(double lat1, double long1, double lat2, double long2)
    {
        return (_bearing(lat2, long2, lat1, long1) + 180.0) % 360;
    }

    static private double _bearing(double lat1, double long1, double lat2, double long2)
    {
        double degToRad = Math.PI / 180.0;
        double phi1 = lat1 * degToRad;
        double phi2 = lat2 * degToRad;
        double lam1 = long1 * degToRad;
        double lam2 = long2 * degToRad;

        return Math.atan2(Math.sin(lam2-lam1)*Math.cos(phi2),
                Math.cos(phi1)*Math.sin(phi2) - Math.sin(phi1)*Math.cos(phi2)*Math.cos(lam2-lam1)
        ) * 180/Math.PI;
    }



    // TODO mehod draw Marker on map
    private void drawMarkerforuserlocations(String pickuplat, String pickuplot, String currentlat, String currentlot) {
        /*if(pickuplat!=null&&pickuplat!=""&&pickuplot!=null&&pickuplot!=""&&currentlat!=null&&currentlat!=""&&currentlot!=null&&currentlot!=null)
        {*/
            picuplatlong = new LatLng(Double.parseDouble(pickuplat), Double.parseDouble(pickuplot));
            destilatlong = new LatLng(Double.parseDouble(currentlat), Double.parseDouble(currentlot));

        final LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(picuplatlong);
        builder.include(destilatlong);

        googleMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {

            @Override
            public void onCameraChange(CameraPosition arg0) {
                googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(builder.build(),3));
                googleMap.setOnCameraChangeListener(null);

            }
        });

        MarkerOptions options = new MarkerOptions();
        MarkerOptions options2 = new MarkerOptions();

        // Setting the position of the marker
        options.position(picuplatlong);
        options2.position(destilatlong);
     if(flagmarkerremove==true)
        {

        }else {

            options2.icon(BitmapDescriptorFactory.fromBitmap(car_icon));
        }
        if(CommonMethod.getclientpickupLat(getActivity())!=""&&CommonMethod.getclientpickupLat(getActivity())!=null)
        {
            options.title("Pick Up Location").snippet(getCompleteAddressString(Double.valueOf(CommonMethod.getclientpickupLat(getActivity())), Double.valueOf(CommonMethod.getclientpickupLong(getActivity()))));
            options2.title("Current Location").snippet(getCompleteAddressString(destilatlong.latitude,destilatlong.longitude));

        }

      googleMap.addMarker(options);

    }


    //*//***for getting lattitude and longitude****//*///
    private void getLatLong() {

        if (gps.canGetLocation()) {
            latitude = gps.getLatitude();
            longitude = gps.getLongitude();
            drawMarkerforcurrentlocations(gps.getLatitude(),gps.getLongitude());
            Log.d("latitude....=", ">>>>>>>>>>>" + latitude);
            Log.d("longitude....=", ">>>>>>>>>>" + longitude);


        } else {
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }
    }

    // TODO mehod draw Marker on map
    private void drawMarkerforcurrentlocations(double currentLocationLatitude,double currentLocationLongitude) {
        if(marker!=null)
        {

            marker.setPosition(new LatLng(currentLocationLatitude,currentLocationLongitude));
            angle = (float) finalBearing(previousLocationLatitude, previousLocationLongitude, currentLocationLatitude, currentLocationLongitude);

            CameraPosition cameraPosition =
                    new CameraPosition.Builder()
                            .target(new LatLng(previousLocationLatitude, previousLocationLongitude))
                            .bearing(angle)
                            .tilt(90)
                            .zoom(15)
                            .build();
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

            previousLocationLatitude = currentLocationLatitude;
            previousLocationLongitude = currentLocationLongitude;

        }else {
            marker = googleMap.addMarker(markerOptions.position(new LatLng(currentLocationLatitude, currentLocationLongitude))
                    .icon(BitmapDescriptorFactory.fromBitmap(mBitmap))
                    .anchor((float) 0.5, (float) 0.5));

            angle = (float) finalBearing(previousLocationLatitude, previousLocationLongitude, currentLocationLatitude, currentLocationLongitude);

            CameraPosition cameraPosition =
                    new CameraPosition.Builder()
                            .target(new LatLng(previousLocationLatitude, previousLocationLongitude))
                            .bearing(angle)
                            .tilt(90)
                            .zoom(18)
                            .build();
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

            previousLocationLatitude = currentLocationLatitude;
            previousLocationLongitude = currentLocationLongitude;
        }

    }

    @Override
    public void onLocationChanged(Location location) {
        currentLocationLatitude = location.getLatitude();
        currentLocationLongitude = location.getLongitude();
        if(flagmarkerremove==true)
        {
            RidecarongoogleMap(currentLocationLatitude,currentLocationLongitude);

        }else {
            drawMarkerforcurrentlocations(currentLocationLatitude,currentLocationLongitude);
        }

       //Toast.makeText(getActivity(), "latitude"+currentLocationLatitude+"longitude"+currentLocationLongitude,Toast.LENGTH_LONG).show();

    }


    public void onStatusChanged(String provider, int status, Bundle extras) {

    }


    public void onProviderEnabled(String provider) {

    }

    public void onProviderDisabled(String provider) {

    }

    private void getFontFamily() {
        wayfromclient.setTypeface(mTypeface);
        clientname.setTypeface(mTypeface);
        nameofclient.setTypeface(mTypeface);
        location.setTypeface(mTypeface);
        editlocation.setTypeface(mTypeface);
        paymethod.setTypeface(mTypeface);
        selectpaymethod.setTypeface(mTypeface);
        btncancel.setTypeface(mTypeface);
        btnconfrm.setTypeface(mTypeface);
    }

    public static String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> addresses = geocoder
                    .getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
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

    // Asyntask for user Ride confirm
    public class Confirm_cancle_trip extends AsyncTask<String, Void, String> {
        private Dialog mDialog;
        private String response;


        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            mDialog = ProgressDialog.show(getActivity(), "", "Loading...", true);
            mDialog.setCancelable(false);
        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            response = confir_canclemtrip();
            return response;
        }

        @Override
        protected void onPostExecute(String response) {
            // TODO Auto-generated method stub
            super.onPostExecute(response);
            if (mDialog != null) {
                mDialog.dismiss();
            }
            Log.d("Confirm_trip_Response", "" + response);

            //responseCode
            JSONObject object, userdetail;

            if (response != null) {

                try {

                    object = new JSONObject(response);

                    String responseCode = object.getString("responseCode");
                    System.out.println("response code=" + responseCode);


                    // String userid=object.getString("userId");
                    String responsemsg = object.getString("responseMsg");
                    System.out.println("responsemsg confirm driver=" + responsemsg);
                    if (responseCode.equals("200")) {
                        Toast.makeText(getActivity(),"Trip confirm successfully" , Toast.LENGTH_LONG).show();
                        userdetail = object.getJSONObject("userDetail");
                        String cname = userdetail.getString("name");
                        String cmobileNO = userdetail.getString("mobileNO");
                        String cpickupLat = userdetail.getString("pickupLat");
                        String cpickupLong = userdetail.getString("pickupLong");
                        String cestimatedistance = userdetail.getString("estimatedistance");
                        String cestimatedTime = userdetail.getString("estimatedTime");

                       // CommonMethod.setprofileupdat(getActivity(),"profileupdate",true);



                        CommonMethod.setclientname(getActivity(), Constant.CLIENT_NAME, cname);
                        CommonMethod.setclientmobile(getActivity(), Constant.CLIENT_MOBILE, cmobileNO);

                        CommonMethod.setclientpickupLat(getActivity(), Constant.CLIENT_PICKLAT, cpickupLat);
                        CommonMethod.setclientpickupLong(getActivity(), Constant.CLIENT_PICLOT, cpickupLong);


                        CommonMethod.setclientestimatedistance(getActivity(), Constant.CLIENT_ESTIMATED_DISTANCE, cestimatedistance);
                        CommonMethod.setclientestimatedTime(getActivity(), Constant.CLIENT_ESTIMATED_TIME, cestimatedTime);
                        CommonMethod.setclientestimatedTime(getActivity(), Constant.CLIENT_ESTIMATED_TIME, cestimatedTime);
                        CommonMethod.setconfirmstatus(getActivity(), Constant.CLIENT_TRIP_CONFIRM, "yes");

                        Intent broadcastIntent = new Intent();
                        broadcastIntent.setAction("upadteride");
                        getActivity().sendBroadcast(broadcastIntent);

                        ln_confirm_cancle.setVisibility(View.GONE);



                    } else if(responseCode.equals("0")){
                        Toast.makeText(getActivity(),responsemsg , Toast.LENGTH_LONG).show();
                        CommonMethod.setclientname(getActivity(), Constant.CLIENT_NAME,"");
                        CommonMethod.setclientmobile(getActivity(), Constant.CLIENT_MOBILE,"");
                        CommonMethod.setclientpickupLat(getActivity(), Constant.CLIENT_PICKLAT,"");
                        CommonMethod.setclientpickupLong(getActivity(), Constant.CLIENT_PICLOT,"");
                        CommonMethod.setclientestimatedistance(getActivity(), Constant.CLIENT_ESTIMATED_DISTANCE,"");
                        CommonMethod.setclientestimatedTime(getActivity(), Constant.CLIENT_ESTIMATED_TIME,"");
                        CommonMethod.setclientestimatedTime(getActivity(), Constant.CLIENT_ESTIMATED_TIME,"");
                        CommonMethod.setconfirmstatus(getActivity(), Constant.CLIENT_TRIP_CONFIRM,"no");
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
                        broadcastIntent.setAction("upadteride");
                        getActivity().sendBroadcast(broadcastIntent);



                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }


            } else {

            }
        }

        private String confir_canclemtrip() {
            String url = ConstantUtil.CONFIRM_TRIP;
            HttpClient client = new HttpClient(url);
            Log.e("before connection", "" + url);
            try {
                client.connectForMultipart();
                Log.e("after connection", "" + url);
                client.addFormPart("userId", MyApplication.userId);
                System.out.println("Accepttype==" + Accepttype);
                client.addFormPart("accept", Accepttype);
                client.addFormPart("bookingId", MyApplication.bookingId);
                client.addFormPart("pickupLat", MyApplication.pickupLat);
                client.addFormPart("pickupLong", MyApplication.pickupLong);
                client.addFormPart("destLat", MyApplication.destinationLat);
                client.addFormPart("destLong", MyApplication.destinationLong);
                client.addFormPart("cabType", CommonMethod.getCabType(getActivity()));

                client.addFormPart("paymentMethod", MyApplication.paymentMethod);
                client.addFormPart("distance", MyApplication.distance);
                client.addFormPart("driverId", CommonMethod.getdriverid(getActivity()));
                client.addFormPart("currLat", String.valueOf(latitude));
                client.addFormPart("currLong", String.valueOf(longitude));

                client.addFormPart("pickupaddress", MyApplication.pickupadress);
                client.addFormPart("destinationaddress", MyApplication.destinationsaddress);

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

    // Asyntask for Start ride
    public class Start_ride extends AsyncTask<String, Void, String> {
        private Dialog mDialog;
        private String response;


        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            mDialog = ProgressDialog.show(getActivity(), "", "Loading...", true);
            mDialog.setCancelable(false);
        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            response = start_ride();
            return response;
        }

        @Override
        protected void onPostExecute(String response) {
            // TODO Auto-generated method stub
            super.onPostExecute(response);
            if (mDialog != null) {
                mDialog.dismiss();
            }
            Log.d("start_ride", "" + response);

            //responseCode
            JSONObject object;

            if (response != null) {

                try {

                    object = new JSONObject(response);
                    String responsemsg = object.getString("responseMsg");
                    System.out.println("responseMsgfromserver" + responsemsg);

                    if (responsemsg.equals("success ride start")) {
                        Toast.makeText(getActivity(),"Trip Started" , Toast.LENGTH_LONG).show();
                        CommonMethod.setTripcomplete(getActivity(),Constant.TRIPCOMPLETE,true);

                        ln_confirm_cancle.setVisibility(View.GONE);
                        ln_tripcomplete.setVisibility(View.VISIBLE);
                        ln_startride.setVisibility(View.GONE);
                        ln_nameofclient.setVisibility(View.VISIBLE);
                        ln_editlocation.setVisibility(View.VISIBLE);
                        ln_selectpaymethod.setVisibility(View.VISIBLE);
                        ln_arrived.setVisibility(View.VISIBLE);
                        ln_ride.setVisibility(View.GONE);
                        ln_arrived.setVisibility(View.GONE);
                        phoneno.setVisibility(View.VISIBLE);

                        ln_phone.setVisibility(View.VISIBLE);

                        wayfromclient.setVisibility(View.INVISIBLE);
                       // showphonenumbe.setText(CommonMethod.getclientmobile(getActivity()).toString());

                        showphonenumbe.setText("call to " + CommonMethod.getclientname(getActivity()));
                        nameofclient.setText(CommonMethod.getclientname(getActivity()).toString());
                        df2.setRoundingMode(RoundingMode.UP);
                        wayfromclient.setText("YOU ARE " +df2.format(Double.valueOf(CommonMethod.getclientestimatedistance(getActivity()).toString())) + " MILES AWAY FROM THE CLIENT");
                        flagmarkerremove=true;
                    } else {
                        CommonMethod.showAlert("RIDE CAN NOT START", getActivity());
                    }


                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            } else {

            }
        }

        private String start_ride() {
            String url = ConstantUtil.RIDE_START;
            HttpClient client = new HttpClient(url);
            Log.e("before connection", "" + url);
            try {
                client.connectForMultipart();
                Log.e("after connection", "" + url);
                client.addFormPart("userId", CommonMethod.getUserId(getActivity()));
                client.addFormPart("driverId", CommonMethod.getdriverid(getActivity()));
                client.addFormPart("ride", "yes");
                client.addFormPart("bookingId", CommonMethod.getBookingID(getActivity()));
                client.finishMultipart();
                response = client.getResponse().toString();
                Log.d(" ride_response", response);

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return response;
        }
    }
// Asyntask for arrived diver at user locations send sms fire


    public class Arrieved_at_user_locations extends AsyncTask<String, Void, String> {
        private Dialog mDialog;
        private String response;


        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            mDialog = ProgressDialog.show(getActivity(), "", "Loading...", true);
            mDialog.setCancelable(false);
        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            response = HTTP_method_Arrieved_at_user_locations();
            return response;
        }

        @Override
        protected void onPostExecute(String response) {
            // TODO Auto-generated method stub
            super.onPostExecute(response);
            if (mDialog != null) {
                mDialog.dismiss();
            }
            JSONObject object;
            if (response != null) {
                try {
                    object = new JSONObject(response);
                    String responsemsg = object.getString("responseMsg");
                    System.out.println("Arived_responseMsgfromserver" + responsemsg);
                    CommonMethod.showAlert("your message was successfuly sent to passenger.", getActivity());
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            } else {

            }
        }

    private String HTTP_method_Arrieved_at_user_locations() {
        String url = ConstantUtil.ARRIVED_DRIVER;
        HttpClient client = new HttpClient(url);
        try {
            client.connectForMultipart();
            client.addFormPart("userid", CommonMethod.getUserId(getActivity()));
            client.finishMultipart();
            response = client.getResponse().toString();
            Log.d(" arrived_response", response);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return response;
    }
}

    //Call AsyncTask for TripCompletion
    private class TripCompleteAsync extends AsyncTask<String,Void,String>{
        private Dialog mDialog;
        private String response;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mDialog = ProgressDialog.show(getActivity(), "", "Loading...", true);
            mDialog.setCancelable(false);
        }

        @Override
        protected String doInBackground(String... params) {
            response = callTripConmpletion();
            return response;

        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (mDialog != null) {
                mDialog.dismiss();
            }

            JSONObject object, ridedetails;

            if (response != null) {
                try {

                    object = new JSONObject(response);
                    String responseCode = object.getString("responseCode");
                    Log.e("responseTripComplete",""+object.toString(2));
                    String responseMessage = object.getString("responseMsg");
                    if (responseCode.equals("200")) {
                        Toast.makeText(getActivity(),"Trip Complete Successfully", Toast.LENGTH_LONG).show();
                        ridedetails=object.getJSONObject("rideData");
                        if(ridedetails!=null)
                        {
                            CommonMethod.setTripcomplete(getActivity(),Constant.TRIPCOMPLETE,false);
                            MyApplication.totalfair=ridedetails.getString("total_fare");
                            String distance=ridedetails.getString("distance");
                            String time_taken=ridedetails.getString("time_taken");



                            if(CommonMethod.getPaymentMode(getActivity()).equals("1"))
                            {
                                MyApplication.trip_chrg="$"+ " "+MyApplication.totalfair+" "+"HAS BEEN CHARGED TO YOUR "+" Credit Card";

                            }else if(CommonMethod.getPaymentMode(getActivity()).equals("2")){

                                MyApplication.trip_chrg="$"+ " "+MyApplication.totalfair+" "+"HAS BEEN CHARGED TO YOUR "+" PayPal";

                            }else if(CommonMethod.getPaymentMode(getActivity()).equals("3"))
                            {
                                MyApplication.trip_chrg="$"+ " "+MyApplication.totalfair+" "+"HAS BEEN CHARGED TO YOUR "+" VIP";

                            }else if(MyApplication.paymentMethod.toString().equals("PayPal"))
                            {
                                MyApplication.trip_chrg="$"+ " "+MyApplication.totalfair+" "+"HAS BEEN CHARGED TO YOUR "+" PayPal";
                            }else if(MyApplication.paymentMethod.toString().equals("Credit Card"))
                            {
                                MyApplication.trip_chrg="$"+ " "+MyApplication.totalfair+" "+"HAS BEEN CHARGED TO YOUR "+" Credit Card";
                            }else if(MyApplication.paymentMethod.toString().equals("VIP")){
                                MyApplication.trip_chrg="$"+ " "+MyApplication.totalfair+" "+"HAS BEEN CHARGED TO YOUR "+" VIP";
                            }

                            Intent in = new Intent(getActivity(), TripCompleteActivity.class);
                            startActivity(in);


                        }

                    }
                    else{
                        Toast toast = Toast.makeText(getActivity(), responseMessage, Toast.LENGTH_LONG);
                        toast.show();
                        toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 35, 250);
                    }
                } catch (JSONException e) {

                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } else {

            }
        }



    private String callTripConmpletion() {
        String url = ConstantUtil.TRIP_COMPLETION;

        HttpClient client = new HttpClient(url);
        Log.e("after connection", "" + url);
        Log.e("Before Connection", "" + url);
        try {
            driverId = CommonMethod.getdriverid(getActivity());
            userId = CommonMethod.getUserId(getActivity());
            bookingId = CommonMethod.getBookingID(getActivity());


            System.out.println("userid>>>>>"+userId);
            System.out.println("driverId>>>>"+driverId);
            System.out.println("bookingId>>>>>"+bookingId);
            System.out.println("currLat>>>>>>"+String.valueOf(latitude));
            System.out.println("currLong>>>>>>>>"+String.valueOf(longitude));

            client.connectForMultipart();
            client.addFormPart("userId", userId);
            client.addFormPart("driverId",driverId);
            client.addFormPart("bookingId",bookingId);
            client.addFormPart("currLat",String.valueOf(latitude));
            client.addFormPart("currLong",String.valueOf(longitude));
            client.finishMultipart();
            response = client.getResponse().toString();



            System.out.println("response_tripcompleate>>>>>>>>>>>>>"+response.toString());
            Log.d("TripCompletion", response);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return response;
    }


}

    public  void RidecarongoogleMap(double currentLocationLatitude,double currentLocationLongitude)
    {
        Matrix mat = new Matrix();
        if(previousLocationLatitude>0.0d)
        {

                angle=(float)finalBearing(previousLocationLatitude, previousLocationLongitude, currentLocationLatitude, currentLocationLongitude);
                mat.preRotate(angle);
                marker.setPosition(new LatLng(currentLocationLatitude, currentLocationLongitude));
                marker.setFlat(true);
                marker.setAnchor((float)0.5, (float)0.5);
                marker.setRotation(angle);


            polylineOptions.add(new LatLng(currentLocationLatitude , currentLocationLongitude ),
                    new LatLng( previousLocationLatitude, previousLocationLongitude))
                    .width(20).color(Color.BLUE);


           CameraPosition cameraPosition = new CameraPosition.Builder()
                            .target(new LatLng(previousLocationLatitude, previousLocationLongitude))
                            .zoom(15.5f)
                            .bearing(angle)
                            .tilt(30)
                            .build();


            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

            googleMap.addPolyline(polylineOptions);

            previousLocationLatitude=currentLocationLatitude;
            previousLocationLongitude=currentLocationLongitude;

        }else {
            previousLocationLatitude=currentLocationLatitude;
            previousLocationLongitude=currentLocationLongitude;

        }

    }




        protected void onPostExecute(String file_url) {
            wayfromclient.setVisibility(View.GONE);
            if (status.equals("OK")) {
                for (int i = 0; i < polyz.size() - 1; i++) {
                    LatLng src = polyz.get(i);
                    LatLng dest = polyz.get(i + 1);

                    googleMap.addPolyline(new PolylineOptions()
                            .add(new LatLng(src.latitude, src.longitude),
                                    new LatLng(dest.latitude, dest.longitude))
                            .width(15.f).color(Color.BLUE).geodesic(true));

                    animateMarker(googleMap, marker, polyz, false);
                }
            }
        }


    private static void animateMarker(final GoogleMap myMap, final Marker marker, final List<LatLng> directionPoint,
                                      final boolean hideMarker) {
        //   System.out.println("control in  animateMarker " +directionPoint);
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        Projection proj = myMap.getProjection();
        final long duration = 30000;

        final Interpolator interpolator = new LinearInterpolator();

        handler.post(new Runnable() {
            int i = directionPoint.size()-1;

            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;

                float t = interpolator.getInterpolation((float) elapsed
                        / duration);

                if (i > 0) {

                    LatLng src = directionPoint.get(i);
                    LatLng dest = directionPoint.get(i -1);

                    LatLng mapCenter = new LatLng(dest.latitude, dest.longitude);

                    rotateMarker(marker,bearingBetweenLocations(src,dest));

                    marker.setAnchor((float)0.5, (float)0.5);
                    marker.setPosition(directionPoint.get(i));
                    marker.setTitle("Current Location");
                    marker.setSnippet(getCompleteAddressString(mapCenter.latitude,mapCenter.longitude));

                    i--;

                }
                if (t < 1.0) {
                    // Post again 16ms later.
                    handler.postDelayed(this, 16);
                } else {
                    if (hideMarker) {
                        marker.setVisible(false);
                    } else {
                        marker.setVisible(true);
                    }
                }
            }
        });
    }

    // TODO method for  decode polyline points
    private List<LatLng> decodePoly(String encoded) {

        List<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }

        return poly;
    }

public static double bearingBetweenLocations(LatLng latLng1,LatLng latLng2) {

        double PI = 3.14159;
        double lat1 = latLng1.latitude * PI / 180;
        double long1 = latLng1.longitude * PI / 180;
        double lat2 = latLng2.latitude * PI / 180;
        double long2 = latLng2.longitude * PI / 180;

        double dLon = (long2 - long1);

        double y = Math.sin(dLon) * Math.cos(lat2);
        double x = Math.cos(lat1) * Math.sin(lat2) - Math.sin(lat1)
                * Math.cos(lat2) * Math.cos(dLon);

        double brng = Math.atan2(y, x);

        brng = Math.toDegrees(brng);
        brng = (brng + 360) % 360;

        return brng;
    }
    public static void rotateMarker(final Marker marker, final Double toRotation) {
        if(!isMarkerRotating) {
            final Handler handler = new Handler();
            final long start = SystemClock.uptimeMillis();
            final float startRotation = marker.getRotation();
            final long duration = 1000;

            final Interpolator interpolator = new LinearInterpolator();

            handler.post(new Runnable() {
                @Override
                public void run() {
                    isMarkerRotating = true;

                    long elapsed = SystemClock.uptimeMillis() - start;
                    float t = interpolator.getInterpolation((float) elapsed / duration);

                    float rot = t * toRotation.floatValue() + (1 - t) * startRotation;

                    marker.setRotation(-rot > 180 ? rot / 2 : rot);
                    if (t < 1.0) {
                        // Post again 16ms later.
                        handler.postDelayed(this, 16);
                    } else {
                        isMarkerRotating = false;
                    }
                }
            });
        }
    }
    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }
}



