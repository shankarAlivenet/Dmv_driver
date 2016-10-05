package com.alivenet.dmv.driverapplication.fragment;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
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

import com.alivenet.dmv.driverapplication.AppData;
import com.alivenet.dmv.driverapplication.CabType;
import com.alivenet.dmv.driverapplication.CommonMethod;
import com.alivenet.dmv.driverapplication.CommonMethodUtil;
import com.alivenet.dmv.driverapplication.Constant;
import com.alivenet.dmv.driverapplication.ConstantUtil;
import com.alivenet.dmv.driverapplication.InternalStorageContentProvider;
import com.alivenet.dmv.driverapplication.MainActivity;
import com.alivenet.dmv.driverapplication.MyApplication;
import com.alivenet.dmv.driverapplication.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;

import util.HttpClient;

/**
 * Created by navin on 7/14/2016.
 */
public class MyAccount extends Fragment {
    public View view;
    String cabId;
    int mYear,mMonth,mDay;
    ArrayList<CabType> cabtypeList = new ArrayList<CabType>();
    ArrayList<String> cablist = new ArrayList<String>();
    Bitmap bitmap;
    ProgressDialog pDialog;
    EditText fname,lname,pass,dctc,licenid,proofincu,nameinsu,vehicle,tabsate,et_pvin;
    String first_name,last_name,emails,country_code,mobile_no ,password ,newsletter,license_id,image,cab_type,vehicles,cab_icon,tab_state,
            insurer_name,dctc_id,insurence_proof;
    String expiration_date="";
    TextView experdate,email,cell;
    ImageView profile_image;
    Spinner spincabname;
    public Typeface mTypeface;
    Button btndriver;
    protected static final int CAMERA_REQUEST = 0;
    int REQUEST_CAMERA = 0;
    int PICK_IMAGE = 1;
    private Bitmap bm;
    double imageName;
    boolean setprofile=false;
    String getdate,monthd;
    SharedPreferences mPref;
    public static final String MYPREF = "user_info";
    Activity mActivity;
    byte[] mData;
    private Bitmap mRawBitmap;

    @Override
    public void onAttach(Activity activity) {
        // TODO Auto-generated method stub
        super.onAttach(activity);
        mActivity=activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.myaccount, container, false);
        getActivity().setTitle("      MY ACCOUNT");
        spincabname = (Spinner) view.findViewById(R.id.cabspinner);
        profile_image=(ImageView)view.findViewById(R.id.img_prifile_show);
        fname=(EditText)view.findViewById(R.id.edit_firname);
        lname=(EditText)view.findViewById(R.id.edit_lname);
        pass=(EditText)view.findViewById(R.id.edit_pass);
        dctc=(EditText)view.findViewById(R.id.edit_dctc);
        experdate=(TextView)view.findViewById(R.id.edit_exprdate);
        cell=(TextView)view.findViewById(R.id.edit_cell);
        email=(TextView)view.findViewById(R.id.edit_email);
        licenid=(EditText)view.findViewById(R.id.edit_licenid);
        proofincu=(EditText)view.findViewById(R.id.edit_proofinsu);
        nameinsu=(EditText)view.findViewById(R.id.edit_nameinsu);
        vehicle=(EditText)view.findViewById(R.id.edit_vehicle);
        tabsate=(EditText)view.findViewById(R.id.edit_tabstate);
        et_pvin=(EditText)view.findViewById(R.id.edit_pvin);
        btndriver=(Button)view.findViewById(R.id.btndriver);

        mPref = getActivity().getSharedPreferences(MYPREF, Context.MODE_PRIVATE);


        mTypeface = Typeface.createFromAsset(getActivity().getAssets(), "System San Francisco Display Regular.ttf");
        getFontFamily();

        if (CommonMethod.isOnline(getActivity())) {

            Myaccountdata myacc = new Myaccountdata();
            myacc.execute();

            GetCablistAsync  cablist =new GetCablistAsync();
            cablist.execute();

        } else {
            CommonMethodUtil.showAlert("Please connect internet", getActivity());
        }




        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showActionSheet();

            }
        });

        experdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                // Launch Date Picker Dialog
                DatePickerDialog dpd = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // Display Selected date in textbox
                                MyApplication.dataupdateflag=true;
                                try{
                                    expiration_date=String.format("%02d", dayOfMonth) + "-" + String.format("%02d", (monthOfYear + 1)) + "-" + year;

                                    SharedPreferences.Editor editor = mPref.edit();
                                    editor.putString("dateformat1", expiration_date);
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

                                    // Display Selected date in textbox
                                    experdate.setText(getdate);

                                }catch (Exception e)
                                {
                                    e.printStackTrace();
                                }

                                //experdate.setText(String.format("%02d", dayOfMonth) + "-" + String.format("%02d", (monthOfYear + 1)) + "-" + year);
                            }
                        }, mYear, mMonth, mDay);
                dpd.getDatePicker();
                dpd.show();

            }
        });


        spincabname.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                if (position > 0) {
                    if(cabtypeList!=null)
                    {
                        cabId = cabtypeList.get(position-1).getCabId();
                    }

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        btndriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (CommonMethod.isOnline(getActivity())) {
                    if(checkValidationUpdateProfile())
                    {
                        UPdateProfile update = new UPdateProfile();
                        update.execute();
                      /*  if(CommonMethod.getprofileupdate(getActivity()))
                        {
                            CommonMethodUtil.showAlert("You cant update profile after accepting ride .", getActivity());
                        }else {

                        }*/
                    }



                } else {
                    CommonMethodUtil.showAlert("Please connect internet", getActivity());
                }

            }
        });

        return view;
    }

    @Override
    public void onResume() {

        if(MainActivity.mRawBitmap!=null)
        {
            MyApplication.imageupdateflag=true;
            mRawBitmap=MainActivity.mRawBitmap;
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            mRawBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            mData=  bytes.toByteArray();
            profile_image.setImageBitmap(mRawBitmap);
        }
        super.onResume();
    }

    private void getFontFamily(){

        fname.setTypeface(mTypeface);
        lname.setTypeface(mTypeface);
        pass.setTypeface(mTypeface);
        dctc.setTypeface(mTypeface);
        experdate.setTypeface(mTypeface);
        cell.setTypeface(mTypeface);
        email.setTypeface(mTypeface);
        licenid.setTypeface(mTypeface);
        proofincu.setTypeface(mTypeface);
        nameinsu.setTypeface(mTypeface);
        vehicle.setTypeface(mTypeface);
        tabsate.setTypeface(mTypeface);
    }

    public class Myaccountdata extends AsyncTask<String, Void, String> {

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
            response = getmyaccoutdetails();
            return response;
        }

        @Override
        protected void onPostExecute(String response) {
            // TODO Auto-generated method stub
            super.onPostExecute(response);
            if (mDialog != null) {
                mDialog.dismiss();
            }
            Log.d("MyAccount_details==>>", "" + response);

            //responseCode
            JSONObject object,driverdetails;

            if (response != null) {

                try {

                    object = new JSONObject(response);
                    String success = object.getString("responseCode");
                    String responseMsg = object.getString("responseMsg");
                    driverdetails = object.getJSONObject("driverDetail");

                    Log.d("object", "" + object);
                    if (success.equals("200")) {

                         first_name = driverdetails.getString("first_name");
                         last_name = driverdetails.getString("last_name");
                         emails = driverdetails.getString("email");
                         country_code = driverdetails.getString("country_code");
                         mobile_no = driverdetails.getString("mobile_no");
                         password = driverdetails.getString("password");
                         newsletter = driverdetails.getString("newsletter");
                         license_id = driverdetails.getString("license_id");
                         image = driverdetails.getString("image");
                         CommonMethod.setImage(getActivity(),"IMAGE",image);


                         cab_type = driverdetails.getString("cab_name");
                         cabId = driverdetails.getString("cab_type");

                         vehicles = driverdetails.getString("vehicle");
                         cab_icon = driverdetails.getString("cab_icon");
                         tab_state = driverdetails.getString("tab_state");
                         insurer_name = driverdetails.getString("insurer_name");
                         expiration_date =driverdetails.getString("expiration_date") ;
                         dctc_id = driverdetails.getString("dctc_id");
                         insurence_proof = driverdetails.getString("insurence_proof");

                        StringBuilder number = new StringBuilder(mobile_no);
                        System.out.println("string = " + number);

                        if(number.length()>3)
                        number.insert(3, '-');
                        if(number.length()>7)
                        number.insert(7, '-');

                        fname.setText(first_name);
                        lname.setText(last_name);
                        pass.setText(password);
                        cell.setText(number);
                        email.setText(emails);
                        vehicle.setText(vehicles);
                        licenid.setText(license_id);
                        dctc.setText(dctc_id);
                        //experdate.setText(expiration_date);
                        if(MyApplication.dataupdateflag=true){
                            experdate.setText(CommonMethodUtil.DateFormatApp(expiration_date));
                        }else {
                            experdate.setText(mPref.getString("dateformat1",""));
                        }
                        //experdate.setText(mPref.getString("dateformat1",""));
                        proofincu.setText(insurence_proof);
                        nameinsu.setText(insurer_name);
                        tabsate.setText(tab_state);

                       String baseurl= ConstantUtil.baseurl+"uploads/driver/";

                        String image1= baseurl+""+image;
                        Picasso.with(getActivity())
                                .load(image1)
                                .error(R.mipmap.profile_icon)
                                .placeholder(R.mipmap.profile_icon)
                                .into(profile_image);

                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } else {

                Toast.makeText(mActivity, "Please try again", Toast.LENGTH_LONG).show();
            }
        }

        private String getmyaccoutdetails() {
            String url = ConstantUtil.myaccount_details;
            HttpClient client = new HttpClient(url);
           System.out.println("check driver id "+CommonMethod.getdriverid(getActivity()));
            try {
                client.connectForMultipart();
                Log.d("getdriverid==", CommonMethod.getdriverid(getActivity()));
                client.addFormPart("driverId",CommonMethod.getdriverid(getActivity()));
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


//update Profile


    public class UPdateProfile extends AsyncTask<String, Void, String> {

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
            response = updatedetails();
            return response;
        }

        @Override
        protected void onPostExecute(String response) {
            // TODO Auto-generated method stub
            super.onPostExecute(response);
            if (mDialog != null) {
                mDialog.dismiss();
            }
           System.out.println("first responce========>>>>"+response);

            //responseCode
            JSONObject object,driverdetails;

            if (response!=null) {
                try {

                    object = new JSONObject(response);
                    String success = object.getString("responseCode");

                    Log.d("object", "" + object);
                    if (success.equals("200")) {

                        String cabType= object.getString("cabType");
                        CommonMethod.setcabType(getActivity(), Constant.CABTYPE,cabType);
                        System.out.println("checkcabType:"+cabType);

                        if(MyApplication.dataupdateflag==true)
                        {
                            SharedPreferences.Editor editor = mPref.edit();
                            editor.putString("dateformat1", getdate);
                            editor.apply();
                        }


                        Toast.makeText(mActivity,"Update account Successfully", Toast.LENGTH_LONG).show();

                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(mActivity, "Please try again", Toast.LENGTH_LONG).show();
            }
        }

        private String updatedetails() {
            String url = ConstantUtil.Profile_Update;
            HttpClient client = new HttpClient(url);

            try {
                client.connectForMultipart();

                first_name=fname.getText().toString().trim();
                last_name=lname.getText().toString().trim();
                emails=email.getText().toString().trim();
                mobile_no=cell.getText().toString().trim();
                password=pass.getText().toString().trim();
                license_id=licenid.getText().toString().trim();
                vehicles=vehicle.getText().toString().trim();
                tab_state=tabsate.getText().toString().trim();
                insurer_name=nameinsu.getText().toString().trim();
                if(MyApplication.dataupdateflag==false)
                {
                    expiration_date=mPref.getString("dateformat1", "");
                }
                //expiration_date=experdate.getText().toString().trim();
                dctc_id=dctc.getText().toString().trim();
                insurence_proof=proofincu.getText().toString().trim();


                Log.d("getdriverid==", CommonMethod.getdriverid(getActivity()));



                Log.e("firstnameupdate",""+first_name);

                Log.e("imageeeeeeeee", imageName + ".jpg" + ", " + mData);
                Log.e("first_name...", "" + first_name);
                Log.e("last_name...", "" + last_name);
                Log.e("cab_type...", "" + cabId);
                Log.e("insurer_name...", "" + insurer_name);
                Log.e("insurence_proof...", "" + insurence_proof);
                Log.e("license_id...", "" + license_id);
                Log.e("expiration_date...", "" + expiration_date);
                Log.e("vehicle...", "" + vehicles);
                Log.e("tab_state...", "" + tab_state);
                System.out.println("first name========>>>>"+first_name);

                client.addFormPart("id",CommonMethod.getdriverid(getActivity()));
                client.addFormPart("first_name",first_name);
                client.addFormPart("last_name",last_name);
                client.addFormPart("country_code","+91");
                client.addFormPart("newsletter","Y");
                client.addFormPart("dctc_id",dctc_id);
                client.addFormPart("expiration_date",expiration_date);
                client.addFormPart("license_id",license_id);
                client.addFormPart("insurence_proof",insurence_proof);
                client.addFormPart("insurer_name",insurer_name);
                client.addFormPart("vehicle",vehicles);
                client.addFormPart("tab_state",tab_state);
                client.addFormPart("cab_type", cabId);


                if(MyApplication.imageupdateflag==false) {


                }else {
                    client.addFilePart("image", imageName + ".jpg", mData);
                    MyApplication.imageupdateflag=false;


                }

                client.finishMultipart();
                response = client.getResponse().toString();




            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return response;
        }
    }


//cablist Asyntask

    public class GetCablistAsync extends AsyncTask<String, Void, String> {
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
            response = cablistService();
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

                    Log.d("object", "" + object);
                    if (success.equals("200")) {

                        Log.d("success", "" + success);
                        try {
                            JSONArray jsonarry = object.getJSONArray("cablist");
                            cablist.clear();
                            cabtypeList.clear();
                            cablist.add(cab_type);
                            if(jsonarry!=null) {
                                for (int i = 0; i < jsonarry.length(); i++) {

                                    JSONObject jsonObject2 = jsonarry.getJSONObject(i);
                                    if (cab_type!=null&&!cab_type.equals(jsonObject2.getString("cab_name"))) {
                                        CabType details = new CabType();
                                        details.setCabId(jsonObject2.getString("id"));
                                        details.setCabName(jsonObject2.getString("cab_name"));

                                        cabtypeList.add(details);
                                        cablist.add(jsonObject2.getString("cab_name"));
                                    }
                                }
                                Log.d("cabtypeList", "" + cabtypeList.size());
                                Log.d("cablist", "" + cablist.size());
                                if(cablist!=null) {
                                    ArrayAdapter<String> leadProductAdapter = new ArrayAdapter<String>(
                                            mActivity, R.layout.simple_spinner_item, cablist);
                                    leadProductAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    spincabname.setAdapter(leadProductAdapter);
                                }

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } else {

                Toast.makeText(mActivity, "Please try again", Toast.LENGTH_LONG).show();
            }
        }

        private String cablistService() {
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


    private boolean checkValidationUpdateProfile() {
        insurence_proof=proofincu.getText().toString().trim();

        if (fname.getText().toString().trim().length() == 0) {
            CommonMethodUtil.showAlert("Please enter your first name.", getActivity());
            return false;
        }else if (lname.getText().toString().trim().length() == 0) {
            CommonMethodUtil.showAlert("Please enter your last name.",getActivity());

            return false;
        }else if (email.getText().toString().trim().length() == 0) {
            CommonMethodUtil.showAlert("Please enter your email.",getActivity());

            return false;
        }else if (cell.getText().toString().trim().length() == 0) {
            CommonMethodUtil.showAlert("Please enter your mobile no.",getActivity());

            return false;
        }else if (pass.getText().toString().trim().length() == 0) {
            CommonMethodUtil.showAlert("Please enter your password.",getActivity());

            return false;
        }else if (licenid.getText().toString().trim().length() == 0) {

            CommonMethodUtil.showAlert("Please enter your license id.",getActivity());
            return false;
        }else if (proofincu.getText().toString().trim().length() == 0) {

            CommonMethodUtil.showAlert("Please enter your proof of Insurance.",getActivity());
            return false;
        }else if (nameinsu.getText().toString().trim().length() == 0) {

            CommonMethodUtil.showAlert("Please enter your proof of Insurance.",getActivity());
            return false;
        }else if (vehicle.getText().toString().trim().length() == 0) {

            CommonMethodUtil.showAlert("Please enter your taxi Name.",getActivity());
            return false;
        }else if (dctc.getText().toString().trim().length() == 0) {

            CommonMethodUtil.showAlert("Please enter your Dctc id.",getActivity());
            return false;
        }
        return true;
    }

    //Modify By Amit

    public void showActionSheet() {

        LayoutInflater inflater = LayoutInflater
                .from(getActivity());
        final Dialog myDialog = new Dialog(getActivity(),
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
        AppData.getSingletonObject().setmFileTemp(getActivity(), "" + timestamp);
        mTvTakeFromCamera.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                myDialog.dismiss();
                takePicture(getActivity());

            }

        });

        mTvTakeFromLibrary.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                myDialog.dismiss();
                openGallery(getActivity());
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
            intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
            intent.putExtra("return-data", true);
            context.startActivityForResult(intent, ConstantUtil.REQUEST_CODE_TAKE_PICTURE);
        } catch (ActivityNotFoundException e) {

            Log.d(ConstantUtil.BLANK_TEXT, "cannot take picture", e);
        }

    }



}