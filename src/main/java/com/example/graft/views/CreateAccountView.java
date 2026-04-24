package com.example.graft.views;

import com.example.graft.UserService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@Route("newaccount")
@AnonymousAllowed
@PageTitle("New Account")
public class CreateAccountView extends VerticalLayout {
    public CreateAccountView(UserService userService) {
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        setHeight("100vh");
        setPadding(false);
        setSpacing(false);
        getStyle().set("background", "linear-gradient(135deg, #667eea 0%, #000000 100%)");
        getStyle().set("overflow", "auto");
        
        var header = new VerticalLayout();
        header.setAlignItems(Alignment.CENTER);
        header.getStyle().set("text-align", "center");
        header.getStyle().set("flex-shrink", "0");
        header.getStyle().set("padding", "2em 0 1em 0");
        
        var title = new H1("Graft");
        title.getStyle()
            .set("font-family", "'Outfit', 'Poppins', sans-serif")
            .set("font-size", "3.5em")
            .set("font-weight", "700")
            .set("margin", "0")
            .set("letter-spacing", "-2px")
            .set("color", "#FFFFFF")
            .set("text-shadow", "0 2px 20px rgba(33, 150, 243, 0.5)")
            .set("text-transform", "uppercase");
        
        var subtitle = new Paragraph("Join Our Network");
        subtitle.getStyle()
            .set("font-family", "'Outfit', 'Poppins', sans-serif")
            .set("font-size", "1.1em")
            .set("font-weight", "300")
            .set("margin", "0")
            .set("letter-spacing", "1px")
            .set("color", "#FFFFFF")
            .set("text-shadow", "0 2px 20px rgba(33, 150, 243, 0.5)");
        
        header.add(title, subtitle);
        
        var form = new VerticalLayout();
        form.setAlignItems(Alignment.STRETCH);
        form.setMaxWidth("500px");
        form.setWidth("90%");
        form.getStyle()
            .set("padding", "2em 2em 1em 2em")
            .set("background", "rgba(255, 255, 255, 0.95)")
            .set("border-radius", "20px")
            .set("box-shadow", "0 20px 60px rgba(0, 0, 0, 0.3)")
            .set("backdrop-filter", "blur(10px)");
        
        var formTitle = new H1("Create Account");
        formTitle.getStyle()
            .set("font-family", "'Outfit', 'Poppins', sans-serif")
            .set("font-size", "1.1em")
            .set("font-weight", "700")
            .set("color", "#000000")
            .set("letter-spacing", "0.5px")
            .set("margin", "0 0 1.2em 0");
        form.add(formTitle);
        
        var name = new TextField();
        name.setPlaceholder("Full Name");
        name.setLabel("Full Name *");
        name.setWidthFull();
        name.getStyle().set("font-family", "'Nunito', sans-serif").set("font-size", "0.9em");
        
        var email = new TextField();
        email.setPlaceholder("Email Address");
        email.setLabel("Email Address *");
        email.setWidthFull();
        email.getStyle().set("font-family", "'Nunito', sans-serif").set("font-size", "0.9em");
        
        var username = new TextField();
        username.setPlaceholder("Username");
        username.setLabel("Username *");
        username.setWidthFull();
        username.getStyle().set("font-family", "'Nunito', sans-serif").set("font-size", "0.9em");
        
        var password = new PasswordField();
        password.setPlaceholder("Password");
        password.setLabel("Password *");
        password.setWidthFull();
        password.getStyle().set("font-family", "'Nunito', sans-serif").set("font-size", "0.9em");
        
        var age = new IntegerField();
        age.setPlaceholder("Age");
        age.setLabel("Age (Optional)");
        age.setWidthFull();
        age.getStyle().set("font-family", "'Nunito', sans-serif").set("font-size", "0.9em");
        
        var bio = new TextArea();
        bio.setPlaceholder("Tell us about yourself");
        bio.setLabel("Bio (Optional)");
        bio.setWidthFull();
        bio.setHeight("65px");
        bio.getStyle().set("font-family", "'Nunito', sans-serif").set("font-size", "0.9em");
        
        var country = new ComboBox<String>();
        country.setItems("US","AE","AF","AG","AI","AL","AM","AO","AR","AS","AT","AU","AW","AZ","BA","BB","BD","BE","BF","BG","BH","BI","BJ","BL","BM","BN","BO","BR","BS","BT","BW","BY","BZ","CA","CD","CF","CG","CH","CI","CK","CL","CM","CN","CO","CR","CU","CV","CW","CY","CZ","DE","DJ","DK","DM","DO","DZ","EC","EE","EG","EH","ER","ES","ET","FI","FJ","FK","FM","FO","FR","GA","GB","GD","GE","GF","GG","GH","GI","GL","GM","GN","GP","GQ","GR","GT","GU","GW","GY","HK","HN","HR","HT","HU","ID","IE","IL","IM","IN","IQ","IR","IS","IT","JE","JM","JO","JP","KE","KG","KH","KI","KM","KN","KP","KR","KW","KY","KZ","LA","LB","LC","LI","LK","LR","LS","LT","LU","LV","LY","MA","MC","MD","ME","MF","MG","MH","MK","ML","MM","MN","MO","MP","MQ","MR","MS","MT","MU","MV","MW","MX","MY","MZ","NA","NC","NE","NG","NI","NL","NO","NP","NR","NU","NZ","OM","PA","PE","PF","PG","PH","PK","PL","PM","PR","PS","PT","PW","PY","QA","RE","RO","RS","RU","RW","SA","SB","SC","SD","SE","SG","SH","SI","SK","SL","SM","SN","SO","SR","SS","ST","SV","SX","SY","SZ","TC","TD","TG","TH","TJ","TK","TL","TM","TN","TO","TR","TT","TV","TW","TZ","UA","UG","UY","UZ","VA","VC","VE","VG","VI","VN","VU","WF","WS","YE","YT","ZA","ZM","ZW");
        country.setValue("US");
        country.setPlaceholder("Country");
        country.setLabel("Country (Optional)");
        country.setWidthFull();
        country.getStyle().set("font-family", "'Nunito', sans-serif").set("font-size", "0.9em");
        
        form.add(name, email, username, password, age, bio, country);
        
        var submit = new Button("Create Account", event -> {
            try {
                // Validation for required fields
                if (name.getValue().isEmpty()) {
                    Notification.show("Full Name is required!", 3000, Notification.Position.TOP_CENTER);
                    return;
                }
                if (email.getValue().isEmpty()) {
                    Notification.show("Email Address is required!", 3000, Notification.Position.TOP_CENTER);
                    return;
                }
                if (username.getValue().isEmpty()) {
                    Notification.show("Username is required!", 3000, Notification.Position.TOP_CENTER);
                    return;
                }
                if (password.getValue().isEmpty()) {
                    Notification.show("Password is required!", 3000, Notification.Position.TOP_CENTER);
                    return;
                }
                
                userService.createUser(name.getValue(), username.getValue(),
                        password.getValue(), email.getValue(), 
                        age.getValue(), bio.getValue(), country.getValue());
                Notification.show("Account created successfully! You can now log in.", 3000, Notification.Position.TOP_CENTER);
                UI.getCurrent().navigate(MainView.class);
            } catch (Exception e) {
                Notification.show("Error: " + e.getMessage(), 3000, Notification.Position.TOP_CENTER);
            }
        });
        submit.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        submit.setWidthFull();
        submit.getStyle()
            .set("font-family", "'Poppins', sans-serif")
            .set("font-weight", "500")
            .set("font-size", "0.95em")
            .set("letter-spacing", "0.5px")
            .set("border-radius", "12px")
            .set("margin", "0.5em 0");

        var back = new Button("Back", event -> MainView.show());
        back.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        back.setWidthFull();
        back.getStyle()
            .set("font-family", "'Poppins', sans-serif")
            .set("font-size", "0.8em")
            .set("font-weight", "500")
            .set("color", "#0435fbff")
            .set("letter-spacing", "0.5px")
            .set("text-decoration", "underline");
        
        form.add(submit, back);
        add(header, form);
    }

    public static void show() {
        UI.getCurrent().navigate(CreateAccountView.class);
    }
}