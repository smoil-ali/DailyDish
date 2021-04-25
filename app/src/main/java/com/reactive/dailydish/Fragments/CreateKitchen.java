package com.reactive.dailydish.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.reactive.dailydish.Acitivities.KitchenActivity;
import com.reactive.dailydish.R;
import com.reactive.dailydish.Utils.Constants;
import com.reactive.dailydish.databinding.CreteKitchenBinding;
import com.reactive.dailydish.model.Kitchen;

import java.io.IOException;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;

public class CreateKitchen extends Fragment {


    final String TAG = CreateKitchen.class.getSimpleName();
    private final int PICK_IMAGE_REQUEST = 71;
    CreteKitchenBinding binding;
    Kitchen model;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    StorageReference storageReference = firebaseStorage.getReference();
    DatabaseReference mRef = firebaseDatabase.getReference(Constants.KITCHEN);
    private Uri filePath;
    private String downloadUrl;
    ProgressDialog progressDialog;
    KitchenActivity activity;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.crete_kitchen,container,false);
        model = new Kitchen();
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Kitchen");
        progressDialog.setMessage("Data is uploading...");
        binding.setVisibility(true);
        if (getArguments() != null){
            model = (Kitchen) getArguments().getSerializable(Constants.PARAMS);
        }
        binding.setData(model);
        activity = (KitchenActivity)getActivity();
        binding.imageButton.setOnClickListener(v ->
        {
            chooseImage();
        });

        binding.back.setOnClickListener(v -> {
            activity.back(new KitchenDashboard());
        });

        binding.done.setOnClickListener(v -> {
            if (isValid()){
                progressDialog.show();
                if (downloadUrl != null)
                    model.setImage(downloadUrl);
                model.setChefId(FirebaseAuth.getInstance().getCurrentUser().getUid());
                mRef.child(model.getChefId())
                        .setValue(model)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                progressDialog.dismiss();
                                activity.back(new KitchenDashboard());
                                Toast.makeText(getContext(), "Kitchen Created", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.dismiss();
                                Log.i(TAG,e.getMessage());
                                Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        return binding.getRoot();
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
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                binding.imageView.setImageBitmap(bitmap);
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
            final ProgressDialog progressDialog = new ProgressDialog(getContext());
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
                            binding.setVisibility(false);
                            Toast.makeText(getContext(), "Uploaded", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(getContext(), "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
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


    boolean isValid(){
        model.setDisplayError(true);
        if (!model.getNameError().isEmpty()){
            return false;
        }
        if (!model.getAddressError().isEmpty()){
            return false;
        }
        if (downloadUrl == null && model.getImage() == null){
            Toast.makeText(getContext(), "Please Add Image", Toast.LENGTH_SHORT).show();
            return false;
        }
        model.setDisplayError(false);
        return true;
    }
}
