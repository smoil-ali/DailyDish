package com.reactive.dailydish.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.reactive.dailydish.Interfaces.ClickListener;
import com.reactive.dailydish.R;
import com.reactive.dailydish.databinding.ItemCartBinding;
import com.reactive.dailydish.model.Cart;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    final String TAG = CartAdapter.class.getSimpleName();
    Context context;
    List<Cart> list;
    ClickListener listener;

    public void setListener(ClickListener listener) {
        this.listener = listener;
    }

    public CartAdapter(Context context, List<Cart> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cart,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Cart cart = list.get(position);
        Log.i(TAG,cart.toString());
        Glide.with(context).load(cart.getMenu().getImage())
                .placeholder(R.drawable.picture)
                .into(holder.binding.image);
        holder.binding.setData(cart.getMenu());
        holder.binding.setCart(cart);
        holder.binding.total.setText(cart.getTotal());
        if (cart.getMenu().getDiscount().isEmpty()){
            holder.binding.discount.setText("0");
        }else
            holder.binding.discount.setText(cart.getMenu().getDiscount());

        holder.binding.delete.setOnClickListener(v -> {
            listener.onDeleteClick(position);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemCartBinding binding;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemCartBinding.bind(itemView);
        }
    }
}
