package in.mapbazar.mapbazar.Adapter;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import in.mapbazar.mapbazar.ActivityCategoryProduct;
import in.mapbazar.mapbazar.Fragment.HomeFragment;
import in.mapbazar.mapbazar.Utili.Common;
import in.mapbazar.mapbazar.Utili.Utils;
import in.mapbazar.mapbazar.View.CustomTextView;
import in.mapbazar.mapbazar.View.DialogUtils;
import in.mapbazar.mapbazar.callback.CallbackMessage;
import in.mapbazar.mapbazar.connection.API;
import in.mapbazar.mapbazar.connection.RestAdapter;
import in.mapbazar.mapbazar.customanimation.SimpleArcLoader;
import in.mapbazar.mapbazar.Model.ProductListModel.ProductItem;
import in.mapbazar.mapbazar.R;
import in.mapbazar.mapbazar.Utili.Url;
import in.mapbazar.mapbazar.dialog.SelectSizeDialogFragment;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryProductItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    int quatity;
    List<ProductItem> listUpcoming;
    private AppCompatActivity activity;
    private boolean loading;
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;
    private boolean isScrollEnabled = true;
    private OnItemClickListener onItemClickListener;
    private OnLoadMoreListener onLoadMoreListener;
    private ShoppingCartAdapter.OnItemMinsClick onItemMinsClick;
    private ShoppingCartAdapter.OnItemPuchClick onItemPuchClick;
    private int height, width;
    Dialog ProgressDialog;
    HomeFragment homeFragment;

    private SharedPreferences sPref;

    public CategoryProductItemAdapter(AppCompatActivity activity, RecyclerView view, List<ProductItem> items, int width, int height,HomeFragment homeFragment) {
        this.listUpcoming = items;
        this.activity = activity;
        this.width = width;
        this.height = height;
        lastItemViewDetector(view);
       this.homeFragment=homeFragment;
        sPref = PreferenceManager.getDefaultSharedPreferences(activity);
        ProgressDialog = new Dialog(activity, android.R.style.Theme_Translucent_NoTitleBar);
        ProgressDialog.setContentView(R.layout.progressbar);
        ProgressDialog.setCancelable(false);
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
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_category_productitem, parent, false);
            vh = new OriginalViewHolder(v);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false);
            vh = new ProgressViewHolder(v);
        }
        return vh;

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        if (holder instanceof OriginalViewHolder) {


            final OriginalViewHolder vItem = (OriginalViewHolder) holder;
            final ProductItem c = listUpcoming.get(position);

            vItem.product_name.setText(c.getProductName()+"\n "+c.getProduct_name_hindi());
            if (c.getSelectedProductItem() == -1) {
                vItem.product_price.setText( sPref.getString("CUR", "") +c.getProductAttribute().get(0).getAttributeValue());
            } else {
                vItem.product_price.setText( sPref.getString("CUR", "") +c.getProductAttribute().get(c.getSelectedProductItem()).getAttributeValue());

            }

            String temp = Url.product_url + c.getProductPrimaryImage();

            boolean isWhitespace = Utils.containsWhitespace(temp);

            if (isWhitespace)
                temp = temp.replaceAll(" ", "%20");

            Picasso.with(activity).load(temp).into(vItem.img_product);
            if (c.getQuantity() == null) {
                c.setQuantity("0");
            }
            if(c.getQuantity().equalsIgnoreCase( "0")){
                vItem.btn_add.setVisibility(View.VISIBLE);
                vItem.ll_plus_minu.setVisibility(View.GONE);
            }else {
                vItem.ll_plus_minu.setVisibility(View.VISIBLE);
                vItem.btn_add.setVisibility(View.GONE);
            }
            vItem.btn_quty.setText("" + c.getQuantity());

            vItem.layoutClick.setTag(position);
            vItem.layoutClick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(v, c, Integer.parseInt(v.getTag().toString()));
                    }

                }
            });
            vItem.btn_plus.setTag(position);
            vItem.btn_plus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int qty = 0;
                    if (!c.getQuantity().equals(""))
                        qty = Integer.parseInt(c.getQuantity());

                    qty = qty + 1;
                    c.setQuantity("" + qty);
                    notifyDataSetChanged();
                    if (c.getSelectedProductItem() == -1) {
                        requestCheck_stock_for_add_to_cart(c, c.getProductAttribute().get(0).getAttributeId(), "","plus");
                    }else {
                        requestCheck_stock_for_add_to_cart(c, c.getProductAttribute().get(c.getSelectedProductItem()).getAttributeId(), "","plus");

                    }
//                    if (onItemPuchClick != null) {
//                        onItemPuchClick.onItemClick(v, c, Integer.parseInt(v.getTag().toString()));
//                    }
                }
            });
            vItem.btn_add.setTag(position);
//            vItem.btn_add.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//
//                    if (onItemPuchClick != null) {
//                        onItemPuchClick.onItemClick(v, c, Integer.parseInt(v.getTag().toString()));
//                    }
//                }
//            });
            vItem.btn_min.setTag(position);
            vItem.btn_min.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int qty = 0;
                    if (!c.getQuantity().equals(""))
                        qty = Integer.parseInt(c.getQuantity());

                    if (qty != 1) {
                        qty = qty - 1;
                        c.setQuantity("" + qty);
                        notifyDataSetChanged();
                        if (c.getSelectedProductItem() == -1) {
                            requestCheck_stock_for_add_to_cart(c, c.getProductAttribute().get(0).getAttributeId(), "", "mins");
                        } else {
                            requestCheck_stock_for_add_to_cart(c, c.getProductAttribute().get(c.getSelectedProductItem()).getAttributeId(), "", "mins");

                        }

//                    if (onItemMinsClick != null) {
//                        onItemMinsClick.onItemClick(v, c, Integer.parseInt(v.getTag().toString()));
//                    }
                    }
                }
            });

            vItem.btn_add.setTag(position);
            vItem.btn_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    ProductItem obj=listUpcoming.get(position);

//                    int qty = 0;
//                    if (!obj.getQuantity().equals(""))
//                        qty = Integer.parseInt(obj.getQuantity());
//
//                    quatity = qty + 1;
//                    obj.setQuantity("" + quatity);
//
//
//
//
////                    mAdapter.notifyDataSetChanged();
//                    if (obj.getSelectedProductItem() == -1) {
//                        requestCheck_stock_for_add_to_cart(obj, obj.getProductAttribute().get(0).getAttributeId(), "");
//                    } else {
//                        requestCheck_stock_for_add_to_cart(obj, obj.getProductAttribute().get(obj.getSelectedProductItem()).getAttributeId(), "");
//
//                    }


                    int qty = 0;
                    if (!obj.getQuantity().equals(""))
                        qty = Integer.parseInt(obj.getQuantity());

                    qty = qty + 1;
                    obj.setQuantity("" + qty);
                    notifyDataSetChanged();
                    if (obj.getSelectedProductItem() == -1) {
                        requestCheck_stock_for_add_to_cart(obj, obj.getProductAttribute().get(0).getAttributeId(), "","plus");
                    }else {
                        requestCheck_stock_for_add_to_cart(obj, obj.getProductAttribute().get(obj.getSelectedProductItem()).getAttributeId(), "","plus");

                    }
                }
            });
            if (c.getProductAttribute() != null) {
                if (c.getSelectedProductItem() == -1) {
                    vItem.tv_weight_quantity.setText(c.getProductAttribute().get(0).getAttributeName() + " / " + c.getProductAttribute().get(0).getAttributeValue());
                } else {
                    vItem.tv_weight_quantity.setText(c.getProductAttribute().get(c.getSelectedProductItem()).getAttributeName() + " / " + c.getProductAttribute().get(c.getSelectedProductItem()).getAttributeValue());


                }
                vItem.ly_select_weight.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SelectSizeDialogFragment selectSizeDialogFragment = new SelectSizeDialogFragment();
                        selectSizeDialogFragment.setProductItem(c);
                        selectSizeDialogFragment.setOnDismissListener(new OnDismissListener() {
                            @Override
                            public void onDismiss() {
                                notifyDataSetChanged();
                            }
                        });
                        selectSizeDialogFragment.show(activity.getSupportFragmentManager(), "");

                    }
                });
            } else {
                vItem.tv_weight_quantity.setText(c.getProductAttribute().get(0).getAttributeName() + " / " + c.getProductAttribute().get(0).getAttributeValue());
            }


        } else ((ProgressViewHolder) holder).arc_loader.setVisibility(View.GONE);
        //else ((ProgressViewHolder) holder).arc_loader.start();
    }

    @Override
    public int getItemCount() {
        return listUpcoming.size();
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {

        public RelativeLayout layoutClick;

        LinearLayout ly_ctg_product, ly_select_weight,ll_plus_minu;
        ImageView img_product;
        CustomTextView product_name, product_price, tv_weight_quantity;
        Button btn_min, btn_quty, btn_plus, btn_add;


        public OriginalViewHolder(View v) {
            super(v);

            layoutClick = (RelativeLayout) v.findViewById(R.id.layoutClick);

            ly_ctg_product = (LinearLayout) v.findViewById(R.id.ly_ctg_product);
            ll_plus_minu = (LinearLayout) v.findViewById(R.id.ll_plus_minu);
            product_name = (CustomTextView) v.findViewById(R.id.product_name);
            product_price = (CustomTextView) v.findViewById(R.id.product_price);
            img_product = (ImageView) v.findViewById(R.id.img_product);
            btn_min = (Button) itemView.findViewById(R.id.btn_min);
            btn_quty = (Button) itemView.findViewById(R.id.btn_quty);
            btn_plus = (Button) itemView.findViewById(R.id.btn_plus);
            ly_select_weight = itemView.findViewById(R.id.ll_select_size);
            tv_weight_quantity = itemView.findViewById(R.id.tet_size);
            btn_add = itemView.findViewById(R.id.btn_add);

        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, ProductItem obj, int position);
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

    public void setOnItemMinsClick(ShoppingCartAdapter.OnItemMinsClick onItemClickListener) {
        this.onItemMinsClick = onItemClickListener;
    }

    public interface OnItemMinsClick {
        void onItemClick(View view, ProductItem obj, int position);
    }

    public void setOnItemPlusClick(ShoppingCartAdapter.OnItemPuchClick onItemClickListener) {
        this.onItemPuchClick = onItemClickListener;
    }

    public interface OnItemPuchClick {
        void onItemClick(View view, ProductItem obj, int position);
    }

    public interface OnDismissListener {
        void onDismiss();
    }
//    private void requestCheck_stock_for_add_to_cart(final ProductItem productItem, String str_sizeid, String str_colorcode) {
//
//        ProgressDialog.show();
//
//        final API api = RestAdapter.createAPI();
//        Call<JsonObject> callback_login = api.cart_stock_check(sPref.getString("uid", ""), productItem.getProductId(), str_sizeid, str_colorcode);
//        callback_login.enqueue(new Callback<JsonObject>() {
//            @Override
//            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
//                Log.e("Login onResponse", "=>" + response.body());
//
//                if (response.isSuccessful()) {
//                    JsonObject jsonObject = response.body();
//
//
//                    if (jsonObject != null && jsonObject.get("status").getAsString().equals("success")) {
//
//                       /* {"status":"success","stock_flag":1,"Isactive":"1"}*/
//
//                        if (jsonObject.has("stock_flag") && !jsonObject.get("stock_flag").isJsonNull()) {
//                            int stock_flag = jsonObject.get("stock_flag").getAsInt();
//
//                            if (stock_flag == 1) {
//                                GetJsonObject(productItem);
//                            }
//                        }
//
//
//                    } else {
//
//                        if (ProgressDialog != null && ProgressDialog.isShowing())
//                            ProgressDialog.dismiss();
//                      /*  {"status":"false","stock_flag":0,"Isactive":"1"}*/
//
//                        if (jsonObject.has("stock_flag") && !jsonObject.get("stock_flag").isJsonNull()) {
//                            int stock_flag = jsonObject.get("stock_flag").getAsInt();
//
//                            if (stock_flag == 0) {
//                                ErrorMessage(activity.getResources().getString(R.string.outofstock));
//                            }
//                        }
//
//                        int Isactive = 0;
//                        if (jsonObject.has("Isactive") && !jsonObject.get("Isactive").isJsonNull()) {
//                            Isactive = jsonObject.get("Isactive").getAsInt();
//                        }
//
//                        if (Isactive == 0) {
//                            Common.AccountLock(activity);
//                        } else if (jsonObject.has("message") && !jsonObject.get("message").isJsonNull()) {
//                            Common.Errordialog(activity, jsonObject.get("message").toString());
//                        }
//                        String quatity = Integer.parseInt(productItem.getQuantity())-1+"";
//                        productItem.setQuantity("" + quatity);
//                        notifyDataSetChanged();
//
//                    }
//
//                } else {
//
//                    if (ProgressDialog != null && ProgressDialog.isShowing())
//                        ProgressDialog.dismiss();
//
//                    ErrorMessage(activity.getString(R.string.error));
//                    String quatity = "0";
//                    productItem.setQuantity("" + quatity);
//                    notifyDataSetChanged();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<JsonObject> call, Throwable t) {
//
//                if (ProgressDialog != null && ProgressDialog.isShowing())
//                    ProgressDialog.dismiss();
//
//                String message = "";
//                if (t instanceof SocketTimeoutException) {
//                    message = "Socket Time out. Please try again.";
//                    Common.ShowHttpErrorMessage(activity, message);
//                }
//            }
//        });
//    }
//    private void ErrorMessage(String Message) {
//        // Common.showInternetInfo(ActivitySignIn.this, "Network is not available");
//        Dialog dialog = new DialogUtils(activity).buildDialogMessage(new CallbackMessage() {
//            @Override
//            public void onSuccess(Dialog dialog) {
//                dialog.dismiss();
//            }
//
//            @Override
//            public void onCancel(Dialog dialog) {
//                dialog.dismiss();
//            }
//        }, Message);
//        dialog.show();
//    }
//    private void GetJsonObject(ProductItem item) {
//        JsonObject finalobject = new JsonObject();
//
//        JsonArray jsonArray = new JsonArray();
//
//        JsonObject product = new JsonObject();
//
//        product.addProperty("Font_type_1", "");
//        product.addProperty("Font_type_2", "");
//        product.addProperty("Font_type_3", "");
//        product.addProperty("Font_type_4", "");
//        product.addProperty("Font_type_5", "");
//        product.addProperty("Font_type_6", "");
//        product.addProperty("Font_type_7", "");
//        product.addProperty("Font_type_8", "");
//        product.addProperty("Text_1", "");
//        product.addProperty("Text_2", "");
//        product.addProperty("Text_3", "");
//        product.addProperty("Text_4", "");
//        product.addProperty("Text_5", "");
//        product.addProperty("Text_6", "");
//        product.addProperty("Text_7", "");
//        product.addProperty("Text_8", "");
//        product.addProperty("Text_comment", "");
//        product.addProperty("Text_line_instruction", "");
//        product.addProperty("Text_quantity", "");
//        product.addProperty("address1", "");
//        product.addProperty("address2", "");
//        product.addProperty("back_Font_type_1", "");
//        product.addProperty("back_Font_type_2", "");
//        product.addProperty("back_Font_type_3", "");
//        product.addProperty("back_Font_type_4", "");
//        product.addProperty("back_Font_type_5", "");
//        product.addProperty("back_Font_type_6", "");
//        product.addProperty("back_Font_type_7", "");
//        product.addProperty("back_Font_type_8", "");
//        product.addProperty("back_Text_1", "");
//        product.addProperty("back_Text_2", "");
//        product.addProperty("back_Text_3", "");
//        product.addProperty("back_Text_4", "");
//        product.addProperty("back_Text_5", "");
//        product.addProperty("back_Text_6", "");
//        product.addProperty("back_Text_7", "");
//        product.addProperty("back_Text_8", "");
//        product.addProperty("back_Text_comment", "");
//        product.addProperty("back_Text_line_instruction", "");
//        product.addProperty("back_Text_quantity", "");
//        product.addProperty("city", "");
//        product.addProperty("comment", "");
//        product.addProperty("image_2_image_path", "");
//        product.addProperty("image_path", "");
//        product.addProperty("payment_type", "");
//        product.addProperty("pincode", "");
//        product.addProperty("mobile", "");
//        product.addProperty("state", "");
//        product.addProperty("transaction_id", "");
//
//
//        // product.addProperty("color", item.str_colorcode);
//        product.addProperty("isFormSelected", item.getIsFormSelected());
//
//        product.addProperty("color", "6");
//        product.addProperty("size_id", item.getProductId());
//
//        if (item.getSelectedProductItem() == -1) {
//            product.addProperty("price", item.getProductAttribute().get(0).getAttributeValue());
//         //   product.addProperty("size_id", item.getProductAttribute().get(0).getAttributeId());
//        } else {
//            product.addProperty("price", item.getProductAttribute().get(item.getSelectedProductItem()).getAttributeValue());
//           // product.addProperty("size_id", item.getProductAttribute().get(item.getSelectedProductItem()).getAttributeId());
//
//
//        }
//        product.addProperty("price", item.getProductPrice());
//        product.addProperty("product_id", item.getProductId());
//        product.addProperty("quantity", 1);
//
//        product.addProperty("user_id", sPref.getString("uid", ""));
//        product.addProperty("user_name", sPref.getString("name", ""));
//
//        jsonArray.add(product);
//
//        finalobject.add("product_order_detail", jsonArray);
//        finalobject.addProperty("user_id", "" + sPref.getString("uid", ""));
//
//        Log.e("Product Object", "" + finalobject.toString());
//
//        requestsubmitProductOrder(finalobject);
//    }
//    private void requestsubmitProductOrder(JsonObject finalobject) {
//
//        //ProgressDialog.show();
//
//        final API api = RestAdapter.createAPI();
//        Call<JsonObject> callback_login = api.submitProductOrder(finalobject);
//        callback_login.enqueue(new Callback<JsonObject>() {
//            @Override
//            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
//                Log.e("Submit Order", "=>" + response.body());
//
//                if (response.isSuccessful()) {
//                    JsonObject jsonObject = response.body();
//
//                    if (jsonObject != null && jsonObject.get("status").getAsString().equals("success")) {
//
//                        /*{"total_order":"11","status":"success","Isactive":"1"}*/
//
//                        if (ProgressDialog != null && ProgressDialog.isShowing())
//                            ProgressDialog.dismiss();
//
//                        if (jsonObject.has("total_order") && !jsonObject.get("total_order").isJsonNull()) {
//                            int total_order = jsonObject.get("total_order").getAsInt();
//
//                            SharedPreferences.Editor sh = sPref.edit();
//                            sh.putInt("Cart", total_order);
//                            sh.commit();
//
//                            Intent iReceiver = new Intent("com.megastore.shopingcart");
//                            activity.sendBroadcast(iReceiver);
//                        } else {
//                            int count = sPref.getInt("Cart", 0);
//
//                            SharedPreferences.Editor sh = sPref.edit();
//                            sh.putInt("Cart", (count + 1));
//                            sh.commit();
//
//                            Intent iReceiver = new Intent("com.megastore.shopingcart");
//                            activity.sendBroadcast(iReceiver);
//                        }
//
//
//                        ErrorMessage(activity.getString(R.string.order_successfully));
//
//                    } else {
//
//                        if (ProgressDialog != null && ProgressDialog.isShowing())
//                            ProgressDialog.dismiss();
//
//                        int Isactive = 0;
//                        if (jsonObject.has("Isactive") && !jsonObject.get("Isactive").isJsonNull()) {
//                            Isactive = jsonObject.get("Isactive").getAsInt();
//                        }
//
//                        if (Isactive == 0) {
//                            Common.AccountLock(activity);
//                        } else if (jsonObject.has("message") && !jsonObject.get("message").isJsonNull()) {
//                            Common.Errordialog(activity, jsonObject.get("message").toString());
//                        }
//                    }
//
//                } else {
//
//                    if (ProgressDialog != null && ProgressDialog.isShowing())
//                        ProgressDialog.dismiss();
//
//                    ErrorMessage(activity.getString(R.string.error));
//                }
//            }
//
//            @Override
//            public void onFailure(Call<JsonObject> call, Throwable t) {
//
//                if (ProgressDialog != null && ProgressDialog.isShowing())
//                    ProgressDialog.dismiss();
//
//                String message = "";
//                if (t instanceof SocketTimeoutException) {
//                    message = "Socket Time out. Please try again.";
//                    Common.ShowHttpErrorMessage(activity, message);
//                }
//            }
//        });
//
//    }

    private void requestCheck_stock_for_add_to_cart(final ProductItem productItem, String str_sizeid, String str_colorcode,final String quantity) {

        ProgressDialog.show();

        final API api = RestAdapter.createAPI();
        Call<JsonObject> callBooking = api.stock_check(sPref.getString("uid", ""), productItem.getProductId(), null, quantity, str_sizeid, str_colorcode);
        callBooking.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.e("Login onResponse", "=>" + response.body());

                if (response.isSuccessful()) {
                    JsonObject jsonObject = response.body();


                    if (jsonObject != null && jsonObject.get("status").getAsString().equals("success")) {

                        /* {"status":"success","stock_flag":1,"Isactive":"1"}*/

//                        if (jsonObject.has("stock_flag") && !jsonObject.get("stock_flag").isJsonNull()) {
//                            int stock_flag = jsonObject.get("stock_flag").getAsInt();
//
//                            if (stock_flag == 1) {
                        GetJsonObject(productItem,quantity);
//                            }
//                        }


                    } else {

                        if (ProgressDialog != null && ProgressDialog.isShowing())
                            ProgressDialog.dismiss();
                        /*  {"status":"false","stock_flag":0,"Isactive":"1"}*/

                        if (jsonObject.has("stock_flag") && !jsonObject.get("stock_flag").isJsonNull()) {
                            int stock_flag = jsonObject.get("stock_flag").getAsInt();

                            if (stock_flag == 0) {
                                ErrorMessage(activity.getResources().getString(R.string.outofstock));
                            }
                        }

                        int Isactive = 0;
                        if (jsonObject.has("Isactive") && !jsonObject.get("Isactive").isJsonNull()) {
                            Isactive = jsonObject.get("Isactive").getAsInt();
                        }

                        if (Isactive == 0) {
                            Common.AccountLock(activity);
                        } else if (jsonObject.has("message") && !jsonObject.get("message").isJsonNull()) {
                            Common.Errordialog(activity, jsonObject.get("message").toString());
                        }
                        String quatity = Integer.parseInt(productItem.getQuantity())-1+"";
                        productItem.setQuantity("" + quatity);
                        notifyDataSetChanged();

                    }

                } else {

                    if (ProgressDialog != null && ProgressDialog.isShowing())
                        ProgressDialog.dismiss();

                    ErrorMessage(activity.getString(R.string.error));
                    String quatity = "0";
                    productItem.setQuantity("" + quatity);
                    notifyDataSetChanged();
                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

                if (ProgressDialog != null && ProgressDialog.isShowing())
                    ProgressDialog.dismiss();

                String message = "";
                if (t instanceof SocketTimeoutException) {
                    message = "Socket Time out. Please try again.";
                    Common.ShowHttpErrorMessage(activity, message);
                }
            }
        });
    }
    private void ErrorMessage(String Message) {
        // Common.showInternetInfo(ActivitySignIn.this, "Network is not available");
        Dialog dialog = new DialogUtils(activity).buildDialogMessage(new CallbackMessage() {
            @Override
            public void onSuccess(Dialog dialog) {
                dialog.dismiss();
            }

            @Override
            public void onCancel(Dialog dialog) {
                dialog.dismiss();
            }
        }, Message);
        dialog.show();
    }
    private void GetJsonObject(ProductItem item,String quantity) {
        JsonObject finalobject = new JsonObject();

        JsonArray jsonArray = new JsonArray();

        JsonObject product = new JsonObject();

        product.addProperty("Font_type_1", "");
        product.addProperty("Font_type_2", "");
        product.addProperty("Font_type_3", "");
        product.addProperty("Font_type_4", "");
        product.addProperty("Font_type_5", "");
        product.addProperty("Font_type_6", "");
        product.addProperty("Font_type_7", "");
        product.addProperty("Font_type_8", "");
        product.addProperty("Text_1", "");
        product.addProperty("Text_2", "");
        product.addProperty("Text_3", "");
        product.addProperty("Text_4", "");
        product.addProperty("Text_5", "");
        product.addProperty("Text_6", "");
        product.addProperty("Text_7", "");
        product.addProperty("Text_8", "");
        product.addProperty("Text_comment", "");
        product.addProperty("Text_line_instruction", "");
        product.addProperty("Text_quantity", "");
        product.addProperty("address1", "");
        product.addProperty("address2", "");
        product.addProperty("back_Font_type_1", "");
        product.addProperty("back_Font_type_2", "");
        product.addProperty("back_Font_type_3", "");
        product.addProperty("back_Font_type_4", "");
        product.addProperty("back_Font_type_5", "");
        product.addProperty("back_Font_type_6", "");
        product.addProperty("back_Font_type_7", "");
        product.addProperty("back_Font_type_8", "");
        product.addProperty("back_Text_1", "");
        product.addProperty("back_Text_2", "");
        product.addProperty("back_Text_3", "");
        product.addProperty("back_Text_4", "");
        product.addProperty("back_Text_5", "");
        product.addProperty("back_Text_6", "");
        product.addProperty("back_Text_7", "");
        product.addProperty("back_Text_8", "");
        product.addProperty("back_Text_comment", "");
        product.addProperty("back_Text_line_instruction", "");
        product.addProperty("back_Text_quantity", "");
        product.addProperty("city", "");
        product.addProperty("comment", "");
        product.addProperty("image_2_image_path", "");
        product.addProperty("image_path", "");
        product.addProperty("payment_type", "");
        product.addProperty("pincode", "");
        product.addProperty("mobile", "");
        product.addProperty("state", "");
        product.addProperty("transaction_id", "");

        if(quantity.equalsIgnoreCase("mins")){
            product.addProperty("qmethod","mins");
        }
        // product.addProperty("color", item.str_colorcode);
        //   product.addProperty("isFormSelected", item.getIsFormSelected());

        product.addProperty("color", "6");
   //     product.addProperty("size_id", item.getProductId());
        if (item.getSelectedProductItem() == -1) {
            product.addProperty("price", item.getProductAttribute().get(0).getAttributeValue());
              product.addProperty("size_id", item.getProductAttribute().get(0).getAttributeId());
        } else {
            product.addProperty("price", item.getProductAttribute().get(item.getSelectedProductItem()).getAttributeValue());
             product.addProperty("size_id", item.getProductAttribute().get(item.getSelectedProductItem()).getAttributeId());


        }
      //  product.addProperty("price", item.getProductPrice());
        product.addProperty("product_id", item.getProductId());
        product.addProperty("quantity", 1);
        // product.addProperty("size_id", str_sizeid);
        product.addProperty("user_id", sPref.getString("uid", ""));
        product.addProperty("user_name", sPref.getString("name", ""));

        jsonArray.add(product);

        finalobject.add("product_order_detail", jsonArray);
        finalobject.addProperty("user_id", "" + sPref.getString("uid", ""));

        Log.e("Product Object", "" + finalobject.toString());

        requestsubmitProductOrder(finalobject);
    }
    private void requestsubmitProductOrder(JsonObject finalobject) {

        //ProgressDialog.show();

        final API api = RestAdapter.createAPI();
        Call<JsonObject> callback_login = api.submitProductOrder(finalobject);
        callback_login.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.e("Submit Order", "=>" + response.body());

                if (response.isSuccessful()) {
                    JsonObject jsonObject = response.body();

                    if (jsonObject != null && jsonObject.get("status").getAsString().equals("success")) {

                        /*{"total_order":"11","status":"success","Isactive":"1"}*/

                        if (ProgressDialog != null && ProgressDialog.isShowing())
                            ProgressDialog.dismiss();

                        if (jsonObject.has("total_order") && !jsonObject.get("total_order").isJsonNull()) {
                            int total_order = jsonObject.get("total_order").getAsInt();

                            SharedPreferences.Editor sh = sPref.edit();
                            sh.putInt("Cart", total_order);
                            sh.commit();

                            Intent iReceiver = new Intent("com.megastore.shopingcart");
                            activity.sendBroadcast(iReceiver);
                        } else {
                            int count = sPref.getInt("Cart", 0);

                            SharedPreferences.Editor sh = sPref.edit();
                            sh.putInt("Cart", (count + 1));
                            sh.commit();

                            Intent iReceiver = new Intent("com.megastore.shopingcart");
                            activity.sendBroadcast(iReceiver);
                        }



                        if(homeFragment != null)
                         //   homeFragment.setUpdateCart();
                      //  updateCart();

                        ErrorMessage(activity.getString(R.string.order_successfully));

                    } else {

                        if (ProgressDialog != null && ProgressDialog.isShowing())
                            ProgressDialog.dismiss();

                        int Isactive = 0;
                        if (jsonObject.has("Isactive") && !jsonObject.get("Isactive").isJsonNull()) {
                            Isactive = jsonObject.get("Isactive").getAsInt();
                        }

                        if (Isactive == 0) {
                            Common.AccountLock(activity);
                        } else if (jsonObject.has("message") && !jsonObject.get("message").isJsonNull()) {
                            Common.Errordialog(activity, jsonObject.get("message").toString());
                        }
                    }

                } else {

                    if (ProgressDialog != null && ProgressDialog.isShowing())
                        ProgressDialog.dismiss();

                    ErrorMessage(activity.getString(R.string.error));
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

                if (ProgressDialog != null && ProgressDialog.isShowing())
                    ProgressDialog.dismiss();

                String message = "";
                if (t instanceof SocketTimeoutException) {
                    message = "Socket Time out. Please try again.";
                    Common.ShowHttpErrorMessage(activity, message);
                }
            }
        });

    }



//    public  void updateCart(){
//        if (sPref.getInt("Cart", 0) == 0) {
//            ActivityCategoryProduct.txt_cart_cout.setText("");
//            ActivityCategoryProduct.layout_cart_cout.setVisibility(View.GONE);
//        } else {
//            ActivityCategoryProduct.txt_cart_cout.setText("" + sPref.getInt("Cart", 0));
//            ActivityCategoryProduct.layout_cart_cout.setVisibility(View.VISIBLE);
//        }
//    }

}

