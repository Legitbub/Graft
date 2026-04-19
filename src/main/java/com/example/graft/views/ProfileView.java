package com.example.graft.views;

import com.example.graft.User;
import com.example.graft.UserRepository;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.security.AuthenticationContext;
import jakarta.annotation.security.PermitAll;
import org.springframework.security.core.userdetails.UserDetails;

@Route("profile")
@PermitAll
public class ProfileView extends VerticalLayout {
    public ProfileView(AuthenticationContext ac, UserRepository repo) throws Exception {
        String username = ac.getAuthenticatedUser(UserDetails.class)
                .map(UserDetails::getUsername).orElse("invalid");

        add(new H1(username));
        User u = repo.findByUsername(username).orElseThrow(() -> new Exception("Invalid user"));
        add(new Text(u.getBio()));
        Button logout = new Button("Log Out", event -> {
            ac.logout();
        });
        add(logout);
    }
}
