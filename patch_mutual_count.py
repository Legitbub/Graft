import os

file_path = "/Users/MINOTEBOOKAIR/Downloads/Graft/src/main/java/com/example/graft/views/UserProfileSectionFactory.java"
with open(file_path, "r") as f:
    content = f.read()

old_block = """        if (!isOwnProfile && currentUserId != null && targetId != null) {
            var mutualTitle = new H4("Mutual Friends");
            mutualTitle.getStyle()
                .set("margin", "0 0 0.5em 0")
                .set("font-family", "'Outfit', 'Poppins', sans-serif")
                .set("font-size", "1em")
                .set("color", "#2b3c89ff")
                .set("font-weight", "600");
            mutualFriendsLayout.add(mutualTitle);

            System.out.println("DEBUG: Finding mutual friends for currentUserId=" + currentUserId + " and targetId=" + targetId);
            List<SearchUserDTO> mutuals = repo.findMutualFriends(currentUserId, targetId);
            System.out.println("DEBUG: Found " + (mutuals != null ? mutuals.size() : 0) + " mutual friends.");
            if (mutuals != null && !mutuals.isEmpty()) {
                for (SearchUserDTO mf : mutuals) {"""

new_block = """        if (!isOwnProfile && currentUserId != null && targetId != null) {
            var mutualTitle = new H4("Mutual Friends");
            mutualTitle.getStyle()
                .set("margin", "0 0 0.2em 0")
                .set("font-family", "'Outfit', 'Poppins', sans-serif")
                .set("font-size", "1em")
                .set("color", "#2b3c89ff")
                .set("font-weight", "600");
            mutualFriendsLayout.add(mutualTitle);

            System.out.println("DEBUG: Finding mutual friends for currentUserId=" + currentUserId + " and targetId=" + targetId);
            List<SearchUserDTO> mutuals = repo.findMutualFriends(currentUserId, targetId);
            int mutualCount = mutuals != null ? mutuals.size() : 0;
            System.out.println("DEBUG: Found " + mutualCount + " mutual friends.");

            var countLabel = new Paragraph(mutualCount == 1 ? "1 mutual friend" : mutualCount + " mutual friends");
            countLabel.getStyle()
                .set("margin", "0 0 1em 0")
                .set("font-size", "0.9em")
                .set("color", "#555555");
            mutualFriendsLayout.add(countLabel);

            if (mutualCount > 0) {
                for (SearchUserDTO mf : mutuals) {"""

content = content.replace(old_block, new_block)

old_else_block = """                    mutualFriendsLayout.add(mfCard);
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
        }"""

new_else_block = """                    mutualFriendsLayout.add(mfCard);
                }
            }
        } else {
            mutualFriendsLayout.setVisible(false);
        }"""

content = content.replace(old_else_block, new_else_block)

with open(file_path, "w") as f:
    f.write(content)

print("Patch applied")
