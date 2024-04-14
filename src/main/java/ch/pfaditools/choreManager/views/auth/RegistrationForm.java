package ch.pfaditools.choreManager.views.auth;

import ch.pfaditools.choreManager.backend.service.ServiceResponse;
import ch.pfaditools.choreManager.backend.service.impl.GroupEntityService;
import ch.pfaditools.choreManager.security.service.UserEntityDetailsService;
import ch.pfaditools.choreManager.exception.UsernameAlreadyExistsException;
import ch.pfaditools.choreManager.model.GroupEntity;
import ch.pfaditools.choreManager.model.UserEntity;
import ch.pfaditools.choreManager.util.Notifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;

import java.util.regex.Pattern;

public class RegistrationForm extends FormLayout {
    private static final String PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,20}$";

    private final GroupEntityService groupService;
    private final UserEntityDetailsService userService;
    private final Binder<UserEntity> binder = new Binder<>();

    private final H2 title = new H2(getTranslation("registerView.title"));
    private final TextField usernameField = new TextField(getTranslation("registerView.username"));
    private final PasswordField passwordField = new PasswordField(getTranslation("registerView.password"));
    private final TextField groupField = new TextField(getTranslation("registerView.group"));

    private final Button registerButton = new Button(getTranslation("registerView.registerButton"));
    private final Anchor loginLink = new Anchor("/login", getTranslation("registerView.loginLink"));

    public RegistrationForm(GroupEntityService groupService, UserEntityDetailsService userService) {
        this.groupService = groupService;
        this.userService = userService;
        setupLayout();
        setupBinder();
        setupButton();
    }

    private void setupLayout() {
        add(title, usernameField, passwordField, groupField, registerButton, loginLink);
        passwordField.setHelperText(getTranslation("registerView.password.helper"));
        this.setResponsiveSteps(new ResponsiveStep("0", 1));
    }

    private void setupBinder() {
        binder.forField(usernameField).asRequired().bind(UserEntity::getUsername, UserEntity::setUsername);
        binder.forField(passwordField).asRequired().withValidator(pass -> Pattern.compile(PASSWORD_REGEX).matcher(pass).matches(), "").bind(UserEntity::getPassword, UserEntity::setPassword);
    }

    private void setupButton() {
        registerButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        registerButton.addClickListener(click -> {
            try {
                UserEntity newUser = new UserEntity();
                binder.writeBean(newUser);
                ServiceResponse<GroupEntity> groupResponse = groupService.getByCode(groupField.getValue());
                if (!groupResponse.isOperationSuccessful()) {
                    Notifier.showErrorNotification(getTranslation(groupResponse.getErrorMessage()));
                    return;
                }
                GroupEntity group = groupResponse.getBusinessObjects().getFirst();
                newUser.addGroup(group);
                userService.registerUser(newUser);
                Notifier.showSuccessNotification(getTranslation("registerView.message.registered"));
                registerButton.getUI().ifPresent(ui -> ui.navigate("login"));
            } catch(ValidationException e) {
                Notifier.showWarningNotification(getTranslation("registerView.error.weakPassword"));
            } catch (UsernameAlreadyExistsException e) {
                Notifier.showWarningNotification(getTranslation("registerView.error.usernameExists"));
            }
        });
    }



    public TextField getGroupField() {
        return groupField;
    }

}
