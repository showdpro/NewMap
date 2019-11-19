package in.mapbazar.mapbazar;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import in.mapbazar.mapbazar.Fragment.WomenFragment;

public class VerficationActivity extends AppCompatActivity {

 TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verfication);
        textView=(TextView)findViewById(R.id.txt);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(VerficationActivity.this,MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP);

                startActivity(intent);
                finish();

            }
        });

        WomenFragment privacyPolicyFragment = new WomenFragment();
        FragmentManager privacyPolicyFragmentManager = getSupportFragmentManager();
        privacyPolicyFragmentManager.beginTransaction()
                .add(R.id.verify_container, privacyPolicyFragment)
                .commit();

    }
}
