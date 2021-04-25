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
import com.reactive.dailydish.databinding.CreateMenuBinding;
import com.reactive.dailydish.model.Menu;

import java.io.IOException;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;

public class CreateMenuFragment extends Fragment {

    final String TAG = CreateMenuFragment.class.getSimpleName();
    private final int PICK_IMAGE_REQUEST = 71;
    CreateMenuBinding binding;
    Menu model;
    KitchenActivity activity;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    StorageReference storageReference = firebaseStorage.getReference();
    DatabaseReference mRef = firebaseDatabase.getReference(Constants.MENU);
    private Uri filePath;
    private String downloadUrl;
    ProgressDialog progressDialog;
    boolean isEdit;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.create_menu,container,false);
        activity = (KitchenActivity) getActivity();
        model = new Menu();
        if (getArguments() != null){
            model = (Menu)getArguments().getSerializable(Constants.PARAMS);
            isEdit = true;
        }
        binding.setData(model);
        binding.setVisibility(true);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Menu");
        progressDialog.setMessage("Data Adding...");

        binding.imageButton.setOnClickListener(v ->{
            chooseImage();
        });

        binding.back.setOnClickListener(v -> {
            activity.back(new MenuFragment());
        });

        binding.done.setOnClickListener(v -> {
            if (isValid()){
                progressDialog.show();
                if (!isEdit){
                    model.setKitchenId(FirebaseAuth.getInstance().getCurrentUser().getUid());
                    model.setImage(downloadUrl);
                    mRef.child(model.getKitchenId()).push()
                            .setValue(model)
                            .addOnCompleteListener(task -> {
                                progressDialog.dismiss();
                                activity.back(new MenuFragment());
                            })
                            .addOnFailureListener(e -> {
                                Log.i(TAG,e.getMessage());
                                progressDialog.dismiss();
                                Toast.makeText(activity, "Failed", Toast.LENGTH_SHORT).show();
                            });
                }else {
                    if (downloadUrl != null)
                        model.setImage(downloadUrl);
                    mRef.child(model.getKitchenId()).child(model.getId())
                            .setValue(model)
                            .addOnCompleteListener(task -> {
                                progressDialog.dismiss();
                                activity.back(new MenuFragment());
                            })
                            .addOnFailureListener(e -> {
                                Log.i(TAG,e.getMessage());
                                progressDialog.dismiss();
                                Toast.makeText(activity, "Failed", Toast.LENGTH_SHORT).show();
                            });
                }

            }
        });

        return binding.getRoot();
    }


    boolean isValid(){
        model.setDisplayError(true);
        if (!model.getNameError().isEmpty()){
            return false;
        }
        if (!model.getPriceError().isEmpty()){
            return false;
        }
        if (!model.getCategoryError().isEmpty()){
            return false;
        }
        if (downloadUrl == null && model.getImage() == null){
            Toast.makeText(getContext(), "Please Add Image", Toast.LENGTH_SHORT).show();
            return false;
        }
        model.setDisplayError(false);
        return true;
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
}
