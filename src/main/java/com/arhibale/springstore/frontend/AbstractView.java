package com.arhibale.springstore.frontend;

import com.arhibale.springstore.config.security.CustomUserDetails;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractView extends VerticalLayout {
    private static final SimpleGrantedAuthority ADMIN_ROLE = new SimpleGrantedAuthority("admin");
    private static final SimpleGrantedAuthority SELLER_ROLE = new SimpleGrantedAuthority("seller");
    private static final SimpleGrantedAuthority MANAGER_ROLE = new SimpleGrantedAuthority("manager");

    public AbstractView() {
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        Button logoutButton = new Button("Выйти", buttonClickEvent -> {
            SecurityContextHolder.clearContext();
            UI.getCurrent().navigate(LoginView.class);
        });
        Button mainPageButton = new Button("На главную", buttonClickEvent -> UI.getCurrent().navigate(MainView.class));
        Button toCartButton = new Button("Корзина", buttonClickEvent -> UI.getCurrent().navigate(""));

        var h1 = new HorizontalLayout();
        h1.add(mainPageButton, toCartButton);
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
        var details = SecurityContextHolder.getContext().getAuthentication().getDetails();
        var buttons = new ArrayList<Button>();

        if (details instanceof CustomUserDetails) {
            if (((CustomUserDetails) details).getAuthorities().contains(ADMIN_ROLE)) {
                buttons.add(new Button("Список пользователей", buttonClickEvent -> UI.getCurrent().navigate(PersonListView.class)));
            }
            var detail = ((CustomUserDetails) details).getAuthorities();
            if (detail.contains(ADMIN_ROLE) || detail.contains(SELLER_ROLE) || detail.contains(MANAGER_ROLE)) {
                buttons.add(new Button("Добавить товар", buttonClickEvent -> UI.getCurrent().navigate(AddProductView.class)));
            }
        }
        return buttons;
    }
}