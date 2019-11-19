package in.mapbazar.mapbazar.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import in.mapbazar.mapbazar.R;
import in.mapbazar.mapbazar.View.CustomTextView;
import in.mapbazar.mapbazar.dialog.SelectTimeDialogFragment;

import java.util.List;

/**
 * Created by kanani kalpesh on 31/05/18.
 */

public class SelectDeliveryTimeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private SharedPreferences userPref;
    private List<String> listItems;
    private Context mContext;
    SelectTimeDialogFragment.OnItemClickListener onItemClickListener;

    public SelectDeliveryTimeAdapter(Context context, List<String> listItems, SelectTimeDialogFragment.OnItemClickListener onItemClickListener) {
        userPref = PreferenceManager.getDefaultSharedPreferences(context);
        this.onItemClickListener = onItemClickListener;
        this.listItems = listItems;
        this.mContext = context;


    }
    public List<String> getSizelist() {
        return listItems;
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

            customViewHolder.tv_pack_detail.setText(listItems.get(position));
        customViewHolder.tv_pack_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(null, position);
            }
        });


    }

    // Return the size arraylist
    @Override
    public int getItemCount() {
            return listItems.size();


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
