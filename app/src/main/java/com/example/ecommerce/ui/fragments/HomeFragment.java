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

import com.example.ecommerce.R;
import com.example.ecommerce.data.model.Product;
import com.example.ecommerce.data.model.User;
import com.example.ecommerce.data.repository.CartRepository;
import com.example.ecommerce.data.repository.ProductImageRepository;
import com.example.ecommerce.data.repository.ProductRepository;
import com.example.ecommerce.databinding.FragmentHomeBinding;
import com.example.ecommerce.ui.CartActivity;
import com.example.ecommerce.ui.MainActivity;
import com.example.ecommerce.ui.ProductDetailActivity;
import com.example.ecommerce.ui.adapter.ProductAdapter;

import java.util.List;

public class HomeFragment extends Fragment implements ProductAdapter.Listener {

    private FragmentHomeBinding binding;
    private ProductAdapter adapter;
    private ProductRepository productRepository;
    private CartRepository cartRepository;
    private ProductImageRepository imageRepository;
    private User user;

    public static HomeFragment newInstance() {
        return new HomeFragment();
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
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new ProductAdapter(this, imageRepository);
        binding.recyclerProducts.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerProducts.setHasFixedSize(true);
        binding.recyclerProducts.setAdapter(adapter);

        binding.categoryGroup.check(R.id.category_all);
        binding.categoryGroup.setOnCheckedChangeListener((group, checkedId) -> updateProductList());

        binding.searchField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateProductList();
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        updateProductList();
    }

    private void updateProductList() {
        if (productRepository == null) {
            return;
        }

        String searchQuery = binding.searchField.getText().toString().trim();
        int checkedId = binding.categoryGroup.getCheckedChipId();

        String category = null;
        if (checkedId == R.id.category_laptops) {
            category = ProductRepository.CATEGORY_LAPTOPS;
        } else if (checkedId == R.id.category_phones) {
            category = ProductRepository.CATEGORY_PHONES;
        } else if (checkedId == R.id.category_tablets) {
            category = ProductRepository.CATEGORY_TABLETS;
        }

        List<Product> products = productRepository.searchInCategory(category, searchQuery);
        adapter.submitList(products);

        if (products.isEmpty()) {
            binding.emptyState.setVisibility(View.VISIBLE);
        } else {
            binding.emptyState.setVisibility(View.GONE);
        }
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
