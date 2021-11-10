package com.arhibale.springstore.service;

import com.arhibale.springstore.entity.OrdersEntity;
import com.arhibale.springstore.entity.PersonEntity;
import com.arhibale.springstore.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }


    public OrdersEntity save(OrdersEntity orders) {
        return orderRepository.save(orders);
    }

    public List<OrdersEntity> getOrdersByPersonId(PersonEntity person) {
        return orderRepository.findOrdersEntitiesByPersonId(person);
    }
}
