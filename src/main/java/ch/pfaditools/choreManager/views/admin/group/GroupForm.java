package ch.pfaditools.choreManager.views.admin.group;

import ch.pfaditools.choreManager.backend.service.IGroupEntityService;
import ch.pfaditools.choreManager.backend.service.ServiceResponse;
import ch.pfaditools.choreManager.model.GroupEntity;
import ch.pfaditools.choreManager.util.Notifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;

public class GroupForm extends FormLayout {
    private final IGroupEntityService choreService;
    private final GroupView parent;
    private final Binder<GroupEntity> binder = new Binder<>();
    private final TextField nameField = new TextField(getTranslation("groupView.groupForm.nameField"));
    private final TextField codeField = new TextField(getTranslation("groupView.groupForm.codeField"));
    private final TextField createdAtField = new TextField(getTranslation("groupView.groupForm.createdAtField"));
    private final Button saveButton = new Button(getTranslation("groupView.groupForm.saveButton"));
    private final Button deleteButton = new Button(getTranslation("groupView.groupForm.deleteButton"));

    private GroupEntity group = new GroupEntity();

    public GroupForm(GroupView parent, IGroupEntityService choreService) {
        this.parent = parent;
        this.choreService = choreService;
        setupLayout();
        setupBinder();
        setupButtons();
    }

    private void setupLayout() {
        add(nameField, codeField, createdAtField, saveButton, deleteButton);
        setResponsiveSteps(
                new ResponsiveStep("0", 1),
                new ResponsiveStep("500px", 2)
        );

        this.setMaxWidth("1000px");
        this.setColspan(createdAtField, 2);
    }

    private void setupButtons() {
        saveButton.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
        deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR);

        saveButton.addClickListener(click -> {
            try {
                binder.writeBean(group);
                ServiceResponse<GroupEntity> response = choreService.save(group);
                if (!response.isOperationSuccessful()) {
                    Notifier.showErrorNotification(getTranslation(response.getErrorMessage()));
                    return;
                }
                Notifier.showSuccessNotification(getTranslation(response.getInfoMessage()));
                parent.setupGridItems();
                setItem();
            } catch(ValidationException e) {
                Notifier.showErrorNotification(getTranslation("groupView.groupForm.validate"));
            }
        });

        deleteButton.addClickListener(click -> {
            ServiceResponse<GroupEntity> response = choreService.delete(group);
            if (!response.isOperationSuccessful()) {
                Notifier.showErrorNotification(getTranslation(response.getErrorMessage()));
                return;
            }
            Notifier.showSuccessNotification(getTranslation(response.getInfoMessage()));
            parent.setupGridItems();
        });
    }

    private void setupBinder() {
        binder.forField(nameField).asRequired().bind(GroupEntity::getName, GroupEntity::setName);
        binder.forField(codeField).bind(GroupEntity::getGroupCode, null);
        binder.forField(createdAtField).bind(GroupEntity::getCreatedAtString, null);
    }

    public void setItem(GroupEntity chore) {
        this.group = chore;
        binder.readBean(this.group);
    }

    public void setItem() {
        setItem(new GroupEntity());
    }
}
