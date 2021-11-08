package com.arhibale.springstore.frontend;

import com.arhibale.springstore.entity.ProductEntity;
import com.arhibale.springstore.service.ProductService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.function.SerializablePredicate;
import com.vaadin.flow.router.Route;

import java.math.BigDecimal;

@Route("add-product")
public class AddProductView extends AbstractView {
    private final ProductService productService;

    public AddProductView(ProductService productService) {
        this.productService = productService;

        initProductView();
    }

    private void initProductView() {
        var nameTextField = new TextField("Имя продукта");
        var priceTextField = new TextField("Цена продукта");
        var countTextField = new TextField("Количество");
        var vendorCodeTextField = new TextField("Артикул");

        var formLayout = new FormLayout();
        formLayout.add(nameTextField, priceTextField, countTextField, vendorCodeTextField);
        formLayout.setMaxWidth(500f, Unit.PIXELS);

        SerializablePredicate<String> predicate = value ->
                        !nameTextField.getValue().trim().isEmpty() ||
                        !priceTextField.getValue().trim().isEmpty() ||
                        !countTextField.getValue().trim().isEmpty() ||
                        !vendorCodeTextField.getValue().trim().isEmpty();

        Binder<ProductEntity> binder = new Binder<>();
        binder.forField(nameTextField)
                .withValidator(predicate, "Пустое имя!")
                .withValidator(name -> name.matches("[а-яА-ЯЁё0-9]{1,128}+"), "Неккоректное имя!")
                .bind(ProductEntity::getName, ProductEntity::setName);
        binder.forField(vendorCodeTextField)
                .withValidator(predicate, "Пустой пртикул!")
                .withValidator(vendorCode -> vendorCode.matches(".{1,64}+"), "Неккоректный пртикул!")
                .bind(ProductEntity::getVendorCode, ProductEntity::setVendorCode);
        binder.forField(priceTextField)
                .withValidator(predicate, "Цена не может быть пустым!")
                .withValidator(price -> price.matches("[0-9]{1,8}+"), "Неккорекная цена!");
        binder.forField(countTextField)
                .withValidator(predicate, "Количество не может быть пустым!");

        var createProductButton = new Button("Создать", buttonClickEvent -> {
            try {
                var product = new ProductEntity();
                if (binder.writeBeanIfValid(product)) {
                    product.setPrice(new BigDecimal(priceTextField.getValue()));
                    product.setCount(Integer.parseInt(countTextField.getValue()));
                    productService.save(product);
                    Notification.show("Продукт создан!");
                    UI.getCurrent().navigate(MainView.class);
                } else {
                    Notification.show("Есть незаполненные поля!");
                }
            } catch (Exception e) {
                e.printStackTrace();
                Notification.show("Произошла ошибка!");
            }
        });

        add(new H1("Создание продукта"), new HorizontalLayout(formLayout), createProductButton);
    }
}
