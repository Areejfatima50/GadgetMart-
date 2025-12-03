package com.example.ecommerce.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ecommerce.data.model.Product;
import com.example.ecommerce.data.model.User;
import com.example.ecommerce.data.repository.AuthRepository;
import com.example.ecommerce.data.repository.CartRepository;
import com.example.ecommerce.data.repository.ProductImageRepository;
import com.example.ecommerce.data.repository.ProductRepository;
import com.example.ecommerce.databinding.ActivityProductDetailBinding;

import java.text.NumberFormat;
import java.util.Locale;

public class ProductDetailActivity extends AppCompatActivity {

    public static final String EXTRA_PRODUCT_ID = "extra_product_id";

    private ActivityProductDetailBinding binding;
    private ProductRepository productRepository;
    private CartRepository cartRepository;
    private ProductImageRepository imageRepository;
    private AuthRepository authRepository;
    private Product product;
    private final NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(Locale.getDefault());

    private final ActivityResultLauncher<String[]> imagePicker =
            registerForActivityResult(new ActivityResultContracts.OpenDocument(), this::handleImagePicked);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProductDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        productRepository = new ProductRepository(this);
        cartRepository = new CartRepository(this, productRepository);
        imageRepository = new ProductImageRepository(this);
        authRepository = new AuthRepository(this);

        setSupportActionBar(binding.toolbar);
        binding.toolbar.setNavigationOnClickListener(v -> finish());

        String productId = getIntent().getStringExtra(EXTRA_PRODUCT_ID);
        product = productRepository.getById(productId);
        if (product == null) {
            Toast.makeText(this, "Product not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        bindProduct();

        binding.btnAddToCart.setOnClickListener(v -> addToCart());
        binding.btnCheckout.setOnClickListener(v -> startActivity(new Intent(this, CartActivity.class)));
        binding.btnUploadImage.setOnClickListener(v ->
                imagePicker.launch(new String[]{"image/*"}));
    }

    private void bindProduct() {
        binding.productTitle.setText(product.getName());
        binding.productCategory.setText(product.getCategory());
        binding.productPrice.setText(currencyFormatter.format(product.getPrice()));
        binding.productDescription.setText(product.getDescription());
        binding.productSpecs.setText(product.getDetails());
        binding.productRating.setText(String.format(Locale.getDefault(), "%.1f ★ rating", product.getRating()));

        Uri custom = imageRepository.getImage(product.getId());
        if (custom != null) {
            binding.productImage.setImageURI(custom);
        } else {
            binding.productImage.setImageResource(product.getImageRes());
        }
    }

    private void addToCart() {
        User user = authRepository.getActiveUser();
        if (user == null) {
            startActivity(new Intent(this, SignInActivity.class));
            return;
        }
        cartRepository.addToCart(user.getEmail(), product);
        Toast.makeText(this, "Added to cart", Toast.LENGTH_SHORT).show();
    }

    private void handleImagePicked(Uri uri) {
        if (uri == null || product == null) return;
        try {
            getContentResolver().takePersistableUriPermission(uri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } catch (SecurityException ignored) {
        }
        imageRepository.saveImage(product.getId(), uri);
        binding.productImage.setImageURI(uri);
    }
}
