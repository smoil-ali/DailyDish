package com.reactive.dailydish.Acitivities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;

import com.reactive.dailydish.R;
import com.reactive.dailydish.Utils.Constants;
import com.reactive.dailydish.databinding.ActivityDashBoardBinding;

public class DashBoard extends AppCompatActivity {

    final String TAG = DashBoard.class.getSimpleName();
    ActivityDashBoardBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_dash_board);

        binding.logIn.setOnClickListener(v -> openSignInScreen());

        binding.chef.setOnClickListener(v -> {
            Constants.USER = Constants.CHEF;
            openRegisterScreen();
        });

        binding.customer.setOnClickListener(v -> {
            Constants.USER = Constants.CUSTOMER;
            openRegisterScreen();
        });
    }

    void openRegisterScreen(){
        Intent intent = new Intent(this,RegisterActivity.class);
        startActivity(intent);
        finish();
    }


    void openSignInScreen(){
        Intent intent = new Intent(this,SignInActivitye.class);
        startActivity(intent);
        finish();
    }
}