package com.arhibale.springstore.frontend;

import com.arhibale.springstore.util.PersonUtil;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractView extends VerticalLayout {
    private static final String ADMIN_ROLE = "admin";
    private static final String SELLER_ROLE = "seller";
    private static final String MANAGER_ROLE = "manager";

    public AbstractView() {
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        var logoutButton = new Button("Выйти", buttonClickEvent -> {
            SecurityContextHolder.clearContext();
            UI.getCurrent().navigate(LoginView.class);
        });
        var mainPageButton = new Button("На главную", buttonClickEvent -> UI.getCurrent().navigate(MainView.class));
        var toOrdersButton = new Button("Мои заказы", buttonClickEvent -> UI.getCurrent().navigate(OrderView.class));
        var toCartButton = new Button("Корзина", buttonClickEvent -> UI.getCurrent().navigate(CartView.class));

        var h1 = new HorizontalLayout();
        h1.add(mainPageButton, toOrdersButton, toCartButton);
        var adminButtons = initSpecialButtons();
        if (CollectionUtils.isNotEmpty(adminButtons)) {
            for (Button button : adminButtons) {
                h1.add(button);
            }
        }
        h1.add(logoutButton);
        add(h1);
    }

    private List<Button> initSpecialButtons() {
        var buttons = new ArrayList<Button>();

        if (PersonUtil.isRole(ADMIN_ROLE)) {
            buttons.add(new Button("Список пользователей", buttonClickEvent -> UI.getCurrent().navigate(PersonListView.class)));
        }
        if (PersonUtil.isRole(ADMIN_ROLE) || PersonUtil.isRole(SELLER_ROLE) || PersonUtil.isRole(MANAGER_ROLE)) {
            buttons.add(new Button("Добавить товар", buttonClickEvent -> UI.getCurrent().navigate(AddProductView.class)));
        }

        return buttons;
    }
}