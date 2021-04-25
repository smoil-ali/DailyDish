package com.reactive.dailydish.Fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.reactive.dailydish.Acitivities.KitchenActivity;
import com.reactive.dailydish.Adapter.KitchenAdapter;
import com.reactive.dailydish.Adapter.OrderAdapter;
import com.reactive.dailydish.Adapter.OrderDetailAdapter;
import com.reactive.dailydish.R;
import com.reactive.dailydish.Utils.Constants;
import com.reactive.dailydish.databinding.OrderDetailFragmentBinding;
import com.reactive.dailydish.model.Cart;
import com.reactive.dailydish.model.Checkout;
import com.reactive.dailydish.model.Kitchen;

import java.util.ArrayList;
import java.util.List;

public class OrderDetailFragment extends Fragment {

    final String TAG = OrderDetailFragment.class.getSimpleName();
    OrderDetailFragmentBinding binding;
    Checkout checkout;
    KitchenActivity activity;
    OrderDetailAdapter adapter;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference mRef = firebaseDatabase.getReference(Constants.ORDER);
    Kitchen kitchen;
    ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.order_detail_fragment,container,false);
        activity = (KitchenActivity)getActivity();
        checkout = new Checkout();
        if (getArguments() != null){
            checkout = (Checkout)getArguments().getSerializable(Constants.PARAMS);
        }

        binding.done.setEnabled(false);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Order");
        progressDialog.setMessage("Your Order is loading");
        binding.setVisibility(true);
        binding.recycler.hasFixedSize();
        adapter = new OrderDetailAdapter(getContext(),checkout.getCart());
        binding.recycler.setAdapter(adapter);

        binding.back.setOnClickListener(v -> {
            activity.back(new OrderFragment());
        });

        binding.done.setOnClickListener(v ->{
            checkout.setStatus(Constants.DELIVERED);
            progressDialog.show();
            updateStatus();
        });

        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        getSales();
    }

    void getSales(){
        mRef = firebaseDatabase.getReference(Constants.KITCHEN);
        mRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        kitchen = snapshot.getValue(Kitchen.class);
                        binding.done.setEnabled(true);
                        Log.i(TAG,kitchen.toString());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.i(TAG,error.getMessage());
                    }
                });
    }

    void updateSales(){
        int sales = Integer.parseInt(kitchen.getSale());
        kitchen.setSale(String.valueOf(sales+1));
        mRef = firebaseDatabase.getReference(Constants.KITCHEN);
        mRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .setValue(kitchen)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(activity, "Order Delivered", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i(TAG,e.getMessage());
                        progressDialog.dismiss();
                    }
                });
    }

    void updateStatus(){
        mRef = firebaseDatabase.getReference(Constants.ORDER);
        mRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child(checkout.getId())
                .setValue(checkout)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        updateSales();
                        activity.back(new OrderFragment());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Log.i(TAG,e.getMessage());
                    }
                });
    }
}
