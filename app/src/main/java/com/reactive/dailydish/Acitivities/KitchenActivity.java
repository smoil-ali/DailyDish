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
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.reactive.dailydish.Fragments.CreateKitchen;
import com.reactive.dailydish.Fragments.CreateMenuFragment;
import com.reactive.dailydish.Fragments.KitchenDashboard;
import com.reactive.dailydish.Fragments.MenuFragment;
import com.reactive.dailydish.Fragments.OrderDetailFragment;
import com.reactive.dailydish.Fragments.OrderFragment;
import com.reactive.dailydish.Fragments.ProfileFragment;
import com.reactive.dailydish.R;
import com.reactive.dailydish.Utils.Constants;
import com.reactive.dailydish.databinding.ActivityKitchenBinding;

public class KitchenActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    final String TAG = KitchenActivity.class.getSimpleName();
    public ActivityKitchenBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_kitchen);
        binding.bottomNavigation.setOnNavigationItemSelectedListener(this);
        if (savedInstanceState == null){
            binding.bottomNavigation.setSelectedItemId(R.id.kitchen);
        }



//        binding.buttonMenu.setOnClickListener(v -> {
//            OrderFragment fragment = (OrderFragment)getSupportFragmentManager().findFragmentByTag(Constants.ORDER_FRAGMENT);
//            OrderDetailFragment fragment1 = (OrderDetailFragment)getSupportFragmentManager().findFragmentByTag(Constants.ORDER_DETAIL_FRAGMENT);
//            if (fragment != null){
//                if (fragment.isVisible()){
//                    Log.i(TAG,"Visible");
//                    openMenuFragment(new MenuFragment());
//                }
//            }else if (fragment1 != null){
//                if (fragment1.isVisible()){
//                    openMenuFragment(new MenuFragment());
//                }
//            }else {
//                openFragment(new MenuFragment());
//            }
//
//
//        });
//
//        binding.buttonKitchen.setOnClickListener(v -> {
//            openFragment(new KitchenDashboard());
//        });
//
//        binding.order.setOnClickListener(v -> {
//            openFragment(new OrderFragment());
//        });





    }

    public void openMenuFragment(Fragment fragment){
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

    public void openFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        for(int i = 0; i < fragmentManager.getBackStackEntryCount(); ++i) {
            fragmentManager.popBackStack();
        }
        if (fragment instanceof MenuFragment){
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
            transaction.replace(R.id.container,fragment);
            transaction.setReorderingAllowed(true);
            transaction.commit();
        }

        if (fragment instanceof CreateMenuFragment){
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
            transaction.replace(R.id.container,fragment, Constants.MENU_FRAGMENT);
            transaction.setReorderingAllowed(true);
            transaction.commit();
        }

        if (fragment instanceof KitchenDashboard){
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
            transaction.replace(R.id.container,fragment);
            transaction.setReorderingAllowed(true);
            transaction.commit();
        }

        if (fragment instanceof OrderFragment){
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
            transaction.replace(R.id.container,fragment,Constants.ORDER_FRAGMENT);
            transaction.setReorderingAllowed(true);
            transaction.commit();
        }
    }

    public void back(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        for(int i = 0; i < fragmentManager.getBackStackEntryCount(); ++i) {
            fragmentManager.popBackStack();
        }
        if (fragment instanceof OrderFragment){
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
            transaction.replace(R.id.container,fragment,Constants.ORDER_FRAGMENT);
            transaction.setReorderingAllowed(true);
            transaction.commit();
        }else {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
            transaction.replace(R.id.container,fragment);
            transaction.setReorderingAllowed(true);
            transaction.commit();
        }

    }

    public void openCreateKitchen(Bundle bundle){
        FragmentManager fragmentManager = getSupportFragmentManager();
        for(int i = 0; i < fragmentManager.getBackStackEntryCount(); ++i) {
            fragmentManager.popBackStack();
        }
        CreateKitchen kitchen = new CreateKitchen();
        kitchen.setArguments(bundle);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
        transaction.replace(R.id.container,kitchen);
        transaction.setReorderingAllowed(true);
        transaction.commit();
    }

    public void openCreateMenu(Bundle bundle){
        FragmentManager fragmentManager = getSupportFragmentManager();
        for(int i = 0; i < fragmentManager.getBackStackEntryCount(); ++i) {
            fragmentManager.popBackStack();
        }
        CreateMenuFragment kitchen = new CreateMenuFragment();
        kitchen.setArguments(bundle);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
        transaction.replace(R.id.container,kitchen);
        transaction.setReorderingAllowed(true);
        transaction.commit();
    }

    public void openOrderDetail(Bundle bundle){
        FragmentManager fragmentManager = getSupportFragmentManager();
        for(int i = 0; i < fragmentManager.getBackStackEntryCount(); ++i) {
            fragmentManager.popBackStack();
        }
        OrderDetailFragment orderDetailFragment = new OrderDetailFragment();
        orderDetailFragment.setArguments(bundle);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
        transaction.replace(R.id.container,orderDetailFragment,Constants.ORDER_DETAIL_FRAGMENT);
        transaction.setReorderingAllowed(true);
        transaction.commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.kitchen:
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.container,new KitchenDashboard())
                        .commit();
                return true;
            case R.id.menu:
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.container,new MenuFragment())
                        .commit();
                return true;
            case R.id.order:
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.container,new OrderFragment())
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