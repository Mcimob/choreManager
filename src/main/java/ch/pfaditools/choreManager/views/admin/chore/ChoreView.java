package ch.pfaditools.choreManager.views.admin.chore;

import ch.pfaditools.choreManager.backend.service.IChoreEntityService;
import ch.pfaditools.choreManager.backend.service.ServiceResponse;
import ch.pfaditools.choreManager.layout.MainLayout;
import ch.pfaditools.choreManager.model.ChoreEntity;
import ch.pfaditools.choreManager.util.Notifier;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

import java.util.Optional;

@Route(value = "chore", layout = MainLayout.class)
@PermitAll
public class ChoreView extends VerticalLayout implements HasDynamicTitle {

    private final IChoreEntityService choreService;
    private final Grid<ChoreEntity> choreGrid = new Grid<>();
    private final ChoreForm choreForm;

    public ChoreView(IChoreEntityService choreService) {
        this.choreService = choreService;
        this.choreForm = new ChoreForm(this, choreService);
        setupLayout();
        setupGrid();
        setupGridItems();
    }

    private void setupGrid() {
        choreGrid.addColumn(ChoreEntity::getName).setHeader(getTranslation("choreView.grid.name"));
        choreGrid.addColumn(ChoreEntity::getCreatedAt).setHeader(getTranslation("choreView.grid.createdAt"));

        choreGrid.addSelectionListener(select -> {
            Optional<ChoreEntity> potentialChore = select.getFirstSelectedItem();
            if (potentialChore.isEmpty()) {
                choreForm.setItem();
            } else {
                choreForm.setItem(potentialChore.get());
            }
        });
    }


    public void setupGridItems() {
        ServiceResponse<ChoreEntity> response = choreService.getAll();
        if (!response.isOperationSuccessful()) {
            Notifier.showErrorNotification(getTranslation(response.getErrorMessage()));
        }
        choreGrid.setItems(response.getBusinessObjects());
    }

    private void setupLayout() {
        H1 title = new H1(getTranslation("choreView.title"));
        add(title, choreGrid, choreForm);
    }

    @Override
    public String getPageTitle() {
        return getTranslation("choreView.title");
    }
}
