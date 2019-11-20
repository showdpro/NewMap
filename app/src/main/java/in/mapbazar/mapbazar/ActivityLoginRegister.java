package in.mapbazar.mapbazar;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import in.mapbazar.mapbazar.Modules.Module;
import in.mapbazar.mapbazar.Utili.Url;
import in.mapbazar.mapbazar.View.CustomEditTextView;
import in.mapbazar.mapbazar.View.CustomTextView;
import in.mapbazar.mapbazar.util.CustomVolleyJsonRequest;
import in.mapbazar.mapbazar.util.Session_management;

public class ActivityLoginRegister extends AppCompatActivity implements View.OnClickListener {

    Dialog ProgressDialog;
  CustomEditTextView edtMobile_login,edtPassword_login;
  CustomTextView txtLogin,txtRegister,txtGuest;
  ImageView imgPassword;

    private boolean isConfirmPasswordShow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_register);

          initData();
    }

    private void initData() {

        ProgressDialog = new Dialog(ActivityLoginRegister.this, android.R.style.Theme_Translucent_NoTitleBar);
        ProgressDialog.setContentView(R.layout.progressbar);
        ProgressDialog.setCancelable(false);

        edtMobile_login=(CustomEditTextView)findViewById(R.id.edtMobile_login);
        edtPassword_login=(CustomEditTextView)findViewById(R.id.edtPassword_login);

        txtLogin=(CustomTextView)findViewById(R.id.txtLogin);
        txtRegister=(CustomTextView)findViewById(R.id.txtRegister);
        txtGuest=(CustomTextView)findViewById(R.id.txtGuest);
        imgPassword=(ImageView)findViewById(R.id.imgPassword);

        txtLogin.setOnClickListener(this);
        txtRegister.setOnClickListener(this);
        txtGuest.setOnClickListener(this);


    }


    @Override
    public void onClick(View view) {

        int id=view.getId();
        if(id == R.id.txtLogin)
        {
            attemptLogin();
        }
        else if(id == R.id.txtRegister)
        {

        }
        else if(id == R.id.txtGuest)
        {

        }
        else if (id == R.id.imgPassword) {
            if (isConfirmPasswordShow) {

                isConfirmPasswordShow = false;

                int resID = getResources().getIdentifier("@drawable/hide_password", "drawable",getPackageName());
                imgPassword.setImageResource(resID);

                edtPassword_login.setTransformationMethod(new PasswordTransformationMethod());
                edtPassword_login.setSelection(edtPassword_login.getText().length());

            } else {

                isConfirmPasswordShow = true;

                int resID = getResources().getIdentifier("@drawable/show_password", "drawable",getPackageName());
                imgPassword.setImageResource(resID);

                edtPassword_login.setTransformationMethod(null);
                edtPassword_login.setSelection(edtPassword_login.getText().length());

            }
        }
    }

    private void attemptLogin() {

        String mob_no=edtMobile_login.getText().toString();

        if(mob_no.isEmpty() || mob_no.equals(null))
        {
            edtMobile_login.setError("Enter Mobile Number");
            edtMobile_login.requestFocus();

        }
       else if (edtPassword_login.getText().toString().length() == 0) {
            edtPassword_login.setError("Enter Password");
            edtPassword_login.requestFocus();
//        } else if (edtPassword_login.getText().toString().length() < 6 || edtPassword_login.getText().toString().length() > 32) {
//            edtPassword_login.setError("Password length must be between 6-32 characters");
//            edtPassword_login.requestFocus();
//        } else if (!isValidPass(edtPassword_login.getText().toString().trim())) {
//            edtPassword_login.setError("Please enter password with letter & numbers");
//            edtPassword_login.requestFocus();
//        }
        }
       else
        {
            String pass=edtPassword_login.getText().toString();
            getLogin(mob_no,pass);
        }


    }

    private void getLogin(String mob_no, final String pass) {
        ProgressDialog.show();
        String tag_json_obj = "json_login_req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("user_phone", mob_no);
        params.put("password", pass);

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                Url.LOGIN_URL, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("login", response.toString());
               ProgressDialog.dismiss();
                try {
                    Boolean status = response.getBoolean("responce");
                    if (status) {
                        JSONObject obj = response.getJSONObject("data");
                        String user_id = obj.getString("user_id");
                        String user_fullname = obj.getString("user_fullname");
                        String user_email = obj.getString("user_email");
                        String user_phone = obj.getString("user_phone");
                        String user_image = obj.getString("user_image");
                        String wallet_ammount = obj.getString("wallet");
                        String reward_points = obj.getString("rewards");
                        Session_management sessionManagement = new Session_management(ActivityLoginRegister.this);
                        sessionManagement.createLoginSession(user_id, user_email, user_fullname, user_phone, user_image, wallet_ammount, reward_points, "", "", "", "", pass);
                        Intent i = new Intent(ActivityLoginRegister.this, MainActivity.class);
                        startActivity(i);
                        finish();

                        txtLogin.setEnabled(false);

                    } else {
                        String error = response.getString("error");
                        txtLogin.setEnabled(true);

                        Toast.makeText(ActivityLoginRegister.this, "" + error, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                ProgressDialog.dismiss();
                Module module=new Module(ActivityLoginRegister.this);
                String errormsg = module.VolleyErrorMessage(error);
                Toast.makeText( ActivityLoginRegister.this,""+ errormsg,Toast.LENGTH_LONG ).show();
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }



    public boolean isValidPass(String s) {
        String n = ".*[0-9].*";
        String a = ".*[a-zA-Z].*";
        return s.matches(n) && s.matches(a);
    }

}
