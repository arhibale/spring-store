package com.arhibale.springstore.frontend;

import com.arhibale.springstore.entity.OrdersEntity;
import com.arhibale.springstore.service.OrderService;
import com.arhibale.springstore.util.PersonUtil;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.details.DetailsVariant;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.apache.commons.collections4.CollectionUtils;

@Route("order")
@PageTitle("Мои заказы")
public class OrderView extends AbstractView {
    private final OrderService orderService;

    public OrderView(OrderService orderService) {
        this.orderService = orderService;

        initPage();
    }

    private void initPage() {
        var orders = orderService.getOrdersByPersonId(PersonUtil.getCurrentPerson());

        if (CollectionUtils.isNotEmpty(orders)) {
            var accordion = new Accordion();
            accordion.setWidth(500f, Unit.PIXELS);

            for (OrdersEntity ordersEntity: orders) {
                accordion.add("Заказ №" + ordersEntity.getId(),
                        new Text("Цена заказа: " + ordersEntity.getCost()
                                + ", отправленно на адрес: " + ordersEntity.getAddress()
                                + ", дата заказа: " + ordersEntity.getCreatedAt()))
                        .addThemeVariants(DetailsVariant.FILLED);
            }
            add(new H1("Мои заказы"), accordion);
        } else {
            add(new H1("Заказов нет!"));
        }
    }
}
