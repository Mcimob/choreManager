package ch.pfaditools.choreManager.layout;

import ch.pfaditools.choreManager.model.UserEntity;
import ch.pfaditools.choreManager.security.SecurityService;
import ch.pfaditools.choreManager.views.admin.chore.ChoreView;
import ch.pfaditools.choreManager.views.admin.group.GroupView;
import ch.pfaditools.choreManager.views.admin.groupChoreRegistration.GroupChoreRegistrationView;
import ch.pfaditools.choreManager.views.main.MainView;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.SvgIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.atmosphere.interceptor.AtmosphereResourceStateRecovery;

public class MainLayout extends AppLayout {
    private final SecurityService securityService;

    public MainLayout(SecurityService securityService) {
        this.securityService = securityService;
        UserEntity user = securityService.getAuthenticatedUser().get();
        createHeader();
        if ("ADMIN".equals(user.getRole())) {
            createDrawer();
        }
    }

    private void createHeader() {
        UserEntity user = securityService.getAuthenticatedUser().get();


        SvgIcon profileIcon = getSvgIcon("profile-round-1342-svgrepo-com");
        SvgIcon choreIcon = getSvgIcon("clean-svgrepo-com");
        SvgIcon mealIcon = getSvgIcon("restaurant-svgrepo-com");
        SvgIcon logoutIcon = getSvgIcon("exit-svgrepo-com");

        Button profileBUtton = new Button(profileIcon);
        profileBUtton.addClickListener(click -> profileBUtton.getUI().ifPresent(ui -> ui.navigate("profileView")));
        Button choreButton = new Button(choreIcon);
        choreButton.addClickListener(click -> choreButton.getUI().ifPresent(ui -> ui.navigate("")));
        Button mealButton = new Button(mealIcon);
        mealButton.addClickListener(click -> mealButton.getUI().ifPresent(ui -> ui.navigate("mealView")));
        Button logoutButton = new Button(logoutIcon);
        logoutButton.addClickListener(click -> securityService.logout());


        var header = new HorizontalLayout();
        if ("ADMIN".endsWith(user.getRole())) {
            header.add(new DrawerToggle());
        }
        header.add(new HorizontalLayout(profileBUtton, choreButton, mealButton, logoutButton));

        header.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        header.setWidthFull();
        header.addClassNames(
                LumoUtility.Padding.Vertical.NONE,
                LumoUtility.Padding.Horizontal.MEDIUM);
        addToNavbar(true, header);
    }

    private SvgIcon getSvgIcon(String iconName) {
        return new SvgIcon("/icons/%s.svg".formatted(iconName));
    }

    private void createDrawer() {
        UserEntity user = securityService.getAuthenticatedUser().get();
        if (user.getRole().equals("ADMIN")) {
            VerticalLayout adminLayout = new VerticalLayout();
            VerticalLayout adminItems = new VerticalLayout();

            adminLayout.add(new Div(getTranslation("layout.admin")));
            adminLayout.add(adminItems);

            adminItems.add(new RouterLink(getTranslation("groupView.title"), GroupView.class));
            adminItems.add(new RouterLink(getTranslation("choreView.title"), ChoreView.class));
            adminItems.add(new RouterLink(getTranslation("groupChoreRegistrationView.title"), GroupChoreRegistrationView.class));

            addToDrawer(adminLayout);
        }
        addToDrawer(new VerticalLayout(
                new RouterLink(getTranslation("mainView.title"), MainView.class)
        ));
    }
}
