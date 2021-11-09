package com.arhibale.springstore.frontend;

import com.arhibale.springstore.config.security.CustomUserDetails;
import com.arhibale.springstore.entity.CartEntity;
import com.arhibale.springstore.service.CartService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.security.core.context.SecurityContextHolder;

@Route("cart")
@PageTitle("Корзина")
public class CartView extends AbstractView {
    private final Grid<CartEntity.InnerProduct> cartGrid = new Grid<>(CartEntity.InnerProduct.class);

    private final CartService cartService;

    public CartView(CartService cartService) {
        this.cartService = cartService;

        initPage();
    }

    private void initPage() {
        var person = ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getDetails()).getPerson();
        var cart = cartService.getCartByPersonId(person);

        if (CollectionUtils.isNotEmpty(cart.getProducts())) {
            cartGrid.setItems(cart.getProducts());
            cartGrid.setColumns("name", "vendorCode", "price", "count");
            cartGrid.setSizeUndefined();
            cartGrid.setSelectionMode(Grid.SelectionMode.NONE);

            Button createOrderButton = new Button("Оформить заказ", buttonClickEvent -> {
                Notification.show("Заказ оформлен!");
            });

            add(new H1("Корзина"), cartGrid, createOrderButton);
        } else {
            add(new H1("В корзине пусто!"));
        }
    }
}