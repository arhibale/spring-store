package com.arhibale.springstore.frontend;

import com.arhibale.springstore.config.security.CustomUserDetails;
import com.arhibale.springstore.config.security.CustomUserDetailsService;
import com.arhibale.springstore.integration.KeycloakIntegration;
import com.arhibale.springstore.util.DecodeJwtToken;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.BearerTokenAuthenticationToken;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Route("login")
@PageTitle("Авторизация")
public class LoginView extends VerticalLayout implements BeforeEnterObserver {

    private final LoginForm loginForm = new LoginForm();
    private final KeycloakIntegration keycloakIntegration;
    private final CustomUserDetailsService customUserDetailsService;

    public LoginView(KeycloakIntegration keycloakIntegration, CustomUserDetailsService customUserDetailsService) {
        this.keycloakIntegration = keycloakIntegration;
        this.customUserDetailsService = customUserDetailsService;
        initLoginView();
    }

    private void initLoginView() {
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        loginByJwtListener();

        var registrationButton = new Button("Регистрация", buttonClickEvent -> UI.getCurrent().navigate(RegistrationView.class));

        add(new H1("ArhiBale Shop"), loginForm, registrationButton);
    }

    private void loginByJwtListener() {
        loginForm.addLoginListener(loginEvent -> {
            var bearerToken = keycloakIntegration.authorize(loginEvent.getUsername(), loginEvent.getPassword());
            var httpServletResponse = (HttpServletResponse) VaadinResponse.getCurrent();
            httpServletResponse.addCookie(new Cookie("ecm-jwt", bearerToken.getAccessToken()));

            var securityContext = SecurityContextHolder.getContext();
            var tokenAuthentication = new BearerTokenAuthenticationToken(bearerToken.getAccessToken());
            tokenAuthentication.setAuthenticated(true);

            securityContext.setAuthentication(tokenAuthentication);

            var customDetails = (CustomUserDetails) customUserDetailsService.loadUserByUsername((String) DecodeJwtToken.decode("sub"));
            tokenAuthentication.setDetails(customDetails);

            UI.getCurrent().navigate(MainView.class);
        });
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        if (beforeEnterEvent.getLocation().getQueryParameters().getParameters().containsKey("error")) {
            loginForm.setError(true);
        }
    }
}