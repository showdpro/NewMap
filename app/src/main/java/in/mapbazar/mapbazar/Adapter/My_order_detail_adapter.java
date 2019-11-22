package in.mapbazar.mapbazar.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import in.mapbazar.mapbazar.Model.My_order_detail_model;
import in.mapbazar.mapbazar.R;
import in.mapbazar.mapbazar.Utili.Url;
import in.mapbazar.mapbazar.View.CustomTextView;


/**
 * Created by Rajesh Dabhi on 30/6/2017.
 */

public class My_order_detail_adapter extends RecyclerView.Adapter<My_order_detail_adapter.MyViewHolder> {

    private List<My_order_detail_model> modelList;
    private Context context;
    ArrayList<String> image_list;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public CustomTextView tv_title, tv_price, tv_qty;
        public ImageView iv_img;

        public MyViewHolder(View view) {
            super(view);
            tv_title = (CustomTextView) view.findViewById(R.id.tv_order_Detail_title);
            tv_price = (CustomTextView) view.findViewById(R.id.tv_order_Detail_price);
            tv_qty = (CustomTextView) view.findViewById(R.id.tv_order_Detail_qty);
            iv_img = (ImageView) view.findViewById(R.id.iv_order_detail_img);
            image_list=new ArrayList<>();

        }
    }

    public My_order_detail_adapter(List<My_order_detail_model> modelList) {
        this.modelList = modelList;
    }

    @Override
    public My_order_detail_adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_my_order_detail_rv, parent, false);

        context = parent.getContext();

        return new My_order_detail_adapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(My_order_detail_adapter.MyViewHolder holder, int position) {
        My_order_detail_model mList = modelList.get(position);


        try {
            image_list.clear();
            JSONArray array = new JSONArray(mList.getProduct_image());
            //Toast.makeText(this,""+product_images,Toast.LENGTH_LONG).show();
            if (mList.getProduct_image().equals(null)) {
                Glide.with(context)
                        .load(Url.IMG_PRODUCT_URL + mList.getProduct_image())
                        .centerCrop()
                        .placeholder(R.drawable.logo)
                        .crossFade()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .dontAnimate()
                        .into(holder.iv_img);
            } else {
                for (int i = 0; i <= array.length() - 1; i++) {
                    image_list.add(array.get(i).toString());

                }
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }


                Glide.with(context)
                .load(Url.IMG_PRODUCT_URL + image_list.get(0))
                .centerCrop()
                .placeholder(R.drawable.logo)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
                .into(holder.iv_img);

        holder.tv_title.setText(mList.getProduct_name());
        holder.tv_price.setText(context.getResources().getString(R.string.currency)+mList.getUnit_value()+"/"+mList.getUnit());
        holder.tv_qty.setText(mList.getQty());

    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

}