package com.magabyzr.ecommercemg.orders;

import com.magabyzr.ecommercemg.carts.Cart;
import com.magabyzr.ecommercemg.users.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private User customer;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    @Column(name = "created_at", insertable = false, updatable = false)                         //To tell Hibernate we assigned a default value in our db and there is no need to assign a value.
    private LocalDateTime createdAt;

    @Column(name = "total_price")
    private BigDecimal totalPrice;

    @OneToMany(mappedBy = "order", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private Set<OrderItem> items = new LinkedHashSet<>();

    //define a method for creating and order from a shopping cart.
    public static Order fromCart(Cart cart, User customer){
        //c.create an order, save it, clear the cart and return 200 ok
        var order = new Order();
        order.setCustomer(customer);
        order.setStatus(PaymentStatus.PENDING);
        order.setTotalPrice(cart.getTotalPrice());

        //convert each cart item to and order item.
        cart.getItems().forEach(item -> {
            //1. create an order item
            var orderItem = new OrderItem(order, item.getProduct(), item.getQuantity());
            //2. add it to our order.
            order.items.add(orderItem);
        });

        return order;
    }
    //Verify which user placed an order.
    public boolean isPlacedBy(User customer){
        return this.customer.equals(customer);
    }

}