package com.arhibale.springstore.frontend;

import com.arhibale.springstore.entity.ProductEntity;
import com.arhibale.springstore.service.ProductService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.function.SerializablePredicate;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route("add-product")
@PageTitle("Добавить новый продукт")
public class AddProductView extends AbstractView {
    private final ProductService productService;

    public AddProductView(ProductService productService) {
        this.productService = productService;

        initProductView();
    }

    private void initProductView() {
        var nameTextField = new TextField("Имя продукта");
        var vendorCodeTextField = new TextField("Артикул");

        var countTextField = new IntegerField("Количество");
        countTextField.setHasControls(true);
        countTextField.setMin(1);
        countTextField.setValue(1);

        var priceTextField = new BigDecimalField("Цена продукта");
        var rubPrefix = new Div();
        rubPrefix.setText("₽");
        priceTextField.setPrefixComponent(rubPrefix);

        var formLayout = new FormLayout();
        formLayout.add(nameTextField, vendorCodeTextField, priceTextField, countTextField);
        formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("400px", 2));
        formLayout.setMaxWidth(500f, Unit.PIXELS);

        SerializablePredicate<String> predicate = value ->
                        !nameTextField.getValue().trim().isEmpty() ||
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
                .withValidator(price -> price.intValue() > 0, "Цена не может быть отрицательно или равна нулю!")
                .bind(ProductEntity::getPrice, ProductEntity::setPrice);
        binder.forField(countTextField)
                .withValidator(count -> count > 0, "Количество не может быть отрицательным или равно нулю!")
                .bind(ProductEntity::getCount, ProductEntity::setCount);

        var createProductButton = new Button("Создать", buttonClickEvent -> {
            try {
                var product = new ProductEntity();
                if (binder.writeBeanIfValid(product)) {
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
