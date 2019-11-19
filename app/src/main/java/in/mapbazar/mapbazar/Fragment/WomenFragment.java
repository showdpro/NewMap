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



        FirebaseApp.initializeApp(getActivity());
        mAuth = FirebaseAuth.getInstance();
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
                    if(mVerificationId != null)
                    {

                        //pb1.setVisibility(View.VISIBLE);
                        verifyCodeWithPhoneNumber();

                    }
                }


            }
        });

        mCallBacks=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

                String code=phoneAuthCredential.getSmsCode();


//                if(code !=null )
//                {
//                    etOtp.setText(code);
//                }
              //  singUpWithCredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(verificationId, forceResendingToken);
                rel_sendotp.setVisibility(View.GONE);
                rel_verifyotp.setVisibility(View.VISIBLE);

                mVerificationId=verificationId;
                //  btnSignUp.setText("Verify OTP");
            }
        };

        return view;
    }

    private void verifyCodeWithPhoneNumber() {
        PhoneAuthCredential credential=PhoneAuthProvider.getCredential(mVerificationId, edt_otp.getText().toString());
        singUpWithCredential(credential);
    }

    public void initdata() {


        userPref = PreferenceManager.getDefaultSharedPreferences(Common.Activity);


        ProgressDialog = new Dialog(getActivity(), android.R.style.Theme_Translucent_NoTitleBar);
        ProgressDialog.setContentView(R.layout.progressbar);
        ProgressDialog.setCancelable(false);

    }




    private void startPhoneNumberVerification() {

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91"+edt_mobile.getText().toString(),
                60,
                TimeUnit.SECONDS,
                getActivity(),
                mCallBacks);
        //edt_mobile.setEnabled(false);

    }


    private void singUpWithCredential(PhoneAuthCredential phoneAuthCredential) {
        ProgressDialog.show();

        FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful())
                {
                    ProgressDialog.dismiss();


                    number=edt_mobile.getText().toString().trim();
                    // Toast.makeText(PasswordActivity.this,"Verification is completed...",Toast.LENGTH_LONG).show();
                    ResetPasswordDialog resetPasswordDialog=new ResetPasswordDialog();
                    resetPasswordDialog.show(getFragmentManager(),"ResetPasswordDialog");
                    //userIsLoggedIn();
                }
                else
                {
                    String message="";
                    ProgressDialog.dismiss();
                    if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                        message = "Invalid OTP";

                    }
                  ErrorMessage(message);

                }
            }
        });
    }


    public void getPhoneNumberValidataion(String phone_number)
    {
        ProgressDialog.show();
        API api=RestAdapter.createAPI();
        Call<JsonObject> callback_homecategory = api.get_user_validation(phone_number);
        callback_homecategory.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                if(response.isSuccessful())
                {
                    ProgressDialog.dismiss();
                    JsonObject object=response.body();

                    String status=object.get("status").getAsString();

                    if(status.equals("false"))
                    {
                        ErrorMessage(object.get("message").getAsString());
                    }
                    else if(status.equals("true"))
                    {
                      startPhoneNumberVerification();
                    }

                }
                else
                {
                    ErrorMessage("Something Went Wrong");
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

                ProgressDialog.dismiss();
               t.printStackTrace();
               ErrorMessage("Server Not Responding..");
            }
        });
    }

    private void ErrorMessage(String Message) {
        // Common.showInternetInfo(ActivitySignIn.this, "Network is not available");
        Dialog dialog = new DialogUtils(getActivity()).buildDialogMessage(new CallbackMessage() {
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
}
