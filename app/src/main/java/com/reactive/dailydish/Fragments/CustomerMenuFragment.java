package com.reactive.dailydish.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
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
import com.reactive.dailydish.Acitivities.CustomerActivity;
import com.reactive.dailydish.Acitivities.DashBoard;
import com.reactive.dailydish.Acitivities.KitchenActivity;
import com.reactive.dailydish.Adapter.CustomerMenuAdapter;
import com.reactive.dailydish.Adapter.MenuAdapter;
import com.reactive.dailydish.Interfaces.ClickListener;
import com.reactive.dailydish.R;
import com.reactive.dailydish.Utils.Constants;
import com.reactive.dailydish.Utils.Helper;
import com.reactive.dailydish.databinding.CustomerMenuFragmentBinding;
import com.reactive.dailydish.model.Checkout;
import com.reactive.dailydish.model.Kitchen;
import com.reactive.dailydish.model.Menu;

import java.util.ArrayList;
import java.util.List;

public class CustomerMenuFragment  extends Fragment implements ClickListener {

    final String TAG = CustomerMenuFragment.class.getSimpleName();
    CustomerMenuFragmentBinding binding;
    CustomerActivity activity;
    CustomerMenuAdapter adapter;
    List<Menu> list =  new ArrayList<>();
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference mRef = firebaseDatabase.getReference(Constants.MENU);
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.customer_menu_fragment,container,false);
        activity = (CustomerActivity) getActivity();
        binding.setVisibility(true);
        binding.placeHolder.getRoot().setVisibility(View.VISIBLE);
        binding.recycler.hasFixedSize();
        adapter = new CustomerMenuAdapter(getContext(),list);
        binding.recycler.setAdapter(adapter);
        adapter.setListener(this);

        binding.signout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Helper.setLogin(getContext(),false);
            startActivity(new Intent(getContext(), DashBoard.class));
            getActivity().finish();
        });



        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fetchData();
    }

    void fetchData(){
        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot snapshot1:snapshot.getChildren()){
                        for (DataSnapshot snapshot2:snapshot1.getChildren()){
                            Menu model = snapshot2.getValue(Menu.class);
                            model.setId(snapshot2.getKey());
                            Log.i(TAG,model.toString());
                            list.add(model);
                        }
                    }
                    adapter.notifyDataSetChanged();
                    binding.placeHolder.getRoot().setVisibility(View.GONE);
                }else {
                    binding.placeHolder.getRoot().setVisibility(View.GONE);
                    binding.setVisibility(false);
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

    @Override
    public void onStart() {
        super.onStart();
        List<Menu> menuList = new ArrayList<>();
        String strList = Helper.fromListToString(menuList);
        Log.i(TAG,strList);
        Log.i(TAG,Helper.fromStringToList(strList).size()+" size");
    }

    @Override
    public void onMenuClick(Menu menu) {
        String cartData = Helper.getCartData(getContext());
        if (cartData.isEmpty()){
            List<Menu> menuList = new ArrayList<>();
            menuList.add(menu);
            String strList = Helper.fromListToString(menuList);
            Helper.setCartData(strList,getContext());
        }else {
            List<Menu> menuList = Helper.fromStringToList(cartData);
            if (check(menuList,menu)){
                if (menuList.get(0).getKitchenId().equals(menu.getKitchenId())){
                    menuList.add(menu);
                }else {
                    menuList = new ArrayList<>();
                    menuList.add(menu);
                }
                String strList = Helper.fromListToString(menuList);
                Helper.setCartData(strList,getContext());
            }
        }
        Toast.makeText(activity, "Add To Cart", Toast.LENGTH_SHORT).show();
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
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.PARAMS,menu);
        activity.binding.bottomNavigation.setSelectedItemId(R.id.kitchen);
        activity.openKitchenFragment(new CustomerKitchen(),bundle);
    }

    boolean check(List<Menu> list,Menu menu){
        for (Menu menu1:list){
            if (menu.getId().equals(menu1.getId())){
                return false;
            }
        }
        return true;
    }
}
