package com.arhibale.springstore.frontend;

import com.arhibale.springstore.entity.PersonEntity;
import com.arhibale.springstore.service.PersonService;
import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route("person-list-view")
@PageTitle("Список пользователей")
public class PersonListView extends AbstractView {
    private final  PersonService personService;
    private final Grid<PersonEntity> personGrid = new Grid<>(PersonEntity.class);

    public PersonListView(PersonService personService) {
        this.personService = personService;

        initPersonGrid();

        add(new H1("Список пользователей"), personGrid);
    }

    private void initPersonGrid() {
        var person = personService.findAll();

        personGrid.setItems(person);
        personGrid.setColumns("login", "firstName", "lastName", "phone", "email");
        personGrid.setSizeUndefined();
        personGrid.setSelectionMode(Grid.SelectionMode.MULTI);

        ListDataProvider<PersonEntity> dataProvider = DataProvider.ofCollection(person);
        personGrid.setDataProvider(dataProvider);

        personGrid.addColumn(new ComponentRenderer<>(item -> {
            var disable = new Checkbox(item.isDisabled());
            disable.addValueChangeListener((HasValue.ValueChangeListener<AbstractField.ComponentValueChangeEvent<Checkbox, Boolean>>)
                    checkboxBooleanComponentValueChangeEvent -> {
                        item.setDisabled(disable.getValue());
                        personService.update(item);
                    });
            return new HorizontalLayout(disable);
        }));
    }
}
                                                                                                                                                        