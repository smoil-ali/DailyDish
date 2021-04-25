package com.reactive.dailydish.Acitivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.reactive.dailydish.R;
import com.reactive.dailydish.Utils.Constants;
import com.reactive.dailydish.Utils.Helper;
import com.reactive.dailydish.databinding.ActivityRegisterBinding;
import com.reactive.dailydish.model.ProfileModel;

import java.io.IOException;
import java.util.UUID;

public class RegisterActivity extends AppCompatActivity {

    final String TAG = RegisterActivity.class.getSimpleName();
    private final int PICK_IMAGE_REQUEST = 71;
    ActivityRegisterBinding binding;
    ProfileModel model;
    FirebaseAuth mAuth;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference(Constants.USERS);
    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    StorageReference storageReference = firebaseStorage.getReference();
    private Uri filePath;
    private String downloadUrl;
    String currentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_register);
        model = new ProfileModel();
        binding.setData(model);
        binding.setVisibility(true);
        mAuth = FirebaseAuth.getInstance();


        binding.done.setOnClickListener(v -> {
            if (isValid()){
                model.setImage(downloadUrl);
                binding.setVisibility(false);
                register();
            }

        });

        binding.image.setOnClickListener(v -> {
            chooseImage();
        });

    }

    void register(){
        mAuth.createUserWithEmailAndPassword(binding.email.getText().toString(),
                binding.password.getText().toString())
                .addOnCompleteListener(task -> {
                    currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    Log.i(TAG,FirebaseAuth.getInstance().getCurrentUser().getUid());
                    addProfile();
                })
                .addOnFailureListener(e -> {
                    Log.i(TAG,e.getMessage());
                    binding.setVisibility(true);
                    Toast.makeText(RegisterActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    void addProfile(){
        databaseReference.child(Constants.USER).child(currentUser)
                .setValue(model)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        binding.setVisibility(true);
                        Helper.setLogin(RegisterActivity.this,true);
                        Helper.setUser(RegisterActivity.this,Constants.USER);
                        openScreen();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i(TAG,e.getMessage());
                        binding.setVisibility(true);
                        Toast.makeText(RegisterActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    boolean isValid(){
        model.setDisplayError(true);
        if (!model.getNameError().isEmpty()){
            return false;
        }
        if (!model.getEmailError().isEmpty()){
            return false;
        }
        if (!model.getPasswordError().isEmpty()){
            return false;
        }
        if (downloadUrl == null){
            Toast.makeText(this, "Add Profile Image", Toast.LENGTH_SHORT).show();
            return false;
        }
        model.setDisplayError(false);
        return true;
    }

    void openScreen(){
        if (Constants.USER.equals(Constants.CHEF)){
            Intent intent = new Intent(this,KitchenActivity.class);
            startActivity(intent);
            finish();
        }else {
            Intent intent = new Intent(this,CustomerActivity.class);
            startActivity(intent);
            finish();
        }

    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                binding.image.setImageBitmap(bitmap);
                uploadImage();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    void uploadImage(){
        if(filePath != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(RegisterActivity.this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            StorageReference ref = storageReference.child("images/"+ UUID.randomUUID().toString());
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                            while (!uriTask.isComplete());
                            downloadUrl = uriTask.getResult().toString();
                            progressDialog.dismiss();
                            Toast.makeText(RegisterActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(RegisterActivity.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded "+(int)progress+"%");
                        }
                    });
        }

    }

}