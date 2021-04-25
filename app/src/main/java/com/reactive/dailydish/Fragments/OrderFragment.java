package com.reactive.dailydish.Fragments;

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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.reactive.dailydish.Acitivities.KitchenActivity;
import com.reactive.dailydish.Adapter.MenuAdapter;
import com.reactive.dailydish.Adapter.OrderAdapter;
import com.reactive.dailydish.Interfaces.ClickListener;
import com.reactive.dailydish.R;
import com.reactive.dailydish.Utils.Constants;
import com.reactive.dailydish.databinding.OrderFragmentBinding;
import com.reactive.dailydish.model.Checkout;
import com.reactive.dailydish.model.Kitchen;
import com.reactive.dailydish.model.Menu;

import java.util.ArrayList;
import java.util.List;

public class OrderFragment extends Fragment implements ClickListener  {

    final String TAG = OrderFragment.class.getSimpleName();
    OrderFragmentBinding binding;
    KitchenActivity activity;
    OrderAdapter adapter;
    List<Checkout> list =  new ArrayList<>();
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference mRef = firebaseDatabase.getReference(Constants.ORDER);
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.order_fragment,container,false);

        activity = (KitchenActivity) getActivity();
        binding.setVisibility(true);

        binding.recycler.hasFixedSize();
        adapter = new OrderAdapter(getContext(),list);
        binding.recycler.setAdapter(adapter);
        adapter.setListener(this);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fetchData();
    }

    void fetchData(){
        mRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .orderByChild("status").equalTo(Constants.PENDING)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            for (DataSnapshot snapshot1:snapshot.getChildren()){
                                Checkout checkout = snapshot1.getValue(Checkout.class);
                                checkout.setId(snapshot1.getKey());
                                list.add(checkout);
                            }
                            adapter.notifyDataSetChanged();
                        }else {
                            binding.setVisibility(false);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        binding.setVisibility(false);
                        Log.i(TAG,error.getMessage());
                    }
                });
    }

    @Override
    public void onMenuClick(Menu menu) {

    }

    @Override
    public void onLikeClick(Kitchen model) {

    }

    @Override
    public void onKitchenClick(Kitchen model) {

    }

    @Override
    public void onDeleteClick(int pos) {

    }

    @Override
    public void onOrderClick(Checkout checkout) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.PARAMS,checkout);
        activity.openOrderDetail(bundle);
    }

    @Override
    public void onChefClick(Menu menu) {

    }
}
