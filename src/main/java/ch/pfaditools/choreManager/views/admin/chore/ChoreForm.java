package ch.pfaditools.choreManager.views.admin.chore;

import ch.pfaditools.choreManager.backend.service.IChoreEntityService;
import ch.pfaditools.choreManager.backend.service.ServiceResponse;
import ch.pfaditools.choreManager.model.ChoreEntity;
import ch.pfaditools.choreManager.util.Notifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;

public class ChoreForm extends FormLayout {

    private final IChoreEntityService choreService;
    private final ChoreView parent;
    private final Binder<ChoreEntity> binder = new Binder<>();
    private final TextField nameField = new TextField(getTranslation("choreView.choreForm.nameField"));
    private final TextField createdAtField = new TextField(getTranslation("choreView.choreForm.createdAtField"));
    private final Button saveButton = new Button(getTranslation("choreView.choreForm.saveButton"));
    private final Button deleteButton = new Button(getTranslation("choreView.choreForm.deleteButton"));

    private ChoreEntity chore = new ChoreEntity();

    public ChoreForm(ChoreView parent, IChoreEntityService choreService) {
        this.parent = parent;
        this.choreService = choreService;
        setupLayout();
        setupBinder();
        setupButtons();
    }

    private void setupLayout() {
        add(nameField, createdAtField, saveButton, deleteButton);
        setResponsiveSteps(
                new ResponsiveStep("0", 1),
                new ResponsiveStep("500px", 2)
        );

        this.setMaxWidth("1000px");
    }

    private void setupButtons() {
        saveButton.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
        deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR);

        saveButton.addClickListener(click -> {
            try {
                binder.writeBean(chore);
                ServiceResponse<ChoreEntity> response = choreService.save(chore);
                if (!response.isOperationSuccessful()) {
                    Notifier.showErrorNotification(getTranslation(response.getErrorMessage()));
                    return;
                }
                Notifier.showSuccessNotification(getTranslation(response.getInfoMessage()));
                parent.setupGridItems();
                setItem();
            } catch(ValidationException e) {
                Notifier.showErrorNotification(getTranslation("choreView.choreForm.validate"));
            }
        });

        deleteButton.addClickListener(click -> {
           ServiceResponse<ChoreEntity> response = choreService.delete(chore);
            if (!response.isOperationSuccessful()) {
                Notifier.showErrorNotification(getTranslation(response.getErrorMessage()));
                return;
            }
            Notifier.showSuccessNotification(getTranslation(response.getInfoMessage()));
            parent.setupGridItems();
        });
    }

    private void setupBinder() {
        binder.forField(nameField).asRequired().bind(ChoreEntity::getName, ChoreEntity::setName);
        binder.forField(createdAtField).bind(ChoreEntity::getCreatedAtString, null);
    }

    private void clearForm() {

    }

    public void setItem(ChoreEntity chore) {
        this.chore = chore;
        binder.readBean(this.chore);
    }

    public void setItem() {
        setItem(new ChoreEntity());
    }
}
