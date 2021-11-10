package com.arhibale.springstore.frontend;

import com.arhibale.springstore.entity.CartEntity;
import com.arhibale.springstore.entity.PersonEntity;
import com.arhibale.springstore.service.CartService;
import com.arhibale.springstore.service.OrderService;
import com.arhibale.springstore.service.PersonService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.validator.EmailValidator;
import com.vaadin.flow.function.SerializablePredicate;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.ArrayList;

@Route("registration")
@PageTitle("Регистрация")
public class RegistrationView extends AbstractView {

    private final PersonService personService;
    private final CartService cartService;
    private final OrderService orderService;

    public RegistrationView(PersonService personService, CartService cartService, OrderService orderService) {
        this.personService = personService;
        this.cartService = cartService;
        this.orderService = orderService;

        initRegistrationView();
    }

    private void initRegistrationView() {
        var firstNameTextField = new TextField("Имя");
        var lastNameTextField = new TextField("Фамилия");
        var patronymicTextField = new TextField("Отчество");
        var phoneTextField = new TextField("Номер телефона");
        var addressTextField = new TextField("Адрес");
        var emailTextField = new EmailField("E-mail");
        var loginTextField = new TextField("Логин");
        var passwordTextField = new PasswordField("Пароль");

        var formLayout = new FormLayout();
        formLayout.add(firstNameTextField, lastNameTextField, patronymicTextField, phoneTextField, emailTextField, addressTextField, loginTextField, passwordTextField);
        formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1), new FormLayout.ResponsiveStep("500px", 2));
        formLayout.setColspan(patronymicTextField, 2);
        formLayout.setColspan(addressTextField, 2);
        formLayout.setMaxWidth(500f, Unit.PIXELS);

        SerializablePredicate<String> predicate = value ->
                        !firstNameTextField.getValue().trim().isEmpty() ||
                        !lastNameTextField.getValue().trim().isEmpty() ||
                        !patronymicTextField.getValue().trim().isEmpty() ||
                        !phoneTextField.getValue().trim().isEmpty() ||
                        !addressTextField.getValue().isEmpty() ||
                        !emailTextField.getValue().trim().isEmpty() ||
                        !loginTextField.getValue().trim().isEmpty() ||
                        !passwordTextField.getValue().trim().isEmpty();

        Binder<PersonEntity> binder = new Binder<>();

        binder.forField(firstNameTextField)
                .withValidator(predicate, "Пустое имя!")
                .withValidator(firstName -> firstName.matches("[а-яА-ЯЁё]{1,128}+"), "Неккоректное имя!")
                .bind(PersonEntity::getFirstName, PersonEntity::setFirstName);
        binder.forField(lastNameTextField)
                .withValidator(predicate, "Пустая фамилия!")
                .withValidator(lastName -> lastName.matches("[а-яА-ЯЁё]{1,128}+"), "Неккоректная фамилия!")
                .bind(PersonEntity::getLastName, PersonEntity::setLastName);
        binder.forField(patronymicTextField)
                .withValidator(predicate, "Пустое отчество!")
                .withValidator(patronymic -> patronymic.matches("[а-яА-ЯЁё]{1,128}+"), "Неккоректное отчество!")
                .bind(PersonEntity::getPatronymic, PersonEntity::setPatronymic);
        binder.forField(phoneTextField)
                .withValidator(predicate, "Пустой номер телефона!")
                .withValidator(phone -> phone.matches("\\d{11,14}+"), "Неккоректный номер телефона!")
                .bind(PersonEntity::getPhone, PersonEntity::setPhone);
        binder.forField(addressTextField)
                .withValidator(predicate, "Пустой адрес!")
                .withValidator(address -> address.matches(".{1,1024}+"), "Слишком большой или короткий адрес!")
                .bind(PersonEntity::getAddress, PersonEntity::setAddress);
        binder.forField(emailTextField)
                .withValidator(predicate, "Нустой e-mail!")
                .withValidator(new EmailValidator("Неккоректный e-mail!"))
                .bind(PersonEntity::getEmail, PersonEntity::setEmail);
        binder.forField(loginTextField)
                .withValidator(predicate, "Пустой логин!")
                .withValidator(login -> login.matches("[a-zA-Z0-9]{1,256}+"), "Неккоректный логин!")
                .bind(PersonEntity::getLogin, PersonEntity::setLogin);
        binder.forField(passwordTextField)
                .withValidator(predicate, "Пустой пароль!")
                .withValidator(password -> password.matches("(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,256}+"),
                        "Пароль должен содержать по крайней мере одну цифру и одну прописную и строчную букву, а также не менее 8 или более символов!")
                .bind(PersonEntity::getPassword, PersonEntity::setPassword);

        var registrationButton = new Button("Регистрация" , buttonClickEvent -> {
            try {
                var person = new PersonEntity();
                if (binder.writeBeanIfValid(person)) {
                    person.setRole("customer");
                    personService.save(person);

                    var cart = new CartEntity();
                    cart.setPersonId(person);
                    cart.setProducts(new ArrayList<>());
                    cartService.save(cart);

                    Notification.show("Регистрация прошла успешно!");
                    UI.getCurrent().navigate(LoginView.class);
                } else {
                    Notification.show("Есть незаполненные поля!");
                }
            } catch (Exception e) {
                e.printStackTrace();
                Notification.show("Произошла ошибка!");
            }
        });

        add(new H1("Регистрация"), new HorizontalLayout(formLayout), registrationButton);
    }
}