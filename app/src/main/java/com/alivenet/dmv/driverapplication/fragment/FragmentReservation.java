package com.alivenet.dmv.driverapplication.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;


import com.alivenet.dmv.driverapplication.CommonMethod;
import com.alivenet.dmv.driverapplication.CommonMethodUtil;
import com.alivenet.dmv.driverapplication.ConstantUtil;
import com.alivenet.dmv.driverapplication.R;
import com.alivenet.dmv.driverapplication.ReservationListModel;
import com.alivenet.dmv.driverapplication.ReservationlistRecylerViewAdapter;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;

/**
 * Created by narendra on 6/17/2016.
 */
public class FragmentReservation extends Fragment {

    RecyclerView recyclerView;
    public static final String MYPREF = "user_info";
    SharedPreferences mPref;
    public String mUserId;
    private ArrayList<ReservationListModel> mReslist = new ArrayList<ReservationListModel>();
    ProgressDialog progressDialog;
    TextView mreservation;
    Activity activity;
    ReservationlistRecylerViewAdapter rcAdapter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = getActivity();
    }

    public static FragmentReservation newInstance(String sectionTitle) {
        FragmentReservation fragment = new FragmentReservation();
        Bundle args = new Bundle();
        // args.putString(ARG_SECTION_TITLE, sectionTitle);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reservation_list, container, false);
        mPref = getActivity().getSharedPreferences(MYPREF, Context.MODE_PRIVATE);
        mUserId = mPref.getString("userId", null);

        progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //recyclerView.setHasFixedSize(true);
        /*LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
*/

        mreservation=(TextView) view.findViewById(R.id.tv_reservation);
        RequestParams params = new RequestParams();
        params.put("driverId",CommonMethod.getdriverid(getActivity()));
        Log.e("driveridres====>",CommonMethod.getdriverid(getActivity()));
        rideListWs(params);

        return view;
    }


    public void rideListWs(RequestParams params) {
        String url = ConstantUtil.Reservation_list;
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(url, params, new JsonHttpResponseHandler() {

            @Override
            public void onStart() {
                super.onStart();
                progressDialog.show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {

                    Log.d("reservationlist", response.toString());
                    String responseCode = response.getString("responseCode");
                    String responseMessage = response.getString("responseMsg");

                    if (responseCode.equals("200")) {

                        if (responseMessage.equals("success")) {
                            if (response.get("reservationList") instanceof JSONArray) {

                                mReslist.clear();
                                ReservationListModel mResv = null;
                                JSONArray jsonArray = null;
                                jsonArray = response.getJSONArray("reservationList");
                                for (int i = 0; i < jsonArray.length(); i++) {

                                    mResv = new ReservationListModel();
                                    JSONObject obj = (JSONObject) jsonArray.get(i);
                                    mResv.setBookingId(obj.getString("reservationId").toString().trim());
                                    mResv.setRevdate(obj.getString("reservatonDate").toString().trim());
                                    mResv.setRevtime(obj.getString("reservatonTime").toString().trim());
                                    mResv.setPicAddress(obj.getString("pickupAddress").toString().trim());
                                    mResv.setDestAddress(obj.getString("destinationAddress").toString().trim());


                                    mReslist.add(mResv);
                                }
                                rcAdapter = new ReservationlistRecylerViewAdapter(getActivity(), mReslist);
                                recyclerView.setAdapter(rcAdapter);
                            }

                        }
                    } else {
                        //Toast.makeText(getActivity(), "" + responseMessage, Toast.LENGTH_LONG).show();
                        mreservation.setVisibility(View.VISIBLE);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                progressDialog.dismiss();
                mreservation.setVisibility(View.GONE);
                CommonMethodUtil.showAlert(getResources().getString(R.string.connection_error),getActivity());
            }
            @Override
            public void onFinish() {
                super.onFinish();
                progressDialog.dismiss();
            }
        });

    }

    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();

            } else {

            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        return strAdd;
    }
}



