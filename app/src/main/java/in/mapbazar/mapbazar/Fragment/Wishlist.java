package in.mapbazar.mapbazar.Fragment;



import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashMap;

import in.mapbazar.mapbazar.Adapter.Wishlist_Adapter;
import in.mapbazar.mapbazar.MainActivity;
import in.mapbazar.mapbazar.R;
import in.mapbazar.mapbazar.util.DatabaseCartHandler;
import in.mapbazar.mapbazar.util.DatabaseHandlerWishList;



public class Wishlist extends Fragment {
    private static String TAG = Wishlist.class.getSimpleName();
    private Bundle savedInstanceState;
    private DatabaseHandlerWishList db_wish;
    RecyclerView rv_wishlist;
    DatabaseCartHandler db_cart;
    ProgressDialog loadingBar;

    public Wishlist() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate( savedInstanceState );

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate( R.layout.fragment_wishlist, container, false );
        setHasOptionsMenu( true );

        ((MainActivity) getActivity()).setTitle( getResources().getString( R.string.wishlist ) );
        rv_wishlist = view.findViewById( R.id.rv_wishlist );
        rv_wishlist.setLayoutManager( new LinearLayoutManager( getActivity() ) );
        db_cart=new DatabaseCartHandler(getActivity());
        //db = new DatabaseHandler(getActivity());
        db_wish = new DatabaseHandlerWishList( getActivity() );

        loadingBar=new ProgressDialog(getActivity());
        loadingBar.setMessage("Loading...");
        loadingBar.setCanceledOnTouchOutside(false);
        ArrayList<HashMap<String, String>> map = db_wish.getWishtableAll();

//        Log.d("cart all ",""+db_cart.getCartAll());

        Wishlist_Adapter adapter = new Wishlist_Adapter( map,getActivity() );
        rv_wishlist.setAdapter( adapter );
        adapter.notifyDataSetChanged();



        return view;
    }



    private void showClearDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder( getActivity() );
        alertDialog.setMessage( getResources().getString( R.string.sure_del ) );
        alertDialog.setNegativeButton( getResources().getString( R.string.cancle ), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        } );
        alertDialog.setPositiveButton( getResources().getString( R.string.yes ), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // clear cart data
                db_wish.clearWishtable();
                ArrayList<HashMap<String, String>> map = db_wish.getWishtableAll();
                Wishlist_Adapter adapter = new Wishlist_Adapter(  map,getActivity() );
                rv_wishlist.setAdapter( adapter );
                adapter.notifyDataSetChanged();

                //updateData();

                dialogInterface.dismiss();
            }
        } );

        alertDialog.show();


    }


    @Override
    public void onResume() {
        super.onResume();
        // register reciver
        getActivity().registerReceiver(mCart, new IntentFilter("Grocery_cart"));
    }

    // broadcast reciver for receive data
    private BroadcastReceiver mCart = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String type = intent.getStringExtra("type");

            if (type.contentEquals("update")) {
                updateData();
            }
        }
    };

    private void updateData() {

    //((MainActivity) getActivity()).setCartCounter("" + db_cart.getCartCount());
    }


    @Override
    public void onPause() {
        super.onPause();
        // unregister reciver
        getActivity().unregisterReceiver(mCart);
    }

}

