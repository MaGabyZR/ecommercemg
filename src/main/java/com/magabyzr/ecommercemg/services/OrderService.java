package com.magabyzr.ecommercemg.services;

import com.magabyzr.ecommercemg.dtos.OrderDto;
import com.magabyzr.ecommercemg.mappers.OrderMapper;
import com.magabyzr.ecommercemg.repositories.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class OrderService {
    private final AuthService authService;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    public List<OrderDto> getAllOrders(){
        //1.Find the current authenticated user.
        var user = authService.getCurrentUser();
        //2.Find the orders placed by this user.
        var orders = orderRepository.getAllByCustomer(user);
        //3.Map orders to out Dtos and return it.
        return orders.stream().map(orderMapper::toDto).toList();


    }
}
