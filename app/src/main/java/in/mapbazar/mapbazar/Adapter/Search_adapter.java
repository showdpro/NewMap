package in.mapbazar.mapbazar.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import in.mapbazar.mapbazar.ActivityProductDetails;
import in.mapbazar.mapbazar.Model.Product_model;
import in.mapbazar.mapbazar.R;
import in.mapbazar.mapbazar.Utili.Url;
import in.mapbazar.mapbazar.util.DatabaseCartHandler;


public class Search_adapter extends RecyclerView.Adapter<Search_adapter.MyViewHolder>
        implements Filterable {

    private List<Product_model> modelList;
    private List<Product_model> mFilteredList;
    private Context context;
    private DatabaseCartHandler dbcart;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView tv_title, tv_price, tv_reward, tv_total, tv_contetiy, tv_add;
        public ImageView iv_logo, iv_plus, iv_minus, iv_remove ;
        public RelativeLayout rel_search ;

        public MyViewHolder(View view) {
            super(view);
            tv_title = (TextView) view.findViewById(R.id.tv_subcat_title);
            tv_price = (TextView) view.findViewById(R.id.tv_subcat_price);
            tv_reward = (TextView) view.findViewById(R.id.tv_reward_point);
            tv_total = (TextView) view.findViewById(R.id.tv_subcat_total);
            tv_contetiy = (TextView) view.findViewById(R.id.tv_subcat_contetiy);
            tv_add = (TextView) view.findViewById(R.id.tv_subcat_add);
            iv_logo = (ImageView) view.findViewById(R.id.iv_subcat_img);
            iv_plus = (ImageView) view.findViewById(R.id.iv_subcat_plus);
            iv_minus = (ImageView) view.findViewById(R.id.iv_subcat_minus);
            iv_remove = (ImageView) view.findViewById(R.id.iv_subcat_remove);
            rel_search=(RelativeLayout)view.findViewById(R.id.rel_search);

            iv_remove.setVisibility(View.GONE);

            iv_minus.setOnClickListener(this);
            iv_plus.setOnClickListener(this);
            tv_add.setOnClickListener(this);
            iv_logo.setOnClickListener(this);
            rel_search.setOnClickListener(this);

            CardView cardView = (CardView) view.findViewById(R.id.card_view);
            cardView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            int id = view.getId();
            int position = getAdapterPosition();

//            if (id == R.id.iv_subcat_plus) {
//
//                int qty = Integer.valueOf(tv_contetiy.getText().toString());
//                qty = qty + 1;
//
//                tv_contetiy.setText(String.valueOf(qty));
//
//            } else if (id == R.id.iv_subcat_minus) {
//
//                int qty = 0;
//                if (!tv_contetiy.getText().toString().equalsIgnoreCase(""))
//                    qty = Integer.valueOf(tv_contetiy.getText().toString());
//
//                if (qty > 0) {
//                    qty = qty - 1;
//                    tv_contetiy.setText(String.valueOf(qty));
//                }
//
//            } else if (id == R.id.tv_subcat_add) {
//
//                HashMap<String, String> map = new HashMap<>();
//
//                map.put("product_id", modelList.get(position).getProduct_id());
//                map.put("product_name", modelList.get(position).getProduct_name());
//                map.put("category_id",modelList.get(position).getCategory_id());
//                map.put("product_description", modelList.get(position).getProduct_description());
//                map.put("deal_price", modelList.get(position).getDeal_price());
//                map.put("start_date", modelList.get(position).getStart_date());
//                map.put("start_time", modelList.get(position).getStart_time());
//                map.put("end_date", modelList.get(position).getEnd_date());
//                map.put("end_time", modelList.get(position).getEnd_time());
//                map.put("price", modelList.get(position).getPrice());
//                map.put("product_image", modelList.get(position).getProduct_image());
//                map.put("status", modelList.get(position).getStatus());
//                map.put("in_stock", modelList.get(position).getIn_stock());
//                map.put("unit_value", modelList.get(position).getUnit_value());
//                map.put("unit", modelList.get(position).getUnit());
//                map.put("increament", modelList.get(position).getIncreament());
//                map.put("rewards",modelList.get(position).getRewards());
//                map.put("stock", modelList.get(position).getStock());
//                map.put("title", modelList.get(position).getTitle());
//
//
//                if (!tv_contetiy.getText().toString().equalsIgnoreCase("0")) {
//
//                    if (dbcart.isInCart(map.get("product_id"))) {
//                        dbcart.setCart(map, Float.valueOf(tv_contetiy.getText().toString()));
//                        tv_add.setText(context.getResources().getString(R.string.tv_pro_update));
//                    } else {
//                        dbcart.setCart(map, Float.valueOf(tv_contetiy.getText().toString()));
//                        tv_add.setText(context.getResources().getString(R.string.tv_pro_update));
//                    }
//                } else {
//                    dbcart.removeItemFromCart(map.get("product_id"));
//                    tv_add.setText(context.getResources().getString(R.string.tv_pro_add));
//                }
//
//                Double items = Double.parseDouble(dbcart.getInCartItemQty(map.get("product_id")));
//                Double price = Double.parseDouble(map.get("price"));
//                Double reward = Double.parseDouble(map.get("rewards"));
//                tv_reward.setText("" + reward * items);
//
//                tv_total.setText("" + price * items);
//                ((MainActivity) context).setCartCounter("" + dbcart.getCartCount());
//
//            }

            if (id == R.id.rel_search) {

                Intent intent = new Intent( context, ActivityProductDetails.class );

                HashMap<String ,String> args = new HashMap<>(  );

                args.put("cat_id", modelList.get(position).getCategory_id());
                args.put("product_id",modelList.get(position).getProduct_id());
                args.put("product_image",modelList.get(position).getProduct_image());
                args.put("product_name",modelList.get(position).getProduct_name());
                args.put("product_description",modelList.get(position).getProduct_description());
                args.put("stock",modelList.get(position).getIn_stock());

                args.put("price",modelList.get(position).getPrice());
                args.put("mrp",modelList.get(position).getMrp());
                args.put("unit_value",modelList.get(position).getUnit_value());
                args.put("unit",modelList.get(position).getUnit());
                args.put("product_attribute",modelList.get(position).getProduct_attribute());
                args.put("rewards",modelList.get(position).getRewards());
                args.put("increment",modelList.get(position).getIncreament());
                args.put("title",modelList.get(position).getTitle());
                // Toast.makeText(getActivity(),""+getid,Toast.LENGTH_LONG).show();

                intent.putExtra( "details",args );
                context.startActivity( intent );


            }

        }
    }

    public Search_adapter(List<Product_model> modelList, Context context) {
        this.modelList = modelList;
        this.mFilteredList = modelList;

        dbcart = new DatabaseCartHandler(context);
    }

    @Override
    public Search_adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_search_rv, parent, false);

        context = parent.getContext();

        return new Search_adapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(Search_adapter.MyViewHolder holder, int position) {
        Product_model mList = modelList.get(position);


        String img_array= mList.getProduct_image();
        String img_name = null;
        try {
            JSONArray array=new JSONArray(img_array);
            img_name=array.get(0).toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Glide.with(context)
                .load( Url.IMG_PRODUCT_URL +img_name)
                .placeholder(R.drawable.logo)
              //  .centerCrop()
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
                .into(holder.iv_logo);

        holder.tv_title.setText(mList.getProduct_name());
        holder.tv_reward.setText(mList.getRewards());
        holder.tv_price.setText(context.getResources().getString(R.string.currency)+ mList.getPrice()+" / "+ mList.getUnit_value()+" "+mList.getUnit());

//        if (dbcart.isInCart(mList.getProduct_id())) {
//            holder.tv_add.setText(context.getResources().getString(R.string.tv_pro_update));
//            holder.tv_contetiy.setText(dbcart.getCartItemQty(mList.getProduct_id()));
//        } else {
//            holder.tv_add.setText(context.getResources().getString(R.string.tv_pro_add));
//        }

//        Double items = Double.parseDouble(dbcart.getInCartItemQty(mList.getProduct_id()));
//        Double price = Double.parseDouble(mList.getPrice());
//        Double reward = Double.parseDouble(mList.getRewards());
//        holder.tv_reward.setText("" + reward * items);
//
//
//        holder.tv_total.setText("" + price * items);

    }

    @Override
    public int getItemCount() {
        return mFilteredList.size();
    }

    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String charString = charSequence.toString();

                if (charString.isEmpty()) {

                    mFilteredList = modelList;
                } else {

                    ArrayList<Product_model> filteredList = new ArrayList<>();

                    for (Product_model androidVersion : modelList) {

                        if (androidVersion.getProduct_name().toLowerCase().contains(charString)) {

                            filteredList.add(androidVersion);
                        }
                    }
                    mFilteredList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mFilteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mFilteredList = (ArrayList<Product_model>) filterResults.values;
                notifyDataSetChanged();

            }
        };
    }



}