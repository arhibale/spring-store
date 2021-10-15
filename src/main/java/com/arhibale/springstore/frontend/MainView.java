package com.arhibale.springstore.frontend;

import com.arhibale.springstore.entity.Cart;
import com.arhibale.springstore.entity.Person;
import com.arhibale.springstore.entity.Product;
import com.arhibale.springstore.service.CartService;
import com.arhibale.springstore.service.ProductService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.Route;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

@Route("main")
public class MainView extends VerticalLayout {
    private final Grid<Product> productGrid = new Grid<>(Product.class);

    private final ProductService productService;
    private final CartService cartService;

    public MainView(ProductService productService, CartService cartService) {
        this.productService = productService;
        this.cartService = cartService;

        initPage();
    }

    private void initPage() {
        var cartLayout = initCartButton();
        initProductGrid();

        add(productGrid, cartLayout);
    }

    private HorizontalLayout initCartButton() {
        var addToCardButton = new Button("Добавить в корзину", event -> {

            Cart cart = new Cart();
            List<Cart.InnerProduct> innerProducts = new CopyOnWriteArrayList<>();
            for (Product a : productGrid.getSelectedItems()) {
                Cart.InnerProduct innerProduct = new Cart.InnerProduct();
                innerProduct.setName(a.getName());
                innerProduct.setPrice(a.getPrice());
                innerProduct.setCount(a.getCount());
                innerProduct.setVendorCode(a.getVendorCode());
                innerProducts.add(innerProduct);
            }
            cart.setProducts(innerProducts);
            cart.setPersonId(new Person().setId(UUID.fromString("e458d0f5-f717-42ea-ad11-f82d770ea6e0")));
            cart.setId(UUID.fromString("68d364da-31a4-47d1-8c79-f6b84dcc97f3"));
            cart.setCreatedAt(LocalDateTime.now());
            cartService.save(cart);

            Notification.show("Товар успешно добавлен в корзину");
        });

        var toCartButton = new Button("Корзина", event -> UI.getCurrent().navigate("cart"));

        return new HorizontalLayout(addToCardButton, toCartButton);
    }

    private void initProductGrid() {
        var products = productService.getAll();

        productGrid.setItems(products);
        productGrid.setColumns("name", "price", "count", "vendorCode");
        productGrid.setSizeUndefined();
        productGrid.setSelectionMode(Grid.SelectionMode.MULTI);

        ListDataProvider<Product> dataProvider = DataProvider.ofCollection(products);
        productGrid.setDataProvider(dataProvider);

        productGrid.addColumn(new ComponentRenderer<>(item -> {
            var plusButton = new Button("+", i -> {
                item.incrementCount();
                productService.save(item);
                productGrid.getDataProvider().refreshItem(item);
            });

            var minusButton = new Button("-", i -> {
                item.decreaseCount();
                productService.save(item);
                productGrid.getDataProvider().refreshItem(item);
            });

            return new HorizontalLayout(plusButton, minusButton);
        }));
    }
}
