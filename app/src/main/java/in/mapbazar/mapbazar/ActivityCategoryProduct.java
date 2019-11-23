package in.mapbazar.mapbazar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import in.mapbazar.mapbazar.Adapter.Product_adapter;
import in.mapbazar.mapbazar.Adapter.SortAdapter;
import in.mapbazar.mapbazar.Fragment.HomeFragment;

import in.mapbazar.mapbazar.Model.Product_model;
import in.mapbazar.mapbazar.Modules.Module;

import in.mapbazar.mapbazar.Utili.Url;
import in.mapbazar.mapbazar.View.CustomTextView;


import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.mapbazar.mapbazar.util.ConnectivityReceiver;
import in.mapbazar.mapbazar.util.CustomVolleyJsonRequest;
import in.mapbazar.mapbazar.util.Session_management;

public class ActivityCategoryProduct extends AppCompatActivity implements View.OnClickListener {

    private static String TAG = ActivityCategoryProduct.class.getSimpleName();

    int height, width;

    SortAdapter sortAdapter;
    //Shared Preferences
    Session_management session_management;
    private SharedPreferences sPref;
    Activity activity=ActivityCategoryProduct.this;
    RelativeLayout back ,layout_cart ;
    RecyclerView recyclerView;
    Dialog ProgressDialog;
    String filter_cat_id,min,max;
    private List<Product_model> product_modelList = new ArrayList<>();
    private Product_adapter adapter_product;
String getcat_id="";
String title="";
CustomTextView txt_filter,txt_sort;
    String Categoryid, Categoryname, f_tags, f_brand, f_size, f_color, f_startprice, f_endprice;
   HomeFragment homeFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_product);
   recyclerView=findViewById(R.id.recyclerView);
   layout_cart=findViewById( R.id.layout_cart );
   layout_cart.setOnClickListener( this );
   session_management=new Session_management(ActivityCategoryProduct.this);

        initComponent();
    // getcat_id=getIntent().getStringExtra("cat_id");
     getcat_id=session_management.getCat_id();
     if(getcat_id.isEmpty() || getcat_id.equals("") || getcat_id.equals("null"))
     {
         filter_cat_id=session_management.getFilter_id();
         min=getIntent().getStringExtra("min");
         max=getIntent().getStringExtra("max");
         makeFilterProductRequest(filter_cat_id,min,max);

     }
     else
     {
         makeGetProductRequest(getcat_id);

     }
        //filter_cat_id=getIntent().getStringExtra("filter_cat_id");

     title=getIntent().getStringExtra("title");
        if (ConnectivityReceiver.isConnected()) {
            //Shop by Catogary
            // makeGetSliderCategoryRequest(id);
            // Toast.makeText(getActivity(),"a"+getcat_id,Toast.LENGTH_LONG).show();


            //Deal Of The Day Products
            //      makedealIconProductRequest(get_deal_id);
            //Top Sale Products
            //        maketopsaleProductRequest(get_top_sale_id);


            //Slider
//      makeGetBannerSliderRequest();

        }
        else
        {
            //((MainActivity) getActivity()).onNetworkConnectionChanged(false);
        }

    }

    private void makeFilterProductRequest(String filter_cat_id, String min, String max) {
        ProgressDialog.show();
        String tag_json_obj = "json_product_req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("cat_id", filter_cat_id);
        params.put("min_price", min);
        params.put("max_price", max);

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                Url.GET_PRODUCT_URL, params, new com.android.volley.Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("rett" +
                        "", response.toString());
                ProgressDialog.dismiss();
                try {

                    Boolean status = response.getBoolean("responce");

                    if (status) {

                        ///         Toast.makeText(getActivity(),""+response.getString("data"),Toast.LENGTH_LONG).show();
                        Gson gson = new Gson();
                        Type listType = new TypeToken<List<Product_model>>() {
                        }.getType();
                        product_modelList = gson.fromJson(response.getString("data"), listType);
                        adapter_product = new Product_adapter(product_modelList, ActivityCategoryProduct.this);

                        recyclerView.setAdapter(adapter_product);
                        adapter_product.notifyDataSetChanged();

                        if (product_modelList.isEmpty()) {


                            recyclerView.setVisibility( View.GONE );


                            Toast.makeText(ActivityCategoryProduct.this, getResources().getString(R.string.no_rcord_found), Toast.LENGTH_SHORT).show();
                        }

                    }
                } catch (JSONException e) {

                    //   e.printStackTrace();
                    String ex=e.getMessage();




                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                Module module=new Module(ActivityCategoryProduct.this);
                String msg=module.VolleyErrorMessage(error);
                Toast.makeText(ActivityCategoryProduct.this, ""+msg, Toast.LENGTH_SHORT).show();
//                VolleyLog.d(TAG, "Error: " + error.getMessage());
//                //loadingBar.dismiss();
//                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
//                    loadingBar.dismiss();
//                    Toast.makeText(getActivity(), getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
//                }

            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }

    private void initComponent() {




        ProgressDialog = new Dialog(ActivityCategoryProduct.this, android.R.style.Theme_Translucent_NoTitleBar);
        ProgressDialog.setContentView(R.layout.progressbar);
        ProgressDialog.setCancelable(false);


        txt_sort=(CustomTextView)findViewById(R.id.txt_sort);
        txt_filter=(CustomTextView)findViewById(R.id.txt_filter);
        back = findViewById( R.id.ly_back );
        //progressBar = (ProgressBar) ProgressDialog.findViewById(R.id.progress_circular);

        DisplayMetrics localDisplayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(localDisplayMetrics);
        height = localDisplayMetrics.heightPixels;
        width = localDisplayMetrics.widthPixels;

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(activity, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        //recyclerView.addItemDecoration(new GridSpacingItemDecoration(this, 2, 1, true));
        //recyclerView.setItemAnimator(new DefaultItemAnimator());

   txt_filter.setOnClickListener(this);
   txt_sort.setOnClickListener(this);
   back.setOnClickListener( this );

        // on swipe list



    }
    private void makeDescendingProductRequest(String cat_id) {
        String tag_json_obj = "json_product_desc_req";
        Toast.makeText(ActivityCategoryProduct.this,"cat_id:-  "+cat_id+"\nfilter_id:-- "+filter_cat_id,Toast.LENGTH_LONG).show();
        Map<String, String> params = new HashMap<String, String>();
        params.put("cat_id", cat_id);
        ProgressDialog.show();
        product_modelList.clear();

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                Url.GET_PRODUCT_DESC, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
               ProgressDialog.dismiss();
                try {
                    Boolean status = response.getBoolean("responce");
                    if (status) {
                        Gson gson = new Gson();
                        Type listType = new TypeToken<List<Product_model>>() {
                        }.getType();
                        product_modelList = gson.fromJson(response.getString("data"), listType);
                        adapter_product = new Product_adapter(product_modelList, ActivityCategoryProduct.this);
                    //    recyclerView.setLayoutManager( new LinearLayoutManager(ActivityCategoryProduct.this,LinearLayoutManager.VERTICAL,false));
                        recyclerView.setAdapter(adapter_product);
                        adapter_product.notifyDataSetChanged();
//                        if (getActivity() != null) {
                            if (product_modelList.isEmpty()) {
                                Toast.makeText(ActivityCategoryProduct.this, getResources().getString(R.string.no_rcord_found), Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                //  Toast.makeText(getActivity(),""+response, Toast.LENGTH_SHORT).show();
                            }
//                        }

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
                Toast.makeText( ActivityCategoryProduct.this,""+ errormsg,Toast.LENGTH_LONG ).show();
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }
    private void makeAscendingProductRequest(String cat_id) {
        Toast.makeText(ActivityCategoryProduct.this,"cat_id:-  "+cat_id+"\nfilter_id:-- "+filter_cat_id,Toast.LENGTH_LONG).show();
        String tag_json_obj = "json_product_asc_req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("cat_id", cat_id);
        product_modelList.clear();
        ProgressDialog.show();
        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
               Url.GET_PRODUCT_ASC, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
               ProgressDialog.dismiss();
                try {
                    Boolean status = response.getBoolean("responce");
                    if (status) {
                        Gson gson = new Gson();
                        Type listType = new TypeToken<List<Product_model>>() {
                        }.getType();
                        product_modelList = gson.fromJson(response.getString("data"), listType);
                        adapter_product = new Product_adapter(product_modelList,ActivityCategoryProduct.this);
                       // recyclerView.setLayoutManager( new LinearLayoutManager(ActivityCategoryProduct.this,LinearLayoutManager.VERTICAL,false));
                       recyclerView.setAdapter(adapter_product);
                        adapter_product.notifyDataSetChanged();
                        if (ActivityCategoryProduct.this != null) {
                            if (product_modelList.isEmpty()) {
                                Toast.makeText(ActivityCategoryProduct.this, getResources().getString(R.string.no_rcord_found), Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                //  Toast.makeText(getActivity(),""+response, Toast.LENGTH_SHORT).show();
                            }
                        }

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
                Toast.makeText(ActivityCategoryProduct.this,""+ errormsg,Toast.LENGTH_LONG ).show();
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }

    private void makeNewestProductRequest(String cat_id) {
        String tag_json_obj = "json_product_newest_req";
        Map<String, String> params = new HashMap<String, String>();

        Toast.makeText(ActivityCategoryProduct.this,""+cat_id,Toast.LENGTH_LONG).show();
        params.put("cat_id",cat_id);
        product_modelList.clear();
       ProgressDialog.show();
        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
               Url.GET_PRODUCT_NEWEST, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                ProgressDialog.dismiss();
                try {
                    Boolean status = response.getBoolean("responce");
                    if (status) {
                        Gson gson = new Gson();
                        Type listType = new TypeToken<List<Product_model>>() {
                        }.getType();
                        product_modelList = gson.fromJson(response.getString("data"), listType);
                        adapter_product = new Product_adapter(product_modelList,ActivityCategoryProduct.this);
                     //   recyclerView.setLayoutManager( new LinearLayoutManager(ActivityCategoryProduct.this,LinearLayoutManager.VERTICAL,false));
                        recyclerView.setAdapter(adapter_product);
                        adapter_product.notifyDataSetChanged();
                        if (ActivityCategoryProduct.this != null) {
                            if (product_modelList.isEmpty()) {
                                Toast.makeText(ActivityCategoryProduct.this, getResources().getString(R.string.no_rcord_found), Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                //  Toast.makeText(getActivity(),""+response, Toast.LENGTH_SHORT).show();
                            }
                        }

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
                Toast.makeText( ActivityCategoryProduct.this,""+ errormsg,Toast.LENGTH_LONG ).show();
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }





    @Override
    public void onBackPressed() {
        //super.onBackPressed();
      Intent intent=new Intent(ActivityCategoryProduct.this,MainActivity.class);
      startActivity(intent);

    }


    @Override
    public void onClick(View view) {
        int  id=view.getId();
        if(id == R.id.txt_filter)
        {




            Intent filter_intent = new Intent(ActivityCategoryProduct.this, ProductFilter.class);
            filter_intent.putExtra("cat_id",getcat_id);
            startActivity(filter_intent);
            finish();
        }
        else if(id == R.id.ly_back)
        {
            finish();
        }
        else if(id == R.id.layout_cart)
        {
            Intent intent = new Intent( ActivityCategoryProduct.this , ActivityCart.class);
            startActivity( intent );
        }
        else if (id == R.id.txt_sort) {

           String c_id="";
            final ArrayList <String>  sort_List = new ArrayList<>(  );


            sort_List.add( "Price Low - High" );
            sort_List.add("Price High - Low");
            sort_List.add("Newest First");
            //  sort_List.add ("Trending");
            AlertDialog.Builder builder=new AlertDialog.Builder(ActivityCategoryProduct.this);
            LayoutInflater layoutInflater=(LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row=layoutInflater.inflate(R.layout.dialog_sort_layout,null);
            ListView l1=(ListView)row.findViewById(R.id.list_sort);
            sortAdapter=new SortAdapter(activity,sort_List);
            //productVariantAdapter.notifyDataSetChanged();
            l1.setAdapter(sortAdapter);
            builder.setView(row);

            final AlertDialog ddlg=builder.create();
            ddlg.show();

            final String finalC_id = c_id;
            l1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    ddlg.dismiss();
                    String item = sort_List.get( i ).toString();
          if (item.equals( "Price Low - High" ))
                    {

                        ddlg.dismiss();



                      //  Toast.makeText(activity,"l-h"+cat_id,Toast.LENGTH_LONG).show();
                        makeAscendingProductRequest(getcat_id);

                    }
                    else if(item.equals( "Price High - Low" ))
                    {
                        //  Toast.makeText( getActivity(), "category id :" +cat_id, Toast.LENGTH_SHORT ).show();
                        ddlg.dismiss();

                      makeDescendingProductRequest(getcat_id);



                    }
                    else if(item.equals( "Newest First" ))
                    {
                        ddlg.dismiss();

                       makeNewestProductRequest(getcat_id);

                    }
                    else if (item.equals( "Trending" ))
                    {

                    }

                    // Toast.makeText( getActivity(),"Showing items:" +item,Toast.LENGTH_LONG ).show();
                }
            } );
        }

    }

    //Get Shop By Catogary Products
    private void makeGetProductRequest(String cat_id) {

       ProgressDialog.show();
        String tag_json_obj = "json_product_req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("cat_id", cat_id);

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                Url.GET_PRODUCT_URL, params, new com.android.volley.Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("rett" +
                        "", response.toString());
             ProgressDialog.dismiss();
                try {

                    Boolean status = response.getBoolean("responce");

                    if (status) {

                        ///         Toast.makeText(getActivity(),""+response.getString("data"),Toast.LENGTH_LONG).show();
                        Gson gson = new Gson();
                        Type listType = new TypeToken<List<Product_model>>() {
                        }.getType();
                        product_modelList = gson.fromJson(response.getString("data"), listType);
                        adapter_product = new Product_adapter(product_modelList, ActivityCategoryProduct.this);

                        recyclerView.setAdapter(adapter_product);
                        adapter_product.notifyDataSetChanged();

                            if (product_modelList.isEmpty()) {


                                recyclerView.setVisibility( View.GONE );


                                Toast.makeText(ActivityCategoryProduct.this, getResources().getString(R.string.no_rcord_found), Toast.LENGTH_SHORT).show();
                            }

                    }
                } catch (JSONException e) {

                    //   e.printStackTrace();
                    String ex=e.getMessage();




                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                Module module=new Module(ActivityCategoryProduct.this);
                String msg=module.VolleyErrorMessage(error);
                Toast.makeText(ActivityCategoryProduct.this, ""+msg, Toast.LENGTH_SHORT).show();
//                VolleyLog.d(TAG, "Error: " + error.getMessage());
//                //loadingBar.dismiss();
//                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
//                    loadingBar.dismiss();
//                    Toast.makeText(getActivity(), getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
//                }

            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }


}
