package com.example.ecommerce.ui.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerce.data.model.CartItem;
import com.example.ecommerce.data.model.Order;
import com.example.ecommerce.databinding.ItemOrderBinding;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private final List<Order> orders = new ArrayList<>();
    private final NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(Locale.getDefault());
    private final DateFormat dateFormat = DateFormat.getDateTimeInstance();

    public void submitList(List<Order> data) {
        orders.clear();
        if (data != null) {
            orders.addAll(data);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemOrderBinding binding = ItemOrderBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new OrderViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        holder.bind(orders.get(position));
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    class OrderViewHolder extends RecyclerView.ViewHolder {
        private final ItemOrderBinding binding;

        OrderViewHolder(ItemOrderBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(Order order) {
            binding.orderId.setText(order.getId());
            binding.orderDate.setText(dateFormat.format(new Date(order.getPlacedAt())));
            binding.orderTotal.setText(currencyFormatter.format(order.getTotal()));
            binding.orderSummary.setText(buildSummary(order.getItems()));
        }

        private String buildSummary(List<CartItem> items) {
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < items.size(); i++) {
                CartItem item = items.get(i);
                builder.append(item.getQuantity())
                        .append(" x ")
                        .append(item.getProduct().getName());
                if (i < items.size() - 1) {
                    builder.append("\n");
                }
            }
            return builder.toString();
        }
    }
}

