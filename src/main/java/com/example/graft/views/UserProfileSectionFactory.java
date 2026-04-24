package com.example.graft.views;

import com.example.graft.UserProfileDTO;
import com.example.graft.UserRepository;
import java.util.List;
import com.example.graft.SearchUserDTO;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

final class UserProfileSectionFactory {
    private UserProfileSectionFactory() {
    }

    static VerticalLayout createProfileSection(
        UserRepository repo,
        String currentUsername,
        UserProfileDTO user,
        Long viewedUserId,
        Long followerCount,
        String backLabel,
        String secondaryTitleText,
        String secondaryBodyText,
        Runnable onBack,
        String debugPrefix
    ) {
        var section = new VerticalLayout();
        section.setAlignItems(VerticalLayout.Alignment.CENTER);
        section.setJustifyContentMode(VerticalLayout.JustifyContentMode.START);
        section.setSpacing(false);
        section.setWidthFull();
        section.getStyle()
            .set("padding", "3em 2.5em 2.5em 2.5em")
            .set("background", "rgba(255, 255, 255, 0.95)")
            .set("border-radius", "20px")
            .set("box-shadow", "0 20px 60px rgba(0, 0, 0, 0.3)")
            .set("backdrop-filter", "blur(10px)")
            .set("max-width", "1000px")
            .set("position", "relative")
            .set("font-family", "'Outfit', 'Poppins', sans-serif");

        var backBtn = new Button(backLabel, new Icon(VaadinIcon.ARROW_LEFT), e -> onBack.run());
        backBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        backBtn.getStyle()
            .set("position", "absolute")
            .set("top", "15px")
            .set("left", "15px")
            .set("font-family", "'Outfit', 'Poppins', sans-serif")
            .set("color", "#2b3c89ff")
            .set("cursor", "pointer");
        section.add(backBtn);

        var profileImage = new Image("images/user.png", "Profile Picture");
        profileImage.setWidth("120px");
        profileImage.setHeight("120px");
        profileImage.getStyle()
            .set("border-radius", "50%")
            .set("object-fit", "cover")
            .set("border", "4px solid #fff")
            .set("box-shadow", "0 4px 10px rgba(0, 0, 0, 0.1)")
            .set("margin-bottom", "0.8em")
            .set("flex-shrink", "0");

        var nameDisplay = new H2(user.getName() != null ? user.getName() : "");
        nameDisplay.getStyle()
            .set("font-family", "'Outfit', 'Poppins', sans-serif")
            .set("margin", "0.5em 0 0.2em 0")
            .set("font-weight", "700")
            .set("color", "#1a1a1a");

        var usernameDisplay = new Paragraph("@" + (user.getUsername() != null ? user.getUsername() : ""));
        usernameDisplay.getStyle()
            .set("margin", "0 0 0.5em 0")
            .set("font-size", "0.9em")
            .set("color", "#666666");

        var bioDisplay = new Paragraph(user.getBio() != null ? user.getBio() : "");
        bioDisplay.getStyle()
            .set("margin", "0 0 1.2em 0")
            .set("font-size", "0.95em")
            .set("color", "#555555")
            .set("line-height", "1.5")
            .set("font-family", "'Outfit', 'Poppins', sans-serif");

        var infoLayout = new VerticalLayout();
        infoLayout.setWidthFull();
        infoLayout.setSpacing(false);
        infoLayout.setPadding(false);
        infoLayout.getStyle()
            .set("border", "1px solid #e2e8f0")
            .set("border-radius", "8px")
            .set("overflow", "hidden")
            .set("max-width", "70%");

        var emailSpan = new Span(user.getEmail() != null ? user.getEmail() : "N/A");
        var ageSpan = new Span(user.getAge() != null ? String.valueOf(user.getAge()) : "N/A");
        var countrySpan = new Span(user.getCountry() != null ? user.getCountry() : "N/A");

        var emailRow = createDetailRow("Email", emailSpan);
        var ageRow = createDetailRow("Age", ageSpan);
        var countryRow = createDetailRow("Country", countrySpan);
        countryRow.getStyle().set("border-bottom", "none");
        infoLayout.add(emailRow, ageRow, countryRow);

        Long currentUserId = repo.findUserIdByUsername(currentUsername).orElse(null);
        Long resolvedTargetId = viewedUserId != null ? viewedUserId : user.getId();
        if (resolvedTargetId == null && user.getUsername() != null) {
            resolvedTargetId = repo.findUserIdByUsername(user.getUsername()).orElse(null);
        }
        final Long targetId = resolvedTargetId;
        Long effectiveFollowerCount = followerCount != null ? followerCount : (targetId != null ? repo.countFollowers(targetId) : 0L);

        var followersLayout = new VerticalLayout();
        followersLayout.setAlignItems(VerticalLayout.Alignment.CENTER);
        followersLayout.setSpacing(true);
        followersLayout.setPadding(true);
        followersLayout.setWidthFull();
        followersLayout.getStyle()
            .set("background", "#f8fafc")
            .set("border-radius", "8px")
            .set("border", "1px solid #e2e8f0")
            .set("margin-top", "1.2em")
            .set("max-width", "70%")
            .set("font-family", "'Outfit', 'Poppins', sans-serif");

        var followersCount = new H4(String.valueOf(effectiveFollowerCount != null ? effectiveFollowerCount : 0));
        followersCount.getStyle()
            .set("margin", "0")
            .set("font-family", "'Outfit', 'Poppins', sans-serif")
            .set("font-size", "1.3em")
            .set("color", "#2b3c89ff")
            .set("font-weight", "700");

        var followersLabel = new Paragraph("followers");
        followersLabel.getStyle()
            .set("margin", "0")
            .set("font-size", "0.85em")
            .set("color", "#999999");
        followersLayout.add(followersCount, followersLabel);

        var secondaryLayout = new VerticalLayout();
        secondaryLayout.setAlignItems(VerticalLayout.Alignment.CENTER);
        secondaryLayout.setSpacing(true);
        secondaryLayout.setPadding(true);
        secondaryLayout.setWidthFull();
        secondaryLayout.getStyle()
            .set("background", "#f8fafc")
            .set("border-radius", "8px")
            .set("border", "1px solid #e2e8f0")
            .set("margin-top", "0.8em")
            .set("max-width", "70%")
            .set("font-family", "'Outfit', 'Poppins', sans-serif");

        if (secondaryTitleText != null) {
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
        }

        boolean isOwnProfile = currentUserId != null && currentUserId.equals(targetId);

        final VerticalLayout[] viewModeContainerHolder = new VerticalLayout[1];

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
                .set("margin", "0 0 0.2em 0")
                .set("font-family", "'Outfit', 'Poppins', sans-serif")
                .set("font-size", "1em")
                .set("color", "#2b3c89ff")
                .set("font-weight", "600");
            mutualFriendsLayout.add(mutualTitle);

            
            List<SearchUserDTO> mutuals = repo.findMutualFriends(currentUserId, targetId);
            int mutualCount = mutuals != null ? mutuals.size() : 0;

            var countLabel = new Paragraph(mutualCount == 1 ? "1 mutual friend" : mutualCount + " mutual friends");
            countLabel.getStyle()
                .set("margin", "0 0 1em 0")
                .set("font-size", "0.9em")
                .set("color", "#555555");
            mutualFriendsLayout.add(countLabel);

            if (mutualCount > 0) {
                for (SearchUserDTO mf : mutuals) {
                    var mfCard = new HorizontalLayout();
                    mfCard.setAlignItems(VerticalLayout.Alignment.CENTER);
                    mfCard.setSpacing(true);
                    mfCard.setPadding(true);
                    mfCard.setWidthFull();
                    mfCard.getStyle()
                        .set("background", "linear-gradient(135deg, rgba(33, 150, 243, 0.08), rgba(76, 175, 80, 0.08))")
                        .set("border", "1px solid rgba(33, 150, 243, 0.2)")
                        .set("border-radius", "12px")
                        .set("padding", "1em 1.5em")
                        .set("transition", "all 0.3s ease")
                        .set("cursor", "pointer");

                    var mfImage = new Image("images/user.png", "User avatar");
                    mfImage.setWidth("60px");
                    mfImage.setHeight("60px");
                    mfImage.getStyle()
                        .set("border-radius", "50%")
                        .set("object-fit", "cover")
                        .set("border", "2px solid #2b3c89ff");

                    var mfInfo = new VerticalLayout();
                    mfInfo.setPadding(false);
                    mfInfo.setSpacing(false);
                    mfInfo.setFlexGrow(1);

                    var mfName = new H4(mf.getName() != null ? mf.getName() : "");
                    mfName.getStyle()
                        .set("margin", "0 0 0.3em 0")
                        .set("font-family", "'Outfit', 'Poppins', sans-serif")
                        .set("font-size", "1.1em")
                        .set("color", "#1a1a1a");

                    var mfHandle = new Paragraph("@" + (mf.getUsername() != null ? mf.getUsername() : ""));
                    mfHandle.getStyle()
                        .set("margin", "0")
                        .set("font-size", "0.9em")
                        .set("color", "#666666");

                    mfInfo.add(mfName, mfHandle);

                    var viewProfileBtn = new Button("View Profile", event -> {
                        try {
                            java.util.Optional<UserProfileDTO> userOptional = java.util.Optional.empty();
                            Long resolvedUserId = mf.getId();

                            if (resolvedUserId != null) {
                                userOptional = repo.findUserProfileByUserId(resolvedUserId);
                            } else {
                                userOptional = repo.findUserProfileByUsername(mf.getUsername());
                                if (userOptional.isPresent()) {
                                    resolvedUserId = userOptional.get().getId();
                                }
                            }

                            if (userOptional.isPresent()) {
                                if (resolvedUserId == null) {
                                    resolvedUserId = repo.findUserIdByUsername(mf.getUsername()).orElse(null);
                                }
                                Long effectiveUserId = resolvedUserId != null ? resolvedUserId : userOptional.get().getId();
                                Long fCount = effectiveUserId != null ? repo.countFollowers(effectiveUserId) : 0L;
                                
                                final VerticalLayout[] newSectionHolder = new VerticalLayout[1];
                                newSectionHolder[0] = createProfileSection(
                                    repo, currentUsername, userOptional.get(), effectiveUserId, fCount,
                                    "Back", null, null, () -> {
                                        if (newSectionHolder[0] != null) {
                                            newSectionHolder[0].getParent().ifPresent(parent -> {
                                                if (parent instanceof com.vaadin.flow.component.HasComponents) {
                                                    ((com.vaadin.flow.component.HasComponents) parent).remove(newSectionHolder[0]);
                                                    ((com.vaadin.flow.component.HasComponents) parent).add(section);
                                                }
                                            });
                                        }
                                    }, debugPrefix
                                );
                                section.getParent().ifPresent(parent -> {
                                    if (parent instanceof com.vaadin.flow.component.HasComponents) {
                                        ((com.vaadin.flow.component.HasComponents) parent).remove(section);
                                        ((com.vaadin.flow.component.HasComponents) parent).add(newSectionHolder[0]);
                                    }
                                });
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                    viewProfileBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SMALL);
                    viewProfileBtn.getStyle()
                        .set("font-family", "'Poppins', sans-serif")
                        .set("font-weight", "500")
                        .set("border-radius", "8px")
                        .set("flex-shrink", "0")
                        .set("background", "#2b3c89ff");

                    mfCard.add(mfImage, mfInfo, viewProfileBtn);

                    mfCard.getElement().addEventListener("mouseenter", event ->
                        mfCard.getStyle().set("box-shadow", "0 8px 24px rgba(33, 150, 243, 0.25)")
                    );
                    mfCard.getElement().addEventListener("mouseleave", event ->
                        mfCard.getStyle().set("box-shadow", "none")
                    );

                    mutualFriendsLayout.add(mfCard);
                }
            }
        } else {
            mutualFriendsLayout.setVisible(false);
        }


        final long[] currentFollowerCount = {effectiveFollowerCount != null ? effectiveFollowerCount : 0L};

        var followBtn = new Button("+ Follow");
        followBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        followBtn.getStyle()
            .set("margin-bottom", "1.2em")
            .set("border-radius", "20px")
            .set("font-weight", "600")
            .set("padding", "0.5em 1em")
            .set("background", "#2b3c89ff")
            .set("font-family", "'Outfit', 'Poppins', sans-serif");

        var followStatus = new Paragraph("You followed this user");
        followStatus.getStyle()
            .set("margin", "0 0 1.2em 0")
            .set("font-size", "0.9em")
            .set("color", "#2b3c89ff")
            .set("font-weight", "600")
            .set("font-family", "'Outfit', 'Poppins', sans-serif");

        var ownProfileStatus = new Paragraph("You are viewing your profile.");
        ownProfileStatus.getStyle()
            .set("margin", "0 0 1.2em 0")
            .set("font-size", "0.9em")
            .set("color", "#2b3c89ff")
            .set("font-weight", "600")
            .set("font-family", "'Outfit', 'Poppins', sans-serif");

        Runnable updateFollowState = () -> {
            boolean isFollowed = currentUserId != null
                && targetId != null
                && !currentUserId.equals(targetId)
                && repo.isFollowing(currentUserId, targetId);

            followBtn.setText(isFollowed ? "Unfollow" : "+ Follow");
            followStatus.setVisible(isFollowed);
            followBtn.setEnabled(currentUserId != null && targetId != null && !currentUserId.equals(targetId));
        };

        if (!isOwnProfile) {
            updateFollowState.run();
            followBtn.addClickListener(event -> {
                if (currentUserId == null || targetId == null || currentUserId.equals(targetId)) {
                    return;
                }

                boolean isFollowed = repo.isFollowing(currentUserId, targetId);
                if (isFollowed) {
                    Long unfollowResult = repo.unfollowUser(currentUserId, targetId);
                    if (currentFollowerCount[0] > 0) {
                        currentFollowerCount[0]--;
                    }
                } else {
                    Long followResult = repo.followUser(currentUserId, targetId);
                    currentFollowerCount[0]++;
                }

                boolean isFollowedAfter = repo.isFollowing(currentUserId, targetId);
                Long actualFollowerCount = repo.countFollowers(targetId);
                followersCount.setText(String.valueOf(currentFollowerCount[0]));
                updateFollowState.run();
            });
        }

        var viewModeContainer = isOwnProfile
            ? new VerticalLayout(nameDisplay, usernameDisplay, ownProfileStatus, bioDisplay, infoLayout, followersLayout, secondaryLayout, mutualFriendsLayout)
            : new VerticalLayout(nameDisplay, usernameDisplay, followBtn, followStatus, bioDisplay, infoLayout, followersLayout, secondaryLayout, mutualFriendsLayout);
        viewModeContainerHolder[0] = viewModeContainer;
        viewModeContainer.setAlignItems(VerticalLayout.Alignment.CENTER);
        viewModeContainer.setPadding(false);
        viewModeContainer.setSpacing(false);
        viewModeContainer.setWidthFull();

        section.add(profileImage, viewModeContainer);
        return section;
    }

    private static HorizontalLayout createDetailRow(String label, Span valueSpan) {
        var layout = new HorizontalLayout();
        layout.setWidthFull();
        layout.setJustifyContentMode(HorizontalLayout.JustifyContentMode.BETWEEN);
        layout.setAlignItems(HorizontalLayout.Alignment.CENTER);
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
}
