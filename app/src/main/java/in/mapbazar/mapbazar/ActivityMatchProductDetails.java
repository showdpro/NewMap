package in.mapbazar.mapbazar;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import in.mapbazar.mapbazar.Fragment.DeliveryFragment;

public class ActivityMatchProductDetails extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery);

        DeliveryFragment deliveryFragment=new DeliveryFragment();
        FragmentManager fragmentManager=getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.container_delivery, deliveryFragment).commit();


    }

}
