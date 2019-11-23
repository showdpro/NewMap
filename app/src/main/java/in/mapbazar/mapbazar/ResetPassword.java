package in.mapbazar.mapbazar;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import in.mapbazar.mapbazar.Modules.Module;
import in.mapbazar.mapbazar.Utili.Url;
import in.mapbazar.mapbazar.View.CustomEditTextView;
import in.mapbazar.mapbazar.util.CustomVolleyJsonRequest;

import static com.android.volley.VolleyLog.TAG;

public class ResetPassword extends AppCompatActivity {
    CustomEditTextView et_pass , et_cpass ;
   RelativeLayout reset ;
Dialog ProgressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_reset_password );
        ProgressDialog = new Dialog(ResetPassword.this, android.R.style.Theme_Translucent_NoTitleBar);
        ProgressDialog.setContentView(R.layout.progressbar);
        ProgressDialog.setCancelable(false);

        final String mobile = getIntent().getStringExtra( "mobile" );
        et_pass = findViewById( R.id.et_password );
        et_cpass=findViewById( R.id.et_con_pass );
        reset = findViewById( R.id.btn_reset );

        reset.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pass = et_pass.getText().toString();
                String cpass = et_cpass.getText().toString();

                if (pass.isEmpty())
                {
                    et_pass.setError( "Enter Password" );
                    et_pass.requestFocus();
                }
                else if (cpass.isEmpty())
                {
                    et_cpass.setError( "Enter comfirm password" );
                    et_cpass.requestFocus();
                }
                else if (pass.length()<6)
                {
                    et_pass.setError( "password too short" );
                    et_pass.requestFocus();
                }
                else if (cpass.length()<6)
                {
                    et_cpass.setError( "password too short");
                    et_cpass.requestFocus();
                }

                else
                {
                    if (pass.equals( cpass ))
                    {
                        update_password( mobile,pass );
                    }
                     else
                {
                    et_cpass.setError( "password dont match" );
                    et_cpass.requestFocus();
                    Toast.makeText( ResetPassword.this,"passwords dont match",Toast.LENGTH_LONG ).show();
                }

                }
            }
        } );

    }

    private void update_password(String number, String pass) {
        ProgressDialog.show();
        String json_tag="json_reset_tag";
        HashMap<String,String> map=new HashMap<>();
        map.put("mobile",number);
        map.put("password",pass);

        CustomVolleyJsonRequest customVolleyJsonRequest=new CustomVolleyJsonRequest( Request.Method.POST, Url.FORGOT_URL, map, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
               ProgressDialog.dismiss();
                try {
                    Boolean status = response.getBoolean("responce");
                    if (status) {
                        String error = response.getString("error");

                        Toast.makeText(ResetPassword.this, "" + error, Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(ResetPassword.this,ActivityLoginRegister.class);
                        startActivity(intent);
                        finish();
                    } else {
                        String error = response.getString("error");

                        Toast.makeText(ResetPassword.this, "" + error, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ProgressDialog.dismiss();
                String errormsg = Module.VolleyErrorMessage(error);
                Toast.makeText( ResetPassword.this,""+ errormsg,Toast.LENGTH_LONG ).show();
            }
        });

        AppController.getInstance().addToRequestQueue(customVolleyJsonRequest,json_tag);

    }

}
