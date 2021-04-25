package com.reactive.dailydish.Adapter;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
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
import com.reactive.dailydish.databinding.ItemMenuBinding;
import com.reactive.dailydish.model.Menu;
import com.reactive.dailydish.model.ProfileModel;

import java.util.List;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder> {
    final String TAG = MenuAdapter.class.getSimpleName();
    Context context;
    List<Menu> list ;
    ClickListener listener;

    public MenuAdapter(Context context, List<Menu> list) {
        this.context = context;
        this.list = list;
    }

    public void setListener(ClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_menu,parent,false);
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
        holder.binding.card.setOnClickListener(v -> {
            listener.onMenuClick(model);
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemMenuBinding binding;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemMenuBinding.bind(itemView);
        }
    }


}
