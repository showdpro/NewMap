package in.mapbazar.mapbazar.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import in.mapbazar.mapbazar.Adapter.CategoryProductItemAdapter;
import in.mapbazar.mapbazar.Adapter.SelectSizeQuantityAdapter;
import in.mapbazar.mapbazar.Model.HomeData.NewProductItem;
import in.mapbazar.mapbazar.Model.MatchWithProduct.MatchProductItem;
import in.mapbazar.mapbazar.Model.ProductListModel.ProductItem;
import in.mapbazar.mapbazar.Model.RecentlyProduct.RecentlyProductItem;
import in.mapbazar.mapbazar.R;


/**
 * Created by sonam_gupta on 15/06/19.
 */


public class SelectSizeDialogFragment extends DialogFragment {

    private OnItemClickListener onItemClickListener;

    public CategoryProductItemAdapter.OnDismissListener getOnDismissListener() {
        return onDismissListener;
    }

    public void setOnDismissListener(CategoryProductItemAdapter.OnDismissListener onDismissListener) {
        this.onDismissListener = onDismissListener;
    }

    private CategoryProductItemAdapter.OnDismissListener onDismissListener;

    public ProductItem getProductItem() {
        return productItem;
    }

    public void setProductItem(ProductItem productItem) {
        this.productItem = productItem;
    }

    private ProductItem productItem;

    public RecentlyProductItem getRecentlyProductItem() {
        return recentlyProductItem;
    }

    public void setRecentlyProductItem(RecentlyProductItem recentlyProductItem) {
        this.recentlyProductItem = recentlyProductItem;
    }

    private RecentlyProductItem recentlyProductItem;

    public MatchProductItem getMatchProductItem() {
        return matchProductItem;
    }

    public void setMatchProductItem(MatchProductItem matchProductItem) {
        this.matchProductItem = matchProductItem;
    }

    private MatchProductItem matchProductItem;

    public NewProductItem getNewProductItem() {
        return newProductItem;
    }

    public void setNewProductItem(NewProductItem newProductItem) {
        this.newProductItem = newProductItem;
    }

    private NewProductItem newProductItem;
    private RecyclerView recyclerView;
    private SelectSizeQuantityAdapter selectSizeQuantityAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().setCanceledOnTouchOutside(true);
        getDialog().getWindow().setGravity(Gravity.CENTER | Gravity.CENTER);
        getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        return inflater.inflate(R.layout.select_size, null);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.size_recycler);
        onItemClickListener = new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if(productItem != null)
                productItem.setSelectedProductItem(position);
                else if ((newProductItem!= null))
                    newProductItem.setSelectedProductItem(position);
                else if ((matchProductItem!= null))
                    matchProductItem.setSelectedProductItem(position);
                else
                    recentlyProductItem.setSelectedProductItem(position);

                onDismissListener.onDismiss();
                dismiss();
            }
        };
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        if(productItem != null)
        selectSizeQuantityAdapter = new SelectSizeQuantityAdapter(getContext(),productItem.getProductAttribute(),onItemClickListener,0);
        else if ((newProductItem!= null))
            selectSizeQuantityAdapter = new SelectSizeQuantityAdapter(getContext(),newProductItem.getProductAttribute(),onItemClickListener);
        else if ((matchProductItem!= null))
            selectSizeQuantityAdapter = new SelectSizeQuantityAdapter(getContext(),matchProductItem.getProductAttribute(),onItemClickListener,"");
        else
        selectSizeQuantityAdapter = new SelectSizeQuantityAdapter(getContext(),recentlyProductItem.getProductAttribute(),onItemClickListener,true);

        recyclerView.setAdapter(selectSizeQuantityAdapter );


    }

    @Override
        public void onResume()
        {
            super.onResume();
            Window window = getDialog().getWindow();
        //    window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

            getDialog().getWindow().setGravity(Gravity.CENTER | Gravity.CENTER);

            //TODO:
        }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

}
