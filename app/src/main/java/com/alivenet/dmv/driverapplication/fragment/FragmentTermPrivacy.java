package com.alivenet.dmv.driverapplication.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.alivenet.dmv.driverapplication.CommonMethodUtil;
import com.alivenet.dmv.driverapplication.ConstantUtil;
import com.alivenet.dmv.driverapplication.MyApplication;
import com.alivenet.dmv.driverapplication.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;
import org.sufficientlysecure.htmltextview.HtmlRemoteImageGetter;
import org.sufficientlysecure.htmltextview.HtmlTextView;

import cz.msebera.android.httpclient.Header;

/**
 * Created by sunil on 8/9/2016.
 */
public class FragmentTermPrivacy extends Fragment {
    public View view;
    ProgressDialog prgDialog;
    HtmlTextView htmlTextView;

    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.terms_privacy, container, false);
        htmlTextView = (HtmlTextView)view.findViewById(R.id.html_text);


        if(MyApplication.termprivacy==true){
            RequestParams params = new RequestParams();
                params.put("","");
            driverprivacy(params);

        }
        if(MyApplication.termprivacy==false){
            Log.e("privaccyterm","calling privacy term");
           RequestParams params = new RequestParams();
            params.put("","");
            privacyterm(params);

        }
        return view;
    }


    public void driverprivacy(RequestParams params) {

        String url = ConstantUtil.driver_terms;
        prgDialog = new ProgressDialog(getActivity());
        prgDialog.setMessage("Please wait...");
        prgDialog.setCancelable(false);

        prgDialog.show();
        AsyncHttpClient client = new AsyncHttpClient();
       // client.setTimeout(3000);
        client.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                prgDialog.hide();
                try {
                    Log.d("driver term Responce",response.toString());
                    String responseCode = response.getString("responseCode");
                    String responseMessage = response.getString("responseMsg");
                    String driveragreement = response.getString("termsCondition");
                    if(responseMessage.equals("success"))
                    {
                        htmlTextView.setHtml(driveragreement,new HtmlRemoteImageGetter(htmlTextView));

                    }else{
                        CommonMethodUtil.showAlert(responseMessage,getActivity());
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                Toast toast = Toast.makeText(getActivity(), "Connection error please try again", Toast.LENGTH_LONG);
                toast.show();
                prgDialog.dismiss();
            }

            @Override
            public void onFinish() {
                super.onFinish();
                prgDialog.dismiss();
            }
        });
    }



    public void privacyterm(RequestParams params) {

        String url = ConstantUtil.policy_terms;
        prgDialog = new ProgressDialog(getActivity());
        prgDialog.setMessage("Please wait...");
        prgDialog.setCancelable(false);

        prgDialog.show();
        AsyncHttpClient client = new AsyncHttpClient();
       // client.setTimeout(3000);
        client.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                prgDialog.hide();
                try {
                    Log.d("driver term Responce",response.toString());
                    String responseCode = response.getString("responseCode");
                    String responseMessage = response.getString("responseMsg");
                    String driveragreement = response.getString("customerPolicy");
                    if(responseMessage.equals("success"))
                    {
                        htmlTextView.setHtml(driveragreement,new HtmlRemoteImageGetter(htmlTextView));

                    }else{
                        CommonMethodUtil.showAlert(responseMessage,getActivity());
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                Toast toast = Toast.makeText(getActivity(), "Connection error please try again", Toast.LENGTH_LONG);
                toast.show();
                prgDialog.dismiss();
            }

            @Override
            public void onFinish() {
                super.onFinish();
                prgDialog.dismiss();
            }
        });
    }


}
