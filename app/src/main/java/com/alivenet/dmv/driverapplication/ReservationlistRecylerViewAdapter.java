package com.alivenet.dmv.driverapplication;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import util.HttpClient;

/**
 * Created by navin on 8/1/2016.
 */
public class ReservationlistRecylerViewAdapter extends RecyclerView.Adapter<ReservationlistRecylerViewAdapter.CustomViewHolder> {
    public static ArrayList<ReservationListModel> mResvationlist;
    ReservationListModel feedItem;
    public Context context;
    ProgressDialog prgDialog;
    public ReservationlistRecylerViewAdapter(Context context, ArrayList<ReservationListModel> mResvlist) {
        this.mResvationlist = mResvlist;
        this.context = context;

    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.reservationlistadapterlayout, viewGroup, false);
        CustomViewHolder viewHolder = new CustomViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final CustomViewHolder customViewHolder, int i) {

            feedItem = mResvationlist.get(i);

            customViewHolder.bokkingTime.setText(CommonMethodUtil.Timeformate(feedItem.getRevdate()+" "+feedItem.getRevtime()));
            customViewHolder.booking_id.setText(feedItem.getBookingId());
            customViewHolder.pickaddress.setText(feedItem.getPicAddress());
            customViewHolder.destinations.setText(feedItem.getDestAddress());
            customViewHolder.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelreservation cancel=new cancelreservation();
                cancel.execute();

            }
        });

        prgDialog = new ProgressDialog(context);
        prgDialog.setMessage("Please wait...");
        prgDialog.setCancelable(false);

        }

    @Override
    public int getItemCount() {
        return (null != mResvationlist ? mResvationlist.size() : 0);


    }


    class CustomViewHolder extends RecyclerView.ViewHolder {

        public TextView destinations, bokkingTime,booking_id,pickaddress,cancel;

        public CustomViewHolder(View view) {
            super(view);
            bokkingTime = (TextView) itemView.findViewById(R.id.booking_time);
            booking_id = (TextView) itemView.findViewById(R.id.booking_id);
            pickaddress = (TextView) itemView.findViewById(R.id.txt_sourceaddress);
            destinations = (TextView) itemView.findViewById(R.id.txt_destinationaddress);
            cancel=(TextView)itemView.findViewById(R.id.Text_cancel);
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


    public class cancelreservation extends AsyncTask<String, Void, String> {

        private Dialog mDialog;
        private String response;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            mDialog = ProgressDialog.show(context, "", "Loading...", true);
            mDialog.setCancelable(false);
        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            response = cancelreservaton();
            return response;
        }

        @Override
        protected void onPostExecute(String response) {
            // TODO Auto-generated method stub
            super.onPostExecute(response);
            if (mDialog != null) {
                mDialog.dismiss();
            }
            System.out.println("cancel responce========>>>>"+response);

            //responseCode
            JSONObject object,driverdetails;

            if (response!=null) {
                try {

                    object = new JSONObject(response);

                    String responseCode = object.getString("responseCode");
                    String responseMessage = object.getString("responseMsg");
                    // driverdetails = object.getJSONObject("driverDetail");

                    Log.d("object", "" + object);
                    if (responseCode.equals("200")) {
                        CommonMethod.showAlert(responseMessage,context);
                    }else{
                        Toast.makeText(context, responseMessage, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } else {

            }
        }

        private String cancelreservaton() {
            String url = ConstantUtil.Reservation_CANCEL;
            HttpClient client = new HttpClient(url);

            try {
                client.connectForMultipart();
                String resevrationid = feedItem.getBookingId();

                client.addFormPart("reservationId",resevrationid);
                client.addFormPart("driverId",CommonMethod.getdriverid(context));




                client.finishMultipart();
                response = client.getResponse().toString();




            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return response;
        }
    }


   /* public void cancelleddriverresveration(RequestParams params) {

        String url = ConstantUtil.Reservation_CANCEL;
        prgDialog.show();
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                prgDialog.hide();
                try {
                    Log.d("resevrationcancel rs", response.toString());
                    String responseCode = response.getString("responseCode");
                    String responseMessage = response.getString("responseMsg");

                    if (responseCode.equals("200")) {

                     CommonMethod.showAlert(responseMessage,context);

                    } else {
                        //  new IncorrectPassworllodDialog(LoginActivity.this, "hello").show();
                        Toast.makeText(context, responseMessage, Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                prgDialog.hide();
                Toast toast = Toast.makeText(context, "Unable to connect to the server, please try again later.", Toast.LENGTH_SHORT);toast.show();
            }


            @Override
            public void onFinish() {
                super.onFinish();
                prgDialog.hide();
            }
        });
    }*/


}




















/* extends RecyclerView.Adapter<ReservationlistRecylerViewAdapter.ResverationListViewHolder>  {
    public static ArrayList<ReservationListModel> mResvationlist;
    private Context context;
    private SparseBooleanArray selectedItems;
    ProgressDialog prgDialog;

    public ReservationlistRecylerViewAdapter(Context context, ArrayList<ReservationListModel> mResvlist) {
        this.mResvationlist = mResvlist;
        this.context = context;
        prgDialog = new ProgressDialog(context);
        prgDialog.setMessage("Please wait...");
        prgDialog.setCancelable(false);

    }

    @Override
    public ResverationListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.reservationlistadapterlayout, null);
        ResverationListViewHolder rcv = new ResverationListViewHolder(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(final ResverationListViewHolder holder, final int position) {

        String status = mResvationlist.get(position).getStatus();

        //    holder.cabName.setText(mCabDetailsList.get(position).getCabName());
        holder.bokkingTime.setText(mResvationlist.get(position).getRevdate()+" , "+mResvationlist.get(position).getRevtime());
        holder.booking_id.setText(mResvationlist.get(position).bookingId);


        holder.pickaddress.setText(mResvationlist.get(position).getPicAddress());
        holder.destinations.setText(mResvationlist.get(position).getDestAddress());


       *//* ((Activity)context).runOnUiThread(new Runnable()
        {
            public void run()
            {
                System.out.println("Thread Running");
                holder.pickaddress.setText(getCompleteAddressString(Double.parseDouble(mResvationlist.get(position).getPiclat()),Double.parseDouble(mResvationlist.get(position).getPiclong())));
                holder.destinations.setText(getCompleteAddressString(Double.parseDouble(mResvationlist.get(position).getDestlat()),Double.parseDouble(mResvationlist.get(position).getDestlong())));

            }
        });*//*


*//*

        if (status.equals("1")) {
            holder.status.setText("Scheduled");
            holder.status.setBackgroundColor(ContextCompat.getColor(context, R.color.status_sheduled));
            holder.status.setTag(1);
        } else if (status.equals("2")) {
            holder.status.setText("Scheduled");
            holder.status.setBackgroundColor(ContextCompat.getColor(context, R.color.status_sheduled));
            holder.status.setTag(2);

        } else if (status.equals("3")) {
            holder.status.setText("Cancelled");
            holder.status.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary));
            holder.status.setTag(3);
            //  holder.status.setBackgroundColor(Color.RED);
        } else if (status.equals("4")) {
            holder.status.setText("Completed");
            holder.status.setBackgroundColor(ContextCompat.getColor(context, R.color.graycolor));
            holder.status.setTag(4);
        }*//*

        //  getCompleteAddressString(Double.parseDouble(mRideList.get(position).getPickupLat()),Double.parseDouble(mRideList.get(position).getPickupLong()));
    }

    @Override
    public int getItemCount() {
        return this.mResvationlist.size();
    }



 private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
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


    class ResverationListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private View oldSelection = null;
        public TextView destinations, bokkingTime, status,booking_id,pickaddress;

        public ImageView cabIcon;
        public CardView myBackground;
        int position;
        int oldposition;
        ProgressDialog prgDialog;


        public ResverationListViewHolder(View itemView) {
            super(itemView);


            bokkingTime = (TextView) itemView.findViewById(R.id.booking_time);
            booking_id = (TextView) itemView.findViewById(R.id.booking_id);
            pickaddress = (TextView) itemView.findViewById(R.id.txt_sourceaddress);
            destinations = (TextView) itemView.findViewById(R.id.txt_destinationaddress);
          //  status = (TextView) itemView.findViewById(R.id.status);
           // status.setOnClickListener(this);

        }


        @Override
        public void onClick(View view) {
         *//*   position = getPosition();
            if (Integer.parseInt(status.getTag().toString()) == 1) {

                String bookingID = mResvationlist.get(position).getBookingId();
                Toast.makeText(context, "hello1 " + bookingID, Toast.LENGTH_LONG).show();
                RequestParams params = new RequestParams();
                params.put("bookingId", bookingID);
                cancelledRideWs(params);
                // Toast.makeText(context,"hello1 ",Toast.LENGTH_LONG).show();
            } else if (Integer.parseInt(status.getTag().toString()) == 2) {
                Toast.makeText(context, "hello2 ", Toast.LENGTH_LONG).show();
            } else if (Integer.parseInt(status.getTag().toString()) == 3) {
                Toast.makeText(context, "hello2 ", Toast.LENGTH_LONG).show();
            } else if (Integer.parseInt(status.getTag().toString()) == 4) {
                Toast.makeText(context, "hello2 ", Toast.LENGTH_LONG).show();
            }*//*


        }



    }

}
*/