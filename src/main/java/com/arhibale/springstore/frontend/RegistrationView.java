package com.arhibale.springstore.frontend;

import com.arhibale.springstore.entity.Person;
import com.arhibale.springstore.service.PersonService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.BinderValidationStatus;
import com.vaadin.flow.data.binder.BindingValidationStatus;
import com.vaadin.flow.data.validator.EmailValidator;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.function.SerializablePredicate;
import com.vaadin.flow.router.Route;

import java.util.Optional;
import java.util.stream.Collectors;

@Route("reg")
public class RegistrationView extends VerticalLayout {

    private final PersonService personService;

    public RegistrationView(PersonService personService) {
        this.personService = personService;

        initPage();
    }

    private void initPage() {
        FormLayout formLayout = new FormLayout();
        Binder<Person> binder = new Binder<>();

        TextField firstName = new TextField();
        firstName.setValueChangeMode(ValueChangeMode.EAGER);
        TextField lastName = new TextField();
        lastName.setValueChangeMode(ValueChangeMode.EAGER);
        TextField patronymic = new TextField();
        patronymic.setValueChangeMode(ValueChangeMode.EAGER);
        TextField phone = new TextField();
        phone.setValueChangeMode(ValueChangeMode.EAGER);
        TextField address = new TextField();
        address.setValueChangeMode(ValueChangeMode.EAGER);
        EmailField email = new EmailField();
        email.setValueChangeMode(ValueChangeMode.EAGER);
        PasswordField password = new PasswordField();
        password.setValueChangeMode(ValueChangeMode.EAGER);

        formLayout.addFormItem(firstName, "First Name");
        formLayout.addFormItem(lastName, "Last Name");
        formLayout.addFormItem(patronymic, "Patronymic");
        formLayout.addFormItem(phone, "Phone");
        formLayout.addFormItem(address, "Address");
        formLayout.addFormItem(email, "E-mail");
        formLayout.addFormItem(password, "Password");

        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Button save = new Button("Save");
        Button reset = new Button("Reset");
        horizontalLayout.add(save, reset);

        SerializablePredicate<String> predicate = value ->
                !lastName.getValue().trim().isEmpty()
                || !phone.getValue().trim().isEmpty()
                || !password.getValue().trim().isEmpty();

        Binder.Binding<Person, String> lastNameBinding = binder.forField(lastName)
                        .withValidator(predicate, "Please specify your last name")
                        .bind(Person::getLastName, Person::setLastName);
        Binder.Binding<Person, String> emailBinding = binder.forField(email)
                        .withNullRepresentation("")
                        .withValidator(predicate, "Please specify your email")
                        .withValidator(new EmailValidator("Incorrect email address"))
                        .bind(Person::getEmail, Person::setEmail);
        Binder.Binding<Person, String> phoneBinding = binder.forField(phone)
                        .withValidator(predicate, "Please specify your phone")
                        .bind(Person::getPhone, Person::setPhone);

        lastName.addValueChangeListener(event -> {
            emailBinding.validate();
            phoneBinding.validate();
        });

        email.addValueChangeListener(event -> {
            lastNameBinding.validate();
            phoneBinding.validate();
        });

        phone.addValueChangeListener(event -> {
            lastNameBinding.validate();
            emailBinding.validate();
        });

        Person person = new Person();
        Label infoLabel = new Label();

        save.addClickListener(event -> {
            if (binder.writeBeanIfValid(person)) {
                personService.save(person);
                infoLabel.setText("Saved bean values: " + person);
            } else {
                BinderValidationStatus<Person> validate = binder.validate();
                String errorText = validate.getFieldValidationStatuses().stream()
                        .filter(BindingValidationStatus::isError)
                        .map(BindingValidationStatus::getMessage)
                        .map(Optional::get).distinct()
                        .collect(Collectors.joining(", "));
                infoLabel.setText("There are errors: " + errorText);
            }
        });

        reset.addClickListener(event -> {
            binder.readBean(null);
            infoLabel.setText("");
        });

        add(formLayout, horizontalLayout, infoLabel);
    }
}
