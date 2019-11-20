package in.mapbazar.mapbazar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import in.mapbazar.mapbazar.Adapter.ProductVariantAdapter;
import in.mapbazar.mapbazar.Model.ProductVariantModel;
import in.mapbazar.mapbazar.Utili.Common;
import in.mapbazar.mapbazar.View.CustomTextView;
import in.mapbazar.mapbazar.util.DatabaseCartHandler;

import static in.mapbazar.mapbazar.Utili.Url.IMG_PRODUCT_URL;

public class ActivityProductDetails extends AppCompatActivity implements View.OnClickListener {

    DatabaseCartHandler db_cart;
    ProductVariantAdapter productVariantAdapter;
    ArrayList<ProductVariantModel> variantList;
    String atr_id="";
    String atr_product_id="";
    String attribute_name="";
    String attribute_value="";
    String attribute_mrp="";
    int status=0;
    private TextView dialog_unit_type,dialog_txtId,dialog_txtVar;
    //Activity activity;
    SliderLayout img_slider;
    CustomTextView details_product_name,details_product_price,details_product_mrp,details_product_per,
            details_product_description,product_rate,unit_type,txt_cart_cout;
    ElegantNumberButton numberButton;
    RecyclerView related_recycler;
    Button btn_add, btn_Add_to_cart;
    RelativeLayout ly_back,layout_cart_cout,rel_variant;
    Dialog progressDialog;
    String name,images,cat_id,p_id,desc,in_stock,stock,p_status,price,mrp,unit_value,unit,attr,rewards,increment,title;

    HashMap<String,String> p_map=new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        initData();

        useSlider(images);


        details_product_name.setText(name);
        details_product_description.setText(desc);
//        details_product_price.setText(getResources().getString(R.string.currency)+price);
//        details_product_mrp.setText(getResources().getString(R.string.currency)+mrp);
//        details_product_per.setText(""+getDiscount(price,mrp)+"% OFF");

        if(attr.equals("[]"))
        {
            details_product_per.setText(""+getDiscount(price,mrp)+"% OFF");

            status=1;
            details_product_price.setText(getResources().getString(R.string.currency)+price);
            details_product_mrp.setText(getResources().getString(R.string.currency)+mrp);

            product_rate.setVisibility(View.VISIBLE);

            rel_variant.setVisibility(View.GONE);
            //  Toast.makeText(getActivity(),""+atr,Toast.LENGTH_LONG).show();
            product_rate.setText(unit_value+unit);
            //txtTotal.setText("\u20B9"+String.valueOf(db_cart.getTotalAmount()));
        }

        else
        {

            rel_variant.setVisibility(View.VISIBLE);
            product_rate.setVisibility(View.GONE);
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



                //     arrayList.add(new AttributeModel(atr_id,product_id,attribute_name,attribute_value));

                //Toast.makeText(getActivity(),"id "+atr_id+"\n p_id "+product_id+"\n atr_name "+attribute_name+"\n atr_value "+attribute_value,Toast.LENGTH_LONG).show();


                String atr_price=String.valueOf(attribute_value);
                String atr_mrp=String.valueOf(attribute_mrp);
                int atr_dis=getDiscount(atr_price,atr_mrp);
                details_product_price.setText(getResources().getString(R.string.currency)+attribute_value);
                details_product_mrp.setText(getResources().getString(R.string.currency)+attribute_mrp);
                dialog_txtId.setText(String.valueOf(atr_id.toString()+"@"+"0"));
                dialog_txtVar.setText(attribute_value+"@"+attribute_name+"@"+attribute_mrp);
                dialog_unit_type.setText("\u20B9"+attribute_value+"/"+attribute_name);
                //  holder.txtTotal.setText("\u20B9"+String.valueOf(list_atr_value.get(0).toString()));
                details_product_per.setText(""+getDiscount(price,mrp)+"% OFF");

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


                        model.setId(atr_id);
                        model.setProduct_id(atr_product_id);
                        model.setAttribute_value(attribute_value);
                        model.setAttribute_name(attribute_name);
                        model.setAttribute_mrp(attribute_mrp);

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

                        dialog_unit_type.setText("\u20B9"+attribute_value+"/"+attribute_name);
                        dialog_txtId.setText(variantList.get(i).getId()+"@"+i);
                        dialog_txtVar.setText(attribute_value+"@"+attribute_name+"@"+attribute_mrp);
                        //    txtPer.setText(String.valueOf(df)+"% off");

                        details_product_price.setText("\u20B9"+attribute_value.toString());
                        details_product_mrp.setText("\u20B9"+attribute_mrp.toString());
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
                                numberButton.setNumber(db_cart.getCartItemQty(p_id));
                                numberButton.setVisibility(View.VISIBLE);
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
                                numberButton.setNumber(db_cart.getCartItemQty(at_id));
                                numberButton.setVisibility(View.VISIBLE);
                            }
                            else
                            {
                                btn_add.setVisibility(View.VISIBLE);

                                numberButton.setVisibility(View.GONE);
                            }
                        }

                        numberButton.setNumber("1");

                       // txtTotal.setText("\u20B9"+String.valueOf(db_cart.getTotalAmount()));
                        ddlg.dismiss();
                    }
                });

            }
        });
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
        product_rate=findViewById(R.id.product_rate);
        unit_type=findViewById(R.id.unit_type);
        txt_cart_cout=findViewById(R.id.txt_cart_cout);

        dialog_unit_type=(TextView)findViewById(R.id.unit_type);
        dialog_txtId=(TextView)findViewById(R.id.txtId);
        dialog_txtVar=(TextView)findViewById(R.id.txtVar);
        btn_add=(Button) findViewById(R.id.btn_add);
        btn_Add_to_cart=(Button) findViewById(R.id.btn_Add_to_cart);

        related_recycler=(RecyclerView)findViewById(R.id.related_recycler);

        db_cart=new DatabaseCartHandler(ActivityProductDetails.this);
        numberButton=(ElegantNumberButton)findViewById(R.id.product_qty);
        variantList=new ArrayList<>();
        rel_variant=(RelativeLayout)findViewById(R.id.rel_variant);
        ly_back=(RelativeLayout)findViewById(R.id.ly_back);
        layout_cart_cout=(RelativeLayout)findViewById(R.id.layout_cart_cout);


        progressDialog = new Dialog(Common.Activity, android.R.style.Theme_Translucent_NoTitleBar);
        progressDialog.setContentView(R.layout.progressbar);
        progressDialog.setCancelable(false);
        setCart();
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
        layout_cart_cout.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {

        int id=view.getId();
        if(id==R.id.btn_add)
        {
          //  String tot1 = txtTotal.getText().toString().trim();

         //   String tot_amount = tot1.substring(1, tot1.length());
            float qty = 1;
            //String tot_amount = String.valueOf(tot);

//                    String sz = txtSize.getText().toString();
//                    String col = txtColor.getText().toString();

//                  Toast.makeText(getActivity(),"tot_amount"+tot_amount+"\n sz"+sz+"\n col"+col,Toast.LENGTH_LONG).show();

            // int sd = txtColor.getVisibility();
            // int ds = txtSize.getVisibility();


            //  if (sz.equals("Select Size") && col.equals("Select Color")) {
            //Toast.makeText(getActivity(), ""+tot_amount, Toast.LENGTH_LONG).show();

            //  }
            // else {
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
                mapProduct.put("type", "p");
                try {

                    boolean tr = db_cart.setCart(mapProduct, qty);
                    if (tr == true) {
                        MainActivity mainActivity = new MainActivity();
//                        mainActivity.setCartCounter("" + db_cart.getCartCount());
                        setCart();
                        //   context.setCartCounter("" + holder.db_cart.getCartCount());
                        Toast.makeText(ActivityProductDetails.this, "Added to Cart", Toast.LENGTH_LONG).show();
                        int n = db_cart.getCartCount();
                       // updateintent();
//                        txtTotal.setText("\u20B9"+String.valueOf(db_cart.getTotalAmount()));

                    } else if (tr == false) {
                        Toast.makeText(ActivityProductDetails.this, "cart updated", Toast.LENGTH_LONG).show();
                       // txtTotal.setText("\u20B9"+String.valueOf(db_cart.getTotalAmount()));
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
                mapProduct.put("unit",st1);
                mapProduct.put("mrp",st2);
                mapProduct.put("type","a");
                //  Toast.makeText(context,""+attributeList.get(j).getId()+"\n"+mapProduct,Toast.LENGTH_LONG).show();
                try {

                    boolean tr = db_cart.setCart(mapProduct, qty );
                    if (tr == true) {
                        setCart();

                     //   MainActivity mainActivity = new MainActivity();
                       // mainActivity.setCartCounter("" + db_cart.getCartCount());

                        //   context.setCartCounter("" + holder.db_cart.getCartCount());
                        Toast.makeText(ActivityProductDetails.this, "Added to Cart", Toast.LENGTH_LONG).show();
                        int n= db_cart.getCartCount();
                        //updateintent();

                       // txtTotal.setText("\u20B9"+String.valueOf(db_cart.getTotalAmount()));
                    }
                    else if(tr==false)
                    {
                        Toast.makeText(ActivityProductDetails.this,"cart updated",Toast.LENGTH_LONG).show();
                        //txtTotal.setText("\u20B9"+String.valueOf(db_cart.getTotalAmount()));
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                    //Toast.makeText(activity, "" + ex.getMessage(), Toast.LENGTH_LONG).show();
                }

            }

            btn_add.setVisibility(View.GONE);
            numberButton.setNumber("1");
            numberButton.setVisibility(View.VISIBLE);


        }
        else if(id == R.id.layout_cart_cout)
        {
            Intent intent=new Intent(ActivityProductDetails.this,ActivityNewProductDetails.class);
            startActivity(intent);
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


    public int getDiscount(String price, String mrp)
    {
        double mrp_d=Double.parseDouble(mrp);
        double price_d=Double.parseDouble(price);
        double per=((mrp_d-price_d)/mrp_d)*100;
        double df=Math.round(per);
        int d=(int)df;
        return d;
    }

    public void setCart()
    {
        int cnt=db_cart.getCartCount();
        if(cnt<1)
        {

        }
        else
        {
            layout_cart_cout.setVisibility(View.VISIBLE);
            txt_cart_cout.setText(""+cnt);
        }
    }
}
