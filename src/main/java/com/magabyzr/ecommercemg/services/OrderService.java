package com.magabyzr.ecommercemg.services;

import com.magabyzr.ecommercemg.dtos.OrderDto;
import com.magabyzr.ecommercemg.exceptions.OrderNotFoundException;
import com.magabyzr.ecommercemg.mappers.OrderMapper;
import com.magabyzr.ecommercemg.repositories.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
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
        var orders = orderRepository.getOrdersByCustomer(user);
        //3.Map orders to out Dtos and return it.
        return orders.stream().map(orderMapper::toDto).toList();
    }

    public OrderDto getOrder(Long orderId) {
        //1.Find the order by the given id with its items, and a.throw 404 if not found.
        var order = orderRepository
                .getOrderWithItems(orderId)
                .orElseThrow(OrderNotFoundException::new);
        //b.403 forbidden, if it belongs to another user.
        //1.get the current user and check if he placed the order, if not throw a 403 forbidden.
        var user = authService.getCurrentUser();
        if (!order.isPlacedBy(user)) {
            throw new AccessDeniedException("You don´t have access to this order!");
        }
        //c. If everything is ok, 200 ok. Map the order to a dto and return it
        return orderMapper.toDto(order);
    }
}
