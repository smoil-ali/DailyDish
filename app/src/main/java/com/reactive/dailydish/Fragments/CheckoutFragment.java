package com.reactive.dailydish.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
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
import com.reactive.dailydish.Acitivities.CustomerActivity;
import com.reactive.dailydish.R;
import com.reactive.dailydish.Utils.Constants;
import com.reactive.dailydish.Utils.Helper;
import com.reactive.dailydish.databinding.CheckoutFragmentBinding;
import com.reactive.dailydish.model.Cart;
import com.reactive.dailydish.model.Checkout;
import com.reactive.dailydish.model.ProfileModel;

public class CheckoutFragment extends Fragment {

    final String TAG = CheckoutFragment.class.getSimpleName();
    CheckoutFragmentBinding binding;
    CustomerActivity activity;
    Checkout checkout;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference(Constants.ORDER);
    String chefId;
    ProfileModel model;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.checkout_fragment,container,false);
        activity = (CustomerActivity)getActivity();
        binding.setVisibility(true);
        checkout = new Checkout();
        binding.done.setEnabled(false);
        if (getArguments() != null){
            checkout = (Checkout) getArguments().getSerializable(Constants.PARAMS);
        }
        chefId = checkout.getCart().get(0).getMenu().getKitchenId();
        binding.bill.setText(checkout.getGrandTotal());
        binding.back.setOnClickListener(v -> {
            activity.back(new CartFragment());
        });

        binding.done.setOnClickListener(v -> {
            if (isValid()){
                databaseReference = firebaseDatabase.getReference(Constants.ORDER);
                binding.setVisibility(false);
                checkout.setStatus(Constants.PENDING);
                checkout.setModel(model);
                checkout.setAddress(binding.address.getText().toString());
                databaseReference.child(chefId)
                        .push()
                        .setValue(checkout)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                binding.setVisibility(true);
                                Helper.setCartData(null,getContext());
                                activity.binding.bottomNavigation.setSelectedItemId(R.id.home);
                                activity.openFragment(new CustomerMenuFragment());
                                Toast.makeText(activity, "Order Placed!!!", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                binding.setVisibility(true);
                                Log.i(TAG,e.getMessage());
                            }
                        });
            }

        });
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        getUserInfo();
    }

    boolean isValid(){
        if (binding.address.getText().toString().trim().matches("")){
            binding.address.setError("Address Field is Empty");
            binding.address.requestFocus();
            return false;
        }
        return true;
    }

    void getUserInfo(){
        databaseReference = firebaseDatabase.getReference(Constants.USERS).child(Constants.CUSTOMER);
        databaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
        .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                model = snapshot.getValue(ProfileModel.class);
                binding.done.setEnabled(true);
                Log.i(TAG,model.toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(activity, "something went wrong, try again", Toast.LENGTH_SHORT).show();
                Log.i(TAG,error.getMessage());
            }
        });
    }
}
