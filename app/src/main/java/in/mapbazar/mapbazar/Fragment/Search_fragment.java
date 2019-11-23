package in.mapbazar.mapbazar.Fragment;

import android.app.Dialog;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.mapbazar.mapbazar.Adapter.Product_adapter;
import in.mapbazar.mapbazar.Adapter.Search_adapter;
import in.mapbazar.mapbazar.Adapter.SuggestionAdapter;
import in.mapbazar.mapbazar.AppController;
import in.mapbazar.mapbazar.MainActivity;
import in.mapbazar.mapbazar.Model.Product_model;
import in.mapbazar.mapbazar.Modules.Module;
import in.mapbazar.mapbazar.R;
import in.mapbazar.mapbazar.Utili.Url;
import in.mapbazar.mapbazar.util.ConnectivityReceiver;
import in.mapbazar.mapbazar.util.CustomVolleyJsonRequest;


public class Search_fragment extends Fragment {

    private static String TAG = Search_fragment.class.getSimpleName();
    //    String[] fruits = {"MIlk butter & cream", "Bread Buns & Pals", "Dals Mix Pack", "buns-pavs", "cakes", "Channa Dal", "Toor Dal", "Wheat Atta"
//            , "Beson", "Almonds", "Packaged Drinking", "Cola drinks", "Other soft drinks", "Instant Noodles", "Cup Noodles", "Salty Biscuits", "cookie", "Sanitary pads", "sanitary Aids"
//            , "Toothpaste", "Mouthwash", "Hair oil", "Shampoo", "Pure & pomace olive", "ICE cream", "Theme Egg", "Amul Milk", "AMul Milk Pack power", "kaju pista dd"};
    private AutoCompleteTextView acTextView;
    private RelativeLayout btn_search;
    private RecyclerView rv_search;
    Module module = new Module(getActivity());
    private List<Product_model> modelList = new ArrayList<>();
    private Product_adapter adapter_product;
    Dialog loadingBar ;
    public Search_fragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadingBar=new Dialog(getActivity(),android.R.style.Theme_Translucent_NoTitleBar);
        loadingBar.setContentView( R.layout.progressbar );
        loadingBar.setCanceledOnTouchOutside(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        ((MainActivity) getActivity()).setTitle(getResources().getString(R.string.search));
        loadingBar=new Dialog(getActivity(),android.R.style.Theme_Translucent_NoTitleBar);
        loadingBar.setContentView( R.layout.progressbar );
        loadingBar.setCanceledOnTouchOutside(false);

        acTextView = (AutoCompleteTextView) view.findViewById(R.id.et_search);


        acTextView.setAdapter(new SuggestionAdapter(getActivity(), acTextView.getText().toString()));

        acTextView.setTextColor(getResources().getColor(R.color.green));
        btn_search = (RelativeLayout) view.findViewById(R.id.btn_search);
        rv_search = (RecyclerView) view.findViewById(R.id.rv_search);
        //rv_search.setLayoutManager(new LinearLayoutManager(getActivity()));
        GridLayoutManager gridLayoutManager = new GridLayoutManager( getActivity(),2 );
        rv_search.setLayoutManager( gridLayoutManager );

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String get_search_txt = "%" + acTextView.getText().toString() + "%";
                if (TextUtils.isEmpty(get_search_txt)) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.enter_keyword), Toast.LENGTH_SHORT).show();
                } else {
                    if (ConnectivityReceiver.isConnected()) {
                        makeGetProductRequest(get_search_txt);
                    } else {
                        //((MainActivity) getActivity()).onNetworkConnectionChanged(false);
                    }
                }

            }
        });

//        rv_search.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), rv_search, new RecyclerTouchListener.OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//
//                Fragment details_fragment=new Details_Fragment();
//                // bundle.putString("data",as);
//                Bundle args = new Bundle();
//
//                //Intent intent=new Intent(context, Product_details.class);
//                args.putString("cat_id", modelList.get(position).getCategory_id());
//                args.putString("product_id",modelList.get(position).getProduct_id());
//                args.putString("product_image",modelList.get(position).getProduct_image());
//                args.putString("product_name",modelList.get(position).getProduct_name());
//                args.putString("product_description",modelList.get(position).getProduct_description());
//                args.putString("stock",modelList.get(position).getIn_stock());
////                args.putString("product_size",modelList.get(position).getSize());
//                args.putString("product_color",modelList.get( position).getColor());
//                args.putString("price",modelList.get(position).getPrice());
//                args.putString("mrp",modelList.get(position).getMrp());
//                args.putString( "unit_price",modelList.get( position ).getPrice());
//                args.putString("unit_value",modelList.get(position).getUnit_value());
//                args.putString("unit",modelList.get(position).getUnit());
//                args.putString("product_attribute",String.valueOf(module.getAttribute(modelList.get(position).getProduct_attribute())));
//                args.putString("rewards",modelList.get(position).getRewards());
//                args.putString("increment",modelList.get(position).getIncreament());
//                args.putString("title",modelList.get(position).getTitle());
//                details_fragment.setArguments(args);
//
//
//                // Toast.makeText(getActivity(),"col"+product_modelList.get(position).getColor(),Toast.LENGTH_LONG).show();
//
//
//
//                FragmentManager fragmentManager=getFragmentManager();
//                fragmentManager.beginTransaction().replace(R.id.contentPanel,details_fragment)
//
//                        .addToBackStack(null).commit();
//
//
//            }
//
//            @Override
//            public void onLongItemClick(View view, int position) {
//
//            }
//        }));
        return view;
    }


        private void makeGetProductRequest (String search_text){

            // Tag used to cancel the request
            String tag_json_obj = "json_product_req";


            Map<String, String> params = new HashMap<String, String>();
            params.put("search", search_text);

            CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest( Request.Method.POST,
                   Url.GET_PRODUCT_URL, params, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    Log.d("search", response.toString());

                    try {
                        Boolean status = response.getBoolean("responce");
                        if (status) {

                            Gson gson = new Gson();
                            Type listType = new TypeToken<List<Product_model>>() {
                            }.getType();

                            modelList = gson.fromJson(response.getString("data"), listType);

                            adapter_product = new Product_adapter(modelList, getActivity());
                            rv_search.setAdapter(adapter_product);

                            adapter_product.notifyDataSetChanged();


                            if (getActivity() != null) {
                                if (modelList.isEmpty()) {
                                    Toast.makeText(getActivity(), getResources().getString(R.string.no_rcord_found), Toast.LENGTH_SHORT).show();
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
                    String errormsg = Module.VolleyErrorMessage(error);
                    Toast.makeText( getActivity(),""+ errormsg,Toast.LENGTH_LONG ).show();
                }
            });

            // Adding request to request queue
            AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
        }

    }

