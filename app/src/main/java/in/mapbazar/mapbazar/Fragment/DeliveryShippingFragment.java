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

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.loopeer.itemtouchhelperextension.ItemTouchHelperExtension;

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
import in.mapbazar.mapbazar.util.DatabaseCartHandler;
import in.mapbazar.mapbazar.util.Session_management;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DeliveryShippingFragment extends Fragment implements View.OnClickListener{

    LinearLayout btn_order_now;
CustomTextView recivername,mobileno,pincode,address,tvItems,tvMrp,tvDiscount,tvDelivary,tvSubTotal;
    Dialog ProgressDialog;
    DatabaseCartHandler db_cart;
    Session_management sessionManagement;
    String rv_address,rv_location_id,rv_name,rv_pin,rv_house,rv_society,rv_phone,rv_deliCharges,rv_store_id;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_delivery_shipping, container, false);
            initComponents(view);


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

        Bundle bundle=getArguments();
        rv_address=bundle.getString("address");
        rv_location_id=bundle.getString("location_id");
        rv_name=bundle.getString("name");
        rv_pin=bundle.getString("pin");
        rv_house=bundle.getString("house");
        rv_society=bundle.getString("society");
        rv_phone=bundle.getString("phone");
        rv_deliCharges=bundle.getString("deli_charges");
        rv_store_id=bundle.getString("store_id");

        btn_order_now.setOnClickListener(this);

    }


    @Override
    public void onClick(View view) {
        int id=view.getId();
        if(id == R.id.btn_order_now)
        {
            Toast.makeText(getActivity(),"address :- "+rv_address+"\n l_id:-- "+rv_location_id+"\n name:-- "+rv_name+
                    "\n pin:-- "+rv_pin+"\n house:-- "+rv_house+"\n society:-- "+rv_phone+"\n charges:-- "+rv_deliCharges+"\n s_id:-- "+rv_store_id,Toast.LENGTH_LONG).show();
        }
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

}
