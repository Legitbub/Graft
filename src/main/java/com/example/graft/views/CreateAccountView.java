package com.example.graft.views;

import com.example.graft.UserService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@Route("newaccount")
@AnonymousAllowed
@PageTitle("New Account")
public class CreateAccountView extends HorizontalLayout {
    public CreateAccountView(UserService userService) {
        var form = new VerticalLayout();
        form.setAlignItems(Alignment.CENTER);
        add(form);
        form.add(new H1("Welcome to Graft! Create your account."));
        var username = new TextField();
        username.setPlaceholder("Username");
        var password = new TextField();
        password.setPlaceholder("Password");
        form.add(username);
        form.add(password);
    }

    public static void show() {
        UI.getCurrent().navigate(CreateAccountView.class);
    }
}
