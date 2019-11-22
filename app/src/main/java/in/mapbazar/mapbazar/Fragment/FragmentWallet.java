package in.mapbazar.mapbazar.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import in.mapbazar.mapbazar.MainActivity;
import in.mapbazar.mapbazar.R;

import in.mapbazar.mapbazar.Utili.Url;
import in.mapbazar.mapbazar.util.ConnectivityReceiver;
import in.mapbazar.mapbazar.util.Session_management;
import in.mapbazar.mapbazar.util.SharedPref;


public class FragmentWallet extends Fragment {

    private static String TAG = FragmentWallet.class.getSimpleName();

    TextView Wallet_Ammount;


    RelativeLayout Recharge_Wallet;
    private Session_management sessionManagement;

    public FragmentWallet() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate( R.layout.fragment_wallet, container, false);
        ((MainActivity) getActivity()).setTitle(getResources().getString(R.string.tv_app_name));
        sessionManagement = new Session_management(getActivity());
        String getwallet = sessionManagement.getUserDetails().get( Url.KEY_WALLET_Ammount);
        Wallet_Ammount = (TextView) view.findViewById(R.id.wallet_ammount);
         Wallet_Ammount.setText(getwallet);
        Recharge_Wallet = (RelativeLayout) view.findViewById(R.id.recharge_wallet);

//        Recharge_Wallet.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(), RechargeWallet.class);
//                startActivity(intent);
//            }
//        });
        if (ConnectivityReceiver.isConnected()) {
           getRefresrh();
        }


        return view;

    }

    public void getRefresrh() {
        String user_id = sessionManagement.getUserDetails().get(Url.KEY_ID);
        RequestQueue rq = Volley.newRequestQueue(getActivity());
        StringRequest strReq = new StringRequest( Request.Method.GET, Url.WALLET_REFRESH + user_id,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jObj = new JSONObject(response);
                            if (jObj.optString("success").equalsIgnoreCase("success")) {
                                String wallet_amount = jObj.getString("wallet");
                                Wallet_Ammount.setText(wallet_amount);
                                SharedPref.putString(getActivity(), Url.KEY_WALLET_Ammount, wallet_amount);
                            } else {
                                // Toast.makeText(DashboardPage.this, "" + jObj.optString("msg"), Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {

        };
        rq.add(strReq);
    }

}