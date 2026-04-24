package com.example.graft.views;

import com.example.graft.User;
import com.example.graft.UserRepository;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.security.AuthenticationContext;
import jakarta.annotation.security.PermitAll;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.userdetails.UserDetails;

@Route("home")
@PermitAll
public class HomePage extends VerticalLayout {
    private UserRepository repo;
    private AuthenticationContext ac;
    private PasswordEncoder encoder;
    private User currentUser;
    private VerticalLayout contentContainer;
    private Button profileBtn;
    private Button connectionsBtn;
    private Button searchBtn;
    private Button recommendBtn;
    private Button popularUsersBtn;

    public HomePage(AuthenticationContext ac, UserRepository repo, PasswordEncoder encoder) throws Exception {
        this.ac = ac;
        this.repo = repo;
        this.encoder = encoder;

        String username = ac.getAuthenticatedUser(UserDetails.class)
                .map(UserDetails::getUsername).orElse("invalid");

        currentUser = repo.findByUsername(username)
                .orElseThrow(() -> new Exception("Invalid user"));

        setAlignItems(Alignment.STRETCH);
        setHeight("100vh");
        setSpacing(false);
        setPadding(false);
        getStyle()
            .set("background", "linear-gradient(135deg, #667eea 0%, #000000 100%)")
            .set("overflow", "hidden");

        var navbar = createNavBar();
        navbar.getStyle().set("box-shadow", "0 4px 20px #ffffffff");
        add(navbar);

        contentContainer = new VerticalLayout();
        contentContainer.setSizeFull();
        contentContainer.setSpacing(false);
        contentContainer.setPadding(true);
        contentContainer.setAlignItems(Alignment.CENTER);
        contentContainer.getStyle()
            .set("overflow", "auto")
            .set("background", "linear-gradient(135deg, #667eea 0%, #000000 100%)");
        add(contentContainer);
        setFlexGrow(1, contentContainer);

        showProfilePage();
    }

    private HorizontalLayout createNavBar() {
        var navbar = new HorizontalLayout();
        navbar.setWidthFull();
        navbar.setAlignItems(Alignment.CENTER);
        navbar.setSpacing(true);
        navbar.getStyle()
            .set("padding", "1.5em 2em")
            .set("box-shadow", "0 4px 20px rgba(0, 0, 0, 0.1)")
            .set("flex-shrink", "0");

        var title = new H1("Graft");
        title.getStyle()
            .set("font-family", "'Outfit', 'Poppins', sans-serif")
            .set("font-size", "2.5em")
            .set("font-weight", "700")
            .set("margin", "0")
            .set("letter-spacing", "-2px")
            .set("color", "#FFFFFF")
            .set("text-shadow", "0 2px 20px rgba(33, 150, 243, 0.5)")
            .set("text-transform", "uppercase");

        profileBtn = createNavButton("Your Profile", () -> {
            setActiveButton(profileBtn);
            showProfilePage();
        });
        connectionsBtn = createNavButton("Connections", () -> {
            setActiveButton(connectionsBtn);
            showConnectionsPage();
        });
        searchBtn = createNavButton("Search Users", () -> {
            setActiveButton(searchBtn);
            showSearchPage();
        });
        recommendBtn = createNavButton("Recommendations", () -> {
            setActiveButton(recommendBtn);
            showRecommendationsPage();
        });
        popularUsersBtn = createNavButton("Popular Users", () -> {
            setActiveButton(popularUsersBtn);
            showPopularUsersViewPage();
        });
        var logoutBtn = createLogoutButton();

        navbar.add(title);
        navbar.setFlexGrow(0, title);

        var spacer = new HorizontalLayout();
        navbar.add(spacer);
        navbar.setFlexGrow(1, spacer);

        navbar.add(profileBtn, connectionsBtn, searchBtn, recommendBtn, popularUsersBtn, logoutBtn);
        
        // Set profile button as active initially
        setActiveButton(profileBtn);

        return navbar;
    }

    private Button createNavButton(String label, Runnable action) {
        var btn = new Button(label);
        btn.addClickListener(e -> action.run());
        btn.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        btn.getStyle()
            .set("font-family", "'Poppins', sans-serif")
            .set("font-weight", "600")
            .set("font-size", "0.9em")
            .set("letter-spacing", "0.5px")
            .set("color", "#ffffff")
            .set("border-radius", "8px")
            .set("padding", "0.5em 1em");
        return btn;
    }

    private Button createLogoutButton() {
        var btn = new Button("Log Out", event -> ac.logout());
        btn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        btn.getStyle()
            .set("font-family", "'Poppins', sans-serif")
            .set("font-weight", "600")
            .set("font-size", "0.9em")
            .set("letter-spacing", "0.5px")
            .set("border-radius", "8px")
            .set("padding", "0.5em 1em");
        return btn;
    }

    private void setActiveButton(Button activeBtn) {
        Button[] allButtons = {profileBtn, connectionsBtn, searchBtn, recommendBtn, popularUsersBtn};
        for (Button btn : allButtons) {
            if (btn == activeBtn) {
                btn.getStyle().set("color", "#2196F3");
            } else {
                btn.getStyle().set("color", "#ffffff");
            }
        }
    }

    private void showProfilePage() {
        contentContainer.removeAll();
        try {
            contentContainer.add(new ProfileView(ac, repo, encoder));
        } catch (Exception e) {
            com.vaadin.flow.component.notification.Notification.show("Error loading profile: " + e.getMessage());
        }
    }

    private void showConnectionsPage() {
        contentContainer.removeAll();
        contentContainer.add(new ConnectionsView(repo, currentUser.getUsername()));
    }

    private void showSearchPage() {
        contentContainer.removeAll();
        contentContainer.add(new SearchView(repo, currentUser.getUsername()));
    }

    private void showRecommendationsPage() {
        contentContainer.removeAll();
        contentContainer.add(new RecommendationsView(repo, currentUser.getUsername()));
    }

    private void showPopularUsersViewPage() {
        contentContainer.removeAll();
        contentContainer.add(new PopularUsersView(repo, currentUser.getUsername()));
    }
}
