package in.mapbazar.mapbazar.Fragment;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.gson.JsonObject;

import in.mapbazar.mapbazar.Utili.Common;
import in.mapbazar.mapbazar.Utili.ResetPasswordDialog;
import in.mapbazar.mapbazar.View.DialogUtils;
import in.mapbazar.mapbazar.callback.CallbackMessage;
import in.mapbazar.mapbazar.connection.API;
import in.mapbazar.mapbazar.R;
import in.mapbazar.mapbazar.connection.RestAdapter;

import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;



public class WomenFragment extends Fragment {

    SharedPreferences userPref;

    Dialog ProgressDialog;
    Button btn_send_otp,btn_verify_otp;
    String code="";
   public static String number="";
    EditText edt_mobile,edt_otp;
    private FirebaseAuth mAuth;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBacks;
    RelativeLayout rel_sendotp ,rel_verifyotp ;
    String mVerificationId;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.women_fregment, container, false);
         btn_send_otp=(Button)view.findViewById(R.id.btn_send_otp);
         btn_verify_otp=(Button)view.findViewById(R.id.btn_verify_otp);
         edt_mobile=(EditText)view.findViewById(R.id.edt_mobile);
         edt_otp=(EditText)view.findViewById(R.id.edt_otp);

         rel_sendotp=view.findViewById(R.id.sendotp);
        rel_verifyotp=view.findViewById(R.id.rel_verify);




        initdata();


        btn_send_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               // startPhoneNumberVerification();
//                String phone_number=edt_mobile.getText().toString().trim();
//
//                if(phone_number.isEmpty())
//                {
//                    edt_mobile.setText("Enter Mobile Number");
//                    edt_mobile.requestFocus();
//                }
//                else
//                {
//                    getPhoneNumberValidataion(phone_number);
//                }


            }
        });


        btn_verify_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String otp_code=edt_otp.getText().toString().trim();

                if(otp_code.isEmpty())
                {
                    edt_otp.setError("Enter OTP");
                    edt_otp.requestFocus();
                }
                else
                {
                }


            }
        });

        return view;
    }



    public void initdata() {


        userPref = PreferenceManager.getDefaultSharedPreferences(Common.Activity);


        ProgressDialog = new Dialog(getActivity(), android.R.style.Theme_Translucent_NoTitleBar);
        ProgressDialog.setContentView(R.layout.progressbar);
        ProgressDialog.setCancelable(false);

    }





}
