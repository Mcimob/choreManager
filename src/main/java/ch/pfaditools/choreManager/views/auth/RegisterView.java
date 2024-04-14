package ch.pfaditools.choreManager.views.auth;

import ch.pfaditools.choreManager.backend.service.impl.GroupEntityService;
import ch.pfaditools.choreManager.security.service.UserEntityDetailsService;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@Route("register")
@AnonymousAllowed
public class RegisterView extends VerticalLayout implements HasUrlParameter<String>, HasDynamicTitle {

    private RegistrationForm registrationForm;

    public RegisterView(GroupEntityService groupService, UserEntityDetailsService userService) {
        this.registrationForm = new RegistrationForm(groupService, userService);
        setupLayout();
    }

    public void setupLayout() {
        setAlignItems(FlexComponent.Alignment.CENTER);
        setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        setHeightFull();
        registrationForm.setMaxWidth("300px");
        add(new Div(registrationForm));
    }

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter String parameter) {
        if (parameter != null) {
            TextField groupField = registrationForm.getGroupField();
            groupField.setValue(parameter);
            groupField.setEnabled(false);
        }
    }


    @Override
    public String getPageTitle() {
        return getTranslation("registerView.title");
    }
}
