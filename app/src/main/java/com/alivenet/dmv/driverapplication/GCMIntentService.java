package com.alivenet.dmv.driverapplication;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gcm.GCMBaseIntentService;

import java.util.List;

import static com.alivenet.dmv.driverapplication.CommonUtilities.SENDER_ID;
import static com.alivenet.dmv.driverapplication.CommonUtilities.displayMessage;

public class GCMIntentService extends GCMBaseIntentService {

	private static final String TAG = "GCMIntentService";


    private SharedPreferences mPref;
    public static final String MYPREF = "user_info";
    public static String bookingId,message,driverId,userId,pickupLat,reservation,reservationId,cabType,pickupaddress,destinationaddress,pickupadress,destaddress;
    public static String pickupLong,distance,destinationLong,destinationLat,username,paymentMethod;
    public GCMIntentService()
    {
        super(SENDER_ID);
    }

    @Override
    // TODO:   method  Override here for device registered
    protected void onRegistered(Context context, String registrationId) {
        Log.i(TAG, "Device registered: regId = " + registrationId);
        displayMessage(context, "Your device registred with GCM");

        ServerUtilities.register(context, registrationId);
    }


    @Override
    // TODO:   method  Override here for device Unregistered
    protected void onUnregistered(Context context, String registrationId) {
        Log.i(TAG, "Device unregistered");
        displayMessage(context, getString(R.string.gcm_unregistered));
      //  ServerUtilities.unregister(context, registrationId);
    }


    @Override
    // TODO:   method  Override here for Receiving a new message
    protected void onMessage(Context context, Intent intent) {
        Log.i(TAG, "Received message"+intent.toString());

        message = intent.getExtras().getString("message");
        System.out.println("notifications message==>>" +message);

        if(message.equals("user/admin send a reservation request"))
        {
            MyApplication.resvflag=true;
            driverId = intent.getExtras().getString("driverId");
            userId = intent.getExtras().getString("userId");
            pickupLat = intent.getExtras().getString("pickupLat");
            pickupLong = intent.getExtras().getString("pickupLong");
            destinationLat = intent.getExtras().getString("destinationLat");
            destinationLong = intent.getExtras().getString("destinationLong");
            username = intent.getExtras().getString("username");
            paymentMethod = intent.getExtras().getString("paymentMethod");
            reservationId=intent.getExtras().getString("reservationId");
            cabType=intent.getExtras().getString("cabType");
            pickupaddress=intent.getExtras().getString("pickupaddress");
            destinationaddress=intent.getExtras().getString("destinationaddress");

            MyApplication.RdriverId=driverId;
            MyApplication.RuserId=userId;
            MyApplication.RpickupLat=pickupLat;
            MyApplication.RpickupLong=pickupLong;
            MyApplication.RdestinationLat=destinationLat;
            MyApplication.RdestinationLong=destinationLong;
            MyApplication.RpaymentMethod=paymentMethod;
            MyApplication.RreservationId=reservationId;
            MyApplication.RcabType=cabType;
            MyApplication.Rpickupaddress=pickupaddress;
            MyApplication.Rdestinationaddress=destinationaddress;
            System.out.println("dataFrom gcm bookingId==>>" + MyApplication.RdriverId);
            System.out.println("dataFrom gcm reservationId ==>>" + MyApplication.RreservationId);
            System.out.println("dataFrom gcm message ==>>" + MyApplication.message);


            Intent broadcastIntent = new Intent();
            broadcastIntent.setAction("upadteride");
            context.sendBroadcast(broadcastIntent);
            displayMessage(context, message);

            generateNotification(context, message);



        }else if(message.equals("user cancel this ride")) {

            MyApplication.declineuser=true;
            MyApplication.userdeclinebookingId=intent.getExtras().getString("bookingId");

            Intent broadcastIntent = new Intent();
            broadcastIntent.setAction("decineuser");
            context.sendBroadcast(broadcastIntent);
            displayMessage(context, message);

            generateNotification(context, message);

        }else{
            mPref = context.getSharedPreferences(MYPREF, Context.MODE_PRIVATE);
            bookingId = intent.getExtras().getString("bookingId");
            driverId = intent.getExtras().getString("driverId");
            userId = intent.getExtras().getString("userId");
            pickupLat = intent.getExtras().getString("pickupLat");
            pickupLong = intent.getExtras().getString("pickupLong");
            destinationLat = intent.getExtras().getString("destinationLat");
            destinationLong = intent.getExtras().getString("destinationLong");
            distance = intent.getExtras().getString("distance");
            username = intent.getExtras().getString("username");
            paymentMethod = intent.getExtras().getString("paymentMethod");
            MyApplication.usermobile = intent.getExtras().getString("userMobile");

            pickupadress = intent.getExtras().getString("pickupaddress");
            destaddress = intent.getExtras().getString("destinationaddress");

            MyApplication.pickupadress=pickupadress;
            MyApplication.destinationsaddress=destaddress;


            System.out.println("dataFrom gcm bookingId==>>" + bookingId);
            System.out.println("dataFrom gcm message==>>" + message);
            System.out.println("dataFrom gcm driverId ==>>" + driverId);
            System.out.println("dataFrom gcm reservationId ==>>" + intent.getExtras().getString("reservationId"));
            System.out.println("dataFrom gcm userId ==>>" + userId);
            System.out.println("dataFrom gcm pickupLat ==>>" + pickupLat);
            System.out.println("dataFrom gcm userMobile ==>>" + MyApplication.usermobile);
            System.out.println("dataFrom gcm pickupLong ==>>" + pickupLong);
            System.out.println("dataFrom gcm destinationLat ==>>" + destinationLat);
            System.out.println("dataFrom gcm destinationLong ==>>" + destinationLong);
            System.out.println("dataFrom gcm distance ==>>" + distance);
            System.out.println("dataFrom gcm username ==>>" + username);
            System.out.println("dataFrom gcm paymentMethod ==>>" + paymentMethod);
            System.out.println("dataFrom gcm pickupadress ==>>" + pickupadress);
            System.out.println("dataFrom gcm destaddress ==>>" + destaddress);


            CommonMethodUtil.saveUserId(context, ConstantUtil.USER_ID, userId);
            CommonMethodUtil.saveBookingId(context, ConstantUtil.BOOKING_ID, bookingId);


            /*if(message!=null&&bookingId!=null&&driverId!=null&&userId!=null&&pickupLat!=null&&pickupLong!=null&&destinationLat!=null&&destinationLong!=null&&distance!=null&&username!=null&&paymentMethod!=null)
            {*/
            MyApplication.message = message;
            MyApplication.bookingId = bookingId;
            MyApplication.driverId = driverId;
            MyApplication.userId = userId;
            MyApplication.pickupLat = pickupLat;
            MyApplication.pickupLong = pickupLong;
            MyApplication.destinationLat = destinationLat;
            MyApplication.destinationLong = destinationLong;
            MyApplication.distance = distance;
            MyApplication.username = username;
            MyApplication.paymentMethod = paymentMethod;
            System.out.println("Payment Method ==>>" +"CheckForPayament"+intent.getExtras().getString("paymentMethod"));
            System.out.println("Payment Method ==>>" + MyApplication.paymentMethod);
            //}


            Intent broadcastIntent = new Intent();
            broadcastIntent.setAction("upadteride");
            context.sendBroadcast(broadcastIntent);

            displayMessage(context, message);
            // notifies user
            generateNotification(context, message);
            //generateNotification1(context, message);
        }
    }


    @Override
    // TODO:   method  Override here for receiving a deleted message
    protected void onDeletedMessages(Context context, int total) {
        Log.i(TAG, "Received deleted messages notification");
        String message = getString(R.string.gcm_deleted, total);
       
     
        displayMessage(context, message);
        // notifies user
        generateNotification(context, message);
        //generateNotification1(context, message);
    }


    @Override
    // TODO:   method  Override here for  Error
    public void onError(Context context, String errorId) {
        Log.i(TAG, "Received error: " + errorId);
        displayMessage(context, getString(R.string.gcm_error, errorId));
    }

    @Override
    protected boolean onRecoverableError(Context context, String errorId) {
        // log message
        Log.i(TAG, "Received recoverable error: " + errorId);
        displayMessage(context, getString(R.string.gcm_recoverable_error,
                errorId));
        return super.onRecoverableError(context, errorId);
    }

    /**
     * Issues a notification to inform the user that server has sent a message.
     */
    @SuppressWarnings("deprecation")
	private static void generateNotification(Context context, String message) {

            NotificationManager notificationManager = (NotificationManager)
                    context.getSystemService(Context.NOTIFICATION_SERVICE);
            //oldNotification notification = new Notification(icon, message, when);
        ActivityManager am = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);

        // get the info from the currently running task
        List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);

        ComponentName componentInfo = taskInfo.get(0).topActivity;
        if (componentInfo.getPackageName().equalsIgnoreCase("com.alivenet.dmv.driverapplication")) {

        } else {

            Notification notification;
            Intent myIntent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, myIntent, 0);
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                    .setContentTitle(message).setSmallIcon(R.mipmap.ic_launcher).setContentIntent(pendingIntent);
            notification = notificationBuilder.build();
            //old notification.setLatestEventInfo(context, title, message, intent);
            notification.flags |= Notification.FLAG_AUTO_CANCEL;
            // Play default notification sound
            notification.defaults |= Notification.DEFAULT_SOUND;
            // Vibrate if vibrate is enabled
            notification.defaults |= Notification.DEFAULT_VIBRATE;
            notificationManager.notify(0, notification);

            String title = context.getString(R.string.app_name);
            System.out.println("activitey call on notifications");
            System.out.println("after activitey call on notifications");
        }


    }
}
