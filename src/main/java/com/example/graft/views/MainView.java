package com.example.graft.views;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.notification.Notification;
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
        setJustifyContentMode(JustifyContentMode.CENTER);
        setHeight("100vh");
        getStyle().set("background", "linear-gradient(135deg, #667eea 0%, #000000 100%)");
        
        var header = new VerticalLayout();
        header.setAlignItems(Alignment.CENTER);
        header.getStyle().set("text-align", "center");
        
        var title = new H1("Graft");
        title.getStyle()
            .set("font-family", "'Outfit', 'Poppins', sans-serif")
            .set("font-size", "4em")
            .set("font-weight", "700")
            .set("margin", "0")
            .set("letter-spacing", "-2px")
            .set("color", "#FFFFFF")
            .set("text-shadow", "0 2px 20px rgba(33, 150, 243, 0.5)")
            .set("text-transform", "uppercase");
        
        var subtitle = new Paragraph("Your Trusted Social Networking Platform.");
        subtitle.getStyle()
            .set("font-family", "'Outfit', 'Poppins', sans-serif")
            .set("font-size", "1.3em")
            .set("font-weight", "300")
            .set("margin", "0")
            .set("letter-spacing", "1px")
            .set("color", "#FFFFFF")
            .set("text-shadow", "0 2px 20px rgba(33, 150, 243, 0.5)");
        
        header.add(title, subtitle);
        
        
        var cardContainer = new VerticalLayout();
        cardContainer.setWidth("420px");
        cardContainer.getStyle()
            .set("background", "rgba(255, 255, 255, 0.95)")
            .set("border-radius", "20px")
            .set("padding", "2.5em")
            .set("box-shadow", "0 20px 60px rgba(0, 0, 0, 0.3)")
            .set("backdrop-filter", "blur(10px)");
        
        login.setAction("login");
        login.setForgotPasswordButtonVisible(false);
        login.addForgotPasswordListener(event -> {
            UI.getCurrent().navigate(ForgotPasswordView.class);
        });
        login.getStyle()
            .set("font-family", "'Nunito', sans-serif")
            .set("margin-top", "-1em");

        cardContainer.add(login);
        
        // Password reset button
        var forgotBtn = new Button("Forgot Password?", event -> ForgotPasswordView.show());
        forgotBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        forgotBtn.setWidthFull();
        forgotBtn.getStyle()
            .set("font-family", "'Poppins', sans-serif")
            .set("font-size", "0.8em")
            .set("font-weight", "500")
            .set("color", "#0435fbff")
            .set("letter-spacing", "0.5px")
            .set("border-radius", "12px")
            .set("text-decoration", "underline");

        // Create account button
        var createBtn = new Button("New User? Create Account Here", event -> CreateAccountView.show());
        createBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        createBtn.setWidthFull();
        createBtn.getStyle()
            .set("font-family", "'Poppins', sans-serif")
            .set("font-size", "0.8em")
            .set("font-weight", "500")
            .set("color", "#0435fbff")
            .set("letter-spacing", "0.5px")
            .set("border-radius", "12px")
            .set("text-decoration", "underline");

        cardContainer.add(forgotBtn, createBtn);
        
        add(header, cardContainer);
    }

    public void beforeEnter(BeforeEnterEvent event) {
        if (ac.isAuthenticated()) {
            event.forwardTo(HomePage.class);
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