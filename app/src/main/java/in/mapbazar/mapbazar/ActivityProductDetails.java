package in.mapbazar.mapbazar;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.mapbazar.mapbazar.Adapter.ProductVariantAdapter;
import in.mapbazar.mapbazar.Adapter.RelatedProductAdapter;
import in.mapbazar.mapbazar.Model.ProductVariantModel;
import in.mapbazar.mapbazar.Model.RelatedProductModel;
import in.mapbazar.mapbazar.Utili.Common;
import in.mapbazar.mapbazar.Utili.Url;
import in.mapbazar.mapbazar.View.CustomTextView;
import in.mapbazar.mapbazar.util.CustomVolleyJsonRequest;
import in.mapbazar.mapbazar.util.DatabaseCartHandler;
import in.mapbazar.mapbazar.util.DatabaseHandlerWishList;

import static in.mapbazar.mapbazar.Utili.Url.IMG_PRODUCT_URL;

public class ActivityProductDetails extends AppCompatActivity implements View.OnClickListener {

    ArrayList<ProductVariantModel> variantList;
    ProductVariantAdapter productVariantAdapter;
    Context context;
    DatabaseCartHandler db_cart;
    DatabaseHandlerWishList db_wish;
    String atr_id="";
    String atr_product_id="";
    String attribute_name="";
    String attribute_value="";
    String attribute_mrp="";
    String attribute_reward="";
    int status=0;
    private TextView dialog_unit_type,dialog_txtId,dialog_txtVar;
    //Activity activity;
    SliderLayout img_slider;
    ImageView wish_before,wish_after ;
    CustomTextView details_product_name,details_product_price,details_product_mrp,details_product_per,
            details_product_description,product_rate,unit_type,txt_cart_cout ,details_product_rewards;
    ElegantNumberButton product_qty;
    RecyclerView related_recycler;
    Button btn_add, btn_Add_to_cart;
    RelativeLayout ly_back,layout_cart_cout,rel_variant,layout_cart;
    Dialog progressDialog;
    String name,images,cat_id,p_id,desc,in_stock,stock,p_status,price,mrp,unit_value,unit,attr,rewards,increment,title;

    RelatedProductAdapter relatedProductAdapter ;
    ArrayList<RelatedProductModel>relatedProductModels ;

    HashMap<String,String> p_map=new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);


        initData();
        makeRelatedProductRequest( cat_id );
        useSlider(images);

if (db_wish.isInWishtable( p_id ))
{
    wish_before.setVisibility( View.GONE );
    wish_after.setVisibility( View.VISIBLE );
}
else
{
    wish_before.setVisibility( View.VISIBLE );
    wish_after.setVisibility( View.GONE );
}

if (db_cart.isInCart( p_id ))
{
    btn_add.setVisibility( View.GONE );
    product_qty.setNumber( db_cart.getCartItemQty( p_id ) );
    product_qty.setVisibility( View.VISIBLE );
}
else
{
    btn_add.setVisibility( View.VISIBLE );
    product_qty.setVisibility( View.GONE );
}



        details_product_name.setText(name);
        details_product_description.setText(desc);
//        details_product_price.setText(getResources().getString(R.string.currency)+price);
//        details_product_mrp.setText(getResources().getString(R.string.currency)+mrp);
//        details_product_per.setText(""+getDiscount(price,mrp)+"% OFF");

        if(attr.equals("[]"))
        {
            details_product_price.setText(getResources().getString(R.string.currency)+price);
            double pp = Double.parseDouble( price );
            double mm = Double.parseDouble( mrp );
            if (mm>pp)
            {
                details_product_mrp.setText(getResources().getString(R.string.currency)+mrp);
                details_product_per.setText(""+getDiscount(price,mrp)+"% OFF");
            }
            else
            {
                details_product_mrp.setVisibility( View.GONE );
                details_product_per.setVisibility( View.GONE );
            }


            status=1;


            details_product_rewards.setText( rewards );
            product_rate.setVisibility(View.VISIBLE);
            //  Toast.makeText(getActivity(),""+atr,Toast.LENGTH_LONG).show();
            product_rate.setText(unit_value+unit);
            //txtTotal.setText("\u20B9"+String.valueOf(db_cart.getTotalAmount()));
        }

        else
        {

            rel_variant.setVisibility(View.VISIBLE);
            status=2;
            JSONArray jsonArr = null;
            try {

                jsonArr = new JSONArray(attr);

                ProductVariantModel model=new ProductVariantModel();
                JSONObject jsonObj = jsonArr.getJSONObject(0);
                atr_id=jsonObj.getString("id");
                atr_product_id=jsonObj.getString("product_id");
                attribute_name=jsonObj.getString("attribute_name");
                attribute_value=jsonObj.getString("attribute_value");
                attribute_mrp=jsonObj.getString("attribute_mrp");
                attribute_reward=jsonObj.getString( "rewards" );



                //     arrayList.add(new AttributeModel(atr_id,product_id,attribute_name,attribute_value));

                //Toast.makeText(getActivity(),"id "+atr_id+"\n p_id "+product_id+"\n atr_name "+attribute_name+"\n atr_value "+attribute_value,Toast.LENGTH_LONG).show();


                String atr_price=String.valueOf(attribute_value);
                String atr_mrp=String.valueOf(attribute_mrp);
                double a_mrp = Double.parseDouble( atr_mrp );
                double a_price=Double.parseDouble( atr_price );


                details_product_price.setText(getResources().getString(R.string.currency)+attribute_value);

                if (a_mrp>a_price) {
                    details_product_mrp.setText( getResources().getString( R.string.currency ) + attribute_mrp );
                    details_product_per.setText( "" + getDiscount( price, mrp ) + "% OFF" );
                }
                else
                {
                    details_product_per.setVisibility( View.GONE );
                    details_product_mrp.setVisibility( View.GONE );
                }
                dialog_txtId.setText(String.valueOf(atr_id.toString()+"@"+"0"));
                dialog_txtVar.setText(attribute_value+"@"+attribute_name+"@"+attribute_mrp);
                dialog_unit_type.setText("\u20B9"+attribute_value+"/"+attribute_name);
                //  holder.txtTotal.setText("\u20B9"+String.valueOf(list_atr_value.get(0).toString()));

                details_product_rewards.setText( attribute_reward );

            } catch (JSONException e) {
                e.printStackTrace();
            }



        }

        rel_variant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //      final Product_model mList = modelList.get(position);
                AlertDialog.Builder builder=new AlertDialog.Builder(ActivityProductDetails.this);
                LayoutInflater layoutInflater=(LayoutInflater)ActivityProductDetails.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View row=layoutInflater.inflate(R.layout.dialog_vairant_layout,null);
                variantList.clear();
                String atr=String.valueOf(attr);
                JSONArray jsonArr = null;
                try {

                    jsonArr = new JSONArray(atr);
                    for (int i = 0; i < jsonArr.length(); i++)
                    {
                        ProductVariantModel model=new ProductVariantModel();
                        JSONObject jsonObj = jsonArr.getJSONObject(i);
                        String atr_id=jsonObj.getString("id");
                        String atr_product_id=jsonObj.getString("product_id");
                        String attribute_name=jsonObj.getString("attribute_name");
                        String attribute_value=jsonObj.getString("attribute_value");
                        String attribute_mrp=jsonObj.getString("attribute_mrp");
                        String attribute_reward = jsonObj.getString( "rewards" );


                        model.setId(atr_id);
                        model.setProduct_id(atr_product_id);
                        model.setAttribute_value(attribute_value);
                        model.setAttribute_name(attribute_name);
                        model.setAttribute_mrp(attribute_mrp);
                        model.setAttribute_rewards( attribute_reward );

                        variantList.add(model);

                        //     arrayList.add(new AttributeModel(atr_id,product_id,attribute_name,attribute_value));

                        //Toast.makeText(getActivity(),"id "+atr_id+"\n p_id "+product_id+"\n atr_name "+attribute_name+"\n atr_value "+attribute_value,Toast.LENGTH_LONG).show();
                    }



                } catch (JSONException e) {
                    e.printStackTrace();
                }
                ListView l1=(ListView)row.findViewById(R.id.list_view_varaint);
                productVariantAdapter=new ProductVariantAdapter(ActivityProductDetails.this,variantList);
                //productVariantAdapter.notifyDataSetChanged();
                l1.setAdapter(productVariantAdapter);


                builder.setView(row);
                final AlertDialog ddlg=builder.create();
                ddlg.show();
                l1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                        atr_id=String.valueOf(variantList.get(i).getId());
                        atr_product_id=String.valueOf(variantList.get(i).getProduct_id());
                        attribute_name=String.valueOf(variantList.get(i).getAttribute_name());
                        attribute_value=String.valueOf(variantList.get(i).getAttribute_value());
                        attribute_mrp=String.valueOf(variantList.get(i).getAttribute_mrp());
                        attribute_reward=String.valueOf( variantList.get( i ).getAttribute_rewards() );
                        dialog_unit_type.setText("\u20B9"+attribute_value+"/"+attribute_name);
                        dialog_txtId.setText(variantList.get(i).getId()+"@"+i);
                        dialog_txtVar.setText(attribute_value+"@"+attribute_name+"@"+attribute_mrp);
                        //    txtPer.setText(String.valueOf(df)+"% off");

                        details_product_price.setText("\u20B9"+attribute_value.toString());
                        details_product_mrp.setText("\u20B9"+attribute_mrp.toString());
                        details_product_rewards.setText( attribute_reward );
                        String pr=String.valueOf(attribute_value);
                        String mr=String.valueOf(attribute_mrp);
                        int atr_dis=getDiscount(pr,mr);
                        product_rate.setText(""+atr_dis+"% OFF");
                        String atr=String.valueOf(attr);
                        if(atr.equals("[]"))
                        {
                            boolean st=db_cart.isInCart(p_id);
                            if(st==true)
                            {
                                btn_add.setVisibility(View.GONE);
                                product_qty.setNumber(db_cart.getCartItemQty(p_id));
                                product_qty.setVisibility(View.VISIBLE);
                            }
                        }
                        else
                        {
                            String str_id=dialog_txtId.getText().toString();
                            String[] str=str_id.split("@");
                            String at_id=String.valueOf(str[0]);
                            boolean st=db_cart.isInCart(at_id);
                            if(st==true)
                            {
                                btn_add.setVisibility(View.GONE);
                                product_qty.setNumber(db_cart.getCartItemQty(at_id));
                                product_qty.setVisibility(View.VISIBLE);
                            }
                            else
                            {
                                btn_add.setVisibility(View.VISIBLE);

                                product_qty.setVisibility(View.GONE);
                            }
                        }

                        product_qty.setNumber("1");

                        // txtTotal.setText("\u20B9"+String.valueOf(db_cart.getTotalAmount()));
                        ddlg.dismiss();
                    }
                });

            }
        });
        product_qty.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
            @Override
            public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {

                if(newValue<=0)
                {
                    boolean st=checkAttributeStatus(attr);
                    if(st==false)
                    {
                        db_cart.removeItemFromCart(p_id);

                    }
                    else if(st==true)
                    {

                        String str_id = dialog_txtId.getText().toString();
                        String[] str = str_id.split("@");
                        String at_id = String.valueOf(str[0]);
                        db_cart.removeItemFromCart(at_id);

                    }
                    updateCart();
                    product_qty.setVisibility(View.GONE);
                    btn_add.setVisibility(View.VISIBLE);
                }
                else {


                    float qty = Float.parseFloat(String.valueOf(newValue));

                    String atr = String.valueOf(attr);
                    if (atr.equals("[]")) {
                        double pr = Double.parseDouble(price);
                        double amt = pr * qty;
                        HashMap<String, String> mapProduct = new HashMap<String, String>();
                        String unt = String.valueOf(unit_value + " " + unit);
                        mapProduct.put("cart_id", p_id);
                        mapProduct.put("product_id", p_id);
                        mapProduct.put("product_image", images);
                        mapProduct.put("cat_id", cat_id);
                        mapProduct.put("product_name", name);
                        mapProduct.put("price", String.valueOf(amt));
                        mapProduct.put("unit_price", price);
                        mapProduct.put("unit", unt);
                        mapProduct.put( "rewards",rewards );
                        mapProduct.put("mrp", mrp);
                        mapProduct.put("type", "p");
                        try {

                            boolean tr = db_cart.setCart(mapProduct, qty);
                            if (tr == true) {
                                updateCart();
                                Toast.makeText(ActivityProductDetails.this, "Added to Cart", Toast.LENGTH_LONG).show();
                                int n = db_cart.getCartCount();



                            } else if (tr == false) {
                                Toast.makeText(ActivityProductDetails.this, "cart updated", Toast.LENGTH_LONG).show();
                            }

                        } catch (Exception ex) {
                            ex.printStackTrace();
                            //Toast.makeText(getActivity(), "" + ex.getMessage(), Toast.LENGTH_LONG).show();
                        }

                        //Toast.makeText(context,"1\n"+status+"\n"+modelList.get(position).getProduct_attribute(),Toast.LENGTH_LONG).show();
                    } else {
                        //ProductVariantModel model=variantList.get(position);




                        String s = dialog_txtVar.getText().toString();
                        String[] st = s.split("@");
                        String st0 = String.valueOf(st[0]);
                        String st1 = String.valueOf(st[1]);
                        String st2 = String.valueOf(st[2]);
                        String str_id = dialog_txtId.getText().toString();
                        String[] str = str_id.split("@");
                        String at_id = String.valueOf(str[0]);
                        int k = Integer.parseInt(String.valueOf(str[1]));
                        double pr = Double.parseDouble(st0);


                        double amt = pr * qty;
                        //       Toast.makeText(context,""+str[0].toString()+"\n"+str[1].toString(),Toast.LENGTH_LONG).show();
                        HashMap<String, String> mapProduct = new HashMap<String, String>();
                        mapProduct.put("cart_id", at_id);
                        mapProduct.put("product_id", p_id);
                        mapProduct.put("product_image", images);
                        mapProduct.put("cat_id", cat_id);
                        mapProduct.put("product_name", name);
                        mapProduct.put("price", String.valueOf(amt));
                        mapProduct.put("unit_price", st0);
                        mapProduct.put("unit", st1);
                        mapProduct.put("mrp", st2);
                        mapProduct.put("type", "a");
                        mapProduct.put( "rewards",rewards );
                        //  Toast.makeText(context,""+attributeList.get(j).getId()+"\n"+mapProduct,Toast.LENGTH_LONG).show();
                        try {

                            boolean tr = db_cart.setCart(mapProduct, qty);
                            if (tr == true) {
                                updateCart();
                                Toast.makeText(context, "Added to Cart", Toast.LENGTH_LONG).show();
                                int n = db_cart.getCartCount();


                            } else if (tr == false) {
                                Toast.makeText(context, "cart updated", Toast.LENGTH_LONG).show();
                            }

                        } catch (Exception ex) {
                            ex.printStackTrace();
                            // Toast.makeText(getActivity(), "" + ex.getMessage(), Toast.LENGTH_LONG).show();
                        }

                    }
                }
                //txtTotal.setText("\u20B9"+String.valueOf(db_cart.getTotalAmount()));
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    private void initData() {
        //activity=ActivityProductDetails.this;
        img_slider=(SliderLayout)findViewById(R.id.img_slider);
        details_product_name=findViewById(R.id.details_product_name);
        details_product_price=findViewById(R.id.details_product_price);
        details_product_mrp=findViewById(R.id.details_product_mrp);
        details_product_mrp.setPaintFlags(details_product_mrp.getPaintFlags() |Paint.STRIKE_THRU_TEXT_FLAG );
        details_product_per=findViewById(R.id.details_product_per);
        details_product_description=findViewById(R.id.details_product_description);
        details_product_rewards=findViewById( R.id.details_product_rewards );
        wish_before = findViewById( R.id.wish_before );
        wish_after=findViewById( R.id.wish_after );
        product_rate=findViewById(R.id.product_rate);
        unit_type=findViewById(R.id.unit_type);
        txt_cart_cout=findViewById(R.id.txt_cart_cout);

        dialog_unit_type=(TextView)findViewById(R.id.unit_type);
        dialog_txtId=(TextView)findViewById(R.id.txtId);
        dialog_txtVar=(TextView)findViewById(R.id.txtVar);
        btn_add=(Button) findViewById(R.id.btn_add);
        btn_Add_to_cart=(Button) findViewById(R.id.btn_Add_to_cart);

        related_recycler=(RecyclerView)findViewById(R.id.related_recycler);

        product_qty=(ElegantNumberButton)findViewById(R.id.product_qty);

        rel_variant=(RelativeLayout)findViewById(R.id.rel_variant);
        ly_back=(RelativeLayout)findViewById(R.id.ly_back);
        layout_cart_cout=(RelativeLayout)findViewById(R.id.layout_cart_cout);
        layout_cart=(RelativeLayout)findViewById(R.id.layout_cart);
        variantList=new ArrayList<>();
        progressDialog = new Dialog(Common.Activity, android.R.style.Theme_Translucent_NoTitleBar);
        progressDialog.setContentView(R.layout.progressbar);
        progressDialog.setCancelable(false);
        db_cart=new DatabaseCartHandler(ActivityProductDetails.this);
        db_wish =new DatabaseHandlerWishList( ActivityProductDetails.this );
        updateCart();
        Intent intent=getIntent();
             p_map=(HashMap<String, String>) intent.getSerializableExtra("details");

             cat_id=p_map.get("cat_id");
             p_id=p_map.get("product_id");
             images=p_map.get("product_images");
             name=p_map.get("product_name");
             desc=p_map.get("product_description");
             in_stock=p_map.get("in_stock");
             stock=p_map.get("stock");
             p_status=p_map.get("status");
             price=p_map.get("price");
             mrp=p_map.get("mrp");
             unit_value=p_map.get("unit_value");
             unit=p_map.get("unit");
             attr=p_map.get("product_attribute");
             rewards=p_map.get("rewards");
             increment=p_map.get("increment");
             title=p_map.get("title");
             btn_add.setOnClickListener(this);
             ly_back.setOnClickListener( this );
             layout_cart.setOnClickListener(this);
             wish_before.setOnClickListener( this );
             wish_after.setOnClickListener( this );


    }


    @Override
    public void onClick(View view) {

        int id=view.getId();
        if(id==R.id.btn_add)
        {
          float qty = 1;

            String atr = String.valueOf(attr);
            if (atr.equals("[]")) {
                HashMap<String, String> mapProduct = new HashMap<String, String>();
                String unt = String.valueOf(unit_value + " " + unit);
                mapProduct.put("cart_id", p_id);
                mapProduct.put("product_id", p_id);
                mapProduct.put("product_image", images);
                mapProduct.put("cat_id", cat_id);
                mapProduct.put("product_name", name);
                mapProduct.put("price", price);
                mapProduct.put("unit_price", price);
                mapProduct.put("unit", unt);
                mapProduct.put("mrp", mrp);
                mapProduct.put( "rewards",rewards);
                mapProduct.put("type", "p");
                try {

                    boolean tr = db_cart.setCart(mapProduct, qty);
                    if (tr == true) {
                        updateCart();
                        //   context.setCartCounter("" + holder.db_cart.getCartCount());
                        Toast.makeText(ActivityProductDetails.this, "Added to Cart", Toast.LENGTH_LONG).show();
                        int n = db_cart.getCartCount();

                    } else if (tr == false) {
                        Toast.makeText(ActivityProductDetails.this, "cart updated", Toast.LENGTH_LONG).show();

                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                    // Toast.makeText(getActivity(), "" + ex.getMessage(), Toast.LENGTH_LONG).show();
                }

                //Toast.makeText(context,"1\n"+status+"\n"+modelList.get(position).getProduct_attribute(),Toast.LENGTH_LONG).show();
            }
            else {
                //ProductVariantModel model=variantList.get(position);

                String str_id=dialog_txtId.getText().toString();
                String s=dialog_txtVar.getText().toString();
                String[] st=s.split("@");
                String st0=String.valueOf(st[0]);
                String st1=String.valueOf(st[1]);
                String st2=String.valueOf(st[2]);
                String[] str=str_id.split("@");
                String at_id=String.valueOf(str[0]);
                int j=Integer.parseInt(String.valueOf(str[1]));
                //       Toast.makeText(context,""+str[0].toString()+"\n"+str[1].toString(),Toast.LENGTH_LONG).show();
                HashMap<String, String> mapProduct = new HashMap<String, String>();
                mapProduct.put("cart_id",at_id);
                mapProduct.put("product_id",p_id);
                mapProduct.put("product_image",images);
                mapProduct.put("cat_id",cat_id);
                mapProduct.put("product_name",name);
                mapProduct.put("price", st0);
                mapProduct.put("unit_price",st0);
                mapProduct.put( "rewards",attribute_reward );
                mapProduct.put("unit",st1);
                mapProduct.put("mrp",st2);
                mapProduct.put("type","a");
                //  Toast.makeText(ActivityProductDetails.this,""+attribute_reward,Toast.LENGTH_LONG).show();
                try {

                    boolean tr = db_cart.setCart(mapProduct, qty );
                    if (tr == true) {
                        updateCart();
                        Toast.makeText(ActivityProductDetails.this, "Added to Cart" +attribute_reward, Toast.LENGTH_LONG).show();
                        int n= db_cart.getCartCount();
//                        updateintent();

//
//                        txtTotal.setText("\u20B9"+String.valueOf(db_cart.getTotalAmount()));
                    }
                    else if(tr==false)
                    {
                        Toast.makeText(ActivityProductDetails.this,"cart updated",Toast.LENGTH_LONG).show();
                    //    txtTotal.setText("\u20B9"+String.valueOf(db_cart.getTotalAmount()));
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                    //Toast.makeText(activity, "" + ex.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
            //updateData();
            btn_add.setVisibility(View.GONE);
            product_qty.setNumber("1");
            product_qty.setVisibility(View.VISIBLE);


        }
        else if(id == R.id.ly_back)
        {
            finish();
        }
        else if(id == R.id.layout_cart)
        {
            Intent intent=new Intent(ActivityProductDetails.this, ActivityCart.class);
            startActivity(intent);
        }
        else if(id == R.id.wish_before)
        {
            wish_before.setVisibility( View.GONE );
            wish_after.setVisibility( View.VISIBLE );



            HashMap<String, String> mapProduct = new HashMap<String, String>();
            mapProduct.put("product_id", p_id);
            mapProduct.put("product_images",images);
            mapProduct.put("cat_id",cat_id);
            mapProduct.put("product_name",name);
            mapProduct.put("price", price);
            mapProduct.put("product_description",desc);
            mapProduct.put("rewards",rewards);
            mapProduct.put("unit_value",unit_value);
            mapProduct.put("unit",unit);
            mapProduct.put("increment",increment);
            mapProduct.put("stock",stock);
            mapProduct.put("title",title);
            mapProduct.put("mrp",mrp);
            mapProduct.put("product_attribute",attr);
            mapProduct.put("in_stock",in_stock);
            mapProduct.put("status", String.valueOf( status ) );
            try {

                boolean tr =db_wish.setwishTable(mapProduct);
                if (tr == true) {

                    //   context.setCartCounter("" + holder.db_cart.getCartCount());
                    Toast.makeText(ActivityProductDetails.this, "Added to Wishlist" +db_wish.getWishtableCount(), Toast.LENGTH_LONG).show();



                }
                else
                {
                    Toast.makeText(ActivityProductDetails.this, "Something Went Wrong" +db_wish.getWishtableCount(), Toast.LENGTH_LONG).show();
                }

            } catch (Exception ex) {
                ex.printStackTrace();
                //  Toast.makeText(context, "" + ex.getMessage(), Toast.LENGTH_LONG).show();
            }




        }

        else if (id == R.id.wish_after)
        {
            wish_before.setVisibility( View.VISIBLE );
            wish_after.setVisibility( View.GONE );

            db_wish.removeItemFromWishtable( p_id );
            Toast.makeText(ActivityProductDetails.this, "removed from Wishlist", Toast.LENGTH_LONG).show();
            // list.remove(position);
        }

    }

    private void useSlider(String images) {

        final HashMap<String,String> url_maps=new HashMap<String, String>();

        try
        {
            JSONArray array=new JSONArray(images);
            for(int i=0; i<array.length();i++)
            {
                String object=array.getString(i);
               url_maps.put(String.valueOf(i),object.toString());
            }
        }
        catch (Exception ex)
        {
            Toast.makeText(this,""+ex.getMessage(),Toast.LENGTH_LONG).show();
        }


        for(final String name:url_maps.keySet())
        {
            TextSliderView textSliderView=new TextSliderView(this);
            textSliderView.description(name)
                    .image(IMG_PRODUCT_URL+url_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.FitCenterCrop)
                    .setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                        @Override
                        public void onSliderClick(BaseSliderView slider) {

                            //  Toast.makeText(getActivity(),""+url_maps.get(name),Toast.LENGTH_LONG).show();
                        }
                    });
            textSliderView.bundle(new Bundle());
        //    textSliderView.getBundle().putString("extra",name);
            img_slider.addSlider(textSliderView);
        }
      //  img_slider.setPresetTransformer(SliderLayout.Transformer.Accordion);
       // img_slider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
       // img_slider.setCustomAnimation(new DescriptionAnimation());
        img_slider.setDuration(2500);

    }

    private void makeRelatedProductRequest(String cat_id) {
       progressDialog.show();
        String tag_json_obj = "json_product_req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("cat_id", cat_id);

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest( Request.Method.POST,
                Url.GET_PRODUCT_URL, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("rett" +
                        "", response.toString());

                try {

                    Boolean status = response.getBoolean("responce");

                    if (status) {
                        ///         Toast.makeText(getActivity(),""+response.getString("data"),Toast.LENGTH_LONG).show();
                        Gson gson = new Gson();
                        Type listType = new TypeToken<List<RelatedProductModel>>() {
                        }.getType();
                        relatedProductModels = gson.fromJson(response.getString("data"), listType);
                        progressDialog.dismiss();
                        relatedProductAdapter = new RelatedProductAdapter(ActivityProductDetails.this,relatedProductModels,p_id);
                        GridLayoutManager gridLayoutManager = new GridLayoutManager( ActivityProductDetails.this,2 );
                  //      LinearLayoutManager linearLayoutManager = new LinearLayoutManager( ActivityProductDetails.this,LinearLayoutManager.HORIZONTAL,false );
                        related_recycler.setLayoutManager(gridLayoutManager);
                       related_recycler.setAdapter(relatedProductAdapter);
                        relatedProductAdapter.notifyDataSetChanged();
                       // if (getActivity() != null) {
                            if (relatedProductModels.isEmpty()) {

                                progressDialog.dismiss();
                                //  Toast.makeText(getActivity(), getResources().getString(R.string.no_rcord_found), Toast.LENGTH_SHORT).show();
                            }
                        //}

                    }
                } catch (JSONException e) {
                    progressDialog.dismiss();
                    //   e.printStackTrace();
                    String ex=e.getMessage();




                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Error: " + error.getMessage());
                //loadingBar.dismiss();
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                  progressDialog.dismiss();
                    Toast.makeText(ActivityProductDetails.this, getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                }

            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }
    public int getDiscount(String price, String mrp)
    {
        double mrp_d=Double.parseDouble(mrp);
        double price_d=Double.parseDouble(price);
        double per=((mrp_d-price_d)/mrp_d)*100;
        double df=Math.round(per);
        int d=(int)df;
        return d;
    }

    public boolean checkAttributeStatus(String atr)
    {
        boolean sts=false;
        if(atr.equals("[]"))
        {
            sts=false;
        }
        else
        {
            sts=true;
        }
        return sts;
    }

    public void updateCart()
    {

        int cnt=db_cart.getCartCount();
        if(cnt<1)
        {

        }
        else
        {
            layout_cart_cout.setVisibility(View.VISIBLE);
            txt_cart_cout.setText(String.valueOf(cnt));
        }
    }
}
