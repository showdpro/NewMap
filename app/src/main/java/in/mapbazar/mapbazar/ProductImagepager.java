package in.mapbazar.mapbazar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.squareup.picasso.Picasso;
import in.mapbazar.mapbazar.Model.ProductListModel.ProductImageItem;
import in.mapbazar.mapbazar.Model.ProductListModel.ProductItem;

import in.mapbazar.mapbazar.Utili.TouchImageView;
import in.mapbazar.mapbazar.Utili.Url;
import in.mapbazar.mapbazar.Utili.Utils;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProductImagepager extends AppCompatActivity {

//    private ProductItem productItem;
//
//    //Shared Preferences
//    private SharedPreferences sPref;
//
//    @BindView(R.id.product_pager)
//    ViewPager product_pager;
//
//    @BindView(R.id.product_indicator)
//    CirclePageIndicator product_indicator;
//
//    @BindView(R.id.ly_back)
//    RelativeLayout ly_back;
//    int position1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_login);
//        ButterKnife.bind(this);
//        Intent intent = getIntent();
//        productItem = (ProductItem) intent.getSerializableExtra("ProductItem");
//        position1 = intent.getExtras().getInt("position");
//
//        sPref = PreferenceManager.getDefaultSharedPreferences(ProductImagepager.this);
//
//        initdata();
    }

//    private void initdata() {
//        ly_back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
//
//        ProductViewPager mCustomPagerAdapter = new ProductViewPager(ProductImagepager.this, productItem.getProductImage());
//        product_pager.setAdapter(mCustomPagerAdapter);
//        product_indicator.setViewPager(product_pager);
//        product_pager.setCurrentItem(position1);
//    }
//
//    public class ProductViewPager extends PagerAdapter {
//
//        Context context;
//        String Image_path;
//        List<ProductImageItem> banner;
//        LayoutInflater inflater;
//
//        public ProductViewPager(Context context, List<ProductImageItem> banner) {
//            this.context = context;
//            this.banner = banner;
//        }
//
//        @Override
//        public int getCount() {
//            return banner.size();
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
//            View itemView = inflater.inflate(R.layout.row_image_viewpager, container, false);
//
//            // Locate the ImageView in viewpager_item.xml
//            final TouchImageView pager_image = (TouchImageView) itemView.findViewById(R.id.pager_image);
//
//            ProductImageItem imageItem = banner.get(position);
//
//            String temp = "" + Url.product_url + imageItem.getImage();
//            boolean isWhitespace = Utils.containsWhitespace(temp);
//            if (isWhitespace)
//                temp = temp.replaceAll(" ", "%20");
//
//            if (!temp.equalsIgnoreCase("null") && !temp.equals("")) {
//                Picasso.with(context).load(temp).into(pager_image);
//            }
//
//            // Add viewpager_item.xml to ViewPager
//            ((ViewPager) container).addView(itemView);
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
}
