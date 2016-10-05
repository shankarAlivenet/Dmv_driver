package com.alivenet.dmv.driverapplication;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by sunil on 6/23/2016.
 */
public class RegisterDialodActivity  extends Dialog {

    Activity activity;
    String noofpeople;
    Button btncancel , btnlogin;
    String message;
    TextView txtmsg;

    public RegisterDialodActivity(Activity a, String msg,String responseMessage) {
        super(a, R.style.custom_dialog_theme);
        // TODO Auto-generated constructor stub
        activity = a;
        noofpeople = msg;
        message=responseMessage;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // hide notification bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.register_dialog);
        txtmsg=(TextView)findViewById(R.id.text_carsname);
        btnlogin = (Button) findViewById(R.id.btnok);
        txtmsg.setText(message);
        btnlogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                // dismiss dialog box
               /* Intent in = new Intent(getContext() ,LoginActivity.class);
                in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                getContext().startActivity(in);
                dismiss();*/
                activity.finish();

            }
        });

    }

}