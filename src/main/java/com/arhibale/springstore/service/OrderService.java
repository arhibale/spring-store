package com.arhibale.springstore.service;

import com.arhibale.springstore.entity.CartEntity;
import com.arhibale.springstore.entity.OrdersEntity;
import com.arhibale.springstore.repository.OrderRepository;
import com.arhibale.springstore.util.PersonUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.IOException;
import java.math.BigDecimal;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.Objects;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public OrdersEntity save(OrdersEntity orders) {
        var savedOrder = orderRepository.save(orders);
        try {
            var subject = String.format("Заказ №%s успешно создан!", savedOrder.getId());
            var messageText = String.format("Содержание заказа: \n%s \nЗаказ отправлне на адресс: %s",
                    messageForEmail(savedOrder.getCartId().getProducts(), savedOrder.getCost()), savedOrder.getAddress());

            if (PersonUtil.getCurrentPerson() == null || StringUtils.isBlank(PersonUtil.getCurrentPerson().getEmail())) {
                return savedOrder;
            }

            MailService.sendMail(Objects.requireNonNull(PersonUtil.getCurrentPerson()).getEmail(), messageText, subject);
        } catch (GeneralSecurityException | IOException | MessagingException e) {
            e.printStackTrace();
        }
        return savedOrder;
    }

    private String messageForEmail(List<CartEntity.InnerProduct> products, BigDecimal cost) {
        var stringBuilder = new StringBuilder();
        for (CartEntity.InnerProduct innerProduct : products) {
            stringBuilder
                    .append("Название: ").append(innerProduct.getName())
                    .append("Количество: ").append(innerProduct.getCount())
                    .append("Цена: ").append(innerProduct.getPrice())
                    .append("\n");

        }
        stringBuilder.append("ИТОГОВАЯ ЦЕНА: ").append(cost);
        return stringBuilder.toString();
    }
}
