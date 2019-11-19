package in.mapbazar.mapbazar;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
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

import java.util.HashMap;

import in.mapbazar.mapbazar.Utili.Common;
import in.mapbazar.mapbazar.View.CustomTextView;

import static in.mapbazar.mapbazar.Utili.Url.IMG_PRODUCT_URL;

public class ActivityProductDetails extends AppCompatActivity implements View.OnClickListener {

    //Activity activity;
    SliderLayout img_slider;
    CustomTextView details_product_name,details_product_price,details_product_mrp,details_product_per,
            details_product_description,product_rate,unit_type,txt_cart_cout;
    ElegantNumberButton product_qty;
    RecyclerView related_recycler;
    Button btn_add, btn_Add_to_cart;
    RelativeLayout ly_back,layout_cart_cout,rel_variant;
    Dialog progressDialog;
    String name,images,cat_id,p_id,desc,in_stock,stock,status,price,mrp,unit_value,unit,attr,rewards,increment,title;

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
            txtPrice.setText("\u20B9"+details_product_price);
            txtMrp.setText("\u20B9"+details_product_mrp);

            txtrate.setVisibility(View.VISIBLE);
            //  Toast.makeText(getActivity(),""+atr,Toast.LENGTH_LONG).show();
            txtrate.setText(details_product_unit_value+details_product_unit);
            txtTotal.setText("\u20B9"+String.valueOf(db_cart.getTotalAmount()));
        }

        else
        {

            rel_variant.setVisibility(View.VISIBLE);
            status=2;
            JSONArray jsonArr = null;
            try {

                jsonArr = new JSONArray(atr);

                ProductVariantModel model=new ProductVariantModel();
                JSONObject jsonObj = jsonArr.getJSONObject(0);
                atr_id=jsonObj.getString("id");
                product_id=jsonObj.getString("product_id");
                attribute_name=jsonObj.getString("attribute_name");
                attribute_value=jsonObj.getString("attribute_value");
                attribute_mrp=jsonObj.getString("attribute_mrp");



                //     arrayList.add(new AttributeModel(atr_id,product_id,attribute_name,attribute_value));

                //Toast.makeText(getActivity(),"id "+atr_id+"\n p_id "+product_id+"\n atr_name "+attribute_name+"\n atr_value "+attribute_value,Toast.LENGTH_LONG).show();


                String atr_price=String.valueOf(attribute_value);
                String atr_mrp=String.valueOf(attribute_mrp);
                int atr_dis=getDiscount(atr_price,atr_mrp);
                txtPrice.setText("\u20B9"+attribute_value.toString());
                txtMrp.setText("\u20B9"+attribute_mrp.toString());
                dialog_txtId.setText(String.valueOf(atr_id.toString()+"@"+"0"));
                dialog_txtVar.setText(attribute_value+"@"+attribute_name+"@"+attribute_mrp);
                dialog_unit_type.setText("\u20B9"+attribute_value+"/"+attribute_name);
                //  holder.txtTotal.setText("\u20B9"+String.valueOf(list_atr_value.get(0).toString()));
                txtPer.setText(""+atr_dis+"% OFF");

            } catch (JSONException e) {
                e.printStackTrace();
            }



        }

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

        btn_add=(Button) findViewById(R.id.btn_add);
        btn_Add_to_cart=(Button) findViewById(R.id.btn_Add_to_cart);

        related_recycler=(RecyclerView)findViewById(R.id.related_recycler);

        product_qty=(ElegantNumberButton)findViewById(R.id.product_qty);

        rel_variant=(RelativeLayout)findViewById(R.id.rel_variant);
        ly_back=(RelativeLayout)findViewById(R.id.ly_back);
        layout_cart_cout=(RelativeLayout)findViewById(R.id.layout_cart_cout);


        progressDialog = new Dialog(Common.Activity, android.R.style.Theme_Translucent_NoTitleBar);
        progressDialog.setContentView(R.layout.progressbar);
        progressDialog.setCancelable(false);

        Intent intent=getIntent();
             p_map=(HashMap<String, String>) intent.getSerializableExtra("details");

             cat_id=p_map.get("cat_id");
             p_id=p_map.get("product_id");
             images=p_map.get("product_images");
             name=p_map.get("product_name");
             desc=p_map.get("product_description");
             in_stock=p_map.get("in_stock");
             stock=p_map.get("stock");
             status=p_map.get("status");
             price=p_map.get("price");
             mrp=p_map.get("mrp");
             unit_value=p_map.get("unit_value");
             unit=p_map.get("unit");
             attr=p_map.get("product_attribute");
             rewards=p_map.get("rewards");
             increment=p_map.get("increment");
             title=p_map.get("title");
             btn_add.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {

        int id=view.getId();
        if(id==R.id.btn_add)
        {
            Toast.makeText(ActivityProductDetails.this,"id : "+p_id+"\n desc :-- "+desc.toString(),Toast.LENGTH_LONG).show();

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
}
