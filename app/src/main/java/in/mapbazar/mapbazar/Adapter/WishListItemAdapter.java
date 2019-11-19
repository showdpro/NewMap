package in.mapbazar.mapbazar.Adapter;

import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.squareup.picasso.Picasso;
import in.mapbazar.mapbazar.Utili.Utils;
import in.mapbazar.mapbazar.View.CustomTextView;
import in.mapbazar.mapbazar.customanimation.SimpleArcLoader;
import in.mapbazar.mapbazar.Model.ProductListModel.ProductItem;
import in.mapbazar.mapbazar.R;
import in.mapbazar.mapbazar.Utili.Url;

import java.util.ArrayList;
import java.util.List;

public class WishListItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<ProductItem> listUpcoming;
    private Activity activity;
    private boolean loading;
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;
    private boolean isScrollEnabled = true;
    private OnItemClickListener onItemClickListener;
    private OnItemRemove onItemRemove;
    private OnLoadMoreListener onLoadMoreListener;
    private int height, width;

    public WishListItemAdapter(Activity activity, RecyclerView view, List<ProductItem> items, int width, int height) {
        this.listUpcoming = items;
        this.activity = activity;
        this.width = width;
        this.height = height;
        lastItemViewDetector(view);
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public SimpleArcLoader arc_loader;

        public ProgressViewHolder(View v) {
            super(v);
            arc_loader = (SimpleArcLoader) v.findViewById(R.id.arc_loader);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return this.listUpcoming.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }

    public void setLoading() {
        if (getItemCount() != 0) {
            this.listUpcoming.add(null);
            notifyItemInserted(getItemCount() - 1);
            loading = true;
        }
    }

    public void resetListData() {
        isScrollEnabled = true;
        this.listUpcoming = new ArrayList<>();
        notifyDataSetChanged();
    }

    public void setItems(List<ProductItem> items) {
        this.listUpcoming = items;
        notifyDataSetChanged();
    }

    public void insertData(List<ProductItem> items) {
        setLoaded();
        int positionStart = getItemCount();
        int itemCount = items.size();
        this.listUpcoming.addAll(items);
        notifyItemRangeInserted(positionStart, itemCount);
    }

    public void setLoaded() {
        loading = false;
        for (int i = 0; i < getItemCount(); i++) {
            if (listUpcoming.get(i) == null) {
                listUpcoming.remove(i);
                notifyItemRemoved(i);
            }
        }
    }

    public void setScroll(boolean scrollEnabled) {
        isScrollEnabled = scrollEnabled;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder vh;
        if (viewType == VIEW_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_wishlist, parent, false);
            vh = new OriginalViewHolder(v);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false);
            vh = new ProgressViewHolder(v);
        }
        return vh;

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof OriginalViewHolder) {

            OriginalViewHolder vItem = (OriginalViewHolder) holder;
            final ProductItem c = listUpcoming.get(position);

            vItem.product_name.setText(c.getProductName());

            String temp = Url.product_url + c.getProductPrimaryImage();

            boolean isWhitespace = Utils.containsWhitespace(temp);

            if (isWhitespace)
                temp = temp.replaceAll(" ", "%20");

            Picasso.with(activity).load(temp).into(vItem.img_product);

            vItem.image_cancle.setTag(position);
            vItem.image_cancle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (onItemRemove != null) {
                        onItemRemove.onRemove(v, c, Integer.parseInt(v.getTag().toString()),listUpcoming);
                    }

                }
            });

            vItem.layoutClick.setTag(position);
            vItem.layoutClick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(v, c, Integer.parseInt(v.getTag().toString()));
                    }

                }
            });

        } else ((ProgressViewHolder) holder).arc_loader.setVisibility(View.GONE);
        //else ((ProgressViewHolder) holder).arc_loader.start();
    }

    @Override
    public int getItemCount() {
        return listUpcoming.size();
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {

        public RelativeLayout layoutClick;

        LinearLayout ly_ctg_product;
        ImageView img_product,image_cancle;
        CustomTextView product_name;

        public OriginalViewHolder(View v) {
            super(v);

            layoutClick = (RelativeLayout) v.findViewById(R.id.layoutClick);

            ly_ctg_product = (LinearLayout) v.findViewById(R.id.ly_ctg_product);
            product_name = (CustomTextView) v.findViewById(R.id.product_name);
            img_product = (ImageView) v.findViewById(R.id.img_product);
            image_cancle = (ImageView) v.findViewById(R.id.image_cancle);

        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, ProductItem obj, int position);
    }

    public void setOnItemRemove(OnItemRemove onItemRemove) {
        this.onItemRemove = onItemRemove;
    }

    public interface OnItemRemove {
        void onRemove(View view, ProductItem obj, int position, List<ProductItem> listUpcoming);
    }


    private void lastItemViewDetector(RecyclerView recyclerView) {
        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
            final LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    int lastPos = layoutManager.findLastVisibleItemPosition();
                    if (!loading && lastPos == getItemCount() - 1 && onLoadMoreListener != null && isScrollEnabled) {
                        if (onLoadMoreListener != null) {
                            int current_page = getItemCount();
                            onLoadMoreListener.onLoadMore(current_page);
                        }
                        loading = true;
                    }
                }
            });
        }
    }

    public interface OnLoadMoreListener {
        void onLoadMore(int current_page);
    }

}
