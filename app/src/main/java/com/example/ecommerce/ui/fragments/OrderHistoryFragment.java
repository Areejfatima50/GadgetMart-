package com.example.ecommerce.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.ecommerce.data.model.User;
import com.example.ecommerce.data.repository.OrderRepository;
import com.example.ecommerce.data.repository.ProductRepository;
import com.example.ecommerce.databinding.FragmentOrderHistoryBinding;
import com.example.ecommerce.ui.MainActivity;
import com.example.ecommerce.ui.adapter.OrderAdapter;

public class OrderHistoryFragment extends Fragment {

    private FragmentOrderHistoryBinding binding;
    private OrderRepository orderRepository;
    private User user;
    private final OrderAdapter adapter = new OrderAdapter();

    public static OrderHistoryFragment newInstance() {
        return new OrderHistoryFragment();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof MainActivity) {
            MainActivity host = (MainActivity) context;
            orderRepository = host.getOrderRepository();
            user = host.getActiveUser();
        } else {
            ProductRepository productRepository = new ProductRepository(context);
            orderRepository = new OrderRepository(context, productRepository);
            user = null;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentOrderHistoryBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.recyclerOrders.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerOrders.setHasFixedSize(true);
        binding.recyclerOrders.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (orderRepository != null && user != null) {
            adapter.submitList(orderRepository.getOrders(user.getEmail()));
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

