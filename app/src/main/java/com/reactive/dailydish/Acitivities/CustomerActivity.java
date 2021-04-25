package com.reactive.dailydish.Acitivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.reactive.dailydish.Fragments.CartFragment;
import com.reactive.dailydish.Fragments.CheckoutFragment;
import com.reactive.dailydish.Fragments.CreateMenuFragment;
import com.reactive.dailydish.Fragments.CustomerKitchen;
import com.reactive.dailydish.Fragments.CustomerKitchenDetailFragment;
import com.reactive.dailydish.Fragments.CustomerMenuFragment;
import com.reactive.dailydish.Fragments.KitchenDashboard;
import com.reactive.dailydish.Fragments.MenuFragment;
import com.reactive.dailydish.Fragments.OrderFragment;
import com.reactive.dailydish.Fragments.ProfileFragment;
import com.reactive.dailydish.R;
import com.reactive.dailydish.Utils.Constants;
import com.reactive.dailydish.databinding.ActivityCustomerBinding;

public class CustomerActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    final String TAG = CustomerActivity.class.getSimpleName();
    public ActivityCustomerBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_customer);
        binding.bottomNavigation.setOnNavigationItemSelectedListener(this);
        if (savedInstanceState == null){
            binding.bottomNavigation.setSelectedItemId(R.id.home);
        }

//        binding.home.setOnClickListener(v ->{
//            openFragment(new CustomerMenuFragment());
//        });
//
//        binding.cultery.setOnClickListener(v -> {
//            CartFragment cartFragment = (CartFragment) getSupportFragmentManager().findFragmentByTag(Constants.CART_FRAGMENT);
//            CheckoutFragment checkoutFragment = (CheckoutFragment) getSupportFragmentManager().findFragmentByTag(Constants.CHECKOUT_FRAGMENT);
//            if (cartFragment != null){
//                if (cartFragment.isVisible()){
//                    Log.i(TAG,"Visible");
//                    openKitchenFragment(new CustomerKitchen());
//                }
//            }else if (checkoutFragment != null){
//                if (checkoutFragment.isVisible()){
//                    openKitchenFragment(new CustomerKitchen());
//                }
//            }else {
//                openFragment(new CustomerKitchen());
//            }
//        });
//
//        binding.cart.setOnClickListener(v -> {
//            openFragment(new CartFragment());
//        });

    }

    public void openKitchenFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        for(int i = 0; i < fragmentManager.getBackStackEntryCount(); ++i) {
            fragmentManager.popBackStack();
        }
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
        transaction.replace(R.id.container,fragment);
        transaction.setReorderingAllowed(true);
        transaction.commit();
    }

    public void openKitchenFragment(Fragment fragment,Bundle bundle){
        FragmentManager fragmentManager = getSupportFragmentManager();
        for(int i = 0; i < fragmentManager.getBackStackEntryCount(); ++i) {
            fragmentManager.popBackStack();
        }
        fragment.setArguments(bundle);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
        transaction.replace(R.id.container,fragment);
        transaction.setReorderingAllowed(true);
        transaction.commit();
    }

    public void openFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        for(int i = 0; i < fragmentManager.getBackStackEntryCount(); ++i) {
            fragmentManager.popBackStack();
        }
        if (fragment instanceof CustomerKitchen){
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
            transaction.replace(R.id.container,fragment);
            transaction.setReorderingAllowed(true);
            transaction.commit();
        }

        if (fragment instanceof CustomerMenuFragment){
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
            transaction.replace(R.id.container,fragment);
            transaction.setReorderingAllowed(true);
            transaction.commit();
        }

        if (fragment instanceof CartFragment){
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
            transaction.replace(R.id.container,fragment,Constants.CART_FRAGMENT);
            transaction.setReorderingAllowed(true);
            transaction.commit();
        }

    }

    public void openCustomerKitchenDetailFragment(Bundle bundle){
        FragmentManager fragmentManager = getSupportFragmentManager();
        for(int i = 0; i < fragmentManager.getBackStackEntryCount(); ++i) {
            fragmentManager.popBackStack();
        }
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        CustomerKitchenDetailFragment fragment = new CustomerKitchenDetailFragment();
        fragment.setArguments(bundle);
        transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
        transaction.replace(R.id.container,fragment);
        transaction.setReorderingAllowed(true);
        transaction.commit();

    }

    public void openCheckout(Bundle bundle){
        FragmentManager fragmentManager = getSupportFragmentManager();
        for(int i = 0; i < fragmentManager.getBackStackEntryCount(); ++i) {
            fragmentManager.popBackStack();
        }
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        CheckoutFragment fragment = new CheckoutFragment();
        fragment.setArguments(bundle);
        transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
        transaction.replace(R.id.container,fragment,Constants.CHECKOUT_FRAGMENT);
        transaction.setReorderingAllowed(true);
        transaction.commit();

    }


    public void back(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        for(int i = 0; i < fragmentManager.getBackStackEntryCount(); ++i) {
            fragmentManager.popBackStack();
        }

        if (fragment instanceof CartFragment){
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
            transaction.replace(R.id.container,fragment,Constants.CART_FRAGMENT);
            transaction.setReorderingAllowed(true);
            transaction.commit();
        }
        else {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
            transaction.replace(R.id.container,fragment);
            transaction.setReorderingAllowed(true);
            transaction.commit();
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.home:
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.container,new CustomerMenuFragment())
                        .commit();
                return true;
            case R.id.kitchen:
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.container,new CustomerKitchen())
                        .commit();
                return true;
            case R.id.cart:
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.container,new CartFragment())
                        .commit();
                return true;
            case R.id.profile:
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.container,new ProfileFragment())
                        .commit();
                return true;
        }
        return false;
    }
}