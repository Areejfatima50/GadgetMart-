package com.example.ecommerce.ui.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerce.data.model.CartItem;
import com.example.ecommerce.databinding.ItemCartBinding;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    public interface Listener {
        void onQuantityChanged(CartItem item, int newQuantity);
        void onRemove(CartItem item);
    }

    private final List<CartItem> items = new ArrayList<>();
    private final Listener listener;
    private final NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(Locale.getDefault());

    public CartAdapter(Listener listener) {
        this.listener = listener;
    }

    public void submitList(List<CartItem> data) {
        items.clear();
        if (data != null) {
            items.addAll(data);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCartBinding binding = ItemCartBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new CartViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class CartViewHolder extends RecyclerView.ViewHolder {
        private final ItemCartBinding binding;

        CartViewHolder(ItemCartBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(CartItem item) {
            binding.productThumb.setImageResource(item.getProduct().getImageRes());
            binding.productTitle.setText(item.getProduct().getName());
            binding.productCategory.setText(item.getProduct().getCategory());
            binding.productPrice.setText(currencyFormatter.format(item.getProduct().getPrice()));
            binding.productTotal.setText(currencyFormatter.format(item.getLineTotal()));
            binding.quantityLabel.setText(String.valueOf(item.getQuantity()));

            binding.btnIncrease.setOnClickListener(v -> listener.onQuantityChanged(item, item.getQuantity() + 1));
            binding.btnDecrease.setOnClickListener(v -> {
                if (item.getQuantity() > 1) {
                    listener.onQuantityChanged(item, item.getQuantity() - 1);
                }
            });
            binding.btnRemove.setOnClickListener(v -> listener.onRemove(item));
        }
    }
}

