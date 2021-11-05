package com.arhibale.springstore.frontend;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route("main")
@PageTitle("Home")
public class MainView extends VerticalLayout {

    public MainView() {
        initPage();
    }

    private void initPage() {
        HorizontalLayout horizontalLayout = initCartButton();
        add(horizontalLayout);
    }

    private HorizontalLayout initCartButton() {
        var toRegistrationButton = new Button("Registration", event -> UI.getCurrent().navigate("registration"));
        return new HorizontalLayout(toRegistrationButton);
    }
}
