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
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.theme.lumo.LumoUtility;

public class MainLayout extends AppLayout {
    private final SecurityService securityService;

    public MainLayout(SecurityService securityService) {
        this.securityService = securityService;
        createHeader();
        createDrawer();
    }

    private void createHeader() {
        H1 logo = new H1("pageTitle");
        logo.addClassNames(
                LumoUtility.FontSize.LARGE,
                LumoUtility.Margin.MEDIUM);

        String u = securityService.getAuthenticatedUser().get().getUsername();
        Button logout = new Button("Log out " + u, e -> securityService.logout());

        var header = new HorizontalLayout(new DrawerToggle(), logout);

        header.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        header.expand(logo);
        header.setWidthFull();
        header.addClassNames(
                LumoUtility.Padding.Vertical.NONE,
                LumoUtility.Padding.Horizontal.MEDIUM);
        addToNavbar(header);

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
