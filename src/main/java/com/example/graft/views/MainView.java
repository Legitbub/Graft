package com.example.graft.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@Route("")
@AnonymousAllowed // public entry page
@PageTitle("Graft - A Social Media Sim")
public class MainView extends VerticalLayout {
    public MainView() {
        add(new H1("Graft"));
        var username = new TextField();
        username.setPlaceholder("Username");
        var password = new TextField();
        password.setPlaceholder("Password");
        add(username);
        add(password);
        var buttonBar = new HorizontalLayout();
        buttonBar.add(new Button("Login"));
        buttonBar.add(new Button("Create Account", event -> {
            CreateAccountView.show();
        }));
        add(buttonBar);
    }
}