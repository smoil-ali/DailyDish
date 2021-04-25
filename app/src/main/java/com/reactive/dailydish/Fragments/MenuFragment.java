package com.reactive.dailydish.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.reactive.dailydish.Acitivities.KitchenActivity;
import com.reactive.dailydish.Adapter.MenuAdapter;
import com.reactive.dailydish.Interfaces.ClickListener;
import com.reactive.dailydish.R;
import com.reactive.dailydish.Utils.Constants;
import com.reactive.dailydish.databinding.ItemKitchenBinding;
import com.reactive.dailydish.databinding.MenuListBinding;
import com.reactive.dailydish.model.Checkout;
import com.reactive.dailydish.model.Kitchen;
import com.reactive.dailydish.model.Menu;

import java.util.ArrayList;
import java.util.List;

public class MenuFragment extends Fragment implements ClickListener {

    final String TAG = MenuFragment.class.getSimpleName();
    MenuListBinding binding;
    KitchenActivity activity;
    MenuAdapter adapter;
    List<Menu> list =  new ArrayList<>();
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference mRef = firebaseDatabase.getReference(Constants.MENU);
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.menu_list,container,false);

        activity = (KitchenActivity) getActivity();
        binding.setVisibility(true);

        binding.recycler.hasFixedSize();
        adapter = new MenuAdapter(getContext(),list);
        binding.recycler.setAdapter(adapter);
        adapter.setListener(this);

        binding.add.setOnClickListener(v -> {
            activity.openFragment(new CreateMenuFragment());
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
                            for (DataSnapshot snapshot1 :snapshot.getChildren()){
                                Menu model = snapshot1.getValue(Menu.class);
                                model.setId(snapshot1.getKey());
                                Log.i(TAG,model.toString());
                                list.add(model);
                            }
                            adapter.notifyDataSetChanged();
                        }else {
                            binding.setVisibility(false);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    @Override
    public void onMenuClick(Menu menu) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.PARAMS,menu);
        activity.openCreateMenu(bundle);
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

    }

    @Override
    public void onChefClick(Menu menu) {

    }
}
