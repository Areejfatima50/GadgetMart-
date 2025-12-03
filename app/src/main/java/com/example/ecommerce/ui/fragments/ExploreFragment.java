package com.example.ecommerce.ui.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.ecommerce.data.model.Product;
import com.example.ecommerce.data.model.User;
import com.example.ecommerce.data.repository.CartRepository;
import com.example.ecommerce.data.repository.ProductImageRepository;
import com.example.ecommerce.data.repository.ProductRepository;
import com.example.ecommerce.databinding.FragmentExploreBinding;
import com.example.ecommerce.ui.ProductDetailActivity;
import com.example.ecommerce.ui.adapter.ProductAdapter;
import com.example.ecommerce.ui.MainActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ExploreFragment extends Fragment implements ProductAdapter.Listener {

    private FragmentExploreBinding binding;
    private ProductRepository productRepository;
    private CartRepository cartRepository;
    private ProductImageRepository imageRepository;
    private ProductAdapter adapter;
    private User user;
    private final List<Product> fullList = new ArrayList<>();

    public static ExploreFragment newInstance() {
        return new ExploreFragment();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof MainActivity) {
            MainActivity host = (MainActivity) context;
            productRepository = host.getProductRepository();
            cartRepository = host.getCartRepository();
            imageRepository = host.getImageRepository();
            user = host.getActiveUser();
        } else {
            productRepository = new ProductRepository(context);
            cartRepository = new CartRepository(context, productRepository);
            imageRepository = new ProductImageRepository(context);
            user = null;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentExploreBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new ProductAdapter(this, imageRepository);
        binding.recyclerProducts.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerProducts.setHasFixedSize(true);
        binding.recyclerProducts.setAdapter(adapter);
        binding.searchField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterProducts(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        if (productRepository != null) {
            fullList.addAll(productRepository.getAllProducts());
            adapter.submitList(fullList);
        }
    }

    private void filterProducts(String query) {
        if (fullList.isEmpty()) return;
        if (query.isEmpty()) {
            adapter.submitList(fullList);
            return;
        }
        String lower = query.toLowerCase(Locale.getDefault());
        List<Product> filtered = new ArrayList<>();
        for (Product product : fullList) {
            if (product.getName().toLowerCase(Locale.getDefault()).contains(lower)
                    || product.getCategory().toLowerCase(Locale.getDefault()).contains(lower)) {
                filtered.add(product);
            }
        }
        adapter.submitList(filtered);
    }

    @Override
    public void onProductSelected(Product product) {
        Intent intent = new Intent(requireContext(), ProductDetailActivity.class);
        intent.putExtra(ProductDetailActivity.EXTRA_PRODUCT_ID, product.getId());
        startActivity(intent);
    }

    @Override
    public void onAddToCart(Product product) {
        if (cartRepository != null && user != null) {
            cartRepository.addToCart(user.getEmail(), product);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

