package in.mapbazar.mapbazar;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;

import in.mapbazar.mapbazar.Model.Address;
import in.mapbazar.mapbazar.Model.PinModel;
import in.mapbazar.mapbazar.Model.ProductListModel.ProductItem;

import in.mapbazar.mapbazar.Model.ShippinModel;
import in.mapbazar.mapbazar.Payment.Checksum;
import in.mapbazar.mapbazar.Payment.Paytm;
import in.mapbazar.mapbazar.Payment.WebServiceCaller;

import in.mapbazar.mapbazar.Utili.Common;
import in.mapbazar.mapbazar.View.CustomTextView;
import in.mapbazar.mapbazar.View.DialogUtils;
import in.mapbazar.mapbazar.callback.CallbackMessage;
import in.mapbazar.mapbazar.connection.API;
import in.mapbazar.mapbazar.connection.RestAdapter;
import in.mapbazar.mapbazar.dialog.SelectTimeDialogFragment;

import java.net.SocketTimeoutException;
import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityPayment extends AppCompatActivity {

    String m_id="";
    String channel_id="";
    String website="";
    String indus_type_id="";
    String call_url="";
    CustomTextView notify;

    double gms=0;
    double pcs=0;
   LinearLayout linear_pay;
   View line_pay,line1_pay;

    //Shared Preferences
    private SharedPreferences sPref;

    Dialog ProgressDialog;

    @BindView(R.id.ly_back)
    RelativeLayout ly_back;

    @BindView(R.id.lay_status_delivery)
    LinearLayout lay_status_delivery;

    @BindView(R.id.lay_status_payment)
    LinearLayout lay_status_payment;

    @BindView(R.id.txt_place_order)
    CustomTextView txt_place_order;

    @BindView(R.id.image_status)
    ImageView image_status;

    @BindView(R.id.image_payment)
    ImageView image_payment;

    @BindView(R.id.ll_date)
    RelativeLayout llDate;

    @BindView(R.id.ll_time)
    RelativeLayout llTime;

    @BindView(R.id.tv_date)
    CustomTextView tvDate;

    @BindView(R.id.tv_time)
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



    double del=0;
    JsonArray jsonArr=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        ButterKnife.bind(this);
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
        initdata();
        request_payment_status();

        pin=address.getPincode().toString();
        getData(pin);
        //setAmount();


        txt_place_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (image_payment.getVisibility() == View.VISIBLE) {

                  GetOnlineJsonObject();
                   // Common.Errordialog(ActivityPayment.this, "Feature not implemented ");

                }else  if (image_payment.getVisibility() == View.GONE && image_status.getVisibility() == View.GONE) {
                    Common.Errordialog(ActivityPayment.this, "Please select a payment method ");

                }  else if (Common.list_ProductItem != null && Common.list_ProductItem.size() > 0) {
                    if (tvTime.getText().toString().equalsIgnoreCase("No time slot for today")) {
                        Common.Errordialog(ActivityPayment.this, "Please select a time slot ");

                    } else {
                        GetJsonObject();
                    }
                }
            }
        });
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

    private void getDataShip()
    {

        JsonArray jsonArray = new JsonArray();


        for (int i = 0; i < Common.list_ProductItem.size(); i++) {

            ProductItem productItem = Common.list_ProductItem.get(i);

            JsonObject product = new JsonObject();

            product.addProperty("Font_type_1", "");
            product.addProperty("Font_type_2", "");
            product.addProperty("Font_type_3", "");
            product.addProperty("Font_type_4", "");
            product.addProperty("Font_type_5", "");
            product.addProperty("Font_type_6", "");
            product.addProperty("Font_type_7", "");
            product.addProperty("Font_type_8", "");
            product.addProperty("Text_1", "");
            product.addProperty("Text_2", "");
            product.addProperty("Text_3", "");
            product.addProperty("Text_4", "");
            product.addProperty("Text_5", "");
            product.addProperty("Text_6", "");
            product.addProperty("Text_7", "");
            product.addProperty("Text_8", "");
            product.addProperty("Text_comment", "");
            product.addProperty("Text_line_instruction", "");
            product.addProperty("Text_quantity", "");

            product.addProperty("back_Font_type_1", "");
            product.addProperty("back_Font_type_2", "");
            product.addProperty("back_Font_type_3", "");
            product.addProperty("back_Font_type_4", "");
            product.addProperty("back_Font_type_5", "");
            product.addProperty("back_Font_type_6", "");
            product.addProperty("back_Font_type_7", "");
            product.addProperty("back_Font_type_8", "");
            product.addProperty("back_Text_1", "");
            product.addProperty("back_Text_2", "");
            product.addProperty("back_Text_3", "");
            product.addProperty("back_Text_4", "");
            product.addProperty("back_Text_5", "");
            product.addProperty("back_Text_6", "");
            product.addProperty("back_Text_7", "");
            product.addProperty("back_Text_8", "");
            product.addProperty("back_Text_comment", "");
            product.addProperty("back_Text_line_instruction", "");
            product.addProperty("back_Text_quantity", "");
            product.addProperty("city", "");
            product.addProperty("comment", "");
            product.addProperty("image_2_image_path", "");
            product.addProperty("image_path", "");

            product.addProperty("color", productItem.getColorCode());
            product.addProperty("isFormSelected", productItem.getIsFormSelected());

            product.addProperty("product_id", productItem.getProductId());
            product.addProperty("quantity", productItem.getQuantity());
            product.addProperty("size_id", productItem.getSizeId());

            product.addProperty("price", productItem.getProductAttribute().get(0).getAttributeValue());
            String s=productItem.getProductAttribute().get(0).getAttributeName().toString();

            String[] st=getType(s);

            if(st[1].equals("k"))
            {
                del= del+Double.parseDouble(st[0])*Double.parseDouble(productItem.getQuantity());
            }
            else if(st[1].equals("p"))
            {
                pcs=pcs+Double.parseDouble(st[0])*Double.parseDouble(productItem.getQuantity());
            }
            else if(st[1].equals("g"))
            {
              //  double gm=Double.parseDouble(st[0])/1000;
                gms=gms+Double.parseDouble(st[0])*Double.parseDouble(productItem.getQuantity());
            }
            //double sd=getQtySum(productItem.getProductAttribute().get(0).getAttributeName());
            //sum_qty=sum_qty+sd;

            jsonArray.add(product);
        }



jsonArr=jsonArray;

         d=setShippinCharge();
        txt_shipping.setText(""+d);
         //tot= DeliveryShippingFragment.subtotal+(float) d;
        txt_qty.setText(""+Common.list_ProductItem.size());
       // txt_subtotal.setText(""+ DeliveryShippingFragment.subtotal);
        txt_total.setText(""+tot);
    }

    private void GetJsonObject() {
        JsonObject finalobject = new JsonObject();

     //   Toast.makeText(ActivityPayment.this,"del"+del+"\n ship"+d,Toast.LENGTH_LONG).show();
        finalobject.add("product_order_detail", jsonArr);

        finalobject.addProperty("user_id", "" + sPref.getString("uid", ""));
        finalobject.addProperty("user_name", sPref.getString("name", ""));
        finalobject.addProperty("user_email", sPref.getString("email", ""));
        finalobject.addProperty("address1", "" + address.getAddress());
        finalobject.addProperty("address2", "");
        finalobject.addProperty("address_name", "" + address.getName());
        finalobject.addProperty("city", "" + address.getCity());
        finalobject.addProperty("mobile", "" + address.getMobile_no());
        finalobject.addProperty("payment_type", "cash");
        finalobject.addProperty("pincode", "" + address.getPincode());
        finalobject.addProperty("state", "" + address.getState());
        finalobject.addProperty("tax", sPref.getString("TAX", "0"));
        finalobject.addProperty("tax_price", sPref.getString("tax_value", "0"));
        finalobject.addProperty("transaction_id", "");
        finalobject.addProperty("delivery_date", tvDate.getText().toString());
        finalobject.addProperty("delivery_time", tvTime.getText().toString());
        finalobject.addProperty("shipping_charge", d);
        finalobject.addProperty("quantity" ,sPref.getString("quantity",""));


        //Toast.makeText(ActivityPayment.this,""+address.getPincode().toString()+"\n"+address.getAddress(),Toast.LENGTH_LONG).show();
        Log.e("Product Object", "" + finalobject.toString());

       requestsubmitProductOrder(finalobject);
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

    private void requestsubmitProductOrder(JsonObject finalobject) {

        ProgressDialog.show();

        API api = RestAdapter.createAPI();
        Call<JsonObject> callback_login = api.checkout_ProductOrder(finalobject);
        callback_login.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.e("Submit Order", "=>" + response.body());

                if (response.isSuccessful()) {
                    JsonObject jsonObject = response.body();

                    if (jsonObject != null && jsonObject.get("status").getAsString().equals("success")) {

                        /*{"total_order":"11","status":"success","Isactive":"1"}*/

                        if (ProgressDialog != null && ProgressDialog.isShowing())
                            ProgressDialog.dismiss();

                      /*  {"status":"success","order_id":62,"Isactive":"1"}*/

                        int order_id = 0;
                        if (jsonObject.has("order_id") && !jsonObject.get("order_id").isJsonNull())
                            order_id = jsonObject.get("order_id").getAsInt();
                        else
                            order_id = 0;

                        SharedPreferences.Editor sh = sPref.edit();
                        sh.putInt("Cart", 0);
                        sh.putInt("order_id", order_id);
                        sh.putString("mobile", "" + address.getMobile_no());
                        sh.commit();

                        SuccessDailog(getString(R.string.place_order_successful));

                    } else {

                        if (ProgressDialog != null && ProgressDialog.isShowing())
                            ProgressDialog.dismiss();

                        int Isactive = 0;
                        if (jsonObject.has("Isactive") && !jsonObject.get("Isactive").isJsonNull()) {
                            Isactive = jsonObject.get("Isactive").getAsInt();
                        }

                        if (Isactive == 0) {
                            Common.AccountLock(ActivityPayment.this);
                        } else if (jsonObject.has("message") && !jsonObject.get("message").isJsonNull()) {
                            Common.Errordialog(ActivityPayment.this, jsonObject.get("message").toString());
                        }

                    }

                } else {

                    if (ProgressDialog != null && ProgressDialog.isShowing())
                        ProgressDialog.dismiss();

                    ErrorMessage(getString(R.string.error));
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

                if (ProgressDialog != null && ProgressDialog.isShowing())
                    ProgressDialog.dismiss();

                String message = "";
                if (t instanceof SocketTimeoutException) {
                    message = "Socket Time out. Please try again.";
                    Common.ShowHttpErrorMessage(ActivityPayment.this, message);
                } else {
                    ErrorMessage(getString(R.string.error));
                }
            }
        });

    }


    private void GetOnlineJsonObject() {
        JsonObject finalobject = new JsonObject();

        //   Toast.makeText(ActivityPayment.this,"del"+del+"\n ship"+d,Toast.LENGTH_LONG).show();
        finalobject.add("product_order_detail", jsonArr);

        finalobject.addProperty("user_id", "" + sPref.getString("uid", ""));
        finalobject.addProperty("user_name", sPref.getString("name", ""));
        finalobject.addProperty("user_email", sPref.getString("email", ""));
        finalobject.addProperty("address1", "" + address.getAddress());
        finalobject.addProperty("address2", "");
        finalobject.addProperty("address_name", "" + address.getName());
        finalobject.addProperty("city", "" + address.getCity());
        finalobject.addProperty("mobile", "" + address.getMobile_no());
        finalobject.addProperty("payment_type", "online");
        finalobject.addProperty("pincode", "" + address.getPincode());
        finalobject.addProperty("state", "" + address.getState());
        finalobject.addProperty("tax", sPref.getString("TAX", "0"));
        finalobject.addProperty("tax_price", sPref.getString("tax_value", "0"));
        finalobject.addProperty("transaction_id", "");
        finalobject.addProperty("delivery_date", tvDate.getText().toString());
        finalobject.addProperty("delivery_time", tvTime.getText().toString());
        finalobject.addProperty("shipping_charge", d);
        finalobject.addProperty("quantity" ,sPref.getString("quantity",""));


        //Toast.makeText(ActivityPayment.this,""+address.getPincode().toString()+"\n"+address.getAddress(),Toast.LENGTH_LONG).show();
        Log.e("Product Object", "" + finalobject.toString());

        requestonlinesubmitProductOrder(finalobject);
    }

    private void requestonlinesubmitProductOrder(JsonObject finalobject) {

        ProgressDialog.show();

        API api = RestAdapter.createAPI();
        Call<JsonObject> callback_login = api.checkout_ProductOrder(finalobject);
        callback_login.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.e("Submit Order", "=>" + response.body());

                if (response.isSuccessful()) {
                    JsonObject jsonObject = response.body();

                    if (jsonObject != null && jsonObject.get("status").getAsString().equals("success")) {

                        /*{"total_order":"11","status":"success","Isactive":"1"}*/

                        if (ProgressDialog != null && ProgressDialog.isShowing())
                            ProgressDialog.dismiss();

                        /*  {"status":"success","order_id":62,"Isactive":"1"}*/

                        int order_id = 0;
                        if (jsonObject.has("order_id") && !jsonObject.get("order_id").isJsonNull()) {
                            order_id = jsonObject.get("order_id").getAsInt();
                            processPaytm(String.valueOf(order_id), String.valueOf(tot));
                        }
                        else
                            order_id = 0;

                        SharedPreferences.Editor sh = sPref.edit();
                        sh.putInt("Cart", 0);
                        sh.putInt("order_id", order_id);
                        sh.putString("mobile", "" + address.getMobile_no());
                        sh.commit();

                        SuccessDailog(getString(R.string.place_order_successful));

                    } else {

                        if (ProgressDialog != null && ProgressDialog.isShowing())
                            ProgressDialog.dismiss();

                        int Isactive = 0;
                        if (jsonObject.has("Isactive") && !jsonObject.get("Isactive").isJsonNull()) {
                            Isactive = jsonObject.get("Isactive").getAsInt();
                        }

                        if (Isactive == 0) {
                            Common.AccountLock(ActivityPayment.this);
                        } else if (jsonObject.has("message") && !jsonObject.get("message").isJsonNull()) {
                            Common.Errordialog(ActivityPayment.this, jsonObject.get("message").toString());
                        }

                    }

                } else {

                    if (ProgressDialog != null && ProgressDialog.isShowing())
                        ProgressDialog.dismiss();

                    ErrorMessage(getString(R.string.error));
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

                if (ProgressDialog != null && ProgressDialog.isShowing())
                    ProgressDialog.dismiss();

                String message = "";
                if (t instanceof SocketTimeoutException) {
                    message = "Socket Time out. Please try again.";
                    Common.ShowHttpErrorMessage(ActivityPayment.this, message);
                } else {
                    ErrorMessage(getString(R.string.error));
                }
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


    public void getData(String pin)
    {
        ProgressDialog.show();

        pinList=new ArrayList<>();
        API api = RestAdapter.createAPI();
        Call<JsonObject> callback_login = api.get_pincode(pin);
        callback_login.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                if(response.isSuccessful())
                {
                    JsonObject jsonObject=response.body();

                    if(jsonObject !=null )
                    {
                        if (ProgressDialog != null && ProgressDialog.isShowing())
                            ProgressDialog.dismiss();

                        JsonArray data_arr=jsonObject.getAsJsonArray("pincodedetail");

                        for(int i=0; i<data_arr.size();i++) {
                            JsonObject object = data_arr.get(i).getAsJsonObject();
                            PinModel pinModel = new PinModel();


                            pinModel.setPicode(object.get("pincode").getAsString());
                            pinModel.setArea(object.get("area").getAsString());
                            String str_array = object.get("shipping_amt").getAsString();
                            String s = str_array.substring(0, (str_array.length() - 1));
                            JsonParser parser = new JsonParser();
                            JsonElement tradeElement = parser.parse(str_array);
                            JsonArray array = tradeElement.getAsJsonArray();
                            //Toast.makeText(ActivityAddAddress.this, "asdas \n"+array.get(0).toString(), Toast.LENGTH_LONG).show();
                            //JsonArray array=object.getAsJsonArray("shipping_amt");

                            for(int j=0; j<array.size(); j++)
                            {
                                JsonObject obj=array.get(j).getAsJsonObject();
                                //Toast.makeText(ActivityAddAddress.this,""+obj.get("max_quantity").getAsString(),Toast.LENGTH_LONG).show();
                                ShippinModel model=new ShippinModel();
                                model.setMin_quantity(obj.get("min_quantity").getAsString());
                                model.setMax_quantity(obj.get("max_quantity").getAsString());
                                model.setShipping_charges(obj.get("shipping_charges").getAsString());

                                modelList.add(model);

                            }
                            pinModel.setShippinModelList(modelList);

                            pinList.add(pinModel);

                        }

                        getDataShip();

                        //Toast.makeText(ActivityPayment.this,""+sp[0].toString(),Toast.LENGTH_LONG).show();
                        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(ActivityPayment.this, android.R.layout.simple_list_item_1, sp);
                       // act_pincode.setAdapter(adapter);
                        // List<ShippinModel> models=pinList.get(0).getShippinModelList();
                        //Toast.makeText(ActivityAddAddress.this,"asdasd"+pinList.get(0).getShippinModelList().get(0).getShipping_charges().toString(),Toast.LENGTH_LONG).show();

                    }
                }

                else
                {
                    ProgressDialog.dismiss();
                    ErrorMessage("Something Wrong");
                }
                //     Toast.makeText(ActivityAddAddress.this,""+response.body(),Toast.LENGTH_LONG).show();
            }






            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(ActivityPayment.this,""+t.getMessage(),Toast.LENGTH_LONG).show();

            }
        });
    }


    public double getQtySum(String qty)
    {
        double qt=0;
        int q=Integer.parseInt(qty);
        if(q>=100)
        {
           qt=Double.parseDouble("."+q);
        }
        else
        {
            qt=q;
        }

        return qt;
    }



    public String[] getType(String atr)
    {
        String[] str=new String[2];
        double atr_value=0;
        String s="";
        if(atr.contains("kg") || atr.contains("Kg"))
        {
            atr_value=Double.parseDouble(atr.substring(0,(atr.length()-2)));
            s="k";
        }
        else if(atr.contains("gm") || atr.contains("Gm") || atr.contains("gms") || atr.contains("Gms")|| atr.contains("g") || atr.contains("G"))
        {
            if(atr.contains("gms") || atr.contains("Gms"))
            {
                atr_value=Double.parseDouble(atr.substring(0,(atr.length()-3)));
                s="g";
            }
            else if(atr.contains("gm") || atr.contains("Gm"))
            {
                atr_value=Double.parseDouble(atr.substring(0,(atr.length()-2)));
                s="g";
            }
            else if(atr.contains("g") || atr.contains("G"))
            {
                atr_value=Double.parseDouble(atr.substring(0,(atr.length()-1)));
                s="g";
            }
        }
        else if(atr.contains("Piece") || atr.contains("Pieces") || atr.contains("piece") || atr.contains("pieces") || atr.contains("pic")|| atr.contains("Pic")|| atr.contains("pcs")|| atr.contains("Pcs"))
        {
            if(atr.contains("Pieces") || atr.contains("pieces"))
            {
                atr_value=Double.parseDouble(atr.substring(0,(atr.length()-6)));
                s="p";
            }
            else if(atr.contains("Piece") || atr.contains("piece"))
            {
                atr_value=Double.parseDouble(atr.substring(0,(atr.length()-5)));
                s="p";
            }
            else if(atr.contains("Pic") || atr.contains("pic"))
            {
                atr_value=Double.parseDouble(atr.substring(0,(atr.length()-3)));
                s="p";
            }
            else if(atr.contains("pcs") || atr.contains("Pcs"))
            {
                atr_value=Double.parseDouble(atr.substring(0,(atr.length()-3)));
                s="p";
            }
            else
            {
                atr_value=Double.parseDouble(atr.substring(0,(atr.length()-3)));
                s="p";
            }

        }
        else if(atr.contains("Pack") || atr.contains("pack"))
        {
            atr_value=Double.parseDouble(atr.substring(0,(atr.length()-4)));
            s="p";
        }


        str[0]=String.valueOf(atr_value);
        str[1]=s;
        return str;
    }

    public double setShippinCharge()
    {

      double max_price= 0;
        double ship_amt=0;
        double dec=gms/1000;
        double min=0;
        double max=0;
            del=del+dec;

        if( max_price>=500)
        {
            ship_amt=0;
        }
        else
        {
          if(del==0)
          {
              if(pcs<=2)
              {
                  ship_amt=10;
              }
              else if(pcs>=2 && pcs<=5)
              {
                  ship_amt=20;
              }
              else if(pcs>5)
              {
                  ship_amt=20;
              }
          }
          else
          {
              ship_amt = Double.parseDouble(modelList.get(0).getShipping_charges());
              //double ship_amt = 0;
              for(int i=0;i<modelList.size();i++)
              {
                  min=Double.parseDouble(modelList.get(i).getMin_quantity());
                  max=Double.parseDouble(modelList.get(i).getMax_quantity());

                  if(del>=min && del<=max) {
                      ship_amt = Double.parseDouble(modelList.get(i).getShipping_charges());
                      break;
                  }

              }

          }




        }

        return ship_amt;
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
    }
