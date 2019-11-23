package in.mapbazar.mapbazar;


import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import in.mapbazar.mapbazar.Modules.Module;
import in.mapbazar.mapbazar.Utili.Common;
import in.mapbazar.mapbazar.Utili.Url;
import in.mapbazar.mapbazar.Utili.Utils;
import in.mapbazar.mapbazar.View.CustomEditTextView;
import in.mapbazar.mapbazar.View.CustomTextView;
import in.mapbazar.mapbazar.util.CustomVolleyJsonRequest;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    CustomEditTextView edtUser,edtMobile,edtPassword,edtconfirmPassword;
    Switch switch_driver_status;
    CustomTextView txtregister;
    Dialog ProgressDialog;
    private boolean isPasswordShow, isConfirmPasswordShow;

    ImageView imgConfirmpass,imgPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initComponents();
    }

    private void initComponents() {

        ProgressDialog = new Dialog(RegisterActivity.this, android.R.style.Theme_Translucent_NoTitleBar);
        ProgressDialog.setContentView(R.layout.progressbar);
        ProgressDialog.setCancelable(false);

        edtUser=(CustomEditTextView)findViewById(R.id.edtUser);
        edtMobile=(CustomEditTextView)findViewById(R.id.edtMobile);
        edtPassword=(CustomEditTextView)findViewById(R.id.edtPassword);
        edtconfirmPassword=(CustomEditTextView)findViewById(R.id.edtconfirmPassword);

        switch_driver_status=(Switch)findViewById(R.id.switch_driver_status);
        String phone_number = getIntent().getStringExtra( "mobile" );
        edtMobile.setText( phone_number );
        edtMobile.setEnabled(false);

        txtregister=(CustomTextView)findViewById(R.id.txtregister);
        imgConfirmpass=(ImageView) findViewById(R.id.imgConfirmpass);
        imgPassword=(ImageView) findViewById(R.id.imgPassword);
        imgConfirmpass.setOnClickListener(this);
        imgPassword.setOnClickListener(this);
        txtregister.setOnClickListener(this);





    }

    @Override
    public void onClick(View view) {

        int id=view.getId();

        if(id==R.id.txtregister)
        {
          attemptRegiater();
        }
        else if(id == R.id.imgConfirmpass)
        {
            if (isConfirmPasswordShow) {

                isConfirmPasswordShow = false;

                int resID = getResources().getIdentifier("@drawable/hide_password", "drawable", getPackageName());
                imgConfirmpass.setImageResource(resID);

                edtconfirmPassword.setTransformationMethod(new PasswordTransformationMethod());
                edtconfirmPassword.setSelection(edtconfirmPassword.getText().length());

            } else {

                isConfirmPasswordShow = true;

                int resID = getResources().getIdentifier("@drawable/show_password", "drawable", getPackageName());
                imgConfirmpass.setImageResource(resID);

                edtconfirmPassword.setTransformationMethod(null);
                edtconfirmPassword.setSelection(edtconfirmPassword.getText().length());

            }
        }
        else if(id == R.id.imgPassword)
        {
            if (isPasswordShow) {

                isPasswordShow = false;

                int resID = getResources().getIdentifier("@drawable/hide_password", "drawable",getPackageName());
                imgPassword.setImageResource(resID);

                edtPassword.setTransformationMethod(new PasswordTransformationMethod());
                edtPassword.setSelection(edtPassword.getText().length());

            } else {

                isPasswordShow = true;

                int resID = getResources().getIdentifier("@drawable/show_password", "drawable",getPackageName());
                imgPassword.setImageResource(resID);

                edtPassword.setTransformationMethod(null);
                edtPassword.setSelection(edtPassword.getText().length());

            }
        }

    }

    private void attemptRegiater() {

        if (edtUser.getText().toString().length() == 0) {
            edtUser.setError(getString(R.string.enter_username));
            edtUser.requestFocus();
        }
        else if (edtMobile.getText().toString().equals("")) {
            edtMobile.setError("please Enter Mobile no.");
            edtMobile.requestFocus();
        }
//            else if (edtEmail.getText().toString().equals("")) {
//                edtEmail.setError(getActivity().getString(R.string.enter_email));
//                edtEmail.requestFocus();
//            }
//        else if (!Utils.isValidEmail(edtEmail.getText().toString().trim())) {
//                edtEmail.setError(getActivity().getString(R.string.enter_valid_email));
//                edtEmail.requestFocus();
        //    }
        else if (edtPassword.getText().toString().length() == 0) {
            edtPassword.setError(getString(R.string.enter_password));
            edtPassword.requestFocus();
        } else if (edtPassword.getText().toString().length() < 6 || edtPassword.getText().toString().length() > 32) {
            edtPassword.setError(getResources().getString(R.string.password_length));
            edtPassword.requestFocus();
        } else if (!Utils.isValidPass(edtPassword.getText().toString().trim())) {
            edtPassword.setError(getResources().getString(R.string.password_valid));
            edtPassword.requestFocus();
        } else if (edtconfirmPassword.getText().toString().equals("")) {
            edtconfirmPassword.setError(getString(R.string.enter_cnfpassword));
            edtconfirmPassword.requestFocus();
        } else if (!edtconfirmPassword.getText().toString().equals(edtPassword.getText().toString())) {
            edtconfirmPassword.setError(getString(R.string.enter_pass_notmatch));
            edtconfirmPassword.requestFocus();
        } else {
            //Toast.makeText(activity, "Success", Toast.LENGTH_SHORT).show();

            String username = edtUser.getText().toString().trim();
            String phone_no = edtMobile.getText().toString().trim();
            // String email = edtEmail.getText().toString().trim();
            String password = edtPassword.getText().toString().trim();

            makeRegisterRequest(username,phone_no,"",password);

        }

    }

    private void makeRegisterRequest(String name, String mobile,
                                     String email, String password) {
        ProgressDialog.show();
        // Tag used to cancel the request
        String tag_json_obj = "json_register_req";

        Map<String, String> params = new HashMap<String, String>();
        params.put("user_name", name);
        params.put("user_mobile", mobile);
        params.put("user_email", email);
        params.put("password", password);

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                Url.REGISTER_URL, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("register", response.toString());


                try {
                    ProgressDialog.dismiss();
                    Boolean status = response.getBoolean("responce");
                    if (status) {

                        String msg = response.getString("message");
                        Toast.makeText(RegisterActivity.this, "" + msg, Toast.LENGTH_SHORT).show();

                        Intent i = new Intent(RegisterActivity.this, ActivityLoginRegister.class);
                        startActivity(i);
                        finish();
                        txtregister.setEnabled(false);

                    } else {
                        String error = response.getString("error");


                        txtregister.setEnabled(true);

                        if(error.equals("The Mobile Number field must contain a unique value."))
                        {
                            Toast.makeText(RegisterActivity.this, "Mobile number already exist.", Toast.LENGTH_SHORT).show();
                        }
                        else if(error.equals("The User Email field must contain a unique value."))
                        {
                            Toast.makeText(RegisterActivity.this, "Email address already exist.", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(RegisterActivity.this, "Mobile number already exist.\nEmail address already exist.", Toast.LENGTH_SHORT).show();
                        }

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
                Module module=new Module(RegisterActivity.this);
                String msg=module.VolleyErrorMessage(error);
                Toast.makeText(RegisterActivity.this, ""+msg, Toast.LENGTH_SHORT).show();
//                VolleyLog.d(TAG, "Error: " + error.getMessage());
//                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
//                    Toast.makeText(RegisterActivity.this, getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
//                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }

}