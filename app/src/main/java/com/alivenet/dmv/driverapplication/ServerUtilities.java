package com.alivenet.dmv.driverapplication;

import android.content.Context;
import android.util.Log;

import com.google.android.gcm.GCMRegistrar;

import java.util.Random;

import static com.alivenet.dmv.driverapplication.CommonUtilities.TAG;
import static com.alivenet.dmv.driverapplication.CommonUtilities.displayMessage;


public final class ServerUtilities {
	private static final int MAX_ATTEMPTS = 1000;
    private static final int BACKOFF_MILLI_SECONDS = 2000;
    private static final Random random = new Random();
    public static String regID;



public static void register(final Context context,final String regId)
{
    regID=regId; long backoff = BACKOFF_MILLI_SECONDS + random.nextInt(1000);
        // Once GCM returns a registration id, we need to register on our server
        // As the server might be down, we will retry it a couple
        // times.
        for (int i = 1; i <= MAX_ATTEMPTS; i++) {
            Log.d(TAG, "Attempt #" + i + " to register");
          
                displayMessage(context, context.getString(
                        R.string.server_registering, i, MAX_ATTEMPTS));
                GCMRegistrar.setRegisteredOnServer(context, true);
                String message = context.getString(R.string.server_registered);
                CommonUtilities.displayMessage(context, message);
                return;
            }
                // increase backoff exponentially
                backoff *= 2;


        String message = context.getString(R.string.server_register_error,
                MAX_ATTEMPTS);
        CommonUtilities.displayMessage(context, message);
    
    }

}
