package com.alivenet.dmv.driverapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import util.HttpClient;

/**
 * Created by sunil on 8/7/2016.
 */
public class ForgotPassword extends AppCompatActivity {

    EditText etEmaiId;
    TextView btnSubmit;
    ProgressDialog prgDialog;
    Toolbar toolbar;
    String email;
    ForgotPassword_validate forgotPassword_validate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        toolbar = (Toolbar) findViewById(R.id.maintoolbar);
        toolbar.setNavigationIcon(getResources().getDrawable(R.mipmap.back_icon));
       // setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        prgDialog = new ProgressDialog(this);
        prgDialog.setMessage("Please wait...");
        prgDialog.setCancelable(false);

        etEmaiId = (EditText) findViewById(R.id.etforgot_emailid);
        btnSubmit = (TextView) findViewById(R.id.txt_submit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CommonMethodUtil.isOnline(ForgotPassword.this)) {
                    email = etEmaiId.getText().toString().trim();
                    if (checkValidationJoinUs()) {
                        forgotPassword_validate=new ForgotPassword_validate();
                        forgotPassword_validate.execute();
                    }
                }
            }
        });
    }


    // Asyntask for forgot password
    public class ForgotPassword_validate extends AsyncTask<String, Void, String> {
        private Dialog mDialog;
        private String response;
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            mDialog = ProgressDialog.show(ForgotPassword.this, "", "Loading...", true);
            mDialog.setCancelable(false);
        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            response = forgotPassword();
            return response;
        }

        @Override
        protected void onPostExecute(String response) {
            // TODO Auto-generated method stub
            super.onPostExecute(response);
            if (mDialog != null) {
                mDialog.dismiss();
            }
            try {
                JSONObject object=new JSONObject(response);
                Log.d("Json_con", object.toString(2));
                String responseCode = object.getString("responseCode");
                String responseMessage = object.getString("responseMsg");
                if (responseCode.equals("200")) {

                    String userId = object.getString("userId");

                    MyAlert(responseMessage, userId, ForgotPassword.this);

                } else {
                    CommonMethodUtil.showAlert(responseMessage, ForgotPassword.this);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        private String forgotPassword() {
            String url = ConstantUtil.forgot_password;
            HttpClient client = new HttpClient(url);
            Log.e("before connection", "" + url);
            try {
                client.connectForMultipart();
                client.addFormPart("email_id", email);
                client.finishMultipart();
                response = client.getResponse().toString();
                Log.d("forgotPassword_Respo", response);

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return response;
        }


    }
    private boolean checkValidationJoinUs() {

        if (etEmaiId.getText().toString().trim().length() == 0) {
            CommonMethodUtil.showAlert("Please enter your email", ForgotPassword.this);
            return false;
        } else if (!CommonMethodUtil.isEmailValid(etEmaiId.getText().toString().trim())) {
            CommonMethodUtil.showAlert("Please enter valid email", ForgotPassword.this);
            return false;
        }
        return true;

    }

    public void MyAlert(String message, final String userId, Activity context) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message).setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        Intent in = new Intent(getApplication(), ResetPasswordActivity.class);
                        in.putExtra("userId", userId);
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
