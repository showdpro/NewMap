package in.mapbazar.mapbazar;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;


public class ActivityRecentlyProductDetails extends AppCompatActivity {

//    @BindView(R.id.ly_back)
//    RelativeLayout ly_back;
//
//    @BindView(R.id.layout_cart)
//    RelativeLayout layout_cart;
//
//    @BindView(R.id.layout_cart_cout)
//    RelativeLayout layout_cart_cout;
//
//    @BindView(R.id.txt_cart_cout)
//    CustomTextView txt_cart_cout;
//
//    @BindView(R.id.ly_product_main)
//    RelativeLayout ly_product_main;
//
//    @BindView(R.id.txt_addtocart)
//    CustomTextView txt_addtocart;
//
//    @BindView(R.id.product_pager)
//    ViewPager product_pager;
//
//    @BindView(R.id.product_indicator)
//    CirclePageIndicator product_indicator;
//
//    @BindView(R.id.txt_product_name)
//    CustomTextView txt_product_name;
//
//    @BindView(R.id.txt_product_prise)
//    CustomTextView txt_product_prise;
//
//    @BindView(R.id.layout_favorite)
//    LinearLayout layout_favorite;
//
//    @BindView(R.id.img_favorite)
//    ImageView img_favorite;
//
//    @BindView(R.id.txt_product_brand)
//    CustomTextView txt_product_brand;
//
//    @BindView(R.id.html_txt_product_decrip)
//    HtmlTextView html_txt_product_decrip;
//
//    @BindView(R.id.tet_size)
//    CustomTextView txt_product_size;
//
//    @BindView(R.id.layout_color)
//    LinearLayout layout_color;
//
//    @BindView(R.id.layout_attribute)
//    LinearLayout layout_attribute;
//
//    @BindView(R.id.match_product_recycler)
//    RecyclerView match_product_recycler;
//
//    @BindView(R.id.recent_prduct_recycle)
//    RecyclerView recent_prduct_recycle;
//
//    @BindView(R.id.ly_matchwith)
//    LinearLayout ly_matchwith;
//
//    @BindView(R.id.ly_recentview)
//    LinearLayout ly_recentview;
//
//    @BindView(R.id.ly_product_attribute)
//    LinearLayout ly_product_attribute;
//
//    @BindView(R.id.ly_sizeview)
//    LinearLayout ly_sizeview;
//
//    @BindView(R.id.ly_decription)
//    LinearLayout ly_decription;
//
//    @BindView(R.id.layout_cart_item)
//    FrameLayout layoutCartItem;
//
//    @BindView(R.id.ll_select_size)
//    LinearLayout ll_select_size;
//
//    private RecentlyProductItem productItem;
//    List<RevcentlyProductAttributeItem> recentlyProductSizeItemList;
//    List<RevcentlyProductAttributeItem> revcentlyProductAttributeItemList;
//
//    List<MatchProductItem> list_MatchProductItem;
//    List<RecentlyProductItem> list_RecentlyProductItem;
//
//    ArrayList<String> colorlist = new ArrayList<>();
//
//    //Shared Preferences
//    private SharedPreferences sPref;
//
//    int isproductfavorite = 0;
//
//    String str_sizeid = "", str_colorcode = "";
//
//    Dialog ProgressDialog;
//
//    //Broadcast Receiver
//    BroadcastReceiver UpdateCart = null;
//    Boolean UpdateCartRegistered = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
//        ButterKnife.bind(this);
//        Intent intent = getIntent();
//        productItem = (RecentlyProductItem) intent.getSerializableExtra("RecentProductItem");
//        deliveryShippingFragment = null;
//        sPref = PreferenceManager.getDefaultSharedPreferences(ActivityRecentlyProductDetails.this);
//        initdata();
//
//        if (Common.isNetworkAvailable(ActivityRecentlyProductDetails.this)) {
//            requestProductFavrite();
//            requestProductDetails();
//            requestViewProduct();
//
//        } else {
//            GetRetriverd();
//        }
//
//        resetSizeValue();
//        freeMemory();
    }

//    public void freeMemory() {
//        System.runFinalization();
//        Runtime.getRuntime().gc();
//        System.gc();
//
//    }
//    public void resetSizeValue(){
//        if (productItem.getProductAttribute() != null) {
//            if (productItem.getSelectedProductItem() == -1) {
//                txt_product_prise.setText(sPref.getString("CUR", "") + " " + productItem.getProductPrice());
//
//                txt_product_size.setText(productItem.getProductAttribute().get(0).getAttributeName() + " / " + productItem.getProductAttribute().get(0).getAttributeValue());
//            } else {
//                txt_product_prise.setText(sPref.getString("CUR", "") + " " + productItem.getProductAttribute().get(productItem.getSelectedProductItem()).getAttributeValue());
//
//                txt_product_size.setText(productItem.getProductAttribute().get(productItem.getSelectedProductItem()).getAttributeName() + " / " + productItem.getProductAttribute().get(productItem.getSelectedProductItem()).getAttributeValue());
//
//
//            }
//            ll_select_size.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    SelectSizeDialogFragment selectSizeDialogFragment = new SelectSizeDialogFragment();
//                    selectSizeDialogFragment.setRecentlyProductItem(productItem);
//                    selectSizeDialogFragment.setOnDismissListener(new CategoryProductItemAdapter.OnDismissListener() {
//                        @Override
//                        public void onDismiss() {
//                            resetSizeValue();
//                        }
//                    });
//                    selectSizeDialogFragment.show(getSupportFragmentManager(), "");
//
//                }
//            });
//        } else {
//            txt_product_size.setText(productItem.getProductAttribute().get(0).getAttributeName() + " / " + productItem.getProductAttribute().get(0).getAttributeValue());
//        }
//    }
//
//
//    private void GetRetriverd() {
//        // Common.showInternetInfo(ActivitySignIn.this, "Network is not available");
//        Dialog dialog = new DialogUtils(ActivityRecentlyProductDetails.this).buildDialogInternate(new CallbackInternate() {
//            @Override
//            public void onSuccess(Dialog dialog) {
//                dialog.dismiss();
//
//                if (Common.isNetworkAvailable(ActivityRecentlyProductDetails.this)) {
//                    requestProductDetails();
//                    requestViewProduct();
//                } else {
//                    GetRetriverd();
//                }
//            }
//        });
//        dialog.show();
//    }
//
//    public void initdata() {
//
//        UpdateCart = new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                Log.e(Common.TAG, "onReceive-user");
//                try {
//
//                    if (sPref.getInt("Cart", 0) == 0) {
//                        txt_cart_cout.setText("");
//                        layout_cart_cout.setVisibility(View.GONE);
//                    } else {
//                        txt_cart_cout.setText("" + sPref.getInt("Cart", 0));
//                        layout_cart_cout.setVisibility(View.VISIBLE);
//                    }
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        };
//
//
//        if (sPref.getInt("Cart", 0) == 0) {
//            txt_cart_cout.setText("");
//            layout_cart_cout.setVisibility(View.GONE);
//        } else {
//            txt_cart_cout.setText("" + sPref.getInt("Cart", 0));
//            layout_cart_cout.setVisibility(View.VISIBLE);
//        }
//
//        ProgressDialog = new Dialog(ActivityRecentlyProductDetails.this, android.R.style.Theme_Translucent_NoTitleBar);
//        ProgressDialog.setContentView(R.layout.progressbar);
//        ProgressDialog.setCancelable(false);
//
//        //Adapter for Home_Slider
//        RecentlyProductViewPagerAdapter mCustomPagerAdapter = new RecentlyProductViewPagerAdapter(ActivityRecentlyProductDetails.this, productItem.getProductImage());
//        product_pager.setAdapter(mCustomPagerAdapter);
//        product_indicator.setViewPager(product_pager);
//
//        txt_product_name.setText("" + productItem.getProductName());
//        txt_product_prise.setText(sPref.getString("CUR", "") + " " + productItem.getProductPrice());
//        txt_product_brand.setText("" + productItem.getProductBrand());
//
//        if (productItem.getProductBrand().equals(""))
//            txt_product_brand.setVisibility(View.GONE);
//        else
//            txt_product_brand.setVisibility(View.VISIBLE);
//
//        html_txt_product_decrip.setHtml(productItem.getProductDescription(), new HtmlHttpImageGetter(html_txt_product_decrip));
//
//        if (productItem.getProductDescription().equals("") || productItem.getProductDescription().equalsIgnoreCase("null")) {
//            ly_decription.setVisibility(View.GONE);
//        }
//
//        recentlyProductSizeItemList = productItem.getProductAttribute();
//
//        if (recentlyProductSizeItemList != null && recentlyProductSizeItemList.size() > 0) {
//            String str_size = "";
//            for (int i = 0; i < recentlyProductSizeItemList.size(); i++) {
//                RevcentlyProductAttributeItem recentlyProductSizeItem = recentlyProductSizeItemList.get(i);
//
//                if (i == recentlyProductSizeItemList.size() - 1)
//                    str_size = str_size + recentlyProductSizeItem.getAttributeName();
//                else
//                    str_size = str_size + recentlyProductSizeItem.getAttributeName() + "|";
//
//
//             //   colorlist.add(recentlyProductSizeItem.getSizeColor());
//
//            }
//            txt_product_size.setText("" + str_size);
//            setColorView(colorlist);
//        } else {
//            ly_sizeview.setVisibility(View.GONE);
//        }
//
//        revcentlyProductAttributeItemList = productItem.getProductAttribute();
//
//        if (revcentlyProductAttributeItemList != null && revcentlyProductAttributeItemList.size() > 0) {
//            setAttributeView(revcentlyProductAttributeItemList);
//        } else {
//            ly_product_attribute.setVisibility(View.GONE);
//        }
//
//        ly_back.setOnClickListener(this);
//        ly_sizeview.setOnClickListener(this);
//        layout_favorite.setOnClickListener(this);
//        txt_addtocart.setOnClickListener(this);
//        layout_cart.setOnClickListener(this);
//    }
//
//    @Override
//    public void onClick(View v) {
//
//        switch (v.getId()) {
//
//            case R.id.ly_back:
//                if (deliveryShippingFragment != null) {
//                    getSupportFragmentManager().beginTransaction().remove(deliveryShippingFragment);
//                    deliveryShippingFragment = null;
//                    layoutCartItem.setVisibility(View.GONE);
//                }else {
//                    list_MatchProductItem = new ArrayList<>();
//                    list_MatchProductItem.clear();
//                    list_RecentlyProductItem = new ArrayList<>();
//                    list_RecentlyProductItem.clear();
//                    finish();
//                }
//                break;
//
////            case R.id.ly_sizeview:
////                Dialog dialog = new DialogUtils(ActivityRecentlyProductDetails.this).buildDialogRecentSizeColor(new ProductSizeCallbackMessage() {
////                    @Override
////                    public void onSuccess(Dialog dialog, String sizeid, String colorcode) {
////                        dialog.dismiss();
////                        str_sizeid = sizeid;
////                        str_colorcode = colorcode;
////
////                        requestCheck_stock();
////                    }
////
////                    @Override
////                    public void onCancel(Dialog dialog) {
////                        dialog.dismiss();
////                    }
////                }, productItem);
////                dialog.show();
////                break;
//
//            case R.id.layout_favorite:
//                if (isproductfavorite == 1) {
//                    updateHeartButton(false);
//                    requestRemoveFavrite();
//
//                } else {
//                    updateHeartButton(true);
//                    requestUserFavrite();
//                }
//                break;
//
//            case R.id.txt_addtocart:
//
////                if (recentlyProductSizeItemList != null && recentlyProductSizeItemList.size() > 0) {
////                    if (str_sizeid.equals("") && str_colorcode.equals("")) {
////                        Dialog dialog1 = new DialogUtils(ActivityRecentlyProductDetails.this).buildDialogMessage(new CallbackMessage() {
////                            @Override
////                            public void onSuccess(Dialog dialog) {
////                                dialog.dismiss();
////                            }
////
////                            @Override
////                            public void onCancel(Dialog dialog) {
////                                dialog.dismiss();
////                            }
////                        }, getResources().getString(R.string.please_size));
////                        dialog1.show();
////                    } else {
////                        requestCheck_stock();
////                    }
////                } else if (colorlist != null && colorlist.size() > 0) {
////                    if (str_sizeid.equals("") && str_colorcode.equals("")) {
////                        Dialog dialog1 = new DialogUtils(ActivityRecentlyProductDetails.this).buildDialogMessage(new CallbackMessage() {
////                            @Override
////                            public void onSuccess(Dialog dialog) {
////                                dialog.dismiss();
////                            }
////
////                            @Override
////                            public void onCancel(Dialog dialog) {
////                                dialog.dismiss();
////                            }
////                        }, getResources().getString(R.string.please_size));
////                        dialog1.show();
////                    } else {
////                        requestCheck_stock();
////                    }
////                } else {
//                    requestCheck_stock();
//           //     }
//
//                break;
//            case R.id.layout_cart:
//                if(deliveryShippingFragment == null) {
//                    layoutCartItem.setVisibility(View.VISIBLE);
//                    deliveryShippingFragment = new DeliveryShippingFragment();
//                    FragmentManager cartfragmentManager = getSupportFragmentManager();
//                    cartfragmentManager.beginTransaction()
//                            .replace(R.id.layout_cart_item, deliveryShippingFragment).addToBackStack("deliveryShippingFragment")
//                            .commit();
//                }
////                Intent main = new Intent(ActivityRecentlyProductDetails.this,MainActivity.class);
////                SharedPreferences.Editor sh = sPref.edit();
////                sh.putBoolean("Iscart",true);
////                sh.commit();
////                startActivity(main);
//
//                break;
//        }
//
//    }
//    DeliveryShippingFragment deliveryShippingFragment;
//
//    public void setColorView(ArrayList<String> colorlist) {
//        layout_color.removeAllViews();
//
//        for (int i = 0; i < colorlist.size(); i++) {
//
//            LayoutInflater inflater = getLayoutInflater();
//            View view = inflater.inflate(R.layout.row_colorsize, ly_product_main, false);
//
//            ImageView imageView = (ImageView) view.findViewById(R.id.img_color);
//
//            String colorcode = colorlist.get(i);
//
//            if (!colorcode.equals("") && !colorcode.equalsIgnoreCase("null")) {
//                imageView.setBackgroundColor(Color.parseColor(colorcode));
//            }
//
//            layout_color.addView(view);
//        }
//
//    }
//
//    public void setAttributeView(List<RevcentlyProductAttributeItem> revcentlyProductAttributeItemList) {
//        layout_attribute.removeAllViews();
//
//        for (int i = 0; i < revcentlyProductAttributeItemList.size(); i++) {
//
//            LayoutInflater inflater = getLayoutInflater();
//            View view = inflater.inflate(R.layout.row_product_attribute, ly_product_main, false);
//
//            CustomTextView txt_attribute_name = (CustomTextView) view.findViewById(R.id.txt_attribute_name);
//            CustomTextView txt_attribute_value = (CustomTextView) view.findViewById(R.id.txt_attribute_value);
//
//            RevcentlyProductAttributeItem revcentlyProductAttributeItem = revcentlyProductAttributeItemList.get(i);
//
//            txt_attribute_name.setText(revcentlyProductAttributeItem.getAttributeName());
//            txt_attribute_value.setText(revcentlyProductAttributeItem.getAttributeValue());
//
//            layout_attribute.addView(view);
//        }
//
//    }
//
//    private void requestProductDetails() {
//
//        API api = RestAdapter.createAPI();
//
//        Call<JsonObject> callBooking = api.product_detail(sPref.getString("uid", ""), productItem.getProductId());
//
//        callBooking.enqueue(new Callback<JsonObject>() {
//            @Override
//            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
//                JsonObject resp = response.body();
//                Log.e(Common.TAG, "onResponse-recent_details" + resp);
//                if (resp != null) {
//                    if (resp.get("status").getAsString().equals("success")) {
//
//                        JsonArray match_product_detail;
//
//                        if (resp.has("match_product_detail") && resp.get("match_product_detail").isJsonArray())
//
//                            match_product_detail = resp.get("match_product_detail").getAsJsonArray();
//                        else
//                            match_product_detail = null;
//
//                        if (match_product_detail != null && match_product_detail.size() > 0) {
//
//                            list_MatchProductItem = new ArrayList<>();
//
//                            for (int i = 0; i < match_product_detail.size(); i++) {
//
//                                JsonObject jsonObject_new_product = match_product_detail.get(i).getAsJsonObject();
//
//                                MatchProductItem newProductItem = new MatchProductItem();
//
//                                if (jsonObject_new_product.has("product_id") && !jsonObject_new_product.get("product_id").isJsonNull())
//                                    newProductItem.setProductId(jsonObject_new_product.get("product_id").getAsString());
//                                else
//                                    newProductItem.setProductId("");
//
//                                if (jsonObject_new_product.has("product_name") && !jsonObject_new_product.get("product_name").isJsonNull())
//                                    newProductItem.setProductName(jsonObject_new_product.get("product_name").getAsString());
//                                else
//                                    newProductItem.setProductName("");
//
//                                if (jsonObject_new_product.has("product_name_hindi") && !jsonObject_new_product.get("product_name_hindi").isJsonNull())
//                                    newProductItem.setProduct_name_hindi(jsonObject_new_product.get("product_name_hindi").getAsString());
//                                else
//                                    newProductItem.setProduct_name_hindi("");
//
//                                if (jsonObject_new_product.has("product_status") && !jsonObject_new_product.get("product_status").isJsonNull())
//                                    newProductItem.setProductStatus(jsonObject_new_product.get("product_status").getAsString());
//                                else
//                                    newProductItem.setProductStatus("");
//
//                                if (jsonObject_new_product.has("product_primary_image") && !jsonObject_new_product.get("product_primary_image").isJsonNull())
//                                    newProductItem.setProductPrimaryImage(jsonObject_new_product.get("product_primary_image").getAsString());
//                                else
//                                    newProductItem.setProductPrimaryImage("");
//
//                                if (jsonObject_new_product.has("product_price") && !jsonObject_new_product.get("product_price").isJsonNull())
//                                    newProductItem.setProductPrice(jsonObject_new_product.get("product_price").getAsString());
//                                else
//                                    newProductItem.setProductPrice("");
//
//                                if (jsonObject_new_product.has("product_old_price") && !jsonObject_new_product.get("product_old_price").isJsonNull())
//                                    newProductItem.setProductOldPrice(jsonObject_new_product.get("product_old_price").getAsString());
//                                else
//                                    newProductItem.setProductOldPrice("");
//
//                                if (jsonObject_new_product.has("product_stock") && !jsonObject_new_product.get("product_stock").isJsonNull())
//                                    newProductItem.setProductStock(jsonObject_new_product.get("product_stock").getAsString());
//                                else
//                                    newProductItem.setProductStock("");
//
//                                if (jsonObject_new_product.has("product_description") && !jsonObject_new_product.get("product_description").isJsonNull())
//                                    newProductItem.setProductDescription(jsonObject_new_product.get("product_description").getAsString());
//                                else
//                                    newProductItem.setProductDescription("");
//
//                                if (jsonObject_new_product.has("isProductCustomizable") && !jsonObject_new_product.get("isProductCustomizable").isJsonNull())
//                                    newProductItem.setIsProductCustomizable(jsonObject_new_product.get("isProductCustomizable").getAsString());
//                                else
//                                    newProductItem.setIsProductCustomizable("");
//
//                                if (jsonObject_new_product.has("product_brand") && !jsonObject_new_product.get("product_brand").isJsonNull())
//                                    newProductItem.setProductBrand(jsonObject_new_product.get("product_brand").getAsString());
//                                else
//                                    newProductItem.setProductBrand("");
//
//                                if (jsonObject_new_product.has("product_color") && !jsonObject_new_product.get("product_color").isJsonNull())
//                                    newProductItem.setProductColor(jsonObject_new_product.get("product_color").getAsString());
//                                else
//                                    newProductItem.setProductColor("");
//
//                                if (jsonObject_new_product.has("favorite_flag") && !jsonObject_new_product.get("favorite_flag").isJsonNull())
//                                    newProductItem.setFavoriteFlag(jsonObject_new_product.get("favorite_flag").getAsInt());
//                                else
//                                    newProductItem.setFavoriteFlag(0);
//
//
//                                JsonArray product_image;
//
//                                if (jsonObject_new_product.has("product_image") && jsonObject_new_product.get("product_image").isJsonArray())
//
//                                    product_image = jsonObject_new_product.get("product_image").getAsJsonArray();
//                                else
//                                    product_image = null;
//
//                                if (product_image != null && product_image.size() > 0) {
//
//                                    List<in.mapbazar.mapbazar.Model.MatchWithProduct.ProductImageItem> list_ProductImageItem = new ArrayList<>();
//
//                                    for (int j = 0; j < product_image.size(); j++) {
//
//                                        JsonObject jsonObject_product_image = product_image.get(j).getAsJsonObject();
//
//                                        in.mapbazar.mapbazar.Model.MatchWithProduct.ProductImageItem productImageItem = new in.mapbazar.mapbazar.Model.MatchWithProduct.ProductImageItem();
//
//                                        if (jsonObject_product_image.has("image") && !jsonObject_product_image.get("image").isJsonNull())
//                                            productImageItem.setImage(jsonObject_product_image.get("image").getAsString());
//                                        else
//                                            productImageItem.setImage("");
//
//                                        list_ProductImageItem.add(productImageItem);
//
//                                    }
//
//                                    newProductItem.setProductImage(list_ProductImageItem);
//                                }
//
//                                JsonArray product_size;
//
//                                if (jsonObject_new_product.has("product_size") && jsonObject_new_product.get("product_size").isJsonArray())
//
//                                    product_size = jsonObject_new_product.get("product_size").getAsJsonArray();
//                                else
//                                    product_size = null;
//
//                                if (product_size != null && product_size.size() > 0) {
//
//                                    List<MatchProductSizeItem> list_Match_ProductSizeItem = new ArrayList<>();
//
//                                    for (int j = 0; j < product_size.size(); j++) {
//
//                                        JsonObject jsonObject_product_size = product_size.get(j).getAsJsonObject();
//
//                                        MatchProductSizeItem matchProductSizeItem = new MatchProductSizeItem();
//
//                                        if (jsonObject_product_size.has("size_id") && !jsonObject_product_size.get("size_id").isJsonNull())
//                                            matchProductSizeItem.setSizeId(jsonObject_product_size.get("size_id").getAsString());
//                                        else
//                                            matchProductSizeItem.setSizeId("");
//
//                                        if (jsonObject_product_size.has("size_name") && !jsonObject_product_size.get("size_name").isJsonNull())
//                                            matchProductSizeItem.setSizeName(jsonObject_product_size.get("size_name").getAsString());
//                                        else
//                                            matchProductSizeItem.setSizeName("");
//
//                                        if (jsonObject_product_size.has("size_image") && !jsonObject_product_size.get("size_image").isJsonNull())
//                                            matchProductSizeItem.setSizeImage(jsonObject_product_size.get("size_image").getAsString());
//                                        else
//                                            matchProductSizeItem.setSizeImage("");
//
//                                        if (jsonObject_product_size.has("size_stock") && !jsonObject_product_size.get("size_stock").isJsonNull())
//                                            matchProductSizeItem.setSizeStock(jsonObject_product_size.get("size_stock").getAsString());
//                                        else
//                                            matchProductSizeItem.setSizeStock("");
//
//                                        if (jsonObject_product_size.has("size_color") && !jsonObject_product_size.get("size_color").isJsonNull())
//                                            matchProductSizeItem.setSizeColor(jsonObject_product_size.get("size_color").getAsString());
//                                        else
//                                            matchProductSizeItem.setSizeColor("");
//
//                                        if (jsonObject_product_size.has("size_price") && !jsonObject_product_size.get("size_price").isJsonNull())
//                                            matchProductSizeItem.setSizePrice(jsonObject_product_size.get("size_price").getAsString());
//                                        else
//                                            matchProductSizeItem.setSizePrice("");
//
//
//                                        JsonArray color;
//
//                                        if (jsonObject_product_size.has("color") && jsonObject_product_size.get("color").isJsonArray())
//
//                                            color = jsonObject_product_size.get("color").getAsJsonArray();
//                                        else
//                                            color = null;
//
//                                        if (color != null && color.size() > 0) {
//
//                                            List<ColorItem> list_ColorItem = new ArrayList<>();
//
//                                            for (int k = 0; k < color.size(); k++) {
//
//                                                JsonObject jsonObject_ColorItem = color.get(k).getAsJsonObject();
//
//                                                ColorItem colorItem = new ColorItem();
//
//                                                if (jsonObject_ColorItem.has("color_id") && !jsonObject_ColorItem.get("color_id").isJsonNull())
//                                                    colorItem.setColorId(jsonObject_ColorItem.get("color_id").getAsString());
//                                                else
//                                                    colorItem.setColorId("");
//
//                                                if (jsonObject_ColorItem.has("color_code") && !jsonObject_ColorItem.get("color_code").isJsonNull())
//                                                    colorItem.setColorCode(jsonObject_ColorItem.get("color_code").getAsString());
//                                                else
//                                                    colorItem.setColorCode("");
//
//                                                if (jsonObject_ColorItem.has("color_image") && !jsonObject_ColorItem.get("color_image").isJsonNull())
//                                                    colorItem.setColorImage(jsonObject_ColorItem.get("color_image").getAsString());
//                                                else
//                                                    colorItem.setColorImage("");
//
//                                                if (jsonObject_ColorItem.has("color_stock") && !jsonObject_ColorItem.get("color_stock").isJsonNull())
//                                                    colorItem.setColorStock(jsonObject_ColorItem.get("color_stock").getAsString());
//                                                else
//                                                    colorItem.setColorStock("");
//
//                                                if (jsonObject_ColorItem.has("color_price") && !jsonObject_ColorItem.get("color_price").isJsonNull())
//                                                    colorItem.setColorPrice(jsonObject_ColorItem.get("color_price").getAsString());
//                                                else
//                                                    colorItem.setColorPrice("");
//
//                                                list_ColorItem.add(colorItem);
//
//                                            }
//
//                                            matchProductSizeItem.setColor(list_ColorItem);
//                                        }
//
//                                        list_Match_ProductSizeItem.add(matchProductSizeItem);
//
//                                    }
//
//                                    newProductItem.setProductSize(list_Match_ProductSizeItem);
//                                }
//
//                                JsonArray product_attribute;
//
//                                if (jsonObject_new_product.has("product_attribute") && jsonObject_new_product.get("product_attribute").isJsonArray())
//
//                                    product_attribute = jsonObject_new_product.get("product_attribute").getAsJsonArray();
//                                else
//                                    product_attribute = null;
//
//                                if (product_attribute != null && product_attribute.size() > 0) {
//
//                                    List<MatchProductAttributeItem> list_Match_ProductAttributeItem = new ArrayList<>();
//
//                                    for (int j = 0; j < product_attribute.size(); j++) {
//
//                                        JsonObject jsonObject_product_attribute = product_attribute.get(j).getAsJsonObject();
//
//                                        MatchProductAttributeItem matchProductAttributeItem = new MatchProductAttributeItem();
//
//                                        if (jsonObject_product_attribute.has("attribute_id") && !jsonObject_product_attribute.get("attribute_id").isJsonNull())
//                                            matchProductAttributeItem.setAttributeId(jsonObject_product_attribute.get("attribute_id").getAsString());
//                                        else
//                                            matchProductAttributeItem.setAttributeId("");
//
//                                        if (jsonObject_product_attribute.has("attribute_name") && !jsonObject_product_attribute.get("attribute_name").isJsonNull())
//                                            matchProductAttributeItem.setAttributeName(jsonObject_product_attribute.get("attribute_name").getAsString());
//                                        else
//                                            matchProductAttributeItem.setAttributeName("");
//
//                                        if (jsonObject_product_attribute.has("attribute_value") && !jsonObject_product_attribute.get("attribute_value").isJsonNull())
//                                            matchProductAttributeItem.setAttributeValue(jsonObject_product_attribute.get("attribute_value").getAsString());
//                                        else
//                                            matchProductAttributeItem.setAttributeValue("");
//
//                                        list_Match_ProductAttributeItem.add(matchProductAttributeItem);
//
//                                    }
//
//                                    newProductItem.setProductAttribute(list_Match_ProductAttributeItem);
//                                }
//
//                                list_MatchProductItem.add(newProductItem);
//                            }
//
//                            if (list_MatchProductItem.size() > 0)
//                                displayForMatchView(list_MatchProductItem);
//                            else
//                                ly_matchwith.setVisibility(View.GONE);
//
//                        } else {
//
//                            ly_matchwith.setVisibility(View.GONE);
//                        }
//
//                        JsonArray recent_product;
//
//                        if (resp.has("recent_product") && resp.get("recent_product").isJsonArray())
//
//                            recent_product = resp.get("recent_product").getAsJsonArray();
//                        else
//                            recent_product = null;
//
//                        if (recent_product != null && recent_product.size() > 0) {
//
//                            list_RecentlyProductItem = new ArrayList<>();
//
//                            for (int i = 0; i < recent_product.size(); i++) {
//
//                                JsonObject jsonObject_new_product = recent_product.get(i).getAsJsonObject();
//
//                                RecentlyProductItem newProductItem = new RecentlyProductItem();
//
//                                if (jsonObject_new_product.has("product_id") && !jsonObject_new_product.get("product_id").isJsonNull())
//                                    newProductItem.setProductId(jsonObject_new_product.get("product_id").getAsString());
//                                else
//                                    newProductItem.setProductId("");
//
//                                if (jsonObject_new_product.has("product_name") && !jsonObject_new_product.get("product_name").isJsonNull())
//                                    newProductItem.setProductName(jsonObject_new_product.get("product_name").getAsString());
//                                else
//                                    newProductItem.setProductName("");
//                                if (jsonObject_new_product.has("product_name_hindi") && !jsonObject_new_product.get("product_name_hindi").isJsonNull())
//                                    newProductItem.setProduct_name_hindi(jsonObject_new_product.get("product_name_hindi").getAsString());
//                                else
//                                    newProductItem.setProduct_name_hindi("");
//
//                                if (jsonObject_new_product.has("product_status") && !jsonObject_new_product.get("product_status").isJsonNull())
//                                    newProductItem.setProductStatus(jsonObject_new_product.get("product_status").getAsString());
//                                else
//                                    newProductItem.setProductStatus("");
//
//                                if (jsonObject_new_product.has("product_primary_image") && !jsonObject_new_product.get("product_primary_image").isJsonNull())
//                                    newProductItem.setProductPrimaryImage(jsonObject_new_product.get("product_primary_image").getAsString());
//                                else
//                                    newProductItem.setProductPrimaryImage("");
//
//                                if (jsonObject_new_product.has("product_price") && !jsonObject_new_product.get("product_price").isJsonNull())
//                                    newProductItem.setProductPrice(jsonObject_new_product.get("product_price").getAsString());
//                                else
//                                    newProductItem.setProductPrice("");
//
//                                if (jsonObject_new_product.has("product_old_price") && !jsonObject_new_product.get("product_old_price").isJsonNull())
//                                    newProductItem.setProductOldPrice(jsonObject_new_product.get("product_old_price").getAsString());
//                                else
//                                    newProductItem.setProductOldPrice("");
//
//                                if (jsonObject_new_product.has("product_stock") && !jsonObject_new_product.get("product_stock").isJsonNull())
//                                    newProductItem.setProductStock(jsonObject_new_product.get("product_stock").getAsString());
//                                else
//                                    newProductItem.setProductStock("");
//
//                                if (jsonObject_new_product.has("product_description") && !jsonObject_new_product.get("product_description").isJsonNull())
//                                    newProductItem.setProductDescription(jsonObject_new_product.get("product_description").getAsString());
//                                else
//                                    newProductItem.setProductDescription("");
//
//                                if (jsonObject_new_product.has("isProductCustomizable") && !jsonObject_new_product.get("isProductCustomizable").isJsonNull())
//                                    newProductItem.setIsProductCustomizable(jsonObject_new_product.get("isProductCustomizable").getAsString());
//                                else
//                                    newProductItem.setIsProductCustomizable("");
//
//                                if (jsonObject_new_product.has("product_brand") && !jsonObject_new_product.get("product_brand").isJsonNull())
//                                    newProductItem.setProductBrand(jsonObject_new_product.get("product_brand").getAsString());
//                                else
//                                    newProductItem.setProductBrand("");
//
//                                if (jsonObject_new_product.has("product_color") && !jsonObject_new_product.get("product_color").isJsonNull())
//                                    newProductItem.setProductColor(jsonObject_new_product.get("product_color").getAsString());
//                                else
//                                    newProductItem.setProductColor("");
//
//                                if (jsonObject_new_product.has("favorite_flag") && !jsonObject_new_product.get("favorite_flag").isJsonNull())
//                                    newProductItem.setFavoriteFlag(jsonObject_new_product.get("favorite_flag").getAsInt());
//                                else
//                                    newProductItem.setFavoriteFlag(0);
//
//
//                                JsonArray product_image;
//
//                                if (jsonObject_new_product.has("product_image") && jsonObject_new_product.get("product_image").isJsonArray())
//
//                                    product_image = jsonObject_new_product.get("product_image").getAsJsonArray();
//                                else
//                                    product_image = null;
//
//                                if (product_image != null && product_image.size() > 0) {
//
//                                    List<in.mapbazar.mapbazar.Model.RecentlyProduct.ProductImageItem> list_ProductImageItem = new ArrayList<>();
//
//                                    for (int j = 0; j < product_image.size(); j++) {
//
//                                        JsonObject jsonObject_product_image = product_image.get(j).getAsJsonObject();
//
//                                        in.mapbazar.mapbazar.Model.RecentlyProduct.ProductImageItem productImageItem = new in.mapbazar.mapbazar.Model.RecentlyProduct.ProductImageItem();
//
//                                        if (jsonObject_product_image.has("image") && !jsonObject_product_image.get("image").isJsonNull())
//                                            productImageItem.setImage(jsonObject_product_image.get("image").getAsString());
//                                        else
//                                            productImageItem.setImage("");
//
//                                        list_ProductImageItem.add(productImageItem);
//
//                                    }
//
//                                    newProductItem.setProductImage(list_ProductImageItem);
//                                }
//
//                                JsonArray product_size;
//
//                                if (jsonObject_new_product.has("product_size") && jsonObject_new_product.get("product_size").isJsonArray())
//
//                                    product_size = jsonObject_new_product.get("product_size").getAsJsonArray();
//                                else
//                                    product_size = null;
//
//                                if (product_size != null && product_size.size() > 0) {
//
//                                    List<RecentlyProductSizeItem> list_Recently_ProductSizeItem = new ArrayList<>();
//
//                                    for (int j = 0; j < product_size.size(); j++) {
//
//                                        JsonObject jsonObject_product_size = product_size.get(j).getAsJsonObject();
//
//                                        RecentlyProductSizeItem recentlyProductSizeItem = new RecentlyProductSizeItem();
//
//                                        if (jsonObject_product_size.has("size_id") && !jsonObject_product_size.get("size_id").isJsonNull())
//                                            recentlyProductSizeItem.setSizeId(jsonObject_product_size.get("size_id").getAsString());
//                                        else
//                                            recentlyProductSizeItem.setSizeId("");
//
//                                        if (jsonObject_product_size.has("size_name") && !jsonObject_product_size.get("size_name").isJsonNull())
//                                            recentlyProductSizeItem.setSizeName(jsonObject_product_size.get("size_name").getAsString());
//                                        else
//                                            recentlyProductSizeItem.setSizeName("");
//
//                                        if (jsonObject_product_size.has("size_image") && !jsonObject_product_size.get("size_image").isJsonNull())
//                                            recentlyProductSizeItem.setSizeImage(jsonObject_product_size.get("size_image").getAsString());
//                                        else
//                                            recentlyProductSizeItem.setSizeImage("");
//
//                                        if (jsonObject_product_size.has("size_stock") && !jsonObject_product_size.get("size_stock").isJsonNull())
//                                            recentlyProductSizeItem.setSizeStock(jsonObject_product_size.get("size_stock").getAsString());
//                                        else
//                                            recentlyProductSizeItem.setSizeStock("");
//
//                                        if (jsonObject_product_size.has("size_color") && !jsonObject_product_size.get("size_color").isJsonNull())
//                                            recentlyProductSizeItem.setSizeColor(jsonObject_product_size.get("size_color").getAsString());
//                                        else
//                                            recentlyProductSizeItem.setSizeColor("");
//
//                                        if (jsonObject_product_size.has("size_price") && !jsonObject_product_size.get("size_price").isJsonNull())
//                                            recentlyProductSizeItem.setSizePrice(jsonObject_product_size.get("size_price").getAsString());
//                                        else
//                                            recentlyProductSizeItem.setSizePrice("");
//
//
//                                        JsonArray color;
//
//                                        if (jsonObject_product_size.has("color") && jsonObject_product_size.get("color").isJsonArray())
//
//                                            color = jsonObject_product_size.get("color").getAsJsonArray();
//                                        else
//                                            color = null;
//
//                                        if (color != null && color.size() > 0) {
//
//                                            List<in.mapbazar.mapbazar.Model.RecentlyProduct.ColorItem> list_ColorItem = new ArrayList<>();
//
//                                            for (int k = 0; k < color.size(); k++) {
//
//                                                JsonObject jsonObject_ColorItem = color.get(k).getAsJsonObject();
//
//                                                in.mapbazar.mapbazar.Model.RecentlyProduct.ColorItem colorItem = new in.mapbazar.mapbazar.Model.RecentlyProduct.ColorItem();
//
//                                                if (jsonObject_ColorItem.has("color_id") && !jsonObject_ColorItem.get("color_id").isJsonNull())
//                                                    colorItem.setColorId(jsonObject_ColorItem.get("color_id").getAsString());
//                                                else
//                                                    colorItem.setColorId("");
//
//                                                if (jsonObject_ColorItem.has("color_code") && !jsonObject_ColorItem.get("color_code").isJsonNull())
//                                                    colorItem.setColorCode(jsonObject_ColorItem.get("color_code").getAsString());
//                                                else
//                                                    colorItem.setColorCode("");
//
//                                                if (jsonObject_ColorItem.has("color_image") && !jsonObject_ColorItem.get("color_image").isJsonNull())
//                                                    colorItem.setColorImage(jsonObject_ColorItem.get("color_image").getAsString());
//                                                else
//                                                    colorItem.setColorImage("");
//
//                                                if (jsonObject_ColorItem.has("color_stock") && !jsonObject_ColorItem.get("color_stock").isJsonNull())
//                                                    colorItem.setColorStock(jsonObject_ColorItem.get("color_stock").getAsString());
//                                                else
//                                                    colorItem.setColorStock("");
//
//                                                if (jsonObject_ColorItem.has("color_price") && !jsonObject_ColorItem.get("color_price").isJsonNull())
//                                                    colorItem.setColorPrice(jsonObject_ColorItem.get("color_price").getAsString());
//                                                else
//                                                    colorItem.setColorPrice("");
//
//                                                list_ColorItem.add(colorItem);
//
//                                            }
//
//                                            recentlyProductSizeItem.setColor(list_ColorItem);
//                                        }
//
//                                        list_Recently_ProductSizeItem.add(recentlyProductSizeItem);
//
//                                    }
//
//                                    newProductItem.setProductSize(list_Recently_ProductSizeItem);
//                                }
//
//                                JsonArray product_attribute;
//
//                                if (jsonObject_new_product.has("product_attribute") && jsonObject_new_product.get("product_attribute").isJsonArray())
//
//                                    product_attribute = jsonObject_new_product.get("product_attribute").getAsJsonArray();
//                                else
//                                    product_attribute = null;
//
//                                if (product_attribute != null && product_attribute.size() > 0) {
//
//                                    List<RevcentlyProductAttributeItem> list_Revcently_ProductAttributeItem = new ArrayList<>();
//
//                                    for (int j = 0; j < product_attribute.size(); j++) {
//
//                                        JsonObject jsonObject_product_attribute = product_attribute.get(j).getAsJsonObject();
//
//                                        RevcentlyProductAttributeItem revcentlyProductAttributeItem = new RevcentlyProductAttributeItem();
//
//                                        if (jsonObject_product_attribute.has("attribute_id") && !jsonObject_product_attribute.get("attribute_id").isJsonNull())
//                                            revcentlyProductAttributeItem.setAttributeId(jsonObject_product_attribute.get("attribute_id").getAsString());
//                                        else
//                                            revcentlyProductAttributeItem.setAttributeId("");
//
//                                        if (jsonObject_product_attribute.has("attribute_name") && !jsonObject_product_attribute.get("attribute_name").isJsonNull())
//                                            revcentlyProductAttributeItem.setAttributeName(jsonObject_product_attribute.get("attribute_name").getAsString());
//                                        else
//                                            revcentlyProductAttributeItem.setAttributeName("");
//
//                                        if (jsonObject_product_attribute.has("attribute_value") && !jsonObject_product_attribute.get("attribute_value").isJsonNull())
//                                            revcentlyProductAttributeItem.setAttributeValue(jsonObject_product_attribute.get("attribute_value").getAsString());
//                                        else
//                                            revcentlyProductAttributeItem.setAttributeValue("");
//
//                                        list_Revcently_ProductAttributeItem.add(revcentlyProductAttributeItem);
//
//                                    }
//
//                                    newProductItem.setProductAttribute(list_Revcently_ProductAttributeItem);
//                                }
//
//                                list_RecentlyProductItem.add(newProductItem);
//                            }
//
//                            if (list_RecentlyProductItem.size() > 0) {
//                                ly_recentview.setVisibility(View.VISIBLE);
//                                displayForRecentlyView(list_RecentlyProductItem);
//                            } else {
//                                ly_recentview.setVisibility(View.GONE);
//                            }
//
//                        } else {
//                            //onFailRequest(page_no);
//
//                            ly_recentview.setVisibility(View.GONE);
//                        }
//
//                    } else if (resp.get("status").getAsString().equals("false")) {
//
//
//                        int Isactive=0;
//                        if (resp.has("Isactive") && !resp.get("Isactive").isJsonNull()) {
//                            Isactive = resp.get("Isactive").getAsInt();
//                        }
//
//                        if (Isactive == 0) {
//                            Common.AccountLock(ActivityRecentlyProductDetails.this);
//                        }
//                        else if (resp.has("message") && !resp.get("message").isJsonNull()) {
//                            Common.Errordialog(ActivityRecentlyProductDetails.this,resp.get("message").toString());
//                        }
//
//                    } else if (resp.get("status").getAsString().equals("failed")) {
//
//
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<JsonObject> call, Throwable t) {
//
//                String message = "";
//                if (t instanceof SocketTimeoutException) {
//                    message = "Socket Time out. Please try again.";
//                    Common.ShowHttpErrorMessage(ActivityRecentlyProductDetails.this, message);
//                }
//            }
//        });
//    }
//
//    public void displayForMatchView(List<MatchProductItem> matchProductItemList) {
//
//        RecyclerView.LayoutManager horizontalLayoutManagaer = new LinearLayoutManager(ActivityRecentlyProductDetails.this, LinearLayoutManager.HORIZONTAL, false);
//        match_product_recycler.setLayoutManager(horizontalLayoutManagaer);
//        match_product_recycler.setItemAnimator(new DefaultItemAnimator());
//        match_product_recycler.setHasFixedSize(true);
//
//        if (matchProductItemList != null && matchProductItemList.size() > 0) {
//            //Adapter for New Products
//            MatchWithproductAdapter newproduct_adpter = new MatchWithproductAdapter(ActivityRecentlyProductDetails.this, matchProductItemList);
//            match_product_recycler.setAdapter(newproduct_adpter);
//
//        }
//    }
//    int quatity;
//    public void displayForRecentlyView(List<RecentlyProductItem> recentlyProductItemList) {
//
//        RecyclerView.LayoutManager horizontalLayoutManagaer = new LinearLayoutManager(ActivityRecentlyProductDetails.this, LinearLayoutManager.HORIZONTAL, false);
//        recent_prduct_recycle.setLayoutManager(horizontalLayoutManagaer);
//        recent_prduct_recycle.setItemAnimator(new DefaultItemAnimator());
//        recent_prduct_recycle.setHasFixedSize(true);
//
//        if (recentlyProductItemList != null && recentlyProductItemList.size() > 0) {
//            //Adapter for New Products
//            final RecentproductAdapter newproduct_adpter = new RecentproductAdapter(ActivityRecentlyProductDetails.this, recentlyProductItemList,null);
//            newproduct_adpter.setOnItemMinusClick(new RecentproductAdapter.OnItemMinsClick() {
//                @Override
//                public void onItemClick(View view, RecentlyProductItem obj, int position) {
//
//                    int qty = 0;
//                    if (!obj.getQuantity().equals(""))
//                        qty = Integer.parseInt(obj.getQuantity());
//
//                    if (qty != 1) {
//                        quatity = qty - 1;
//                        obj.setQuantity("" + quatity);
//                        newproduct_adpter.notifyDataSetChanged();
//
//                      //  requestStockCheck(obj, obj.getProductId(), obj.getOrder_id(), "" + quatity, obj.getSizeId(), obj.getColorCode());
//                    }
//
//
//                }
//            });
//
//            newproduct_adpter.setOnItemPlusClick(new RecentproductAdapter.OnItemPuchClick() {
//                @Override
//                public void onItemClick(View view, RecentlyProductItem obj, int position) {
//
//                    int qty = 0;
//                    if (!obj.getQuantity().equals(""))
//                        qty = Integer.parseInt(obj.getQuantity());
//
//                    quatity = qty + 1;
//                    obj.setQuantity("" + quatity);
//                    newproduct_adpter.notifyDataSetChanged();
////                    if (quatity == 1) {
////                        //need to be change
////                        requestCheck_stock_for_add_to_cart(obj, "1", "1");
////                    } else {
////                        requestStockCheck(obj, obj.getProductId(), obj.getOrder_id(), "" + quatity, obj.getSizeId(), obj.getColorCode());
////
////                    }
//                }
//            });
//
//            recent_prduct_recycle.setAdapter(newproduct_adpter);
//
//        }
//    }
//
//    @Override
//    public void onBackPressed() {
//        //super.onBackPressed();
//        if (deliveryShippingFragment != null) {
//            getSupportFragmentManager().beginTransaction().remove(deliveryShippingFragment);
//            deliveryShippingFragment = null;
//            layoutCartItem.setVisibility(View.GONE);
//        }else {
//            ly_back.performClick();
//        }
//
//    }
//
//    public class RecentlyProductViewPagerAdapter extends PagerAdapter {
//        // Declare Variables
//        Context context;
//        String Image_path;
//        List<in.mapbazar.mapbazar.Model.RecentlyProduct.ProductImageItem> banner;
//        // ViewHolder holder;
//        LayoutInflater inflater;
//
//        // int positiondata;
//
//        public RecentlyProductViewPagerAdapter(Context context, List<in.mapbazar.mapbazar.Model.RecentlyProduct.ProductImageItem> banner) {
//            this.context = context;
//            this.banner = banner;
//
//            // this.positiondata = position;
//        }
//
//        @Override
//        public int getCount() {
//            if(banner != null)
//            return banner.size();
//            else
//                return 0;
//        }
//
//        @Override
//        public boolean isViewFromObject(View view, Object object) {
//            return view == ((LinearLayout) object);
//        }
//
//        @Override
//        public Object instantiateItem(ViewGroup container, int position) {
//
//            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            View itemView = inflater.inflate(R.layout.custom_product_viewpager, container, false);
//
//
//            // Locate the ImageView in viewpager_item.xml
//            final ImageView pager_image = (ImageView) itemView.findViewById(R.id.pager_image);
//
//            in.mapbazar.mapbazar.Model.RecentlyProduct.ProductImageItem imageItem = banner.get(position);
//
//            String temp = "" + Url.product_url + imageItem.getImage();
//
//            boolean isWhitespace = Utils.containsWhitespace(temp);
//            if (isWhitespace)
//                temp = temp.replaceAll(" ", "%20");
//
//            if (!temp.equalsIgnoreCase("null") && !temp.equals("")) {
//                Picasso.with(context).load(temp).into(pager_image);
//            }
//
//
//            pager_image.setId(position);
//            pager_image.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                    Intent i = new Intent(context, GuestLoginActivity.class);
//                    i.putExtra("RecentProductItem", productItem);
//                    i.putExtra("position", v.getId());
//                    context.startActivity(i);
//
//                }
//            });
//
//            // Add viewpager_item.xml to ViewPager
//            ((ViewPager) container).addView(itemView);
//
//
//            return itemView;
//        }
//
//
//        @Override
//        public void destroyItem(ViewGroup container, int position, Object object) {
//            // Remove viewpager_item.xml from ViewPager
//            ((ViewPager) container).removeView((LinearLayout) object);
//
//        }
//
//    }
//
//    private void requestViewProduct() {
//
//        API api = RestAdapter.createAPI();
//
//        Call<JsonObject> callBooking = api.user_view_product(sPref.getString("uid", ""), productItem.getProductId());
//
//        callBooking.enqueue(new Callback<JsonObject>() {
//            @Override
//            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
//                JsonObject resp = response.body();
//                Log.e(Common.TAG, "onResponse-subcategory" + resp);
//                if (resp != null) {
//                    if (resp.get("status").getAsString().equals("success")) {
//
//                        JsonArray recent_product;
//
//                        if (resp.has("recent_product") && resp.get("recent_product").isJsonArray())
//
//                            recent_product = resp.get("recent_product").getAsJsonArray();
//                        else
//                            recent_product = null;
//
//                        if (recent_product != null && recent_product.size() > 0) {
//
//                            list_RecentlyProductItem = new ArrayList<>();
//
//                            for (int i = 0; i < recent_product.size(); i++) {
//
//                                JsonObject jsonObject_new_product = recent_product.get(i).getAsJsonObject();
//
//                                RecentlyProductItem newProductItem = new RecentlyProductItem();
//
//                                if (jsonObject_new_product.has("product_id") && !jsonObject_new_product.get("product_id").isJsonNull())
//                                    newProductItem.setProductId(jsonObject_new_product.get("product_id").getAsString());
//                                else
//                                    newProductItem.setProductId("");
//
//                                if (jsonObject_new_product.has("product_name") && !jsonObject_new_product.get("product_name").isJsonNull())
//                                    newProductItem.setProductName(jsonObject_new_product.get("product_name").getAsString());
//                                else
//                                    newProductItem.setProductName("");
//                                if (jsonObject_new_product.has("product_name_hindi") && !jsonObject_new_product.get("product_name_hindi").isJsonNull())
//                                    newProductItem.setProduct_name_hindi(jsonObject_new_product.get("product_name_hindi").getAsString());
//                                else
//                                    newProductItem.setProduct_name_hindi("");
//                                if (jsonObject_new_product.has("product_status") && !jsonObject_new_product.get("product_status").isJsonNull())
//                                    newProductItem.setProductStatus(jsonObject_new_product.get("product_status").getAsString());
//                                else
//                                    newProductItem.setProductStatus("");
//
//                                if (jsonObject_new_product.has("product_primary_image") && !jsonObject_new_product.get("product_primary_image").isJsonNull())
//                                    newProductItem.setProductPrimaryImage(jsonObject_new_product.get("product_primary_image").getAsString());
//                                else
//                                    newProductItem.setProductPrimaryImage("");
//
//                                if (jsonObject_new_product.has("product_price") && !jsonObject_new_product.get("product_price").isJsonNull())
//                                    newProductItem.setProductPrice(jsonObject_new_product.get("product_price").getAsString());
//                                else
//                                    newProductItem.setProductPrice("");
//
//                                if (jsonObject_new_product.has("product_old_price") && !jsonObject_new_product.get("product_old_price").isJsonNull())
//                                    newProductItem.setProductOldPrice(jsonObject_new_product.get("product_old_price").getAsString());
//                                else
//                                    newProductItem.setProductOldPrice("");
//
//                                if (jsonObject_new_product.has("product_stock") && !jsonObject_new_product.get("product_stock").isJsonNull())
//                                    newProductItem.setProductStock(jsonObject_new_product.get("product_stock").getAsString());
//                                else
//                                    newProductItem.setProductStock("");
//
//                                if (jsonObject_new_product.has("product_description") && !jsonObject_new_product.get("product_description").isJsonNull())
//                                    newProductItem.setProductDescription(jsonObject_new_product.get("product_description").getAsString());
//                                else
//                                    newProductItem.setProductDescription("");
//
//                                if (jsonObject_new_product.has("isProductCustomizable") && !jsonObject_new_product.get("isProductCustomizable").isJsonNull())
//                                    newProductItem.setIsProductCustomizable(jsonObject_new_product.get("isProductCustomizable").getAsString());
//                                else
//                                    newProductItem.setIsProductCustomizable("");
//
//                                if (jsonObject_new_product.has("product_brand") && !jsonObject_new_product.get("product_brand").isJsonNull())
//                                    newProductItem.setProductBrand(jsonObject_new_product.get("product_brand").getAsString());
//                                else
//                                    newProductItem.setProductBrand("");
//
//                                if (jsonObject_new_product.has("product_color") && !jsonObject_new_product.get("product_color").isJsonNull())
//                                    newProductItem.setProductColor(jsonObject_new_product.get("product_color").getAsString());
//                                else
//                                    newProductItem.setProductColor("");
//
//                                if (jsonObject_new_product.has("favorite_flag") && !jsonObject_new_product.get("favorite_flag").isJsonNull())
//                                    newProductItem.setFavoriteFlag(jsonObject_new_product.get("favorite_flag").getAsInt());
//                                else
//                                    newProductItem.setFavoriteFlag(0);
//
//
//                                JsonArray product_image;
//
//                                if (jsonObject_new_product.has("product_image") && jsonObject_new_product.get("product_image").isJsonArray())
//
//                                    product_image = jsonObject_new_product.get("product_image").getAsJsonArray();
//                                else
//                                    product_image = null;
//
//                                if (product_image != null && product_image.size() > 0) {
//
//                                    List<in.mapbazar.mapbazar.Model.RecentlyProduct.ProductImageItem> list_ProductImageItem = new ArrayList<>();
//
//                                    for (int j = 0; j < product_image.size(); j++) {
//
//                                        JsonObject jsonObject_product_image = product_image.get(j).getAsJsonObject();
//
//                                        in.mapbazar.mapbazar.Model.RecentlyProduct.ProductImageItem productImageItem = new in.mapbazar.mapbazar.Model.RecentlyProduct.ProductImageItem();
//
//                                        if (jsonObject_product_image.has("image") && !jsonObject_product_image.get("image").isJsonNull())
//                                            productImageItem.setImage(jsonObject_product_image.get("image").getAsString());
//                                        else
//                                            productImageItem.setImage("");
//
//                                        list_ProductImageItem.add(productImageItem);
//
//                                    }
//
//                                    newProductItem.setProductImage(list_ProductImageItem);
//                                }
//
//                                JsonArray product_size;
//
//                                if (jsonObject_new_product.has("product_size") && jsonObject_new_product.get("product_size").isJsonArray())
//
//                                    product_size = jsonObject_new_product.get("product_size").getAsJsonArray();
//                                else
//                                    product_size = null;
//
//                                if (product_size != null && product_size.size() > 0) {
//
//                                    List<RecentlyProductSizeItem> list_Recently_ProductSizeItem = new ArrayList<>();
//
//                                    for (int j = 0; j < product_size.size(); j++) {
//
//                                        JsonObject jsonObject_product_size = product_size.get(j).getAsJsonObject();
//
//                                        RecentlyProductSizeItem recentlyProductSizeItem = new RecentlyProductSizeItem();
//
//                                        if (jsonObject_product_size.has("size_id") && !jsonObject_product_size.get("size_id").isJsonNull())
//                                            recentlyProductSizeItem.setSizeId(jsonObject_product_size.get("size_id").getAsString());
//                                        else
//                                            recentlyProductSizeItem.setSizeId("");
//
//                                        if (jsonObject_product_size.has("size_name") && !jsonObject_product_size.get("size_name").isJsonNull())
//                                            recentlyProductSizeItem.setSizeName(jsonObject_product_size.get("size_name").getAsString());
//                                        else
//                                            recentlyProductSizeItem.setSizeName("");
//
//                                        if (jsonObject_product_size.has("size_image") && !jsonObject_product_size.get("size_image").isJsonNull())
//                                            recentlyProductSizeItem.setSizeImage(jsonObject_product_size.get("size_image").getAsString());
//                                        else
//                                            recentlyProductSizeItem.setSizeImage("");
//
//                                        if (jsonObject_product_size.has("size_stock") && !jsonObject_product_size.get("size_stock").isJsonNull())
//                                            recentlyProductSizeItem.setSizeStock(jsonObject_product_size.get("size_stock").getAsString());
//                                        else
//                                            recentlyProductSizeItem.setSizeStock("");
//
//                                        if (jsonObject_product_size.has("size_color") && !jsonObject_product_size.get("size_color").isJsonNull())
//                                            recentlyProductSizeItem.setSizeColor(jsonObject_product_size.get("size_color").getAsString());
//                                        else
//                                            recentlyProductSizeItem.setSizeColor("");
//
//                                        if (jsonObject_product_size.has("size_price") && !jsonObject_product_size.get("size_price").isJsonNull())
//                                            recentlyProductSizeItem.setSizePrice(jsonObject_product_size.get("size_price").getAsString());
//                                        else
//                                            recentlyProductSizeItem.setSizePrice("");
//
//
//                                        JsonArray color;
//
//                                        if (jsonObject_product_size.has("color") && jsonObject_product_size.get("color").isJsonArray())
//
//                                            color = jsonObject_product_size.get("color").getAsJsonArray();
//                                        else
//                                            color = null;
//
//                                        if (color != null && color.size() > 0) {
//
//                                            List<in.mapbazar.mapbazar.Model.RecentlyProduct.ColorItem> list_ColorItem = new ArrayList<>();
//
//                                            for (int k = 0; k < color.size(); k++) {
//
//                                                JsonObject jsonObject_ColorItem = color.get(k).getAsJsonObject();
//
//                                                in.mapbazar.mapbazar.Model.RecentlyProduct.ColorItem colorItem = new in.mapbazar.mapbazar.Model.RecentlyProduct.ColorItem();
//
//                                                if (jsonObject_ColorItem.has("color_id") && !jsonObject_ColorItem.get("color_id").isJsonNull())
//                                                    colorItem.setColorId(jsonObject_ColorItem.get("color_id").getAsString());
//                                                else
//                                                    colorItem.setColorId("");
//
//                                                if (jsonObject_ColorItem.has("color_code") && !jsonObject_ColorItem.get("color_code").isJsonNull())
//                                                    colorItem.setColorCode(jsonObject_ColorItem.get("color_code").getAsString());
//                                                else
//                                                    colorItem.setColorCode("");
//
//                                                if (jsonObject_ColorItem.has("color_image") && !jsonObject_ColorItem.get("color_image").isJsonNull())
//                                                    colorItem.setColorImage(jsonObject_ColorItem.get("color_image").getAsString());
//                                                else
//                                                    colorItem.setColorImage("");
//
//                                                if (jsonObject_ColorItem.has("color_stock") && !jsonObject_ColorItem.get("color_stock").isJsonNull())
//                                                    colorItem.setColorStock(jsonObject_ColorItem.get("color_stock").getAsString());
//                                                else
//                                                    colorItem.setColorStock("");
//
//                                                if (jsonObject_ColorItem.has("color_price") && !jsonObject_ColorItem.get("color_price").isJsonNull())
//                                                    colorItem.setColorPrice(jsonObject_ColorItem.get("color_price").getAsString());
//                                                else
//                                                    colorItem.setColorPrice("");
//
//                                                list_ColorItem.add(colorItem);
//
//                                            }
//
//                                            recentlyProductSizeItem.setColor(list_ColorItem);
//                                        }
//
//                                        list_Recently_ProductSizeItem.add(recentlyProductSizeItem);
//
//                                    }
//
//                                    newProductItem.setProductSize(list_Recently_ProductSizeItem);
//                                }
//
//                                JsonArray product_attribute;
//
//                                if (jsonObject_new_product.has("product_attribute") && jsonObject_new_product.get("product_attribute").isJsonArray())
//
//                                    product_attribute = jsonObject_new_product.get("product_attribute").getAsJsonArray();
//                                else
//                                    product_attribute = null;
//
//                                if (product_attribute != null && product_attribute.size() > 0) {
//
//                                    List<RevcentlyProductAttributeItem> list_Revcently_ProductAttributeItem = new ArrayList<>();
//
//                                    for (int j = 0; j < product_attribute.size(); j++) {
//
//                                        JsonObject jsonObject_product_attribute = product_attribute.get(j).getAsJsonObject();
//
//                                        RevcentlyProductAttributeItem revcentlyProductAttributeItem = new RevcentlyProductAttributeItem();
//
//                                        if (jsonObject_product_attribute.has("attribute_id") && !jsonObject_product_attribute.get("attribute_id").isJsonNull())
//                                            revcentlyProductAttributeItem.setAttributeId(jsonObject_product_attribute.get("attribute_id").getAsString());
//                                        else
//                                            revcentlyProductAttributeItem.setAttributeId("");
//
//                                        if (jsonObject_product_attribute.has("attribute_name") && !jsonObject_product_attribute.get("attribute_name").isJsonNull())
//                                            revcentlyProductAttributeItem.setAttributeName(jsonObject_product_attribute.get("attribute_name").getAsString());
//                                        else
//                                            revcentlyProductAttributeItem.setAttributeName("");
//
//                                        if (jsonObject_product_attribute.has("attribute_value") && !jsonObject_product_attribute.get("attribute_value").isJsonNull())
//                                            revcentlyProductAttributeItem.setAttributeValue(jsonObject_product_attribute.get("attribute_value").getAsString());
//                                        else
//                                            revcentlyProductAttributeItem.setAttributeValue("");
//
//                                        list_Revcently_ProductAttributeItem.add(revcentlyProductAttributeItem);
//
//                                    }
//
//                                    newProductItem.setProductAttribute(list_Revcently_ProductAttributeItem);
//                                }
//
//                                list_RecentlyProductItem.add(newProductItem);
//                            }
//
//                            if (list_RecentlyProductItem.size() > 0) {
//                                ly_recentview.setVisibility(View.VISIBLE);
//                                displayForRecentlyView(list_RecentlyProductItem);
//                            } else {
//                                ly_recentview.setVisibility(View.GONE);
//                            }
//
//                        } else {
//                            //onFailRequest(page_no);
//
//                            ly_recentview.setVisibility(View.GONE);
//                        }
//
//                    } else if (resp.get("status").getAsString().equals("false")) {
//
//
//                        int Isactive=0;
//                        if (resp.has("Isactive") && !resp.get("Isactive").isJsonNull()) {
//                            Isactive = resp.get("Isactive").getAsInt();
//                        }
//
//                        if (Isactive == 0) {
//                            Common.AccountLock(ActivityRecentlyProductDetails.this);
//                        }
//                        else if (resp.has("message") && !resp.get("message").isJsonNull()) {
//                            Common.Errordialog(ActivityRecentlyProductDetails.this,resp.get("message").toString());
//                        }
//                    } else if (resp.get("status").getAsString().equals("failed")) {
//
//
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<JsonObject> call, Throwable t) {
//
//                String message = "";
//                if (t instanceof SocketTimeoutException) {
//                    message = "Socket Time out. Please try again.";
//                    Common.ShowHttpErrorMessage(ActivityRecentlyProductDetails.this, message);
//                }
//            }
//        });
//    }
//
//    private void requestProductFavrite() {
//
//        API api = RestAdapter.createAPI();
//
//        Call<JsonObject> callBooking = api.product_favorite(sPref.getString("uid", ""), productItem.getProductId());
//
//        callBooking.enqueue(new Callback<JsonObject>() {
//            @Override
//            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
//                JsonObject resp = response.body();
//                Log.e(Common.TAG, "onResponse-subcategory" + resp);
//                if (resp != null) {
//                    if (resp.get("status").getAsString().equals("success")) {
//
//                        if (resp.has("favorite_flag") && !resp.get("favorite_flag").isJsonNull())
//                            isproductfavorite = resp.get("favorite_flag").getAsInt();
//                        else
//                            isproductfavorite = 0;
//
//
//                        if (isproductfavorite == 1) {
//                            updateHeartButton(true);
//                        } else {
//                            updateHeartButton(false);
//                        }
//
//                    } else if (resp.get("status").getAsString().equals("false")) {
//
//
//                        int Isactive=0;
//                        if (resp.has("Isactive") && !resp.get("Isactive").isJsonNull()) {
//                            Isactive = resp.get("Isactive").getAsInt();
//                        }
//
//                        if (Isactive == 0) {
//                            Common.AccountLock(ActivityRecentlyProductDetails.this);
//                        }
//                        else if (resp.has("message") && !resp.get("message").isJsonNull()) {
//                            Common.Errordialog(ActivityRecentlyProductDetails.this,resp.get("message").toString());
//                        }
//
//                    } else if (resp.get("status").getAsString().equals("failed")) {
//
//
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<JsonObject> call, Throwable t) {
//
//                String message = "";
//                if (t instanceof SocketTimeoutException) {
//                    message = "Socket Time out. Please try again.";
//                    Common.ShowHttpErrorMessage(ActivityRecentlyProductDetails.this, message);
//                }
//            }
//        });
//    }
//
//    private void requestUserFavrite() {
//
//        API api = RestAdapter.createAPI();
//
//        Call<JsonObject> callBooking = api.user_favorite(sPref.getString("uid", ""), productItem.getProductId());
//
//        callBooking.enqueue(new Callback<JsonObject>() {
//            @Override
//            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
//                JsonObject resp = response.body();
//                Log.e(Common.TAG, "onResponse-subcategory" + resp);
//                if (resp != null) {
//                    if (resp.get("status").getAsString().equals("success")) {
//
//                        if (resp.has("message") && !resp.get("message").isJsonNull()) {
//                            String Message = resp.get("message").getAsString();
//
//                            Toast.makeText(ActivityRecentlyProductDetails.this, Message, Toast.LENGTH_SHORT).show();
//                        }
//
//                        if (resp.has("total_favorite") && !resp.get("total_favorite").isJsonNull()) {
//                            int total_favorite = resp.get("total_favorite").getAsInt();
//
//                            SharedPreferences.Editor sh = sPref.edit();
//                            sh.putInt("wishlist", total_favorite);
//                            sh.commit();
//
//                            Intent iReceiver = new Intent("com.megastore.wishlist");
//                            sendBroadcast(iReceiver);
//
//                        }
//
//                        isproductfavorite = 1;
//                        img_favorite.setImageResource(R.drawable.favorite);
//
//                    } else if (resp.get("status").getAsString().equals("false")) {
//
//                        int Isactive=0;
//                        if (resp.has("Isactive") && !resp.get("Isactive").isJsonNull()) {
//                            Isactive = resp.get("Isactive").getAsInt();
//                        }
//
//                        if (Isactive == 0) {
//                            Common.AccountLock(ActivityRecentlyProductDetails.this);
//                        }
//                        else if (resp.has("message") && !resp.get("message").isJsonNull()) {
//                            Common.Errordialog(ActivityRecentlyProductDetails.this,resp.get("message").toString());
//                        }
//
//                    } else if (resp.get("status").getAsString().equals("failed")) {
//
//
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<JsonObject> call, Throwable t) {
//
//                String message = "";
//                if (t instanceof SocketTimeoutException) {
//                    message = "Socket Time out. Please try again.";
//                    Common.ShowHttpErrorMessage(ActivityRecentlyProductDetails.this, message);
//                }
//            }
//        });
//    }
//
//    private void requestRemoveFavrite() {
//
//        API api = RestAdapter.createAPI();
//
//        Call<JsonObject> callBooking = api.remove_user_favorite(sPref.getString("uid", ""), productItem.getProductId());
//
//        callBooking.enqueue(new Callback<JsonObject>() {
//            @Override
//            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
//                JsonObject resp = response.body();
//                Log.e(Common.TAG, "onResponse-subcategory" + resp);
//                if (resp != null) {
//                    if (resp.get("status").getAsString().equals("success")) {
//
//                        if (resp.has("total_favorite") && !resp.get("total_favorite").isJsonNull()) {
//                            int total_favorite = resp.get("total_favorite").getAsInt();
//
//                            SharedPreferences.Editor sh = sPref.edit();
//                            sh.putInt("wishlist", total_favorite);
//                            sh.commit();
//
//                            Intent iReceiver = new Intent("com.megastore.wishlist");
//                            sendBroadcast(iReceiver);
//
//                        }
//
//                        isproductfavorite = 0;
//                        img_favorite.setImageResource(R.drawable.unfavorite);
//
//                    } else if (resp.get("status").getAsString().equals("false")) {
//
//
//                        if (resp.has("error code") && resp.get("error code").getAsString().equals("1")) {
//
//                            //User Inactive
//                               /* Intent logInt = new Intent(ActivitySubCategory.this, LoginRegisterActivity.class);
//                                logInt.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                logInt.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                                logInt.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                startActivity(logInt);*/
//                        } else {
//
//                            String errorCode = "";
//                            if (resp.has("error code")) {
//                                errorCode = resp.get("error code").getAsString();
//                            } else if (resp.has("error_code")) {
//                                errorCode = resp.get("error_code").getAsString();
//                            }
//                            Common.showMkError(ActivityRecentlyProductDetails.this, errorCode);
//                        }
//
//                    } else if (resp.get("status").getAsString().equals("failed")) {
//
//
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<JsonObject> call, Throwable t) {
//
//                String message = "";
//                if (t instanceof SocketTimeoutException) {
//                    message = "Socket Time out. Please try again.";
//                    Common.ShowHttpErrorMessage(ActivityRecentlyProductDetails.this, message);
//                }
//            }
//        });
//    }
//
//    private void updateHeartButton(final boolean flag) {
//
//
//        AnimatorSet animatorSet = new AnimatorSet();
//
//        ObjectAnimator rotationAnim = ObjectAnimator.ofFloat(img_favorite, "rotation", 0f, 360f);
//        rotationAnim.setDuration(300);
//        rotationAnim.setInterpolator(Utils.ACCELERATE_INTERPOLATOR);
//
//        ObjectAnimator bounceAnimX = ObjectAnimator.ofFloat(img_favorite, "scaleX", 0.2f, 1f);
//        bounceAnimX.setDuration(300);
//        bounceAnimX.setInterpolator(Utils.OVERSHOOT_INTERPOLATOR);
//
//        ObjectAnimator bounceAnimY = ObjectAnimator.ofFloat(img_favorite, "scaleY", 0.2f, 1f);
//        bounceAnimY.setDuration(300);
//        bounceAnimY.setInterpolator(Utils.OVERSHOOT_INTERPOLATOR);
//        bounceAnimY.addListener(new AnimatorListenerAdapter() {
//            @Override
//            public void onAnimationStart(Animator animation) {
//
//                if (flag) {
//                    img_favorite.setImageResource(R.drawable.favorite);
//                } else {
//                    img_favorite.setImageResource(R.drawable.unfavorite);
//                }
//
//            }
//
//        });
//
//        animatorSet.play(rotationAnim);
//        animatorSet.play(bounceAnimX).with(bounceAnimY).after(rotationAnim);
//
//        animatorSet.start();
//
//
//    }
//
//    private void GetJsonObject() {
//
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
//        product.addProperty("color", str_colorcode);
//        product.addProperty("isFormSelected", 0);
//        product.addProperty("price", productItem.getProductPrice());
//        product.addProperty("product_id", productItem.getProductId());
//        product.addProperty("quantity", 1);
//        product.addProperty("size_id", str_sizeid);
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
//
//    private void requestCheck_stock() {
//
//        ProgressDialog.show();
//        if (productItem.getSelectedProductItem() == -1) {
//            str_sizeid =  productItem.getProductAttribute().get(0).getAttributeId();
//        }else {
//            str_sizeid =  productItem.getProductAttribute().get(productItem.getSelectedProductItem()).getAttributeId();
//
//        }
//        API api = RestAdapter.createAPI();
//        Call<JsonObject> callBooking = api.stock_check(sPref.getString("uid", ""), productItem.getProductId(), null, "plus", str_sizeid, str_colorcode);
//        callBooking.enqueue(new Callback<JsonObject>() {
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
////                        if (jsonObject.has("stock_flag") && !jsonObject.get("stock_flag").isJsonNull()) {
////                            int stock_flag = jsonObject.get("stock_flag").getAsInt();
////
////                            if (stock_flag == 1) {
//                                GetJsonObject();
////                            }
////                        }
//
//
//                    } else {
//                        if (ProgressDialog != null && ProgressDialog.isShowing())
//                            ProgressDialog.dismiss();
//                      /*  {"status":"false","stock_flag":0,"Isactive":"1"}*/
//
//                        if (jsonObject.has("stock_flag") && !jsonObject.get("stock_flag").isJsonNull()) {
//                            int stock_flag = jsonObject.get("stock_flag").getAsInt();
//
//                            if (stock_flag == 0) {
//                                ErrorMessage(getResources().getString(R.string.outofstock));
//                            }
//                        }
//
//                        int Isactive=0;
//                        if (jsonObject.has("Isactive") && !jsonObject.get("Isactive").isJsonNull()) {
//                            Isactive = jsonObject.get("Isactive").getAsInt();
//                        }
//
//                        if (Isactive == 0) {
//                            Common.AccountLock(ActivityRecentlyProductDetails.this);
//                        }
//                        else if (jsonObject.has("message") && !jsonObject.get("message").isJsonNull()) {
//                            Common.Errordialog(ActivityRecentlyProductDetails.this,jsonObject.get("message").toString());
//                        }
//
//                    }
//
//                } else {
//
//                    if (ProgressDialog != null && ProgressDialog.isShowing())
//                        ProgressDialog.dismiss();
//
//                    ErrorMessage(getString(R.string.error));
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
//                    Common.ShowHttpErrorMessage(ActivityRecentlyProductDetails.this, message);
//                }
//            }
//        });
//
//    }
//
//    private void requestsubmitProductOrder(JsonObject finalobject) {
//
//        //ProgressDialog.show();
//
//        API api = RestAdapter.createAPI();
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
//                            sendBroadcast(iReceiver);
//                        } else {
//                            int count = sPref.getInt("Cart", 0);
//
//                            SharedPreferences.Editor sh = sPref.edit();
//                            sh.putInt("Cart", (count + 1));
//                            sh.commit();
//
//                            Intent iReceiver = new Intent("com.megastore.shopingcart");
//                            sendBroadcast(iReceiver);
//                        }
//
//                        ErrorMessage(getString(R.string.order_successfully));
//
//                    } else {
//
//                        if (ProgressDialog != null && ProgressDialog.isShowing())
//                            ProgressDialog.dismiss();
//
//                        int Isactive=0;
//                        if (jsonObject.has("Isactive") && !jsonObject.get("Isactive").isJsonNull()) {
//                            Isactive = jsonObject.get("Isactive").getAsInt();
//                        }
//
//                        if (Isactive == 0) {
//                            Common.AccountLock(ActivityRecentlyProductDetails.this);
//                        }
//                        else if (jsonObject.has("message") && !jsonObject.get("message").isJsonNull()) {
//                            Common.Errordialog(ActivityRecentlyProductDetails.this,jsonObject.get("message").toString());
//                        }
//
//                    }
//
//                } else {
//
//                    if (ProgressDialog != null && ProgressDialog.isShowing())
//                        ProgressDialog.dismiss();
//
//                    ErrorMessage(getString(R.string.error));
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
//                    Common.ShowHttpErrorMessage(ActivityRecentlyProductDetails.this, message);
//                }
//            }
//        });
//
//    }
//
//    private void ErrorMessage(String Message) {
//        // Common.showInternetInfo(ActivitySignIn.this, "Network is not available");
//        Dialog dialog = new DialogUtils(ActivityRecentlyProductDetails.this).buildDialogMessage(new CallbackMessage() {
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
//
//    @Override
//    protected void onResume() {
//
//        if (!UpdateCartRegistered) {
//            registerReceiver(UpdateCart, new IntentFilter("com.megastore.shopingcart"));
//            UpdateCartRegistered = true;
//        }
//
//        if (sPref.getInt("Cart", 0) == 0) {
//            txt_cart_cout.setText("");
//            layout_cart_cout.setVisibility(View.GONE);
//        } else {
//            txt_cart_cout.setText("" + sPref.getInt("Cart", 0));
//            layout_cart_cout.setVisibility(View.VISIBLE);
//        }
//
//        freeMemory();
//
//        super.onResume();
//    }
//
//    @Override
//    protected void onPause() {
//
//
//        if (UpdateCartRegistered) {
//            unregisterReceiver(UpdateCart);
//            UpdateCartRegistered = false;
//        }
//
//        super.onPause();
//    }
}
