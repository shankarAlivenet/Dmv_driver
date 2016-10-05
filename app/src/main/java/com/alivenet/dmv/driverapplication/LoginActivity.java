package com.alivenet.dmv.driverapplication;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;

import android.provider.Settings;
import android.support.multidex.MultiDex;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gcm.GCMRegistrar;


import static com.alivenet.dmv.driverapplication.CommonUtilities.DISPLAY_MESSAGE_ACTION;
import static com.alivenet.dmv.driverapplication.CommonUtilities.EXTRA_MESSAGE;
import static com.alivenet.dmv.driverapplication.CommonUtilities.SENDER_ID;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.Scanner;

import util.HttpClient;

/**
 * Created by narendra on 6/16/2016.
 */
public class LoginActivity extends AppCompatActivity {
    private static final String AUTH_KEY = "key=YOUR KEY SERVER";
    Toolbar toolbar;
    TextView txtCreateAccount;
    Button btnlogin;
    private EditText med_email, med_password;
    private GPSTracker gps;
    Typeface mTypeface;
    protected Context context;
    protected double latitude,longitude;

    String email ;
    String password;
    CheckBox chkRememberMe;
    String latvalue;
    String longvalue;
    // GCM//Asyntask
    AsyncTask<Void, Void, Void> mRegisterTask;
    // Connection detector
    ConnectionDetector cd;
    String regId = "";
    String deviceId = "";
    boolean login =false;
    TextView txtfogot;

    private static final String SPF_NAME = "0"; //  <--- Add this





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
       setSupportActionBar(toolbar);
        btnlogin = (Button) findViewById(R.id.logindriver);
        txtCreateAccount = (TextView) findViewById(R.id.tvcreate_account);
        med_email = (EditText) findViewById(R.id.emaildriver);
        med_password = (EditText) findViewById(R.id.passdriver);
        chkRememberMe = (CheckBox) findViewById(R.id.chkremember);
        txtfogot=(TextView)findViewById(R.id.txt_forgotpass) ;

        latvalue=String.valueOf(latitude);
        longvalue=String.valueOf(longitude);

      //  logout();
        SpannableString contents = new SpannableString(txtfogot.getText().toString().trim());
        contents.setSpan(new UnderlineSpan(), 0, contents.length(), 0);
        txtfogot.setText(contents);

        SpannableString content = new SpannableString(txtCreateAccount.getText().toString().trim());
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        txtCreateAccount.setText(content);
        //remember pass&email
        final SharedPreferences loginPreferences = getSharedPreferences(SPF_NAME, Context.MODE_PRIVATE);

        if(loginPreferences.getBoolean("CHECK",false)==true)
        {
                med_email.setText(loginPreferences.getString("USERNAME",null));
                med_password.setText(loginPreferences.getString("PASSWORD",null));
               chkRememberMe.setChecked(true);
        }else {
            chkRememberMe.setChecked(false);

        }

        chkRememberMe.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if(checkValidationRemeberme())
                {
                    if (((CheckBox) v).isChecked()) {
                        SharedPreferences.Editor editor = loginPreferences.edit();
                        String userName=med_email.getText().toString().trim();
                        String password=med_password.getText().toString().trim();
                        editor.putString("USERNAME", userName);
                        editor.putString("PASSWORD", password);
                        System.out.println("checkboxtrue"+"USERNAME"+userName);
                        System.out.println("checkboxtrue"+"PASSWORD"+password);
                        editor.putBoolean("CHECK", true);
                        editor.apply();

                    }else {
                        SharedPreferences.Editor editor = loginPreferences.edit();
                        editor.putString("USERNAME","");
                        editor.putString("PASSWORD", "");
                        editor.putBoolean("CHECK", false);
                        editor.apply();
                    }

                }


            }
        });

        mTypeface = Typeface.createFromAsset(getAssets(), "System San Francisco Display Regular.ttf");
        getFontFamily();
        txtCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getApplication(), DriverRegistaionActivity.class);
                startActivity(in);
            }
        });
        txtfogot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getApplication(),ForgotPassword.class);
                startActivity(in);
            }
        });
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CommonMethodUtil.isOnline(LoginActivity.this)) {

                    checkValidation();
                }else {
                    CommonMethodUtil.showAlert("Please connect internet", LoginActivity.this);
                }
            }
        });

        gps = new GPSTracker(LoginActivity.this);
        getLatLong();
       //GCM Integration
        deviceId = Settings.Secure.getString(
                LoginActivity.this.getContentResolver(),
                Settings.Secure.ANDROID_ID);

        System.out.println("deviceId"+deviceId);


        cd = new ConnectionDetector(getApplicationContext());
        // Make sure the device has the proper dependencies.
        GCMRegistrar.checkDevice(this);
        // Make sure the manifest was properly set - comment out this line
        // while developing the app, then uncomment it when it's ready.
        GCMRegistrar.checkManifest(this);

        registerReceiver(mHandleMessageReceiver, new IntentFilter(DISPLAY_MESSAGE_ACTION));
        //unregisterReceiver(mHandleMessageReceiver);
        // Get GCM registration id
        regId = GCMRegistrar.getRegistrationId(this);

        System.out.println("regId"+regId);
        if (regId.equals("")) {
            // Registration is not present, register now with GCM
            GCMRegistrar.register(this, SENDER_ID);
            //regId = GCMRegistrar.getRegistrationId(this);
            //System.out.println("regId2nd"+regId);

        } else {
            // Device is already registered on GCM
            if (GCMRegistrar.isRegisteredOnServer(this)) {
                // Skips registration.
				/*
				 * Toast.makeText(getApplicationContext(),
				 * "Already registered with GCM", Toast.LENGTH_LONG) .show();
				 */
            } else {


                System.out.println("Calling GCM");
                final Context context = this;
                mRegisterTask = new AsyncTask<Void, Void, Void>() {

                    @Override
                    protected Void doInBackground(Void... params) {
                        ServerUtilities.register(context, regId);

                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void result) {
                        mRegisterTask = null;
                    }

                };
                mRegisterTask.execute(null, null, null);
            }

        }

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

            gps.showSettingsAlert();
        }
    }


    // TODO: method for CM IntegrationG with BroadcastReceiver
    private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String newMessage = intent.getExtras().getString(EXTRA_MESSAGE);
            //String count = intent.getExtras().getString(EXTRA_COUNT);

            // Waking up mobile if it is sleeping
            WakeLocker.acquire(getApplicationContext());


            WakeLocker.release();
        }
    };
    public class LoginActivityAsync extends AsyncTask<String, Void, String> {
        private Dialog mDialog;
        private String response;
        private String Loginone;
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            mDialog = ProgressDialog.show(LoginActivity.this, "", "Loading...", true);
            mDialog.setCancelable(false);
        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            response = callCityService();
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
                    String message=object.getString("responseMsg");
                    String responseCode = object.getString("responseCode");
                    String atempt = object.optString("invalid_attemp_today");
                    // String responseMessage = object.getString("responseMessage");
                    Log.d("object", "" + object);

                    if (responseCode.equals("200")) {
                        //loginAction();
                        String driverId=object.getString("userId");
                        String cabtype=object.getString("cabType");

                         CommonMethod.setDriverId(LoginActivity.this,"DRIVER_ID",driverId);
                         CommonMethod.setcabType(LoginActivity.this,Constant.CABTYPE,cabtype);

                        login=true;
                        CommonMethodUtil.savelogin(LoginActivity.this,"loginType",login);

                        Intent intent = new Intent(getApplication(), MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                        Toast toast = Toast.makeText(getApplicationContext(), "You are logged in successfully", Toast.LENGTH_LONG);
                        toast.show();
                        toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 35, 250);
                    }
                   else {
                        if(atempt.equals("3"))
                        {
                            MyAlert("Please reset password",LoginActivity.this);
                        }else {
                            new LoginDialogActivity(LoginActivity.this, "hello",message).show();
                        }

                    }

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } else {

            }
        }

        private String callCityService() {
            String url = ConstantUtil.LOG_IN;
             email = med_email.getText().toString().trim();
             password = med_password.getText().toString().trim();

            HttpClient client = new HttpClient(url);
            Log.e("before connection", "" + url);
            try {
                client.connectForMultipart();

                Log.e("after connection", "" + url);
                client.addFormPart("email", email);
                client.addFormPart("password", password);
                client.addFormPart("lat",latvalue);
                client.addFormPart("long",longvalue);
                client.addFormPart("fcmid",regId);
                client.addFormPart("deviceType","android");
                client.addFormPart("deviceid",deviceId);

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

    private boolean checkValidation() {
        if (med_email.getText().toString().trim().length() == 0) {
            CommonMethodUtil.showAlert("Please enter user name", LoginActivity.this);
            return false;
        } else if (med_password.getText().toString().trim().length() == 0) {
            CommonMethodUtil.showAlert("Please enter password", LoginActivity.this);
            return false;
        }else if (CommonMethod.isOnline(getApplicationContext())) {

            if (regId.equals("")) {
                // Registration is not present, register now with GCM
                GCMRegistrar.register(this, SENDER_ID);
            } else {
                // Device is already registered on GCM
                if (GCMRegistrar.isRegisteredOnServer(this)) {
                    // Skips registration.

                }
            }

            regId = GCMRegistrar.getRegistrationId(this);
            System.out.println(regId);

            if (regId.length() > 0) {

                LoginActivityAsync login = new LoginActivityAsync();
                login.execute();

            }
        }
            return true;
    }
    //remember email&pass
    private boolean checkValidationRemeberme() {
        if (med_email.getText().toString().trim().length() == 0) {
            CommonMethodUtil.showAlert("Please enter Email", LoginActivity.this);
            chkRememberMe.setChecked(false);
            return false;
        }else if (med_password.getText().toString().trim().length() == 0) {
            CommonMethodUtil.showAlert("Please enter Password", LoginActivity.this);
            chkRememberMe.setChecked(false);
            return false;
        }
        return true;
    }
    private void getFontFamily() {
        txtCreateAccount.setTypeface(mTypeface);
        med_email.setTypeface(mTypeface);
        med_password.setTypeface(mTypeface);
    }

    private String convertStreamToString(InputStream is) {
        Scanner s = new Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next().replace(",", ",\n") : "";
    }
    public void logout(){
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("ACTION_LOGOUT");
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d("onReceive","Logout in progress");
                //At this point you should start the login activity and finish this one
                Intent in = new Intent(LoginActivity.this,LoginActivity.class);
                in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(in);
                finish();
            }
        }, intentFilter);

    }
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
    public void MyAlert(String message, Activity context) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message).setCancelable(false)
                .setPositiveButton("OK",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent in = new Intent(getApplication(),ForgotPassword.class);
                        startActivity(in);
                        finish();
                    }
                });
        try {
            builder.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}















