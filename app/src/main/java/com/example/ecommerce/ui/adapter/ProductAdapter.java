package com.example.ecommerce.ui.adapter;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerce.data.model.Product;
import com.example.ecommerce.data.repository.ProductImageRepository;
import com.example.ecommerce.databinding.ItemProductBinding;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    public interface Listener {
        void onProductSelected(Product product);
        void onAddToCart(Product product);
    }

    private final List<Product> products = new ArrayList<>();
    private final Listener listener;
    private final ProductImageRepository imageRepository;
    private final NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(Locale.getDefault());

    public ProductAdapter(Listener listener, ProductImageRepository imageRepository) {
        this.listener = listener;
        this.imageRepository = imageRepository;
    }

    public void submitList(List<Product> data) {
        products.clear();
        if (data != null) {
            products.addAll(data);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemProductBinding binding = ItemProductBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ProductViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        holder.bind(products.get(position));
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    class ProductViewHolder extends RecyclerView.ViewHolder {
        private final ItemProductBinding binding;

        ProductViewHolder(ItemProductBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(Product product) {
            binding.productTitle.setText(product.getName());
            binding.productCategory.setText(product.getCategory());
            binding.productPrice.setText(currencyFormatter.format(product.getPrice()));
            binding.productSpecs.setText(product.getDetails());
            binding.productRating.setText(String.format(Locale.getDefault(), "%.1f ★", product.getRating()));

            if (imageRepository != null) {
                Uri customImage = imageRepository.getImage(product.getId());
                if (customImage != null) {
                    binding.productImage.setImageURI(customImage);
                } else {
                    binding.productImage.setImageResource(product.getImageRes());
                }
            } else {
                binding.productImage.setImageResource(product.getImageRes());
            }

            binding.getRoot().setOnClickListener(v -> listener.onProductSelected(product));
            binding.btnAddToCart.setOnClickListener(v -> listener.onAddToCart(product));
        }
    }
}

