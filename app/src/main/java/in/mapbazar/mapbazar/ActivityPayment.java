package in.mapbazar.mapbazar;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import in.mapbazar.mapbazar.Model.Address;
import in.mapbazar.mapbazar.Model.PinModel;
import in.mapbazar.mapbazar.Model.ProductListModel.ProductItem;

import in.mapbazar.mapbazar.Model.ShippinModel;
import in.mapbazar.mapbazar.Modules.Module;
import in.mapbazar.mapbazar.Payment.Checksum;
import in.mapbazar.mapbazar.Payment.Paytm;
import in.mapbazar.mapbazar.Payment.WebServiceCaller;

import in.mapbazar.mapbazar.Utili.Common;
import in.mapbazar.mapbazar.Utili.Url;
import in.mapbazar.mapbazar.View.CustomTextView;
import in.mapbazar.mapbazar.View.DialogUtils;
import in.mapbazar.mapbazar.callback.CallbackMessage;
import in.mapbazar.mapbazar.connection.API;
import in.mapbazar.mapbazar.connection.RestAdapter;
import in.mapbazar.mapbazar.dialog.SelectTimeDialogFragment;

import java.net.SocketTimeoutException;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.mapbazar.mapbazar.util.ConnectivityReceiver;
import in.mapbazar.mapbazar.util.CustomVolleyJsonRequest;
import in.mapbazar.mapbazar.util.DatabaseCartHandler;
import in.mapbazar.mapbazar.util.Session_management;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.android.volley.VolleyLog.TAG;
import static in.mapbazar.mapbazar.Utili.Url.KEY_ID;
import static in.mapbazar.mapbazar.Utili.Url.TOPIC_GLOBAL;

public class ActivityPayment extends AppCompatActivity {

    String m_id="";
    String channel_id="";
    String website="";
    String indus_type_id="";
    String call_url="";
    double subtotal=0;
    double delivery_carges=0;
    double amount=0;
    CustomTextView notify;
    private Double rewards;
    String gettime,getdate,getuser_id,getlocation_id,getstore_id;
    double gms=0;
    double pcs=0;
   LinearLayout linear_pay;
   View line_pay,line1_pay;

    //Shared Preferences
    private SharedPreferences sPref;

    Dialog ProgressDialog;

    RelativeLayout ly_back;
    LinearLayout lay_status_delivery;
    LinearLayout lay_status_payment;
    Button txt_place_order;
    ImageView image_status;
    ImageView image_payment;
    RelativeLayout llDate;
    RelativeLayout llTime;
    CustomTextView tvDate;
    CustomTextView tvTime;
    Address address;
    String pin=null;
    ArrayList<String> timeSlotList;
    ArrayList<String> allTimeSlotList;
    SelectTimeDialogFragment selectTimeDialogFragment;

    CustomTextView txt_total,txt_shipping,txt_subtotal,txt_qty;

    int pYear, pMonth, pDay;
    DatePickerDialog.OnDateSetListener pDateSetListener;


    AutoCompleteTextView act_pincode;
    List<ShippinModel> modelList;
    List<PinModel> pinList;
    String[] sp=null;
    double d=0;
    double sum_qty=0;
    float tot=0;

  String location_id="";
  String shipping_charges="";
  String user_id="";
  String total_amount="";

    double del=0;
    JsonArray jsonArr=null;
    Session_management session_management;
    private DatabaseCartHandler db_cart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        db_cart=new DatabaseCartHandler(this);

        txt_qty=findViewById(R.id.txt_qty);
        txt_shipping=findViewById(R.id.txt_shipping);
        txt_subtotal=findViewById(R.id.txt_subtotal);
        txt_total=findViewById(R.id.txt_total);
        line_pay=(View)findViewById(R.id.line_pay);
        line1_pay=(View)findViewById(R.id.line1_pay);
        linear_pay=(LinearLayout)findViewById(R.id.linear_pay);
        notify=(CustomTextView)findViewById(R.id.notify);

        allTimeSlotList = new ArrayList<>();
        allTimeSlotList.add("10:00 AM - 2:00PM");
        allTimeSlotList.add("4:00 PM - 7:00PM");
        modelList=new ArrayList<>();
        sPref = PreferenceManager.getDefaultSharedPreferences(ActivityPayment.this);
        jsonArr=new JsonArray();
        initComponents();
        initdata();
      //  request_payment_status();

//        pin=address.getPincode().toString();
//        getData(pin);
        //setAmount();


        txt_place_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               if (image_status.getVisibility() == View.VISIBLE) {

//                    Toast.makeText(ActivityPayment.this,"data :- "+tvDate.getText().toString()+"\n time :-- "+tvTime.getText().toString()+
//                            "\n loc"+location_id+"\n user_id "+user_id+"\n ship - "+shipping_charges,Toast.LENGTH_LONG).show();

                    attemptOrder();
                 // GetOnlineJsonObject();
                //    Common.Errordialog(ActivityPayment.this, "Feature not implemented ");

                }else  if (image_status.getVisibility() == View.GONE && image_status.getVisibility() == View.GONE) {
                    Common.Errordialog(ActivityPayment.this, "Please select a payment method ");

                }
                else
                {
                    Toast.makeText(ActivityPayment.this,"data :- "+tvDate.getText().toString()+"\n time :-- "+tvTime.getText().toString()+
                            "\n loc"+location_id+"\n user_id "+user_id+"\n ship - "+shipping_charges,Toast.LENGTH_LONG).show();

                }

            }
        });
    }

    private void initComponents() {
        ly_back=(RelativeLayout)findViewById(R.id.ly_back);
        llDate=(RelativeLayout)findViewById(R.id.ll_date);
        llTime=(RelativeLayout)findViewById(R.id.ll_time);
        lay_status_delivery=(LinearLayout) findViewById(R.id.lay_status_delivery);
        lay_status_payment=(LinearLayout) findViewById(R.id.lay_status_payment);
        txt_place_order=(Button)findViewById(R.id.txt_place_order);
        image_status=(ImageView) findViewById(R.id.image_status);
        image_payment=(ImageView) findViewById(R.id.image_payment);
        tvDate=(CustomTextView)findViewById(R.id.tv_date);
        tvTime=(CustomTextView)findViewById(R.id.tv_time);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //btnData.performClick();
    }

    private void initdata() {
        Intent intent = getIntent();
        address = (Address) intent.getSerializableExtra("Address");

        ProgressDialog = new Dialog(ActivityPayment.this, android.R.style.Theme_Translucent_NoTitleBar);
        ProgressDialog.setContentView(R.layout.progressbar);
        ProgressDialog.setCancelable(false);

        lay_status_delivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                image_status.setVisibility(View.VISIBLE);
                image_payment.setVisibility(View.GONE);
            }
        });
        lay_status_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                image_payment.setVisibility(View.VISIBLE);
                image_status.setVisibility(View.GONE);
            }
        });


        location_id=getIntent().getStringExtra("location");
        shipping_charges=getIntent().getStringExtra("shipping_charge");
        session_management=new Session_management(ActivityPayment.this);
        user_id=session_management.getUserDetails().get(KEY_ID);


        txt_qty.setText(String.valueOf(db_cart.getCartCount()));
        subtotal=Double.parseDouble(db_cart.getTotalAmount());
        delivery_carges=Double.parseDouble(shipping_charges);
        amount=subtotal+delivery_carges;
        txt_subtotal.setText(getResources().getString(R.string.currency)+String.valueOf(subtotal));
        txt_shipping.setText(getResources().getString(R.string.currency)+String.valueOf(delivery_carges));
        txt_total.setText(getResources().getString(R.string.currency)+String.valueOf(subtotal)+" + "+getResources().getString(R.string.currency)+String.valueOf(delivery_carges)+" = "+getResources().getString(R.string.currency)+String.valueOf(amount));

        ly_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        final Calendar c = Calendar.getInstance();
        pYear = c.get(Calendar.YEAR);
        pMonth = c.get(Calendar.MONTH);
        pDay = c.get(Calendar.DAY_OF_MONTH);
        showDate();
        getTimeSlot();
        pDateSetListener = new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year,
                                  int monthOfYear, int dayOfMonth) {

                pYear = year;
                pMonth = monthOfYear;
                pDay = dayOfMonth;

                showDate();
                getTimeSlot();
            }
        };

        llDate.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        DatePickerDialog datePickerDialog = new DatePickerDialog(ActivityPayment.this, pDateSetListener, pYear, pMonth,
                                pDay);
                        long now = System.currentTimeMillis() - 1000;
                        datePickerDialog.getDatePicker().setMinDate(now);
                        datePickerDialog.getDatePicker().setMaxDate(now + (1000 * 60 * 60 * 24 * 2));
                        datePickerDialog.show();
                    }
                }
        );

        llTime.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!tvTime.getText().toString().equalsIgnoreCase("No time slot for today")) {
                            selectTimeDialogFragment = new SelectTimeDialogFragment();
                            selectTimeDialogFragment.setTimeSlot(timeSlotList);
                            selectTimeDialogFragment.setOnDismissListener(new OnDismissListener() {
                                @Override
                                public void onDismiss(String time) {
                                    tvTime.setText(time);
                                    selectTimeDialogFragment = null;
                                }
                            });


                            selectTimeDialogFragment.show(ActivityPayment.this.getSupportFragmentManager(), "");
                        }
                    }
                }
        );


    }

    public void getTimeSlot() {

        Calendar now = Calendar.getInstance();
        if (now.get(Calendar.DATE) == pDay) {
            int currentHourIn12Format = now.get(Calendar.HOUR_OF_DAY);
            if (currentHourIn12Format < 13) {
                timeSlotList = new ArrayList<>();
                timeSlotList.add(allTimeSlotList.get(0));
                timeSlotList.add(allTimeSlotList.get(1));
                tvTime.setText(allTimeSlotList.get(0));
            } else if (currentHourIn12Format < 18) {
                timeSlotList = new ArrayList<>();
                timeSlotList.add(allTimeSlotList.get(1));
                tvTime.setText(timeSlotList.get(0));
            } else {
                tvTime.setText("No time slot for today");
            }

            Log.d("", currentHourIn12Format + "");
        } else {
            timeSlotList = new ArrayList<>();
            timeSlotList.add(allTimeSlotList.get(0));
            timeSlotList.add(allTimeSlotList.get(1));
            tvTime.setText(allTimeSlotList.get(0));
        }

    }

    public interface OnDismissListener {
        void onDismiss(String time);
    }

    public String getMonth(int month) {
        return new DateFormatSymbols().getMonths()[month - 1];
    }

    public void showDate() {
        tvDate.setText(new StringBuilder()
                // to set date in editext
                .append(pDay).append(" ").append(getMonth(pMonth + 1))
                .append(" ").append(pYear).append(" "));

    }


    private void request_payment_status()
    {
        ProgressDialog.show();
        String user_id=sPref.getString("u_id","0");

        API api=RestAdapter.createAPI();
        Call<JsonObject> callback_payment=api.get_payment_status(user_id);
        callback_payment.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                try
                {
                    ProgressDialog.dismiss();
                   if(response.isSuccessful())
                   {
                       JsonObject object=response.body();
                       boolean status=object.get("status").getAsBoolean();
                       if(status)
                       {
                           line_pay.setVisibility(View.VISIBLE);
                           line1_pay.setVisibility(View.VISIBLE);
                           linear_pay.setVisibility(View.VISIBLE);

                           JsonObject jsonObject=object.get("data").getAsJsonObject();

                           m_id=jsonObject.get("MID").getAsString();
                           channel_id=jsonObject.get("CHANNEL_ID").getAsString();
                           website=jsonObject.get("WEBSITE").getAsString();
                           indus_type_id=jsonObject.get("INDUSTRY_TYPE_ID").getAsString();
                           call_url=jsonObject.get("CALLBACK_URL").getAsString();
                           notify.setText(object.get("data").getAsString());
                           notify.setTextColor(Color.parseColor(jsonObject.get("color").getAsString()));

                       }
                       else
                       {
                           JsonObject jsonObject=object.get("data").getAsJsonObject();

                           notify.setText(jsonObject.get("text").getAsString());
                           notify.setTextColor(Color.parseColor(jsonObject.get("color").getAsString()));
                       }

                   }
                   else
                   {
                       //Toast.makeText(ActivityPayment.this,"Something Wrong",Toast.LENGTH_LONG).show();
                   }
                }
                catch (Exception ex)
                {
                    if (ProgressDialog != null && ProgressDialog.isShowing())
                        ProgressDialog.dismiss();

                    ErrorMessage("Something went wrong!");
                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                ProgressDialog.dismiss();
            }
        });
    }




    private void ErrorMessage(String Message) {
        // Common.showInternetInfo(ActivitySignIn.this, "Network is not available");
        Dialog dialog = new DialogUtils(ActivityPayment.this).buildDialogMessage(new CallbackMessage() {

            @Override
            public void onSuccess(Dialog dialog) {
                dialog.dismiss();
            }

            @Override
            public void onCancel(Dialog dialog) {
                dialog.dismiss();
            }

        }, Message);
        dialog.show();
    }

    private void SuccessDailog(String Message) {
        // Common.showInternetInfo(ActivitySignIn.this, "Network is not available");
        Dialog dialog = new DialogUtils(ActivityPayment.this).buildDialogMessage(new CallbackMessage() {

            @Override
            public void onSuccess(Dialog dialog) {
                dialog.dismiss();

                Intent thank = new Intent(ActivityPayment.this, ActivityThank.class);
                startActivity(thank);
            }

            @Override
            public void onCancel(Dialog dialog) {
                dialog.dismiss();
            }

        }, Message);
        dialog.show();
    }

    @Override
    public void onBackPressed() {
        if (selectTimeDialogFragment != null) {
            selectTimeDialogFragment = null;
        }
        super.onBackPressed();
    }



    private void processPaytm(String paytm_oreder_id,String total_amount) {

        //String mid ="qyXXCi36131314161940";

        String custID=sPref.getString("uid", "");
        String orderID=paytm_oreder_id;
        String callBackurl=call_url+orderID;
        //  https://securegw.paytm.in/order/process
     //    String callBackurl="https://securegw-stage.paytm.in/theia/paytmCallback?ORDER_ID="+orderID;
    //   String callBackurl="https://securegw.paytm.in/theia/paytmCallback?ORDER_ID="+orderID;


        final Paytm paytm=new Paytm(
                m_id,
                channel_id,
                total_amount,
                website,
                callBackurl,
                indus_type_id,
                orderID,
                custID
        );

        WebServiceCaller.getClient().getChecksum(paytm.getmId(), paytm.getOrderId(), paytm.getCustId()
                , paytm.getChannelId(), paytm.getTxnAmount(), paytm.getWebsite(), paytm.getCallBackUrl(), paytm.getIndustryTypeId()
        ).enqueue(new Callback<Checksum>() {
            @Override
            public void onResponse(Call<Checksum> call, retrofit2.Response<Checksum> response) {
                if (response.isSuccessful()) {
                    processToPay(response.body().getChecksumHash(),paytm);
                }
            }

            @Override
            public void onFailure(Call<Checksum> call, Throwable t) {

            }
        });
    }

    private void processToPay(String checksumHash, Paytm paytm) {
        PaytmPGService Service = PaytmPGService.getStagingService();

        HashMap<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("MID", paytm.getmId());
// Key in your staging and production MID available in your dashboard
        paramMap.put("ORDER_ID", paytm.getOrderId());
        paramMap.put("CUST_ID", paytm.getCustId());
        paramMap.put("CHANNEL_ID", paytm.getChannelId());
        paramMap.put("TXN_AMOUNT", paytm.getTxnAmount());
        paramMap.put("WEBSITE", paytm.getWebsite());
// This is the staging value. Production value is available in your dashboard
        paramMap.put("INDUSTRY_TYPE_ID", paytm.getIndustryTypeId());
// This is the staging value. Production value is available in your dashboard
        paramMap.put("CALLBACK_URL", paytm.getCallBackUrl());
        paramMap.put("CHECKSUMHASH", checksumHash);
        PaytmOrder Order = new PaytmOrder(paramMap);
        Service.initialize(Order, null);

        Service.startPaymentTransaction(ActivityPayment.this, true, true, new PaytmPaymentTransactionCallback() {
            /*Call Backs*/
            public void someUIErrorOccurred(String inErrorMessage) {
            }

            public void onTransactionResponse(Bundle inResponse) {

                try
                {
                    String st=inResponse.getString("STATUS");
                    String txn_id=inResponse.getString("TXNID");
                  //  updatePaymentStatus(paytm_oreder_id,txn_id);
                    // Toast.makeText(getActivity(), "status :-- "+st.toString()+"\n txn_id:-- "+txn_id.toString(), Toast.LENGTH_SHORT).show();
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                    Toast.makeText(ActivityPayment.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
                }
                // Toast.makeText(getActivity(), inResponse.toString(), Toast.LENGTH_SHORT).show();

            }

            public void networkNotAvailable() {
                Toast.makeText(ActivityPayment.this,"networkNotAvailable", Toast.LENGTH_SHORT).show();
            }

            public void clientAuthenticationFailed(String inErrorMessage) {
                Toast.makeText(ActivityPayment.this,"clientAuthenticationFailed", Toast.LENGTH_SHORT).show();
            }

            public void onErrorLoadingWebPage(int iniErrorCode, String inErrorMessage, String inFailingUrl) {
                Toast.makeText(ActivityPayment.this,"onErrorLoadingWebPage", Toast.LENGTH_SHORT).show();
            }

            public void onBackPressedCancelTransaction() {
                Toast.makeText(ActivityPayment.this,"onBackPressedCancelTransaction", Toast.LENGTH_SHORT).show();
            }

            public void onTransactionCancel(String inErrorMessage, Bundle inResponse) {
                Toast.makeText(ActivityPayment.this,"onTransactionCancel "+ inErrorMessage.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void attemptOrder() {
        try
        {
            List<HashMap<String, String>> items = new ArrayList<>();
            items=db_cart.getCartAll();
            //rewards = Double.parseDouble(db_cart.getColumnRewards());
            rewards = Double.parseDouble("0");

            if (items.size() > 0) {
                JSONArray passArray = new JSONArray();
                for (int i = 0; i < items.size(); i++) {
                    HashMap<String, String> map = items.get(i);
                    // String unt=
                    JSONObject jObjP = new JSONObject();
                    try {

                        jObjP.put("product_id", map.get("product_id"));
                        jObjP.put("qty", map.get("qty"));
                        jObjP.put("unit_value", map.get("unit_price"));
                        jObjP.put("unit", map.get("unit"));
                        jObjP.put("price", map.get("price"));
                        jObjP.put("rewards", "0");
                        passArray.put(jObjP);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(ActivityPayment.this,""+e.getMessage(),Toast.LENGTH_LONG).show();
                    }
                }

                getuser_id = user_id;

                if (ConnectivityReceiver.isConnected()) {



                    Date date=new Date();

                    SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
                    String g=dateFormat.format(date);
                    SimpleDateFormat dateFormat1=new SimpleDateFormat("hh:mm a");
                    String t=dateFormat1.format(date);

                    gettime=tvTime.getText().toString();
                    //   Toast.makeText(getActivity(),"Time"+t,Toast.LENGTH_LONG).show();
                    getdate=tvDate.getText().toString();
                    getlocation_id=location_id;
                    getstore_id="";
                    //gettime="03:00 PM - 03:30 PM";
                    // getdate="2019-7-23";
                    Log.e(TAG, "from:" +"03:00 PM - 03:30 PM" + "\ndate:" + "2019-7-23" +
                            "\n" + "\nuser_id:" + getuser_id + "\n l" + getlocation_id + getstore_id + "\ndata:" + passArray.toString());
//                    Toast.makeText(ActivityPayment.this, "from:" + gettime + "\ndate:" + getdate +
//                            "\n" + "\nuser_id:" + getuser_id + "\n" + getlocation_id + getstore_id + "\ndata:" + passArray.toString(),Toast.LENGTH_LONG).show();

                    makeAddOrderRequest(getdate, gettime, getuser_id, getlocation_id, getstore_id, passArray);


                }
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            Toast.makeText(ActivityPayment.this,""+ex.getMessage(),Toast.LENGTH_LONG).show();
        }

    }

    private void makeAddOrderRequest(String date, String gettime, String userid, String
            location, String store_id, JSONArray passArray) {

        ProgressDialog.show();
        String tag_json_obj = "json_add_order_req";
       String  getvalue="Cash On Delivery";
        total_amount=String.valueOf(amount);
        Map<String, String> params = new HashMap<String, String>();
        params.put("date", date);
        params.put("time", gettime);
        params.put("user_id", userid);
        params.put("location", location);
        params.put("delivery_charges", shipping_charges);
        params.put("total_ammount",total_amount);
        params.put("payment_method", getvalue);
        params.put("data", passArray.toString());
        // Toast.makeText(getActivity(),""+passArray,Toast.LENGTH_LONG).show();
        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                Url.ADD_ORDER_URL, params, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {
                    Boolean status = response.getBoolean("responce");
                    if (status) {
                        JSONObject object = response.getJSONObject("data");
                        String msg=object.getString("msg");
                        String id=object.getString("order_id");
                        db_cart.clearCart();
                      ProgressDialog.dismiss();

                      Intent intent=new Intent(ActivityPayment.this,ActivityThank.class);
                      intent.putExtra("id",id);
                      intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                      startActivity(intent);
                      finish();

                        //      Toast.makeText(getActivity(),"success",Toast.LENGTH_LONG).show();
                    }
                    else

                    {
                      ProgressDialog.dismiss();
                        Toast.makeText(ActivityPayment.this,"Something went wrong",Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    ProgressDialog.dismiss();
                    Toast.makeText(ActivityPayment.this,""+e.getMessage() ,Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                ProgressDialog.dismiss();
                Module module=new Module(ActivityPayment.this);
                String msg=module.VolleyErrorMessage(error);
                Toast.makeText(ActivityPayment.this, ""+msg, Toast.LENGTH_SHORT).show();
            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }


}
