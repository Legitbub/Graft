import sys

def modify_file(file_path):
    with open(file_path, 'r') as f:
        content = f.read()

    # 1. Add mainSection field
    content = content.replace(
        "private Consumer<User> onViewProfile;",
        "private Consumer<User> onViewProfile;\n    private VerticalLayout mainSection;"
    )

    # 2. Assign mainSection in constructor
    content = content.replace(
        "var section = new VerticalLayout();\n        section.setAlignItems(Alignment.STRETCH);",
        "var section = new VerticalLayout();\n        this.mainSection = section;\n        section.setAlignItems(Alignment.STRETCH);"
    )

    # 3. Replace start of showUserProfileDialog
    old_start = """    private void showUserProfileDialog(User user) {
        Dialog dialog = new Dialog();
        dialog.setWidth("600px");
        dialog.setCloseOnEsc(true);
        dialog.setCloseOnOutsideClick(true);
        dialog.getElement().getThemeList().add("no-padding");

        var section = new VerticalLayout();
        section.setAlignItems(Alignment.CENTER);
        section.setJustifyContentMode(JustifyContentMode.START);
        section.setSpacing(false);
        section.setWidthFull();
        section.getStyle()
            .set("padding", "1.5em")
            .set("background", "rgba(255, 255, 255, 0.95)")
            .set("border-radius", "20px")
            .set("box-shadow", "0 20px 60px rgba(0, 0, 0, 0.3)")
            .set("backdrop-filter", "blur(10px)")
            .set("overflow-y", "auto")
            .set("max-height", "100%")
            .set("max-width", "100%")
            .set("position", "relative")
            .set("border", "1px solid #000");

        var closeIcon = new Icon(VaadinIcon.CLOSE);
        closeIcon.getStyle()
            .set("position", "absolute")
            .set("top", "15px")
            .set("right", "15px")
            .set("cursor", "pointer")
            .set("color", "#666")
            .set("font-size", "1.5em");
        closeIcon.addClickListener(e -> dialog.close());
        section.add(closeIcon);"""

    new_start = """    private void showUserProfileDialog(User user) {
        removeAll();

        var section = new VerticalLayout();
        section.setAlignItems(Alignment.CENTER);
        section.setJustifyContentMode(JustifyContentMode.START);
        section.setSpacing(false);
        section.setWidthFull();
        section.getStyle()
            .set("padding", "3em 2.5em 2.5em 2.5em")
            .set("background", "rgba(255, 255, 255, 0.95)")
            .set("border-radius", "20px")
            .set("box-shadow", "0 20px 60px rgba(0, 0, 0, 0.3)")
            .set("backdrop-filter", "blur(10px)")
            .set("max-width", "1000px")
            .set("position", "relative");

        var backBtn = new Button("Back to popular users", new Icon(VaadinIcon.ARROW_LEFT), e -> {
            removeAll();
            add(mainSection);
        });
        backBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        backBtn.getStyle()
            .set("position", "absolute")
            .set("top", "15px")
            .set("left", "15px")
            .set("font-family", "'Poppins', sans-serif")
            .set("cursor", "pointer");
        
        section.add(backBtn);"""
    
    # Try exact replace first, but if there's no match (due to slight differences in whitespace/theming), try a more robust approach
    if old_start in content:
        content = content.replace(old_start, new_start)
    else:
        # Fallback regex or substring matching
        import re
        content = re.sub(
            r'    private void showUserProfileDialog\(User user\) \{.*?section\.add\(closeIcon\);',
            new_start,
            content,
            flags=re.DOTALL
        )

    # 4. Replace end of showUserProfileDialog
    old_end = """        section.add(profileImage, viewModeContainer);
        dialog.add(section);
        dialog.open();
    }"""
    
    new_end = """        section.add(profileImage, viewModeContainer);
        add(section);
    }"""
    content = content.replace(old_end, new_end)

    with open(file_path, 'w') as f:
        f.write(content)

if __name__ == "__main__":
    modify_file(sys.argv[1])
