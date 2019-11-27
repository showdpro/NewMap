package in.mapbazar.mapbazar;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import in.mapbazar.mapbazar.Modules.Module;
import in.mapbazar.mapbazar.Utili.Url;
import in.mapbazar.mapbazar.util.CustomVolleyJsonRequest;

public class MobileVerify extends AppCompatActivity {

EditText et_phone ;
Button btn_continue ;
TextView back ;
Dialog ProgressDialog ;
    int number;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_mobile_verify );
        et_phone = findViewById( R.id.et_phone );
        back = findViewById( R.id.txt_back );
        final String type = getIntent().getStringExtra( "type" );

        ProgressDialog = new Dialog(MobileVerify.this, android.R.style.Theme_Translucent_NoTitleBar);
        ProgressDialog.setContentView(R.layout.progressbar);
        ProgressDialog.setCancelable(false);

        back.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        } );

        btn_continue= findViewById( R.id.btn_continue );
                btn_continue.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String phone_number = et_phone.getText().toString();



                if (phone_number.isEmpty())
                {
                    et_phone.setError(" please enter phone number");
                    et_phone.requestFocus();
                }
                else if (phone_number.length()<10)
                {
                    et_phone.setError(" invalid phone number, enter 10 digit no.");
                    et_phone.requestFocus();
                }
                else if (phone_number.startsWith( "+" ))
                {
                    et_phone.setError(" invalid phone number");
                    et_phone.requestFocus();
                }
                else if (phone_number.startsWith("0"))
                {
                    et_phone.setError(" invalid phone number");
                    et_phone.requestFocus();
                }
                else
                {
                    int first = phone_number.charAt( 0 );
                    if (first <6)
                    {
                        et_phone.setError(" invalid phone number");
                        et_phone.requestFocus();
                    }

                    else {
                        Random rnd = new Random();
                        number = 100000 + rnd.nextInt( 900000 );
                        //      Toast.makeText( MobileVerify.this,"" +number,Toast.LENGTH_LONG).show();
                        if (type.equalsIgnoreCase( "f" )) {
                            sendOtp( phone_number, String.valueOf( number ) );

                        } else if (type.equalsIgnoreCase( "g" )) {
                            guestOtp( phone_number, String.valueOf( number ) );
                        } else if (type.equalsIgnoreCase( "r" )) {
                            otpRegister( phone_number, String.valueOf( number ) );
                        }

                    }
                }

            }
        } );



    }
    private void sendOtp(final String phone_number, String otp) {
        ProgressDialog.show();
        String tag_json_obj = "json_otp_req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("mobile",phone_number);
        params.put("otp", otp);

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest( Request.Method.POST,
                Url.URL_SEND_OTP, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("otp_forgot", response.toString());
                ProgressDialog.dismiss();
                try {
                    Boolean status = response.getBoolean("responce");
                    if (status) {
//                      Toast.makeText(ForgotActivity.this, "" + error, Toast.LENGTH_SHORT).show();
                       Intent i = new Intent(MobileVerify.this, OtpVerification.class);
                       i.putExtra( "type","f" );
                        i.putExtra( "mobile",phone_number );
                        i.putExtra( "otp",String.valueOf( number ));
                       startActivity(i);
                       finish();

                    } else {
                        String error = response.getString("error");

                        Toast.makeText(MobileVerify.this, "" + error, Toast.LENGTH_SHORT).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                ProgressDialog.dismiss();
                Module module=new Module(MobileVerify.this);
                String errormsg = module.VolleyErrorMessage(error);
                Toast.makeText( MobileVerify.this,""+ errormsg,Toast.LENGTH_LONG ).show();
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }

    private void otpRegister(final String phone_number, String otp) {
        ProgressDialog.show();
        String tag_json_obj = "json_otp_register";
        Map<String, String> params = new HashMap<String, String>();
        params.put("mobile",phone_number);
        params.put("otp", otp);

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest( Request.Method.POST,
                Url.URL_REG_OTP, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("otp_reg", response.toString());
                ProgressDialog.dismiss();
                try {
                    Boolean status = response.getBoolean("responce");
                    if (status) {
//                      Toast.makeText(ForgotActivity.this, "" + error, Toast.LENGTH_SHORT).show();
                       Intent i = new Intent(MobileVerify.this, OtpVerification.class);
                        i.putExtra( "type","r" );
                        i.putExtra( "mobile",phone_number );
                        i.putExtra( "otp",String.valueOf( number ) );
                       startActivity(i);
                       finish();

                    } else {
                        String error = response.getString("error");

                        Toast.makeText(MobileVerify.this, "" + error, Toast.LENGTH_SHORT).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                ProgressDialog.dismiss();
                Module module=new Module(MobileVerify.this);
                String errormsg = module.VolleyErrorMessage(error);
                Toast.makeText( MobileVerify.this,""+ errormsg,Toast.LENGTH_LONG ).show();
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }

    private void guestOtp(final String phone_number, String otp) {
        ProgressDialog.show();
        String tag_json_obj = "json_otp_register";
        Map<String, String> params = new HashMap<String, String>();
        params.put("mobile",phone_number);
        params.put("otp", otp);

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest( Request.Method.POST,
                Url.URL_GUEST_OTP, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("guest_otp", response.toString());
                ProgressDialog.dismiss();
                try {
                    Boolean status = response.getBoolean("responce");
                    if (status) {
//                      Toast.makeText(ForgotActivity.this, "" + error, Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(MobileVerify.this, OtpVerification.class);
                        i.putExtra( "type","g" );
                        i.putExtra( "mobile",phone_number );
                        i.putExtra( "otp",String.valueOf( number ));
                        startActivity(i);
                        finish();

                    } else {
                        String error = response.getString("error");

                        Toast.makeText(MobileVerify.this, "" + error, Toast.LENGTH_SHORT).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                ProgressDialog.dismiss();
                Module module=new Module(MobileVerify.this);
                String errormsg = module.VolleyErrorMessage(error);
                Toast.makeText( MobileVerify.this,""+ errormsg,Toast.LENGTH_LONG ).show();
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }

}
