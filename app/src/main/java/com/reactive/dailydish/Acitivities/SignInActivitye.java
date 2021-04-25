package com.reactive.dailydish.Acitivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.reactive.dailydish.R;
import com.reactive.dailydish.Utils.Constants;
import com.reactive.dailydish.Utils.Helper;
import com.reactive.dailydish.databinding.ActivitySignInActivityeBinding;
import com.reactive.dailydish.model.SignInModel;

public class SignInActivitye extends AppCompatActivity {

    final String TAG = SignInActivitye.class.getSimpleName();
    ActivitySignInActivityeBinding binding;
    SignInModel model;
    FirebaseAuth mAuth;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference mRef = firebaseDatabase.getReference(Constants.USERS);
    private Uri filePath;
    private String downloadUrl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_sign_in_activitye);
        model = new SignInModel();
        binding.setData(model);
        binding.setVisibility(true);

        mAuth = FirebaseAuth.getInstance();


        binding.done.setOnClickListener(v -> {
            if (isValid()){
                binding.setVisibility(false);
                signIn();
            }
        });
    }

    boolean isValid(){
        model.setDisplayError(true);
        if (!model.getEmailError().isEmpty()){
            return false;
        }
        if (!model.getPasswordError().isEmpty()){
            return false;
        }
        model.setDisplayError(false);
        return true;
    }

    void signIn(){
        mAuth.signInWithEmailAndPassword(binding.email.getText().toString(),
                binding.password.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        checkUser();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i(TAG,e.getMessage());
                        binding.setVisibility(true);
                        Toast.makeText(SignInActivitye.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    void openKitchenScreen(){
        Helper.setUser(this,Constants.CHEF);
        Intent intent = new Intent(this,KitchenActivity.class);
        startActivity(intent);
        finish();
    }

    void openCustomerScreen(){
        Helper.setUser(this,Constants.CUSTOMER);
        Intent intent = new Intent(this,CustomerActivity.class);
        startActivity(intent);
        finish();
    }

    void checkUser(){
        mRef.child(Constants.CHEF)
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        binding.setVisibility(true);
                        Helper.setLogin(SignInActivitye.this,true);
                        if (snapshot.exists()){
                            openKitchenScreen();
                        }else {
                            openCustomerScreen();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        binding.setVisibility(true);
                        Log.i(TAG,error.getMessage());
                    }
                });
    }
}