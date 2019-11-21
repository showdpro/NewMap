package in.mapbazar.mapbazar.Fragment;

import android.app.DatePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.mapbazar.mapbazar.Adapter.Delivery_get_address_adapter;
import in.mapbazar.mapbazar.AppController;
import in.mapbazar.mapbazar.Model.Delivery_address_model;
import in.mapbazar.mapbazar.Modules.Module;
import in.mapbazar.mapbazar.R;
import in.mapbazar.mapbazar.Utili.Url;

import in.mapbazar.mapbazar.util.ConnectivityReceiver;
import in.mapbazar.mapbazar.util.CustomVolleyJsonRequest;
import in.mapbazar.mapbazar.util.DatabaseCartHandler;
import in.mapbazar.mapbazar.util.Session_management;
import in.mapbazar.mapbazar.util.SharedPref;

import static android.content.Context.MODE_PRIVATE;

public class DeliveryFragment extends Fragment implements View.OnClickListener {

    private static String TAG = DeliveryFragment.class.getSimpleName();

    private TextView tv_afternoon, tv_morning, tv_total, tv_item, tv_socity;
    private TextView tv_date, tv_time;
    private EditText et_address;
    private RelativeLayout btn_checkout, tv_add_adress;
    private RecyclerView rv_address;

    private Delivery_get_address_adapter adapter;
    private List<Delivery_address_model> delivery_address_modelList = new ArrayList<>();

    private DatabaseCartHandler db_cart;
    SharedPreferences preferences;
    private Session_management sessionManagement;

    private int mYear, mMonth, mDay, mHour, mMinute;

    private String gettime = "";
    private String getdate = "";

    private String deli_charges;
    String store_id;
    String language;
    public DeliveryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.men_fragment, container, false);

        //((MainActivity) getActivity()).setTitle(getResources().getString(R.string.delivery));

        store_id = SharedPref.getString(getActivity(), Url.STORE_ID);
        preferences = getActivity().getSharedPreferences("lan", MODE_PRIVATE);

        tv_date = (TextView) view.findViewById(R.id.tv_deli_date);
        tv_time = (TextView) view.findViewById(R.id.tv_deli_fromtime);
        tv_add_adress = (RelativeLayout) view.findViewById(R.id.tv_deli_add_address);
        tv_total = (TextView) view.findViewById(R.id.tv_deli_total);
        tv_item = (TextView) view.findViewById(R.id.tv_deli_item);
        btn_checkout = (RelativeLayout) view.findViewById(R.id.btn_deli_checkout);
        rv_address = (RecyclerView) view.findViewById(R.id.rv_deli_address);
        rv_address.setLayoutManager(new LinearLayoutManager(getActivity()));
        //tv_socity = (TextView) view.findViewById(R.id.tv_deli_socity);
        //et_address = (EditText) view.findViewById(R.id.et_deli_address);

        db_cart = new DatabaseCartHandler(getActivity());
        tv_total.setText(getResources().getString(R.string.currency)+db_cart.getTotalAmount());
        tv_item.setText("" + db_cart.getCartCount());

        // get session user data
        sessionManagement = new Session_management(getActivity());
        String getsocity = sessionManagement.getUserDetails().get(Url.KEY_SOCITY_NAME);
        String getaddress = sessionManagement.getUserDetails().get(Url.KEY_HOUSE);

        //tv_socity.setText("Socity Name: " + getsocity);
        //et_address.setText(getaddress);

        tv_date.setOnClickListener(this);
        tv_time.setOnClickListener(this);
        tv_add_adress.setOnClickListener(this);
        btn_checkout.setOnClickListener(this);

        String date = sessionManagement.getdatetime().get(Url.KEY_DATE);
        String time = sessionManagement.getdatetime().get(Url.KEY_TIME);



        if (date != null && time != null) {

            getdate = date;
            gettime = time;

            try {
                String inputPattern = "yyyy-MM-dd";
                String outputPattern = "dd-MM-yyyy";
                SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
                SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

                Date date1 = inputFormat.parse(getdate);
                String str = outputFormat.format(date1);

                tv_date.setText(getResources().getString(R.string.delivery_date) + str);

            } catch (ParseException e) {
                e.printStackTrace();

                tv_date.setText(getResources().getString(R.string.delivery_date) + getdate);
            }
            language=preferences.getString("language","");
            if (language.contains("spanish")) {
                String timeset=time;
                timeset=timeset.replace("PM","م");
                timeset=timeset.replace("AM","ص");
                tv_time.setText(timeset);

            }
            else {

                tv_time.setText(time);
            }
        }


        if (ConnectivityReceiver.isConnected()) {
            String user_id = sessionManagement.getUserDetails().get(Url.KEY_ID);
            makeGetAddressRequest(user_id);
        } else {
            //((MainActivity) getActivity()).onNetworkConnectionChanged(false);
        }

        return view;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.btn_deli_checkout) {
            attemptOrder();
        } else if (id == R.id.tv_deli_date) {
            getdate();
        } else if (id == R.id.tv_deli_fromtime) {

            if (TextUtils.isEmpty(getdate)) {
                Toast.makeText(getActivity(), getResources().getString(R.string.please_select_date), Toast.LENGTH_SHORT).show();
            } else {
//                Bundle args = new Bundle();
//                android.app.Fragment fm = new View_time_fragment();
//                args.putString("date", getdate);
//                fm.setArguments(args);
//                FragmentManager fragmentManager = getFragmentManager();
//                fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
//                        .addToBackStack(null).commit();
            }
        } else if (id == R.id.tv_deli_add_address) {

            sessionManagement.updateSocity("", "");

            Add_delivery_address_fragment add_delivery_address_fragment = new Add_delivery_address_fragment();
            FragmentManager fragmentTransaction = getActivity().getSupportFragmentManager();
            fragmentTransaction.beginTransaction().replace(R.id.container_delivery, add_delivery_address_fragment).commit();

        }

    }

    private void getdate() {
        // Get Current Date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                R.style.datepicker,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        getdate = "" + year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;

                        tv_date.setText(getResources().getString(R.string.delivery_date) + getdate);

                        try {
                            String inputPattern = "yyyy-MM-dd";
                            String outputPattern = "dd-MM-yyyy";
                            SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
                            SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

                            Date date = inputFormat.parse(getdate);
                            String str = outputFormat.format(date);

                            tv_date.setText(getResources().getString(R.string.delivery_date) + str);
                        } catch (ParseException e) {
                            e.printStackTrace();
                            tv_date.setText(getResources().getString(R.string.delivery_date) + getdate);
                        }

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();

    }

    private void attemptOrder() {

        //String getaddress = et_address.getText().toString();

        String location_id = "";
        String address = "";

        boolean cancel = false;

//        if (TextUtils.isEmpty(getdate)) {
//            Toast.makeText(getActivity(), getResources().getString(R.string.please_select_date_time), Toast.LENGTH_SHORT).show();
//            cancel = true;
//        } else if (TextUtils.isEmpty(gettime)) {
//            Toast.makeText(getActivity(), getResources().getString(R.string.please_select_date_time), Toast.LENGTH_SHORT).show();
//            cancel = true;
//        }

        if (!delivery_address_modelList.isEmpty()) {
            if (adapter.ischeckd()) {
                location_id = adapter.getlocation_id();
                address =adapter.getaddress();
                HashMap<String,String> addmap = adapter.getAlladdress();
                String name = addmap.get( "name" );
                String phone = addmap.get("phone");
                String society = addmap.get("society");
                String pin = addmap.get("pin");
                String house = addmap.get("house");
            } else {
                Toast.makeText(getActivity(), getResources().getString(R.string.please_select_address), Toast.LENGTH_SHORT).show();
                cancel = true;
            }
        } else {
            Toast.makeText(getActivity(), getResources().getString(R.string.please_add_address), Toast.LENGTH_SHORT).show();
            cancel = true;
        }

        /*if (TextUtils.isEmpty(getaddress)) {
            Toast.makeText(getActivity(), "Please add your address", Toast.LENGTH_SHORT).show();
            cancel = true;
        }*/

        if (!cancel) {
            //Toast.makeText(getActivity(), "date:"+getdate+"Fromtime:"+getfrom_time+"Todate:"+getto_time, Toast.LENGTH_SHORT).show();

            sessionManagement.cleardatetime();
//
            Bundle args = new Bundle();
            DeliveryShippingFragment fm = new DeliveryShippingFragment();
            HashMap<String,String> addmap = adapter.getAlladdress();
            String name = addmap.get( "name" );
            String phone = addmap.get("phone");
            String society = addmap.get("society");
            String pin = addmap.get("pin");
            String house = addmap.get("house");
            //args.putString("getdate", getdate);
            //args.putString("time", gettime);
            args.putString( "address",address );
//            args.putString("getdate", "00/00/0000");
//            args.putString("time", "00:00");
            args.putString("location_id", location_id);
            args.putString("name",name);
            args.putString( "pin",pin );
            args.putString( "house",house );
            args.putString( "society",society );
            args.putString( "phone",phone );

            args.putString("deli_charges", deli_charges);
            args.putString("store_id", store_id);
            fm.setArguments(args);
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
                    .addToBackStack(null).commit();

        }
    }

    /**
     * Method to make json object request where json response starts wtih
     */
    private void makeGetAddressRequest(String user_id) {

        // Tag used to cancel the request
        String tag_json_obj = "json_get_address_req";

        Map<String, String> params = new HashMap<String, String>();
        params.put("user_id", user_id);

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                Url.GET_ADDRESS_URL, params, new com.android.volley.Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("address", response.toString());

                try {
                    Boolean status = response.getBoolean("responce");
                    if (status) {

                        delivery_address_modelList.clear();

                        Gson gson = new Gson();
                        Type listType = new TypeToken<List<Delivery_address_model>>() {
                        }.getType();

                        delivery_address_modelList = gson.fromJson(response.getString("data"), listType);

                        //RecyclerView.Adapter adapter1 = new Delivery_get_address_adapter(delivery_address_modelList);
                        adapter = new Delivery_get_address_adapter(delivery_address_modelList);
                        //   ((Delivery_get_address_adapter) adapter).setMode(Attributes.Mode.Single);
                        rv_address.setAdapter(adapter);
                        adapter.notifyDataSetChanged();

                        if (delivery_address_modelList.isEmpty()) {
                            if (getActivity() != null) {
                                //Toast.makeText(getActivity(), getResources().getString(R.string.no_rcord_found), Toast.LENGTH_SHORT).show();
                            }
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                Module module=new Module(getActivity());
                String msg=module.VolleyErrorMessage(error);
                Toast.makeText(getActivity(), ""+msg, Toast.LENGTH_SHORT).show();
//                VolleyLog.d(TAG, "Error: " + error.getMessage());
//                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
//                    if (getActivity() != null) {
//                        Toast.makeText(getActivity(), getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
//                    }
//                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }

    @Override
    public void onPause() {
        super.onPause();
        // unregister reciver
        getActivity().unregisterReceiver(mCart);
    }

    @Override
    public void onResume() {
        super.onResume();
        // register reciver
        getActivity().registerReceiver(mCart, new IntentFilter("Grocery_delivery_charge"));
    }

    // broadcast reciver for receive data
    private BroadcastReceiver mCart = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String type = intent.getStringExtra("type");

            if (type.contentEquals("update")) {
                //updateData();

                //Toast.makeText(getActivity(), deli_charges, Toast.LENGTH_SHORT).show();

                float tot=Float.parseFloat(db_cart.getTotalAmount());
                int dt_chargs=0;
                if(tot>250)
                {
                    deli_charges = "0";
                }
                else
                {
                    deli_charges = intent.getStringExtra("charge");
                }
                Double total = Double.parseDouble(db_cart.getTotalAmount()) +Integer.parseInt(deli_charges);



                tv_total.setText(getResources().getString(R.string.currency) + db_cart.getTotalAmount() + " + "+getResources().getString(R.string.currency) + deli_charges + " = "+getResources().getString(R.string.currency)  + total+ getActivity().getResources().getString(R.string.currency));
            }
        }
    };

}
