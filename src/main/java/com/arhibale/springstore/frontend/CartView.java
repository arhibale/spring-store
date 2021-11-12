package com.arhibale.springstore.frontend;

import com.arhibale.springstore.entity.CartEntity;
import com.arhibale.springstore.entity.OrdersEntity;
import com.arhibale.springstore.service.CartService;
import com.arhibale.springstore.service.OrderService;
import com.arhibale.springstore.util.PersonUtil;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.apache.commons.collections4.CollectionUtils;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.stream.Collectors;

@Route("cart")
@PageTitle("Корзина")
public class CartView extends AbstractView {
    private final Grid<CartEntity.InnerProduct> innerProductGrid = new Grid<>(CartEntity.InnerProduct.class);

    private final CartService cartService;
    private final OrderService orderService;

    private final CartEntity cart;

    public CartView(CartService cartService, OrderService orderService) {
        this.cartService = cartService;
        this.orderService = orderService;

        this.cart = cartService.findLastCart(PersonUtil.getCurrentPerson()).orElse(null);

        initCartView();
    }

    private void initCartView() {
        if (cart != null) {
            if (CollectionUtils.isNotEmpty(cart.getProducts())) {
                innerProductGrid.setSelectionMode(Grid.SelectionMode.NONE);
                innerProductGrid.setSizeUndefined();
                innerProductGrid.setColumns("name", "vendorCode", "price", "count");
                innerProductGrid.setItems(cart.getProducts());

                ListDataProvider<CartEntity.InnerProduct> dataProvider = DataProvider.ofCollection(cart.getProducts());
                innerProductGrid.setDataProvider(dataProvider);

                innerProductGrid.addComponentColumn(this::createDeleteButton);

                var initOrderButton = new Button("Создать заказ", buttonClickEvent -> {
                    var order = new OrdersEntity()
                            .setCartId(cart)
                            .setAddress(Objects.requireNonNull(PersonUtil.getCurrentPerson()).getAddress())
                            .setCost(calculateCost())
                            .setPersonId(PersonUtil.getCurrentPerson());

                    order = orderService.save(order);
                    cart.setOrders(order);
                    cartService.save(cart);
                    Notification.show("Заказ успешно оформлен!");
                    reloadPage();
                });

                add(new H1("Корзина"), innerProductGrid, initOrderButton);
            } else {
                emptyCart();
            }
        } else {
            emptyCart();
        }
    }

    private void reloadPage() {
        UI.getCurrent().getPage().reload();
    }

    private void emptyCart() {
        add(new H1("В корзине пусто!"));
    }

    private BigDecimal calculateCost() {
        var cost = BigDecimal.ZERO;
        for (CartEntity.InnerProduct innerProduct : cart.getProducts()) {
            cost = cost.add(innerProduct.getPrice());
        }
        return cost;
    }

    private HorizontalLayout createDeleteButton(CartEntity.InnerProduct product) {
        //todo cart count!
        var countField = new IntegerField();
        countField.setValue(1);
        countField.setMin(1);
        countField.setHasControls(true);
        return new HorizontalLayout(countField, new Button("Удалить", buttonClickEvent -> deleteProductToTheCart(product)));
    }

    private void deleteProductToTheCart(CartEntity.InnerProduct product) {
        var products = cart.getProducts()
                .stream()
                .filter(p -> !p.getId().equals(product.getId()))
                .collect(Collectors.toList());
        cart.setProducts(products);
        cartService.save(cart);
        Notification.show("Продукт убран из корзины!");
        reloadPage();
    }
}