package com.alivenet.dmv.driverapplication;

import android.content.Context;
import android.content.Intent;

public final class CommonUtilities {




   // sever key -AIzaSyB5MpYoqg7_zZDYKv7qePenn_KelWZeCH4
    // Google project id
	 public static final String SENDER_ID = "289049271412";
    /**
     * Tag used on log messages.
     */
    public static final String TAG = "AndroidHive GCM";

    public static final String DISPLAY_MESSAGE_ACTION =
            "com.alivenetsolutions.driverapp.DISPLAY_MESSAGE";

    public static final String EXTRA_MESSAGE = "message";

   public  static void displayMessage(Context context, String message) {
        Intent intent = new Intent(DISPLAY_MESSAGE_ACTION);
        intent.putExtra(EXTRA_MESSAGE, message);
        context.sendBroadcast(intent);
    }
}
