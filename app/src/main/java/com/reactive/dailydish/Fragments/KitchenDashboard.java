package com.reactive.dailydish.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.gesture.GestureLibraries;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.reactive.dailydish.Acitivities.DashBoard;
import com.reactive.dailydish.Acitivities.KitchenActivity;
import com.reactive.dailydish.R;
import com.reactive.dailydish.Utils.Constants;
import com.reactive.dailydish.Utils.Helper;
import com.reactive.dailydish.databinding.KitchenDashboardBinding;
import com.reactive.dailydish.model.Kitchen;

public class KitchenDashboard  extends Fragment {

    KitchenDashboardBinding binding;
    KitchenActivity activity;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference mRef = firebaseDatabase.getReference(Constants.KITCHEN);
    Kitchen model;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.kitchen_dashboard,container,false);
        activity =(KitchenActivity)getActivity();
        model = new Kitchen();
        binding.setData(model);
        binding.add.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable(Constants.PARAMS,binding.getData());
            activity.openCreateKitchen(bundle);
        });

        binding.signout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Helper.setLogin(getContext(),false);
            startActivity(new Intent(getContext(), DashBoard.class));
            ((Activity)getContext()).finish();
        });

        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fetchData();
    }


    void fetchData(){
        mRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            Kitchen model = snapshot.getValue(Kitchen.class);
                            binding.setData(model);
                            Glide.with(getActivity())
                                    .load(model.getImage())
                                    .placeholder(R.drawable.picture)
                                    .into(binding.image);
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }


}
