package in.mapbazar.mapbazar;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import java.util.HashMap;

import in.mapbazar.mapbazar.Fragment.DeliveryFragment;
import in.mapbazar.mapbazar.Fragment.DeliveryShippingFragment;

public class ActivityMatchProductDetails extends AppCompatActivity {

    String flag,address,location_id,name,pin,phone,user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery);

        flag=getIntent().getStringExtra("flag");
        address=getIntent().getStringExtra("address");
        location_id=getIntent().getStringExtra("location_id");
        name=getIntent().getStringExtra("name");
        pin=getIntent().getStringExtra("pin");
        phone=getIntent().getStringExtra("phone");
        user_id=getIntent().getStringExtra("user_id");

        if(flag.equals("d"))
        {
            DeliveryFragment deliveryFragment=new DeliveryFragment();
            FragmentManager fragmentManager=getSupportFragmentManager();
            fragmentManager.beginTransaction().add(R.id.container_delivery, deliveryFragment).commit();

        }
        else if(flag.equals("s"))
        {
            Bundle args = new Bundle();
            DeliveryShippingFragment fm = new DeliveryShippingFragment();

            args.putString( "address",address );
            args.putString("location_id", location_id);
            args.putString("name",name);
            args.putString( "pin",pin );
            args.putString( "phone",phone );
            args.putString( "user_id",user_id );
            fm.setArguments(args);
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().add(R.id.container_delivery, fm)
                    .addToBackStack(null).commit();

        }



    }

}
