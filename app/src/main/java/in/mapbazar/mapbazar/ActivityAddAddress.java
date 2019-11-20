package in.mapbazar.mapbazar;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import in.mapbazar.mapbazar.Model.PinModel;
import in.mapbazar.mapbazar.Model.ShippinModel;
import in.mapbazar.mapbazar.R;

import in.mapbazar.mapbazar.Model.Address;


import in.mapbazar.mapbazar.Utili.Common;
import in.mapbazar.mapbazar.View.CustomEditTextView;
import in.mapbazar.mapbazar.View.CustomTextView;
import in.mapbazar.mapbazar.View.DialogUtils;
import in.mapbazar.mapbazar.callback.CallbackInternate;
import in.mapbazar.mapbazar.callback.CallbackMessage;
import in.mapbazar.mapbazar.connection.API;
import in.mapbazar.mapbazar.connection.RestAdapter;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityAddAddress extends AppCompatActivity implements View.OnClickListener {

    //Shared Preferences
    private SharedPreferences sPref;

    Dialog ProgressDialog;
    Spinner spinner_pin;

    @BindView(R.id.ly_back)
    RelativeLayout ly_back;

    @BindView(R.id.txt_title)
    CustomTextView txt_title;

    @BindView(R.id.edt_name)
    CustomEditTextView edt_name;

    @BindView(R.id.edt_address)
    CustomEditTextView edt_address;

    @BindView(R.id.edt_pincode)
    CustomEditTextView edt_pincode;

    @BindView(R.id.edt_city)
    CustomEditTextView edt_city;

    @BindView(R.id.edt_state)
    CustomEditTextView edt_state;

    @BindView(R.id.edt_phone)
    CustomEditTextView edt_phone;

    @BindView(R.id.txt_add_address)
    CustomTextView txt_add_address;

    boolean isEdit = false;
    String add_id = "";

    Address address;
    AutoCompleteTextView act_pincode;
  List<ShippinModel> modelList;
  List<PinModel> pinList;
    String[] sp=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);
        ButterKnife.bind(this);
        act_pincode=(AutoCompleteTextView)findViewById(R.id.act_pincode);
        spinner_pin=(Spinner)findViewById(R.id.spinner_pin);
        sPref = PreferenceManager.getDefaultSharedPreferences(ActivityAddAddress.this);

        initdata();
        getData();
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


    private void initdata() {
        edt_pincode.setRawInputType(InputType.TYPE_CLASS_NUMBER);
        edt_phone.setRawInputType(InputType.TYPE_CLASS_NUMBER);

        isEdit = sPref.getBoolean("isEdit", false);

        if (isEdit) {
            txt_title.setText(getString(R.string.edit_address));
            txt_add_address.setText("" + getString(R.string.edit_address1));
        } else {
            txt_title.setText(getString(R.string.add_address));
            txt_add_address.setText("" + getString(R.string.add_address1));
        }

        if (isEdit) {
            Intent intent = getIntent();
            address = (Address) intent.getSerializableExtra("Address");

            edt_name.setText(address.getName());
            edt_address.setText(address.getAddress());
            act_pincode.setText(address.getPincode());
            edt_city.setText(address.getCity());
            edt_state.setText(address.getState());
            edt_phone.setText(address.getMobile_no());
            add_id=address.getAddress_id();
        }


        ProgressDialog = new Dialog(ActivityAddAddress.this, android.R.style.Theme_Translucent_NoTitleBar);
        ProgressDialog.setContentView(R.layout.progressbar);
        ProgressDialog.setCancelable(false);
        //progressBar = (ProgressBar) ProgressDialog.findViewById(R.id.progress_circular);

        ly_back.setOnClickListener(this);
        txt_add_address.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.ly_back:

                SharedPreferences.Editor sh = sPref.edit();
                sh.putBoolean("Add", false);
                sh.commit();

                finish();
                break;

            case R.id.txt_add_address:

                /*Intent i = new Intent(ActivityAddAddress.this,RegisterActivity.class);
                SharedPreferences.Editor sh = sPref.edit();
                sh.putBoolean("Add",true);
                sh.commit();
                startActivity(i);*/

                CheckValidation();
                break;
        }
    }

    public void CheckValidation() {
        String sn=edt_phone.getText().toString();
        int a=Integer.parseInt(sn.substring(0,1));

       // Toast.makeText(ActivityAddAddress.this,""+a,Toast.LENGTH_LONG).show();
        if (edt_name.getText().toString().length() == 0) {
            edt_name.setError(getString(R.string.enter_name));
            edt_name.requestFocus();
        } else if (edt_address.getText().toString().equals("")) {
            edt_address.setError(getString(R.string.enter_address));
            edt_address.requestFocus();
        } else if (spinner_pin.getSelectedItem().toString().equals("Select any pin code")) {
            ErrorMessage("Please select any pin code");
           // act_pincode.requestFocus();
        } else if (edt_city.getText().toString().equals("")) {
            edt_city.setError(getString(R.string.enter_city));
            edt_city.requestFocus();
        } else if (edt_state.getText().toString().equals("")) {
            edt_state.setError(getString(R.string.enter_state));
            edt_state.requestFocus();
        } else if (edt_phone.getText().toString().equals("")) {
            edt_phone.setError(getString(R.string.enter_phone));
            edt_phone.requestFocus();
        }  else if (a<6) {
            edt_phone.requestFocus();
            edt_phone.setText("");
            ErrorMessage("Invalid Mobile number");

        } else if (isValidMobile(edt_phone.getText().toString())==false) {

            edt_phone.requestFocus();
            edt_phone.setText("");
            ErrorMessage("Invalid Mobile number");

        }
        else {
            //Toast.makeText(activity, "Success", Toast.LENGTH_SHORT).show();

            String name = edt_name.getText().toString().trim();
            String address = edt_address.getText().toString().trim();
            String pincode = spinner_pin.getSelectedItem().toString().trim();
            String city = edt_city.getText().toString().trim();
            String state = edt_state.getText().toString().trim();
            String phone = edt_phone.getText().toString().trim();


            if (isEdit) {
                if (Common.isNetworkAvailable(ActivityAddAddress.this)) {
                    requestEdtAddress(name, address, pincode, city, state, phone);
                } else {
                    Getretrive(name, address, pincode, city, state, phone);
                }
            } else {
                if (Common.isNetworkAvailable(ActivityAddAddress.this)) {
                    requestAddAddress(name, address, pincode, city, state, phone);
                } else {
                    Getretrive(name, address, pincode, city, state, phone);
                }
            }

        }
    }

    public void getData()
    {
        ProgressDialog.show();
        modelList=new ArrayList<>();
        pinList=new ArrayList<>();
        API api = RestAdapter.createAPI();
        Call<JsonObject> callback_login = api.get_pincode("");
        callback_login.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

              //  Toast.makeText(ActivityAddAddress.this,""+response,Toast.LENGTH_LONG).show();
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

                      sp =new String[data_arr.size()+1];
                        for(int k=1;k<=pinList.size();k++)
                        {
                            sp[0]="Select any pin code";
                            sp[k]=pinList.get(k-1).getPicode().toString();
                        }
                       //
                        // Toast.makeText(ActivityAddAddress.this,""+sp[0].toString(),Toast.LENGTH_LONG).show();
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(ActivityAddAddress.this, android.R.layout.simple_list_item_1, sp){
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

                    else
                    {
                        ProgressDialog.dismiss();
                ErrorMessage("Something Wrong");
                    }
           //     Toast.makeText(ActivityAddAddress.this,""+response.body(),Toast.LENGTH_LONG).show();
                }






            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                ProgressDialog.dismiss();
                Toast.makeText(ActivityAddAddress.this,""+t.getMessage(),Toast.LENGTH_LONG).show();

            }
        });
    }

    private void requestAddAddress(final String name, final String address, final String pincode, final String city, final String state, final String phone) {

        ProgressDialog.show();

        API api = RestAdapter.createAPI();
        Call<JsonObject> callback_login = api.add_address(sPref.getString("uid", ""), name, address, pincode, city, state, phone);
        callback_login.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.e("Login onResponse", "=>" + response.body());

                if (response.isSuccessful()) {
                    JsonObject jsonObject = response.body();

                    /*{"message":"address add successfully","status":"success","Isactive":"1"}*/

                    if (jsonObject != null && jsonObject.get("status").getAsString().equals("success")) {

                        if (ProgressDialog != null && ProgressDialog.isShowing())
                            ProgressDialog.dismiss();

                        SharedPreferences.Editor sh = sPref.edit();
                        sh.putBoolean("Add", true);
                        sh.commit();

                        finish();

                    } else {
                        if (ProgressDialog != null && ProgressDialog.isShowing())
                            ProgressDialog.dismiss();

                        int Isactive=0;
                        if (jsonObject.has("Isactive") && !jsonObject.get("Isactive").isJsonNull()) {
                            Isactive = jsonObject.get("Isactive").getAsInt();
                        }

                        if (Isactive == 0) {
                            Common.AccountLock(ActivityAddAddress.this);
                        }
                        else if (jsonObject.has("message") && !jsonObject.get("message").isJsonNull()) {
                            Common.Errordialog(ActivityAddAddress.this,jsonObject.get("message").toString());
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
                    Common.ShowHttpErrorMessage(ActivityAddAddress.this, message);
                }
            }
        });

    }

    private void requestEdtAddress(final String name, final String address, final String pincode, final String city, final String state, final String phone) {

        ProgressDialog.show();

        API api = RestAdapter.createAPI();
        Call<JsonObject> callback_login = api.edit_address(sPref.getString("uid", ""), name, address, pincode, city, state, phone, add_id);
        callback_login.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.e("Login onResponse", "=>" + response.body());

                if (response.isSuccessful()) {
                    JsonObject jsonObject = response.body();

                    if (jsonObject != null && jsonObject.get("status").getAsString().equals("success")) {


                        /*{"message":"address update successfully","status":"success","Isactive":"1"}*/

                        if (ProgressDialog != null && ProgressDialog.isShowing())
                            ProgressDialog.dismiss();


                        SharedPreferences.Editor sh = sPref.edit();
                        sh.putBoolean("Add", true);
                        sh.commit();

                        finish();

                    } else {
                        if (ProgressDialog != null && ProgressDialog.isShowing())
                            ProgressDialog.dismiss();

                        int Isactive=0;
                        if (jsonObject.has("Isactive") && !jsonObject.get("Isactive").isJsonNull()) {
                            Isactive = jsonObject.get("Isactive").getAsInt();
                        }

                        if (Isactive == 0) {
                            Common.AccountLock(ActivityAddAddress.this);
                    }
                        else if (jsonObject.has("message") && !jsonObject.get("message").isJsonNull()) {
                            Common.Errordialog(ActivityAddAddress.this,jsonObject.get("message").toString());
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
                    Common.ShowHttpErrorMessage(ActivityAddAddress.this, message);
                }
            }
        });

    }

    private void Getretrive(final String name, final String address, final String pincode, final String city, final String state, final String phone) {
        // Common.showInternetInfo(ActivitySignIn.this, "Network is not available");
        Dialog dialog = new DialogUtils(ActivityAddAddress.this).buildDialogInternate(new CallbackInternate() {
            @Override
            public void onSuccess(Dialog dialog) {
                dialog.dismiss();

                if (isEdit) {
                    if (Common.isNetworkAvailable(ActivityAddAddress.this)) {
                        requestEdtAddress(name, address, pincode, city, state, phone);
                    } else {
                        Getretrive(name, address, pincode, city, state, phone);
                    }
                } else {
                    if (Common.isNetworkAvailable(ActivityAddAddress.this)) {
                        requestAddAddress(name, address, pincode, city, state, phone);
                    } else {
                        Getretrive(name, address, pincode, city, state, phone);
                    }
                }
            }
        });
        dialog.show();
    }

    private void ErrorMessage(String Message) {
        // Common.showInternetInfo(ActivitySignIn.this, "Network is not available");
        Dialog dialog = new DialogUtils(ActivityAddAddress.this).buildDialogMessage(new CallbackMessage() {
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

    @Override
    public void onBackPressed() {

        ly_back.performClick();
    }



    public static boolean isValidMobile(String phone) {
        boolean check = false;
        if (!Pattern.matches("[a-zA-Z]+", phone)) {
            if (phone.length() < 9 || phone.length() > 13) {
                // if(phone.length() != 10) {
                check = false;
                // txtPhone.setError("Not Valid Number");
            } else {

                check = android.util.Patterns.PHONE.matcher(phone).matches();
            }
        } else {
            check = false;
        }
        return check;
    }

    public boolean isValidPin(String[] sp, String pin)
    {
        boolean check=false;
        int cnt=0;
        for(int i=0; i<sp.length;i++) {
            if (sp[i].equals(pin)) {
                  cnt++;
            }

        }
    //    Toast.makeText(ActivityAddAddress.this,""+cnt,Toast.LENGTH_LONG).show();

        if(cnt<=0)
        {
            check=false;
        }
        else
        {
            check=true;
        }
        return check;
    }
}
