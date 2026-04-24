import os

file_path = "/Users/MINOTEBOOKAIR/Downloads/Graft/src/main/java/com/example/graft/views/UserProfileSectionFactory.java"
with open(file_path, "r") as f:
    content = f.read()

# 1. Add imports if needed
if "import java.util.List;" not in content:
    content = content.replace("import com.example.graft.UserRepository;", "import com.example.graft.UserRepository;\nimport java.util.List;\nimport com.example.graft.SearchUserDTO;")

# 2. Add mutualFriendsLayout block after secondaryLayout.setVisible(false);
insert_point = "} else {\n            secondaryLayout.setVisible(false);\n        }"
mutual_layout_code = """} else {
            secondaryLayout.setVisible(false);
        }

        boolean isOwnProfile = currentUserId != null && currentUserId.equals(targetId);

        var mutualFriendsLayout = new VerticalLayout();
        mutualFriendsLayout.setAlignItems(VerticalLayout.Alignment.CENTER);
        mutualFriendsLayout.setSpacing(true);
        mutualFriendsLayout.setPadding(true);
        mutualFriendsLayout.setWidthFull();
        mutualFriendsLayout.getStyle()
            .set("background", "#f8fafc")
            .set("border-radius", "8px")
            .set("border", "1px solid #e2e8f0")
            .set("margin-top", "0.8em")
            .set("max-width", "70%")
            .set("font-family", "'Outfit', 'Poppins', sans-serif");

        if (!isOwnProfile && currentUserId != null && targetId != null) {
            var mutualTitle = new H4("Mutual Friends");
            mutualTitle.getStyle()
                .set("margin", "0 0 0.5em 0")
                .set("font-family", "'Outfit', 'Poppins', sans-serif")
                .set("font-size", "1em")
                .set("color", "#2b3c89ff")
                .set("font-weight", "600");
            mutualFriendsLayout.add(mutualTitle);

            List<SearchUserDTO> mutuals = repo.findMutualFriends(currentUserId, targetId);
            if (mutuals != null && !mutuals.isEmpty()) {
                for (SearchUserDTO mf : mutuals) {
                    var mfCard = new HorizontalLayout();
                    mfCard.setAlignItems(VerticalLayout.Alignment.CENTER);
                    mfCard.setSpacing(true);
                    mfCard.setWidthFull();
                    mfCard.getStyle()
                        .set("background", "white")
                        .set("padding", "0.5em")
                        .set("border-radius", "6px")
                        .set("box-shadow", "0 1px 3px rgba(0,0,0,0.1)");

                    var mfImage = new Image("images/user.png", "User avatar");
                    mfImage.setWidth("35px");
                    mfImage.setHeight("35px");
                    mfImage.getStyle().set("border-radius", "50%");

                    var mfName = new Paragraph(mf.getName() != null ? mf.getName() : mf.getUsername());
                    mfName.getStyle()
                        .set("margin", "0")
                        .set("font-weight", "500")
                        .set("font-size", "0.9em")
                        .set("color", "#333");

                    mfCard.add(mfImage, mfName);
                    mutualFriendsLayout.add(mfCard);
                }
            } else {
                var noMutual = new Paragraph("0 mutual friends");
                noMutual.getStyle()
                    .set("margin", "0")
                    .set("font-size", "0.9em")
                    .set("color", "#555555");
                mutualFriendsLayout.add(noMutual);
            }
        } else {
            mutualFriendsLayout.setVisible(false);
        }
"""
content = content.replace(insert_point, mutual_layout_code, 1)

# 3. We also need to remove boolean isOwnProfile = ... that appears later since we moved it up.
content = content.replace("""        boolean isOwnProfile = currentUserId != null && currentUserId.equals(targetId);
        final long[] currentFollowerCount = {effectiveFollowerCount != null ? effectiveFollowerCount : 0L};""", """        final long[] currentFollowerCount = {effectiveFollowerCount != null ? effectiveFollowerCount : 0L};""")

# 4. Add mutualFriendsLayout to viewModeContainer
old_view_container_1 = "new VerticalLayout(nameDisplay, usernameDisplay, ownProfileStatus, bioDisplay, infoLayout, followersLayout, secondaryLayout)"
new_view_container_1 = "new VerticalLayout(nameDisplay, usernameDisplay, ownProfileStatus, bioDisplay, infoLayout, followersLayout, secondaryLayout, mutualFriendsLayout)"
content = content.replace(old_view_container_1, new_view_container_1)

old_view_container_2 = "new VerticalLayout(nameDisplay, usernameDisplay, followBtn, followStatus, bioDisplay, infoLayout, followersLayout, secondaryLayout)"
new_view_container_2 = "new VerticalLayout(nameDisplay, usernameDisplay, followBtn, followStatus, bioDisplay, infoLayout, followersLayout, secondaryLayout, mutualFriendsLayout)"
content = content.replace(old_view_container_2, new_view_container_2)

with open(file_path, "w") as f:
    f.write(content)

# Fix SearchView and PopularUsersView
for v_file in ["SearchView.java", "PopularUsersView.java"]:
    p = f"/Users/MINOTEBOOKAIR/Downloads/Graft/src/main/java/com/example/graft/views/{v_file}"
    with open(p, "r") as f:
        c = f.read()
    c = c.replace('"Mutual Friends",\n            "0 mutual friends",', 'null,\n            null,')
    c = c.replace('"Mutual Friends",\n            isMutual ? "1 mutual connection" : "0 mutual connections",', 'null,\n            null,')
    with open(p, "w") as f:
        f.write(c)

print("Patch applied.")
