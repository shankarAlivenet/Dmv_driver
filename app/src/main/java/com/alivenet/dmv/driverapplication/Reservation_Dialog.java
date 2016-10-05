package com.alivenet.dmv.driverapplication;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import util.HttpClient;

/**
 * Created by sunil on 8/19/2016.
 */
public class Reservation_Dialog extends Dialog {

    SharedPreferences mPref;
    public static final String MYPREF = "user_info";
    Activity activity;
    String noofpeople,status,responseMessage;
    Button accept , cancel;
    TextView message;

    public Reservation_Dialog(Activity a, String msg) {
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
        setContentView(R.layout.reservationconfirm_dialog);
        mPref = getContext().getSharedPreferences(MYPREF, Context.MODE_PRIVATE);
        message=(TextView)findViewById(R.id.text_message) ;
        accept = (Button) findViewById(R.id.btnaccept);
        cancel = (Button) findViewById(R.id.btncancel);
        // people_size.setText(noofpeople);
        cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                status="reject";
                acceptAsync accept =new acceptAsync();
                accept.execute();
            }
        });

        accept.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                status="accept";
                acceptAsync accept =new acceptAsync();
                accept.execute();

            }
        });

    }


    /// accept.... AsynTask
    public class acceptAsync extends AsyncTask<String, Void, String> {
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
            response = AcceptRejectResv();
            return response;
        }

        @Override
        protected void onPostExecute(String response) {
            // TODO Auto-generated method stub
            super.onPostExecute(response);



            if (mDialog != null) {
                mDialog.dismiss();
            }
            Log.d("AcceptReject Response", "" + response);

            //responseCode
            JSONObject object;

            if (response != null) {
                try {

                    object = new JSONObject(response);
                    String responseCode = object.getString("responseCode");
                    responseMessage = object.getString("responseMsg");
                    Log.d("object", "" + object);
                    if (responseCode.equals("200")) {

                        CommonMethod.showAlert(responseMessage,activity);
                        dismiss();
                    }
                    else {
                        CommonMethod.showAlert(responseMessage,activity);
                        dismiss();
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } else {

            }
        }

        private String AcceptRejectResv() {
            String url = ConstantUtil.ResvrationAcceptReject;

            HttpClient client = new HttpClient(url);
            try {
                client.connectForMultipart();

                Log.e("after connection", "" + url);

                client.addFormPart("reservationId",MyApplication.RreservationId);
                client.addFormPart("driverId",MyApplication.RdriverId);
                client.addFormPart("status",status);

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