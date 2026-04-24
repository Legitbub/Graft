package com.example.graft.views;

import com.example.graft.RecommendationUserDTO;
import com.example.graft.User;
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
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

import java.util.List;
import java.util.Optional;

@Route("recommendations")
@PermitAll
public class RecommendationsView extends VerticalLayout {
    private final UserRepository repo;
    private final String currentUsername;
    private VerticalLayout cardsContainer;
    private HorizontalLayout limitLayout;
    private int currentLimit = 10;
    private int currentPage = 1;
    private final int itemsPerPage = 20;
    private List<RecommendationUserDTO> allData;
    private VerticalLayout mainSection;
    private HorizontalLayout paginationLayout;

    public RecommendationsView(UserRepository repo, String currentUsername) {
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

        var title = new H2("Friend Recommendations");
        title.getStyle()
            .set("font-family", "'Outfit', 'Poppins', sans-serif")
            .set("margin", "0 0 1.5em 0")
            .set("font-size", "1.8em")
            .set("font-weight", "700")
            .set("color", "#2b3c89ff");

        section.add(title);

        limitLayout = new HorizontalLayout();
        limitLayout.setSpacing(true);
        limitLayout.setJustifyContentMode(JustifyContentMode.END);
        limitLayout.setWidthFull();
        limitLayout.getStyle()
            .set("margin-bottom", "1.5em")
            .set("align-items", "right")
            .set("max-width", "100%");

        var limitText = new Span("View Limit: ");
        limitText.getStyle()
            .set("font-family", "'Poppins', sans-serif")
            .set("font-weight", "900")
            .set("font-size", "1em")
            .set("color", "#2b3c89ff")
            .set("padding", "0.5em");

        Button limit10Btn = createLimitButton("10", 10, limitLayout);
        Button limit20Btn = createLimitButton("20", 20, limitLayout);
        Button limit50Btn = createLimitButton("50", 50, limitLayout);
        Button limit100Btn = createLimitButton("100", 100, limitLayout);
        Button limit500Btn = createLimitButton("500", 500, limitLayout);
        Button limit1000Btn = createLimitButton("1000", 1000, limitLayout);

        limit10Btn.getStyle().set("background", "#2b3c89ff").set("color", "white");

        limitLayout.add(limitText, limit10Btn, limit20Btn, limit50Btn, limit100Btn, limit500Btn, limit1000Btn);
        section.add(limitLayout);

        paginationLayout = new HorizontalLayout();
        paginationLayout.setSpacing(true);
        paginationLayout.setJustifyContentMode(JustifyContentMode.END);
        paginationLayout.setWidthFull();
        paginationLayout.getStyle()
            .set("margin-bottom", "1.5em");
        section.add(paginationLayout);

        cardsContainer = new VerticalLayout();
        cardsContainer.setSpacing(true);
        cardsContainer.setPadding(false);
        cardsContainer.setWidthFull();

        loadData(10);
        section.add(cardsContainer);

        add(section);
    }

    private Button createLimitButton(String label, int limit, HorizontalLayout parent) {
        Button btn = new Button(label + " Users", event -> {
            currentLimit = limit;
            loadData(limit);

            parent.getChildren()
                .filter(c -> c instanceof Button)
                .forEach(c -> {
                    ((Button) c).getStyle().remove("background");
                    ((Button) c).getStyle().remove("color");
                });
            ((Button) event.getSource()).getStyle()
                .set("background", "#2b3c89ff")
                .set("color", "white");
        });
        btn.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        btn.getStyle()
            .set("font-family", "'Poppins', sans-serif")
            .set("font-weight", "500")
            .set("border-radius", "8px");
        return btn;
    }

    private void loadData(int limit) {
        try {
            Optional<Long> currentUserId = repo.findUserIdByUsername(currentUsername);
            if (currentUserId.isEmpty()) {
                var noResultLayout = new HorizontalLayout();
                noResultLayout.setJustifyContentMode(JustifyContentMode.CENTER);
                noResultLayout.setWidthFull();

                var noResults = new Paragraph("No recommendations found for current user.");
                noResults.getStyle()
                    .set("font-family", "'Nunito', sans-serif")
                    .set("color", "#666666")
                    .set("text-align", "center");
                noResultLayout.add(noResults);
                cardsContainer.add(noResultLayout);
                limitLayout.setVisible(false);
                return;
            }

            allData = repo.findFriendRecommendationsByUserId(currentUserId.get(), limit);
            currentPage = 1;
            renderPage();
        } catch (Exception e) {
            var errorMsg = new Paragraph("Error loading friend recommendations: " + e.getMessage());
            errorMsg.getStyle()
                .set("font-family", "'Nunito', sans-serif")
                .set("color", "#d32f2f")
                .set("text-align", "center");
            cardsContainer.removeAll();
            cardsContainer.add(errorMsg);
            paginationLayout.removeAll();
        }
    }

    private void renderPage() {
        cardsContainer.removeAll();
        if (allData == null || allData.isEmpty()) {
            limitLayout.setVisible(false);
            paginationLayout.removeAll();
            var noResultLayout = new HorizontalLayout();
            noResultLayout.setJustifyContentMode(JustifyContentMode.CENTER);
            noResultLayout.setWidthFull();

            var noResults = new Paragraph("No recommendations found for current user.");
            noResults.getStyle()
                .set("font-family", "'Nunito', sans-serif")
                .set("color", "#666666")
                .set("text-align", "center");
            noResultLayout.add(noResults);
            cardsContainer.add(noResultLayout);
            return;
        }

        limitLayout.setVisible(true);

        int totalItems = allData.size();
        int totalPages = (int) Math.ceil((double) totalItems / itemsPerPage);

        if (currentPage < 1) currentPage = 1;
        if (currentPage > totalPages) currentPage = totalPages;

        int startIndex = (currentPage - 1) * itemsPerPage;
        int endIndex = Math.min(startIndex + itemsPerPage, totalItems);

        for (int i = startIndex; i < endIndex; i++) {
            RecommendationUserDTO user = allData.get(i);
            HorizontalLayout card = createUserCard(user, i + 1);
            cardsContainer.add(card);
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

    private HorizontalLayout createUserCard(RecommendationUserDTO user, int rank) {
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

        var userName = new H4(user.getName());
        userName.getStyle()
            .set("margin", "0 0 0.3em 0")
            .set("font-family", "'Outfit', 'Poppins', sans-serif")
            .set("font-size", "1.1em")
            .set("color", "#1a1a1a");

        var userHandle = new Paragraph("@" + user.getUsername());
        userHandle.getStyle()
            .set("margin", "0")
            .set("font-size", "0.9em")
            .set("color", "#666666");

        infoContainer.add(userName, userHandle);

        var scoreContainer = new VerticalLayout();
        scoreContainer.setPadding(false);
        scoreContainer.setSpacing(false);
        scoreContainer.setAlignItems(Alignment.CENTER);

        var scoreValue = new H4(String.valueOf(user.getMutualConnectionScore() != null ? user.getMutualConnectionScore() : 0));
        scoreValue.getStyle()
            .set("margin", "0")
            .set("font-family", "'Outfit', 'Poppins', sans-serif")
            .set("font-size", "1.3em")
            .set("color", "#2b3c89ff")
            .set("font-weight", "700");

        var scoreLabel = new Paragraph("mutual score");
        scoreLabel.getStyle()
            .set("margin", "0")
            .set("font-size", "0.85em")
            .set("color", "#999999");

        scoreContainer.add(scoreValue, scoreLabel);

        var viewProfileBtn = new Button("View Profile", event -> {;
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
                    if (resolvedUserId == null) {
                        resolvedUserId = repo.findUserIdByUsername(user.getUsername()).orElse(null);
                    }
                    System.out.println("  Found user in database: " + userOptional.get().getUsername());
                    Long effectiveUserId = resolvedUserId != null ? resolvedUserId : userOptional.get().getId();
                    Long followerCount = effectiveUserId != null ? repo.countFollowers(effectiveUserId) : 0L;
                    showUserProfileDialog(userOptional.get(), effectiveUserId, followerCount, user.getMutualConnectionScore());
                } else {
                    System.out.println("  User not found for username/userId");
                }
            } catch (Exception e) {
                System.err.println("DEBUG: Error in RecommendationsView View Profile button: " + e.getMessage());
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

        card.add(rankBadge, userImage, infoContainer, scoreContainer, viewProfileBtn);

        card.getElement().addEventListener("mouseenter", event ->
            card.getStyle().set("box-shadow", "0 8px 24px rgba(33, 150, 243, 0.25)")
        );
        card.getElement().addEventListener("mouseleave", event ->
            card.getStyle().set("box-shadow", "none")
        );

        return card;
    }

    private void showUserProfileDialog(UserProfileDTO user, Long viewedUserId, Long followerCount, Long mutualConnectionScore) {
        removeAll();
        add(UserProfileSectionFactory.createProfileSection(
            repo,
            currentUsername,
            user,
            viewedUserId,
            followerCount,
            "Back to recommendations",
            "Mutual Connection Score",
            String.valueOf(mutualConnectionScore != null ? mutualConnectionScore : 0),
            () -> {
                removeAll();
                add(mainSection);
            },
            "RecommendationsView"
        ));
    }
}
