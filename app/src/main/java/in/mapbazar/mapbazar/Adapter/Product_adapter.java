
package in.mapbazar.mapbazar.Adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import in.mapbazar.mapbazar.ActivityCategoryProduct;
import in.mapbazar.mapbazar.ActivityProductDetails;
import in.mapbazar.mapbazar.MainActivity;
import in.mapbazar.mapbazar.Model.ProductVariantModel;
import in.mapbazar.mapbazar.Model.Product_model;
import in.mapbazar.mapbazar.R;
import in.mapbazar.mapbazar.Utili.Url;
import in.mapbazar.mapbazar.util.DatabaseCartHandler;
import in.mapbazar.mapbazar.util.DatabaseHandler;
import in.mapbazar.mapbazar.util.DatabaseHandlerWishList;

import static android.content.Context.MODE_PRIVATE;


public class Product_adapter extends RecyclerView.Adapter<Product_adapter.MyViewHolder> {

    List<String> image_list;
   // Dialog dialog;
   // RelativeLayout rel_out;
    ListView listView1;
    String atr_id="";
    String atr_product_id="";
    String attribute_name="";
    String attribute_value="";
    String attribute_mrp="";
    ArrayList<ProductVariantModel> variantList;
    ArrayList<ProductVariantModel> attributeList;
    ProductVariantAdapter productVariantAdapter;
    RelativeLayout rel_click;

    private List<Product_model> modelList;
    private Context context;
    int status=0;
    private DatabaseHandler dbcart;
    private DatabaseCartHandler db_cart;
    String language;
SharedPreferences preferences;
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView tv_title, tv_price,product_mrp ,discount;
        public ImageView iv_logo, wish_before ,wish_after;
       // public ConstraintLayout con_product;
        public RelativeLayout rel_variant;
        TextView txtrate;
        ElegantNumberButton elegantNumberButton;
        private TextView dialog_unit_type,dialog_txtId,dialog_txtVar;
        Button add ;
        public Double reward;
        DatabaseHandlerWishList db_wish;


        public MyViewHolder(View view) {
            super(view);

            tv_title = (TextView) view.findViewById(R.id.tv_subcat_title);
            tv_price = (TextView) view.findViewById(R.id.tv_subcat_price);
           // tv_reward = (TextView) view.findViewById(R.id.tv_reward_point);
          //  tv_total = (TextView) view.findViewById(R.id.tv_subcat_total);
            product_mrp = (TextView) view.findViewById(R.id.product_mrp);
            discount= view.findViewById( R.id.dis );
            rel_click=view.findViewById(R.id.rel_click);
            add = view.findViewById( R.id.btn_add );
            wish_after=view.findViewById( R.id.wish_after );
            wish_before=view.findViewById( R.id.wish_before );
            iv_logo = (ImageView) view.findViewById(R.id.iv_subcat_img);
            rel_variant=(RelativeLayout)view.findViewById(R.id.rel_variant);
         //   rel_out=(RelativeLayout)view.findViewById(R.id.rel_out);
            dialog_unit_type=(TextView)view.findViewById(R.id.unit_type);
            dialog_txtId=(TextView)view.findViewById(R.id.txtId);
            dialog_txtVar=(TextView)view.findViewById(R.id.txtVar);
            txtrate=(TextView)view.findViewById(R.id.single_varient);
          //  con_product=(ConstraintLayout)view.findViewById(R.id.con_layout_product);
            elegantNumberButton=view.findViewById( R.id.elegantButton );
            image_list=new ArrayList<>();

            variantList=new ArrayList<>();
            attributeList=new ArrayList<>();
            wish_before.setOnClickListener(this);
            wish_after.setOnClickListener(this );
            rel_click.setOnClickListener(this);
            rel_variant.setOnClickListener(this);
            add.setOnClickListener(this);
            db_wish = new DatabaseHandlerWishList( context );

            CardView cardView = (CardView) view.findViewById(R.id.card_view);
            cardView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            int id = view.getId();
            final int position = getAdapterPosition();
             if(id == R.id.rel_click)
            {
                //double stock=Double.parseDouble(modelList.get(position).getStock());
    //Toast.makeText(context,"asd",Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(context, ActivityProductDetails.class);

                    HashMap<String, String> args = new HashMap<>();
//
//               //Intent intent=new Intent(context, Product_details.class);
                    args.put("cat_id", modelList.get(position).getCategory_id());
                    args.put("product_id", modelList.get(position).getProduct_id());
                    args.put("product_images", modelList.get(position).getProduct_image());
                    args.put("product_name", modelList.get(position).getProduct_name());
                    args.put("product_description", modelList.get(position).getProduct_description());
                    args.put("in_stock", modelList.get(position).getIn_stock());
                    args.put("stock", modelList.get(position).getStock());
                    args.put("status", modelList.get(position).getStatus());
                    args.put("price", modelList.get(position).getPrice());
                    args.put("mrp", modelList.get(position).getMrp());
                    args.put("unit_value", modelList.get(position).getUnit_value());
                    args.put("unit", modelList.get(position).getUnit());
                    args.put("product_attribute", modelList.get(position).getProduct_attribute());
                    args.put("rewards", modelList.get(position).getRewards());
                    args.put("increment", modelList.get(position).getIncreament());
                    args.put("title", modelList.get(position).getTitle());

                    intent.putExtra("details", args);
                   context.startActivity(intent);


            }
                else if(id == R.id.btn_add)
                {
                    Product_model mList=modelList.get(position);
                    String atr=String.valueOf(modelList.get(position).getProduct_attribute());
                    if(atr.equals("[]"))
                    {
                        HashMap<String, String> mapProduct = new HashMap<String, String>();
                        String unt=String.valueOf( mList.getUnit_value()+" "+mList.getUnit());
                        mapProduct.put("cart_id", mList.getProduct_id());
                        mapProduct.put("product_id", mList.getProduct_id());
                        mapProduct.put("product_image",mList.getProduct_image());
                        mapProduct.put("cat_id",mList.getCategory_id());
                        mapProduct.put("product_name",mList.getProduct_name());
                        mapProduct.put("price", mList.getPrice());
                        mapProduct.put("unit_price",mList.getPrice());
                        mapProduct.put("unit",unt);
                        mapProduct.put("mrp",mList.getMrp());
                        mapProduct.put("type","p");
                        try {

                            boolean tr = db_cart.setCart(mapProduct, (float) 1 );
                            if (tr == true) {
                                MainActivity mainActivity = new MainActivity();
                             //   mainActivity.setCartCounter("" + db_cart.getCartCount());

                                //   context.setCartCounter("" + holder.db_cart.getCartCount());
                                Toast.makeText(context, "Added to Cart", Toast.LENGTH_LONG).show();
                                int n= db_cart.getCartCount();
                                updateintent();


                            }
                            else if(tr==false)
                            {
                                Toast.makeText(context,"cart updated",Toast.LENGTH_LONG).show();
                            }

                        } catch (Exception ex) {
                            Toast.makeText(context, "" + ex.getMessage(), Toast.LENGTH_LONG).show();
                        }

                        //Toast.makeText(context,"1\n"+status+"\n"+modelList.get(position).getProduct_attribute(),Toast.LENGTH_LONG).show();
                    }
                    else
                    {
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
                        mapProduct.put("product_id", mList.getProduct_id());
                        mapProduct.put("product_image",mList.getProduct_image());
                        mapProduct.put("cat_id",mList.getCategory_id());
                        mapProduct.put("product_name",mList.getProduct_name());
                        mapProduct.put("price", st0);
                        mapProduct.put("unit_price",st0);
                        mapProduct.put("unit",st1);
                        mapProduct.put("mrp",st2);
                        mapProduct.put("type","a");
                        //  Toast.makeText(context,""+attributeList.get(j).getId()+"\n"+mapProduct,Toast.LENGTH_LONG).show();
                        try {

                            boolean tr = db_cart.setCart(mapProduct, (float) 1 );
                            if (tr == true) {
                                MainActivity mainActivity = new MainActivity();
                               // mainActivity.setCartCounter("" + db_cart.getCartCount());

                                //   context.setCartCounter("" + holder.db_cart.getCartCount());
                                Toast.makeText(context, "Added to Cart", Toast.LENGTH_LONG).show();
                                int n= db_cart.getCartCount();
                                updateintent();


                            }
                            else if(tr==false)
                            {
                                Toast.makeText(context,"cart updated",Toast.LENGTH_LONG).show();
                            }

                        } catch (Exception ex) {
                            Toast.makeText(context, "" + ex.getMessage(), Toast.LENGTH_LONG).show();
                        }

                    }
                    updateintent();
                    add.setVisibility(View.GONE);
                    elegantNumberButton.setNumber("1");
                    elegantNumberButton.setVisibility(View.VISIBLE);

                }
                else if(id==R.id.rel_variant)
                {
                  //  AlertDialog dlg=null;

                    final Product_model mList = modelList.get(position);
                    AlertDialog.Builder builder=new AlertDialog.Builder(context);
                    LayoutInflater layoutInflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View row=layoutInflater.inflate(R.layout.dialog_vairant_layout,null);
                  variantList.clear();
                    String atr=String.valueOf(mList.getProduct_attribute());
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
                    productVariantAdapter=new ProductVariantAdapter(context,variantList);
                    //productVariantAdapter.notifyDataSetChanged();
                    l1.setAdapter(productVariantAdapter);


                    builder.setView(row);
                    final AlertDialog ddlg=builder.create();
                    ddlg.show();
                    l1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {



                            dialog_unit_type.setText("\u20B9"+variantList.get(i).getAttribute_value()+"/"+variantList.get(i).getAttribute_name());
                            dialog_txtId.setText(variantList.get(i).getId()+"@"+i);
                            dialog_txtVar.setText(variantList.get(i).getAttribute_value()+"@"+variantList.get(i).getAttribute_name()+"@"+variantList.get(i).getAttribute_mrp());
                            //    txtPer.setText(String.valueOf(df)+"% off");
                            String pr=String.valueOf(variantList.get(i).getAttribute_value());
                            String mr=String.valueOf(variantList.get(i).getAttribute_mrp());
                            int m =Integer.parseInt( mr );
                            int p = Integer.parseInt( pr );
                            tv_price.setText("\u20B9"+variantList.get(i).getAttribute_value().toString());
                            if (m>p) {
                                product_mrp.setText( "\u20B9" + variantList.get( i ).getAttribute_mrp().toString() );

                                int atr_dis = getDiscount( pr, mr );
                                discount.setText( "" + atr_dis + "% OFF" );
                            }
                            else
                                product_mrp.setVisibility( View.GONE );
                                discount.setVisibility( View.GONE );
                            elegantNumberButton.setNumber("1");
                            elegantNumberButton.setVisibility(View.GONE);
                            add.setVisibility(View.VISIBLE);
                          ddlg.dismiss();
                        }
                    });



                }

            else if(id==R.id.wish_before) {
                    final Product_model mList = modelList.get(position);
                    wish_after.setVisibility( View.VISIBLE );
                    wish_before.setVisibility( View.INVISIBLE );












                    HashMap<String, String> mapProduct = new HashMap<String, String>();
                    mapProduct.put("product_id", mList.getProduct_id());
                    mapProduct.put("product_images",mList.getProduct_image());
                    mapProduct.put("cat_id",mList.getCategory_id());
                    mapProduct.put("product_name",mList.getProduct_name());
                    mapProduct.put("price", mList.getPrice());
                    mapProduct.put("product_description",mList.getProduct_description());
                    mapProduct.put("rewards", mList.getRewards());
                    mapProduct.put("unit_value",mList.getUnit_value());
                    mapProduct.put("unit", mList.getUnit());
                    mapProduct.put("increment",mList.getIncreament());
                    mapProduct.put("stock",mList.getStock());
                    mapProduct.put("title",mList.getTitle());
                    mapProduct.put("mrp",mList.getMrp());
                    mapProduct.put("product_attribute",mList.getProduct_attribute());
                    mapProduct.put("in_stock",mList.getIn_stock());
                    mapProduct.put("status",mList.getStatus());

                       // Toast.makeText(context,""+mapProduct,Toast.LENGTH_LONG).show();


                    try {

                        boolean tr =db_wish.setwishTable(mapProduct);
                        if (tr == true) {

                            //   context.setCartCounter("" + holder.db_cart.getCartCount());
                            Toast.makeText(context, "Added to Wishlist" , Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            Toast.makeText(context, "Something Went Wrong", Toast.LENGTH_LONG).show();
                        }

                    } catch (Exception ex) {
                        ex.printStackTrace();
                      //  Toast.makeText(context, "" + ex.getMessage(), Toast.LENGTH_LONG).show();
                    }


                    //   Toast.makeText(context,"wish",Toast.LENGTH_LONG).show();
                              //  AppCompatActivity activity = (AppCompatActivity) view.getContext();

            }
            else if (id == R.id.wish_after) {
                    final Product_model mList = modelList.get(position);
                wish_after.setVisibility( View.INVISIBLE );
                wish_before.setVisibility( View.VISIBLE );
               db_wish.removeItemFromWishtable(mList.getProduct_id());

               Toast.makeText(context, "removed from Wishlist", Toast.LENGTH_LONG).show();
               // list.remove(position);
              notifyDataSetChanged();

            }



        }

    }

    public Product_adapter(List<Product_model> modelList, Context context) {
        this.modelList = modelList;
        this.context=context;
        dbcart = new DatabaseHandler(context);
        db_cart=new DatabaseCartHandler(context);
    }

    @Override
    public Product_adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_product_rv, parent, false);

        return new Product_adapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final Product_adapter.MyViewHolder holder, final int position) {
        final Product_model mList = modelList.get(position);
        final String getid = mList.getProduct_id();
        if(holder.db_wish.isInWishtable( getid ))
        {
            holder.wish_after.setVisibility( View.VISIBLE );
            holder.wish_before.setVisibility( View.GONE );
        }
        else
        {
            holder.wish_after.setVisibility( View.GONE );
            holder.wish_before.setVisibility( View.VISIBLE );
        }

//        double stock=Double.parseDouble(modelList.get(position).getStock());
//        if(stock<1) {
//            rel_out.setVisibility(View.VISIBLE);
//        }
        try
        {
            image_list.clear();
            JSONArray array=new JSONArray(mList.getProduct_image());
            //Toast.makeText(this,""+product_images,Toast.LENGTH_LONG).show();
            if(mList.getProduct_image().equals(null))
            {
                Glide.with(context)
                        .load(Url.IMG_PRODUCT_URL +mList.getProduct_image() )
                        .fitCenter()
                        .placeholder(R.drawable.logo)
                        .crossFade()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .dontAnimate()
                        .into(holder.iv_logo);
            }
            else
            {
                for(int i=0; i<=array.length()-1;i++)
                {
                    image_list.add(array.get(i).toString());

                }


                Glide.with(context)
                        .load(Url.IMG_PRODUCT_URL +image_list.get(0) )
                        .fitCenter()
                                  .placeholder(R.drawable.logo)
                        .crossFade()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .dontAnimate()
                        .into(holder.iv_logo);
            }


            // Toast.makeText(Product_Frag_details.this,""+image_list.toString(),Toast.LENGTH_LONG).show();

        }
        catch (Exception ex)
        {
            // Toast.makeText(Product_Frag_details.this,""+ex.getMessage(),Toast.LENGTH_LONG).show();
        }
//


        preferences = context.getSharedPreferences("lan", MODE_PRIVATE);
        language=preferences.getString("language","");
        if (language.contains("english")) {
            holder.tv_title.setText(mList.getProduct_name());
        }
        else {
            holder.tv_title.setText(mList.getProduct_name());


        }

        holder.product_mrp.setPaintFlags( holder.product_mrp.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);


        String atr=String.valueOf(mList.getProduct_attribute());
        if(atr.equals("[]"))
        {
         status=1;

            String p=String.valueOf(mList.getPrice());
            String m=String.valueOf(mList.getMrp());
            int mm = Integer.parseInt( m );
            int pp =Integer.parseInt( p );
            holder.tv_price.setText(context.getResources().getString(R.string.currency)+ mList.getPrice());
            if(mm>pp) {
                holder.product_mrp.setText( context.getResources().getString( R.string.currency ) + mList.getMrp() );
                int discount = getDiscount( p, m );
                //Toast.makeText(getActivity(),""+atr,Toast.LENGTH_LONG).show();
                holder.discount.setText( "" + discount + "% OFF" );
                holder.product_mrp.setVisibility( View.VISIBLE );
                holder.discount.setVisibility( View.VISIBLE );
            }
            else
            {
                holder.product_mrp.setVisibility( View.GONE );
                holder.discount.setVisibility( View.GONE );
            }
            holder.txtrate.setVisibility(View.VISIBLE);
            holder.rel_variant.setVisibility(View.GONE);
            holder.txtrate.setText(mList.getUnit_value()+" "+mList.getUnit());

        }

        else
        {

            status=2;
        attributeList.clear();
            holder.rel_variant.setVisibility(View.VISIBLE);
            holder.txtrate.setVisibility(View.GONE);
//            String atr=String.valueOf(mList.getProduct_attribute());
            JSONArray jsonArr = null;
            try {

                jsonArr = new JSONArray(atr);
                for (int i = 0; i < jsonArr.length(); i++)
                {
                    ProductVariantModel model=new ProductVariantModel();
                    JSONObject jsonObj = jsonArr.getJSONObject(i);
                    String atrid=jsonObj.getString("id");
                    String atrproductid=jsonObj.getString("product_id");
                    String attributename=jsonObj.getString("attribute_name");
                    String attributevalue=jsonObj.getString("attribute_value");
                    String attributemrp=jsonObj.getString("attribute_mrp");


                    model.setId(atrid);
                    model.setProduct_id(atrproductid);
                    model.setAttribute_value(attributevalue);
                    model.setAttribute_name(attributename);
                    model.setAttribute_mrp(attributemrp);

                    attributeList.add(model);

                    //     arrayList.add(new AttributeModel(atr_id,product_id,attribute_name,attribute_value));

                    //Toast.makeText(getActivity(),"id "+atr_id+"\n p_id "+product_id+"\n atr_name "+attribute_name+"\n atr_value "+attribute_value,Toast.LENGTH_LONG).show();
                }



            } catch (JSONException e) {
                e.printStackTrace();
            }


            try
            {




                     atr_id=attributeList.get(0).getId();
                     atr_product_id=attributeList.get(0).getProduct_id();
                     attribute_name=attributeList.get(0).getAttribute_name();
                     attribute_value=attributeList.get(0).getAttribute_value();
                     attribute_mrp=attributeList.get(0).getAttribute_mrp();



                    //     arrayList.add(new AttributeModel(atr_id,product_id,attribute_name,attribute_value));

                    //Toast.makeText(getActivity(),"id "+atr_id+"\n p_id "+product_id+"\n atr_name "+attribute_name+"\n atr_value "+attribute_value,Toast.LENGTH_LONG).show();



                String atr_price=String.valueOf(attribute_value);
                String atr_mrp=String.valueOf(attribute_mrp);
                int atr_m =Integer.parseInt( atr_mrp );
                int atr_p =Integer.parseInt( atr_price );
                holder.tv_price.setText("\u20B9"+attribute_value.toString());
                if (atr_m>atr_p) {
                    int atr_dis = getDiscount( atr_price, atr_mrp );
                    holder.discount.setText( "" + atr_dis + "% OFF" );

                    holder.product_mrp.setText( "\u20B9" + attribute_mrp.toString() );
                }
                else
                {
                    holder.discount.setVisibility( View.GONE );
                    holder.product_mrp.setVisibility( View.GONE );
                }
                holder.dialog_txtId.setText(atr_id.toString()+"@"+"0");
              holder.dialog_unit_type.setText("\u20B9"+attribute_value+"/"+attribute_name);
                holder.dialog_txtVar.setText(attribute_value+"@"+attribute_name+"@"+attribute_mrp);
              //  holder.txtTotal.setText("\u20B9"+String.valueOf(list_atr_value.get(0).toString()));

            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }


        }

        final String product_id=String.valueOf(mList.getProduct_id());
        if(atr.equals("[]"))
        {
            boolean st=db_cart.isInCart(product_id);
            if(st==true)
            {
                holder.add.setVisibility(View.GONE);
                holder.elegantNumberButton.setNumber(db_cart.getCartItemQty(product_id));
                holder.elegantNumberButton.setVisibility(View.VISIBLE);
            }
            else
            {
                holder.add.setVisibility(View.VISIBLE);
               // holder.elegantNumberButton.setNumber(db_cart.getCartItemQty(product_id));
                holder.elegantNumberButton.setVisibility(View.GONE);
            }

        }
        else
        {
            String str_id=holder.dialog_txtId.getText().toString();
            String[] str=str_id.split("@");
            String at_id=String.valueOf(str[0]);
            boolean st=db_cart.isInCart(at_id);
            if(st==true)
            {
                holder.add.setVisibility(View.GONE);
                holder.elegantNumberButton.setNumber(db_cart.getCartItemQty(at_id));
                holder.elegantNumberButton.setVisibility(View.VISIBLE);
            }
            else {
                holder.add.setVisibility(View.VISIBLE);
                //  holder.elegantNumberButton.setNumber(db_cart.getCartItemQty(product_id));
                holder.elegantNumberButton.setVisibility(View.GONE);

            }
        }


//        holder.add.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Product_model mList=modelList.get(position);
//                    String atr=String.valueOf(modelList.get(position).getProduct_attribute());
//                    if(atr.equals("[]"))
//                    {
//                        HashMap<String, String> mapProduct = new HashMap<String, String>();
//                      String unt=String.valueOf( mList.getUnit_value()+" "+mList.getUnit());
//                        mapProduct.put("cart_id", mList.getProduct_id());
//                        mapProduct.put("product_id", mList.getProduct_id());
//                        mapProduct.put("product_image",mList.getProduct_image());
//                        mapProduct.put("cat_id",mList.getCategory_id());
//                        mapProduct.put("product_name",mList.getProduct_name());
//                        mapProduct.put("price", mList.getPrice());
//                        mapProduct.put("unit_price",mList.getPrice());
//                        mapProduct.put("unit",unt);
//                        mapProduct.put("mrp",mList.getMrp());
//                        mapProduct.put("type","p");
//                        try {
//
//                            boolean tr = db_cart.setCart(mapProduct, (float) 1 );
//                            if (tr == true) {
//                                MainActivity mainActivity = new MainActivity();
//                                mainActivity.setCartCounter("" + db_cart.getCartCount());
//
//                                //   context.setCartCounter("" + holder.db_cart.getCartCount());
//                                Toast.makeText(context, "Added to Cart", Toast.LENGTH_LONG).show();
//                                int n= db_cart.getCartCount();
//                                updateintent();
//
//
//                            }
//                            else if(tr==false)
//                            {
//                                Toast.makeText(context,"cart updated",Toast.LENGTH_LONG).show();
//                            }
//
//                        } catch (Exception ex) {
//                            Toast.makeText(context, "" + ex.getMessage(), Toast.LENGTH_LONG).show();
//                        }
//
//                        //Toast.makeText(context,"1\n"+status+"\n"+modelList.get(position).getProduct_attribute(),Toast.LENGTH_LONG).show();
//                    }
//                    else
//                    {
//                        //ProductVariantModel model=variantList.get(position);
//
//                        String str_id=holder.dialog_txtId.getText().toString();
//                        String s=holder.dialog_txtVar.getText().toString();
//                        String[] st=s.split("@");
//                        String st0=String.valueOf(st[0]);
//                        String st1=String.valueOf(st[1]);
//                        String st2=String.valueOf(st[2]);
//                        String[] str=str_id.split("@");
//                        String at_id=String.valueOf(str[0]);
//                        int j=Integer.parseInt(String.valueOf(str[1]));
//                 //       Toast.makeText(context,""+str[0].toString()+"\n"+str[1].toString(),Toast.LENGTH_LONG).show();
//                        HashMap<String, String> mapProduct = new HashMap<String, String>();
//                        mapProduct.put("cart_id",at_id);
//                        mapProduct.put("product_id", mList.getProduct_id());
//                        mapProduct.put("product_image",mList.getProduct_image());
//                        mapProduct.put("cat_id",mList.getCategory_id());
//                        mapProduct.put("product_name",mList.getProduct_name());
//                        mapProduct.put("price", st0);
//                        mapProduct.put("unit_price",st0);
//                        mapProduct.put("unit",st1);
//                        mapProduct.put("mrp",st2);
//                        mapProduct.put("type","a");
//                      //  Toast.makeText(context,""+attributeList.get(j).getId()+"\n"+mapProduct,Toast.LENGTH_LONG).show();
//                        try {
//
//                            boolean tr = db_cart.setCart(mapProduct, (float) 1 );
//                            if (tr == true) {
//                                MainActivity mainActivity = new MainActivity();
//                                mainActivity.setCartCounter("" + db_cart.getCartCount());
//
//                                //   context.setCartCounter("" + holder.db_cart.getCartCount());
//                                Toast.makeText(context, "Added to Cart", Toast.LENGTH_LONG).show();
//                                int n= db_cart.getCartCount();
//                                updateintent();
//
//
//                            }
//                            else if(tr==false)
//                            {
//                                Toast.makeText(context,"cart updated",Toast.LENGTH_LONG).show();
//                            }
//
//                        } catch (Exception ex) {
//                            Toast.makeText(context, "" + ex.getMessage(), Toast.LENGTH_LONG).show();
//                        }
//
//                    }
//                updateintent();
//                    holder.add.setVisibility(View.GONE);
//                    notifyDataSetChanged();
//                    holder.elegantNumberButton.setVisibility(View.VISIBLE);
//            }
//        });

        holder.elegantNumberButton.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
            @Override
            public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {

                String atr=String.valueOf(modelList.get(position).getProduct_attribute());
                if(newValue<=0)
                {
                   // Toast.makeText(context,""+newValue,Toast.LENGTH_LONG).show();
                    boolean st=checkAttributeStatus(atr);
                    if(st==false)
                    {
                        db_cart.removeItemFromCart(product_id);

                    }
                    else if(st==true)
                    {

                        String str_id = holder.dialog_txtId.getText().toString();
                        String[] str = str_id.split("@");
                        String at_id = String.valueOf(str[0]);
                        db_cart.removeItemFromCart(at_id);
                    }

                    holder.elegantNumberButton.setVisibility(View.GONE);
                    holder.add.setVisibility(View.VISIBLE);
                    updateintent();
                }
                else {

                    final Product_model mList = modelList.get(position);
                    float qty = Float.parseFloat(String.valueOf(newValue));

                    String satr = String.valueOf(modelList.get(position).getProduct_attribute());
                    if (satr.equals("[]")) {
                        double pr = Double.parseDouble(mList.getPrice());
                        double amt = pr * qty;
                        HashMap<String, String> mapProduct = new HashMap<String, String>();
                        String unt = String.valueOf(mList.getUnit_value() + " " + mList.getUnit());
                        mapProduct.put("cart_id", mList.getProduct_id());
                        mapProduct.put("product_id", mList.getProduct_id());
                        mapProduct.put("product_image", mList.getProduct_image());
                        mapProduct.put("cat_id", mList.getCategory_id());
                        mapProduct.put("product_name", mList.getProduct_name());
                        mapProduct.put("price", String.valueOf(amt));
                        mapProduct.put("unit_price", mList.getPrice());
                        mapProduct.put("unit", unt);
                        mapProduct.put("mrp", mList.getMrp());
                        mapProduct.put("type", "p");
                        try {

                            boolean tr = db_cart.setCart(mapProduct, qty);
                            if (tr == true) {
                                MainActivity mainActivity = new MainActivity();
                               // mainActivity.setCartCounter("" + db_cart.getCartCount());

                                //   context.setCartCounter("" + holder.db_cart.getCartCount());
                                Toast.makeText(context, "Added to Cart", Toast.LENGTH_LONG).show();
                                int n = db_cart.getCartCount();
                                updateintent();


                            } else if (tr == false) {
                                Toast.makeText(context, "cart updated", Toast.LENGTH_LONG).show();
                            }

                        } catch (Exception ex) {
                            Toast.makeText(context, "" + ex.getMessage(), Toast.LENGTH_LONG).show();
                        }

                        //Toast.makeText(context,"1\n"+status+"\n"+modelList.get(position).getProduct_attribute(),Toast.LENGTH_LONG).show();
                    } else {
                        //ProductVariantModel model=variantList.get(position);

                        String str_id = holder.dialog_txtId.getText().toString();


                        String s = holder.dialog_txtVar.getText().toString();
                        String[] st = s.split("@");
                        String st0 = String.valueOf(st[0]);
                        String st1 = String.valueOf(st[1]);
                        String st2 = String.valueOf(st[2]);
                        String[] str = str_id.split("@");
                        String at_id = String.valueOf(str[0]);
                        int k = Integer.parseInt(String.valueOf(str[1]));
                        double pr = Double.parseDouble(st0);
                        double amt = pr * qty;
                        //       Toast.makeText(context,""+str[0].toString()+"\n"+str[1].toString(),Toast.LENGTH_LONG).show();
                        HashMap<String, String> mapProduct = new HashMap<String, String>();
                        mapProduct.put("cart_id", at_id);
                        mapProduct.put("product_id", mList.getProduct_id());
                        mapProduct.put("product_image", mList.getProduct_image());
                        mapProduct.put("cat_id", mList.getCategory_id());
                        mapProduct.put("product_name", mList.getProduct_name());
                        mapProduct.put("price", String.valueOf(amt));
                        mapProduct.put("unit_price", st0);
                        mapProduct.put("unit", st1);
                        mapProduct.put("mrp", st2);
                        mapProduct.put("type", "a");
                        //  Toast.makeText(context,""+attributeList.get(j).getId()+"\n"+mapProduct,Toast.LENGTH_LONG).show();
                        try {

                            boolean tr = db_cart.setCart(mapProduct, qty);
                            if (tr == true) {
                                MainActivity mainActivity = new MainActivity();
                               // mainActivity.setCartCounter("" + db_cart.getCartCount());

                                //   context.setCartCounter("" + holder.db_cart.getCartCount());
                                Toast.makeText(context, "Added to Cart", Toast.LENGTH_LONG).show();
                                int n = db_cart.getCartCount();
                                updateintent();


                            } else if (tr == false) {
                                Toast.makeText(context, "cart updated", Toast.LENGTH_LONG).show();
                            }

                        } catch (Exception ex) {
                            Toast.makeText(context, "" + ex.getMessage(), Toast.LENGTH_LONG).show();
                        }

                    }
                }

            }
        });


        //holder.tv_reward.setText(mList.getRewards());
        //holder.tv_price.setText(context.getResources().getString(R.string.currency)+ mList.getPrice()+" / "+ mList.getUnit_value()+" "+mList.getUnit());



//        holder.tv_price.setText(context.getResources().getString(R.string.tv_pro_price) + mList.getUnit_value() + " " +
//                mList.getUnit() +" \n"+ mList.getPrice()+ context.getResources().getString(R.string.currency));
//        Double items = Double.parseDouble(dbcart.getInCartItemQty(mList.getProduct_id()));
//        Double prices = Double.parseDouble(mList.getPrice());
//        Double reward = Double.parseDouble(mList.getRewards());
//        //holder.tv_total.setText("" + price * items);
     //  holder.tv_reward.setText("" + reward * items);




//       holder.tv_add.setOnClickListener(new View.OnClickListener() {
//           @Override
//           public void onClick(View view) {
//
//               int sd=db_cart.getCartCount();
//
//               Toast.makeText(context,""+sd,Toast.LENGTH_LONG).show();
//
//           }
//       });

//       holder.con_product.setOnClickListener(new View.OnClickListener() {
//           @Override
//           public void onClick(View view) {
//
//         int y=dbcart.getCartCount();
//          //     Toast.makeText(context,""+y,Toast.LENGTH_LONG).show();
//
//
//               Bundle args = new Bundle();
//
//               Intent intent=new Intent(context, Product_details.class);
//               args.putString("cat_id",mList.getCategory_id());
//               args.putString("product_id",mList.getProduct_id());
//               args.putString("product_images",mList.getProduct_image());
//
//               args.putString("product_name",mList.getProduct_name());
//               args.putString("product_description",mList.getProduct_description());
//               args.putString("product_in_stock",mList.getIn_stock());
//               args.putString("product_size",mList.getSize());
//               args.putString("product_color",mList.getColor());
//               args.putString("product_price",mList.getPrice());
//               args.putString("product_mrp",mList.getIncreament());
//               args.putString("product_unit_value",mList.getUnit_value());
//               args.putString("product_unit",mList.getUnit());
//               args.putString("product_rewards",mList.getRewards());
//               args.putString("product_increament",mList.getIncreament());
//               args.putString("product_title",mList.getTitle());
//
//               Product_Frag_details product_frag_details=new Product_Frag_details();
//
//               product_frag_details.setArguments(args);
//               AppCompatActivity activity=(AppCompatActivity) view.getContext();
//               activity.getSupportFragmentManager().beginTransaction().replace(R.id.contentPanel,product_frag_details)
//                       .addToBackStack(null)
//                       .commit();
//
//           }
//      });
    }

    @Override
    public int getItemCount() {
        return modelList.size();
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

    private void updateintent() {
        Intent updates = new Intent("Grocery_cart");
        updates.putExtra("type", "update");
        context.sendBroadcast(updates);
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

}