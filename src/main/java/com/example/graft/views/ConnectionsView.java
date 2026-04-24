package com.example.graft.views;

import com.example.graft.SearchUserDTO;
import com.example.graft.UserProfileDTO;
import com.example.graft.UserRepository;
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
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import jakarta.annotation.security.PermitAll;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@PermitAll
public class ConnectionsView extends VerticalLayout {
    private final UserRepository repo;
    private final String currentUsername;
    private final int itemsPerPage = 20;

    private VerticalLayout mainSection;
    private VerticalLayout cardsContainer;
    private HorizontalLayout paginationLayout;

    private Tab followingTab;
    private Tab followersTab;

    private List<SearchUserDTO> followingData = new ArrayList<>();
    private List<SearchUserDTO> followersData = new ArrayList<>();
    private List<SearchUserDTO> activeData = new ArrayList<>();
    private String activeTabKey = "following";
    private int currentPage = 1;

    public ConnectionsView(UserRepository repo, String currentUsername) {
        this.repo = repo;
        this.currentUsername = currentUsername;

        setAlignItems(Alignment.CENTER);
        setSpacing(true);
        setPadding(true);
        setWidthFull();
        getStyle().set("background", "transparent");

        var section = new VerticalLayout();
        this.mainSection = section;
        section.setAlignItems(Alignment.STRETCH);
        section.setWidth("90%");
        section.setMaxWidth("1000px");
        section.getStyle()
            .set("padding", "2.5em")
            .set("background", "rgba(255, 255, 255, 0.95)")
            .set("border-radius", "20px")
            .set("box-shadow", "0 20px 60px rgba(0, 0, 0, 0.3)")
            .set("backdrop-filter", "blur(10px)")
            .set("font-family", "'Outfit', 'Poppins', sans-serif");

        var title = new H2("Your Connections");
        title.getStyle()
            .set("font-family", "'Outfit', 'Poppins', sans-serif")
            .set("margin", "0 0 1.5em 0")
            .set("font-size", "1.8em")
            .set("font-weight", "700")
            .set("color", "#2b3c89ff");

        followingTab = new Tab("Following (0)");
        followersTab = new Tab("Followers (0)");

        followingTab.getStyle()
            .set("background", "#2b3c89ff")
            .set("color", "white");

        var tabs = new Tabs(followingTab, followersTab);
        tabs.setWidthFull();
        //tabs.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        tabs.getStyle()
            .set("font-family", "'Poppins', sans-serif")
            .set("font-weight", "500")
            .set("border-radius", "8px")
            .set("margin-bottom", "1.5em");

        tabs.addSelectedChangeListener(event -> {
            Tab selectedTab = event.getSelectedTab();
            
            followingTab.getStyle().remove("background");
            followingTab.getStyle().remove("color");
            followersTab.getStyle().remove("background");
            followersTab.getStyle().remove("color");
            
            if (selectedTab == followingTab) {
                activeTabKey = "following";
                activeData = followingData;
            } else if (selectedTab == followersTab) {
                activeTabKey = "followers";
                activeData = followersData;
            }
            selectedTab.getStyle()
                .set("background", "#2b3c89ff")
                .set("color", "white");
            currentPage = 1;
            renderPage();
        });

        paginationLayout = new HorizontalLayout();
        paginationLayout.setSpacing(true);
        paginationLayout.setJustifyContentMode(JustifyContentMode.END);
        paginationLayout.setWidthFull();
        paginationLayout.getStyle().set("margin-bottom", "1.5em");

        cardsContainer = new VerticalLayout();
        cardsContainer.setSpacing(true);
        cardsContainer.setPadding(false);
        cardsContainer.setWidthFull();

        section.add(title, tabs, paginationLayout, cardsContainer);
        add(section);

        loadData();
    }

    private void loadData() {
        try {
            followingData = repo.findFollowingUsers(currentUsername);
            followersData = repo.findFollowerUsers(currentUsername);

            followingTab.setLabel("Following (" + followingData.size() + ")");
            followersTab.setLabel("Followers (" + followersData.size() + ")");

            activeTabKey = "following";
            activeData = followingData;
            currentPage = 1;
            renderPage();
        } catch (Exception e) {
            cardsContainer.removeAll();
            paginationLayout.removeAll();
            var errorMsg = new Paragraph("Error loading connections: " + e.getMessage());
            errorMsg.getStyle()
                .set("font-family", "'Nunito', sans-serif")
                .set("color", "#d32f2f");
            cardsContainer.add(errorMsg);
        }
    }

    private void renderPage() {
        cardsContainer.removeAll();
        if (activeData == null || activeData.isEmpty()) {
            paginationLayout.removeAll();
            String label = switch (activeTabKey) {
                case "followers" -> "No followers yet";
                default -> "Not following anyone yet";
            };
            var emptyMessage = new Paragraph(label);
            emptyMessage.getStyle()
                .set("font-family", "'Nunito', sans-serif")
                .set("color", "#666666")
                .set("text-align", "center")
                .set("margin", "2em 0");
            cardsContainer.add(emptyMessage);
            return;
        }

        int totalItems = activeData.size();
        int totalPages = (int) Math.ceil((double) totalItems / itemsPerPage);

        if (currentPage < 1) currentPage = 1;
        if (currentPage > totalPages) currentPage = totalPages;

        int startIndex = (currentPage - 1) * itemsPerPage;
        int endIndex = Math.min(startIndex + itemsPerPage, totalItems);

        for (int i = startIndex; i < endIndex; i++) {
            SearchUserDTO user = activeData.get(i);
            cardsContainer.add(createUserCard(user, i + 1));
        }

        updatePagination(totalPages);
    }

    private void updatePagination(int totalPages) {
        paginationLayout.removeAll();
        if (totalPages <= 1) return;

        var paginationText = new Span("Page " + currentPage + " of " + totalPages);
        paginationText.getStyle()
            .set("font-family", "'Poppins', sans-serif")
            .set("font-weight", "900")
            .set("font-size", "1em")
            .set("color", "#2b3c89ff")
            .set("padding", "0.5em");
        paginationLayout.add(paginationText);

        Button prevBtn = new Button("Prev", e -> {
            if (currentPage > 1) {
                currentPage--;
                renderPage();
            }
        });
        prevBtn.setEnabled(currentPage > 1);
        prevBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        paginationLayout.add(prevBtn);

        int startPage = Math.max(1, currentPage - 1);
        int endPage = Math.min(totalPages, currentPage + 1);

        if (currentPage == 1 && totalPages >= 3) {
            endPage = 3;
        } else if (currentPage == totalPages && totalPages >= 3) {
            startPage = totalPages - 2;
        }

        for (int i = startPage; i <= endPage; i++) {
            final int pageToLoad = i;
            Button pageBtn = new Button(String.valueOf(pageToLoad), e -> {
                currentPage = pageToLoad;
                renderPage();
            });
            if (pageToLoad == currentPage) {
                pageBtn.getStyle()
                    .set("background", "#2b3c89ff")
                    .set("color", "white");
            } else {
                pageBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            }
            paginationLayout.add(pageBtn);
        }

        Button nextBtn = new Button("Next", e -> {
            if (currentPage < totalPages) {
                currentPage++;
                renderPage();
            }
        });
        nextBtn.setEnabled(currentPage < totalPages);
        nextBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        paginationLayout.add(nextBtn);
    }

    private HorizontalLayout createUserCard(SearchUserDTO user, int rank) {
        var card = new HorizontalLayout();
        card.setAlignItems(Alignment.CENTER);
        card.setSpacing(true);
        card.setPadding(true);
        card.setWidthFull();
        card.getStyle()
            .set("background", "linear-gradient(135deg, rgba(33, 150, 243, 0.08), rgba(76, 175, 80, 0.08))")
            .set("border", "1px solid rgba(33, 150, 243, 0.2)")
            .set("border-radius", "12px")
            .set("padding", "1em 1.5em")
            .set("transition", "all 0.3s ease")
            .set("cursor", "pointer");

        var rankBadge = new Paragraph(rank + ".");
        rankBadge.getStyle()
            .set("font-weight", "700")
            .set("font-size", "1.2em")
            .set("color", "#2b3c89ff")
            .set("min-width", "50px")
            .set("text-align", "center")
            .set("margin", "0");

        var userImage = new Image("images/user.png", "User avatar");
        userImage.setWidth("60px");
        userImage.setHeight("60px");
        userImage.getStyle()
            .set("border-radius", "50%")
            .set("object-fit", "cover")
            .set("border", "2px solid #2b3c89ff");

        var infoContainer = new VerticalLayout();
        infoContainer.setPadding(false);
        infoContainer.setSpacing(false);
        infoContainer.setFlexGrow(1);

        var userName = new H4(user.getName() != null ? user.getName() : "");
        userName.getStyle()
            .set("margin", "0 0 0.3em 0")
            .set("font-family", "'Outfit', 'Poppins', sans-serif")
            .set("font-size", "1.1em")
            .set("color", "#1a1a1a");

        var userHandle = new Paragraph("@" + (user.getUsername() != null ? user.getUsername() : ""));
        userHandle.getStyle()
            .set("margin", "0")
            .set("font-size", "0.9em")
            .set("color", "#666666");

        infoContainer.add(userName, userHandle);

        var viewProfileBtn = new Button("View Profile", event -> {
            try {
                Optional<UserProfileDTO> userOptional;
                Long resolvedUserId = user.getId();

                if (resolvedUserId != null) {
                    userOptional = repo.findUserProfileByUserId(resolvedUserId);
                } else {
                    userOptional = repo.findUserProfileByUsername(user.getUsername());
                    if (userOptional.isPresent()) {
                        resolvedUserId = userOptional.get().getId();
                    }
                }

                if (userOptional.isPresent()) {
                    Long followerCount = resolvedUserId != null ? repo.countFollowers(resolvedUserId) : 0L;
                    showUserProfileDialog(userOptional.get(), resolvedUserId, followerCount);
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

        card.add(rankBadge, userImage, infoContainer, viewProfileBtn);

        card.getElement().addEventListener("mouseenter", event ->
            card.getStyle().set("box-shadow", "0 8px 24px rgba(33, 150, 243, 0.25)")
        );
        card.getElement().addEventListener("mouseleave", event ->
            card.getStyle().set("box-shadow", "none")
        );

        return card;
    }

    private void showUserProfileDialog(UserProfileDTO user, Long viewedUserId, Long followerCount) {
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
    }
     
}
