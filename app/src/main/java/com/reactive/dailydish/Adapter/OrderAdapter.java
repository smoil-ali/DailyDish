package com.reactive.dailydish.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.CheckResult;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.reactive.dailydish.Interfaces.ClickListener;
import com.reactive.dailydish.R;
import com.reactive.dailydish.databinding.ItemOrderBinding;
import com.reactive.dailydish.model.Checkout;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {
    Context context;
    List<Checkout> list;
    ClickListener listener;

    public void setListener(ClickListener listener) {
        this.listener = listener;
    }

    public OrderAdapter(Context context, List<Checkout> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Checkout checkout = list.get(position);
        holder.binding.name.setText(checkout.getModel().getName());
        holder.binding.status.setText(checkout.getStatus());
        holder.binding.address.setText(checkout.getAddress());
        holder.binding.total.setText(checkout.getGrandTotal());

        holder.binding.card.setOnClickListener(v -> {
            listener.onOrderClick(checkout);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemOrderBinding binding;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemOrderBinding.bind(itemView);
        }
    }
}
