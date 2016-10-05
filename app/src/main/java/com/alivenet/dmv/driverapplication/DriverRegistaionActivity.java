package com.alivenet.dmv.driverapplication;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;

import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.multidex.MultiDex;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsoluteLayout;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gcm.GCMRegistrar;
import com.hbb20.CountryCodePicker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;


import eu.janmuller.android.simplecropimage.CropImage;
import util.HttpClient;


import static com.alivenet.dmv.driverapplication.CommonUtilities.DISPLAY_MESSAGE_ACTION;
import static com.alivenet.dmv.driverapplication.CommonUtilities.EXTRA_MESSAGE;
import static com.alivenet.dmv.driverapplication.CommonUtilities.SENDER_ID;

public class DriverRegistaionActivity extends AppCompatActivity{
    Button btnnext;
    Spinner spincabname;
     //TextView experdate;
    EditText fname,lname,pass,edit_confirmemail,dctc,confirmpass, cell,email,licenid,proofincu,nameinsu,vehicle,tabsate,et_pvin,experdate;
    String first_name,last_name,password,dctc_id,experatin_date,cell_phone,emai_Id,licence_id,proof_insu,name_insu,vehicle_name,tab_state,PVIn;
    ImageView profile_image;
    int mYear,mMonth,mDay;
    ArrayList<String> cablist = new ArrayList<String>();
    String cabId;
    boolean setprofile=false;
    ArrayList<CabType> cabtypeList = new ArrayList<CabType>();
     Typeface mTypeface;

    // GCM//Asyntask
    AsyncTask<Void, Void, Void> mRegisterTask;
    // Connection detector
    ConnectionDetector cd;
    String regId = "";
    String deviceId = "";

    SharedPreferences mPref;
    public static final String MYPREF = "user_info";
    protected static final int CAMERA_REQUEST = 0;
    int REQUEST_CAMERA = 0;
    int PICK_IMAGE = 1;
    private Bitmap bm;
    byte[] mData;
    double imageName;
    private ImageLoader1 imgload;
    private GPSTracker gps;
    double latitude,longitude;
    String latitudeval ;
    CountryCodePicker ccp;
    String longitudeval;
    private Bitmap bitmap;
    String getdate,monthd;
    String a;
    int keyDel;
    int len;
    Toolbar toolbar;
    private boolean isDelete;
    private Bitmap mRawBitmap;
    Spinner spinnerCountryCode;
    String mcountrycode="value";
    String[] countryCodeArray = {"+1", "+1242", "+1246", "+1264", "+1268", "+1284", "+1340",
            "+1345", "+1441", "+1473", "+1649", "+1664", "+1670", "+1671", "+1684", "+1758", "+1767",
            "+1784", "+1787",  "+1808", "+1809", "+1868", "+1869", "+1869", "+1876", "+20", "+212",
            "+213", "+216", "+218", "+220", "+221", "+222", "+223", "+224", "+225", "+226", "+227", "+228",
            "+229", "+230", "+231", "+232", "+233", "+234", "+235","+236", "+237", "+238", "+239",
            "+240", "+241", "+242", "+242", "+243", "+243", "+244", "+245", "+246", "+246", "+247", "+248",
            "+249", "+250", "+251", "+252", "+253", "+254",  "+255", "+256", "+257", "+258", "+260",
            "+261", "+262",  "+263", "+264", "+265", "+266", "+267", "+268",
            "+269", "+27", "+290", "+291", "+297", "+298", "+299", "+30", "+31", "+32",
            "+33", "+34", "+350", "+351", "+352", "+353", "+354", "+355", "+356",
            "+357", "+358", "+359", "+36", "+370", "+371", "+372", "+373", "+374", "+375",
            "+376", "+377", "+378", "+380", "+381", "+382", "+385", "+386", "+387", "+389", "+39",
            "+39066", "+40", "+41", "+420", "+421", "+423", "+43", "+44",
            "+45", "+46", "+47", "+48", "+49", "+500", "+501", "+502", "+503", "+504", "+505",
            "+506", "+507", "+508", "+509", "+51", "+52", "+53", "+5399", "+5399", "+54", "+55", "+56", "+56", "+57",
            "+58", "+590", "+591", "+592", "+593", "+594", "+595","+596","+597", "+598", "+599",
            "+60", "+61", "+62", "+63", "+64", "+65", "+66", "+670", "+672", "+673",
            "+674", "+675", "+676", "+677", "+678", "+679", "+680", "+681", "+682", "+683", "+685", "+686", "+687", "+688",
            "+689", "+690", "+691", "+692", "+7", "+76", "+7840", "+800", "+808", "+81", "+82", "+84",
            "+850", "+852", "+853", "+855", "+856", "+857", "+86",
            "+870", "+878", "+880", "+881", "+8810", "+8812", "+8816", "+8818", "+88213", "+88216", "+886", "+90",
            "+91", "+92", "+93", "+94", "+95", "+960", "+961", "+962", "+963", "+964", "+965",
            "+966", "+967", "+968", "+970", "+971", "+972", "+973", "+974", "+975", "+976",
            "+977", "+98", "+992", "+993", "+994", "+995", "+996", "+998"};



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_registaion);

        toolbar = (Toolbar) findViewById(R.id.maintoolbar);
        toolbar.setNavigationIcon(getResources().getDrawable(R.mipmap.back_icon));
        // setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        spincabname = (Spinner) findViewById(R.id.cabspinner);
        profile_image=(ImageView)findViewById(R.id.img_prifile_show);
        fname=(EditText)findViewById(R.id.edit_firname);
        lname=(EditText)findViewById(R.id.edit_lname);
        pass=(EditText)findViewById(R.id.edit_pass);
        confirmpass=(EditText)findViewById(R.id.edit_confirmpass);
        dctc=(EditText)findViewById(R.id.edit_dctc);
        experdate=(EditText) findViewById(R.id.edit_exprdate);
        cell=(EditText)findViewById(R.id.edit_cell);
        email=(EditText)findViewById(R.id.edit_email);
        edit_confirmemail=(EditText)findViewById(R.id.edit_confirmemail);
        licenid=(EditText)findViewById(R.id.edit_licenid);
        proofincu=(EditText)findViewById(R.id.edit_proofinsu);
        nameinsu=(EditText)findViewById(R.id.edit_nameinsu);
        vehicle=(EditText)findViewById(R.id.edit_vehicle);
        tabsate=(EditText)findViewById(R.id.edit_tabstate);
        btnnext=(Button)findViewById(R.id.btndriver);
        et_pvin=(EditText)findViewById(R.id.edit_pvin);
        spinnerCountryCode = (Spinner) findViewById(R.id.strip_countrycode);
        mTypeface = Typeface.createFromAsset(getAssets(), "System San Francisco Display Regular.ttf");
        getFontFamily();


        ArrayAdapter monthAdapter = new ArrayAdapter(this, R.layout.simple_spinner_item, countryCodeArray);
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCountryCode.setAdapter(monthAdapter);




        spinnerCountryCode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?>arg0, View view, int arg2, long arg3) {

                mcountrycode=spinnerCountryCode.getSelectedItem().toString();
                System.out.println("mcountrycode"+mcountrycode);


            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });

        vehicle.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    hideSoftKeyboard();
                    textView.clearFocus();
                    spincabname.requestFocus();
                    spincabname.performClick();
                }
                return true;
            }
        });

     if(CommonMethodUtil.isOnline(DriverRegistaionActivity.this)) {
         GetCablistAsync cabasyntask = new GetCablistAsync();
         cabasyntask.execute();
     }else {
         CommonMethodUtil.showAlert("Please connect internet", DriverRegistaionActivity.this);
     }
         latitudeval = String.valueOf(latitude);
         longitudeval = String.valueOf(longitude);

        String locale = DriverRegistaionActivity.this.getResources().getConfiguration().locale.getCountry();
        System.out.println("country = "+locale);


        cell.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                cell.setOnKeyListener(new View.OnKeyListener() {
                    @Override
                    public boolean onKey(View v, int keyCode, KeyEvent event) {

                        if (keyCode == KeyEvent.KEYCODE_DEL)
                            keyDel =1;
                        return false;
                    }
                });

                if (keyDel == 0) {
                    len = cell.getText().length();
                    if(len == 3||len==7) {
                        cell.setText(cell.getText() + "-");
                        cell.setSelection(cell.getText().length());
                    }
                } else {
                    keyDel = 0;
                }
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
                if (len > cell.getText().length() ){
                    len--;
                    return ;
                }
                len = cell.getText().length();

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                // TODO Auto-generated method stub
            }
        });

        btnnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    if (checkValidation()) {
                        if (CommonMethod.isOnline(DriverRegistaionActivity.this)) {
                            Log.d("##########Response", "" + "click on next");
                            System.out.println("calling asynctask");
                            RegistationAsync RegAsynTask = new RegistationAsync();
                            RegAsynTask.execute();
                        } else {
                            CommonMethodUtil.showAlert("Please connect internet", DriverRegistaionActivity.this);
                        }
                    }
            }
        });

        mPref = DriverRegistaionActivity.this.getSharedPreferences(MYPREF, Context.MODE_PRIVATE);
        ///keyboard next open calender
        licenid.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    hideSoftKeyboard();
                    textView.clearFocus();
                    experdate.requestFocus();
                    experdate.performClick();


                    final Calendar c = Calendar.getInstance();
                    mYear = c.get(Calendar.YEAR);
                    mMonth = c.get(Calendar.MONTH);
                    mDay = c.get(Calendar.DAY_OF_MONTH);
                    // Launch Date Picker Dialog
                    DatePickerDialog dpd = new DatePickerDialog(DriverRegistaionActivity.this,
                            new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int year,
                                                      int monthOfYear, int dayOfMonth) {
                                    try{
                                        experatin_date=String.format("%02d", dayOfMonth) + "-" + String.format("%02d", (monthOfYear + 1)) + "-" + year;

                                        SharedPreferences.Editor editor = mPref.edit();
                                        editor.putString("dateformat1", experatin_date);
                                        editor.apply();
                                        Log.e("monthe",""+monthOfYear);
                                        if(monthOfYear==0)
                                        {
                                            monthd="Jan";
                                        }else if(monthOfYear==1)
                                        {
                                            monthd="Feb";
                                        }else if(monthOfYear==2)
                                        {
                                            monthd="Mar";
                                        }else if(monthOfYear==3)
                                        {
                                            monthd="Apr";
                                        }else if(monthOfYear==4)
                                        {
                                            monthd="May";
                                        }else if(monthOfYear==5)
                                        {
                                            monthd="Jun";
                                        }else if(monthOfYear==6)
                                        {
                                            monthd="Jul";
                                        }else if(monthOfYear==7)
                                        {
                                            monthd="Aug";
                                        }else if(monthOfYear==8)
                                        {
                                            monthd="Sept";
                                        }else if(monthOfYear==9)
                                        {
                                            monthd="Oct";
                                        }else if(monthOfYear==10)
                                        {
                                            monthd="Nov";
                                        }else if(monthOfYear==11)
                                        {
                                            monthd="Dec";
                                        }
                                        getdate=monthd+"  "+String.valueOf(dayOfMonth)+","+String.valueOf(year);
                                        System.out.println("dateprntln>>>>>>>>>>"+getdate);

                                        experdate.setText(getdate);

                                    }catch (Exception e)
                                    {
                                        e.printStackTrace();
                                    }

                                }
                            }, mYear, mMonth, mDay);
                    dpd.getDatePicker();
                    dpd.show();
                    dpd.dismiss();

                }
                return true;
            }
        });

        //end

        experdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                // Launch Date Picker Dialog
                DatePickerDialog dpd = new DatePickerDialog(DriverRegistaionActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                try{
                                    experatin_date=String.format("%02d", dayOfMonth) + "-" + String.format("%02d", (monthOfYear + 1)) + "-" + year;

                                    SharedPreferences.Editor editor = mPref.edit();
                                    editor.putString("dateformat1", experatin_date);
                                    editor.apply();
                                    Log.e("monthe",""+monthOfYear);
                                    if(monthOfYear==0)
                                    {
                                        monthd="Jan";
                                    }else if(monthOfYear==1)
                                    {
                                        monthd="Feb";
                                    }else if(monthOfYear==2)
                                    {
                                        monthd="Mar";
                                    }else if(monthOfYear==3)
                                    {
                                        monthd="Apr";
                                    }else if(monthOfYear==4)
                                    {
                                        monthd="May";
                                    }else if(monthOfYear==5)
                                    {
                                        monthd="Jun";
                                    }else if(monthOfYear==6)
                                    {
                                        monthd="Jul";
                                    }else if(monthOfYear==7)
                                    {
                                        monthd="Aug";
                                    }else if(monthOfYear==8)
                                    {
                                        monthd="Sept";
                                    }else if(monthOfYear==9)
                                    {
                                        monthd="Oct";
                                    }else if(monthOfYear==10)
                                    {
                                        monthd="Nov";
                                    }else if(monthOfYear==11)
                                    {
                                        monthd="Dec";
                                    }
                                    getdate=monthd+"  "+String.valueOf(dayOfMonth)+","+String.valueOf(year);
                                    System.out.println("dateprntln>>>>>>>>>>"+getdate);

                                    experdate.setText(getdate);

                                }catch (Exception e)
                                {
                                    e.printStackTrace();
                                }

                            }
                        }, mYear, mMonth, mDay);
                dpd.getDatePicker();
                dpd.show();

            }
        });

        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             //amit   selectImage();

                showActionSheet();


            }
        });
        spincabname.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    cabId = cabtypeList.get(position-1).getCabId();

                    Log.e("cabId>>>>>>>>>>>>>",""+cabId);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        gps = new GPSTracker(DriverRegistaionActivity.this);
        getLatLong();
        //GCM Integration
        deviceId = Settings.Secure.getString(
                DriverRegistaionActivity.this.getContentResolver(),
                Settings.Secure.ANDROID_ID);

        System.out.println("deviceId"+deviceId);


        cd = new ConnectionDetector(getApplicationContext());
        // Make sure the device has the proper dependencies.
        GCMRegistrar.checkDevice(this);
        // Make sure the manifest was properly set - comment out this line
        // while developing the app, then uncomment it when it's ready.
        GCMRegistrar.checkManifest(this);

        registerReceiver(mHandleMessageReceiver, new IntentFilter(DISPLAY_MESSAGE_ACTION));

        // Get GCM registration id
        regId = GCMRegistrar.getRegistrationId(this);

        System.out.println("regId"+regId);
        if (regId.equals("")) {
            // Registration is not present, register now with GCM
            GCMRegistrar.register(this, SENDER_ID);

            regId = GCMRegistrar.getRegistrationId(this);

            System.out.println("regId2nd"+regId);
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
                // Try to register again, but not in the UI thread.
                // It's also necessary to cancel the thread onDestroy(),
                // hence the use of AsyncTask instead of a raw thread.
                final Context context = this;
                mRegisterTask = new AsyncTask<Void, Void, Void>() {

                    @Override
                    protected Void doInBackground(Void... params) {
                        // Register on our server
                        // On server creates a new user

                        // This data is sending to server
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


    public class GetCablistAsync extends AsyncTask<String, Void, String> {
        private Dialog mDialog;
        private String response;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            mDialog = ProgressDialog.show(DriverRegistaionActivity.this, "", "Loading...", true);
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
                    String success = object.getString("responseCode");
                    String message=object.getString("responseMsg");

                    Log.d("object", "" + object);
                    if (success.equals("200")) {

                        Log.d("success", "" + success);
                        try {
                            JSONArray jsonarry = object.getJSONArray("cablist");
                            cablist.clear();
                            cabtypeList.clear();
                            cablist.add("Select Taxi Type");
                            for (int i = 0; i < jsonarry.length(); i++) {
                                CabType details = new CabType();

                                JSONObject jsonObject2 = jsonarry.getJSONObject(i);
                                details.setCabId(jsonObject2.getString("id"));
                                details.setCabName(jsonObject2.getString("cab_name"));

                                cabtypeList.add(details);
                                cablist.add(jsonObject2.getString("cab_name"));
                            }
                            Log.d("cabtypeList", "" + cabtypeList.size());
                            Log.d("cablist", "" + cablist.size());
                            ArrayAdapter<String> leadProductAdapter = new ArrayAdapter<String>(
                                    DriverRegistaionActivity.this, R.layout.simple_spinner_item, cablist);
                            leadProductAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spincabname.setAdapter(leadProductAdapter);


                        } catch (JSONException e) {
                            e.printStackTrace();
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
            String url = ConstantUtil.Cab_list;
            HttpClient client = new HttpClient(url);
            Log.e("before connection", "" + url);
            try {
                client.connectForMultipart();

                Log.e("after connection", "" + url);


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



    public class RegistationAsync extends AsyncTask<String, Void, String> {
        private Dialog mDialog;
        private String response;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            System.out.println("in asynctask");


            super.onPreExecute();
            mDialog = ProgressDialog.show(DriverRegistaionActivity.this, "", "Loading...", true);
            mDialog.setCancelable(false);


        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            response = callService();
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
                    String success = object.getString("responseCode");
                    String responseMessage = object.getString("responseMsg");
                    Log.d("object", "" + object);
                    if (success.equals("200")) {

                        SharedPreferences.Editor editor = mPref.edit();
                        editor.putString("dateformat", getdate);
                        editor.apply();

                        new RegisterDialodActivity(DriverRegistaionActivity.this ,"hello",responseMessage).show();


                    } else if (success.equals("0")) {
                      Toast.makeText(DriverRegistaionActivity.this, responseMessage, Toast.LENGTH_SHORT).show();
                       // new RegisterDialodActivity(DriverRegistaionActivity.this ,"hello",responseMessage).show();

                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } else {

                Toast.makeText(DriverRegistaionActivity.this, "Internal server error", Toast.LENGTH_LONG).show();
            }
        }

        private String callService() {
            String url = ConstantUtil.Registation;
            HttpClient client = new HttpClient(url);


            Log.e("before connection", "" + url);
            try {
                client.connectForMultipart();
                Log.e("after connection", "" + url);
                first_name =fname.getText().toString().trim();
                last_name =lname.getText().toString().trim();
                password =pass.getText().toString().trim();
                dctc_id =dctc.getText().toString();
                cell_phone =cell.getText().toString().trim();
                emai_Id =email.getText().toString().trim();
                licence_id =licenid.getText().toString().trim();
                proof_insu =proofincu.getText().toString().trim();
                name_insu =nameinsu.getText().toString().trim();
                vehicle_name =vehicle.getText().toString().trim();
                tab_state =tabsate.getText().toString().trim();
                PVIn=et_pvin.getText().toString().trim();

                String result = cell.getText().toString().replaceAll("[-+.^:,]","");
                result.replaceAll("\\s+"," ");
                result.trim();
                String countrycode=mcountrycode.substring(1);
                client.addFormPart("role_id", "1");
                client.addFormPart("first_name",first_name);
                client.addFormPart("last_name", last_name);
                client.addFormPart("country_code", countrycode);
                System.out.println("countrycode"+"check"+countrycode);
                client.addFormPart("email", emai_Id);
                client.addFormPart("mobile_no", result);
                System.out.println("Mobile No"+"check"+result);
                client.addFormPart("password", password);
                client.addFormPart("cab_type", cabId);
                client.addFormPart("insurer_name", name_insu);
                client.addFormPart("insurence_proof", proof_insu);
                client.addFormPart("license_id", licence_id);



                client.addFormPart("expiration_date", experatin_date);
                client.addFormPart("dctc_id", dctc_id);
                client.addFormPart("vehicle", vehicle_name);
                client.addFormPart("tab_state", tab_state);
                client.addFormPart("lat",latitudeval);
                client.addFormPart("long",longitudeval);
                client.addFormPart("pvin",PVIn);
                client.addFilePart("image", imageName + ".jpg", mData);
                client.addFormPart("fcmid",regId);
                client.addFormPart("deviceType","android");
                client.addFormPart("deviceid",deviceId);

                Log.e("imageeeeeeeee", imageName + ".jpg" + ", " + mData);
                Log.e("first_name...", "" + first_name);
                Log.e("countrycode...", "" +countrycode);
                Log.e("last_name...", "" + last_name);
                Log.e("email...", "" + emai_Id);
                Log.e("mobile_no...", "" + result);
                Log.e("password...", "" + password);
                Log.e("cab_type...", "" + cabId);
                Log.e("insurer_name...", "" + name_insu);
                Log.e("insurence_proof...", "" + proof_insu);
                Log.e("license_id...", "" + licence_id);
                Log.e("expiration_date...", "" + experatin_date);
                Log.e("vehicle...", "" + vehicle_name);
                Log.e("tab_state...", "" + tab_state);
                Log.e("latitudeval...", "" + latitudeval);
                Log.e("longitudeval...",""+longitudeval);
                System.out.println("first name========>>>>"+first_name);


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
    //*//***for getting lattitude and longitude****//*///
    private void getLatLong() {

        if(gps.canGetLocation()){
            latitude = gps.getLatitude();
            longitude = gps.getLongitude();
            Log.d("latitude....=",""+latitude);
            Log.d("longitude....=",""+longitude);

        }else{
            gps.showSettingsAlert();
        }
    }

    private boolean checkValidation() {
        if (mData==null) {
            showAlert("Please set profile pic", DriverRegistaionActivity.this);
            return false;
        }  if (fname.getText().toString().trim().length()==0) {
            CommonMethodUtil.showAlert("Please enter first name", DriverRegistaionActivity.this);
            return false;
        } if (lname.getText().toString().trim().length() == 0) {
            CommonMethodUtil.showAlert("Please enter last name", DriverRegistaionActivity.this);
            return false;
        }if (email.getText().toString().trim().length() == 0) {
            CommonMethodUtil.showAlert("Please enter email", DriverRegistaionActivity.this);
            return false;
        } if (edit_confirmemail.getText().toString().trim().length() == 0) {
            CommonMethodUtil.showAlert("Please enter confirm email", DriverRegistaionActivity.this);
            return false;
        } if (!edit_confirmemail.getText().toString().trim().equals(email.getText().toString().trim())) {
            CommonMethodUtil.showAlert("Email and confirm email are not matched", DriverRegistaionActivity.this);
            return false;
        }if (!CommonMethodUtil.isEmailValid(email.getText().toString().trim())) {
            CommonMethodUtil.showAlert("Please enter valid email", DriverRegistaionActivity.this);
            return false;
        }  if (pass.getText().toString().trim().length() == 0) {
            CommonMethodUtil.showAlert("Please enter password", DriverRegistaionActivity.this);
            return false;
        }if (confirmpass.getText().toString().trim().length() == 0) {
            CommonMethodUtil.showAlert("Please enter confirm password", DriverRegistaionActivity.this);
            return false;
        } if (!confirmpass.getText().toString().trim().equals(pass.getText().toString().trim())) {
            CommonMethodUtil.showAlert("Password and confirm password are not matched", DriverRegistaionActivity.this);
            return false;
        } if (dctc.getText().toString().trim().length() == 0) {
            CommonMethodUtil.showAlert("Please enter dctc id", DriverRegistaionActivity.this);
            return false;
        } if (cell.getText().toString().trim().length() == 0||cell.getText().toString().trim().length()<10) {
            CommonMethodUtil.showAlert("Please enter phone number", DriverRegistaionActivity.this);
            return false;
        }  if (spinnerCountryCode.getSelectedItem().toString().trim().equals("+01")) {
            CommonMethodUtil.showAlert("Please select country code", DriverRegistaionActivity.this);
            return false;
        } if (licenid.getText().toString().trim().length() == 0) {
            CommonMethodUtil.showAlert("Please enter licence id", DriverRegistaionActivity.this);
            return false;
        }
        if (experdate.getText().toString().trim().length() == 0) {
            CommonMethodUtil.showAlert("Please enter expire date", DriverRegistaionActivity.this);
            return false;
        }
        if (nameinsu.getText().toString().trim().length() == 0) {
            CommonMethodUtil.showAlert("Please enter name of insurance", DriverRegistaionActivity.this);
            return false;
        }
        if(proofincu.getText().toString().trim().length() == 0) {
            CommonMethodUtil.showAlert("Please enter proof of insurance", DriverRegistaionActivity.this);
            return false;
        } if (vehicle.getText().toString().trim().length() == 0) {
            CommonMethodUtil.showAlert("Please enter taxi name", DriverRegistaionActivity.this);
            return false;
        }
        if (spincabname.getSelectedItem().toString().trim().equals("Select Taxi Type")) {
            CommonMethodUtil.showAlert("Please select taxi type", DriverRegistaionActivity.this);
            return false;
        }if (tabsate.getText().toString().trim().length() == 0) {
            CommonMethodUtil.showAlert("Please enter tabstate", DriverRegistaionActivity.this);
            return false;
        }if (et_pvin.getText().toString().trim().length() == 0) {
            CommonMethodUtil.showAlert("Please enter pvin", DriverRegistaionActivity.this);
            return false;
        }
        return true;


    }


    private void getFontFamily(){

            fname.setTypeface(mTypeface);
            lname.setTypeface(mTypeface);
            pass.setTypeface(mTypeface);
            confirmpass.setTypeface(mTypeface);
            dctc.setTypeface(mTypeface);
            experdate.setTypeface(mTypeface);
            cell.setTypeface(mTypeface);
            email.setTypeface(mTypeface);
            edit_confirmemail.setTypeface(mTypeface);
            licenid.setTypeface(mTypeface);
            proofincu.setTypeface(mTypeface);
            nameinsu.setTypeface(mTypeface);
            vehicle.setTypeface(mTypeface);
            tabsate.setTypeface(mTypeface);
            btnnext.setTypeface(mTypeface);

        }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
    public void showAlert(String message, Activity context) {

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
        builder.setMessage(message).setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                       //amit selectImage();
                        showActionSheet();


                    }
                });
        try {
            builder.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //Modify By Amit

    public void showActionSheet() {

        LayoutInflater inflater = LayoutInflater
                .from(DriverRegistaionActivity.this);
        final Dialog myDialog = new Dialog(DriverRegistaionActivity.this,
                android.R.style.Theme_Translucent_NoTitleBar);
        myDialog.setCanceledOnTouchOutside(true);
        myDialog.getWindow().setLayout(AbsoluteLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        myDialog.getWindow().setGravity(Gravity.BOTTOM);
        myDialog.getWindow().getAttributes().windowAnimations = R.anim.slide_up;
        WindowManager.LayoutParams lp = myDialog.getWindow().getAttributes();
        lp.dimAmount = 0.75f;
        myDialog.getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        myDialog.getWindow();

        View dialoglayout = inflater.inflate(
                R.layout.dialog_profile_actionsheet, null);
        myDialog.setContentView(dialoglayout);
        TextView mTvTakeFromCamera = (TextView) myDialog
                .findViewById(R.id.tvTakeFromCamera);
        TextView mTvTakeFromLibrary = (TextView) myDialog
                .findViewById(R.id.tvTakeFromLibrary);

        long timestamp = System.currentTimeMillis();
        AppData.getSingletonObject().setmFileTemp(DriverRegistaionActivity.this, "" + timestamp);
        mTvTakeFromCamera.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                myDialog.dismiss();
                takePicture(DriverRegistaionActivity.this);

            }

        });

        mTvTakeFromLibrary.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                myDialog.dismiss();
                openGallery(DriverRegistaionActivity.this);
            }

        });


        TextView tvCancel = (TextView) myDialog.findViewById(R.id.tvCancel);

        tvCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }

        });

        try {
            myDialog.show();
        } catch (WindowManager.BadTokenException e) {

            Log.e("", "View not attached.");
        } catch (Exception e) {

            e.printStackTrace();
        }
    }


    public static void openGallery(final Activity context) {

        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        context.startActivityForResult(photoPickerIntent,
                ConstantUtil.REQUEST_CODE_GALLERY);
    }

    public static void takePicture(final Activity context) {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        try {
            Uri mImageCaptureUri = null;
            String state = Environment.getExternalStorageState();
            if (Environment.MEDIA_MOUNTED.equals(state)) {
                mImageCaptureUri = Uri.fromFile(AppData.getSingletonObject()
                        .getmFileTemp());
            } else {

                mImageCaptureUri = InternalStorageContentProvider.CONTENT_URI;
            }
            intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
            intent.putExtra("return-data", true);
            context.startActivityForResult(intent, ConstantUtil.REQUEST_CODE_TAKE_PICTURE);
        } catch (ActivityNotFoundException e) {

            Log.d(ConstantUtil.BLANK_TEXT, "cannot take picture", e);
        }

    }

    private void startCropImage() {

        mRawBitmap = BitmapFactory.decodeFile(AppData.getSingletonObject().getmFileTemp().getPath());

        Intent intent = new Intent(DriverRegistaionActivity.this, CropImage.class);
        intent.putExtra(CropImage.IMAGE_PATH, AppData.getSingletonObject().getmFileTemp().getPath());
        intent.putExtra(CropImage.SCALE, true);
        intent.putExtra(CropImage.ASPECT_X, 1);
        intent.putExtra(CropImage.ASPECT_Y, 1);
        intent.putExtra("outputX", 250);
        intent.putExtra("outputY", 250);
        startActivityForResult(intent, ConstantUtil.REQUEST_CODE_CROP_IMAGE);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (resultCode != DriverRegistaionActivity.this.RESULT_OK) {

            return;
        }
        switch (requestCode) {

            case ConstantUtil.REQUEST_CODE_GALLERY:

                try {

                    InputStream inputStream = DriverRegistaionActivity.this.getContentResolver().openInputStream(
                            data.getData());
                    FileOutputStream fileOutputStream = new FileOutputStream(AppData.getSingletonObject().getmFileTemp());
                    boolean isCopied = ConstantUtil.copyStream(inputStream, fileOutputStream);
                    fileOutputStream.close();
                    inputStream.close();
                    startCropImage();

                } catch (Exception e) {

                    Log.e("", "Error while creating temp file", e);
                }

                break;
            case ConstantUtil.REQUEST_CODE_TAKE_PICTURE:

                startCropImage();
                break;
            case ConstantUtil.REQUEST_CODE_CROP_IMAGE:

                String path = data.getStringExtra(CropImage.IMAGE_PATH);
                if (path == null) {

                    return;
                }
                mRawBitmap = BitmapFactory.decodeFile(AppData.getSingletonObject().getmFileTemp().getPath());

                if (mRawBitmap != null) {
                    Log.e("imapgedk", "" + mRawBitmap);

                    profile_image.setImageBitmap(mRawBitmap);
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    mRawBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                    mData=  bytes.toByteArray();

                } else {
                    // mtv_upload_profile.setVisibility(View.VISIBLE);
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    public void hideSoftKeyboard() {
        if(getCurrentFocus()!=null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }


}
