package com.alivenet.dmv.driverapplication;


import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.HttpStatus;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.StatusLine;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.message.BasicNameValuePair;
import cz.msebera.android.httpclient.params.BasicHttpParams;
import cz.msebera.android.httpclient.params.HttpParams;

/**
 * Created by navin on 7/12/2016.
 */
public class AndroidLocationServices extends Service {
    PowerManager.WakeLock wakeLock;

    private LocationManager locationManager;
    public String lat,logi;
    public static String id;
    public static String finalid;
    public AndroidLocationServices() {
        // TODO Auto-generated constructor stub
    }

    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        System.out.println("calling background services  on create ");
        PowerManager pm = (PowerManager) getSystemService(this.POWER_SERVICE);

        wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "DoNotSleep");
        // this acquires the wake lock
        wakeLock.acquire();
        Log.e("Google", "Service Created");


    }

    @Override
    @Deprecated
    public void onStart(Intent intent, int startId) {
        // TODO Auto-generated method stub
        super.onStart(intent, startId);

        Log.e("Google", "Service Started");
        System.out.println("calling background services ");
      if(intent!=null) {
            id = intent.getExtras().getString("id");
          CommonMethod.setDriverId_sevice(AndroidLocationServices.this,"DRIVER_ID_SERVICE",id);
        }
        if(id==null){
            id= CommonMethod.getdriverid_service(AndroidLocationServices.this);
        }
        locationManager = (LocationManager) getApplicationContext()
                .getSystemService(Context.LOCATION_SERVICE);

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                5000, 5, listener);

    }

    private LocationListener listener = new LocationListener() {

        @Override
        public void onLocationChanged(final Location location) {
            // TODO Auto-generated method stub

            Log.e("Google", "Location Changed");

            if (location == null)
                return;
            ScheduledExecutorService scheduleTaskExecutor = Executors.newScheduledThreadPool(5);
            // This schedule a runnable task every 2 seconds
            scheduleTaskExecutor.scheduleAtFixedRate(new Runnable() {
                public void run() {
                    System.out.println(" @DMV_APP send locations services is runing  ");
                    System.out.println(" @DMV_APP send locations == "+id);
                    lat = String.valueOf(location.getLatitude());
                    logi = String.valueOf(location.getLongitude());
                    Log.e("sndlatitude", lat + "");
                    Log.e("sndlongitude", logi + "");
                    SendLocationsAsync sndloc = new SendLocationsAsync();
                    sndloc.execute();
                }
            }, 0, 5, TimeUnit.SECONDS);


        }

        @Override
        public void onProviderDisabled(String provider) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onProviderEnabled(String provider) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            // TODO Auto-generated method stub

        }
    };

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        locationManager.removeUpdates(listener);
        locationManager = null;
        wakeLock.release();

    }


    public class SendLocationsAsync extends AsyncTask<String, Void, String> {
        private String response;


        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            System.out.println("calling time  sendlocations()");
            response = sendlocations();
            return response;
        }

        @Override
        protected void onPostExecute(String response) {
            // TODO Auto-generated method stub
            super.onPostExecute(response);

            Log.d("sendlocationResponse", "" + response);

            //responseCode
            JSONObject object;

            if (response != null) {
              /*  try {

                  //  object = new JSONObject(response);
                 //   String success = object.getString("responseCode");
                   // String driverId = object.getString("driverId");
                    // CommonMethod.setDriverId_sevice(AndroidLocationServices.this,"DRIVER_ID_SERVICE",driverId);
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }*/
            } else {

            }
        }

        private String sendlocations() {
            String url = ConstantUtil.Sendlocation;
            System.out.println("calling time  in method sendlocations()");
            util.HttpClient client = new util.HttpClient(url);
            System.out.println("calling time in method below  HttpClient sendlocations()");
            Log.e("before connection", "" + url);
            try {
                client.connectForMultipart();
                Log.e("after connection", "" + url);
                client.addFormPart("lat",lat);
                client.addFormPart("long",logi);
                client.addFormPart("id",id);

                Log.d("client", client.toString());
                client.finishMultipart();
                System.out.println("sendlocations"+"hahahahahahaha");
                System.out.println("sendlocations id==>> "+id);
                response = client.getResponse();
                System.out.println("sendlocations"+response);
                Log.d("sendlocations response", response);

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return response;
        }


    }


}