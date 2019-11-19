package in.mapbazar.mapbazar.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import in.mapbazar.mapbazar.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Generate_passwordFragment extends Fragment {


    public Generate_passwordFragment() {
        // Required empty public constructor
    }


    String mob_no="";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_generate_password, container, false);
        mob_no=getArguments().getString("mobile").toString();
        return view;
    }

}
