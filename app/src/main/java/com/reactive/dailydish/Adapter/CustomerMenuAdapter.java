package com.reactive.dailydish.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.reactive.dailydish.Interfaces.ClickListener;
import com.reactive.dailydish.R;
import com.reactive.dailydish.Utils.Constants;
import com.reactive.dailydish.databinding.CustomerItemMenuBinding;
import com.reactive.dailydish.databinding.ItemMenuBinding;
import com.reactive.dailydish.model.Menu;
import com.reactive.dailydish.model.ProfileModel;

import java.util.List;

public class CustomerMenuAdapter extends RecyclerView.Adapter<CustomerMenuAdapter.ViewHolder> {
    final String TAG = CustomerMenuAdapter.class.getSimpleName();
    Context context;
    List<Menu> list;
    ClickListener listener;

    public void setListener(ClickListener listener) {
        this.listener = listener;
    }

    public CustomerMenuAdapter(Context context, List<Menu> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.customer_item_menu,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Menu model = list.get(position);
        holder.binding.setData(model);
        Glide.with(context).load(model.getImage())
                .placeholder(R.drawable.picture).into(holder.binding.image);
        if (model.getDiscount().isEmpty())
            holder.binding.discount.setVisibility(View.GONE);
        if (model.getOffers().isEmpty()) {
            model.setOffers("Offer not available");
        }
        getChefInfo(model.getKitchenId(),holder.binding);
        holder.binding.buttonCart.setOnClickListener( v -> {
            listener.onMenuClick(model);
        });

        holder.binding.chefname.setOnClickListener(v -> {
            listener.onChefClick(model);
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CustomerItemMenuBinding binding;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = CustomerItemMenuBinding.bind(itemView);
        }
    }

    void getChefInfo(String id,CustomerItemMenuBinding binding){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference(Constants.USERS)
                .child(Constants.CHEF).child(id);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ProfileModel model = snapshot.getValue(ProfileModel.class);
                if (!model.getImage().isEmpty()){
                    Glide.with(context).load(model.getImage()).placeholder(R.drawable.user)
                            .into(binding.userImage);
                }
                binding.chefname.setText("Chef "+model.getName());
                Log.i(TAG,model.toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i(TAG,error.getMessage());
            }
        });
    }
}
