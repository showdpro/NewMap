package in.mapbazar.mapbazar.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


import in.mapbazar.mapbazar.MainActivity;
import in.mapbazar.mapbazar.R;
import in.mapbazar.mapbazar.Utili.Url;
import in.mapbazar.mapbazar.networkconnectivity.NetworkConnection;
import in.mapbazar.mapbazar.networkconnectivity.NetworkError;
import in.mapbazar.mapbazar.util.ConnectivityReceiver;
import in.mapbazar.mapbazar.util.Session_management;
import in.mapbazar.mapbazar.util.SharedPref;
import pl.droidsonroids.gif.GifImageView;




public class Reward_fragment extends Fragment {
    private GifImageView gifImageView;
    private static String TAG = Reward_fragment.class.getSimpleName();
    RelativeLayout Reedeem_Points;
    TextView Rewards_Points;
    private Session_management sessionManagement;

    public Reward_fragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.activity_reward_points, container, false);
        ((MainActivity) getActivity()).setTitle(getResources().getString( R.string.tv_toolbar_name));
        sessionManagement = new Session_management(getActivity());

        String getrewards = sessionManagement.getUserDetails().get( Url.KEY_REWARDS_POINTS);
        Rewards_Points = (TextView) view.findViewById(R.id.reward_points);
        Rewards_Points.setText(getrewards);
//        gifImageView = (GifImageView) view.findViewById(R.id.gif_image);
//        gifImageView.setBackgroundResource( R.drawable.pay );
       // gifImageView.setImageResource(R.drawable.pay);

        Reedeem_Points = (RelativeLayout) view.findViewById(R.id.reedme_point);
        Reedeem_Points.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Shift_Reward_to_WAllet();
              //  gifImageView.setVisibility(View.VISIBLE);
              //  final View myview = gifImageView;
                view.postDelayed(new Runnable() {
                    public void run() {
                       // myview.setVisibility(View.GONE);
                    }
                }, 5000);
            }
        });

        if (ConnectivityReceiver.isConnected()) {
            getRewards();
        }
        return view;

    }

    public void getRewards() {
        String user_id = sessionManagement.getUserDetails().get(Url.KEY_ID);
        RequestQueue rq = Volley.newRequestQueue(getActivity());
        StringRequest strReq = new StringRequest(Request.Method.GET, Url.REWARDS_REFRESH + user_id,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jObj = new JSONObject(response);
                            if (jObj.optString("success").equalsIgnoreCase("success")) {
                                String rewards_points = jObj.getString("total_rewards");
                                if (rewards_points.equals("null")) {
                                    Rewards_Points.setText("0");
                                } else {
                                    Rewards_Points.setText(rewards_points);
                                    SharedPref.putString(getActivity(), Url.KEY_REWARDS_POINTS, rewards_points);
                                }

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

    private void Shift_Reward_to_WAllet() {
        final String user_id = sessionManagement.getUserDetails().get(Url.KEY_ID);
        final String getreward = Rewards_Points.getText().toString();
        final String getwallet = SharedPref.getString(getActivity(),Url.KEY_WALLET_Ammount);
        if (NetworkConnection.connectionChecking(getActivity())) {
            RequestQueue rq = Volley.newRequestQueue(getActivity());
            StringRequest postReq = new StringRequest(Request.Method.POST, Url.BASE_URL+"index.php/api/shift",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.i("eclipse", "Response=" + response);
                            try {
                                JSONObject object = new JSONObject(response);
                                JSONArray Jarray = object.getJSONArray("wallet_amount");
                                for (int i = 0; i < Jarray.length(); i++) {
                                    JSONObject json_data = Jarray.getJSONObject(i);
                                    String final_amount = json_data.getString("final_amount");
                                    String final_rewards = json_data.getString("final_rewards");
                                    Rewards_Points.setText(final_rewards);
                                    SharedPref.putString(getActivity(), Url.KEY_WALLET_Ammount, final_amount);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    System.out.println("Error [" + error + "]");
                }
            }) {

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("user_id", user_id);
                    params.put("rewards", getreward);
                    params.put("amount", getwallet);
                    return params;
                }
            };
            rq.add(postReq);
        } else {
            Intent intent = new Intent(getActivity(), NetworkError.class);
            startActivity(intent);
        }

    }


}