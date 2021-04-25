package com.reactive.dailydish.Fragments;

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
import com.reactive.dailydish.Adapter.CustomerMenuAdapter;
import com.reactive.dailydish.Adapter.KitchenAdapter;
import com.reactive.dailydish.Interfaces.ClickListener;
import com.reactive.dailydish.R;
import com.reactive.dailydish.Utils.Constants;
import com.reactive.dailydish.databinding.CustomerKitchenBinding;
import com.reactive.dailydish.databinding.ItemKitchenBinding;
import com.reactive.dailydish.model.Checkout;
import com.reactive.dailydish.model.Kitchen;
import com.reactive.dailydish.model.Like;
import com.reactive.dailydish.model.Menu;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class CustomerKitchen extends Fragment implements ClickListener {

    final String TAG = CustomerKitchen.class.getSimpleName();
    CustomerKitchenBinding binding;
    CustomerActivity activity;
    KitchenAdapter adapter;
    List<Kitchen> list =  new ArrayList<>();
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference mRef = firebaseDatabase.getReference(Constants.KITCHEN);
    Menu menu;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.customer_kitchen,container,false);
        activity = (CustomerActivity) getActivity();
        binding.setVisibility(true);
        binding.placeHolder.getRoot().setVisibility(View.VISIBLE);
        binding.recycler.hasFixedSize();
        adapter = new KitchenAdapter(getContext(),list);
        binding.recycler.setAdapter(adapter);
        adapter.setListener(this);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null){
            menu = (Menu)getArguments().getSerializable(Constants.PARAMS);
            fetchMenuData();
        }else {
            fetchData();
        }

    }

    void fetchData(){
        mRef = firebaseDatabase.getReference(Constants.KITCHEN);
        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot snapshot1:snapshot.getChildren()){
                        Kitchen model = snapshot1.getValue(Kitchen.class);
                        Log.i(TAG,model.toString());
                        list.add(model);
                    }
                    adapter.notifyDataSetChanged();
                    binding.placeHolder.getRoot().setVisibility(View.GONE);
                }else {
                    binding.setVisibility(false);
                    binding.placeHolder.getRoot().setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                binding.placeHolder.getRoot().setVisibility(View.GONE);
                binding.setVisibility(false);
                Log.i(TAG,error.getMessage());
            }
        });
    }

    void fetchMenuData(){
        mRef = firebaseDatabase.getReference(Constants.KITCHEN).child(menu.getKitchenId());
        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    Kitchen model = snapshot.getValue(Kitchen.class);
                    Log.i(TAG,model.toString());
                    list.add(model);
                    adapter.notifyDataSetChanged();
                    binding.placeHolder.getRoot().setVisibility(View.GONE);
                }else {
                    binding.setVisibility(false);
                    binding.placeHolder.getRoot().setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                binding.placeHolder.getRoot().setVisibility(View.GONE);
                binding.setVisibility(false);
                Log.i(TAG,error.getMessage());
            }
        });
    }


    void Like(Kitchen model){
        mRef = firebaseDatabase.getReference(Constants.LIKES);
        mRef.child(model.getChefId())
                .orderByChild("userId").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            Log.i(TAG,snapshot.getChildrenCount()+"");
                            for (DataSnapshot snapshot1:snapshot.getChildren()){
                                Like like = snapshot1.getValue(Like.class);
                                if (like.isStatus()){
                                    like.setStatus(false);
                                    int likes = Integer.parseInt(model.getLikes());
                                    if (likes > 0){
                                        model.setLikes(likes-1+"");
                                    }
                                    updateLikes(like,"Dislike",model,snapshot1.getKey());
                                }else {
                                    like.setStatus(true);
                                    int likes = Integer.parseInt(model.getLikes());
                                    model.setLikes(likes+1+"");
                                    updateLikes(like,"Like",model,snapshot1.getKey());
                                }
                                Log.i(TAG,like.toString());
                            }
                        }else {
                            Log.i(TAG,"Don't Exist");
                            Like like = new Like();
                            like.setStatus(true);
                            like.setUserId(FirebaseAuth.getInstance().getCurrentUser().getUid());
                            int likes = Integer.parseInt(model.getLikes());
                            model.setLikes(likes+1+"");
                            createLikes(like,"Liked",model);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.i(TAG,error.getMessage());

                    }
                });

    }

    @Override
    public void onMenuClick(Menu menu) {

    }

    @Override
    public void onLikeClick(Kitchen model) {
        Like(model);
    }

    @Override
    public void onKitchenClick(Kitchen model) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.PARAMS,model);
        activity.openCustomerKitchenDetailFragment(bundle);
    }

    @Override
    public void onDeleteClick(int pos) {

    }

    @Override
    public void onOrderClick(Checkout checkout) {

    }

    @Override
    public void onChefClick(Menu menu) {
    }

    void updateLikes(Like like,String msg,Kitchen model,String id){
        mRef.child(model.getChefId()).child(id)
                .setValue(like)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        updateKitchenLikes(model,"");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i(TAG,e.getMessage());
                    }
                });
    }

    void createLikes(Like like,String msg,Kitchen model){
        mRef.child(model.getChefId())
                .push().setValue(like)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        updateKitchenLikes(model,"");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i(TAG,e.getMessage());
                    }
                });
    }

    void updateKitchenLikes(Kitchen kitchen,String msg){
        mRef=firebaseDatabase.getReference(Constants.KITCHEN);
        mRef.child(kitchen.getChefId()).setValue(kitchen)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i(TAG,e.getMessage());
                    }
                });
    }
}
