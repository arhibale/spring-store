package com.arhibale.springstore.frontend;

import com.arhibale.springstore.entity.Cart;
import com.arhibale.springstore.service.CartService;
import com.arhibale.springstore.service.PersonService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

import java.util.UUID;

@Route("cart")
public class CartView extends VerticalLayout {
    private final Grid<Cart.InnerProduct> cartGrid = new Grid<>(Cart.InnerProduct.class);

    private final CartService cartService;
    private final PersonService personService;

    public CartView(CartService cartService, PersonService personService) {
        this.cartService = cartService;
        this.personService = personService;

        initPage();
    }

    private void initPage() {
        var cartLayout = initCartButton();
        initCartGrid();

        add(cartGrid, cartLayout);
    }

    private HorizontalLayout initCartButton() {
        var toCartButton = new Button("Main", event -> UI.getCurrent().navigate("main"));

        return new HorizontalLayout(toCartButton);
    }

    private void initCartGrid() {
        var cart = cartService.getCartByPerson(
                personService.getPersonById(
                        UUID.fromString("e458d0f5-f717-42ea-ad11-f82d770ea6e0")
                ));
        cartGrid.setItems(cart.getProducts());
        cartGrid.setColumns("name", "count", "price", "vendorCode");
        cartGrid.setSizeUndefined();
    }
}
