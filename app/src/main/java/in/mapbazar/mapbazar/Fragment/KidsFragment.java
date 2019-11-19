package in.mapbazar.mapbazar.Fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import in.mapbazar.mapbazar.Utili.Common;
import in.mapbazar.mapbazar.R;


public class KidsFragment extends Fragment {


    SharedPreferences userPref;
String code="";
String mVerificationId="";
String mob_no="";


   Button btn_otp_verify;
   EditText et_otp1,et_otp2,et_otp3,et_otp4,et_otp5,et_otp6;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.kids_fregment, container, false);

        btn_otp_verify=(Button)view.findViewById(R.id.btn_otp_verify);
        et_otp1=(EditText)view.findViewById(R.id.et_otp1);
        et_otp2=(EditText)view.findViewById(R.id.et_otp2);
        et_otp3=(EditText)view.findViewById(R.id.et_otp3);
        et_otp4=(EditText)view.findViewById(R.id.et_otp4);
        et_otp5=(EditText)view.findViewById(R.id.et_otp5);
        et_otp6=(EditText)view.findViewById(R.id.et_otp6);

        initdata();

        btn_otp_verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                verifyCodeWithPhoneNumber();
                //Toast.makeText(getActivity(),""+mVerificationId,Toast.LENGTH_LONG).show();
            }
        });
        return view;
    }

    public void initdata() {
     userPref = PreferenceManager.getDefaultSharedPreferences(Common.Activity);
    code=getArguments().getString("code");
    mob_no=getArguments().getString("mobile");

    if(code==null || code.isEmpty())
    {


    }
    else
    {
        for(int i=0; i<code.length();i++)
        {
            et_otp1.setText(code.substring(0));
            et_otp2.setText(code.substring(1));
            et_otp3.setText(code.substring(2));
            et_otp4.setText(code.substring(3));
            et_otp5.setText(code.substring(4));
            et_otp6.setText(code.substring(5));
           // Toast.makeText(getActivity(),""+code.substring(3).toString(),Toast.LENGTH_LONG).show();
        }
    }
        mVerificationId=getArguments().getString("mVerificationId");


    }

    private void verifyCodeWithPhoneNumber()
    {

        String otp=allOtp();
        PhoneAuthCredential credential= PhoneAuthProvider.getCredential(mVerificationId,otp );
        String match_code=credential.getSmsCode().toString().trim();

//     //Toast.makeText(getActivity(),""+match_code,Toast.LENGTH_LONG).show();

        String m_code=getSMSCode(match_code);
        Toast.makeText(getActivity(),""+match_code+"\n "+m_code,Toast.LENGTH_LONG).show();
        if(m_code.equals(code))
        {
            Generate_passwordFragment fragment=new Generate_passwordFragment();
            FragmentManager privacyPolicyFragmentManager = getFragmentManager();
            Bundle bundle=new Bundle();
            bundle.putString("mobile",mob_no);
            fragment.setArguments(bundle);

            privacyPolicyFragmentManager.beginTransaction()
                    .replace(R.id.verify_container, fragment)
                    .addToBackStack(null)
                    .commit();

        }
        else
        {
            Toast.makeText(getActivity(),"Invalid code entered...",Toast.LENGTH_LONG).show();
        }

//        singUpWithCredential(credential);
    }

    private String getSMSCode(String match_code) {

        String s1="";
        String s2="";
        String s3="";
        String s4="";
        String s5="";
        String s6="";
        String sms_cd="";
        String[] str=new String[match_code.length()];
        str=match_code.split("");


        s1=str[0].toString().trim();
        s2=str[1].toString().trim();
        s3=str[2].toString().trim();
        s4=str[3].toString().trim();
        //s1=str[4].toString().trim();
        s5=str[5].toString().trim();
        s6=str[6].toString().trim();


         sms_cd=s1+s2+s3+s4+s5+s6;
        return sms_cd;
    }

    private void singUpWithCredential(PhoneAuthCredential credential) {

        FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful())
                {
                    Generate_passwordFragment fragment=new Generate_passwordFragment();
                    FragmentManager privacyPolicyFragmentManager = getFragmentManager();
                    // kidsFragment.setArguments(bundle);
                    privacyPolicyFragmentManager.beginTransaction()
                            .replace(R.id.verify_container, fragment)
                            .addToBackStack(null)
                            .commit();
                    //Toast.makeText(getActivity(),"success",Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(getActivity(),"Invalid code entered...",Toast.LENGTH_LONG).show();
                }

            }
        });
    }


    public String allOtp()
    {
        String otp1=et_otp1.getText().toString().trim();
        String otp2=et_otp2.getText().toString().trim();
        String otp3=et_otp3.getText().toString().trim();
        String otp4=et_otp4.getText().toString().trim();
        String otp5=et_otp5.getText().toString().trim();
        String otp6=et_otp6.getText().toString().trim();

        String all=otp1+otp2+otp3+otp4+otp4+otp5+otp6;
        return all;
    }

}
