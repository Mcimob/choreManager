package ch.pfaditools.choreManager.views;

import ch.pfaditools.choreManager.model.UserEntity;
import ch.pfaditools.choreManager.security.SecurityService;
import ch.pfaditools.choreManager.util.HasLogger;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class AbstractView extends VerticalLayout implements HasLogger {

    public AbstractView(SecurityService securityService, String viewName) {
        UserEntity user = securityService.getAuthenticatedUser().get();
        getLogger().info("{} entered {}", user, viewName);
    }

}
