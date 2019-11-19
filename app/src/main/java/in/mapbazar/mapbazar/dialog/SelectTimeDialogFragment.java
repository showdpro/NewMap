package in.mapbazar.mapbazar.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import in.mapbazar.mapbazar.ActivityPayment;
import in.mapbazar.mapbazar.Adapter.SelectDeliveryTimeAdapter;
import in.mapbazar.mapbazar.R;

import java.util.ArrayList;


/**
 * Created by sonam_gupta on 15/06/19.
 */


public class SelectTimeDialogFragment extends DialogFragment {

    private OnItemClickListener onItemClickListener;


    public void setOnDismissListener(ActivityPayment.OnDismissListener onDismissListener) {
        this.onDismissListener = onDismissListener;
    }

    private ActivityPayment.OnDismissListener onDismissListener;

    public ArrayList<String> getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(ArrayList<String> timeSlot) {
        this.timeSlot = timeSlot;
    }

    private ArrayList<String> timeSlot;



    private RecyclerView recyclerView;
    private SelectDeliveryTimeAdapter selectSizeQuantityAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().setCanceledOnTouchOutside(true);
        getDialog().getWindow().setGravity(Gravity.CENTER | Gravity.CENTER);
        getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        return inflater.inflate(R.layout.select_delivery_time, null);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.size_recycler);
        onItemClickListener = new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                onDismissListener.onDismiss(timeSlot.get(position));
                dismiss();
            }
        };
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        if(timeSlot != null)
        selectSizeQuantityAdapter = new SelectDeliveryTimeAdapter(getContext(), timeSlot,onItemClickListener);

        recyclerView.setAdapter(selectSizeQuantityAdapter );


    }

    @Override
        public void onResume()
        {
            super.onResume();
            Window window = getDialog().getWindow();
        //    window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

            getDialog().getWindow().setGravity(Gravity.CENTER | Gravity.CENTER);

            //TODO:
        }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

}
