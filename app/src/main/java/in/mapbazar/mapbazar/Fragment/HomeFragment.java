package in.mapbazar.mapbazar.Fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import in.mapbazar.mapbazar.ActivityCategoryProduct;
import in.mapbazar.mapbazar.ActivitySubCategory;
import in.mapbazar.mapbazar.Adapter.Deal_OfDay_Adapter;
import in.mapbazar.mapbazar.Adapter.HomeCategoryItemAdapter;
import in.mapbazar.mapbazar.Adapter.HomeViewPagerAdapter;
import in.mapbazar.mapbazar.Adapter.Home_Icon_Adapter;
import in.mapbazar.mapbazar.Adapter.Home_NewproductAdapter;
import in.mapbazar.mapbazar.Adapter.Home_adapter;
import in.mapbazar.mapbazar.Adapter.RecentproductAdapter;
import in.mapbazar.mapbazar.Adapter.Top_Selling_Adapter;
import in.mapbazar.mapbazar.AppController;
import in.mapbazar.mapbazar.CustomSlider;
import in.mapbazar.mapbazar.MainActivity;
import in.mapbazar.mapbazar.Model.Category_model;
import in.mapbazar.mapbazar.Model.Deal_Of_Day_model;
import in.mapbazar.mapbazar.Model.FilterProduct.FilterData;
import in.mapbazar.mapbazar.Model.HomeData.CategoryDataItem;
import in.mapbazar.mapbazar.Model.HomeData.HomeData;
import in.mapbazar.mapbazar.Model.HomeData.HomeSliderImageItem;
import in.mapbazar.mapbazar.Model.HomeData.NewProductAttributeItem;
import in.mapbazar.mapbazar.Model.HomeData.NewProductItem;
import in.mapbazar.mapbazar.Model.HomeData.NewProductSizeItem;
import in.mapbazar.mapbazar.Model.Home_Icon_model;
import in.mapbazar.mapbazar.Model.Menu1.Menu1CategoryItem;
import in.mapbazar.mapbazar.Model.Menu1.Menu1Data;
import in.mapbazar.mapbazar.Model.Menu1.Menu1SliderItem;
import in.mapbazar.mapbazar.Model.Menu2.Menu2CategoryItem;
import in.mapbazar.mapbazar.Model.Menu2.Menu2Data;
import in.mapbazar.mapbazar.Model.Menu2.Menu2SliderItem;
import in.mapbazar.mapbazar.Model.Menu3.Menu3CategoryItem;
import in.mapbazar.mapbazar.Model.Menu3.Menu3Data;
import in.mapbazar.mapbazar.Model.Menu3.Menu3SliderItem;
import in.mapbazar.mapbazar.Model.RecentlyProduct.RecentlyProductItem;
import in.mapbazar.mapbazar.Model.RecentlyProduct.RecentlyProductSizeItem;
import in.mapbazar.mapbazar.Model.RecentlyProduct.RevcentlyProductAttributeItem;
import in.mapbazar.mapbazar.Model.Top_Selling_model;
import in.mapbazar.mapbazar.Modules.Module;
import in.mapbazar.mapbazar.Utili.Common;
import in.mapbazar.mapbazar.Utili.GridSpacingItemDecoration;
import in.mapbazar.mapbazar.Utili.Url;
import in.mapbazar.mapbazar.View.CustomTextView;
import in.mapbazar.mapbazar.R;
import in.mapbazar.mapbazar.View.DialogUtils;
import in.mapbazar.mapbazar.callback.CallbackInternate;
import in.mapbazar.mapbazar.callback.CallbackMessage;
import in.mapbazar.mapbazar.connection.API;
import in.mapbazar.mapbazar.connection.RestAdapter;

import com.google.gson.reflect.TypeToken;
import com.viewpagerindicator.CirclePageIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.mapbazar.mapbazar.util.ConnectivityReceiver;
import in.mapbazar.mapbazar.util.CustomVolleyJsonRequest;
import in.mapbazar.mapbazar.util.DatabaseCartHandler;
import in.mapbazar.mapbazar.util.RecyclerTouchListener;
import retrofit2.Call;
import retrofit2.Callback;


public class HomeFragment extends Fragment {

    private static String TAG = HomeFragment.class.getSimpleName();
    private SliderLayout imgSlider, banner_slider, featuredslider;
    private RecyclerView new_products_recycler, rv_top_selling, rv_headre_icons;
    private List<Category_model> category_modelList = new ArrayList<>();
    private Home_adapter adapter;
    private boolean isSubcat = false;
    LinearLayout Search_layout;
    DatabaseCartHandler db_cart;
    String getid;
    String getcat_title;
    ScrollView scrollView;
    TextView footer ;
    SharedPreferences sharedpreferences;
    Dialog ProgressDialog;
    Animation animation;



    //Home Icons
    private Home_Icon_Adapter menu_adapter;
    private List<Home_Icon_model> menu_models = new ArrayList<>();


    //Deal O Day
    private Deal_OfDay_Adapter deal_ofDay_adapter;
    private List<Deal_Of_Day_model> deal_of_day_models = new ArrayList<>();
    LinearLayout Deal_Linear_layout;
    FrameLayout Deal_Frame_layout, Deal_Frame_layout1;


    //Top Selling Products
    private Top_Selling_Adapter top_selling_adapter;
    private List<Top_Selling_model> top_selling_models = new ArrayList<>();


    //  private ImageView iv_Call, iv_Whatspp, iv_reviews, iv_share_via;
    private TextView txt, timer;
    Button View_all_deals, View_all_TopSell;
   // ProgressDialog loadingBar;

    private ImageView Top_Selling_Poster, Deal_Of_Day_poster;

    View view;

    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment, container, false);

        ProgressDialog = new Dialog(Common.Activity, android.R.style.Theme_Translucent_NoTitleBar);
        ProgressDialog.setContentView(R.layout.progressbar);
        ProgressDialog.setCancelable(false);
        setHasOptionsMenu(true);
        ((MainActivity) getActivity()).setTitle(getResources().getString(R.string.app_name));
       // ((MainActivity) getActivity()).updateHeader();
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        db_cart=new DatabaseCartHandler(getActivity());
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
                    builder.setTitle("Confirmation");
                    builder.setMessage("Are you sure want to exit?");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            //((MainActivity) getActivity()).finish();
                            getActivity().finishAffinity();


                        }
                    })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            });
                    AlertDialog dialog=builder.create();
                    dialog.show();
                    return true;
                }
                return false;
            }
        });
        //Check Internet Connection
        if (ConnectivityReceiver.isConnected()) {

            makeGetCategoryRequest();
            makeGetSliderRequest();
            make_menu_items();


            makeGetBannerSliderRequest();


            new_products();
            //make_deal_od_the_day();
            make_top_selling();


        }
        else
        {
          //  ((MainActivity) getActivity()).onNetworkConnectionChanged(false);
        }

        //  View_all_deals = (Button) view.findViewById(R.id.view_all_deals);
        //View_all_TopSell = (Button) view.findViewById(R.id.view_all_topselling);
        // Deal_Frame_layout = (FrameLayout) view.findViewById(R.id.deal_frame_layout);
        // Deal_Frame_layout1 = (FrameLayout) view.findViewById(R.id.deal_frame_layout1);
        //Deal_Linear_layout = (LinearLayout) view.findViewById(R.id.deal_linear_layout);


        //Top Selling Poster
        // Top_Selling_Poster = (ImageView) view.findViewById(R.id.top_selling_imageview);

        //Deal Of Day Poster
        //   Deal_Of_Day_poster = (ImageView) view.findViewById(R.id.deal_of_day_imageview);

        //Scroll View
//        scrollView = (ScrollView) view.findViewById(R.id.scroll_view);
//        scrollView.setSmoothScrollingEnabled(true);

        //Search
//        Search_layout = (LinearLayout) view.findViewById(R.id.search_layout);
//        Search_layout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                android.app.Fragment fm = new Search_fragment();
//                android.app.FragmentManager fragmentManager = getFragmentManager();
//                fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
//                        .addToBackStack(null).commit();
//
//            }
//        });
//        //Slider
        imgSlider = (SliderLayout) view.findViewById(R.id.home_img_slider);
        banner_slider = (SliderLayout) view.findViewById(R.id.relative_banner);
        // featuredslider = (SliderLayout) view.findViewById(R.id.featured_img_slider);


        //Catogary Icons
        //rv_items = (RecyclerView) view.findViewById(R.id.rv_home);
        // GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        // rv_items.setLayoutManager(gridLayoutManager);
        // rv_items.setItemAnimator(new DefaultItemAnimator());
        // rv_items.setNestedScrollingEnabled(false);

        //DealOf the Day
        new_products_recycler = (RecyclerView) view.findViewById(R.id.recentproduct_recycler);
        //  GridLayoutManager gridLayoutManager1 = new GridLayoutManager(getActivity(), 2);
        new_products_recycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        new_products_recycler.setItemAnimator(new DefaultItemAnimator());
        new_products_recycler.setNestedScrollingEnabled(false);
       // new_products_recycler.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(1), true));


        //Top Selling Products
        rv_top_selling = (RecyclerView) view.findViewById(R.id.new_product_recycler);
//        GridLayoutManager gridLayoutManager2 = new GridLayoutManager(getActivity(), 2);
        rv_top_selling.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        rv_top_selling.setItemAnimator(new DefaultItemAnimator());
        rv_top_selling.setNestedScrollingEnabled(false);
        //rv_top_selling.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(1), true));



        //make_menu_items Icons
        rv_headre_icons = (RecyclerView) view.findViewById(R.id.home_recycler);
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(),4) {

            @Override
            public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
                LinearSmoothScroller smoothScroller = new LinearSmoothScroller(getActivity()) {
                    private static final float SPEED = 2000f;// Change this value (default=25f)

                    @Override
                    protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
                        return SPEED / displayMetrics.densityDpi;
                    }
                };
                smoothScroller.setTargetPosition(position);
                startSmoothScroll(smoothScroller);
            }
        };
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv_headre_icons.setLayoutManager(layoutManager);
        rv_headre_icons.setHasFixedSize(false);
        rv_headre_icons.setItemViewCacheSize(10);
        rv_headre_icons.setDrawingCacheEnabled(true);
        rv_headre_icons.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_LOW);


//        footer.setOnClickListener( new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                android.app.Fragment fm = new Help_Fragment();
//                android.app.FragmentManager fragmentManager = getFragmentManager();
//                fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
//                        .addToBackStack(null).commit();
//            }
//
//        } );

//        //Call And Whatsapp
//        iv_Call = (ImageView) view.findViewById(R.id.iv_call);
//        iv_Whatspp = (ImageView) view.findViewById(R.id.iv_whatsapp);
//        iv_reviews = (ImageView) view.findViewById(R.id.reviews);
//        iv_share_via = (ImageView) view.findViewById(R.id.share_via);
//
//        iv_Call.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//             Intent intent = new Intent( Intent.ACTION_DIAL );
//             String number = "7617855680";
//             intent.setData( Uri.parse("tel:" +number) );
//             startActivity( intent );
//            }
//        });
//        iv_Whatspp.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String smsNumber = "9889887711";
//                Intent sendIntent = new Intent("android.intent.action.MAIN");
//                sendIntent.setComponent(new ComponentName("com.whatsapp","com.whatsapp.Conversation"));
//                sendIntent.putExtra("jid",     PhoneNumberUtils.stripSeparators(smsNumber)+"@s.whatsapp.net");//phone number without "+" prefix
//                startActivity(sendIntent);
//
//            }
//        });
//        iv_reviews.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                reviewOnApp();
//            }
//        });
//        iv_share_via.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                shareApp();
//
//            }
//        });


        //Recycler View Shop By Catogary
      /*  rv_items.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), rv_items, new RecyclerTouchListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                getid = category_modelList.get(position).getId();
                getcat_title = category_modelList.get(position).getTitle();
                Bundle args = new Bundle();
                Fragment fm = new Product_fragment();
                args.putString("cat_id", getid);
                args.putString("cat_title", getcat_title);
                fm.setArguments(args);
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
                        .addToBackStack(null).commit();

            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));*/


        //Recycler View Menu Products
        rv_headre_icons.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), rv_headre_icons, new RecyclerTouchListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                getid = menu_models.get(position).getId();

                String title=menu_models.get(position).getTitle();
                SubCategory_Fragment fm = new SubCategory_Fragment();

                Intent intent=new Intent(getActivity(),ActivityCategoryProduct.class);
                intent.putExtra("cat_id",getid);
                intent.putExtra( "title" ,title );
                startActivity(intent);
                // args.putString( "" );

            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));

        //Recycler View Deal Of Day
//        rv_deal_of_day.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), rv_deal_of_day, new RecyclerTouchListener.OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//                getid = deal_of_day_models.get(position).getId();
//                Bundle args = new Bundle();
//                Fragment fm = new Product_fragment();
//                args.putString("cat_deal", "2");
//                fm.setArguments(args);
//                FragmentManager fragmentManager = getFragmentManager();
//                fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
//                        .addToBackStack(null).commit();
//
//            }
//
//            @Override
//            public void onLongItemClick(View view, int position) {
//
//            }
//        }));
//        View_all_deals.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Bundle args = new Bundle();
//                Fragment fm = new Product_fragment();
//                args.putString("cat_deal", "2");
//                fm.setArguments(args);
//                FragmentManager fragmentManager = getFragmentManager();
//                fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
//                        .addToBackStack(null).commit();
//
//            }
//        });


        //REcyclerview Top Selling
//        rv_top_selling.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), rv_top_selling, new RecyclerTouchListener.OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//                getid = top_selling_models.get(position).getProduct_id();
//                Bundle args = new Bundle();
//                Fragment fm = new Product_fragment();
//                args.putString("cat_top_selling", "2");
//                fm.setArguments(args);
//               // String as=top_selling_models.get(position).getColor();
//                //Toast.makeText(getActivity(),""+as,Toast.LENGTH_LONG).show();
//                FragmentManager fragmentManager = getFragmentManager();
//                fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
//                        .addToBackStack(null).commit();
//
//            }
//
//            @Override
//            public void onLongItemClick(View view, int position) {
//
//            }
//        }));
//        View_all_TopSell.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Bundle args = new Bundle();
//                Fragment fm = new Product_fragment();
//                args.putString("cat_top_selling", "2");
//                fm.setArguments(args);
//                FragmentManager fragmentManager = getFragmentManager();
//                fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
//                        .addToBackStack(null).commit();
//
//            }
//        });

        return view;
    }

    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    private void makeGetCategoryRequest() {
        ProgressDialog.show();
        String tag_json_obj = "json_category_req";
        isSubcat = false;
        Map<String, String> params = new HashMap<String, String>();
        params.put("parent", "");
        isSubcat = true;
       /* if (parent_id != null && parent_id != "") {
        }*/

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                Url.GET_CATEGORY_URL, params, new com.android.volley.Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                try {
                    ProgressDialog.dismiss();
                    if (response != null && response.length() > 0) {
                        Boolean status = response.getBoolean("responce");
                        if (status) {

                            Gson gson = new Gson();
                            Type listType = new TypeToken<List<Category_model>>() {
                            }.getType();
                            category_modelList = gson.fromJson(response.getString("data"), listType);
                            adapter = new Home_adapter(category_modelList);

                            //   rv_items.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        }
                    }
                } catch (JSONException e) {
                    ProgressDialog.dismiss();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                if (ProgressDialog != null && ProgressDialog.isShowing())
                    ProgressDialog.dismiss();
                  Module module=new Module(getActivity());
                 String msg=module.VolleyErrorMessage(error);
                    Common.ShowHttpErrorMessage(Common.Activity, msg);

//                VolleyLog.d(TAG, "Error: " + error.getMessage());
//                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
//                    Toast.makeText(getActivity(), getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
//                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);

    }

    private void makeGetSliderRequest() {
        JsonArrayRequest req = new JsonArrayRequest(Url.GET_SLIDER_URL,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        try {
                            ArrayList<HashMap<String, String>> listarray = new ArrayList<>();
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = (JSONObject) response.get(i);
                                HashMap<String, String> url_maps = new HashMap<String, String>();
                                url_maps.put("slider_title", jsonObject.getString("slider_title"));
                                url_maps.put("sub_cat", jsonObject.getString("sub_cat"));
                                url_maps.put("slider_image", Url.IMG_SLIDER_URL + jsonObject.getString("slider_image"));
                                listarray.add(url_maps);
                            }
                            for (HashMap<String, String> name : listarray) {
                                CustomSlider textSliderView = new CustomSlider(getActivity());
                                textSliderView.description(name.get("")).image(name.get("slider_image")).setScaleType(BaseSliderView.ScaleType.Fit);
                                textSliderView.bundle(new Bundle());
                                textSliderView.getBundle().putString("extra", name.get("slider_title"));
                                textSliderView.getBundle().putString("extra", name.get("sub_cat"));
                                imgSlider.addSlider(textSliderView);
                                final String sub_cat = (String) textSliderView.getBundle().get("extra");
//                                textSliderView.setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
//                                    @Override
//                                    public void onSliderClick(BaseSliderView slider) {
//                                        //   Toast.makeText(getActivity(), "" + sub_cat, Toast.LENGTH_SHORT).show();
//                                        Bundle args = new Bundle();
//                                        android.app.Fragment fm = new Product_fragment();
//                                        args.putString("id", sub_cat);
//                                        //Toast.makeText(getActivity(),""+sub_cat, Toast.LENGTH_LONG).show();
//                                        fm.setArguments(args);
//                                        android.app.FragmentManager fragmentManager = getFragmentManager();
//                                        fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
//                                                .addToBackStack(null).commit();
//                                    }
//                                });

                                imgSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
                                imgSlider.setCustomAnimation(new DescriptionAnimation());
                                imgSlider.setDuration(2500);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(),
                                    "Error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Module module=new Module(getActivity());
                String msg=module.VolleyErrorMessage(error);
                Common.ShowHttpErrorMessage(Common.Activity, msg);

//                VolleyLog.d(TAG, "Error: " + error.getMessage());
//                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
//                    Toast.makeText(getActivity(), getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
//                }
            }
        });
        AppController.getInstance().addToRequestQueue(req);

    }

    private void makeGetBannerSliderRequest() {
        JsonArrayRequest req = new JsonArrayRequest(Url.GET_BANNER_URL,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        try {
                            ArrayList<HashMap<String, String>> listarray = new ArrayList<>();
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = (JSONObject) response.get(i);
                                HashMap<String, String> url_maps = new HashMap<String, String>();
                                url_maps.put("slider_title", jsonObject.getString("slider_title"));
                                url_maps.put("sub_cat", jsonObject.getString("sub_cat"));
                                url_maps.put("slider_image", Url.IMG_SLIDER_URL + jsonObject.getString("slider_image"));
                                listarray.add(url_maps);
                            }
                            for (HashMap<String, String> name : listarray) {
                                CustomSlider textSliderView = new CustomSlider(getActivity());
                                textSliderView.description(name.get("")).image(name.get("slider_image")).setScaleType(BaseSliderView.ScaleType.Fit);
                                textSliderView.bundle(new Bundle());
                                textSliderView.getBundle().putString("extra", name.get("slider_title"));
                                textSliderView.getBundle().putString("extra", name.get("sub_cat"));
                                banner_slider.addSlider(textSliderView);
                                final String sub_cat = (String) textSliderView.getBundle().get("extra");
//                                textSliderView.setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
//                                    @Override
//                                    public void onSliderClick(BaseSliderView slider) {
//                                        //   Toast.makeText(getActivity(), "" + sub_cat, Toast.LENGTH_SHORT).show();
//                                        Bundle args = new Bundle();
//                                        Fragment fm = new Product_fragment();
//                                        args.putString("id", sub_cat);
//                                        fm.setArguments(args);
//                                        FragmentManager fragmentManager = getFragmentManager();
//                                        fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
//                                                .addToBackStack(null).commit();
//                                    }
//                                });

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(),
                                    "Error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Module module=new Module(getActivity());
                String msg=module.VolleyErrorMessage(error);
                Common.ShowHttpErrorMessage(Common.Activity, msg);

//                VolleyLog.d(TAG, "Error: " + error.getMessage());
//                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
//                    Toast.makeText(getActivity(), getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                //}
            }
        });
        AppController.getInstance().addToRequestQueue(req);

    }


    private void new_products() {
        String tag_json_obj = "json_category_req";
        isSubcat = false;
        Map<String, String> params = new HashMap<String, String>();
        params.put("parent", "");
        isSubcat = true;
       /* if (parent_id != null && parent_id != "") {
        }*/

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                Url.GET_NEW_PRODUCTS, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("sels", response.toString());
                //Toast.makeText(getActivity(),""+response.toString(),Toast.LENGTH_LONG).show();

                try {
                    if (response != null && response.length() > 0) {
                        Boolean status = response.getBoolean("responce");
                        if (status) {
                            Gson gson = new Gson();
                            Type listType = new TypeToken<List<Deal_Of_Day_model>>() {
                            }.getType();
                            deal_of_day_models = gson.fromJson(response.getString("new_product"), listType);
                            deal_ofDay_adapter = new Deal_OfDay_Adapter(deal_of_day_models,getActivity());
                            new_products_recycler.setAdapter(deal_ofDay_adapter);
                            deal_ofDay_adapter.notifyDataSetChanged();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Module module=new Module(getActivity());
                String msg=module.VolleyErrorMessage(error);
                Common.ShowHttpErrorMessage(Common.Activity, msg);
//                Toast.makeText(getActivity(), ""+msg, Toast.LENGTH_SHORT).show();
//                VolleyLog.d(TAG, "Error: " + error.getMessage());
//                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
//                    Toast.makeText(getActivity(), getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
//                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);

    }


    private void make_menu_items() {
        String tag_json_obj = "json_category_req";
        isSubcat = false;
        Map<String, String> params = new HashMap<String, String>();
        params.put("parent", "");
        isSubcat = true;
       /* if (parent_id != null && parent_id != "") {
        }*/

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                Url.GET_CATEGORY_URL, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {
                    if (response != null && response.length() > 0) {
                        Boolean status = response.getBoolean("responce");
                        if (status) {
                            Gson gson = new Gson();
                            Type listType = new TypeToken<List<Home_Icon_model>>() {
                            }.getType();
                            menu_models = gson.fromJson(response.getString("data"), listType);
                            menu_adapter = new Home_Icon_Adapter(menu_models);
                            rv_headre_icons.setAdapter(menu_adapter);
                            menu_adapter.notifyDataSetChanged();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                Module module=new Module(getActivity());
                String msg=module.VolleyErrorMessage(error);
                Toast.makeText(getActivity(), ""+msg, Toast.LENGTH_SHORT).show();
//                VolleyLog.d(TAG, "Error: " + error.getMessage());
//                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
//                    Toast.makeText(getActivity(), getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
//                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);

    }


    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {
        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        //Defining retrofit api service

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    private void make_top_selling() {
        String tag_json_obj = "json_category_req";
        isSubcat = false;
        Map<String, String> params = new HashMap<String, String>();
        params.put("parent", "");
        isSubcat = true;
       /* if (parent_id != null && parent_id != "") {
        }*/

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                Url.GET_TOP_SELLING_PRODUCTS, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("sels", response.toString());
                //Toast.makeText(getActivity(),""+response.toString(),Toast.LENGTH_LONG).show();

                try {
                    if (response != null && response.length() > 0) {
                        Boolean status = response.getBoolean("responce");
                        if (status) {
                            Gson gson = new Gson();
                            Type listType = new TypeToken<List<Top_Selling_model>>() {
                            }.getType();
                            top_selling_models = gson.fromJson(response.getString("top_selling_product"), listType);
                            top_selling_adapter = new Top_Selling_Adapter(getActivity(),top_selling_models);
                            rv_top_selling.setAdapter(top_selling_adapter);
                            top_selling_adapter.notifyDataSetChanged();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Module module=new Module(getActivity());
                String msg=module.VolleyErrorMessage(error);
                Toast.makeText(getActivity(), ""+msg, Toast.LENGTH_SHORT).show();
//                VolleyLog.d(TAG, "Error: " + error.getMessage());
//                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
//                    Toast.makeText(getActivity(), getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
//                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);

    }

}
