package com.reactive.dailydish.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.reactive.dailydish.R;
import com.reactive.dailydish.databinding.ItemOrderDetailBinding;
import com.reactive.dailydish.model.Cart;
import com.reactive.dailydish.model.Checkout;
import com.reactive.dailydish.model.Menu;

import java.util.List;

public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.ViewHolder> {
    Context context;
    List<Cart> list;

    public OrderDetailAdapter(Context context, List<Cart> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order_detail,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Cart cart = list.get(position);
        Menu menu = cart.getMenu();
        holder.binding.name.setText(menu.getName());
        holder.binding.address.setText(cart.getQty());
        holder.binding.offer.setText(menu.getOffers());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemOrderDetailBinding binding;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemOrderDetailBinding.bind(itemView);
        }
    }
}
