package com.example.graft.views;

import com.example.graft.User;
import com.example.graft.UserRepository;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.springframework.security.crypto.password.PasswordEncoder;

@Route("forgot-password")
@AnonymousAllowed
@PageTitle("Forgot Password")
public class ForgotPasswordView extends VerticalLayout {
    private UserRepository repo;
    private PasswordEncoder encoder;
    private User foundUser;
    private VerticalLayout searchForm;
    private VerticalLayout resetForm;

    public ForgotPasswordView(UserRepository repo, PasswordEncoder encoder) {
        this.repo = repo;
        this.encoder = encoder;
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
        
        var subtitle = new Paragraph("Reset Your Password");
        subtitle.getStyle()
            .set("font-family", "'Outfit', 'Poppins', sans-serif")
            .set("font-size", "1.3em")
            .set("font-weight", "300")
            .set("margin", "0")
            .set("letter-spacing", "1px")
            .set("color", "#FFFFFF")
            .set("text-shadow", "0 2px 20px rgba(33, 150, 243, 0.5)");
        
        header.add(title, subtitle);
        
        // Search form to find account
        searchForm = new VerticalLayout();
        searchForm.setAlignItems(Alignment.STRETCH);
        searchForm.setWidth("420px");
        searchForm.getStyle()
            .set("padding", "2.5em")
            .set("background", "rgba(255, 255, 255, 0.95)")
            .set("border-radius", "20px")
            .set("box-shadow", "0 20px 60px rgba(0, 0, 0, 0.3)")
            .set("backdrop-filter", "blur(10px)");
        
        var searchTitle = new H1("Find Your Account");
        searchTitle.getStyle()
            .set("font-family", "'Outfit', 'Poppins', sans-serif")
            .set("font-size", "1.2em")
            .set("font-weight", "700")
            .set("color", "#000000")
            .set("letter-spacing", "0.5px");
        searchForm.add(searchTitle);
        
        var username = new TextField();
        username.setPlaceholder("Username");
        username.setLabel("Username *");
        username.setWidthFull();
        username.getStyle()
            .set("font-family", "'Nunito', sans-serif")
            .set("margin-bottom", "1em");
        
        var email = new TextField();
        email.setPlaceholder("Email Address");
        email.setLabel("Email Address *");
        email.setWidthFull();
        email.getStyle()
            .set("font-family", "'Nunito', sans-serif")
            .set("margin-bottom", "1em");

        searchForm.add(username, email);
        
        
        var searchBtn = new Button("Find Account", event -> {
            try {
                if (username.getValue().isEmpty() || email.getValue().isEmpty()) {
                    Notification.show("Username and Email are required!", 3000, Notification.Position.TOP_CENTER);
                    return;
                }
                
                foundUser = repo.findByUsername(username.getValue()).orElse(null);
                
                if (foundUser == null || !foundUser.getEmail().equals(email.getValue())) {
                    Notification.show("Account not found. Please check your username and email.", 3000, Notification.Position.TOP_CENTER);
                    foundUser = null;
                    return;
                }
                
                showResetForm();
            } catch (Exception e) {
                Notification.show("Error: " + e.getMessage(), 3000, Notification.Position.TOP_CENTER);
            }
        });
        searchBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        searchBtn.setWidthFull();
        searchBtn.getStyle()
            .set("font-family", "'Poppins', sans-serif")
            .set("font-weight", "500")
            .set("letter-spacing", "0.5px")
            .set("border-radius", "12px")
            .set("margin", "0.5em 0");
        
        var backBtn = new Button("Back", event -> MainView.show());
        backBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        backBtn.setWidthFull();
        backBtn.getStyle()
            .set("font-family", "'Poppins', sans-serif")
            .set("font-size", "0.8em")
            .set("font-weight", "500")
            .set("color", "#0435fbff")
            .set("letter-spacing", "0.5px")
            .set("border-radius", "12px")
            .set("text-decoration", "underline");
    
        searchForm.add(searchBtn, backBtn);
        
        resetForm = new VerticalLayout();
        resetForm.setAlignItems(Alignment.STRETCH);
        resetForm.setWidth("420px");
        resetForm.getStyle()
            .set("padding", "2.5em")
            .set("background", "rgba(255, 255, 255, 0.95)")
            .set("border-radius", "20px")
            .set("box-shadow", "0 20px 60px rgba(0, 0, 0, 0.3)")
            .set("backdrop-filter", "blur(10px)")
            .set("display", "none");
        
        var resetTitle = new H1("Reset Password");
        resetTitle.getStyle()
            .set("font-family", "'Outfit', 'Poppins', sans-serif")
            .set("font-size", "1.2em")
            .set("font-weight", "700")
            .set("color", "#000000")
            .set("letter-spacing", "0.5px");
        resetForm.add(resetTitle);
        
        var newPassword = new PasswordField();
        newPassword.setPlaceholder("New Password");
        newPassword.setLabel("New Password *");
        newPassword.setWidthFull();
        newPassword.getStyle()
            .set("font-family", "'Nunito', sans-serif")
            .set("margin-bottom", "1em");
        
        var confirmPassword = new PasswordField();
        confirmPassword.setPlaceholder("Confirm New Password");
        confirmPassword.setLabel("Confirm New Password *");
        confirmPassword.setWidthFull();
        confirmPassword.getStyle()
            .set("font-family", "'Nunito', sans-serif")
            .set("margin-bottom", "1em");

        resetForm.add(newPassword, confirmPassword);
        
        var resetBtn = new Button("Update Password", event -> {
            try {
                if (newPassword.getValue().isEmpty()) {
                    Notification.show("New Password is required!", 3000, Notification.Position.TOP_CENTER);
                    return;
                }
                if (confirmPassword.getValue().isEmpty()) {
                    Notification.show("Confirm Password is required!", 3000, Notification.Position.TOP_CENTER);
                    return;
                }
                if (!newPassword.getValue().equals(confirmPassword.getValue())) {
                    Notification.show("Passwords do not match!", 3000, Notification.Position.TOP_CENTER);
                    return;
                }
                
                String encodedPassword = encoder.encode(newPassword.getValue());
                foundUser.setPassword(encodedPassword);
                repo.save(foundUser);
                
                Notification.show("✓ Password updated successfully! You can now log in.", 3000, Notification.Position.TOP_CENTER);
                UI.getCurrent().navigate(MainView.class);
            } catch (Exception e) {
                Notification.show("Error: " + e.getMessage(), 3000, Notification.Position.TOP_CENTER);
            }
        });
        resetBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        resetBtn.setWidthFull();
        resetBtn.getStyle()
            .set("font-family", "'Poppins', sans-serif")
            .set("font-weight", "500")
            .set("letter-spacing", "0.5px")
            .set("border-radius", "12px")
            .set("margin", "0.5em 0");
        
        var cancelBtn = new Button("Cancel", event -> {
            foundUser = null;
            searchForm.getStyle().set("display", "block");
            resetForm.getStyle().set("display", "none");
            username.clear();
            email.clear();
            newPassword.clear();
            confirmPassword.clear();
        });
        cancelBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        cancelBtn.setWidthFull();
        cancelBtn.getStyle()
            .set("font-family", "'Poppins', sans-serif")
            .set("font-size", "0.8em")
            .set("font-weight", "500")
            .set("color", "#0435fbff")
            .set("letter-spacing", "0.5px")
            .set("border-radius", "12px")
            .set("text-decoration", "underline");
        
        resetForm.add(resetBtn, cancelBtn);
        
        add(header, searchForm, resetForm);
    }

    private void showResetForm() {
        searchForm.getStyle().set("display", "none");
        resetForm.getStyle().set("display", "block");
    }

    public static void show() {
        UI.getCurrent().navigate(ForgotPasswordView.class);
    }
}
