package com.reactive.dailydish.Acitivities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.reactive.dailydish.R;
import com.reactive.dailydish.Utils.Constants;
import com.reactive.dailydish.Utils.Helper;
import com.reactive.dailydish.databinding.ActivitySplashBinding;

public class SplashActivity extends AppCompatActivity {

    ActivitySplashBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_splash);

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                if (Helper.IsLogin(SplashActivity.this)){
                    if (Helper.getUser(SplashActivity.this).equals(Constants.CHEF)){
                        startActivity(new Intent(SplashActivity.this,KitchenActivity.class));
                        finish();
                    }else {
                        startActivity(new Intent(SplashActivity.this,CustomerActivity.class));
                        finish();
                    }
                }else {
                    startActivity(new Intent(SplashActivity.this,DashBoard.class));
                    finish();
                }

            }
        },2000);
    }
}