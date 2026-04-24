import sys

file_path = "/Users/MINOTEBOOKAIR/Downloads/Graft/src/main/java/com/example/graft/views/ConnectionsView.java"
with open(file_path, "r") as f:
    content = f.read()

content = content.replace("""    private Tab followingTab;
    private Tab followersTab;
    private Tab mutualTab;

    private List<SearchUserDTO> followingData = new ArrayList<>();
    private List<SearchUserDTO> followersData = new ArrayList<>();
    private List<SearchUserDTO> mutualData = new ArrayList<>();
    private List<SearchUserDTO> activeData = new ArrayList<>();""", """    private Tab followingTab;
    private Tab followersTab;

    private List<SearchUserDTO> followingData = new ArrayList<>();
    private List<SearchUserDTO> followersData = new ArrayList<>();
    private List<SearchUserDTO> activeData = new ArrayList<>();""")

content = content.replace("""        followingTab = new Tab("Following (0)");
        followersTab = new Tab("Followers (0)");
        mutualTab = new Tab("Mutual (0)");

        followingTab.getStyle()
            .set("background", "#2b3c89ff")
            .set("color", "white");

        var tabs = new Tabs(followingTab, followersTab, mutualTab);""", """        followingTab = new Tab("Following (0)");
        followersTab = new Tab("Followers (0)");

        followingTab.getStyle()
            .set("background", "#2b3c89ff")
            .set("color", "white");

        var tabs = new Tabs(followingTab, followersTab);""")

content = content.replace("""            followingTab.getStyle().remove("background");
            followingTab.getStyle().remove("color");
            followersTab.getStyle().remove("background");
            followersTab.getStyle().remove("color");
            mutualTab.getStyle().remove("background");
            mutualTab.getStyle().remove("color");
            
            if (selectedTab == followingTab) {
                activeTabKey = "following";
                activeData = followingData;
            } else if (selectedTab == followersTab) {
                activeTabKey = "followers";
                activeData = followersData;
            } else {
                activeTabKey = "mutual";
                activeData = mutualData;
            }""", """            followingTab.getStyle().remove("background");
            followingTab.getStyle().remove("color");
            followersTab.getStyle().remove("background");
            followersTab.getStyle().remove("color");
            
            if (selectedTab == followingTab) {
                activeTabKey = "following";
                activeData = followingData;
            } else if (selectedTab == followersTab) {
                activeTabKey = "followers";
                activeData = followersData;
            }""")

content = content.replace("""            followingData = repo.findFollowingUsers(currentUsername);
            followersData = repo.findFollowerUsers(currentUsername);
            mutualData = repo.findMutualUsers(currentUsername);

            followingTab.setLabel("Following (" + followingData.size() + ")");
            followersTab.setLabel("Followers (" + followersData.size() + ")");
            mutualTab.setLabel("Mutual (" + mutualData.size() + ")");""", """            followingData = repo.findFollowingUsers(currentUsername);
            followersData = repo.findFollowerUsers(currentUsername);

            followingTab.setLabel("Following (" + followingData.size() + ")");
            followersTab.setLabel("Followers (" + followersData.size() + ")");""")

content = content.replace("""            String label = switch (activeTabKey) {
                case "followers" -> "No followers yet";
                case "mutual" -> "No mutual connections yet";
                default -> "Not following anyone yet";
            };""", """            String label = switch (activeTabKey) {
                case "followers" -> "No followers yet";
                default -> "Not following anyone yet";
            };""")

content = content.replace("""    private void showUserProfileDialog(UserProfileDTO user, Long viewedUserId, Long followerCount) {
        removeAll();
        Long currentUserId = repo.findUserIdByUsername(currentUsername).orElse(null);
        Long targetId = viewedUserId != null ? viewedUserId : user.getId();
        boolean isMutual = currentUserId != null && targetId != null
            && repo.isFollowing(currentUserId, targetId)
            && repo.isFollowing(targetId, currentUserId);

        add(UserProfileSectionFactory.createProfileSection(
            repo,
            currentUsername,
            user,
            viewedUserId,
            followerCount,
            "Back to connections",
            "Mutual Friends",
            isMutual ? "1 mutual connection" : "0 mutual connections",
            () -> {
                removeAll();
                add(mainSection);
            },
            "ConnectionsView"
        ));
    }""", """    private void showUserProfileDialog(UserProfileDTO user, Long viewedUserId, Long followerCount) {
        removeAll();

        add(UserProfileSectionFactory.createProfileSection(
            repo,
            currentUsername,
            user,
            viewedUserId,
            followerCount,
            "Back to connections",
            null,
            null,
            () -> {
                removeAll();
                add(mainSection);
            },
            "ConnectionsView"
        ));
    }""")

with open(file_path, "w") as f:
    f.write(content)

file_path2 = "/Users/MINOTEBOOKAIR/Downloads/Graft/src/main/java/com/example/graft/views/UserProfileSectionFactory.java"
with open(file_path2, "r") as f:
    content2 = f.read()

content2 = content2.replace("""        var secondaryTitle = new H4(secondaryTitleText);
        secondaryTitle.getStyle()
            .set("margin", "0 0 0.5em 0")
            .set("font-family", "'Outfit', 'Poppins', sans-serif")
            .set("font-size", "1em")
            .set("color", "#2b3c89ff")
            .set("font-weight", "600");

        var secondaryBody = new Paragraph(secondaryBodyText);
        secondaryBody.getStyle()
            .set("margin", "0")
            .set("font-size", "0.9em")
            .set("color", "#555555")
            .set("font-family", "'Outfit', 'Poppins', sans-serif");
        secondaryLayout.add(secondaryTitle, secondaryBody);""", """        if (secondaryTitleText != null) {
            var secondaryTitle = new H4(secondaryTitleText);
            secondaryTitle.getStyle()
                .set("margin", "0 0 0.5em 0")
                .set("font-family", "'Outfit', 'Poppins', sans-serif")
                .set("font-size", "1em")
                .set("color", "#2b3c89ff")
                .set("font-weight", "600");

            var secondaryBody = new Paragraph(secondaryBodyText != null ? secondaryBodyText : "");
            secondaryBody.getStyle()
                .set("margin", "0")
                .set("font-size", "0.9em")
                .set("color", "#555555")
                .set("font-family", "'Outfit', 'Poppins', sans-serif");
            secondaryLayout.add(secondaryTitle, secondaryBody);
        } else {
            secondaryLayout.setVisible(false);
        }""")

with open(file_path2, "w") as f:
    f.write(content2)

print("Modifications applied.")
