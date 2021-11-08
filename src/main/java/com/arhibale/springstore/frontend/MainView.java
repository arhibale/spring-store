package com.arhibale.springstore.frontend;

import com.arhibale.springstore.entity.ProductEntity;
import com.arhibale.springstore.repository.filter.ProductFilter;
import com.arhibale.springstore.service.ProductService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.HashMap;

@Route("main")
@PageTitle("Home")
public class MainView extends AbstractView {
    private final Grid<ProductEntity> productGrid = new Grid<>(ProductEntity.class);

    private final ProductService productService;

    private final TextField minPriceTextField = new TextField();
    private final TextField maxPriceTextField = new TextField();
    private final TextField nameTextField = new TextField();

    public MainView(ProductService productService) {
        this.productService = productService;

        initPage();
    }

    private void initPage() {
        initProductGrid();

        add(productGrid, initFilterLayout(), initCartButton());
    }

    private Component initFilterLayout() {
        var vl = new VerticalLayout();
        vl.setAlignItems(Alignment.CENTER);
        vl.setJustifyContentMode(JustifyContentMode.CENTER);

        minPriceTextField.setPlaceholder("Минимальная цена");
        maxPriceTextField.setPlaceholder("Максимальная цена");
        nameTextField.setPlaceholder("Наименование");

        vl.add(minPriceTextField, maxPriceTextField, nameTextField);

        return vl;
    }

    private HorizontalLayout initCartButton() {
        var addToCardButton = new Button("Добавить в корзину", event -> {
            //TODO: Сохранение в бд какому-либо пользователю
            Notification.show("Товар успешно добавлен в корзину");
        });

        var filterButton = new Button("Отфильтровать", event -> {
            var map = new HashMap<String, String>();
            map.put("minPrice", minPriceTextField.getValue());
            map.put("maxPrice", maxPriceTextField.getValue());
            map.put("name", nameTextField.getValue());

            var productFilter = new ProductFilter(map);
            var filteredProducts = productService.findAllByFilter(productFilter);

            ListDataProvider<ProductEntity> dataProvider = DataProvider.ofCollection(filteredProducts);
            productGrid.setDataProvider(dataProvider);
        });

        return new HorizontalLayout(addToCardButton, filterButton);
    }

    private void initProductGrid() {
        var products = productService.getAll();

        productGrid.setItems(products);
        productGrid.setColumns("name", "price", "count", "vendorCode");
        productGrid.setSizeUndefined();
        productGrid.setSelectionMode(Grid.SelectionMode.MULTI);

        ListDataProvider<ProductEntity> dataProvider = DataProvider.ofCollection(products);
        productGrid.setDataProvider(dataProvider);

        productGrid.addColumn(new ComponentRenderer<>(item -> {
            var plusButton = new Button("+", i -> {
                //productService.save(item);
                productGrid.getDataProvider().refreshItem(item);
            });

            var minusButton = new Button("-", i -> {
                //productService.save(item);
                productGrid.getDataProvider().refreshItem(item);
            });

            return new HorizontalLayout(plusButton, minusButton);
        }));
    }
}