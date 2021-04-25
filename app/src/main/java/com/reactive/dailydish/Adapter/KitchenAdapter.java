package com.reactive.dailydish.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.reactive.dailydish.Interfaces.ClickListener;
import com.reactive.dailydish.R;
import com.reactive.dailydish.Utils.Constants;
import com.reactive.dailydish.databinding.ItemKitchenBinding;
import com.reactive.dailydish.model.Kitchen;
import com.reactive.dailydish.model.Like;

import java.util.List;

public class KitchenAdapter extends RecyclerView.Adapter<KitchenAdapter.ViewHolder> {
    final String TAG = KitchenAdapter.class.getSimpleName();
    Context context;
    List<Kitchen> list;
    ClickListener listener;
    boolean status;

    public KitchenAdapter(Context context, List<Kitchen> list) {
        this.context = context;
        this.list = list;
    }

    public void setListener(ClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_kitchen,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Kitchen model = list.get(position);
        holder.binding.setData(model);
        Glide.with(context).load(model.getImage())
                .placeholder(R.drawable.picture).into(holder.binding.image);
        isLiked(model.getChefId(),holder.binding.disableLike);
        holder.binding.disableLike.setOnClickListener(v -> {
            getLike(model.getChefId(),holder.binding.disableLike,model);
        });
        holder.binding.card.setOnClickListener(v -> {
            listener.onKitchenClick(model);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemKitchenBinding binding;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemKitchenBinding.bind(itemView);
        }
    }


    void isLiked(String id, ImageView imageView){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference(Constants.LIKES);
        databaseReference.child(id)
                .orderByChild("status").equalTo(true)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            status = true;
                            imageView.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.active_like));
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    void getLike(String id,ImageView imageView,Kitchen model){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference(Constants.LIKES);
        databaseReference.child(id)
                .orderByChild("userId").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            for (DataSnapshot snapshot1:snapshot.getChildren()){
                                Like like = snapshot1.getValue(Like.class);
                                Log.i(TAG,like.toString());
                                if (like.isStatus()){
                                    imageView
                                            .setImageDrawable(ContextCompat.getDrawable(context,R.drawable.disable_like));
                                }else {
                                    imageView
                                            .setImageDrawable(ContextCompat.getDrawable(context, R.drawable.active_like));
                                }
                            }
                            listener.onLikeClick(model);
                        }else {
                            imageView
                                    .setImageDrawable(ContextCompat.getDrawable(context, R.drawable.active_like));
                            listener.onLikeClick(model);
                            Log.i(TAG,"Like dont exist");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.i(TAG,error.getMessage());
                    }
                });
    }
}

