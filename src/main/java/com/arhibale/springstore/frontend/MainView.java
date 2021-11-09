package com.arhibale.springstore.frontend;

import com.arhibale.springstore.config.security.CustomUserDetails;
import com.arhibale.springstore.entity.CartEntity;
import com.arhibale.springstore.entity.PersonEntity;
import com.arhibale.springstore.entity.ProductEntity;
import com.arhibale.springstore.repository.filter.ProductFilter;
import com.arhibale.springstore.service.CartService;
import com.arhibale.springstore.service.ProductService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;
import java.util.HashMap;

@Route("main")
@PageTitle("Список продуктов")
public class MainView extends AbstractView {
    private final Grid<ProductEntity> productGrid = new Grid<>(ProductEntity.class);

    private final ProductService productService;
    private final CartService cartService;

    private final IntegerField minPriceTextField = new IntegerField("Минимальная цена");
    private final IntegerField maxPriceTextField = new IntegerField("Максимальная цена");
    private final TextField nameTextField = new TextField("Наименование");

    private final PersonEntity person = ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getDetails()).getPerson();

    public MainView(ProductService productService, CartService cartService) {
        this.productService = productService;
        this.cartService = cartService;

        initPage();
    }

    private void initPage() {
        initProductGrid();

        add(new H1("Список товаров"), productGrid, initFilterLayout());
    }

    private Component initFilterLayout() {
        minPriceTextField.setHasControls(true);
        minPriceTextField.setValue(1);
        minPriceTextField.setMin(0);

        maxPriceTextField.setHasControls(true);
        maxPriceTextField.setValue(1);
        maxPriceTextField.setMin(0);

        var formLayout = new FormLayout();
        formLayout.add(minPriceTextField, maxPriceTextField, nameTextField);
        formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1), new FormLayout.ResponsiveStep("300px", 2));
        formLayout.setColspan(nameTextField, 2);
        formLayout.setMaxWidth(300f, Unit.PIXELS);

        var filterButton = new Button("Отфильтровать", event -> {
            var map = new HashMap<String, String>();
            map.put("minPrice", minPriceTextField.getValue().toString());
            map.put("maxPrice", maxPriceTextField.getValue().toString());
            map.put("name", nameTextField.getValue());

            var productFilter = new ProductFilter(map);
            var filteredProducts = productService.findAllByFilter(productFilter);

            ListDataProvider<ProductEntity> dataProvider = DataProvider.ofCollection(filteredProducts);
            productGrid.setDataProvider(dataProvider);
        });

        var verticalLayout = new VerticalLayout();
        verticalLayout.add(new HorizontalLayout(formLayout), filterButton);
        verticalLayout.setAlignItems(Alignment.CENTER);
        verticalLayout.setJustifyContentMode(JustifyContentMode.CENTER);

        return verticalLayout;
    }

    private void initProductGrid() {
        var products = productService.getAll();

        productGrid.setItems(products);
        productGrid.setColumns("name", "vendorCode", "price");
        productGrid.addComponentColumn(this::createAddButton);
        productGrid.setSelectionMode(Grid.SelectionMode.NONE);
    }

    private HorizontalLayout createAddButton(ProductEntity item) {
        var countField = new IntegerField();
        countField.setValue(1);
        countField.setMin(1);
        countField.setHasControls(true);
        return new HorizontalLayout(countField, new Button("В корзину", buttonClickEvent -> addProductToTheCart(item, countField.getValue())));
    }

    private void addProductToTheCart(ProductEntity product, int count) {
        var person = ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getDetails()).getPerson();
        var cart = cartService.getCartByPersonId(person);
        var productList = cart.getProducts();

        if (CollectionUtils.isNotEmpty(productList)) {
            for (CartEntity.InnerProduct inner : cart.getProducts()) {
                if (inner.getId().equals(String.valueOf(product.getId()))) {
                    inner.setCount(inner.getCount() + count);
                    inner.setPrice(product.getPrice().multiply(new BigDecimal(inner.getCount())));

                    cartService.save(cart);
                    Notification.show("Товар добавлен!");
                    return;
                }
            }
        }

        productList.add(new CartEntity.InnerProduct()
                .setId(String.valueOf(product.getId()))
                .setName(product.getName())
                .setVendorCode(product.getVendorCode())
                .setPrice(product.getPrice().multiply(new BigDecimal(count)))
                .setCount(count));
        cart.setProducts(productList);

        cartService.save(cart);
        Notification.show("Товар добавлен!");
    }
}