package in.mapbazar.mapbazar.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.json.JSONArray;
import org.json.JSONException;

import in.mapbazar.mapbazar.ActivityCart;
import in.mapbazar.mapbazar.R;
import in.mapbazar.mapbazar.Utili.Url;
import in.mapbazar.mapbazar.View.CustomTextView;
import in.mapbazar.mapbazar.util.DatabaseCartHandler;

import java.util.ArrayList;
import java.util.HashMap;

import static android.content.Context.MODE_PRIVATE;


public class Cart_Adapter extends RecyclerView.Adapter<Cart_Adapter.ProductHolder> {
        ArrayList<HashMap<String, String>> list;
        Activity activity;
        String Reward;
        Double price ,reward ;
        SharedPreferences preferences;
        String language;
        int qty = 0;

        int lastpostion;
        // DatabaseHandler dbHandler;
        DatabaseCartHandler db_cart;

public Cart_Adapter(Activity activity, ArrayList<HashMap<String, String>> list) {
        this.list = list;
        this.activity = activity;

        //dbHandler = new DatabaseHandler(activity);
        db_cart=new DatabaseCartHandler(activity);
        /*common = new CommonClass(activity);
        File cacheDir = StorageUtils.getCacheDirectory(activity);
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.loading)
                .showImageForEmptyUri(R.drawable.loading)
                .showImageOnFail(R.drawable.loading)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .displayer(new SimpleBitmapDisplayer())
                .imageScaleType(ImageScaleType.EXACTLY)
                .build();

        imgconfig = new ImageLoaderConfiguration.Builder(activity)
                .build();
        ImageLoader.getInstance().init(imgconfig);*/
        }

@Override
public ProductHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_cart_rv, parent, false);
        return new ProductHolder(view);
        }

@Override
public void onBindViewHolder(final ProductHolder holder, final int position) {
final HashMap<String, String> map = list.get(position);

        String img_array=map.get("product_image");
        String img_name = null;
        try {
        JSONArray array=new JSONArray(img_array);
        img_name=array.get(0).toString();
        } catch (JSONException e) {
        e.printStackTrace();
        }

        Glide.with(activity)
        .load(Url.IMG_PRODUCT_URL + img_name)
        .centerCrop()
        .placeholder(R.drawable.logo)
        .crossFade()
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .dontAnimate()
        .into(holder.iv_logo);
        preferences = activity.getSharedPreferences("lan", MODE_PRIVATE);
        language=preferences.getString("language","");
        holder.tv_title.setText(map.get("product_name"));


        holder.tv_price.setText(activity.getResources().getString(R.string.currency)+map.get("unit_price"));
        holder.tv_contetiy.setText(map.get("qty"));
        int items = Integer.parseInt(db_cart.getInCartItemQty(map.get("cart_id")));
        price = Double.parseDouble(map.get("unit_price"));
        holder.tv_subcat_weight.setText("Weight : "+map.get("unit"));
        holder.tv_total.setText("" + price * items);
         holder.tv_reward.setText("" +map.get( "rewards" ));
        // holder.btnQty.setNumber(String.valueOf(items));


        holder.iv_minus.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View view) {


        int id=Integer.parseInt(map.get("cart_id"));

        ArrayList<HashMap<String, String>> mapP=db_cart.getCartProduct(id);

        HashMap<String,String> m=mapP.get(0);



        // Toast.makeText(activity,"Count"+m.get("price"),Toast.LENGTH_LONG).show();

        if (!holder.tv_contetiy.getText().toString().equalsIgnoreCase(""))
        qty = Integer.valueOf(holder.tv_contetiy.getText().toString());

        if (qty > 0) {
        qty = qty - 1;
        holder.tv_contetiy.setText(String.valueOf(qty));
        double t=Double.parseDouble(m.get("price"));
        double p=Double.parseDouble(m.get("unit_price"));
        holder.tv_total.setText("" + p * qty);
        String pr=String.valueOf(t-p);
        float qt=Float.valueOf(qty);

        HashMap<String, String> mapProduct = new HashMap<String, String>();
        mapProduct.put("cart_id", map.get("cart_id"));
        mapProduct.put("product_id", map.get("product_id"));
        mapProduct.put("product_image", map.get("product_image"));
        mapProduct.put("cat_id", map.get("cat_id"));
        mapProduct.put("product_name",map.get("product_name"));
        mapProduct.put("price",pr);
        mapProduct.put("unit_price",map.get("unit_price"));
        mapProduct.put( "rewards",map.get( "rewards" ) );
        mapProduct.put("unit", map.get("unit"));
        mapProduct.put("mrp",map.get("mrp"));

//
//                Toast.makeText(activity,"id- "+map.get("product_id")+"\n img- "+map.get("product_image")+"\n cat_id- "+map.get("category_id")+"\n" +
//                        "\n name- "+map.get("product_name")+"\n price- "+pr+"\n unit_price- "+map.get("unit_price")+
//                        "\n size- "+ map.get("size")+"\n col- "+ map.get("color")+"rew- "+ map.get("rewards")+"unit_value- "+ map.get("unit_value")+
//                        "unit- "+map.get("unit")+"\n inc- "+map.get("increament")+"stock- "+map.get("stock")+"title- "+map.get("title"),Toast.LENGTH_LONG).show();

        boolean update_cart=db_cart.setCart(mapProduct,qt);
        if(update_cart==true)
        {
        Toast.makeText(activity,"Qty Not Updated",Toast.LENGTH_LONG).show();

        }
        else
        {
        Toast.makeText(activity,"Qty Updated",Toast.LENGTH_LONG).show();
        ActivityCart.tv_total.setText(activity.getResources().getString(R.string.currency)+" "+db_cart.getTotalAmount());
        }

        }

        if (holder.tv_contetiy.getText().toString().equalsIgnoreCase("0")) {

        String type=String.valueOf(map.get("type"));
        if(type.equals("p"))
        {
        db_cart.removeItemFromCart(map.get("product_id"));

        }
        else if(type.equals("a"))
        {
        db_cart.removeItemFromCart(map.get("cart_id"));
        }
        list.remove(position);
        notifyDataSetChanged();

        // db_cart.getCartAll()
        updateintent();
        }
        }
        });

        holder.iv_plus.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View view) {

        int qty = Integer.parseInt(holder.tv_contetiy.getText().toString());
        qty = qty + 1;

        holder.tv_contetiy.setText(String.valueOf(qty));

        //    holder.tv_reward.setText("" + reward * qty);
        int id=Integer.parseInt(map.get("cart_id"));

        ArrayList<HashMap<String, String>> mapP=db_cart.getCartProduct(id);

        HashMap<String,String> m=mapP.get(0);

        double t=Double.parseDouble(m.get("price"));
        double p=Double.parseDouble(m.get("unit_price"));
        holder.tv_total.setText("" + p * qty);
        String pr=String.valueOf(t+p);
        float qt=Float.valueOf(qty);

        // Toast.makeText(activity,"\npri "+map.get("unit_value")+"\n am "+pr,Toast.LENGTH_LONG ).show();
        HashMap<String, String> mapProduct = new HashMap<String, String>();
        mapProduct.put("cart_id", map.get("cart_id"));
        mapProduct.put("product_id", map.get("product_id"));
        mapProduct.put("product_image", map.get("product_image"));
        mapProduct.put("cat_id", map.get("cat_id"));
        mapProduct.put("product_name",map.get("product_name"));
        mapProduct.put("price",pr);
        mapProduct.put("mrp",map.get("mrp"));
        mapProduct.put("unit_price",map.get("unit_price"));
        mapProduct.put("unit", map.get("unit"));
        mapProduct.put( "rewards",map.get( "rewards" ) );
//
//                Toast.makeText(activity,"id- "+map.get("product_id")+"\n img- "+map.get("product_image")+"\n cat_id- "+map.get("category_id")+"\n" +
//                        "\n name- "+map.get("product_name")+"\n price- "+pr+"\n unit_price- "+map.get("unit_price")+
//                        "\n size- "+ map.get("size")+"\n col- "+ map.get("color")+"rew- "+ map.get("rewards")+"unit_value- "+ map.get("unit_value")+
//                        "unit- "+map.get("unit")+"\n inc- "+map.get("increament")+"stock- "+map.get("stock")+"title- "+map.get("title"),Toast.LENGTH_LONG).show();

        boolean update_cart=db_cart.setCart(mapProduct,qt);
        if(update_cart==true)
        {
        Toast.makeText(activity,"Qty Not Updated",Toast.LENGTH_LONG).show();

        }
        else
        {
        Toast.makeText(activity,"Qty Updated",Toast.LENGTH_LONG).show();
        ActivityCart.tv_total.setText(activity.getResources().getString(R.string.currency)+" "+db_cart.getTotalAmount());
        }
        //  holder.tv_total.setText(""+db_cart.getTotalAmount());
        }
        });
//
//


        holder.tv_total.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View view) {
        int cnt=db_cart.getCartCount();
        Toast.makeText(activity,"id- "+map.get("product_id")+"\n img- "+map.get("product_image")+"\n cat_id- "+map.get("category_id")+"\n" +
        "\n name- "+map.get("product_name")+"\n price- "+map.get("price")+"\n unit_price- "+map.get("unit_price")+
        "\n size- "+ map.get("size")+"\n col- "+ map.get("color")+"rew- "+ map.get("rewards")+"unit_value- "+ map.get("unit_value")+
        "unit- "+map.get("unit")+"\n inc- "+map.get("increament")+"stock- "+map.get("stock")+"title- "+map.get("title")+"cnt- "+cnt,Toast.LENGTH_LONG).show();


        }
        });


//       holder.tv_add.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                db_cart.setCart(map, Float.valueOf(holder.tv_contetiy.getText().toString()));
//
//                Double items = Double.parseDouble(db_cart.getInCartItemQty(map.get("product_id")));
//                Double price = Double.parseDouble(map.get("price"));
//                Double reward = Double.parseDouble(map.get("rewards"));
//                holder.tv_total.setText("" + price * qty);
//                holder.tv_reward.setText("" + reward * qty);
//             // holder.tv_total.setText(activity.getResources().getString(R.string.tv_cart_total) + price * items + " " + activity.getResources().getString(R.string.currency));
//                updateintent();
//            }
//        });

        holder.iv_remove.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View view) {
        String type=String.valueOf(map.get("type"));
        if(type.equals("p"))
        {
        db_cart.removeItemFromCart(map.get("product_id"));

        }
        else if(type.equals("a"))
        {
        db_cart.removeItemFromCart(map.get("cart_id"));
        }

        list.remove(position);
        notifyDataSetChanged();

        updateintent();
        }
        });

        }

@Override
public int getItemCount() {
        return list.size();
        }

class ProductHolder extends RecyclerView.ViewHolder {
    public CustomTextView tv_title, tv_price,  tv_total,tv_contetiy, tv_reward,
            tv_unit, tv_unit_value,tv_subcat_weight;

    // ElegantNumberButton btnQty;
    public ImageView iv_logo,iv_plus,iv_minus, iv_remove;

    public ProductHolder(View view) {
        super(view);

        tv_title = (CustomTextView) view.findViewById(R.id.tv_subcat_title);
        tv_price = (CustomTextView) view.findViewById(R.id.tv_subcat_price);
        tv_total = (CustomTextView) view.findViewById(R.id.tv_subcat_total);
        tv_reward = (CustomTextView) view.findViewById(R.id.tv_reward_point);
        tv_contetiy = (CustomTextView) view.findViewById(R.id.tv_subcat_contetiy);
        tv_subcat_weight = (CustomTextView) view.findViewById(R.id.tv_subcat_weight);
        //tv_add = (TextView) view.findViewById(R.id.tv_subcat_add);
        //  btnQty=(ElegantNumberButton)view.findViewById(R.id.product_qty);
        iv_logo = (ImageView) view.findViewById(R.id.iv_subcat_img);
        iv_plus = (ImageView) view.findViewById(R.id.iv_subcat_plus);
        iv_minus = (ImageView) view.findViewById(R.id.iv_subcat_minus);
        iv_remove = (ImageView) view.findViewById(R.id.iv_subcat_remove);

        //tv_add.setText(R.string.tv_pro_update);

    }
}

    private void updateintent() {
        Intent updates = new Intent("Grocery_cart");
        updates.putExtra("type", "update");
        activity.sendBroadcast(updates);
    }

}