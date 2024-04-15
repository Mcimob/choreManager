package ch.pfaditools.choreManager.views.admin.groupChoreRegistration;

import ch.pfaditools.choreManager.backend.service.IChoreEntityService;
import ch.pfaditools.choreManager.backend.service.IGroupChoreRegistrationEntityService;
import ch.pfaditools.choreManager.backend.service.IGroupEntityService;
import ch.pfaditools.choreManager.backend.service.ServiceResponse;
import ch.pfaditools.choreManager.layout.MainLayout;
import ch.pfaditools.choreManager.model.GroupChoreRegistrationEntity;
import ch.pfaditools.choreManager.security.SecurityService;
import ch.pfaditools.choreManager.util.Notifier;
import ch.pfaditools.choreManager.views.AbstractView;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

import java.util.Optional;

@Route(value = "groupChoreRegistration", layout = MainLayout.class)
@PermitAll
public class GroupChoreRegistrationView extends AbstractView implements HasDynamicTitle {
    private final IGroupChoreRegistrationEntityService groupRegistrationService;
    private final Grid<GroupChoreRegistrationEntity> grid = new Grid<>();
    private final GroupRegistrationForm groupRegistrationForm;

    public GroupChoreRegistrationView(
            IGroupChoreRegistrationEntityService groupRegistrationService,
            IGroupEntityService groupService,
            IChoreEntityService choreService,
            SecurityService securityService) {
        super(securityService, "groupChoreRegistrationView");
        this.groupRegistrationService = groupRegistrationService;
        this.groupRegistrationForm = new GroupRegistrationForm(this, groupRegistrationService, groupService, choreService);
        setupLayout();
        setupGrid();
        setupGridItems();
        setupForm();
    }

    private void setupForm() {
    }

    private void setupGrid() {
        grid.addColumn(reg -> reg.getChore().getName()).setHeader(getTranslation("groupChoreRegistrationView.grid.choreName"));
        grid.addColumn(reg -> reg.getGroup().getName()).setHeader(getTranslation("groupChoreRegistrationView.grid.groupName"));
        grid.addColumn(GroupChoreRegistrationEntity::getStartDate).setHeader(getTranslation("groupChoreRegistrationView.grid.startDate"));
        grid.addColumn(GroupChoreRegistrationEntity::getEndDate).setHeader(getTranslation("groupChoreRegistrationView.grid.endDate"));
        grid.addColumn(GroupChoreRegistrationEntity::getCreatedAt).setHeader(getTranslation("groupChoreRegistrationView.grid.createdAt"));

        grid.addSelectionListener(select -> {
            Optional<GroupChoreRegistrationEntity> potentialChore = select.getFirstSelectedItem();
            if (potentialChore.isEmpty()) {
                groupRegistrationForm.setItem();
            } else {
                groupRegistrationForm.setItem(potentialChore.get());
            }
        });
    }


    public void setupGridItems() {
        ServiceResponse<GroupChoreRegistrationEntity> response = groupRegistrationService.getAll();
        if (!response.isOperationSuccessful()) {
            Notifier.showErrorNotification(getTranslation(response.getErrorMessage()));
        }
        grid.setItems(response.getBusinessObjects());
    }

    private void setupLayout() {
        H1 title = new H1(getTranslation("groupChoreRegistrationView.title"));
        add(title, grid, groupRegistrationForm);
    }

    @Override
    public String getPageTitle() {
        return getTranslation("groupChoreRegistrationView.title");
    }
}
