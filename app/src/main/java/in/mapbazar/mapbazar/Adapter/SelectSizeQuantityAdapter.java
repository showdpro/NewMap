package in.mapbazar.mapbazar.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import in.mapbazar.mapbazar.Model.HomeData.NewProductAttributeItem;
import in.mapbazar.mapbazar.Model.MatchWithProduct.MatchProductAttributeItem;
import in.mapbazar.mapbazar.Model.ProductListModel.ProductAttributeItem;
import in.mapbazar.mapbazar.Model.RecentlyProduct.RevcentlyProductAttributeItem;
import in.mapbazar.mapbazar.R;
import in.mapbazar.mapbazar.View.CustomTextView;
import in.mapbazar.mapbazar.dialog.SelectSizeDialogFragment;

import java.util.List;

/**
 * Created by kanani kalpesh on 31/05/18.
 */

public class SelectSizeQuantityAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private SharedPreferences userPref;
    private List<ProductAttributeItem> listItems;
    private List<RevcentlyProductAttributeItem> recentlyProductSizeItems;
    private List<NewProductAttributeItem> newProductSizeItems;
    private List<MatchProductAttributeItem> matchProductSizeItems;
    private Context mContext;
    SelectSizeDialogFragment.OnItemClickListener onItemClickListener;

    public SelectSizeQuantityAdapter(Context context, List<ProductAttributeItem> listItems, SelectSizeDialogFragment.OnItemClickListener onItemClickListener, int i) {
        userPref = PreferenceManager.getDefaultSharedPreferences(context);
        this.onItemClickListener = onItemClickListener;
        this.listItems = listItems;
        this.mContext = context;


    }

    public SelectSizeQuantityAdapter(Context context, List<RevcentlyProductAttributeItem> listItems, SelectSizeDialogFragment.OnItemClickListener onItemClickListener, boolean i) {
        userPref = PreferenceManager.getDefaultSharedPreferences(context);
        this.onItemClickListener = onItemClickListener;
        this.recentlyProductSizeItems = listItems;
        this.mContext = context;


    }

    public SelectSizeQuantityAdapter(Context context, List<NewProductAttributeItem> listItems, SelectSizeDialogFragment.OnItemClickListener onItemClickListener) {
        userPref = PreferenceManager.getDefaultSharedPreferences(context);
        this.onItemClickListener = onItemClickListener;
        this.newProductSizeItems = listItems;
        this.mContext = context;


    }
    public SelectSizeQuantityAdapter(Context context, List<MatchProductAttributeItem> listItems, SelectSizeDialogFragment.OnItemClickListener onItemClickListener, String abc) {
        userPref = PreferenceManager.getDefaultSharedPreferences(context);
        this.onItemClickListener = onItemClickListener;
        this.matchProductSizeItems = listItems;
        this.mContext = context;


    }

    // Create new views
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_select_size, null);

        // create ViewHolder

        ViewHolder viewHolder = new ViewHolder(itemLayoutView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        final int pos = position;


        final ViewHolder customViewHolder = (ViewHolder) holder;

        if (listItems != null)
            customViewHolder.tv_pack_detail.setText(listItems.get(position).getAttributeName() + " / " + listItems.get(position).getAttributeValue());
        else if (recentlyProductSizeItems != null)
            customViewHolder.tv_pack_detail.setText(recentlyProductSizeItems.get(position).getAttributeName() + " / " + recentlyProductSizeItems.get(position).getAttributeValue());
        else if (matchProductSizeItems != null)
            customViewHolder.tv_pack_detail.setText(matchProductSizeItems.get(position).getAttributeName() + " / " + matchProductSizeItems.get(position).getAttributeValue());
        else
            customViewHolder.tv_pack_detail.setText(newProductSizeItems.get(position).getAttributeName() + " / " + newProductSizeItems.get(position).getAttributeValue());

        customViewHolder.tv_pack_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(null, position);
            }
        });
        // Toast.makeText(v.getContext(), "Clicked on Checkbox: " + cb.getText() + " is " + cb.isChecked(), Toast.LENGTH_LONG).show();


    }

    // Return the size arraylist
    @Override
    public int getItemCount() {
        if (listItems != null)
            return listItems.size();
        else if (recentlyProductSizeItems != null)
            return recentlyProductSizeItems.size();
        else if (matchProductSizeItems != null)
            return matchProductSizeItems.size();


        else return newProductSizeItems.size();

    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        // protected TextView txt_location_name;
        CustomTextView tv_pack_detail;

        public ViewHolder(View view) {
            super(view);
            // this.txt_location_name = (TextView) view.findViewById(R.id.txt_location_name);

            this.tv_pack_detail = view.findViewById(R.id.txt_colorname);


        }

    }

}
