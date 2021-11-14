package com.arhibale.springstore.frontend;

import com.arhibale.springstore.entity.CartEntity;
import com.arhibale.springstore.entity.ProductEntity;
import com.arhibale.springstore.repository.filter.ProductFilter;
import com.arhibale.springstore.service.CartService;
import com.arhibale.springstore.service.ProductService;
import com.arhibale.springstore.util.PersonUtil;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
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
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteParameters;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Route("main")
@PageTitle("Список продуктов")
public class MainView extends AbstractView {
    private final Grid<ProductEntity> productGrid = new Grid<>(ProductEntity.class);

    private final ProductService productService;
    private final CartService cartService;

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
        var minPriceTextField = new IntegerField("Минимальная цена");
        var maxPriceTextField = new IntegerField("Максимальная цена");
        var nameTextField = new TextField("Наименование");

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
        productGrid.addComponentColumn(this::createButton);
        productGrid.setSelectionMode(Grid.SelectionMode.NONE);
    }

    private HorizontalLayout createButton(ProductEntity product) {
        var countField = new IntegerField();
        countField.setValue(1);
        countField.setMin(1);
        countField.setHasControls(true);
        return new HorizontalLayout(countField,
                new Button("В корзину", buttonClickEvent -> addProductToTheCart(product, countField.getValue())),
                new Button("Открыть отзывы", buttonClickEvent -> {
                    Map<String, List<String>> queryParams = new HashMap<>();
                    queryParams.put("product", List.of(product.getId().toString()));
                    var queryParameters = new QueryParameters(queryParams);
                    UI.getCurrent().navigate("review", queryParameters);
                }));
    }

    private void addProductToTheCart(ProductEntity product, int count) {
        var innerProduct = new CartEntity.InnerProduct()
                .setId(product.getId().toString())
                .setPrice(product.getPrice().multiply(new BigDecimal(count)))
                .setName(product.getName())
                .setCount(count)
                .setVendorCode(product.getVendorCode());
        var cartOptional = cartService.findLastCart(PersonUtil.getCurrentPerson());
        CartEntity cart;
        if (cartOptional.isPresent()) {
            cart = cartOptional.get();
            cart.addProduct(innerProduct);
        } else {
            cart = new CartEntity()
                    .setPersonId(PersonUtil.getCurrentPerson())
                    .setProducts(List.of(innerProduct));
        }
        cartService.save(cart);
        Notification.show("Продукт добавлен в корзину!");
    }
}