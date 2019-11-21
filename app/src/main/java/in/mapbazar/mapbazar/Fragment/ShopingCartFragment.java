package in.mapbazar.mapbazar.Fragment;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.loopeer.itemtouchhelperextension.ItemTouchHelperExtension;

import in.mapbazar.mapbazar.RegisterActivity;
import in.mapbazar.mapbazar.ActivityLoginRegister;
import in.mapbazar.mapbazar.Adapter.ShoppingCartAdapter;
import in.mapbazar.mapbazar.Model.ProductListModel.ColorItem;
import in.mapbazar.mapbazar.Model.ProductListModel.ProductAttributeItem;
import in.mapbazar.mapbazar.Model.ProductListModel.ProductImageItem;
import in.mapbazar.mapbazar.Model.ProductListModel.ProductItem;
import in.mapbazar.mapbazar.Model.ProductListModel.ProductSizeItem;
import in.mapbazar.mapbazar.R;
import in.mapbazar.mapbazar.SwipeRow.DividerItemDecoration;
import in.mapbazar.mapbazar.SwipeRow.ItemTouchHelperCallback;
import in.mapbazar.mapbazar.Utili.Common;
import in.mapbazar.mapbazar.View.CustomTextView;
import in.mapbazar.mapbazar.View.DialogUtils;
import in.mapbazar.mapbazar.callback.CallbackInternate;
import in.mapbazar.mapbazar.callback.CallbackMessage;
import in.mapbazar.mapbazar.connection.API;
import in.mapbazar.mapbazar.connection.RestAdapter;
import in.mapbazar.mapbazar.Fragment.*;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ShopingCartFragment extends Fragment {

    //Shared Preferences
    private SharedPreferences sPref;

    Dialog ProgressDialog;

    @BindView(R.id.ly_shopnow)
    RelativeLayout ly_shopnow;

    @BindView(R.id.txt_shopnow)
    CustomTextView txt_shopnow;

    @BindView(R.id.layout_main)
    LinearLayout layout_main;

    @BindView(R.id.layout_productview)
    LinearLayout layout_productview;

    @BindView(R.id.product_recycler)
    RecyclerView product_recycler;

    @BindView(R.id.ly_wishlist)
    LinearLayout ly_wishlist;

    @BindView(R.id.txt_subtotal)
    CustomTextView txt_subtotal;

    @BindView(R.id.txt_tax)
    CustomTextView txt_tax;

    @BindView(R.id.txt_taxwithtotal)
    CustomTextView txt_taxwithtotal;

    @BindView(R.id.txt_total)
    CustomTextView txt_total;

    @BindView(R.id.txt_proceedtocheckout)
    CustomTextView txt_proceedtocheckout;


    private ShoppingCartAdapter mAdapter;
    public ItemTouchHelperExtension mItemTouchHelper;
    public ItemTouchHelperExtension.Callback mCallback;

    String tax;
   public static float tax_per = 0, subtotal = 0, tax_value = 0, total_amount = 0;
    int quatity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.shopingcart_fragment, container, false);
        ButterKnife.bind(this, view);

        sPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        //tax = sPref.getString("TAX", "0");
        tax = "0";
        tax_per = Float.parseFloat(tax);

        initComponent();

        if (Common.isNetworkAvailable(getActivity())) {
            requestShopingCart();
        } else {
            RetriveCall();
        }

        return view;
    }

    private void RetriveCall() {
        // Common.showInternetInfo(ActivitySignIn.this, "Network is not available");
        Dialog dialog = new DialogUtils(getActivity()).buildDialogInternate(new CallbackInternate() {
            @Override
            public void onSuccess(Dialog dialog) {
                dialog.dismiss();

                if (Common.isNetworkAvailable(getActivity())) {
                    requestShopingCart();
                } else {
                    RetriveCall();
                }
            }
        });
        dialog.show();
    }

    private void initComponent() {

        ProgressDialog = new Dialog(getActivity(), android.R.style.Theme_Translucent_NoTitleBar);
        ProgressDialog.setContentView(R.layout.progressbar);
        ProgressDialog.setCancelable(false);


        txt_shopnow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                HomeFragment fragment = new HomeFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.layout_item, fragment)
                        .commit();
            }
        });

        ly_wishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Wishlist wishListFragment = new Wishlist();
                FragmentManager wishListfragmentManager = getActivity().getSupportFragmentManager();
                wishListfragmentManager.beginTransaction()
                        .replace(R.id.layout_item, wishListFragment)
                        .commit();
            }
        });

        txt_proceedtocheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!sPref.getString("guest", "").equalsIgnoreCase("guest")) {

                    Intent checkout = new Intent(getActivity(), RegisterActivity.class);
                    SharedPreferences.Editor sh = sPref.edit();
                    sh.putBoolean("Add", false);
                    sh.putString("tax_value", "" + tax_value);
                    sh.putString("quantity", String.valueOf(quatity));
                    sh.commit();
                    startActivity(checkout);
                } else {
                    Intent login = new Intent(getActivity(), ActivityLoginRegister.class);
                    startActivity(login);
                }
            }
        });
    }

    private void requestShopingCart() {

        ProgressDialog.show();

        API api = RestAdapter.createAPI();

        Call<JsonObject> callBooking = api.order_history(sPref.getString("uid", ""));

        callBooking.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                JsonObject resp = response.body();
                Log.e(Common.TAG, "onResponse-subcategory" + resp);
                if (resp != null) {
                    if (resp.get("status").getAsString().equals("success")) {

                        JsonArray order_history;

                        if (resp.has("order_history") && resp.get("order_history").isJsonArray())

                            order_history = resp.get("order_history").getAsJsonArray();
                        else
                            order_history = null;

                        if (order_history != null && order_history.size() > 0) {

                            Common.list_ProductItem = new ArrayList<>();

                            for (int i = 0; i < order_history.size(); i++) {

                                JsonObject jsonObject_new_product = order_history.get(i).getAsJsonObject();

                                ProductItem newProductItem = new ProductItem();

                                if (jsonObject_new_product.has("order_id") && !jsonObject_new_product.get("order_id").isJsonNull())
                                    newProductItem.setOrder_id(jsonObject_new_product.get("order_id").getAsString());
                                else
                                    newProductItem.setOrder_id("");

                                if (jsonObject_new_product.has("size_id") && !jsonObject_new_product.get("size_id").isJsonNull())
                                    newProductItem.setSizeId(jsonObject_new_product.get("size_id").getAsString());
                                else
                                    newProductItem.setSizeId("");

                                if (jsonObject_new_product.has("size_name") && !jsonObject_new_product.get("size_name").isJsonNull())
                                    newProductItem.setSizeName(jsonObject_new_product.get("size_name").getAsString());
                                else
                                    newProductItem.setSizeName("");

                                if (jsonObject_new_product.has("current_stock") && !jsonObject_new_product.get("current_stock").isJsonNull())
                                    newProductItem.setCurrent_stock(jsonObject_new_product.get("current_stock").getAsString());
                                else
                                    newProductItem.setCurrent_stock("");

                                if (jsonObject_new_product.has("color_code") && !jsonObject_new_product.get("color_code").isJsonNull())
                                    newProductItem.setColorCode(jsonObject_new_product.get("color_code").getAsString());
                                else
                                    newProductItem.setColorCode("");

                                if (jsonObject_new_product.has("color_name") && !jsonObject_new_product.get("color_name").isJsonNull())
                                    newProductItem.setColorName(jsonObject_new_product.get("color_name").getAsString());
                                else
                                    newProductItem.setColorName("");

                                if (jsonObject_new_product.has("price") && !jsonObject_new_product.get("price").isJsonNull())
                                    newProductItem.setPrice(jsonObject_new_product.get("price").getAsString());
                                else
                                    newProductItem.setPrice("");

                                if (jsonObject_new_product.has("user_id") && !jsonObject_new_product.get("user_id").isJsonNull())
                                    newProductItem.setUser_id(jsonObject_new_product.get("user_id").getAsString());
                                else
                                    newProductItem.setUser_id("");

                                if (jsonObject_new_product.has("user_name") && !jsonObject_new_product.get("user_name").isJsonNull())
                                    newProductItem.setUser_name(jsonObject_new_product.get("user_name").getAsString());
                                else
                                    newProductItem.setUser_name("");

                                if (jsonObject_new_product.has("address1") && !jsonObject_new_product.get("address1").isJsonNull())
                                    newProductItem.setAddress1(jsonObject_new_product.get("address1").getAsString());
                                else
                                    newProductItem.setAddress1("");

                                if (jsonObject_new_product.has("address2") && !jsonObject_new_product.get("address2").isJsonNull())
                                    newProductItem.setAddress2(jsonObject_new_product.get("address2").getAsString());
                                else
                                    newProductItem.setAddress2("");

                                if (jsonObject_new_product.has("pincode") && !jsonObject_new_product.get("pincode").isJsonNull())
                                    newProductItem.setPincode(jsonObject_new_product.get("pincode").getAsString());
                                else
                                    newProductItem.setPincode("");

                                if (jsonObject_new_product.has("city") && !jsonObject_new_product.get("city").isJsonNull())
                                    newProductItem.setCity(jsonObject_new_product.get("city").getAsString());
                                else
                                    newProductItem.setCity("");

                                if (jsonObject_new_product.has("state") && !jsonObject_new_product.get("state").isJsonNull())
                                    newProductItem.setState(jsonObject_new_product.get("state").getAsString());
                                else
                                    newProductItem.setState("");

                                if (jsonObject_new_product.has("mobile") && !jsonObject_new_product.get("mobile").isJsonNull())
                                    newProductItem.setMobile(jsonObject_new_product.get("mobile").getAsString());
                                else
                                    newProductItem.setMobile("");

                                if (jsonObject_new_product.has("isFormSelected") && !jsonObject_new_product.get("isFormSelected").isJsonNull())
                                    newProductItem.setIsFormSelected(jsonObject_new_product.get("isFormSelected").getAsString());
                                else
                                    newProductItem.setIsFormSelected("");

                                if (jsonObject_new_product.has("comment") && !jsonObject_new_product.get("comment").isJsonNull())
                                    newProductItem.setComment(jsonObject_new_product.get("comment").getAsString());
                                else
                                    newProductItem.setComment("");

                                if (jsonObject_new_product.has("quantity") && !jsonObject_new_product.get("quantity").isJsonNull())
                                    newProductItem.setQuantity(jsonObject_new_product.get("quantity").getAsString());
                                else
                                    newProductItem.setQuantity("");

                                if (jsonObject_new_product.has("stock_flag") && !jsonObject_new_product.get("stock_flag").isJsonNull())
                                    newProductItem.setStock_flag(jsonObject_new_product.get("stock_flag").getAsString());
                                else
                                    newProductItem.setStock_flag("");

                                if (jsonObject_new_product.has("product_id") && !jsonObject_new_product.get("product_id").isJsonNull())
                                    newProductItem.setProductId(jsonObject_new_product.get("product_id").getAsString());
                                else
                                    newProductItem.setProductId("");

                                if (jsonObject_new_product.has("product_name") && !jsonObject_new_product.get("product_name").isJsonNull())
                                    newProductItem.setProductName(jsonObject_new_product.get("product_name").getAsString());
                                else
                                    newProductItem.setProductName("");

                                if (jsonObject_new_product.has("product_status") && !jsonObject_new_product.get("product_status").isJsonNull())
                                    newProductItem.setProductStatus(jsonObject_new_product.get("product_status").getAsString());
                                else
                                    newProductItem.setProductStatus("");

                                if (jsonObject_new_product.has("product_primary_image") && !jsonObject_new_product.get("product_primary_image").isJsonNull())
                                    newProductItem.setProductPrimaryImage(jsonObject_new_product.get("product_primary_image").getAsString());
                                else
                                    newProductItem.setProductPrimaryImage("");

                                if (jsonObject_new_product.has("product_price") && !jsonObject_new_product.get("product_price").isJsonNull())
                                    newProductItem.setProductPrice(jsonObject_new_product.get("product_price").getAsString());
                                else
                                    newProductItem.setProductPrice("");

                                if (jsonObject_new_product.has("product_old_price") && !jsonObject_new_product.get("product_old_price").isJsonNull())
                                    newProductItem.setProductOldPrice(jsonObject_new_product.get("product_old_price").getAsString());
                                else
                                    newProductItem.setProductOldPrice("");

                                if (jsonObject_new_product.has("product_stock") && !jsonObject_new_product.get("product_stock").isJsonNull())
                                    newProductItem.setProductStock(jsonObject_new_product.get("product_stock").getAsString());
                                else
                                    newProductItem.setProductStock("");

                                if (jsonObject_new_product.has("product_description") && !jsonObject_new_product.get("product_description").isJsonNull())
                                    newProductItem.setProductDescription(jsonObject_new_product.get("product_description").getAsString());
                                else
                                    newProductItem.setProductDescription("");

                                if (jsonObject_new_product.has("isProductCustomizable") && !jsonObject_new_product.get("isProductCustomizable").isJsonNull())
                                    newProductItem.setIsProductCustomizable(jsonObject_new_product.get("isProductCustomizable").getAsString());
                                else
                                    newProductItem.setIsProductCustomizable("");

                                if (jsonObject_new_product.has("product_brand") && !jsonObject_new_product.get("product_brand").isJsonNull())
                                    newProductItem.setProductBrand(jsonObject_new_product.get("product_brand").getAsString());
                                else
                                    newProductItem.setProductBrand("");

                                if (jsonObject_new_product.has("product_color") && !jsonObject_new_product.get("product_color").isJsonNull())
                                    newProductItem.setProductColor(jsonObject_new_product.get("product_color").getAsString());
                                else
                                    newProductItem.setProductColor("");

                                if (jsonObject_new_product.has("favorite_flag") && !jsonObject_new_product.get("favorite_flag").isJsonNull())
                                    newProductItem.setFavoriteFlag(jsonObject_new_product.get("favorite_flag").getAsInt());
                                else
                                    newProductItem.setFavoriteFlag(0);

                                JsonArray product_image;

                                if (jsonObject_new_product.has("product_image") && jsonObject_new_product.get("product_image").isJsonArray())

                                    product_image = jsonObject_new_product.get("product_image").getAsJsonArray();
                                else
                                    product_image = null;

                                if (product_image != null && product_image.size() > 0) {

                                    List<ProductImageItem> list_ProductImageItem = new ArrayList<>();

                                    for (int j = 0; j < product_image.size(); j++) {

                                        JsonObject jsonObject_product_image = product_image.get(j).getAsJsonObject();

                                        ProductImageItem productImageItem = new ProductImageItem();

                                        if (jsonObject_product_image.has("image") && !jsonObject_product_image.get("image").isJsonNull())
                                            productImageItem.setImage(jsonObject_product_image.get("image").getAsString());
                                        else
                                            productImageItem.setImage("");

                                        list_ProductImageItem.add(productImageItem);

                                    }

                                    newProductItem.setProductImage(list_ProductImageItem);
                                }

                                JsonArray product_size;

                                if (jsonObject_new_product.has("product_size") && jsonObject_new_product.get("product_size").isJsonArray())

                                    product_size = jsonObject_new_product.get("product_size").getAsJsonArray();
                                else
                                    product_size = null;

                                if (product_size != null && product_size.size() > 0) {

                                    List<ProductSizeItem> list_ProductSizeItem = new ArrayList<>();

                                    for (int j = 0; j < product_size.size(); j++) {

                                        JsonObject jsonObject_product_size = product_size.get(j).getAsJsonObject();

                                        ProductSizeItem productSizeItem = new ProductSizeItem();

                                        if (jsonObject_product_size.has("size_id") && !jsonObject_product_size.get("size_id").isJsonNull())
                                            productSizeItem.setSizeId(jsonObject_product_size.get("size_id").getAsString());
                                        else
                                            productSizeItem.setSizeId("");

                                        if (jsonObject_product_size.has("size_name") && !jsonObject_product_size.get("size_name").isJsonNull())
                                            productSizeItem.setSizeName(jsonObject_product_size.get("size_name").getAsString());
                                        else
                                            productSizeItem.setSizeName("");

                                        if (jsonObject_product_size.has("size_image") && !jsonObject_product_size.get("size_image").isJsonNull())
                                            productSizeItem.setSizeImage(jsonObject_product_size.get("size_image").getAsString());
                                        else
                                            productSizeItem.setSizeImage("");

                                        if (jsonObject_product_size.has("size_stock") && !jsonObject_product_size.get("size_stock").isJsonNull())
                                            productSizeItem.setSizeStock(jsonObject_product_size.get("size_stock").getAsString());
                                        else
                                            productSizeItem.setSizeStock("");

                                        if (jsonObject_product_size.has("size_color") && !jsonObject_product_size.get("size_color").isJsonNull())
                                            productSizeItem.setSizeColor(jsonObject_product_size.get("size_color").getAsString());
                                        else
                                            productSizeItem.setSizeColor("");

                                        if (jsonObject_product_size.has("size_price") && !jsonObject_product_size.get("size_price").isJsonNull())
                                            productSizeItem.setSizePrice(jsonObject_product_size.get("size_price").getAsString());
                                        else
                                            productSizeItem.setSizePrice("");


                                        JsonArray color;

                                        if (jsonObject_product_size.has("color") && jsonObject_product_size.get("color").isJsonArray())

                                            color = jsonObject_product_size.get("color").getAsJsonArray();
                                        else
                                            color = null;

                                        if (color != null && color.size() > 0) {

                                            List<ColorItem> list_ColorItem = new ArrayList<>();

                                            for (int k = 0; k < color.size(); k++) {

                                                JsonObject jsonObject_ColorItem = color.get(k).getAsJsonObject();

                                                ColorItem colorItem = new ColorItem();

                                                if (jsonObject_ColorItem.has("color_id") && !jsonObject_ColorItem.get("color_id").isJsonNull())
                                                    colorItem.setColorId(jsonObject_ColorItem.get("color_id").getAsString());
                                                else
                                                    colorItem.setColorId("");

                                                if (jsonObject_ColorItem.has("color_code") && !jsonObject_ColorItem.get("color_code").isJsonNull())
                                                    colorItem.setColorCode(jsonObject_ColorItem.get("color_code").getAsString());
                                                else
                                                    colorItem.setColorCode("");

                                                if (jsonObject_ColorItem.has("color_image") && !jsonObject_ColorItem.get("color_image").isJsonNull())
                                                    colorItem.setColorImage(jsonObject_ColorItem.get("color_image").getAsString());
                                                else
                                                    colorItem.setColorImage("");

                                                if (jsonObject_ColorItem.has("color_stock") && !jsonObject_ColorItem.get("color_stock").isJsonNull())
                                                    colorItem.setColorStock(jsonObject_ColorItem.get("color_stock").getAsString());
                                                else
                                                    colorItem.setColorStock("");

                                                if (jsonObject_ColorItem.has("color_price") && !jsonObject_ColorItem.get("color_price").isJsonNull())
                                                    colorItem.setColorPrice(jsonObject_ColorItem.get("color_price").getAsString());
                                                else
                                                    colorItem.setColorPrice("");

                                                list_ColorItem.add(colorItem);

                                            }

                                            productSizeItem.setColor(list_ColorItem);
                                        }

                                        list_ProductSizeItem.add(productSizeItem);

                                    }

                                    newProductItem.setProductSize(list_ProductSizeItem);
                                }

                                JsonArray product_attribute;

                                if (jsonObject_new_product.has("product_attribute") && jsonObject_new_product.get("product_attribute").isJsonArray())

                                    product_attribute = jsonObject_new_product.get("product_attribute").getAsJsonArray();
                                else
                                    product_attribute = null;

                                if (product_attribute != null && product_attribute.size() > 0) {

                                    List<ProductAttributeItem> list_ProductAttributeItem = new ArrayList<>();

                                    for (int j = 0; j < product_attribute.size(); j++) {

                                        JsonObject jsonObject_product_attribute = product_attribute.get(j).getAsJsonObject();

                                        ProductAttributeItem productAttributeItem = new ProductAttributeItem();

                                        if (jsonObject_product_attribute.has("attribute_id") && !jsonObject_product_attribute.get("attribute_id").isJsonNull())
                                            productAttributeItem.setAttributeId(jsonObject_product_attribute.get("attribute_id").getAsString());
                                        else
                                            productAttributeItem.setAttributeId("");

                                        if (jsonObject_product_attribute.has("attribute_name") && !jsonObject_product_attribute.get("attribute_name").isJsonNull())
                                            productAttributeItem.setAttributeName(jsonObject_product_attribute.get("attribute_name").getAsString());
                                        else
                                            productAttributeItem.setAttributeName("");

                                        if (jsonObject_product_attribute.has("attribute_value") && !jsonObject_product_attribute.get("attribute_value").isJsonNull())
                                            productAttributeItem.setAttributeValue(jsonObject_product_attribute.get("attribute_value").getAsString());
                                        else
                                            productAttributeItem.setAttributeValue("");

                                        list_ProductAttributeItem.add(productAttributeItem);

                                    }

                                    newProductItem.setProductAttribute(list_ProductAttributeItem);
                                }

                                Common.list_ProductItem.add(newProductItem);
                            }

                            if (Common.list_ProductItem.size() > 0) {
                                ly_shopnow.setVisibility(View.GONE);
                                displayApiResult(Common.list_ProductItem);

                            } else {

                                SharedPreferences.Editor sh = sPref.edit();
                                sh.putInt("Cart", 0);
                                sh.commit();

                                ly_shopnow.setVisibility(View.VISIBLE);
                            }

                        } else {

                            SharedPreferences.Editor sh = sPref.edit();
                            sh.putInt("Cart", 0);
                            sh.commit();

                            ly_shopnow.setVisibility(View.VISIBLE);
                        }

                        if (ProgressDialog != null && ProgressDialog.isShowing())
                            ProgressDialog.dismiss();


                    } else if (resp.get("status").getAsString().equals("false")) {

                        if (ProgressDialog != null && ProgressDialog.isShowing())
                            ProgressDialog.dismiss();

                        int Isactive = 0;
                        if (resp.has("Isactive") && !resp.get("Isactive").isJsonNull()) {
                            Isactive = resp.get("Isactive").getAsInt();
                        }

                        if (Isactive == 0) {
                            Common.AccountLock(getActivity());
                        } else if (resp.has("message") && !resp.get("message").isJsonNull()) {
                            Common.Errordialog(getActivity(), resp.get("message").toString());
                        }

                    } else if (resp.get("status").getAsString().equals("failed")) {

                        if (ProgressDialog != null && ProgressDialog.isShowing())
                            ProgressDialog.dismiss();


                        ErrorMessage(getString(R.string.error));

                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                if (ProgressDialog != null && ProgressDialog.isShowing())
                    ProgressDialog.dismiss();
                String message = "";
                if (t instanceof SocketTimeoutException) {
                    message = "Socket Time out. Please try again.";
                    Common.ShowHttpErrorMessage(getActivity(), message);
                }
            }
        });
    }

    private void displayApiResult(List<ProductItem> list_ProductItem) {

        SetPricedata(list_ProductItem);
        product_recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new ShoppingCartAdapter(getActivity(), list_ProductItem);
        product_recycler.setAdapter(mAdapter);
        product_recycler.addItemDecoration(new DividerItemDecoration(getActivity()));
        mCallback = new ItemTouchHelperCallback();
        mItemTouchHelper = new ItemTouchHelperExtension(mCallback);
        mItemTouchHelper.attachToRecyclerView(product_recycler);

        SharedPreferences.Editor sh = sPref.edit();
        sh.putInt("Cart", list_ProductItem.size());
        sh.commit();

        Intent iReceiver = new Intent("com.megastore.shopingcart");
        getActivity().sendBroadcast(iReceiver);

        mAdapter.setOnItemClickListener(new ShoppingCartAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, ProductItem obj, int position) {

              //  Toast.makeText(getActivity(),""+obj.getProductImage().get(0).getImage(),Toast.LENGTH_LONG).show();

//                Toast.makeText(getActivity(),"p_id "+obj.getProductId()+"\n customiza "+obj.getIsProductCustomizable()+"\nb "+obj.getProductBrand()+"\nc "+obj.getProductColor()+"\n "+obj.getProductDescription()+"\n "+obj.getProductName()
//                        +"\n "+obj.getProductOldPrice()+"\n "+obj.getProductPrice()+"\n "+obj.getProductPrimaryImage()+"\n "+obj.getProductStatus()+"\n "+obj.getProductStock()+"\n "+obj.getFavoriteFlag()+"\n "+obj.getQuantity()
//                        +"\n atr_id- "+obj.getProductAttribute().get(0).getAttributeId()+"\n atr_value- "+obj.getProductAttribute().get(0).getAttributeValue()+"\n atr_name- "+obj.getProductAttribute().get(0).getAttributeName()+"\n "+obj.getProductImage().get(0).getImage(),Toast.LENGTH_LONG).show();

//                Intent intent = new Intent(getActivity(), ActivityProductDetails.class);
//                intent.putExtra("ProductItem", obj);
//                startActivity(intent);
            }
        });


        mAdapter.setOnItemMinsClick(new ShoppingCartAdapter.OnItemMinsClick() {
            @Override
            public void onItemClick(View view, ProductItem obj, int position) {

                int qty = 0;
                if (!obj.getQuantity().equals(""))
                    qty = Integer.parseInt(obj.getQuantity());


                if (qty != 1) {
                    quatity = qty - 1;
                    obj.setQuantity("" + quatity);
                    mAdapter.notifyDataSetChanged();
                    if (obj.getSelectedProductItem() == -1) {
                        requestCheck_stock_for_add_to_cart(obj, obj.getProductAttribute().get(0).getAttributeId(), "", "mins");
                    } else {
                        requestCheck_stock_for_add_to_cart(obj, obj.getProductAttribute().get(obj.getSelectedProductItem()).getAttributeId(), "", "mins");

                    }
                }


            }
        });

        mAdapter.setOnItemPlusClick(new ShoppingCartAdapter.OnItemPuchClick() {
            @Override
            public void onItemClick(View view, ProductItem obj, int position) {


              //  Toast.makeText(getActivity(),"p_id "+obj.getProductAttribute().get(obj.getSelectedProductItem()).getAttributeId(),Toast.LENGTH_LONG).show();

                int qty = 0;
                if (!obj.getQuantity().equals(""))
                    qty = Integer.parseInt(obj.getQuantity());

                quatity = qty + 1;
                obj.setQuantity("" + quatity);




                mAdapter.notifyDataSetChanged();
                if (obj.getSelectedProductItem() == -1) {
                    requestCheck_stock_for_add_to_cart(obj, obj.getProductAttribute().get(0).getAttributeId(), "", "plus");
                } else {
                    requestCheck_stock_for_add_to_cart(obj, obj.getProductAttribute().get(obj.getSelectedProductItem()).getAttributeId(), "", "plus");

                }
            }
        });

        mAdapter.setOnItemDelete(new ShoppingCartAdapter.OnItemDelete() {
            @Override
            public void onDelete(List<ProductItem> mDatas) {
                SetPricedata(mDatas);

                if (mDatas.size() < 0) {
                    ly_shopnow.setVisibility(View.VISIBLE);

                    SharedPreferences.Editor sh = sPref.edit();
                    sh.putInt("Cart", 0);
                    sh.commit();

                } else {
                    int size = mDatas.size();

                    SharedPreferences.Editor sh = sPref.edit();
                    sh.putInt("Cart", size);
                    sh.commit();
                }


                Intent iReceiver = new Intent("com.megastore.shopingcart");
                getActivity().sendBroadcast(iReceiver);
            }

        });

    }

    private void setSelectedPosition(ProductItem productItem) {
        if (productItem.getProductAttribute() != null && productItem.getProductAttribute().size() != 0) {
            for (int i = 0; i < productItem.getProductAttribute().size(); i++) {
                if (productItem.getProductAttribute().get(i).getAttributeId().equalsIgnoreCase(productItem.getSizeId())) {
                    productItem.setSelectedProductItem(i);
                    break;
                }
            }
        }
    }

    private void SetPricedata(List<ProductItem> list_ProductItem) {

        subtotal = 0;
        tax_value = 0;
        total_amount = 0;

        for (int i = 0; i < list_ProductItem.size(); i++) {
            ProductItem productItem = list_ProductItem.get(i);
            setSelectedPosition(productItem);

            float price = 0;
            if (productItem.getSelectedProductItem() == -1) {
                if (!productItem.getProductAttribute().get(0).getAttributeValue().equals(""))
                    price = Float.parseFloat(productItem.getProductAttribute().get(0).getAttributeValue());
            } else {
                if (!productItem.getProductAttribute().get(productItem.getSelectedProductItem()).getAttributeValue().equals(""))
                    price = Float.parseFloat(productItem.getProductAttribute().get(productItem.getSelectedProductItem()).getAttributeValue());
            }

            float quatity = 0;
            if (!productItem.getQuantity().equals(""))
                quatity = Float.parseFloat(productItem.getQuantity());

            float total_price = price * quatity;

            subtotal = subtotal + total_price;
        }

        tax_value = (subtotal * tax_per) / 100;
        //total_amount = tax_value + subtotal;
        total_amount =  subtotal;

        txt_subtotal.setText(sPref.getString("CUR", "") + " " + subtotal);
        txt_tax.setText(getString(R.string.tax) + " (" + tax + " %)");

        txt_taxwithtotal.setText(sPref.getString("CUR", "") + " " + tax_value);

        txt_total.setText(sPref.getString("CUR", "") + " " + total_amount);
    }


//    private void requestStockCheck(final ProductItem obj, String prod_id, String orderid, final String qty, String sizeid, String colorcode) {
//
//        ProgressDialog.show();
//
//        API api = RestAdapter.createAPI();
//
//        Call<JsonObject> callBooking = api.stock_check(sPref.getString("uid", ""), prod_id, orderid, qty, sizeid, colorcode);
//
//        callBooking.enqueue(new Callback<JsonObject>() {
//            @Override
//            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
//                JsonObject resp = response.body();
//                Log.e(Common.TAG, "onResponse-subcategory" + resp);
//                if (resp != null) {
//                    if (resp.get("status").getAsString().equals("success")) {
//
//                      /*  {"message":"available","quantity":"2","status":"success","Isactive":"1"}*/
//
//                        if (ProgressDialog != null && ProgressDialog.isShowing())
//                            ProgressDialog.dismiss();
//
//                        String quatity = "";
//
//                        if (resp.has("quantity") && !resp.get("quantity").isJsonNull())
//                            quatity = resp.get("quantity").getAsString();
//
//
////                        obj.setQuantity("" + quatity);
////                        mAdapter.notifyDataSetChanged();
//                        SetPricedata(Common.list_ProductItem);
//
//
//                    } else if (resp.get("status").getAsString().equals("false")) {
//
//                           /*   {"message":"not available","quantity":"3","status":"false","Isactive":"1"}*/
//
//                        if (ProgressDialog != null && ProgressDialog.isShowing())
//                            ProgressDialog.dismiss();
//
//                        String quatity = "";
//
//                        if (resp.has("quantity") && !resp.get("quantity").isJsonNull()) {
//                            quatity = resp.get("quantity").getAsString();
//
//                            obj.setQuantity("" + quatity);
//                            mAdapter.notifyDataSetChanged();
//                            SetPricedata(Common.list_ProductItem);
//                        }
//
//                        int Isactive=0;
//                        if (resp.has("Isactive") && !resp.get("Isactive").isJsonNull()) {
//                            Isactive = resp.get("Isactive").getAsInt();
//                        }
//
//                        if (Isactive == 0) {
//                            Common.AccountLock(getActivity());
//                        }
//                        else if (resp.has("message") && !resp.get("message").isJsonNull()) {
//                            Common.Errordialog(getActivity(),resp.get("message").toString());
//                        }
//
//                    } else if (resp.get("status").getAsString().equals("failed")) {
//
//                        if (ProgressDialog != null && ProgressDialog.isShowing())
//                            ProgressDialog.dismiss();
//
//
//                        ErrorMessage(getString(R.string.error));
//
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<JsonObject> call, Throwable t) {
//                if (ProgressDialog != null && ProgressDialog.isShowing())
//                    ProgressDialog.dismiss();
//                String message = "";
//                if (t instanceof SocketTimeoutException) {
//                    message = "Socket Time out. Please try again.";
//                    Common.ShowHttpErrorMessage(getActivity(), message);
//                }
//            }
//        });
//    }
//

    private void requestCheck_stock_for_add_to_cart(final ProductItem productItem, String str_sizeid, final String str_colorcode, final String quantity) {

        ProgressDialog.show();

        final API api = RestAdapter.createAPI();
        Call<JsonObject> callBooking = api.stock_check(sPref.getString("uid", ""), productItem.getProductId(), "", quantity, str_sizeid, str_colorcode);
        callBooking.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.e("Login onResponse", "=>" + response.body());

                if (response.isSuccessful()) {
                    JsonObject jsonObject = response.body();


                    if (jsonObject != null && jsonObject.get("status").getAsString().equals("success")) {
                        SetPricedata(Common.list_ProductItem);
                       /* {"status":"success","stock_flag":1,"Isactive":"1"}*/

//                        if (jsonObject.has("stock_flag") && !jsonObject.get("stock_flag").isJsonNull()) {
//                            int stock_flag = jsonObject.get("stock_flag").getAsInt();

                        //  if (stock_flag == 1) {
                        GetJsonObject(productItem, quantity);

//                            }
//                        }


                    } else {

                        if (ProgressDialog != null && ProgressDialog.isShowing())
                            ProgressDialog.dismiss();
                      /*  {"status":"false","stock_flag":0,"Isactive":"1"}*/

                        if (jsonObject.has("stock_flag") && !jsonObject.get("stock_flag").isJsonNull()) {
                            int stock_flag = jsonObject.get("stock_flag").getAsInt();

                            if (stock_flag == 0) {
                                ErrorMessage(getActivity().getResources().getString(R.string.outofstock));
                            }
                        }

                        int Isactive = 0;
                        if (jsonObject.has("Isactive") && !jsonObject.get("Isactive").isJsonNull()) {
                            Isactive = jsonObject.get("Isactive").getAsInt();
                        }

                        if (Isactive == 0) {
                            Common.AccountLock(getActivity());
                        } else if (jsonObject.has("message") && !jsonObject.get("message").isJsonNull()) {
                            Common.Errordialog(getActivity(), jsonObject.get("message").toString());
                        }
                        String quatity = Integer.parseInt(productItem.getQuantity()) - 1 + "";
                        productItem.setQuantity("" + quatity);
                        mAdapter.notifyDataSetChanged();

                    }

                } else {

                    if (ProgressDialog != null && ProgressDialog.isShowing())
                        ProgressDialog.dismiss();

                    ErrorMessage(getActivity().getString(R.string.error));
                    String quatity = "0";
                    productItem.setQuantity("" + quatity);
                    mAdapter.notifyDataSetChanged();
                }
                SetPricedata(Common.list_ProductItem);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

                if (ProgressDialog != null && ProgressDialog.isShowing())
                    ProgressDialog.dismiss();

                String message = "";
                if (t instanceof SocketTimeoutException) {
                    message = "Socket Time out. Please try again.";
                    Common.ShowHttpErrorMessage(getActivity(), message);
                }
            }
        });
    }

    private void ErrorMessage(String Message) {
        // Common.showInternetInfo(ActivitySignIn.this, "Network is not available");
        Dialog dialog = new DialogUtils(getActivity()).buildDialogMessage(new CallbackMessage() {
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

    private void GetJsonObject(ProductItem item, String quantity) {
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


        // product.addProperty("color", item.str_colorcode);
        //   product.addProperty("isFormSelected", item.getIsFormSelected());

      //  Toast.makeText(getActivity(),""+item.getProductId(),Toast.LENGTH_LONG).show();
        product.addProperty("color", "6");
       product.addProperty("size_id", item.getProductAttribute().get(item.getSelectedProductItem()).getAttributeId());
        if (item.getSelectedProductItem() == -1) {
            product.addProperty("price", item.getProductAttribute().get(0).getAttributeValue());

        } else {
            product.addProperty("price", item.getProductAttribute().get(item.getSelectedProductItem()).getAttributeValue());
//            product.addProperty("size_id", item.getProductId());


        }
      //  product.addProperty("price", item.getProductPrice());
        product.addProperty("product_id", item.getProductId());
        product.addProperty("quantity", 1);
        // product.addProperty("size_id", str_sizeid);
        //product.addProperty("size_id", str_sizeid);
        if (quantity.equalsIgnoreCase("mins")) {
            product.addProperty("qmethod", "mins");
        }

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
                            getActivity().sendBroadcast(iReceiver);
                        } else {
                            int count = sPref.getInt("Cart", 0);

                            SharedPreferences.Editor sh = sPref.edit();
                            sh.putInt("Cart", (count + 1));
                            sh.commit();

                            Intent iReceiver = new Intent("com.megastore.shopingcart");
                            getActivity().sendBroadcast(iReceiver);
                        }


                        ErrorMessage(getActivity().getString(R.string.order_updated));

                    } else {

                        if (ProgressDialog != null && ProgressDialog.isShowing())
                            ProgressDialog.dismiss();

                        int Isactive = 0;
                        if (jsonObject.has("Isactive") && !jsonObject.get("Isactive").isJsonNull()) {
                            Isactive = jsonObject.get("Isactive").getAsInt();
                        }

                        if (Isactive == 0) {
                            Common.AccountLock(getActivity());
                        } else if (jsonObject.has("message") && !jsonObject.get("message").isJsonNull()) {
                            Common.Errordialog(getActivity(), jsonObject.get("message").toString());
                        }
                    }

                } else {

                    if (ProgressDialog != null && ProgressDialog.isShowing())
                        ProgressDialog.dismiss();

                    ErrorMessage(getActivity().getString(R.string.error)+response);
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

                if (ProgressDialog != null && ProgressDialog.isShowing())
                    ProgressDialog.dismiss();

                String message = "";
                if (t instanceof SocketTimeoutException) {
                    message = "Socket Time out. Please try again.";
                    Common.ShowHttpErrorMessage(getActivity(), message);
                }
            }
        });

    }
}
