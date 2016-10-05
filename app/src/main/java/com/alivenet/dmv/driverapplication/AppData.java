package com.alivenet.dmv.driverapplication;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Environment;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;


public class AppData {
	public String user_Id;
	public String latitude;
	public String longitude;
	public void setUserId(String userId){
		this.user_Id=userId;
	}
	public String getUserId(){
		return user_Id;
	}

	public File mFileTemp;

	

	
	public File getmFileTemp() {
		return mFileTemp;
	}

	public void setmFileTemp(Activity context, String timestamp) {
		ConstantUtil.TEMP_PHOTO_FILE_NAME=timestamp+".jpg";
		String state = Environment.getExternalStorageState();
    	if (Environment.MEDIA_MOUNTED.equals(state)) {
    		mFileTemp = new File(Environment.getExternalStorageDirectory(), ConstantUtil.TEMP_PHOTO_FILE_NAME);
    	}
    	else {
    		mFileTemp = new File(context.getFilesDir(), ConstantUtil.TEMP_PHOTO_FILE_NAME);
    	}
		//this.mFileTemp = mFileTemp;
	}

	private static AppData singletonObject;

	public static AppData getSingletonObject() {
		if (singletonObject == null) {
			singletonObject = new AppData();
		}
		return singletonObject;
	}


}
