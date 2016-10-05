package com.alivenet.dmv.driverapplication;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by narendra on 6/15/2016.
 */
public class LoginDialogActivity extends Dialog {

    Activity activity;
    String noofpeople;
    Button  btnlogin;
    TextView pass,notmatch,tryagain;
    private Typeface mTypeface;
    private static final String SPF_NAME = "0"; //  <--- Add this
    private static final String USERNAME = "username";  //  <--- To save username
    private static final String PASSWORD = "password";  //  <--- To save password
    private static final String CHECK = "check";
    String messag;
    public LoginDialogActivity(Activity a, String msg,String message) {
        super(a, R.style.custom_dialog_theme);
        // TODO Auto-generated constructor stub
        activity = a;
        noofpeople = msg;
        messag=message;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // hide notification bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.login_dialog);

        pass=(TextView)findViewById(R.id.pass);
        btnlogin = (Button) findViewById(R.id.btnok);
        pass.setText(messag);
        btnlogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                // dismiss dialog box

                SharedPreferences loginPreferences = activity.getSharedPreferences(SPF_NAME, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = loginPreferences.edit();
                editor.putString("USERNAME","");
                editor.putString("PASSWORD","");
                editor.putBoolean("CHECK", false);
                editor.apply();

                dismiss();
            }
        });

    }

}