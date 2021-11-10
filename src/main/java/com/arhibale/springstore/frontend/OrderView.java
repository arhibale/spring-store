package com.arhibale.springstore.frontend;

import com.arhibale.springstore.config.security.CustomUserDetails;
import com.arhibale.springstore.entity.CartEntity;
import com.arhibale.springstore.entity.OrdersEntity;
import com.arhibale.springstore.service.OrderService;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.details.DetailsVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.treegrid.TreeGrid;
import com.vaadin.flow.data.renderer.TemplateRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.List;

@Route("order")
@PageTitle("Мои заказы")
public class OrderView extends AbstractView {
    private final OrderService orderService;

    public OrderView(OrderService orderService) {
        this.orderService = orderService;

        initPage();
    }

    private void initPage() {
        var person = ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getDetails()).getPerson();
        var orders = orderService.getOrdersByPersonId(person);


        if (CollectionUtils.isNotEmpty(orders)) {
            var accordion = new Accordion();
            accordion.setWidth(500f, Unit.PIXELS);

            for (OrdersEntity order : orders) {
                accordion.add("Заказ №" + order.getId(),
                                new Span(getCart(order.getCartId())))
                                .addThemeVariants(DetailsVariant.FILLED);
            }

            add(new H1("Мои заказы"), accordion);
        } else {
            add(new H1("Заказов нет!"));
        }
    }

    private String getCart(CartEntity cartId) {
        var builder = new StringBuilder();
        var products = cartId.getProducts();

        for (CartEntity.InnerProduct product: products) {
            builder.append("Название: ").append(product.getName())
                    .append(" Цена: ").append(product.getPrice())
                    .append(" Количество: ").append(product.getCount())
                    .append("\n");
        }

        return builder.toString();
    }
}
