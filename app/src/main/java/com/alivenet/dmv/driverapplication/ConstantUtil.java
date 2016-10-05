package com.alivenet.dmv.driverapplication;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ConstantUtil {

    public static final String FALSE = "false";
    public static final String TXT_BLANK = "";
    public static final int REQUEST_CODE_GALLERY = 0x1;
    public static final int REQUEST_CODE_TAKE_PICTURE = 0x2;
    public static final int REQUEST_CODE_CROP_IMAGE = 0x3;
    public static final String BLANK_TEXT = "";
    public static String TEMP_PHOTO_FILE_NAME = "temp_photo.jpg";
    //public static final String baseurl = "http://alivenetsolution.co/dmv_taxi/webservice/";
    public static final String baseurl = "http://www.dmvtaxi.com/webservice/";

    //public static final String LOG_IN = "http://alivenetsolution.co/dmv_taxi/webservice/login.php";

    public static final String LOG_IN = baseurl+"login.php";
    public static final String CONFIRM_TRIP = baseurl+"acceptride.php";
    public static final String RIDE_START = baseurl+"ridestart.php";
    public static final String ARRIVED_DRIVER = baseurl+"driverReachedNotification.php";
    public static final String Cab_list = baseurl+"cablist.php";
    public static final String myaccount_details = baseurl+"driverDetail.php";
    public static final String Registation = baseurl+"registration.php";
    public static final String Sendlocation = baseurl+"sendlocation.php";
    public static final String TRIP_COMPLETION =baseurl+"ridecomplete.php";
    public static final String Profile_Update= baseurl+"updateProfile.php";
    public static final String Log_out= baseurl+"logoutD.php";
    public static final String ResvrationAcceptReject= baseurl+"accept_reservation.php";
    public static final String driver_terms= baseurl+"customeragreement.php";
    public static final String policy_terms= baseurl+"customerpolicy.php";
    public static final String forgot_password= baseurl+"forgot_password.php";
    public static final String Reset_password= baseurl+"reset_password.php";
    public static final String Reservation_list = baseurl+"driver_reservation_list.php";
    public static final String Reservation_CANCEL = baseurl+"cancel_reservation.php";
    public static final String USER_ID = "userId";
    public static final String BOOKING_ID = "bookingId";
    public static final String LoginType = "loginType";


    public static boolean copyStream(InputStream input, OutputStream output)
            throws IOException {
        boolean isCopied = false;
        byte[] buffer = new byte[1024];
        int bytesRead;
        try {
            while ((bytesRead = input.read(buffer)) != -1) {
                output.write(buffer, 0, bytesRead);
            }
            isCopied = true;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            isCopied = false;
        }
        return isCopied;
    }

}
