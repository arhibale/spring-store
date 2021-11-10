package com.arhibale.springstore.frontend;

import com.arhibale.springstore.config.security.CustomUserDetails;
import com.arhibale.springstore.entity.CartEntity;
import com.arhibale.springstore.entity.OrdersEntity;
import com.arhibale.springstore.service.CartService;
import com.arhibale.springstore.service.MailService;
import com.arhibale.springstore.service.OrderService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.mail.MessagingException;
import java.io.IOException;
import java.math.BigDecimal;
import java.security.GeneralSecurityException;
import java.util.List;

@Route("cart")
@PageTitle("Корзина")
public class CartView extends AbstractView {
    private final Grid<CartEntity.InnerProduct> cartGrid = new Grid<>(CartEntity.InnerProduct.class);

    private final CartService cartService;
    private final OrderService orderService;
    private final MailService mailService;

    public CartView(CartService cartService, OrderService orderService, MailService mailService) {
        this.cartService = cartService;
        this.orderService = orderService;
        this.mailService = mailService;

        initPage();
    }

    /*
    1) Создать кнопку "Создать заказ" на странице "Корзина"
    2) Создается сущность Order и отправляется письмо на почту с информацией о заказе
    3) Создать страницу "Мои заказы" с доступом по всему приложению(AbstractView)
     */

    private void initPage() {
        var person = ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getDetails()).getPerson();
        var cart = cartService.getCartByPersonId(person);

        if (CollectionUtils.isNotEmpty(cart.getProducts())) {
            cartGrid.setItems(cart.getProducts());
            cartGrid.setColumns("name", "vendorCode", "price", "count");
            cartGrid.setSizeUndefined();
            cartGrid.setSelectionMode(Grid.SelectionMode.NONE);

            Button createOrderButton = new Button("Оформить заказ", buttonClickEvent -> {
                var order = new OrdersEntity();
                order.setCartId(cart);
                order.setCost(getCostByProducts(cart.getProducts()));
                order.setPersonId(person);
                order.setAddress(person.getAddress());
                orderService.save(order);

                try {
                    mailService.sendMail(person, getCart(cart));
                    Notification.show("Заказ оформлен! Письмо отправленно на почту!");
                } catch (GeneralSecurityException | MessagingException | IOException e) {
                    e.printStackTrace();
                }
            });

            add(new H1("Корзина"), cartGrid, createOrderButton);
        } else {
            add(new H1("В корзине пусто!"));
        }
    }

    private BigDecimal getCostByProducts(List<CartEntity.InnerProduct> products) {
        var cost = new BigDecimal("0");
        for (CartEntity.InnerProduct product: products) {
            cost = cost.add(product.getPrice());
        }
        return cost;
    }

    private String getCart(CartEntity cartId) {
        var builder = new StringBuilder();
        var products = cartId.getProducts();

        for (CartEntity.InnerProduct product: products) {
            builder.append("Название: ").append(product.getName())
                    .append(" Цена: ").append(product.getPrice())
                    .append(" Количество: ").append(product.getCount())
                    .append("\n");
        }

        return builder.toString();
    }
}