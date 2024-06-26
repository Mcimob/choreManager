package ch.pfaditools.choreManager.views.profile;

import ch.pfaditools.choreManager.layout.MainLayout;
import ch.pfaditools.choreManager.model.UserEntity;
import ch.pfaditools.choreManager.security.SecurityService;
import ch.pfaditools.choreManager.security.service.UserEntityDetailsService;
import ch.pfaditools.choreManager.util.Notifier;
import ch.pfaditools.choreManager.views.AbstractView;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.dom.Style;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

@Route(value = "profileView", layout = MainLayout.class)
@PermitAll
public class ProfileView extends AbstractView implements HasDynamicTitle {

    private final SecurityService securityService;
    private final UserEntityDetailsService userService;
    private final TextField userNameField = new TextField(getTranslation("profileView.userName"));
    private final TextField displayNameField = new TextField(getTranslation("profileView.displayName"));
    private final Button displayNameButton = new Button(getTranslation("profileView.displayName.button"));

    public ProfileView(SecurityService securityService, UserEntityDetailsService userService) {
        super(securityService, "profileView");
        this.securityService = securityService;
        this.userService = userService;
        setupLayout();
        setupInputs();
        setupStatic();
    }

    private void setupLayout() {
        this.getStyle().setAlignItems(Style.AlignItems.CENTER);
        Div container = new Div();
        container.addClassName("profile_view_container");

        container.add(userNameField, new Div(), displayNameField, displayNameButton);

        H1 title = new H1(getTranslation("profileView.title"));

        add(title, container);
    }

    private void setupInputs() {
        displayNameButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        displayNameButton.addClickListener(click -> {
            UserEntity user = securityService.getAuthenticatedUser().get();
            if (displayNameField.getValue().isEmpty()) {
                Notifier.showErrorNotification(getTranslation("profileView.displayName.error.empty"));
                return;
            }
            user.setDisplayName(displayNameField.getValue());
            userService.saveUserWithoutPassword(user);
            Notifier.showSuccessNotification(getTranslation("profileView.displayName.message.save"));
            getLogger().info("{} successfully changed their displayName to  {}", user, displayNameField.getValue());
        });
    }

    private void setupStatic() {
        UserEntity user = securityService.getAuthenticatedUser().get();
        userNameField.setValue(user.getUsername());
        userNameField.setEnabled(false);
        userNameField.setHelperText(getTranslation("profileView.userName.helperText"));

        displayNameField.setValue(user.getDisplayName() == null ? "" : user.getDisplayName());
    }

    @Override
    public String getPageTitle() {
        return getTranslation("profileView.title");
    }
}
