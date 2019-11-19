package in.mapbazar.mapbazar.Adapter;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;
import in.mapbazar.mapbazar.ActivityNewProductDetails;
import in.mapbazar.mapbazar.Fragment.HomeFragment;
import in.mapbazar.mapbazar.Model.HomeData.NewProductItem;
import in.mapbazar.mapbazar.R;
import in.mapbazar.mapbazar.Utili.Common;
import in.mapbazar.mapbazar.Utili.Url;
import in.mapbazar.mapbazar.Utili.Utils;
import in.mapbazar.mapbazar.View.CustomTextView;
import in.mapbazar.mapbazar.View.DialogUtils;
import in.mapbazar.mapbazar.callback.CallbackMessage;
import in.mapbazar.mapbazar.connection.API;
import in.mapbazar.mapbazar.connection.RestAdapter;
import in.mapbazar.mapbazar.dialog.SelectSizeDialogFragment;

import java.net.SocketTimeoutException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Home_NewproductAdapter extends RecyclerView.Adapter<Home_NewproductAdapter.MyViewHolder> {
    private List<NewProductItem> newProductsList;
    int width, height;
    private AppCompatActivity activity;
    private HomeFragment homeFragment;

    private SharedPreferences sPref;
    Dialog ProgressDialog;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public CustomTextView product_name, price,tv_weight_quantity;
        public ImageView imageView;
        LinearLayout rl, ly_select_weight,ll_plus_minu;
        Button btn_min, btn_quty, btn_plus, btn_add;


        public MyViewHolder(View view) {
            super(view);
            product_name = (CustomTextView) view.findViewById(R.id.product_name);
            price = (CustomTextView) view.findViewById(R.id.price);
            imageView = (ImageView) view.findViewById(R.id.item_img);
            rl = (LinearLayout) view.findViewById(R.id.category_rl);
            btn_min = (Button) itemView.findViewById(R.id.btn_min);
            btn_quty = (Button) itemView.findViewById(R.id.btn_quty);
            btn_plus = (Button) itemView.findViewById(R.id.btn_plus);
            ly_select_weight = itemView.findViewById(R.id.ll_select_size);
            tv_weight_quantity = itemView.findViewById(R.id.tet_size);
            btn_add = itemView.findViewById(R.id.btn_add);
            ll_plus_minu = (LinearLayout) itemView.findViewById(R.id.ll_plus_minu);

        }
    }

    public Home_NewproductAdapter(AppCompatActivity activity, List<NewProductItem> newProductsList, int width, int height, HomeFragment homeFragment) {
        this.activity = activity;
        this.homeFragment = homeFragment;
        this.newProductsList = newProductsList;
        this.width = width;
        this.height = height;
        ProgressDialog = new Dialog(activity, android.R.style.Theme_Translucent_NoTitleBar);
        ProgressDialog.setContentView(R.layout.progressbar);
        ProgressDialog.setCancelable(false);

        sPref = PreferenceManager.getDefaultSharedPreferences(activity);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_newprod_signle_item, parent, false);
        return new MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final NewProductItem newProductItem = newProductsList.get(position);


        holder.product_name.setText(newProductItem.getProductName()+"\n "+newProductItem.getProduct_name_hindi());
        holder.price.setText(sPref.getString("CUR", "") + " " + newProductItem.getProductPrice());

        if (newProductItem.getSelectedProductItem() == -1) {
            holder.price.setText( sPref.getString("CUR", "") +newProductItem.getProductAttribute().get(0).getAttributeValue());
        } else {
            holder.price.setText( sPref.getString("CUR", "") +newProductItem.getProductAttribute().get(newProductItem.getSelectedProductItem()).getAttributeValue());

        }

        String temp = Url.product_url + newProductItem.getProductPrimaryImage();

        boolean isWhitespace = Utils.containsWhitespace(temp);

        if (isWhitespace)
            temp = temp.replaceAll(" ", "%20");


        Log.d("lemon", String.valueOf(temp));
        Picasso.with(holder.imageView.getContext()).load(temp).into(holder.imageView);

        holder.rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(activity, ActivityNewProductDetails.class);
                intent.putExtra("NewProductItem", newProductItem);
               // Toast.makeText(activity,""+newProductItem.getProductImage()+"\n "+newProductItem.getProductPrimaryImage(),Toast.LENGTH_LONG).show();
                activity.startActivity(intent);
            }
        });
        holder.btn_plus.setTag(position);
        holder.btn_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int qty = 0;
                if (!newProductItem.getQuantity().equals(""))
                    qty = Integer.parseInt(newProductItem.getQuantity());

                qty = qty + 1;
                newProductItem.setQuantity("" + qty);
                notifyDataSetChanged();
                if (newProductItem.getSelectedProductItem() == -1) {
                    requestCheck_stock_for_add_to_cart(newProductItem, newProductItem.getProductAttribute().get(0).getAttributeId(), "","plus");
                }else {
                    requestCheck_stock_for_add_to_cart(newProductItem, newProductItem.getProductAttribute().get(newProductItem.getSelectedProductItem()).getAttributeId(), "","plus");

                }
            }
        });
        holder.btn_add.setTag(position);
        holder.btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Toast.makeText(activity,""+newProductItem.getProductAttribute().get(newProductItem.getSelectedProductItem()).getAttributeValue().toString(),Toast.LENGTH_LONG).show();
//                Toast.makeText(activity,"p_id "+newProductItem.getProductId()+"\n customiza "+newProductItem.getIsProductCustomizable()+"\n "+newProductItem.getProductBrand()+"\n "+newProductItem.getProductColor()+"\n "+newProductItem.getProductDescription()+"\n "+newProductItem.getProductName()
//                        +"\n "+newProductItem.getProductOldPrice()+"\n "+newProductItem.getProductPrice()+"\n "+newProductItem.getProductPrimaryImage()+"\n "+newProductItem.getProductStatus()+"\n "+newProductItem.getProductStock()+"\n "+newProductItem.getFavoriteFlag()+"\n "+newProductItem.getQuantity()
//                        +"\n "+newProductItem.getProductAttribute().get(newProductItem.getSelectedProductItem()).getAttributeId().toString()+"\n "+newProductItem.getProductImage()+"\n "+newProductItem.getProductSize(),Toast.LENGTH_LONG).show();
                int qty = 0;
                if (!newProductItem.getQuantity().equals(""))
                    qty = Integer.parseInt(newProductItem.getQuantity());

                qty = qty + 1;
                newProductItem.setQuantity("" + qty);
                notifyDataSetChanged();
                if (newProductItem.getSelectedProductItem() == -1) {
                    requestCheck_stock_for_add_to_cart(newProductItem, newProductItem.getProductAttribute().get(0).getAttributeId(), "","plus");
                }else {
                    requestCheck_stock_for_add_to_cart(newProductItem, newProductItem.getProductAttribute().get(newProductItem.getSelectedProductItem()).getAttributeId(), "","plus");

                }
            }
        });
        holder.btn_min.setTag(position);
        holder.btn_min.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int qty = 0;
                if (!newProductItem.getQuantity().equals(""))
                    qty = Integer.parseInt(newProductItem.getQuantity());

                if (qty != 1) {
                    qty = qty - 1;
                    newProductItem.setQuantity("" + qty);
                    notifyDataSetChanged();
                    if (newProductItem.getSelectedProductItem() == -1) {
                        requestCheck_stock_for_add_to_cart(newProductItem, newProductItem.getProductAttribute().get(0).getAttributeId(), "","mins");
                    }else {
                        requestCheck_stock_for_add_to_cart(newProductItem, newProductItem.getProductAttribute().get(newProductItem.getSelectedProductItem()).getAttributeId(), "","mins");

                    }

                    //  requestStockCheck(obj, obj.getProductId(), obj.getOrder_id(), "" + quatity, obj.getAttributeId(), obj.getColorCode());
                }
            }
        });
        if (newProductItem.getQuantity() == null) {
            newProductItem.setQuantity("0");
        }
        if(newProductItem.getQuantity().equalsIgnoreCase( "0")){
            holder.btn_add.setVisibility(View.VISIBLE);
            holder.ll_plus_minu.setVisibility(View.GONE);
        }else {
            holder.ll_plus_minu.setVisibility(View.VISIBLE);
            holder.btn_add.setVisibility(View.GONE);
        }
        if (newProductItem.getProductAttribute() != null) {
            if (newProductItem.getSelectedProductItem() == -1) {
                holder.tv_weight_quantity.setText(newProductItem.getProductAttribute().get(0).getAttributeName() + " / " + newProductItem.getProductAttribute().get(0).getAttributeValue());
            } else {
                holder.tv_weight_quantity.setText(newProductItem.getProductAttribute().get(newProductItem.getSelectedProductItem()).getAttributeName() + " / " + newProductItem.getProductAttribute().get(newProductItem.getSelectedProductItem()).getAttributeValue());


            }
            holder.ly_select_weight.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SelectSizeDialogFragment selectSizeDialogFragment = new SelectSizeDialogFragment();
                    selectSizeDialogFragment.setNewProductItem(newProductItem);
                    selectSizeDialogFragment.setOnDismissListener(new CategoryProductItemAdapter.OnDismissListener() {
                        @Override
                        public void onDismiss() {
                            notifyDataSetChanged();
                        }
                    });
                    selectSizeDialogFragment.show(activity.getSupportFragmentManager(), "");

                }
            });
        } else {
            holder.tv_weight_quantity.setText(newProductItem.getProductAttribute().get(0).getAttributeName() + " / " + newProductItem.getProductAttribute().get(0).getAttributeValue());
        }
        holder.btn_quty.setText("" + newProductItem.getQuantity());
    }

    @Override
    public int getItemCount() {
        return newProductsList.size();
    }

    @Override
    public int getItemViewType(int position) {
        int viewType = 0;

        return viewType;
    }
    private void requestCheck_stock_for_add_to_cart(final NewProductItem productItem, String str_sizeid, String str_colorcode,final String quantity) {

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
    private void GetJsonObject(NewProductItem item,String quantity) {
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
        //product.addProperty("size_id", item.getProductId());

        if (item.getSelectedProductItem() == -1) {

            product.addProperty("size_id", item.getProductAttribute().get(0).getAttributeId());
            product.addProperty("price", item.getProductAttribute().get(0).getAttributeValue());

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
                    //        homeFragment.setUpdateCart();
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
}