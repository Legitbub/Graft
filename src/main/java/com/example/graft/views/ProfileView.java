package com.example.graft.views;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.security.AuthenticationContext;
import jakarta.annotation.security.PermitAll;

@Route("profile")
@PermitAll
public class ProfileView extends VerticalLayout {
    public ProfileView(AuthenticationContext ac) {
        
    }
}
