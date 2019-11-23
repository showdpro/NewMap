package in.mapbazar.mapbazar;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.squareup.picasso.Picasso;

import in.mapbazar.mapbazar.Fragment.DeliveryFragment;
import in.mapbazar.mapbazar.Fragment.DeliveryShippingFragment;
import in.mapbazar.mapbazar.Model.Address;
import in.mapbazar.mapbazar.Model.PinModel;
import in.mapbazar.mapbazar.Model.RecentlyProduct.RecentlyProductItem;

import in.mapbazar.mapbazar.Model.ShippinModel;
import in.mapbazar.mapbazar.Modules.Module;
import in.mapbazar.mapbazar.Utili.Common;
import in.mapbazar.mapbazar.Utili.TouchImageView;
import in.mapbazar.mapbazar.Utili.Url;
import in.mapbazar.mapbazar.Utili.Utils;
import com.viewpagerindicator.CirclePageIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.mapbazar.mapbazar.Model.RecentlyProduct.ProductImageItem;
import in.mapbazar.mapbazar.View.CustomEditTextView;
import in.mapbazar.mapbazar.View.CustomTextView;
import in.mapbazar.mapbazar.View.DialogUtils;
import in.mapbazar.mapbazar.callback.CallbackInternate;
import in.mapbazar.mapbazar.callback.CallbackMessage;
import in.mapbazar.mapbazar.connection.API;
import in.mapbazar.mapbazar.connection.RestAdapter;
import in.mapbazar.mapbazar.util.ConnectivityReceiver;
import in.mapbazar.mapbazar.util.CustomVolleyJsonRequest;
import in.mapbazar.mapbazar.util.Session_management;
import retrofit2.Call;
import retrofit2.Callback;

public class GuestLoginActivity extends AppCompatActivity implements View.OnClickListener{

    Dialog ProgressDialog;
    List<ShippinModel> modelList;
    Spinner spinner_pin;
    List<PinModel> pinList;
    private CustomEditTextView et_phone, et_name, et_address,edt_city,edt_state;
    private RelativeLayout btn_update;
    private CustomTextView tv_phone, tv_name, tv_pin, tv_address, tv_socity, btn_socity;
    private String getsocity = "";

    private Session_management sessionManagement;

    private boolean isEdit = false;

    private String getlocation_id;
    private String [] pincodes ={"202002"};
    String[] sp=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_login);
        sessionManagement = new Session_management(GuestLoginActivity.this);
        spinner_pin=(Spinner)findViewById(R.id.spinner_pin);
        et_phone = (CustomEditTextView) findViewById(R.id.et_add_adres_phone);
        et_name = (CustomEditTextView) findViewById(R.id.et_add_adres_name);
        tv_phone = (CustomTextView) findViewById(R.id.tv_add_adres_phone);
        tv_name = (CustomTextView) findViewById(R.id.tv_add_adres_name);
        tv_pin = (CustomTextView) findViewById(R.id.tv_add_adres_pin);
        edt_city = (CustomEditTextView) findViewById(R.id.edt_city);
        edt_state = (CustomEditTextView) findViewById(R.id.edt_state);
        //  et_pin = (AutoCompleteTextView) view.findViewById(R.id.et_add_adres_pin);
        et_address = (CustomEditTextView) findViewById(R.id.et_add_adres_home);
        tv_address =(CustomTextView)findViewById( R.id.tv_add );
        //tv_socity = (TextView) view.findViewById(R.id.tv_add_adres_socity);
        btn_update = (RelativeLayout) findViewById(R.id.btn_add_adres_edit);
        //  btn_socity = (TextView) view.findViewById(R.id.btn_add_adres_socity);
        ProgressDialog = new Dialog(GuestLoginActivity.this, android.R.style.Theme_Translucent_NoTitleBar);
        ProgressDialog.setContentView(R.layout.progressbar);
        ProgressDialog.setCancelable(false);
        String getsocity_name = sessionManagement.getUserDetails().get(Url.KEY_SOCITY_NAME);
        //String getsocity_id = sessionManagement.getUserDetails().get(BaseURL.KEY_SOCITY_ID);
        String getsocity_id = "1";

        String mobile = getIntent().getStringExtra( "mobile" );
        et_phone.setText( mobile );
        et_phone.setEnabled( false );


        btn_update.setOnClickListener(this);
        // btn_socity.setOnClickListener(this);
        getPinCodes();
        spinner_pin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedItemText = (String) adapterView.getItemAtPosition(i);
                // If user change the default selection
                // First item is disable and it is used for hint
                if(i > 0){
                    // Notify the selected item text

                    edt_city.setText("Jhansi");
                    edt_state.setText("UP");
                    edt_city.setEnabled(false);
                    edt_state.setEnabled(false);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.btn_add_adres_edit) {
            attemptEditProfile();
        } /*else if (id == R.id.btn_add_adres_socity) {

            /*String getpincode = et_pin.getText().toString();

            if (!TextUtils.isEmpty(getpincode)) {*/

//                Bundle args = new Bundle();
//                Fragment fm = new Socity_fragment();
//                //args.putString("pincode", getpincode);
//                fm.setArguments(args);
//                FragmentManager fragmentManager = getFragmentManager();
//                fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
//                        .addToBackStack(null).commit();
            /*} else {
                Toast.makeText(getActivity(), getResources().getString(R.string.please_enter_pincode), Toast.LENGTH_SHORT).show();
            }*/


    }

    private void attemptEditProfile() {

        tv_phone.setText(getResources().getString(R.string.receiver_mobile_number));
        tv_pin.setText(getResources().getString(R.string.tv_reg_pincode));
        tv_name.setText(getResources().getString(R.string.receiver_name_req));
        tv_address.setText("Address");
        //  tv_socity.setText(getResources().getString(R.string.tv_reg_socity));

        tv_name.setTextColor(getResources().getColor(R.color.dark_gray));
        tv_phone.setTextColor(getResources().getColor(R.color.dark_gray));
        tv_pin.setTextColor(getResources().getColor(R.color.dark_gray));
        tv_address.setTextColor(getResources().getColor(R.color.dark_gray));
        // tv_socity.setTextColor(getResources().getColor(R.color.dark_gray));

        String getphone = et_phone.getText().toString();
        String getname = et_name.getText().toString();
        String getadd = et_address.getText().toString();
        // String getsocity = sessionManagement.getUserDetails().get(BaseURL.KEY_SOCITY_ID);
        String getsocity = "1";

        boolean cancel = false;
        View focusView = null;

        List<String> list = Arrays.asList(pincodes);
        if (TextUtils.isEmpty(getphone)) {
            tv_phone.setTextColor(getResources().getColor(R.color.colorPrimary));
            focusView = et_phone;
            cancel = true;
        } else if (!isPhoneValid(getphone)) {
            tv_phone.setText(getResources().getString(R.string.phone_too_short));
            tv_phone.setTextColor(getResources().getColor(R.color.colorPrimary));
            focusView = et_phone;
            cancel = true;
        }

        if (TextUtils.isEmpty(getname)) {
            tv_name.setTextColor(getResources().getColor(R.color.colorPrimary));
            focusView = et_name;
            cancel = true;
        }



        if (TextUtils.isEmpty(getadd)) {
            tv_address.setTextColor(getResources().getColor(R.color.colorPrimary));
            focusView = et_address;
            cancel = true;
        }

        /*if (TextUtils.isEmpty(getsocity) && getsocity == null) {
            tv_socity.setTextColor(getResources().getColor(R.color.colorPrimary));
            focusView = btn_socity;
            cancel = true;
        }*/

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            if (focusView != null)
                focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.

            if (ConnectivityReceiver.isConnected()) {



                // check internet connection
                String getpin=spinner_pin.getSelectedItem().toString();

                if (ConnectivityReceiver.isConnected()) {

                        try
                        { makeAddAddressRequest( getpin, getsocity,getadd, getname, getphone);
                        }
                        catch (Exception ex){
                            Toast.makeText(GuestLoginActivity.this,""+ex.getMessage(),Toast.LENGTH_LONG).show();
                        }


                }
            }

        }


    }

    private boolean isPhoneValid(String phoneno) {
        //TODO: Replace this with your own logic
        return phoneno.length() > 9;
    }

    /**
     * Method to make json object request where json response starts wtih
     */
    private void makeAddAddressRequest(final String pincode, String socity_id,
                                       final String address, final String receiver_name, final String receiver_mobile) {
        ProgressDialog.show();
        // Tag used to cancel the request
        String tag_json_obj = "json_add_address_req";
     //   Toast.makeText(GuestLoginActivity.this," pin :- "+pincode+"\n add:- "+address+"\n re_name:- "+receiver_name+"\n mob :- "+receiver_mobile,Toast.LENGTH_LONG).show();
        Map<String, String> params = new HashMap<String, String>();
        params.put("pincode", pincode);
        params.put("socity_id", "11");
        params.put("house_no", address);
        params.put("user_name", receiver_name);
        params.put("user_mobile", receiver_mobile);

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                Url.GUEST_LOGIN, params, new com.android.volley.Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("address", response.toString());

                try {
                    ProgressDialog.dismiss();
                    Boolean status = response.getBoolean("responce");
                    if (status) {

                        String location_id=response.getString("message");
                        String user_id=response.getString("user_id");
                        sessionManagement.setGuestUser(user_id);
                        Intent i = new Intent( GuestLoginActivity.this, ActivityMatchProductDetails.class);
                        i.putExtra("flag","s");
                        i.putExtra("address",address);
                        i.putExtra("location_id",location_id);
                        i.putExtra("name",receiver_name);
                        i.putExtra("pin",pincode);
                        i.putExtra("phone",receiver_mobile);
                        i.putExtra("user_id",user_id);
                        startActivity(i);
                    }
                    else
                    {
                        String msg=response.getString("error");
                        Toast.makeText(GuestLoginActivity.this,""+msg,Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    ProgressDialog.dismiss();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                ProgressDialog.dismiss();
                Module module=new Module(GuestLoginActivity.this);
                String msg=module.VolleyErrorMessage(error);
                Toast.makeText(GuestLoginActivity.this, "er1"+msg, Toast.LENGTH_SHORT).show();
//                VolleyLog.d(TAG, "Error: " + error.getMessage());
//                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
//                    Toast.makeText(getActivity(), getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
//                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }

    public void getPinCodes()
    {
        ProgressDialog.show();
        modelList=new ArrayList<>();
        pinList=new ArrayList<>();
        HashMap<String,String> map=new HashMap<>();
        String json_tag="json_pincode";

        CustomVolleyJsonRequest customVolleyJsonRequest=new CustomVolleyJsonRequest(Request.Method.POST, Url.GET_PINCODES, map, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try
                {
                    ProgressDialog.dismiss();
                    JSONArray s_array=response.getJSONArray("pincodedetail");
                    for(int i=0; i<s_array.length();i++) {
                        JSONObject object = s_array.getJSONObject(i);
                        PinModel pinModel = new PinModel();


                        pinModel.setPicode(object.getString("pincode"));
                        pinModel.setArea(object.getString("area"));
                        String str_array = object.getString("shipping_amt");
                        String s = str_array.substring(0, (str_array.length() - 1));
                        JSONArray array = new JSONArray(str_array);
                        //Toast.makeText(ActivityAddAddress.this, "asdas \n"+array.get(0).toString(), Toast.LENGTH_LONG).show();
                        //JsonArray array=object.getAsJsonArray("shipping_amt");

                        for(int j=0; j<array.length(); j++)
                        {
                            JSONObject obj=array.getJSONObject(j);
                            //Toast.makeText(ActivityAddAddress.this,""+obj.get("max_quantity").getAsString(),Toast.LENGTH_LONG).show();
                            ShippinModel model=new ShippinModel();
                            model.setMin_quantity(obj.getString("min_quantity"));
                            model.setMax_quantity(obj.getString("max_quantity"));
                            model.setShipping_charges(obj.getString("shipping_charges"));

                            modelList.add(model);

                        }
                        pinModel.setShippinModelList(modelList);

                        pinList.add(pinModel);

                        sp =new String[s_array.length()+1];
                        for(int k=1;k<=pinList.size();k++)
                        {
                            sp[0]="Select any pin code";
                            sp[k]=pinList.get(k-1).getPicode().toString();
                        }
                        //
                        // Toast.makeText(ActivityAddAddress.this,""+sp[0].toString(),Toast.LENGTH_LONG).show();
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(GuestLoginActivity.this, android.R.layout.simple_list_item_1, sp){
                            @Override
                            public boolean isEnabled(int position) {

                                if(position==0)
                                {
                                    return false;
                                }
                                else {
                                    return true;
                                }
                            }

                            @Override
                            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                                View view = super.getDropDownView(position, convertView, parent);
                                TextView tv = (TextView) view;
                                if(position == 0){
                                    // Set the hint text color gray
                                    tv.setTextColor(Color.GRAY);
                                }
                                else {
                                    tv.setTextColor(Color.GRAY);
                                }
                                return view;
                            }
                        };
                        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                        spinner_pin.setAdapter(adapter);
                        //  act_pincode.setAdapter(adapter);
                        // List<ShippinModel> models=pinList.get(0).getShippinModelList();
                        //Toast.makeText(ActivityAddAddress.this,"asdasd"+pinList.get(0).getShippinModelList().get(0).getShipping_charges().toString(),Toast.LENGTH_LONG).show();



                    }
                }
                catch (Exception ex)
                {
                    ProgressDialog.dismiss();
                    Toast.makeText(GuestLoginActivity.this,""+ex.getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                ProgressDialog.dismiss();
                Module module=new Module(GuestLoginActivity.this);
                String msg=module.VolleyErrorMessage(error);
                Toast.makeText(GuestLoginActivity.this,""+msg,Toast.LENGTH_LONG).show();

            }
        });

        AppController.getInstance().addToRequestQueue(customVolleyJsonRequest,json_tag);
    }

}
