package com.alivenet.dmv.driverapplication;
import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.multidex.MultiDex;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.Fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;

import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsoluteLayout;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alivenet.dmv.driverapplication.fragment.DriverAcceptenceFragment;
import com.alivenet.dmv.driverapplication.fragment.FragmentReservation;
import com.alivenet.dmv.driverapplication.fragment.FragmentTermPrivacy;
import com.alivenet.dmv.driverapplication.fragment.MyAccount;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

import eu.janmuller.android.simplecropimage.CropImage;
import util.*;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    Fragment newFragment;
    public static String bookingId, message, driverId, userId, pickupLat, pickupLong, distance, destinationLong, destinationLat, username, paymentMethod;
    Toolbar toolbar;
    private BroadcastReceiver registerReceiver;
    private BroadcastReceiver declineregister;
    private BroadcastReceiver broadcastReceiver;
    public static Bitmap mRawBitmap;
    byte[] mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout frame = new FrameLayout(this);
        frame.setId(R.id.framain);

        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // logout();
        userdecline();

        if (MyApplication.resvflag) {
            MyApplication.resvflag = false;
            new Reservation_Dialog(MainActivity.this, "hello").show();
        } else {



            if(CommonMethod.getTripcomplete(MainActivity.this))
            {
                CommonMethod.showAlert("Ride is running...\nYou can't Switch",MainActivity.this);

            }else {
                Intent i = new Intent(MainActivity.this, AndroidLocationServices.class);
                i.putExtra("id", CommonMethod.getdriverid(MainActivity.this));
                MainActivity.this.startService(i);
                System.out.println("calling time ");
            }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        navigationView.setItemBackground(getResources().getDrawable(R.mipmap.button_bg));
        navigationView.setItemIconTintList(null);
        navigationView.setBackground(getResources().getDrawable(R.mipmap.backgroundtwo));


        Menu menu1 = navigationView.getMenu();
        for (int menuItemIndex = 0; menuItemIndex < menu1.size(); menuItemIndex++) {
            MenuItem menuItem = menu1.getItem(menuItemIndex);
            if (menuItem.getItemId() == R.id.nav_logout) {
                menuItem.setVisible(true);
                SpannableString s = new SpannableString("LOG OUT");
                s.setSpan(new ForegroundColorSpan(Color.RED), 0, s.length(), 0);
                menuItem.setTitle(s);
            }

        }

            if (MyApplication.declineuser){

                MyApplication.declineuser=false;
                CommonMethod.showAlert("User cancel this ride\n"+" BookingId: "+MyApplication.userdeclinebookingId,MainActivity.this);
                toolbar.setTitle("DMV DRIVER");
                newFragment = new DriverAcceptenceFragment();
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.framain, newFragment).commit();
            }


        newFragment = new DriverAcceptenceFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.framain, newFragment).commit();


        TextView driverterm = (TextView) findViewById(R.id.footer_item_1);
        TextView privacyterm = (TextView) findViewById(R.id.footer_item_2);

        SpannableString content = new SpannableString(driverterm.getText().toString().trim());
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        driverterm.setText(content);

        SpannableString contents = new SpannableString(privacyterm.getText().toString().trim());
        contents.setSpan(new UnderlineSpan(), 0, contents.length(), 0);
        privacyterm.setText(contents);


        driverterm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(CommonMethod.getTripcomplete(MainActivity.this))
                {
                    CommonMethod.showAlert("Ride is running...\nYou can't Switch",MainActivity.this);

                }else {

                    MyApplication.termprivacy = true;
                    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                    if (drawer.isDrawerOpen(GravityCompat.START)) {
                        drawer.closeDrawer(GravityCompat.START);
                    } else {
                    }
                    toolbar.setTitle("DRIVER TERMS");

                    newFragment = new FragmentTermPrivacy();
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.framain, newFragment).commit();
                }
            }
        });


        privacyterm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(CommonMethod.getTripcomplete(MainActivity.this))
                {
                    CommonMethod.showAlert("Ride is running...\nYou can't Switch",MainActivity.this);

                }else {
                    MyApplication.termprivacy = false;
                    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                    if (drawer.isDrawerOpen(GravityCompat.START)) {
                        drawer.closeDrawer(GravityCompat.START);
                    } else {
                    }
                    toolbar.setTitle("PRIVACY TERMS");
                    newFragment = new FragmentTermPrivacy();
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.framain, newFragment).commit();

                }
            }
        });

    }

}

    @Override
    protected void onResume() {
        super.onResume();
        getusernotifications();
    }

    @Override
    protected void onStop() {
        super.onStop();

        try {
            if (registerReceiver != null) {
                unregisterReceiver(registerReceiver);
            }
            if (declineregister != null) {
                unregisterReceiver(declineregister);
            }
        } catch (IllegalArgumentException e) {
            Log.i("Brodcastrecever alredy","epicReciver is already unregistered");
            registerReceiver = null;
            declineregister=null;
        }




    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

   /* @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        if (id == R.id.nav_accepting) {

            if(CommonMethod.getTripcomplete(MainActivity.this)) {
                CommonMethod.showAlert("Ride is running...\nYou can't Switch",MainActivity.this);

            }else {
                newFragment = new DriverAcceptenceFragment();
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.framain, newFragment).commit();
                toolbar.setTitle("DMV DRIVER");
            }







         /*   Intent intent = new Intent(MainActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            MainActivity.this.finish();*/


        } else if (id == R.id.nav_myaccount) {

            if(CommonMethod.getTripcomplete(MainActivity.this)) {
                CommonMethod.showAlert("Ride is running...\nYou can't Switch",MainActivity.this);

            }else {
                newFragment = new MyAccount();
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.framain, newFragment).commit();
            }

        } else if(id==R.id.nav_reservationlist){
            if(CommonMethod.getTripcomplete(MainActivity.this)) {
                CommonMethod.showAlert("Ride is running...\nYou can't Switch",MainActivity.this);

            }else {
                toolbar.setTitle("RESERVATION");
                newFragment = new FragmentReservation();
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.framain, newFragment).commit();
            }

        }
        else if (id == R.id.nav_logout) {
/*

          if(CommonMethod.getTripcomplete(MainActivity.this))
            {
                CommonMethod.showAlert("Ride is running...\nYou can't logout",MainActivity.this);

            }else {
               new LogOutDialog(MainActivity.this, "hello").show();
              CommonMethod.setTripcomplete(MainActivity.this,Constant.TRIPCOMPLETE,false);
           }
*/

            new LogOutDialog(MainActivity.this, "hello").show();
            CommonMethod.setTripcomplete(MainActivity.this,Constant.TRIPCOMPLETE,false);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;

    }
//13 aug
   /* public void getusernotifications() {

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("upadteride");
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //TODO  map updations work here

                Log.d("onReceive","Logout in progress");
                //At this point you should start the login activity and finish this one
                Intent in = new Intent(MainActivity.this,MainActivity.class);
                in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(in);
                finish();

                unregisterReceiver(broadcastReceiver);}
        };
        registerReceiver(broadcastReceiver, intentFilter);


    }*/


    public void getusernotifications(){
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("upadteride");
        registerReceiver=new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d("onReceive","Logout in progress");
                //At this point you should start the login activity and finish this one
                if (MyApplication.resvflag) {
                    MyApplication.resvflag = false;
                    new Reservation_Dialog(MainActivity.this, "hello").show();
                } else {
                    toolbar.setTitle("DMV DRIVER");
                    newFragment = new DriverAcceptenceFragment();
                    try {
                        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.framain, newFragment).commit();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        };
        registerReceiver(registerReceiver, intentFilter);
    }

    public void userdecline(){
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("decineuser");
        declineregister=new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                MyApplication.declineuser=false;

                   CommonMethod.showAlert("User cancel this ride\n"+" BookingId: "+MyApplication.userdeclinebookingId,MainActivity.this);
                    toolbar.setTitle("DMV DRIVER");
                    newFragment = new DriverAcceptenceFragment();
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.framain, newFragment).commit();


            }
        };
        registerReceiver(declineregister, intentFilter);
    }



    public void logout(){
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("ACTION_LOGOUT");
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d("onReceive","Logout in progress");
                //At this point you should start the login activity and finish this one
                Intent in = new Intent(MainActivity.this,LoginActivity.class);
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

    private void startCropImage() {

        mRawBitmap = BitmapFactory.decodeFile(AppData.getSingletonObject().getmFileTemp().getPath());

        Intent intent = new Intent(MainActivity.this, CropImage.class);
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


        if (resultCode != MainActivity.this.RESULT_OK) {

            return;
        }

        // Bitmap rawBitmap;
        switch (requestCode) {

            case ConstantUtil.REQUEST_CODE_GALLERY:

                try {

                    InputStream inputStream = MainActivity.this.getContentResolver().openInputStream(
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

                    //profile_image.setImageBitmap(mRawBitmap);
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

}
