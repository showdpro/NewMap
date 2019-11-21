package in.mapbazar.mapbazar.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import in.mapbazar.mapbazar.R;

public class SplashImageAdapter extends PagerAdapter {

    Context context ;
    private int[] sliderImageId = new int[]{
            R.drawable.map_welcom, R.drawable.map_welcom, R.drawable.map_welcom,R.drawable.map_welcom
    };

    public SplashImageAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return sliderImageId.length;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull View container, int position) {
        ImageView imageView = new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setImageResource(sliderImageId[position]);
        ((ViewPager) container).addView(imageView, 0);
        return imageView;
    }

    @Override
    public void destroyItem(@NonNull View container, int position, @NonNull Object object) {
        ((ViewPager) container).removeView((ImageView) object);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == ((ImageView) object);


    }
}
