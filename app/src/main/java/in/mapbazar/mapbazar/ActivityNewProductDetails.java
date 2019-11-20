package in.mapbazar.mapbazar;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import in.mapbazar.mapbazar.Adapter.Cart_Adapter;
import in.mapbazar.mapbazar.Adapter.CategoryProductItemAdapter;
import in.mapbazar.mapbazar.Adapter.MatchWithproductAdapter;
import in.mapbazar.mapbazar.Adapter.RecentproductAdapter;
import in.mapbazar.mapbazar.Fragment.DeliveryFragment;
import in.mapbazar.mapbazar.Fragment.ShopingCartFragment;
import in.mapbazar.mapbazar.Model.HomeData.NewProductAttributeItem;
import in.mapbazar.mapbazar.Model.HomeData.NewProductItem;
import in.mapbazar.mapbazar.Model.MatchWithProduct.ColorItem;
import in.mapbazar.mapbazar.Model.MatchWithProduct.MatchProductAttributeItem;
import in.mapbazar.mapbazar.Model.MatchWithProduct.MatchProductItem;
import in.mapbazar.mapbazar.Model.MatchWithProduct.MatchProductSizeItem;
import in.mapbazar.mapbazar.Model.RecentlyProduct.RecentlyProductItem;
import in.mapbazar.mapbazar.Model.RecentlyProduct.RecentlyProductSizeItem;

import in.mapbazar.mapbazar.Model.RecentlyProduct.RevcentlyProductAttributeItem;

import in.mapbazar.mapbazar.Modules.Module;
import in.mapbazar.mapbazar.Utili.Common;
import in.mapbazar.mapbazar.Utili.Utils;
import in.mapbazar.mapbazar.View.CustomTextView;
import in.mapbazar.mapbazar.View.DialogUtils;
import in.mapbazar.mapbazar.callback.CallbackInternate;
import in.mapbazar.mapbazar.connection.API;
import in.mapbazar.mapbazar.Utili.Url;
import in.mapbazar.mapbazar.callback.CallbackMessage;
import in.mapbazar.mapbazar.connection.RestAdapter;
import in.mapbazar.mapbazar.dialog.SelectSizeDialogFragment;
import com.viewpagerindicator.CirclePageIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.sufficientlysecure.htmltextview.HtmlHttpImageGetter;
import org.sufficientlysecure.htmltextview.HtmlTextView;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import in.mapbazar.mapbazar.util.ConnectivityReceiver;
import in.mapbazar.mapbazar.util.DatabaseCartHandler;
import in.mapbazar.mapbazar.util.Session_management;



public class ActivityNewProductDetails extends AppCompatActivity implements View.OnClickListener{
    private RecyclerView rv_cart;
    public static CustomTextView tv_clear, tv_total, tv_item;
    private RelativeLayout btn_checkout;

    //  private DatabaseHandler db;
    private DatabaseCartHandler db_cart;
    private Session_management sessionManagement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_details);
        sessionManagement = new Session_management(this);
        sessionManagement.cleardatetime();


        tv_clear = (CustomTextView) findViewById(R.id.tv_cart_clear);
        tv_total = (CustomTextView) findViewById(R.id.tv_cart_total);
        tv_item = (CustomTextView) findViewById(R.id.tv_cart_item);
        btn_checkout = (RelativeLayout) findViewById(R.id.btn_cart_checkout);
        rv_cart = (RecyclerView) findViewById(R.id.rv_cart);
        rv_cart.setLayoutManager(new LinearLayoutManager(ActivityNewProductDetails.this));

        //db = new DatabaseHandler(getActivity());
        db_cart=new DatabaseCartHandler(this);


        ArrayList<HashMap<String, String>> map = db_cart.getCartAll();
//        final HashMap<String, String> map1 = map.get(0);
//       Log.d("cart all ",""+map1);

        Cart_Adapter adapter = new Cart_Adapter(ActivityNewProductDetails.this, map);
        rv_cart.setAdapter(adapter);
        adapter.notifyDataSetChanged();

       // updateData();

        tv_clear.setOnClickListener(ActivityNewProductDetails.this);
        btn_checkout.setOnClickListener(ActivityNewProductDetails.this);


    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.tv_cart_clear) {
            // showdialog
            // Toast.makeText(getActivity(),""+db_cart.getCartCount(),Toast.LENGTH_LONG).show();
            showClearDialog();
        } else if (id == R.id.btn_cart_checkout) {

            if (ConnectivityReceiver.isConnected()) {
                makeGetLimiteRequest();
            } else {
                //((MainActivity) getActivity()).onNetworkConnectionChanged(false);
            }

        }
    }

    // update UI
//    private void updateData() {
//        tv_total.setText(this.getString(R.string.currency)+ db_cart.getTotalAmount());
//        tv_item.setText("" + db_cart.getCartCount());
//        ((MainActivity) getActivity()).setCartCounter("" + db_cart.getCartCount());
//    }

    private void showClearDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setMessage(getResources().getString(R.string.sure_del));
        alertDialog.setNegativeButton(getResources().getString(R.string.cancle), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alertDialog.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // clear cart data
                db_cart.clearCart();
                ArrayList<HashMap<String, String>> map = db_cart.getCartAll();
                Cart_Adapter adapter = new Cart_Adapter(ActivityNewProductDetails.this, map);
                rv_cart.setAdapter(adapter);
                adapter.notifyDataSetChanged();

                //updateData();

                dialogInterface.dismiss();
            }
        });

        alertDialog.show();
    }

    /**
     * Method to make json array request where json response starts wtih
     */
    private void makeGetLimiteRequest() {

        JsonArrayRequest req = new JsonArrayRequest(Url.GET_LIMITE_SETTING_URL,
                new com.android.volley.Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("limit", response.toString());

                        Double total_amount = Double.parseDouble(db_cart.getTotalAmount());


                        try {
                            // Parsing json array response
                            // loop through each json object

                            boolean issmall = false;
                            boolean isbig = false;

                            // arraylist list variable for store data;
                            ArrayList<HashMap<String, String>> listarray = new ArrayList<>();

                            for (int i = 0; i < response.length(); i++) {

                                JSONObject jsonObject = (JSONObject) response
                                        .get(i);
                                int value;

                                if (jsonObject.getString("id").equals("1")) {
                                    value = Integer.parseInt(jsonObject.getString("value"));

                                    if (total_amount < value) {
                                        issmall = true;
                                        Toast.makeText(ActivityNewProductDetails.this, "" + jsonObject.getString("title") + " : " + value, Toast.LENGTH_SHORT).show();
                                    }
                                } else if (jsonObject.getString("id").equals("2")) {
                                    value = Integer.parseInt(jsonObject.getString("value"));

                                    if (total_amount > value) {
                                        isbig = true;
                                        Toast.makeText(ActivityNewProductDetails.this, "" + jsonObject.getString("title") + " : " + value, Toast.LENGTH_SHORT).show();
                                    }
                                }

                            }

                            if (!issmall && !isbig) {
                                if (sessionManagement.isLoggedIn()) {
//                                    Bundle args = new Bundle();
//                                    DeliveryFragment fm = new DeliveryFragment();
//                                    fm.setArguments(args);
//
//
//                                    FragmentManager fragmentManager = getFragmentManager();
//                                    fragmentManager.beginTransaction().replace(R.id)
//                                            .addToBackStack(null).commit();
                                } else {
                                    //Toast.makeText(getActivity(), "Please login or regiter.\ncontinue", Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(ActivityNewProductDetails.this, ActivityLoginRegister.class);
                                    startActivity(i);
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(ActivityNewProductDetails.this,
                                    "Error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Module module=new Module(ActivityNewProductDetails.this);
                String msg=module.VolleyErrorMessage(error);
                Toast.makeText(ActivityNewProductDetails.this, ""+msg, Toast.LENGTH_SHORT).show();
//                VolleyLog.d(TAG, "Error: " + error.getMessage());
//                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
//                    Toast.makeText(getActivity(), "Connection Time out", Toast.LENGTH_SHORT).show();
//                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(req);

    }


}
