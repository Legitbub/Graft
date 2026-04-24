package com.example.graft.views;

import com.example.graft.User;
import com.example.graft.UserRepository;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.security.AuthenticationContext;
import jakarta.annotation.security.PermitAll;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.userdetails.UserDetails;

@Route("profile")
@PermitAll
public class ProfileView extends VerticalLayout {

    private HorizontalLayout createDetailRow(String label, Span valueSpan) {
        var layout = new HorizontalLayout();
        layout.setWidthFull();
        layout.setJustifyContentMode(JustifyContentMode.BETWEEN);
        layout.setAlignItems(Alignment.CENTER);
        layout.getStyle()
            .set("padding", "0.6em 0.8em")
            .set("border-bottom", "1px solid #e2e8f0");

        var labelSpan = new Span(label);
        labelSpan.getStyle()
            .set("font-family", "'Poppins', sans-serif")
            .set("font-weight", "600")
            .set("color", "#64748b")
            .set("font-size", "0.85em");

        valueSpan.getStyle()
            .set("font-family", "'Nunito', sans-serif")
            .set("color", "#0f172a")
            .set("font-weight", "500")
            .set("font-size", "0.9em");

        layout.add(labelSpan, valueSpan);
        return layout;
    }

    public ProfileView(AuthenticationContext ac, UserRepository repo, PasswordEncoder encoder) throws Exception {
        String username = ac.getAuthenticatedUser(UserDetails.class)
                .map(UserDetails::getUsername).orElse("invalid");

        User currentUser = repo.findByUsername(username).orElseThrow(() -> new Exception("Invalid user"));
        
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        setSpacing(false);
        setPadding(false);
        setWidth("80%");
        //setHeight("100vh");
        getStyle()
            .set("background", "transparent")
            .set("overflow", "hidden");
        
        var section = new VerticalLayout();
        section.setAlignItems(Alignment.CENTER);
        section.setJustifyContentMode(JustifyContentMode.START);
        section.setSpacing(false);
        section.setPadding(false);
        section.setWidth("80%");
        section.setMaxWidth("600px");
        section.setHeightFull();
        section.getStyle()
            .set("padding", "1.5em")
            .set("background", "rgba(255, 255, 255, 0.95)")
            .set("border-radius", "20px")
            .set("box-shadow", "0 20px 60px rgba(0, 0, 0, 0.3)")
            .set("backdrop-filter", "blur(10px)")
            .set("overflow-y", "auto")
            .set("max-height", "95vh")
            .set("width", "80%");

        // Profile image
        Image profileImage = new Image("images/user.png", "Profile Picture");
        profileImage.setWidth("120px");
        profileImage.setHeight("120px");
        profileImage.getStyle()
            .set("border-radius", "50%")
            .set("object-fit", "cover")
            .set("border", "4px solid #fff")
            .set("box-shadow", "0 4px 10px rgba(0, 0, 0, 0.1)")
            .set("margin-bottom", "0.8em")
            .set("flex-shrink", "0");

        // Display components
        var nameDisplay = new H2(currentUser.getName() != null ? currentUser.getName() : "User");
        nameDisplay.getStyle()
            .set("font-family", "'Outfit', 'Poppins', sans-serif")
            .set("margin", "0 0 0.2em 0")
            .set("font-size", "1.6em")
            .set("font-weight", "700")
            .set("color", "#2b3c89ff");

        var usernameDisplay = new Paragraph("@" + currentUser.getUsername());
        usernameDisplay.getStyle()
            .set("font-family", "'Nunito', sans-serif")
            .set("margin", "0 0 0.6em 0")
            .set("font-size", "0.95em")
            .set("color", "#757575");

        var bioDisplay = new Paragraph(currentUser.getBio() != null && !currentUser.getBio().isEmpty() ? currentUser.getBio() : "No bio yet");
        bioDisplay.getStyle()
            .set("font-family", "'Nunito', sans-serif")
            .set("margin", "0 0 1em 0")
            .set("font-style", "italic")
            .set("text-align", "center")
            .set("font-size", "0.9em")
            .set("color", "#64748b");

        var infoLayout = new VerticalLayout();
        infoLayout.setAlignItems(Alignment.STRETCH);
        infoLayout.setSpacing(false);
        infoLayout.setPadding(false);
        infoLayout.setWidthFull();
        infoLayout.getStyle()
            .set("background", "#f8fafc")
            .set("border-radius", "12px")
            .set("border", "1px solid #e2e8f0")
            .set("margin-bottom", "1em")
            .set("width", "70%");

        Span emailSpan = new Span(currentUser.getEmail() != null ? currentUser.getEmail() : "N/A");
        Span ageSpan = new Span(currentUser.getAge() != null ? currentUser.getAge().toString() : "N/A");
        Span countrySpan = new Span(currentUser.getCountry() != null ? currentUser.getCountry() : "N/A");
        Span passwordSpan = new Span("********");

        var emailRow = createDetailRow("Email", emailSpan);
        var ageRow = createDetailRow("Age", ageSpan);
        var countryRow = createDetailRow("Country", countrySpan);
        var passwordRow = createDetailRow("Password", passwordSpan);
        passwordRow.getStyle().set("border-bottom", "none");

        infoLayout.add(emailRow, ageRow, countryRow, passwordRow);

        var viewModeContainer = new VerticalLayout(nameDisplay, usernameDisplay, bioDisplay, infoLayout);
        viewModeContainer.setAlignItems(Alignment.CENTER);
        viewModeContainer.setPadding(false);
        viewModeContainer.setSpacing(false);
        //viewModeContainer.setWidthFull();

        // Edit components
        var editLayout = new VerticalLayout();
        editLayout.setWidthFull();
        editLayout.setSpacing(true);
        editLayout.setPadding(true);


        var nameEdit = new TextField("Name");
        nameEdit.setValue(currentUser.getName() != null ? currentUser.getName() : "");
        nameEdit.setWidthFull();
        nameEdit.getStyle()
            .set("font-size", "0.9em")
            .set("font-family", "'Nunito', sans-serif");

        var usernameEdit = new TextField("Username");
        usernameEdit.setValue(currentUser.getUsername() != null ? currentUser.getUsername() : "");
        usernameEdit.setWidthFull();
        usernameEdit.getStyle()
            .set("font-size", "0.9em")
            .set("font-family", "'Nunito', sans-serif");

        var emailEdit = new TextField("Email");
        emailEdit.setValue(currentUser.getEmail() != null ? currentUser.getEmail() : "");
        emailEdit.setWidthFull();
        emailEdit.getStyle()
            .set("font-size", "0.9em")
            .set("font-family", "'Nunito', sans-serif");

        var ageEdit = new IntegerField("Age");
        ageEdit.setValue(currentUser.getAge());
        ageEdit.setWidthFull();
        ageEdit.getStyle()
            .set("font-size", "0.9em")
            .set("font-family", "'Nunito', sans-serif");

        var countryEdit = new ComboBox<String>("Country");
        countryEdit.setItems("US","AE","AF","AG","AI","AL","AM","AO","AR","AS","AT","AU","AW","AZ","BA","BB","BD","BE","BF","BG","BH","BI","BJ","BL","BM","BN","BO","BR","BS","BT","BW","BY","BZ","CA","CD","CF","CG","CH","CI","CK","CL","CM","CN","CO","CR","CU","CV","CW","CY","CZ","DE","DJ","DK","DM","DO","DZ","EC","EE","EG","EH","ER","ES","ET","FI","FJ","FK","FM","FO","FR","GA","GB","GD","GE","GF","GG","GH","GI","GL","GM","GN","GP","GQ","GR","GT","GU","GW","GY","HK","HN","HR","HT","HU","ID","IE","IL","IM","IN","IQ","IR","IS","IT","JE","JM","JO","JP","KE","KG","KH","KI","KM","KN","KP","KR","KW","KY","KZ","LA","LB","LC","LI","LK","LR","LS","LT","LU","LV","LY","MA","MC","MD","ME","MF","MG","MH","MK","ML","MM","MN","MO","MP","MQ","MR","MS","MT","MU","MV","MW","MX","MY","MZ","NA","NC","NE","NG","NI","NL","NO","NP","NR","NU","NZ","OM","PA","PE","PF","PG","PH","PK","PL","PM","PR","PS","PT","PW","PY","QA","RE","RO","RS","RU","RW","SA","SB","SC","SD","SE","SG","SH","SI","SK","SL","SM","SN","SO","SR","SS","ST","SV","SX","SY","SZ","TC","TD","TG","TH","TJ","TK","TL","TM","TN","TO","TR","TT","TV","TW","TZ","UA","UG","UY","UZ","VA","VC","VE","VG","VI","VN","VU","WF","WS","YE","YT","ZA","ZM","ZW");
        countryEdit.setValue(currentUser.getCountry() != null && !currentUser.getCountry().isEmpty() ? currentUser.getCountry() : "US");
        countryEdit.setWidthFull();
        countryEdit.getStyle()
            .set("font-size", "0.9em")
            .set("font-family", "'Nunito', sans-serif");

        var bioEditArea = new TextArea("Bio");
        bioEditArea.setPlaceholder("Enter your bio...");
        bioEditArea.setValue(currentUser.getBio() != null ? currentUser.getBio() : "");
        bioEditArea.setWidthFull();
        bioEditArea.setHeight("80px");
        bioEditArea.getStyle()
            .set("font-size", "0.9em")
            .set("font-family", "'Nunito', sans-serif");

        editLayout.add(nameEdit, usernameEdit, emailEdit, ageEdit, countryEdit, bioEditArea);
        editLayout.getStyle().set("display", "none");

        // Password Update Layout
        var passwordLayout = new VerticalLayout();
        passwordLayout.setWidthFull();
        passwordLayout.setSpacing(true);
        passwordLayout.setPadding(false);

        

        var newPasswordEdit = new PasswordField("New Password");
        newPasswordEdit.setPlaceholder("Enter new password...");
        newPasswordEdit.setWidthFull();
        newPasswordEdit.getStyle()
            .set("font-size", "0.9em")
            .set("font-family", "'Nunito', sans-serif");

        var confirmPasswordEdit = new PasswordField("Confirm Password");
        confirmPasswordEdit.setPlaceholder("Confirm new password...");
        confirmPasswordEdit.setWidthFull();
        confirmPasswordEdit.getStyle()
            .set("font-size", "0.9em")
            .set("font-family", "'Nunito', sans-serif");

        passwordLayout.add(newPasswordEdit, confirmPasswordEdit);
        passwordLayout.getStyle().set("display", "none");

        var buttonLayout = new HorizontalLayout();
        buttonLayout.setWidthFull();
        buttonLayout.setSpacing(true);
        buttonLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        buttonLayout.getStyle()
            .set("margin-top", "0.8em")
            .set("margin-bottom", "0")
            .set("gap", "0.5em")
            .set("flex-wrap", "wrap");

        Button editBtn = new Button("Edit Profile");
        Button updatePwdViewBtn = new Button("Update Password");
        
        Button updateBtn = new Button("Update");
        Button savePwdBtn = new Button("Save Password");
        Button cancelBtn = new Button("Cancel");

        Runnable showViewMode = () -> {
            viewModeContainer.getStyle().set("display", "flex");
            editLayout.getStyle().set("display", "none");
            passwordLayout.getStyle().set("display", "none");

            editBtn.getStyle().set("display", "block");
            updatePwdViewBtn.getStyle().set("display", "block");
            updateBtn.getStyle().set("display", "none");
            savePwdBtn.getStyle().set("display", "none");
            cancelBtn.getStyle().set("display", "none");
        };

        Runnable showEditMode = () -> {
            viewModeContainer.getStyle().set("display", "none");
            editLayout.getStyle().set("display", "flex");
            passwordLayout.getStyle().set("display", "none");

            editBtn.getStyle().set("display", "none");
            updatePwdViewBtn.getStyle().set("display", "none");
            updateBtn.getStyle().set("display", "block");
            savePwdBtn.getStyle().set("display", "none");
            cancelBtn.getStyle().set("display", "block");
        };

        Runnable showPasswordMode = () -> {
            viewModeContainer.getStyle().set("display", "none");
            editLayout.getStyle().set("display", "none");
            passwordLayout.getStyle().set("display", "flex");

            editBtn.getStyle().set("display", "none");
            updatePwdViewBtn.getStyle().set("display", "none");
            updateBtn.getStyle().set("display", "none");
            savePwdBtn.getStyle().set("display", "block");
            cancelBtn.getStyle().set("display", "block");
        };

        editBtn.addClickListener(event -> showEditMode.run());
        editBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        editBtn.setIcon(VaadinIcon.EDIT.create());
        editBtn.getStyle()
            .set("font-family", "'Poppins', sans-serif")
            .set("font-weight", "500")
            .set("font-size", "0.85em")
            .set("letter-spacing", "0.5px")
            .set("border-radius", "12px")
            .set("margin", "0.25em 0")
            .set("padding", "0.4em 0.8em")
            .set("background", "#2b3c89ff");

        updatePwdViewBtn.addClickListener(event -> showPasswordMode.run());
        updatePwdViewBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        updatePwdViewBtn.setIcon(VaadinIcon.LOCK.create());
        updatePwdViewBtn.getStyle()
            .set("font-family", "'Poppins', sans-serif")
            .set("font-weight", "500")
            .set("font-size", "0.85em")
            .set("letter-spacing", "0.5px")
            .set("border-radius", "12px")
            .set("margin", "0.25em 0")
            .set("padding", "0.4em 0.8em")
            .set("background", "#2b3c89ff");

        updateBtn.addClickListener(event -> {
            try {
                if (nameEdit.getValue().isEmpty() || usernameEdit.getValue().isEmpty() || emailEdit.getValue().isEmpty()) {
                    Notification.show("✗ Name, Username, and Email are required", 3000, Notification.Position.TOP_CENTER);
                    return;
                }

                currentUser.setName(nameEdit.getValue());
                currentUser.setUsername(usernameEdit.getValue());
                currentUser.setEmail(emailEdit.getValue());
                currentUser.setAge(ageEdit.getValue());
                currentUser.setCountry(countryEdit.getValue());
                currentUser.setBio(bioEditArea.getValue());
                
                repo.save(currentUser);

                nameDisplay.setText(currentUser.getName());
                usernameDisplay.setText("@" + currentUser.getUsername());
                bioDisplay.setText(currentUser.getBio() != null && !currentUser.getBio().isEmpty() ? currentUser.getBio() : "No bio yet");

                emailSpan.setText(currentUser.getEmail());
                ageSpan.setText(currentUser.getAge() != null ? currentUser.getAge().toString() : "N/A");
                countrySpan.setText(currentUser.getCountry() != null ? currentUser.getCountry() : "N/A");

                Notification.show("Profile updated successfully!", 3000, Notification.Position.TOP_CENTER);
                showViewMode.run();
            } catch (Exception e) {
                Notification.show("Error: " + e.getMessage(), 3000, Notification.Position.TOP_CENTER);
            }
        });
        updateBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        updateBtn.setIcon(VaadinIcon.CHECK.create());
        updateBtn.getStyle()
            .set("font-family", "'Poppins', sans-serif")
            .set("font-weight", "500")
            .set("font-size", "0.85em")
            .set("letter-spacing", "0.5px")
            .set("border-radius", "12px")
            .set("margin", "0.25em 0")
            .set("padding", "0.4em 0.8em")
            .set("background", "#2b3c89ff");

        savePwdBtn.addClickListener(event -> {
            try {
                if (newPasswordEdit.getValue().isEmpty() || confirmPasswordEdit.getValue().isEmpty()) {
                    Notification.show("Password fields cannot be empty", 3000, Notification.Position.TOP_CENTER);
                    return;
                }
                
                if (!newPasswordEdit.getValue().equals(confirmPasswordEdit.getValue())) {
                    Notification.show("Passwords do not match", 3000, Notification.Position.TOP_CENTER);
                    return;
                }

                currentUser.setPassword(encoder.encode(newPasswordEdit.getValue()));
                repo.save(currentUser);

                newPasswordEdit.clear();
                confirmPasswordEdit.clear();

                Notification.show("Password updated successfully!", 3000, Notification.Position.TOP_CENTER);
                showViewMode.run();
            } catch (Exception e) {
                Notification.show("Error: " + e.getMessage(), 3000, Notification.Position.TOP_CENTER);
            }
        });
        savePwdBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        savePwdBtn.setIcon(VaadinIcon.CHECK.create());
        savePwdBtn.getStyle()
            .set("font-family", "'Poppins', sans-serif")
            .set("font-weight", "500")
            .set("font-size", "0.85em")
            .set("letter-spacing", "0.5px")
            .set("border-radius", "12px")
            .set("margin", "0.25em 0")
            .set("padding", "0.4em 0.8em")
            .set("background", "#2b3c89ff");

        cancelBtn.addClickListener(event -> {
            nameEdit.setValue(currentUser.getName() != null ? currentUser.getName() : "");
            usernameEdit.setValue(currentUser.getUsername() != null ? currentUser.getUsername() : "");
            emailEdit.setValue(currentUser.getEmail() != null ? currentUser.getEmail() : "");
            ageEdit.setValue(currentUser.getAge());
            countryEdit.setValue(currentUser.getCountry() != null ? currentUser.getCountry() : "");
            bioEditArea.setValue(currentUser.getBio() != null ? currentUser.getBio() : "");
            
            newPasswordEdit.clear();
            confirmPasswordEdit.clear();
            
            showViewMode.run();
        });
        cancelBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        cancelBtn.getStyle()
            .set("font-family", "'Poppins', sans-serif")
            .set("font-weight", "500")
            .set("font-size", "0.85em")
            .set("letter-spacing", "0.5px")
            .set("border-radius", "12px")
            .set("margin", "0.25em 0")
            .set("padding", "0.4em 0.8em")
            .set("background", "#2b3c89ff")
            .set("color", "#fff");

        buttonLayout.add(editBtn, updatePwdViewBtn, updateBtn, savePwdBtn, cancelBtn);
        
        section.add(
            profileImage, viewModeContainer, editLayout, passwordLayout, buttonLayout
        );
        add(section);
        
        // Initialize view mode
        showViewMode.run();
    }

    public ProfileView(User viewedUser) throws Exception {
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        setSpacing(false);
        setPadding(false);
        setWidth("80%");
        getStyle()
            .set("background", "transparent")
            .set("overflow", "hidden");
        
        var section = new VerticalLayout();
        section.setAlignItems(Alignment.CENTER);
        section.setJustifyContentMode(JustifyContentMode.START);
        section.setSpacing(false);
        section.setPadding(false);
        section.setWidth("80%");
        section.setMaxWidth("600px");
        section.setHeightFull();
        section.getStyle()
            .set("padding", "1.5em")
            .set("background", "rgba(255, 255, 255, 0.95)")
            .set("border-radius", "20px")
            .set("box-shadow", "0 20px 60px rgba(0, 0, 0, 0.3)")
            .set("backdrop-filter", "blur(10px)")
            .set("overflow-y", "auto")
            .set("max-height", "95vh")
            .set("width", "80%");

        // Profile image
        Image profileImage = new Image("images/user.png", "Profile Picture");
        profileImage.setWidth("120px");
        profileImage.setHeight("120px");
        profileImage.getStyle()
            .set("border-radius", "50%")
            .set("object-fit", "cover")
            .set("border", "4px solid #fff")
            .set("box-shadow", "0 4px 10px rgba(0, 0, 0, 0.1)")
            .set("margin-bottom", "0.8em")
            .set("flex-shrink", "0");

        // Display components
        var nameDisplay = new H2(viewedUser.getName() != null ? viewedUser.getName() : "");
        nameDisplay.getStyle()
            .set("font-family", "'Outfit', 'Poppins', sans-serif")
            .set("margin", "0.5em 0 0.2em 0")
            .set("font-weight", "700")
            .set("color", "#1a1a1a");

        var usernameDisplay = new Paragraph("@" + (viewedUser.getUsername() != null ? viewedUser.getUsername() : ""));
        usernameDisplay.getStyle()
            .set("margin", "0 0 1.2em 0")
            .set("font-size", "0.9em")
            .set("color", "#666666");

        var bioDisplay = new Paragraph(viewedUser.getBio() != null ? viewedUser.getBio() : "");
        bioDisplay.getStyle()
            .set("margin", "0 0 1.2em 0")
            .set("font-size", "0.95em")
            .set("color", "#555555")
            .set("line-height", "1.5");

        var infoLayout = new VerticalLayout();
        infoLayout.setWidthFull();
        infoLayout.setSpacing(false);
        infoLayout.setPadding(false);
        infoLayout.getStyle()
            .set("border", "1px solid #e2e8f0")
            .set("border-radius", "8px")
            .set("overflow", "hidden");

        var emailSpan = new Span(viewedUser.getEmail() != null ? viewedUser.getEmail() : "N/A");
        var ageSpan = new Span(viewedUser.getAge() != null ? String.valueOf(viewedUser.getAge()) : "N/A");
        var countrySpan = new Span(viewedUser.getCountry() != null ? viewedUser.getCountry() : "N/A");
        var passwordSpan = new Span("••••••••");

        var emailRow = createDetailRow("Email", emailSpan);
        var ageRow = createDetailRow("Age", ageSpan);
        var countryRow = createDetailRow("Country", countrySpan);
        var passwordRow = createDetailRow("Password", passwordSpan);
        passwordRow.getStyle().set("border-bottom", "none");

        infoLayout.add(emailRow, ageRow, countryRow, passwordRow);

        var viewModeContainer = new VerticalLayout(nameDisplay, usernameDisplay, bioDisplay, infoLayout);
        viewModeContainer.setAlignItems(Alignment.CENTER);
        viewModeContainer.setPadding(false);
        viewModeContainer.setSpacing(false);

        section.add(profileImage, viewModeContainer);
        add(section);
    }
}
