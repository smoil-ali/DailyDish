package com.reactive.dailydish.Interfaces;

import com.reactive.dailydish.databinding.ItemKitchenBinding;
import com.reactive.dailydish.model.Cart;
import com.reactive.dailydish.model.Checkout;
import com.reactive.dailydish.model.Kitchen;
import com.reactive.dailydish.model.Menu;

public interface ClickListener {
    void onMenuClick(Menu menu);
    void onLikeClick(Kitchen model);
    void onKitchenClick(Kitchen model);
    void onDeleteClick(int pos);
    void onOrderClick(Checkout checkout);
    void onChefClick(Menu menu);
}
