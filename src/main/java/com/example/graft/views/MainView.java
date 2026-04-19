package com.example.graft.views;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.spring.security.AuthenticationContext;

@Route("login")
@RouteAlias("")
@AnonymousAllowed // public entry page
@PageTitle("Graft - A Social Media Sim")
public class MainView extends VerticalLayout implements BeforeEnterObserver {
    private final LoginForm login = new LoginForm();
    private AuthenticationContext ac;

    public MainView(AuthenticationContext ac) {
        this.ac = ac;
        setAlignItems(Alignment.CENTER);
        add(new H1("Graft"));
        login.setAction("login");
        add(login);
        var buttonBar = new HorizontalLayout();
        buttonBar.add(new Button("Create Account", event -> {
            CreateAccountView.show();
        }));
        add(buttonBar);
    }

    public void beforeEnter(BeforeEnterEvent event) {
        if (ac.isAuthenticated()) {
            event.forwardTo(ProfileView.class);
            return;
        }
        // If the URL contains "?error", display it on login form
        if (event.getLocation().getQueryParameters().getParameters().containsKey("error")) {
            login.setError(true);
        }
    }

    public static void show() {
        UI.getCurrent().navigate(MainView.class);
    }
}