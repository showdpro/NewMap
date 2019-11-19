package in.mapbazar.mapbazar.Utili;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.JsonObject;

import in.mapbazar.mapbazar.Fragment.WomenFragment;
import in.mapbazar.mapbazar.MainActivity;
import in.mapbazar.mapbazar.R;
import in.mapbazar.mapbazar.connection.API;
import in.mapbazar.mapbazar.connection.RestAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResetPasswordDialog extends AppCompatDialogFragment {

    private EditText etPass,etConPass;
    private ProgressDialog loadingBar;
    private Button btnReset;
    private String userNumber="";
    private int msg=1;

    Dialog ProgressDialog;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {


        final AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        LayoutInflater layoutInflater=getActivity().getLayoutInflater();
        final View view=layoutInflater.inflate(R.layout.dialog_password_layout,null);

        ProgressDialog = new Dialog(getActivity(), android.R.style.Theme_Translucent_NoTitleBar);
        ProgressDialog.setContentView(R.layout.progressbar);
        ProgressDialog.setCancelable(false);
        userNumber= WomenFragment.number.toString().trim();
        etPass=(EditText)view.findViewById(R.id.etPassword);
        etConPass=(EditText)view.findViewById(R.id.etConPassword);
        btnReset=(Button)view.findViewById(R.id.btnReset);
       // loadingBar=new ProgressDialog(getContext());
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                if (etPass.getText().toString().length() == 0) {
                    etPass.setError(getActivity().getString(R.string.enter_password));
                    etPass.requestFocus();
                } else if (etPass.getText().toString().length() < 6 || etPass.getText().toString().length() > 32) {
                    etPass.setError(getActivity().getResources().getString(R.string.password_length));
                    etPass.requestFocus();
                } else if (!Utils.isValidPass(etPass.getText().toString().trim())) {
                    etPass.setError(getActivity().getResources().getString(R.string.password_valid));
                    etPass.requestFocus();
                } else if (etConPass.getText().toString().equals("")) {
                    etConPass.setError(getActivity().getString(R.string.enter_cnfpassword));
                    etConPass.requestFocus();
                } else if (!etConPass.getText().toString().equals(etPass.getText().toString())) {
                    etConPass.setError(getActivity().getString(R.string.enter_pass_notmatch));
                    etConPass.requestFocus();
                }
                else
                {
                    String pass=etPass.getText().toString().trim();
                    String conpass=etConPass.getText().toString().trim();

                        update_password(pass);
                    dismiss();



                }


//                Toast.makeText(getContext(),"Data1 :"+userNumber+"Data2 :"+etPass.getText().toString()+"Data3 :"+etConPass.getText().toString(),Toast.LENGTH_LONG).show();
              //  Toast.makeText(getContext(),"Data1 :"+userNumber+"Data2 :"+etPass.getText().toString(),Toast.LENGTH_LONG).show();

            }
        });
        builder.setView(view);
        return builder.create();


    }


    public void update_password(final String password)
    {
ProgressDialog.show();
        API api= RestAdapter.createAPI();
        Call<JsonObject> callback_homecategory = api.update_pass(userNumber,password);
        callback_homecategory.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                if(response.isSuccessful())
                {
                    ProgressDialog.dismiss();
                    JsonObject object=response.body();
                    String status=object.get("status").getAsString();
                    if(status.equals("success"))
                    {
                        msg=1;
                       // Toast.makeText(getContext(),""+object.get("message").getAsString(),Toast.LENGTH_LONG).show();


                    }
                    else
                    {
                        msg=2;
                    }
                }
                else
                {
                    ProgressDialog.dismiss();
                   // Toast.makeText(getActivity(),"Server Not Responding",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
           t.printStackTrace();
                ProgressDialog.dismiss();
                Toast.makeText(getActivity(),"Server Not Responding",Toast.LENGTH_LONG).show();
            //    ErrorMessage("Server Not Responding");
            }
        });

    }

//    private void ErrorMessage(String Message) {
//        // Common.showInternetInfo(ActivitySignIn.this, "Network is not available");
//        Dialog dialog = new DialogUtils(getActivity()).buildDialogMessage(new CallbackMessage() {
//            @Override
//            public void onSuccess(Dialog dialog) {
//                dialog.dismiss();
//            }
//
//            @Override
//            public void onCancel(Dialog dialog) {
//                dialog.dismiss();
//            }
//        }, Message);
//        dialog.show();
//    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);

        if(msg==1)
        {
            Intent intent=new Intent(getContext(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

//            PasswordActivity.relativeLayout1.setVisibility(View.INVISIBLE);
//            PasswordActivity.relativeLayout2.setVisibility(View.INVISIBLE);
            Toast.makeText(getContext(),"Password updated successfully.",Toast.LENGTH_LONG).show();
       //     PasswordActivity.relativeLayout5.setVisibility(View.VISIBLE);

        }
        else
        {

            Toast.makeText(getContext(),"Something went wrong \n Please try another time.",Toast.LENGTH_LONG).show();
            //PasswordActivity.relativeLayout5.setVisibility(View.VISIBLE);
        }



    }
}
