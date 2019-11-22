package in.mapbazar.mapbazar.Fragment;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.loopeer.itemtouchhelperextension.ItemTouchHelperExtension;

import org.json.JSONArray;
import org.json.JSONObject;

import in.mapbazar.mapbazar.ActivityPayment;
import in.mapbazar.mapbazar.AppController;
import in.mapbazar.mapbazar.Model.PinModel;
import in.mapbazar.mapbazar.Model.ShippinModel;
import in.mapbazar.mapbazar.Modules.Module;
import in.mapbazar.mapbazar.RegisterActivity;
import in.mapbazar.mapbazar.ActivityLoginRegister;
import in.mapbazar.mapbazar.Adapter.ShoppingCartAdapter;
import in.mapbazar.mapbazar.Model.ProductListModel.ColorItem;
import in.mapbazar.mapbazar.Model.ProductListModel.ProductAttributeItem;
import in.mapbazar.mapbazar.Model.ProductListModel.ProductImageItem;
import in.mapbazar.mapbazar.Model.ProductListModel.ProductItem;
import in.mapbazar.mapbazar.Model.ProductListModel.ProductSizeItem;
import in.mapbazar.mapbazar.R;
import in.mapbazar.mapbazar.SwipeRow.DividerItemDecoration;
import in.mapbazar.mapbazar.SwipeRow.ItemTouchHelperCallback;
import in.mapbazar.mapbazar.Utili.Common;
import in.mapbazar.mapbazar.Utili.Url;
import in.mapbazar.mapbazar.View.CustomTextView;
import in.mapbazar.mapbazar.View.DialogUtils;
import in.mapbazar.mapbazar.callback.CallbackInternate;
import in.mapbazar.mapbazar.callback.CallbackMessage;
import in.mapbazar.mapbazar.connection.API;
import in.mapbazar.mapbazar.connection.RestAdapter;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.mapbazar.mapbazar.util.CustomVolleyJsonRequest;
import in.mapbazar.mapbazar.util.DatabaseCartHandler;
import in.mapbazar.mapbazar.util.Session_management;
import retrofit2.Call;
import retrofit2.Callback;


public class DeliveryShippingFragment extends Fragment implements View.OnClickListener{

    double gms=0;
    double pcs=0;
    List<ShippinModel> modelList;
    List<PinModel> pinList;
    String[] sp=null;
    double d=0;
    double sum_qty=0;
    float tot=0;
    double mrp=0;
    double price=0;
    double discount=0;
    double deli_chrgs=0;


    double del=0;
    LinearLayout btn_order_now;
CustomTextView recivername,mobileno,pincode,address,tvItems,tvMrp,tvDiscount,tvDelivary,tvSubTotal;
    Dialog ProgressDialog;
    DatabaseCartHandler db_cart;
    Session_management sessionManagement;
    String rv_address,rv_location_id,rv_name,rv_pin,rv_house,rv_society,rv_phone,rv_deliCharges,rv_store_id,user_id;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_delivery_shipping, container, false);
            initComponents(view);

        getShippingCharges(rv_pin);
        recivername.setText(rv_name);
        mobileno.setText(rv_phone);
        pincode.setText(rv_pin);
        address.setText(rv_address);
        tvItems.setText(String.valueOf(db_cart.getCartCount()));
        mrp=Double.parseDouble(getTotMRp());
        price=Double.parseDouble(db_cart.getTotalAmount());
        discount=mrp-price;
        tvMrp.setText(getActivity().getResources().getString(R.string.currency)+mrp);
      //  deli_chrgs=setShippinCharge();
        tvDiscount.setText("-"+getActivity().getResources().getString(R.string.currency)+discount);
        tvSubTotal.setText(getActivity().getResources().getString(R.string.currency)+price);
//        tvDelivary.setText(getActivity().getResources().getString(R.string.currency)+deli_chrgs);

        return view;
    }

    private void initComponents(View view) {

        recivername=(CustomTextView)view.findViewById(R.id.recivername);
        mobileno=(CustomTextView)view.findViewById(R.id.mobileno);
        pincode=(CustomTextView)view.findViewById(R.id.pincode);
        address=(CustomTextView)view.findViewById(R.id.address);
        tvItems=(CustomTextView)view.findViewById(R.id.tvItems);
        tvMrp=(CustomTextView)view.findViewById(R.id.tvMrp);
        tvDiscount=(CustomTextView)view.findViewById(R.id.tvDiscount);
        tvDelivary=(CustomTextView)view.findViewById(R.id.tvDelivary);
        tvSubTotal=(CustomTextView)view.findViewById(R.id.tvSubTotal);
        btn_order_now=(LinearLayout) view.findViewById(R.id.btn_order_now);

        ProgressDialog = new Dialog(getActivity(), android.R.style.Theme_Translucent_NoTitleBar);
        ProgressDialog.setContentView(R.layout.progressbar);
        ProgressDialog.setCancelable(false);
        db_cart = new DatabaseCartHandler(getActivity());
        sessionManagement = new Session_management(getActivity());
        modelList=new ArrayList<>();
        Bundle bundle=getArguments();
        rv_address=bundle.getString("address");
        rv_location_id=bundle.getString("location_id");
        rv_name=bundle.getString("name");
        rv_pin=bundle.getString("pin");
        user_id=bundle.getString("user_id");
        //rv_house=bundle.getString("house");
       // rv_society=bundle.getString("society");
        rv_phone=bundle.getString("phone");
//        rv_deliCharges=bundle.getString("deli_charges");
       // rv_store_id=bundle.getString("store_id");

        btn_order_now.setOnClickListener(this);

    }


    @Override
    public void onClick(View view) {
        int id=view.getId();
        if(id == R.id.btn_order_now)
        {



            //Toast.makeText(getActivity(),"d-- "+d+"\n deli-- "+deli_chrgs,Toast.LENGTH_LONG).show();
            Intent intent=new Intent(getActivity(), ActivityPayment.class);
            intent.putExtra("location",rv_location_id);
            intent.putExtra("shipping_charge",String.valueOf(deli_chrgs));
            intent.putExtra("user_id",user_id);
            startActivity(intent);

           // Toast.makeText(getActivity(),"name:- "+rv_name+"\n mobile :- "+rv_phone+"\n pin :- "+rv_pin+"\naddress :- "+rv_address+"\n deli :- "+d,Toast.LENGTH_LONG).show();

    }
    }

    private void getShippingCharges(String rv_pin) {

        ProgressDialog.show();
        String json_tag="json_ship";
        HashMap<String,String> map=new HashMap<>();
        map.put("pincode",rv_pin);
        pinList=new ArrayList<>();

        CustomVolleyJsonRequest customVolleyJsonRequest=new CustomVolleyJsonRequest(Request.Method.POST, Url.GET_PINCODES, map, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    if (ProgressDialog != null && ProgressDialog.isShowing())
                        ProgressDialog.dismiss();

                    JSONArray data_arr = response.getJSONArray("pincodedetail");

                    for (int i = 0; i < data_arr.length(); i++) {
                        JSONObject object = data_arr.getJSONObject(i);
                        PinModel pinModel = new PinModel();
                        pinModel.setPicode(object.getString("pincode"));
                        pinModel.setArea(object.getString("area"));
                        String str_array = object.getString("shipping_amt");
                        String s = str_array.substring(0, (str_array.length() - 1));

                        JSONArray array = new JSONArray(str_array);
                        //Toast.makeText(ActivityAddAddress.this, "asdas \n"+array.get(0).toString(), Toast.LENGTH_LONG).show();
                        //JsonArray array=object.getAsJsonArray("shipping_amt");

                        for (int j = 0; j < array.length(); j++) {
                            JSONObject obj = array.getJSONObject(j);
                            //Toast.makeText(ActivityAddAddress.this,""+obj.get("max_quantity").getAsString(),Toast.LENGTH_LONG).show();
                            ShippinModel model = new ShippinModel();
                            model.setMin_quantity(obj.getString("min_quantity"));
                            model.setMax_quantity(obj.getString("max_quantity"));
                            model.setShipping_charges(obj.getString("shipping_charges"));

                            modelList.add(model);

                        }
                        pinModel.setShippinModelList(modelList);

                        pinList.add(pinModel);

                    }

                    getDataShip();

                    //Toast.makeText(ActivityPayment.this,""+sp[0].toString(),Toast.LENGTH_LONG).show();
                    //ArrayAdapter<String> adapter = new ArrayAdapter<String>(ActivityPayment.this, android.R.layout.simple_list_item_1, sp);
                    // act_pincode.setAdapter(adapter);
                    // List<ShippinModel> models=pinList.get(0).getShippinModelList();
                    //Toast.makeText(ActivityAddAddress.this,"asdasd"+pinList.get(0).getShippinModelList().get(0).getShipping_charges().toString(),Toast.LENGTH_LONG).show();






                }
                catch (Exception ex)
                {
                    ProgressDialog.dismiss();
                    Toast.makeText(getActivity(),""+ex.getMessage(),Toast.LENGTH_LONG).show();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ProgressDialog.dismiss();
                Module module=new Module(getActivity());
                String msg=module.VolleyErrorMessage(error);
                Toast.makeText(getActivity(),""+msg,Toast.LENGTH_LONG).show();

            }
        });

        AppController.getInstance().addToRequestQueue(customVolleyJsonRequest,json_tag);

    }

    public String getTotMRp()
    {
        ArrayList<HashMap<String, String>> list = db_cart.getCartAll();
        float sum=0;
        for(int i=0;i<list.size();i++)
        {
            final HashMap<String, String> map = list.get(i);

            float q=Float.parseFloat(map.get("qty"));
            float m=Float.parseFloat(map.get("mrp"));

            sum=sum+(q*m);
            //   Toast.makeText(getActivity(),""+q+"\n"+m,Toast.LENGTH_LONG).show();

        }
        if(sum!=0)
        {
            return String.valueOf(sum);
        }
        else
            return "0";
        //Toast.makeText(getActivity(),""+sum,Toast.LENGTH_LONG).show();
    }

    private void getDataShip()
    {

        ArrayList<HashMap<String, String>> map = db_cart.getCartAll();




        for (int i = 0; i < map.size(); i++) {


            HashMap<String,String> p_map=map.get(i);

            String s=p_map.get("unit").toString();

            String[] st=getType(s);

            if(st[1].equals("k"))
            {
                del= del+Double.parseDouble(st[0])*Double.parseDouble(p_map.get("qty"));
            }
            else if(st[1].equals("p"))
            {
                pcs=pcs+Double.parseDouble(st[0])*Double.parseDouble(p_map.get("qty"));
            }
            else if(st[1].equals("g"))
            {
                //  double gm=Double.parseDouble(st[0])/1000;
                gms=gms+Double.parseDouble(st[0])*Double.parseDouble(p_map.get("qty"));
            }
            //double sd=getQtySum(productItem.getProductAttribute().get(0).getAttributeName());
            //sum_qty=sum_qty+sd;

            //jsonArray.add(product);
        }



     double amt=Double.parseDouble(db_cart.getTotalAmount());
        if(amt<=500)
        {
            d=setShippinCharge();
        }
        else
        {
            d=0;
        }

        deli_chrgs=d;
       tvDelivary.setText(getActivity().getResources().getString(R.string.currency)+d);
        tvSubTotal.setText(getActivity().getResources().getString(R.string.currency)+price+" + "+getActivity().getResources().getString(R.string.currency)+d+
                " = "+getActivity().getResources().getString(R.string.currency)+(price+d));

        //tot= DeliveryShippingFragment.subtotal+(float) d;
        //txt_qty.setText(""+Common.list_ProductItem.size());
        // txt_subtotal.setText(""+ DeliveryShippingFragment.subtotal);
        //txt_total.setText(""+tot);
    }

    public String[] getType(String atr)
    {
        String[] str=new String[2];
        double atr_value=0;
        String s="";
        if(atr.contains("kg") || atr.contains("Kg"))
        {
            atr_value=Double.parseDouble(atr.substring(0,(atr.length()-2)));
            s="k";
        }
        else if(atr.contains("gm") || atr.contains("Gm") || atr.contains("gms") || atr.contains("Gms")|| atr.contains("g") || atr.contains("G"))
        {
            if(atr.contains("gms") || atr.contains("Gms"))
            {
                atr_value=Double.parseDouble(atr.substring(0,(atr.length()-3)));
                s="g";
            }
            else if(atr.contains("gm") || atr.contains("Gm"))
            {
                atr_value=Double.parseDouble(atr.substring(0,(atr.length()-2)));
                s="g";
            }
            else if(atr.contains("g") || atr.contains("G"))
            {
                atr_value=Double.parseDouble(atr.substring(0,(atr.length()-1)));
                s="g";
            }
        }
        else if(atr.contains("Piece") || atr.contains("Pieces") || atr.contains("piece") || atr.contains("pieces") || atr.contains("pic")|| atr.contains("Pic")|| atr.contains("pcs")|| atr.contains("Pcs"))
        {
            if(atr.contains("Pieces") || atr.contains("pieces"))
            {
                atr_value=Double.parseDouble(atr.substring(0,(atr.length()-6)));
                s="p";
            }
            else if(atr.contains("Piece") || atr.contains("piece"))
            {
                atr_value=Double.parseDouble(atr.substring(0,(atr.length()-5)));
                s="p";
            }
            else if(atr.contains("Pic") || atr.contains("pic"))
            {
                atr_value=Double.parseDouble(atr.substring(0,(atr.length()-3)));
                s="p";
            }
            else if(atr.contains("pcs") || atr.contains("Pcs"))
            {
                atr_value=Double.parseDouble(atr.substring(0,(atr.length()-3)));
                s="p";
            }
            else
            {
                atr_value=Double.parseDouble(atr.substring(0,(atr.length()-3)));
                s="p";
            }

        }
        else if(atr.contains("Pack") || atr.contains("pack"))
        {
            atr_value=Double.parseDouble(atr.substring(0,(atr.length()-4)));
            s="p";
        }


        str[0]=String.valueOf(atr_value);
        str[1]=s;
        return str;
    }

    public double setShippinCharge()
    {

        double max_price= 0;
        double ship_amt=0;
        double dec=gms/1000;
        double min=0;
        double max=0;
        del=del+dec;

        if( max_price>=500)
        {
            ship_amt=0;
        }
        else
        {
            if(del==0)
            {
                if(pcs<=2)
                {
                    ship_amt=10;
                }
                else if(pcs>=2 && pcs<=5)
                {
                    ship_amt=20;
                }
                else if(pcs>5)
                {
                    ship_amt=20;
                }
            }
            else
            {
                ship_amt = Double.parseDouble(modelList.get(0).getShipping_charges());
                //double ship_amt = 0;
                for(int i=0;i<modelList.size();i++)
                {
                    min=Double.parseDouble(modelList.get(i).getMin_quantity());
                    max=Double.parseDouble(modelList.get(i).getMax_quantity());

                    if(del>=min && del<=max) {
                        ship_amt = Double.parseDouble(modelList.get(i).getShipping_charges());
                        break;
                    }

                }

            }




        }

        return ship_amt;
    }


}
