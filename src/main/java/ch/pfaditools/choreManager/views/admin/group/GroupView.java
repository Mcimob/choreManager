package ch.pfaditools.choreManager.views.admin.group;

import ch.pfaditools.choreManager.backend.service.IGroupEntityService;
import ch.pfaditools.choreManager.backend.service.ServiceResponse;
import ch.pfaditools.choreManager.layout.MainLayout;
import ch.pfaditools.choreManager.model.GroupEntity;
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

@Route(value = "group", layout = MainLayout.class)
@PermitAll
public class GroupView extends AbstractView implements HasDynamicTitle {
    private final IGroupEntityService groupService;
    private final Grid<GroupEntity> groupGrid = new Grid<>();
    private final GroupForm groupForm;

    public GroupView(IGroupEntityService groupService, SecurityService securityService) {
        super(securityService, "groupView");
        this.groupService = groupService;
        this.groupForm = new GroupForm(this, groupService);
        setupLayout();
        setupGrid();
        setupGridItems();
    }

    private void setupGrid() {
        groupGrid.addColumn(GroupEntity::getName).setHeader(getTranslation("groupView.grid.name"));
        groupGrid.addColumn(GroupEntity::getGroupCode).setHeader(getTranslation("groupView.grid.groupCode"));
        groupGrid.addColumn(GroupEntity::getCreatedAt).setHeader(getTranslation("groupView.grid.createdAt"));

        groupGrid.addSelectionListener(select -> {
            Optional<GroupEntity> potentialChore = select.getFirstSelectedItem();
            if (potentialChore.isEmpty()) {
                groupForm.setItem();
            } else {
                groupForm.setItem(potentialChore.get());
            }
        });
    }


    public void setupGridItems() {
        ServiceResponse<GroupEntity> response = groupService.getAll();
        if (!response.isOperationSuccessful()) {
            Notifier.showErrorNotification(getTranslation(response.getErrorMessage()));
        }
        groupGrid.setItems(response.getBusinessObjects());
    }

    private void setupLayout() {
        H1 title = new H1(getTranslation("groupView.title"));
        add(title, groupGrid, groupForm);
    }

    @Override
    public String getPageTitle() {
        return getTranslation("groupView.title");
    }
}
