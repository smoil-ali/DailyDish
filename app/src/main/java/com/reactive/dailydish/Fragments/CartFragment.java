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

import com.reactive.dailydish.Acitivities.CustomerActivity;
import com.reactive.dailydish.Adapter.CartAdapter;
import com.reactive.dailydish.Adapter.KitchenAdapter;
import com.reactive.dailydish.Interfaces.ClickListener;
import com.reactive.dailydish.R;
import com.reactive.dailydish.Utils.Constants;
import com.reactive.dailydish.Utils.Helper;
import com.reactive.dailydish.databinding.CartFragmentBinding;
import com.reactive.dailydish.databinding.EmtyCartBinding;
import com.reactive.dailydish.model.Cart;
import com.reactive.dailydish.model.Checkout;
import com.reactive.dailydish.model.Kitchen;
import com.reactive.dailydish.model.Menu;

import java.util.ArrayList;
import java.util.List;

public class CartFragment extends Fragment implements ClickListener {

    final String TAG = CartFragment.class.getSimpleName();
    CartFragmentBinding binding;
    CustomerActivity activity;
    CartAdapter adapter;
    List<Cart> list =  new ArrayList<>();
    double grandTotal = 0;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.cart_fragment,container,false);
        activity = (CustomerActivity) getActivity();
        binding.setVisibility(true);
        binding.recycler.hasFixedSize();
        adapter = new CartAdapter(getContext(),list);
        binding.recycler.setAdapter(adapter);
        adapter.setListener(this);

        binding.done.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            Checkout checkout = new Checkout();
            checkout.setCart(list);
            for (Cart cart:list){
                grandTotal = grandTotal + Double.parseDouble(cart.getTotal());
            }
            String.format("%.1f", grandTotal);
            checkout.setGrandTotal(grandTotal+"");
            bundle.putSerializable(Constants.PARAMS,checkout);
            activity.openCheckout(bundle);
        });

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fetchData();
    }
    void fetchData(){
        if (!Helper.getCartData(getContext()).isEmpty()){
            List<Menu> menuList = Helper.fromStringToList(Helper.getCartData(getContext()));
            for (Menu menu : menuList){
                Cart cart = new Cart();
                cart.setMenu(menu);
                if (!cart.getMenu().getDiscount().isEmpty()){
                    double price = Double.parseDouble(cart.getMenu().getPrice());
                    double discount = Double.parseDouble(cart.getMenu().getDiscount());
                    double val = price - (price * discount)/100;
                    cart.setDiscountedPrice(String.valueOf(val));
                }else {
                    cart.setDiscountedPrice(cart.getMenu().getPrice());
                }
                list.add(cart);
            }
            Log.i(TAG,list.size()+" size");
            adapter.notifyDataSetChanged();
        }else {
            binding.done.setVisibility(View.GONE);
            binding.setVisibility(false);
        }
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
        list.remove(pos);
        List<Menu> menuList = new ArrayList<>();
        if (list.size() > 0){
            for (Cart cart:list){
                menuList.add(cart.getMenu());
            }
            String strList = Helper.fromListToString(menuList);
            Helper.setCartData(strList,getContext());
            adapter.notifyDataSetChanged();
        }else {
            Helper.setCartData(null,getContext());
            binding.done.setVisibility(View.GONE);
            binding.setVisibility(false);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onOrderClick(Checkout checkout) {

    }

    @Override
    public void onChefClick(Menu menu) {

    }
}
